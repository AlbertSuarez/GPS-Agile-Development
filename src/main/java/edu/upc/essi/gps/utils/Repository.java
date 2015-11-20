package edu.upc.essi.gps.utils;

import edu.upc.essi.gps.domain.Entity;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Repository<T extends Entity> {

    private List<T> entities = new LinkedList<>();
    private long nextId = 1l;

    public long newId() {
        return nextId++;
    }

    protected abstract void checkInsert(T entity) throws RuntimeException;

    public void insert(T entity) {
        checkInsert(entity);
        entities.add(entity);
    }

    public List<T> list() {
        return list(Matchers.all, Comparators.byId);
    }

    public List<T> list(Comparator<? super T> sortedBy) {
        return list(Matchers.all, sortedBy);
    }

    public List<T> list(Matcher<? super T> matcher) {
        return list(matcher, Comparators.byId);
    }

    public List<T> list(Matcher<? super T> matcher, Comparator<? super T> sortedBy) {
        List<T> result = entities.stream().filter(matcher::matches).collect(Collectors.toCollection(LinkedList::new));
        result.sort(sortedBy);
        return Collections.unmodifiableList(result);
    }

    public T find(Matcher<? super T> matcher) {
        for (T entity : entities) {
            if (matcher.matches(entity)) return entity;
        }
        return null;
    }


}
