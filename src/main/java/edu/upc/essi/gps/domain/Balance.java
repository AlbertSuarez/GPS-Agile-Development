package edu.upc.essi.gps.domain;

import java.util.Date;

/**
 * Created by jmotger on 25/11/15.
 */
public class Balance implements Entity{

    /**
     * Identificador únic d'aquest producte al sistema.
     * */
    private long id;
    /**
     * Quantitat desquadrada
     */
    private double qtt;
    /**
     * Nom del caixer que ha registrat el balanç
     */
    private String saleAssistantName;
    /**
     * Nom de la botiga on s'ha registrat el balanç
     */
    private String nomBotiga;

    public Balance(long id, double qtt, String saleAssistantName, String nomBotiga) {
        this.id = id;
        this.qtt = qtt;
        this.saleAssistantName = saleAssistantName;
        this.nomBotiga = nomBotiga;
    }

    @Override
    public long getId() {
        return id;
    }

    public double getQtt() { return this.qtt;}

    public String getSaleAssistantName() { return this.saleAssistantName;}

    public String getNomBotiga() { return this.nomBotiga;}

}
