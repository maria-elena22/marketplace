package com.fcul.marketplace.model;

import com.fcul.marketplace.model.enums.Continente;
import com.fcul.marketplace.model.utils.Coordinate;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Locale;

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

    public Consumidor(Integer idFiscal, String nome, Integer telemovel, Coordinate coordenadas,
                      String morada, String freguesia, String municipio, String distrito,
                      Locale.IsoCountryCode pais, Continente continente) {
        super(null, idFiscal, nome, telemovel, coordenadas, morada, freguesia, municipio, distrito, pais, continente);
    }
}
