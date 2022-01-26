package com.aguilera.control.diseniador;

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

import com.aguilera.modelo.Categoria;
import com.aguilera.modelo.Disenio;
import com.aguilera.modelo.DisenioArchivo;
import com.aguilera.modeloDAO.CategoriaDAO;
import com.aguilera.modeloDAO.DisenioDAO;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DisenioLista {

	private final static String LISTA_DISENIOS = "disenios";
	private final static String VENTANA_DISENIO_EDITA = "/diseniador/disenioEdita.zul";
	private final static String VENTANA_DISENIO_EDITA_CATEGORIAS = "/diseniador/disenioCategoriaEdita.zul";
	
	public final static String COMANDO_BUSCAR = "DisenioLista.buscar";
	public final static String PARAMETRO_DISENIO = "disenio";

	@Getter @Setter private List<Disenio> disenios;
	@Getter @Setter private String textoBuscar;
	@Getter @Setter private Categoria categoria;
	
	private DisenioDAO disenioDAO = new DisenioDAO();
	private CategoriaDAO categoriaDAO = new CategoriaDAO(); 
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception{
		Selectors.wireComponents(view, this, false);
		textoBuscar = "";
		disenios = disenioDAO.buscarConFiltros(categoria, textoBuscar);
	}
	
	public List<Categoria> getCategorias(){
		return categoriaDAO.findAll();
	}
	
	@Command
	@GlobalCommand(COMANDO_BUSCAR)
	@NotifyChange({LISTA_DISENIOS})
	public void buscar() throws Exception{
		if (disenios != null) {
			disenios = null;
		}
		
		disenios = disenioDAO.buscarConFiltros(categoria, textoBuscar);
	}
	
	@Command
	public void nuevo() {
		Window ventanaCargar = (Window) Executions.createComponents(VENTANA_DISENIO_EDITA, null, null);
		ventanaCargar.doModal();
	}

	@Command
	public void editar(@BindingParam(PARAMETRO_DISENIO) Disenio disenio) {
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(PARAMETRO_DISENIO, disenio);
		Window ventanaCargar = (Window) Executions.createComponents(VENTANA_DISENIO_EDITA, null, parametros);
		ventanaCargar.doModal(); 		
	}
	
	@Command
	public void editarCategorias(@BindingParam(PARAMETRO_DISENIO) Disenio disenio) {
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(PARAMETRO_DISENIO, disenio);
		Window ventanaCargar = (Window) Executions.createComponents(VENTANA_DISENIO_EDITA_CATEGORIAS, null, parametros);
		ventanaCargar.doModal(); 		
	}

	@Command
	public void eliminar(@BindingParam(PARAMETRO_DISENIO) Disenio disenio) {
		
		Messagebox.show("Desea eliminar este diseño?", "Confirmación",
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL},
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							disenio.setEstado("X");
							for(DisenioArchivo objeto : disenio.getDisenioArchivos()) {
								objeto.setEstado("X");
							}
							
							disenioDAO.saveOrUpdate(disenio);
				        	BindUtils.postGlobalCommand(null, null, COMANDO_BUSCAR, null);
				        }
					}
				});	
	}
}
