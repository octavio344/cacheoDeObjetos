package com.iua.soa.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.iua.soa.business.exception.BadStateException;
import com.iua.soa.business.exception.BusinessException;
import com.iua.soa.business.exception.NotFoundException;
import com.iua.soa.business.exception.UserNotMatchException;
import com.iua.soa.model.Cliente;
import com.iua.soa.model.Factura;
import com.iua.soa.model.Transaccion;
import com.iua.soa.model.dto.FacturasPagasDto;
import com.iua.soa.model.persistence.ClienteRepository;
import com.iua.soa.model.persistence.FacturaRepository;
import com.iua.soa.model.persistence.TransaccionRepository;

@Service
public class FacturaBusiness implements IFacturaBusiness {

	@Autowired FacturaRepository facturaDAO;
	@Autowired ClienteRepository clienteDAO;
	@Autowired TransaccionRepository transaccionDAO;
	
	@Override
	public Factura save(Factura f) throws BusinessException {
		
		
			
		if(f.getCliente().getId()==null) {	
				try {
					f.setEstado(Factura.Estado.EMITIDA);
					return facturaDAO.save(f);
				} catch (Exception e) {
					System.out.println(e.getMessage());
					throw new BusinessException(e.getMessage()); 
				}
			}else {
				try {
					Optional<Cliente> opCliente = clienteDAO.findById(f.getCliente().getId());
					f.setCliente(opCliente.get());
					return facturaDAO.save(f);
				} catch (Exception e) {
					System.out.println(e.getMessage());
					throw new BusinessException(e.getMessage()); 
				}
			}
		}

	

	

	@Override
	public List<Factura> list() throws BusinessException {
		List<Factura> facturas;
		
		try {
			facturas = facturaDAO.findAll();
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		
		return facturas;
		
	}

	@Override
	public Factura findById(Long id) throws BusinessException, NotFoundException {
		Optional<Factura> op;
		
		try {
			op = facturaDAO.findById(id);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		
		if(!op.isPresent()) {
			throw new NotFoundException("No se encuentra la factura con el id: "+id);
		}
		
		return op.get();
	}

	@Override
	public void delete(Long id) throws BusinessException, NotFoundException {
		
		try {
			facturaDAO.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new NotFoundException("No se encontro ninguna factura con el id: "+id);
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}

	}
	
	@Override
	public Factura pagar(Factura f) throws BusinessException, NotFoundException, BadStateException {
		
		
		Optional<Cliente> opCliente = clienteDAO.findById(f.getCliente().getId());
		
		if(!opCliente.isPresent()) {
			throw new NotFoundException("No se encuentra el cliente con id: "+f.getCliente().getId());
		}
		
		Optional<Factura> opFactura = facturaDAO.findById(f.getId());
		
		if(!opFactura.isPresent()) {
			throw new NotFoundException("No se encuentra la factura con id: "+f.getId()+" asociada al usuario con id: "+f.getCliente().getId());
		}
		
		if(opFactura.get().getEstado().equals(Factura.Estado.ANULADA)||opFactura.get().getEstado().equals(Factura.Estado.PAGADA)) {
			throw new BadStateException("La factura con id: "+opFactura.get().getId()+" se encuentra pagada o anulada!");
		}
		
		
		Optional<Transaccion> opTransaccion = transaccionDAO.findByClienteIdClienteAndFacturaIdFactura(opCliente.get().getId(),opFactura.get().getId());
		
		
		if(!opTransaccion.isPresent()) {
			Transaccion tra= new Transaccion();
		
			tra.setCliente(opCliente.get());
			tra.setFactura(opFactura.get());
			
			Date date = new Date();
			
			tra.setFecha(date);
			
			try {
				Factura fa= opFactura.get();
				transaccionDAO.save(tra);
				fa.setEstado(Factura.Estado.PAGADA);
				
				return facturaDAO.save(fa);
			}catch (Exception e) {
				System.out.println(opFactura.get().toString());
				throw new BusinessException(e.getMessage());
			}	
			
			
		}else {
			try {
				f.setEstado(Factura.Estado.PAGADA);
			    Transaccion tra = opTransaccion.get();
			    tra.setFecha(new Date());
				transaccionDAO.save(tra);
				return facturaDAO.save(f);
				//return opTransaccion.get();
			}catch (Exception e) {
				throw new BusinessException(e.getMessage());
			}
		}
		
	
	}

	
	@Override
	public List<Factura> pagar(List<Factura> facturaList) throws NotFoundException, BusinessException,UserNotMatchException,BadStateException {
		
		Optional<Cliente> op =clienteDAO.findById(facturaList.get(0).getCliente().getId()); 
		
		
		if(!op.isPresent()) {
			throw new NotFoundException(" No se encuentra el usaurio con el id: "+facturaList.get(0).getCliente().getId());
		}
		// Obtenemos el Usaurio y comprobamos que se encuentre en la base de datos
		
		Cliente aux = op.get();
		
		
		
		// Comprobamos si las facturas que recibimos son del mismo usuario
		
		for(int i = 0;i < facturaList.size();i++) {
			if(aux.getId().equals(facturaList.get(i).getCliente().getId())) {
				
				// Verificamos que las facturas se encuentren asociadas al usaurio y si su estado es el correcto
				
				Optional<Factura> opF= facturaDAO.findById(facturaList.get(i).getId());
				
				if(!opF.isPresent()) {
					throw new NotFoundException("No se encuentra la factura con id: "+opF.get().getId());
				}
				
				if(!(opF.get().getCliente().equals(aux))) {
					throw new UserNotMatchException("Las facturas que quiere pagar no corresponden al usuario");
				}else{
					if(opF.get().getEstado().equals(Factura.Estado.ANULADA)|opF.get().getEstado().equals(Factura.Estado.PAGADA)) {
						throw new BadStateException("La factura con id: "+opF.get().getId()+" se encuentra pagada o anulada!");
					}
				}
			}else {
				throw new UserNotMatchException("Las facturas que desea pagar no corresponden con el mismo usuario");
			}
		}
		
		// Si pasaron todos los filtros ahora podemos guardarlas como una transaccion exitosa
		
		List<Factura> facturas = new ArrayList<Factura>();
		
		for(int i = 0;i< facturaList.size();i++) {
			
			// Creamos una transaccion nueva y la llenamos con los datos
			Transaccion t =new Transaccion();
			
			Factura f = facturaDAO.findById(facturaList.get(i).getId()).get();
			
			t.setFactura(f);
			t.setCliente(aux);
			t.setFecha(new Date());
			
			// Almacenamos la transaccion y actualizamos el estado de la factura a PAGADO
			try {
				transaccionDAO.save(t);
				f.setEstado(Factura.Estado.PAGADA);
				facturas.add(facturaDAO.save(f));
			} catch (Exception e) {
				throw new BusinessException(e.getMessage());
			}
			
		}
		
		return facturas;
	}

	@Override
	public List<FacturasPagasDto> getPagadas(Long id) throws BusinessException, NotFoundException {
		
		Optional<Cliente> opCliente = clienteDAO.findById(id);
		
		if(!opCliente.isPresent()) {
			throw new NotFoundException("No se encuentra el cliente con id: "+id);
		}
		
		try {
			List<Factura> facturasList = facturaDAO.findAll();
			List<FacturasPagasDto> facturasPagasList = new ArrayList<FacturasPagasDto>();
			
			for(int i = 0; i < facturasList.size(); i++) {
				if(!facturasList.get(i).getCliente().equals(opCliente.get())) {
					facturasList.remove(i);
				}else if(facturasList.get(i).getEstado().equals(Factura.Estado.PAGADA)){
					FacturasPagasDto fp= new FacturasPagasDto();
					fp.setEstado(facturasList.get(i).getEstado());
					fp.setFechaEmision(facturasList.get(i).getFechaEmision());
					fp.setFechaVencimiento(facturasList.get(i).getFechaVencimiento());
					fp.setIdFactura(facturasList.get(i).getId());
					facturasPagasList.add(fp);
				}
					
			}	
			return facturasPagasList;
		}catch (Exception e) {
			throw new BusinessException();
		}

	}

}
