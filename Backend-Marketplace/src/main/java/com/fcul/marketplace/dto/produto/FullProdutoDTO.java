package com.fcul.marketplace.dto.produto;

import com.fcul.marketplace.dto.categoria.PropriedadeDTO;
import com.fcul.marketplace.dto.categoria.SubCategoriaDTO;
import com.fcul.marketplace.dto.uniProd.ProdutoUniProdDTO;
import com.fcul.marketplace.model.enums.IVA;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class FullProdutoDTO {

    private Integer idProduto;

    private String nome;

    private String descricao;

    private IVA iva;

    private List<ProdutoUniProdDTO> uniProds;

    private List<SubCategoriaDTO> subCategorias;

    private Map<PropriedadeDTO, String> propriedades;

    private List<ProdutoFornecedorInfoDTO> precoFornecedores;

}
