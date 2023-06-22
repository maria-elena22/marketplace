package com.fcul.marketplace.dto.encomenda;

import com.fcul.marketplace.model.enums.EstadoEncomenda;
import lombok.Data;

import java.sql.Date;

@Data
public class EncomendaDTO {

    private Integer idEncomenda;

    private Double preco;

    private Date dataEncomenda;

    private EstadoEncomenda estadoEncomenda;


}
