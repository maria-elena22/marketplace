package com.fcul.marketplace.model;

import com.fcul.marketplace.model.annotations.Unique;
import com.fcul.marketplace.model.enums.Continente;
import com.fcul.marketplace.model.enums.Pais;
import com.fcul.marketplace.model.utils.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @Unique(message = "O id fiscal já se encontra no sistema",parameterName="idFiscal",className = "Utilizador")
    private Integer idFiscal;

    @NotBlank
    private String nome;

    @NotBlank
    @Unique(message = "O email já se encontra no sistema",parameterName="email",className = "Utilizador")
    private String email;


    private Integer telemovel;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "longitude"))
    })
    private Coordinate coordenadas;

    @NotBlank
    private String morada;

    @NotBlank
    private String freguesia;

    @NotBlank
    private String municipio;

    @NotBlank
    private String distrito;

    private Pais pais;

    @NotNull
    private Continente continente;

    private boolean active;
}
