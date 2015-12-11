package edu.upc.essi.gps.ecommerce.services;

import edu.upc.essi.gps.ecommerce.repositories.MoneyFlowRepository;

/**
 * MoneyFlowService in edu.upc.essi.gps.ecommerce.services
 *
 * @author casassg
 * @version 1.0
 *          Creation Date: 11/12/15
 */
public class MoneyFlowService {

    private MoneyFlowRepository moneyFlowRepository;

    public MoneyFlowService(MoneyFlowRepository moneyFlowRepository) {
        this.moneyFlowRepository = moneyFlowRepository;
    }
}
