package com.fcul.marketplace.model.enums;

public enum Pagamento {
    MULTIBANCO("m"),
    MBWAY("w"),
    VISACARD("v");

    private String rep;

    Pagamento(String s) {
        this.rep = s;
    }

    @Override
    public String toString() {
        return this.rep;
    }
}
