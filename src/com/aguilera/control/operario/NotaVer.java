package com.aguilera.control.operario;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import com.aguilera.modelo.NotaCabecera;
import com.aguilera.modelo.NotaDetalle;
import com.aguilera.modelo.Pedido;
import com.aguilera.modelo.PedidoDisenio;
import com.aguilera.modelo.Usuario;
import com.aguilera.modeloDAO.NotaCabeceraDAO;
import com.aguilera.modeloDAO.PedidoDAO;
import com.aguilera.modeloDAO.UsuarioDAO;
import com.aguilera.util.Constantes;
import com.aguilera.util.ReporteUtil;
import com.aguilera.util.SecurityUtil;

import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;

@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public class NotaVer {
	
	private final static String REPORTE_NOTA = "/operario/reportes/reporte_nota.jasper";
	private final static String PARAMETRO_ID_CABECERA = "__ID_CABECERA__";
	private final static String PARAMETRO_CLIENTE_NOMBRE = "__CLIENTE_NOMBRE__";
	private final static String PARAMETRO_PEDIDO = "__PEDIDO__";
	private final static String PARAMETRO_OPERARIO_NOMBRE = "__OPERARIO_NOMBRE__";
	private final static String PARAMETRO_FECHA = "__FECHA__";
	
	private PedidoDAO pedidoDAO = new PedidoDAO();
	private NotaCabeceraDAO notaCabeceraDAO = new NotaCabeceraDAO();
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	
	@Getter @Setter private Pedido pedido;
	@Getter @Setter private NotaCabecera notaCabecera;
	@Getter @Setter private List<NotaDetalle> notaDetalles;
	private Pedido pedidoLista;
	private Usuario operario;
	
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
			notaCabecera = new NotaCabecera();
			notaDetalles = new ArrayList<NotaDetalle>();
			
			for(PedidoDisenio objeto : this.pedido.getPedidoDisenios()) {
				NotaDetalle notaDetalle = new NotaDetalle();
				notaDetalle.setDisenio(objeto.getDisenio());
				notaDetalle.setCantidad(objeto.getCantidad());
				notaDetalles.add(notaDetalle);
			}
			
			notaCabecera.setPedido(this.pedido);
			notaCabecera.setFechaEntrega(new Date());
			
			operario = usuarioDAO.buscarUsuario(SecurityUtil.getUser().getUsername());
			
			notaCabecera.setOperario(operario);
		}
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
	
	@Command
	public void imprimir(@ContextParam(ContextType.VIEW) Component view){
				
		Messagebox.show("Desea guardar e imprimir esta nota de entrega?", "Confirmación", 
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL}, 
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							pedido.setEstadoPedido(Constantes.ESTADO_PEDIDO_FABRICADO);
							pedidoDAO.saveOrUpdate(pedido);
							pedidoLista = pedido;
							
							notaCabeceraDAO.saveOrUpdate(notaCabecera);
							for(NotaDetalle objeto : notaDetalles) {
								objeto.setNotaCabecera(notaCabecera);
								notaCabecera.getNotaDetalles().add(objeto);
							}
							notaCabeceraDAO.saveOrUpdate(notaCabecera);
							
							notaCabeceraDAO.refresh(notaCabecera);
							
							HashMap<String,Object> parametros = new HashMap<String, Object>();
							parametros.put(PARAMETRO_ID_CABECERA, notaCabecera.getId());
							parametros.put(PARAMETRO_CLIENTE_NOMBRE, notaCabecera.getPedido().getCliente().getPersona().getNombreCompleto());
							parametros.put(PARAMETRO_PEDIDO, notaCabecera.getPedido().getDetalle());
							parametros.put(PARAMETRO_OPERARIO_NOMBRE, operario.getPersona().getIdentificacion());
							parametros.put(PARAMETRO_FECHA, notaCabecera.getFechaEntrega());
							try {
								ReporteUtil.ejecutaReporte(notaCabeceraDAO, REPORTE_NOTA, 
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
