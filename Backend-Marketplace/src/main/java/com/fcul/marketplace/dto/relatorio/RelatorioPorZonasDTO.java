package com.fcul.marketplace.dto.relatorio;

import lombok.Data;

import java.util.Map;

@Data
public class RelatorioPorZonasDTO {

    //Key Representa o nome da zona
    //Value representa a quantidade de encomendas registadas na zona {key}
    Map<String, Long> mapEncomendasFreguesias;
    Map<String, Long> mapEncomendasMunicipio;
    Map<String, Long> mapEncomendasDistrito;
    Map<String, Long> mapEncomendasPais;
    Map<String, Long> mapEncomendasContinente;
    //Representa a quantidade de encomendas feitas no mundo
    Integer totalDeEncomendas;
}
