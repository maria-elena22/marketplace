package com.fcul.marketplace.model;

import com.fcul.marketplace.model.enums.EstadoEncomenda;
import com.fcul.marketplace.model.enums.Pagamento;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Data
public class Encomenda {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idEncomenda;

    private Integer quantidade;

    private Double preco;

    private Date dataEntrega;

    private EstadoEncomenda estadoEncomenda;

    private Pagamento tipoPagamento;

    @ManyToOne
    private Consumidor consumidor;

    @ManyToMany
    private List<Fornecedor> fornecedores;

    @OneToMany
    private List<Produto> produtos;

}
