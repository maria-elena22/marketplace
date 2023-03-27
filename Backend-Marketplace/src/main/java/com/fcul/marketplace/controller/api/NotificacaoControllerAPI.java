package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.dto.NotificacaoDTO;
import com.fcul.marketplace.dto.ViagemDTO;
import com.fcul.marketplace.model.Encomenda;
import com.fcul.marketplace.model.Item;
import com.fcul.marketplace.model.Notificacao;
import com.fcul.marketplace.model.Viagem;
import com.fcul.marketplace.model.enums.TipoNotificacao;
import com.fcul.marketplace.service.EncomendaService;
import com.fcul.marketplace.service.NotificacaoService;
import com.fcul.marketplace.service.ViagemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notificacao")
public class NotificacaoControllerAPI {

    @Autowired
    NotificacaoService notificacaoService;

    @Autowired
    EncomendaService encomendaService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/{userId}")
    public List<NotificacaoDTO> getNotificacoes(@PathVariable Integer userId){
        List<Notificacao> notificacoes = notificacaoService.getNotificacoes(userId);
        return notificacoes.stream().map(notificacao -> modelMapper.map(notificacao,NotificacaoDTO.class)).collect(Collectors.toList());
    }

    @PutMapping("/{encomendaId}")
    public void generateSaidaTransporteNotificacao(List<Item> itens){
        notificacaoService.generateSaidaTransporteNotificacao(itens);
    }


    @PutMapping("/{encomendaId}")
    public void generateChegadaEncomendaNotificacao(Item item){
        notificacaoService.generateChegadaEncomendaNotificacao(item);
        encomendaService.setItemAsEntregue(item);
    }


}
