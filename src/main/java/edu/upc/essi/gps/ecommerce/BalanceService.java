package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.Balance;

import java.util.Date;

/**
 * Created by jmotger on 25/11/15.
 */
public class BalanceService {

    private BalancesRepository imbalanceRepository;

    public BalanceService(BalancesRepository imbalanceRepository) {this.imbalanceRepository = imbalanceRepository;}

    public long newImbalance(Date date, int qtt ){
        long id = imbalanceRepository.newId();
        Balance imbalance = new Balance(id, date, qtt);
        imbalanceRepository.insert(imbalance);
        return id;
    }

}
