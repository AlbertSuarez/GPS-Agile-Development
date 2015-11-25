package edu.upc.essi.gps.domain;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe que representa una venta mitjançant un conjunt de línies de venta.
 * */
public class Sale {

    /**
     * Conjunt de línies de venta associats a la venta.
     * */
    private final List<SaleLine> lines = new LinkedList<>();

    /**
     * Afegeix un nou producte a la venta.
     * @param p producte a afegir a la venta.
     * */
    public void addProduct(Product p) {
        lines.add(new SaleLine(p,1));
    }

    /**
     * Afegeix un nou descompte a la venta.
     * @param d producte a afegir a la venta.
     * @param posicions índex dels articles als que es vol aplicar el descompte.
     * */
    public void addDiscount(Discount d, int... posicions) {
        for (int pos : posicions) {
            if (pos > lines.size())
                throw new IndexOutOfBoundsException("No es pot accedir a la línia " + pos +
                        " de la venta, aquesta només té " + lines.size() + " línies");
            SaleLine saleLine = lines.get(pos-1);
            if (saleLine.getId() != d.getTrigger().getId())
                throw new IllegalArgumentException("Els productes del descompte i de la línia no coincideixen");
            SaleLine newLine = new SaleLine(d, saleLine.getAmount());
            lines.add(pos, newLine);
        }
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
    public int getTotal() {
        int res = 0;
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
        private int unitPrice;

        /**
         * Quantitat d'unitats del producte o despompte
         * */
        private int amount;

        /**
         * Codi de barres del producte o despompte
         * */
        private int barCode;

        /**
         * Crea una nova instància de <code>SaleLine</code> a partir de les dades d'un producte.
         * @param product producte a partir del qual es crea la línia de venta.
         * @param amount quantitat d'aquest producte.
         */
        public SaleLine(Product product, int amount) {
            this.id = product.getId();
            this.name = product.getName();
            this.unitPrice = product.getPrice();
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
            unitPrice = (int) discount.getDiscount(); //TODO: passem tots els preus a double plz ^^'
            this.amount = amount;
            barCode = discount.getBarCode();
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
        public int getUnitPrice() {
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
        public int getTotalPrice() {
            return unitPrice * amount;
        }

        /**
         * Consulta el codi de barres del producte o descompte de la línia.
         * @return codi de barres del producte o descompte de la línia.
         */
        public int getBarCode() {
            return barCode;
        }

    }

}
