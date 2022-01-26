package com.aguilera.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import com.aguilera.util.Constantes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the da_pedido database table.
 * 
 */
@Entity
@Table(name="da_pedido")
@NamedQueries({
	@NamedQuery(name="Pedido.findAll", query="SELECT p FROM Pedido p"),
	@NamedQuery(name="Pedido.buscarPorEstado", query="SELECT p FROM Pedido p "
			+ "WHERE p.estadoPedido = :estadoPedido")
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class Pedido implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;
	
	@Getter @Setter private int correcciones;

	@Getter @Setter private String detalle;

	@Getter @Setter private String estado;

	@Column(name="estado_pedido")
	@Getter @Setter private String estadoPedido;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="id_cliente")
	@Getter @Setter private Usuario cliente;

	@Getter @Setter private String observacion;
	
	@Getter @Setter private float recargo;
	
	//bi-directional many-to-one association to Medida
	@OneToMany(mappedBy="pedido", cascade=CascadeType.ALL)
	@Getter @Setter private List<Medida> medidas;

	//bi-directional many-to-one association to PedidoDisenio
	@OneToMany(mappedBy="pedido", cascade=CascadeType.ALL)
	@Getter @Setter private List<PedidoDisenio> pedidoDisenios;
	
	//bi-directional many-to-one association to PedidoProducto
	@OneToMany(mappedBy="pedido", cascade=CascadeType.ALL)
	@Getter @Setter private List<PedidoProducto> pedidoProductos;

	public Medida addMedida(Medida medida) {
		getMedidas().add(medida);
		medida.setPedido(this);

		return medida;
	}

	public Medida removeMedida(Medida medida) {
		getMedidas().remove(medida);
		medida.setPedido(null);

		return medida;
	}
	
	public PedidoDisenio addPedidoDisenio(PedidoDisenio pedidoDisenio) {
		getPedidoDisenios().add(pedidoDisenio);
		pedidoDisenio.setPedido(this);

		return pedidoDisenio;
	}

	public PedidoDisenio removePedidoDisenio(PedidoDisenio pedidoDisenio) {
		getPedidoDisenios().remove(pedidoDisenio);
		pedidoDisenio.setPedido(null);

		return pedidoDisenio;
	}
	
	public PedidoProducto addPedidoProducto(PedidoProducto pedidoProducto) {
		getPedidoProductos().add(pedidoProducto);
		pedidoProducto.setPedido(this);

		return pedidoProducto;
	}

	public PedidoProducto removePedidoProducto(PedidoProducto pedidoProducto) {
		getPedidoProductos().remove(pedidoProducto);
		pedidoProducto.setPedido(null);

		return pedidoProducto;
	}
	
	public String getEstadoPedidoTexto() {
		String retorno = "No definido";
		if(estadoPedido == null) {
			retorno = "No definido";
		}else if(estadoPedido.equals(Constantes.ESTADO_PEDIDO_CREADO)) {
			retorno = "Creado";
		}else if(estadoPedido.equals(Constantes.ESTADO_PEDIDO_DISENIO)) {
			retorno = "Diseñado";
		}else if(estadoPedido.equals(Constantes.ESTADO_PEDIDO_APROBADO)) {
			retorno = "Aprobado";
		}else if(estadoPedido.equals(Constantes.ESTADO_PEDIDO_DEVUELTO)) {
			retorno = "Rechazado";
		}else if(estadoPedido.equals(Constantes.ESTADO_PEDIDO_FINALIZADO)) {
			retorno = "Finalizado";
		}else if(estadoPedido.equals(Constantes.ESTADO_PEDIDO_PAGADO)) {
			retorno = "Pagado";
		}else if(estadoPedido.equals(Constantes.ESTADO_PEDIDO_FABRICADO)) {
			retorno = "Fabricado";
		}
		
		return retorno;
	}
	
	public boolean isCreado() {
		boolean retorno = false;
		if(estadoPedido.equals(Constantes.ESTADO_PEDIDO_CREADO)) {
			retorno = true;
		}
		return retorno;
	}
	
	public boolean isAprobado() {
		boolean retorno = false;
		if(estadoPedido.equals(Constantes.ESTADO_PEDIDO_APROBADO)) {
			retorno = true;
		}
		return retorno;
	}
	
	public boolean isDiseniado() {
		boolean retorno = false;
		if(estadoPedido.equals(Constantes.ESTADO_PEDIDO_DISENIO)) {
			retorno = true;
		}
		return retorno;
	}
	
	public boolean isDevuelto() {
		boolean retorno = false;
		if(estadoPedido.equals(Constantes.ESTADO_PEDIDO_DEVUELTO)) {
			retorno = true;
		}
		return retorno;
	}
	
	public boolean isFinalizado() {
		boolean retorno = false;
		if(estadoPedido.equals(Constantes.ESTADO_PEDIDO_FINALIZADO)) {
			retorno = true;
		}
		return retorno;
	}
	
	public boolean isPagado() {
		boolean retorno = false;
		if(estadoPedido.equals(Constantes.ESTADO_PEDIDO_PAGADO)) {
			retorno = true;
		}
		return retorno;
	}
	
	public boolean isFabricado() {
		boolean retorno = false;
		if(estadoPedido.equals(Constantes.ESTADO_PEDIDO_FABRICADO)) {
			retorno = true;
		}
		return retorno;
	}
	
	public float getTotal() {
		float retorno = 0;
		if(pedidoDisenios != null) {
			for(PedidoDisenio objeto : pedidoDisenios) {
				retorno = retorno + (objeto.getDisenio().getPrecio() * objeto.getCantidad());
			}
		}
		retorno = retorno + recargo;
		return retorno;
	}

}