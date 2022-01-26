package com.aguilera.control.cliente;

import java.util.HashMap;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.aguilera.modelo.Disenio;
import com.aguilera.modelo.Parametro;
import com.aguilera.modelo.Pedido;
import com.aguilera.modelo.PedidoDisenio;
import com.aguilera.modeloDAO.ParametroDAO;
import com.aguilera.modeloDAO.PedidoDAO;
import com.aguilera.util.Constantes;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public class PedidoEdita {

	private final static String OBJETO_PEDIDO = "pedido";
	private final static String VENTANA_DISENIO_VER = "/cliente/disenioVer.zul";
	
	public final static String PARAMETRO_DISENIO = "disenio";
	
	private PedidoDAO pedidoDAO = new PedidoDAO();
	private ParametroDAO parametroDAO = new ParametroDAO();
	
	@Getter @Setter private Pedido pedido;
	private Pedido pedidoLista;
	private Parametro recargo;
	
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
		}
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		recargo = parametroDAO.buscarPorNombre(Constantes.PARAMETRO_RECARGO);
	}
	
	@Command
	public void ver(@BindingParam("disenio") Disenio disenio) {
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(PARAMETRO_DISENIO, disenio);
		Window ventanaCargar = (Window) Executions.createComponents(VENTANA_DISENIO_VER, null, parametros);
		ventanaCargar.doModal(); 		
	}
	
	@Command
	@NotifyChange({OBJETO_PEDIDO})
	public void aprobar(@BindingParam("disenio") PedidoDisenio pedidoDisenio) {
		pedidoDisenio.setEstadoDisenio(Constantes.ESTADO_PEDIDO_D_APROBADO);
		Clients.showNotification("Diseño aprobado exitosamente", "info", null, "top_center", 0);
		return;
	}
	
	@Command
	@NotifyChange({OBJETO_PEDIDO})
	public void rechazar(@BindingParam("disenio") PedidoDisenio pedidoDisenio) {
		Clients.showNotification("Diseño rechazado exitosamente, ingrese la crítica", 
				"warning", null, "top_center", 0);
		pedidoDisenio.setEstadoDisenio(Constantes.ESTADO_PEDIDO_D_DEVUELTO);
	}
	
	@Command
	public void aceptar(@ContextParam(ContextType.VIEW) Component view){
		
		int contAprobadoAux = 0;
		int contRechazadoAux = 0;
		int contCantidad = 0;
		int contCritica = 0;
		for(PedidoDisenio objeto : pedido.getPedidoDisenios()) {
			if(objeto.getEstadoDisenio().equals(Constantes.ESTADO_PEDIDO_D_APROBADO)) {
				contAprobadoAux++;
				if(objeto.getCantidad() > 0) {
					contCantidad++;
				}
			}else if(objeto.getEstadoDisenio().equals(Constantes.ESTADO_PEDIDO_D_DEVUELTO)) {
				contRechazadoAux++;
				if(objeto.getCritica() != null) {
					contCritica++;
				}
			}
		}
		
		if((contAprobadoAux + contRechazadoAux) != pedido.getPedidoDisenios().size()) {
			Clients.showNotification("Debe evaluar todos los diseños!", 
					"error", null, "top_center", 0);
			return;
		}
		
		if(contRechazadoAux != contCritica) {
			Clients.showNotification("Debe ingresar la crítica de los diseños rechazados!", 
					"error", null, "top_center", 0);
			return;
		}
		
		if(contAprobadoAux != contCantidad) {
			Clients.showNotification("Debe ingresar la cantidad de los diseños aprobados!", 
					"error", null, "top_center", 0);
			return;
		}
				
		Messagebox.show("Desea guardar la evaluación de este pedido?", "Confirmación", 
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL}, 
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {

							int contAprobado = 0;
							int contRechazado = 0;
							for(PedidoDisenio objeto : pedido.getPedidoDisenios()) {
								if(objeto.getEstadoDisenio().equals(Constantes.ESTADO_PEDIDO_D_APROBADO)) {
									objeto.setCritica(null);
									contAprobado++;
								}else if(objeto.getEstadoDisenio().equals(Constantes.ESTADO_PEDIDO_D_DEVUELTO)) {
									contRechazado++;
								}
							}
							
							if((contAprobado) == pedido.getPedidoDisenios().size()) {
								pedido.setEstadoPedido(Constantes.ESTADO_PEDIDO_APROBADO);
							}
							
							if(contRechazado > 0) {
								pedido.setEstadoPedido(Constantes.ESTADO_PEDIDO_DEVUELTO);
								pedido.setCorrecciones(pedido.getCorrecciones() + 1);
							}
							
							if(pedido.getCorrecciones() > Constantes.MAX_CAMBIOS && 
									pedido.getEstadoPedido().equals(Constantes.ESTADO_PEDIDO_DEVUELTO)) {
								pedido.setRecargo(pedido.getRecargo() + recargo.getValor());
							}
							
							pedidoDAO.saveOrUpdate(pedido);
							pedidoLista = pedido;
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
