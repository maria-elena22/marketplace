package com.fcul.marketplace.model.enums;

public enum IVA {
    IVA_6(6),
    IVA_13(13),
    IVA_23(23);

    private final int value;

    IVA(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

