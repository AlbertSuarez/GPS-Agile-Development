package edu.upc.essi.gps.utils;

import edu.upc.essi.gps.domain.HasName;

public final class Matchers {

    private Matchers() {
    }

    public static Matcher<HasName> nameMatcher(final String name) {
        return named -> named.getName().equals(name);
    }

    public static Matcher<Object> all = entity -> true;

}



