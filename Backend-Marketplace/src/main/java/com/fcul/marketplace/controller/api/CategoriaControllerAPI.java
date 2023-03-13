package com.fcul.marketplace.controller.api;

import com.fcul.marketplace.dto.CategoriaDTO;
import com.fcul.marketplace.dto.CategoriaPropriedadesDTO;
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


    @GetMapping()
    public List<CategoriaPropriedadesDTO> getCategorias() {

        List<Categoria> categorias = categoriaService.getCategorias();
        List<CategoriaPropriedadesDTO> categoriaPropriedadesDTOS = categorias.stream()
                .map(categoria -> modelMapper.map(categoria,CategoriaPropriedadesDTO.class)).collect(Collectors.toList());

        return categoriaPropriedadesDTOS;
    }

    @GetMapping("/propriedade")
    public List<PropriedadeDTO> getPropriedades() {

        List<Propriedade> propriedades = categoriaService.getPropriedades();
        List<PropriedadeDTO> propriedadeDTOS = propriedades.stream()
                .map(propriedade -> modelMapper.map(propriedade,PropriedadeDTO.class)).collect(Collectors.toList());

        return propriedadeDTOS;
    }


    @PostMapping()
    public CategoriaDTO insertCategoria(@RequestBody CategoriaDTO categoriaDTO) {

        Categoria categoria = modelMapper.map(categoriaDTO, Categoria.class);

        return modelMapper.map(categoriaService.addCategoria(categoria), CategoriaDTO.class);
    }

    @PostMapping("/{categoriaId}/propriedade")
    public CategoriaPropriedadesDTO insertCategoriaProperty(@PathVariable Integer categoriaId, @RequestBody PropriedadeDTO propriedadeDTO) {

        Propriedade propriedade = modelMapper.map(propriedadeDTO, Propriedade.class);

        return modelMapper.map(categoriaService.addPropriedade(categoriaId,propriedade), CategoriaPropriedadesDTO.class);
    }

    @PutMapping("/{id}")
    public CategoriaDTO updateCategoria(@PathVariable Integer id,@RequestBody CategoriaDTO categoriaDTO){
        Categoria categoria = categoriaService.updateCategoria(id,modelMapper.map(categoriaDTO,Categoria.class));
        return modelMapper.map(categoria,CategoriaDTO.class);
    }

    @PutMapping("/propriedade/{id}")
    public PropriedadeDTO updatePropriedade(@PathVariable Integer id,@RequestBody PropriedadeDTO propriedadeDTO){
        Propriedade propriedade = categoriaService.updatePropriedade(id,modelMapper.map(propriedadeDTO,Propriedade.class));
        return modelMapper.map(propriedade,PropriedadeDTO.class);
    }

    @PutMapping("/{categoriaId}/propriedade/{propriedadeId}")
    @Operation(summary = "addExistingPropriedadeToCategoria",description = "Associacao de uma propriedade existente a uma categoria existente")
    @Parameters(value={
            @Parameter(name = "categoriaId", description = "Id da categoria a associar a propriedade"),
            @Parameter(name = "propriedadeId", description = "Id da propriedade a associar a categoria")
    })
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "All went well")
    })
    public CategoriaPropriedadesDTO addExistingPropriedadeToCategoria(@PathVariable Integer categoriaId,@PathVariable Integer propriedadeId){
        return modelMapper.map(categoriaService.addExistingPropriedadeToCategoria(categoriaId,propriedadeId),CategoriaPropriedadesDTO.class);
    }



    @DeleteMapping("/propriedade/{id}")
    public void deletePropriedade(@PathVariable Integer id){
        categoriaService.deletePropriedade(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCategoria(@PathVariable Integer id){
        categoriaService.deleteCategoria(id);
    }

    //TODO SubCategoriaDTO
    @GetMapping("/subcategoria")
    public List<SubCategoria> getSubCategorias() {
        return categoriaService.getSubcategorias();
    }

    @PostMapping("/subcategoria")
    public SubCategoria insertSubCategoria(@RequestBody SubCategoria subCategoriaDTO) {
        return categoriaService.addSubCategoria(subCategoriaDTO);
    }

    @PutMapping("/subcategoria/{id}")
    public SubCategoria updateSubCategoria(@PathVariable Integer id,@RequestBody SubCategoria subCategoriaDTO) {
        return categoriaService.updateSubcategoria(id,subCategoriaDTO);
    }

    @DeleteMapping("/subcategoria/{id}")
    public void deleteSubCategoria(@PathVariable Integer id){
        categoriaService.deleteSubCategoria(id);
    }







}
