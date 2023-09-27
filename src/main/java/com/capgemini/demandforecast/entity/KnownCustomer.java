package com.capgemini.demandforecast.entity;

public enum KnownCustomer {
    IKEA(false),
    SKF(false),
    VOLVO(false),
    IKANO(false),
    EON(false),
    Skatteverket(true),
    Polisen(true);

    public final boolean isPSU;
    KnownCustomer(boolean isPSU) {
        this.isPSU = isPSU;
    }
}
