package com.fcul.marketplace.service;

import com.fcul.marketplace.dto.categoria.SubCategoriaDTO;
import com.fcul.marketplace.dto.produto.FullProdutoDTO;
import com.fcul.marketplace.dto.uniProd.UniProdDTO;
import com.fcul.marketplace.exceptions.ForbiddenActionException;
import com.fcul.marketplace.exceptions.MissingPropertiesException;
import com.fcul.marketplace.exceptions.TooMuchPropertiesException;
import com.fcul.marketplace.model.*;
import com.fcul.marketplace.repository.EncomendaRepository;
import com.fcul.marketplace.repository.ProdutoRepository;
import com.fcul.marketplace.repository.SubEncomendaRepository;
import com.fcul.marketplace.repository.SubItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    ProdutoRepository produtoRepository;

    @Mock
    UniProdService uniProdService;

    @Mock
    UtilizadorService utilizadorService;

    @InjectMocks
    ProdutoService produtoService;

    @Test
    public void testGetProdutoByID() {
        Produto produto = new Produto();
        produto.setIdProduto(1);

        when(produtoRepository.findById(anyInt())).thenReturn(Optional.of(produto));
        assertEquals(produto,produtoService.getProdutoByID(1));
    }

    @Test
    public void testUpdateUniProdStock() throws ForbiddenActionException {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setIdUtilizador(1);
        UniProd uniProd = new UniProd();
        uniProd.setFornecedor(fornecedor);
        Produto produto = new Produto();
        produto.setIdProduto(4);

        Stock stock = new Stock(produto,1);
        List<Stock> stocks = new ArrayList<>();
        stocks.add(stock);
        uniProd.setStocks(stocks);

        List<ProdutoFornecedorInfo> produtoFornecedorInfos = new ArrayList<>();
        ProdutoFornecedorInfo produtoFornecedorInfo = new ProdutoFornecedorInfo(fornecedor, 1.0);
        produtoFornecedorInfos.add(produtoFornecedorInfo);
        produto.setPrecoFornecedores(produtoFornecedorInfos);

        List<Produto> produtos = new ArrayList<>();
        produtos.add(produto);
        uniProd.setProdutos(produtos);


        when(utilizadorService.findFornecedorByEmail(anyString())).thenReturn(fornecedor);
        when(uniProdService.getUniProdByID(anyInt())).thenReturn(uniProd);
        when(produtoRepository.findById(anyInt())).thenReturn(Optional.of(produto));

        when(produtoRepository.save(any())).thenReturn(produto);

        assertEquals(produto,produtoService.updateUniProdStock("test@test.com",2,10,4));
    }

    @Test
    public void testUpdateUniProdStockException() throws ForbiddenActionException {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setIdUtilizador(1);
        UniProd uniProd = new UniProd();
        uniProd.setFornecedor(fornecedor);
        Produto produto = new Produto();
        produto.setIdProduto(4);

        Stock stock = new Stock(produto,1);
        List<Stock> stocks = new ArrayList<>();
        stocks.add(stock);
        uniProd.setStocks(stocks);

        List<ProdutoFornecedorInfo> produtoFornecedorInfos = new ArrayList<>();
        produto.setPrecoFornecedores(produtoFornecedorInfos);

        List<Produto> produtos = new ArrayList<>();
        produtos.add(produto);
        uniProd.setProdutos(produtos);


        when(utilizadorService.findFornecedorByEmail(anyString())).thenReturn(fornecedor);
        when(uniProdService.getUniProdByID(anyInt())).thenReturn(uniProd);
        when(produtoRepository.findById(anyInt())).thenReturn(Optional.of(produto));


        assertThrows(ForbiddenActionException.class,()->produtoService.updateUniProdStock("test@test.com",2,10,4));
    }

}
