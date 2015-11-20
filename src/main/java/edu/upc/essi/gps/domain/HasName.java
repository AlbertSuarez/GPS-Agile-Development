package edu.upc.essi.gps.domain;

/**
 * Interfície que correspòn a una entitat del sistema identificada per un <code>nom</code>
 * */
public interface HasName {

    /**
     * Obté el nom associat a aquest <code>HasName</code>.<br>
     * @return el nom d'aquest <code>HasName</code> del sistema.
     * */
    String getName();

}
