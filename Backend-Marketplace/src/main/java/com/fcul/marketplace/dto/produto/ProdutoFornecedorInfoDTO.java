package com.fcul.marketplace.dto.produto;

import com.fcul.marketplace.dto.utilizador.SimpleUtilizadorDTO;
import com.fcul.marketplace.dto.utilizador.UtilizadorCoordsDTO;
import lombok.Data;

@Data
public class ProdutoFornecedorInfoDTO {
    private UtilizadorCoordsDTO fornecedor;
    private Double preco;
}
