package edu.upc.essi.gps.domain;

import edu.upc.essi.gps.domain.Entity;
import edu.upc.essi.gps.ecommerce.TPVState;

public class TPV implements Entity {

    private final long id;
    private final String shop;
    private final int pos;
    private int nIntents;
    private TPVState state;
    private double cash;

    public TPV(String shop, int pos, long id) {
        this.shop = shop;
        this.pos = pos;
        this.id = id;
        this.nIntents = 0;
        this.state = TPVState.AVAILABLE;
        this.cash = 0;
    }

    public int getnIntents() {
        return nIntents;
    }

    public TPVState getState() {
        return state;
    }

    public int getPos() {
        return pos;
    }

    public String getShop() {
        return shop;
    }

    public void setnIntents(int nIntents) {
        this.nIntents = nIntents;
    }

    public void setState(TPVState state) {
        this.state = state;
    }

    @Override
    public long getId() {
        return id;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

}
