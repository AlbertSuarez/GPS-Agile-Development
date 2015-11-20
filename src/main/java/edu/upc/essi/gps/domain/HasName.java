package edu.upc.essi.gps.domain;

/**
 * Interf�cie que corresp�n a una entitat del sistema identificada per un <code>nom</code>
 * */
public interface HasName {

    /**
     * Obt� el nom associat a aquest <code>HasName</code>.<br>
     * @return el nom d'aquest <code>HasName</code> del sistema.
     * */
    String getName();

}
