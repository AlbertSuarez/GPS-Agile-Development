package edu.upc.essi.gps.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe que representa una categoria que agrupa productes.
 */
public class Category implements HasName {

    /**
     * Nom de la categoria.
     */
    private String nom;

    /**
     * Conjunt de línies de devolucions associades a la devolució
     */
    private List<Product> productes = new LinkedList<>();


    public Category(String nom) {
        this.nom = nom;
    }

    @Override
    public String getName() {
        return nom;
    }

    public void addProduct(Product p) {
        productes.add(p);
    }

    public List<Product> getProductes() {
        return productes;
    }
}
