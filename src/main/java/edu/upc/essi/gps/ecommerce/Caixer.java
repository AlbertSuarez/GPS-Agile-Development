package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.Entity;
import edu.upc.essi.gps.domain.HasName;

/**
 * Classe que representa un caixer del sistema, identificat per <code>id</code> i amb un nom propi.
 * A m�s, cada caixer t� un password amb el qual s'identifica al nostre sistema.
 * */
public class Caixer implements Entity, HasName {

    /**
     * <code>long</code> que identifica �nicament el caixer al sistema.
     * */
    private long id;

    /**
     * <code>String</code> associat al nom i cognoms del caixer.
     * */
    private String name;

    /**
     * <code>String</code> que emmagatzema el password encriptat del nostre usuari.
     * */
    private String encryptedPass;

    /**
     * Crea una nova inst�ncia d'un <code>Caixer</code> amb el nom d'usuari indicat i el password a emmagatzemar.<br>
     * L'identificador intern de l'usuari l'assigna el sistema autom�ticament.
     * @param name Nom i cognoms del caixer que es vol donar d'alta.
     * @param encryptedPass Password pr�viament encriptat que s'ha d'emmagatzemar al sistema.
     * */
    public Caixer(String name, String encryptedPass) {
        this.name = name;
        id = CaixerRepository.getNewId();
        this.encryptedPass = encryptedPass;
    }


    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getEncryptedPass() {
        return encryptedPass;
    }
}
