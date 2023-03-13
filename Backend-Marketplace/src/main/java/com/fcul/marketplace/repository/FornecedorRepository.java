package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Fornecedor;
import com.fcul.marketplace.model.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor,Integer> {
}
