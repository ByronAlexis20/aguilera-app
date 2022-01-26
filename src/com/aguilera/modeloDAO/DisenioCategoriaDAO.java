package com.aguilera.modeloDAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CacheStoreMode;
import javax.persistence.Query;

import org.eclipse.persistence.config.QueryHints;

import com.aguilera.modelo.DisenioCategoria;

@SuppressWarnings("unchecked")
public class DisenioCategoriaDAO extends ClaseDAO {

	public List<DisenioCategoria> buscarPorCategoria(Integer idCat, Integer idDis) {
		List<DisenioCategoria> retorno = new ArrayList<DisenioCategoria>(); 
		Query consulta;		
		try {
			consulta = getEntityManager().createNamedQuery("DisenioCategoria.buscarPorCategoria");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			consulta.setParameter("idCat", idCat);
			consulta.setParameter("idDis", idDis);
			
			retorno = (List<DisenioCategoria>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
	
}
