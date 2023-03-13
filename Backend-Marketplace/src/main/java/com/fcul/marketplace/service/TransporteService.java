package com.fcul.marketplace.service;

import com.fcul.marketplace.model.Categoria;
import com.fcul.marketplace.model.Fornecedor;
import com.fcul.marketplace.model.Transporte;
import com.fcul.marketplace.repository.FornecedorRepository;
import com.fcul.marketplace.repository.TransporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TransporteService {

    @Autowired
    TransporteRepository transporteRepository;

    //============================GET=============================

    public List<Transporte> getTransportes() {
        return transporteRepository.findAll();
    }

    public Transporte getTransporteById(Integer id){
        return transporteRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    /*public List<Transporte> getTransportesFornecedor(Integer idFornecedor){
        return transporteRepository.findByFornecedorId(idFornecedor);
    }*/

    //===========================INSERT===========================

    public Transporte addTransporte(Transporte transporte) {
        return transporteRepository.save(transporte);
    }

    //===========================UPDATE===========================

    //===========================DELETE===========================

    public void deleteTransporte(Integer id) {
        transporteRepository.deleteById(id);
    }

    public void deleteTransporteBatch(List<Integer> ids){
        transporteRepository.deleteAllByIdInBatch(ids);
    }

}
