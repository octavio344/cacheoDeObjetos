package com.iua.soa.model.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iua.soa.model.Transaccion;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long>{
	
	public Optional<Transaccion> findByClienteIdClienteAndFacturaIdFactura(Long idCliente, Long idFactura);

}
