package com.fcul.marketplace.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRawValue;
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
    @JoinColumn(name="categoriaId")
    private Categoria categoria;

}
