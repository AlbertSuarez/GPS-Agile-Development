package edu.upc.essi.gps.domain.flow;

import edu.upc.essi.gps.domain.TPV;

/**
 * TPVFlow in edu.upc.essi.gps.domain.flow
 *
 * @author casassg
 * @version 1.0
 *          Creation Date: 11/12/15
 */
public class TPVFlow extends MoneyFlow {

    private final long originId;
    private final long destinyId;

    public TPVFlow(long id, double amount, TPV origin, TPV destiny) {
        super(id, amount);
        originId = origin.getId();
        destinyId = destiny.getId();
    }

    @Override
    public String getKind() {
        return "TPVFlow";
    }
}
