package com.aguilera.modeloDAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CacheStoreMode;
import javax.persistence.Query;

import org.eclipse.persistence.config.QueryHints;

import com.aguilera.modelo.Producto;

@SuppressWarnings("unchecked")
public class ProductoDAO extends ClaseDAO{

	public List<Producto> buscarPorTexto(String texto){
		List<Producto> retorno = new ArrayList<Producto>(); 
		Query consulta;
		String patron = texto;

		if (texto == null || texto.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron + "%";
		}
		
		try {
			consulta = getEntityManager().createNamedQuery("Producto.buscarPorTexto");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			consulta.setParameter("texto", patron);
			retorno = (List<Producto>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
	
	public List<Producto> buscarConFiltros(String texto, String tipo) {
		List<Producto> retorno; 
		
		String jpql = "SELECT p FROM Producto p WHERE p.estado is NULL ";
		
		if(tipo != null) {
			jpql += "AND p.tipoProducto = :tipo ";
		}
		
		if (texto != null && texto.length() > 0) {
			jpql += "AND p.nombre LIKE :patron ";
		}

		Query consulta;
		consulta = getEntityManager().createQuery(jpql);
		
		if(tipo != null) {
			consulta.setParameter("tipo", tipo);
		}
		
		if (texto != null && texto.length() > 0) {
			String patron = "%" + texto + "%";
			consulta.setParameter("patron", patron);
		}

		consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);

		retorno = (List<Producto>) consulta.getResultList();

		return retorno;
	}
}
