package com.fcul.marketplace.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class ProdutoDTO {

    private Integer idProduto;

    private String nome;

    private Double preco;

    private Double iva;

    private Date dataProducao;


}
