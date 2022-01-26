package com.aguilera.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import com.aguilera.util.Constantes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


/**
 * The persistent class for the da_producto database table.
 * 
 */
@Entity
@Table(name="da_producto")
@NamedQueries({
	@NamedQuery(name="Producto.findAll", query="SELECT p FROM Producto p"),
	@NamedQuery(name="Producto.buscarPorTexto",query="SELECT p FROM Producto p WHERE p.nombre LIKE :texto")
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class Producto implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Getter @Setter private String descripcion;

	@Getter @Setter private String estado;

	@Getter @Setter private String nombre;

	@Getter @Setter private float precio;

	@Getter @Setter private int stock;
	
	@Column(name="tipo_producto")
	@Getter @Setter private String tipoProducto;

	//bi-directional many-to-one association to Compra
	@OneToMany(mappedBy="producto")
	@Getter @Setter private List<Compra> compras;

	public Compra addCompra(Compra compra) {
		getCompras().add(compra);
		compra.setProducto(this);

		return compra;
	}

	public Compra removeCompra(Compra compra) {
		getCompras().remove(compra);
		compra.setProducto(null);

		return compra;
	}
	
	public String getTipoTexto() {
		String retorno = "No definido";
		if(tipoProducto == null) {
			retorno = "No definido";
		}else if(tipoProducto.equals(Constantes.TIPO_PRODUCTO_HILOS)) {
			retorno = "Hilos";
		}else if(tipoProducto.equals(Constantes.TIPO_PRODUCTO_TELAS)) {
			retorno = "Telas";
		}
		
		return retorno;
	}
	
	public String getStyle() {
		String retorno = "";
		if(stock < 5) {
			retorno = "font-weight: bold; color: red;";
		}
		return retorno;
	}
}