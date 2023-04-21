package com.fcul.marketplace.dto.encomenda;

import com.fcul.marketplace.utils.ChargeRequest;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class CompraDTO {

    private Double preco;

    private Date dataEncomenda;

    List<SimpleItemDTO> items;
}
