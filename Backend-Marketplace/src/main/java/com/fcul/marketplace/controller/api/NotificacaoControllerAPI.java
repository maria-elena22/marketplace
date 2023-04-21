package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.config.security.SecurityUtils;
import com.fcul.marketplace.dto.NotificacaoDTO;
import com.fcul.marketplace.exceptions.JWTTokenMissingException;
import com.fcul.marketplace.model.Item;
import com.fcul.marketplace.model.Notificacao;
import com.fcul.marketplace.service.EncomendaService;
import com.fcul.marketplace.service.NotificacaoService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
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

    @Autowired
    SecurityUtils securityUtils;

    @GetMapping()
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR","CONSUMIDOR"})
    public List<NotificacaoDTO> getNotificacoes(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader) throws JWTTokenMissingException {
        List<Notificacao> notificacoes = notificacaoService.getNotificacoes(securityUtils.getEmailFromAuthHeader(authorizationHeader));
        return notificacoes.stream().map(notificacao -> modelMapper.map(notificacao,NotificacaoDTO.class)).collect(Collectors.toList());
    }

    @PostMapping("/saida")
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    public void generateSaidaTransporteNotificacao(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                                   @RequestBody List<Item> itens) throws JWTTokenMissingException{
        notificacaoService.generateSaidaTransporteNotificacao(securityUtils.getEmailFromAuthHeader(authorizationHeader),itens);
    }


    @PostMapping("/chegada")
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    public void generateChegadaItemNotificacao(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                                    @RequestBody Item item) throws JWTTokenMissingException{
        notificacaoService.generateChegadaEncomendaNotificacao(securityUtils.getEmailFromAuthHeader(authorizationHeader),item);
        encomendaService.setItemAsEntregue(item);
    }


}
