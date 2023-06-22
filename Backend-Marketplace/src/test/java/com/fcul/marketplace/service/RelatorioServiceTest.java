package com.fcul.marketplace.service;

import com.fcul.marketplace.dto.relatorio.RelatorioPorDistanciasDTO;
import com.fcul.marketplace.dto.relatorio.RelatorioPorZonasDTO;
import com.fcul.marketplace.exceptions.ForbiddenActionException;
import com.fcul.marketplace.model.*;
import com.fcul.marketplace.model.enums.Continente;
import com.fcul.marketplace.model.enums.Pais;
import com.fcul.marketplace.model.utils.Coordinate;
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

import java.text.ParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RelatorioServiceTest {

    @Mock
    EncomendaService encomendaService;

    @InjectMocks
    RelatorioService relatorioService;

    @Test
    public void testGenerateRelatorioZonas() throws ForbiddenActionException, ParseException {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setDistrito("a");
        fornecedor.setFreguesia("a");
        fornecedor.setMunicipio("a");
        fornecedor.setContinente(Continente.EUROPA);
        fornecedor.setPais(Pais.PORTUGAL);


        SubEncomenda subEncomenda = new SubEncomenda();
        subEncomenda.setFornecedor(fornecedor);
        List<SubEncomenda> subEncomendas = new ArrayList<>();
        subEncomendas.add(subEncomenda);
        subEncomenda.setFornecedor(fornecedor);

        when(encomendaService.getAllSubEncomendas()).thenReturn(subEncomendas);
        Map<String, Long> mapEncomendasFreguesias = new HashMap<>();
        mapEncomendasFreguesias.put("a", 1L);
        Map<String, Long> mapEncomendasMunicipio = new HashMap<>();
        mapEncomendasMunicipio.put("a", 1L);
        Map<String, Long> mapEncomendasDistrito = new HashMap<>();
        mapEncomendasDistrito.put("a", 1L);

        Map<String, Long> mapEncomendasPais = new HashMap<>();
        mapEncomendasPais.put("PORTUGAL", 1L);

        Map<String, Long> mapEncomendasContinente = new HashMap<>();
        mapEncomendasContinente.put("EUROPA", 1L);

        RelatorioPorZonasDTO relatorioPorZonasDTO = new RelatorioPorZonasDTO();

        relatorioPorZonasDTO.setMapEncomendasContinente(mapEncomendasContinente);
        relatorioPorZonasDTO.setMapEncomendasFreguesias(mapEncomendasFreguesias);
        relatorioPorZonasDTO.setMapEncomendasMunicipio(mapEncomendasMunicipio);
        relatorioPorZonasDTO.setMapEncomendasDistrito(mapEncomendasDistrito);
        relatorioPorZonasDTO.setMapEncomendasPais(mapEncomendasPais);
        relatorioPorZonasDTO.setTotalDeEncomendas(1);

        assertEquals(relatorioPorZonasDTO,relatorioService.generateRelatorioZonas("test@test.com","ADMIN", null,null,null));
    }


    @Test
    public void testGenerateRelatorioDistancias() throws ForbiddenActionException, ParseException {
        Fornecedor fornecedor = new Fornecedor();
        Coordinate coordenadasF = new Coordinate();
        coordenadasF.setLatitude(0);
        coordenadasF.setLongitude(0);
        fornecedor.setCoordenadas(coordenadasF);


        Consumidor consumidor = new Consumidor();
        Coordinate coordenadasC = new Coordinate();
        coordenadasC.setLatitude(0);
        coordenadasC.setLongitude(0);
        consumidor.setCoordenadas(coordenadasC);


        SubEncomenda subEncomenda = new SubEncomenda();
        Encomenda encomenda = new Encomenda();
        encomenda.setConsumidor(consumidor);
        subEncomenda.setFornecedor(fornecedor);
        subEncomenda.setEncomenda(encomenda);
        List<SubEncomenda> subEncomendas = new ArrayList<>();
        subEncomendas.add(subEncomenda);
        subEncomenda.setFornecedor(fornecedor);


        when(encomendaService.getAllSubEncomendas()).thenReturn(subEncomendas);


        RelatorioPorDistanciasDTO relatorioPorDistanciasDTO = new RelatorioPorDistanciasDTO();
        relatorioPorDistanciasDTO.setGamaDistanciasQuantidadeEncomendasMap(new HashMap<>());

        relatorioPorDistanciasDTO.getGamaDistanciasQuantidadeEncomendasMap().put("<10",1);
        relatorioPorDistanciasDTO.getGamaDistanciasQuantidadeEncomendasMap().put("10-100",0);
        relatorioPorDistanciasDTO.getGamaDistanciasQuantidadeEncomendasMap().put("100-1000",0);
        relatorioPorDistanciasDTO.getGamaDistanciasQuantidadeEncomendasMap().put(">1000",0);

        assertEquals(relatorioPorDistanciasDTO,relatorioService.generateRelatorioDistancias("test@test.com","ADMIN", null,null,null));
    }



}
