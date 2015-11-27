package edu.upc.essi.gps.domain;

/**
 * Classe que representa un producte del nostre sistema.<br>
 * Aquest producte té associat un identificador, un nom i un codi de barres.
 * */
public class Product implements Entity, HasName, HasBarCode {

    /**
     * Identificador únic d'aquest producte al sistema.
     * */
    private long id;

    /**
     * Nom del producte.
     * */
    private String name;

    /**
     * Preu unitari d'aquest producte.
     * */
    private final double price;

    /**
     * Percentatge d'IVA corresponent
     * */
    private final double vatPct;

    /**
     * Codi de barres d'aquest producte. Aquest codi és únic al sistema.
     * */
    private final long barCode;

    /**
     * Crea una nova instància d'un producte.
     * @param id identificador únic d'aquest producte al sistema.
     * @param name nom del producte.
     * @param price preu unitar d'aquest producte.
     * @param vatPct dafuq is dat? (:|)
     * @param barCode Codi de barres d'aquest producte.
     * */
    public Product(long id, String name, double price, double vatPct, long barCode) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.vatPct = vatPct;
        this.barCode = barCode;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getBarCode() {
        return barCode;
    }

    /**
     * Consulta el preu unitar d'aquest producte.
     * @return el preu unitar d'aquest producte.
     * */
    public double getPrice() {
        return price;
    }

    /**
     * Consulta el percentatge d'IVA corresponent al producte
     * @return el percentatge d'IVA d'aquest producte
     * */
    public double getVatPct() {
        return vatPct;
    }

}
