package edu.upc.essi.gps.ecommerce;

import cucumber.api.java.ca.Aleshores;
import cucumber.api.java.ca.Donat;
import cucumber.api.java.ca.Quan;
import edu.upc.essi.gps.domain.Balance;
import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.domain.Sale;
import edu.upc.essi.gps.domain.SaleLine;

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
    private Exception exception;
    private TPVController tpvController;
    private ProductManagerController productManagerController;
    private double change;
    private double refund;
    private List<SaleLine> lines;
    private List<Product> products;
    private List<Balance> balances;
    private List<Sale> sales;

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
        tpvController = new TPVController(refundsService, salesService, productsService, saleAssistantService, tpvService, balancesService, discountService, shop, posNumber);
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
        productManagerController = new ProductManagerController(balancesService, tpvService,salesService);
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
    public void que_ens_desconectem_del_panell_de_gestio_del_product_manager() throws Throwable {
        productManagerController = null;
    }

    @Donat("^el producte amb codi de barres (\\d+) esta pagat en targeta$")
    public void el_producte_amb_codi_de_barres_esta_pagat_en_targeta(int barCode) throws Throwable {
        tpvController.startSale();
        tpvController.addProductByBarCode(barCode);
        tpvController.tarjetPayment();
    }


    @Donat("^el producte amb codi de barres (\\d+) esta pagat en metalic: €(\\d+)€$")
    public void el_producte_amb_codi_de_barres_esta_pagat_en_metalic_€(int barCode, int pagat) throws Throwable {
        tpvController.startSale();
        tpvController.addProductByBarCode(barCode);
        tpvController.cashPayment(pagat);
    }


    @Donat("^que hi ha hagut una venda del producte amb codi de barres (\\d+) pagat en metode \"([^\"]*)\"$")
    public void createSaleProductManager(int barCode, String type) throws Throwable {
        salesService.newSale(productsService.findByBarCode(barCode), type);
    }

    @Donat("^que es fa una venda amb id (\\d+) del producte amb codi de barres (\\d+)$")
    public void que_es_fa_una_venda_amb_id_del_producte_amb_codi_de_barres(long id, int barCode) throws Throwable {
        Sale s = new Sale(id);
        s.addProduct(productsService.findByBarCode(barCode), 1, null);
        salesService.insertSale(s);
    }

    @Donat("^que es fa una venda amb id (\\d+) amb (\\d+) unitats del producte amb codi de barres (\\d+)$")
    public void que_es_fa_una_venda_amb_id_amb_unitats_del_producte_amb_codi_de_barres(int id, int unitats, int barCode) throws Throwable {
        Sale s = new Sale(id);
        s.addProduct(productsService.findByBarCode(barCode), unitats, null);
        salesService.insertSale(s);
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
    public void indico_que_vull_consulta_la_linia_de_venda() throws Throwable {
        tryCatch(() -> lines = tpvController.getCurrentSale().getLines());
    }

    @Quan("^Aplico un descompte manual que anomeno \"([^\"]*)\" al producte (\\d+) de la venda amb valor (\\d+)%$")
    public void Aplico_un_descompte_de_tipus_que_anomeno_al_producte_amb_valor_(String name, int prodLine, int percent) throws Throwable {
        tryCatch(() -> tpvController.addNewDiscountToCurrentSale(prodLine, name, (double) percent));
    }

    @Quan("^afegeixo (\\d+) unitats del producte amb codi de barres (\\d+) a la venta$")
    public void addProducteCodiBarresUnitats(int unitats, int codiBarra) throws Throwable {
        tryCatch(() -> tpvController.addProductByBarCode(codiBarra, unitats));
    }

    @Quan("^afegeixo el producte per nom \"([^\"]*)\" a la venta$")
    public void afegeixo_el_producte_per_nom_a_la_venta(String nom) throws Throwable {
        tryCatch(() -> products = tpvController.addProductByName(nom));
    }

    @Quan("^afegeixo (\\d+) unitats del producte per nom \"([^\"]*)\" a la venta$")
    public void afegeixo_unitats_del_producte_per_nom_a_la_venta(int unitatsProducte, String nomProducte) throws Throwable {
        tryCatch(() -> tpvController.addProductByName(nomProducte, unitatsProducte));
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
    public void newRegal(String name, int codiBarresRequerit, int codiBarresRegal) throws Throwable {
        tryCatch(() -> tpvController.newDiscountPresent(name, codiBarresRequerit, codiBarresRegal));
    }

    @Quan("^indico que el client ha entregat €([^\"]*)€ per a pagar en metàlic$")
    public void indico_que_el_client_ha_entregat_€_€_per_a_pagar_en_metàlic(double delivered) throws Throwable {
        tryCatch(() -> change = tpvController.cashPayment(delivered));
    }

    @Quan("^faig una devolució de (\\d+) unitat/s del producte amb codi de barres (\\d+) de la venta (\\d+) amb el motiu \"([^\"]*)\"$")
    public void addDevolucio(int unitats, int barCode, long idVenda, String reason) throws Throwable {
        tryCatch(()-> refund = tpvController.addRefund(unitats, barCode, idVenda, reason));

    }

    @Quan("^consulto les vendes$")
    public void consulto_les_vendes() throws Throwable {
        tryCatch(() -> sales = productManagerController.listSales());
    }

    @Quan("^vull llistar les vendes pagades en \"([^\"]*)\"$")
    public void getSalesType(String type) throws Throwable {
        tryCatch(() -> productManagerController.llistaVentesPerTipusPagament(type));
    }

    @Quan("^vull llistar totes les vendes$")
    public void vull_llistar_totes_les_vendes() throws Throwable {
        tryCatch(() -> sales = productManagerController.listSales());
    }

    @Quan("^indico que el client paga el total de la venda amb targeta$")
    public void indico_que_el_client_paga_el_total_de_la_venda_amb_targeta() throws Throwable {
        tryCatch(tpvController::tarjetPayment);
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
    public void la_venta_té_n_linies(int expectedNumberOfLines) throws Throwable {
        assertEquals(expectedNumberOfLines, tpvController.getCurrentSale().getLines().size());
    }

    @Aleshores("^línia de venta (\\d+) és de (\\d+) unitats de \"([^\"]*)\" a €([^\"]*)€ cada una per un total de €([^\"]*)€$")
    public void línia_de_venta_és_de_unitats_de_a_€_cada_una_per_un_total_de_€(int lineNumber, int units, String productName, double unitPrice, double totalPrice) throws Throwable {
        SaleLine sl = tpvController.getCurrentSale().getLines().get(lineNumber - 1);
        assertEquals(units,sl.getAmount());
        assertEquals(unitPrice,sl.getUnitPrice(), DELTA);
        assertEquals(totalPrice, sl.getTotalPrice(), DELTA);
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

    @Aleshores("^el tpv m'indica que el canvi a retornar és de €([^\"]*)€$")
    public void checkChange(double expectedChange) throws Throwable {
        assertEquals(expectedChange, change, DELTA);
    }

    @Aleshores("^la venta esta iniciada$")
    public void la_venta_esta_iniciada() throws Throwable {
        assertTrue(tpvController.isSaleStarted());
    }

    @Aleshores("^la venda conté el producte amb codi de barres (\\d+)$")
    public void la_venda_conté_el_producte_amb_codi_de_barres(int barCode) {
        assertTrue(tpvController.getCurrentSale().hasProductByBarCode(barCode));
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

    @Aleshores("^la venta esta tancada$")
    public void la_venta_esta_tancada() throws Throwable {
        assertFalse(tpvController.isSaleStarted());
    }

    @Aleshores("^el descompte \"([^\"]*)\" té un valor de €([^\"]*)€$")
    public void checkDescompte(String name, String valor) throws Throwable {
        assertEquals(Double.parseDouble(valor), discountService.findByName(name).getDiscount(), DELTA);
    }

    @Aleshores("^el TPV m'indica que haig de retornar una quantitat de €([^\"]*)€$")
    public void checkRefund(double expectedRefund) throws Throwable {
        assertEquals(expectedRefund,refund,DELTA);
    }

    @Aleshores("^obtinc el producte amb nom \"([^\"]*)\" pagat en \"([^\"]*)\"$")
    public void checkNameAndType(String nom, String tipus) throws Throwable {
            assertEquals(productManagerController.llistaVentesPerTipusPagament(tipus).get(0).getLines().get(0).getName(), nom);
    }

    @Aleshores("^obtinc el producte amb nom \"([^\"]*)\"$")
    public void checkName(String name) throws Throwable {
        assertTrue(sales.get(0).hasProductByName(name));
    }



}
