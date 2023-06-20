package com.fcul.marketplace.service;

import com.fcul.marketplace.exceptions.ForbiddenActionException;
import com.fcul.marketplace.model.*;
import com.fcul.marketplace.repository.EncomendaRepository;
import com.fcul.marketplace.repository.SubEncomendaRepository;
import com.fcul.marketplace.repository.SubItemRepository;
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
    EncomendaRepository encomendaRepository;

    @Mock
    SubEncomendaRepository subEncomendaRepository;

    @Mock
    SubItemRepository subItemRepository;

    @Mock
    UtilizadorService utilizadorService;


    @InjectMocks
    EncomendaService encomendaService;

    @Test
    public void testGetSubEncomendaById() throws ForbiddenActionException {
        Fornecedor fornecedor = new Fornecedor();
        SubEncomenda subEncomenda = new SubEncomenda();
        subEncomenda.setFornecedor(fornecedor);

        when(utilizadorService.findFornecedorByEmail(anyString())).thenReturn(fornecedor);
        when(subEncomendaRepository.findById(anyInt())).thenReturn(Optional.of(subEncomenda));

        assertEquals(subEncomenda,encomendaService.getSubEncomendaById("test@test.com",2));
    }


    @Test
    public void testGetSubEncomendaByIdException(){
        Fornecedor fornecedor1 = new Fornecedor();
        fornecedor1.setActive(true);
        SubEncomenda subEncomenda =new SubEncomenda();
        Fornecedor fornecedor2 = new Fornecedor();
        fornecedor2.setActive(false);
        subEncomenda.setFornecedor(fornecedor2);

        when(utilizadorService.findFornecedorByEmail(anyString())).thenReturn(fornecedor1);
        when(subEncomendaRepository.findById(anyInt())).thenReturn(Optional.of(subEncomenda));

        assertThrows(ForbiddenActionException.class, ()->encomendaService.getSubEncomendaById("test@test.com",2));
    }


    @Test
    public void testGetEncomendas(){
        Consumidor consumidor = new Consumidor();
        consumidor.setIdUtilizador(1);
        List<Encomenda> encomendas = new ArrayList<>();
        Encomenda encomenda = new Encomenda();
        encomendas.add(encomenda);

        when(utilizadorService.findConsumidorByEmail(anyString())).thenReturn(consumidor);
        when(encomendaRepository.findAllOpt(anyInt(),anyDouble(),anyDouble(),any(),any(),any(),any())).thenReturn(encomendas);

        assertEquals(encomendas,encomendaService.getEncomendas("test@test.com",1.0,1.0, null,null,null,null, null, null,null));
    }


    @Test
    public void testGetAllSubEncomendas(){
        List<SubEncomenda> subEncomendas = new ArrayList<>();
        SubEncomenda subEncomenda = new SubEncomenda();
        subEncomendas.add(subEncomenda);

        when(subEncomendaRepository.findAll()).thenReturn(subEncomendas);

        assertEquals(subEncomendas,encomendaService.getAllSubEncomendas());
    }

    @Test
    public void testGetEncomendaById() throws ForbiddenActionException {
        Consumidor consumidor = new Consumidor();

        Encomenda encomenda = new Encomenda();
        List<Encomenda> encomendas = new ArrayList<>();
        encomendas.add(encomenda);
        consumidor.setEncomendas(encomendas);

        when(utilizadorService.findConsumidorByEmail(anyString())).thenReturn(consumidor);
        when(encomendaRepository.findById(anyInt())).thenReturn(Optional.of(encomenda));

        assertEquals(encomenda,encomendaService.getEncomendaById("test@test.com",2));
    }


    @Test
    public void testGetEncomendaByIdException(){
        Consumidor consumidor1 = new Consumidor();
        Encomenda encomenda =new Encomenda();
        List<Encomenda> encomendas = new ArrayList<>();
        consumidor1.setEncomendas(encomendas);

        Consumidor consumidor2 = new Consumidor();
        encomenda.setConsumidor(consumidor2);

        when(utilizadorService.findConsumidorByEmail(anyString())).thenReturn(consumidor1);
        when(encomendaRepository.findById(anyInt())).thenReturn(Optional.of(encomenda));

        assertThrows(ForbiddenActionException.class, ()->encomendaService.getEncomendaById("test@test.com",2));
    }


    @Test
    public void testGetSubItemById() throws ForbiddenActionException {
        Fornecedor fornecedor = new Fornecedor();

        SubItem subItem = new SubItem();
        Item item = new Item();
        SubEncomenda subEncomenda = new SubEncomenda();
        subEncomenda.setFornecedor(fornecedor);
        item.setSubEncomenda(subEncomenda);
        subItem.setItem(item);


        when(utilizadorService.findFornecedorByEmail(anyString())).thenReturn(fornecedor);
        when(subItemRepository.findById(anyInt())).thenReturn(Optional.of(subItem));

        assertEquals(subItem,encomendaService.getSubItemById("test@test.com",2));
    }


    @Test
    public void testGetSubItemByIdException(){
        Fornecedor fornecedor1 = new Fornecedor();
        fornecedor1.setActive(true);
        Fornecedor fornecedor2 = new Fornecedor();

        SubEncomenda subEncomenda = new SubEncomenda();
        SubItem subItem = new SubItem();
        Item item = new Item();

        subEncomenda.setFornecedor(fornecedor2);
        item.setSubEncomenda(subEncomenda);
        subItem.setItem(item);

        when(utilizadorService.findFornecedorByEmail(anyString())).thenReturn(fornecedor1);
        when(subItemRepository.findById(anyInt())).thenReturn(Optional.of(subItem));

        assertThrows(ForbiddenActionException.class, ()->encomendaService.getSubItemById("test@test.com",2));
    }


    @Test
    public void testGetSubEncomendas(){
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setIdUtilizador(1);

        List<SubEncomenda> subEncomendas = new ArrayList<>();
        SubEncomenda subEncomenda = new SubEncomenda();
        subEncomendas.add(subEncomenda);

        when(utilizadorService.findFornecedorByEmail(anyString())).thenReturn(fornecedor);
        when(subEncomendaRepository.findAllOpt(anyInt(),anyDouble(),anyDouble(),any(),any(),any(),any())).thenReturn(subEncomendas);

        assertEquals(subEncomendas,encomendaService.getSubEncomendas("test@test.com",1.0,1.0, null,null,null,null, null, null,null));
    }

    @Test
    public void testGetSubEncomendasByFornecedorEmail(){
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setEmail("test@test.com");

        List<SubEncomenda> subEncomendas = new ArrayList<>();
        SubEncomenda subEncomenda = new SubEncomenda();
        subEncomenda.setFornecedor(fornecedor);
        subEncomendas.add(subEncomenda);

        when(subEncomendaRepository.findByFornecedorEmail(anyString())).thenReturn(subEncomendas);

        assertEquals(subEncomendas,encomendaService.getSubEncomendasByFornecedorEmail("test@test.com"));
    }

    @Test
    public void testGetSubEncomendasByConsumidorEmail(){
        Consumidor consumidor = new Consumidor();
        consumidor.setEmail("test@test.com");

        List<SubEncomenda> subEncomendas = new ArrayList<>();
        SubEncomenda subEncomenda = new SubEncomenda();
        Encomenda encomenda = new Encomenda();
        encomenda.setConsumidor(consumidor);
        subEncomenda.setEncomenda(encomenda);
        subEncomendas.add(subEncomenda);

        when(subEncomendaRepository.findByEncomendaConsumidorEmail(anyString())).thenReturn(subEncomendas);

        assertEquals(subEncomendas,encomendaService.getSubEncomendasByConsumidorEmail("test@test.com"));
    }

    @Test
    public void testGetSubItensNaoEntregues(){
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setIdUtilizador(1);

        Page<SubItem> subItems = new PageImpl<>(new ArrayList<>());

        when(utilizadorService.findFornecedorByEmail(anyString())).thenReturn(fornecedor);
        when(subItemRepository.getSubItensNaoEntregues(anyInt(),any())).thenReturn(subItems);

        assertEquals(subItems.getContent(),encomendaService.getSubItensNaoEntregues("test@test.com",null,null,null,null));
    }


}
