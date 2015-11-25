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
     * Data del torn del desquadrament
     */
    private Date date;
    /**
     * Quantitat desquadrada
     */
    private int qtt;

    public Balance(long id, Date date, int qtt) {
        this.id = id;
        this.date = date;
        this.qtt = qtt;
    }

    @Override
    public long getId() {
        return id;
    }

    public Date getDate() { return date;}

    public int getQtt() { return qtt;}

}
