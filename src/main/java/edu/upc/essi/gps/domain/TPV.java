package edu.upc.essi.gps.domain;

public class TPV implements Entity {

    private final long id;
    private final String shop;
    private final int pos;

    private int nIntents;
    private TPVState state;
    private double cash;
    private double initialCash;

    private SaleAssistant currentSaleAssistant;
    private Sale currentSale;


    public TPV(String shop, int pos, long id) {
        this.shop = shop;
        this.pos = pos;
        this.id = id;
        reset();
    }

    public void reset() {
        this.nIntents = 0;
        this.state = TPVState.AVAILABLE;
        this.cash = 0;
        currentSaleAssistant = null;
        currentSale = null;
    }

    public int getnIntents() {
        return nIntents;
    }

    public TPVState getState() {
        return state;
    }

    public void setNIntents(int nIntents) {
        this.nIntents = nIntents;
    }

    public void addNIntents(int n) {
        this.nIntents += n;
    }

    public int getPos() {
        return pos;
    }

    public String getShop() {
        return shop;
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

    public void addCash(double cash) {
        this.cash += cash;
    }

    public double getInitialCash() {
        return initialCash;
    }

    public void setInitialCash(double initialCash) {
        this.initialCash = initialCash;
    }

    public SaleAssistant getCurrentSaleAssistant(){
        return currentSaleAssistant;
    }

    public void setCurrentSaleAssistant(SaleAssistant saleAssistant) {
        currentSaleAssistant = saleAssistant;
    }

    public boolean hasSaleAssistant() {
        return currentSaleAssistant != null;
    }

    public Sale getCurrentSale() {
        return currentSale;
    }

    public void newSale() {
        currentSale = new Sale();
    }

    public void endSale() {
        currentSale = null;
    }

    public boolean hasSale() {
        return currentSale != null;
    }

}
