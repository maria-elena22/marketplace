package com.fcul.marketplace.dto.utilizador;

import com.fcul.marketplace.model.utils.Coordinate;
import lombok.Data;

@Data
public class UtilizadorCoordsDTO {

    private Integer idUtilizador;

    private String nome;

    private String email;

    private Coordinate coordenadas;

}
