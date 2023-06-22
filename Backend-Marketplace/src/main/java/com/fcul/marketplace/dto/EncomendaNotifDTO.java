package com.fcul.marketplace.dto;

import com.fcul.marketplace.dto.utilizador.SimpleUtilizadorDTO;
import com.fcul.marketplace.model.enums.EstadoEncomenda;
import lombok.Data;

@Data
public class EncomendaNotifDTO {

    private SimpleUtilizadorDTO consumidor;

    private Integer idEncomenda;


}
