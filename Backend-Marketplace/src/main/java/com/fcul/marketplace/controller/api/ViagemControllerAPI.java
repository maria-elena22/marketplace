package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.config.security.SecurityUtils;
import com.fcul.marketplace.dto.ViagemDTO;
import com.fcul.marketplace.dto.ViagemInputDTO;
import com.fcul.marketplace.exceptions.ForbiddenActionException;
import com.fcul.marketplace.exceptions.JWTTokenMissingException;
import com.fcul.marketplace.model.Viagem;
import com.fcul.marketplace.service.ViagemService;
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
@RequestMapping("/api/viagem")
public class ViagemControllerAPI {

    @Autowired
    ViagemService viagemService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SecurityUtils securityUtils;

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
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    public List<ViagemDTO> getViagensTransporte(@PathVariable Integer idTransporte,
                                                @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                                @RequestParam(required = false) Integer page,
                                                @RequestParam(required = false) Integer size,
                                                @RequestParam(required = false) String sortKey,
                                                @RequestParam(required = false) Sort.Direction sortDir) throws JWTTokenMissingException, ForbiddenActionException {

        List<Viagem> viagens = viagemService.getViagensByTransporte(securityUtils.getEmailFromAuthHeader(authorizationHeader), idTransporte, page, size, sortKey, sortDir);
        return viagens.stream()
                .map(viagem -> modelMapper.map(viagem, ViagemDTO.class)).collect(Collectors.toList());
    }

    //===========================INSERT===========================

    @PostMapping()
    @Operation(summary = "insertViagem",
            description = "Adiciona uma nova Viagem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    public ViagemDTO insertViagem(@RequestBody ViagemInputDTO viagemDTO,
                                  @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader) throws JWTTokenMissingException, ForbiddenActionException {
        Viagem viagem = modelMapper.map(viagemDTO, Viagem.class);
        return modelMapper.map(viagemService.addViagem(securityUtils.getEmailFromAuthHeader(authorizationHeader), viagem), ViagemDTO.class);
    }

}
