package com.iua.soa.business;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.iua.soa.business.exception.BusinessException;
import com.iua.soa.business.exception.NotFoundException;
import com.iua.soa.model.Cliente;
import com.iua.soa.model.persistence.ClienteRepository;

@Service
public class ClienteBusiness implements IClienteBusiness{

	@Autowired
	private ClienteRepository clienteDAO;
	
	@Override
	public List<Cliente> list() throws BusinessException {
		List<Cliente> clientes;
		
		try {
			clientes = clienteDAO.findAll();
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		
		return clientes;
	}

	@Override
	public Cliente load(Long id) throws BusinessException, NotFoundException {
		Optional<Cliente> c ;
		
		try {
			c = clienteDAO.findById(id);
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		
		if (!c.isPresent()) {
			throw new NotFoundException("No se encuentra el cliente con el identificador: "+id);
		}
		
		return c.get();
	}

	@Override
	public Cliente save(Cliente c) throws BusinessException {
		try {
			return clienteDAO.save(c);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public void delete(Long id) throws BusinessException, NotFoundException {
		
		try {
			clienteDAO.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new NotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

}
