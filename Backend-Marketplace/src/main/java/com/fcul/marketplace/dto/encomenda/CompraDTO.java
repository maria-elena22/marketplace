package com.fcul.marketplace.dto.encomenda;

import com.fcul.marketplace.dto.item.SimpleItemDTO;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class CompraDTO {

    List<SimpleItemDTO> items;
    private Double preco;
}
