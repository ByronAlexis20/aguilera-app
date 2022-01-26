package com.aguilera.control.operario;

import java.util.ArrayList;
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

import com.aguilera.modelo.Pedido;
import com.aguilera.modeloDAO.PedidoDAO;

import lombok.Getter;
import lombok.Setter;

public class PedidoLista {

	public static final String COMANDO_BUSCAR = "PedidoLista.buscar";
	private static final String LISTA_OBJETOS = "pedidos";
	private final static String VENTANA_PEDIDO_VER = "/operario/pedidoVer.zul";
	private final static String VENTANA_NOTA_VER = "/operario/notaVer.zul";
	private final static String VENTANA_PEDIDO_PRODUCTO = "/operario/pedidoProductoEdita.zul";
	
	public final static String PARAMETRO_PEDIDO = "pedido";
	
	private PedidoDAO pedidoDAO = new PedidoDAO();
	
	@Getter @Setter private String estadoBuscar;

	@Getter @Setter private List<Pedido> pedidos;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		pedidos = new ArrayList<Pedido>();
		estadoBuscar = null;
	}
	
	@Command
	@GlobalCommand(COMANDO_BUSCAR)
	@NotifyChange({LISTA_OBJETOS})
	public void buscar() {
		
		if (pedidos != null) {
			pedidos = null;
		}
		pedidos = pedidoDAO.buscarConFiltros(0, estadoBuscar);
	}
	
	@Command
	public void ver(@BindingParam("pedido") Pedido pedido) {
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(PARAMETRO_PEDIDO, pedido);
		Window ventanaCargar = (Window) Executions.createComponents(VENTANA_PEDIDO_VER, null, parametros);
		ventanaCargar.doModal();
	}
	
	@Command
	public void finalizar(@BindingParam("pedido") Pedido pedido) {
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(PARAMETRO_PEDIDO, pedido);
		Window ventanaCargar = (Window) Executions.createComponents(VENTANA_NOTA_VER, null, parametros);
		ventanaCargar.doModal();
	}
	
	@Command
	public void seleccionarMateriales(@BindingParam("pedido") Pedido pedido) {
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(PARAMETRO_PEDIDO, pedido);
		Window ventanaCargar = (Window) Executions.createComponents(VENTANA_PEDIDO_PRODUCTO, null, parametros);
		ventanaCargar.doModal();
	}
}
