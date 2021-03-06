package com.iua.soa.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "facturas")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="idFactura")
public class Factura implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1683325841981889586L;
	
	public enum Estado{
		EMITIDA,PAGADA, VENCIDA, ANULADA
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("idFactura")
	private Long idFactura;
	
	@Column(nullable = false )
	private Date fechaEmision;
	
	@Column(nullable = false)
	private Date fechaVencimiento;
	
	@Column(nullable = false)
	private Estado estado;
	
	//23-09-20
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE,CascadeType.PERSIST})
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Long getId() {
		return idFactura;
	}

	public void setId(Long id) {
		this.idFactura = id;
	}

	@Override
	public String toString() {
		return "Factura [id=" + idFactura + ", fechaEmision=" + fechaEmision + ", fechaVencimiento=" + fechaVencimiento
				+ ", estado=" + estado + ", cliente=" + cliente + "]";
	}
	
	
}
