package com.fcul.marketplace.dto.produto;

import com.fcul.marketplace.dto.categoria.PropriedadeDTO;
import com.fcul.marketplace.dto.categoria.SubCategoriaDTO;
import com.fcul.marketplace.model.enums.IVA;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class ProdutoConsumidorDTO {

    private Integer idProduto;

    private String nome;

    private IVA iva;

    private List<SubCategoriaDTO> subCategorias;

    private Map<PropriedadeDTO, String> propriedades;

    private List<ProdutoFornecedorInfoDTO> precoFornecedores;

}
