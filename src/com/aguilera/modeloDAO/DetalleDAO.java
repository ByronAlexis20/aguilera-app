package com.aguilera.modeloDAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CacheStoreMode;
import javax.persistence.Query;

import org.eclipse.persistence.config.QueryHints;

import com.aguilera.modelo.Detalle;

@SuppressWarnings("unchecked")
public class DetalleDAO extends ClaseDAO {
	public List<Detalle> findAll() {
		List<Detalle> retorno = new ArrayList<Detalle>(); 
		Query consulta;		
		try {
			consulta = getEntityManager().createNamedQuery("Detalle.findAll");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			retorno = (List<Detalle>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
}
