package com.aguilera.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the da_cabecera database table.
 * 
 */
@Entity
@Table(name="da_cabecera")
@NamedQueries({
	@NamedQuery(name="Cabecera.findAll", query="SELECT c FROM Cabecera c")
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class Cabecera implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Getter @Setter private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_venta")
	@Getter @Setter private Date fechaVenta;
	
	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="id_pedido")
	@Getter @Setter private Pedido pedido;

	@Getter @Setter private float iva;
	
	@Getter @Setter private float recargo;

	@Getter @Setter private float subtotal;

	@Getter @Setter private float total;

	//bi-directional many-to-one association to Detalle
	@OneToMany(mappedBy="cabecera", cascade=CascadeType.ALL)
	@Getter @Setter private List<Detalle> detalles;

	public Detalle addDetalle(Detalle detalle) {
		getDetalles().add(detalle);
		detalle.setCabecera(this);

		return detalle;
	}

	public Detalle removeDetalle(Detalle detalle) {
		getDetalles().remove(detalle);
		detalle.setCabecera(null);

		return detalle;
	}

}