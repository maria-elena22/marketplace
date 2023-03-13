package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Encomenda;
import com.fcul.marketplace.model.Transporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransporteRepository extends JpaRepository<Transporte,Integer> {



}
