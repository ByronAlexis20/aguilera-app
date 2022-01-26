package com.aguilera.control.diseniador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.aguilera.control.administrador.CitaLista;
import com.aguilera.modelo.Archivo;
import com.aguilera.modelo.Cita;
import com.aguilera.modelo.Disenio;
import com.aguilera.modelo.DisenioArchivo;
import com.aguilera.modelo.Pedido;
import com.aguilera.modelo.PedidoDisenio;
import com.aguilera.modeloDAO.ArchivoDAO;
import com.aguilera.modeloDAO.CitaDAO;
import com.aguilera.modeloDAO.DisenioDAO;
import com.aguilera.modeloDAO.PedidoDAO;
import com.aguilera.util.Constantes;
import com.aguilera.util.FileUtil;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"rawtypes", "unused", "unchecked"})
public class PedidoEdita {
	
	private final static String COMANDO_ACTUALIZAR = "PedidoLista.actualizarFormulario";
	private final static String OBJETO_PEDIDO = "pedido";
	private final static String LISTA_DISENIOS_AGREGADOS = "diseniosAgregados";
	private final static String VENTANA_DISENIO_NUEVO = "/diseniador/disenioEdita.zul";
	private final static String VENTANA_DISENIO_EDITA = "/diseniador/disenioEdita.zul";
	private final static String VENTANA_DISENIO_BUSCAR = "/diseniador/disenioBuscar.zul";
	
	public final static String PARAMETRO_DISENIO = "disenio";
	public final static String PARAMETRO_COMANDO = "PedidoLista.actualizarFormulario";
	public final static String PARAMETRO_LISTA = "lista";

	private PedidoDAO pedidoDAO = new PedidoDAO();
	private DisenioDAO disenioDAO = new DisenioDAO();
	
	@Getter @Setter private Pedido pedido;
	private Pedido pedidoLista;
	@Getter @Setter private List<Disenio> diseniosAgregados = new ArrayList<Disenio>();
	
	@Init
	public void init(@ExecutionArgParam(PedidoLista.PARAMETRO_PEDIDO) Pedido pedido) {
		pedidoLista = pedido;
		if (pedido == null) {
			this.pedido = new Pedido();
		}else{
			
			if (pedido.getId() != 0) {
				this.pedido = pedidoDAO.getEntityManager().find(Pedido.class, pedido.getId());
			}else {
				this.pedido = pedido;
			}
			
			if (this.pedido.getPedidoDisenios() != null) {
				for(PedidoDisenio objeto : this.pedido.getPedidoDisenios()) {
					objeto.getDisenio().setCritica(objeto.getCritica());
					diseniosAgregados.add(objeto.getDisenio());
				}
			}
		}
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
	
	private void actualizarDisenios() {
		if (pedido.getPedidoDisenios() == null) {
			pedido.setPedidoDisenios(new ArrayList<PedidoDisenio>());
		}
		for(PedidoDisenio objeto : pedido.getPedidoDisenios()) {
			int cont = 0;
			for (Disenio objeto2 : diseniosAgregados) {
				if (objeto.getDisenio().getId() == objeto2.getId()) {
					cont++;
				}
			}
			if(cont == 0) {
				objeto.setEstado("X");
			}
		}
		for(Disenio objeto2 : diseniosAgregados) {
			int cont = 0;
			for (PedidoDisenio objeto : pedido.getPedidoDisenios()) {
				if (objeto.getDisenio().getId() == objeto2.getId()) {
					cont++;
				}
			}
			if (cont == 0) {
				PedidoDisenio pedidoDisenio = new PedidoDisenio();
				pedidoDisenio.setPedido(pedido);
				pedidoDisenio.setDisenio(objeto2);
				pedido.getPedidoDisenios().add(pedidoDisenio);
			}
		}
	}
	
	@Command
	@GlobalCommand(COMANDO_ACTUALIZAR)
	@NotifyChange({LISTA_DISENIOS_AGREGADOS})
	public void actualizarFormulario(){
	}
	
	@Command
    public void nuevoDisenio(){
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(PARAMETRO_COMANDO, COMANDO_ACTUALIZAR);
		parametros.put(PARAMETRO_LISTA, diseniosAgregados);
		Window ventanaCargar = (Window) Executions.createComponents(VENTANA_DISENIO_NUEVO, null, parametros);
		ventanaCargar.doModal();
	 }
	
	@Command
    public void buscarDisenio(){
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(PARAMETRO_COMANDO, COMANDO_ACTUALIZAR);
		parametros.put(PARAMETRO_LISTA, diseniosAgregados);
		Window ventanaCargar = (Window) Executions.createComponents(VENTANA_DISENIO_BUSCAR, null, parametros);
		ventanaCargar.doModal();
	 }
	
	@Command
	@NotifyChange({LISTA_DISENIOS_AGREGADOS, OBJETO_PEDIDO})
	public void eliminar(@BindingParam("disenio") Disenio disenio) {
		diseniosAgregados.remove(disenio);
	}
	
	@Command
	public void editar(@BindingParam("disenio") Disenio disenio) {
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(PARAMETRO_DISENIO, disenio);
		parametros.put(PARAMETRO_COMANDO, COMANDO_ACTUALIZAR);
		parametros.put(PARAMETRO_LISTA, diseniosAgregados);
		Window ventanaCargar = (Window) Executions.createComponents(VENTANA_DISENIO_EDITA, null, parametros);
		ventanaCargar.doModal();
	}
	
	@Command
	public void aceptar(@ContextParam(ContextType.VIEW) Component view){
				
		actualizarDisenios();
		
		Messagebox.show("Desea guardar este pedido?", "Confirmación", 
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL}, 
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							for(PedidoDisenio objeto : pedido.getPedidoDisenios()) {
								objeto.setEstadoDisenio(Constantes.ESTADO_PEDIDO_D_CREADO);
								objeto.setCritica(null);
							}
							pedido.setEstadoPedido(Constantes.ESTADO_PEDIDO_DISENIO);
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
