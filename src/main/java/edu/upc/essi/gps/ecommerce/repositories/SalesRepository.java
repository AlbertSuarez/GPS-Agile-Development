package edu.upc.essi.gps.ecommerce.repositories;

import edu.upc.essi.gps.domain.Sale;
import edu.upc.essi.gps.utils.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Albert on 29/11/2015.
 */
public class SalesRepository extends Repository<Sale> {

    public List<Sale> listSales() {
        List<Sale> l = list();
        if (l.size() == 0) {
            throw new IllegalStateException("No hi ha cap venda enregistrada al sistema");
        }
        return l;
    }

    @Override
    protected void checkInsert(final Sale sale) throws RuntimeException {
        if (findById(sale.getId()) != null) {
            throw new IllegalArgumentException("Ja existeix una venda amb aquest identificador");
        }
    }

    public List<Sale> listByTipus(final String tipus) {
        return list((s) -> s.getTipusPagament() == tipus);
    }

    public List<Sale> listByDate(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        return list((s) -> (s.getDay() == day) &&  (s.getMonth() == month) && (s.getYear() == year));
    }

    public List<Sale> listPeriod(Date d1, Date d2) {
        Calendar c = Calendar.getInstance();
        c.setTime(d1);
        int day1 = c.get(Calendar.DATE);
        int month1 = c.get(Calendar.MONTH);
        int year1 = c.get(Calendar.YEAR);
        c.setTime(d2);
        int day2 = c.get(Calendar.DATE);
        int month2 = c.get(Calendar.MONTH);
        int year2 = c.get(Calendar.YEAR);

        return list((s) -> (s.getDay() >= day1) &&  (s.getMonth() >= month1) && (s.getYear() >= year1) && (s.getDay() <= day2) &&  (s.getMonth() <= month2) && (s.getYear() <= year2));
    }
}
