package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.dto.UtilizadorDTO;
import com.fcul.marketplace.model.Consumidor;
import com.fcul.marketplace.model.Fornecedor;
import com.fcul.marketplace.service.UtilizadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/utilizador")
public class UtilizadorControllerAPI {

    @Autowired
    UtilizadorService utilizadorService;

    @Autowired
    ModelMapper modelMapper;

    //============================GET=============================

    @GetMapping("/consumidor")
    @Operation(summary = "getConsumidores",
            description = "Devolve todos os Consumidores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public List<UtilizadorDTO> getConsumidores() {

        List<Consumidor> consumidores = utilizadorService.getConsumidores();
        List<UtilizadorDTO> utilizadorDTOS = consumidores
                .stream().map(consumidor -> modelMapper.map(consumidor, UtilizadorDTO.class)).collect(Collectors.toList());
        return utilizadorDTOS;
    }

    @GetMapping("/fornecedor")
    @Operation(summary = "getFornecedores",
            description = "Devolve todos os Fornecedores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public List<UtilizadorDTO> getFornecedores() {

        List<Fornecedor> fornecedores = utilizadorService.getFornecedores();
        List<UtilizadorDTO> utilizadorDTOS = fornecedores
                .stream().map(fornecedor -> modelMapper.map(fornecedor, UtilizadorDTO.class)).collect(Collectors.toList());
        return utilizadorDTOS;
    }

    @GetMapping("/consumidor/{idConsumidor}")
    @Operation(summary = "getConsumidorByID",
            description = "Devolve o Consumidor com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "idConsumidor", description = "ID do Consumidor")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public UtilizadorDTO getConsumidorByID(@PathVariable Integer idConsumidor) {
        return modelMapper.map(utilizadorService.getConsumidorByID(idConsumidor), UtilizadorDTO.class);
    }

    @GetMapping("/fornecedor/{idFornecedor}")
    @Operation(summary = "getFornecedorByID",
            description = "Devolve o Fornecedor com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "idFornecedor", description = "ID do Fornecedor")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public UtilizadorDTO getFornecedorByID(@PathVariable Integer idFornecedor) {
        return modelMapper.map(utilizadorService.getFornecedorByID(idFornecedor), UtilizadorDTO.class);
    }

    //===========================INSERT===========================

    @PostMapping("/consumidor")
    @Operation(summary = "insertConsumidor",
            description = "Adiciona um novo Consumidor à BD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public UtilizadorDTO insertConsumidor(@RequestBody UtilizadorDTO utilizadorDTO) {
        Consumidor consumidor = modelMapper.map(utilizadorDTO, Consumidor.class);
        consumidor = utilizadorService.addConsumidor(consumidor);
        return modelMapper.map(consumidor, UtilizadorDTO.class);
    }

    @PostMapping("/fornecedor")
    @Operation(summary = "insertFornecedor",
            description = "Adiciona um novo Fornecedor à BD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public UtilizadorDTO insertFornecedor(@RequestBody UtilizadorDTO utilizadorDTO) {
        Fornecedor fornecedor = modelMapper.map(utilizadorDTO, Fornecedor.class);
        fornecedor = utilizadorService.addFornecedor(fornecedor);
        return modelMapper.map(fornecedor, UtilizadorDTO.class);
    }

    //===========================UPDATE===========================

    @PutMapping("/consumidor/{idConsumidor}")
    @Operation(summary = "updateConsumidor",
            description = "Atualiza os dados de um Consumidor")
    @Parameters(value = {
            @Parameter(name = "idConsumidor", description = "ID do Consumidor a atualizar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public UtilizadorDTO updateConsumidor(@PathVariable Integer idConsumidor, @RequestBody UtilizadorDTO utilizadorDTO) {
        Consumidor consumidor = modelMapper.map(utilizadorDTO, Consumidor.class);
        consumidor = utilizadorService.updateConsumidor(idConsumidor, consumidor);
        return modelMapper.map(consumidor, UtilizadorDTO.class);
    }

    @PutMapping("/fornecedor/{idFornecedor}")
    @Operation(summary = "updateFornecedor",
            description = "Atualiza os dados de um Fornecedor")
    @Parameters(value = {
            @Parameter(name = "idFornecedor", description = "ID do Fornecedor a atualizar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public UtilizadorDTO updateFornecedor(@PathVariable Integer idFornecedor, @RequestBody UtilizadorDTO utilizadorDTO) {
        Fornecedor fornecedor = modelMapper.map(utilizadorDTO, Fornecedor.class);
        fornecedor = utilizadorService.updateFornecedor(idFornecedor, fornecedor);
        return modelMapper.map(fornecedor, UtilizadorDTO.class);
    }

    //===========================DELETE===========================

    @DeleteMapping("/consumidor/{idConsumidor}")
    @Operation(summary = "deleteConsumidor",
            description = "Apaga o Consumidor com o ID indicado da BD")
    @Parameters(value = {
            @Parameter(name = "idConsumidor", description = "ID do Consumidor a apagar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public void deleteConsumidor(@PathVariable Integer idConsumidor) {
        utilizadorService.deleteConsumidor(idConsumidor);
    }

    @DeleteMapping("/fornecedor/{idFornecedor}")
    @Operation(summary = "deleteFornecedor",
            description = "Apaga o Fornecedor com o ID indicado da BD")
    @Parameters(value = {
            @Parameter(name = "idFornecedor", description = "ID do Fornecedor a apagar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public void deleteFornecedor(@PathVariable Integer idFornecedor) {
        utilizadorService.deleteFornecedor(idFornecedor);
    }

    @DeleteMapping("/consumidor")
    @Operation(summary = "deleteConsumidorBatch",
            description = "Apaga os Consumidores com os IDs indicados da BD")
    @Parameters(value = {
            @Parameter(name = "ids", description = "IDs dos Consumidores a apagar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public void deleteConsumidorBatch(@RequestParam List<Integer> ids) {
        utilizadorService.deleteConsumidorBatch(ids);
    }

    @DeleteMapping("/fornecedor")
    @Operation(summary = "deleteFornecedorBatch",
            description = "Apaga os Fornecedores com os IDs indicados da BD")
    @Parameters(value = {
            @Parameter(name = "ids", description = "IDs dos Fornecedores a apagar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public void deleteFornecedorBatch(@RequestParam List<Integer> ids) {
        utilizadorService.deleteFornecedorBatch(ids);
    }


}
