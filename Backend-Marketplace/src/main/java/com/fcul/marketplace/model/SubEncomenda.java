package com.fcul.marketplace.model;

import com.fcul.marketplace.model.enums.EstadoEncomenda;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Data
public class SubEncomenda {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idSubEncomenda;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    private Double preco;

    private EstadoEncomenda estadoEncomenda;

    private Date dataEncomenda;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Item> items;

    @ManyToOne
    @JoinColumn(name = "encomenda_id")
    private Encomenda encomenda;
}
