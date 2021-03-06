package com.iua.soa.business;

import java.util.List;

import com.iua.soa.business.exception.BadStateException;
import com.iua.soa.business.exception.BusinessException;
import com.iua.soa.business.exception.NotFoundException;
import com.iua.soa.business.exception.UserNotMatchException;
import com.iua.soa.model.Cliente;
import com.iua.soa.model.Factura;
import com.iua.soa.model.dto.FacturasPagasDto;

public interface IFacturaBusiness {

		public Factura save(Factura f) throws BusinessException;
		
		public List<Factura> list() throws BusinessException;
		
		public Factura findById(Long id) throws BusinessException, NotFoundException;
		
		public void delete(Long id) throws BusinessException, NotFoundException;
		
		public Factura pagar(Factura f) throws BusinessException,NotFoundException,BadStateException;
		
		public List<Factura> pagar(List<Factura> facturaList) throws NotFoundException, BusinessException ,BadStateException ,UserNotMatchException;

		public List<FacturasPagasDto> getPagadas(Long id) throws BusinessException, NotFoundException;
}
