package com.aguilera.modeloDAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CacheStoreMode;
import javax.persistence.Query;

import org.eclipse.persistence.config.QueryHints;

import com.aguilera.modelo.Privilegio;

@SuppressWarnings("unchecked")
public class PrivilegioDAO extends ClaseDAO{

	public List<Privilegio> findAll() {
		List<Privilegio> retorno = new ArrayList<Privilegio>(); 
		Query consulta;		
		
		try {
			consulta = getEntityManager().createNamedQuery("Privilegio.findAll");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			
			retorno = (List<Privilegio>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}

	public List<Privilegio> buscarNoSeleccionadosPorOpcion(int idOpcion) {
		List<Privilegio> retorno = new ArrayList<Privilegio>(); 
		Query consulta;	
		
		try {
			consulta = getEntityManager().createNamedQuery("Privilegio.buscarNoSeleccionadosPorOpcion");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			consulta.setParameter("id_opcion", idOpcion);
			
			retorno = (List<Privilegio>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
	
	public List<Privilegio> buscarNoSeleccionadosPorUsuario(int idUsuario) {
		List<Privilegio> retorno = new ArrayList<Privilegio>(); 
		Query consulta;	
		
		try {
			consulta = getEntityManager().createNamedQuery("Privilegio.buscarNoSeleccionadosPorUsuario");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			consulta.setParameter("id_usuario", idUsuario);
			
			retorno = (List<Privilegio>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
	
	public List<Privilegio> buscarPorTexto(String texto) {
		List<Privilegio> retorno = new ArrayList<Privilegio>(); 
		Query consulta;
		String patron = texto;

		if (texto == null || texto.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron + "%";
		}
		
		try {
			consulta = getEntityManager().createNamedQuery("Privilegio.buscarPorTexto");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			consulta.setParameter("texto", patron);
			
			retorno = (List<Privilegio>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
	
	public Privilegio buscarPorRol(String rol) {
		Privilegio retorno = null; 
		Query consulta;
		String patron;

		if (rol == null || rol.length() == 0) {
			patron = "";
		}else{
			patron = rol;
		}
		
		try {
			consulta = getEntityManager().createNamedQuery("Privilegio.buscarPorRol");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			consulta.setParameter("rol", patron);
			
			retorno = (Privilegio) consulta.getSingleResult();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
}
