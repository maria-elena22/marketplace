package com.fcul.marketplace.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Categoria {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idCategoria;

    private String nomeCategoria;

    @ManyToMany()
    @JoinTable(joinColumns = @JoinColumn(name = "id_categoria"),
            inverseJoinColumns = @JoinColumn(name = "id_propriedade"))
    private List<Propriedade> propriedades;

    @OneToMany(mappedBy = "categoria")
    private List<SubCategoria> subCategorias;
}
