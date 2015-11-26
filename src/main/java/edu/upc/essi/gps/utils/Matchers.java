package edu.upc.essi.gps.utils;

import edu.upc.essi.gps.domain.HasName;

public final class Matchers {

    public static Matcher<Object> all = entity -> true;

    private Matchers() {
    }

    public static Matcher<HasName> nameMatcher(final String name) {
        return entity -> entity.getName().equals(name);
    }

    public static Matcher<HasName> containsNameMatcher(final String name) {
        return entity -> entity.getName().contains(name);
    }

}



