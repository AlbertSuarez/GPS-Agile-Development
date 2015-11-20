package edu.upc.essi.gps.utils;

public final class Validations {
    private Validations(){}

    public static void checkNotNull(Object value, String name){
        if(value == null) throw new IllegalArgumentException(name + " must be not null");
    }
}
