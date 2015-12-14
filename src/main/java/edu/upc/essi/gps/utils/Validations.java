package edu.upc.essi.gps.utils;

/**
 * Classe encarregada de realitzar totes les validacions del sistema.
 * */
public final class Validations {

    private Validations(){}

    /**
     * Valida si un objecte donat és null. En cas afirmatiu, llença una excepció amb una explicació adient.
     * @param value objecte a validar si és null.
     * @param name nom de la variable a validar.
     * @throws IllegalArgumentException si l'objecte donat és null.
     * */
    public static void checkNotNull(Object value, String name){
        if(value == null) throw new IllegalArgumentException(name + " must be not null");
    }

    /**
     * Valida si un password introduit coincideix amb el password emmagatzemat.
     * @param password password introduit.
     * @param cryptedPass password emmagatzemat.
     * */
    public static boolean validPassword(String password, String cryptedPass) {
        return crypt(password).equals(cryptedPass);
    }

    /**
     * Encripta un password. Tots els passwords del sistema han de ser encriptats amb aquest mètode. <br>
     * La implementació per defecte retorna el propi password sense encriptar.
     * @param password password a encriptar.
     * @return el resultat d'encriptar el password introduit.
     * */
    public static String crypt(String password) {
        return password; // #noFaRes
    }

}
