package edu.upc.essi.gps.domain.flow;

import edu.upc.essi.gps.domain.Entity;

/**
 * MoneyFlow in edu.upc.essi.gps.domain
 *
 * @author casassg
 * @version 1.0
 *          Creation Date: 11/12/15
 */
public abstract class MoneyFlow implements Entity {

    private long id;

    private double amount;

    protected MoneyFlow(long id, double amount) {
        this.id = id;
        this.amount = amount;
    }

    @Override
    public long getId() {
        return id;
    }

    public abstract String getKind();

    public String getOrigin() {
        return "origen";
    }

    public String getDestiny() {
        return "desti";
    }

    public double getAmount() {
        return amount;
    }
}
