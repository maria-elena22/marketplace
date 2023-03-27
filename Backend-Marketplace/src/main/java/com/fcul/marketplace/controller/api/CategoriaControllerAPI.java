package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.dto.CategoriaDTO;
import com.fcul.marketplace.dto.FullCategoriaDTO;
import com.fcul.marketplace.dto.PropriedadeDTO;
import com.fcul.marketplace.dto.SubCategoriaDTO;
import com.fcul.marketplace.model.Categoria;
import com.fcul.marketplace.model.Propriedade;
import com.fcul.marketplace.model.SubCategoria;
import com.fcul.marketplace.service.CategoriaService;
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
@RequestMapping("/api/categoria")
public class CategoriaControllerAPI {

    @Autowired
    CategoriaService categoriaService;

    @Autowired
    ModelMapper modelMapper;

    //============================GET=============================

    @GetMapping()
    @Operation(summary = "getCategorias",
            description = "Devolve todas as Categorias podendo os resultados conter nomeCategoria")
    @Parameters(value = {
            @Parameter(name = "nomeCategoria", description = "Filtrar resultados por nomes que contem o parametro"),
            @Parameter(name = "page", description = "A pagina requerida"),
            @Parameter(name = "size", description = "A dimensao das paginas"),
            @Parameter(name = "sortKey", description = "Chave do ordenamento"),
            @Parameter(name = "sortDir", description = "Direcao do ordenamento")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public List<FullCategoriaDTO> getCategorias(@RequestParam(required = false) String nomeCategoria,
                                                @RequestParam(required = false) Integer page,
                                                @RequestParam(required = false) Integer size,
                                                @RequestParam(required = false) String sortKey,
                                                @RequestParam(required = false) Sort.Direction sortDir) {

        List<Categoria> categorias = categoriaService.getCategorias(nomeCategoria, page, size, sortKey, sortDir);
        List<FullCategoriaDTO> categoriaPropriedadesDTOS = categorias.stream()
                .map(categoria -> modelMapper.map(categoria, FullCategoriaDTO.class)).collect(Collectors.toList());

        return categoriaPropriedadesDTOS;
    }

    @GetMapping("/{idCategoria}")
    @Operation(summary = "getCategoria",
            description = "Devolve a categoria com o ID indicado com as propriedades e subcategorias da mesma")
    @Parameters(value = {
            @Parameter(name = "idCategoria", description = "ID da Categoria")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public FullCategoriaDTO getCategoria(@PathVariable Integer idCategoria) {

        return modelMapper.map(categoriaService.getCategoriaByID(idCategoria), FullCategoriaDTO.class);
    }

    @GetMapping("/propriedade")
    @Operation(summary = "getPropriedades",
            description = "Devolve todas as Propriedades podendo os resultados conter nomePropriedade")
    @Parameters(value = {
            @Parameter(name = "nomePropriedade", description = "Filtrar resultados por nomes que contem o parametro"),
            @Parameter(name = "page", description = "A pagina requerida"),
            @Parameter(name = "size", description = "A dimensao das paginas"),
            @Parameter(name = "sortKey", description = "Chave do ordenamento"),
            @Parameter(name = "sortDir", description = "Direcao do ordenamento")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public List<PropriedadeDTO> getPropriedades(@RequestParam(required = false) String nomePropriedade,
                                                @RequestParam(required = false) Integer page,
                                                @RequestParam(required = false) Integer size,
                                                @RequestParam(required = false) String sortKey,
                                                @RequestParam(required = false) Sort.Direction sortDir) {

        List<Propriedade> propriedades = categoriaService.getPropriedades();
        List<PropriedadeDTO> propriedadeDTOS = propriedades.stream()
                .map(propriedade -> modelMapper.map(propriedade, PropriedadeDTO.class)).collect(Collectors.toList());

        return propriedadeDTOS;
    }

    @GetMapping("/{idCategoria}/subcategoria")
    @Operation(summary = "getSubCategorias",
            description = "Devolve todas as Subcategorias da Categoria com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "idCategoria", description = "ID da Categoria")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public List<SubCategoriaDTO> getSubCategorias(@PathVariable Integer idCategoria) {
        List<SubCategoria> subCategorias = categoriaService.getSubcategorias(idCategoria);
        List<SubCategoriaDTO> subCategoriaDTOS = subCategorias.stream()
                .map(subCategoria -> modelMapper.map(subCategoria, SubCategoriaDTO.class)).collect(Collectors.toList());
        return subCategoriaDTOS;
    }

    //===========================INSERT===========================

    @PostMapping()
    @Operation(summary = "insertCategoria",
            description = "Adiciona uma nova Categoria à BD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public CategoriaDTO insertCategoria(@RequestBody CategoriaDTO categoriaDTO) {

        Categoria categoria = modelMapper.map(categoriaDTO, Categoria.class);

        return modelMapper.map(categoriaService.addCategoria(categoria), CategoriaDTO.class);
    }

    @PostMapping("/{categoriaId}/propriedade")
    @Operation(summary = "insertPropriedade",
            description = "Adiciona uma nova Propriedade à BD, associada à Categoria com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "categoriaId", description = "ID da Categoria a associar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public FullCategoriaDTO insertPropriedade(@PathVariable Integer categoriaId, @RequestBody PropriedadeDTO propriedadeDTO) {

        Propriedade propriedade = modelMapper.map(propriedadeDTO, Propriedade.class);

        return modelMapper.map(categoriaService.addPropriedade(categoriaId, propriedade), FullCategoriaDTO.class);
    }

    @PostMapping("/{categoriaId}/subcategoria")
    @Operation(summary = "insertSubCategoria",
            description = "Adiciona uma nova Subcategoria à BD, associada à Categoria com o ID indicado")
    @Parameters(value = {
            @Parameter(name = "categoriaId", description = "ID da Categoria a associar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public SubCategoriaDTO insertSubCategoria(@PathVariable Integer categoriaId, @RequestBody SubCategoriaDTO subCategoriaDTO) {
        return modelMapper.map(categoriaService.addSubCategoria(modelMapper.map(subCategoriaDTO, SubCategoria.class), categoriaId), SubCategoriaDTO.class);
    }

    //===========================UPDATE===========================


    @PutMapping("/{categoriaId}")
    @Operation(summary = "updateCategoria",
            description = "Atualiza os dados de uma Categoria")
    @Parameters(value = {
            @Parameter(name = "categoriaId", description = "ID da Categoria a atualizar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public CategoriaDTO updateCategoria(@PathVariable Integer categoriaId, @RequestBody CategoriaDTO categoriaDTO) {
        Categoria categoria = categoriaService.updateCategoria(categoriaId, modelMapper.map(categoriaDTO, Categoria.class));
        return modelMapper.map(categoria, CategoriaDTO.class);
    }

    @PutMapping("/propriedade/{propriedadeId}")
    @Operation(summary = "updatePropriedade",
            description = "Atualiza os dados de uma Propriedade")
    @Parameters(value = {
            @Parameter(name = "propriedadeId", description = "ID da Propriedade a atualizar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public PropriedadeDTO updatePropriedade(@PathVariable Integer propriedadeId, @RequestBody PropriedadeDTO propriedadeDTO) {
        Propriedade propriedade = categoriaService.updatePropriedade(propriedadeId, modelMapper.map(propriedadeDTO, Propriedade.class));
        return modelMapper.map(propriedade, PropriedadeDTO.class);
    }

    @PutMapping("/{categoriaId}/propriedade/add/{propriedadeId}")
    @Operation(summary = "addPropriedadeExistenteACategoria",
            description = "Associação de uma propriedade existente a uma categoria existente")
    @Parameters(value = {
            @Parameter(name = "categoriaId", description = "Id da categoria a associar a propriedade"),
            @Parameter(name = "propriedadeId", description = "Id da propriedade a associar a categoria")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public FullCategoriaDTO addPropriedadeExistenteACategoria(@PathVariable Integer categoriaId, @PathVariable Integer propriedadeId) {
        return modelMapper.map(categoriaService.addExistingPropriedadeToCategoria(categoriaId, propriedadeId), FullCategoriaDTO.class);
    }

    @PutMapping("/{categoriaId}/propriedade/remove/{propriedadeId}")
    @Operation(summary = "removePropriedadeExistenteDeCategoria",
            description = "Desassociação de uma propriedade existente numa categoria existente")
    @Parameters(value = {
            @Parameter(name = "categoriaId", description = "Id da categoria a desassociar da propriedade"),
            @Parameter(name = "propriedadeId", description = "Id da propriedade a desassociar da categoria")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public FullCategoriaDTO removePropriedadeExistenteDeCategoria(@PathVariable Integer categoriaId, @PathVariable Integer propriedadeId) {
        return modelMapper.map(categoriaService.removeExistingPropriedadeFromCategoria(categoriaId, propriedadeId), FullCategoriaDTO.class);
    }


    @PutMapping("/subcategoria/{subcategoriaId}")
    @Operation(summary = "updateSubCategoria",
            description = "Atualiza os dados de uma SubCategoria")
    @Parameters(value = {
            @Parameter(name = "subcategoriaId", description = "ID da SubCategoria a atualizar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public SubCategoriaDTO updateSubCategoria(@PathVariable Integer subcategoriaId, @RequestBody SubCategoriaDTO subCategoriaDTO) {
        SubCategoria subCategoria = modelMapper.map(subCategoriaDTO, SubCategoria.class);
        return modelMapper.map(categoriaService.updateSubcategoria(subcategoriaId, subCategoria), SubCategoriaDTO.class);
    }

    //===========================DELETE===========================


    @DeleteMapping("/propriedade/{propriedadeId}")
    @Operation(summary = "deletePropriedade",
            description = "Apaga a Propriedade com o ID indicado da BD")
    @Parameters(value = {
            @Parameter(name = "propriedadeId", description = "ID da Propriedade a apagar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public void deletePropriedade(@PathVariable Integer propriedadeId) {
        categoriaService.deletePropriedade(propriedadeId);
    }

    @DeleteMapping("/{categoriaId}")
    @Operation(summary = "deleteCategoria",
            description = "Apaga a Categoria com o ID indicado da BD")
    @Parameters(value = {
            @Parameter(name = "categoriaId", description = "ID da Categoria a apagar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public void deleteCategoria(@PathVariable Integer categoriaId) {
        categoriaService.deleteCategoria(categoriaId);
    }


    @DeleteMapping("/subcategoria/{subcategoriaId}")
    @Operation(summary = "deleteSubCategoria",
            description = "Apaga a SubCategoria com o ID indicado da BD")
    @Parameters(value = {
            @Parameter(name = "subcategoriaId", description = "ID da SubCategoria a apagar")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    })
    public void deleteSubCategoria(@PathVariable Integer subcategoriaId) {
        categoriaService.deleteSubCategoria(subcategoriaId);
    }


}
