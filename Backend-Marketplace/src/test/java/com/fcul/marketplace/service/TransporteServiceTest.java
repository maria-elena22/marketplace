package com.fcul.marketplace.service;

import com.fcul.marketplace.exceptions.ForbiddenActionException;
import com.fcul.marketplace.model.*;
import com.fcul.marketplace.repository.EncomendaRepository;
import com.fcul.marketplace.repository.SubEncomendaRepository;
import com.fcul.marketplace.repository.SubItemRepository;
import com.fcul.marketplace.repository.TransporteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransporteServiceTest {

    @Mock
    TransporteRepository transporteRepository;

    @Mock
    UtilizadorService utilizadorService;

    @Mock
    UniProdService uniProdService;

    @InjectMocks
    TransporteService transporteService;

    @Test
    public void testGetTransporteById() {
        Transporte transporte = new Transporte();
        transporte.setIdTransporte(1);

        when(transporteRepository.findById(anyInt())).thenReturn(Optional.of(transporte));

        assertEquals(transporte,transporteService.getTransporteById(1));
    }


    @Test
    public void testGetTransporteByIdException(){
        Transporte transporte = new Transporte();
        transporte.setIdTransporte(1);

        when(transporteRepository.findById(anyInt())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,()->transporteService.getTransporteById(1));
    }


    @Test
    public void testGetTransportesFornecedor() throws ForbiddenActionException {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setEmail("test@test.com");

        List<Transporte> transportes = new ArrayList<>();
        Transporte transporte = new Transporte();
        transportes.add(transporte);
        Page<Transporte> transportePage = new PageImpl<>(transportes);

        when(utilizadorService.findFornecedorByEmail(anyString())).thenReturn(fornecedor);
        when(transporteRepository.findByFornecedorId(any(),any(),any())).thenReturn(transportePage);

        assertEquals(transportes,transporteService.getTransportesFornecedor("test@test.com",
                null, null, null,null,null,null));
    }

    @Test
    public void testGetTransportesFornecedorException() throws ForbiddenActionException {
        Fornecedor fornecedor1 = new Fornecedor();
        fornecedor1.setIdUtilizador(1);
        fornecedor1.setEmail("test@test.com");

        Fornecedor fornecedor2 = new Fornecedor();
        fornecedor2.setIdUtilizador(2);

        UniProd uniProd = new UniProd();

        uniProd.setFornecedor(fornecedor2);

        when(uniProdService.getUniProdByID(anyInt())).thenReturn(uniProd);
        when(utilizadorService.findFornecedorByEmail(anyString())).thenReturn(fornecedor1);

        assertThrows(ForbiddenActionException.class,()->transporteService.getTransportesFornecedor(
                "test@test.com",3,null, null,null,null,
                null));

    }

    @Test
    public void testAddTransporte() throws ForbiddenActionException {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setIdUtilizador(1);
        fornecedor.setEmail("test@test.com");


        UniProd uniProd = new UniProd();
        Transporte transporte = new Transporte();
        Transporte transporteBD = new Transporte();
        transporteBD.setUnidadeDeProducao(uniProd);
        transporteBD.setActive(true);
        uniProd.setFornecedor(fornecedor);

        when(uniProdService.getUniProdByID(anyInt())).thenReturn(uniProd);
        when(utilizadorService.findFornecedorByEmail(anyString())).thenReturn(fornecedor);
        when(transporteRepository.save(any())).thenReturn(transporteBD);

        assertEquals(transporteBD,transporteService.addTransporte("test@test.com",
                3,transporte));

    }

    @Test
    public void testAddTransporteException() throws ForbiddenActionException {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setIdUtilizador(1);
        fornecedor.setEmail("test@test.com");

        Fornecedor fornecedor2 = new Fornecedor();
        fornecedor2.setIdUtilizador(2);
        fornecedor2.setEmail("test@test.com");

        UniProd uniProd = new UniProd();
        Transporte transporte = new Transporte();
        uniProd.setFornecedor(fornecedor2);

        when(uniProdService.getUniProdByID(anyInt())).thenReturn(uniProd);
        when(utilizadorService.findFornecedorByEmail(anyString())).thenReturn(fornecedor);

        assertThrows(ForbiddenActionException.class,()->transporteService.addTransporte(
                "test@test.com", 3,transporte));

    }





}
