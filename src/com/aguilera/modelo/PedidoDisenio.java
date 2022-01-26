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

import com.aguilera.util.Constantes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the da_pedido_disenio database table.
 * 
 */
@Entity
@Table(name="da_pedido_disenio")
@NamedQueries({
	@NamedQuery(name="PedidoDisenio.findAll", query="SELECT p FROM PedidoDisenio p")
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class PedidoDisenio implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;
	
	@Getter @Setter private int cantidad;

	@Getter @Setter private String critica;
	
	@Getter @Setter private String estado;
	
	@Column(name="estado_disenio")
	@Getter @Setter private String estadoDisenio;
	
	//bi-directional many-to-one association to Disenio
	@ManyToOne
	@JoinColumn(name="id_disenio")
	@Getter @Setter private Disenio disenio;

	//bi-directional many-to-one association to Pedido
	@ManyToOne
	@JoinColumn(name="id_pedido")
	@Getter @Setter private Pedido pedido;
	
	public boolean isCreado() {
		boolean retorno = false;
		if(estadoDisenio.equals(Constantes.ESTADO_PEDIDO_D_CREADO)) {
			retorno = true;
		}
		return retorno;
	}
	
	public boolean isAprobado() {
		boolean retorno = false;
		if(estadoDisenio.equals(Constantes.ESTADO_PEDIDO_D_APROBADO)) {
			retorno = true;
		}
		return retorno;
	}
	
	public boolean isDevuelto() {
		boolean retorno = false;
		if(estadoDisenio.equals(Constantes.ESTADO_PEDIDO_D_DEVUELTO)) {
			retorno = true;
		}
		return retorno;
	}
}