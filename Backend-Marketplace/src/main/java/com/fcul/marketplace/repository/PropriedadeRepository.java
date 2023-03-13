package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Propriedade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropriedadeRepository extends JpaRepository<Propriedade,Integer> {
}
