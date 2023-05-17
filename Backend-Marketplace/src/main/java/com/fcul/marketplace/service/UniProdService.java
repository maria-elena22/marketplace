package com.fcul.marketplace.service;

import com.fcul.marketplace.exceptions.ForbiddenActionException;
import com.fcul.marketplace.model.*;
import com.fcul.marketplace.model.enums.EstadoViagem;
import com.fcul.marketplace.repository.UniProdRepository;
import com.fcul.marketplace.repository.utils.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UniProdService {

    @Autowired
    UniProdRepository uniProdRepository;

    @Autowired
    UtilizadorService utilizadorService;

    //============================GET=============================//

    public List<UniProd> getUniProds(String fornecedorEmail, String nomeUniProd, Integer page,
                                     Integer size, String sortKey, Sort.Direction sortDir) {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(fornecedorEmail);

        Pageable pageable = PageableUtils.getDefaultPageable(page, size, sortDir, sortKey);

        return uniProdRepository.findByOpt(fornecedor.getIdUtilizador(), nomeUniProd, pageable).getContent();
    }

    public UniProd getUniProdByID(Integer idUnidade) {
        return uniProdRepository.findById(idUnidade).orElseThrow(EntityNotFoundException::new);
    }

    //===========================INSERT===========================//

    public UniProd addUniProd(String fornecedorEmail, UniProd uniProd) {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(fornecedorEmail);

        uniProd.setFornecedor(fornecedor);
        uniProd.setActive(true);
        return uniProdRepository.save(uniProd);
    }

    //===========================UPDATE===========================//

    public UniProd updateUniProd(String fornecedorEmail, Integer idUniProd, UniProd uniProd) throws ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(fornecedorEmail);
        UniProd uniProdBD = uniProdRepository.findById(idUniProd).orElseThrow(EntityNotFoundException::new);

        if (!uniProdBD.getFornecedor().getIdUtilizador().equals(fornecedor.getIdUtilizador())) {
            throw new ForbiddenActionException("Você não é o dono desta unidade de produção");
        }

        uniProdBD.setNomeUniProd(uniProd.getNomeUniProd());
        return uniProdRepository.save(uniProdBD);
    }

    public UniProd updateUniProdStock(String fornecedorEmail, Integer idUniProd, UniProd uniProd) throws ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(fornecedorEmail);
        UniProd uniProdBD = uniProdRepository.findById(idUniProd).orElseThrow(EntityNotFoundException::new);

        if (!uniProdBD.getFornecedor().getIdUtilizador().equals(fornecedor.getIdUtilizador())) {
            throw new ForbiddenActionException("Você não é o dono desta unidade de produção");
        }

        uniProdBD.setNomeUniProd(uniProd.getNomeUniProd());
        return uniProdRepository.save(uniProdBD);
    }

    //===========================DELETE===========================//

    public void deleteUniProd(String fornecedorEmail, Integer idUniProd) throws ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(fornecedorEmail);
        UniProd uniProdBD = uniProdRepository.findById(idUniProd).orElseThrow(EntityNotFoundException::new);
        if (!uniProdBD.getFornecedor().getIdUtilizador().equals(fornecedor.getIdUtilizador())) {
            throw new ForbiddenActionException("Você não é o dono desta unidade de produção");
        }

        if (!verifyDeactivationUniProd(uniProdBD)){
            throw new ForbiddenActionException("Não pode eliminar esta Unidade porque ainda tem viagens não finalizadas!");
        }
        for (Transporte t : uniProdBD.getTransportes()){
            t.setActive(false);

        }
        uniProdBD.setProdutos(new ArrayList<>());
        uniProdBD.setStocks(new ArrayList<>());
        uniProdBD.setActive(false);
        uniProdRepository.save(uniProdBD);
        //uniProdRepository.deleteById(idUniProd);
    }

    private boolean verifyDeactivationUniProd(UniProd uniProd) {
        for (Transporte t : uniProd.getTransportes()){
            for (Viagem v : t.getViagens()){
                if(!v.getEstadoViagem().equals(EstadoViagem.FINALIZADA)){
                    return false;
                }
            }
        }
        return true;

    }


}
