package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Consumidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsumidorRepository extends JpaRepository<Consumidor, Integer> {
    Optional<Consumidor> findByEmail(String email);
}
