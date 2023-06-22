package com.fcul.marketplace.dto.categoria;

import lombok.Data;

import java.util.List;

@Data
public class SubCategoriaDTO {

    private Integer idSubCategoria;

    private String nomeSubCategoria;

    private CategoriaDTO categoria;

    private List<SimpleSubCategoriaDTO> subCategoriasFilhos;


}
