package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.Sale;
import edu.upc.essi.gps.utils.Repository;

/**
 * Created by Albert on 29/11/2015.
 */
public class SalesRepository extends Repository<Sale> {

    @Override
    protected void checkInsert(final Sale sale) throws RuntimeException {

    }
}
