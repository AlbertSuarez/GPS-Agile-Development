package edu.upc.essi.gps.utils;

import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.domain.Sale;
import edu.upc.essi.gps.domain.discounts.Discount;
import edu.upc.essi.gps.domain.lines.SaleLine;
import edu.upc.essi.gps.ecommerce.services.DiscountService;

import java.util.*;

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

    public static double calculate(Sale currentSale, DiscountService discountService) {
        double discountAdd = 0.0;
        Set<Discount> applicableDiscounts = new HashSet<>();
        currentSale
                .getLines()
                .stream()
                .map(SaleLine::getProduct)
                .map(Product::getId)
                .map(discountService::listByTriggerId)
                .forEach(applicableDiscounts::addAll);

        Discount bestDiscount;

        if (applicableDiscounts.isEmpty()) return discountAdd;

        for (int i = 0; i < currentSale.getLines().size(); ++i) {
            final Integer pos = i;
            try {
                bestDiscount = applicableDiscounts
                        .stream()
                        .filter(discount -> isAplicable(discount, currentSale, pos))
                        .max((o1, o2) -> new Double(o1.calculate(currentSale, pos)).compareTo(o2.calculate(currentSale, pos)))
                        .get();
                discountAdd += bestDiscount.calculate(currentSale, pos);
            } catch (NoSuchElementException ignored) {
            }
            //TODO: falta obligar a que s'apliqui un regal incomplert
        }


        return discountAdd;

    }

    private static boolean isAplicable(Discount discount, Sale currentSale, int line) {
        if (!discount.isTriggeredBy(currentSale.getLines().get(line).getProduct().getId()))
            return false;
        for (Product requiredProduct : discount.requiresProducts()) {
            if (!currentSale.hasProductByBarCode(requiredProduct.getBarCode()))
                return false;
        }
        return true;
    }

}
