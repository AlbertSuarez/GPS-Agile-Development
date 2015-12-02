package edu.upc.essi.gps.domain;

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
    private List<SaleLine> devolucions = new LinkedList<>();

    /**
     * Moitu de la devolució
     */
    private String reason;

    /**
     * Constructora sense paràmetres
     */
    public Refund() {}

    /**
     * Constructora amb identificador i línies de devolució com a paràmetres
     * @param id
     * @param devolucions
     */
    public Refund(long id, List<SaleLine> devolucions, String reason) {
        this.id = id;
        this.devolucions = devolucions;
        this.reason = reason;
    }

    @Override
    public long getId() {
        return id;
    }

}
