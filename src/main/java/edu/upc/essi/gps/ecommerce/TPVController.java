package edu.upc.essi.gps.ecommerce;

import com.sun.istack.internal.NotNull;
import edu.upc.essi.gps.domain.*;
import edu.upc.essi.gps.domain.discounts.Discount;
import edu.upc.essi.gps.domain.discounts.Percent;
import edu.upc.essi.gps.domain.lines.SaleLine;
import edu.upc.essi.gps.ecommerce.services.*;

import java.util.List;

import static edu.upc.essi.gps.utils.Validations.checkNotNull;

public class TPVController {

    public static final String ADD_NON_EXISTING_PRODUCT_ERROR = "No es pot afegir un producte inexistent";

    private final ProductsService productsService;
    private final SaleAssistantService saleAssistantService;
    private final TPVService tpvService;
    private final BalancesService balancesService;
    private final DiscountService discountService;
    private final SalesService salesService;
    private final TPV tpv;
    private final RefundsService refundsService;
    private final CategoriesService categoriesService;

    public TPVController(CategoriesService categoriesService, RefundsService refundsService, SalesService salesService, ProductsService productsService, SaleAssistantService saleAssistantService, TPVService tpvService, BalancesService balancesService, DiscountService discountService, String shop, int pos) {
        this.productsService = productsService;
        this.saleAssistantService = saleAssistantService;
        this.tpvService = tpvService;
        this.balancesService = balancesService;
        this.discountService = discountService;
        this.salesService = salesService;
        this.refundsService = refundsService;
        this.categoriesService = categoriesService;
        tpv = tpvService.findByShopPos(shop, pos);
        tpv.endTurn();
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

    public void login(long saleAssistantId, @NotNull String password, double cash) {
        if (tpv.getState().equals(TPVState.IDLE))
            throw new IllegalStateException("Aquest tpv està en ús per " + tpv.getCurrentSaleAssistant().getName());
        if (tpv.getState().equals(TPVState.BLOCKED))
            throw new IllegalStateException("Aquest tpv està bloquejat");

        checkNotNull(password, "password");

        try {
            saleAssistantService.validate(saleAssistantId, password);
        } catch (IllegalStateException e) {
            tpvService.failLogin(tpv.getId());
            throw e;
        }

        tpvService.successLogin(tpv.getId());
        tpv.newTurn(saleAssistantService.findById(saleAssistantId), cash);
    }

    public void unblock(@NotNull String password) {
        if (!tpv.getState().equals(TPVState.BLOCKED)) throw new IllegalStateException("Aquest tpv no està bloquejat");

        checkNotNull(password, "password");

        if (!tpvService.validateAdmin(tpv.getId(), password))
            throw new IllegalStateException("Password d'administrador incorrecte");
    }

    public void iniciaQuadrament() {
        if (!tpv.getState().equals(TPVState.IDLE)) throw new IllegalStateException("Aquest tpv no està en un torn actualment");
        if (tpv.getCurrentSale() != null) throw new IllegalStateException("Actualment hi ha una venta iniciada");
        tpv.setState(TPVState.BALANCE);
    }

    public void quadra(double cash){
        if (!tpv.getState().equals(TPVState.BALANCE)) throw new IllegalStateException("La caixa no es troba actualment en procés de quadrament");
        if (Math.abs(tpv.getCash() - cash) > 1) {
            throw new IllegalStateException("La caixa no quadra: hi ha un desquadrament de més d'1€");
        }
        else if (Math.abs(tpv.getCash()-cash) > 0){
            throw new IllegalStateException("La caixa no quadra: hi ha un desquadrament de menys d'1€");
        }
        else {
            tpv.endTurn();
        }

    }

    public Balance addDesquadrament(double cash) {
        double qtt = Math.round((tpv.getCash() - cash) * 100.0) / 100.0;
        Balance b = balancesService.newBalance(qtt, tpv.getCurrentSaleAssistant().getName(), tpv.getShop());
        tpv.endTurn();
        return b;
    }

    public void startSale() {
        if (tpv.hasSale())
            throw new IllegalStateException("Aquest tpv ja té una venta iniciada");
        tpv.newSale();
    }

    public boolean isSaleStarted() {
        return tpv.hasSale();
    }

    public void addProductByBarCode(int barCode) {
        addProductByBarCode(barCode, 1);
    }

    public void addNewDiscountToCurrentSale(int prodLine, @NotNull String name, double percent) {
        if (!isSaleStarted())
            throw new IllegalStateException("No hi ha cap venda iniciada");
        int max = getCurrentSale().getLines().size();
        if (prodLine-1 < 0 || prodLine-1 >= max)
            throw new IndexOutOfBoundsException("No es pot accedir a la línia " + prodLine +
                    " de la venta, aquesta només té " + max + " línies");
        Product product = productsService.findById(tpv.getCurrentSale().getId(prodLine-1));
        Discount discount = new Percent(product, name, -1, percent);
        getCurrentSale().addManualDiscount(discount, prodLine);
    }

    public void newDiscountPercent(String name, long barCode, double percent) {
        Product product = productsService.findByBarCode(barCode);
        discountService.newDiscount(product, name, percent);
    }

    public void newDiscountPromotion(String name, long barCode, int A, int B) {
        Product product = productsService.findByBarCode(barCode);
        discountService.newDiscount(product, name, A, B);
    }

    public void newDiscountPresent(String name, long barCodeRequired, long barCodePresent) {
        Product present = productsService.findByBarCode(barCodePresent);
        Product required = productsService.findByBarCode(barCodeRequired);
        discountService.newDiscount(present, name, required);
    }

    public void newCategory(String catName) {
        categoriesService.newCategory(catName);
    }

    public String getCustomerScreenMessage() {
        String welcomeMessage = "Li donem la benvinguda a Joguets i Joguines!";
        if (!tpv.hasSale()) return welcomeMessage;
        if (tpv.getCurrentSale().isEmpty()) {
            return welcomeMessage + System.lineSeparator() + "L'atén " + tpv.getCurrentSaleAssistant().getName();
        }
        StringBuilder sb = new StringBuilder();
        for (SaleLine sl : tpv.getCurrentSale().getLines()) {
            sb.append(sl.getName())
                    .append(" - ")
                    .append(sl.getUnitPrice())
                    .append("€/u x ")
                    .append(sl.getAmount())
                    .append("u = ")
                    .append(sl.getTotalPrice())
                    .append("€")
                    .append(System.lineSeparator());
        }
        sb.append("---")
                .append(System.lineSeparator())
                .append("Total: ")
                .append(tpv.getCurrentSale().getTotal())
                .append("€");
        return sb.toString();
    }

    public double cashPayment(double delivered) {
        checkPaymentConditions();
        double total = getCurrentSale().getTotal();
        if (total > delivered)
            throw new IllegalStateException("No es pot cobrar una venta amb un import inferior al total de la venta");
        tpv.addCash(total);
        tpv.getCurrentSale().setTipusPagament("cash");
        salesService.insertSale(tpv.getCurrentSale());
        tpv.endSale();
        return calculateChange(delivered, total);
    }

    private double calculateChange(double delivered, double total) {
        return Math.round((delivered - total) * 100.0) / 100.0;
    }

    private void checkPaymentConditions() {
        if (!tpv.hasSale())
            throw new IllegalStateException("No es pot cobrar una venta si no està iniciada");
        if (tpv.getCurrentSale().isEmpty())
            throw new IllegalStateException("No es pot cobrar una venta sense cap producte");
    }

    public void tarjetPayment() {
        checkPaymentConditions();
        tpv.getCurrentSale().setTipusPagament("card");
        salesService.insertSale(tpv.getCurrentSale());
        tpv.endSale();
    }

    public void addProductByBarCode(int barCode, int unitats) {
        Product p = productsService.findByBarCode(barCode);
        if (p == null) {
            throw new IllegalStateException(ADD_NON_EXISTING_PRODUCT_ERROR);
        }
        List<Discount> dList = discountService.listByTriggerId(p.getId());
        tpv.addProduct(p, unitats, dList);
    }

    public List<Product> addProductByName(@NotNull String nom) {
        return addProductByName(nom, 1);
    }

    /**
     * Add product to sale by name
     *
     * @param nomProducte product name
     * @param unitatsProducte units added
     * @return products that match the name introduced
     */
    public List<Product> addProductByName(@NotNull String nomProducte, int unitatsProducte) {
        List<Product> products = productsService.findByName(nomProducte);
        if (products.isEmpty()) {
            throw new IllegalStateException(ADD_NON_EXISTING_PRODUCT_ERROR);
        }
        if (products.size() == 1) {
            Product p = products.get(0);
            List<Discount> dList= discountService.listByTriggerId(p.getId());
            tpv.addProduct(p, unitatsProducte, dList);
        }
        return products;
    }

    public Balance getLastBalance() {
        return balancesService.getLastBalance();
    }


    public double addRefund(int unitats, int barCode, long idVenda, String reason) {
        Sale sale = salesService.findById(idVenda);
        if(sale==null) {
            throw new IllegalStateException("La venda que es vol retornar no existeix");
        }
        Product p = sale.getProductByBarCode(barCode);
        if (p == null)
            throw new IllegalStateException("El producte que es vol retornar no s'ha venut amb anterioritat");
        if (unitats > sale.getAmountByProduct(p))
            throw new IllegalStateException("La quantitat de producte retornat es major a la que es va vendre");


        double refundMoney = p.getPrice()*unitats;
        tpv.addCash(-refundMoney);
        refundsService.newRefund(p, unitats, reason);
        return refundMoney;
    }

    public Sale getLastSale(){
        return salesService.listSales().get(salesService.listSales().size()-1);
    }

    public void traspasaCash(String shop, int posSource, int posTarget, double cash) {
        if (cash > tpvService.findByShopPos(shop, posSource).getCash())
            throw new IllegalStateException("El tpv no disposa de prou efectiu per a realitzar la retirada");
        tpvService.findByShopPos(shop, posSource).removeCash(cash);
        tpvService.findByShopPos(shop, posTarget).addCash(cash);
    }

    public void cancelCurrentSale() {
        tpv.cancelCurrentSale();
    }

    public void addItemsSaleLine(int units, int line) {
        if (line > tpv.getCurrentSale().getLines().size())
            throw new IllegalStateException("La línia de venta no existeix a la venta actual");
        tpv.getCurrentSale().getLines().get(line-1).incrAmount(units);
    }

    public void deleteItemsSaleLine(int units, int line) {
        if (line > tpv.getCurrentSale().getLines().size())
            throw new IllegalStateException("La línia de venta no existeix a la venta actual");
        if (units > tpv.getCurrentSale().getLines().get(line-1).getAmount())
            throw new IllegalStateException("No es poden eliminar més unitats de les que hi ha a la línia de venda");
        if (units == tpv.getCurrentSale().getLines().get(line - 1).getAmount()) {
            tpv.getCurrentSale().removeSaleLine(line-1);
        }
        else tpv.getCurrentSale().getLines().get(line-1).decrAmount(units);
    }

    public void deleteSaleLine(int line) {
        if (line > tpv.getCurrentSale().getLines().size())
            throw new IllegalStateException("La línia de venta no existeix a la venta actual");
        tpv.getCurrentSale().removeSaleLine(line-1);
    }

    public List<Category> listCategories() {
        return categoriesService.list();
    }

    public void addProductToCategory(Product product, String catName){
        if(categoriesService.findByName(catName).hasProduct(product)) throw new IllegalStateException("El producte ja pertany a la categoria");
        categoriesService.addProductToCategory(product, catName);
    }

    public List<Product> listProductsByCategory(String catName) {
        List<Product> list = categoriesService.listProductsByCategory(catName);
        if(list.size()==0) throw new IllegalStateException("La categoria no té productes");
        return list;
    }

}
