package com.fcul.marketplace.dto;

import com.fcul.marketplace.dto.encomenda.SubEncomendaNotifDTO;
import com.fcul.marketplace.dto.utilizador.SimpleUtilizadorDTO;
import com.fcul.marketplace.model.enums.TipoNotificacao;
import lombok.Data;

@Data
public class NotificacaoDTO {

    private SubEncomendaNotifDTO subEncomenda;

    private TipoNotificacao tipoNotificacao;

    private String mensagem;

    private SimpleUtilizadorDTO remetente;

    private SimpleUtilizadorDTO destinatario;
}
