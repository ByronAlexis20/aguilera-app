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
 * The persistent class for the da_privilegio database table.
 * 
 */
@Entity
@Table(name="da_privilegio")
@NamedQueries({
	@NamedQuery(name="Privilegio.findAll", query="SELECT p FROM Privilegio p"),
	@NamedQuery(name="Privilegio.buscarPorTexto", query="SELECT p FROM Privilegio p" + 
			" WHERE p.codigo LIKE :texto OR p.descripcion LIKE :texto" ),
	@NamedQuery(name="Privilegio.buscarNoSeleccionadosPorOpcion", query="SELECT p" + 
			" FROM Privilegio p WHERE NOT EXISTS (SELECT o FROM OpcionPrivilegio o" + 
			" WHERE o.opcion.id = :id_opcion AND o.privilegio = p)"),
	@NamedQuery(name="Privilegio.buscarNoSeleccionadosPorUsuario", query="SELECT p" + 
			" FROM Privilegio p WHERE NOT EXISTS (SELECT u FROM UsuarioPrivilegio u" + 
			" WHERE u.usuario.id = :id_usuario AND u.privilegio = p)"),
	@NamedQuery(name="Privilegio.buscarPorRol", query="SELECT p FROM Privilegio p" + 
			" WHERE p.codigo = :rol" )
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class Privilegio implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Getter @Setter private String codigo;

	@Getter @Setter private String descripcion;

	@Getter @Setter private String estado;

	//bi-directional many-to-one association to OpcionPrivilegio
	@OneToMany(mappedBy="privilegio")
	@Getter @Setter private List<OpcionPrivilegio> opcionPrivilegios;

	//bi-directional many-to-one association to UsuarioPrivilegio
	@OneToMany(mappedBy="privilegio")
	@Getter @Setter private List<UsuarioPrivilegio> usuarioPrivilegios;
	
	public OpcionPrivilegio addOpcionPrivilegio(OpcionPrivilegio opcionPrivilegio) {
		getOpcionPrivilegios().add(opcionPrivilegio);
		opcionPrivilegio.setPrivilegio(this);

		return opcionPrivilegio;
	}

	public OpcionPrivilegio removeOpcionPrivilegio(OpcionPrivilegio opcionPrivilegio) {
		getOpcionPrivilegios().remove(opcionPrivilegio);
		opcionPrivilegio.setPrivilegio(null);

		return opcionPrivilegio;
	}

	public UsuarioPrivilegio addUsuarioPrivilegio(UsuarioPrivilegio usuarioPrivilegio) {
		getUsuarioPrivilegios().add(usuarioPrivilegio);
		usuarioPrivilegio.setPrivilegio(this);

		return usuarioPrivilegio;
	}

	public UsuarioPrivilegio removeUsuarioPrivilegio(UsuarioPrivilegio usuarioPrivilegio) {
		getUsuarioPrivilegios().remove(usuarioPrivilegio);
		usuarioPrivilegio.setPrivilegio(null);

		return usuarioPrivilegio;
	}

}