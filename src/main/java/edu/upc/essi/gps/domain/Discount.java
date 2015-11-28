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
     * Crea una nova instància d'un descompte a partir d'un producte.
     * @param product producte amb el qual s'asocia el descompte.
     * @param name nom del descompte.
     * En cas que es tracti d'un descompte manual, aquest paràmetre ha de tenir valor <code>-1</code>.
     * @param id identificador del descompte al sistema.
     * */
    public Discount(Product product, String name, long id) {
        trigger = product;
        this.id = id;
        this.name = name;
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
     * Calcula la quantitat total a descomptar del preu final.
     * @return un nombre negatiu indicant quin preu s'ha de descomptar.
     * */
    public abstract double getDiscount();

    /**
     * Comproba si el descompte indicat és vàlid a la venta actual.
     * @param currentSale venta a la que es pot aplicar potencialment el descompte actual.
     * @return <code>true</code> si el descompte pot ser aplicat a la venta actual.<br>
     * <code>false</code> altrament.
     * */
    public abstract boolean checkSale(Sale currentSale);

    /**
     * Calcula el nombre d'aplicacions del descompte a la venta actual.
     * @param currentSale venta a la que es pot aplicar el descompte actual.
     * @return el nombre de vegades que s'ha d'aplicar el descompte a la venta actual.
     * */
    public abstract int getAmount(Sale currentSale);


    /**
     * Consulta el producte que activa aquest descompte.
     * @return el producte al que s'associa aquest descompte.
     * */
    public Product getTrigger() {
        return trigger;
    }

}
