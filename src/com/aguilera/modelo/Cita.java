package com.aguilera.modelo;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import com.aguilera.util.Constantes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the da_cita database table.
 * 
 */
@Entity
@Table(name="da_cita")
@NamedQueries({
	@NamedQuery(name="Cita.findAll", query="SELECT c FROM Cita c"),
	@NamedQuery(name="Cita.buscarPorUsuarioFecha", query="SELECT c FROM Cita c WHERE "
			+ "c.cliente.id = :idCliente AND c.fecha = :fecha"),
	@NamedQuery(name="Cita.buscarPorUsuarioFechaTipoEstado", query="SELECT c FROM Cita c WHERE "
			+ "c.cliente.id = :idCliente AND c.fecha = :fecha AND c.tipo = :tipo AND "
			+ "c.estadoCita = :estado"),
	@NamedQuery(name="Cita.buscarPorFecha", query="SELECT c FROM Cita c WHERE "
			+ "c.fecha = :fecha"),
	@NamedQuery(name="Cita.validarRepetido",query="SELECT c FROM Cita c WHERE"
			+ " ((c.horaInicio >= :horaInicio AND c.horaInicio < :horaFin) OR"
			+ " (c.horaFin > :horaInicio AND c.horaFin < :horaFin)) AND c.fecha = :fecha AND c.id <> :idCita")
})
@AdditionalCriteria("this.estado is NULL")
@NoArgsConstructor
public class Cita implements Serializable, DaEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private int id;

	@Getter @Setter private String estado;

	@Column(name="estado_cita")
	@Getter @Setter private String estadoCita;

	@Temporal(TemporalType.DATE)
	@Getter @Setter private Date fecha;

	@Column(name="hora_inicio")
	@Temporal(TemporalType.TIME)
	@Getter @Setter private Date horaInicio;
	
	@Column(name="hora_fin")
	@Temporal(TemporalType.TIME)
	@Getter @Setter private Date horaFin;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="id_cliente")
	@Getter @Setter private Usuario cliente;

	@Getter @Setter private String tipo;

	public String getTipoTexto() {
		String retorno = "No definido";
		if(tipo == null) {
			retorno = "No definido";
		}else if(tipo.equals(Constantes.TIPO_CITA_ENTREVISTA)) {
			retorno = "Entrevista";
		}else if(tipo.equals(Constantes.TIPO_CITA_TOMA_MEDIDAS)) {
			retorno = "Toma de medidas";
		}
		return retorno;
	}
	
	public String getEstadoCitaTexto() {
		String retorno = "No definido";
		if(estadoCita == null) {
			retorno = "No definido";
		}else if(estadoCita.equals(Constantes.ESTADO_CITA_EJECUTADA)) {
			retorno = "Ejecutada";
		}else if(estadoCita.equals(Constantes.ESTADO_CITA_PENDIENTE)) {
			retorno = "Pendiente";
		}else if(estadoCita.equals(Constantes.ESTADO_CITA_PERDIDA)) {
			retorno = "Perdida";
		}
		return retorno;
	}
	
	public boolean isPendiente() {
		boolean retorno = false;
		if(estadoCita.equals(Constantes.ESTADO_CITA_PENDIENTE)) {
			retorno = true;
		}
		return retorno;
	}
	
	public boolean isPendienteCliente() {
		boolean retorno = false;
		if(estadoCita.equals(Constantes.ESTADO_CITA_PENDIENTE) && fecha.after(new Date())) {
			retorno = true;
		}
		return retorno;
	}
	
	public boolean isEntrevista() {
		boolean retorno = false;
		if(tipo.equals(Constantes.TIPO_CITA_ENTREVISTA)) {
			retorno = true;
		}
		return retorno;
	}
}