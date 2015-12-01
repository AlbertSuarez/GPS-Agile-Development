package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.Balance;
import edu.upc.essi.gps.domain.Sale;

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
    private final BalancesService balancesService;
    private final RefundsService refundsService;

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
        return salesService.listSales();
    }

    public void setMasterPassword(String password) {
        tpvService.setMasterPass(password);
    }

}
