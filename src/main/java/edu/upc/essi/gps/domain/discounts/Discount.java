package edu.upc.essi.gps.domain.discounts;

import edu.upc.essi.gps.domain.Entity;
import edu.upc.essi.gps.domain.HasName;
import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.domain.lines.SaleLine;

import java.util.List;

/**
 * Interficie per descomptes
 * */
public interface Discount extends Entity, HasName {

    /**
     * Calculate money to discount
     *
     * @param saleLine
     * @return
     */
    double calculate(SaleLine saleLine);

    /**
     * Is product contained?
     *
     * @param productId
     * @return
     */
    boolean isTriggeredBy(long productId);

    /**
     * Gets all products required for the Discount to apply (Present)
     *
     * @return
     */
    List<Product> requiresProducts();
}
