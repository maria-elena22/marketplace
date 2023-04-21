package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.config.security.SecurityUtils;
import com.fcul.marketplace.dto.uniProd.UniProdDTO;
import com.fcul.marketplace.dto.uniProd.UniProdInputDTO;
import com.fcul.marketplace.exceptions.ForbiddenActionException;
import com.fcul.marketplace.exceptions.JWTTokenMissingException;
import com.fcul.marketplace.model.UniProd;
import com.fcul.marketplace.service.UniProdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/uniProd")
public class UniProdControllerAPI {

    @Autowired
    UniProdService uniProdService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SecurityUtils securityUtils;

    //============================GET=============================

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
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    public List<UniProdDTO> getUniProds(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(required = false) String nomeUniProd,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortKey,
            @RequestParam(required = false) Sort.Direction sortDir
    ) throws JWTTokenMissingException {

        List<UniProd> uniProds = uniProdService.getUniProds(securityUtils.getEmailFromAuthHeader(authorizationHeader), nomeUniProd, page, size, sortKey, sortDir);
        List<UniProdDTO> uniProdDTOS = uniProds.stream()
                .map(uniProd -> modelMapper.map(uniProd, UniProdDTO.class)).collect(Collectors.toList());
        return uniProdDTOS;
    }


    //===========================INSERT===========================

    @PostMapping()
    @Operation(summary = "insertUniProd",
            description = "Adiciona uma nova Unidade de Produção à BD, associada ao Fornecedor com o ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    public UniProdDTO insertUniProd(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader
            ,@RequestBody UniProdInputDTO uniProdDTO) throws JWTTokenMissingException{
        UniProd uniProd = modelMapper.map(uniProdDTO, UniProd.class);
        return modelMapper.map(uniProdService.addUniProd(securityUtils.getEmailFromAuthHeader(authorizationHeader), uniProd), UniProdDTO.class);
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
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    public UniProdDTO updateUniProd(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                    @PathVariable Integer idUniProd, @RequestBody UniProdInputDTO uniProdDTO) throws JWTTokenMissingException,ForbiddenActionException{
        UniProd uniProd = modelMapper.map(uniProdDTO, UniProd.class);
        return modelMapper.map(uniProdService.updateUniProd(securityUtils.getEmailFromAuthHeader(authorizationHeader),idUniProd, uniProd), UniProdDTO.class);
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
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    public void deleteUniProd(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                              @PathVariable Integer idUniProd) throws JWTTokenMissingException, ForbiddenActionException {
        uniProdService.deleteUniProd(securityUtils.getEmailFromAuthHeader(authorizationHeader),idUniProd);
    }
}
