package com.fcul.marketplace;

import com.fcul.marketplace.model.*;
import com.fcul.marketplace.model.enums.EstadoTransporte;
import com.fcul.marketplace.repository.*;
import com.fcul.marketplace.service.CategoriaService;
import com.fcul.marketplace.service.TransporteService;
import com.fcul.marketplace.service.UniProdService;
import com.fcul.marketplace.service.UtilizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    private TransporteService transporteService;

    @Autowired
    private UtilizadorService utilizadorService;

    @Autowired
    private UniProdService uniProdService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private TransporteRepository transporteRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private UniProdRepository uniProdRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private SubCategoriaRepository subCategoriaRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

//        transporteRepository.deleteAll();
//        fornecedorRepository.deleteAll();
//        uniProdRepository.deleteAll();
//        subCategoriaRepository.deleteAll();
//        categoriaRepository.deleteAll();
//
//        Fornecedor fornecedor = new Fornecedor();
//        //fornecedor.setContinente("");
//        fornecedor.setDistrito("");
//        fornecedor.setFreguesia("");
//        //fornecedor.setCoordenadas("");
//        fornecedor.setMorada("");
//        fornecedor.setMunicipio("");
//        //fornecedor.setPais("");
//        fornecedor.setNome("Maria");
//        fornecedor.setIdFiscal(123456789);
//        fornecedor.setTelemovel(914695962);
//
//        Fornecedor fornecedor2 = new Fornecedor();
//        //fornecedor.setContinente("");
//        fornecedor.setDistrito("");
//        fornecedor.setFreguesia("");
//        //fornecedor.setCoordenadas("");
//        fornecedor.setMorada("");
//        fornecedor.setMunicipio("");
//        //fornecedor.setPais("");
//        fornecedor.setNome("Elena");
//        fornecedor.setIdFiscal(987654321);
//        fornecedor.setTelemovel(256314789);
//
//        fornecedor = utilizadorService.addFornecedor(fornecedor);
//        fornecedor2 = utilizadorService.addFornecedor(fornecedor2);
//
//
//        Transporte transporte = new Transporte();
//        transporte.setEstadoTransporte(EstadoTransporte.DISPONIVEL);
//        transporte.setMatricula("12-12-12");
//
//        transporte = transporteService.addTransporte(transporte, fornecedor.getIdUtilizador());
//
//        UniProd uniProd = new UniProd();
//        uniProd.setNomeUniProd("Fabrica Porto");
//        UniProd uniProd2 = new UniProd();
//        uniProd2.setNomeUniProd("Fabrica Braga");
//
//        uniProd = uniProdService.addUniProd(fornecedor.getIdUtilizador(), uniProd);
//        uniProd2 = uniProdService.addUniProd(fornecedor2.getIdUtilizador(), uniProd2);
//
//
//        Categoria categoria = new Categoria();
//        categoria.setNomeCategoria("cat1");
//
//        SubCategoria subCategoria = new SubCategoria();
//        subCategoria.setNomeSubCategoria("subCat1");
//
//        categoria = categoriaService.addCategoria(categoria);
//        subCategoria = categoriaService.addSubCategoria(subCategoria, categoria.getIdCategoria());

    }


}
