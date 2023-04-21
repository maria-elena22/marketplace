package com.fcul.marketplace.service;

import com.fcul.marketplace.dto.encomenda.SimpleItemDTO;
import com.fcul.marketplace.exceptions.EncomendaAlreadyCancelledException;
import com.fcul.marketplace.exceptions.EncomendaCannotBeCancelledException;
import com.fcul.marketplace.exceptions.ForbiddenActionException;
import com.fcul.marketplace.model.*;
import com.fcul.marketplace.model.enums.EstadoEncomenda;
import com.fcul.marketplace.repository.EncomendaRepository;
import com.fcul.marketplace.repository.ItemRepository;
import com.fcul.marketplace.repository.SubEncomendaRepository;
import com.fcul.marketplace.repository.utils.PageableUtils;
import com.fcul.marketplace.utils.ChargeRequest;
import com.stripe.Stripe;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.TokenCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EncomendaService {

    @Autowired
    EncomendaRepository encomendaRepository;

    @Autowired
    SubEncomendaRepository subEncomendaRepository;

    @Autowired
    UtilizadorService utilizadorService;

    @Autowired
    ProdutoService produtoService;

    @Autowired
    NotificacaoService notificacaoService;

    @Autowired
    ItemRepository itemRepository;

    //============================GET=============================

    public List<Encomenda> getEncomendas(String emailFromAuthHeader, Double precoMin, Double precoMax, Date dataMin, Date dataMax, EstadoEncomenda estadoEncomenda, Integer page, Integer size, String sortKey, Sort.Direction sortDir) {

       Consumidor consumidor =  utilizadorService.findConsumidorByEmail(emailFromAuthHeader);
        Pageable pageable = PageableUtils.getDefaultPageable(page, size, sortDir, sortKey);

        return encomendaRepository.findAllOpt(consumidor.getIdUtilizador(),precoMin,precoMax,dataMin,dataMax,estadoEncomenda,pageable);

    }

    public List<SubEncomenda> getAllSubEncomendas() {
        return subEncomendaRepository.findAll();
    }

    public List<SubEncomenda> getSubEncomendas(String emailFromAuthHeader,String role, Double precoMin, Double precoMax, Date dataMin, Date dataMax, EstadoEncomenda estadoEncomenda, Integer page, Integer size, String sortKey, Sort.Direction sortDir) {
        List<SubEncomenda> subEncomendas;
        Pageable pageable = PageableUtils.getDefaultPageable(page, size, sortDir, sortKey);
        if(role=="CONSUMIDOR") {
            Consumidor consumidor =  utilizadorService.findConsumidorByEmail(emailFromAuthHeader);
            subEncomendas = subEncomendaRepository.findAllOpt(consumidor.getIdUtilizador(), null, precoMin, precoMax, dataMin, dataMax, estadoEncomenda, pageable);
        }else{
            Fornecedor fornecedor =  utilizadorService.findFornecedorByEmail(emailFromAuthHeader);
            subEncomendas = subEncomendaRepository.findAllOpt(null, fornecedor.getIdUtilizador(), precoMin, precoMax, dataMin, dataMax, estadoEncomenda, pageable);
        }
        return subEncomendas;
    }

    public List<SubEncomenda> getSubEncomendasByFornecedorEmail(String email) {

        return subEncomendaRepository.findByFornecedorEmail(email);

    }

    public List<SubEncomenda> getSubEncomendasByConsumidorEmail(String email) {

        return subEncomendaRepository.findByEncomendaConsumidorEmail(email);
    }

    //===========================INSERT===========================

    public List<Encomenda> addEncomenda(Encomenda encomenda, Integer idConsumidor, List<SimpleItemDTO> items) {

        Map<Fornecedor,List<Item>> itensByFornecedor = this.splitByFornecedor(items);

        //TODO
//        Double precoCal = itensByFornecedor.values().stream().mapToDouble(item->item.getProduto().getPreco()).reduce(Double::sum).getAsDouble();
//        if(precoCal != encomenda.getPreco()){
//            throw new ErroCalculoDoPrecoEnviadoException();
//        }


        /* TokenCreateParams params = TokenCreateParams.builder()
                .setCard(TokenCreateParams.Card.builder()
                        .setNumber("4242424242424242")
                        .setExpMonth(12)
                        .setExpYear(2022)
                        .setCvc("123")
                        .build())
                .build();

        Token token = Token.create(params);

        ChargeCreateParams params = ChargeCreateParams.builder()
                .setAmount(1000) // amount in cents
                .setCurrency("usd")
                .setDescription("Example charge")
                .setSource(token.getId())
                .build();

        Charge charge = Charge.create(params);*/
        //TODO verificacao do pagamento

        Consumidor consumidor = utilizadorService.getConsumidorByID(idConsumidor);
        encomenda.setConsumidor(consumidor);

        List<Encomenda> encomendas = new ArrayList<>(); //getSubEncomendas(encomenda,itensByFornecedor);

        //Criacao das Notificacoes

//        for(Encomenda subencomenda : encomendas){
//
//            subencomenda = encomendaRepository.save(encomenda);
//            Notificacao notificacao = new Notificacao();
//            notificacaoService.insertNotificacao(encomenda,TipoNotificacao.NOVA_ENCOMENDA
//                    ,"Nova encomenda do consumidor" +  encomenda.getConsumidor().getNome(),encomenda.getConsumidor());
//        }

        return encomendas;
    }

    //===========================UPDATE===========================


    public Encomenda cancelEncomenda(String emailConsumidor, Integer idEncomenda) throws EncomendaAlreadyCancelledException,EncomendaCannotBeCancelledException, ForbiddenActionException{

        Consumidor consumidor = utilizadorService.findConsumidorByEmail(emailConsumidor);


        Encomenda encomenda = encomendaRepository.findById(idEncomenda).orElseThrow(EntityNotFoundException::new);
        if(consumidor.getIdUtilizador()!= encomenda.getConsumidor().getIdUtilizador()){
            throw new ForbiddenActionException("Não pode cancelar encomenda");
        }

            if(encomenda.getEstadoEncomenda().equals(EstadoEncomenda.CANCELADO)){
            throw new EncomendaAlreadyCancelledException("Esta encomenda já se encontra cancelada");
        }
        if(encomenda.getEstadoEncomenda().equals(EstadoEncomenda.A_PROCESSAR)){
            throw new EncomendaCannotBeCancelledException("Esta encomenda já não pode ser cancelada");

        }

        encomenda.setEstadoEncomenda(EstadoEncomenda.CANCELADO);

        return encomendaRepository.save(encomenda);



    }

    //===========================DELETE===========================

    public void deleteEncomenda(Integer id) {
        encomendaRepository.deleteById(id);
    }


    private Encomenda getSubEncomendas(Encomenda encomenda,List<Item> items){
        //uma lista de produtos por fornecedor
        //TODO
        /*List<List<Item>> ItemsByFornecedor = this.splitByFornecedor(items);

        List<SubEncomenda> subEncomendas = new ArrayList<>();


        for(List<Item> listaItensPerFornecedor : ItemsByFornecedor){
            SubEncomenda subencomenda = new SubEncomenda();

            subencomenda.setDataEncomenda(encomenda.getDataEncomenda());
            subencomenda.setEstadoEncomenda(encomenda.getEstadoEncomenda());
            //subencomenda.setConsumidor(encomenda.getConsumidor());
            subencomenda.setPreco(listaItensPerFornecedor.stream().mapToDouble(item -> item.getProduto().getPreco()).reduce(0,Double::sum));
            subencomenda.setItems(listaItensPerFornecedor);
            //encomendas.add(subencomenda);
        }

        encomenda.setSubEncomendas(subEncomendas);*/
        return encomenda;
    }

    public Map<Fornecedor,List<Item>> splitByFornecedor(List<SimpleItemDTO> itens) {

        Map<Fornecedor,List<Item>> itensPorFornecedor = new HashMap<>();

        for(SimpleItemDTO simpleItemDTO : itens){

            Fornecedor fornecedor = utilizadorService.getFornecedorByID(simpleItemDTO.getFornecedorId());
            Produto produto = produtoService.getProdutoByID(simpleItemDTO.getProdutoId());


            if(!itensPorFornecedor.containsKey(fornecedor)) {
                itensPorFornecedor.put(fornecedor, new ArrayList<>());
            }

            Item item = new Item();
            item.setProduto(produto);
            item.setEntregue(false);
            item.setQuantidade(simpleItemDTO.getQuantidade());

            itensPorFornecedor.get(fornecedor).add(item);

        }

        return itensPorFornecedor;



    }


    public void setItemAsEntregue(Item item) {
        //todo
        item = null;
    }

    private Charge charge(ChargeRequest chargeRequest) throws StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequest.getAmount());
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("description", chargeRequest.getDescription());
        chargeParams.put("source", chargeRequest.getStripeToken());
        return Charge.create(chargeParams);
    }


}
