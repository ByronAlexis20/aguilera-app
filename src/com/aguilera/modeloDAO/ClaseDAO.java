package com.aguilera.modeloDAO;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.aguilera.modelo.DaEntity;

public abstract class ClaseDAO {
	
	// Crea una sola instancia de EntityManagerFactory para toda la applicacion.
	private static final String PERSISTENCE_UNIT_NAME = "aguilera-app";
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	
	// Objeto Entity Manager para cada instancia de un objeto que 
	// herede de esta clase.
	private EntityManager em;
	
	/**
	 * Retorna el Entity Mananager, si no existe lo crea.
	 * @return
	 */
	public EntityManager getEntityManager() {
		if (em == null){
	        em = emf.createEntityManager();
	    }
	    return em; 
	}
	
	/**
	 * Obtiene una conexion JDBC.
	 * @return
	 */
	public Connection abreConexion() {
		EntityManager entityManager; 
		entityManager = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
	    Connection connection = null;
	    entityManager.getTransaction().begin();
	    connection = entityManager.unwrap(Connection.class);
	    return connection;
	  }

	/**
	 * Cierra una conexion JDBC.
	 * @param cn
	 */
	public void cierraConexion(Connection cn) {
		 try {
			cn.close();
			cn = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void saveOrUpdate(DaEntity instancia) throws Exception {
		
		if (instancia == null) {
			throw new Exception("El objeto a grabar no puede ser nulo.");
		}
		
		try {
			getEntityManager().getTransaction().begin();
			if (instancia.getId() > 0) {
				instancia = getEntityManager().merge(instancia);
				getEntityManager().flush();
			}else{
				getEntityManager().persist(instancia);
				getEntityManager().flush();
				getEntityManager().refresh(instancia);
			}
			getEntityManager().getTransaction().commit();
		} catch (Exception e) {
			getEntityManager().getTransaction().rollback();
			throw e;
		}

	}
	
	public void refresh(DaEntity instancia) throws Exception {
		if (getEntityManager().contains(instancia)) {
			getEntityManager().refresh(instancia);
		}else{
			// Si el em no contiene el objeto, intenta reasociarlo.
			try {
				if (instancia.getId() > 0) {
					instancia = getEntityManager().find(instancia.getClass(), instancia.getId());
					getEntityManager().refresh(instancia);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
