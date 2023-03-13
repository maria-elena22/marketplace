package com.fcul.marketplace.service;

import com.fcul.marketplace.model.Categoria;
import com.fcul.marketplace.model.SubCategoria;
import com.fcul.marketplace.model.UnidadeProducao;
import com.fcul.marketplace.model.Viagem;
import com.fcul.marketplace.repository.ViagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ViagemService {

    @Autowired
    ViagemRepository viagemRepository;

    //============================GET=============================

    public List<Viagem> getViagens() {
        return viagemRepository.findAll();
    }

    public Viagem getViagemByID(Integer id){
        return viagemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Viagem> getViagensByTransporte(Integer idTransporte){
        return null;
    }

    //===========================INSERT===========================

    public Viagem addViagem(Viagem viagem){
        return viagemRepository.save(viagem);
    }

    //===========================UPDATE===========================

    //===========================DELETE===========================

    public void deleteViagem(Integer id){ viagemRepository.deleteById(id); }

    public void deleteViagemBatch(List<Integer> ids){
        viagemRepository.deleteAllByIdInBatch(ids);
    }


}
