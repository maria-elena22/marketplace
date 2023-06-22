package com.fcul.marketplace.dto.item;

import com.fcul.marketplace.dto.produto.ProdutoDTO;
import lombok.Data;

@Data
public class ItemViagemDTO {

    private Integer idItem;
    private ProdutoDTO produto;


}