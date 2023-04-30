package com.fcul.marketplace.service;

import com.fcul.marketplace.exceptions.ForbiddenActionException;
import com.fcul.marketplace.model.*;
import com.fcul.marketplace.model.enums.TipoNotificacao;
import com.fcul.marketplace.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificacaoService {

    @Autowired
    NotificacaoRepository notificacaoRepository;

    @Autowired
    UtilizadorService utilizadorService;

    @Autowired
    EncomendaService encomendaService;

    @Autowired
    ViagemService viagemService;

    public List<Notificacao> getNotificacoes(String userEmail) {
        Utilizador utilizador = utilizadorService.getUtilizadorByEmail(userEmail);
        List<Notificacao> notificacaos = notificacaoRepository.findByDestinatarioIdUtilizadorAndEntregueFalse(utilizador.getIdUtilizador());
        notificacaos.forEach(notificacao -> notificacao.setEntregue(true));
        notificacaoRepository.saveAll(notificacaos);
        return notificacaos;
    }


    @Transactional
    public Notificacao insertNotificacao(SubEncomenda subEncomenda, TipoNotificacao tipoNotificacao, String message, Utilizador receiver, Utilizador issuer) {
        Notificacao notificacao = new Notificacao(null, message, subEncomenda, tipoNotificacao, issuer, receiver, false);
        return notificacaoRepository.save(notificacao);
    }

    @Transactional
    public List<Notificacao> generateSaidaTransporteNotificacao(String emailFornecedor, List<SubItem> subItems) throws ForbiddenActionException {
        Fornecedor issuer = utilizadorService.findFornecedorByEmail(emailFornecedor);
        for (SubItem subItem : subItems) {
            verifyFornecedorSubItem(issuer, subItem);
        }
        List<Notificacao> notificacoes = new ArrayList<>();

        for (SubItem subItem : subItems) {
            Item item = subItem.getItem();
            SubEncomenda subEncomenda = item.getSubEncomenda();
            TipoNotificacao tipoNotificacao = TipoNotificacao.SAIDA_UNI_PROD;
            String message = "O transporte do fornecedor " + issuer.getNome() + " acabou de sair com: " +
                    subItem.getQuantidade() + "x - " + item.getProduto().getNome();
            Consumidor receiver = subEncomenda.getEncomenda().getConsumidor();

            viagemService.iniciaViagem(subItem.getViagem());

            Notificacao notificacao = insertNotificacao(subEncomenda, tipoNotificacao, message, receiver, issuer);
            notificacoes.add(notificacao);
            encomendaService.setSubEncomendaEmDistribuicao(subEncomenda);
        }
        return notificacoes;
    }

    public Notificacao generateChegadaEncomendaNotificacao(String emailFornecedor, SubItem subItem) throws ForbiddenActionException {

        Fornecedor issuer = utilizadorService.findFornecedorByEmail(emailFornecedor);
        verifyFornecedorSubItem(issuer, subItem);

        Item item = subItem.getItem();
        SubEncomenda subEncomenda = item.getSubEncomenda();
        TipoNotificacao tipoNotificacao = TipoNotificacao.CHEGADA_IMINENTE;
        String message = "O transporte do fornecedor " + issuer.getNome() + " está a chegar com: " +
                item.getQuantidade() + "x - " + item.getProduto().getNome();
        Consumidor receiver = subEncomenda.getEncomenda().getConsumidor();

        Notificacao notificacao = insertNotificacao(subEncomenda, tipoNotificacao, message, receiver, issuer);
        encomendaService.setSubItemAsEntregue(subItem);
        viagemService.terminaViagem(subItem.getViagem());

        return notificacao;
    }

    private void verifyFornecedorSubItem(Fornecedor fornecedor, SubItem subItem) throws ForbiddenActionException {
        if (!subItem.getItem().getSubEncomenda().getFornecedor().equals(fornecedor)) {
            throw new ForbiddenActionException("O sub item " + subItem.getIdSubItem() + " - " +
                    subItem.getItem().getProduto().getNome() + " não é seu");
        }

        if (subItem.getEntregue()) {
            throw new ForbiddenActionException("O sub item " + subItem.getIdSubItem() + " - " +
                    subItem.getItem().getProduto().getNome() + " já foi entregue");
        }

    }
}
