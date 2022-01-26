package com.aguilera.control.cliente;

import java.util.HashMap;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Window;

import com.aguilera.modelo.Categoria;
import com.aguilera.modelo.Disenio;
import com.aguilera.modeloDAO.CategoriaDAO;
import com.aguilera.modeloDAO.DisenioDAO;

import lombok.Getter;
import lombok.Setter;

public class DisenioLista {
	private final static String LISTA_DISENIOS = "disenios";
	private final static String VENTANA_DISENIO_VER = "/cliente/disenioVer.zul";
	
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
	public void ver(@BindingParam(PARAMETRO_DISENIO) Disenio disenio) {
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(PARAMETRO_DISENIO, disenio);
		Window ventanaCargar = (Window) Executions.createComponents(VENTANA_DISENIO_VER, null, parametros);
		ventanaCargar.doModal(); 		
	}
}
