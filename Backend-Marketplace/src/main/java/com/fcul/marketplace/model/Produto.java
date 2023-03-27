package com.fcul.marketplace.model;

import com.fcul.marketplace.model.enums.IVA;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idProduto;

    private String nome;

    private Double preco;

    @Enumerated(EnumType.STRING)
    private IVA vatValue;

    private Date dataProducao;

    @ManyToMany()
    @JoinTable(joinColumns = @JoinColumn(name = "id_produto"),
            inverseJoinColumns = @JoinColumn(name = "id_subcategoria"))
    private List<SubCategoria> subCategorias;

    @ManyToMany()
    @JoinTable(joinColumns = @JoinColumn(name = "id_produto"),
            inverseJoinColumns = @JoinColumn(name = "id_unidade"))
    private List<UniProd> uniProds;

    @ElementCollection
    @MapKeyColumn(name = "propriedade")
    @Column(name = "valor_propriedade")
    @CollectionTable(name = "produto_propriedades", joinColumns = {@JoinColumn(name = "produto_id",
            referencedColumnName = "idProduto")})
    private Map<Propriedade, String> propriedades;


}
