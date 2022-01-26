package com.aguilera.control.administrador;

import java.util.ArrayList;
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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import com.aguilera.modelo.OpcionPrivilegio;
import com.aguilera.modelo.Privilegio;
import com.aguilera.modelo.UsuarioPrivilegio;
import com.aguilera.modeloDAO.PrivilegioDAO;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PrivilegioLista {

	private static final String COMANDO_BUSCAR = "PrivilegioLista.buscar";
	private static final String ID_OBJETO_EDICION = "idEnEdicion";
	private static final String OBJETO_EDICION = "privilegio";
	private static final String LISTA_OBJETOS = "privilegios";
	
	@Wire private Listbox lstDatos;
	
	private PrivilegioDAO privilegioDAO = new PrivilegioDAO();
	
	@Getter @Setter private String textoBuscar;

	@Getter @Setter private List<Privilegio> privilegios;
	@Getter @Setter private int idEnEdicion;
	@Getter @Setter private Privilegio privilegio;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		privilegios = new ArrayList<Privilegio>();
		textoBuscar = "";
		buscar();
	}
	
	@Command
	@GlobalCommand(COMANDO_BUSCAR)
	@NotifyChange({LISTA_OBJETOS, OBJETO_EDICION, ID_OBJETO_EDICION})
	public void buscar() {
		if (privilegios != null) {
			privilegios = null;
		}
		privilegios = privilegioDAO.buscarPorTexto(textoBuscar);	
		idEnEdicion = 0;
		privilegio = null;
	}
	
	@Command
	@NotifyChange({LISTA_OBJETOS, ID_OBJETO_EDICION})
	public void nuevo() {
		if (this.privilegio != null && this.privilegio.getId() == 0 && idEnEdicion == 0) {
			cancelar();
		}
		if (this.privilegio == null || this.privilegio.getId() != 0) {
			this.privilegio  = new Privilegio();
			privilegios.add((lstDatos.getActivePage() * lstDatos.getPageSize()), this.privilegio);
			idEnEdicion = this.privilegio.getId();
		}
	}
	
	@Command
	@NotifyChange({OBJETO_EDICION, ID_OBJETO_EDICION})
	public void editar(@BindingParam(OBJETO_EDICION) Privilegio privilegio) {
		if (privilegio.getId() != 0) {
			this.privilegio = privilegioDAO.getEntityManager().find(privilegio.getClass(), privilegio.getId());
		}else{
			this.privilegio = privilegio;
		}
		idEnEdicion = privilegio.getId();
	}
	
	@Command
	@NotifyChange({OBJETO_EDICION, ID_OBJETO_EDICION})
	public void aceptar(@ContextParam(ContextType.COMPONENT) Component component, 
			@ContextParam(ContextType.VIEW) Component view) {
		
		Messagebox.show("Desea guardar este privilegio?", "Confirmación", 
						new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL}, 
						null,
						new EventListener() {
							@Override
							public void onEvent(Event event) throws Exception {
								if (event.getName().equals(Events.ON_OK)) {
									privilegioDAO.saveOrUpdate(privilegio);
						        	BindUtils.postGlobalCommand(null, null, COMANDO_BUSCAR, null);
						        }
							}
						});
	}
	
	@Command
	@NotifyChange({OBJETO_EDICION, ID_OBJETO_EDICION})
	public void cancelar() {
		if (idEnEdicion == 0) {
			privilegios.remove(privilegio);
		}
		idEnEdicion = 0;
		privilegio = null;
	}

	@Command
	@NotifyChange({ID_OBJETO_EDICION})
	public void eliminar(@BindingParam(OBJETO_EDICION) Privilegio privilegio) {
		
		Messagebox.show("Desea eliminar este privilegio?", "Confirmación",
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL},
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							privilegio.setEstado("X");
							for (OpcionPrivilegio objeto : privilegio.getOpcionPrivilegios()) {
								objeto.setEstado("X");
							}
							for (UsuarioPrivilegio objeto : privilegio.getUsuarioPrivilegios()) {
								objeto.setEstado("X");
							}
							privilegioDAO.saveOrUpdate(privilegio);
				        	BindUtils.postGlobalCommand(null, null, COMANDO_BUSCAR, null);
				        }
					}
				});
	}
}
