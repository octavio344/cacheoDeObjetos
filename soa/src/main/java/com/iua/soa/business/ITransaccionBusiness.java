package com.iua.soa.business;

import java.util.List;


import com.iua.soa.business.exception.BusinessException;
import com.iua.soa.business.exception.NotFoundException;

import com.iua.soa.model.Transaccion;

public interface ITransaccionBusiness {

	public List<Transaccion> list()throws BusinessException;
	
	public Transaccion load(Long id) throws NotFoundException, BusinessException;
	
	public void delete(Long id) throws NotFoundException, BusinessException;
	
	public Transaccion save(Transaccion t) throws BusinessException;
	
}
