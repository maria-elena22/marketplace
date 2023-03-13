package com.fcul.marketplace.model.enums;

public enum EstadoEncomenda {
    PAGO("p"),
    A_PROCESSAR("ap"),
    PROCESSADO("pr"),
    EM_DISTRIBUICAO("d"),
    ENTREGUE("e"),
    CANCELADO("c"),
    POR_PAGAR("pg");

    private String rep;

    EstadoEncomenda(String s) {
        this.rep = s;
    }

    @Override
    public String toString() {
        return this.rep;
    }
}
