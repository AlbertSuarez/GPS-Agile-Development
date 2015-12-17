package edu.upc.essi.gps.ecommerce.repositories;

import edu.upc.essi.gps.domain.SaleAssistant;
import edu.upc.essi.gps.utils.Repository;

public class SaleAssistantRepository extends Repository<SaleAssistant> {

    public SaleAssistant findById(final long id){
        return find((c) -> c.getId() == id);
    }

    @Override
    protected void checkInsert(final SaleAssistant entity) throws RuntimeException {

    }

    public Object findByName(String name) {
        return find((c) -> c.getName() == name);
    }
}
