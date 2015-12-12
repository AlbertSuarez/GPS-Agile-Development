package edu.upc.essi.gps.ecommerce.repositories;

import edu.upc.essi.gps.domain.discounts.Discount;
import edu.upc.essi.gps.utils.Matchers;
import edu.upc.essi.gps.utils.Repository;

import java.util.List;

public class DiscountRepository extends Repository<Discount> {

    public Discount findByName(final String name) {
        return find(Matchers.nameMatcher(name));
    }

    public List<Discount> listByProductId(final long productId) {
        return list((d) -> d.isTriggeredBy(productId));
    }

    @Override
    protected void checkInsert(Discount discount) throws RuntimeException {
        if(findByName(discount.getName())!=null)
            throw new IllegalArgumentException("Ja existeix un descompte amb aquest nom");
    }

}
