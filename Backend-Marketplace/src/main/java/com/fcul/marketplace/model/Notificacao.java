package com.fcul.marketplace.model;

import com.fcul.marketplace.model.enums.TipoNotificacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idNotificacao;

    private String mensagem;

    @ManyToOne
    @JoinColumn(name = "encomenda_id")
    private Encomenda encomenda;

    @Enumerated(value = EnumType.STRING)
    private TipoNotificacao tipoNotificacao;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Utilizador utilizador;

    private Boolean entregue = false;

}
