package com.aguilera.control;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
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
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.aguilera.control.administrador.UsuarioLista;
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
public class Registrarse {

	private final static String OBJETO_USUARIO = "usuario";
	
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	private PersonaDAO personaDAO = new PersonaDAO();
	private PrivilegioDAO privilegioDAO = new PrivilegioDAO();
	
	@Getter @Setter private Usuario usuario;
	@Getter @Setter private Privilegio privilegioCliente;
	@Getter @Setter private String clave = "";
	@Getter @Setter private String confirmarClave = "";
	
	@Init
	public void init() {
		
		this.usuario = new Usuario();
		this.usuario.setPersona(new Persona());
		privilegioCliente = privilegioDAO.buscarPorRol("ROLE_CLIENTE");
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
	
	@Command
	@NotifyChange({OBJETO_USUARIO})
	public void copiarUsuario() {
		usuario.setUsuario(usuario.getPersona().getIdentificacion());
	}
	
	private void agregarPrivilegioCliente() {
		int contador = 0;
		if(usuario.getUsuarioPrivilegios() == null) {
			usuario.setUsuarioPrivilegios(new ArrayList<UsuarioPrivilegio>());
		}
		
		for(UsuarioPrivilegio objeto : usuario.getUsuarioPrivilegios()) {
			if(objeto.getPrivilegio().getId() == privilegioCliente.getId()) {
				contador++;
			}
		}
		
		if(contador == 0) {
			UsuarioPrivilegio usuarioPrivilegio = new UsuarioPrivilegio();
			usuarioPrivilegio.setPrivilegio(privilegioCliente);
			usuarioPrivilegio.setUsuario(usuario);
			usuario.getUsuarioPrivilegios().add(usuarioPrivilegio);
		}
	}
	
	@Command
	public void aceptar(@ContextParam(ContextType.VIEW) Component view){
		
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
			}
		}
		
		if(usuario.getPersona().getIdentificacion() != null) {
			if(personaDAO.esIdentificacionRepetida(usuario.getPersona().getIdentificacion(), usuario.getPersona().getId())) {
				Clients.showNotification("Las identificación " + usuario.getPersona().getIdentificacion() + " ya existe!",
						"error", null, "top_center", 0);
				return;
			}
		}
		
		agregarPrivilegioCliente();
		
		Messagebox.show("Desea registrar este usuario?", "Confirmación", 
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL}, 
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							usuarioDAO.saveOrUpdate(usuario);
				        	salir(view);
				        }else if (event.getName().equals(Events.ON_CANCEL)) {
				        	if (usuario.getPersona() == null) {
				        		usuario.setPersona(new Persona());
							}
				        }
					}
				});
	}	
	
	@Command
	public void salir(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		usuario = null;
		view.detach();
	}
}
