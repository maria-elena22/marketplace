package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.config.security.SecurityUtils;
import com.fcul.marketplace.dto.NotificacaoDTO;
import com.fcul.marketplace.exceptions.ForbiddenActionException;
import com.fcul.marketplace.exceptions.JWTTokenMissingException;
import com.fcul.marketplace.model.Notificacao;
import com.fcul.marketplace.model.SubItem;
import com.fcul.marketplace.service.EncomendaService;
import com.fcul.marketplace.service.NotificacaoService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
    @RolesAllowed({"FORNECEDOR", "CONSUMIDOR"})
    @CrossOrigin("*")
    public List<NotificacaoDTO> getNotificacoes(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader) throws JWTTokenMissingException {
        List<Notificacao> notificacoes = notificacaoService.getNotificacoes(
                securityUtils.getEmailFromAuthHeader(authorizationHeader));
        return notificacoes.stream().map(notificacao -> modelMapper.map(notificacao, NotificacaoDTO.class))
                .collect(Collectors.toList());
    }


    @PutMapping("/{idNotificacao}")
    @Parameters(value = {
            @Parameter(name = "idNotificacao", description = "ID do Notificacao a entregar")})
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR", "CONSUMIDOR"})
    @CrossOrigin("*")
    public void entregarNotificacao(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader, @PathVariable Integer idNotificacao) throws JWTTokenMissingException {
        notificacaoService.entregarNotificacao(securityUtils.getEmailFromAuthHeader(authorizationHeader),idNotificacao);

    }

    @GetMapping("/num")
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR", "CONSUMIDOR"})
    @CrossOrigin("*")
    public Integer getNotificacoesNum(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader) throws JWTTokenMissingException {

        return notificacaoService.getNotificacoesNum(securityUtils.getEmailFromAuthHeader(authorizationHeader));
    }

    @PostMapping("/saida")
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    @CrossOrigin("*")
    public List<NotificacaoDTO> generateSaidaTransporteNotificacao(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                                                   @RequestParam List<Integer> subItemsIds) throws JWTTokenMissingException, ForbiddenActionException {
        String emailFornecedor = securityUtils.getEmailFromAuthHeader(authorizationHeader);
        List<SubItem> subItems = subItemsIds.stream().map(id -> {
            try {
                return encomendaService.getSubItemById(emailFornecedor, id);
            } catch (ForbiddenActionException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        List<Notificacao> notificacoes = notificacaoService.generateSaidaTransporteNotificacao(
                emailFornecedor, subItems);

        return notificacoes.stream().map(notificacao -> modelMapper.map(notificacao, NotificacaoDTO.class))
                .collect(Collectors.toList());

    }


    @PostMapping("/chegada")
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    @CrossOrigin("*")
    public NotificacaoDTO generateChegadaItemNotificacao(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                                         @RequestParam Integer subItemId) throws JWTTokenMissingException, ForbiddenActionException {
        String emailFornecedor = securityUtils.getEmailFromAuthHeader(authorizationHeader);
        SubItem subItem = encomendaService.getSubItemById(emailFornecedor, subItemId);
        Notificacao notificacao = notificacaoService.generateChegadaEncomendaNotificacao(emailFornecedor, subItem);
        return modelMapper.map(notificacao, NotificacaoDTO.class);
    }


}
