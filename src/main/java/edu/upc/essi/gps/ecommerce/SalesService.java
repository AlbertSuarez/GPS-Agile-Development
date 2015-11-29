package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.Sale;
import java.util.List;

/**
 * Created by Albert on 29/11/2015.
 */
public class SalesService {

    SalesRepository salesRepository;

    public SalesService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public Sale newSale(List<Sale.SaleLine> lines) {
        long id = salesRepository.newId();
        Sale sale = new Sale(id, lines);
        salesRepository.insert(sale);
        return sale;
    }

    public List<Sale> listAll() {
        return salesRepository.list();
    }

    public List<Sale> listRefunds () {
        return salesRepository.listRefunds();
    }

    public List<Sale> listSales () {
        return salesRepository.listSales();
    }
}
