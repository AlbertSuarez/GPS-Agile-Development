package edu.upc.essi.gps.domain;

/**
 * Interfície que correspòn a una entitat del sistema identificada per un <code>id</code>
 * */
public interface Entity {

    /**
     * Obté l'identificador associat a aquest <code>Entity</code>.<br>
     * NOTA: Dos objectes diferents instàncies de classes diferents poden tenir el mateix <code>id</code>.
     * En canvi, dos objectes instància de la mateixa classe no poden tenir el mateix <code>id</code>.
     * @return un <code>long</code> que identifica únicament aquest <code>Entity</code> al sistema.
     * */
    long getId();

}
