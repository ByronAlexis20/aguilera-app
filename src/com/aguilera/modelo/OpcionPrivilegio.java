package com.aguilera.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the da_opcion_privilegio database table.
 * 
 */
@Entity
@Table(name="da_opcion_privilegio")
@NamedQueries({
	@NamedQuery(name="OpcionPrivilegio.findAll", query="SELECT o FROM OpcionPrivilegio o")
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class OpcionPrivilegio implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Getter @Setter private String estado;

	//bi-directional many-to-one association to Opcion
	@ManyToOne
	@JoinColumn(name="id_opcion")
	@Getter @Setter private Opcion opcion;

	//bi-directional many-to-one association to Privilegio
	@ManyToOne
	@JoinColumn(name="id_privilegio")
	@Getter @Setter private Privilegio privilegio;
}