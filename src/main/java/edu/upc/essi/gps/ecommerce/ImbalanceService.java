package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.Imbalance;

import java.util.Date;

/**
 * Created by jmotger on 25/11/15.
 */
public class ImbalanceService {

    private ImbalanceRepository imbalanceRepository;

    public ImbalanceService(ImbalanceRepository imbalanceRepository) {this.imbalanceRepository = imbalanceRepository;}

    public long newImbalance(Date date, int qtt ){
        long id = imbalanceRepository.newId();
        Imbalance imbalance = new Imbalance(id, date, qtt);
        imbalanceRepository.insert(imbalance);
        return id;
    }

}
