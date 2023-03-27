package com.fcul.marketplace.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ViagemDTO {

    private Integer idViagem;
    private Timestamp dataInicio;
    private Timestamp dataFim;

}
