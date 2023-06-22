package com.fcul.marketplace.dto.encomenda;

import com.fcul.marketplace.dto.EncomendaNotifDTO;
import com.fcul.marketplace.model.enums.EstadoEncomenda;
import lombok.Data;

@Data
public class SubEncomendaNotifDTO {

    private Integer idSubEncomenda;

    private EstadoEncomenda estadoEncomenda;

    private EncomendaNotifDTO encomenda;

}
