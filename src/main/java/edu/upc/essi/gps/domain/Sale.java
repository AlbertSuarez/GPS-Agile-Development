package edu.upc.essi.gps.domain;

import edu.upc.essi.gps.domain.discounts.Discount;
import edu.upc.essi.gps.domain.lines.SaleLine;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe que representa una venta mitjançant un conjunt de línies de venta.
 * */
public class Sale implements Entity {

    public static final String CARD_PAYMENT = "card";
    public static final String CASH_PAYMENT = "cash";
    /**
     * Identificador únic d'aquesta venda al sistema.
     * */
    private long id;

    /**
     * Conjunt de línies de venta associats a la venta.
     * */
    private List<SaleLine> lines = new LinkedList<>();

    /**
     * Tipus de pagament
     */
    private String tipusPagament;    //cash or card
    private double discountedPrice = 0;

    /**
     * Constructora sense parametres.
     */
    public Sale() {

    }

    /**
     * Constructora amb parametres.
     * @param id Identificador.
     * @param lines Linies de venda.
     */
    public Sale(long id, List<SaleLine> lines) {
        this.id = id;
        this.lines = lines;
    }

    public Sale(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    /**
     * Afegeix un nou descompte a la venta.
     * @param d producte a afegir a la venta.
     * @param pos índex dels articles als que es vol aplicar el descompte.
     * */
    public void addManualDiscount(Discount d, int pos) {
        if (pos > lines.size())
            throw new IndexOutOfBoundsException("No es pot accedir a la línia " + pos +
                    " de la venta, aquesta només té " + lines.size() + " línies");
        SaleLine saleLine = lines.get(pos-1);
        if (!d.isTriggeredBy(saleLine.getProduct().getId()))
            throw new IllegalArgumentException("Els productes del descompte i de la línia no coincideixen");
        discountedPrice += d.calculate(saleLine);
    }

    /**
     * Consulta l'identificador del producte d'una línia.
     * @param prodLine línia de la qual es vol consultar el producte.
     * @return identificador del producte de la línia indicada
     * */
    public long getId(int prodLine) {
        return lines.get(prodLine).getId();
    }

    /**
     * Consulta el conjunt de línies de venta associats a la venta.
     * @return el conjunt de <code>SaleLine</code> associat a la venta.
     * */
    public List<SaleLine> getLines() {
        if (lines.isEmpty()) throw new IllegalStateException("No hi ha cap venda");
        return Collections.unmodifiableList(lines);
    }

    /**
     * Consulta del producte de la linia de venda donat un codi de barres
     * @param barcode codi de barres que identifica el producte
     * @return el producte amb codi de barres <code>barcode</code>
     */
    public Product getProductByBarCode(int barcode) {
        for (SaleLine s : lines) {
            if (s.getBarCode() == barcode) return s.getProduct();
        }
        return null;
    }

    /**
     * Consulta de la quantitat de productes donat el producte en una venda
     * @param product el producte
     * @return la quantitat de producte venut
     */
    public int getAmountByProduct(Product product) {
        for (SaleLine s : lines) {
            if (s.getProduct().getBarCode() == product.getBarCode()) return s.getAmount();
        }
        return 0;
    }

    /**
     * Consulta el còmput total de la venta.
     * @return el total a pagar en aquesta venta.
     * */
    public double getTotal() {
        double res = 0;
        for (SaleLine l : lines) {
            res += l.getTotalPrice();
        }
        res -= discountedPrice;
        return res;
    }

    /**
     * Consulta si la venta té algun producte introduït.
     * @return <code>true</code> si la venta és buida, <code>false</code> altrment.
     * */
    public boolean isEmpty() {
        return lines.isEmpty();
    }

    /**
     * Consulta si la venta té algun producte amb el codi de barres indicat.
     * @return <code>true</code> si la té algun producte amb el codi de barres indicat, <code>false</code> altrment.
     * @param barCode this is the barCode*/
    public boolean hasProductByBarCode(long barCode) {
        return lines
                .stream()
                .anyMatch((l) -> l.getBarCode() == barCode);
    }

    /**
     * Afegeix un nou producte a la venta.
     * @param product producte a afegir a la venta.
     * @param unitats nombre d'unitats del producte a afegir
     * */
    public void addProduct(Product product, int unitats, List<Discount> discountList) {
        if (hasProductByBarCode(product.getBarCode())) {
            SaleLine line = getLineProduct(product);
            line.incrAmount(unitats);
        } else {
            lines.add(new SaleLine(product, unitats));
        }
    }

    private SaleLine getLineProduct(Product product) {
        return lines.stream()
                .filter((e) -> e.getProduct().getBarCode() == product.getBarCode())
                .findFirst()
                .get();
    }


    /**
     * Consulta si la venta té algun producte amb el nom indicat.
     * @return <code>true</code> si la té algun producte amb el nom indicat, <code>false</code> altrment.
     * */
    public boolean hasProductByName(String nom) {
        return lines
                .stream()
                .anyMatch((l) -> l.getName().equals(nom));
    }

    public String getTipusPagament() {
        return tipusPagament;
    }

    public void setTipusPagament(String tipusPagament) {
        if (tipusPagament.equals(CARD_PAYMENT) || tipusPagament.equals(CASH_PAYMENT))
            this.tipusPagament = tipusPagament;
    }

    public void removeSaleLine(int line) {
        lines.remove(line);
    }

    public void addDiscount(double discount) {
        discountedPrice += discount;
    }
}
