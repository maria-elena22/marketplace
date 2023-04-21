package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Encomenda;
import com.fcul.marketplace.model.SubEncomenda;
import com.fcul.marketplace.model.enums.EstadoEncomenda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface SubEncomendaRepository extends JpaRepository<SubEncomenda, Integer> {

    List<SubEncomenda> findByFornecedorEmail(String email);

    List<SubEncomenda> findByEncomendaConsumidorEmail(String email);


    @Query("SELECT s FROM SubEncomenda s " +
            "WHERE (:idConsumidor IS NULL OR s.encomenda.consumidor.idUtilizador = :idConsumidor) " +
            "AND (:idFornecedor IS NULL OR s.fornecedor.idUtilizador = :idFornecedor) " +
            "AND (:precoMin IS NULL OR s.preco >= :precoMin) " +
            "AND (:precoMax IS NULL OR s.preco <= :precoMax) " +
            "AND (:dataMin IS NULL OR s.dataEncomenda >= :dataMin) " +
            "AND (:dataMax IS NULL OR s.dataEncomenda <= :dataMax) " +
            "AND (:estadoEncomenda IS NULL OR s.estadoEncomenda = :estadoEncomenda)")
    List<SubEncomenda> findAllOpt(Integer idConsumidor,Integer idFornecedor, Double precoMin, Double precoMax, Date dataMin, Date dataMax, EstadoEncomenda estadoEncomenda, Pageable pageable);

}
