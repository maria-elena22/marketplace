package com.fcul.marketplace.dto;

import lombok.Data;

@Data
public class UtilizadorDTO {

    private Integer idUtilizador;

    private Integer idFiscal;

    private String nome;

    private Integer telemovel;

    private String coordenadas;

    private String morada;

    private String freguesia;

    private String municipio;

    private String distrito;

    private String pais;

    private String continente;
}
