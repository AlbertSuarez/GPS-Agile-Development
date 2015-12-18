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


    private final String originId;

    public OutFlow(long id, double amount, TPV origin) {
        super(id, amount);
        originId = origin.getShop() + " " + origin.getPos();
    }


    @Override
    public String getKind() {
        return "OutFlow";
    }

    @Override
    public String getOrigin() { return originId; }
}
