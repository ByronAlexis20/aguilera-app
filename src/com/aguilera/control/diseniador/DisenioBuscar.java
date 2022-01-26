package com.aguilera.control.diseniador;

import java.util.List;

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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import com.aguilera.modelo.Disenio;
import com.aguilera.modeloDAO.DisenioDAO;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DisenioBuscar {

	private final static String LISTA_DISENIOS = "disenios";
	private final static String PARAMETRO_DISENIO = "disenio";
	
	private List<Disenio> listaRetorno;
	private String comandoEjecutar;

	@Getter @Setter private List<Disenio> disenios;
	@Getter @Setter private String textoBuscar;
	
	private DisenioDAO disenioDAO = new DisenioDAO();
	
	@Init
	public void init(@ExecutionArgParam(PedidoEdita.PARAMETRO_LISTA) List<Disenio> listaRetorno,
			 		 @ExecutionArgParam(PedidoEdita.PARAMETRO_COMANDO) String comandoEjecutar) {
		this.listaRetorno = listaRetorno;
		this.comandoEjecutar = comandoEjecutar;
	}
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception{
		Selectors.wireComponents(view, this, false);
		textoBuscar = "";
	}
	
	@Command
	@NotifyChange({LISTA_DISENIOS})
	public void buscar() throws Exception{
		if (disenios != null) {
			disenios = null;
		}
		
		disenios = disenioDAO.buscarPorTexto(textoBuscar);
	}
	
	@Command
	public void agregar(@ContextParam(ContextType.VIEW) Component view,
						@BindingParam(PARAMETRO_DISENIO) Disenio disenio) {
		
		Messagebox.show("Desea agregar este diseño al pedido?", "Confirmación", 
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL}, 
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
				        	listaRetorno.add(disenio);
			        		BindUtils.postGlobalCommand(null, null, comandoEjecutar, null);
				        	salir(view);
				        }else if (event.getName().equals(Events.ON_CANCEL)) {
				        	BindUtils.postGlobalCommand(null, null, comandoEjecutar, null);
				        }
					}
				});
	}
	
	@Command
	public void salir(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		view.detach();
	}
}
