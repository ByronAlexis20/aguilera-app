package com.aguilera.modeloDAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CacheStoreMode;
import javax.persistence.Query;

import org.eclipse.persistence.config.QueryHints;

import com.aguilera.modelo.Categoria;

@SuppressWarnings("unchecked")
public class CategoriaDAO extends ClaseDAO{
	
	public List<Categoria> findAll() {
		List<Categoria> retorno = new ArrayList<Categoria>(); 
		Query consulta;		
		try {
			consulta = getEntityManager().createNamedQuery("Categoria.findAll");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			
			retorno = (List<Categoria>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
}
