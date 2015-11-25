package edu.upc.essi.gps.ecommerce;

import cucumber.api.PendingException;
import cucumber.api.java.ca.Aleshores;
import cucumber.api.java.ca.Donat;
import cucumber.api.java.ca.Quan;
import edu.upc.essi.gps.domain.Sale;
import edu.upc.essi.gps.domain.Sale.SaleLine;
import java.util.List;

import static org.junit.Assert.*;

public class StepDefinitions {

    private ProductsService productsService = new ProductsService(new ProductsRepository());
    private TPVService tpvService = new TPVService(new TPVRepository());
    private SaleAssistantService saleAssistantService = new SaleAssistantService(new SaleAssistantRepository());
    private DiscountService discountService = new DiscountService(new DiscountRepository());
    private Exception exception;
    private TPVController TPVController;
    private int change;
    private List<SaleLine> lines;

    public void tryCatch(Runnable r){
        try {
            r.run();
            exception = null;
        } catch (Exception e){
            exception = e;
        }
    }

    @Aleshores("^obtinc un error que diu: \"([^\"]*)\"$")
    public void checkErrorMessage(String msg) throws Throwable {
        assertNotNull(exception);
        assertEquals(msg, exception.getMessage());
    }

    @Donat("^que estem al tpv número (\\d+) de la botiga \"([^\"]*)\"$")
    public void setupPos(int posNumber, String shop) throws Throwable {
        tpvService.newTPV(shop, posNumber);
        TPVController = new TPVController(productsService, saleAssistantService, discountService, tpvService, shop, posNumber);
    }

    @Aleshores("^el tpv està en ús per en \"([^\"]*)\"$")
    public void checkCurrentSaleAssistantName(String saleAssistantName) throws Throwable {
        assertEquals(saleAssistantName, TPVController.getCurrentSaleAssistant().getName());
    }

    @Aleshores("^el tpv té un efectiu inicial de ([0-9]*\\.?[0-9]{2})€$")
    public void checkInitialCash(double cash) throws Throwable {
        assertEquals(cash, TPVController.getTpv().getInitialCash(), 0.005);
    }

    @Aleshores("^la venta actual és de'n \"([^\"]*)\" al tpv (\\d+) de la botiga \"([^\"]*)\"$")
    public void checkCurrentSaleData(String saleAssistant, int posNumber, String shop) throws Throwable {
        Sale s = TPVController.getCurrentSale();
        assertNotNull(s);
        assertEquals(shop, TPVController.getTpv().getShop());
        assertEquals(posNumber, TPVController.getTpv().getPos());
        assertEquals(saleAssistant, TPVController.getCurrentSaleAssistant().getName());
    }

    //TODO: STUB - NO ÉS FUNCIONALITAT FINAL
    @Donat("^que el \"([^\"]*)\" s'ha registrat al sistema amb password \"([^\"]*)\" i reb l'identificador (\\d+)$")
    public void register(String name, String password, long saleAssistantID) throws Throwable {
        tryCatch(() -> saleAssistantService.insert(name, password, saleAssistantID));
    }

    @Donat("^que s'inica el torn al tpv amb identificador (\\d+) i password \"([^\"]*)\", amb un efectiu inicial de ([0-9]*\\.?[0-9]{2})€$")
    public void loginDonat(long saleAssistantID, String password, double cash) throws Throwable {
        TPVController.login(saleAssistantID, password, cash);
    }

    @Quan("^inicio el torn al tpv amb identificador (\\d+) i password \"([^\"]*)\", amb un efectiu inicial de ([0-9]*\\.?[0-9]{2})€$")
    public void login(long saleAssistantID, String password, double cash) throws Throwable {
        tryCatch(() -> TPVController.login(saleAssistantID, password, cash));
    }

    @Quan("^desbloquejo el tpv amb el password \"([^\"]*)\"$")
    public void unblock(String password) throws Throwable {
        tryCatch(() -> TPVController.unblock(password));
    }

    @Quan("^incremento l'efectiu de la caixa en ([0-9]*\\.?[0-9]{2})€$")
    public void increaseCash(double cash) throws Throwable {
        tryCatch(() -> TPVController.getTpv().addCash(cash));
    }

    @Quan("^decremento l'efectiu de la caixa en ([0-9]*\\.?[0-9]{2})€$")
    public void dereaseCash(double cash) throws Throwable {
        tryCatch(() -> TPVController.getTpv().addCash(-cash));
    }

    @Aleshores("^s'emmagatzema el quadrament de caixa$") //TODO: Falta validar tota la info que es vulgui emmagatzemar
    public void checkQuadrament() throws Throwable {
    }

    @Aleshores("^s'emmagatzema el desquadrament de caixa$") //TODO: Ídem, si cal es poden ajuntar tots dos en un sol cas.
    public void checkDesquadrament() throws Throwable {
    }

    @Quan("^finalitzo el meu torn, amb un efectiu final de ([0-9]*\\.?[0-9]{2})€$")
    public void logout(double cash) throws Throwable {
        tryCatch(() -> TPVController.logout(cash));
    }

    //TODO: STUB - NO ÉS FUNCIONALITAT FINAL
    @Quan("^inicio el torn al tpv amb identificador (\\d+) i password \"([^\"]*)\", amb un efectiu inicial de ([0-9]*\\.?[0-9]{2})€ (\\d+) cops$")
    public void login(long saleAssistantID, String password, double cash, int n) throws Throwable {
        for (int i = 0; i < n; ++i)
            tryCatch(() -> TPVController.login(saleAssistantID, password, cash));
    }

    @Aleshores("^el tpv es troba en estat \"([^\"]*)\"$")
    public void tpvState(String state) throws Throwable {
        assertEquals(state, TPVController.getTpv().getState().toString());
    }

    @Quan("^inicio una nova venta$")
    public void tryStartSale() throws Throwable {
        tryCatch(TPVController::startSale);
    }

    @Donat("^que hi ha una venta iniciada$")
    public void saleStarted() throws Throwable {
        TPVController.startSale();
    }

    @Donat("^un producte amb nom \"([^\"]*)\", preu (\\d+)€, iva (\\d+)% i codi de barres (\\d+)$")
    public void productCreated(String productName, int price, int vatPct, int barCode) throws Throwable {
        productsService.newProduct(productName, price, vatPct, barCode);
    }

    @Quan("^afegeixo el producte de codi de barres (\\d+) a la venta$")
    public void addProductByBarCode(int barCode) throws Throwable {
        TPVController.addProductByBarCode(barCode);
    }

    @Donat("^que he afegit el producte de codi de barres (\\d+) a la venta$")
    public void productByBarCodeAdded(int barCode) throws Throwable {
        TPVController.addProductByBarCode(barCode);
    }

    @Aleshores("^la venta té (\\d+) (?:línia|línies)$")
    public void la_venta_té_n_linies(int expectedNumberOfLines) throws Throwable {
        assertEquals(expectedNumberOfLines, TPVController.getCurrentSale().getLines().size());
    }

    @Aleshores("^línia de venta (\\d+) és de (\\d+) unitats de \"([^\"]*)\" a (\\d+)€ cada una per un total de (\\d+)€$")
    public void línia_de_venta_és_de_unitats_de_a_€_cada_una_per_un_total_de_€(int lineNumber, int units, String productName, int unitPrice, int totalPrice) throws Throwable {
        SaleLine sl = TPVController.getCurrentSale().getLines().get(lineNumber - 1);
        assertEquals(units,sl.getAmount());
        assertEquals(unitPrice,sl.getUnitPrice());
        assertEquals(totalPrice,sl.getTotalPrice());
        assertEquals(productName, sl.getProductName());
    }

    @Aleshores("^el total de la venta actual és de (\\d+)€$")
    public void el_total_de_la_venta_actual_és_de_€(int saleTotal) throws Throwable {
        assertEquals(saleTotal, TPVController.getCurrentSale().getTotal());
    }

    @Aleshores("^la pantalla del client del tpv mostra$")
    public void la_pantalla_del_client_del_tpv_mostra(String msg) throws Throwable {
        assertEquals(msg, TPVController.getCustomerScreenMessage());
    }

    @Quan("^indico que el client ha entregat (\\d+)€ per a pagar en metàlic$")
    public void cashPayment(int delivered) throws Throwable {
        tryCatch(() -> change = TPVController.cashPayment(delivered));
    }

    @Quan("^indico que el client paga (\\d+)€ amb targeta$")
    public void tarjetPayment(int delivered) throws Throwable {
        tryCatch(() -> change = TPVController.tarjetPayment(delivered));
    }

    @Aleshores("^el tpv m'indica que el canvi a retornar és de (\\d+)€$")
    public void checkChange(int expectedChange) throws Throwable {
        assertEquals(expectedChange, change);
    }

    @Aleshores("^el tpv indica que s'ha pagat amb targeta$")
    public void setTarget() throws Throwable {

    }

    @Aleshores("^el tpv tanca la venda actual$")
    public void el_tpv_tanca_la_venda_actual() throws Throwable {
        tryCatch(TPVController::endSale);
    }

    @Aleshores("^la venta esta iniciada$")
    public void la_venta_esta_iniciada() throws Throwable {
        assertTrue(TPVController.isSaleStarted());
    }

    @Aleshores("^la venda conté el producte amb codi de barres (\\d+)$")
    public void la_venda_conté_el_producte_amb_codi_de_barres(int barCode) {
        assertTrue(TPVController.getCurrentSale().hasProductByBarCode(barCode));
    }

    @Aleshores("^es consulta les linies de venda$")
    public void es_consulta_les_linies_de_venda() throws Throwable {
        tryCatch(() -> lines = TPVController.getCurrentSale().getLines());
    }

    @Quan("^indico que vull consulta la linia de venda$")
    public void indico_que_vull_consulta_la_linia_de_venda() throws Throwable {
        tryCatch(() -> lines = TPVController.getCurrentSale().getLines());
    }

    @Donat("^que no hi ha cap venda a la linia de venda$")
    public void que_no_hi_ha_cap_venda_a_la_linia_de_venda() throws Throwable {
        assertTrue(TPVController.getCurrentSale().isEmpty());
    }
}
