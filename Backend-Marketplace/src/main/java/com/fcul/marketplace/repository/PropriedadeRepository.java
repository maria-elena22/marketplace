package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Propriedade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PropriedadeRepository extends JpaRepository<Propriedade, Integer> {

    @Query("SELECT p FROM Propriedade p where" +
            "(:nomePropriedade is null or p.nomePropriedade LIKE %:nomePropriedade%)")
    Page<Propriedade> findByOpt(String nomePropriedade, Pageable pageable);
}
