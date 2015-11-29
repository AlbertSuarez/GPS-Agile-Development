package edu.upc.essi.gps.ecommerce;

/**
 * Created by Albert on 29/11/2015.
 */
public class SalesService {

    SalesRepository salesRepository;

    public SalesService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

}
