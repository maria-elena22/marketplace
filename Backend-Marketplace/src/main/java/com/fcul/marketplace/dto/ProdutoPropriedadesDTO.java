package com.fcul.marketplace.dto;

import lombok.Data;

import java.sql.Date;
import java.util.Map;

@Data
public class ProdutoPropriedadesDTO {

    private ProdutoDTO produtoDTO;

    private Map<Integer, String> propriedades;

}
