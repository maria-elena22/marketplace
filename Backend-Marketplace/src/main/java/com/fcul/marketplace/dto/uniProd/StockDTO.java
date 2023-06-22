package com.fcul.marketplace.dto.uniProd;

import com.fcul.marketplace.dto.produto.ProdutoDTO;
import lombok.Data;

@Data
public class StockDTO {

    private ProdutoDTO produto;

    private Integer quantidade;
}
