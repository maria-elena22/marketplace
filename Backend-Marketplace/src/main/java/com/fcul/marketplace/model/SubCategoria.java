package com.fcul.marketplace.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class SubCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idSubCategoria;

    private String nomeSubCategoria;

    @ManyToOne
    private Categoria categoria;

    @ManyToOne
    private SubCategoria subCategoriaPai;

    @OneToMany(mappedBy = "subCategoriaPai")
    private List<SubCategoria> subCategoriasFilhos;

}
