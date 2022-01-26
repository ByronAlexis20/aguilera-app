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
 * The persistent class for the da_disenio_categoria database table.
 * 
 */
@Entity
@Table(name="da_disenio_categoria")
@NamedQueries({
	@NamedQuery(name="DisenioCategoria.buscarPorCategoria", query="SELECT d FROM DisenioCategoria d where d.categoria.id = :idCat and d.disenio.id = :idDis")
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class DisenioCategoria implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Getter @Setter private String estado;

	//bi-directional many-to-one association to Disenio
	@ManyToOne
	@JoinColumn(name="id_disenio")
	@Getter @Setter private Disenio disenio;

	//bi-directional many-to-one association to Categoria
	@ManyToOne
	@JoinColumn(name="id_categoria")
	@Getter @Setter private Categoria categoria;

}