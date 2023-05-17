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
            "(t.unidadeDeProducao.idUnidade = :idUniProd) and" +
            "(:estadoTransporte is null or t.estadoTransporte =:estadoTransporte) and t.active = true")
    Page<Transporte> findByUniProdId(Integer idUniProd, EstadoTransporte estadoTransporte, Pageable pageable);

    @Query("SELECT t FROM Transporte t where" +
            "(t.unidadeDeProducao.fornecedor.idUtilizador = :idUtilizador) and" +
            "(:estadoTransporte is null or t.estadoTransporte =:estadoTransporte) and t.active = true")
    Page<Transporte> findByFornecedorId(Integer idUtilizador, EstadoTransporte estadoTransporte, Pageable pageable);
}
