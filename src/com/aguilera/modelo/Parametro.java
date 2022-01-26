package com.aguilera.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the da_parametro database table.
 * 
 */
@Entity
@Table(name="da_parametro")
@NamedQueries({
	@NamedQuery(name="Parametro.findAll", query="SELECT p FROM Parametro p"),
	@NamedQuery(name="Parametro.buscarPorNombre", query="SELECT p FROM Parametro p WHERE"
			+ " p.nombre = :nombre")
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class Parametro implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Getter @Setter private String descripcion;

	@Getter @Setter private String estado;

	@Getter @Setter private String nombre;

	@Getter @Setter private float valor;
}