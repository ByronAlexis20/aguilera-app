package com.aguilera.control.administrador;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
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

import com.aguilera.modelo.Cabecera;
import com.aguilera.modelo.Detalle;
import com.aguilera.modelo.Pedido;
import com.aguilera.modelo.PedidoDisenio;
import com.aguilera.modeloDAO.CabeceraDAO;
import com.aguilera.modeloDAO.PedidoDAO;
import com.aguilera.util.Constantes;
import com.aguilera.util.ReporteUtil;

import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;

@SuppressWarnings({"rawtypes", "unchecked", "unused", "resource"})
public class FacturaVer {
	
	private final static String REPORTE_FACTURAS = "/administrador/reportes/reporte_factura.jasper";
	private final static String PARAMETRO_ID_CABECERA = "__ID_CABECERA__";
	private final static String PARAMETRO_TOTAL = "__TOTAL__";
	private final static String PARAMETRO_SUBTOTAL = "__SUBTOTAL__";
	private final static String PARAMETRO_RECARGO = "__RECARGO__";
	private final static String PARAMETRO_IVA = "__IVA__";
	private final static String PARAMETRO_CLIENTE_NOMBRE = "__CLIENTE_NOMBRE__";
	private final static String PARAMETRO_CLIENTE_IDENTIFICACION = "__CLIENTE_IDENTIFICACION__";
	private final static String PARAMETRO_CLIENTE_DIRECCION = "__CLIENTE_DIRECCION__";
	private final static String PARAMETRO_NUMERO_FACTURA = "__NUMERO_FACTURA__";
	private final static String PARAMETRO_FECHA = "__FECHA__";
	
	private PedidoDAO pedidoDAO = new PedidoDAO();
	private CabeceraDAO cabeceraDAO = new CabeceraDAO();
	
	@Getter @Setter private Pedido pedido;
	@Getter @Setter private Cabecera cabecera;
	@Getter @Setter private List<Detalle> detalles;
	private Pedido pedidoLista;
	
	@Init
	public void init(@ExecutionArgParam(PedidoLista.PARAMETRO_PEDIDO) Pedido pedido) {
		pedidoLista = pedido;
		
		if (pedido == null) {
			this.pedido = new Pedido();
		}else{
			if (pedido.getId() != 0) {
				this.pedido = pedidoDAO.getEntityManager().find(Pedido.class, pedido.getId());
			}else {
				this.pedido = pedido;
			}
			cabecera = new Cabecera();
			detalles = new ArrayList<Detalle>();
			
			float sumSubtotal = 0;
			float sumIVA = 0;
			
			for(PedidoDisenio objeto : this.pedido.getPedidoDisenios()) {
				Detalle detalle = new Detalle();
				detalle.setDisenio(objeto.getDisenio());
				detalle.setCantidad(objeto.getCantidad());
				detalle.setValorUnitario(objeto.getDisenio().getPrecio());
				detalle.setSubtotal(detalle.getCantidad() * detalle.getValorUnitario());
				detalle.setIva(detalle.getSubtotal() * Constantes.IVA);
				detalle.setTotal(detalle.getSubtotal() + detalle.getIva());
				sumSubtotal = sumSubtotal + detalle.getSubtotal();
				sumIVA = sumIVA + detalle.getIva();
				detalles.add(detalle);
			}
			
			cabecera.setPedido(this.pedido);
			cabecera.setFechaVenta(new Date());
			cabecera.setSubtotal(sumSubtotal);
			cabecera.setIva(sumIVA + (this.pedido.getRecargo() * Constantes.IVA));
			cabecera.setRecargo(this.pedido.getRecargo());
			cabecera.setTotal(cabecera.getSubtotal() + cabecera.getIva() + cabecera.getRecargo());
		}
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
	
	@Command
	public void imprimir(@ContextParam(ContextType.VIEW) Component view){
				
		Messagebox.show("Desea guardar e imprimir esta nota de venta?", "Confirmación", 
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL}, 
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							pedido.setEstadoPedido(Constantes.ESTADO_PEDIDO_PAGADO);
							pedidoDAO.saveOrUpdate(pedido);
							pedidoLista = pedido;
							
							cabeceraDAO.saveOrUpdate(cabecera);
							for(Detalle objeto : detalles) {
								objeto.setCabecera(cabecera);
								cabecera.getDetalles().add(objeto);
							}
							cabeceraDAO.saveOrUpdate(cabecera);
							
							cabeceraDAO.refresh(cabecera);
							
							Formatter fmt = new Formatter();
							HashMap<String,Object> parametros = new HashMap<String, Object>();
							parametros.put(PARAMETRO_ID_CABECERA, cabecera.getId());
							parametros.put(PARAMETRO_TOTAL, cabecera.getTotal());
							parametros.put(PARAMETRO_SUBTOTAL, cabecera.getSubtotal());
							parametros.put(PARAMETRO_RECARGO, cabecera.getRecargo());
							parametros.put(PARAMETRO_IVA, cabecera.getIva());
							parametros.put(PARAMETRO_CLIENTE_NOMBRE, cabecera.getPedido().getCliente().getPersona().getNombreCompleto());
							parametros.put(PARAMETRO_CLIENTE_IDENTIFICACION, cabecera.getPedido().getCliente().getPersona().getIdentificacion());
							parametros.put(PARAMETRO_CLIENTE_DIRECCION, cabecera.getPedido().getCliente().getPersona().getDireccion());
							parametros.put(PARAMETRO_NUMERO_FACTURA, fmt.format("%08d",cabecera.getId()).toString());
							parametros.put(PARAMETRO_FECHA, cabecera.getFechaVenta());
							try {
								ReporteUtil.ejecutaReporte(cabeceraDAO, REPORTE_FACTURAS, 
												null, 
												Constantes.FORMATO_PDF, parametros);
							} catch (SQLException | JRException | IOException | InterruptedException e) {
								e.printStackTrace();
							}
							
				        	BindUtils.postGlobalCommand(null, null, PedidoLista.COMANDO_BUSCAR, null);
				        	salir(view);
				        }else if (event.getName().equals(Events.ON_CANCEL)) {
				        	BindUtils.postGlobalCommand(null, null, PedidoLista.COMANDO_BUSCAR, null);
				        }
					}
				});
	}	

	@Command
	public void salir(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		pedido = null;
		BindUtils.postGlobalCommand(null, null, PedidoLista.COMANDO_BUSCAR, null);
		view.detach();
	}
}
