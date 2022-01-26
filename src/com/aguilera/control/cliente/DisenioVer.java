package com.aguilera.control.cliente;

import java.io.IOException;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;

import com.aguilera.control.diseniador.DisenioLista;
import com.aguilera.modelo.Disenio;

import lombok.Getter;
import lombok.Setter;

public class DisenioVer {

	private final static String OBJETO_ANTERIOR = "tieneAnterior";
	private final static String OBJETO_SIGUIENTE = "tieneSiguiente";
	private final static String OBJETO_IMAGEN = "imagenActual";
	
	public final static String COMANDO_HABILITAR_BOTONES = "DisenioVer.habilitarBotones";
	
	@Getter @Setter private Disenio disenio;
	@Getter @Setter private AImage imagenActual;
	@Getter @Setter private boolean tieneAnterior;
	@Getter @Setter private boolean tieneSiguiente;
	private int cantidadFotos;
	private int posicionActual;
	
	@Init
	public void init(@ExecutionArgParam(DisenioLista.PARAMETRO_DISENIO) Disenio disenio) throws IOException {
		this.disenio = disenio;
		if(!this.disenio.getDisenioArchivos().isEmpty()) {
			cantidadFotos = this.disenio.getDisenioArchivos().size();
			posicionActual = 1;
			tieneAnterior = false;
			tieneSiguiente = false;
			imagenActual = this.disenio.getDisenioArchivos().get(posicionActual - 1).getArchivo().getImage();
		}
		
		habilitarBotones();
	}
	
	@Command
	@GlobalCommand(COMANDO_HABILITAR_BOTONES)
	@NotifyChange({OBJETO_ANTERIOR, OBJETO_SIGUIENTE})
	public void habilitarBotones() {
		int inicio = 1;
		int fin = cantidadFotos;
		
		if (posicionActual == inicio && inicio != fin) {
			tieneAnterior = false;
			tieneSiguiente = true;
		}else if (posicionActual > inicio && posicionActual < fin) {
			tieneAnterior = true;
			tieneSiguiente = true;
		}else if (posicionActual == fin && inicio != fin) {
			tieneAnterior = true;
			tieneSiguiente = false;
		}else if (inicio == fin) {
			tieneAnterior = false;
			tieneSiguiente = false;
		}else {
			tieneAnterior = false;
			tieneSiguiente = false;
		}
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
	
	@Command
	@NotifyChange({OBJETO_IMAGEN})
	public void anterior() throws IOException {
		posicionActual = posicionActual - 1;
		BindUtils.postGlobalCommand(null, null, COMANDO_HABILITAR_BOTONES, null);
		imagenActual = this.disenio.getDisenioArchivos().get(posicionActual - 1).getArchivo().getImage();
	}
	
	@Command
	@NotifyChange({OBJETO_IMAGEN})
	public void siguiente() throws IOException {
		posicionActual = posicionActual + 1;
		BindUtils.postGlobalCommand(null, null, COMANDO_HABILITAR_BOTONES, null);
		imagenActual = this.disenio.getDisenioArchivos().get(posicionActual - 1).getArchivo().getImage();
	}
	
	@Command
	public void salir(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		disenio = null;
		BindUtils.postGlobalCommand(null, null, DisenioLista.COMANDO_BUSCAR, null);
		view.detach();
	}
}
