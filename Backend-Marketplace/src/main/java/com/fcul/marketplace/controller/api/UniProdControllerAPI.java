package com.fcul.marketplace.controller.api;


import com.fcul.marketplace.dto.UniProdDTO;
import com.fcul.marketplace.model.UniProd;
import com.fcul.marketplace.service.UniProdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/uniProd")
public class UniProdControllerAPI {

    @Autowired
    UniProdService uniProdService;

    @Autowired
    ModelMapper modelMapper;

    //============================GET=============================

    @GetMapping("/{idUniProd}")
    @Operation(summary = "getUniProdByID",
            description = "Devolve a Unidade de Produção com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "idUniProd", description = "ID da Unidade")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public UniProdDTO getUniProdByID(@PathVariable Integer idUniProd) {

        UniProd uniProd = uniProdService.getUniProdByID(idUniProd);
        return modelMapper.map(uniProd, UniProdDTO.class);
    }

    @GetMapping
    @Operation(summary = "getUniProds",
            description = "Devolve todas as Unidades de Produção podendo os resultados serem filtrados por fornecedorID ou conter nomeUniProd")
    @Parameters(value = {
            @Parameter(name = "fornecedorId", description = "Filtrar resultados por ID do Fornecedor"),
            @Parameter(name = "nomeUniProd", description = "Filtrar resultados por nomes que contem o parametro"),
            @Parameter(name = "page", description = "A pagina requerida"),
            @Parameter(name = "size", description = "A dimensao das paginas"),
            @Parameter(name = "sortKey", description = "Chave do ordenamento"),
            @Parameter(name = "sortDir", description = "Direcao do ordenamento")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public List<UniProdDTO> getUniProds(
            @RequestParam(required = false) Integer fornecedorId,
            @RequestParam(required = false) String nomeUniProd,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortKey,
            @RequestParam(required = false) Sort.Direction sortDir
    ) {

        List<UniProd> uniProds = uniProdService.getUniProds(fornecedorId, nomeUniProd, page, size, sortKey, sortDir);
        List<UniProdDTO> uniProdDTOS = uniProds.stream()
                .map(uniProd -> modelMapper.map(uniProd, UniProdDTO.class)).collect(Collectors.toList());
        return uniProdDTOS;
    }


    //===========================INSERT===========================

    @PostMapping("/{idFornecedor}")
    @Operation(summary = "insertUniProd",
            description = "Adiciona uma nova Unidade de Produção à BD, associada ao Fornecedor com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "idFornecedor", description = "ID do Fornecedor")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public UniProdDTO insertUniProd(@RequestBody UniProdDTO uniProdDTO, @PathVariable Integer idFornecedor) {
        UniProd uniProd = modelMapper.map(uniProdDTO, UniProd.class);
        return modelMapper.map(uniProdService.addUniProd(idFornecedor, uniProd), UniProdDTO.class);
    }

    //===========================UPDATE===========================

    @PutMapping("/{idUniProd}")
    @Operation(summary = "updateUniProd",
            description = "Atualiza os dados de uma Unidade de Produção")
    @Parameters(value = {
            @Parameter(name = "idUniProd", description = "ID da Unidade a atualizar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public UniProdDTO updateUniProd(@PathVariable Integer idUniProd, @RequestBody UniProdDTO uniProdDTO) {
        UniProd uniProd = modelMapper.map(uniProdDTO, UniProd.class);
        return modelMapper.map(uniProdService.updateUniProd(idUniProd, uniProd), UniProdDTO.class);
    }

    //===========================DELETE===========================

    @DeleteMapping("/{idUniProd}")
    @Operation(summary = "deleteUniProd",
            description = "Apaga a Unidade de Produção com o ID indicado da BD")
    @Parameters(value = {
            @Parameter(name = "idUniProd", description = "ID da Unidade a apagar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public void deleteUniProd(@PathVariable Integer idUniProd) {
        uniProdService.deleteUniProd(idUniProd);
    }

    @DeleteMapping()
    @Operation(summary = "deleUniProdBatch",
            description = "Apaga as Unidades de Produção com os IDs indicados da BD")
    @Parameters(value = {
            @Parameter(name = "ids", description = "IDs das Unidades a apagar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public void deleUniProdBatch(@RequestParam List<Integer> ids) {
        uniProdService.deleteUniProdBatch(ids);
    }
}
