package com.fcul.marketplace.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Data
public class UnidadeProducao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idUnidade;

    private String regiao;

    @ManyToMany
    private List<Fornecedor> fornecedores;

    @ManyToMany
    private List<Produto> produtos;

}
