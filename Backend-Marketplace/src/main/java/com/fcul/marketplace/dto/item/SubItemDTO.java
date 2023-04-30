package com.fcul.marketplace.dto.item;

import lombok.Data;

@Data
public class SubItemDTO {

    private Integer idSubItem;

    private ItemViagemDTO item;

    private Integer quantidade;


}