package com.iua.soa.business;

import java.util.List;

import com.iua.soa.business.exception.BusinessException;
import com.iua.soa.business.exception.NotFoundException;
import com.iua.soa.model.Cliente;

public interface IClienteBusiness {

	public List<Cliente> list() throws BusinessException;
	
	public Cliente load(Long id) throws BusinessException, NotFoundException;
	
	public Cliente save(Cliente c) throws BusinessException;
	
	public void delete(Long id) throws BusinessException, NotFoundException;
}
