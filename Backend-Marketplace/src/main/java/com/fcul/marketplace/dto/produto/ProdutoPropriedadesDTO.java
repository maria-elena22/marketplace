package com.fcul.marketplace.dto.produto;

import lombok.Data;

import java.util.Map;

@Data
public class ProdutoPropriedadesDTO {

    private Integer Stock;

    private ProdutoDTO produtoDTO;

    private Map<Integer, String> propriedades;

}
