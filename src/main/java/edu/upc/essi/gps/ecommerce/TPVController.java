package edu.upc.essi.gps.ecommerce;

import com.sun.istack.internal.NotNull;
import edu.upc.essi.gps.domain.*;

import java.util.List;

import static edu.upc.essi.gps.utils.Validations.checkNotNull;

public class TPVController {

    public static final String ADD_NON_EXISTING_PRODUCT_ERROR = "No es pot afegir un producte inexistent";

    private final ProductsService productsService;
    private final SaleAssistantService saleAssistantService;
    private final TPVService tpvService;
    private final BalancesService balancesService;
    private final TPV tpv;

    public TPVController(ProductsService productsService, SaleAssistantService saleAssistantService, TPVService tpvService, BalancesService balancesService, String shop, int pos) {
        this.productsService = productsService;
        this.saleAssistantService = saleAssistantService;
        this.tpvService = tpvService;
        this.balancesService = balancesService;
        tpv = tpvService.findByShopPos(shop, pos);
        tpv.endTurn();
    }

    public TPV getTpv() {
        return tpv;
    }

    public SaleAssistant getCurrentSaleAssistant() {
        return tpv.getCurrentSaleAssistant();
    }

    public Sale getCurrentSale() {
        return tpv.getCurrentSale();
    }

    public boolean login(long saleAssistantId, @NotNull String password, double cash) {
        if (tpv.getState().equals(TPVState.IDLE))
            throw new IllegalStateException("Aquest tpv està en ús per " + tpv.getCurrentSaleAssistant().getName());
        if (tpv.getState().equals(TPVState.BLOCKED)) throw new IllegalStateException("Aquest tpv està bloquejat");

        checkNotNull(password, "password");

        boolean loggedIn = saleAssistantService.validate(saleAssistantId, password);
        tpvService.validation(tpv.getId(), loggedIn);
        if (loggedIn) {
            tpv.newTurn(saleAssistantService.findById(saleAssistantId), cash);
        }
        return loggedIn;
    }

    public void unblock(@NotNull String password) {
        if (!tpv.getState().equals(TPVState.BLOCKED)) throw new IllegalStateException("Aquest tpv no està bloquejat");

        checkNotNull(password, "password");

        if (!tpvService.validateAdmin(tpv.getId(), password))
            throw new IllegalStateException("Password d'administrador incorrecte");
    }

    public void quadra(double cash){

        if (cash >= tpv.getCash()) {
            tpv.endTurn();
        }
        else {
           throw new IllegalStateException("La caixa no quadra: hi ha un desquadrament de " + (tpv.getCash() - cash) + "€");
        }

    }

    public void addDesquadrament(double cash) {
        balancesService.newBalance(tpv.getCash() - cash, tpv.getCurrentSaleAssistant().getName(), tpv.getShop());
        tpv.endTurn();
    }

    public void startSale() {
        if (tpv.hasSale())
            throw new IllegalStateException("Aquest tpv ja té una venta iniciada");
        tpv.newSale();
    }

    public boolean isSaleStarted() {
        return tpv.hasSale();
    }

    public void endSale() {
        if (!tpv.hasSale())
            throw new IllegalStateException("Aquest tpv no té una venta iniciada");
        tpv.endSale();
    }

    public void addProductByBarCode(int barCode) {
        addProductByBarCode(barCode, 1);
    }

    public void addNewDiscountToCurrentSale(int prodLine, String name, double percent) {
        if (!isSaleStarted()) throw new IllegalStateException("No hi ha cap venda iniciada");
        int max = getCurrentSale().getLines().size();
        if (prodLine-1 < 0 || prodLine-1 >= max)
            throw new IndexOutOfBoundsException("No es pot accedir a la línia " + prodLine +
                    " de la venta, aquesta només té " + max + " línies");
        Product product = productsService.findById(tpv.getCurrentSale().getId(prodLine-1));
        Discount discount = new Percent(product, name, -1, -1, percent);
        getCurrentSale().addDiscount(discount, prodLine);
    }

    public String getCustomerScreenMessage() {
        String welcomeMessage = "Li donem la benvinguda a Joguets i Joguines!";
        if (!tpv.hasSale()) return welcomeMessage;
        if (tpv.getCurrentSale().isEmpty()) {
            return welcomeMessage + System.lineSeparator() + "L'atén " + tpv.getCurrentSaleAssistant().getName();
        }
        StringBuilder sb = new StringBuilder();
        for (Sale.SaleLine sl : tpv.getCurrentSale().getLines()) {
            sb.append(sl.getName()).append(" - ")
                    .append(sl.getUnitPrice()).append("€/u x ").append(sl.getAmount()).append("u = ")
                    .append(sl.getTotalPrice()).append("€" + System.lineSeparator());
        }
        sb.append("---" + System.lineSeparator()).append("Total: ").append(tpv.getCurrentSale().getTotal()).append("€");
        return sb.toString();
    }

    public double cashPayment(double delivered) {
        if(!tpv.hasSale())
            throw new IllegalStateException("No es pot cobrar una venta si no està iniciada");
        if(tpv.getCurrentSale().isEmpty())
            throw new IllegalStateException("No es pot cobrar una venta sense cap producte");
        tpv.addCash(getCurrentSale().getTotal());
        return getCanvi(delivered);
    }

    private double getCanvi(double delivered) {
        double total = tpv.getCurrentSale().getTotal();
        if(total > delivered)
            throw new IllegalStateException("No es pot cobrar una venta amb un import inferior al total de la venta");
        return delivered-total;
    }

    public double tarjetPayment(double delivered) {
        if(!tpv.hasSale())
            throw new IllegalStateException("No es pot cobrar una venta si no està iniciada");
        if(tpv.getCurrentSale().isEmpty())
            throw new IllegalStateException("No es pot cobrar una venta sense cap producte");
        return delivered-tpv.getCurrentSale().getTotal();
    }

    public void addProductByBarCode(int barCode, int unitats) {
        Product p = productsService.findByBarCode(barCode);
        if (p == null) {
            throw new IllegalStateException(ADD_NON_EXISTING_PRODUCT_ERROR);
        }
        tpv.addProduct(p, unitats);
    }

    public List<Product> addProductByName(String nom) {
        return addProductByName(nom, 1);
    }

    /**
     * Add product to sale by name
     *
     * @param nomProducte product name
     * @param unitatsProducte units added
     * @return products that match the name introduced
     */
    public List<Product> addProductByName(String nomProducte, int unitatsProducte) {
        List<Product> products = productsService.findByName(nomProducte);
        if (products.isEmpty()) {
            throw new IllegalStateException(ADD_NON_EXISTING_PRODUCT_ERROR);
        }
        if (products.size() == 1) {
            tpv.addProduct(products.get(0), unitatsProducte);
        }
        return products;
    }
}
