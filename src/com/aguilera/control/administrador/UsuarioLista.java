package com.aguilera.control.administrador;

import java.util.HashMap;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.aguilera.modelo.Usuario;
import com.aguilera.modelo.UsuarioPrivilegio;
import com.aguilera.modeloDAO.UsuarioDAO;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"rawtypes", "unchecked"})
public class UsuarioLista {

	private final static String LISTA_USUARIOS = "usuarios";
	private final static String VENTANA_USUARIO_EDITA = "/administrador/usuarioEdita.zul";
	
	public final static String COMANDO_BUSCAR = "UsuarioLista.buscar";
	public final static String PARAMETRO_USUARIO = "usuario";

	@Getter @Setter private List<Usuario> usuarios;
	@Getter @Setter private String textoBuscar;
	
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception{
		Selectors.wireComponents(view, this, false);
		textoBuscar = "";
		buscar();
	}
	
	@Command
	@GlobalCommand(COMANDO_BUSCAR)
	@NotifyChange({LISTA_USUARIOS})
	public void buscar() throws Exception{
		if (usuarios != null) {
			usuarios = null;
		}
		
		usuarios = usuarioDAO.buscarPorTexto(textoBuscar);
	}
	
	@Command
	public void nuevo() {
		Window ventanaCargar = (Window) Executions.createComponents(VENTANA_USUARIO_EDITA, null, null);
		ventanaCargar.doModal();
	}

	@Command
	public void editar(@BindingParam(PARAMETRO_USUARIO) Usuario usuario) {
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(PARAMETRO_USUARIO, usuario);
		Window ventanaCargar = (Window) Executions.createComponents(VENTANA_USUARIO_EDITA, null, parametros);
		ventanaCargar.doModal(); 		
	}

	@Command
	public void eliminar(@BindingParam(PARAMETRO_USUARIO) Usuario usuario) {
		
		Messagebox.show("Desea eliminar este usuario?", "Confirmación",
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL},
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							usuario.setEstado("X");
							if (usuario.getPersona() != null) {
								usuario.getPersona().setEstado("X");
							}
							for(UsuarioPrivilegio objeto : usuario.getUsuarioPrivilegios()) {
								objeto.setEstado("X");
							}
							
							usuarioDAO.saveOrUpdate(usuario);
				        	BindUtils.postGlobalCommand(null, null, COMANDO_BUSCAR, null);
				        }
					}
				});	
	}
}
