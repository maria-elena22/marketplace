package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Produto;
import com.fcul.marketplace.model.enums.IVA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            "and p.nome LIKE %:nome% "
    )
    List<Produto> getFilteredProducts(String nome, Integer idCategoria);

    @Query("SELECT DISTINCT p FROM Produto p " +
            "INNER JOIN p.subCategorias sc " +
            "LEFT JOIN p.uniProds up " +
            "INNER JOIN sc.categoria c " +
            "LEFT JOIN p.precoFornecedores pf " +
            "LEFT JOIN p.propriedades props " +
            "WHERE (:iva is null or p.iva = :iva) " +
            "AND (:nomeProduto is null or p.nome LIKE %:nomeProduto%) " +
            "AND (:categoriaId is null or c.idCategoria=:categoriaId) " +
            "AND (:subcategoriaId is null or sc.idSubCategoria=:subcategoriaId) " +
            "AND (:unidadeId is null or up.idUnidade=:unidadeId) " +
            "AND (:idFornecedor is null or up.fornecedor.idUtilizador=:idFornecedor) " +
            "AND (:precoMin is null or pf.preco>= :precoMin) " +
            "AND (:precoMax is null or pf.preco<= :precoMax) " +
            "AND (:propriedadeId is null or KEY(props) = :propriedadeId) "+
            "AND (:descricao is null or p.descricao LIKE %:descricao%) ")
    Page<Produto> findByOpt(Integer idFornecedor, Integer propriedadeId, Integer categoriaId, Integer unidadeId, String nomeProduto,
                            Integer subcategoriaId, Double precoMin, Double precoMax, IVA iva, String descricao ,Pageable pageable);
}
