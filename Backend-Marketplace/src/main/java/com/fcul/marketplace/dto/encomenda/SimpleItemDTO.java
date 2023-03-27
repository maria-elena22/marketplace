package com.fcul.marketplace.dto.encomenda;

import lombok.Data;

@Data
public class SimpleItemDTO {

    private Integer idItem;

    private Integer produtoId;

    private Integer fornecedorId;

    private Integer quantidade;
}
