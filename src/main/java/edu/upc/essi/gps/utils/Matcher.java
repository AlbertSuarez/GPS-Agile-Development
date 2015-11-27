package edu.upc.essi.gps.utils;

/**
 * Classe que gestiona les comparacions i cerques entre objectes.
 * */
public interface Matcher<T> {

    /**
     * Consulta si l'objecte coincideix (<i>matches</i>) amb el paràmetre indicat.
     * @param t objecte a comparar.
     * @return <code>true</code> si els dos objectes coincideixen (<i>match</i>).<br>
     * <code>false</code> altrament.
     * */
    boolean matches(T t);

}

