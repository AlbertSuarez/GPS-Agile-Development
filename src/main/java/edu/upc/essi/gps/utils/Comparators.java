package edu.upc.essi.gps.utils;

import edu.upc.essi.gps.domain.Entity;
import edu.upc.essi.gps.domain.HasName;

import java.util.Comparator;

public final class Comparators {

    private Comparators() {
    }

    public static Comparator<HasName> byName = (o1, o2) -> o1.getName().compareTo(o2.getName());


    public static Comparator<Entity> byId = (o1, o2) -> new Long(o1.getId()).compareTo(o2.getId());
}
