package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.*;
import edu.upc.essi.gps.domain.flow.MoneyFlow;
import edu.upc.essi.gps.domain.lines.SaleLine;
import edu.upc.essi.gps.ecommerce.services.*;

import java.util.List;

/**
 * ${NAME} in ${PACKAGE_NAME}
 * @author jmotger
 * @version 1.0
 * Creation Date: 26/11/15
 */
public class ProductManagerController {

    private final TPVService tpvService;
    private final SalesService salesService;
    private final RefundsService refundsService;
    private final BalancesService balancesService;
    private final MoneyFlowService moneyFlowService;
    private final ProductsService productsService;
    private final SaleAssistantService saleAssistantService;
    private List<Sale> sales;
    private List<SaleAssistant> caixers;

    public ProductManagerController(MoneyFlowService moneyFlowService, BalancesService balancesService, TPVService tpvService, SalesService salesService, RefundsService refundsService, ProductsService productsService, SaleAssistantService saleAssistantService) {
        this.balancesService = balancesService;
        this.salesService = salesService;
        this.tpvService = tpvService;
        this.refundsService = refundsService;
        this.moneyFlowService = moneyFlowService;
        this.productsService = productsService;
        this.saleAssistantService = saleAssistantService;
    }

    /**
     * Obté els desquadraments de tot el sistema
     * @return llista amb els desquadraments
     */
    public List<Balance> listBalances() {
        return balancesService.list();
    }

    /**
     * Obté els desquadraments de tot el sistema
     *
     * @return llista amb els desquadraments
     */
    public List<Refund> listRefunds() {
        List<Refund> refundList = refundsService.list();
        if (refundList.size() == 0)
            throw new IllegalStateException("No hi ha cap devolució enregistrada al sistema");
        return refundList;
    }

    /**
     * Obté els desquadraments per a la botiga especificada
     * @param name  shopName
     * @return  llista amb els desquadraments per la botiga amb nom shopName
     */
    public List<Balance> listBalancesByShopName(String name) {
        return balancesService.listByShopName(name);
    }

    public List<Sale> listSales() {
        sales = salesService.listSales();
        return sales;
    }

    public List<MoneyFlow> listMoneyFlows() {
        return moneyFlowService.list();
    }

    public List<MoneyFlow> listMoneyFlowsByKind(String flowKind) {
        return moneyFlowService.listByKind(flowKind);
    }

    public void setMasterPassword(String password) {
        tpvService.setMasterPass(password);
    }

    public List<Sale> llistaVentesPerTipusPagament(String tipus) {
        return salesService.listByTipus(tipus);
    }

    //TODO: Acabar métode en cas de que sigui necessari
    public String getScreenSales(){
        StringBuilder sb = new StringBuilder();
        for(Sale sale:sales){
            sb.append("Linies de venda");
            sb.append(sale.getId());
            for(SaleLine s:sale.getLines()){
                sb.append(s.getName())
                        .append(" - ")
                        .append(s.getUnitPrice())
                        .append("€/u x ")
                        .append(s.getAmount())
                        .append("u = ")
                        .append(s.getTotalPrice())
                        .append("€")
                        .append(System.lineSeparator());
            }
            sb.append("---")
                    .append(System.lineSeparator())
                    .append("Total: ")
                    .append(sale.getTotal())
                    .append("€")
                    .append("Mètode pagament: ")
                    .append(sale.getTipusPagament());
                    //.append()

        }
        return sb.toString();

    }

    public List<Product> addNewProduct(String name, double price, double vatPct, int barCode) {
        if (productsService.findByBarCode(barCode) != null)
            throw new IllegalStateException("Ja existeix un producte amb aquest codi de barres");
        productsService.newProduct(name, price, vatPct, barCode);
        return productsService.list();
    }

    public List<SaleAssistant> newSaleAssistant(String name, String pass) {
        if (saleAssistantService.findByName(name) != null)
            throw new IllegalStateException("Ja existeix un caixer amb aquest nom");
        saleAssistantService.newAssistant(name, pass);
        return saleAssistantService.list();
    }

    public List<Product> getProducts() {
        List<Product> l =  productsService.list();
        if (l.size() == 0)
            throw new IllegalStateException("No hi ha productes donats d'alta al sistema");
        return l;
    }

    public List<SaleAssistant> getSaleAssistants() {
        List<SaleAssistant> l =  saleAssistantService.list();
        if (l.size() == 0)
            throw new IllegalStateException("No hi ha caixers donats d'alta al sistema");
        return l;
    }

}
