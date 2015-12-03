package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.domain.Sale;
import edu.upc.essi.gps.domain.SaleLine;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Albert on 29/11/2015.
 */
public class SalesService {

    SalesRepository salesRepository;

    public SalesService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public Sale newSale(){
        Sale sale = new Sale();
        return sale;
    }

    public void newSale(Product product, String type){
        List<SaleLine> saleLineList = new LinkedList<>();
        saleLineList.add(new SaleLine(product, 1));
        int id = 0;
        Sale sale = new Sale(id, saleLineList);
        sale.setTipusPagament(type);
        salesRepository.insert(sale);
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

    public List<Sale> listByTipus(String tipus) {
        return salesRepository.listByTipus(tipus);
    }

    public void insertSale(Sale currentSale) {
        salesRepository.insert(currentSale);
    }
}
