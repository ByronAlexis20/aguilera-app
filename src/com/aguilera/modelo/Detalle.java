package com.aguilera.modelo;

import java.io.Serializable;

import javax.persistence.Column;
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
 * The persistent class for the da_detalle database table.
 * 
 */
@Entity
@Table(name="da_detalle")
@NamedQueries({
	@NamedQuery(name="Detalle.findAll", query="SELECT d FROM Detalle d"),
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class Detalle implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Getter @Setter private int cantidad;

	@Getter @Setter private String estado;

	@Getter @Setter private float iva;

	@Getter @Setter private float subtotal;

	@Getter @Setter private float total;

	@Column(name="valor_unitario")
	@Getter @Setter private float valorUnitario;

	//bi-directional many-to-one association to Cabecera
	@ManyToOne
	@JoinColumn(name="id_cabecera")
	@Getter @Setter private Cabecera cabecera;

	//bi-directional many-to-one association to Pedido
	@ManyToOne
	@JoinColumn(name="id_disenio")
	@Getter @Setter private Disenio disenio;

}