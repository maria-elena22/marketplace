package com.fcul.marketplace.dto.relatorio;

import lombok.Data;

import java.util.Map;

@Data
public class RelatorioPorZonasDTO {

    //Key Representa o nome da zona
    //Value representa a quantidade de encomendas registadas na zona {key}
    Map<String, Integer> mapEncomendasFreguesias;
    Map<String, Integer> mapEncomendasMunicipio;
    Map<String, Integer> mapEncomendasDistrito;
    Map<String, Integer> mapEncomendasPais;
    Map<String, Integer> mapEncomendasContinente;
    //Representa a quantidade de encomendas feitas no mundo
    Integer totalDeEncomendas;
}
