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

    public List<Encomenda> getEncomendas() { return encomendaRepository.findAll(); }

    public Encomenda getEncomendaByID(Integer id){
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

    //update estado encomenda
    //update data entrega

    //===========================DELETE===========================

    public void deleteEncomenda(Integer id){
        encomendaRepository.deleteById(id);
    }

    public void deleteEncomendaBatch(List<Integer> ids){
        encomendaRepository.deleteAllByIdInBatch(ids);
    }
}
