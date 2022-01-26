package com.aguilera.control.operario;

import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import com.aguilera.modelo.Pedido;
import com.aguilera.modelo.PedidoProducto;
import com.aguilera.modelo.Producto;
import com.aguilera.modeloDAO.PedidoDAO;
import com.aguilera.modeloDAO.ProductoDAO;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({ "rawtypes", "unchecked", "unused"})
public class PedidoProductoEdita {
	
	private static final String ID_OBJETO_EDICION = "idEnEdicion";
	private static final String OBJETO_EDICION = "pedidoProducto";
	private static final String LISTA_OBJETOS = "pedido";
	private static final String LISTA_PRODUCTOS = "productos";
	private static final String COMANDO_CARGAR_PRODUCTOS = "PedidoProductoEdita.cargarProductos";
	
	@Wire private Listbox lstDatos;

	private PedidoDAO pedidoDAO = new PedidoDAO();
	private ProductoDAO productoDAO = new ProductoDAO();
	
	@Getter @Setter private Pedido pedido;
	@Getter @Setter private int idEnEdicion;
	@Getter @Setter private PedidoProducto pedidoProducto;
	@Getter @Setter private List<Producto> productos;
	private Pedido pedidoLista;
	
	@Init
	public void init(@ExecutionArgParam(PedidoLista.PARAMETRO_PEDIDO) Pedido pedido) {
		pedidoLista = pedido;
		this.pedido = pedidoDAO.getEntityManager().find(Pedido.class, pedido.getId());
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
	
	@Command
	@NotifyChange({LISTA_PRODUCTOS, OBJETO_EDICION})
	public void buscarProductos(@BindingParam("textoBuscar") String textoBuscar) {
		if (productos !=null) {
			productos = null;
		}
		
		productos = productoDAO.buscarPorTexto(textoBuscar);
	}
	
	@Command
	@GlobalCommand(COMANDO_CARGAR_PRODUCTOS)
	@NotifyChange({LISTA_OBJETOS, OBJETO_EDICION, ID_OBJETO_EDICION, LISTA_PRODUCTOS})
	public void cargarProductos() {
		try {
			pedidoDAO.refresh(pedido);
		} catch (Exception e) {
			e.printStackTrace();
		}
		productos = null;
		pedidoProducto = null;
		idEnEdicion = 0;
	}
	
	@Command
	@NotifyChange({LISTA_OBJETOS, ID_OBJETO_EDICION, OBJETO_EDICION})
	public void nuevoProducto() {
		if (this.pedidoProducto != null && this.pedidoProducto.getId() == 0 && idEnEdicion == 0) {
			cancelarProducto();
		}
		if (this.pedidoProducto == null || this.pedidoProducto.getId() != 0) {
			this.pedidoProducto  = new PedidoProducto();
			pedido.getPedidoProductos().add((lstDatos. getActivePage() * lstDatos.getPageSize()), this.pedidoProducto);
			idEnEdicion = this.pedidoProducto.getId();
		}
	}
	
	@Command
	@NotifyChange({OBJETO_EDICION, ID_OBJETO_EDICION})
	public void aceptarProducto(@ContextParam(ContextType.COMPONENT) Component component, 
			@ContextParam(ContextType.VIEW) Component view) {
		
		if (pedidoProducto.getCantidad() > pedidoProducto.getProducto().getStock()) {
			Clients.showNotification("Stock insuficiente!", "error", null, "top_center", 0);
			return;
		}
		
		this.pedidoProducto.setPedido(this.pedido);
		
		Messagebox.show("Desea registrar este producto?", "Confirmación", 
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL}, 
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							pedidoDAO.saveOrUpdate(pedido);
							BindUtils.postGlobalCommand(null, null, COMANDO_CARGAR_PRODUCTOS, null);
				        }
					}
				});
	}
	
	@Command
	@NotifyChange({OBJETO_EDICION, ID_OBJETO_EDICION, LISTA_OBJETOS})
	public void cancelarProducto() {
		if (idEnEdicion == 0) {
			pedido.getPedidoProductos().remove(pedidoProducto);
		}
		idEnEdicion = 0;
		pedidoProducto = null;
	}
	
	@Command
	@NotifyChange({LISTA_OBJETOS})
	public void eliminarProducto(@BindingParam(OBJETO_EDICION) PedidoProducto pedidoProducto) {
		
		Messagebox.show("Desea descartar este producto?", "Confirmación",
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL},
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							
							for (PedidoProducto objeto : pedido.getPedidoProductos()) {
								if (objeto.getId() == pedidoProducto.getId()) {
									objeto.setEstado("X");
								}
							}
							pedidoDAO.saveOrUpdate(pedido);
				        	BindUtils.postGlobalCommand(null, null, COMANDO_CARGAR_PRODUCTOS, null);
				        }
					}
				});
	}
	
	@Command
	public void aceptar(@ContextParam(ContextType.VIEW) Component view){
		pedidoLista = pedido;
		BindUtils.postGlobalCommand(null, null, PedidoLista.COMANDO_BUSCAR, null);
		view.detach();
	}	

	@Command
	public void salir(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		pedido = null;
		BindUtils.postGlobalCommand(null, null, PedidoLista.COMANDO_BUSCAR, null);
		view.detach();
	}
}
