package edu.upc.essi.gps.ecommerce;

import cucumber.api.java.ca.Aleshores;
import cucumber.api.java.ca.Donat;
import cucumber.api.java.ca.Quan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StepDefinitions {

    private ProductsService productsService = new ProductsService(new ProductsRepository());
    private Exception exception;
    private PosController posController;
    private int change;

    public void tryCatch(Runnable r){
        try {
            r.run();
            this.exception = null;
        } catch (Exception e){
            this.exception = e;
        }
    }

    @Aleshores("^obtinc un error que diu: \"([^\"]*)\"$")
    public void checkErrorMessage(String msg) throws Throwable {
        assertNotNull(this.exception);
        assertEquals(msg, this.exception.getMessage());
    }

    @Donat("^que estem al tpv número (\\d+) de la botiga \"([^\"]*)\"$")
    public void setupPos(int posNumber, String shop) throws Throwable {
        this.posController = new PosController(shop,posNumber, productsService);
    }

    @Aleshores("^el tpv està en ús per en \"([^\"]*)\"$")
    public void checkCurrentSaleAssistantName(String saleAssistantName) throws Throwable {
        assertEquals(saleAssistantName, this.posController.getCurrentSaleAssistantName());
    }

    @Aleshores("^la venta actual és de'n \"([^\"]*)\" al tpv (\\d+) de la botiga \"([^\"]*)\"$")
    public void checkCurrentSaleData(String saleAssistant, int posNumber, String shop) throws Throwable {
        Sale s = this.posController.getCurrentSale();
        assertNotNull(s);
        assertEquals(shop, s.getShop());
        assertEquals(posNumber, s.getPosNumber());
        assertEquals(saleAssistant, s.getSaleAssistantName());
    }

    @Quan("^inicio el torn al tpv com a \"([^\"]*)\"$")
    public void login(String saleAssistantName) throws Throwable {
        tryCatch(() -> this.posController.login(saleAssistantName));
    }

    @Donat("^que en \"([^\"]*)\" ha iniciat el torn al tpv$")
    public void hasLoggedIn(String saleAssistantName) throws Throwable {
        this.posController.login(saleAssistantName);
    }

    @Quan("^inicio una nova venta$")
    public void tryStartSale() throws Throwable {
        tryCatch(() -> this.posController.startSale());
    }

    @Donat("^que hi ha una venta iniciada$")
    public void saleStarted() throws Throwable {
        this.posController.startSale();
    }

    @Donat("^un producte amb nom \"([^\"]*)\", preu (\\d+)€, iva (\\d+)% i codi de barres (\\d+)$")
    public void productCreated(String productName, int price, int vatPct, int barCode) throws Throwable {
        this.productsService.newProduct(productName, price, vatPct, barCode);
    }

    @Quan("^afegeixo el producte de codi de barres (\\d+) a la venta$")
    public void addProductByBarCode(int barCode) throws Throwable {
        this.posController.addProductByBarCode(barCode);
    }

    @Donat("^que he afegit el producte de codi de barres (\\d+) a la venta$")
    public void productByBarCodeAdded(int barCode) throws Throwable {
        this.posController.addProductByBarCode(barCode);
    }

    @Aleshores("^la venta té (\\d+) (?:línia|línies)$")
    public void la_venta_té_n_linies(int expectedNumberOfLines) throws Throwable {
        assertEquals(expectedNumberOfLines, this.posController.getCurrentSale().getLines().size());
    }

    @Aleshores("^línia de venta (\\d+) és de (\\d+) unitats de \"([^\"]*)\" a (\\d+)€ cada una per un total de (\\d+)€$")
    public void línia_de_venta_és_de_unitats_de_a_€_cada_una_per_un_total_de_€(int lineNumber, int units, String productName, int unitPrice, int totalPrice) throws Throwable {
        SaleLine sl = this.posController.getCurrentSale().getLines().get(lineNumber - 1);
        assertEquals(units,sl.getAmount());
        assertEquals(unitPrice,sl.getUnitPrice());
        assertEquals(totalPrice,sl.getTotalPrice());
        assertEquals(productName, sl.getProductName());
    }

    @Aleshores("^el total de la venta actual és de (\\d+)€$")
    public void el_total_de_la_venta_actual_és_de_€(int saleTotal) throws Throwable {
        assertEquals(saleTotal,this.posController.getCurrentSale().getTotal());
    }

    @Aleshores("^la pantalla del client del tpv mostra$")
    public void la_pantalla_del_client_del_tpv_mostra(String msg) throws Throwable {
        assertEquals(msg, this.posController.getCustomerScreenMessage());
    }

    @Quan("^indico que el client ha entregat (\\d+)€ per a pagar en metàlic$")
    public void cashPayment(int delivered) throws Throwable {
        tryCatch(() -> this.change = this.posController.cashPayment(delivered));
    }

    @Quan("^indico que el client paga amb targeta$")
    public void tarjetPayment(int delivered) throws Throwable {
        tryCatch(() -> this.change = this.posController.tarjetPayment(delivered));
    }

    @Aleshores("^el tpv m'indica que el canvi a retornar és de (\\d+)€$")
    public void checkChange(int expectedChange) throws Throwable {
        assertEquals(expectedChange, change);
    }

    @Aleshores("^el tpv indica que s'ha pagat amb targeta$")
    public void setTarget() throws Throwable {

    }
}
