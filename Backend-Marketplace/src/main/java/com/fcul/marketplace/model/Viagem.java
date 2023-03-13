package com.fcul.marketplace.model;


import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Data
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idViagem;

    @ManyToOne
    private Transporte transporte;

    private Date dataInicio;

    private Date dataFim;

    @OneToMany
    private List<Encomenda> encomendas;

}
