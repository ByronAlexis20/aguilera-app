package com.aguilera.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the da_persona database table.
 * 
 */
@Entity
@Table(name="da_persona")
@NamedQueries({
	@NamedQuery(name="Persona.findAll", query="SELECT p FROM Persona p"),
	@NamedQuery(name="Persona.validarCedula", query="SELECT p FROM Persona p WHERE "
			+ "p.identificacion = :identificacion AND p.id <> :idPersona")
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class Persona implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Getter @Setter private String apellidos;

	@Getter @Setter private String correo;

	@Getter @Setter private String direccion;

	@Getter @Setter private String estado;

	@Getter @Setter private String identificacion;

	@Getter @Setter private String nombres;

	@Getter @Setter private String telefono;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="persona")
	@Getter @Setter private List<Usuario> usuarios;

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setPersona(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setPersona(null);

		return usuario;
	}
	
	public String getNombreCompleto() {
		String nombre = "";
		if(nombres != null) {
			nombre = nombre + nombres;
		}
		if(apellidos != null) {
			nombre = nombre + " " + apellidos;
		}
		if(nombre.isEmpty()) {
			return "No registrado";
		}else {
			return nombre;
		}
	}

}