package com.aguilera.control;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Window;

public class Login {
	
	private final static String VENTANA_REGISTRARSE = "/registrarse.zul";

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
	
	@Command
	public void registrarse() {
		Window ventanaCargar = (Window) Executions.createComponents(VENTANA_REGISTRARSE, null, null);
		ventanaCargar.doModal();
	}
}
