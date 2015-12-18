package edu.upc.essi.gps.utils;

import edu.upc.essi.gps.domain.Product;

import java.util.List;

/**
 * DiscountHolder in edu.upc.essi.gps.utils
 *
 * @author casassg
 * @version 1.0
 *          Creation Date: 18/12/15
 */
public class DiscountHolder {

    private final Double price;

    private final List<Product> required;


    public DiscountHolder(Double price, List<Product> required) {
        this.price = price;
        this.required = required;
    }
    
    public Double getPrice() {
        return price;
    }

    public List<Product> getRequired() {
        return required;
    }

}
