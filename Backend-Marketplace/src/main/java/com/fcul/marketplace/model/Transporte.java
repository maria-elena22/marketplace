package com.fcul.marketplace.model;

import com.fcul.marketplace.model.enums.EstadoTransporte;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
public class Transporte {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idTransporte;

    @NotBlank
    private String matricula;

    @NotNull
    private EstadoTransporte estadoTransporte;

    @OneToMany(mappedBy = "transporte", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Viagem> viagens;

    @ManyToOne
    @JoinColumn(name = "uniProd")
    private UniProd unidadeDeProducao;
}
