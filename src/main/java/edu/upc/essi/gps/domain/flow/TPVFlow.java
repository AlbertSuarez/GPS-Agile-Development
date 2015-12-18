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

    private final String originId;
    private final String destinyId;

    public TPVFlow(long id, double amount, TPV origin, TPV destiny) {
        super(id, amount);
        originId = origin.getShop() + " " + origin.getPos();
        destinyId = destiny.getShop() + " " + destiny.getPos();
    }

    @Override
    public String getKind() {
        return "TPVFlow";
    }

    @Override
    public String getOrigin() { return originId; }

    @Override
    public String getDestiny() { return destinyId; }
}
