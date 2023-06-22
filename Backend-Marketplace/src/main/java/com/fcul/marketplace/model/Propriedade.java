package com.fcul.marketplace.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Propriedade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idPropriedade;

    private String nomePropriedade;

    @ManyToMany
    private List<Categoria> categorias;

    public void addCategoria(Categoria categoria) {
        if (categorias == null) {
            categorias = new ArrayList<>();
        }
        categorias.add(categoria);
    }

    public void removeCategoria(Categoria categoria) {
        if (categorias != null) {
            categorias.remove(categoria);
        }
    }
}
