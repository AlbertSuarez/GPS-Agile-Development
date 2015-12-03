package edu.upc.essi.gps.domain;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jmotger on 1/12/15.
 */
public class Refund implements Entity {

    /**
     * Identificador únic d'aquesta devolució al sistema
     */
    private long id;

    /**
     * Conjunt de línies de devolucions associades a la devolució
     */
    private List<RefundLine> devolucions = new LinkedList<>();

    /**
     * Moitu de la devolució
     */
    private String reason;


    public Refund(long id, Product p, int unitats, String reason) {
        this.id = id;
        this.devolucions = Collections.singletonList(new RefundLine(p,unitats));
        this.reason = reason;
    }

    @Override
    public long getId() {
        return id;
    }



}

