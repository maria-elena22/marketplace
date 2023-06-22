package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {
    Optional<Fornecedor> findByEmail(String email);
}
