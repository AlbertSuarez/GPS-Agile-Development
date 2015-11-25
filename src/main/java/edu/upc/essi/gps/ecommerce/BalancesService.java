package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.Balance;

import java.util.Date;
import java.util.List;

/**
 * Created by jmotger on 25/11/15.
 */
public class BalancesService {

    private BalancesRepository balanceRepository;

    public BalancesService(BalancesRepository imbalanceRepository) {this.balanceRepository = imbalanceRepository;}

    public List<Balance> list() { return balanceRepository.list();}

    public List<Balance> listBalanced() { return balanceRepository.listBalanced();}

    public List<Balance> listImbalanced() {return balanceRepository.listImbalanced();}

    public long newBalance(double qtt ){
        long id = balanceRepository.newId();
        Balance imbalance = new Balance(id, qtt);
        balanceRepository.insert(imbalance);
        return id;
    }

}
