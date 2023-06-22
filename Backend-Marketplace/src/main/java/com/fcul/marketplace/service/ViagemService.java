package com.fcul.marketplace.service;

import com.fcul.marketplace.exceptions.ForbiddenActionException;
import com.fcul.marketplace.model.*;
import com.fcul.marketplace.model.enums.EstadoTransporte;
import com.fcul.marketplace.model.enums.EstadoViagem;
import com.fcul.marketplace.repository.ItemRepository;
import com.fcul.marketplace.repository.SubItemRepository;
import com.fcul.marketplace.repository.ViagemRepository;
import com.fcul.marketplace.repository.utils.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ViagemService {

    @Autowired
    ViagemRepository viagemRepository;

    @Autowired
    TransporteService transporteService;

    @Autowired
    SubItemRepository subItemRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UtilizadorService utilizadorService;

    //============================GET=============================

    public Viagem getViagemByID(Integer id) {
        return viagemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }


    public List<Viagem> getViagensByTransporte(String emailFornecedor, Integer idTransporte, Integer page, Integer size, String sortKey, Sort.Direction sortDir) throws ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(emailFornecedor);
        Transporte transporte = transporteService.getTransporteById(idTransporte);
        if (!fornecedor.getIdUtilizador().equals(transporte.getUnidadeDeProducao().getFornecedor().getIdUtilizador())) {
            throw new ForbiddenActionException("Este transporte não lhe pertence!");
        }
        Pageable pageable = PageableUtils.getDefaultPageable(page, size, sortDir, sortKey);

        return viagemRepository.findByTransporteIdTransporte(idTransporte, pageable).getContent();
    }

    //===========================INSERT===========================

    @Transactional
    public Viagem addViagem(String emailFornecedor, Viagem viagem) throws ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(emailFornecedor);
        Transporte transporte = transporteService.getTransporteById(viagem.getTransporte().getIdTransporte());
        UniProd uniProd = transporte.getUnidadeDeProducao();
        List<SubItem> subItems = viagem.getSubItems();

        if (!uniProd.getFornecedor().getIdUtilizador().equals(fornecedor.getIdUtilizador())) {
            throw new ForbiddenActionException("Este transporte não lhe pertence");
        }
        verificaFornecedorItems(subItems, fornecedor);
        verificaStockSubItens(subItems, uniProd);

        viagem = viagemRepository.save(viagem);
        viagem.setSubItems(handleSubItens(subItems, viagem));
        viagem.setEstadoViagem(EstadoViagem.PROGRAMADA);
        viagem.setTransporte(transporte);
        viagem.setDataFim(null);
        viagem.setDataInicio(null);
        alteraStock(subItems, uniProd);
        viagem = viagemRepository.save(viagem);
        return viagem;
    }

    public void verificaStockSubItens(List<SubItem> subItems, UniProd uniProd) throws ForbiddenActionException {
        //Agrupar num mapa Produto, quantidade e verificar se existe stock para suprir
        Map<Produto, Integer> quantidadeAEntregar = new HashMap<>();
        Map<Item, Integer> quantidadeEntregue = new HashMap<>();
        for (SubItem subItem : subItems) {
            Item item = itemRepository.findById(subItem.getItem().getIdItem()).orElseThrow(EntityNotFoundException::new);
            quantidadeEntregue.computeIfAbsent(item, i -> item.getSubItems().stream().mapToInt(SubItem::getQuantidade).sum());

            Produto produto = item.getProduto();
            Stock stock = uniProd.getStocks().stream().filter(stock1 -> stock1.getProduto().equals(produto)).findFirst().get();

            if ((quantidadeAEntregar.getOrDefault(produto, 0) + subItem.getQuantidade()) > stock.getQuantidade()) {
                throw new ForbiddenActionException("Não tem stock suficiente de " + produto.getNome() + " para suprir esta viagem");
            }

            quantidadeAEntregar.merge(produto, subItem.getQuantidade(), Integer::sum);

            //ver se quantidade de subitens passa quantidade de itens
            int novaQuantidade = quantidadeEntregue.get(item) + subItem.getQuantidade();
            if (novaQuantidade > item.getQuantidade()) {
                throw new ForbiddenActionException(
                        "Está a tentar entregar mais itens de " + produto.getNome() + " do que necessário");
            }
            quantidadeEntregue.merge(item, subItem.getQuantidade(), Integer::sum);
        }
    }


    @Transactional
    public void alteraStock(List<SubItem> subItems, UniProd uniProd) {
        for (SubItem subItem : subItems) {
            Item item = itemRepository.findById(subItem.getItem().getIdItem()).orElseThrow(EntityNotFoundException::new);
            Stock stock = uniProd.getStocks().stream().filter(s -> s.getProduto().equals(item.getProduto())).findFirst().get();
            Integer stockAntigo = stock.getQuantidade();
            stock.setQuantidade(stockAntigo - subItem.getQuantidade());
        }
    }

    public List<SubItem> handleSubItens(List<SubItem> subItems, Viagem viagem) {
        //nova lista
        List<SubItem> subItemsBD = new ArrayList<>();
        for (SubItem subItem : subItems) {
            Item item = itemRepository.findById(subItem.getItem().getIdItem()).orElseThrow(EntityNotFoundException::new);

            SubItem subItemBD = new SubItem();
            subItemBD.setViagem(viagem);
            subItemBD.setItem(item);
            subItemBD.setQuantidade(subItem.getQuantidade());

            subItemsBD.add(subItemRepository.save(subItemBD));
        }
        return subItemsBD;
    }

    public void verificaFornecedorItems(List<SubItem> subItems, Fornecedor fornecedor) throws ForbiddenActionException {
        for (SubItem subItem : subItems) {
            Item item = itemRepository.findById(subItem.getItem().getIdItem()).orElseThrow(EntityNotFoundException::new);
            Produto produto = item.getProduto();
            List<ProdutoFornecedorInfo> produtoFornecedorInfos = (produto.getPrecoFornecedores())
                    .stream().filter(pf -> pf.getFornecedor().getIdUtilizador().equals(fornecedor.getIdUtilizador()))
                    .toList();
            if (produtoFornecedorInfos.isEmpty()) {
                throw new ForbiddenActionException("Você não fornece " + produto.getNome());
            }
        }

    }


    //============================AUX=============================
    public void iniciaViagem(Viagem viagem) {
        if (viagem.getDataInicio() == null) {
            viagem.setEstadoViagem(EstadoViagem.INICIADA);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            viagem.setDataInicio(timestamp);
        }
        viagem.getTransporte().setEstadoTransporte(EstadoTransporte.EM_ENTREGA);
        viagemRepository.save(viagem);
    }

    public void terminaViagem(Viagem viagem) {
        if (viagem.getSubItems().stream().allMatch(SubItem::getEntregue)) {
            viagem.setEstadoViagem(EstadoViagem.FINALIZADA);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            viagem.setDataFim(timestamp);


            Transporte transporte = viagem.getTransporte();
            if (transporte.getViagens().stream().noneMatch(viagemT -> viagemT.getEstadoViagem().equals(EstadoViagem.INICIADA))){
                viagem.getTransporte().setEstadoTransporte(EstadoTransporte.DISPONIVEL);

            }
            viagemRepository.save(viagem);
        }

    }

}
