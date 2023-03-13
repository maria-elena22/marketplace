package com.fcul.marketplace.repository;

import com.fcul.marketplace.model.Categoria;
import com.fcul.marketplace.model.Produto;
import com.fcul.marketplace.model.SubCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoriaRepository extends JpaRepository<SubCategoria,Integer> {

}
