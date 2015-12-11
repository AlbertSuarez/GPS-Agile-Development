package edu.upc.essi.gps.domain.discounts;

import edu.upc.essi.gps.domain.Entity;
import edu.upc.essi.gps.domain.HasName;
import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.domain.Sale;

import java.util.List;

/**
 * Interficie per descomptes
 * */
public interface Discount extends Entity, HasName {

    double calculate(Sale currentSale);

    boolean contains(long productId);

    List<Product> appliedTo();
}
