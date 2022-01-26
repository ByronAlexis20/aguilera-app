package com.aguilera.control;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Include;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;

import com.aguilera.modelo.Opcion;
import com.aguilera.modelo.Pedido;
import com.aguilera.modelo.Usuario;
import com.aguilera.modelo.UsuarioPrivilegio;
import com.aguilera.modeloDAO.PedidoDAO;
import com.aguilera.modeloDAO.UsuarioDAO;
import com.aguilera.modeloDAO.UsuarioPrivilegioDAO;
import com.aguilera.util.Constantes;
import com.aguilera.util.SecurityUtil;

import lombok.Getter;
import lombok.Setter;

public class Index {

	private static final String ATRIBUTO_OPCION = "__OPCION__";
	private static final String ATRIBUTO_TAB = "__TAB__";
	
	@Wire private Menubar mnMenuPrincipal;
	@Wire public Tabbox tbAreaTrabajo;
	@Wire private Include dashboard;
	
	@Getter @Setter String fondo;
	@Getter @Setter String usuario;
	
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	private PedidoDAO pedidoDAO = new PedidoDAO();
	UsuarioPrivilegioDAO usuarioPrivilegioDAO = new UsuarioPrivilegioDAO();
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		usuario = (usuarioDAO.buscarUsuario(SecurityUtil.getUser().getUsername())).getUsuario();
		cargaMenu();
		notificarPedidos();
		dashboard.setVisible(false);
		this.cargarDashboard();
	}
	private void cargarDashboard() {
		Usuario us = this.usuarioDAO.buscarUsuario(SecurityUtil.getUser().getUsername());
		List<UsuarioPrivilegio> lista = this.usuarioPrivilegioDAO.buscarRolPorUsuario(us.getId());
		for(UsuarioPrivilegio up : lista) {
			if(up.getPrivilegio().getId() == 1 || up.getPrivilegio().getId() == 2) {
				dashboard.setVisible(true);				
			}
		}
	}
	private void cargaMenu(){
		List<Opcion> opciones;
		opciones =	usuarioDAO.buscarUsuario(SecurityUtil.getUser().getUsername()).getOpciones();
		
		for (Opcion opcion : opciones) {
			if (opcion.getOpciones() == null || opcion.getOpciones().size() == 0) {
				mnMenuPrincipal.appendChild(getMenuItem(opcion));
			}else{
				mnMenuPrincipal.appendChild(getMenu(opcion));
			}
		}
	}
	
	private Menu getMenu(Opcion opcion) {
		Menu retorno = new Menu();

		if (opcion.getImagen() != null) {
			retorno.setIconSclass(opcion.getImagen());
		}
		retorno.setLabel(opcion.getTitulo());
		retorno.setAttribute(ATRIBUTO_OPCION, opcion);
	
		retorno.appendChild(new Menupopup());
		for (Opcion subOpcion : opcion.getOpciones()) {
			if (subOpcion.getOpciones() == null || subOpcion.getOpciones().size() == 0) {
				retorno.getMenupopup().appendChild(getMenuItem(subOpcion));
			}else{
				retorno.getMenupopup().appendChild(getMenu(subOpcion));
			}
		}
		return retorno;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Menuitem getMenuItem(Opcion opcion) {
		Menuitem retorno = new Menuitem();
		
		if (opcion.getImagen() != null) {
			retorno.setIconSclass(opcion.getImagen());
		}
		retorno.setLabel(opcion.getTitulo());
		retorno.setAttribute(ATRIBUTO_OPCION, opcion);
		
		retorno.addEventListener("onClick", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Menuitem menuItem = (Menuitem) event.getTarget();
				Opcion opcion;
				opcion = (Opcion) menuItem.getAttribute(ATRIBUTO_OPCION);
				if (opcion.getFormulario() != null) {
					cargaAreaTrabajo(opcion.getFormulario(), opcion.getTitulo(), opcion.getImagen());
				}
			}
		});
		
		return retorno;
	}
	
	private void cargaAreaTrabajo(String formulario, String titulo, String icono) {
		// Si la cadena comienza con HTTP inicia el formulario en una nueva ventana.
		if (formulario == null || formulario.isEmpty() || "#".equals(formulario)) {
			return;
		}
		if (formulario.trim().toLowerCase().substring(0, 4).equals("http")) {
			Executions.getCurrent().sendRedirect(formulario, "_blank");
		}else{
			
			// Busca si el formulario ya ha sido cargado.
			
			boolean encontrado = false;
			
			for (Component cmp1 : tbAreaTrabajo.getTabpanels().getChildren()) {
				Tabpanel tp = (Tabpanel) cmp1;
				for (Component cmp2 : tp.getChildren()) {
					if (cmp2 instanceof Include) {
						Include inc = (Include) cmp2;
						if (inc.getSrc().equals(formulario)) {
							Tab tb = (Tab) tp.getAttribute(ATRIBUTO_TAB);
							tb.setSelected(true);
							encontrado = true;
							break;
						}
					}
				} 
				if (encontrado) {
					break;
				}
			}
			
			if (!encontrado) {
				Tab tab = new Tab();
				Tabpanel tabpanel = new Tabpanel();
				Include include = new Include();
				
				tab.setLabel(titulo);
				if (icono != null) {
					tab.setIconSclass(icono);
				}
				tab.setClosable(true);
				tab.setSelected(true);
				include.setSrc(formulario);
				tabpanel.getChildren().add(include);
				tabpanel.setAttribute(ATRIBUTO_TAB, tab);
				
				tbAreaTrabajo.getTabs().appendChild(tab);
				tbAreaTrabajo.getTabpanels().appendChild(tabpanel);
			}
			
		}
	}
	
	private void notificarPedidos() {
		Usuario cliente = usuarioDAO.buscarUsuario(SecurityUtil.getUser().getUsername());
		if(cliente.isCliente()) {
			List<Pedido> pedidos = pedidoDAO.buscarConFiltros(cliente.getId(), Constantes.ESTADO_PEDIDO_DISENIO);
			if (pedidos != null) {
				if(pedidos.size() > 0) {
					Clients.showNotification("Tiene " + pedidos.size() + " pedido(s) diseñados esperando su aprobación", "info", null, "middle_center", 0);
				}
			}
		}
	}
}
