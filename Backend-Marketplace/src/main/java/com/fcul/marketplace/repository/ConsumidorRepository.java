package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Consumidor;
import com.fcul.marketplace.model.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumidorRepository extends JpaRepository<Consumidor,Integer> {
}
