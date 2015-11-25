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

    public Balance(long id, double qtt) {
        this.id = id;
        this.qtt = qtt;
    }

    @Override
    public long getId() {
        return id;
    }

    public double getQtt() { return qtt;}

}
