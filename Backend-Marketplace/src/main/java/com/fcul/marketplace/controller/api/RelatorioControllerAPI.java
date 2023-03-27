package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.dto.relatorio.RelatorioPorDistanciasDTO;
import com.fcul.marketplace.dto.relatorio.RelatorioPorZonasDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relatorio")
public class RelatorioControllerAPI {

    @Autowired
    ModelMapper modelMapper;

    //============================GET=============================

    /*
    Relatorio Administrador
        Todas as encomendas
            Distancias entre cosumidor e fornecedor, utilizando gamas de distancias
            ou hierarquia freguesia,municipio,distrito,pais,continente e mundo
        Deve ser possivel filtrar estes relatorios por categoria e por timestamp de encomenda

    Relatorio Consumidor
        Todas as encomendas do consumidor
            Distancias entre consumidor e fornecedor, utilizando gamas de distancias
            ou hierarquia freguesia,municipio,distrito,pais,continente e mundo
        Deve ser possivel filtrar estes relatorios por categoria e por timestamp de encomenda
    Relatorio Fornecedor
        Todas as encomendas deste fornecedor
            Distancias entre cosumidor e fornecedor, utilizando gamas de distancias
            ou hierarquia freguesia,municipio,distrito,pais,continente e mundo
        Deve ser possivel filtrar estes relatorios por categoria e por timestamp de encomenda


     */

    @GetMapping("/zona")
    @Operation(summary = "getRelatorioPorZonas",
            description = "Devolve o Relatório por Zonas das encomendas do utilizador com o ID indicado, " +
                    "será mudado no futuro assim que implementarmos a autenticação, deixando de receber ids " +
                    "passando apenas a receber o token e através dele saberemos que tipo de relatório gerar.")
    @Parameters(value = {
            @Parameter(name = "consumidorId", description = "ID do consumidor para o qual o relatório será gerado"),
            @Parameter(name = "fornecedorId", description = "ID do fornecedor para o qual o relatório será gerado")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public RelatorioPorZonasDTO getRelatorioPorZonas(@RequestParam(required = false) Integer consumidorId,
                                                     @RequestParam(required = false) Integer fornecedorId) {
        return null;
    }

    @GetMapping("/distancia")
    @Operation(summary = "getRelatorioPorDistancias",
            description = "Devolve o Relatório por Distâncias das encomendas do utilizador com o ID indicado, " +
                    "será mudado no futuro assim que implementarmos a autenticação, deixando de receber ids " +
                    "passando apenas a receber o token e através dele saberemos que tipo de relatório gerar.")
    @Parameters(value = {
            @Parameter(name = "consumidorId", description = "ID do consumidor para o qual o relatório será gerado"),
            @Parameter(name = "fornecedorId", description = "ID do fornecedor para o qual o relatório será gerado")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public RelatorioPorDistanciasDTO getRelatorioPorDistancias(@RequestParam(required = false) Integer consumidorId,
                                                               @RequestParam(required = false) Integer fornecedorId) {
        return null;
    }
}



