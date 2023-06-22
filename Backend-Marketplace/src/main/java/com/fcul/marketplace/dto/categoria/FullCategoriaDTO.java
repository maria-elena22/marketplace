package com.fcul.marketplace.dto.categoria;

import lombok.Data;

import java.util.List;

@Data
public class FullCategoriaDTO extends CategoriaDTO {

    private List<PropriedadeDTO> propriedadesList;

    private List<SimpleSubCategoriaDTO> subCategoriaList;


}
