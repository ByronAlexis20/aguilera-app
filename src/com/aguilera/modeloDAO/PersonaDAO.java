package com.aguilera.modeloDAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.aguilera.modelo.Persona;

@SuppressWarnings("unchecked")
public class PersonaDAO extends ClaseDAO{

	public boolean esIdentificacionRepetida(String identificacion, int idPersona){
		List<Persona> retorno = new ArrayList<Persona>();
		try {
			Query query = getEntityManager().createNamedQuery("Persona.validarCedula");
					query.setParameter("identificacion", identificacion);
					query.setParameter("idPersona", idPersona);
					query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			retorno = (List<Persona>) query.getResultList();
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
