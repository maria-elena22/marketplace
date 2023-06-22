package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Encomenda;
import com.fcul.marketplace.model.enums.EstadoEncomenda;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface EncomendaRepository extends JpaRepository<Encomenda, Integer> {
    @Query("SELECT e FROM Encomenda e " +
            "WHERE ( e.consumidor.idUtilizador = :idConsumidor) " +
            "AND (:precoMin IS NULL OR e.preco >= :precoMin) " +
            "AND (:precoMax IS NULL OR e.preco <= :precoMax) " +
            "AND (:dataMin IS NULL OR e.dataEncomenda >= :dataMin) " +
            "AND (:dataMax IS NULL OR e.dataEncomenda <= :dataMax) " +
            "AND (:estadoEncomenda IS NULL OR e.estadoEncomenda = :estadoEncomenda)")
    List<Encomenda> findAllOpt(Integer idConsumidor, Double precoMin, Double precoMax, Date dataMin, Date dataMax, EstadoEncomenda estadoEncomenda, Pageable pageable);
}
