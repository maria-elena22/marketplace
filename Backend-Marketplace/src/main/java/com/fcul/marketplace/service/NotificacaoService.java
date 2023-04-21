package com.fcul.marketplace.service;

import com.fcul.marketplace.model.*;
import com.fcul.marketplace.model.enums.TipoNotificacao;
import com.fcul.marketplace.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class NotificacaoService {

    @Autowired
    NotificacaoRepository notificacaoRepository;

    @Autowired
    UtilizadorService utilizadorService;

    public List<Notificacao> getNotificacoes(String userEmail){
        Utilizador utilizador = utilizadorService.getUtilizadorByEmail(userEmail);
        List<Notificacao> notificacaos = notificacaoRepository.findByDestinatarioIdUtilizadorAndEntregueFalse(utilizador.getIdUtilizador());
        notificacaos.forEach(notificacao -> notificacao.setEntregue(true));
        notificacaoRepository.saveAll(notificacaos);
        return notificacaos;
    }


    @Transactional
    public Notificacao insertNotificacao(SubEncomenda subEncomenda, TipoNotificacao tipoNotificacao, String message, Utilizador receiver,Utilizador issuer) {
        Notificacao notificacao = new Notificacao(null,message,subEncomenda,tipoNotificacao,issuer,receiver,false);
        return notificacaoRepository.save(notificacao);
    }




    public void generateSaidaTransporteNotificacao(String emailFornecedor, List<Item> items) {
        Fornecedor issuer = utilizadorService.findFornecedorByEmail(emailFornecedor);

        for(Item item:items){
            Notificacao notificacao = new Notificacao();
            notificacao.setIdNotificacao(null);
            notificacao.setRemetente(issuer);
            notificacao.setDestinatario(item.getSubEncomenda().getEncomenda().getConsumidor());
            notificacao.setSubEncomenda(item.getSubEncomenda());
            notificacao.setTipoNotificacao(TipoNotificacao.SAIDA_UNI_PROD);
            notificacao.setMensagem("O transporte do fornecedor " + issuer.getNome() + "acabou de sair com: " +
                    item.getQuantidade() + "x - " + item.getProduto().getNome() );
            notificacaoRepository.save(notificacao);
        }


    }

    public void generateChegadaEncomendaNotificacao(String emailFornecedor, Item item) {
        Fornecedor issuer = utilizadorService.findFornecedorByEmail(emailFornecedor);

        Notificacao notificacao = new Notificacao();
        notificacao.setIdNotificacao(null);
        notificacao.setRemetente(issuer);
        notificacao.setDestinatario(item.getSubEncomenda().getEncomenda().getConsumidor());
        notificacao.setSubEncomenda(item.getSubEncomenda());
        notificacao.setTipoNotificacao(TipoNotificacao.CHEGADA_IMINENTE);
        notificacao.setMensagem("O transporte do fornecedor " + issuer.getNome() + "esta preste a chegar com: " +
                item.getQuantidade() + "x - " + item.getProduto().getNome() );
        notificacaoRepository.save(notificacao);
    }
}
