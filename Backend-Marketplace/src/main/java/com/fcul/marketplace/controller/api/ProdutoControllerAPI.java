package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.model.Produto;
import com.fcul.marketplace.model.Produto;
import com.fcul.marketplace.service.ProdutoService;
import com.fcul.marketplace.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produto")
public class ProdutoControllerAPI {
    @Autowired
    ProdutoService produtoService;


    @GetMapping()
    public List<Produto> getProdutos() {
        return produtoService.getProdutos();
    }

    @GetMapping("/{id}")
    public Produto getProdutosById(@PathVariable Integer id) {
        return produtoService.getProdutoByID(id);
    }

    @GetMapping("/categoria/{id}")
    public List<Produto> getProdutosByCategoria(@PathVariable Integer categoriaId) {
        return produtoService.getProdutosByCategoria(categoriaId);
    }
    @GetMapping("/subcategoria/{id}")
    public List<Produto> getProdutosBySubCategoria(@PathVariable Integer subcategorId) {
        return produtoService.getProdutosBySubCategoria(subcategorId);
    }
    @PostMapping()
    public Produto insertProduto(@RequestBody Produto produto) {
        return produtoService.addProduto(produto);
    }

    @DeleteMapping("/{id}")
    public void deleteProduto(@PathVariable Integer id){
        produtoService.deleteProduto(id);
    }

}
