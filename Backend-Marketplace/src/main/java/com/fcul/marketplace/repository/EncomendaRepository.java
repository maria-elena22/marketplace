package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Categoria;
import com.fcul.marketplace.model.Encomenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncomendaRepository extends JpaRepository<Encomenda,Integer> {

}
