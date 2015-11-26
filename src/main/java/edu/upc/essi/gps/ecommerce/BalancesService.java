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

    public List<Balance> list() {
        if (balanceRepository.list().size() == 0)
            throw new IllegalStateException("No hi ha cap desquadrament enregistrat al sistema");
        return balanceRepository.list();
    }

    public long newBalance(double qtt, String saleAssistantName, String nomBotiga ){
        long id = balanceRepository.newId();
        Balance imbalance = new Balance(id, qtt, saleAssistantName, nomBotiga);
        balanceRepository.insert(imbalance);
        return id;
    }

}
