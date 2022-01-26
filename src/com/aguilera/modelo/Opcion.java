package com.aguilera.modelo;

import java.io.Serializable;
import java.util.List;

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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the da_opcion database table.
 * 
 */
@Entity
@Table(name="da_opcion")
@NamedQueries({
	@NamedQuery(name="Opcion.findAll", query="SELECT o FROM Opcion o")
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class Opcion implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Getter @Setter private String descripcion;

	@Getter @Setter private String estado;

	@Getter @Setter private String formulario;

	@Getter @Setter private String imagen;

	@Getter @Setter private int orden;

	@Getter @Setter private String titulo;

	//bi-directional many-to-one association to Opcion
	@ManyToOne
	@JoinColumn(name="id_opcion_padre")
	@Getter @Setter private Opcion opcionPadre;

	//bi-directional many-to-one association to Opcion
	@OneToMany(mappedBy="opcionPadre")
	@Getter @Setter private List<Opcion> opciones;

	//bi-directional many-to-one association to OpcionPrivilegio
	@OneToMany(mappedBy="opcion")
	@Getter @Setter private List<OpcionPrivilegio> opcionPrivilegios;

	public Opcion addOpcion(Opcion opcion) {
		getOpciones().add(opcion);
		opcion.setOpcionPadre(this);

		return opcion;
	}

	public Opcion removeOpcion(Opcion opcion) {
		getOpciones().remove(opcion);
		opcion.setOpcionPadre(null);

		return opcion;
	}

	public OpcionPrivilegio addOpcionPrivilegio(OpcionPrivilegio opcionPrivilegio) {
		getOpcionPrivilegios().add(opcionPrivilegio);
		opcionPrivilegio.setOpcion(this);

		return opcionPrivilegio;
	}

	public OpcionPrivilegio removeOpcionPrivilegio(OpcionPrivilegio opcionPrivilegio) {
		getOpcionPrivilegios().remove(opcionPrivilegio);
		opcionPrivilegio.setOpcion(null);

		return opcionPrivilegio;
	}

}