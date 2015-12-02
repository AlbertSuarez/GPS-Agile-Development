package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.Sale;
import edu.upc.essi.gps.domain.SaleLine;

import java.util.List;

/**
 * Created by Albert on 29/11/2015.
 */
public class SalesService {

    SalesRepository salesRepository;

    public SalesService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public Sale newSale(long id, List<SaleLine> lines) {
        Sale sale = new Sale(id, lines);
        salesRepository.insert(sale);
        return sale;
    }

    public Sale findById(long id) {
        return salesRepository.findById(id);
    }

    public List<Sale> listAll() {
        return salesRepository.list();
    }

    public List<Sale> listSales () {
        return salesRepository.listSales();
    }
}
