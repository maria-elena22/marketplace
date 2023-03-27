package com.fcul.marketplace.service;

import com.fcul.marketplace.model.Fornecedor;
import com.fcul.marketplace.model.UniProd;
import com.fcul.marketplace.repository.UniProdRepository;
import com.fcul.marketplace.repository.utils.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UniProdService {

    @Autowired
    UniProdRepository uniProdRepository;

    @Autowired
    UtilizadorService utilizadorService;

    //============================GET=============================//

    public UniProd getUniProdByID(Integer id) {
        return uniProdRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<UniProd> getUniProdsFornecedor(Integer idFornecedor) {
        return uniProdRepository.findByFornecedorIdUtilizador(idFornecedor);
    }

    public List<UniProd> getUniProds(Integer fornecedorId, String nomeUniProd, Integer page,
                                     Integer size, String sortKey, Sort.Direction sortDir) {

        Pageable pageable = PageableUtils.getDefaultPageable(page, size, sortDir, sortKey);

        List<UniProd> uniProds = uniProdRepository.findByOpt(fornecedorId, nomeUniProd, pageable).getContent();

        return uniProds;
    }


    //===========================INSERT===========================//

    public UniProd addUniProd(Integer fornecedorId, UniProd uniProd) {
        Fornecedor fornecedor = utilizadorService.getFornecedorByID(fornecedorId);
        uniProd.setFornecedor(fornecedor);
        return uniProdRepository.save(uniProd);
    }

    //===========================UPDATE===========================//

    public UniProd updateUniProd(Integer idUniProd, UniProd uniProd) {
        UniProd uniProdBD = uniProdRepository.findById(idUniProd).orElseThrow(EntityNotFoundException::new);
        uniProdBD.setNomeUniProd(uniProd.getNomeUniProd());
        return uniProdRepository.save(uniProdBD);
    }

    //===========================DELETE===========================//

    public void deleteUniProd(Integer id) {
        uniProdRepository.deleteById(id);
    }

    public void deleteUniProdBatch(List<Integer> ids) {
        uniProdRepository.deleteAllByIdInBatch(ids);
    }


}
