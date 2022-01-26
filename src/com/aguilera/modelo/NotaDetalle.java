package com.aguilera.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the da_nota_detalle database table.
 * 
 */
@Entity
@Table(name="da_nota_detalle")
@NamedQueries({
	@NamedQuery(name="NotaDetalle.findAll", query="SELECT n FROM NotaDetalle n")
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class NotaDetalle implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Getter @Setter private int cantidad;

	@Getter @Setter private String estado;

	//bi-directional many-to-one association to Disenio
	@ManyToOne
	@JoinColumn(name="id_disenio")
	@Getter @Setter private Disenio disenio;

	//bi-directional many-to-one association to NotaCabecera
	@ManyToOne
	@JoinColumn(name="id_nota_cabecera")
	@Getter @Setter private NotaCabecera notaCabecera;

}