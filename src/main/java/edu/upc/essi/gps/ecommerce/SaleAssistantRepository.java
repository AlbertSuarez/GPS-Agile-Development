package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.SaleAssistant;
import edu.upc.essi.gps.utils.Repository;

public class SaleAssistantRepository extends Repository<SaleAssistant> {

    public SaleAssistant findById(final long id){
        return find((c) -> c.getId() == id);
    }

    @Override
    protected void checkInsert(final SaleAssistant entity) throws RuntimeException {

    }

}
