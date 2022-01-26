package com.aguilera.modeloDAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CacheStoreMode;
import javax.persistence.Query;

import org.eclipse.persistence.config.QueryHints;

import com.aguilera.modelo.Usuario;

@SuppressWarnings("unchecked")
public class UsuarioDAO extends ClaseDAO{

	public List<Usuario> findAll() {
		List<Usuario> retorno = new ArrayList<Usuario>(); 
		Query consulta;		
		try {
			consulta = getEntityManager().createNamedQuery("Usuario.findAll");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			
			retorno = (List<Usuario>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
	
	public List<Usuario> buscarPorTexto(String texto){
		List<Usuario> retorno = new ArrayList<Usuario>(); 
		Query consulta;
		String patron = texto;

		if (texto == null || texto.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron + "%";
		}
		
		try {
			consulta = getEntityManager().createNamedQuery("Usuario.buscarPorTexto");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			consulta.setParameter("texto", patron);
			consulta.setMaxResults(10);
			retorno = (List<Usuario>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
	
	public List<Usuario> buscarPorTextoRol(String texto, String rol){
		List<Usuario> retorno = new ArrayList<Usuario>(); 
		Query consulta;
		String patron = texto;

		if (texto == null || texto.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron + "%";
		}
		
		try {
			consulta = getEntityManager().createNamedQuery("Usuario.buscarPorTextoRol");
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			consulta.setParameter("texto", patron);
			consulta.setParameter("rol", rol);
			consulta.setMaxResults(10);
			retorno = (List<Usuario>) consulta.getResultList();
			return retorno;
		} catch (Exception e) {
			e.printStackTrace();
			return retorno;
		}
	}
	
	public Usuario buscarUsuario(String nombreUsuario) {
		Usuario usuario; 
		Query consulta;
		
		try {
			consulta = getEntityManager().createNamedQuery("Usuario.buscarUsuario");
			consulta.setParameter("nombreUsuario", nombreUsuario);
			consulta.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			
			usuario = (Usuario) consulta.getSingleResult();
			
			return usuario;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean esRepetido(String usuario, int idUsuario){
		List<Usuario> retorno = new ArrayList<Usuario>();
		try {
			Query query = getEntityManager().createNamedQuery("Usuario.validarUsuario");
					query.setParameter("nombreUsuario", usuario);
					query.setParameter("idUsuario", idUsuario);
					query.setHint(QueryHints.CACHE_STORE_MODE, CacheStoreMode.REFRESH);
			retorno = (List<Usuario>) query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(retorno.size() > 0) {
			return true;
		}else {
			return false;
		}
	}
}
