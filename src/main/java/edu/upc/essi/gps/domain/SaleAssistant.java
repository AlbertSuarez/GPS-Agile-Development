package edu.upc.essi.gps.domain;

/**
 * Classe que representa un caixer del sistema, identificat per <code>id</code> i amb un nom propi.<br>
 * A més, cada caixer té un password amb el qual s'identifica al nostre sistema.
 * */
public class SaleAssistant implements Entity, HasName {

    /**
     * <code>long</code> que identifica únicament el caixer al sistema.
     * */
    private final long id;

    /**
     * <code>String</code> associat al nom i cognoms del caixer.
     * */
    private String name;

    /**
     * <code>String</code> que emmagatzema el password encriptat del nostre usuari.
     * */
    private String encryptedPass;

    /**
     * Crea una nova instància d'un <code>SaleAssistant</code> amb el nom d'usuari indicat i el password a emmagatzemar.<br>
     * L'identificador intern de l'usuari l'assigna el sistema automàticament.
     * @param name Nom i cognoms del caixer que es vol donar d'alta.
     * @param encryptedPass Password prèviament encriptat que s'ha d'emmagatzemar al sistema.
     * @param id Identificador intern del caixer.
     * */
    public SaleAssistant(String name, String encryptedPass, long id) {
        this.name = name;
        this.id = id;
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

    /**
     * Consulta el password de l'usuari després de ser encriptat
     * @return El password encriptat.
     * */
    public String getEncryptedPass() {
        return encryptedPass;
    }

}
