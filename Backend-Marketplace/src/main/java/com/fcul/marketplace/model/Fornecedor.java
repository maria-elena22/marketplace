package com.fcul.marketplace.model;

import com.fcul.marketplace.model.enums.Continente;
import com.fcul.marketplace.model.enums.Pais;
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
@DiscriminatorValue("Fornecedor")
public class Fornecedor extends Utilizador {

    @OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UniProd> unidadesProducao;

    public Fornecedor() {
        super();
    }

    public Fornecedor(Integer idFiscal, String nome, String email, Integer telemovel, Coordinate coordenadas,
                      String morada, String freguesia, String municipio, String distrito,
                      Pais pais, Continente continente, boolean active) {
        super(null, idFiscal, nome, email, telemovel, coordenadas, morada, freguesia, municipio, distrito, pais, continente,active);
    }
}
