package com.iua.soa.model.dto;

import java.io.Serializable;
import java.sql.Date;

import com.iua.soa.model.Factura.Estado;

public class FacturasPagasDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3339693814698170534L;

	
	private Long idFactura;
	

	private Date fechaEmision;
	
	
	private Date fechaVencimiento;
	

	private Estado estado;

	


	public Long getIdFactura() {
		return idFactura;
	}


	public void setIdFactura(Long idFactura) {
		this.idFactura = idFactura;
	}


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


	

}
