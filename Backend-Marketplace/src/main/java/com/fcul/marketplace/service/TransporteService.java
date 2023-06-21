package com.fcul.marketplace.service;

import com.fcul.marketplace.exceptions.ForbiddenActionException;
import com.fcul.marketplace.model.Fornecedor;
import com.fcul.marketplace.model.Transporte;
import com.fcul.marketplace.model.UniProd;
import com.fcul.marketplace.model.Viagem;
import com.fcul.marketplace.model.enums.EstadoTransporte;
import com.fcul.marketplace.model.enums.EstadoViagem;
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

    @Autowired
    UniProdService uniProdService;

    //============================GET=============================

    public Transporte getTransporteById(Integer id) {
        return transporteRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Transporte> getTransportesFornecedor(String emailFornecedor, Integer uniProdId, EstadoTransporte estadoTransporte,
                                                     Integer page, Integer size, String sortKey, Sort.Direction sortDir)
            throws ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(emailFornecedor);
        Pageable pageable = PageableUtils.getDefaultPageable(page, size, sortDir, sortKey);
        if (uniProdId != null) {
            UniProd uniProd = uniProdService.getUniProdByID(uniProdId);
            if (!uniProd.getFornecedor().getIdUtilizador().equals(fornecedor.getIdUtilizador())) {
                throw new ForbiddenActionException("Você não pode ver os transportes desta Unidade de Produção");
            }

            return transporteRepository.findByUniProdId(uniProdId, estadoTransporte, pageable).getContent();
        } else {
            return transporteRepository.findByFornecedorId(fornecedor.getIdUtilizador(), estadoTransporte, pageable).getContent();
        }
    }

    //===========================INSERT===========================

    @Transactional
    public Transporte addTransporte(String fornecedorEmail, Integer idUniProd, Transporte transporte) throws ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(fornecedorEmail);
        UniProd uniProd = uniProdService.getUniProdByID(idUniProd);
        if (!uniProd.getFornecedor().getIdUtilizador().equals(fornecedor.getIdUtilizador())) {
            throw new ForbiddenActionException("Você não pode adicionar transportes nesta Unidade de Produção");
        }
        transporte.setUnidadeDeProducao(uniProd);
        transporte.setActive(true);
        transporte = transporteRepository.save(transporte);
        return transporte;
    }
    //===========================UPDATE===========================

    public Transporte updateTransporte(String fornecedorEmail, Integer transporteId, Transporte transporte) throws ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(fornecedorEmail);
        Transporte transporteBD = transporteRepository.findById(transporteId).orElseThrow(EntityNotFoundException::new);
        if (!transporteBD.getUnidadeDeProducao().getFornecedor().getIdUtilizador().equals(fornecedor.getIdUtilizador())) {
            throw new ForbiddenActionException("Você não pode atualizar transportes nesta Unidade de Produção");
        }
        transporteBD.setEstadoTransporte(transporte.getEstadoTransporte());
        transporteBD.setMatricula(transporte.getMatricula());
        return transporteRepository.save(transporteBD);
    }

    public Transporte deactivateTransporte(String fornecedorEmail, Integer transporteId) throws ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(fornecedorEmail);
        Transporte transporteBD = transporteRepository.findById(transporteId).orElseThrow(EntityNotFoundException::new);
        if (!transporteBD.getUnidadeDeProducao().getFornecedor().getIdUtilizador().equals(fornecedor.getIdUtilizador())) {
            throw new ForbiddenActionException("Você não pode remover transportes nesta Unidade de Produção");
        }
        transporteBD.setActive(false);
        return transporteRepository.save(transporteBD);
    }

    //===========================DELETE===========================

    public void deleteTransporte(String fornecedorEmail, Integer transporteId) throws ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(fornecedorEmail);
        Transporte transporteBD = transporteRepository.findById(transporteId).orElseThrow(EntityNotFoundException::new);
        if (!transporteBD.getUnidadeDeProducao().getFornecedor().getIdUtilizador().equals(fornecedor.getIdUtilizador())) {
            throw new ForbiddenActionException("Você não pode apagar este transporte");
        }

        if (!verifyDeactivationTransporte(transporteBD)){
            throw new ForbiddenActionException("Não pode eliminar este Transporte porque ainda tem viagens não finalizadas!");
        }

        transporteBD.setActive(false);
        transporteRepository.save(transporteBD);
        //transporteRepository.deleteById(transporteId);
    }

    private boolean verifyDeactivationTransporte(Transporte transporte) {
            for (Viagem v : transporte.getViagens()){
                if(!v.getEstadoViagem().equals(EstadoViagem.FINALIZADA)){
                    return false;
                }
            }
        return true;

    }

}
