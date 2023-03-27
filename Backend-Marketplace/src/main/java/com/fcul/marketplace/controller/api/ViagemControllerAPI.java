package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.dto.ViagemDTO;
import com.fcul.marketplace.model.Viagem;
import com.fcul.marketplace.service.ViagemService;
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
@RequestMapping("/api/viagem")
public class ViagemControllerAPI {

    @Autowired
    ViagemService viagemService;

    @Autowired
    ModelMapper modelMapper;

    //============================GET=============================

    @GetMapping("/{idViagem}")
    @Operation(summary = "getViagemByID",
            description = "Devolve a viagem com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "idViagem", description = "ID da Viagem")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public ViagemDTO getViagemByID(@PathVariable Integer idViagem) {
        Viagem viagem = viagemService.getViagemByID(idViagem);
        return modelMapper.map(viagem, ViagemDTO.class);
    }

    @GetMapping("/transporte/{idTransporte}")
    @Operation(summary = "getViagensTransporte",
            description = "Devolve as viagens do transporte com o ID dado")
    @Parameters(value = {
            @Parameter(name = "idTransporte", description = "ID do transporte"),
            @Parameter(name = "page", description = "A pagina requerida"),
            @Parameter(name = "size", description = "A dimensao das paginas"),
            @Parameter(name = "sortKey", description = "Chave do ordenamento"),
            @Parameter(name = "sortDir", description = "Direcao do ordenamento")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public List<ViagemDTO> getViagensTransporte(@PathVariable Integer idTransporte,
                                                @RequestParam(required = false) Integer page,
                                                @RequestParam(required = false) Integer size,
                                                @RequestParam(required = false) String sortKey,
                                                @RequestParam(required = false) Sort.Direction sortDir) {

        List<Viagem> viagens = viagemService.getViagensByTransporte(idTransporte, page, size, sortKey, sortDir);
        List<ViagemDTO> viagemDTOS = viagens.stream()
                .map(viagem -> modelMapper.map(viagem, ViagemDTO.class)).collect(Collectors.toList());
        return viagemDTOS;
    }

    //===========================INSERT===========================

    @PostMapping("/{idTransporte}")
    @Operation(summary = "insertViagem",
            description = "Adiciona uma nova Viagem à BD associada ao Transporte com o ID indicado")
    @Parameters(value =
            {@Parameter(name = "idTransporte", description = "Transporte associado à Viagem")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public ViagemDTO insertViagem(@RequestBody ViagemDTO viagemDTO, @PathVariable Integer idTransporte) {
        Viagem viagem = modelMapper.map(viagemDTO, Viagem.class);
        return modelMapper.map(viagemService.addViagem(viagem, idTransporte), ViagemDTO.class);
    }

    //===========================UPDATE===========================
    @PutMapping("/{idViagem}")
    @Operation(summary = "updateViagem",
            description = "Atualiza os dados de uma Viagem")
    @Parameters(value = {
            @Parameter(name = "idViagem", description = "ID da Viagem a atualizar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public ViagemDTO updateViagem(@PathVariable Integer idViagem, @RequestBody ViagemDTO viagemDTO) {
        Viagem viagem = modelMapper.map(viagemDTO, Viagem.class);
        return modelMapper.map(viagemService.updateViagem(idViagem, viagem), ViagemDTO.class);
    }
    //===========================DELETE===========================

    @DeleteMapping("/{idViagem}")
    @Operation(summary = "deleteViagem",
            description = "Apaga a Viagem com o ID indicado da BD")
    @Parameters(value = {
            @Parameter(name = "idViagem", description = "ID da Viagem a apagar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public void deleteViagem(@PathVariable Integer idViagem) {
        viagemService.deleteViagem(idViagem);
    }

    @DeleteMapping()
    @Operation(summary = "deleteViagemBatch",
            description = "Apaga as Viagens com os IDs indicados da BD")
    @Parameters(value = {
            @Parameter(name = "ids", description = "IDs das Viagens a apagar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public void deleteViagemBatch(@RequestParam List<Integer> ids) {
        viagemService.deleteViagemBatch(ids);
    }

}
