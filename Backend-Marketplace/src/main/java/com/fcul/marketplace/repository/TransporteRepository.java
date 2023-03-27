package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Transporte;
import com.fcul.marketplace.model.enums.EstadoTransporte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransporteRepository extends JpaRepository<Transporte, Integer> {


    @Query("SELECT t FROM Transporte t where" +
            "(t.fornecedor.idUtilizador = :idFornecedor) and" +
            "(:estadoTransporte is null or t.estadoTransporte =:estadoTransporte)")
    Page<Transporte> findByFornecedorIdUtilizador(Integer idFornecedor, EstadoTransporte estadoTransporte, Pageable pageable);
}
