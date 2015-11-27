package edu.upc.essi.gps.domain;

/**
 * Interfície que correspòn a una entitat del sistema identificada per un <code>barCode</code>
 * */
public interface HasBarCode {

    /**
     * Obté el codi de barres associat a aquest <code>HasBarCode</code>.<br>
     * @return el codi de barres d'aquest <code>HasBarCode</code> del sistema.
     * */
    long getBarCode();

}
