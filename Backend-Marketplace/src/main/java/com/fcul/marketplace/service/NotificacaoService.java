package com.fcul.marketplace.service;

import com.fcul.marketplace.model.Consumidor;
import com.fcul.marketplace.model.Encomenda;
import com.fcul.marketplace.model.Item;
import com.fcul.marketplace.model.Notificacao;
import com.fcul.marketplace.model.enums.TipoNotificacao;
import com.fcul.marketplace.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class NotificacaoService {

    @Autowired
    NotificacaoRepository notificacaoRepository;

    public List<Notificacao> getNotificacoes(Integer userId){
         List<Notificacao> notificacaos = notificacaoRepository.findByUtilizadorIdUtilizadorAndEntregueFalse(userId);

         notificacaos.forEach(notificacao -> notificacao.setEntregue(true));
         notificacaoRepository.saveAll(notificacaos);
         return notificacaos;
    }


    @Transactional
    public Notificacao insertNotificacao(Encomenda encomenda, TipoNotificacao tipoNotificacao, String message, Consumidor consumidor) {
        Notificacao notificacao = new Notificacao(null,message,encomenda,tipoNotificacao,consumidor,false);
        return notificacaoRepository.save(notificacao);
    }

    public void generateSaidaTransporteNotificacao(List<Item> items) {

        for(Item item:items){

            Notificacao notificacao = new Notificacao();
            notificacao.setTipoNotificacao(TipoNotificacao.SAIDA_UNI_PROD);
            //TODO
//            Encomenda encomenda = item.getEncomenda();
//            notificacao.setEncomenda(encomenda);
//            notificacao.setMensagem("O transporte do fornecedor " + item.getFornecedor().getNome()
//                     + " acabou de sair com o item " + item.getProduto().getNome() +
//                    " da sua encomenda"+ encomenda.getIdEncomenda()+ " !");
            notificacaoRepository.save(notificacao);

        }

    }

    public void generateChegadaEncomendaNotificacao(Item item) {

        Notificacao notificacao = new Notificacao();
        notificacao.setTipoNotificacao(TipoNotificacao.CHEGADA_IMINENTE);
        //TODO
//        Encomenda encomenda = item.getEncomenda();
//        notificacao.setUtilizador(encomenda.getConsumidor());
//        notificacao.setEncomenda(encomenda);
//        notificacao.setMensagem("A sua encomenda " + encomenda.getIdEncomenda() + " do fornecedor " +
//                item.getFornecedor().getNome() + " está prestes a chegar!");

        notificacaoRepository.save(notificacao);
    }
}
