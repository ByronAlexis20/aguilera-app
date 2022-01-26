package com.aguilera.control.operario;

import java.util.HashMap;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Window;

import com.aguilera.modelo.Disenio;
import com.aguilera.modelo.Pedido;

import lombok.Getter;
import lombok.Setter;

public class PedidoVer {

	private final static String VENTANA_DISENIO_VER = "/operario/disenioVer.zul";
	
	public final static String PARAMETRO_DISENIO = "disenio";
	
	@Getter @Setter private Pedido pedido;
	
	@Init
	public void init(@ExecutionArgParam(PedidoLista.PARAMETRO_PEDIDO) Pedido pedido) {
		this.pedido = pedido;
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
	
	@Command
	public void ver(@BindingParam("disenio") Disenio disenio) {
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		parametros.put(PARAMETRO_DISENIO, disenio);
		Window ventanaCargar = (Window) Executions.createComponents(VENTANA_DISENIO_VER, null, parametros);
		ventanaCargar.doModal(); 		
	}

	@Command
	public void salir(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		view.detach();
	}
}
