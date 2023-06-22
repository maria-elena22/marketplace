package com.fcul.marketplace.dto.produto;

import com.fcul.marketplace.model.enums.IVA;
import lombok.Data;

@Data
public class ProdutoDTO {

    private String nome;

    private String descricao;

    private IVA iva;

}
