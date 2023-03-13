package com.fcul.marketplace.service;

import com.fcul.marketplace.model.Encomenda;
import com.fcul.marketplace.model.Fornecedor;
import com.fcul.marketplace.model.Produto;
import com.fcul.marketplace.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.Date;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    ProdutoRepository produtoRepository;

    //============================GET=============================

    public List<Produto> getProdutos() {
        return produtoRepository.findAll();
    }

    public Produto getProdutoByID(Integer id){
        return produtoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Produto> getProdutosByCategoria(Integer categoriaId){
        return produtoRepository.findByCategoria(categoriaId);
    }

    public List<Produto> getProdutosBySubCategoria(Integer subCategoriaId){
        return produtoRepository.findBySubCategoria(subCategoriaId);
    }

    public List<Produto> getProdutosByNome(String nome){
        return produtoRepository.findByNomeContaining(nome);
    }

    public List<Produto> getProdutosFiltro(String nome, Double precoMin, Double precoMax, Integer idCategoria) {
        return produtoRepository.getFilteredProducts(nome,precoMax,precoMin,idCategoria);
    }

    //===========================INSERT===========================

    public Produto addProduto(Produto produto) { return produtoRepository.save(produto); }


    //===========================UPDATE===========================
    //update produto

    //===========================DELETE===========================
    public void deleteProduto(Integer id){
        produtoRepository.deleteById(id);
    }

    public void deleteProdutoBatch(List<Integer> ids){
        produtoRepository.deleteAllByIdInBatch(ids);
    }
}
