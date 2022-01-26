package com.aguilera.modeloDAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CacheStoreMode;
import javax.persistence.Query;

import org.eclipse.persistence.config.QueryHints;

import com.aguilera.modelo.UsuarioPrivilegio;

@SuppressWarnings("unchecked")
public class UsuarioPrivilegioDAO extends ClaseDAO {

	public List<UsuarioPrivilegio> buscarRolPorUsuario(Integer idUsuario){
		List<UsuarioPrivilegio> retorno = new ArrayList<UsuarioPrivilegio>(); 
		Query consulta;
		try {
			consulta = getEntityManager().createNamedQuery("UsuarioPrivilegio.buscarPorUsuario");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			consulta.setParameter("idUsuario", idUsuario);
			consulta.setMaxResults(10);
			retorno = (List<UsuarioPrivilegio>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
	
}
