package com.fcul.marketplace.service;

import com.fcul.marketplace.model.Encomenda;
import com.fcul.marketplace.model.Produto;
import com.fcul.marketplace.model.enums.EstadoEncomenda;
import com.fcul.marketplace.repository.EncomendaRepository;
import com.fcul.marketplace.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class EncomendaService {

    @Autowired
    EncomendaRepository encomendaRepository;

    //============================GET=============================

    public List<Encomenda> getEncomendas() {
        return encomendaRepository.findAll();
    }

    public Encomenda getEncomendaByID(Integer id) {
        return encomendaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Encomenda> getEncomendasByConsumidor(Integer idConsumidor){
        return null;
    }

    public List<Encomenda> getEncomendasByFornecedor(Integer idFornecedor){
        return null;
    }

    public List<Encomenda> getEncomendaByEstado(EstadoEncomenda estado){
        return null;
    }

    //===========================INSERT===========================

    public Encomenda addEncomenda(Encomenda encomenda) { return encomendaRepository.save(encomenda); }

    //===========================UPDATE===========================


    public Encomenda cancelEncomenda(Integer idEncomenda) throws EncomendaAlreadyCancelledException,EncomendaCannotBeCancelledException{

        Encomenda encomenda = this.getEncomendaByID(idEncomenda);
        if(encomenda.getEstadoEncomenda().equals(EstadoEncomenda.CANCELADO)){
            throw new EncomendaAlreadyCancelledException("Esta encomenda ja se encontra cancelada");
        }
        if(encomenda.getEstadoEncomenda().equals(EstadoEncomenda.A_PROCESSAR)){
            throw new EncomendaCannotBeCancelledException("Esta encomenda ja nao pode ser cancelada");

        }

        encomenda.setEstadoEncomenda(EstadoEncomenda.CANCELADO);

        return encomendaRepository.save(encomenda);



    }

    //===========================DELETE===========================

    public void deleteEncomenda(Integer id) {
        encomendaRepository.deleteById(id);
    }

    public void deleteEncomendaBatch(List<Integer> ids) {
        encomendaRepository.deleteAllByIdInBatch(ids);
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
}
