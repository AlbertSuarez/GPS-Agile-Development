package edu.upc.essi.gps.ecommerce.services;

import edu.upc.essi.gps.domain.flow.MoneyFlow;
import edu.upc.essi.gps.ecommerce.repositories.MoneyFlowRepository;

import java.util.List;

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

    public List<MoneyFlow> list() {
        List<MoneyFlow> l = moneyFlowRepository.list();
        if (l.size() == 0)
            throw new IllegalStateException("No hi ha cap flux de diners enregistrat al sistema");
        return l;
    }
}
