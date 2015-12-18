package edu.upc.essi.gps.ecommerce;

import cucumber.api.java.ca.Aleshores;
import cucumber.api.java.ca.Donat;
import cucumber.api.java.ca.Quan;
import edu.upc.essi.gps.domain.*;
import edu.upc.essi.gps.domain.flow.MoneyFlow;
import edu.upc.essi.gps.domain.lines.SaleLine;
import edu.upc.essi.gps.ecommerce.repositories.*;
import edu.upc.essi.gps.ecommerce.services.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private List<MoneyFlow> moneyFlows;
    private List<Refund> refunds;
    private List<Category> categories;
    private Sale s;

    public void tryCatch(Runnable r) {
        try {
            r.run();
            exception = null;
        } catch (Exception e) {
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
        productManagerController = new ProductManagerController(moneyFlowService, balancesService, tpvService, salesService, refundsService, productsService, saleAssistantService);
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
        s.addProduct(productsService.findByBarCode(barCode), 1);
        salesService.insertSale(s);
    }

    @Donat("^que es fa una venda amb id (\\d+) amb (\\d+) unitats del producte amb codi de barres (\\d+)$")
    public void thereIsASaleWithIdAmountAndBarCode(int id, int unitats, int barCode) throws Throwable {
        Sale s = new Sale(id);
        s.addProduct(productsService.findByBarCode(barCode), unitats);
        salesService.insertSale(s);
    }

    @Donat("^una devolució de (\\d+) unitats del producte amb codi de barres (\\d+) amb motiu \"([^\"]*)\"$")
    public void thereIsARefundWithAmountBarCodeAndReason(int unitats, int codiBarres, String motiu) throws Throwable {
        refundsService.newRefund(productsService.findByBarCode(codiBarres), unitats, motiu);
    }

    @Donat("^un increment de caixa de €(\\d+)€")
    public void thereIsAInFlow(double amount) throws Throwable {
        tryCatch(() -> tpvController.addFlow(amount));
    }

    @Donat("^un decrement de caixa de €(\\d+)€")
    public void thereIsAOutFlow(double amount) throws Throwable {
        tryCatch(() -> tpvController.removeFlow(amount));
    }

    @Donat("^un traspàs €(\\d+)€ de la botiga \"([^\"]*)\" del tpv número (\\d+) al tpv número (\\d+)$")
    public void thereIsATPVFlow(double amount, String shopName, int tpvNumber1, int tpvNumber2) throws Throwable {
        tryCatch(() -> tpvController.traspasaCash(shopName, tpvNumber1, tpvNumber2, amount));
    }

    @Donat("^que el TPV està bloquejat$")
    public void setBlocked() {
        tryCatch(() -> tpvController.getTpv().setState(TPVState.BLOCKED));
    }

    @Donat("^un caixer amb nom \"([^\"]*)\" i contrasenya \"([^\"]*)\"$")
    public void newCashier(String name, String pass) throws Throwable {
        saleAssistantService.newAssistant(name, pass);
    }

    @Donat("^que hi ha hagut una venda amb id (\\d+) del producte amb codi de barres (\\d+) pagat metode \"([^\"]*)\" el dia \"([^\"]*)\" a les (\\d+) hores, (\\d+) minuts i (\\d+) segons$")
    public void newSaleDate(long id, int barCode, String pagament, String data, int hora, int minut, int segon) throws Throwable {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date d = dateFormat.parse(data);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, hora);
        c.set(Calendar.MINUTE, minut);
        c.set(Calendar.SECOND, segon);
        d = c.getTime();
        salesService.newSale(id, productsService.findByBarCode(barCode), d, pagament);
    }

    @Donat("^que hi ha hagut una venda amb id (\\d+) paga en \"([^\"]*)\" el dia \"([^\"]*)\" a les (\\d+) hores (\\d+) minuts i (\\d+) segons$")
    public void saveSale(int id, String pagament, String data, int hora, int minut, int segon) throws Throwable {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date d = dateFormat.parse(data);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, hora);
        c.set(Calendar.MINUTE, minut);
        c.set(Calendar.SECOND, segon);
        d = c.getTime();
        s = salesService.newSale(id, d, pagament);
    }

    @Donat("^la venta conté el producte amb codi de barres (\\d+)$")
    public void addProductByBarcode(int barCode) throws Throwable {
        s.addProduct(productsService.findByBarCode(barCode), 1);

    }

    ////////////////////////////////////////////////////// @Quan //////////////////////////////////////////////////////

    @Quan("^inicio el torn al tpv amb identificador (\\d+) i password \"([^\"]*)\", amb un efectiu inicial de €([^\"]*)€$")
    public void login(long saleAssistantID, String password, double cash) throws Throwable {
        tryCatch(() -> tpvController.login(saleAssistantID, password, cash));
    }

    @Quan("^bloquejo el tpv amb el password \"([^\"]*)\"$")
    public void block(String password) throws Throwable {
        tryCatch(() -> tpvController.block(password));
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

    @Quan("^Aplico un descompte manual al producte (\\d+) de la venda amb valor (\\d+)%$")
    public void applyDiscount(int prodLine, int percent) throws Throwable {
        tryCatch(() -> tpvController.addNewDiscountToCurrentSale(prodLine, (double) percent));
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

    @Quan("^creo un nou descompte del tipus percentatge anomenat \"([^\"]*)\" del %([^\"]*)% sobre els productes amb codi de barres \"([^\"]*)\"$")
    public void newPercentatgeCjt(String name, double percent, String products) throws Throwable {
        String llistaBarcodes[] = products.split(",");
        List<Product> productList = getProductsFromBarCodes(llistaBarcodes);
        discountService.newProductPercentDiscount(productList, name, percent);
    }

    private List<Product> getProductsFromBarCodes(String[] l) {
        List<Product> productList = new ArrayList<>();
        for (String s : l) {
            Product product = productsService.findByBarCode(Integer.valueOf(s));
            productList.add(product);
        }
        return productList;
    }

    @Quan("^creo un nou descompte del tipus percentatge anomenat \"([^\"]*)\" del %([^\"]*)% sobre els productes de la categoria \"([^\"]*)\"$")
    public void newPercentatgeCat(String name, double percent, String cat) throws Throwable {
        Category category = categoriesService.findByName(cat);
        discountService.newProductPercentDiscount(category, name, percent);
    }

    @Quan("^creo un nou descompte del tipus promoció anomenat \"([^\"]*)\" de (\\d+)x(\\d+) sobre el producte amb codi de barres (\\d+)$")
    public void newPromocio(String name, int A, int B, int codiBarres) throws Throwable {
        tryCatch(() -> tpvController.newDiscountPromotion(name, codiBarres, A, B));
    }

    @Quan("^creo un nou descompte del tipus promoció anomenat \"([^\"]*)\" de (\\d+)x(\\d+) sobre els productes amb codi de barres \"([^\"]*)\"$")
    public void newPromocioCjt(String name, int A, int B, String codiBarres) throws Throwable {
        String llistaBarcodes[] = codiBarres.split(",");
        discountService.newProductPromotionDiscount(getProductsFromBarCodes(llistaBarcodes), name, A, B);
    }

    @Quan("^creo un nou descompte del tipus promoció anomenat \"([^\"]*)\" de (\\d+)x(\\d+) sobre els productes de la categoria \"([^\"]*)\"$")
    public void newPromocioCat(String name, int A, int B, String cat) throws Throwable {
        Category category = categoriesService.findByName(cat);
        discountService.newProductPromotionDiscount(category, name, A, B);
    }

    @Quan("^creo un nou descompte del tipus regal anomenat \"([^\"]*)\", on amb la compra del producte amb codi de barres (\\d+) es regala una unitat del producte amb codi de barres (\\d+)$")
    public void newRegal(String name, int codiBarresRequerit, int codiBarresRegal) throws Throwable {
        tryCatch(() -> tpvController.newDiscountPresent(name, codiBarresRequerit, codiBarresRegal));
    }

    @Quan("^creo un nou descompte del tipus regal anomenat \"([^\"]*)\", on amb la compra del producte d'algun dels productes amb codi de barres \"([^\"]*)\" es regala una unitat de cadascun dels poductes amb codi de barres \"([^\"]*)\"$")
    public void newRegalCjt(String name, String codiBarresRequerits, String codiBarresRegal) throws Throwable {
        String l[] = codiBarresRequerits.split(",");
        String l2[] = codiBarresRegal.split(",");
        discountService.newProductPresentDiscount(getProductsFromBarCodes(l2), name, getProductsFromBarCodes(l));
    }

    @Quan("^creo un nou descompte del tipus regal anomenat \"([^\"]*)\", on amb la compra del producte d'algun dels productes de la categoria \"([^\"]*)\" es regala una unitat de cadascun dels poductes de la categoria \"([^\"]*)\"$")
    public void newRegalCat(String name, String catRequerida, String catRegal) throws Throwable {
        Category requiredCat = categoriesService.findByName(catRequerida);
        Category regalCat = categoriesService.findByName(catRegal);
        discountService.newProductPresentDiscount(regalCat, name, requiredCat);
    }

    @Quan("^indico que el client ha entregat €([^\"]*)€ per a pagar en metàlic$")
    public void clientHasDeliveredInCash(double delivered) throws Throwable {
        tryCatch(() -> change = tpvController.cashPayment(delivered));
    }

    @Quan("^faig una devolució de (\\d+) unitat/s del producte amb codi de barres (\\d+) de la venta (\\d+) amb el motiu \"([^\"]*)\"$")
    public void addDevolucio(int unitats, int barCode, long idVenda, String reason) throws Throwable {
        tryCatch(() -> refund = tpvController.addRefund(unitats, barCode, idVenda, reason));
    }

    @Quan("^consulto les vendes$")
    public void getSales() throws Throwable {
        tryCatch(() -> sales = productManagerController.listSales());
    }

    @Quan("^vull llistar les vendes pagades en \"([^\"]*)\"$")
    public void getSalesType(String type) throws Throwable {
        tryCatch(() -> sales = productManagerController.llistaVentesPerTipusPagament(type));
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
        tryCatch(tpvController::cancelCurrentSale);
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
    public void getMoneyFlow() throws Throwable {
        tryCatch(() -> moneyFlows = productManagerController.listMoneyFlows());
    }

    @Quan("^consulto els fluxos de diners entre caixes del tipus \"([^\"]*)\"$")
    public void getMoneyFlowByKind(String flowKind) throws Throwable {
        tryCatch(() -> moneyFlows = productManagerController.listMoneyFlowsByKind(flowKind));
    }

    @Quan("^consulto els productes del sistema$")
    public void getProducts() throws Throwable {
        tryCatch(() -> products = productManagerController.getProducts());
    }

    @Quan("afegeixo un producte amb nom \"([^\"]*)\", preu €([^\"]*)€, iva %([^\"]*)% i codi de barres (\\d+)$")
    public void addNewProduct(String name, double price, double vatPct, int barCode) throws Throwable {
        tryCatch(() -> productManagerController.addNewProduct(name, price, vatPct, barCode));

    }

    @Quan("^consulto els caixers del sistema$")
    public void getSaleAssistants() throws Throwable {
        tryCatch(productManagerController::getSaleAssistants);
    }

    @Quan("^afegeixo un caixer amb nom \"([^\"]*)\" i contrasenya \"([^\"]*)\"$")
    public void newAssistant(String name, String pass) throws Throwable {
        tryCatch(() -> productManagerController.newSaleAssistant(name, pass));
    }

    @Quan("^vull llistar les vendes del dia \"([^\"]*)\"$")
    public void listByDay(String data) throws Throwable {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date d = dateFormat.parse(data);

        sales = productManagerController.listSalesByDate(d);
    }

    @Quan("^vui llistar les vendes entre el dia \"([^\"]*)\" i el dia \"([^\"]*)\"$")
    public void listSalePeriod(String data1, String data2) throws Throwable {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date d1 = dateFormat.parse(data1);
        Date d2 = dateFormat.parse(data2);
        sales = productManagerController.listSalesPeriod(d1, d2);
    }

    //////////////////////////////////////////////////// @Aleshores ////////////////////////////////////////////////////

    @Aleshores("^obtinc un error que diu: \"([^\"]*)\"$")
    public void checkErrorMessage(String msg) throws Throwable {
        assertNotNull(exception);
        assertEquals(msg, exception.getMessage());
    }

    @Aleshores("^hi ha (\\d+) productes al sistema")
    public void checkQttProducts(int n) throws Throwable {
        assertEquals(n, productsService.list().size());
    }

    @Aleshores("^el producte número (\\d+) té per nom \"([^\"]*)\", preu €([^\"]*)€, IVA %([^\"]*)% i codi de barres (\\d+)$")
    public void checkProduct(int n, String name, double price, double pct, int barCode) throws Throwable {
        Product p = productsService.list().get(n - 1);
        assertEquals(name, p.getName());
        assertEquals(barCode, p.getBarCode());
        assertEquals(pct, p.getVatPct(), DELTA);
        assertEquals(price, p.getPrice(), DELTA);
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
        assertEquals(units, sl.getAmount());
        assertEquals(unitPrice, sl.getUnitPrice(), DELTA);
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
        SaleLine line = tpvController.getCurrentSale().getLines().get(ind - 1);
        assertEquals(line.getName(), productName);
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

    @Aleshores("^obtinc (\\d+) flux de tipus \"([^\"]*)\" de total €(\\d+)€$")
    public void checkFlux(int amount, String fluxKind, double cash) throws Throwable {
        assertEquals(amount, moneyFlowService.listByKind(fluxKind).size());
        double total = 0.0;
        for (int i = 0; i < amount; i++) {
            assertEquals(fluxKind, moneyFlowService.listByKind(fluxKind).get(i).getKind());
            total += moneyFlowService.listByKind(fluxKind).get(i).getAmount();
        }
        assertEquals(cash, total, DELTA);
    }

    @Aleshores("^obtinc (\\d+) fluxes de diners")
    public void checkNFlux(int n) throws Throwable {
        assertEquals(n, moneyFlows.size());
    }

    @Aleshores("^obtinc un flux (\\d+) de tipus \"([^\"]*)\" d'una quantitat de €(\\d+)€ de \"([^\"]*)\" a \"([^\"]*)\"$")
    public void checkFluxElement(int i, String tipus, int qtt, String origen, String desti) throws Throwable {
        MoneyFlow f = moneyFlows.get(i-1);
        assertEquals(tipus, f.getKind());
        assertEquals(qtt, f.getAmount(), DELTA);
        assertEquals(origen, f.getOrigin());
        assertEquals(desti, f.getDestiny());
    }

    @Aleshores("^hi ha (\\d+) caixer al sistema$")
    public void checkCashiersSize(int n) throws Throwable {
        assertEquals(n, saleAssistantService.list().size());
    }

    @Aleshores("^el caixer té per nom \"([^\"]*)\" i la seva contrasenya és \"([^\"]*)\"$")
    public void checkCashier(String name, String pass) throws Throwable {
        List<SaleAssistant> list = saleAssistantService.list();
        SaleAssistant a = list.get(list.size() - 1);
        assertEquals(name, a.getName());
        assertEquals(pass, a.getEncryptedPass());
    }

    @Aleshores("^obtinc (\\d+) venda$")
    public void checkSizeDate(int size) throws Throwable {
        assertEquals(size, sales.size());
    }

    @Aleshores("^la venda (\\d+) conté el producte amb nom \"([^\"]*)\" venut el dia \"([^\"]*)\" a les (\\d+) hores (\\d+) minuts i (\\d+) segons amb import total €(\\d+)€$")
    public void checkSaleDate(int number, String name, String date,int hora,int minuts,int segons, double price) throws Throwable {
        assertEquals(name, sales.get(number-1).getLines().get(0).getName());
        assertEquals(price, sales.get(number-1).getTotal(), DELTA);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date d = dateFormat.parse(date);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        assertEquals(c.get(Calendar.DATE), sales.get(number - 1).getDay());
        assertEquals(c.get(Calendar.MONTH), sales.get(number-1).getMonth());
        assertEquals(c.get(Calendar.YEAR), sales.get(number-1).getYear());
        assertEquals(hora, sales.get(number-1).getHour());
        assertEquals(minuts, sales.get(number-1).getMinutes());
        assertEquals(segons, sales.get(number-1).getSeconds());

    }

    @Aleshores("^la venda (\\d+) esta feta el dia \"([^\"]*)\" a les (\\d+) hores (\\d+) minuts i (\\d+) segons amb import total €(\\d+)€$")
    public void checkSale(int number, String data, int hora, int minuts, int segons, int preu) throws Throwable {
        assertEquals(preu, sales.get(number-1).getTotal(), DELTA);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date d = dateFormat.parse(data);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        assertEquals(c.get(Calendar.DATE), sales.get(number-1).getDay());
        assertEquals(c.get(Calendar.MONTH), sales.get(number-1).getMonth());
        assertEquals(c.get(Calendar.YEAR), sales.get(number - 1).getYear());
        assertEquals(hora, sales.get(number-1).getHour());
        assertEquals(minuts, sales.get(number-1).getMinutes());
        assertEquals(segons, sales.get(number-1).getSeconds());
    }

    @Aleshores("^la venda (\\d+) conté el producte (\\d+) amb nom \"([^\"]*)\"$")
    public void checkProductByNameOnSale(int number,int line, String name) throws Throwable {
        assertEquals(name, sales.get(number-1).getLines().get(line-1).getName());
    }
}
