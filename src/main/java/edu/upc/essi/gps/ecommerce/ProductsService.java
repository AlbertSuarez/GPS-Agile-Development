package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.utils.Comparators;

import java.util.List;

public class ProductsService {

    private ProductsRepository productsRepository;

    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public long newProduct(String name, double price, int vatPct, int barCode){
        long id = productsRepository.newId();
        Product result = new Product(id,name, price, vatPct, barCode);
        productsRepository.insert(result);
        return id;
    }

    public List<Product> list(){
        return productsRepository.list();
    }

    public List<Product> listByName(){
        return productsRepository.list(Comparators.byName);
    }

    public List<Product> findByName(String productName) {
        return productsRepository.lookForName(productName);
    }

    public Product findByBarCode(int barCode) {
        return productsRepository.findByBarCode(barCode);
    }

    public Product findById(long prodId) {
        return productsRepository.findById(prodId);
    }

}
