package com.fcul.marketplace.service;

import com.fcul.marketplace.model.Fornecedor;
import com.fcul.marketplace.model.Transporte;
import com.fcul.marketplace.model.enums.EstadoTransporte;
import com.fcul.marketplace.repository.TransporteRepository;
import com.fcul.marketplace.repository.utils.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class TransporteService {

    @Autowired
    TransporteRepository transporteRepository;

    @Autowired
    UtilizadorService utilizadorService;

    //============================GET=============================

    public Transporte getTransporteById(Integer id) {
        return transporteRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Transporte> getTransportesFornecedor(Integer idFornecedor, EstadoTransporte estadoTransporte, Integer page, Integer size, String sortKey, Sort.Direction sortDir) {
        Pageable pageable = PageableUtils.getDefaultPageable(page, size, sortDir, sortKey);

        return transporteRepository.findByFornecedorIdUtilizador(idFornecedor, estadoTransporte, pageable).getContent();

    }

    //===========================INSERT===========================

    @Transactional
    public Transporte addTransporte(Transporte transporte, Integer idFornecedor) {
        Fornecedor fornecedor = utilizadorService.getFornecedorByID(idFornecedor);
        //TODO
        //transporte.setFornecedor(fornecedor);
        transporte = transporteRepository.save(transporte);
        return transporte;
    }
    //===========================UPDATE===========================

    public Transporte updateTransporte(Integer idTransporte, Transporte transporte) {
        Transporte transporteBD = transporteRepository.findById(idTransporte).orElseThrow(EntityNotFoundException::new);
        transporteBD.setEstadoTransporte(transporte.getEstadoTransporte());
        transporteBD.setMatricula(transporte.getMatricula());
        return transporteRepository.save(transporteBD);
    }

    //===========================DELETE===========================

    public void deleteTransporte(Integer id) {
        transporteRepository.deleteById(id);

    }

}
