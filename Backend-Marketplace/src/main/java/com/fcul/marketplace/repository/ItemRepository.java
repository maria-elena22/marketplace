package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("SELECT i FROM Item i " +
            "JOIN i.produto p " +
            "JOIN i.subEncomenda s " +
            "JOIN p.uniProds up " +
            "JOIN up.fornecedor f " +
            "JOIN up.transportes t " +
            "WHERE i.entregue = false " +
            "AND f.idUtilizador = :idFornecedor " +
            "AND s.fornecedor = :idFornecedor " +
            "AND t.idTransporte = :idTransporte ")
    Page<Item> findByOpt(Integer idFornecedor, Integer idTransporte, Pageable pageable);

}

