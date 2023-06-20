package com.fcul.marketplace.service;

import com.fcul.marketplace.exceptions.ForbiddenActionException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Mock
    CategoriaService categoriaService;

    @Mock
    ModelMapper modelMapper;

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
    public void testAddProduto() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setIdUtilizador(1);
        when(utilizadorService.findFornecedorByEmail(anyString())).thenReturn(fornecedor);

        Categoria categoria = new Categoria();
        SubCategoria subCategoria = new SubCategoria();
        when(categoriaService.getSubCategoriaByID(anyInt())).thenReturn(subCategoria);

        UniProd uniProd = new UniProd();
        uniProd.setFornecedor(fornecedor);
        when(uniProdService.getUniProdByID(anyInt())).thenReturn(uniProd);

        Propriedade propriedade = new Propriedade();
        when(categoriaService.getPropriedadeByID(anyInt())).thenReturn(propriedade);


        Produto produto = new Produto();
        produto.setIdProduto(1);

        when(produtoRepository.save(any())).thenReturn(produto);

        when(produtoRepository.findById(anyInt())).thenReturn(Optional.of(produto));
        assertEquals(produto,produtoService.getProdutoByID(1));
    }




    @Test
    public void testGetSubItemByIdException() {
        Fornecedor fornecedor1 = new Fornecedor();
        fornecedor1.setActive(true);
        Fornecedor fornecedor2 = new Fornecedor();

        SubEncomenda subEncomenda = new SubEncomenda();
        SubItem subItem = new SubItem();
        Item item = new Item();

        subEncomenda.setFornecedor(fornecedor2);
        item.setSubEncomenda(subEncomenda);
        subItem.setItem(item);

//        when(utilizadorService.findFornecedorByEmail(anyString())).thenReturn(fornecedor1);
//        when(subItemRepository.findById(anyInt())).thenReturn(Optional.of(subItem));
//
//        assertThrows(ForbiddenActionException.class, ()->encomendaService.getSubItemById("test@test.com",2));

    }

}
