package com.aguilera.control.diseniador;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import com.aguilera.modelo.Categoria;
import com.aguilera.modelo.Disenio;
import com.aguilera.modelo.DisenioCategoria;
import com.aguilera.modeloDAO.CategoriaDAO;
import com.aguilera.modeloDAO.DisenioDAO;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"rawtypes", "unused", "unchecked"})
public class DisenioCategoriaEdita {

	private CategoriaDAO categoriaDAO = new CategoriaDAO();
	private DisenioDAO disenioDAO = new DisenioDAO();
	
	@Getter @Setter private List<Categoria> categorias;
	@Getter @Setter private Disenio disenio;
	private Disenio disenioLista;
	
	@Init
	public void init(@ExecutionArgParam(DisenioLista.PARAMETRO_DISENIO) Disenio disenio) {
		disenioLista = disenio;
		this.disenio = disenioDAO.getEntityManager().find(Disenio.class, disenio.getId());
		categorias = categoriaDAO.findAll();
			
		if (this.disenio.getDisenioCategorias() != null) {
			for (DisenioCategoria objetoDC : this.disenio.getDisenioCategorias()) {
				for (Categoria objetoC : this.categorias) {
					if (objetoDC.getCategoria().getId() == objetoC.getId()) {
						objetoC.setSeleccionado(true);
					}
				}
			}
		}	
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
	
	private void actualizarCategorias() {
		if (disenio.getDisenioCategorias() == null) {
			disenio.setDisenioCategorias(new ArrayList<DisenioCategoria>());
		}
		for(DisenioCategoria objetoDC : disenio.getDisenioCategorias()) {
			int cont = 0;
			for (Categoria objetoC : categorias) {
				if (objetoDC.getCategoria().getId() == objetoC.getId() && objetoC.isSeleccionado()) {
					cont++;
				}
			}
			if(cont == 0) {
				objetoDC.setEstado("X");
			}
		}
		for(Categoria objetoC : categorias) {
			int cont = 0;
			for (DisenioCategoria objetoDC : disenio.getDisenioCategorias()) {
				if (objetoDC.getCategoria().getId() == objetoC.getId() && objetoC.isSeleccionado()) {
					cont++;
				}
			}
			if (cont == 0 && objetoC.isSeleccionado()) {
				DisenioCategoria disenioCategoria = new DisenioCategoria();
				disenioCategoria.setDisenio(disenio);
				disenioCategoria.setCategoria(objetoC);
				disenio.getDisenioCategorias().add(disenioCategoria);
			}
		}
	}
	
	@Command
	public void aceptar(@ContextParam(ContextType.VIEW) Component view){
				
		actualizarCategorias();
		
		Messagebox.show("Desea guardar estas categorias?", "Confirmación", 
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL}, 
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							disenioDAO.saveOrUpdate(disenio);
							disenioLista = disenio;
				        	BindUtils.postGlobalCommand(null, null, DisenioLista.COMANDO_BUSCAR, null);
				        	salir(view);
				        }else if (event.getName().equals(Events.ON_CANCEL)) {
				        	BindUtils.postGlobalCommand(null, null, DisenioLista.COMANDO_BUSCAR, null);
				        }
					}
				});
	}
	
	@Command
	public void salir(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		disenio = null;
		BindUtils.postGlobalCommand(null, null, DisenioLista.COMANDO_BUSCAR, null);
		view.detach();
	}
}