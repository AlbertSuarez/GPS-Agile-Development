package edu.upc.essi.gps.ecommerce.services;

import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.domain.Sale;
import edu.upc.essi.gps.domain.lines.SaleLine;
import edu.upc.essi.gps.ecommerce.repositories.SalesRepository;

import java.util.Date;
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

    public Sale newSale(long id,Product product, Date data, String tipusPagament){
        List<SaleLine> saleLineList = new LinkedList<>();
        saleLineList.add(new SaleLine(product, 1));
        Sale sale = new Sale(id, saleLineList, data);

        sale.setTipusPagament(tipusPagament);
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

    public List<Sale> listSalesByDate(Date d) {
        return salesRepository.listByDate(d);
    }

    public List<Sale> listSalesPeriod(Date d1, Date d2) {
        return salesRepository.listPeriod(d1,d2);
    }


    public Sale newSale(int id, Date d, String pagament) {
        List<SaleLine> saleLineList = new LinkedList<>();
        Sale sale = new Sale(id,saleLineList, d);
        sale.setTipusPagament(pagament);
        salesRepository.insert(sale);
        return sale;
    }
}
