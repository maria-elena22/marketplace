package com.fcul.marketplace.model;

import com.fcul.marketplace.model.enums.EstadoEncomenda;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
public class Encomenda {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idEncomenda;

    @Min(value = 0, message = "O preço tem de ser positivo")
    private Double preco;

    @NotNull(message = "O estado da encomenda é obrigatório ")
    private EstadoEncomenda estadoEncomenda;

    @NotNull(message = "A data da encomenda é obrigatória ")
    private Timestamp dataEncomenda;

    @ManyToOne
    @JoinColumn(name = "consumidor_id")
    private Consumidor consumidor;

    @OneToMany(mappedBy = "encomenda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubEncomenda> subEncomendas;

}
