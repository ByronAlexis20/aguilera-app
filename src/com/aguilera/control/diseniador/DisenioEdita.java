package com.aguilera.control.diseniador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import com.aguilera.modelo.Archivo;
import com.aguilera.modelo.Disenio;
import com.aguilera.modelo.DisenioArchivo;
import com.aguilera.modelo.Usuario;
import com.aguilera.modeloDAO.ArchivoDAO;
import com.aguilera.modeloDAO.DisenioDAO;
import com.aguilera.modeloDAO.UsuarioDAO;
import com.aguilera.util.FileUtil;
import com.aguilera.util.SecurityUtil;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DisenioEdita {

	private final static String OBJETO_DISENIO = "disenio";
	private final static String LISTA_FOTOS_AGREGADAS = "fotosAgregadas";

	private DisenioDAO disenioDAO = new DisenioDAO();
	private ArchivoDAO archivoDAO = new ArchivoDAO();
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	
	@Getter @Setter private Disenio disenio;
	private Disenio disenioLista;
	private List<Disenio> listaRetorno;
	private String comandoEjecutar;
	@Getter @Setter private List<Archivo> fotosAgregadas = new ArrayList<Archivo>();
	private Usuario usuario;
	
	@Init
	public void init(@ExecutionArgParam(DisenioLista.PARAMETRO_DISENIO) Disenio disenio,
					 @ExecutionArgParam(PedidoEdita.PARAMETRO_LISTA) List<Disenio> listaRetorno,
					 @ExecutionArgParam(PedidoEdita.PARAMETRO_COMANDO) String comandoEjecutar) {
		this.listaRetorno = listaRetorno;
		this.comandoEjecutar = comandoEjecutar;
		disenioLista = disenio;
		if (disenio == null) {
			this.disenio = new Disenio();
		}else{
			
			if (disenio.getId() != 0) {
				this.disenio = disenioDAO.getEntityManager().find(Disenio.class, disenio.getId());
			}else {
				this.disenio = disenio;
			}
			
			if (this.disenio.getDisenioArchivos() != null) {
				for(DisenioArchivo objeto : this.disenio.getDisenioArchivos()) {
					fotosAgregadas.add(objeto.getArchivo());
				}
			}
		}
		
		usuario = usuarioDAO.buscarUsuario(SecurityUtil.getUser().getUsername());
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
	
	private void actualizarFotos() {
		if (disenio.getDisenioArchivos() == null) {
			disenio.setDisenioArchivos(new ArrayList<DisenioArchivo>());
		}
		for(DisenioArchivo objeto : disenio.getDisenioArchivos()) {
			int cont = 0;
			for (Archivo objeto2 : fotosAgregadas) {
				if (objeto.getArchivo().getId() == objeto2.getId()) {
					cont++;
				}
			}
			if(cont == 0) {
				objeto.setEstado("X");
			}
		}
		for(Archivo objeto2 : fotosAgregadas) {
			int cont = 0;
			for (DisenioArchivo objeto : disenio.getDisenioArchivos()) {
				if (objeto.getArchivo().getId() == objeto2.getId()) {
					cont++;
				}
			}
			if (cont == 0) {
				DisenioArchivo disenioArchivo = new DisenioArchivo();
				disenioArchivo.setDisenio(disenio);
				disenioArchivo.setArchivo(objeto2);
				disenio.getDisenioArchivos().add(disenioArchivo);
			}
		}
	}
	
	@Command
	@NotifyChange({LISTA_FOTOS_AGREGADAS, OBJETO_DISENIO})
    public void subirFoto(@ContextParam(ContextType.TRIGGER_EVENT) UploadEvent evento) throws IOException, Exception {
	     Archivo ad;
	     ad = FileUtil.cargaArchivo(evento.getMedia());
		 if (ad != null) {
			 archivoDAO.saveOrUpdate(ad);
			 fotosAgregadas.add(ad);
		 } 
	 }
	
	@Command
	@NotifyChange({LISTA_FOTOS_AGREGADAS, OBJETO_DISENIO})
	public void eliminar(@BindingParam("foto") Archivo foto) {
		fotosAgregadas.remove(foto);
	}
	
	@Command
	public void aceptar(@ContextParam(ContextType.VIEW) Component view){
				
		actualizarFotos();
		
		Messagebox.show("Desea guardar este disenio?", "Confirmación", 
				new Messagebox.Button[]{Messagebox.Button.OK,  Messagebox.Button.CANCEL}, 
				null,
				new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals(Events.ON_OK)) {
							
							if (disenio.getId() == 0) {
								disenio.setFechaRegistro(new Date());
								if (usuario.getPersona() != null) {
									disenio.setDiseniador(usuario.getPersona().getNombreCompleto());
								}
							}
							
							disenioDAO.saveOrUpdate(disenio);
							disenioLista = disenio;
				        	BindUtils.postGlobalCommand(null, null, DisenioLista.COMANDO_BUSCAR, null);
				        	if(listaRetorno != null) {
				        		int cont = 0;
				        		
				        		for(Disenio objeto : listaRetorno) {
				        			if(objeto.getId() == disenioLista.getId()) {
				        				objeto = null;
					        			objeto = disenioLista;
					        			cont++;
				        			}
				        		}
				        		
				        		if(cont == 0) {
				        			listaRetorno.add(disenioLista);
				        		}
				        		
				        		BindUtils.postGlobalCommand(null, null, comandoEjecutar, null);
				        	}
				        	salir(view);
				        }else if (event.getName().equals(Events.ON_CANCEL)) {
				        	BindUtils.postGlobalCommand(null, null, DisenioLista.COMANDO_BUSCAR, null);
				        }
					}
				});
	}	

	@Command
	public void salir(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		disenio = null;
		BindUtils.postGlobalCommand(null, null, DisenioLista.COMANDO_BUSCAR, null);
		view.detach();
	}
}
