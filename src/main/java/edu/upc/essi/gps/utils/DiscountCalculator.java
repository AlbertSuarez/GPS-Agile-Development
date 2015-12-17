package edu.upc.essi.gps.utils;

import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.domain.Sale;
import edu.upc.essi.gps.domain.discounts.Discount;
import edu.upc.essi.gps.domain.lines.SaleLine;
import edu.upc.essi.gps.ecommerce.services.DiscountService;

import java.util.*;
import java.util.stream.Collectors;

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

        // 1 -> Obtenir una llista amb tots els descomptes que es podrien aplicar, i el seu valor
        // 2 -> Ordenar la llista segons el valor del descompte aplicat
        // 3 -> Aplicar cada descompte, eliminant de la llista els productes utilitzats.

        Set<Discount> applicableDiscounts = new HashSet<>();

        List<SaleLine> lines = currentSale.getLines();

        lines.stream()
                .map(SaleLine::getProduct)
                .map(Product::getId)
                .map(discountService::listByTriggerId)
                .forEach(applicableDiscounts::addAll);

        applicableDiscounts = applicableDiscounts
                .stream()
                .filter(discount -> isAplicable(discount, currentSale))
                .collect(Collectors.toSet());

        if (applicableDiscounts.isEmpty()) return 0d;

        List<Product> productesDisponibles = new LinkedList<>();

        lines.stream()
                .forEach(line -> {
                    for (int i = 0; i < line.getAmount(); ++i) {
                        productesDisponibles.add(line.getProduct());
                    }
                });

        List<Discount> descomptesDisponibles = applicableDiscounts
                .stream()
                .collect(Collectors.toList());

        return computateBestDiscountCombination(descomptesDisponibles, productesDisponibles);
    }

    private static boolean isAplicable(Discount discount, Sale currentSale) {
        Boolean someLineIsTrigger = currentSale
                .getLines()
                .stream()
                .map(SaleLine::getProduct)
                .map(Product::getId)
                .anyMatch(discount::isTriggeredBy);

        Boolean hasAllNecessaryProducts = discount
                .requiresProducts()
                .stream()
                .map(Product::getBarCode)
                .allMatch(currentSale::hasProductByBarCode);

        return someLineIsTrigger && hasAllNecessaryProducts;
    }

    private static Double computateBestDiscountCombination(List<Discount> discounts, List<Product> products) {
        //TENIM: discounts(List<Discount>), products(List<Product>)
        //PRE: No hi ha elements repetits a "discounts"

        //CAL: generar totes les permutacions en l'ordre d'aplicació dels descomptes i substracció de productes utilitzats.

        return computateBestDiscountRecursive(discounts, products, 0, 0d);
    }

    private static Double computateBestDiscountRecursive(List<Discount> discounts, List<Product> products, int i, Double acumulated) {
        if (i >= discounts.size()) return acumulated;
        Double A = computateBestDiscountRecursive(discounts, new LinkedList<>(products), i+1, acumulated);
        DiscountHolder holder = discounts.get(i).calculate(products);
        products.removeAll(holder.getRequired());
        Double B = computateBestDiscountRecursive(discounts, products, i+1, acumulated + holder.getPrice());
        return Math.max(A, B);
    }

    public static class DiscountHolder {

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

}
