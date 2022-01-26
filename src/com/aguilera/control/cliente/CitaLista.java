package com.aguilera.control.cliente;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
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
import com.aguilera.modelo.Usuario;
import com.aguilera.modeloDAO.CitaDAO;
import com.aguilera.modeloDAO.PedidoDAO;
import com.aguilera.modeloDAO.UsuarioDAO;
import com.aguilera.util.Constantes;
import com.aguilera.util.SecurityUtil;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"rawtypes", "unchecked"})
public class CitaLista {

	private static final String COMANDO_BUSCAR = "CitaLista.buscar";
	private static final String ID_OBJETO_EDICION = "idEnEdicion";
	private static final String OBJETO_EDICION = "cita";
	private static final String LISTA_OBJETOS = "citas";
	private static final String LISTA_CITAS_FECHA = "citasPorFecha";
	
	@Wire private Listbox lstDatos;
	
	private CitaDAO citaDAO = new CitaDAO();
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	private PedidoDAO pedidoDAO = new PedidoDAO();
	
	@Getter @Setter private Date fechaBuscar;
	@Getter @Setter private String tipoBuscar;
	@Getter @Setter private String estadoBuscar;

	@Getter @Setter private List<Cita> citas;
	@Getter @Setter private List<Cita> citasPorFecha;
	@Getter @Setter private int idEnEdicion;
	@Getter @Setter private Cita cita;
	@Getter @Setter private Usuario usuario;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		citas = new ArrayList<Cita>();
		citasPorFecha = new ArrayList<Cita>();
		fechaBuscar = null;
		tipoBuscar = null;
		estadoBuscar = null;
		usuario = usuarioDAO.buscarUsuario(SecurityUtil.getUser().getUsername());
	}
	
	@Command
	@GlobalCommand(COMANDO_BUSCAR)
	@NotifyChange({LISTA_OBJETOS, OBJETO_EDICION, ID_OBJETO_EDICION})
	public void buscar() {
		
		/*if(fechaBuscar == null || tipoBuscar == null || estadoBuscar == null) {
			Clients.showNotification("Debe seleccionar una fecha, estado y tipo!", "error", null, "top_center", 0);
			return;
		}*/
		
		if (citas != null) {
			citas = null;
		}
		citas = citaDAO.buscarConFiltros(usuario.getId(), fechaBuscar, tipoBuscar, estadoBuscar);	
		idEnEdicion = 0;
		cita = null;
		citasPorFecha = null;
	}
	
	@Command
	@NotifyChange({LISTA_OBJETOS, ID_OBJETO_EDICION})
	public void nuevo() {
		if (this.cita != null && this.cita.getId() == 0 && idEnEdicion == 0) {
			cancelar();
		}
		if (this.cita == null || this.cita.getId() != 0) {
			this.cita  = new Cita();
			citas.add((lstDatos. getActivePage() * lstDatos.getPageSize()), this.cita);
			idEnEdicion = this.cita.getId();
		}
	}
	
	@Command
	@NotifyChange({OBJETO_EDICION, ID_OBJETO_EDICION})
	public void editar(@BindingParam(OBJETO_EDICION) Cita cita) {
		if (cita.getId() != 0) {
			this.cita = citaDAO.getEntityManager().find(Cita.class, cita.getId());
		}else{
			this.cita = cita;
		}
		
		idEnEdicion = cita.getId();
	}
	
	@Command
	@NotifyChange({LISTA_CITAS_FECHA})
	public void cargarCitasPorFecha() {
		if (citasPorFecha != null) {
			citasPorFecha = null;
		}
		
		citasPorFecha = citaDAO.buscarConFiltros(0, cita.getFecha(), null, null);
	}
	
	@Command
	@NotifyChange({OBJETO_EDICION, ID_OBJETO_EDICION})
	public void aceptar(@ContextParam(ContextType.COMPONENT) Component component, 
			@ContextParam(ContextType.VIEW) Component view) {
		
		if (cita.getFecha() == null || cita.getHoraInicio() == null || cita.getTipo() == null) {
			Clients.showNotification("Debe ingresar todos los datos!", "error", null, "top_center", 0);
			return;
		}
		
		if (citaDAO.esRepetida(cita.getFecha(), cita.getHoraInicio(), cita.getHoraFin(), cita.getId())) {
			Clients.showNotification("Ya existe una cita con esa fecha y hora!", "error", null, "top_center", 0);
			return;
		}
		
		if(cita.getTipo().equals(Constantes.TIPO_CITA_TOMA_MEDIDAS)) {
			if(pedidoDAO.buscarConFiltros(usuario.getId(), Constantes.ESTADO_PEDIDO_APROBADO).isEmpty()) {
				Clients.showNotification("Debe tener al menos 1 pedido aprobado para agendar una cita de toma de medidas!",
						"error", null, "top_center", 0);
				return;
			}
		}
		
		Messagebox.show("Desea registrar esta cita?", "Confirmación", 
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL}, 
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							cita.setEstadoCita(Constantes.ESTADO_CITA_PENDIENTE);
							cita.setCliente(usuario);
							citaDAO.saveOrUpdate(cita);
							BindUtils.postGlobalCommand(null, null, COMANDO_BUSCAR, null);
				        }
					}
				});
	}
	
	@Command
	@NotifyChange({OBJETO_EDICION, ID_OBJETO_EDICION})
	public void cancelar() {
		if (idEnEdicion == 0) {
			citas.remove(cita);
		}
		idEnEdicion = 0;
		cita = null;
	}
	
	@Command
	@NotifyChange({OBJETO_EDICION})
	public void calcularHoraFin() {
		if(cita.getHoraInicio() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(cita.getHoraInicio());
			cal.add(Calendar.HOUR, 2);
			cita.setHoraFin(cal.getTime());
		}
	}

	@Command
	@NotifyChange({ID_OBJETO_EDICION})
	public void eliminar(@BindingParam(OBJETO_EDICION) Cita cita) {
		
		Messagebox.show("Desea eliminar esta cita?", "Confirmación",
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL},
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							cita.setEstado("X");
							citaDAO.saveOrUpdate(cita);
				        	BindUtils.postGlobalCommand(null, null, COMANDO_BUSCAR, null);
				        }
					}
				});
	}
}
