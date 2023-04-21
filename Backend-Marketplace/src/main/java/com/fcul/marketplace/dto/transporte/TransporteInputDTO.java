package com.fcul.marketplace.dto.transporte;

import com.fcul.marketplace.model.enums.EstadoTransporte;
import lombok.Data;

@Data
public class TransporteInputDTO {

    private String matricula;
    private EstadoTransporte estadoTransporte;

}
