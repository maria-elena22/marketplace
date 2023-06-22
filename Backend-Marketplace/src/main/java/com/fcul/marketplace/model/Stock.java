package com.fcul.marketplace.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {

    @ManyToOne()
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private Integer quantidade;
}
