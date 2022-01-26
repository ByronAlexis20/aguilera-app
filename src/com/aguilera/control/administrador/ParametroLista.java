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

import com.aguilera.modelo.Parametro;
import com.aguilera.modeloDAO.ParametroDAO;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ParametroLista {

	private static final String COMANDO_BUSCAR = "ParametroLista.buscar";
	private static final String ID_OBJETO_EDICION = "idEnEdicion";
	private static final String OBJETO_EDICION = "parametro";
	private static final String LISTA_OBJETOS = "parametros";
	
	@Wire private Listbox lstDatos;
	
	private ParametroDAO parametroDAO = new ParametroDAO();

	@Getter @Setter private List<Parametro> parametros;
	@Getter @Setter private int idEnEdicion;
	@Getter @Setter private Parametro parametro;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		parametros = new ArrayList<Parametro>();
		buscar();
	}
	
	@Command
	@GlobalCommand(COMANDO_BUSCAR)
	@NotifyChange({LISTA_OBJETOS, OBJETO_EDICION, ID_OBJETO_EDICION})
	public void buscar() {
		if (parametros != null) {
			parametros = null;
		}
		parametros = parametroDAO.findAll();	
		idEnEdicion = 0;
		parametro = null;
	}
	
	@Command
	@NotifyChange({OBJETO_EDICION, ID_OBJETO_EDICION})
	public void editar(@BindingParam(OBJETO_EDICION) Parametro parametro) {
		if (parametro.getId() != 0) {
			this.parametro = parametroDAO.getEntityManager().find(parametro.getClass(), parametro.getId());
		}else{
			this.parametro = parametro;
		}
		idEnEdicion = parametro.getId();
	}
	
	@Command
	@NotifyChange({OBJETO_EDICION, ID_OBJETO_EDICION})
	public void aceptar(@ContextParam(ContextType.COMPONENT) Component component, 
			@ContextParam(ContextType.VIEW) Component view) {
		
		Messagebox.show("Desea guardar este parámetro?", "Confirmación", 
						new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL}, 
						null,
						new EventListener() {
							@Override
							public void onEvent(Event event) throws Exception {
								if (event.getName().equals(Events.ON_OK)) {
									parametroDAO.saveOrUpdate(parametro);
						        	BindUtils.postGlobalCommand(null, null, COMANDO_BUSCAR, null);
						        }
							}
						});
	}
	
	@Command
	@NotifyChange({OBJETO_EDICION, ID_OBJETO_EDICION})
	public void cancelar() {
		if (idEnEdicion == 0) {
			parametros.remove(parametro);
		}
		idEnEdicion = 0;
		parametro = null;
	}
}
