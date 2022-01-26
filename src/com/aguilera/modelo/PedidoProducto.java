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
 * The persistent class for the da_pedido_producto database table.
 * 
 */
@Entity
@Table(name="da_pedido_producto")
@NamedQueries({
	@NamedQuery(name="PedidoProducto.findAll", query="SELECT p FROM PedidoProducto p")
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class PedidoProducto implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Getter @Setter private int cantidad;

	@Getter @Setter private String estado;

	//bi-directional many-to-one association to Pedido
	@ManyToOne
	@JoinColumn(name="id_pedido")
	@Getter @Setter private Pedido pedido;

	//bi-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="id_producto")
	@Getter @Setter private Producto producto;
}