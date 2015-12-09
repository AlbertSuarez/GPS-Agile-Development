package edu.upc.essi.gps.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe que representa una categoria que agrupa productes.
 */
public class Category implements HasName, Entity {

    /**
     * Nom de la categoria.
     */
    private String nom;

    /**
     * identificador de la categoria
     */
    private long id;

    /**
     * Conjunt de productes que pertanyen a la categoria
     */
    private List<Product> productes;


    public Category(String nom, long id) {
        this.nom = nom;
        this.id = id;
        productes = new LinkedList<>();
    }

    @Override
    public String getName() {
        return nom;
    }

    @Override
    public long getId() {
        return id;
    }

    public void addProduct(Product p) {
        productes.add(p);
    }

    public List<Product> getProductes() {
        return productes;
    }


}
