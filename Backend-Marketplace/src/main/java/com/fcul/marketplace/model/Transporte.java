package com.fcul.marketplace.model;

import com.fcul.marketplace.model.enums.EstadoTransporte;
import com.fcul.marketplace.model.enums.Pagamento;
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

    @OneToMany
    private List<Viagem> viagens;

    @ManyToOne
    private Fornecedor fornecedor;
}
