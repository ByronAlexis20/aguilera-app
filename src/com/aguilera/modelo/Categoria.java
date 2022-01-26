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
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the da_categoria database table.
 * 
 */
@Entity
@Table(name="da_categoria")
@NamedQueries({
	@NamedQuery(name="Categoria.findAll", query="SELECT c FROM Categoria c")
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class Categoria implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Getter @Setter private String descripcion;

	@Getter @Setter private String estado;

	@Getter @Setter private String nombre;
	
	@Transient @Getter @Setter private boolean seleccionado;

	//bi-directional many-to-one association to DisenioCategoria
	@OneToMany(mappedBy="categoria")
	@Getter @Setter private List<DisenioCategoria> disenioCategorias;

	public DisenioCategoria addDisenioCategoria(DisenioCategoria disenioCategoria) {
		getDisenioCategorias().add(disenioCategoria);
		disenioCategoria.setCategoria(this);

		return disenioCategoria;
	}

	public DisenioCategoria removeDisenioCategoria(DisenioCategoria disenioCategoria) {
		getDisenioCategorias().remove(disenioCategoria);
		disenioCategoria.setCategoria(null);

		return disenioCategoria;
	}

}