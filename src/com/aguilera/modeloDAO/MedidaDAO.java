package com.aguilera.modeloDAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CacheStoreMode;
import javax.persistence.Query;

import org.eclipse.persistence.config.QueryHints;

import com.aguilera.modelo.Medida;

@SuppressWarnings("unchecked")
public class MedidaDAO extends ClaseDAO {
	
	public List<Medida> buscarPorPedido(int idPedido){
		List<Medida> retorno = new ArrayList<Medida>(); 
		Query consulta;

		try {
			consulta = getEntityManager().createNamedQuery("Medida.findByPedido");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			consulta.setParameter("idPedido", idPedido);
			retorno = (List<Medida>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}

}
