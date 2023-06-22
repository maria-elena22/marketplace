package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.UniProd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniProdRepository extends JpaRepository<UniProd, Integer> {
    List<UniProd> findByFornecedorIdUtilizador(Integer idFornecedor);

    @Query("SELECT u FROM UniProd u where" +
            "(:fornecedorId is null or u.fornecedor.idUtilizador = :fornecedorId) and" +
            "(:nomeUniProd is null or u.nomeUniProd LIKE %:nomeUniProd%) and u.active = true")
    Page<UniProd> findByOpt(Integer fornecedorId, String nomeUniProd, Pageable pageable);
}
