package com.fcul.marketplace.dto.transporte;

import com.fcul.marketplace.dto.uniProd.UniProdIdDTO;
import com.fcul.marketplace.model.enums.EstadoTransporte;
import lombok.Data;

@Data
public class TransporteDTO {

    private Integer idTransporte;
    private String matricula;
    private EstadoTransporte estadoTransporte;
    private UniProdIdDTO unidadeDeProducao;
}
