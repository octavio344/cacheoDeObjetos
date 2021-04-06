package com.iua.soa.model.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iua.soa.model.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

}
