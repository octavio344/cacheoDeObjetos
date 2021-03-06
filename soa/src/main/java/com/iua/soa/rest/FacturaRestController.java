package com.iua.soa.rest;

import com.iua.soa.business.IFacturaBusiness;
import com.iua.soa.business.exception.BadStateException;
import com.iua.soa.business.exception.BusinessException;
import com.iua.soa.business.exception.NotFoundException;
import com.iua.soa.business.exception.UserNotMatchException;
import com.iua.soa.model.Factura;
import com.iua.soa.model.Transaccion;
import com.iua.soa.model.dto.FacturasPagasDto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Constantes.URL_FACTURA)
public class FacturaRestController {

	@Autowired IFacturaBusiness facturaBusiness;
	
	@GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Factura> load(@PathVariable Long id){
	
		try {
			return new ResponseEntity<Factura>(facturaBusiness.findById(id),HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<Factura>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<Factura>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Factura>> list(){
		try {
			return new ResponseEntity<List<Factura>>(facturaBusiness.list(),HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<List<Factura>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Factura> save(@RequestBody Factura factura){
		try {
			facturaBusiness.save(factura);
			HttpHeaders headers = new HttpHeaders();
			headers.set("location", Constantes.URL_FACTURA+ "/"+ factura.getId());
			return new ResponseEntity<Factura>(headers,HttpStatus.CREATED);
			
		} catch (BusinessException e) {
			return new ResponseEntity<Factura>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> delete(@PathVariable Long id){
		try {
			facturaBusiness.delete(id);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (NotFoundException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}catch (BusinessException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/pay",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Transaccion> pay(@RequestBody Factura factura){
		try {
			facturaBusiness.pagar(factura);
			return new ResponseEntity<Transaccion>(HttpStatus.OK);	
		} catch (BusinessException e) {
			return new ResponseEntity<Transaccion>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<Transaccion>(HttpStatus.NOT_FOUND);
		} catch (BadStateException e) {
			return new ResponseEntity<Transaccion>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@PostMapping(value = "payList",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Factura> payList(@RequestBody List<Factura> facturaList) throws UserNotMatchException{
		try {
			facturaBusiness.pagar(facturaList);
			return new ResponseEntity<Factura>(HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<Factura>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<Factura>(HttpStatus.NOT_FOUND);
		} catch (BadStateException | UserNotMatchException e) {
			return new ResponseEntity<Factura>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/{id}/pagadas",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FacturasPagasDto>> getPagadas(@PathVariable Long id){
	
		try {
			return new ResponseEntity<List<FacturasPagasDto>>(facturaBusiness.getPagadas(id),HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<List<FacturasPagasDto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<List<FacturasPagasDto>>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	
}
