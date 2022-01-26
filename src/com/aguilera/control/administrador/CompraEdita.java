package com.aguilera.control.administrador;

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
import org.zkoss.zul.Messagebox;

import com.aguilera.modelo.Compra;
import com.aguilera.modelo.Producto;
import com.aguilera.modeloDAO.CompraDAO;
import com.aguilera.modeloDAO.ProductoDAO;
import com.aguilera.util.Constantes;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public class CompraEdita {

	private final static String OBJETO_COMPRA = "compra";
	
	private CompraDAO compraDAO = new CompraDAO();
	private ProductoDAO productoDAO = new ProductoDAO();
	
	@Getter @Setter private Producto producto;
	@Getter @Setter private Compra compra;
	private Producto productoLista;
	
	@Init
	public void init(@ExecutionArgParam(ProductoLista.PARAMETRO_PRODUCTO) Producto producto) {
		productoLista = producto;
		
		if (producto.getId() != 0) {
			this.producto = productoDAO.getEntityManager().find(Producto.class, producto.getId());
		}
		compra = new Compra();
		compra.setProducto(this.producto);
		compra.setPrecioUnitario(this.producto.getPrecio());
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
	
	@Command
	@NotifyChange(OBJETO_COMPRA)
	public void calcularValores() {
		compra.setSubtotal(compra.getPrecioUnitario() * compra.getCantidad());
		compra.setIva(compra.getSubtotal() * Constantes.IVA);
		compra.setTotal(compra.getSubtotal() + compra.getIva());
	}
	
	@Command
	public void aceptar(@ContextParam(ContextType.VIEW) Component view){
				
		Messagebox.show("Desea guardar la compra de este producto?", "Confirmación", 
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL}, 
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							producto.setPrecio(compra.getPrecioUnitario());
							producto.setStock(compra.getProducto().getStock() + compra.getCantidad());
							
							compraDAO.saveOrUpdate(compra);
							productoDAO.saveOrUpdate(producto);
							productoDAO.refresh(producto);
							
							productoLista = producto;
				        	BindUtils.postGlobalCommand(null, null, ProductoLista.COMANDO_BUSCAR, null);
				        	salir(view);
				        }else if (event.getName().equals(Events.ON_CANCEL)) {
				        	BindUtils.postGlobalCommand(null, null, ProductoLista.COMANDO_BUSCAR, null);
				        }
					}
				});
	}	

	@Command
	public void salir(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		producto = null;
		BindUtils.postGlobalCommand(null, null, ProductoLista.COMANDO_BUSCAR, null);
		view.detach();
	}
}
