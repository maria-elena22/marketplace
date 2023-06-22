package com.fcul.marketplace.service;

import com.fcul.marketplace.exceptions.ForbiddenActionException;
import com.fcul.marketplace.model.*;
import com.fcul.marketplace.model.enums.Continente;
import com.fcul.marketplace.model.enums.Pais;
import com.fcul.marketplace.model.utils.Coordinate;
import com.fcul.marketplace.repository.*;
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
public class UtilizadorServiceTest {

    @Mock
    FornecedorRepository fornecedorRepository;

    @Mock
    ConsumidorRepository consumidorRepository;

    @Mock
    UtilizadorRepository utilizadorRepository;

    @InjectMocks
    UtilizadorService utilizadorService;

    @Test
    public void testGetFornecedores() {
        Fornecedor fornecedor = new Fornecedor();
        List<Fornecedor> fornecedores = new ArrayList<>();
        fornecedores.add(fornecedor);

        when(fornecedorRepository.findAll()).thenReturn(fornecedores);

        assertEquals(fornecedores,utilizadorService.getFornecedores());
    }

    @Test
    public void testGetFornecedorByID() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setIdUtilizador(1);

        when(fornecedorRepository.findById(anyInt())).thenReturn(Optional.of(fornecedor));

        assertEquals(fornecedor,utilizadorService.getFornecedorByID(1));
    }

    @Test
    public void testGetFornecedorByEmail() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setEmail("test@test.com");

        when(fornecedorRepository.findByEmail(anyString())).thenReturn(Optional.of(fornecedor));

        assertEquals(fornecedor,utilizadorService.findFornecedorByEmail("test@test.com"));
    }

    @Test
    public void testAddFornecedor() {
        Fornecedor fornecedor = new Fornecedor();
        Fornecedor fornecedorBD = new Fornecedor();
        fornecedorBD.setActive(true);


        when(fornecedorRepository.save(any())).thenReturn(fornecedorBD);

        assertEquals(fornecedorBD,utilizadorService.addFornecedor(fornecedor));
    }

    @Test
    public void testUpdateFornecedor() {
        Fornecedor fornecedorAntigo = new Fornecedor();
        Fornecedor fornecedorAtualizado = new Fornecedor();
        Fornecedor fornecedorNovo = new Fornecedor();

        fornecedorNovo.setTelemovel(982728292);
        fornecedorNovo.setContinente(Continente.EUROPA);
        fornecedorNovo.setCoordenadas(new Coordinate());
        fornecedorNovo.setDistrito("Lisboa");
        fornecedorNovo.setIdFiscal(98755799);
        fornecedorNovo.setMorada("Rua A");
        fornecedorNovo.setFreguesia("Lisboa");
        fornecedorNovo.setPais(Pais.PORTUGAL);
        fornecedorNovo.setMunicipio("Lisboa");
        fornecedorNovo.setNome("f1");

        fornecedorAtualizado.setTelemovel(982728292);
        fornecedorAtualizado.setContinente(Continente.EUROPA);
        fornecedorAtualizado.setCoordenadas(new Coordinate());
        fornecedorAtualizado.setDistrito("Lisboa");
        fornecedorAtualizado.setIdFiscal(98755799);
        fornecedorAtualizado.setMorada("Rua A");
        fornecedorAtualizado.setFreguesia("Lisboa");
        fornecedorAtualizado.setPais(Pais.PORTUGAL);
        fornecedorAtualizado.setMunicipio("Lisboa");
        fornecedorAtualizado.setNome("f1");

        when(fornecedorRepository.save(any())).thenReturn(fornecedorAtualizado);

        assertEquals(fornecedorAtualizado,utilizadorService.addFornecedor(fornecedorNovo));

    }


    @Test
    public void testDeactivateFornecedor() {
        Fornecedor fornecedor = new Fornecedor();
        SubEncomenda subEncomenda = new SubEncomenda();
        subEncomenda.setFornecedor(fornecedor);

//        when(utilizadorService.findFornecedorByEmail(anyString())).thenReturn(fornecedor);
//        when(subEncomendaRepository.findById(anyInt())).thenReturn(Optional.of(subEncomenda));
//
//        assertEquals(subEncomenda,encomendaService.getSubEncomendaById("test@test.com",2));
    }


    @Test
    public void testActivateFornecedor() {
        Fornecedor fornecedor = new Fornecedor();
        SubEncomenda subEncomenda = new SubEncomenda();
        subEncomenda.setFornecedor(fornecedor);

//        when(utilizadorService.findFornecedorByEmail(anyString())).thenReturn(fornecedor);
//        when(subEncomendaRepository.findById(anyInt())).thenReturn(Optional.of(subEncomenda));
//
//        assertEquals(subEncomenda,encomendaService.getSubEncomendaById("test@test.com",2));
    }


}
