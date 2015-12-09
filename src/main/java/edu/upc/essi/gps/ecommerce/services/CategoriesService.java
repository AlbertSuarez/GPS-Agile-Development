package edu.upc.essi.gps.ecommerce.services;


import edu.upc.essi.gps.domain.Category;
import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.ecommerce.repositories.CategoriesRepository;

import java.util.List;

public class CategoriesService {

    private CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public long newCategory(String name) {
        long id = categoriesRepository.newId();
        Category result = new Category(name, id);
        categoriesRepository.insert(result);
        return id;
    }

    public List<Category> list() {
        return categoriesRepository.list();
    }

    public Category findByName(String categoryName) {
        return categoriesRepository.findByName(categoryName);
    }

    public void addProductToCategory(Product product, String catName) {
        Category c = findByName(catName);
        c.addProduct(product);
    }

    public List<Product> listProductsByCategory(String catName) {
        Category c = findByName(catName);
        return c.getProductes();
    }


    public Category findById(long catId) {
        return categoriesRepository.findById(catId);
    }

}

