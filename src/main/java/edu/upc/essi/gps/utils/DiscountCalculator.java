package edu.upc.essi.gps.utils;

import edu.upc.essi.gps.domain.Sale;
import edu.upc.essi.gps.domain.discounts.Discount;
import edu.upc.essi.gps.ecommerce.services.DiscountService;

import java.util.ArrayList;
import java.util.List;

/**
 * DiscountCalculator in edu.upc.essi.gps.utils
 *
 * @author casassg
 * @version 1.0
 *          Creation Date: 11/12/15
 */
public final class DiscountCalculator {
    private DiscountCalculator() {
    }

    public static List<Discount> calculate(Sale currentSale, DiscountService discountService) {
        List<Discount> lDisc = new ArrayList<>();

        return lDisc;

    }
}
