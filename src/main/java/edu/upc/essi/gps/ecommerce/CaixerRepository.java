package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.utils.Matchers;
import edu.upc.essi.gps.utils.Repository;

public class CaixerRepository extends Repository<Caixer> {

    private static long lastId;

    public Caixer findByName(final String name) {
        return find(Matchers.nameMatcher(name));
    }

    public Caixer findById(final long id){
        return find((c) -> c.getId() == id);
    }

    public static long getNewId() {
        return ++lastId;
    }

    @Override
    protected void checkInsert(final Caixer entity) throws RuntimeException {
        if(findById(entity.getId()) != null)
            throw new IllegalArgumentException("Ja existeix un caixer amb aquest id");
    }

}
