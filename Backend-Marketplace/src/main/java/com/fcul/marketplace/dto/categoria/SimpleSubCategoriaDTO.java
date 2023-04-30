package com.fcul.marketplace.dto.categoria;

import lombok.Data;

import java.util.List;

@Data
public class SimpleSubCategoriaDTO {

    private Integer idSubCategoria;

    private String nomeSubCategoria;

    private List<SimpleSubCategoriaDTO> subCategoriasFilhos;


}
