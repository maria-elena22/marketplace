package com.fcul.marketplace.service;

import com.fcul.marketplace.exceptions.InactiveAccountException;
import com.fcul.marketplace.model.Consumidor;
import com.fcul.marketplace.model.Fornecedor;
import com.fcul.marketplace.model.Utilizador;
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

    public List<Consumidor> getConsumidores() {
        return consumidorRepository.findAll();
    }

    public Consumidor getConsumidorByID(Integer id) {
        return consumidorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Consumidor findConsumidorByEmail(String email) {
        return consumidorRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

    public Fornecedor findFornecedorByEmail(String email) {
        return fornecedorRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

    public Utilizador getUtilizadorByEmail(String userEmail) {
        return utilizadorRepository.findByEmail(userEmail).orElseThrow(EntityNotFoundException::new);
    }

    //===========================INSERT===========================

    public Consumidor addConsumidor(Consumidor consumidor) {
        consumidor.setActive(true);

        return consumidorRepository.save(consumidor);
    }

    public Fornecedor addFornecedor(Fornecedor fornecedor) {
        fornecedor.setActive(true);
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


    public Fornecedor updateFornecedor(String email, Fornecedor fornecedor) {
        Fornecedor fornecedorBD = findFornecedorByEmail(email);
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

    public Fornecedor deactivateFornecedor(Integer idFornecedor) {
        Fornecedor fornecedor = getFornecedorByID(idFornecedor);
        fornecedor.setActive(false);
        return fornecedorRepository.save(fornecedor);
    }

    public Consumidor deactivateConsumidor(Integer idConsumidor) {
        Consumidor consumidor = getConsumidorByID(idConsumidor);
        consumidor.setActive(false);
        return consumidorRepository.save(consumidor);
    }

    public Fornecedor activateFornecedor(Integer idFornecedor) {
        Fornecedor fornecedor = getFornecedorByID(idFornecedor);
        fornecedor.setActive(true);
        return fornecedorRepository.save(fornecedor);
    }

    public Consumidor activateConsumidor(Integer idConsumidor) {
        Consumidor consumidor = getConsumidorByID(idConsumidor);
        consumidor.setActive(true);
        return consumidorRepository.save(consumidor);
    }

    public void verifyAccount(String email) throws InactiveAccountException {

        Utilizador utilizador = getUtilizadorByEmail(email);
        if (!utilizador.isActive()) {
            throw new InactiveAccountException("A sua conta está inativa");
        }
    }

    public void deleteUtilizadorByEmail(String email) {
        utilizadorRepository.deleteById(getUtilizadorByEmail(email).getIdUtilizador());

    }


}
