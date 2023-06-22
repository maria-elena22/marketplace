package com.fcul.marketplace.dto.produto;

import lombok.Data;

@Data
public class ProdutoPrecoDTO {

    private ProdutoDTO produtoDTO;
    private Double preco;
}
