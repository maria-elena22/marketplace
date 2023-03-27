package com.fcul.marketplace.dto;

import com.fcul.marketplace.model.enums.EstadoTransporte;
import lombok.Data;

@Data
public class TransporteDTO {

    private Integer idTransporte;
    private String matricula;
    private EstadoTransporte estadoTransporte;

}
