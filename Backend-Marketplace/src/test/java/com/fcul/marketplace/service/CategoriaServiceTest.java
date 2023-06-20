package com.fcul.marketplace.service;

import com.fcul.marketplace.exceptions.ForbiddenActionException;
import com.fcul.marketplace.model.*;
import com.fcul.marketplace.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    CategoriaRepository categoriaRepository;

    @Mock
    SubCategoriaRepository subCategoriaRepository;

    @Mock
    PropriedadeRepository propriedadeRepository;

    @InjectMocks
    CategoriaService categoriaService;

    @Test
    public void testGetCategoriaByID() {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(1);

        when(categoriaRepository.findById(anyInt())).thenReturn(Optional.of(categoria));
        assertEquals(categoria,categoriaService.getCategoriaByID(1));
    }


    @Test
    public void testGetSubcategorias(){
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(1);

        List<SubCategoria> subCategorias = new ArrayList<>();
        SubCategoria subCategoria = new SubCategoria();
        subCategoria.setCategoria(categoria);
        subCategorias.add(subCategoria);

        when(subCategoriaRepository.findByCategoriaIdCategoria(anyInt())).thenReturn(subCategorias);

        assertEquals(subCategorias,categoriaService.getSubcategorias(1));
    }

    @Test
    public void testGetSubCategoriaByID() {
        SubCategoria subCategoria = new SubCategoria();
        subCategoria.setIdSubCategoria(1);

        when(subCategoriaRepository.findById(anyInt())).thenReturn(Optional.of(subCategoria));
        assertEquals(subCategoria,categoriaService.getSubCategoriaByID(1));
    }

    @Test
    public void testGetPropriedades(){

        List<Propriedade> propriedades = new ArrayList<>();
        Propriedade propriedade = new Propriedade();
        propriedades.add(propriedade);

        when(propriedadeRepository.findAll()).thenReturn(propriedades);

        assertEquals(propriedades,categoriaService.getPropriedades());
    }

    @Test
    public void testGetPropriedadeByID() {
        Propriedade propriedade = new Propriedade();
        propriedade.setIdPropriedade(1);

        when(propriedadeRepository.findById(anyInt())).thenReturn(Optional.of(propriedade));
        assertEquals(propriedade,categoriaService.getPropriedadeByID(1));
    }

}
