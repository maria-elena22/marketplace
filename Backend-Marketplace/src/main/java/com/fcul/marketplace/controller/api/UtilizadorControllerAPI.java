package com.fcul.marketplace.controller.api;

import com.auth0.exception.Auth0Exception;
import com.fcul.marketplace.config.security.SecurityUtils;
import com.fcul.marketplace.dto.utilizador.SignUpDTO;
import com.fcul.marketplace.dto.utilizador.UtilizadorDTO;
import com.fcul.marketplace.dto.utilizador.UtilizadorInputDTO;
import com.fcul.marketplace.exceptions.BadCredentialsException;
import com.fcul.marketplace.exceptions.JWTTokenMissingException;
import com.fcul.marketplace.exceptions.SignUpException;
import com.fcul.marketplace.model.Consumidor;
import com.fcul.marketplace.model.Fornecedor;
import com.fcul.marketplace.service.UtilizadorService;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/utilizador")
public class UtilizadorControllerAPI {

    @Autowired
    UtilizadorService utilizadorService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SecurityUtils securityUtils;

    //============================GET=============================

    @Operation(summary = "getLogin",
            description = "Faz o login do utilizador devolvendo o seu token")
    @Parameters(value = {
            @Parameter(name = "email", description = "Email do utilizador"),
            @Parameter(name = "password", description = "Password do utilizador")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @GetMapping("login")
    public String login(@RequestParam String email, @RequestParam String password) throws BadCredentialsException {

        String token = null;
        try {
            token = securityUtils.generateToken(email, password);
        } catch (Auth0Exception e) {
            throw new BadCredentialsException("Wrong email or password");
        }

        return token;
    }

    @GetMapping("/consumidor")
    @Operation(summary = "getConsumidores",
            description = "Devolve todos os Consumidores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"ADMIN"})
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
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"ADMIN"})
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
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"ADMIN"})
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
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"ADMIN"})
    public UtilizadorDTO getFornecedorByID(@PathVariable Integer idFornecedor) {
        return modelMapper.map(utilizadorService.getFornecedorByID(idFornecedor), UtilizadorDTO.class);
    }

    //===========================INSERT===========================

    @PostMapping("/register/consumidor")
    @Operation(summary = "insertConsumidor",
            description = "Adiciona um novo Consumidor à BD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public String insertConsumidor(@RequestBody SignUpDTO signupDTO) throws Auth0Exception, SignUpException {

        securityUtils.registerUser(signupDTO.getEmail(), signupDTO.getPassword(), "ROLE_CONSUMIDOR");
        String token = securityUtils.generateToken(signupDTO.getEmail(), signupDTO.getPassword());

        Consumidor consumidor = modelMapper.map(signupDTO, Consumidor.class);
        consumidor = utilizadorService.addConsumidor(consumidor);
        return token;
    }

    @PostMapping("/register/fornecedor")
    @Operation(summary = "insertFornecedor",
            description = "Adiciona um novo Fornecedor à BD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public String insertFornecedor(@RequestBody SignUpDTO signupDTO) throws Auth0Exception, SignUpException {

        securityUtils.registerUser(signupDTO.getEmail(), signupDTO.getPassword(), "ROLE_FORNECEDOR");
        String token = securityUtils.generateToken(signupDTO.getEmail(), signupDTO.getPassword());

        Fornecedor fornecedor = modelMapper.map(signupDTO, Fornecedor.class);
        fornecedor = utilizadorService.addFornecedor(fornecedor);
        return token;
    }

    //===========================UPDATE===========================

    @PutMapping("/consumidor")
    @Operation(summary = "updateConsumidor",
            description = "Atualiza os dados de um Consumidor")
    @Parameters(value = {
            @Parameter(name = "idConsumidor", description = "ID do Consumidor a atualizar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"CONSUMIDOR"})
    public UtilizadorDTO updateConsumidor(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                          @RequestBody UtilizadorInputDTO utilizadorDTO) throws JWTTokenMissingException{
        Consumidor consumidor = modelMapper.map(utilizadorDTO, Consumidor.class);
        consumidor = utilizadorService.updateConsumidor(securityUtils.getEmailFromAuthHeader(authorizationHeader), consumidor);
        return modelMapper.map(consumidor, UtilizadorDTO.class);
    }

    @PutMapping("/fornecedor")
    @Operation(summary = "updateFornecedor",
            description = "Atualiza os dados de um Fornecedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    public UtilizadorDTO updateFornecedor(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                          @RequestBody UtilizadorInputDTO utilizadorDTO) throws JWTTokenMissingException{
        Fornecedor fornecedor = modelMapper.map(utilizadorDTO, Fornecedor.class);
        fornecedor = utilizadorService.updateFornecedor(securityUtils.getEmailFromAuthHeader(authorizationHeader), fornecedor);
        return modelMapper.map(fornecedor, UtilizadorDTO.class);
    }

    @PutMapping("/consumidor/deactivate/{idConsumidor}")
    @Operation(summary = "deactivateConsumidor",
            description = "Desativa o Consumidor com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "idConsumidor", description = "ID do Consumidor a desativar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"ADMIN"})
    public void deactivateConsumidor(@PathVariable Integer idConsumidor) {
        utilizadorService.deactivateConsumidor(idConsumidor);
    }

    @PutMapping("/fornecedor/deactivate/{idFornecedor}")
    @Operation(summary = "deactivateFornecedor",
            description = "Desativa o Fornecedor com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "idFornecedor", description = "ID do Fornecedor a desativar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"ADMIN"})
    public void deactivateFornecedor(@PathVariable Integer idFornecedor) {
        utilizadorService.deactivateFornecedor(idFornecedor);
    }
    @PutMapping("/consumidor/activate/{idConsumidor}")
    @Operation(summary = "activateConsumidor",
            description = "Ativa o Consumidor com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "idConsumidor", description = "ID do Consumidor a ativar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"ADMIN"})
    public void activateConsumidor(@PathVariable Integer idConsumidor) {
        utilizadorService.activateConsumidor(idConsumidor);
    }

    @PutMapping("/fornecedor/activate/{idFornecedor}")
    @Operation(summary = "activateFornecedor",
            description = "Ativa o Fornecedor com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "idFornecedor", description = "ID do Fornecedor a ativar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"ADMIN"})
    public void activateFornecedor(@PathVariable Integer idFornecedor) {
        utilizadorService.activateFornecedor(idFornecedor);
    }


    //===========================DELETE===========================

    @DeleteMapping("/consumidor/{idConsumidor}")
    @Operation(summary = "deleteConsumidor",
            description = "Apaga o Consumidor com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "idConsumidor", description = "ID do Consumidor a apagar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"CONSUMIDOR"})
    public void deleteConsumidor(@PathVariable Integer idConsumidor) {
        utilizadorService.deleteConsumidor(idConsumidor);
    }

    @DeleteMapping("/fornecedor/{idFornecedor}")
    @Operation(summary = "deleteFornecedor",
            description = "Apaga o Fornecedor com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "idFornecedor", description = "ID do Fornecedor a apagar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    public void deleteFornecedor(@PathVariable Integer idFornecedor) {
        utilizadorService.deleteFornecedor(idFornecedor);
    }
    

}
