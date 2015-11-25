package edu.upc.essi.gps.domain;

/**
 * Enumeració que recull el conjunt de possibles estats d'un TPV.
 * */
public enum TPVState {

    /**
     * Estat corresponent a un TPV que es troba en espera, sense cap caixer assignat.<br>
     * El seu nom associat és "<i>disponible</i>".
     * */
    AVAILABLE("disponible"),

    /**
     * Estat corresponent a un TPV que es troba en ús, amb un caixer assignat.<br>
     * El seu nom associat és "<i>ocupat</i>".
     * */
    IDLE("ocupat"),

    /**
     * Estat corresponent a un TPV que es troba bloquejat.<br>
     * El seu nom associat és "<i>bloquejat</i>".
     * */
    BLOCKED("bloquejat");

    /**
     * Nom associat a un estat concret.
     * */
    private String name;

    /**
     * Crea una nova instància d'un estat, amb un nom associat.
     * @param name nom de l'estat, amb el qual es pugui identificar.
     * */
    TPVState(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
