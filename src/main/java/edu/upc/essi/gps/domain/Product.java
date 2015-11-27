package edu.upc.essi.gps.domain;

/**
 * Classe que representa un producte del nostre sistema.<br>
 * Aquest producte té associat un identificador, un nom i un codi de barres.
 * */
public class Product implements Entity, HasName {

    /**
     * Identificador únic d'aquest producte al sistema.
     * */
    private long id;

    /**
     * Nom del producte.
     * */
    private String name;

    /**
     * Preu unitar d'aquest producte.
     * */
    private final double price;

    /**
     * TODO: dafuq is dat? (:|)
     * */
    private final int vatPct;

    /**
     * Codi de barres d'aquest producte. Aquest codi és únic al sistema.
     * */
    private final int barCode;

    /**
     * Índex de popularitat del producte.
     * */
    private float popularity;

    /**
     * Crea una nova instància d'un producte.
     * @param id identificador únic d'aquest producte al sistema.
     * @param name nom del producte.
     * @param price preu unitar d'aquest producte.
     * @param vatPct dafuq is dat? (:|)
     * @param barCode Codi de barres d'aquest producte.
     * */
    public Product(long id, String name, double price, int vatPct, int barCode) {
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

    /**
     * Consulta l'índex de popularitat del producte.
     * @return l'índex de popularitat del producte.
     * */
    public float getPopularity() {
        return popularity;
    }

    /**
     * Consulta el preu unitar d'aquest producte.
     * @return el preu unitar d'aquest producte.
     * */
    public double getPrice() {
        return price;
    }

    /** TODO
     * Consulta ¿¿??.
     * @return ¿¿??.
     * */
    public int getVatPct() {
        return vatPct;
    }

    /**
     * Consulta el codi de barres d'aquest producte.
     * @return el codi de barres d'aquest producte.
     * */
    public int getBarCode() {
        return barCode;
    }
}
