package com.aguilera.modeloDAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CacheStoreMode;
import javax.persistence.Query;

import org.eclipse.persistence.config.QueryHints;

import com.aguilera.modelo.Cabecera;

@SuppressWarnings("unchecked")
public class CabeceraDAO extends ClaseDAO{
	public List<Cabecera> findAll() {
		List<Cabecera> retorno = new ArrayList<Cabecera>(); 
		Query consulta;		
		try {
			consulta = getEntityManager().createNamedQuery("Cabecera.findAll");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			
			retorno = (List<Cabecera>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
}
