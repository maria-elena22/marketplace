package com.fcul.marketplace.service;

import com.fcul.marketplace.dto.produto.FullProdutoDTO;
import com.fcul.marketplace.dto.produto.ProdutoPrecoDTO;
import com.fcul.marketplace.exceptions.ForbiddenActionException;
import com.fcul.marketplace.exceptions.MissingPropertiesException;
import com.fcul.marketplace.exceptions.TooMuchPropertiesException;
import com.fcul.marketplace.model.*;
import com.fcul.marketplace.model.enums.IVA;
import com.fcul.marketplace.repository.ProdutoRepository;
import com.fcul.marketplace.repository.utils.PageableUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    UniProdService uniProdService;

    @Autowired
    UtilizadorService utilizadorService;

    @Autowired
    CategoriaService categoriaService;

    @Autowired
    ModelMapper modelMapper;




    public List<Produto> getProdutos(Integer propriedadeId, Integer subcategoriaId, Integer categoriaId, Integer unidadeId, String nomeProduto, Double precoMax, Double precoMin, IVA iva,String descricao, Integer page, Integer size, Sort.Direction sortDir, String sortKey) {
        List<Integer> subCategoriasIds= null;
        if(subcategoriaId!=null){
                subCategoriasIds = resolveSubCategorias(subcategoriaId, new ArrayList<>());
        }
        Pageable pageable = PageableUtils.getDefaultPageable(page, size, sortDir, sortKey);
        Boolean shouldEvaluateList = subcategoriaId!=null;
        return produtoRepository.findByOpt(null, propriedadeId, categoriaId, unidadeId, nomeProduto, subCategoriasIds, precoMin, precoMax, iva, descricao,pageable,shouldEvaluateList).getContent();
    }

    private List<Integer> resolveSubCategorias(Integer subCategoriaId, List<Integer> list) {
        SubCategoria subCategoria = categoriaService.getSubCategoriaByID(subCategoriaId);
        if (subCategoria.getSubCategoriasFilhos() == null || subCategoria.getSubCategoriasFilhos().isEmpty()) {
            list.add(subCategoria.getIdSubCategoria());
        } else {
            for (SubCategoria subCategoria1 : subCategoria.getSubCategoriasFilhos()) {
                resolveSubCategorias(subCategoria1.getIdSubCategoria(), list);
            }
        }
        return list;
    }


    public List<Produto> getProdutosFornecedor(String emailFornecedor, Integer propriedadeId, Integer subcategoriaId, Integer categoriaId, Integer unidadeId, String nomeProduto, Double precoMax, Double precoMin, IVA iva,String descricao ,Integer page, Integer size, Sort.Direction sortDir, String sortKey) {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(emailFornecedor);
        List<Integer> subCategoriasId= null;
        if(subcategoriaId!=null){
            subCategoriasId = resolveSubCategorias(subcategoriaId, new ArrayList<>());
        }
        Pageable pageable = PageableUtils.getDefaultPageable(page, size, sortDir, sortKey);
        Boolean shouldEvaluateList = subcategoriaId!=null;
        return produtoRepository.findByOpt(fornecedor.getIdUtilizador(), propriedadeId, categoriaId, unidadeId, nomeProduto, subCategoriasId, precoMin, precoMax, iva, descricao ,pageable, shouldEvaluateList).getContent();
    }

    public Produto getProdutoByID(Integer id) {
        return produtoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public FullProdutoDTO addProduto(String emailFornecedor, Produto produto, List<Integer> uniProdsIds,
                                     List<Integer> subCategoriasIds, Double preco, Map<Integer, String> propriedades, Integer stock) throws MissingPropertiesException, TooMuchPropertiesException, ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(emailFornecedor);

        List<SubCategoria> subCategorias = subCategoriasIds.stream().map(i -> categoriaService.getSubCategoriaByID(i)).collect(Collectors.toList());
        produto.setSubCategorias(subCategorias);

        List<UniProd> uniProds = uniProdsIds.stream().map(j -> uniProdService.getUniProdByID(j)).collect(Collectors.toList());
        verifyFornecedorUniProds(fornecedor, uniProds);
        produto.setUniProds(uniProds);

        for (UniProd uniProd : uniProds) {
            Stock stock1 = new Stock();
            stock1.setQuantidade(stock);
            stock1.setProduto(produto);
            uniProd.addStock(stock1);
        }

        Map<Propriedade, String> propriedadeMap = convertPropriedadesMap(propriedades);
        produto.setPropriedades(propriedadeMap);

        verifyProdutoPropriedades(produto);

        ProdutoFornecedorInfo produtoFornecedorInfo = new ProdutoFornecedorInfo(fornecedor, preco);

        produto.addPrecoFornecedor(produtoFornecedorInfo);


        produto = produtoRepository.save(produto);

        return produto.convert(modelMapper);
    }

    //===========================UPDATE===========================
    public Produto updateProduto(String fornecedorEmail, Integer id, ProdutoPrecoDTO produtoPrecoDTO) throws ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(fornecedorEmail);
        Produto produtoBD = getProdutoByID(id);

        verifyFornecedorProduto(produtoBD, fornecedor);

        produtoBD.setNome(produtoPrecoDTO.getProdutoDTO().getNome());
        produtoBD.setIva(produtoPrecoDTO.getProdutoDTO().getIva());
        produtoBD.setDescricao(produtoPrecoDTO.getProdutoDTO().getDescricao());

        Optional<ProdutoFornecedorInfo> produtoFornecedorInfo = produtoBD.getPrecoFornecedores().stream()
                .filter(precoFornecedor -> precoFornecedor.getFornecedor().equals(fornecedor)).findFirst();

        produtoFornecedorInfo.ifPresent(fornecedorInfo -> fornecedorInfo.setPreco(produtoPrecoDTO.getPreco()));


        return produtoRepository.save(produtoBD);
    }


    //===========================AUX===========================

    public FullProdutoDTO addUniProds(String fornecedorEmail, Integer produtoId, List<Integer> uniProdsIds, Double preco, Integer stock) throws ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(fornecedorEmail);

        List<UniProd> uniProds = uniProdsIds.stream().map(id -> uniProdService.getUniProdByID(id)).collect(Collectors.toList());
        verifyFornecedorUniProds(fornecedor, uniProds);

        Produto produto = this.getProdutoByID(produtoId);
        Produto finalProduto = produto;
        Optional<UniProd> uniProdContainingProduto = uniProds.stream()
                .filter(uniProd -> uniProd.getProdutos().contains(finalProduto))
                .findFirst();

        if (uniProdContainingProduto.isPresent()) {
            throw new ForbiddenActionException("A unidade de producao " + uniProdContainingProduto.get().getNomeUniProd() + " já contem o produto que está a tentar inserir.");
        }


        Produto finalProduto1 = produto;
        produto.getPrecoFornecedores().stream()
                .filter(precoFornecedor -> precoFornecedor.getFornecedor().equals(fornecedor))
                .findFirst()
                .ifPresentOrElse(
                        produtoFornecedorInfo -> produtoFornecedorInfo.setPreco(preco),
                        () -> finalProduto1.addPrecoFornecedor(new ProdutoFornecedorInfo(fornecedor, preco))
                );


        for (UniProd uniProd : uniProds) {
            Stock stock1 = new Stock();
            stock1.setQuantidade(stock);
            stock1.setProduto(produto);
            uniProd.addStock(stock1);
        }


        produto.getUniProds().addAll(uniProds);
        produto = produtoRepository.save(produto);

        List<UniProd> uniProdList = filterMyUniProds(fornecedorEmail,produto);
        produto.setUniProds(
                uniProdList
        );
        return produto.convert(modelMapper);    }

    public FullProdutoDTO removeUniProds(String fornecedorEmail, Integer produtoId, List<Integer> uniProdsIds) throws ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(fornecedorEmail);
        List<UniProd> uniProds = uniProdsIds.stream().map(id -> uniProdService.getUniProdByID(id)).toList();
        Produto produto = this.getProdutoByID(produtoId);

        produto = removeUniProdsAux(fornecedor, produto, uniProds);

        List<UniProd> uniProdList = filterMyUniProds(fornecedorEmail,produto);
        produto.setUniProds(
                uniProdList
        );
        return produto.convert(modelMapper);    }

    public FullProdutoDTO addSubCategorias(String fornecedorEmail, Integer produtoId, List<Integer> subCategoriasIds, Map<Integer, String> propriedades) throws MissingPropertiesException, TooMuchPropertiesException, ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(fornecedorEmail);
        Produto produto = this.getProdutoByID(produtoId);
        verifyFornecedorProduto(produto, fornecedor);
        List<SubCategoria> subCategorias = subCategoriasIds.stream().map(id -> categoriaService.getSubCategoriaByID(id)).toList();
        Map<Propriedade, String> propriedadeMap = convertPropriedadesMap(propriedades);
        produto.getSubCategorias().addAll(subCategorias);
        produto.getPropriedades().putAll(propriedadeMap);
        verifyProdutoPropriedades(produto);
        produto =  produtoRepository.save(produto);

        List<UniProd> uniProdList = filterMyUniProds(fornecedorEmail,produto);
        produto.setUniProds(
                uniProdList
        );
        return produto.convert(modelMapper);
    }

    public FullProdutoDTO removeSubCategorias(String fornecedorEmail, Integer produtoId, List<Integer> subCategoriasIds) throws ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(fornecedorEmail);
        Produto produto = this.getProdutoByID(produtoId);
        verifyFornecedorProduto(produto, fornecedor);
        List<SubCategoria> subCategorias = subCategoriasIds.stream().map(id -> categoriaService.getSubCategoriaByID(id)).collect(Collectors.toList());
        Collection<Propriedade> propriedades = getAllPropriedadesFromSubCategoriasAux(subCategorias);
        produto.removePropriedades(propriedades);
        produto.getSubCategorias().removeAll(subCategorias);
        if (produto.getSubCategorias().isEmpty()) {
            throw new ForbiddenActionException("É obrigatório o produto ter pelo menos 1 sub categoria");
        }
        produto = produtoRepository.save(produto);
        List<UniProd> uniProdList = filterMyUniProds(fornecedorEmail,produto);
        produto.setUniProds(
                uniProdList
        );
        return produto.convert(modelMapper);
    }

    @Transactional
    public Produto updateUniProdStock(String emailFornecedor, Integer idUniProd, Integer stock, Integer produtoId) throws ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(emailFornecedor);
        UniProd uniProd = uniProdService.getUniProdByID(idUniProd);
        Produto produto = produtoRepository.findById(produtoId).orElseThrow(EntityNotFoundException::new);
        verifyFornecedor(fornecedor, uniProd, produto);
        if (stock < 0) {
            throw new ForbiddenActionException("O novo stock não pode ser negativo!");
        }

        Stock stock2 = uniProd.getStocks().stream().filter(stock1 -> stock1.getProduto().equals(produto)).findFirst().get();
        stock2.setQuantidade(stock);
        return produtoRepository.save(produto);
    }

    public void removeProduto(String emailFornecedor, Integer idProduto) throws ForbiddenActionException {
        Fornecedor fornecedor = utilizadorService.findFornecedorByEmail(emailFornecedor);
        Produto produto = produtoRepository.findById(idProduto).orElseThrow(EntityNotFoundException::new);
        List<UniProd> uniProds = fornecedor.getUnidadesProducao().stream().filter(uniProd -> uniProd.getProdutos().contains(produto)).toList();

        removeUniProdsAux(fornecedor, produto, uniProds);

    }

    public Produto removeUniProdsAux(Fornecedor fornecedor, Produto produto, List<UniProd> uniProds) throws ForbiddenActionException {

        verifyFornecedorUniProds(fornecedor, uniProds);
        verifyUniProdsProduto(uniProds, produto);

        produto.getUniProds().removeAll(uniProds);
        uniProds.forEach(uniProd -> uniProd.getStocks().removeIf(stock -> stock.getProduto().equals(produto)));

        List<UniProd> uniProdsRestantesFornecedor = produto.getUniProds().stream().filter(uniProd -> uniProd.getFornecedor().getIdUtilizador().equals(fornecedor.getIdUtilizador())).toList();
        //se produto nao tem mais uniprods do fornecedor tira o preco
        if (uniProdsRestantesFornecedor.isEmpty()) {
            Optional<ProdutoFornecedorInfo> pf = produto.getPrecoFornecedores().stream().filter(pfi -> pfi.getFornecedor().getIdUtilizador().equals(fornecedor.getIdUtilizador())).findFirst();
            pf.ifPresent(produtoFornecedorInfo -> produto.getPrecoFornecedores().remove(produtoFornecedorInfo));

        }
        return produtoRepository.save(produto);
    }

    private Collection<Propriedade> getAllPropriedadesFromSubCategoriasAux(List<SubCategoria> subCategorias) {
        Set<Propriedade> propriedadeSet = new HashSet<>();
        subCategorias.forEach(subCategoria -> propriedadeSet.addAll(subCategoria.getCategoria().getPropriedades()));
        return propriedadeSet;
    }

    private Map<Propriedade, String> convertPropriedadesMap(Map<Integer, String> propriedadesIntMap) {
        Map<Propriedade, String> propriedadeMap = new HashMap<>();
        propriedadesIntMap.forEach((key, value) -> {
            Propriedade prop = categoriaService.getPropriedadeByID(key);
            propriedadeMap.put(prop, value);
        });
        return propriedadeMap;

    }

    private void verifyProdutoPropriedades(Produto produto) throws MissingPropertiesException, TooMuchPropertiesException {

        //Propriedades existentes no objecto
        Map<Propriedade, String> propriedades = produto.getPropriedades();

        //Propriedades q deve ter
        Collection<Propriedade> propriedadesToVerify = getAllPropriedadesFromSubCategoriasAux(produto.getSubCategorias());

        List<Propriedade> exceedingProperties = new ArrayList<>();

        propriedades.forEach((key, value) -> {

            if (!propriedadesToVerify.contains(key)) {
                //Esta propriedade esta a mais
                exceedingProperties.add(key);
            } else {
                propriedadesToVerify.remove(key);
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

    private static List<UniProd> filterMyUniProds(String emailFornecedor, Produto produto) {
        return produto.getUniProds().stream().filter(uniProd -> uniProd.getFornecedor().getEmail().equals(emailFornecedor)).toList();
    }
    private void verifyFornecedor(Fornecedor fornecedor, UniProd uniProd, Produto produto) throws ForbiddenActionException {
        verifyUniProdProduto(uniProd, produto);
        verifyFornecedorProduto(produto, fornecedor);
        verifyFornecedorUniProd(fornecedor, uniProd);
    }

    private void verifyUniProdProduto(UniProd uniProd, Produto produto) throws ForbiddenActionException {
        //ver se uniprod tem produto
        if (!uniProd.getProdutos().contains(produto)) {
            throw new ForbiddenActionException("A unidade de Produção " + uniProd.getNomeUniProd() + " não produz este produto");
        }
    }

    private void verifyFornecedorProduto(Produto produto, Fornecedor fornecedor) throws ForbiddenActionException {
        Optional<ProdutoFornecedorInfo> produtoFornecedorInfo = produto.getPrecoFornecedores().stream().filter(precoFornecedor -> precoFornecedor.getFornecedor().equals(fornecedor)).findFirst();
        if (produtoFornecedorInfo.isEmpty()) {
            throw new ForbiddenActionException("Você não fornece " + produto.getNome());
        }
    }


    private void verifyFornecedorUniProd(Fornecedor fornecedor, UniProd uniProd) throws ForbiddenActionException {
        if (!uniProd.getFornecedor().getIdUtilizador().equals(fornecedor.getIdUtilizador())) {
            throw new ForbiddenActionException("A unidade de Produção " + uniProd.getNomeUniProd() + " não lhe pertence");
        }
    }

    private void verifyFornecedorUniProds(Fornecedor fornecedor, List<UniProd> uniProds) throws ForbiddenActionException {
        if (!uniProds.stream().allMatch(uniProd -> uniProd.getFornecedor().getIdUtilizador().equals(fornecedor.getIdUtilizador()))) {
            throw new ForbiddenActionException("Pelo menos uma unidade de Produção não lhe pertence");
        }
    }

    private void verifyUniProdsProduto(List<UniProd> uniProds, Produto produto) throws ForbiddenActionException {
        if (!uniProds.stream().allMatch(uniProd -> uniProd.getProdutos().contains(produto))) {
            throw new ForbiddenActionException("Pelo menos uma unidade de Produção não produz este produto");
        }
    }
}
