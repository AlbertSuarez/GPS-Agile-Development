package edu.upc.essi.gps.domain;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Classe que representa una venta mitjançant un conjunt de línies de venta.
 * */
public class Sale implements Entity {

    /**
     * Identificador únic d'aquesta venda al sistema.
     * */
    private long id;

    /**
     * Conjunt de línies de venta associats a la venta.
     * */
    private List<SaleLine> lines = new LinkedList<>();

    /**
     * Conjunt de descomptes aplicables a cada producte.
     * */
    private Map<Product, List<Discount>> desc = new HashMap<>();

    /**
     * Conjunt de descomptes aplicats a la venta.
     * */
    private List<SaleLine> descLines;


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
        if (saleLine.getId() != d.getTrigger().getId() || saleLine.getBarCode() != d.getTrigger().getBarCode())
            throw new IllegalArgumentException("Els productes del descompte i de la línia no coincideixen");
        lines.add(pos, new SaleLine(d, saleLine.getAmount()));
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
        if (descLines == null) testDiscounts();
        List<SaleLine> finalLines = new LinkedList<>();
        finalLines.addAll(lines);
        finalLines.addAll(descLines);
        return Collections.unmodifiableList(finalLines);
    }

    /**
     * Consulta el còmput total de la venta.
     * @return el total a pagar en aquesta venta.
     * */
    public double getTotal() {
        double res = 0;
        for (SaleLine l : lines){
            res += l.getTotalPrice();
        }
        if (descLines == null) testDiscounts();
        for (SaleLine l : descLines) {
            res += l.getTotalPrice();
        }
        return res;
    }

    /**
     * Calcula la millor combinació de descomptes tals que:<br>
     *     a) Cap producte té dos descomptes aplicats alhora.<br>
     *     b) De totes les combinacions de descomptes, és la que aplica una major rebaixa del preu final.
     * */
    private void testDiscounts() {
        Discount best;
        double min;
        double value;
        int bestAmount;
        int valueAmount;
        descLines = new LinkedList<>();
        for (Product p : desc.keySet()) {
            min = 0.0;
            bestAmount = 0;
            best = null;
            for(Discount d : desc.get(p)) {
                valueAmount = d.getAmount(this);
                value = d.getDiscount()*valueAmount;
                if (value < min) {
                    best = d;
                    min = value;
                    bestAmount = valueAmount;
                }
            }
            if (best != null) descLines.add(new SaleLine(best, bestAmount));
        }
    }

    /**
     * Consulta si la venta té algun producte introduït.
     * @return <code>true</code> si la venta és buida, <code>false</code> altrment.
     * */
    public boolean isEmpty() {
        return lines.isEmpty() && desc.isEmpty();
    }

    /**
     * Consulta si la venta té algun producte amb el codi de barres indicat.
     * @return <code>true</code> si la té algun producte amb el codi de barres indicat, <code>false</code> altrment.
     * */
    public boolean hasProductByBarCode(int barCode) {
        return lines
                .stream()
                .anyMatch((l) -> l.getBarCode() == barCode);
    }

    /**
     * Afegeix un nou producte a la venta.
     * @param product producte a afegir a la venta.
     * @param unitats nombre d'unitats del producte a afegir
     * */
    public void addProduct(Product product, int unitats, List<Discount> discountList, boolean refund) {
        lines.add(new SaleLine(product, unitats, refund));
        desc.put(product, discountList);
        descLines = null;
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

    /**
     * Unitat mínima d'una venta, cadascun d'aquests elements s'associa a un producte o descompte
     * */
    public class SaleLine {

        /**
         * Identificador associat al producte inicat a la línia
         * */
        private long id;

        /**
         * Nom del producte o despompte
         * */
        private String name;

        /**
         * Valor del producte o despompte
         * */
        private double unitPrice;

        /**
         * Quantitat d'unitats del producte o despompte
         * */
        private int amount;

        /**
         * Codi de barres del producte o despompte
         * */
        private long barCode;

        /**
         * Crea una nova instància de <code>SaleLine</code> a partir de les dades d'un producte.
         * @param product producte a partir del qual es crea la línia de venta.
         * @param amount quantitat d'aquest producte.
         * @param refund indica si es una devolució o no
         */
        public SaleLine(Product product, int amount, boolean refund) {
            this.id = product.getId();
            this.name = product.getName();
            if (!refund) this.unitPrice = product.getPrice();
            else this.unitPrice = -product.getPrice();
            this.barCode = product.getBarCode();
            this.amount = amount;
        }

        /**
         * Crea una nova instància de <code>SaleLine</code> a partir de les dades d'un descompte.
         * @param discount descompte a partir del qual es crea la línia de venta.
         * @param amount quantitat d'aquest descompte.
         */
        public SaleLine(Discount discount, int amount) {
            id = discount.getTrigger().getId();
            name = discount.getName();
            unitPrice = discount.getDiscount();
            this.amount = amount;
            barCode = -1;
        }

        /**
         * Consulta l'identificador del producte indicat a la línia.
         * @return identificador del producte indicat a la línia.
         */
        public long getId() {
            return id;
        }

        /**
         * Consulta el nom del producte o descompte indicat a la línia.
         * @return identificador del producte o descompte indicat a la línia.
         */
        public String getName() {
            return name;
        }

        /**
         * Consulta el valor unitari del producte o descompte indicat a la línia.
         * @return valor unitari del producte o descompte indicat a la línia.
         */
        public double getUnitPrice() {
            return unitPrice;
        }

        /**
         * Consulta el nombre d'unitats del producte o descompte indicat a la línia.
         * @return nombre d'unitats del producte o descompte indicat a la línia.
         */
        public int getAmount() {
            return amount;
        }

        /**
         * Consulta el valor total de la línia.
         * @return valor total de la línia.
         */
        public double getTotalPrice() {
            return unitPrice * amount;
        }

        /**
         * Consulta el codi de barres del producte de la línia.
         * @return codi de barres del producte de la línia.
         */
        public long getBarCode() {
            return barCode;
        }

    }

}
