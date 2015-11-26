package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.Balance;

import java.util.List;

/**
 * Created by jmotger on 26/11/15.
 */
public class ProductManagerController {

    BalancesService balancesService;

    public ProductManagerController(BalancesService balancesService) {
        this.balancesService = balancesService;
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

}
