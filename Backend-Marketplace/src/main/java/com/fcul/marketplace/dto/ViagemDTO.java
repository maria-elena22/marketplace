package com.fcul.marketplace.dto;

import com.fcul.marketplace.dto.item.SubItemDTO;
import com.fcul.marketplace.dto.transporte.SimpleTransporteDTO;
import com.fcul.marketplace.model.enums.EstadoViagem;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ViagemDTO {

    private Timestamp dataInicio;
    private Timestamp dataFim;
    private EstadoViagem estadoViagem;
    private SimpleTransporteDTO transporte;
    private List<SubItemDTO> subItems;


}
