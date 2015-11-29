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
    private final DiscountService discountService;

    private final TPV tpv;

    public TPVController(ProductsService productsService, SaleAssistantService saleAssistantService, TPVService tpvService, BalancesService balancesService, DiscountService discountService, String shop, int pos) {
        this.productsService = productsService;
        this.saleAssistantService = saleAssistantService;
        this.tpvService = tpvService;
        this.balancesService = balancesService;
        this.discountService = discountService;
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

    public void login(long saleAssistantId, @NotNull String password, double cash) {
        if (tpv.getState().equals(TPVState.IDLE))
            throw new IllegalStateException("Aquest tpv està en ús per " + tpv.getCurrentSaleAssistant().getName());
        if (tpv.getState().equals(TPVState.BLOCKED))
            throw new IllegalStateException("Aquest tpv està bloquejat");

        checkNotNull(password, "password");

        try {
            saleAssistantService.validate(saleAssistantId, password);
        } catch (IllegalStateException e) {
            tpvService.failLogin(tpv.getId());
            throw e;
        }

        tpvService.successLogin(tpv.getId());
        tpv.newTurn(saleAssistantService.findById(saleAssistantId), cash);
    }

    public void unblock(@NotNull String password) {
        if (!tpv.getState().equals(TPVState.BLOCKED)) throw new IllegalStateException("Aquest tpv no està bloquejat");

        checkNotNull(password, "password");

        if (!tpvService.validateAdmin(tpv.getId(), password))
            throw new IllegalStateException("Password d'administrador incorrecte");
    }

    public void iniciaQuadrament() {
        if (!tpv.getState().equals(TPVState.IDLE)) throw new IllegalStateException("Aquest tpv no està en un torn actualment");
        if (tpv.getCurrentSale() != null) throw new IllegalStateException("Actualment hi ha una venta iniciada");
        tpv.setState(TPVState.BALANCE);
    }

    public void quadra(double cash){
        if (!tpv.getState().equals(TPVState.BALANCE)) throw new IllegalStateException("La caixa no es troba actualment en procés de quadrament");
        if (cash >= tpv.getCash()) {
            tpv.endTurn();
        }
        else {
           throw new IllegalStateException("La caixa no quadra: hi ha un desquadrament de " + (tpv.getCash() - cash) + "€");
        }

    }

    public Balance addDesquadrament(double cash) {
        Balance b = balancesService.newBalance(tpv.getCash() - cash, tpv.getCurrentSaleAssistant().getName(), tpv.getShop());
        tpv.endTurn();
        return b;
    }

    public void startSale() {
        if (tpv.hasSale())
            throw new IllegalStateException("Aquest tpv ja té una venta iniciada");
        tpv.newSale();
    }

    public boolean isSaleStarted() {
        return tpv.hasSale();
    }

    public void addProductByBarCode(int barCode, boolean refund) {
        addProductByBarCode(barCode, 1, refund);
    }

    public void addNewDiscountToCurrentSale(int prodLine, @NotNull String name, double percent) {
        if (!isSaleStarted())
            throw new IllegalStateException("No hi ha cap venda iniciada");
        int max = getCurrentSale().getLines().size();
        if (prodLine-1 < 0 || prodLine-1 >= max)
            throw new IndexOutOfBoundsException("No es pot accedir a la línia " + prodLine +
                    " de la venta, aquesta només té " + max + " línies");
        Product product = productsService.findById(tpv.getCurrentSale().getId(prodLine-1));
        Discount discount = new Percent(product, name, -1, percent);
        getCurrentSale().addManualDiscount(discount, prodLine);
    }

    public void newDiscountPercent(String name, long barCode, double percent) {
        Product product = productsService.findByBarCode(barCode);
        discountService.newDiscount(Percent.TYPE_NAME, product, name, percent);
    }

    public void newDiscountPromotion(String name, long barCode, int A, int B) {
        Product product = productsService.findByBarCode(barCode);
        discountService.newDiscount(Promotion.TYPE_NAME, product, name, A, B);
    }

    public void newDiscountPresent(String name, long barCodeA, long barCodeB) {
        Product productA = productsService.findByBarCode(barCodeA);
        Product productB = productsService.findByBarCode(barCodeB);
        discountService.newDiscount(Present.TYPE_NAME, productA, name, productB);
    }

    public String getCustomerScreenMessage() {
        String welcomeMessage = "Li donem la benvinguda a Joguets i Joguines!";
        if (!tpv.hasSale()) return welcomeMessage;
        if (tpv.getCurrentSale().isEmpty()) {
            return welcomeMessage + System.lineSeparator() + "L'atén " + tpv.getCurrentSaleAssistant().getName();
        }
        StringBuilder sb = new StringBuilder();
        for (Sale.SaleLine sl : tpv.getCurrentSale().getLines()) {
            sb.append(sl.getName())
                    .append(" - ")
                    .append(sl.getUnitPrice())
                    .append("€/u x ")
                    .append(sl.getAmount())
                    .append("u = ")
                    .append(sl.getTotalPrice())
                    .append("€")
                    .append(System.lineSeparator());
        }
        sb.append("---")
                .append(System.lineSeparator())
                .append("Total: ")
                .append(tpv.getCurrentSale().getTotal())
                .append("€");
        return sb.toString();
    }

    public double cashPayment(double delivered) {
        checkPaymentConditions();
        double total = getCurrentSale().getTotal();
        if (total > delivered)
            throw new IllegalStateException("No es pot cobrar una venta amb un import inferior al total de la venta");
        tpv.addCash(total);
        tpv.endSale();
        return calculateChange(delivered, total);
    }

    private double calculateChange(double delivered, double total) {
        return Math.round((delivered - total) * 100.0) / 100.0;
    }

    private void checkPaymentConditions() {
        if (!tpv.hasSale())
            throw new IllegalStateException("No es pot cobrar una venta si no està iniciada");
        if (tpv.getCurrentSale().isEmpty())
            throw new IllegalStateException("No es pot cobrar una venta sense cap producte");
    }

    public void tarjetPayment() {
        checkPaymentConditions();
        tpv.endSale();
    }

    public void addProductByBarCode(int barCode, int unitats, boolean refund) {
        Product p = productsService.findByBarCode(barCode);
        if (p == null) {
            throw new IllegalStateException(ADD_NON_EXISTING_PRODUCT_ERROR);
        }
        List<Discount> dList = discountService.listByTriggerId(p.getId());
        tpv.addProduct(p, unitats, dList, refund);
    }

    public List<Product> addProductByName(@NotNull String nom, boolean refund) {
        return addProductByName(nom, 1, refund);
    }

    /**
     * Add product to sale by name
     *
     * @param nomProducte product name
     * @param unitatsProducte units added
     * @return products that match the name introduced
     */
    public List<Product> addProductByName(@NotNull String nomProducte, int unitatsProducte, boolean refund) {
        List<Product> products = productsService.findByName(nomProducte);
        if (products.isEmpty()) {
            throw new IllegalStateException(ADD_NON_EXISTING_PRODUCT_ERROR);
        }
        if (products.size() == 1) {
            Product p = products.get(0);
            List<Discount> dList= discountService.listByTriggerId(p.getId());
            tpv.addProduct(p, unitatsProducte, dList, refund);
        }
        return products;
    }

    public Balance getLastBalance() {
        return balancesService.getLastBalance();
    }

}
