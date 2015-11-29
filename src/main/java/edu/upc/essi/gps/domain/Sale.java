package edu.upc.essi.gps.domain;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
        return Collections.unmodifiableList(lines);
    }

    /**
     * Consulta el còmput total de la venta.
     * @return el total a pagar en aquesta venta.
     * */
    public double getTotal() {
        double res = 0;
        for(SaleLine l : lines){
            res += l.getTotalPrice();
        }
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
     * */
    public boolean hasProductByBarCode(int barCode) {
        for (SaleLine line : lines) {
            if (line.getBarCode() == barCode) {
                return true;
            }
        }
        return false;
    }

    /**
     * Afegeix un nou producte a la venta.
     * @param product producte a afegir a la venta.
     * @param unitats nombre d'unitats del producte a afegir
     * */
    public void addProduct(Product product, int unitats, List<Discount> discountList, boolean refund) {
        lines.add(new SaleLine(product, unitats, refund));
        lines.addAll(
                discountList
                        .stream()
                        .filter(d -> d.checkSale(this))
                        .map(d -> new SaleLine(d, d.getAmount(this)))
                        .collect(Collectors.toList())
        );
    }

    /**
     * Consulta si la venta té algun producte amb el nom indicat.
     * @return <code>true</code> si la té algun producte amb el nom indicat, <code>false</code> altrment.
     * */
    public boolean hasProductByName(String nom) {
        for (SaleLine line : lines) {
            if (line.getName().equals(nom)) {
                return true;
            }
        }
        return false;
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
