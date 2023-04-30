package com.fcul.marketplace.dto.item;

import lombok.Data;

@Data
public class ItemInfoDTO {

    private Integer idItem;

    private String produtoNome;

    private Integer quantidade;

    private Integer quantidadeDespachada;

    private Integer quantidadeStock;

}