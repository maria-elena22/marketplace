package com.fcul.marketplace.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Produto {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idProduto;

    private String nome;

    private Double preco;

    private Double iva;

    private Date dataProducao;

    @ManyToMany()
    @JoinTable(joinColumns = @JoinColumn(name = "id_produto"),
            inverseJoinColumns = @JoinColumn(name = "id_subcategoria"))
    private List<SubCategoria> subCategorias;




}
