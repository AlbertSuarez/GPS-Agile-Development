package edu.upc.essi.gps.domain;

public enum TPVState {
    AVAILABLE("disponible"),
    IDLE("ocupat"),
    BLOCKED("bloquejat");

    private String string;
    TPVState(String name){ string = name; }

    @Override
    public String toString() {
        return string;
    }

}
