package com.fcul.marketplace.dto.encomenda;

import lombok.Data;

@Data
public class EncomendaPaymentDTO {
    private EncomendaDTO encomendaDTO;
    private String StripeClientSecret;
}
