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

        Set<Discount> applicableDiscounts = new LinkedHashSet<>();

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

        productesDisponibles.sort(((o1, o2) -> o1.getName().compareTo(o2.getName())));

        List<Discount> descomptesDisponibles = applicableDiscounts
                .stream()
                .sorted(((o1, o2) -> o1.getName().compareTo(o2.getName())))
                .collect(Collectors.toList());

        return computeBestDiscountCombination(descomptesDisponibles, productesDisponibles);
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

    private static Double computeBestDiscountCombination(List<Discount> discounts, List<Product> products) {
        //TENIM: discounts(List<Discount>), products(List<Product>)
        //PRE: No hi ha elements repetits a "discounts"

        //CAL: generar totes les permutacions en l'ordre d'aplicació dels descomptes i substracció de productes utilitzats.

        List<List<Discount>> permutations = getPermutations(discounts);

        Double max = 0d;
        Double val;

        System.out.println("Permutations");
        for (List<Discount> list : permutations) {
            System.out.println(list);
            val = computeBestDiscountRecursive(list, products, 0, 0d);
            System.out.println(val);
            if (val > max) max = val;
        }

        return max;
    }

    /**
     * Donada una permutació de descomptes, en calcula el màxim valor.
     * */
    private static Double computeBestDiscountRecursive(List<Discount> discounts, List<Product> products, int i, Double acumulated) {
        if (i >= discounts.size()) return acumulated;
        Double A = computeBestDiscountRecursive(discounts, new LinkedList<>(products), i + 1, acumulated);
        DiscountHolder holder = discounts.get(i).calculate(new LinkedList<>(products));
        List<Product> listB = new LinkedList<>(products);
        listB.removeAll(holder.getRequired());
        Double B = computeBestDiscountRecursive(discounts, listB, i + 1, acumulated + holder.getPrice());
        return Math.max(A, B);
    }


    public static List<List<Discount>> getPermutations(List<Discount> discounts){
        if(discounts.size() == 1) {
            List<Discount> list = new LinkedList<>();
            list.add(discounts.get(0));
            List<List<Discount>> listOfList = new LinkedList<>();
            listOfList.add(list);
            return listOfList;
        }

        Set<Discount> set = new HashSet<>(discounts);

        List<List<Discount>> listOfLists = new LinkedList<>();

        for(Discount d: discounts){
            Set<Discount> setOfCopied = new HashSet<>(set);
            setOfCopied.remove(d);

            List<List<Discount>> permute = getPermutations(new ArrayList<>(setOfCopied));
            for (List<Discount> perm : permute) {
                perm.add(d);
                listOfLists.add(perm);
            }
        }

        return listOfLists;
    }


}
