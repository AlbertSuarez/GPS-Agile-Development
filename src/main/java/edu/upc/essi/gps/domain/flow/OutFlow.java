package edu.upc.essi.gps.domain.flow;

import edu.upc.essi.gps.domain.TPV;

/**
 * OutFlow in edu.upc.essi.gps.domain.flow
 *
 * @author casassg
 * @version 1.0
 *          Creation Date: 11/12/15
 */
public class OutFlow extends MoneyFlow {


    private final long originId;

    public OutFlow(long id, double amount, TPV origin) {
        super(id, amount);
        originId = origin.getId();
    }


}
