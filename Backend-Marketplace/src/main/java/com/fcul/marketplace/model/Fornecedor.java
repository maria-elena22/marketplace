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
@DiscriminatorValue("Fornecedor")
public class Fornecedor extends Utilizador {

    @OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UniProd> unidadesProducao;

    public Fornecedor() {
        super();
    }

    public Fornecedor(Integer idFiscal, String nome, Integer telemovel, Coordinate coordenadas,
                      String morada, String freguesia, String municipio, String distrito,
                      Locale.IsoCountryCode pais, Continente continente) {
        super(null, idFiscal, nome, telemovel, coordenadas, morada, freguesia, municipio, distrito, pais, continente);
    }
}
