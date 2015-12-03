package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.Balance;
import edu.upc.essi.gps.domain.Refund;
import edu.upc.essi.gps.domain.Sale;
import edu.upc.essi.gps.domain.SaleLine;

import java.sql.Ref;
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
    private List<Sale> sales;

    public ProductManagerController(BalancesService balancesService, TPVService tpvService, SalesService salesService, RefundsService refundsService) {
        this.balancesService = balancesService;
        this.salesService = salesService;
        this.tpvService = tpvService;
        this.refundsService = refundsService;
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
        return refundsService.list();
    }

    /**
     * Obté els desquadraments per a la botiga especificada
     * @param name  shopName
     * @return  llista amb els desquadraments per la botiga amb nom shopName
     */
    public List<Balance> listBalancesByShopName(String name) {
        return balancesService.listByShopName(name);
    }

    public List<Sale> listAll() {
        return salesService.listAll();
    }

    public List<Sale> listSales() {
        sales = salesService.listSales();
        return sales;
    }

    public void setMasterPassword(String password) {
        tpvService.setMasterPass(password);
    }

    public List<Sale> llistaVentesPerTipusPagament(String tipus) {
        return salesService.listByTipus(tipus);
    }

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
}
