package com.aguilera.modeloDAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CacheStoreMode;
import javax.persistence.Query;

import org.eclipse.persistence.config.QueryHints;

import com.aguilera.modelo.NotaCabecera;

@SuppressWarnings("unchecked")
public class NotaCabeceraDAO extends ClaseDAO{

	public List<NotaCabecera> findAll() {
		List<NotaCabecera> retorno = new ArrayList<NotaCabecera>(); 
		Query consulta;		
		try {
			consulta = getEntityManager().createNamedQuery("NotaCabecera.findAll");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			
			retorno = (List<NotaCabecera>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
	
}
