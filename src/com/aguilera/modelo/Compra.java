package com.aguilera.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


/**
 * The persistent class for the da_compra database table.
 * 
 */
@Entity
@Table(name="da_compra")
@NamedQueries({
	@NamedQuery(name="Compra.findAll", query="SELECT c FROM Compra c")
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class Compra implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Getter @Setter private int cantidad;

	@Getter @Setter private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_compra")
	@Getter @Setter private Date fechaCompra;

	@Getter @Setter private float iva;
	
	@Column(name="nombre_proveedor")
	@Getter @Setter private String nombreProveedor;
	
	@Column(name="numero_factura")
	@Getter @Setter private String numeroFactura;

	@Column(name="precio_unitario")
	@Getter @Setter private float precioUnitario;
	
	@Column(name="ruc_proveedor")
	@Getter @Setter private String rucProveedor;

	@Getter @Setter private float subtotal;

	@Getter @Setter private float total;

	//bi-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="id_producto")
	@Getter @Setter private Producto producto;
}