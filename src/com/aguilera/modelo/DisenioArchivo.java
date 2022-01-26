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
 * The persistent class for the da_disenio_archivo database table.
 * 
 */
@Entity
@Table(name="da_disenio_archivo")
@NamedQueries({
	@NamedQuery(name="DisenioArchivo.findAll", query="SELECT d FROM DisenioArchivo d")
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class DisenioArchivo implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Getter @Setter private String estado;

	//bi-directional many-to-one association to Archivo
	@ManyToOne
	@JoinColumn(name="id_archivo")
	@Getter @Setter private Archivo archivo;

	//bi-directional many-to-one association to Disenio
	@ManyToOne
	@JoinColumn(name="id_disenio")
	@Getter @Setter private Disenio disenio;

}