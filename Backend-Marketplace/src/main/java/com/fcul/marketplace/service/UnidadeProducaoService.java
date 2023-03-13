package com.fcul.marketplace.service;

import com.fcul.marketplace.model.UnidadeProducao;
import com.fcul.marketplace.repository.UnidadeProducaoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class UnidadeProducaoService {

    @Autowired
    UnidadeProducaoRepository unidadeProducaoRepository;

    //============================GET=============================

    public List<UnidadeProducao> getUnidadesProducao() {
        return unidadeProducaoRepository.findAll();
    }

    public UnidadeProducao getUnidadeProducaoByID(Integer id){
        return unidadeProducaoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
    // public List<UnidadeProducao> getUnidadesProducaoByRegiao(String regiao){}

    // public List<UnidadeProducao> getUnidadesProducaoByProduto(Produto produto){}

    // public List<UnidadeProducao> getUnidadesProducaoByFornecedor(Fornecedor fornecedor){}
    //===========================INSERT===========================

    public UnidadeProducao addUnidadeProducao(UnidadeProducao viagem){
        return unidadeProducaoRepository.save(viagem);
    }

    //===========================UPDATE===========================

    //===========================DELETE===========================

    public void deleteUnidadeProducao(Integer id){ unidadeProducaoRepository.deleteById(id); }

    public void deleteUnidadeProducaoBatch(List<Integer> ids){
        unidadeProducaoRepository.deleteAllByIdInBatch(ids);
    }


}
