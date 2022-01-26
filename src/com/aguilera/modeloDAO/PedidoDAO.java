package com.aguilera.modeloDAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CacheStoreMode;
import javax.persistence.Query;

import org.eclipse.persistence.config.QueryHints;

import com.aguilera.modelo.Pedido;

@SuppressWarnings("unchecked")
public class PedidoDAO extends ClaseDAO{

	public List<Pedido> buscarConFiltros(int idCliente, String estado) {
		List<Pedido> retorno; 
		
		String jpql = "SELECT p FROM Pedido p WHERE p.estado is NULL ";
		
		if(estado != null) {
			jpql += "AND p.estadoPedido = :estado ";
		}
		
		if(idCliente != 0) {
			jpql += "AND p.cliente.id = :idCliente ";
		}

		Query consulta;
		consulta = getEntityManager().createQuery(jpql);
		
		if(estado != null) {
			consulta.setParameter("estado", estado);
		}
		
		if(idCliente != 0) {
			consulta.setParameter("idCliente", idCliente);
		}

		consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);

		retorno = (List<Pedido>) consulta.getResultList();

		return retorno;
	}
	
	public List<Pedido> buscarPorEstado(String estadoPedido){
		List<Pedido> retorno = new ArrayList<Pedido>(); 
		Query consulta;
				
		try {
			consulta = getEntityManager().createNamedQuery("Pedido.buscarPorEstado");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			consulta.setParameter("estadoPedido", estadoPedido);
			retorno = (List<Pedido>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
}
