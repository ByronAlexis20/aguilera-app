package com.aguilera.modeloDAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CacheStoreMode;
import javax.persistence.Query;

import org.eclipse.persistence.config.QueryHints;

import com.aguilera.modelo.Parametro;

@SuppressWarnings("unchecked")
public class ParametroDAO extends ClaseDAO{
	
	public List<Parametro> findAll() {
		List<Parametro> retorno = new ArrayList<Parametro>(); 
		Query consulta;		
		try {
			consulta = getEntityManager().createNamedQuery("Parametro.findAll");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			
			retorno = (List<Parametro>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
	
	public Parametro buscarPorNombre(String nombre) {
		Parametro parametro; 
		Query consulta;
		
		try {
			consulta = getEntityManager().createNamedQuery("Parametro.buscarPorNombre");
			consulta.setParameter("nombre", nombre);
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			
			parametro = (Parametro) consulta.getSingleResult();
			
			return parametro;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
