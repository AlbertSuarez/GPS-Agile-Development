package edu.upc.essi.gps.domain.discounts;

import edu.upc.essi.gps.domain.Entity;
import edu.upc.essi.gps.domain.HasName;
import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.domain.Sale;

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
    private long id;

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

    public Discount(Product product, String name) {
        trigger = product;
        this.name = name;
        id = -1;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
