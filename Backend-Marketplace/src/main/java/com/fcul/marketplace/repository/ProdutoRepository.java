package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Produto;
import com.fcul.marketplace.model.enums.IVA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    @Query("SELECT DISTINCT p FROM Produto p " +
            "INNER JOIN p.subCategorias sc " +
            "LEFT JOIN p.uniProds up " +
            "INNER JOIN sc.categoria c " +
            "LEFT JOIN p.precoFornecedores pf " +
            "LEFT JOIN p.propriedades props " +
            "WHERE (:iva is null or p.iva = :iva) " +
            "AND (:nomeProduto is null or LOWER(p.nome) LIKE CONCAT('%', LOWER(:nomeProduto), '%')) " +
            "AND (:categoriaId is null or c.idCategoria=:categoriaId) " +
            "AND (:shouldEvaluateList = false or sc.idSubCategoria IN :subcategoriaIdsList) " +
            "AND (:unidadeId is null or :unidadeId=up.idUnidade) " +
            "AND (:idFornecedor is null or up.fornecedor.idUtilizador=:idFornecedor) " +
            "AND (:precoMin is null or pf.preco>= :precoMin) " +
            "AND (:precoMax is null or pf.preco<= :precoMax) " +
            "AND (:descricao is null or LOWER(p.nome) LIKE CONCAT('%', LOWER(:nomeProduto), '%')) ")
    Page<Produto> findByOpt(Integer idFornecedor, Integer categoriaId, Integer unidadeId, String nomeProduto,
                            List<Integer> subcategoriaIdsList, Double precoMin, Double precoMax, IVA iva, String descricao, Pageable pageable, Boolean shouldEvaluateList);


    @Query("SELECT DISTINCT p FROM Produto p " +
            "INNER JOIN p.uniProds up " +
            "WHERE (:unidadeId is null or up.idUnidade=:unidadeId) " +
            "AND (:idFornecedor is null or up.fornecedor.idUtilizador=:idFornecedor) ")
    Page<Produto> findByOptF(Integer idFornecedor,Integer unidadeId, Pageable pageable);




}
