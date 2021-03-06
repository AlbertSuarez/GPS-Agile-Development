package edu.upc.essi.gps.utils;

import edu.upc.essi.gps.domain.Entity;
import edu.upc.essi.gps.domain.HasBarCode;
import edu.upc.essi.gps.domain.HasName;

import java.util.function.Predicate;

/**
 * Classe que permet accedir a diverses implementacions de la classe <code>Matcher</code>.
 * */
public final class Matchers {

    /**
     * <code>Matcher</code> que retorna sempre <code>true</code>.
     * */
    public static Matcher<Object> all = entity -> true;

    private Matchers() {
    }

    /**
     * Obté un <code>Matcher</code> que selecciona per nom.
     * @param name nom a comparar.
     * @return un <code>Matcher</code> que selecciona per nom.
     * */
    public static Matcher<HasName> nameMatcher(final String name) {
        return entity -> entity.getName().equals(name);
    }

    /**
     * Obté un <code>Matcher</code> que selecciona per part del nom.
     * @param name part del nom a comparar.
     * @return un <code>Matcher</code> que selecciona per part del nom.
     * */
    public static Matcher<HasName> containsNameMatcher(final String name) {
        return entity -> entity.getName().contains(name);
    }

    public static Matcher<Entity> idMatcher(long id) {
        return entity -> entity.getId() == id;
    }

    public static Predicate<HasBarCode> barCode(long barCode) {
        return e -> e.getBarCode() == barCode;
    }
}



