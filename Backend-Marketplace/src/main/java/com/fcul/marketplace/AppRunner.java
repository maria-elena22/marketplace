package com.fcul.marketplace;

import com.fcul.marketplace.model.Categoria;
import com.fcul.marketplace.model.Propriedade;
import com.fcul.marketplace.model.SubCategoria;
import com.fcul.marketplace.repository.*;
import com.fcul.marketplace.service.CategoriaService;
import com.fcul.marketplace.service.TransporteService;
import com.fcul.marketplace.service.UniProdService;
import com.fcul.marketplace.service.UtilizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public void run(ApplicationArguments args) {

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

        List<Categoria> categoriaList = categoriaService.getCategorias(null, null, null, null, null);
        if (categoriaList.isEmpty()) {
            Categoria categoria = new Categoria();
            categoria.setNomeCategoria("Mercearia");
            categoria = categoriaService.addCategoria(categoria);

            Propriedade propriedade = new Propriedade();
            propriedade.setNomePropriedade("Prazo de validade");
            categoriaService.addPropriedade(categoria.getIdCategoria(), propriedade);

            SubCategoria subCategoria = new SubCategoria();
            subCategoria.setNomeSubCategoria("Horticulas");
            subCategoria.setCategoria(categoria);
            subCategoria.setSubCategoriaPai(null);
            subCategoria = categoriaService.addSubCategoria(subCategoria, null, categoria.getIdCategoria());

            SubCategoria subCategoria1 = new SubCategoria();
            subCategoria1.setNomeSubCategoria("Fruta");
            subCategoria1.setCategoria(categoria);
            subCategoria1.setSubCategoriaPai(null);
            subCategoria1 = categoriaService.addSubCategoria(subCategoria1, null, categoria.getIdCategoria());

            SubCategoria subCategoria2 = new SubCategoria();
            subCategoria2.setNomeSubCategoria("Nacionais");
            subCategoria2 = categoriaService.addSubCategoria(subCategoria2, subCategoria.getIdSubCategoria(), categoria.getIdCategoria());

            SubCategoria subCategoria3 = new SubCategoria();
            subCategoria3.setNomeSubCategoria("Nacionais");
            subCategoria3 = categoriaService.addSubCategoria(subCategoria3, subCategoria1.getIdSubCategoria(), categoria.getIdCategoria());
        }

    }


}
