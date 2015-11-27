package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.Discount;
import edu.upc.essi.gps.utils.Matchers;
import edu.upc.essi.gps.utils.Repository;

import java.util.List;

public class DiscountRepository extends Repository<Discount> {

    public Discount findByName(final String name) {
        return find(Matchers.nameMatcher(name));
    }

    public Discount findById(final long id){
        return find((d) -> d.getId() == id);
    }

    public Discount findByBarCode(final long id){
        return find((d) -> d.getId() == id);
    }

    public List<Discount> findByTriggerId(final long productId) {
        return list((d) -> d.getTrigger().getId() == productId);
    }

    @Override
    protected void checkInsert(Discount discount) throws RuntimeException {
        if(findByName(discount.getName())!=null)
            throw new IllegalArgumentException("Ja existeix un descompte amb aquest nom");
    }

}
