package com.iua.soa.business;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.iua.soa.business.exception.BusinessException;
import com.iua.soa.business.exception.NotFoundException;
import com.iua.soa.model.Transaccion;
import com.iua.soa.model.persistence.TransaccionRepository;

@Service
public class TransaccionBusiness implements ITransaccionBusiness{

	@Autowired TransaccionRepository transaccionDAO;

	
	@Override
	public List<Transaccion> list() throws BusinessException {
		
		List<Transaccion> transacciones;
		
		try {
			transacciones = transaccionDAO.findAll();
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		
		return transacciones;
	}

	
	@Override
	public Transaccion load(Long id) throws NotFoundException, BusinessException {
		Optional<Transaccion> op;
		
		try {
			op = transaccionDAO.findById(id);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		
		if(!op.isPresent()) {
			throw new NotFoundException("No se encontro la transaccion con el id: "+id);
		}
		
		return op.get();
	}

	@Override
	public void delete(Long id) throws NotFoundException, BusinessException {
		
		try {
			transaccionDAO.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new NotFoundException("No se encontro la transaccion con el id: "+id);
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
			
	}


	@Override
	public Transaccion save(Transaccion t) throws BusinessException {
		
		try {
			return transaccionDAO.save(t);
		}catch (Exception e) {
			throw new BusinessException();
		}
	}

	
	

}
