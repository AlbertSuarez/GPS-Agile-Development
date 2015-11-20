package edu.upc.essi.gps.domain;

/**
 * Interfície que correspòn a una entitat del sistema identificada per un <code>id</code>
 * */
public interface Entity {

    /**
     * Obté l'identificador associat a aquest <code>Entity</code>.<br>
     * @return un <code>long</code> que identifica únicament aquest <code>Entity</code> al sistema.
     * */
    long getId();

}
