package edu.upc.essi.gps.utils;

import com.sun.istack.internal.NotNull;
import edu.upc.essi.gps.domain.Entity;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe abstracta encarregada d'emmagatzemar objectes identificats per id.
 * */
public abstract class Repository<T extends Entity> {

    /**
     * Llista interna encarregada d'emmagatzemar el contingut del repositori.
     * */
    private List<T> entities = new LinkedList<>();

    /**
     * Comptador d'assignació d'IDs, utilitzat per tal de que cap objecte tingui id repetit.
     * */
    private long nextId = 1l;

    /**
     * Retorna un identificador que no existeix al nostre sistema.
     * @return un identificador preparat per a ser assignat a un nou objecte.
     * */
    public long newId() {
        return nextId++;
    }

    /**
     * Valida que un objecte pugui ser inserit al repositori.
     * @param t objecte que es vol insertar.
     * @throws RuntimeException si l'objecte no pot ser inserit al repositori.
     * */
    protected abstract void checkInsert(@NotNull T t) throws RuntimeException;

    /**
     * Insereix un objecte al repositori.
     * @param t objecte que es vol insertar.
     * */
    public void insert(T t) {
        checkIdDuplicate(t);
        checkInsert(t);
        entities.add(t);
    }

    private void checkIdDuplicate(T t) {
        if (findById(t.getId()) != null) {
            throw new IllegalArgumentException("Ja existeix un " + t.getClass().getSimpleName() + " amb aquest id");
        }
    }

    /**
     * Consulta el conjunt d'objectes del repositori.
     * @return una <code>List</code> que no es pot modificar amb el contingut del repositori.
     * */
    public List<T> list() {
        return list(Matchers.all, Comparators.byId);
    }

    /**
     * Consulta el conjunt d'objectes del repositori.
     * @param sortedBy <code>Comparator</code> que indica la relació d'ordre entre els objectes.
     * @return una <code>List</code> ordenada que no es pot modificar amb el contingut del repositori.
     * */
    public List<T> list(Comparator<? super T> sortedBy) {
        return list(Matchers.all, sortedBy);
    }

    /**
     * Consulta el conjunt d'objectes del repositori.
     * @param matcher <code>Matcher</code> que indica el criteri amb el que es selecciona un objecte del repositori.
     * @return una <code>List</code> que no es pot modificar amb el contingut del resultat de la cerca.
     * */
    public List<T> list(Matcher<? super T> matcher) {
        return list(matcher, Comparators.byId);
    }

    /**
     * Consulta el conjunt d'objectes del repositori.
     * @param matcher <code>Matcher</code> que indica el criteri amb el que es selecciona un objecte del repositori.
     * @param sortedBy <code>Comparator</code> que indica la relació d'ordre entre els objectes.
     * @return una <code>List</code> ordenada que no es pot modificar amb el contingut del resultat de la cerca.
     * */
    public List<T> list(Matcher<? super T> matcher, Comparator<? super T> sortedBy) {
        List<T> result = entities
                .stream()
                .filter(matcher::matches)
                .collect(Collectors.toCollection(LinkedList::new));
        result.sort(sortedBy);
        return Collections.unmodifiableList(result);
    }

    /**
     * Cerca un objecte del repositori.
     * @param matcher <code>Matcher</code> que indica el criteri amb el que es selecciona un objecte del repositori.
     * @return el primer objecte del repositori que coincideixi (<i>match</i>) amb l'objecte donat.<br>
     *     Retorna <code>null</code> si no en troba cap.
     * */
    public T find(Matcher<? super T> matcher) {
        for (T entity : entities) {
            if (matcher.matches(entity)) return entity;
        }
        return null;
    }

    public T findById(long id) {
        return find(Matchers.idMatcher(id));
    }

}
