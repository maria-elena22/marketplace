package com.fcul.marketplace.model;

import com.fcul.marketplace.model.enums.EstadoEncomenda;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@Entity
@Data
public class Encomenda {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idEncomenda;

    private Double preco;

    private EstadoEncomenda estadoEncomenda;

    private Date dataEncomenda;

    @ManyToOne
    @JoinColumn(name = "consumidor_id")
    private Consumidor consumidor;

    @OneToMany(mappedBy = "encomenda", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<SubEncomenda> subEncomendas;



}
