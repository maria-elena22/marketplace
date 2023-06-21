package com.fcul.marketplace.service;

import com.fcul.marketplace.exceptions.ForbiddenActionException;
import com.fcul.marketplace.model.*;
import com.fcul.marketplace.repository.EncomendaRepository;
import com.fcul.marketplace.repository.SubEncomendaRepository;
import com.fcul.marketplace.repository.SubItemRepository;
import com.fcul.marketplace.repository.UniProdRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
public class UniProdServiceTest {

    @Mock
    UniProdRepository uniProdRepository;

    @Mock
    UtilizadorService utilizadorService;

    @InjectMocks
    UniProdService uniProdService;

    @Test
    public void testGetUniProds() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setEmail("test@test.com");

        List<UniProd> uniProds = new ArrayList<>();
        UniProd uniProd = new UniProd();
        uniProds.add(uniProd);
        Page<UniProd> uniProdPage = new PageImpl<>(uniProds);

        when(utilizadorService.findFornecedorByEmail(anyString())).thenReturn(fornecedor);
        when(uniProdRepository.findByOpt(any(),anyString(),any())).thenReturn(uniProdPage);

        assertEquals(uniProds,uniProdService.getUniProds("test@test.com","",null, null,null,null));
    }

    @Test
    public void testGetUniProdById() {
        UniProd uniProd = new UniProd();
        uniProd.setIdUnidade(1);

        when(uniProdRepository.findById(anyInt())).thenReturn(Optional.of(uniProd));

        assertEquals(uniProd,uniProdService.getUniProdByID(1));
    }

    @Test
    public void testAddUniProd() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setIdUtilizador(1);
        fornecedor.setEmail("test@test.com");


        UniProd uniProd = new UniProd();
        UniProd uniProdBD = new UniProd();
        uniProdBD.setFornecedor(fornecedor);
        uniProdBD.setActive(true);

        when(utilizadorService.findFornecedorByEmail(anyString())).thenReturn(fornecedor);
        when(uniProdRepository.save(any())).thenReturn(uniProdBD);

        assertEquals(uniProdBD,uniProdService.addUniProd("test@test.com",uniProd));

    }

    @Test
    public void testUpdateUniProd() throws ForbiddenActionException {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setIdUtilizador(1);
        fornecedor.setEmail("test@test.com");

        UniProd uniProdNova = new UniProd();
        uniProdNova.setNomeUniProd("u1");

        UniProd uniProdAntiga = new UniProd();
        uniProdAntiga.setFornecedor(fornecedor);
        uniProdAntiga.setNomeUniProd("u2");
        UniProd uniProdAtualizada = new UniProd();
        uniProdAtualizada.setNomeUniProd("u1");

        when(uniProdRepository.findById(any())).thenReturn(Optional.of(uniProdAntiga));
        when(utilizadorService.findFornecedorByEmail(anyString())).thenReturn(fornecedor);
        when(uniProdRepository.save(any())).thenReturn(uniProdAtualizada);

        assertEquals(uniProdAtualizada,uniProdService.updateUniProd("test@test.com",1,uniProdNova));

    }

}
