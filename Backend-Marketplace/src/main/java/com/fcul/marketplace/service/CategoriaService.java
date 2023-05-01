package com.fcul.marketplace.service;

import com.fcul.marketplace.model.Categoria;
import com.fcul.marketplace.model.Propriedade;
import com.fcul.marketplace.model.SubCategoria;
import com.fcul.marketplace.repository.CategoriaRepository;
import com.fcul.marketplace.repository.PropriedadeRepository;
import com.fcul.marketplace.repository.SubCategoriaRepository;
import com.fcul.marketplace.repository.utils.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    SubCategoriaRepository subCategoriaRepository;

    @Autowired
    PropriedadeRepository propriedadeRepository;

    //============================GET=============================

    public List<Categoria> getCategorias(String nomeCategoria, Integer page
            , Integer size, String sortKey, Sort.Direction sortDir) {
        Pageable pageable = PageableUtils.getDefaultPageable(page, size, sortDir, sortKey);

        return categoriaRepository.findByOpt(nomeCategoria,pageable).getContent().stream().peek(categoria -> categoria.setSubCategorias(categoria.getSubCategorias().stream().filter(subCategoria -> subCategoria.getSubCategoriaPai() == null).toList())).toList();

    }


    public Categoria getCategoriaByID(Integer id) {
        return categoriaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<SubCategoria> getSubcategorias(Integer idCategoria) {
        return subCategoriaRepository.findByCategoriaIdCategoria(idCategoria);
    }

    public List<SubCategoria> getSubcategoriasByCategoria(Integer categoriaId) {
        Categoria categoria = categoriaRepository.findById(categoriaId).orElseThrow(EntityNotFoundException::new);
        return categoria.getSubCategorias();
    }

    public SubCategoria getSubCategoriaByID(Integer id) {
        return subCategoriaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Propriedade> getPropriedades(String nomePropriedade, Integer page, Integer size, String sortKey, Sort.Direction sortDir) {

        Pageable pageable = PageableUtils.getDefaultPageable(page, size, sortDir, sortKey);

        return propriedadeRepository.findByOpt(nomePropriedade, pageable).getContent();
    }

    public List<Propriedade> getPropriedades() {
        return propriedadeRepository.findAll();
    }

    public Propriedade getPropriedadeByID(Integer id) {
        return propriedadeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Propriedade> getPropriedadesByCategoria(Integer categoriaId) {
        Categoria categoria = categoriaRepository.findById(categoriaId).orElseThrow(EntityNotFoundException::new);
        return categoria.getPropriedades();
    }

    //===========================INSERT===========================

    public Categoria addCategoria(Categoria categoria) {
        categoria.setSubCategorias(new ArrayList<>());
        return categoriaRepository.save(categoria);
    }

    public SubCategoria addSubCategoria(SubCategoria subCategoria, Integer subCategoriaPaiId, Integer idCategoria) {
        Categoria categoria = categoriaRepository.findById(idCategoria).orElseThrow(EntityNotFoundException::new);
        subCategoria.setCategoria(categoria);
        if (subCategoriaPaiId != null) {
            subCategoria.setSubCategoriaPai(getSubCategoriaByID(subCategoriaPaiId));
        }
        return subCategoriaRepository.save(subCategoria);

    }

    public Propriedade addPropriedade(Integer categoriaId, Propriedade propriedade) {
        Categoria categoria = categoriaRepository.findById(categoriaId).orElseThrow(EntityNotFoundException::new);
        propriedade.addCategoria(categoria);
        propriedade = propriedadeRepository.save(propriedade);
        return propriedade;
    }

    //===========================UPDATE===========================

    public Categoria updateCategoria(Integer id, Categoria categoria) {
        Categoria categoriaBD = categoriaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        categoriaBD.setNomeCategoria(categoria.getNomeCategoria());
        return categoriaRepository.save(categoriaBD);


    }

    public Propriedade updatePropriedade(Integer id, Propriedade propriedade) {
        Propriedade propriedadeBD = propriedadeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        propriedadeBD.setNomePropriedade(propriedade.getNomePropriedade());
        return propriedadeRepository.save(propriedadeBD);

    }

    public Categoria updateSubcategoria(Integer id, SubCategoria subCategoria) {
        SubCategoria subCategoriaBD = subCategoriaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        subCategoriaBD.setNomeSubCategoria(subCategoria.getNomeSubCategoria());
        subCategoriaBD = subCategoriaRepository.save(subCategoriaBD);
        return subCategoriaBD.getCategoria();
    }

    public Categoria addExistingPropriedadeToCategoria(Integer categoriaId, Integer propriedadeId) {
        Propriedade propriedade = propriedadeRepository.findById(propriedadeId).orElseThrow(EntityNotFoundException::new);
        Categoria categoria = categoriaRepository.findById(categoriaId).orElseThrow(EntityNotFoundException::new);
        categoria.getPropriedades().add(propriedade);
        return categoriaRepository.save(categoria);
    }

    public Categoria removeExistingPropriedadeFromCategoria(Integer categoriaId, Integer propriedadeId) {
        Propriedade propriedade = propriedadeRepository.findById(propriedadeId).orElseThrow(EntityNotFoundException::new);
        Categoria categoria = categoriaRepository.findById(categoriaId).orElseThrow(EntityNotFoundException::new);
        categoria.getPropriedades().remove(propriedade);
        return categoriaRepository.save(categoria);
    }

    //===========================DELETE===========================

    public void deleteCategoria(Integer id) {
        categoriaRepository.deleteById(id);
    }

    public void deleteCategoriaBatch(List<Integer> ids) {
        categoriaRepository.deleteAllByIdInBatch(ids);
    }

    public void deleteSubCategoria(Integer id) {
        subCategoriaRepository.deleteById(id);
    }

    public void deleteSubCategoriaBatch(List<Integer> ids) {
        subCategoriaRepository.deleteAllByIdInBatch(ids);
    }

    public void deletePropriedade(Integer id) {
        propriedadeRepository.deleteById(id);
    }

    public void deletePropriedadeBatch(List<Integer> ids) {
        propriedadeRepository.deleteAllByIdInBatch(ids);
    }


}
