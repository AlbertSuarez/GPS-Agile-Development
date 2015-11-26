package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.utils.Comparators;
import edu.upc.essi.gps.utils.Matchers;
import edu.upc.essi.gps.utils.Repository;

import java.util.Comparator;
import java.util.List;


public class ProductsRepository extends Repository<Product> {

    public static Comparator<Product> byPopularity = (o1, o2) -> new Float(o1.getPopularity()).compareTo(o2.getPopularity());

    public List<Product> lookForName(final String name) {
        return list(Matchers.containsNameMatcher(name), Comparators.byName);
    }

    private Product findByName(final String name) {
        return find(Matchers.nameMatcher(name));
    }

    public Product findByBarCode(final int barCode){
        return find((p) -> p.getBarCode() == barCode);
    }

    public Product findById(final long prodId){
        return find((p) -> p.getId() == prodId);
    }

    @Override
    protected void checkInsert(final Product entity) throws RuntimeException {
        if(findByName(entity.getName()) != null)
            throw new IllegalArgumentException("Ja existeix un producte amb aquest nom");
        if(findByBarCode(entity.getBarCode()) != null)
            throw new IllegalArgumentException("Ja existeix un producte amb aquest codi de barres");
    }
}
