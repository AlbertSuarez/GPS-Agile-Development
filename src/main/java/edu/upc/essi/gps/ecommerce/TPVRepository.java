package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.TPV;
import edu.upc.essi.gps.utils.Repository;
import edu.upc.essi.gps.utils.Validations;

public class TPVRepository extends Repository<TPV> {

    public TPV findByShopPos(final String shop, final int pos) {
        return find((r) -> r.getShop().equals(shop) && r.getPos() == pos);
    }

    @Override
    protected void checkInsert(final TPV entity) throws RuntimeException {
        Validations.checkNotNull(entity, "TPV");
        TPV tpv = findByShopPos(entity.getShop(), entity.getPos());
        if (tpv != null)
            throw new IllegalArgumentException("Ja existeix un tpv amb aquesta posició en aquesta botiga");
    }

}
