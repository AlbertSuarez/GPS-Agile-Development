package edu.upc.essi.gps.utils;

import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.domain.Sale;
import edu.upc.essi.gps.domain.discounts.Discount;
import edu.upc.essi.gps.domain.lines.SaleLine;
import edu.upc.essi.gps.ecommerce.services.DiscountService;

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

    public static double calculate(Sale currentSale, DiscountService discountService) {
        double discountAdd = 0.0;
        for (SaleLine line : currentSale.getLines()) {
            List<Discount> lineDiscounts = discountService.listByTriggerId(line.getProduct().getId());
            if (lineDiscounts != null && !lineDiscounts.isEmpty()) {
                Discount bestDiscount = lineDiscounts
                        .stream()
                        .max((o1, o2) -> new Double(o1.calculate(line)).compareTo(o2.calculate(line)))
                        .get();
                if (isAplicable(bestDiscount, currentSale)) {
                    discountAdd += bestDiscount.calculate(line);
                }
            }

        }
        return discountAdd;

    }

    private static boolean isAplicable(Discount bestDiscount, Sale currentSale) {
        List<Product> requiredProducts = bestDiscount.requiresProducts();
        for (Product requiredProduct : requiredProducts) {
            if (!currentSale.hasProductByBarCode(requiredProduct.getBarCode()))
                return false;
        }
        return true;
    }
}
