package com.fcul.marketplace.service;

import com.fcul.marketplace.dto.encomenda.EncomendaDTO;
import com.fcul.marketplace.dto.encomenda.EncomendaPaymentDTO;
import com.fcul.marketplace.dto.item.SimpleItemDTO;
import com.fcul.marketplace.exceptions.*;
import com.fcul.marketplace.model.*;
import com.fcul.marketplace.model.enums.EstadoEncomenda;
import com.fcul.marketplace.model.enums.TipoNotificacao;
import com.fcul.marketplace.repository.EncomendaRepository;
import com.fcul.marketplace.repository.ItemRepository;
import com.fcul.marketplace.repository.SubEncomendaRepository;
import com.fcul.marketplace.repository.SubItemRepository;
import com.fcul.marketplace.repository.utils.PageableUtils;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.sql.Date;
import java.util.*;

@Service
public class EncomendaService {

    @Autowired
    EncomendaRepository encomendaRepository;

    @Autowired
    SubEncomendaRepository subEncomendaRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    SubItemRepository subItemRepository;

    @Autowired
    UtilizadorService utilizadorService;

    @Autowired
    ProdutoService produtoService;

    @Autowired
    NotificacaoService notificacaoService;

    @Autowired
    TransporteService transporteService;

    @Autowired
    ModelMapper modelMapper;

    //============================GET=============================

    public List<Encomenda> getEncomendas(String emailFromAuthHeader, Double precoMin, Double precoMax, Date dataMin, Date dataMax, EstadoEncomenda estadoEncomenda, Integer page, Integer size, String sortKey, Sort.Direction sortDir) {
        Consumidor consumidor = utilizadorService.findConsumidorByEmail(emailFromAuthHeader);

        Pageable pageable = PageableUtils.getDefaultPageable(page, size, sortDir, sortKey);

        return encomendaRepository.findAllOpt(consumidor.getIdUtilizador(), precoMin, precoMax, dataMin, dataMax, estadoEncomenda, pageable);

    }

    public List<SubEncomenda> getAllSubEncomendas() {
        return subEncomendaRepository.findAll();
    }


    public SubEncomenda getSubEncomendaById(String emailFornecedor, Integer idSubEncomenda) throws ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(emailFornecedor);
        SubEncomenda subEncomenda = subEncomendaRepository.findById(idSubEncomenda).orElseThrow(EntityNotFoundException::new);

        if (!subEncomenda.getFornecedor().equals(fornecedor)) {
            throw new ForbiddenActionException("Esta sub encomenda não é sua");
        }

        return subEncomenda;
    }

    public Encomenda getEncomendaById(String emailConsumidor, Integer idEncomenda) throws ForbiddenActionException {
        Consumidor consumidor = utilizadorService.findConsumidorByEmail(emailConsumidor);
        Encomenda encomenda = encomendaRepository.findById(idEncomenda).orElseThrow(EntityNotFoundException::new);

        verifyConsumidorEncomenda(consumidor, encomenda);

        return encomenda;
    }

    public SubItem getSubItemById(String emailFornecedor, Integer idSubItem) throws ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(emailFornecedor);
        SubItem subItem = subItemRepository.findById(idSubItem).orElseThrow(EntityNotFoundException::new);
        if (!subItem.getItem().getSubEncomenda().getFornecedor().equals(fornecedor)) {
            throw new ForbiddenActionException("Este sub item não é seu");
        }

        return subItem;
    }


    public List<SubEncomenda> getSubEncomendas(String emailFromAuthHeader, Double precoMin, Double precoMax, Date dataMin, Date dataMax, EstadoEncomenda estadoEncomenda, Integer page, Integer size, String sortKey, Sort.Direction sortDir) {
        List<SubEncomenda> subEncomendas;
        Pageable pageable = PageableUtils.getDefaultPageable(page, size, sortDir, sortKey);

        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(emailFromAuthHeader);
        subEncomendas = subEncomendaRepository.findAllOpt(fornecedor.getIdUtilizador(), precoMin, precoMax, dataMin, dataMax, estadoEncomenda, pageable);
        return subEncomendas;
    }

    public List<SubEncomenda> getSubEncomendasByFornecedorEmail(String email) {

        return subEncomendaRepository.findByFornecedorEmail(email);

    }

    public List<SubEncomenda> getSubEncomendasByConsumidorEmail(String email) {

        return subEncomendaRepository.findByEncomendaConsumidorEmail(email);
    }

    public Map<Item, List<Integer>> getItensNaoEntregues(String emailFornecedor, Integer idTransporte, Integer page, Integer size, String sortKey, Sort.Direction sortDir) throws ForbiddenActionException {
        Pageable pageable = PageableUtils.getDefaultPageable(page, size, sortDir, sortKey);
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(emailFornecedor);
        Transporte transporte = transporteService.getTransporteById(idTransporte);
        if (!transporte.getUnidadeDeProducao().getFornecedor().getIdUtilizador().equals(fornecedor.getIdUtilizador())) {
            throw new ForbiddenActionException("Este transporte não lhe pertence");
        }
        List<Item> itemsNaoEntregues = itemRepository.findByOpt(fornecedor.getIdUtilizador(), idTransporte, pageable).getContent();
        Map<Item, List<Integer>> mapItensQuantidades = new HashMap<>();
        for (Item item : itemsNaoEntregues) {
            List<Integer> quantidadesES = new ArrayList<>();
            // entregue
            quantidadesES.add(item.getSubItems().stream()
                    .mapToInt(SubItem::getQuantidade)
                    .sum());

            // stock
            quantidadesES.add(transporte.getUnidadeDeProducao().getStocks().stream().filter(stock -> stock.getProduto().equals(item.getProduto())).findFirst().get().getQuantidade());

            mapItensQuantidades.put(item, quantidadesES);
        }

        return mapItensQuantidades;
    }


    public List<SubItem> getSubItensNaoEntregues(String emailFornecedor, Integer page, Integer size, String sortKey, Sort.Direction sortDir) {
        Pageable pageable = PageableUtils.getDefaultPageable(page, size, sortDir, sortKey);
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(emailFornecedor);
        return subItemRepository.getSubItensNaoEntregues(fornecedor.getIdUtilizador(), pageable).getContent();
    }


    public Encomenda confirmPayment(String emailConsumidor, Integer encomendaId, String clientSecret) throws ForbiddenActionException, PaymentFailedException {
        Consumidor consumidor = utilizadorService.findConsumidorByEmail(emailConsumidor);
        Encomenda encomenda = encomendaRepository.findById(encomendaId).orElseThrow(EntityNotFoundException::new);

        verifyConsumidorEncomenda(consumidor, encomenda);



//        Stripe.apiKey = "sk_test_51Mtx6JEkGiTSvCsfPldmY7bLNtcBmCcskcxZhq8RGWv2E0C03AruqNkHbCd8e2doaaJSxwYTbFpQSHwc5UbrvDkz00mjkgUsn0";
//
//        try {
//            PaymentIntent paymentIntent = PaymentIntent.retrieve(clientSecret);
//            paymentIntent.confirm();
//
//            if ("succeeded".equals(paymentIntent.getStatus())) {
//                // CHANGE THE STATUS OF ENCOMENDA/SUBENCOMENDA
//                encomenda.setEstadoEncomenda(EstadoEncomenda.A_PROCESSAR);
//                criaNotificacoes(encomenda.getSubEncomendas(), encomenda);
//            }
//        } catch (StripeException e) {
//            // FALHA NA ENCOMENDA , CRIAR EXCEPTION
//            throw new PaymentFailedException(e.getMessage());
//
//        }

        encomenda.setEstadoEncomenda(EstadoEncomenda.A_PROCESSAR);
        criaNotificacoes(encomenda.getSubEncomendas(), encomenda);
        return encomendaRepository.save(encomenda);

    }

    //===========================INSERT===========================

    @Transactional
    public EncomendaPaymentDTO addEncomenda(Encomenda encomenda, String emailConsumidor, List<SimpleItemDTO> items) throws ErroCalculoDoPrecoEnviadoException, ForbiddenActionException {

        Map<Fornecedor, List<Item>> itensByFornecedor = this.splitByFornecedor(items);

        double precoCal = calculaPreco(items);
        verifyPreco(precoCal, encomenda.getPreco());
        PaymentIntent intent = handlePayment((int) (precoCal * 100));

        Consumidor consumidor = utilizadorService.findConsumidorByEmail(emailConsumidor);
        encomenda.setConsumidor(consumidor);
        List<SubEncomenda> subEncomendas = getSubEncomendas(encomenda, itensByFornecedor);
        encomenda.setSubEncomendas(subEncomendas);
        encomenda.setEstadoEncomenda(EstadoEncomenda.POR_PAGAR);
        for (SubEncomenda subEncomenda : subEncomendas) {
            subEncomenda.setEncomenda(encomenda);
            subEncomenda.setEstadoEncomenda(EstadoEncomenda.POR_PAGAR);
            subEncomendaRepository.save(subEncomenda);
        }

        // ENVIAR A ENCOMENDA E O PAYMENT INTENT meter estado encomenda/subencomenda
        encomenda = encomendaRepository.save(encomenda);

        return criaEncomendaPaymentDTO(encomenda, intent);
    }

    //===========================UPDATE===========================

    public Encomenda cancelEncomenda(String emailConsumidor, Integer idEncomenda) throws EncomendaAlreadyCancelledException, EncomendaCannotBeCancelledException, ForbiddenActionException {

        Consumidor consumidor = utilizadorService.findConsumidorByEmail(emailConsumidor);
        Encomenda encomenda = encomendaRepository.findById(idEncomenda).orElseThrow(EntityNotFoundException::new);
        if (!consumidor.getIdUtilizador().equals(encomenda.getConsumidor().getIdUtilizador())) {
            throw new ForbiddenActionException("Não pode cancelar encomenda");
        }
        if (encomenda.getEstadoEncomenda().equals(EstadoEncomenda.CANCELADO)) {
            throw new EncomendaAlreadyCancelledException("Esta encomenda já se encontra cancelada");
        }
        if (encomenda.getEstadoEncomenda().equals(EstadoEncomenda.A_PROCESSAR)) {
            throw new EncomendaCannotBeCancelledException("Esta encomenda já não pode ser cancelada");
        }

        encomenda.setEstadoEncomenda(EstadoEncomenda.CANCELADO);
        return encomendaRepository.save(encomenda);


    }

    public void setSubEncomendaEmDistribuicao(SubEncomenda subEncomenda) {
        Encomenda encomenda = subEncomenda.getEncomenda();

        subEncomenda.setEstadoEncomenda(EstadoEncomenda.EM_DISTRIBUICAO);
        encomenda.setEstadoEncomenda(EstadoEncomenda.EM_DISTRIBUICAO);

        subEncomendaRepository.save(subEncomenda);
        encomendaRepository.save(encomenda);

    }

    public void setSubItemAsEntregue(SubItem subItem) {
        // Mark subitem as delivered
        subItem.setEntregue(true);
        subItemRepository.save(subItem);

        Item item = subItem.getItem();

        // Check if all subitems of the item have been delivered
        boolean allSubItemsDelivered = item.getSubItems().stream().allMatch(SubItem::getEntregue);

        if (!allSubItemsDelivered) {
            return;
        }


        // Check if it is the final amount of items
        Integer quantidadeEntregue = item.getSubItems().stream().mapToInt(SubItem::getQuantidade).sum();
        boolean finalAmountItems = quantidadeEntregue.equals(item.getQuantidade());
        if (!finalAmountItems) {
            return;
        }

        // Mark item as delivered
        item.setEntregue(true);
        itemRepository.save(item);

        SubEncomenda subEncomenda = item.getSubEncomenda();

        // Check if all items of the subencomenda have been delivered
        boolean allItemsDelivered = subEncomenda.getItems().stream().allMatch(Item::getEntregue);
        if (!allItemsDelivered) {
            return;
        }

        // Mark subencomenda as delivered
        subEncomenda.setEstadoEncomenda(EstadoEncomenda.ENTREGUE);
        subEncomendaRepository.save(subEncomenda);

        Encomenda encomenda = subEncomenda.getEncomenda();

        // Check if all subencomendas of the encomenda have been delivered
        boolean allSubEncomendasDelivered = encomenda.getSubEncomendas().stream()
                .allMatch(se -> se.getEstadoEncomenda().equals(EstadoEncomenda.ENTREGUE));
        if (!allSubEncomendasDelivered) {
            return;
        }

        // Mark encomenda as delivered
        encomenda.setEstadoEncomenda(EstadoEncomenda.ENTREGUE);
        encomendaRepository.save(encomenda);
    }


    //=============================AUX=============================

    private List<SubEncomenda> getSubEncomendas(Encomenda encomenda, Map<Fornecedor, List<Item>> items) {

        List<SubEncomenda> subEncomendas = new ArrayList<>();

        for (Map.Entry<Fornecedor, List<Item>> par : items.entrySet()) {
            SubEncomenda subencomenda = new SubEncomenda();
            subencomenda.setDataEncomenda(encomenda.getDataEncomenda());
            subencomenda.setEstadoEncomenda(encomenda.getEstadoEncomenda());
            (par.getValue()).forEach(item -> item.setSubEncomenda(subencomenda));
            subencomenda.setItems(par.getValue());
            subencomenda.setFornecedor(par.getKey());
            subencomenda.setPreco(par.getValue().stream().mapToDouble(
                    item -> getPrecoItem(par.getKey().getIdUtilizador(), item)).reduce(0, Double::sum));
            subEncomendas.add(subencomenda);
        }

        encomenda.setSubEncomendas(subEncomendas);
        return subEncomendas;
    }

    private Double getPrecoItem(Integer fornecedorID, Item item) {
        return item.getProduto().getPrecoFornecedores().stream()
                .filter(pf -> Objects.equals(pf.getFornecedor().getIdUtilizador(), fornecedorID))
                .mapToDouble(ProdutoFornecedorInfo::getPreco)
                .findFirst()
                .orElse(0.0);

    }

    private Map<Fornecedor, List<Item>> splitByFornecedor(List<SimpleItemDTO> itens) {

        Map<Fornecedor, List<Item>> itensPorFornecedor = new HashMap<>();

        for (SimpleItemDTO simpleItemDTO : itens) {

            Fornecedor fornecedor = utilizadorService.getFornecedorByID(simpleItemDTO.getFornecedorId());
            Produto produto = produtoService.getProdutoByID(simpleItemDTO.getProdutoId());


            if (!itensPorFornecedor.containsKey(fornecedor)) {
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

    private void verifyConsumidorEncomenda(Consumidor consumidor, Encomenda encomenda) throws ForbiddenActionException {
        if (!consumidor.getEncomendas().contains(encomenda)) {
            throw new ForbiddenActionException("Esta encomenda não é sua");
        }
    }

    private PaymentIntent handlePayment(int intValue) {
        Stripe.apiKey = "sk_test_51Mtx6JEkGiTSvCsfPldmY7bLNtcBmCcskcxZhq8RGWv2E0C03AruqNkHbCd8e2doaaJSxwYTbFpQSHwc5UbrvDkz00mjkgUsn0";

        Map<String, Object> params = new HashMap<>();
        params.put("amount", intValue);
        params.put("currency", "eur");
        params.put("payment_method_types", Collections.singletonList("card"));

        try {
            return PaymentIntent.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    private void verifyPreco(double precoCal, double precoEsp) throws ForbiddenActionException {
        if (Double.compare(precoCal, precoEsp) != 0) {
            throw new ForbiddenActionException("Ocorreu um erro no calculo do preco!");
        }
    }

    private double calculaPreco(List<SimpleItemDTO> items) throws ForbiddenActionException {
        double total = 0;
        for (SimpleItemDTO simpleItemDTO : items) {

            Fornecedor fornecedor = utilizadorService.getFornecedorByID(simpleItemDTO.getFornecedorId());
            Produto produto = produtoService.getProdutoByID(simpleItemDTO.getProdutoId());

            double preco;
            try {
                preco = produto.getPreco(fornecedor);
            } catch (Exception e) {
                throw new ForbiddenActionException("Este fornecedor nao tem preço para este produto");
            }
            total +=(preco*simpleItemDTO.getQuantidade());

        }
        return total;
    }

    private EncomendaPaymentDTO criaEncomendaPaymentDTO(Encomenda encomenda, PaymentIntent intent) {
        EncomendaPaymentDTO encomendaPaymentDTO = new EncomendaPaymentDTO();
        encomendaPaymentDTO.setEncomendaDTO(modelMapper.map(encomenda, EncomendaDTO.class));
        encomendaPaymentDTO.setStripeClientSecret(intent.getClientSecret());
        return encomendaPaymentDTO;
    }

    private void criaNotificacoes(List<SubEncomenda> subEncomendas, Encomenda encomenda) {

        for (SubEncomenda subEncomenda : subEncomendas) {
            subEncomenda.setEstadoEncomenda(EstadoEncomenda.A_PROCESSAR);
            subEncomenda = subEncomendaRepository.save(subEncomenda);
            notificacaoService.insertNotificacao(subEncomenda, TipoNotificacao.NOVA_ENCOMENDA
                    , "Nova encomenda do consumidor " + encomenda.getConsumidor().getNome()
                    , subEncomenda.getFornecedor(), encomenda.getConsumidor());
        }

    }


}
