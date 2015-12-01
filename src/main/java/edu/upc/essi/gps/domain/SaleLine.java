package edu.upc.essi.gps.domain;

import edu.upc.essi.gps.domain.Discount;
import edu.upc.essi.gps.domain.Product;

/**
 * Unitat mínima d'una venta, cadascun d'aquests elements s'associa a un producte o descompte
 * */
public class SaleLine {

    /**
     * Identificador associat al producte inicat a la línia
     * */
    private long id;

    /**
     * Nom del producte o despompte
     * */
    private String name;

    /**
     * Valor del producte o despompte
     * */
    private double unitPrice;

    /**
     * Quantitat d'unitats del producte o despompte
     * */
    private int amount;

    /**
     * Codi de barres del producte o despompte
     * */
    private long barCode;

    /**
     * Crea una nova instància de <code>SaleLine</code> a partir de les dades d'un producte.
     * @param product producte a partir del qual es crea la línia de venta.
     * @param amount quantitat d'aquest producte.
     * @param refund indica si es una devolució o no
     */
    public SaleLine(Product product, int amount, boolean refund) {
        this.id = product.getId();
        this.name = product.getName();
        if (!refund) this.unitPrice = product.getPrice();
        else this.unitPrice = -product.getPrice();
        this.barCode = product.getBarCode();
        this.amount = amount;
    }

    /**
     * Crea una nova instància de <code>SaleLine</code> a partir de les dades d'un descompte.
     * @param discount descompte a partir del qual es crea la línia de venta.
     * @param amount quantitat d'aquest descompte.
     */
    public SaleLine(Discount discount, int amount) {
        id = discount.getTrigger().getId();
        name = discount.getName();
        unitPrice = discount.getDiscount();
        this.amount = amount;
        barCode = -1;
    }

    /**
     * Consulta l'identificador del producte indicat a la línia.
     * @return identificador del producte indicat a la línia.
     */
    public long getId() {
        return id;
    }

    /**
     * Consulta el nom del producte o descompte indicat a la línia.
     * @return identificador del producte o descompte indicat a la línia.
     */
    public String getName() {
        return name;
    }

    /**
     * Consulta el valor unitari del producte o descompte indicat a la línia.
     * @return valor unitari del producte o descompte indicat a la línia.
     */
    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Consulta el nombre d'unitats del producte o descompte indicat a la línia.
     * @return nombre d'unitats del producte o descompte indicat a la línia.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Consulta el valor total de la línia.
     * @return valor total de la línia.
     */
    public double getTotalPrice() {
        return unitPrice * amount;
    }

    /**
     * Consulta el codi de barres del producte de la línia.
     * @return codi de barres del producte de la línia.
     */
    public long getBarCode() {
        return barCode;
    }

}
