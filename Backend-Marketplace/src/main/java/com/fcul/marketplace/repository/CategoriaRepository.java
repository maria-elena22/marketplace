package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    @Query("SELECT c FROM Categoria c where" +
            "(:nomeCategoria is null or c.nomeCategoria LIKE %:nomeCategoria%)")
    Page<Categoria> findByOpt(String nomeCategoria, Pageable pageable);
}
