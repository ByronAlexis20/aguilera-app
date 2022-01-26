package com.aguilera.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the da_medida database table.
 * 
 */
@Entity
@Table(name="da_medida")
@NamedQueries({
	@NamedQuery(name="Medida.findAll", query="SELECT m FROM Medida m"),
	@NamedQuery(name="Medida.findByPedido", query="SELECT m FROM Medida m "
			+ "WHERE m.pedido.id = :idPedido")
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class Medida implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Column(name="altura_cadera")
	@Getter @Setter private Float alturaCadera;

	@Column(name="ancho_espalda")
	@Getter @Setter private Float anchoEspalda;

	@Getter @Setter private Float brazo;
	
	@Getter @Setter private Integer cantidad;

	@Getter @Setter private Float cintura;

	@Getter @Setter private Float codo;

	@Column(name="contorno_cadera")
	@Getter @Setter private Float contornoCadera;

	@Getter @Setter private Float cuello;

	@Getter @Setter private Float encuentro;

	@Column(name="entre_pierna")
	@Getter @Setter private Float entrePierna;

	@Getter @Setter private String estado;

	@Getter @Setter private Float hombro;

	//bi-directional many-to-one association to Pedido
	@ManyToOne
	@JoinColumn(name="id_pedido")
	@Getter @Setter private Pedido pedido;

	@Getter @Setter private Float muneca;

	@Getter @Setter private Float pecho;

	@Getter @Setter private String persona;

	@Getter @Setter private Float pierna;

	@Getter @Setter private Float rodilla;

	@Getter @Setter private String sexo;

	@Column(name="talla_camisa")
	@Getter @Setter private Integer tallaCamisa;

	@Column(name="talla_pantalon")
	@Getter @Setter private Integer tallaPantalon;

	@Getter @Setter private Float tiro;

	@Getter @Setter private Float torax;
}