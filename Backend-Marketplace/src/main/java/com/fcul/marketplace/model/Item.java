package com.fcul.marketplace.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idItem;

    @ManyToOne
    private SubEncomenda subEncomenda;

    @ManyToOne
    private Produto produto;

    private Integer quantidade;

    private Boolean entregue = false;
}
