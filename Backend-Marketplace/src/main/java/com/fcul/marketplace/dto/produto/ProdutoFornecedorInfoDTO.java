package com.fcul.marketplace.dto.produto;

import com.fcul.marketplace.dto.utilizador.SimpleUtilizadorDTO;
import lombok.Data;

@Data
public class ProdutoFornecedorInfoDTO {
    private SimpleUtilizadorDTO fornecedor;
    private Double preco;
}
