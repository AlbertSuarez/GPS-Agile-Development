package edu.upc.essi.gps.ecommerce.repositories;

import com.sun.istack.internal.NotNull;
import edu.upc.essi.gps.domain.flow.MoneyFlow;
import edu.upc.essi.gps.utils.Repository;

/**
 * MoneyFlowRepository in edu.upc.essi.gps.ecommerce.repositories
 *
 * @author casassg
 * @version 1.0
 *          Creation Date: 11/12/15
 */
public class MoneyFlowRepository extends Repository<MoneyFlow> {
    @Override
    protected void checkInsert(@NotNull MoneyFlow moneyFlow) throws RuntimeException {

    }
}
