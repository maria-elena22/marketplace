package com.fcul.marketplace.model;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.List;

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

    @Min(value = 0, message = "Quantidade nao pode ser negativa")
    private Integer quantidade;

    private Boolean entregue = false;

    @OneToMany(mappedBy = "item")
    private List<SubItem> subItems;

}
