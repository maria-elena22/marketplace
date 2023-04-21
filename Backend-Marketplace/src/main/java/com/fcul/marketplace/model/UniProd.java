package com.fcul.marketplace.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

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

    @ManyToMany(mappedBy = "uniProds")
    private List<Produto> produtos;

    @OneToMany(mappedBy = "unidadeDeProducao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transporte> transportes;



    @ElementCollection
    @MapKeyColumn(name = "produto")
    @Column(name = "stock")
    @CollectionTable(name = "stock", joinColumns = {@JoinColumn(name = "unidade_producao_id",
            referencedColumnName = "idUnidade")})
    private Map<Produto, Integer> produtoStockInteger;

}
