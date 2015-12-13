package edu.upc.essi.gps.ecommerce;

import cucumber.api.PendingException;
import cucumber.api.java.ca.Aleshores;
import cucumber.api.java.ca.Donat;
import cucumber.api.java.ca.Quan;
import edu.upc.essi.gps.domain.*;
import edu.upc.essi.gps.domain.flow.MoneyFlow;
import edu.upc.essi.gps.domain.lines.SaleLine;
import edu.upc.essi.gps.ecommerce.repositories.*;
import edu.upc.essi.gps.ecommerce.services.*;

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
    private RefundsService refundsService = new RefundsService(new RefundsRepository());
    private CategoriesService categoriesService = new CategoriesService(new CategoriesRepository());
    private MoneyFlowService moneyFlowService = new MoneyFlowService(new MoneyFlowRepository());
    private Exception exception;
    private TPVController tpvController;
    private ProductManagerController productManagerController;
    private double change;
    private double refund;
    private List<SaleLine> lines;
    private List<Product> products;
    private List<Balance> balances;
    private List<Sale> sales;
    private List<Refund> refunds;
    private List<Category> categories;
    private List<MoneyFlow> moneyFlows;

    public void tryCatch(Runnable r){
        try {
            r.run();
            exception = null;
        } catch (Exception e){
            exception = e;
        }
    }

    ////////////////////////////////////////////////////// @Donat //////////////////////////////////////////////////////


    @Donat("^que estem al tpv número (\\d+) de la botiga \"([^\"]*)\"$")
    public void setupPos(int posNumber, String shop) throws Throwable {
        tpvService.newTPV(shop, posNumber);
        tpvController = new TPVController(moneyFlowService, categoriesService, refundsService, salesService, productsService, saleAssistantService, tpvService, balancesService, discountService, shop, posNumber);
    }

    @Donat("^que el \"([^\"]*)\" s'ha registrat al sistema amb password \"([^\"]*)\" i rep l'identificador (\\d+)$")
    public void register(String name, String password, long saleAssistantID) throws Throwable {
        tryCatch(() -> saleAssistantService.insert(name, password, saleAssistantID));
    }

    @Donat("^que s'inica el torn al tpv amb identificador (\\d+) i password \"([^\"]*)\", amb un efectiu inicial de €([^\"]*)€$")
    public void loginDonat(long saleAssistantID, String password, double cash) throws Throwable {
        tryCatch(() -> tpvController.login(saleAssistantID, password, cash));
    }

    @Donat("^que el TPV es troba en procés de quadrament$")
    public void quadramentIniciat() throws Throwable {
        tryCatch(tpvController::iniciaQuadrament);
    }

    @Donat("^que hi ha una venta iniciada$")
    public void saleStarted() throws Throwable {
        tpvController.startSale();
    }

    @Donat("^un producte amb nom \"([^\"]*)\", preu €([^\"]*)€, iva %([^\"]*)% i codi de barres (\\d+)$")
    public void productCreated(String productName, double price, int vatPct, int barCode) throws Throwable {
        productsService.newProduct(productName, price, vatPct, barCode);
    }

    @Donat("^que he afegit el producte de codi de barres (\\d+) a la venta$")
    public void productByBarCodeAdded(int barCode) throws Throwable {
        tpvController.addProductByBarCode(barCode);
    }

    @Donat("^que estem al panell de gestió del product manager$")
    public void initManagement() throws Throwable {
        productManagerController = new ProductManagerController(moneyFlowService, balancesService, tpvService, salesService, refundsService);
    }

    @Donat("^un desquadrament del caixer amb nom \"([^\"]*)\" a la botiga \"([^\"]*)\" d'una quantitat de €([^\"]*)€")
    public void addDesquadrament(String nomCaixer, String shopName, double qtt) throws Throwable {
        balancesService.newBalance(qtt, nomCaixer, shopName);
    }

    @Donat("^que configurem el password mestre del sistema com a \"([^\"]*)\"$")
    public void setMasterPassword(String newPassword) {
        productManagerController.setMasterPassword(newPassword);
    }

    @Donat("^que ens desconectem del panell de gestio del product manager$")
    public void disconnectProductManager() throws Throwable {
        productManagerController = null;
    }

    @Donat("^el producte amb codi de barres (\\d+) esta pagat en targeta$")
    public void productIsPaidWithCard(int barCode) throws Throwable {
        tpvController.startSale();
        tpvController.addProductByBarCode(barCode);
        tpvController.tarjetPayment();
    }

    @Donat("^el producte amb codi de barres (\\d+) esta pagat en metalic: €(\\d+)€$")
    public void productIsPaidWithCash(int barCode, int pagat) throws Throwable {
        tpvController.startSale();
        tpvController.addProductByBarCode(barCode);
        tpvController.cashPayment(pagat);
    }

    @Donat("^que hi ha hagut una venda del producte amb codi de barres (\\d+) pagat en metode \"([^\"]*)\"$")
    public void createSaleProductManager(int barCode, String type) throws Throwable {
        salesService.newSale(productsService.findByBarCode(barCode), type);
    }

    @Donat("^que es fa una venda amb id (\\d+) del producte amb codi de barres (\\d+)$")
    public void thereIsASaleWithIdAndBarCode(long id, int barCode) throws Throwable {
        Sale s = new Sale(id);
        s.addProduct(productsService.findByBarCode(barCode), 1, null);
        salesService.insertSale(s);
    }

    @Donat("^que es fa una venda amb id (\\d+) amb (\\d+) unitats del producte amb codi de barres (\\d+)$")
    public void thereIsASaleWithIdAmountAndBarCode(int id, int unitats, int barCode) throws Throwable {
        Sale s = new Sale(id);
        s.addProduct(productsService.findByBarCode(barCode), unitats, null);
        salesService.insertSale(s);
    }

    @Donat("^una devolució de (\\d+) unitats del producte amb codi de barres (\\d+) amb motiu \"([^\"]*)\"$")
    public void thereIsARefundWithAmountBarCodeAndReason(int unitats, int codiBarres, String motiu) throws Throwable {
        refundsService.newRefund(productsService.findByBarCode(codiBarres), unitats, motiu);
    }

    ////////////////////////////////////////////////////// @Quan //////////////////////////////////////////////////////

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
        tryCatch(() -> tpvController.addFlow(cash));
    }

    @Quan("^decremento l'efectiu de la caixa en €([^\"]*)€$")
    public void decreaseCash(double cash) throws Throwable {
        tryCatch(() -> tpvController.removeFlow(cash));
    }

    @Quan("^intento finalitzar el meu torn, indicant un efectiu final de €([^\"]*)€$")
    public void logout(double cash) throws Throwable {
        tryCatch(() -> tpvController.quadra(cash));
    }

    @Quan("^indico al TPV que inicio el procés de quadrament$")
    public void iniciaQuadrament() throws Throwable {
        tryCatch(tpvController::iniciaQuadrament);
    }

    @Quan("^finalitzo el meu torn amb un desquadrament, amb un efectiu final de €([^\"]*)€$")
    public void desquadrament(double cash) throws Throwable {
        tryCatch(() -> tpvController.addDesquadrament(cash));
    }

    @Quan("^consulto els desquadraments$")
    public void consultaDesquadraments() {
        tryCatch(() -> balances = productManagerController.listBalances());
    }

    @Quan("^consulto les devolucions$")
    public void consultaDevolucions() {
        tryCatch(() -> refunds = productManagerController.listRefunds());
    }

    @Quan("^consulto els desquadraments de la botiga \"([^\"]*)\"$")
    public void consultaDesquadraments(String shopName) {
        tryCatch(() -> balances = productManagerController.listBalancesByShopName(shopName));
    }

    @Quan("^inicio el torn al tpv amb identificador (\\d+) i password \"([^\"]*)\", amb un efectiu inicial de €([^\"]*)€ (\\d+) cops$")
    public void login(long saleAssistantID, String password, double cash, int n) throws Throwable {
        for (int i = 0; i < n; ++i)
            tryCatch(() -> tpvController.login(saleAssistantID, password, cash));
    }

    @Quan("^inicio una nova venta$")
    public void tryStartSale() throws Throwable {
        tryCatch(tpvController::startSale);
    }

    @Quan("^afegeixo el producte de codi de barres (\\d+) a la venta$")
    public void addProductByBarCode(int barCode) throws Throwable {
        tryCatch(() -> tpvController.addProductByBarCode(barCode));
    }

    @Quan("^indico que vull consultar la linia de venda$")
    public void indicateToGetSale() throws Throwable {
        tryCatch(() -> lines = tpvController.getCurrentSale().getLines());
    }

    @Quan("^Aplico un descompte manual que anomeno \"([^\"]*)\" al producte (\\d+) de la venda amb valor (\\d+)%$")
    public void applyDiscount(String name, int prodLine, int percent) throws Throwable {
        tryCatch(() -> tpvController.addNewDiscountToCurrentSale(prodLine, name, (double) percent));
    }

    @Quan("^afegeixo (\\d+) unitats del producte amb codi de barres (\\d+) a la venta$")
    public void addProducteCodiBarresUnitats(int unitats, int codiBarra) throws Throwable {
        tryCatch(() -> tpvController.addProductByBarCode(codiBarra, unitats));
    }

    @Quan("^afegeixo el producte per nom \"([^\"]*)\" a la venta$")
    public void addProductByName(String nom) throws Throwable {
        tryCatch(() -> products = tpvController.addProductByName(nom));
    }

    @Quan("^afegeixo (\\d+) unitats del producte per nom \"([^\"]*)\" a la venta$")
    public void addAmountToProductByName(int unitatsProducte, String nomProducte) throws Throwable {
        tryCatch(() -> tpvController.addProductByName(nomProducte, unitatsProducte));
    }

    @Quan("^creo un nou descompte del tipus percentatge anomenat \"([^\"]*)\" del %([^\"]*)% sobre el producte amb codi de barres (\\d+)$")
    public void newPercentatge(String name, double percent, int codiBarres) throws Throwable {
        tryCatch(() -> tpvController.newDiscountPercent(name, codiBarres, percent));
    }

    @Quan("^creo un nou descompte del tipus promoció anomenat \"([^\"]*)\" de (\\d+)x(\\d+) sobre el producte amb codi de barres (\\d+)$")
    public void newPromocio(String name, int A, int B, int codiBarres) throws Throwable {
        tryCatch(() -> tpvController.newDiscountPromotion(name, codiBarres, A, B));
    }

    @Quan("^creo un nou descompte del tipus regal anomenat \"([^\"]*)\", on amb la compra del producte amb codi de barres (\\d+) es regala una unitat del producte amb codi de barres (\\d+)$")
    public void newRegal(String name, int codiBarresRequerit, int codiBarresRegal) throws Throwable {
        tryCatch(() -> tpvController.newDiscountPresent(name, codiBarresRequerit, codiBarresRegal));
    }

    @Quan("^indico que el client ha entregat €([^\"]*)€ per a pagar en metàlic$")
    public void clientHasDeliveredInCash(double delivered) throws Throwable {
        tryCatch(() -> change = tpvController.cashPayment(delivered));
    }

    @Quan("^faig una devolució de (\\d+) unitat/s del producte amb codi de barres (\\d+) de la venta (\\d+) amb el motiu \"([^\"]*)\"$")
    public void addDevolucio(int unitats, int barCode, long idVenda, String reason) throws Throwable {
        tryCatch(()-> refund = tpvController.addRefund(unitats, barCode, idVenda, reason));
    }

    @Quan("^consulto les vendes$")
    public void getSales() throws Throwable {
        tryCatch(() -> sales = productManagerController.listSales());
    }

    @Quan("^vull llistar les vendes pagades en \"([^\"]*)\"$")
    public void getSalesType(String type) throws Throwable {
        tryCatch(() -> productManagerController.llistaVentesPerTipusPagament(type));
    }

    @Quan("^vull llistar totes les vendes$")
    public void getAllSales() throws Throwable {
        tryCatch(() -> sales = productManagerController.listSales());
    }

    @Quan("^indico que el client paga el total de la venda amb targeta$")
    public void clientHasDeliveredInCard() throws Throwable {
        tryCatch(tpvController::tarjetPayment);
    }

    @Quan("^es realitza un traspàs a la botiga \"([^\"]*)\" de €([^\"]*)€ del terminal (\\d+) al terminal (\\d+)$")
    public void traspasaCash(String name, double cash, int posSource, int posTarget) throws Throwable {
        tryCatch(() -> tpvController.traspasaCash(name, posSource, posTarget, cash));
    }

    @Quan("^indico que vull cancelar la venta actual$")
    public void cancelCurrentSale() {
        tryCatch(() -> tpvController.cancelCurrentSale());
    }

    @Quan("^afegeixo (\\d+) unitat/s de la línia de venda (\\d+)$")
    public void addItemsSaleLine(int units, int line) {
        tryCatch(() -> tpvController.addItemsSaleLine(units, line));
    }

    @Quan("^elimino (\\d+) unitat/s de la línia de venda (\\d+)$")
    public void deleteItemsSaleLine(int units, int line) {
        tryCatch(() -> tpvController.deleteItemsSaleLine(units, line));
    }

    @Quan("^elimino la línia de venda (\\d+)$")
    public void deleteSaleLine(int line) {
        tryCatch(() -> tpvController.deleteSaleLine(line));
    }

    @Quan("^creo una nova categoria amb nom \"([^\"]*)\"$")
    public void createCategory(String catNom) throws Throwable {
        tryCatch(() -> tpvController.newCategory(catNom));
    }

    @Quan("^consulto les categories$")
    public void getAllCategories() throws Throwable {
        tryCatch(() -> categories = tpvController.listCategories());
    }

    @Quan("^afegeixo el producte amb codi de barres (\\d+) a la categoria \"([^\"]*)\"$")
    public void addProductToCategory(int codiBarres, String catNom) throws Throwable {
        Product p = productsService.findByBarCode(codiBarres);
        tryCatch(() -> tpvController.addProductToCategory(p, catNom));
    }

    @Quan("^consulto els productes de la categoria \"([^\"]*)\"$")
    public void checkProductByCategory(String catName) throws Throwable {
        tryCatch(() -> products = tpvController.listProductsByCategory(catName));
    }

    @Quan("^consulto els fluxos de diners entre caixes$")
    public void getMonewFlow() throws Throwable {
        tryCatch(() -> moneyFlows = productManagerController.listMoneyFlows());
    }

    //////////////////////////////////////////////////// @Aleshores ////////////////////////////////////////////////////

    @Aleshores("^obtinc un error que diu: \"([^\"]*)\"$")
    public void checkErrorMessage(String msg) throws Throwable {
        assertNotNull(exception);
        assertEquals(msg, exception.getMessage());
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

    @Aleshores("^obtinc (\\d+) desquadrament/s")
    public void getNumberOfDesquadraments(int n) {
        assertEquals(n, balances.size());
    }

    @Aleshores("^queda registrat un desquadrament del caixer amb nom \"([^\"]*)\" a la botiga \"([^\"]*)\" d'una quantitat de €([^\"]*)€$")
    public void getLastDesquadrament(String nomCaixer, String nomBotiga, double imbalance) {
        assertEquals(nomCaixer, tpvController.getLastBalance().getSaleAssistantName());
        assertEquals(nomBotiga, tpvController.getLastBalance().getNomBotiga());
        assertEquals(imbalance, tpvController.getLastBalance().getQtt(), DELTA);
    }

    @Aleshores("^obtinc un desquadrament número (\\d+) del caixer amb nom \"([^\"]*)\" a la botiga \"([^\"]*)\" d'una quantitat de €([^\"]*)€$")
    public void checkDesquadraments(int pos, String nomCaixer, String nomBotiga, double imbalance) {
        assertEquals(nomCaixer, balances.get(pos - 1).getSaleAssistantName());
        assertEquals(nomBotiga, balances.get(pos - 1).getNomBotiga());
        assertEquals(imbalance, balances.get(pos - 1).getQtt(), DELTA);
    }

    @Aleshores("^el tpv es troba en estat \"([^\"]*)\"$")
    public void tpvState(String state) throws Throwable {
        assertEquals(state, tpvController.getTpv().getState().toString());
    }

    @Aleshores("^la venta té (\\d+) (?:línia|línies)$")
    public void saleHasNLines(int expectedNumberOfLines) throws Throwable {
        assertEquals(expectedNumberOfLines, tpvController.getCurrentSale().getLines().size());
    }

    @Aleshores("^línia de venta (\\d+) és de (\\d+) unitats de \"([^\"]*)\" a €([^\"]*)€ cada una per un total de €([^\"]*)€$")
    public void checkSaleLine(int lineNumber, int units, String productName, double unitPrice, double totalPrice) throws Throwable {
        SaleLine sl = tpvController.getCurrentSale().getLines().get(lineNumber - 1);
        assertEquals(units,sl.getAmount());
        assertEquals(unitPrice,sl.getUnitPrice(), DELTA);
        assertEquals(totalPrice, sl.getTotalPrice(), DELTA);
        assertEquals(productName, sl.getName());
    }

    @Aleshores("^el total de la venta actual és de €([^\"]*)€$")
    public void checkTotalPrice(double saleTotal) throws Throwable {
        assertEquals(saleTotal, tpvController.calculateTotal(), DELTA);
    }

    @Aleshores("^la pantalla del client del tpv mostra$")
    public void checkClientScreen(String msg) throws Throwable {
        assertEquals(msg, tpvController.getCustomerScreenMessage());
    }

    @Aleshores("^el tpv m'indica que el canvi a retornar és de €([^\"]*)€$")
    public void checkChange(double expectedChange) throws Throwable {
        assertEquals(expectedChange, change, DELTA);
    }

    @Aleshores("^la venta esta iniciada$")
    public void checkIniSale() throws Throwable {
        assertTrue(tpvController.isSaleStarted());
    }

    @Aleshores("^la venda conté el producte amb codi de barres (\\d+)$")
    public void checkSaleHasProductByBarCode(int barCode) {
        assertTrue(tpvController.getCurrentSale().hasProductByBarCode(barCode));
    }

    @Aleshores("^la venda conté el producte amb nom \"([^\"]*)\"$")
    public void checkSaleHasProductByName(String nomProducte) throws Throwable {
        assertTrue(tpvController.getCurrentSale().hasProductByName(nomProducte));
    }

    @Aleshores("^obtinc (\\d+) noms de productes$")
    public void checkProductSize(int nProductes) throws Throwable {
        assertEquals(nProductes, products.size());
    }

    @Aleshores("^el producte numero (\\d+) es \"([^\"]*)\"$")
    public void checkProductByIndex(int index, String nomProducte) throws Throwable {
        assertEquals(nomProducte, products.get(index - 1).getName());
    }

    @Aleshores("^la venta no esta iniciada$")
    public void checkNotIniSale() throws Throwable {
        assertFalse(tpvController.isSaleStarted());
    }

    @Aleshores("^obtinc una linia de venda amb (\\d+) venda$")
    public void checkSaleLineSize(int n) throws Throwable {
        assertEquals(n, lines.size());
    }

    @Aleshores("^obtinc una linia de venda amb el (\\d+)er producte amb nom \"([^\"]*)\", preu €([^\"]*)€ i codi de barres (\\d+)$")
    public void getSaleLineWithProductByBarCode(int ind, String productName, double price, int barCode) throws Throwable {
        assertTrue(tpvController.getCurrentSale().hasProductByBarCode(barCode));
        SaleLine line = tpvController.getCurrentSale().getLines().get(ind-1);
        assertEquals(line.getName(),productName);
        assertEquals(line.getUnitPrice(), price, DELTA);
    }

    @Aleshores("^la venta esta tancada$")
    public void checkClosedSale() throws Throwable {
        assertFalse(tpvController.isSaleStarted());
    }

    @Aleshores("^el TPV m'indica que haig de retornar una quantitat de €([^\"]*)€$")
    public void checkRefund(double expectedRefund) throws Throwable {
        assertEquals(expectedRefund, refund, DELTA);
    }

    @Aleshores("^obtinc el producte amb nom \"([^\"]*)\" pagat en \"([^\"]*)\"$")
    public void checkNameAndType(String nom, String tipus) throws Throwable {
            assertEquals(productManagerController.llistaVentesPerTipusPagament(tipus).get(0).getLines().get(0).getName(), nom);
    }

    @Aleshores("^obtinc el producte amb nom \"([^\"]*)\"$")
    public void checkName(String name) throws Throwable {
        assertTrue(sales.get(0).hasProductByName(name));
    }

    @Aleshores("^el sistema enregistra una devolució amb (\\d+) linia/es de devolució de (\\d+) unitats del producte amb codi de barres (\\d+) amb motiu \"([^\"]*)\"$")
    public void getLastRefund(int linia, int unitats, int barCode, String motiu) throws Throwable {
        List<RefundLine> linies = refundsService.list().get(refundsService.list().size() - 1).getLiniesDevolucions();
        assertEquals(linia, linies.size());
        assertEquals(unitats, linies.get(linia - 1).getQuantitat());
        assertEquals(barCode, productsService.findById(linies.get(linia - 1).getId()).getBarCode());
        assertEquals(motiu, refundsService.list().get(refundsService.list().size() - 1).getReason());
    }

    @Aleshores("^obtinc (\\d+) devolucions$")
    public void getRefundSize(int quantitat) throws Throwable {
        assertEquals(quantitat, refunds.size());
    }

    @Aleshores("^obtinc una devolució número (\\d+)  amb (\\d+) linia de devolucio de (\\d+) unitats del producte amb nom \"([^\"]*)\" amb motiu \"([^\"]*)\"$")
    public void getSpecifiedRefund(int pos, int linia, int unitats, String name, String motiu) throws Throwable {
        List<RefundLine> linies = refunds.get(pos - 1).getLiniesDevolucions();
        assertEquals(linia, linies.size());
        assertEquals(unitats, linies.get(linia - 1).getQuantitat());
        assertEquals(name, productsService.findById(linies.get(linia - 1).getId()).getName());
        assertEquals(motiu, refunds.get(pos - 1).getReason());
    }

    @Aleshores("^queda registrat una venta d'una quantitat de €([^\"]*)€$")
    public void checkLastSale(double price) throws Throwable {
        assertEquals(price, tpvController.getLastSale().getTotal(), DELTA);
    }

    @Aleshores("^el tpv té un efectiu total de €([^\"]*)€$")
    public void checkCash(double cash) throws Throwable {
        assertEquals(cash, tpvController.getTpv().getCash(), DELTA);
    }

    @Aleshores("^el tpv (\\d+) de la botiga \"([^\"]*)\" té un efectiu total de €([^\"]*)€$")
    public void checkTPVCash(int pos, String shop, double cash) {
        assertEquals(cash, tpvService.findByShopPos(shop, pos).getCash(), DELTA);
    }

    @Aleshores("^obtinc una categoria número (\\d+) amb nom \"([^\"]*)\"$")
    public void getSpecificCategory(int pos, String catName) throws Throwable {
        assertEquals(categories.get(pos - 1).getName(), catName);
    }

    @Aleshores("^obtinc un producte número (\\d+) amb nom \"([^\"]*)\"$")
    public void checkProducts(int pos, String prodName) throws Throwable {
        assertEquals(prodName, products.get(pos - 1).getName());
    }
}
