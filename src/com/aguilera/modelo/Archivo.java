package com.aguilera.modelo;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.AdditionalCriteria;
import org.zkoss.image.AImage;

import com.aguilera.util.FileUtil;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the da_archivo database table.
 * 
 */
@Entity
@Table(name="da_archivo")
@NamedQueries({
	@NamedQuery(name="Archivo.findAll", query="SELECT a FROM Archivo a")
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class Archivo implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Getter @Setter private String direccion;

	@Getter @Setter private String estado;

	@Getter @Setter private String formato;

	@Column(name="mime_type")
	@Getter @Setter private String mimeType;

	@Getter @Setter private String nombre;

	//bi-directional many-to-one association to DisenioArchivo
	@OneToMany(mappedBy="archivo")
	@Getter @Setter private List<DisenioArchivo> disenioArchivos;

	public DisenioArchivo addDisenioArchivo(DisenioArchivo disenioArchivo) {
		getDisenioArchivos().add(disenioArchivo);
		disenioArchivo.setArchivo(this);

		return disenioArchivo;
	}

	public DisenioArchivo removeDisenioArchivo(DisenioArchivo disenioArchivo) {
		getDisenioArchivos().remove(disenioArchivo);
		disenioArchivo.setArchivo(null);

		return disenioArchivo;
	}
	
	public AImage getImage() throws IOException {
		AImage imagen = null; 
		if (FileUtil.isExisteArchivo(toString())) {
			imagen = new AImage(toString());
		}
		return imagen;
	}
	
	public String toString() {
		if (getDireccion() == null) {
			return getNombre();
		}else{
			return getDireccion() + getNombre();
		}
	}

}