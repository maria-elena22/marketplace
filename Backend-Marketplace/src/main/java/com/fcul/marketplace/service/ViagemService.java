package com.fcul.marketplace.service;

import com.fcul.marketplace.model.Transporte;
import com.fcul.marketplace.model.Viagem;
import com.fcul.marketplace.repository.ViagemRepository;
import com.fcul.marketplace.repository.utils.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class ViagemService {

    @Autowired
    ViagemRepository viagemRepository;

    @Autowired
    TransporteService transporteService;

    //============================GET=============================

    public Viagem getViagemByID(Integer id) {
        return viagemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }


    //===========================INSERT===========================

    @Transactional
    public Viagem addViagem(Viagem viagem, Integer idTransporte) {


        Transporte transporte = transporteService.getTransporteById(idTransporte);
        viagem.setTransporte(transporte);




        return viagemRepository.save(viagem);
    }

    //===========================UPDATE===========================
    public Viagem updateViagem(Integer idViagem, Viagem viagem) {
        Viagem viagemBD = viagemRepository.findById(idViagem).orElseThrow(EntityNotFoundException::new);
        viagemBD.setDataInicio(viagem.getDataInicio());
        viagemBD.setDataFim(viagem.getDataFim());
        return viagemRepository.save(viagemBD);
    }
    //===========================DELETE===========================

    public void deleteViagem(Integer id) {
        viagemRepository.deleteById(id);
    }

    public void deleteViagemBatch(List<Integer> ids) {
        viagemRepository.deleteAllByIdInBatch(ids);
    }


    public List<Viagem> getViagensByTransporte(Integer idTransporte, Integer page, Integer size, String sortKey, Sort.Direction sortDir) {
        Pageable pageable = PageableUtils.getDefaultPageable(page, size, sortDir, sortKey);
        return viagemRepository.findByTransporteIdTransporte(idTransporte, pageable).getContent();
    }
}
