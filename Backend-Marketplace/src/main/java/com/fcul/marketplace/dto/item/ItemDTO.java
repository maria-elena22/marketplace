package com.fcul.marketplace.dto.item;

import lombok.Data;

@Data
public class ItemDTO {

    private Integer idItem;

    private Integer produtoId;

    private Integer quantidade;

    private boolean entregue;

}