package com.fcul.marketplace.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "UnidadeProducao")
@Data
public class UniProd {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idUnidade;

    @NotBlank
    private String nomeUniProd;

    @ManyToOne
    @JoinColumn(name = "fornecedorId")
    private Fornecedor fornecedor;

    @ManyToMany(mappedBy = "uniProds", cascade = CascadeType.PERSIST)
    private List<Produto> produtos;

    @OneToMany(mappedBy = "unidadeDeProducao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transporte> transportes;

    @ElementCollection
    private List<Stock> stocks;

    public void addStock(Stock stock1) {
        if (stocks == null) {
            stocks = new ArrayList<>();
        }
        stocks.add(stock1);
    }
}
