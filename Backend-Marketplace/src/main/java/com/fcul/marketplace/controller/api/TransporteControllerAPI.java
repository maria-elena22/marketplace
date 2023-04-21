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

    //============================GET=============================

    @GetMapping("/{idTransporte}")
    @Operation(summary = "getTransporteByID",
            description = "Devolve o transporte com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "idTransporte", description = "ID do Transporte")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public TransporteDTO getTransporteByID(@PathVariable Integer idTransporte) {
        Transporte transporte = transporteService.getTransporteById(idTransporte);
        return modelMapper.map(transporte, TransporteDTO.class);
    }

    @GetMapping("/fornecedor/{idFornecedor}")
    @Operation(summary = "getTransportesFornecedor",
            description = "Devolve os transportes do fornecedor com o ID indicado, podendo os resultados serem filtrados por estado do transporte")
    @Parameters(value = {
            @Parameter(name = "idFornecedor", description = "ID do Fornecedor"),
            @Parameter(name = "estadoTransporte", description = "Filtrar resultados por transportes com o estado indicado"),
            @Parameter(name = "page", description = "A pagina requerida"),
            @Parameter(name = "size", description = "A dimensao das paginas"),
            @Parameter(name = "sortKey", description = "Chave do ordenamento"),
            @Parameter(name = "sortDir", description = "Direcao do ordenamento")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public List<TransporteDTO> getTransportesFornecedor(@PathVariable Integer idFornecedor,
                                                        @RequestParam(required = false) EstadoTransporte estadoTransporte,
                                                        @RequestParam(required = false) Integer page,
                                                        @RequestParam(required = false) Integer size,
                                                        @RequestParam(required = false) String sortKey,
                                                        @RequestParam(required = false) Sort.Direction sortDir) {

        List<Transporte> transportes = transporteService.getTransportesFornecedor(idFornecedor, estadoTransporte, page, size, sortKey, sortDir);
        List<TransporteDTO> transporteDTOS = transportes.stream()
                .map(transporte -> modelMapper.map(transporte, TransporteDTO.class)).collect(Collectors.toList());
        return transporteDTOS;
    }

    //===========================INSERT===========================

    @PostMapping("/{idFornecedor}")
    @Operation(summary = "insertTransporte",
            description = "Adiciona um novo Transporte à BD, associado ao Fornecedor com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "idFornecedor", description = "Fornecedor a associar ao Transporte")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public TransporteDTO insertTransporte(@RequestBody TransporteDTO transporteDTO, @PathVariable Integer idFornecedor) {
        Transporte transporte = modelMapper.map(transporteDTO, Transporte.class);
        return modelMapper.map(transporteService.addTransporte(transporte, idFornecedor), TransporteDTO.class);
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
    public TransporteDTO updateTransporte(@PathVariable Integer idTransporte, @RequestBody TransporteDTO transporteDTO) {
        Transporte transporte = modelMapper.map(transporteDTO, Transporte.class);
        return modelMapper.map(transporteService.updateTransporte(idTransporte, transporte), TransporteDTO.class);
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
    public void deleteTransporte(@PathVariable Integer idTransporte) {
        transporteService.deleteTransporte(idTransporte);
    }

    @DeleteMapping()
    @Operation(summary = "deleteTransportes",
            description = "Apaga os Transportes com os IDs indicados da BD")
    @Parameters(value = {
            @Parameter(name = "ids", description = "IDs dos Transporte a apagar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public void deleteTransportes(@RequestParam List<Integer> ids) {
        transporteService.deleteTransporteBatch(ids);
    }
}
