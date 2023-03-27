package com.fcul.marketplace.service;

import com.fcul.marketplace.exceptions.MissingPropertiesException;
import com.fcul.marketplace.exceptions.TooMuchPropertiesException;
import com.fcul.marketplace.model.*;
import com.fcul.marketplace.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    EncomendaService encomendaService;

    @Autowired
    UniProdService uniProdService;

    @Autowired
    UtilizadorService utilizadorService;

    @Autowired
    CategoriaService categoriaService;

    //============================GET=============================

    public List<Produto> getProdutos() {
        return produtoRepository.findAll();
    }

    public Produto getProdutoByID(Integer id) {
        return produtoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Produto> getProdutosByCategoria(Integer categoriaId) {
        return produtoRepository.findByCategoria(categoriaId);
    }

    public List<Produto> getProdutosBySubCategoria(Integer subCategoriaId) {
        return produtoRepository.findBySubCategoria(subCategoriaId);
    }

    public List<Produto> getProdutosByNome(String nome) {
        return produtoRepository.findByNomeContaining(nome);
    }

    public List<Produto> getProdutosFiltro(String nome, Double precoMin, Double precoMax, Integer idCategoria) {
        return produtoRepository.getFilteredProducts(nome, precoMax, precoMin, idCategoria);
    }

//    public List<Produto> getProdutosByFornecedor(Integer idFornecedor) {
//        return produtoRepository.findByFornecedorIdUtilizador(idFornecedor);
//    }

    public List<Produto> getProdutosByEncomenda(Integer idEncomenda) {
//        Encomenda encomenda = encomendaService.getEncomendaByID(idEncomenda);
//        Set<Produto> produtos = new HashSet<>();
//        encomenda.getItens().stream().forEach(item -> produtos.add(item.getProduto()));
//        return new ArrayList<>(produtos);
        //TODO
        return null;
    }

    public List<Produto> getProdutosByUniProd(Integer idUnidade) {
        UniProd uniProd = uniProdService.getUniProdByID(idUnidade);
        return uniProd.getProdutos();
    }

    //===========================INSERT===========================

    @Transactional
    public Produto addProduto(Produto produto, List<Integer> uniProdsIds,
                              List<Integer> subCategoriasIds, Map<Integer, String> propriedades) throws MissingPropertiesException, TooMuchPropertiesException {

        List<SubCategoria> subCategorias = subCategoriasIds.stream().map(i -> categoriaService.getSubCategoriaByID(i)).collect(Collectors.toList());
        produto.setSubCategorias(subCategorias);

        List<UniProd> uniProds = uniProdsIds.stream().map(j -> uniProdService.getUniProdByID(j)).collect(Collectors.toList());
        produto.setUniProds(uniProds);

        Map<Propriedade, String> propriedadeMap = convertPropriedadesMap(propriedades);
        produto.setPropriedades(propriedadeMap);

        verifyProdutoPropriedades(produto);


        return produtoRepository.save(produto);
    }


    //===========================UPDATE===========================
    public Produto updateProduto(Integer id, Produto produto) {
        Produto produtoBD = produtoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        produtoBD.setNome(produto.getNome());
        produtoBD.setVatValue(produto.getVatValue());
        produtoBD.setPreco(produto.getPreco());
        produtoBD.setDataProducao(produto.getDataProducao());
        return produtoRepository.save(produtoBD);
    }

    public Produto addUniProds(Integer produtoId, List<Integer> uniProdsIds) {
        List<UniProd> uniProds = uniProdsIds.stream().map(id -> uniProdService.getUniProdByID(id)).collect(Collectors.toList());
        Produto produto = this.getProdutoByID(produtoId);
        produto.getUniProds().addAll(uniProds);
        return produtoRepository.save(produto);
    }

    public Produto removeUniProds(Integer produtoId, List<Integer> uniProdsIds) {
        List<UniProd> uniProds = uniProdsIds.stream().map(id -> uniProdService.getUniProdByID(id)).collect(Collectors.toList());
        Produto produto = this.getProdutoByID(produtoId);
        produto.getUniProds().removeAll(uniProds);
        return produtoRepository.save(produto);
    }

    public Produto addSubCategorias(Integer produtoId, List<Integer> subCategoriasIds, Map<Integer, String> propriedades) throws MissingPropertiesException, TooMuchPropertiesException {
        Produto produto = this.getProdutoByID(produtoId);
        List<SubCategoria> subCategorias = subCategoriasIds.stream().map(id -> categoriaService.getSubCategoriaByID(id)).collect(Collectors.toList());
        Map<Propriedade, String> propriedadeMap = convertPropriedadesMap(propriedades);
        produto.getSubCategorias().addAll(subCategorias);
        produto.getPropriedades().putAll(propriedadeMap);
        verifyProdutoPropriedades(produto);
        return produtoRepository.save(produto);
    }

    public Produto removeSubCategorias(Integer produtoId, List<Integer> subCategoriasIds) {
        Produto produto = this.getProdutoByID(produtoId);
        List<SubCategoria> subCategorias = subCategoriasIds.stream().map(id -> categoriaService.getSubCategoriaByID(id)).collect(Collectors.toList());
        Collection<Propriedade> propriedades = getAllPropriedadesFromSubCategoriasAux(subCategorias);
        produto.getPropriedades().remove(propriedades);
        produto.getSubCategorias().removeAll(subCategorias);
        return produtoRepository.save(produto);
    }

    //===========================DELETE===========================
    public void deleteProduto(Integer id) {
        produtoRepository.deleteById(id);
    }

    public void deleteProdutoBatch(List<Integer> ids) {
        produtoRepository.deleteAllByIdInBatch(ids);
    }


    //===========================AUX===========================

    private Collection<Propriedade> getAllPropriedadesFromSubCategoriasAux(List<SubCategoria> subCategorias) {
        Set<Propriedade> propriedadeSet = new HashSet<>();
        subCategorias.stream().forEach(subCategoria -> propriedadeSet.addAll(subCategoria.getCategoria().getPropriedades()));
        return propriedadeSet;
    }

    private Map<Propriedade, String> convertPropriedadesMap(Map<Integer, String> propriedadesIntMap) {
        Map<Propriedade, String> propriedadeMap = new HashMap<>();
        propriedadesIntMap.entrySet().stream().forEach(entry -> {
            Propriedade prop = categoriaService.getPropriedadeByID(entry.getKey());
            propriedadeMap.put(prop, entry.getValue());
        });
        return propriedadeMap;

    }


    private void verifyProdutoPropriedades(Produto produto) throws MissingPropertiesException, TooMuchPropertiesException {

        //Propriedades existentes no objecto
        Map<Propriedade, String> propriedades = produto.getPropriedades();

        //Propriedades q deve ter
        Collection<Propriedade> propriedadesToVerify = getAllPropriedadesFromSubCategoriasAux(produto.getSubCategorias());

        List<Propriedade> exceedingProperties = new ArrayList<>();

        propriedades.entrySet().forEach(entry -> {


            if (!propriedadesToVerify.contains(entry.getKey())) {
                //Esta propriedade esta a mais
                exceedingProperties.add(entry.getKey());
            } else {
                propriedadesToVerify.remove(entry.getKey());
            }

        });

        if (!propriedadesToVerify.isEmpty()) {
            //Existem propriedades em falta no objeto
            throw new MissingPropertiesException("As propriedades" + propriedadesToVerify + " encontram-se em falta");
        }
        if (!exceedingProperties.isEmpty()) {
            //Existem propriedades a mais no objeto
            throw new TooMuchPropertiesException("As propriedades" + exceedingProperties + " encontram-se a mais");
        }
    }
}
