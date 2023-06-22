package com.fcul.marketplace.model;


import com.fcul.marketplace.model.annotations.Unique;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idCategoria;

    @Unique(message = "O nome da categoria já se encontra em uso")
    @NotBlank(message = "O nome da categoria não pode estar vazio")
    private String nomeCategoria;


    @ManyToMany(mappedBy = "categorias")
    private List<Propriedade> propriedades;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<SubCategoria> subCategorias;
}

