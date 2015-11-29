package edu.upc.essi.gps.ecommerce;

import cucumber.api.java.ca.Aleshores;
import cucumber.api.java.ca.Donat;
import cucumber.api.java.ca.I;
import cucumber.api.java.ca.Quan;
import edu.upc.essi.gps.domain.Balance;
import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.domain.Sale;
import edu.upc.essi.gps.domain.Sale.SaleLine;

import java.util.List;

import static org.junit.Assert.*;

public class StepDefinitions {

    private static final double DELTA = 1e-15;
    private ProductsService productsService = new ProductsService(new ProductsRepository());
    private TPVService tpvService = new TPVService(new TPVRepository());
    private SaleAssistantService saleAssistantService = new SaleAssistantService(new SaleAssistantRepository());
    private BalancesService balancesService = new BalancesService(new BalancesRepository());
    private DiscountService discountService = new DiscountService(new DiscountRepository());
    private SalesService salesService = new SalesService(new SalesRepository());
    private Exception exception;
    private TPVController tpvController;
    private ProductManagerController productManagerController;
    private double change;
    private List<SaleLine> lines;
    private List<Product> products;
    private List<Balance> balances;

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
        tpvController = new TPVController(productsService, saleAssistantService, tpvService, balancesService, discountService, shop, posNumber);
    }

    @Aleshores("^el tpv està en ús per en \"([^\"]*)\"$")
    public void checkCurrentSaleAssistantName(String saleAssistantName) throws Throwable {
        assertEquals(saleAssistantName, tpvController.getCurrentSaleAssistant().getName());
    }

    @Aleshores("^el tpv té un efectiu inicial de €([^\"]*)€$")
    public void checkInitialCash(double cash) throws Throwable {
        assertEquals(cash, tpvController.getTpv().getInitialCash(), 0.005);
    }

    @Aleshores("^la venta actual és de'n \"([^\"]*)\" al tpv (\\d+) de la botiga \"([^\"]*)\"$")
    public void checkCurrentSaleData(String saleAssistant, int posNumber, String shop) throws Throwable {
        Sale s = tpvController.getCurrentSale();
        assertNotNull(s);
        assertEquals(shop, tpvController.getTpv().getShop());
        assertEquals(posNumber, tpvController.getTpv().getPos());
        assertEquals(saleAssistant, tpvController.getCurrentSaleAssistant().getName());
    }

    //TODO: STUB - NO ÉS FUNCIONALITAT FINAL
    @Donat("^que el \"([^\"]*)\" s'ha registrat al sistema amb password \"([^\"]*)\" i rep l'identificador (\\d+)$")
    public void register(String name, String password, long saleAssistantID) throws Throwable {
        tryCatch(() -> saleAssistantService.insert(name, password, saleAssistantID));
    }

    @Donat("^que s'inica el torn al tpv amb identificador (\\d+) i password \"([^\"]*)\", amb un efectiu inicial de €([^\"]*)€$")
    public void loginDonat(long saleAssistantID, String password, double cash) throws Throwable {
        tryCatch(() -> tpvController.login(saleAssistantID, password, cash));
    }

    @Quan("^inicio el torn al tpv amb identificador (\\d+) i password \"([^\"]*)\", amb un efectiu inicial de €([^\"]*)€$")
    public void login(long saleAssistantID, String password, double cash) throws Throwable {
        tryCatch(() -> tpvController.login(saleAssistantID, password, cash));
    }

    @Quan("^desbloquejo el tpv amb el password \"([^\"]*)\"$")
    public void unblock(String password) throws Throwable {
        tryCatch(() -> tpvController.unblock(password));
    }

    @Quan("^incremento l'efectiu de la caixa en €([^\"]*)€$")
    public void increaseCash(double cash) throws Throwable {
        tryCatch(() -> tpvController.getTpv().addCash(cash));
    }

    @Quan("^decremento l'efectiu de la caixa en €([^\"]*)€$")
    public void decreaseCash(double cash) throws Throwable {
        tryCatch(() -> tpvController.getTpv().addCash(-cash));
    }

    @Quan("^intento finalitzar el meu torn, indicant un efectiu final de €([^\"]*)€$")
    public void logout(double cash) throws Throwable {
        tryCatch(() -> tpvController.quadra(cash));
    }

    @Donat("^que el TPV es troba en procés de quadrament$")
    public void quadramentIniciat() throws Throwable {
        tryCatch(() -> tpvController.iniciaQuadrament());
    }

    @Quan("^indico al TPV que inicio el procés de quadrament$")
    public void iniciaQuadrament() throws Throwable {
        tryCatch(() -> tpvController.iniciaQuadrament());
    }

    @Quan("^finalitzo el meu torn amb un desquadrament, amb un efectiu final de €([^\"]*)€$")
    public void desquadrament(double cash) throws Throwable {
        tryCatch(() -> tpvController.addDesquadrament(cash));
    }

    @Quan("^consulto els desquadraments$")
    public void consultaDesquadraments() {
        tryCatch(() -> balances = productManagerController.listBalances());
    }

    @Quan("^consulto els desquadraments de la botiga \"([^\"]*)\"$")
    public void consultaDesquadraments(String shopName) {
        tryCatch(() -> balances = productManagerController.listBalancesByShopName(shopName));
    }

    @Aleshores("^obtinc (\\d+) desquadrament/s")
    public void getNumberOfDesquadraments(int n) {
        assertEquals(n, balances.size());
    }

    @Aleshores("^queda registrat un desquadrament del caixer amb nom \"([^\"]*)\" a la botiga \"([^\"]*)\" d'una quantitat de €([^\"]*)€$")
    public void getLastDesquadrament(String nomCaixer, String nomBotiga, double imbalance) {
        assertEquals(nomCaixer, balancesService.getLastBalance().getSaleAssistantName());
        assertEquals(nomBotiga, balancesService.getLastBalance().getNomBotiga());
        assertEquals(imbalance, balancesService.getLastBalance().getQtt(), DELTA);
    }

    @Aleshores("^obtinc un desquadrament número (\\d+) del caixer amb nom \"([^\"]*)\" a la botiga \"([^\"]*)\" d'una quantitat de €([^\"]*)€$")
    public void checkDesquadraments(int pos, String nomCaixer, String nomBotiga, double imbalance) {
        assertEquals(nomCaixer, balances.get(pos-1).getSaleAssistantName());
        assertEquals(nomBotiga, balances.get(pos-1).getNomBotiga());
        assertEquals(imbalance, balances.get(pos - 1).getQtt(), DELTA);
    }

    //TODO: STUB - NO ÉS FUNCIONALITAT FINAL
    @Quan("^inicio el torn al tpv amb identificador (\\d+) i password \"([^\"]*)\", amb un efectiu inicial de €([^\"]*)€ (\\d+) cops$")
    public void login(long saleAssistantID, String password, double cash, int n) throws Throwable {
        for (int i = 0; i < n; ++i)
            tryCatch(() -> tpvController.login(saleAssistantID, password, cash));
    }

    @Aleshores("^el tpv es troba en estat \"([^\"]*)\"$")
    public void tpvState(String state) throws Throwable {
        assertEquals(state, tpvController.getTpv().getState().toString());
    }

    @Quan("^inicio una nova venta$")
    public void tryStartSale() throws Throwable {
        tryCatch(tpvController::startSale);
    }

    @Donat("^que hi ha una venta iniciada$")
    public void saleStarted() throws Throwable {
        tpvController.startSale();
    }

    @Donat("^un producte amb nom \"([^\"]*)\", preu €([^\"]*)€, iva %([^\"]*)% i codi de barres (\\d+)$")
    public void productCreated(String productName, double price, int vatPct, int barCode) throws Throwable {
        productsService.newProduct(productName, price, vatPct, barCode);
    }

    @Quan("^afegeixo el producte de codi de barres (\\d+) a la venta$")
    public void addProductByBarCode(int barCode) throws Throwable {
        tryCatch(() -> tpvController.addProductByBarCode(barCode));
    }

    @Donat("^que he afegit el producte de codi de barres (\\d+) a la venta$")
    public void productByBarCodeAdded(int barCode) throws Throwable {
        tpvController.addProductByBarCode(barCode);
    }

    @Aleshores("^la venta té (\\d+) (?:línia|línies)$")
    public void la_venta_té_n_linies(int expectedNumberOfLines) throws Throwable {
        assertEquals(expectedNumberOfLines, tpvController.getCurrentSale().getLines().size());
    }

    @Aleshores("^línia de venta (\\d+) és de (\\d+) unitats de \"([^\"]*)\" a €([^\"]*)€ cada una per un total de €([^\"]*)€$")
    public void línia_de_venta_és_de_unitats_de_a_€_cada_una_per_un_total_de_€(int lineNumber, int units, String productName, double unitPrice, double totalPrice) throws Throwable {
        SaleLine sl = tpvController.getCurrentSale().getLines().get(lineNumber - 1);
        assertEquals(units,sl.getAmount());
        assertEquals(unitPrice,sl.getUnitPrice(), DELTA);
        assertEquals(totalPrice,sl.getTotalPrice(), DELTA);
        assertEquals(productName, sl.getName());
    }

    @Aleshores("^el total de la venta actual és de €([^\"]*)€$")
    public void el_total_de_la_venta_actual_és_de_€(double saleTotal) throws Throwable {
        assertEquals(saleTotal, tpvController.getCurrentSale().getTotal(), DELTA);
    }

    @Aleshores("^la pantalla del client del tpv mostra$")
    public void la_pantalla_del_client_del_tpv_mostra(String msg) throws Throwable {
        assertEquals(msg, tpvController.getCustomerScreenMessage());
    }

    @Quan("^indico que el client ha entregat €([^\"]*)€ per a pagar en metàlic$")
    public void cashPayment(double delivered) throws Throwable {
        tryCatch(() -> salesService.newSale(tpvController.getCurrentSale().getLines()));
        tryCatch(() -> change = tpvController.cashPayment(delivered));
    }

    @Quan("^indico que el client paga el total de la venda amb targeta$")
    public void cardPayment() throws Throwable {
        tryCatch(() -> salesService.newSale(tpvController.getCurrentSale().getLines()));
        tryCatch(tpvController::tarjetPayment);
    }

    @Aleshores("^el tpv m'indica que el canvi a retornar és de €([^\"]*)€$")
    public void checkChange(double expectedChange) throws Throwable {
        assertEquals(expectedChange, change, DELTA);
    }

    @Aleshores("^el tpv indica que s'ha pagat amb targeta i tanca la venda$")
    public void setTarget() throws Throwable {

    }

    @Aleshores("^la venta esta iniciada$")
    public void la_venta_esta_iniciada() throws Throwable {
        assertTrue(tpvController.isSaleStarted());
    }

    @Aleshores("^la venda conté el producte amb codi de barres (\\d+)$")
    public void la_venda_conté_el_producte_amb_codi_de_barres(int barCode) {
        assertTrue(tpvController.getCurrentSale().hasProductByBarCode(barCode));
    }

    @Quan("^indico que vull consultar la linia de venda$")
    public void indico_que_vull_consulta_la_linia_de_venda() throws Throwable {
        tryCatch(() -> lines = tpvController.getCurrentSale().getLines());
    }

    @Donat("^que no hi ha cap venda a la linia de venda$")
    public void que_no_hi_ha_cap_venda_a_la_linia_de_venda() throws Throwable {
        assertTrue(tpvController.getCurrentSale().isEmpty());
    }

    @Quan("^Aplico un descompte manual que anomeno \"([^\"]*)\" al producte (\\d+) de la venda amb valor (\\d+)%$")
    public void Aplico_un_descompte_de_tipus_que_anomeno_al_producte_amb_valor_(String name, int prodLine, int percent) throws Throwable {
        tryCatch(() -> tpvController.addNewDiscountToCurrentSale(prodLine, name, (double) percent));
    }

    @Quan("^afegeixo (\\d+) unitats del producte amb codi de barres (\\d+) a la venta$")
    public void addProducteCodiBarresUnitats(int unitats, int codiBarra) throws Throwable {
        tpvController.addProductByBarCode(codiBarra, unitats);
    }

    @Donat("^que estem al panell de gestió del product manager$")
    public void initManagement() throws Throwable {
        productManagerController = new ProductManagerController(balancesService, tpvService,salesService);
    }

    @Donat("^un desquadrament del caixer amb nom \"([^\"]*)\" a la botiga \"([^\"]*)\" d'una quantitat de €([^\"]*)€")
    public void addDesquadrament(String nomCaixer, String shopName, double qtt) throws Throwable {
        balancesService.newBalance(qtt, nomCaixer, shopName);
    }

    @Quan("^afegeixo el producte per nom \"([^\"]*)\" a la venta$")
    public void afegeixo_el_producte_per_nom_a_la_venta(String nom) throws Throwable {
        tryCatch(() -> products = tpvController.addProductByName(nom));
    }

    @Aleshores("^la venda conté el producte amb nom \"([^\"]*)\"$")
    public void la_venda_conté_el_producte_amb_nom(String nomProducte) throws Throwable {
        assertTrue(tpvController.getCurrentSale().hasProductByName(nomProducte));
    }

    @Aleshores("^obtinc (\\d+) noms de productes$")
    public void obtinc_noms_de_productes(int nProductes) throws Throwable {
        assertEquals(nProductes, products.size());
    }

    @Aleshores("^el producte numero (\\d+) es \"([^\"]*)\"$")
    public void el_numero_es(int index, String nomProducte) throws Throwable {
        assertEquals(nomProducte, products.get(index - 1).getName());
    }

    @Aleshores("^la venta no esta iniciada$")
    public void la_venta_no_esta_iniciada() throws Throwable {
        assertFalse(tpvController.isSaleStarted());
    }

    @Aleshores("^obtinc una linia de venda amb (\\d+) venda$")
    public void obtinc_una_linia_de_venda_amb_venda(int n) throws Throwable {
        assertEquals(n, lines.size());
    }

    @Aleshores("^obtinc una linia de venda amb el (\\d+)er producte amb nom \"([^\"]*)\", preu €([^\"]*)€ i codi de barres (\\d+)$")
    public void obtinc_una_linia_de_venda_amb_x_producte(int ind, String productName, double price, int barCode) throws Throwable {
        assertTrue(tpvController.getCurrentSale().hasProductByBarCode(barCode));
        SaleLine line = tpvController.getCurrentSale().getLines().get(ind-1);
        assertEquals(line.getName(),productName);
        assertEquals(line.getUnitPrice(), price, DELTA);
    }

    @Quan("^afegeixo (\\d+) unitats del producte per nom \"([^\"]*)\" a la venta$")
    public void afegeixo_unitats_del_producte_per_nom_a_la_venta(int unitatsProducte, String nomProducte) throws Throwable {
        tryCatch(() -> tpvController.addProductByName(nomProducte, unitatsProducte));
    }

    @Donat("^que configurem el password mestre del sistema com a \"([^\"]*)\"$")
    public void setMasterPassword(String newPassword) {
        productManagerController.setMasterPassword(newPassword);
    }

    @Aleshores("^la venta esta tancada$")
    public void la_venta_esta_tancada() throws Throwable {
        assertFalse(tpvController.isSaleStarted());
    }

    @Quan("^creo un nou descompte del tipus percentatge anomenat \"([^\"]*)\" del %([^\"]*)% sobre el producte amb codi de barres (\\d+)$")
    public void newPercentatge(String name, String percent, int codiBarres) throws Throwable {
        tryCatch(() -> tpvController.newDiscountPercent(name, codiBarres, Double.parseDouble(percent)));
    }

    @Quan("^creo un nou descompte del tipus promoció anomenat \"([^\"]*)\" de (\\d+)x(\\d+) sobre el producte amb codi de barres (\\d+)$")
    public void newPromocio(String name, int A, int B, int codiBarres) throws Throwable {
        tryCatch(() -> tpvController.newDiscountPromotion(name, codiBarres, A, B));
    }

    @Quan("^creo un nou descompte del tipus regal anomenat \"([^\"]*)\", on amb la compra del producte amb codi de barres (\\d+) es regala una unitat del producte amb codi de barres (\\d+)$")
    public void newRegal(String name, int codiBarresA, int codiBarresB) throws Throwable {
        tryCatch(() -> tpvController.newDiscountPresent(name, codiBarresA, codiBarresB));
    }

    @Aleshores("^el descompte \"([^\"]*)\" té un valor de €([^\"]*)€$")
    public void checkDescompte(String name, String valor) throws Throwable {
        assertEquals(Double.parseDouble(valor), discountService.findByName(name).getDiscount(), DELTA);
    }

    @I("^que ens desconectem del panell de gestio del product manager$")
    public void que_ens_desconectem_del_panell_de_gestio_del_product_manager() throws Throwable {
        productManagerController = null;
    }
}
