package com.aguilera.control.administrador;

import java.util.ArrayList;
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
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.aguilera.modelo.Producto;
import com.aguilera.modeloDAO.ProductoDAO;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ProductoLista {

	private static final String ID_OBJETO_EDICION = "idEnEdicion";
	private static final String OBJETO_EDICION = "producto";
	private static final String LISTA_OBJETOS = "productos";
	private final static String VENTANA_COMPRA_EDITA = "/administrador/compraEdita.zul";
	
	public static final String COMANDO_BUSCAR = "ProductoLista.buscar";
	public final static String PARAMETRO_PRODUCTO = "producto";
	
	@Wire private Listbox lstDatos;
	
	private ProductoDAO productoDAO = new ProductoDAO();
	
	@Getter @Setter private String textoBuscar;
	@Getter @Setter private String tipoBuscar;

	@Getter @Setter private List<Producto> productos;
	@Getter @Setter private int idEnEdicion;
	@Getter @Setter private Producto producto;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		productos = new ArrayList<Producto>();
		textoBuscar = null;
		tipoBuscar = null;
	}
	
	@Command
	@GlobalCommand(COMANDO_BUSCAR)
	@NotifyChange({LISTA_OBJETOS, OBJETO_EDICION, ID_OBJETO_EDICION})
	public void buscar() {
				
		if (productos != null) {
			productos = null;
		}
		productos = productoDAO.buscarConFiltros(textoBuscar, tipoBuscar);	
		idEnEdicion = 0;
		producto = null;
	}
	
	@Command
	@NotifyChange({LISTA_OBJETOS, ID_OBJETO_EDICION})
	public void nuevo() {
		if (this.producto != null && this.producto.getId() == 0 && idEnEdicion == 0) {
			cancelar();
		}
		if (this.producto == null || this.producto.getId() != 0) {
			this.producto  = new Producto();
			productos.add((lstDatos. getActivePage() * lstDatos.getPageSize()), this.producto);
			idEnEdicion = this.producto.getId();
		}
	}
	
	@Command
	@NotifyChange({OBJETO_EDICION, ID_OBJETO_EDICION})
	public void editar(@BindingParam(OBJETO_EDICION) Producto producto) {
		if (producto.getId() != 0) {
			this.producto = productoDAO.getEntityManager().find(Producto.class, producto.getId());
		}else{
			this.producto = producto;
		}
		
		idEnEdicion = producto.getId();
	}
	
	@Command
	@NotifyChange({OBJETO_EDICION, ID_OBJETO_EDICION})
	public void aceptar(@ContextParam(ContextType.COMPONENT) Component component, 
			@ContextParam(ContextType.VIEW) Component view) {
		
		Messagebox.show("Desea registrar este producto?", "Confirmación", 
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL}, 
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							if(producto.getId() == 0) {
								producto.setStock(0);
								producto.setPrecio(0);
							}
							productoDAO.saveOrUpdate(producto);
							BindUtils.postGlobalCommand(null, null, COMANDO_BUSCAR, null);
				        }
					}
				});
	}
	
	@Command
	@NotifyChange({OBJETO_EDICION, ID_OBJETO_EDICION})
	public void cancelar() {
		if (idEnEdicion == 0) {
			productos.remove(producto);
		}
		idEnEdicion = 0;
		producto = null;
	}

	@Command
	@NotifyChange({ID_OBJETO_EDICION})
	public void eliminar(@BindingParam(OBJETO_EDICION) Producto producto) {
		
		if(!producto.getCompras().isEmpty()) {
			Clients.showNotification("No puede eliminar este producto porque ya existen compras!",
					"error", null, "top_center", 0);
			return;
		}
		
		Messagebox.show("Desea eliminar este producto?", "Confirmación",
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL},
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							producto.setEstado("X");
							productoDAO.saveOrUpdate(producto);
				        	BindUtils.postGlobalCommand(null, null, COMANDO_BUSCAR, null);
				        }
					}
				});
	}
	
	@Command
	@NotifyChange({OBJETO_EDICION})
	public void agregarCompra(@BindingParam(OBJETO_EDICION) Producto producto) {
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(PARAMETRO_PRODUCTO, producto);
		Window ventanaCargar = (Window) Executions.createComponents(VENTANA_COMPRA_EDITA, null, parametros);
		ventanaCargar.doModal();
	}
}
