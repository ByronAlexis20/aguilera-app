package com.aguilera.modeloDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CacheStoreMode;
import javax.persistence.Query;

import org.eclipse.persistence.config.QueryHints;

import com.aguilera.modelo.Cita;

@SuppressWarnings("unchecked")
public class CitaDAO extends ClaseDAO{

	public List<Cita> findAll() {
		List<Cita> retorno = new ArrayList<Cita>(); 
		Query consulta;		
		try {
			consulta = getEntityManager().createNamedQuery("Cita.findAll");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			
			retorno = (List<Cita>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
	
	public List<Cita> buscarPorUsuarioFecha(int idUsuario, Date fecha){
		List<Cita> retorno = new ArrayList<Cita>(); 
		Query consulta;
		try {
			consulta = getEntityManager().createNamedQuery("Cita.buscarPorUsuarioFecha");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			consulta.setParameter("idCliente", idUsuario);
			consulta.setParameter("fecha", fecha);
			retorno = (List<Cita>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
	
	public List<Cita> buscarPorUsuarioFechaTipoEstado(int idUsuario, Date fecha, String tipo, String estado){
		List<Cita> retorno = new ArrayList<Cita>(); 
		Query consulta;
		try {
			consulta = getEntityManager().createNamedQuery("Cita.buscarPorUsuarioFechaTipoEstado");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			consulta.setParameter("idCliente", idUsuario);
			consulta.setParameter("fecha", fecha);
			consulta.setParameter("tipo", tipo);
			consulta.setParameter("estado", estado);
			retorno = (List<Cita>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
	
	public List<Cita> buscarPorFecha(Date fecha){
		List<Cita> retorno = new ArrayList<Cita>(); 
		Query consulta;
		try {
			consulta = getEntityManager().createNamedQuery("Cita.buscarPorFecha");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			consulta.setParameter("fecha", fecha);
			retorno = (List<Cita>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
	
	public boolean esRepetida(Date fecha, Date horaInicio, Date horaFin, int idCita){
		List<Cita> retorno = new ArrayList<Cita>();
		try {
			Query query = getEntityManager().createNamedQuery("Cita.validarRepetido");
					query.setParameter("fecha", fecha);
					query.setParameter("horaInicio", horaInicio);
					query.setParameter("horaFin", horaFin);
					query.setParameter("idCita", idCita);
					query.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			retorno = (List<Cita>) query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(retorno.size() > 0) {
			return true;
		}else {
			return false;
		}
	}
	
	public List<Cita> buscarConFiltros(int idUsuario, Date fecha, String tipo, String estado) {
		List<Cita> retorno; 
		
		String jpql = "SELECT c FROM Cita c WHERE c.estado is NULL ";
		
		if(idUsuario != 0) {
			jpql += "AND c.cliente.id = :idCliente ";
		}

		if(fecha != null) {
			jpql += "AND c.fecha = :fecha ";
		}

		if(tipo != null) {
			jpql += "AND c.tipo = :tipo ";
		}

		if(estado != null) {
			jpql += "AND c.estadoCita = :estado ";
		}

		Query consulta;
		consulta = getEntityManager().createQuery(jpql);
		
		if(idUsuario != 0) {
			consulta.setParameter("idCliente", idUsuario);
		}

		if(fecha != null) {
			consulta.setParameter("fecha", fecha);
		}

		if(tipo != null) {
			consulta.setParameter("tipo", tipo);
		}

		if(estado != null) {
			consulta.setParameter("estado", estado);
		}

		consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);

		retorno = (List<Cita>) consulta.getResultList();

		return retorno;
	}
	
	public String getHoraFormateada(Date fecha) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
		return dateFormatter.format(fecha);
	}
}
