package com.fcul.marketplace.model;

import com.fcul.marketplace.model.enums.Continente;
import com.fcul.marketplace.model.utils.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Locale;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Utilizador {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idUtilizador;

    @Column(unique = true)
    private Integer idFiscal;

    private String nome;

    private Integer telemovel;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "longitude"))
    })
    private Coordinate coordenadas;

    private String morada;

    private String freguesia;

    private String municipio;

    private String distrito;

    private Locale.IsoCountryCode pais;

    private Continente continente;
}
