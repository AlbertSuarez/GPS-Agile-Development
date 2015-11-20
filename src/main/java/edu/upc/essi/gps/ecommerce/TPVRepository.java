package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.TPV;
import edu.upc.essi.gps.utils.Repository;

public class TPVRepository extends Repository<TPV> {

    public TPV findById(final long id){
        return find((r) -> r.getId() == id);
    }

    public TPV findByShopPos(final String shop, final int pos) {
        return find((r) -> r.getShop().equals(shop) && r.getPos() == pos);
    }

    @Override
    protected void checkInsert(final TPV entity) throws RuntimeException {
        TPV tpv = findById(entity.getId());
        if(tpv != null && tpv.getPos() == entity.getPos() && tpv.getShop().equals(entity.getShop()))
            throw new IllegalArgumentException("Ja existeix un tpv amb aquesta posició en aquesta botiga");
    }

}
