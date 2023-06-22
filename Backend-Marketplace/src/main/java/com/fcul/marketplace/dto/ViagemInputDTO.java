package com.fcul.marketplace.dto;

import com.fcul.marketplace.dto.item.SubItemViagemDTO;
import com.fcul.marketplace.dto.transporte.SimpleTransporteDTO;
import lombok.Data;

import java.util.List;

@Data
public class ViagemInputDTO {

    private SimpleTransporteDTO transporte;
    private List<SubItemViagemDTO> subItems;


}
