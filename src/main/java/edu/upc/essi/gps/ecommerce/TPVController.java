package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.*;

import static edu.upc.essi.gps.utils.Validations.*;

public class TPVController {

    private final ProductsService productsService;
    private final SaleAssistantService saleAssistantService;
    private final TPVService tpvService;

    private final TPV tpv;
    private SaleAssistant currentSaleAssistant;
    private Sale currentSale;

    public TPVController(ProductsService productsService, SaleAssistantService saleAssistantService, TPVService tpvService, String shop, int pos) {
        this.tpvService = tpvService;
        this.productsService = productsService;
        this.saleAssistantService = saleAssistantService;
        tpv = tpvService.findByShopPos(shop, pos);
        currentSaleAssistant = null;
    }

    public boolean login(long saleAssistantId, String password, double cash) {
        if (tpv.getState().equals(TPVState.IDLE))
            throw new IllegalStateException("Aquest tpv està en ús per " + this.currentSaleAssistant.getName());
        if (tpv.getState().equals(TPVState.BLOCKED)) throw new IllegalStateException("Aquest tpv està bloquejat");

        checkNotNull(saleAssistantId, "saleAssistantId");
        checkNotNull(password, "password");

        boolean loggedIn = saleAssistantService.validate(saleAssistantId, password);
        tpvService.validation(tpv.getId(), loggedIn);
        if (loggedIn) {
            tpv.setInitialCash(cash);
            tpv.setCash(cash);
        }
        return loggedIn;
    }

    public void unblock(String password) {
        if (!tpv.getState().equals(TPVState.BLOCKED)) throw new IllegalStateException("Aquest tpv no està bloquejat");

        checkNotNull(password, "password");

        if (!tpvService.validateAdmin(tpv.getId(), password))
            throw new IllegalStateException("Password d'administrador incorrecte");
    }

    public TPVState getTpvState() {
        return tpv.getState();
    }

    public double getInitialCash() {
        return tpv.getInitialCash();
    }

    public void startSale() {
        if (this.currentSale != null) throw new IllegalStateException("Aquest tpv ja té una venta iniciada");
        this.currentSale = new Sale(tpv.getShop(), tpv.getPos(), currentSaleAssistant.getName());
    }

    public Sale getCurrentSale() {
        return currentSale;
    }

    public void addProductByBarCode(int barCode) {
        if (currentSale == null) throw new IllegalStateException("No hi ha cap venta iniciada");
        Product p = productsService.findByBarCode(barCode);
        currentSale.addProduct(p);
    }

    public String getCustomerScreenMessage() {
        String welcomeMessage = "Li donem la benvinguda a Joguets i Joguines!";
        if (currentSale == null) return welcomeMessage;
        if (currentSale.isEmpty()) {
            return welcomeMessage + "\nL'atén " + currentSale.getSaleAssistantName();
        }
        StringBuilder sb = new StringBuilder();
        for (Sale.SaleLine sl : currentSale.getLines()) {
            sb.append(sl.getProductName()).append(" - ")
                    .append(sl.getUnitPrice()).append("€/u x ").append(sl.getAmount()).append("u = ")
                    .append(sl.getTotalPrice()).append("€\n");
        }
        sb.append("---\n").append("Total: ").append(currentSale.getTotal()).append("€");
        return sb.toString();
    }

    public int cashPayment(int delivered) {
        if(currentSale==null)
            throw new IllegalStateException("No es pot cobrar una venta si no està iniciada");
        if(currentSale.isEmpty())
            throw new IllegalStateException("No es pot cobrar una venta sense cap producte");
        return getCanvi(delivered);
    }

    private int getCanvi(int delivered) {
        if(currentSale.getTotal() > delivered)
            throw new IllegalStateException("No es pot cobrar una venta amb un import inferior al total de la venta");
        return delivered-currentSale.getTotal();
    }

    public int tarjetPayment(int delivered) {
        if(currentSale==null)
            throw new IllegalStateException("No es pot cobrar una venta si no està iniciada");
        if(currentSale.isEmpty())
            throw new IllegalStateException("No es pot cobrar una venta sense cap producte");
        return delivered-currentSale.getTotal();
    }

    public String getCurrentSaleAssistantName() {
        return currentSaleAssistant.getName();
    }
}
