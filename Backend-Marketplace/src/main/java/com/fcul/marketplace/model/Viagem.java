package com.fcul.marketplace.model;


import com.fcul.marketplace.model.enums.EstadoViagem;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idViagem;

    private Timestamp dataInicio;

    private Timestamp dataFim;

    private EstadoViagem estadoViagem;

    @ManyToOne
    private Transporte transporte;

    @OneToMany(mappedBy = "viagem")
    private List<SubItem> subItems;

}
