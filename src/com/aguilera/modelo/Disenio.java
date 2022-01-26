package com.aguilera.modelo;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.AdditionalCriteria;
import org.zkoss.image.AImage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The persistent class for the da_disenio database table.
 * 
 */
@Entity
@Table(name="da_disenio")
@NamedQueries({
	@NamedQuery(name="Disenio.findAll", query="SELECT d FROM Disenio d"),
	@NamedQuery(name="Disenio.buscarPorTexto",query="SELECT d FROM Disenio d WHERE d.nombre LIKE :texto"),
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class Disenio implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Getter @Setter private String descripcion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_registro")
	@Getter @Setter private Date fechaRegistro;

	@Getter @Setter private String estado;
	
	@Getter @Setter private String diseniador;

	@Getter @Setter private String nombre;

	@Getter @Setter private Float precio;
	
	@Transient @Getter @Setter private String critica;

	//bi-directional many-to-one association to DisenioArchivo
	@OneToMany(mappedBy="disenio", cascade=CascadeType.ALL)
	@Getter @Setter private List<DisenioArchivo> disenioArchivos;
	
	//bi-directional many-to-one association to DisenioArchivo
	//@OneToMany(mappedBy="disenio", cascade=CascadeType.ALL)
	//@Getter @Setter private List<PedidoDisenio> pedidoDisenios;

	//bi-directional many-to-one association to DisenioCategoria
	@OneToMany(mappedBy="disenio", cascade=CascadeType.ALL)
	@Getter @Setter private List<DisenioCategoria> disenioCategorias;

	public DisenioArchivo addDisenioArchivo(DisenioArchivo disenioArchivo) {
		getDisenioArchivos().add(disenioArchivo);
		disenioArchivo.setDisenio(this);

		return disenioArchivo;
	}

	public DisenioArchivo removeDisenioArchivo(DisenioArchivo disenioArchivo) {
		getDisenioArchivos().remove(disenioArchivo);
		disenioArchivo.setDisenio(null);

		return disenioArchivo;
	}
	
	//public PedidoDisenio addPedidoDisenio(PedidoDisenio pedidoDisenio) {
	//	getPedidoDisenios().add(pedidoDisenio);
	//	pedidoDisenio.setDisenio(this);

	//	return pedidoDisenio;
	//}

	//public PedidoDisenio removePedidoDisenio(PedidoDisenio pedidoDisenio) {
	//	getPedidoDisenios().remove(pedidoDisenio);
	//	pedidoDisenio.setDisenio(null);

	//  return pedidoDisenio;
	//}
	
	public DisenioCategoria addDisenioCategoria(DisenioCategoria disenioCategoria) {
		getDisenioCategorias().add(disenioCategoria);
		disenioCategoria.setDisenio(this);

		return disenioCategoria;
	}

	public DisenioCategoria removeDisenioCategoria(DisenioCategoria disenioCategoria) {
		getDisenioCategorias().remove(disenioCategoria);
		disenioCategoria.setDisenio(null);

		return disenioCategoria;
	}
	
	public AImage getFirstPhoto() {
		if(disenioArchivos != null && !disenioArchivos.isEmpty()) {
			try {
				return disenioArchivos.get(0).getArchivo().getImage();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}else {
			return null;
		}
	}
	
	public String getCategorias() {
		String retorno = "";
		
		if (disenioCategorias != null) {
			
			for (DisenioCategoria objeto : disenioCategorias) {
				retorno = retorno + " " + objeto.getCategoria().getNombre() + ";";
			}
		}
		
		return retorno.trim();
	}

}