package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    @Query("SELECT p from Produto p " +
            "inner join p.subCategorias sc " +
            "inner join sc.categoria c " +
            "where c.idCategoria=:idCategoria " +
            "and p.nome LIKE %:nome% " +
            "and p.preco between :precoMin and :precoMax"
    )
    List<Produto> getFilteredProducts(String nome, Double precoMax, Double precoMin, Integer idCategoria);


    @Query("SELECT p from Produto p " +
            "inner join p.subCategorias sc " +
            "where sc.idSubCategoria=:idSubCategoria ")
    List<Produto> findBySubCategoria(Integer idSubCategoria);

    @Query("SELECT p from Produto p " +
            "inner join p.subCategorias sc " +
            "inner join sc.categoria c " +
            "where c.idCategoria=:idCategoria ")
    List<Produto> findByCategoria(Integer idCategoria);

    List<Produto> findByNomeContaining(String nome);

}
