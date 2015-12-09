package edu.upc.essi.gps.ecommerce.repositories;

import edu.upc.essi.gps.domain.Balance;
import edu.upc.essi.gps.utils.Repository;

import java.util.List;

/**
 * Created by jmotger on 25/11/15.
 */
public class BalancesRepository extends Repository<Balance> {

    public List<Balance> listByShopName(String shopName) {
        List<Balance> l = list((b) -> b.getNomBotiga().equals(shopName));
        if (l.size() == 0)
            throw new IllegalStateException("No hi ha cap desquadrament enregistrat al sistema de la botiga " + shopName);
        return l;
    }

    @Override
    protected void checkInsert(final Balance entity) throws RuntimeException {

    }
}
