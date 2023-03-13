package com.fcul.marketplace.model.enums;

public enum EstadoTransporte {
    DISPONIVEL("d"),
    EM_ENTREGA("e"),
    INDISPONIVEL("i");

    private String rep;

    EstadoTransporte(String s) {
        this.rep = s;
    }

    @Override
    public String toString() {
        return this.rep;
    }
}
