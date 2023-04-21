package com.fcul.marketplace.dto.encomenda;

import com.fcul.marketplace.model.enums.EstadoEncomenda;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class FullEncomendaDTO {

    private Integer idEncomenda;

    private Double preco;

    private Date dataEncomenda;

    private EstadoEncomenda estadoEncomenda;

}
