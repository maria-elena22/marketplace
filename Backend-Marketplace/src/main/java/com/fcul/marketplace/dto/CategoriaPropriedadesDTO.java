package com.fcul.marketplace.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoriaPropriedadesDTO extends CategoriaDTO{

    private List<PropriedadeDTO> propriedadesList;

}
