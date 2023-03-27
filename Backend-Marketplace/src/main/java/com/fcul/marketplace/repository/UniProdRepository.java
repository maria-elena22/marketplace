package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Encomenda;
import com.fcul.marketplace.model.UnidadeProducao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnidadeProducaoRepository extends JpaRepository<UnidadeProducao,Integer> {
}
