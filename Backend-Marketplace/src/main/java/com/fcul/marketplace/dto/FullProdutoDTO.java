package com.fcul.marketplace.dto;

import com.fcul.marketplace.model.UniProd;
import lombok.Data;

import java.sql.Date;
import java.util.List;
import java.util.Map;


@Data
public class FullProdutoDTO {

    private Integer idProduto;

    private String nome;

    private Double preco;

    private Double iva;

    private Date dataProducao;

    private List<UniProdDTO> uniProds;

    private List<SubCategoriaDTO> subCategorias;

    private Map<PropriedadeDTO, String> propriedades;

}
