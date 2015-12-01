package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.Refund;
import edu.upc.essi.gps.utils.Repository;

/**
 * Created by jmotger on 1/12/15.
 */
public class RefundsRepository extends Repository<Refund> {

    @Override
    protected void checkInsert(final Refund entity) throws RuntimeException {

    }
}
