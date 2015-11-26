package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.Balance;
import edu.upc.essi.gps.utils.Repository;

import java.util.List;

/**
 * Created by jmotger on 25/11/15.
 */
public class BalancesRepository extends Repository<Balance> {

    @Override
    protected void checkInsert(final Balance entity) throws RuntimeException {

    }
}
