package edu.upc.essi.gps.utils;

import edu.upc.essi.gps.domain.Entity;
import edu.upc.essi.gps.domain.HasName;

import java.util.Comparator;

/**
 * Classe que implementa el conjunt de comparadors utilitzats al sistema
 * */
public final class Comparators {

    private Comparators() {
    }

    /**
     * Comparador que relitza una cerca per nom entre objectes de la classe <code>HasName</code>
     * */
    public static Comparator<HasName> byName = (o1, o2) -> o1.getName().compareTo(o2.getName());

    /**
     * Comparador que relitza una cerca per id entre objectes de la classe <code>Entity</code>
     * */
    public static Comparator<Entity> byId = (o1, o2) -> new Long(o1.getId()).compareTo(o2.getId());
}
