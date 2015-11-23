package edu.upc.essi.gps.ecommerce;

import cucumber.api.java.ca.Aleshores;
import cucumber.api.java.ca.Donat;
import cucumber.api.java.ca.Quan;
import edu.upc.essi.gps.domain.Sale;
import edu.upc.essi.gps.domain.Sale.SaleLine;

import static org.junit.Assert.*;

public class StepDefinitions {

    private ProductsService productsService = new ProductsService(new ProductsRepository());
    private TPVService tpvService = new TPVService(new TPVRepository());
    private SaleAssistantService saleAssistantService = new SaleAssistantService(new SaleAssistantRepository());
    private Exception exception;
    private TPVController TPVController;
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
        this.TPVController = new TPVController(productsService, saleAssistantService, tpvService, shop, posNumber);
    }

    @Aleshores("^el tpv està en ús per en \"([^\"]*)\"$")
    public void checkCurrentSaleAssistantName(String saleAssistantName) throws Throwable {
        assertEquals(saleAssistantName, this.TPVController.getCurrentSaleAssistantName());
    }

    @Aleshores("^el tpv té un efectiu inicial de ([0-9]*\\.?[0-9]{2})€$")
    public void checkInitialCash(double cash) throws Throwable {
        assertEquals(cash, this.TPVController.getInitialCash(), 0.005);
    }

    @Aleshores("^la venta actual és de'n \"([^\"]*)\" al tpv (\\d+) de la botiga \"([^\"]*)\"$")
    public void checkCurrentSaleData(String saleAssistant, int posNumber, String shop) throws Throwable {
        Sale s = this.TPVController.getCurrentSale();
        assertNotNull(s);
        assertEquals(shop, s.getShop());
        assertEquals(posNumber, s.getPosNumber());
        assertEquals(saleAssistant, s.getSaleAssistantName());
    }

    //STUB - NO ÉS FUNCIONALITAT FINAL
    @Donat("^que el \"([^\"]*)\" s'ha registrat al sistema amb password \"([^\"]*)\" i reb l'identificador (\\d+)$")
    public void register(String name, String password, long saleAssistantID) throws Throwable {
        tryCatch(() -> this.saleAssistantService.insert(name, password, saleAssistantID));
    }

    @Donat("^que s'inica el torn al tpv amb identificador (\\d+) i password \"([^\"]*)\", amb un efectiu inicial de ([0-9]*\\.?[0-9]{2})€$")
    public void loginDonat(long saleAssistantID, String password, double cash) throws Throwable {
        this.TPVController.login(saleAssistantID, password, cash);
    }


    @Quan("^inicio el torn al tpv amb identificador (\\d+) i password \"([^\"]*)\", amb un efectiu inicial de ([0-9]*\\.?[0-9]{2})€$")
    public void login(long saleAssistantID, String password, double cash) throws Throwable {
        tryCatch(() -> this.TPVController.login(saleAssistantID, password, cash));
    }

    @Quan("^desbloquejo el tpv amb el password \"([^\"]*)\"$")
    public void unblock(String password) throws Throwable {
        tryCatch(() -> this.TPVController.unblock(password));
    }

    //STUB - NO ÉS FUNCIONALITAT FINAL TODO
    @Quan("^inicio el torn al tpv amb identificador (\\d+) i password \"([^\"]*)\", amb un efectiu inicial de ([0-9]*\\.?[0-9]{2})€ (\\d+) cops$")
    public void login(long saleAssistantID, String password, double cash, int n) throws Throwable {
        for (int i = 0; i < n; ++i)
            tryCatch(() -> this.TPVController.login(saleAssistantID, password, cash));
    }

    @Aleshores("^el tpv es troba en estat \"([^\"]*)\"$")
    public void tpvState(String state) throws Throwable {
        assertEquals(state, this.TPVController.getTpvState().toString());
    }

    @Quan("^inicio una nova venta$")
    public void tryStartSale() throws Throwable {
        tryCatch(this.TPVController::startSale);
    }

    @Donat("^que hi ha una venta iniciada$")
    public void saleStarted() throws Throwable {
        this.TPVController.startSale();
    }

    @Donat("^un producte amb nom \"([^\"]*)\", preu (\\d+)€, iva (\\d+)% i codi de barres (\\d+)$")
    public void productCreated(String productName, int price, int vatPct, int barCode) throws Throwable {
        this.productsService.newProduct(productName, price, vatPct, barCode);
    }

    @Quan("^afegeixo el producte de codi de barres (\\d+) a la venta$")
    public void addProductByBarCode(int barCode) throws Throwable {
        this.TPVController.addProductByBarCode(barCode);
    }

    @Donat("^que he afegit el producte de codi de barres (\\d+) a la venta$")
    public void productByBarCodeAdded(int barCode) throws Throwable {
        this.TPVController.addProductByBarCode(barCode);
    }

    @Aleshores("^la venta té (\\d+) (?:línia|línies)$")
    public void la_venta_té_n_linies(int expectedNumberOfLines) throws Throwable {
        assertEquals(expectedNumberOfLines, this.TPVController.getCurrentSale().getLines().size());
    }

    @Aleshores("^línia de venta (\\d+) és de (\\d+) unitats de \"([^\"]*)\" a (\\d+)€ cada una per un total de (\\d+)€$")
    public void línia_de_venta_és_de_unitats_de_a_€_cada_una_per_un_total_de_€(int lineNumber, int units, String productName, int unitPrice, int totalPrice) throws Throwable {
        SaleLine sl = this.TPVController.getCurrentSale().getLines().get(lineNumber - 1);
        assertEquals(units,sl.getAmount());
        assertEquals(unitPrice,sl.getUnitPrice());
        assertEquals(totalPrice,sl.getTotalPrice());
        assertEquals(productName, sl.getProductName());
    }

    @Aleshores("^el total de la venta actual és de (\\d+)€$")
    public void el_total_de_la_venta_actual_és_de_€(int saleTotal) throws Throwable {
        assertEquals(saleTotal, this.TPVController.getCurrentSale().getTotal());
    }

    @Aleshores("^la pantalla del client del tpv mostra$")
    public void la_pantalla_del_client_del_tpv_mostra(String msg) throws Throwable {
        assertEquals(msg, this.TPVController.getCustomerScreenMessage());
    }

    @Quan("^indico que el client ha entregat (\\d+)€ per a pagar en metàlic$")
    public void cashPayment(int delivered) throws Throwable {
        tryCatch(() -> this.change = this.TPVController.cashPayment(delivered));
    }

    @Quan("^indico que el client paga amb targeta$")
    public void tarjetPayment() throws Throwable {
        //tryCatch(() -> this.change = this.TPVController.tarjetPayment());
    }

    @Aleshores("^el tpv m'indica que el canvi a retornar és de (\\d+)€$")
    public void checkChange(int expectedChange) throws Throwable {
        assertEquals(expectedChange, change);
    }

    @Aleshores("^el tpv indica que s'ha pagat amb targeta$")
    public void setTarget() throws Throwable {

    }

    @Aleshores("^la venta esta iniciada$")
    public void la_venta_esta_iniciada() throws Throwable {
        //assertTrue(TPVController.isSaleStarted());
    }


}
