package com.fcul.marketplace.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class Fornecedor extends Utilizador{

    @OneToMany
    private List<Transporte> transportes;

    @ManyToMany
    private List<UnidadeProducao> unidadesProducao;

    public Fornecedor(){
        super();
    }

    public Fornecedor(Integer idFiscal, String nome, Integer telemovel, String coordenadas,
                      String morada, String freguesia, String municipio, String distrito,
                      String pais, String continente){
        super(null,idFiscal,nome,telemovel,coordenadas,morada,freguesia,municipio,distrito,pais,continente);
    }
}
