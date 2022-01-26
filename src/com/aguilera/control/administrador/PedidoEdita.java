package com.aguilera.control.administrador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

import com.aguilera.control.diseniador.DisenioLista;
import com.aguilera.modelo.Archivo;
import com.aguilera.modelo.Cita;
import com.aguilera.modelo.Disenio;
import com.aguilera.modelo.DisenioArchivo;
import com.aguilera.modelo.Pedido;
import com.aguilera.modelo.Persona;
import com.aguilera.modelo.Privilegio;
import com.aguilera.modelo.Usuario;
import com.aguilera.modelo.UsuarioPrivilegio;
import com.aguilera.modeloDAO.ArchivoDAO;
import com.aguilera.modeloDAO.CitaDAO;
import com.aguilera.modeloDAO.DisenioDAO;
import com.aguilera.modeloDAO.PedidoDAO;
import com.aguilera.modeloDAO.PersonaDAO;
import com.aguilera.modeloDAO.PrivilegioDAO;
import com.aguilera.modeloDAO.UsuarioDAO;
import com.aguilera.util.Constantes;
import com.aguilera.util.FileUtil;
import com.aguilera.util.SecurityUtil;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"rawtypes", "unused", "unchecked"})
public class PedidoEdita {
	
	private final static String OBJETO_PEDIDO = "pedido";
	private final static String LISTA_CLIENTES = "clientes";

	private PedidoDAO pedidoDAO = new PedidoDAO();
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	
	@Getter @Setter private Pedido pedido;
	private Pedido pedidoLista;
	@Getter @Setter private List<Usuario> clientes;
	@Getter @Setter private String usuario;
	
	@Init
	public void init(@ExecutionArgParam(CitaLista.PARAMETRO_CITA) Cita cita,
					 @ExecutionArgParam("pedido") Pedido pedido) {
		pedidoLista = pedido;
		
		if (pedido == null) {
			this.pedido = new Pedido();
		}else{
			
			if (pedido.getId() != 0) {
				this.pedido = pedidoDAO.getEntityManager().find(Pedido.class, pedido.getId());
			}else {
				this.pedido = pedido;
			}
		}
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
	
	@Command
	@NotifyChange({LISTA_CLIENTES, OBJETO_PEDIDO})
	public void buscarCliente(@BindingParam("textoBuscar") String textoBuscar) {
		if (clientes !=null) {
			clientes = null;
		}
		
		clientes = usuarioDAO.buscarPorTextoRol(textoBuscar, Constantes.CLIENTE);
	}
	
	@Command
	public void aceptar(@ContextParam(ContextType.VIEW) Component view){
				
		if(pedido.getObservacion() == null) {
			Clients.showNotification("Debe escribir la observación!", "error", null, "top_center", 0);
			return;
		}
		
		Messagebox.show("Desea guardar este pedido?", "Confirmación", 
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL}, 
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							pedido.setEstadoPedido(Constantes.ESTADO_PEDIDO_CREADO);
							pedidoDAO.saveOrUpdate(pedido);
							pedidoLista = pedido;
				        	BindUtils.postGlobalCommand(null, null, PedidoLista.COMANDO_BUSCAR, null);
				        	salir(view);
				        }else if (event.getName().equals(Events.ON_CANCEL)) {
				        	BindUtils.postGlobalCommand(null, null, PedidoLista.COMANDO_BUSCAR, null);
				        }
					}
				});
	}	

	@Command
	public void salir(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		pedido = null;
		BindUtils.postGlobalCommand(null, null, PedidoLista.COMANDO_BUSCAR, null);
		view.detach();
	}
}