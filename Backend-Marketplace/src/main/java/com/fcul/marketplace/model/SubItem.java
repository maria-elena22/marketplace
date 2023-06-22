package com.fcul.marketplace.model;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Data
public class SubItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idSubItem;

    @ManyToOne
    private Item item;

    @ManyToOne
    private Viagem viagem;

    @Min(value = 0, message = "Quantidade nao pode ser negativa")
    private Integer quantidade;

    private Boolean entregue = false;
}
