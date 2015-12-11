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

    double calculate(SaleLine saleLine);

    boolean contains(long productId);

    List<Product> requires();
}
