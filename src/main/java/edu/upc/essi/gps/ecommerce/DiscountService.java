package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.*;

import java.util.List;

public class DiscountService {

    private DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public long newDiscount(String discountType, Product product, String name, Object... params){
        Discount discount;
        long id;
        switch (discountType) {
            case Promotion.TYPE_NAME:
                int A;
                int B;
                try {
                    A = (int) params[0];
                    B = (int) params[1];
                } catch (ClassCastException e) {
                    throw new IllegalArgumentException("Paràmetres incorrectes: S'espera "
                            + Integer.class.getSimpleName() + ", "
                            + Integer.class.getSimpleName()
                    );
                }
                id = discountRepository.newId();
                discount = new Promotion(product, name, id, A, B);
                break;
            case Percent.TYPE_NAME:
                double percent;
                try {
                    percent = (double) params[0];
                } catch (ClassCastException e) {
                    throw new IllegalArgumentException("Paràmetres incorrectes: S'espera "
                            + Double.class.getSimpleName()
                    );
                }
                id = discountRepository.newId();
                discount = new Percent(product, name, id, percent);
                break;
            case Present.TYPE_NAME:
                Product required;
                try {
                    required = (Product) params[0];
                } catch (ClassCastException e) {
                    throw new IllegalArgumentException("Paràmetres incorrectes: S'espera "
                            + Product.class.getSimpleName()
                    );
                }
                id = discountRepository.newId();
                discount = new Present(product, name, id, required);
                break;
            default:
                throw new IllegalArgumentException("El tipus de descompte indicat no existeix");
        }

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
        return discountRepository.findByTriggerId(id);
    }

}
