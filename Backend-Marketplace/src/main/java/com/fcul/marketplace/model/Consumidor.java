package com.fcul.marketplace.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("Consumidor")
public class Consumidor extends Utilizador {

    @OneToMany(mappedBy = "consumidor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Encomenda> encomendas;

    public Consumidor() {
        super();
    }

    public Consumidor(Integer idFiscal, String nome, Integer telemovel, String coordenadas,
                      String morada, String freguesia, String municipio, String distrito,
                      Locale.IsoCountryCode pais, Continente continente) {
        super(null, idFiscal, nome, telemovel, coordenadas, morada, freguesia, municipio, distrito, pais, continente);
    }
}
