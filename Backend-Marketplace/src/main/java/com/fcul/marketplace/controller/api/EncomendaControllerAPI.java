package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.config.security.SecurityUtils;
import com.fcul.marketplace.dto.PaymentConfirmationRequest;
import com.fcul.marketplace.dto.encomenda.*;
import com.fcul.marketplace.dto.item.ItemInfoDTO;
import com.fcul.marketplace.dto.item.SubItemDTO;
import com.fcul.marketplace.exceptions.*;
import com.fcul.marketplace.model.Encomenda;
import com.fcul.marketplace.model.Item;
import com.fcul.marketplace.model.SubEncomenda;
import com.fcul.marketplace.model.SubItem;
import com.fcul.marketplace.model.enums.EstadoEncomenda;
import com.fcul.marketplace.service.EncomendaService;
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
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/encomenda")
public class EncomendaControllerAPI {

    @Autowired
    EncomendaService encomendaService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SecurityUtils securityUtils;

    //============================GET=============================

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
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"CONSUMIDOR"})
    @CrossOrigin("*")
    public List<FullEncomendaDTO> getEncomendas(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                                @RequestParam(required = false) Double precoMin,
                                                @RequestParam(required = false) Double precoMax,
                                                @RequestParam(required = false) Date dataMin,
                                                @RequestParam(required = false) Date dataMax,
                                                @RequestParam(required = false) EstadoEncomenda estadoEncomenda,
                                                @RequestParam(required = false) Integer page,
                                                @RequestParam(required = false) Integer size,
                                                @RequestParam(required = false) String sortKey,
                                                @RequestParam(required = false) Sort.Direction sortDir) throws JWTTokenMissingException {


        List<Encomenda> encomendas = encomendaService.getEncomendas(securityUtils.getEmailFromAuthHeader(authorizationHeader),
                precoMin, precoMax, dataMin, dataMax, estadoEncomenda, page, size, sortKey, sortDir);
        return encomendas.stream()
                .map(encomenda -> modelMapper.map(encomenda, FullEncomendaDTO.class)).collect(Collectors.toList());
    }


    @GetMapping("/subEncomendas")
    @Operation(summary = "getSubEncomendas",
            description = "Devolve todas as Subencomendas, podendo os resultados serem filtrados por preço, data da encomenda, " +
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
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    @CrossOrigin("*")
    public List<FullSubEncomendaDTO> getSubEncomendas(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                                      @RequestParam(required = false) Double precoMin,
                                                      @RequestParam(required = false) Double precoMax,
                                                      @RequestParam(required = false) Date dataMin,
                                                      @RequestParam(required = false) Date dataMax,
                                                      @RequestParam(required = false) EstadoEncomenda estadoEncomenda,
                                                      @RequestParam(required = false) Integer page,
                                                      @RequestParam(required = false) Integer size,
                                                      @RequestParam(required = false) String sortKey,
                                                      @RequestParam(required = false) Sort.Direction sortDir) throws JWTTokenMissingException {


        List<SubEncomenda> subEncomendas = encomendaService.getSubEncomendas(securityUtils.getEmailFromAuthHeader(authorizationHeader),
                precoMin, precoMax, dataMin, dataMax, estadoEncomenda, page, size, sortKey, sortDir);
        return subEncomendas.stream()
                .map(subEncomenda -> modelMapper.map(subEncomenda, FullSubEncomendaDTO.class)).collect(Collectors.toList());
    }

    @GetMapping("/item")
    @Operation(summary = "getItensNaoEntregues",
            description = "Devolve todos os Itens não entregues")
    @Parameters(value = {
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
    public List<ItemInfoDTO> getItensNaoEntregues(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                                  @RequestParam Integer idTransporte,
                                                  @RequestParam(required = false) Integer page,
                                                  @RequestParam(required = false) Integer size,
                                                  @RequestParam(required = false) String sortKey,
                                                  @RequestParam(required = false) Sort.Direction sortDir) throws JWTTokenMissingException, ForbiddenActionException {

        String emailFornecedor = securityUtils.getEmailFromAuthHeader(authorizationHeader);
        Map<Item, List<Integer>> items = encomendaService.getItensNaoEntregues(emailFornecedor, idTransporte, page, size, sortKey, sortDir);
        List<ItemInfoDTO> itemInfoDTOS = new ArrayList<>();
        for (Map.Entry<Item, List<Integer>> entry : items.entrySet()) {
            Item item = entry.getKey();
            ItemInfoDTO itemInfoDTO = new ItemInfoDTO();
            itemInfoDTO.setIdItem(item.getIdItem());
            itemInfoDTO.setQuantidade(item.getQuantidade());
            itemInfoDTO.setProdutoNome(item.getProduto().getNome());
            itemInfoDTO.setQuantidadeDespachada(entry.getValue().get(0));
            itemInfoDTO.setQuantidadeStock(entry.getValue().get(1));
            itemInfoDTOS.add(itemInfoDTO);
        }
        return itemInfoDTOS;
    }


    @GetMapping("/subItem")
    @Operation(summary = "getSubItensNaoEntregues",
            description = "Devolve todos os SubItens não entregues")
    @Parameters(value = {
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
    public List<SubItemDTO> getSubItensNaoEntregues(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                                    @RequestParam(required = false) Integer page,
                                                    @RequestParam(required = false) Integer size,
                                                    @RequestParam(required = false) String sortKey,
                                                    @RequestParam(required = false) Sort.Direction sortDir) throws JWTTokenMissingException, ForbiddenActionException {

        String emailFornecedor = securityUtils.getEmailFromAuthHeader(authorizationHeader);
        List<SubItem> subItems = encomendaService.getSubItensNaoEntregues(emailFornecedor, page, size, sortKey, sortDir);
        return subItems.stream().map(subItem -> modelMapper.map(subItem, SubItemDTO.class)).collect(Collectors.toList());
    }


    @GetMapping("/encomenda/{encomendaId}")
    @Operation(summary = "getEncomendaById",
            description = "Devolve a encomenda com o id indicado")
    @Parameters(value = {@Parameter(name = "encomendaId", description = "ID da encomenda")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"CONSUMIDOR"})
    @CrossOrigin("*")
    public FullEncomendaDTO getEncomendaById(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                             @PathVariable Integer encomendaId) throws JWTTokenMissingException, ForbiddenActionException {

        String emailConsumidor = securityUtils.getEmailFromAuthHeader(authorizationHeader);
        Encomenda encomenda = encomendaService.getEncomendaById(emailConsumidor, encomendaId);
        return modelMapper.map(encomenda, FullEncomendaDTO.class);
    }

    @GetMapping("/subEncomenda/{subEncomendaId}")
    @Operation(summary = "getSubEncomendaById",
            description = "Devolve a Sub Encomenda com o id indicado")
    @Parameters(value = {@Parameter(name = "subEncomendaId", description = "ID da Sub encomenda")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"FORNECEDOR"})
    @CrossOrigin("*")
    public FullSubEncomendaDTO getSubEncomendaById(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                                   @PathVariable Integer subEncomendaId) throws JWTTokenMissingException, ForbiddenActionException {

        String emailFornecedor = securityUtils.getEmailFromAuthHeader(authorizationHeader);
        SubEncomenda subEncomenda = encomendaService.getSubEncomendaById(emailFornecedor, subEncomendaId);
        return modelMapper.map(subEncomenda, FullSubEncomendaDTO.class);
    }


    //===========================INSERT===========================

    @PostMapping("/confirm/{encomendaId}")
    @Operation(summary = "confirmPayment",
            description = "Confirma o pagamento da encomenda com o ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"CONSUMIDOR"})
    @CrossOrigin("*")
    public FullEncomendaDTO confirmPayment(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                           @PathVariable Integer encomendaId,
                                           @RequestBody PaymentConfirmationRequest request) throws JWTTokenMissingException, ForbiddenActionException, PaymentFailedException {

        Encomenda encomenda = encomendaService.confirmPayment(securityUtils.getEmailFromAuthHeader(authorizationHeader), encomendaId, request.getClientSecret());
        //Fazer DTO
        return modelMapper.map(encomenda, FullEncomendaDTO.class);
    }

    @PostMapping("/{idConsumidor}")
    @Operation(summary = "insertEncomenda",
            description = "Adiciona uma nova Encomenda à BD, associada ao Consumidor com o ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"CONSUMIDOR"})
    @CrossOrigin("*")
    public EncomendaPaymentDTO insertEncomenda(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
                                               @RequestBody CompraDTO compraDTO) throws JWTTokenMissingException, ErroCalculoDoPrecoEnviadoException, ForbiddenActionException {


        Encomenda encomenda = new Encomenda();
        encomenda.setPreco(compraDTO.getPreco());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        encomenda.setDataEncomenda(timestamp);
        return encomendaService.addEncomenda(encomenda, securityUtils.getEmailFromAuthHeader(authorizationHeader), compraDTO.getItems());
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
    @SecurityRequirement(name = "Bearer Authentication")
    @RolesAllowed({"CONSUMIDOR"})
    @CrossOrigin("*")
    public EncomendaDTO cancelEncomenda(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader, @PathVariable Integer idEncomenda) throws EncomendaAlreadyCancelledException, EncomendaCannotBeCancelledException, JWTTokenMissingException, ForbiddenActionException {

        return modelMapper.map(encomendaService.cancelEncomenda(securityUtils.getEmailFromAuthHeader(authorizationHeader), idEncomenda), EncomendaDTO.class);
    }

}
