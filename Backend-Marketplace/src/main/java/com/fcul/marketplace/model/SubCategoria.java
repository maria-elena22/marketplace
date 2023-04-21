package com.fcul.marketplace.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SubCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idSubCategoria;

    private String nomeSubCategoria;

    @ManyToOne
    private Categoria categoria;

}
