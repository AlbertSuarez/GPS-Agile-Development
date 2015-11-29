package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.Sale;
import edu.upc.essi.gps.utils.Repository;
import java.util.List;

/**
 * Created by Albert on 29/11/2015.
 */
public class SalesRepository extends Repository<Sale> {

    public List<Sale> listRefunds() {
        List<Sale> l = list((s) -> s.getTotal() < 0);
        if (l.size() == 0) {
            throw new IllegalStateException("No hi ha cap devolució enregistrada al sistema");
        }
        return l;
    }

    public List<Sale> listSales() {
        List<Sale> l = list((s) -> s.getTotal() > 0);
        if (l.size() == 0) {
            throw new IllegalStateException("No hi ha cap venda enregistrada al sistema");
        }
        return l;
    }

    @Override
    protected void checkInsert(final Sale sale) throws RuntimeException {

    }
}
