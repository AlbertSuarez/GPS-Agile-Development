package edu.upc.essi.gps.domain;

/**
 * Classe abstracta que representa un tipus genèric de descompte.
 * */
public abstract class Discount implements Entity, HasName {

    /**
     * Producte al qual s'activa el descompte, i que en dispara l'aplicació.
     * */
    protected final Product trigger;

    /**
     * Nom del descompte.
     * */
    private final String name;

    /**
     * Identificador del descompte.
     * En cas que es tracti d'un descompte manual, aquest camp té valor <code>-1</code>.
     * */
    private final long id;

    /**
     * Codi de barres del descompte.<br>
     * En cas que es tracti d'un descompte manual, aquest camp té valor <code>-1</code>.
     * */
    private final int barCode;

    /**
     * Crea una nova instància d'un descompte a partir d'un producte.
     * @param product producte amb el qual s'asocia el descompte.
     * @param name nom del descompte.
     * @param barCode codi de barres del descompte.<br>
     * En cas que es tracti d'un descompte manual, aquest paràmetre ha de tenir valor <code>-1</code>.
     * @param id identificador del descompte al sistema.
     * */
    public Discount(Product product, String name, int barCode, long id) {
        trigger = product;
        this.id = id;
        this.name = name;
        this.barCode = barCode;
    }

    @Override
    public long getId() {
        return id;
    }

    /**
     * Calcula la quantitat total a descomptar del preu final.
     * @return un nombre negatiu indicant quin preu s'ha de descomptar.
     * */
    public abstract double getDiscount();

    /**
     * Consulta el producte que activa aquest descompte.
     * @return el producte al que s'associa aquest descompte.
     * */
    public Product getTrigger() {
        return trigger;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Consulta el codi de barres del descompte (si en té).
     * @return el codi de barres del descompte no és un descompte manual, el valor <code>-1</code> altrament.
     * */
    public int getBarCode() {
        return barCode;
    }

}
