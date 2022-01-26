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
 * The persistent class for the da_nota_cabecera database table.
 * 
 */
@Entity
@Table(name="da_nota_cabecera")
@NamedQueries({
	@NamedQuery(name="NotaCabecera.findAll", query="SELECT n FROM NotaCabecera n")
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class NotaCabecera implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Getter @Setter private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_entrega")
	@Getter @Setter private Date fechaEntrega;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="id_operario")
	@Getter @Setter private Usuario operario;

	//bi-directional many-to-one association to Pedido
	@ManyToOne
	@JoinColumn(name="id_pedido")
	@Getter @Setter private Pedido pedido;

	//bi-directional many-to-one association to NotaDetalle
	@OneToMany(mappedBy="notaCabecera", cascade=CascadeType.ALL)
	@Getter @Setter private List<NotaDetalle> notaDetalles;

	public NotaDetalle addNotaDetalle(NotaDetalle notaDetalle) {
		getNotaDetalles().add(notaDetalle);
		notaDetalle.setNotaCabecera(this);

		return notaDetalle;
	}

	public NotaDetalle removeNotaDetalle(NotaDetalle notaDetalle) {
		getNotaDetalles().remove(notaDetalle);
		notaDetalle.setNotaCabecera(null);

		return notaDetalle;
	}

}