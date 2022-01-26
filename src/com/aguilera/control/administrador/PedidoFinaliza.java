package com.aguilera.control.administrador;

import java.util.ArrayList;
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

import com.aguilera.modelo.Cita;
import com.aguilera.modelo.Medida;
import com.aguilera.modelo.Pedido;
import com.aguilera.modeloDAO.CitaDAO;
import com.aguilera.modeloDAO.PedidoDAO;
import com.aguilera.util.Constantes;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PedidoFinaliza {
	
	private static final String ID_OBJETO_EDICION = "idEnEdicion";
	private static final String OBJETO_EDICION = "medida";
	private static final String LISTA_OBJETOS = "pedido";
	private static final String COMANDO_CARGAR_MEDIDAS = "PedidoFinaliza.cargarMedidas";
	
	@Wire private Listbox lstDatos;

	private PedidoDAO pedidoDAO = new PedidoDAO();
	private CitaDAO citaDAO = new CitaDAO();
	
	@Getter @Setter private Pedido pedido;
	@Getter @Setter private int idEnEdicion;
	@Getter @Setter private Medida medida;
	private Cita citaLista;
	
	@Init
	public void init(@ExecutionArgParam(CitaLista.PARAMETRO_CITA) Cita cita) {
		citaLista = cita;
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
	
	public List<Pedido> getPedidos() {
		return pedidoDAO.buscarConFiltros(citaLista.getCliente().getId(), Constantes.ESTADO_PEDIDO_APROBADO);
	}
	
	@Command
	@GlobalCommand(COMANDO_CARGAR_MEDIDAS)
	@NotifyChange({LISTA_OBJETOS, OBJETO_EDICION, ID_OBJETO_EDICION})
	public void cargarMedidas() {
		if (pedido.getMedidas() == null) {
			pedido.setMedidas(new ArrayList<Medida>());
		}
		try {
			pedidoDAO.refresh(pedido);
		} catch (Exception e) {
			e.printStackTrace();
		}
		medida = null;
		idEnEdicion = 0;
	}
	
	@Command
	@NotifyChange({LISTA_OBJETOS, ID_OBJETO_EDICION, OBJETO_EDICION})
	public void nuevaMedida() {
		if (this.medida != null && this.medida.getId() == 0 && idEnEdicion == 0) {
			cancelarMedida();
		}
		if (this.medida == null || this.medida.getId() != 0) {
			this.medida  = new Medida();
			pedido.getMedidas().add((lstDatos. getActivePage() * lstDatos.getPageSize()), this.medida);
			idEnEdicion = this.medida.getId();
		}
	}
	
	@Command
	@NotifyChange({OBJETO_EDICION, ID_OBJETO_EDICION})
	public void editarMedida(@BindingParam(OBJETO_EDICION) Medida medida) {
		if (medida.getId() != 0) {
			this.medida = citaDAO.getEntityManager().find(Medida.class, medida.getId());
		}else{
			this.medida = medida;
		}
		
		idEnEdicion = medida.getId();
	}
	
	@Command
	@NotifyChange({OBJETO_EDICION, ID_OBJETO_EDICION})
	public void aceptarMedida(@ContextParam(ContextType.COMPONENT) Component component, 
			@ContextParam(ContextType.VIEW) Component view) {
		this.medida.setPedido(this.pedido);
		if(medida.getId() != 0) {
			for(Medida objeto : pedido.getMedidas()) {
				if (objeto.getId() == medida.getId()) {
					objeto.setPersona(medida.getPersona());
					objeto.setSexo(medida.getSexo());
					objeto.setCantidad(medida.getCantidad());
					objeto.setTallaCamisa(medida.getTallaCamisa());
					objeto.setTallaPantalon(medida.getTallaPantalon());
					objeto.setPecho(medida.getPecho());
					objeto.setTorax(medida.getTorax());
					objeto.setCintura(medida.getCintura());
					objeto.setContornoCadera(medida.getContornoCadera());
					objeto.setHombro(medida.getHombro());
					objeto.setEncuentro(medida.getEncuentro());
					objeto.setCuello(medida.getCuello());
					objeto.setAnchoEspalda(medida.getAnchoEspalda());
					objeto.setMuneca(medida.getMuneca());
					objeto.setAlturaCadera(medida.getAlturaCadera());
					objeto.setTiro(medida.getTiro());
					objeto.setRodilla(medida.getRodilla());
					objeto.setPierna(medida.getPierna());
					objeto.setCodo(medida.getCodo());
					objeto.setEntrePierna(medida.getEntrePierna());
					objeto.setBrazo(medida.getBrazo());
				}
			}
		}
		
		Messagebox.show("Desea registrar esta medida?", "Confirmación", 
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL}, 
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							pedidoDAO.saveOrUpdate(pedido);
							BindUtils.postGlobalCommand(null, null, COMANDO_CARGAR_MEDIDAS, null);
				        }
					}
				});
	}
	
	@Command
	@NotifyChange({OBJETO_EDICION, ID_OBJETO_EDICION, LISTA_OBJETOS})
	public void cancelarMedida() {
		if (idEnEdicion == 0) {
			pedido.getMedidas().remove(medida);
		}
		idEnEdicion = 0;
		medida = null;
	}
	
	@Command
	@NotifyChange({LISTA_OBJETOS})
	public void eliminarMedida(@BindingParam(OBJETO_EDICION) Medida medida) {
		
		Messagebox.show("Desea descartar esta cita?", "Confirmación",
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL},
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							
							for (Medida objeto : pedido.getMedidas()) {
								if (objeto.getId() == medida.getId()) {
									objeto.setEstado("X");
								}
							}
							pedidoDAO.saveOrUpdate(pedido);
				        	BindUtils.postGlobalCommand(null, null, COMANDO_CARGAR_MEDIDAS, null);
				        }
					}
				});
	}
	
	@Command
	public void aceptar(@ContextParam(ContextType.VIEW) Component view){
				
		if(pedido.getDetalle() == null) {
			Clients.showNotification("Debe escribir los detalles para los operarios!", 
					"error", null, "top_center", 0);
			return;
		}
		
		Messagebox.show("Desea actualizar este pedido?", "Confirmación", 
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL}, 
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							if(citaLista != null) {
								citaLista.setEstadoCita(Constantes.ESTADO_CITA_EJECUTADA);
								citaDAO.saveOrUpdate(citaLista);
							}
							pedido.setEstadoPedido(Constantes.ESTADO_PEDIDO_FINALIZADO);
							pedidoDAO.saveOrUpdate(pedido);
				        	BindUtils.postGlobalCommand(null, null, CitaLista.COMANDO_BUSCAR, null);
				        	salir(view);
				        }else if (event.getName().equals(Events.ON_CANCEL)) {
				        	BindUtils.postGlobalCommand(null, null, CitaLista.COMANDO_BUSCAR, null);
				        }
					}
				});
	}	

	@Command
	public void salir(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		pedido = null;
		BindUtils.postGlobalCommand(null, null, CitaLista.COMANDO_BUSCAR, null);
		view.detach();
	}
}
