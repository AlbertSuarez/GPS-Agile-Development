package edu.upc.essi.gps.domain.flow;

import edu.upc.essi.gps.domain.TPV;

/**
 * InFlow in edu.upc.essi.gps.domain.flow
 *
 * @author casassg
 * @version 1.0
 *          Creation Date: 11/12/15
 */
public class InFlow extends MoneyFlow {

    private final long destinyId;

    public InFlow(long id, double amount, TPV destiny) {
        super(id, amount);
        destinyId = destiny.getId();
    }

    @Override
    public String getKind() {
        return "InFlow";
    }
}
