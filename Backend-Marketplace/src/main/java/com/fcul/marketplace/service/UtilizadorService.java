package com.fcul.marketplace.service;

import com.fcul.marketplace.model.Consumidor;
import com.fcul.marketplace.model.Fornecedor;
import com.fcul.marketplace.repository.ConsumidorRepository;
import com.fcul.marketplace.repository.FornecedorRepository;
import com.fcul.marketplace.repository.UtilizadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UtilizadorService {

    @Autowired
    FornecedorRepository fornecedorRepository;

    @Autowired
    ConsumidorRepository consumidorRepository;

    @Autowired
    UtilizadorRepository utilizadorRepository;

    //============================GET=============================

    public List<Fornecedor> getFornecedores() {
        return fornecedorRepository.findAll();
    }

    public Fornecedor getFornecedorByID(Integer id) {
        return fornecedorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    /* public Consumidor getFornecedorByLocal(Integer id){
        return null;
    }*/

    public List<Consumidor> getConsumidores() {
        return consumidorRepository.findAll();
    }

    public Consumidor getConsumidorByID(Integer id) {
        return consumidorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    /* public Consumidor getConsumidorByLocal(Integer id){
        return null;
    }*/

    //===========================INSERT===========================

    public Consumidor addConsumidor(Consumidor consumidor) {
        return consumidorRepository.save(consumidor);
    }

    public Fornecedor addFornecedor(Fornecedor fornecedor) {
        return fornecedorRepository.save(fornecedor);
    }

    //===========================UPDATE===========================
    public Consumidor updateConsumidor(String email, Consumidor consumidor) {
        Consumidor consumidorBD = findConsumidorByEmail(email);
        consumidorBD.setTelemovel(consumidor.getTelemovel());
        consumidorBD.setContinente(consumidor.getContinente());
        consumidorBD.setCoordenadas(consumidor.getCoordenadas());
        consumidorBD.setDistrito(consumidor.getDistrito());
        consumidorBD.setIdFiscal(consumidor.getIdFiscal());
        consumidorBD.setMorada(consumidor.getMorada());
        consumidorBD.setFreguesia(consumidor.getFreguesia());
        consumidorBD.setPais(consumidor.getPais());
        consumidorBD.setMunicipio(consumidor.getMunicipio());
        consumidorBD.setNome(consumidor.getNome());
        return consumidorRepository.save(consumidorBD);
    }

    public Fornecedor updateFornecedor(Integer id, Fornecedor fornecedor) {
        Fornecedor fornecedorBD = fornecedorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        fornecedorBD.setTelemovel(fornecedor.getTelemovel());
        fornecedorBD.setContinente(fornecedor.getContinente());
        fornecedorBD.setCoordenadas(fornecedor.getCoordenadas());
        fornecedorBD.setDistrito(fornecedor.getDistrito());
        fornecedorBD.setIdFiscal(fornecedor.getIdFiscal());
        fornecedorBD.setMorada(fornecedor.getMorada());
        fornecedorBD.setFreguesia(fornecedor.getFreguesia());
        fornecedorBD.setPais(fornecedor.getPais());
        fornecedorBD.setMunicipio(fornecedor.getMunicipio());
        fornecedorBD.setNome(fornecedor.getNome());
        return fornecedorRepository.save(fornecedorBD);
    }

    //===========================DELETE===========================

    public void deleteConsumidor(Integer id) {
        consumidorRepository.deleteById(id);
    }

    public void deleteFornecedor(Integer id) {
        fornecedorRepository.deleteById(id);
    }

}
