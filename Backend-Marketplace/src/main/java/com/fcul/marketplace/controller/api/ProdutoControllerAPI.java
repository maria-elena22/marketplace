package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.config.security.SecurityUtils;
import com.fcul.marketplace.dto.produto.*;
import com.fcul.marketplace.dto.uniProd.ProdutoUniProdDTO;
import com.fcul.marketplace.exceptions.ForbiddenActionException;
import com.fcul.marketplace.exceptions.JWTTokenMissingException;
import com.fcul.marketplace.exceptions.MissingPropertiesException;
import com.fcul.marketplace.exceptions.TooMuchPropertiesException;
import com.fcul.marketplace.model.Produto;
import com.fcul.marketplace.model.ProdutoFornecedorInfo;
import com.fcul.marketplace.model.UniProd;
import com.fcul.marketplace.model.enums.IVA;
import com.fcul.marketplace.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produto")
public class ProdutoControllerAPI {
    @Autowired
    ProdutoService produtoService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SecurityUtils securityUtils;

    //============================GET=============================

    @GetMapping()
    @Operation(summary = "getProdutos",
            description = "Devolve todos os Produtos podendo os resultados serem filtrados por propriedadeId, subcategoriaId," +
                    " categoriaId, unidadeId, nomeProduto e/ou preço")
    @Parameters(value = {
            @Parameter(name = "propriedadeId", description = "Filtrar resultados por ID da Propriedade"),
            @Parameter(name = "subcategoriaId", description = "Filtrar resultados por ID da Subcategoria"),
            @Parameter(name = "categoriaId", description = "Filtrar resultados por ID da Categoria"),
            @Parameter(name = "nomeProduto", description = "Filtrar resultados por produtos cujo o nome contem o parametro"),
            @Parameter(name = "precoMin", description = "Filtrar resultados por produtos que estão acima deste valor"),
            @Parameter(name = "precoMax", description = "Filtrar resultados por produtos que estão abaixo deste valor"),
            @Parameter(name = "unidadeId", description = "Filtrar resultados por ID da Unidade de Produção"),
            @Parameter(name = "page", description = "A pagina requerida"),
            @Parameter(name = "size", description = "A dimensao das paginas"),
            @Parameter(name = "sortKey", description = "Chave do ordenamento"),
            @Parameter(name = "sortDir", description = "Direcao do ordenamento")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @CrossOrigin("*")
    public List<ProdutoConsumidorDTO> getProdutos(@RequestParam(required = false) Integer propriedadeId,
                                                  @RequestParam(required = false) Integer subcategoriaId,
                                                  @RequestParam(required = false) Integer categoriaId,
                                                  @RequestParam(required = false) Integer unidadeId,
                                                  @RequestParam(required = false) String nomeProduto,
                                                  @RequestParam(required = false) Double precoMin,
                                                  @RequestParam(required = false) Double precoMax,
                                                  @RequestParam(required = false) IVA iva,
                                                  @RequestParam(required = false) String descricao,
                                                  @RequestParam(required = false) Integer page,
                                                  @RequestParam(required = false) Integer size,
                                                  @RequestParam(required = false) String sortKey,
                                                  @RequestParam(required = false) Sort.Direction sortDir) {

        List<Produto> produtos = produtoService.getProdutos(propriedadeId, subcategoriaId, categoriaId, unidadeId, nomeProduto
                , precoMax, precoMin, iva, descricao,page, size, sortDir, sortKey);
        return produtos.stream()
                .map(produto -> modelMapper.map(produto, ProdutoConsumidorDTO.class)).collect(Collectors.toList());
    }

    @GetMapping("/fornecedor")
    @Operation(summary = "getProdutosFornecedor",
            description = "Devolve todos os Produtos podendo os resultados serem filtrados por propriedadeId, subcategoriaId," +
                    " categoriaId, unidadeId, nomeProduto e/ou preço")
    @Parameters(value = {
            @Parameter(name = "propriedadeId", description = "Filtrar resultados por ID da Propriedade"),
            @Parameter(name = "subcategoriaId", description = "Filtrar resultados por ID da Subcategoria"),
            @Parameter(name = "categoriaId", description = "Filtrar resultados por ID da Categoria"),
            @Parameter(name = "nomeProduto", description = "Filtrar resultados por produtos cujo o nome contem o parametro"),
            @Parameter(name = "precoMin", description = "Filtrar resultados por produtos que estão acima deste valor"),
            @Parameter(name = "precoMax", description = "Filtrar resultados por produtos que estão abaixo deste valor"),
            @Parameter(name = "unidadeId", description = "Filtrar resultados por ID da Unidade de Produção"),
            @Parameter(name = "page", description = "A pagina requerida"),
            @Parameter(name = "size", description = "A dimensao das paginas"),
            @Parameter(name = "sortKey", description = "Chave do ordenamento"),
            @Parameter(name = "sortDir", description = "Direcao do ordenamento")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    @CrossOrigin("*")
    public List<ProdutoFornecedorDTO> getProdutosFornecedor(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                                            @RequestParam(required = false) Integer propriedadeId,
                                                            @RequestParam(required = false) Integer subcategoriaId,
                                                            @RequestParam(required = false) Integer categoriaId,
                                                            @RequestParam(required = false) Integer unidadeId,
                                                            @RequestParam(required = false) String nomeProduto,
                                                            @RequestParam(required = false) Double precoMin,
                                                            @RequestParam(required = false) Double precoMax,
                                                            @RequestParam(required = false) IVA iva,
                                                            @RequestParam(required = false) String descricao,
                                                            @RequestParam(required = false) Integer page,
                                                            @RequestParam(required = false) Integer size,
                                                            @RequestParam(required = false) String sortKey,
                                                            @RequestParam(required = false) Sort.Direction sortDir) throws JWTTokenMissingException {

        String emailFornecedor = securityUtils.getEmailFromAuthHeader(authorizationHeader);
        List<Produto> produtos = produtoService.getProdutosFornecedor(emailFornecedor, propriedadeId, subcategoriaId, categoriaId, unidadeId, nomeProduto
                , precoMax, precoMin, iva,descricao, page, size, sortDir, sortKey);

        List<ProdutoFornecedorDTO> produtoDTOS = new ArrayList<>();
        for (Produto produto : produtos) {

            ProdutoFornecedorDTO produtoFornecedorDTO = modelMapper.map(produto, ProdutoFornecedorDTO.class);
            List<ProdutoFornecedorInfo> meusPrecos = produto.getPrecoFornecedores().stream().filter(pf -> pf.getFornecedor().getEmail().equals(emailFornecedor)).toList();
            List<SimpleProdutoFornecedorInfoDTO> meusPrecosDTOS = meusPrecos.stream().map(pf -> modelMapper.map(pf, SimpleProdutoFornecedorInfoDTO.class)).toList();
            produtoFornecedorDTO.setPrecoFornecedor(meusPrecosDTOS);

            List<ProdutoUniProdDTO> uniProds1 = filterMyUniProds(emailFornecedor, produto);
            produtoFornecedorDTO.setUniProds(uniProds1);

            produtoDTOS.add(produtoFornecedorDTO);
        }
        return produtoDTOS;
    }

    private static List<ProdutoUniProdDTO> filterMyUniProds(String emailFornecedor, Produto produto) {
        List<UniProd> myUniProds = produto.getUniProds().stream().filter(uniProd -> uniProd.getFornecedor().getEmail().equals(emailFornecedor)).toList();
        List<ProdutoUniProdDTO> uniProds1 = new ArrayList<>();
        for (UniProd uniProd : myUniProds) {
            ProdutoUniProdDTO uniProdDTO = new ProdutoUniProdDTO();
            uniProdDTO.setNomeUniProd(uniProd.getNomeUniProd());
            uniProdDTO.setIdUnidade(uniProd.getIdUnidade());
            uniProdDTO.setStock(uniProd.getStocks().stream().filter(stock1 -> stock1.getProduto().equals(produto)).findFirst().get().getQuantidade());
            uniProds1.add(uniProdDTO);
        }
        return uniProds1;
    }

    //===========================INSERT===========================

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "insertProduto",
            description = "Adiciona um novo Produto à BD")
    @Parameters(value = {
            @Parameter(name = "uniProdsIds", description = "Unidades de Produção a associar ao produto"),
            @Parameter(name = "subCategoriasIds", description = "SubCategorias a associar ao produto"),
            @Parameter(name = "preco", description = "Preço do produto")

    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    @CrossOrigin("*")
    public FullProdutoDTO insertProduto(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                        @RequestBody ProdutoPropriedadesDTO produtoPropriedadesDTO,
                                        @RequestParam List<Integer> uniProdsIds,
                                        @RequestParam List<Integer> subCategoriasIds,
                                        @RequestParam Double preco)
            throws MissingPropertiesException, TooMuchPropertiesException, JWTTokenMissingException, ForbiddenActionException {
        Integer stock = produtoPropriedadesDTO.getStock();
        Produto produto = modelMapper.map(produtoPropriedadesDTO.getProdutoDTO(), Produto.class);
        return produtoService.addProduto(securityUtils.getEmailFromAuthHeader(authorizationHeader), produto,
                uniProdsIds, subCategoriasIds, preco, produtoPropriedadesDTO.getPropriedades(), stock);


    }

    //===========================UPDATE===========================

    @PutMapping("/{idProduto}")
    @Operation(summary = "updateProduto",
            description = "Atualiza os dados de um Produto")
    @Parameters(value = {
            @Parameter(name = "idProduto", description = "ID do Produto a atualizar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    @CrossOrigin("*")
    public ProdutoDTO updateProduto(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                    @PathVariable Integer idProduto,
                                    @RequestBody ProdutoPrecoDTO produtoPrecoDTO) throws JWTTokenMissingException, ForbiddenActionException {
        Produto produto = produtoService.updateProduto(securityUtils.getEmailFromAuthHeader(authorizationHeader), idProduto, produtoPrecoDTO);
        return modelMapper.map(produto, ProdutoDTO.class);
    }

    @PutMapping("/remove/{idProduto}")
    @Operation(summary = "removeProduto",
            description = "Remoção de um Produto (parar de fornecer)")
    @Parameters(value = {
            @Parameter(name = "idProduto", description = "ID do Produto a remover")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    @CrossOrigin("*")
    public void removeProduto(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                              @PathVariable Integer idProduto) throws JWTTokenMissingException, ForbiddenActionException {
        produtoService.removeProduto(securityUtils.getEmailFromAuthHeader(authorizationHeader), idProduto);
    }


    @PutMapping("/unidade/{produtoId}")
    @Operation(summary = "addUniProds",
            description = "Adiciona o Produto indicado às Unidades de Produção com os IDs indicados")
    @Parameters(value = {
            @Parameter(name = "produtoId", description = "ID do Produto a atualizar"),
            @Parameter(name = "uniProdsIds", description = "Unidades a associar")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    @CrossOrigin("*")
    public FullProdutoDTO addUniProds(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                      @PathVariable Integer produtoId,
                                      @RequestParam List<Integer> uniProdsIds,
                                      @RequestParam Double preco, @RequestParam Integer stock) throws JWTTokenMissingException, ForbiddenActionException {
        return produtoService.addUniProds(securityUtils.getEmailFromAuthHeader(authorizationHeader),
                produtoId, uniProdsIds, preco, stock);
    }

    @PutMapping("/unidade/remove/{produtoId}")
    @Operation(summary = "removeUniProds",
            description = "Remove o Produto indicado das Unidades de Produção com os IDs indicados")
    @Parameters(value = {
            @Parameter(name = "produtoId", description = "ID do Produto a atualizar"),
            @Parameter(name = "uniProdsIds", description = "Unidades a desassociar")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    @CrossOrigin("*")
    public FullProdutoDTO removeUniProds(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                         @PathVariable Integer produtoId,
                                         @RequestParam List<Integer> uniProdsIds) throws JWTTokenMissingException, ForbiddenActionException {
        return produtoService.removeUniProds(securityUtils.getEmailFromAuthHeader(authorizationHeader),
                produtoId, uniProdsIds);

    }


    @PutMapping("/subcategoria/{produtoId}")
    @Operation(summary = "addSubCategorias",
            description = "Adiciona as SubCategorias com os IDs indicados ao Produto indicado")
    @Parameters(value = {
            @Parameter(name = "produtoId", description = "ID do Produto a atualizar"),
            @Parameter(name = "subCategoriasIds", description = "SubCategorias a associar"),
            @Parameter(name = "propriedades", description = "Propriedades a associar")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    @CrossOrigin("*")
    public FullProdutoDTO addSubCategorias(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                           @PathVariable Integer produtoId,
                                           @RequestParam List<Integer> subCategoriasIds,
                                           @RequestParam Map<Integer, String> propriedades)
            throws MissingPropertiesException, TooMuchPropertiesException, JWTTokenMissingException, ForbiddenActionException {

        return produtoService.addSubCategorias(securityUtils.getEmailFromAuthHeader(authorizationHeader),
                produtoId, subCategoriasIds, propriedades);

    }

    @PutMapping("/subcategoria/remove/{produtoId}")
    @Operation(summary = "removeSubCategorias",
            description = "Remove as SubCategorias com o ID indicado do Produto indicado")
    @Parameters(value = {
            @Parameter(name = "produtoId", description = "ID do Produto a atualizar"),
            @Parameter(name = "subCategoriasIds", description = "SubCategorias a desasociar")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    @CrossOrigin("*")
    public FullProdutoDTO removeSubCategorias(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                              @PathVariable Integer produtoId, @RequestParam List<Integer> subCategoriasIds)
            throws JWTTokenMissingException, ForbiddenActionException {

        return produtoService.removeSubCategorias(securityUtils.getEmailFromAuthHeader(authorizationHeader),
                produtoId, subCategoriasIds);

    }

    @PutMapping("/unidade/stock/{produtoId}")
    @Operation(summary = "updateUniProdStock",
            description = "Atualiza o stock de um produto de uma Unidade de Produção")
    @Parameters(value = {
            @Parameter(name = "idUniProd", description = "ID da Unidade de produção"),
            @Parameter(name = "stock", description = "Novo Stock"),
            @Parameter(name = "produtoId", description = "ID do Produto a alterar o stock"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    @CrossOrigin("*")
    public FullProdutoDTO updateUniProdStock(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                                   @RequestParam Integer idUniProd, @RequestParam Integer stock, @PathVariable Integer produtoId) throws JWTTokenMissingException, ForbiddenActionException {
        String emailFornecedor = securityUtils.getEmailFromAuthHeader(authorizationHeader);
        Produto produto = produtoService.updateUniProdStock(emailFornecedor, idUniProd, stock, produtoId);
        FullProdutoDTO fullProdutoDTO = modelMapper.map(produto, FullProdutoDTO.class);

        List<ProdutoFornecedorInfo> meusPrecos = produto.getPrecoFornecedores().stream().filter(pf -> pf.getFornecedor().getEmail().equals(emailFornecedor)).toList();
        List<ProdutoFornecedorInfoDTO> meusPrecosDTOS = meusPrecos.stream().map(pf -> modelMapper.map(pf, ProdutoFornecedorInfoDTO.class)).toList();
        fullProdutoDTO.setPrecoFornecedores(meusPrecosDTOS);

        List<ProdutoUniProdDTO> uniProds1 = filterMyUniProds(emailFornecedor, produto);
        fullProdutoDTO.setUniProds(uniProds1);

        return fullProdutoDTO;
    }


}
