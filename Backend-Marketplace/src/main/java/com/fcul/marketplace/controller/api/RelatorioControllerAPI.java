package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.config.security.SecurityUtils;
import com.fcul.marketplace.dto.relatorio.RelatorioPorDistanciasDTO;
import com.fcul.marketplace.dto.relatorio.RelatorioPorZonasDTO;
import com.fcul.marketplace.exceptions.JWTTokenMissingException;
import com.fcul.marketplace.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/relatorio")
public class RelatorioControllerAPI {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    RelatorioService relatorioService;

    @Autowired
    SecurityUtils securityUtils;

    //============================GET=============================//

    @GetMapping("/zona")
    @Operation(summary = "getRelatorioPorZonas",
            description = "Devolve o Relatório por Zonas das encomendas do utilizador")
    @Parameters(value = {
            @Parameter(name = "categoriasIds", description = "Categorias a filtrar"),
            @Parameter(name = "dataMin", description = "Filtrar resultados por produtos que estão depois desta data"),
            @Parameter(name = "dataMax", description = "Filtrar resultados por produtos que estão antes desta data")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR", "ADMIN", "CONSUMIDOR"})
    @CrossOrigin("*")
    public RelatorioPorZonasDTO getRelatorioPorZonas(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                                     @RequestParam(required = false) String dataMin,
                                                     @RequestParam(required = false) String dataMax,
                                                     @RequestParam(required = false) List<Integer> categoriasIds) throws JWTTokenMissingException, ParseException {

        return relatorioService.generateRelatorioZonas(securityUtils.getEmailFromAuthHeader(authorizationHeader), securityUtils.getRoleFromAuthHeader(authorizationHeader), categoriasIds, dataMin,dataMax);
    }

    @GetMapping("/distancia")
    @Operation(summary = "getRelatorioPorDistancias",
            description = "Devolve o Relatório por Distâncias das encomendas do utilizador")
    @Parameters(value = {
            @Parameter(name = "categoriasIds", description = "Categorias a filtrar"),
            @Parameter(name = "dataMin", description = "Filtrar resultados por produtos que estão depois desta data"),
            @Parameter(name = "dataMax", description = "Filtrar resultados por produtos que estão antes desta data")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR", "ADMIN", "CONSUMIDOR"})
    @CrossOrigin("*")
    public RelatorioPorDistanciasDTO getRelatorioPorDistancias(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                                               @RequestParam(required = false) String dataMin,
                                                               @RequestParam(required = false) String dataMax,
                                                               @RequestParam(required = false) List<Integer> categoriasIds) throws JWTTokenMissingException, ParseException {
        return relatorioService.generateRelatorioDistancias(securityUtils.getEmailFromAuthHeader(authorizationHeader),
                securityUtils.getRoleFromAuthHeader(authorizationHeader), categoriasIds, dataMin,dataMax);
    }
}



