package com.aguilera.modeloDAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CacheStoreMode;
import javax.persistence.Query;

import org.eclipse.persistence.config.QueryHints;

import com.aguilera.modelo.Categoria;
import com.aguilera.modelo.Disenio;

@SuppressWarnings("unchecked")
public class DisenioDAO extends ClaseDAO{

	public List<Disenio> buscarPorTexto(String texto){
		List<Disenio> retorno = new ArrayList<Disenio>(); 
		Query consulta;
		String patron = texto;

		if (texto == null || texto.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron + "%";
		}
		
		try {
			consulta = getEntityManager().createNamedQuery("Disenio.buscarPorTexto");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			consulta.setParameter("texto", patron);
			retorno = (List<Disenio>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
	
	public List<Disenio> buscarConFiltros(Categoria categoria, String texto) {
		List<Disenio> retorno; 
		
		String jpql = "SELECT d FROM Disenio d WHERE d.estado IS NULL ";
		
		String patron = texto;
		if (texto == null || texto.length() == 0) {
			patron = "%";
		}else {
			patron = "%" + patron + "%";
		}
		
		jpql += "AND d.nombre LIKE :texto ";

		if(categoria != null) {
			jpql += "AND EXISTS(SELECT c FROM DisenioCategoria c WHERE c.estado IS NULL AND c.categoria = :categoria) ";
		}

		Query consulta;
		consulta = getEntityManager().createQuery(jpql);
		
		consulta.setParameter("texto", patron);

		if(categoria != null) {
			consulta.setParameter("categoria", categoria);
		}

		consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);

		retorno = (List<Disenio>) consulta.getResultList();

		return retorno;
	}
}
