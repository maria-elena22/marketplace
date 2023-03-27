package com.fcul.marketplace.dto;

import lombok.Data;

import java.util.List;

@Data
public class FullCategoriaDTO extends CategoriaDTO {

    private List<PropriedadeDTO> propriedadesList;

    private List<SubCategoriaDTO> subCategoriaList;


}
