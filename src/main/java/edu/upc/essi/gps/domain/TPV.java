package edu.upc.essi.gps.domain;

import java.util.List;

/**
 * Classe que representa un Terminal Punt de Venda (TPV), identificat per <code>id</code>.<br>
 * A més, cada tpv es pot identificar per la botiga on es troba i el seu numero de terminal.<br>
 * Cada tpv en funcionament té associat un caixer que l'utilitza, i una venta que s'estigui realitzant.<br>
 * A més, el tpv emmagatzema la quantitad d'efectiu que tenia en començar el torn, i la quantitat d'efectiu
 * de que disposa en cada moment.
 * */
public class TPV implements Entity {

    /**
     * <code>long</code> que identifica únicament el terminal al sistema.
     * */
    private final long id;

    /**
     * Nom de la botiga on es troba el tpv.
     * */
    private final String shop;

    /**
     * Número de terminal associat a aquest tvp.
     * */
    private final int pos;

    /**
     * Nombre d'intents de login fallits.
     * */
    private int nIntents;

    /**
     * Estat en que es troba el terminal.
     * */
    private TPVState state;

    /**
     * Efectiu del que disposa el caixer.
     * */
    private double cash;

    /**
     * Efectiu del que disposa el caixer en iniciar el torn.
     * */
    private double initialCash;

    /**
     * Caixer que utilitza el tpv actualment.
     * */
    private SaleAssistant currentSaleAssistant;

    /**
     * Venta que es realitza al tpv actualment.
     * */
    private Sale currentSale;

    /**
     * Crea una nova instància d'un <code>TPV</code> amb la botiga i el número de terminal.<br>
     * L'identificador intern de l'usuari l'assigna el sistema automàticament.
     * @param shop Nom de la botiga on es troba el tpv.
     * @param pos Numero de terminal on es troba el tpv.
     * @param id Identificador intern del tpv.
     * */
    public TPV(String shop, int pos, long id) {
        this.shop = shop;
        this.pos = pos;
        this.id = id;
        endTurn();
    }

    /**
     * Consulta el nombre d'intents de login fallits.
     * @return el nombre d'intents fallits.
     * */
    public int getnIntents() {
        return nIntents;
    }

    /**
     * Incrementa el nombre d'intents de login fallits.
     * @param n nombre d'intents a incrementar.
     * */
    public void addNIntents(int n) {
        this.nIntents += n;
    }

    /**
     * Consulta el número de terminal del tpv.
     * @return número de terminal del tpv.
     * */
    public int getPos() {
        return pos;
    }

    /**
     * Consulta el nom de la botiga on es troba el tpv.
     * @return nom de la botiga on es troba el tpv.
     * */
    public String getShop() {
        return shop;
    }

    /**
     * Consulta l'estat en que es troba el terminal.
     * @return l'estat en que es troba el terminal.
    .
     * */
    public TPVState getState() {
        return state;
    }

    /**
     * Modifica l'estat en que es troba el terminal.
     * @param state estat en que es troba el terminal.
     * */
    public void setState(TPVState state) {
        this.state = state;
    }

    @Override
    public long getId() {
        return id;
    }

    /**
     * Consulta l'efectiu del que disposa el terminal.
     * @return efectiu del que disposa el terminal.
     * */
    public double getCash() {
        return cash;
    }

    /**
     * Incrementa l'efectiu del que disposa el caixer.
     * @param cash efectiu que cal incrementar o decrementar.
     * */
    public void addCash(double cash) {
        this.cash += cash;
    }

    /**
     * Consulta l'efectiu inicial del que disposa el terminal.
     * @return efectiu inicial del que disposa el terminal.
     * */
    public double getInitialCash() {
        return initialCash;
    }

    /**
     * Consulta el caixer que utilitza el terminal.
     * @return <code>SaleAssistant</code> que utilitza el terminal.
     * */
    public SaleAssistant getCurrentSaleAssistant(){
        return currentSaleAssistant;
    }

    /**
     * Inicia un nou torn al terminal.
     * @param saleAssistant <code>SaleAssistant</code> que utilitza el terminal.
     * @param initialCash efectiu inicial del caixer.
     * */
    public void newTurn(SaleAssistant saleAssistant, double initialCash) {
        currentSaleAssistant = saleAssistant;
        this.initialCash = initialCash;
        this.cash = initialCash;
    }

    /**
     * Finalitza el torn actual.
     * */
    public void endTurn() {
        this.nIntents = 0;
        this.state = TPVState.AVAILABLE;
        this.cash = 0;
        this.initialCash = 0;
        currentSaleAssistant = null;
        currentSale = null;
    }

    /**
     * Consulta la venta que s'està realitzant al terminal.
     * @return <code>Sale</code> que s'està realitzant al terminal.
     * */
    public Sale getCurrentSale() {
        return currentSale;
    }

    /**
     * Inicia una nova venta en aquest terminal.
     * */
    public void newSale() {
        currentSale = new Sale();
    }

    /**
     * Finalitza la venta actual d'aquest terminal.
     * */
    public void endSale() {
        currentSale = null;
    }

    /**
     * Consulta si aquest terminal té alguna venta en curs.
     * */
    public boolean hasSale() {
        return currentSale != null;
    }


    public void addProduct(Product p, int unitats, List<Discount> discountList) {
        if (state.equals(TPVState.BALANCE)) throw new IllegalStateException("La caixa es troba en procés de quadrament");
        if (!hasSale()) newSale();
        currentSale.addProduct(p, unitats, discountList);
    }
}
