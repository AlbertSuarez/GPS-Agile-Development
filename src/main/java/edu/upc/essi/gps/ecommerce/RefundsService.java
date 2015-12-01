package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.Refund;
import edu.upc.essi.gps.domain.SaleLine;

import java.util.List;

/**
 * Created by jmotger on 1/12/15.
 */
public class RefundsService {

    private RefundsRepository refundsRepository;

    public RefundsService(RefundsRepository refundsRepository) { this.refundsRepository = refundsRepository;}

    public List<Refund> list() {
        List<Refund> l = refundsRepository.list();
        if (l.size() == 0)
            throw new IllegalStateException("No hi ha cap devolució enregistrada al sistema");
        return l;
    }

    public long newRefund(List<SaleLine> refunds) {
        long id = refundsRepository.newId();
        Refund refund = new Refund(id, refunds);
        refundsRepository.insert(refund);
        return id;
    }

}
