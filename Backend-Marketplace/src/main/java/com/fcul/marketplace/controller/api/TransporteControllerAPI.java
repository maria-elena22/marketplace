package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.config.security.SecurityUtils;
import com.fcul.marketplace.dto.transporte.TransporteDTO;
import com.fcul.marketplace.dto.transporte.TransporteInputDTO;
import com.fcul.marketplace.exceptions.ForbiddenActionException;
import com.fcul.marketplace.exceptions.JWTTokenMissingException;
import com.fcul.marketplace.model.Transporte;
import com.fcul.marketplace.model.enums.EstadoTransporte;
import com.fcul.marketplace.service.TransporteService;
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
@RequestMapping("/api/transporte")
public class TransporteControllerAPI {

    @Autowired
    TransporteService transporteService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SecurityUtils securityUtils;

    //============================GET=============================

    @GetMapping()
    @Operation(summary = "getTransportesFornecedor",
            description = "Devolve os transportes do fornecedor com o ID indicado, podendo os resultados serem filtrados por estado do transporte")
    @Parameters(value = {
            @Parameter(name = "idUniProd", description = "ID da Unidade De Producao"),
            @Parameter(name = "estadoTransporte", description = "Filtrar resultados por transportes com o estado indicado"),
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
    public List<TransporteDTO> getTransportesFornecedor(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                                        @RequestParam(required = false) Integer unidadeProducaoId,
                                                        @RequestParam(required = false) EstadoTransporte estadoTransporte,
                                                        @RequestParam(required = false) Integer page,
                                                        @RequestParam(required = false) Integer size,
                                                        @RequestParam(required = false) String sortKey,
                                                        @RequestParam(required = false) Sort.Direction sortDir) throws JWTTokenMissingException, ForbiddenActionException {
        List<Transporte> transportes = transporteService.getTransportesFornecedor(securityUtils.getEmailFromAuthHeader(authorizationHeader)
                , unidadeProducaoId, estadoTransporte, page, size, sortKey, sortDir);
        return transportes.stream()
                .map(transporte -> modelMapper.map(transporte, TransporteDTO.class)).collect(Collectors.toList());
    }

    //===========================INSERT===========================

    @PostMapping("{uniProdId}")
    @Operation(summary = "insertTransporte",
            description = "Adiciona um novo Transporte à BD, associado ao Fornecedor com o ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    @CrossOrigin("*")
    public TransporteDTO insertTransporte(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                          @PathVariable Integer uniProdId,
                                          @RequestBody TransporteInputDTO transporteDTO) throws JWTTokenMissingException, ForbiddenActionException {
        Transporte transporte = modelMapper.map(transporteDTO, Transporte.class);
        return modelMapper.map(transporteService.addTransporte(securityUtils.getEmailFromAuthHeader(authorizationHeader),
                uniProdId, transporte), TransporteDTO.class);
    }

    //===========================UPDATE===========================

    @PutMapping("/{idTransporte}")
    @Operation(summary = "updateTransporte",
            description = "Atualiza os dados de um Transporte")
    @Parameters(value = {
            @Parameter(name = "idTransporte", description = "ID do Transporte a atualizar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    @CrossOrigin("*")
    public TransporteDTO updateTransporte(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                          @PathVariable Integer idTransporte,
                                          @RequestBody TransporteInputDTO transporteDTO) throws JWTTokenMissingException, ForbiddenActionException {
        Transporte transporte = modelMapper.map(transporteDTO, Transporte.class);
        return modelMapper.map(transporteService.updateTransporte(securityUtils.getEmailFromAuthHeader(authorizationHeader), idTransporte, transporte), TransporteDTO.class);
    }

    //===========================DELETE===========================

    @DeleteMapping("/{idTransporte}")
    @Operation(summary = "deleteTransporte",
            description = "Apaga o Transporte com o ID indicado da BD")
    @Parameters(value = {
            @Parameter(name = "idTransporte", description = "ID do Transporte a apagar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    @CrossOrigin("*")
    public void deleteTransporte(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                 @PathVariable Integer idTransporte) throws JWTTokenMissingException, ForbiddenActionException {
        transporteService.deleteTransporte(securityUtils.getEmailFromAuthHeader(authorizationHeader), idTransporte);
    }

}
