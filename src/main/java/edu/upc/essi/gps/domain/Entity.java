package edu.upc.essi.gps.domain;

/**
 * Interf�cie que corresp�n a una entitat del sistema identificada per un <code>id</code>
 * */
public interface Entity {

    /**
     * Obt� l'identificador associat a aquest <code>Entity</code>.<br>
     * @return un <code>long</code> que identifica �nicament aquest <code>Entity</code> al sistema.
     * */
    long getId();

}
