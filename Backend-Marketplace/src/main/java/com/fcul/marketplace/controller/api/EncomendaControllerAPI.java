package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.dto.encomenda.CompraDTO;
import com.fcul.marketplace.dto.encomenda.EncomendaDTO;
import com.fcul.marketplace.dto.encomenda.FullEncomendaDTO;
import com.fcul.marketplace.exceptions.EncomendaAlreadyCancelledException;
import com.fcul.marketplace.exceptions.EncomendaCannotBeCancelledException;
import com.fcul.marketplace.model.Encomenda;
import com.fcul.marketplace.model.enums.EstadoEncomenda;
import com.fcul.marketplace.service.EncomendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/encomenda")
public class EncomendaControllerAPI {

    @Autowired
    EncomendaService encomendaService;

    @Autowired
    ModelMapper modelMapper;

    //============================GET=============================

    @GetMapping("/{encomendaId}")
    @Operation(summary = "getEncomendaByID",
            description = "Devolve a Encomenda com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "encomendaId", description = "ID da Encomenda")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public EncomendaDTO getEncomendaByID(@PathVariable Integer encomendaId) {
        Encomenda encomenda = encomendaService.getEncomendaByID(encomendaId);
        return modelMapper.map(encomenda, EncomendaDTO.class);
    }

    @GetMapping
    @Operation(summary = "getEncomendas",
            description = "Devolve todas as encomendas, podendo os resultados serem filtrados por preço, data da encomenda, " +
                    "tipo e estado da encomenda")
    @Parameters(value = {
            @Parameter(name = "precoMin", description = "Filtrar resultados por encomendas que estão acima deste valor"),
            @Parameter(name = "precoMax", description = "Filtrar resultados por encomendas que estão abaixo deste valor"),
            @Parameter(name = "dataMin", description = "Filtrar resultados por encomendas cuja data da encomenda é depois da data indicada"),
            @Parameter(name = "dataMax", description = "Filtrar resultados por encomendas cuja data da encomenda é antes da data indicada"),
            @Parameter(name = "estadoEncomenda", description = "Filtrar resultados por encomendas com o estado indicado"),
            @Parameter(name = "page", description = "A pagina requerida"),
            @Parameter(name = "size", description = "A dimensao das paginas"),
            @Parameter(name = "sortKey", description = "Chave do ordenamento"),
            @Parameter(name = "sortDir", description = "Direcao do ordenamento")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")})
    public List<FullEncomendaDTO> getEncomendas(@RequestParam(required = false) Double precoMin,
                                                @RequestParam(required = false) Double precoMax,
                                                @RequestParam(required = false) Date dataMin,
                                                @RequestParam(required = false) Date dataMax,
                                                @RequestParam(required = false) EstadoEncomenda estadoEncomenda,
                                                @RequestParam(required = false) Integer page,
                                                @RequestParam(required = false) Integer size,
                                                @RequestParam(required = false) String sortKey,
                                                @RequestParam(required = false) Sort.Direction sortDir) {
        //TODO
        List<Encomenda> encomendas = encomendaService.getEncomendas();
        List<FullEncomendaDTO> encomendaDTOS = encomendas.stream()
                .map(encomenda -> modelMapper.map(encomenda, FullEncomendaDTO.class)).collect(Collectors.toList());
        return encomendaDTOS;
    }

    @GetMapping("/fornecedor/{fornecedorId}")
    @Operation(summary = "getEncomendasByFornecedor",
            description = "Devolve todas as encomendas do Fornecedor com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "fornecedorId", description = "ID do Fornecedor"),
            @Parameter(name = "page", description = "A pagina requerida"),
            @Parameter(name = "size", description = "A dimensao das paginas"),
            @Parameter(name = "sortKey", description = "Chave do ordenamento"),
            @Parameter(name = "sortDir", description = "Direcao do ordenamento")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public List<FullEncomendaDTO> getEncomendasByFornecedor(@PathVariable Integer fornecedorId,
                                                            @RequestParam(required = false) Integer page,
                                                            @RequestParam(required = false) Integer size,
                                                            @RequestParam(required = false) String sortKey,
                                                            @RequestParam(required = false) Sort.Direction sortDir) {

        List<Encomenda> encomendas = encomendaService.getEncomendasByFornecedor(fornecedorId, page, size, sortKey, sortDir);
        List<FullEncomendaDTO> encomendaDTOS = encomendas.stream()
                .map(encomenda -> modelMapper.map(encomenda, FullEncomendaDTO.class)).collect(Collectors.toList());
        return encomendaDTOS;
    }

    @GetMapping("/consumidor/{idConsumidor}")
    @Operation(summary = "getEncomendasByConsumidor",
            description = "Devolve as encomendas do consumidor com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "idConsumidor", description = "ID do consumidor"),
            @Parameter(name = "page", description = "A pagina requerida"),
            @Parameter(name = "size", description = "A dimensao das paginas"),
            @Parameter(name = "sortKey", description = "Chave do ordenamento"),
            @Parameter(name = "sortDir", description = "Direcao do ordenamento")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public List<FullEncomendaDTO> getEncomendasByConsumidor(@PathVariable Integer idConsumidor,
                                                            @RequestParam(required = false) Integer page,
                                                            @RequestParam(required = false) Integer size,
                                                            @RequestParam(required = false) String sortKey,
                                                            @RequestParam(required = false) Sort.Direction sortDir) {

        List<Encomenda> encomendas = encomendaService.getEncomendasByConsumidor(idConsumidor, page, size, sortKey, sortDir);
        List<FullEncomendaDTO> encomendaDTOS = encomendas.stream()
                .map(encomenda -> modelMapper.map(encomenda, FullEncomendaDTO.class)).collect(Collectors.toList());
        return encomendaDTOS;
    }

    //===========================INSERT===========================

    @PostMapping("/{idConsumidor}")
    @Operation(summary = "insertEncomenda",
            description = "Adiciona uma nova Encomenda à BD, associada ao Consumidor com o ID indicado")
    @Parameters(value =
            {@Parameter(name = "idConsumidor", description = "Consumidor associado à Encomenda")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public FullEncomendaDTO insertEncomenda(@RequestBody CompraDTO compraDTO, @PathVariable Integer idConsumidor) {

        Encomenda encomenda = new Encomenda();
        encomenda.setPreco(compraDTO.getPreco());
        encomenda.setDataEncomenda(compraDTO.getDataEncomenda());
        encomenda.setEstadoEncomenda(EstadoEncomenda.A_PROCESSAR);
        //TODO
        //encomenda = encomendaService.addEncomenda(encomenda,idConsumidor,compraDTO.getItems());
        return modelMapper.map(encomenda,FullEncomendaDTO.class);
    }

    //===========================UPDATE===========================
    @PutMapping("/{idEncomenda}")
    @Operation(summary = "cancelEncomenda",
            description = "Cancela uma Encomenda")
    @Parameters(value = {
            @Parameter(name = "idEncomenda", description = "ID da Encomenda a cancelar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public EncomendaDTO cancelEncomenda(@PathVariable Integer idEncomenda) throws EncomendaAlreadyCancelledException, EncomendaCannotBeCancelledException {

        return modelMapper.map(encomendaService.cancelEncomenda(idEncomenda), EncomendaDTO.class);
    }

}
