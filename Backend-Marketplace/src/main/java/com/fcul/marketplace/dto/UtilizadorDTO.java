package com.fcul.marketplace.dto;

import com.fcul.marketplace.model.utils.Coordinate;
import com.fcul.marketplace.model.enums.Continente;
import lombok.Data;

import java.util.Locale;

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

    private Locale.IsoCountryCode pais;

    private Continente continente;
}
