package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.config.security.SecurityUtils;
import com.fcul.marketplace.dto.relatorio.RelatorioPorDistanciasDTO;
import com.fcul.marketplace.dto.relatorio.RelatorioPorZonasDTO;
import com.fcul.marketplace.exceptions.JWTTokenMissingException;
import com.fcul.marketplace.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR", "ADMIN", "CONSUMIDOR"})
    @CrossOrigin("*")
    public RelatorioPorZonasDTO getRelatorioPorZonas(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader) throws JWTTokenMissingException {

        return relatorioService.generateRelatorioZonas(securityUtils.getEmailFromAuthHeader(authorizationHeader), securityUtils.getRoleFromAuthHeader(authorizationHeader));
    }

    @GetMapping("/distancia")
    @Operation(summary = "getRelatorioPorDistancias",
            description = "Devolve o Relatório por Distâncias das encomendas do utilizador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR", "ADMIN", "CONSUMIDOR"})
    @CrossOrigin("*")
    public RelatorioPorDistanciasDTO getRelatorioPorDistancias(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader) throws JWTTokenMissingException {
        return relatorioService.generateRelatorioDistancias(securityUtils.getEmailFromAuthHeader(authorizationHeader),
                securityUtils.getRoleFromAuthHeader(authorizationHeader));
    }
}



