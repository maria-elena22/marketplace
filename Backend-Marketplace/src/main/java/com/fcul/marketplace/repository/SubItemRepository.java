package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.SubItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubItemRepository extends JpaRepository<SubItem, Integer> {


    @Query("SELECT s FROM SubItem s " +
            "JOIN s.item i " +
            "JOIN i.produto p " +
            "JOIN p.uniProds up " +
            "JOIN up.fornecedor f " +
            "WHERE s.entregue = false " +
            "AND f.idUtilizador = :idFornecedor ")
    Page<SubItem> getSubItensNaoEntregues(Integer idFornecedor, Pageable pageable);

}

