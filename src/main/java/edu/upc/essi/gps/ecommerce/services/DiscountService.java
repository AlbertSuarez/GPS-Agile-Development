package edu.upc.essi.gps.ecommerce.services;

import edu.upc.essi.gps.domain.Category;
import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.domain.discounts.Discount;
import edu.upc.essi.gps.domain.discounts.ProductPercent;
import edu.upc.essi.gps.domain.discounts.ProductPresent;
import edu.upc.essi.gps.domain.discounts.ProductPromotion;
import edu.upc.essi.gps.ecommerce.repositories.DiscountRepository;

import java.util.List;

public class DiscountService {

    private DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public long newProductPercentDiscount(Product product, String name, double percent) {
        long id = discountRepository.newId();
        Discount discount = new ProductPercent(product, name, id, percent);
        discountRepository.insert(discount);
        return id;
    }

    public long newProductPercentDiscount(Category category, String name, double percent) {
        long id = discountRepository.newId();
        Discount discount = new ProductPercent(category, name, id, percent);
        discountRepository.insert(discount);
        return id;
    }

    public long newProductPercentDiscount(List<Product> products, String name, double percent) {
        long id = discountRepository.newId();
        Discount discount = new ProductPercent(products, name, id, percent);
        discountRepository.insert(discount);
        return id;
    }

    public long newProductPromotionDiscount(Product product, String name, int A, int B) {
        long id = discountRepository.newId();
        Discount discount = new ProductPromotion(product, name, id, A, B);
        discountRepository.insert(discount);
        return id;
    }

    public long newProductPromotionDiscount(Category category, String name, int A, int B) {
        long id = discountRepository.newId();
        Discount discount = new ProductPromotion(category, name, id, A, B);
        discountRepository.insert(discount);
        return id;
    }

    public long newProductPromotionDiscount(List<Product> products, String name, int A, int B) {
        long id = discountRepository.newId();
        Discount discount = new ProductPromotion(products, name, id, A, B);
        discountRepository.insert(discount);
        return id;
    }

    public long newProductPresentDiscount(Product present, String name, Product required) {
        long id = discountRepository.newId();
        Discount discount = new ProductPresent(present, name, id, required);
        discountRepository.insert(discount);
        return id;
    }

    public long newProductPresentDiscount(Category presentsCategory, String name, Category requiredCategory) {
        long id = discountRepository.newId();
        Discount discount = new ProductPresent(presentsCategory, name, id, requiredCategory);
        discountRepository.insert(discount);
        return id;
    }

    public long newProductPresentDiscount(List<Product> presents, String name, List<Product> required) {
        long id = discountRepository.newId();
        Discount discount = new ProductPresent(presents, name, id, required);
        discountRepository.insert(discount);
        return id;
    }


    public List<Discount> list(){
        return discountRepository.list();
    }

    public Discount findByName(String productName) {
        return discountRepository.findByName(productName);
    }

    public Discount findById(long id) {
        return discountRepository.findById(id);
    }

    public List<Discount> listByTriggerId(long id) {
        return discountRepository.listByProductId(id);
    }

}
