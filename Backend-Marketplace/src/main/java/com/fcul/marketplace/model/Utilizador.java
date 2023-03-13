package com.fcul.marketplace.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@MappedSuperclass
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

    private String coordenadas;

    private String morada;

    private String freguesia;

    private String municipio;

    private String distrito;

    private String pais;

    private String continente;
}
