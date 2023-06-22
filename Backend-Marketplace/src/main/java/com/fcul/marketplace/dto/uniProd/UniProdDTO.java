package com.fcul.marketplace.dto.uniProd;


import lombok.Data;

import java.util.List;

@Data
public class UniProdDTO {
    private Integer idUnidade;

    private String nomeUniProd;

    private List<StockDTO> stocks;

}
