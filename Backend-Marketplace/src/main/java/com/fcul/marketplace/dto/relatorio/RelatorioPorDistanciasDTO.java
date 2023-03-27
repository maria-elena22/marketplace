package com.fcul.marketplace.dto.relatorio;

import lombok.Data;

import java.util.Map;

@Data
public class RelatorioPorDistanciasDTO {


    //Key Representa a gama de distancia(em km)
    //Value representa a quantidade de encomendas registadas com distancia ate a gama {key}
    Map<Integer, Integer> GamaDistanciasQuantidadeEncomendasMap;
}
