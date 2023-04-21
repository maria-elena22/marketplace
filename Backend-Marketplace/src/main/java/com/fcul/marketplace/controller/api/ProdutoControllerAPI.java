package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.dto.FullProdutoDTO;
import com.fcul.marketplace.dto.ProdutoDTO;
import com.fcul.marketplace.dto.ProdutoPropriedadesDTO;
import com.fcul.marketplace.exceptions.MissingPropertiesException;
import com.fcul.marketplace.exceptions.TooMuchPropertiesException;
import com.fcul.marketplace.model.Produto;
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

    //============================GET=============================

    @GetMapping()
    @Operation(summary = "getProdutos",
            description = "Devolve todos os Produtos podendo os resultados serem filtrados por subcategoriaId," +
                    " categoriaId, unidadeId, nomeProduto e/ou preço")
    @Parameters(value = {
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
    public List<FullProdutoDTO> getProdutos(@RequestParam(required = false) Integer subcategoriaId,
                                        @RequestParam(required = false) Integer categoriaId,
                                        @RequestParam(required = false) Integer unidadeId,
                                        @RequestParam(required = false) String nomeProduto,
                                        @RequestParam(required = false) Double precoMin,
                                        @RequestParam(required = false) Double precoMax,
                                        @RequestParam(required = false) IVA iva,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer size,
                                        @RequestParam(required = false) String sortKey,
                                        @RequestParam(required = false) Sort.Direction sortDir) {

        //TODO
        List<Produto> produtos = produtoService.getProdutos();
        List<FullProdutoDTO> produtoDTOS = produtos.stream()
                .map(produto -> modelMapper.map(produto, FullProdutoDTO.class)).collect(Collectors.toList());
        return produtoDTOS;
    }


    @GetMapping("/encomenda/{idEncomenda}")
    @Operation(summary = "getProdutosByEncomenda",
            description = "Devolve todos os Produtos da Encomenda com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "idEncomenda", description = "ID da Encomenda")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"CONSUMIDOR"})
    public List<ProdutoDTO> getProdutosByEncomenda(@PathVariable Integer idEncomenda) {
        List<Produto> produtos = produtoService.getProdutosByEncomenda(idEncomenda);
        List<ProdutoDTO> produtoDTOS = produtos.stream()
                .map(produto -> modelMapper.map(produto, ProdutoDTO.class)).collect(Collectors.toList());
        return produtoDTOS;
    }

    @GetMapping("/subencomenda/{idEncomenda}")
    @Operation(summary = "getProdutosByEncomenda",
            description = "Devolve todos os Produtos da Encomenda com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "idEncomenda", description = "ID da Encomenda")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    public List<ProdutoDTO> getProdutosBySubEncomenda(@PathVariable Integer idSubEncomenda) {
        List<Produto> produtos = produtoService.getProdutosByEncomenda(idSubEncomenda); //TODO getProdutosBySubEncomenda
        List<ProdutoDTO> produtoDTOS = produtos.stream()
                .map(produto -> modelMapper.map(produto, ProdutoDTO.class)).collect(Collectors.toList());
        return produtoDTOS;
    }

    //===========================INSERT===========================

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "insertProduto",
            description = "Adiciona um novo Produto à BD")
    @Parameters(value = {
            @Parameter(name = "uniProdsIds", description = "Unidades de Produção a associar ao produto"),
            @Parameter(name = "subCategoriasIds", description = "SubCategorias a associar ao produto")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"CONSUMIDOR"})
    public FullProdutoDTO insertProduto(@RequestBody ProdutoPropriedadesDTO produtoPropriedadesDTO,
                                    @RequestParam List<Integer> uniProdsIds,
                                    @RequestParam List<Integer> subCategoriasIds)
            throws MissingPropertiesException, TooMuchPropertiesException {
        Produto produto = modelMapper.map(produtoPropriedadesDTO.getProdutoDTO(), Produto.class);
        return modelMapper.map(produtoService.addProduto(produto, uniProdsIds, subCategoriasIds, produtoPropriedadesDTO.getPropriedades()), FullProdutoDTO.class);
    }

    //===========================UPDATE===========================

    @PutMapping("/{id}")
    @Operation(summary = "updateProduto",
            description = "Atualiza os dados de um Produto")
    @Parameters(value = {
            @Parameter(name = "id", description = "ID do Produto a atualizar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"CONSUMIDOR"})
    public ProdutoDTO updateProduto(@PathVariable Integer id, @RequestBody ProdutoDTO produtoDTO) {
        Produto produto = produtoService.updateProduto(id, modelMapper.map(produtoDTO, Produto.class));
        return modelMapper.map(produto, ProdutoDTO.class);
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
    public FullProdutoDTO addUniProds(@PathVariable Integer produtoId, @RequestParam List<Integer> uniProdsIds) {
        FullProdutoDTO produto = modelMapper.map(produtoService.addUniProds(produtoId, uniProdsIds), FullProdutoDTO.class);
        return produto;
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
    public FullProdutoDTO removeUniProds(@PathVariable Integer produtoId, @RequestParam List<Integer> uniProdsIds) {
        FullProdutoDTO produto = modelMapper.map(produtoService.removeUniProds(produtoId, uniProdsIds), FullProdutoDTO.class);
        return produto;
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
    public FullProdutoDTO addSubCategorias(@PathVariable Integer produtoId,
                                           @RequestParam List<Integer> subCategoriasIds,
                                           @RequestParam Map<Integer, String> propriedades)
            throws MissingPropertiesException, TooMuchPropertiesException {

        FullProdutoDTO produto = modelMapper.map(produtoService.addSubCategorias(produtoId, subCategoriasIds, propriedades), FullProdutoDTO.class);
        return produto;

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
    public FullProdutoDTO removeSubCategorias(@PathVariable Integer produtoId, @RequestParam List<Integer> subCategoriasIds) {

        FullProdutoDTO produto = modelMapper.map(produtoService.removeSubCategorias(produtoId, subCategoriasIds), FullProdutoDTO.class);
        return produto;

    }

    //===========================DELETE===========================

    @DeleteMapping("/{produtoId}")
    @Operation(summary = "deleteProduto",
            description = "Apaga o Produto com o ID indicado da BD")
    @Parameters(value = {
            @Parameter(name = "produtoId", description = "ID do Produto a apagar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public void deleteProduto(@PathVariable Integer produtoId) {
        produtoService.deleteProduto(produtoId);
    }

}
