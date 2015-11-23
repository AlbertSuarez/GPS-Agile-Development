package edu.upc.essi.gps.ecommerce;

import edu.upc.essi.gps.domain.*;

import java.util.Date;

import static edu.upc.essi.gps.utils.Validations.*;

public class TPVController {

    private final ProductsService productsService;
    private final SaleAssistantService saleAssistantService;
    private final TPVService tpvService;
    private final TPV tpv;

    public TPVController(ProductsService productsService, SaleAssistantService saleAssistantService, TPVService tpvService, String shop, int pos) {
        this.tpvService = tpvService;
        this.productsService = productsService;
        this.saleAssistantService = saleAssistantService;
        tpv = tpvService.findByShopPos(shop, pos);
        tpv.setCurrentSaleAssistant(null);
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

    public boolean login(long saleAssistantId, String password, double cash) {
        if (tpv.getState().equals(TPVState.IDLE))
            throw new IllegalStateException("Aquest tpv està en ús per " + tpv.getCurrentSaleAssistant().getName());
        if (tpv.getState().equals(TPVState.BLOCKED)) throw new IllegalStateException("Aquest tpv està bloquejat");

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
        if (tpv.getCurrentSale() != null) throw new IllegalStateException("Aquest tpv ja té una venta iniciada");
        tpv.setCurrentSale(new Sale(tpv.getShop(), tpv.getPos()));
    }

    public void addProductByBarCode(int barCode) {
        if (tpv.getCurrentSale() == null) throw new IllegalStateException("No hi ha cap venta iniciada");
        Product p = productsService.findByBarCode(barCode);
        tpv.getCurrentSale().addProduct(p);
    }

    public String getCustomerScreenMessage() {
        String welcomeMessage = "Li donem la benvinguda a Joguets i Joguines!";
        if (tpv.getCurrentSale() == null) return welcomeMessage;
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
        if(tpv.getCurrentSale()==null)
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
        if(tpv.getCurrentSale()==null)
            throw new IllegalStateException("No es pot cobrar una venta si no està iniciada");
        if(tpv.getCurrentSale().isEmpty())
            throw new IllegalStateException("No es pot cobrar una venta sense cap producte");
        return delivered-tpv.getCurrentSale().getTotal();
    }

}
