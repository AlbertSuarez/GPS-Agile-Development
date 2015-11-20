package edu.upc.essi.gps.domain;

/**
 * Interf�cie que corresp�n a una entitat del sistema identificada per un <code>id</code>
 * */
public interface Entity {

    /**
     * Obt� l'identificador associat a aquest <code>Entity</code>.<br>
     * NOTA: Dos objectes diferents inst�ncies de classes diferents poden tenir el mateix <code>id</code>.
     * En canvi, dos objectes inst�ncia de la mateixa classe no poden tenir el mateix <code>id</code>.
     * @return un <code>long</code> que identifica �nicament aquest <code>Entity</code> al sistema.
     * */
    long getId();

}
