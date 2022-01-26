package com.aguilera.control.administrador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
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

import com.aguilera.modelo.Persona;
import com.aguilera.modelo.Privilegio;
import com.aguilera.modelo.Usuario;
import com.aguilera.modelo.UsuarioPrivilegio;
import com.aguilera.modeloDAO.PersonaDAO;
import com.aguilera.modeloDAO.PrivilegioDAO;
import com.aguilera.modeloDAO.UsuarioDAO;
import com.aguilera.util.SecurityUtil;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"rawtypes", "unused", "unchecked"})
public class UsuarioEdita {

	private final static String OBJETO_USUARIO = "usuario";
	private final static String OBJETO_PRIVILEGIO_AGREGAR = "privilegioAgregar";
	private final static String OBJETO_PRIVILEGIO_QUITAR = "privilegioQuitar";
	private final static String LISTA_PRIVILEGIOS_DISPONIBLES = "privilegiosDisponibles";
	private final static String LISTA_PRIVILEGIOS_ASIGNADOS = "privilegiosAsignados";

	private PersonaDAO personaDAO = new PersonaDAO();
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	private PrivilegioDAO privilegioDAO = new PrivilegioDAO();
	
	@Getter @Setter private Usuario usuario;
	private Usuario usuarioLista;
	@Getter @Setter private Privilegio privilegioAgregar;
	@Getter @Setter private Privilegio privilegioQuitar;
	@Getter @Setter private List<Privilegio> privilegiosDisponibles;
	@Getter @Setter private List<Privilegio> privilegiosAsignados = new ArrayList<Privilegio>();
	@Getter @Setter private String clave = "";
	@Getter @Setter private String confirmarClave = "";
	
	@Init
	public void init(@ExecutionArgParam(UsuarioLista.PARAMETRO_USUARIO) Usuario usuario) {
		usuarioLista = usuario;
		if (usuario == null) {
			this.usuario = new Usuario();
			this.usuario.setPersona(new Persona());
		}else{
			
			if (usuario.getId() != 0) {
				this.usuario = usuarioDAO.getEntityManager().find(Usuario.class, usuario.getId());
			}else {
				this.usuario = usuario;
			}
			
			if (this.usuario.getPersona() == null) {
				this.usuario.setPersona(new Persona());
			}
			
			if (this.usuario.getUsuarioPrivilegios() != null) {
				for(UsuarioPrivilegio objeto : this.usuario.getUsuarioPrivilegios()) {
					privilegiosAsignados.add(objeto.getPrivilegio());
				}
			}
		}
		cargarPrivilegiosDisponibles();
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
	
	private void cargarPrivilegiosDisponibles() {
		if (usuario.getId() == 0) {
			privilegiosDisponibles = privilegioDAO.findAll();
		}else {
			privilegiosDisponibles = privilegioDAO.buscarNoSeleccionadosPorUsuario(usuario.getId());
		}
	}
	
	private void actualizarListaPrivilegios() {
		if (usuario.getUsuarioPrivilegios() == null) {
			usuario.setUsuarioPrivilegios(new ArrayList<UsuarioPrivilegio>());
		}
		for(UsuarioPrivilegio objeto : usuario.getUsuarioPrivilegios()) {
			int cont = 0;
			for (Privilegio objeto2 : privilegiosAsignados) {
				if (objeto.getPrivilegio().getId() == objeto2.getId()) {
					cont++;
				}
			}
			if(cont == 0) {
				objeto.setEstado("X");
			}
		}
		for(Privilegio objeto2 : privilegiosAsignados) {
			int cont = 0;
			for (UsuarioPrivilegio objeto : usuario.getUsuarioPrivilegios()) {
				if (objeto.getPrivilegio().getId() == objeto2.getId()) {
					cont++;
				}
			}
			if (cont == 0) {
				UsuarioPrivilegio usuarioPrivilegio = new UsuarioPrivilegio();
				usuarioPrivilegio.setUsuario(usuario);
				usuarioPrivilegio.setPrivilegio(objeto2);
				usuario.getUsuarioPrivilegios().add(usuarioPrivilegio);
			}
		}
	}
	
	@Command
	@NotifyChange({OBJETO_PRIVILEGIO_AGREGAR, OBJETO_PRIVILEGIO_QUITAR, LISTA_PRIVILEGIOS_DISPONIBLES, 
					LISTA_PRIVILEGIOS_ASIGNADOS, OBJETO_USUARIO})
	public void agregar() {
		privilegiosAsignados.add(privilegioAgregar);
		privilegiosDisponibles.remove(privilegioAgregar);
		privilegioAgregar = null;
		privilegioQuitar = null;
	}
	
	@Command
	@NotifyChange({OBJETO_PRIVILEGIO_AGREGAR, OBJETO_PRIVILEGIO_QUITAR, LISTA_PRIVILEGIOS_DISPONIBLES, 
					LISTA_PRIVILEGIOS_ASIGNADOS, OBJETO_USUARIO})
	public void agregarTodo() {
		for (Privilegio objeto : privilegiosDisponibles) {
			privilegiosAsignados.add(objeto);
		}
		privilegiosDisponibles = null;
		privilegiosDisponibles = new ArrayList<Privilegio>();
		privilegioAgregar = null;
		privilegioQuitar = null;
	}
	
	@Command
	@NotifyChange({OBJETO_PRIVILEGIO_AGREGAR, OBJETO_PRIVILEGIO_QUITAR, LISTA_PRIVILEGIOS_DISPONIBLES, 
					LISTA_PRIVILEGIOS_ASIGNADOS, OBJETO_USUARIO})
	public void quitar() {
		privilegiosDisponibles.add(privilegioQuitar);
		privilegiosAsignados.remove(privilegioQuitar);
		privilegioAgregar = null;
		privilegioQuitar = null;
	}
	
	@Command
	@NotifyChange({OBJETO_PRIVILEGIO_AGREGAR, OBJETO_PRIVILEGIO_QUITAR, LISTA_PRIVILEGIOS_DISPONIBLES, 
					LISTA_PRIVILEGIOS_ASIGNADOS, OBJETO_USUARIO})
	public void quitarTodo() {
		for (Privilegio objeto : privilegiosAsignados) {
			privilegiosDisponibles.add(objeto);		
		}
		privilegiosAsignados = null;
		privilegiosAsignados = new ArrayList<Privilegio>();
		privilegioAgregar = null;
		privilegioQuitar = null;
	}
	
	@Command
	public void aceptar(@ContextParam(ContextType.VIEW) Component view){
				
		if(usuario.getId() != 0) {
			if(usuario.getUsuario() == null  || 
					usuario.getUsuario().trim().length() == 0) {
				Clients.showNotification("Debe ingresar el usuario!", "error", null, "top_center", 0);
				return;
			}else {
				if(usuarioDAO.esRepetido(usuario.getUsuario(), usuario.getId())) {
					Clients.showNotification("El usuario ya existe!", "error", null, "top_center", 0);
					return;
				}
				
				if(clave != null && confirmarClave != null &&
					clave.trim().length() != 0  && confirmarClave.trim().length() != 0) {
					if (!clave.equals(confirmarClave)) {
						Clients.showNotification("Las claves no coinciden!", "error", null, "top_center", 0);
						return;
					}else {
						usuario.setClave(SecurityUtil.encryptToSha(clave.trim()));
						actualizarListaPrivilegios();
					}
				}
			}
		}else {
			if(usuario.getUsuario() == null  || clave == null || confirmarClave == null ||
				usuario.getUsuario().trim().length() == 0 || clave.trim().length() == 0  ||
				confirmarClave.trim().length() == 0) {
				Clients.showNotification("Ingrese los datos de usuario!", "error", null, "top_center", 0);
			}else {
				if(usuarioDAO.esRepetido(usuario.getUsuario(), usuario.getId())) {
					Clients.showNotification("El usuario " + usuario.getUsuario() + " ya existe!", 
							"error", null, "top_center", 0);
					return;
				}
				
				if (!clave.equals(confirmarClave)) {
					Clients.showNotification("Las claves no coinciden!", "error", null, "top_center", 0);
					return;
				}else {
					usuario.setClave(SecurityUtil.encryptToSha(clave.trim()));
					actualizarListaPrivilegios();
				}
			}
		}
		
		if(personaDAO.esIdentificacionRepetida(usuario.getPersona().getIdentificacion(), usuario.getPersona().getId())) {
			Clients.showNotification("Las identificación " + usuario.getPersona().getIdentificacion() + " ya existe!",
					"error", null, "top_center", 0);
			return;
		}
		
		Messagebox.show("Desea guardar este usuario?", "Confirmación", 
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL}, 
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							usuarioDAO.saveOrUpdate(usuario);
							usuarioLista = usuario;
				        	BindUtils.postGlobalCommand(null, null, UsuarioLista.COMANDO_BUSCAR, null);
				        	salir(view);
				        }else if (event.getName().equals(Events.ON_CANCEL)) {
				        	if (usuario.getPersona() == null) {
				        		usuario.setPersona(new Persona());
							}
				        	BindUtils.postGlobalCommand(null, null, UsuarioLista.COMANDO_BUSCAR, null);
				        }
					}
				});
	}	

	@Command
	public void salir(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		usuario = null;
		BindUtils.postGlobalCommand(null, null, UsuarioLista.COMANDO_BUSCAR, null);
		view.detach();
	}
}
