package edu.upc.essi.gps.ecommerce;

import com.sun.istack.internal.NotNull;
import edu.upc.essi.gps.domain.*;

import java.util.Date;

import static edu.upc.essi.gps.utils.Validations.*;

public class TPVController {

    private final ProductsService productsService;
    private final SaleAssistantService saleAssistantService;
    private final DiscountService discountService;
    private final TPVService tpvService;
    private final TPV tpv;

    public TPVController(ProductsService productsService, SaleAssistantService saleAssistantService, DiscountService discountService, TPVService tpvService, String shop, int pos) {
        this.productsService = productsService;
        this.saleAssistantService = saleAssistantService;
        this.discountService = discountService;
        this.tpvService = tpvService;
        tpv = tpvService.findByShopPos(shop, pos);
        tpv.reset();
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
            tpv.setInitialCash(cash);
            tpv.setCash(cash);
            tpv.setCurrentSaleAssistant(saleAssistantService.findById(saleAssistantId));
        }
        return loggedIn;
    }

    public void unblock(@NotNull String password) {
        if (!tpv.getState().equals(TPVState.BLOCKED)) throw new IllegalStateException("Aquest tpv no està bloquejat");

        checkNotNull(password, "password");

        if (!tpvService.validateAdmin(tpv.getId(), password))
            throw new IllegalStateException("Password d'administrador incorrecte");
    }

    public void logout(double cash){
        double tpvCash = tpv.getCash();
        if (tpvCash != cash) {
            //TODO: desquadrament
            //Recullo alguna informació aquí, utilitzar la que faci falta i esborrar la que no
            Date today = new Date();
            double initialCash = tpv.getInitialCash();
            SaleAssistant sa = tpv.getCurrentSaleAssistant();
            String assistantName = sa.getName();
            long assistantId = sa.getId();
            double variation = cash - tpvCash;
        }
        tpv.reset();
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
        if (!tpv.hasSale()) tpv.newSale();
        Product p = productsService.findByBarCode(barCode);
        tpv.getCurrentSale().addProduct(p);
    }

    public void addNewDiscountToCurrentSale(String discountType, int prodLine, String name, Object... params) {
        Product p = productsService.findById(tpv.getCurrentSale().getProductId(prodLine));
        long discId = discountService.newDiscount(discountType, p, name, -1, params);
        Discount discount = discountService.findById(discId);
        getCurrentSale().addDiscount(discount, prodLine);
    }

    public String getCustomerScreenMessage() {
        String welcomeMessage = "Li donem la benvinguda a Joguets i Joguines!";
        if (!tpv.hasSale()) return welcomeMessage;
        if (tpv.getCurrentSale().isEmpty()) {
            return welcomeMessage + "\nL'atén " + tpv.getCurrentSaleAssistant().getName();
        }
        StringBuilder sb = new StringBuilder();
        for (Sale.SaleLine sl : tpv.getCurrentSale().getLines()) {
            sb.append(sl.getProductName()).append(" - ")
                    .append(sl.getUnitPrice()).append("€/u x ").append(sl.getAmount()).append("u = ")
                    .append(sl.getTotalPrice()).append("€\n");
        }
        sb.append("---\n").append("Total: ").append(tpv.getCurrentSale().getTotal()).append("€");
        return sb.toString();
    }

    public int cashPayment(int delivered) {
        if(!tpv.hasSale())
            throw new IllegalStateException("No es pot cobrar una venta si no està iniciada");
        if(tpv.getCurrentSale().isEmpty())
            throw new IllegalStateException("No es pot cobrar una venta sense cap producte");
        return getCanvi(delivered);
    }

    private int getCanvi(int delivered) {
        int total = tpv.getCurrentSale().getTotal();
        if(total > delivered)
            throw new IllegalStateException("No es pot cobrar una venta amb un import inferior al total de la venta");
        return delivered-total;
    }

    public int tarjetPayment(int delivered) {
        if(!tpv.hasSale())
            throw new IllegalStateException("No es pot cobrar una venta si no està iniciada");
        if(tpv.getCurrentSale().isEmpty())
            throw new IllegalStateException("No es pot cobrar una venta sense cap producte");
        return delivered-tpv.getCurrentSale().getTotal();
    }

}
