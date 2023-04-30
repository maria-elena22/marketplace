package com.fcul.marketplace.dto.utilizador;

import com.fcul.marketplace.model.enums.Continente;
import com.fcul.marketplace.model.enums.Pais;
import com.fcul.marketplace.model.utils.Coordinate;
import lombok.Data;

@Data
public class SignUpDTO {

    private Integer idFiscal;

    private String nome;

    private Integer telemovel;

    private Coordinate coordenadas;

    private String morada;

    private String email;

    private String password;

    private String freguesia;

    private String municipio;

    private String distrito;

    private Pais pais;

    private Continente continente;

}
