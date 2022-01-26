package com.aguilera.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import com.aguilera.util.Constantes;
import com.aguilera.util.SortOpcionByOrden;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the da_usuario database table.
 * 
 */
@Entity
@Table(name="da_usuario")
@NamedQueries({
	@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u"),
	@NamedQuery(name="Usuario.buscarPorTexto",query="SELECT u FROM Usuario u "
			+ "WHERE u.usuario LIKE :texto OR u.persona.identificacion like :texto "
			+ "OR u.persona.nombres LIKE :texto OR u.persona.apellidos LIKE :texto"),
	@NamedQuery(name="Usuario.buscarPorTextoRol",query="SELECT u FROM Usuario u "
			+ "WHERE (u.usuario LIKE :texto OR u.persona.identificacion like :texto "
			+ "OR u.persona.nombres LIKE :texto OR u.persona.apellidos LIKE :texto) AND "
			+ "EXISTS(SELECT p FROM UsuarioPrivilegio p WHERE p.usuario.id = u.id AND "
			+ "p.privilegio.codigo = :rol)"),
	@NamedQuery(name="Usuario.buscarUsuario",query="SELECT u FROM Usuario u WHERE u.usuario = :nombreUsuario "),
	@NamedQuery(name="Usuario.validarUsuario",query="SELECT u FROM Usuario u WHERE u.usuario = :nombreUsuario "
			+ "AND u.id <> :idUsuario")
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class Usuario implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Getter @Setter private String clave;

	@Getter @Setter private String estado;

	@Getter @Setter private String usuario;

	//bi-directional many-to-one association to Persona
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_persona")
	@Getter @Setter private Persona persona;

	//bi-directional many-to-one association to UsuarioPrivilegio
	@OneToMany(mappedBy="usuario", cascade=CascadeType.ALL)
	@Getter @Setter private List<UsuarioPrivilegio> usuarioPrivilegios;

	public UsuarioPrivilegio addUsuarioPrivilegio(UsuarioPrivilegio usuarioPrivilegio) {
		getUsuarioPrivilegios().add(usuarioPrivilegio);
		usuarioPrivilegio.setUsuario(this);

		return usuarioPrivilegio;
	}

	public UsuarioPrivilegio removeUsuarioPrivilegio(UsuarioPrivilegio usuarioPrivilegio) {
		getUsuarioPrivilegios().remove(usuarioPrivilegio);
		usuarioPrivilegio.setUsuario(null);

		return usuarioPrivilegio;
	}
	
	public List<Opcion> getOpciones() {
		
		List<Opcion> retorno = new ArrayList<Opcion>();
		
		if (getUsuarioPrivilegios() != null && !usuarioPrivilegios.isEmpty()) {

			List<Opcion> opcionesSinOrden = new ArrayList<Opcion>();
			
			for (UsuarioPrivilegio usuarioPrivilegio : usuarioPrivilegios) {
				if (usuarioPrivilegio.getPrivilegio().getOpcionPrivilegios() != null && 
					!usuarioPrivilegio.getPrivilegio().getOpcionPrivilegios().isEmpty()) {
					for (OpcionPrivilegio opcionPrivilegio : usuarioPrivilegio.getPrivilegio().getOpcionPrivilegios()) {
						if (opcionPrivilegio.getOpcion() != null && 
							!opcionesSinOrden.contains(opcionPrivilegio.getOpcion())) {
							opcionesSinOrden.add(opcionPrivilegio.getOpcion());
						}
					}
				}
			}
			
			for (Opcion opcion : opcionesSinOrden) {
				if (opcion.getOpcionPadre() == null) {
					opcion.getOpciones().clear();
					opcion.setOpciones(getOpcionesHijas(opcion, opcionesSinOrden));
					retorno.add(opcion);
				}
			}

			Collections.sort(retorno, new SortOpcionByOrden());
		}

		return retorno;
	}

	private List<Opcion> getOpcionesHijas(Opcion opcionPadre, List<Opcion> opciones) {
		List<Opcion> retorno = new ArrayList<Opcion>();
		
		for (Opcion opcion : opciones) {
			if (opcion.getOpcionPadre() != null && 
				opcion.getOpcionPadre().getId() == opcionPadre.getId()) {
				opcion.getOpciones().clear();
				opcion.setOpciones(getOpcionesHijas(opcion, opciones));
				retorno.add(opcion);
			}
		}
		
		Collections.sort(retorno, new SortOpcionByOrden());
		return retorno;
	}
	
	public boolean isCliente() {
		boolean retorno = false;
		if(usuarioPrivilegios != null) {
			for(UsuarioPrivilegio objeto : usuarioPrivilegios) {
				if (objeto.getPrivilegio().getCodigo().equals(Constantes.CLIENTE)){
					retorno = true;
				}
			}
		}
		return retorno;
	}

}