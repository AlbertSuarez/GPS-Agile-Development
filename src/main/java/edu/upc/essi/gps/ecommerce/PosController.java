package edu.upc.essi.gps.ecommerce;

import static edu.upc.essi.gps.utils.Validations.checkNotNull;

public class PosController {

    private final ProductsService productsService;
    private final String shop;
    private final int posNumber;
    private String currentSaleAssistantName;
    private Sale currentSale;

    public PosController(String shop, int posNumber, ProductsService productsService) {
        this.shop = shop;
        this.posNumber = posNumber;
        this.productsService = productsService;
    }

    public void login(String saleAssistantName) {
        checkNotNull(saleAssistantName, "saleAssistantName");
        if (this.currentSaleAssistantName != null)
            throw new IllegalStateException("Aquest tpv està en ús per " + this.currentSaleAssistantName);
        this.currentSaleAssistantName = saleAssistantName;
    }

    public void startSale() {
        if (this.currentSale != null) throw new IllegalStateException("Aquest tpv ja té una venta iniciada");
        this.currentSale = new Sale(shop, posNumber, currentSaleAssistantName);
    }

    public void endSale() {
        if (this.currentSale == null) throw new IllegalStateException("Aquest tpv no té una venta iniciada");
        currentSale = null;
    }

    public String getCurrentSaleAssistantName() {
        return currentSaleAssistantName;
    }

    public Sale getCurrentSale() {
        return currentSale;
    }

    public void addProductByBarCode(int barCode) {
        if (currentSale == null) currentSale = new Sale(shop, posNumber, currentSaleAssistantName);
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
        for (SaleLine sl : currentSale.getLines()) {
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

    public int tarjetPayment() {
        if(currentSale==null)
            throw new IllegalStateException("No es pot cobrar una venta si no està iniciada");
        if(currentSale.isEmpty())
            throw new IllegalStateException("No es pot cobrar una venta sense cap producte");
        return currentSale.getTotal();
    }

    boolean isSaleStarted(){
        if(currentSale==null) return false;
        else return true;
    }

}
