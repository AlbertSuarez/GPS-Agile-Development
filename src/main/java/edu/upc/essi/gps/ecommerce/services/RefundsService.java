package edu.upc.essi.gps.ecommerce.services;

import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.domain.Refund;
import edu.upc.essi.gps.ecommerce.repositories.RefundsRepository;

import java.util.List;

/**
 * Created by jmotger on 1/12/15.
 */
public class RefundsService {

    private RefundsRepository refundsRepository;

    public RefundsService(RefundsRepository refundsRepository) { this.refundsRepository = refundsRepository;}

    public List<Refund> list() {
        List<Refund> l = refundsRepository.list();
        return l;
    }

    public void newRefund(Product p, int unitats, String reason) {
        Refund r = new Refund(refundsRepository.newId(),p,unitats,reason);
        refundsRepository.insert(r);
    }
}
