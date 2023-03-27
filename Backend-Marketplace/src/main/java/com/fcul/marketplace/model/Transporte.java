package com.fcul.marketplace.model;

import com.fcul.marketplace.model.enums.EstadoTransporte;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Transporte {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idTransporte;

    private String matricula;

    private EstadoTransporte estadoTransporte;

    @OneToMany(mappedBy = "transporte", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Viagem> viagens;

    @ManyToOne
    private Fornecedor fornecedor;
}
