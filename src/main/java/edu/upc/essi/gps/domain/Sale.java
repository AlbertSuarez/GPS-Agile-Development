package edu.upc.essi.gps.domain;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Sale {

    private final List<SaleLine> lines = new LinkedList<>();

    public void addProduct(Product p) {
        lines.add(new SaleLine(p,1));
    }

    public void addDiscount(Discount d, int... posicions) {
        for (int pos : posicions) {
            if (pos >= lines.size())
                throw new IllegalArgumentException("No es pot accedir a la línia " + pos +
                        " de la venta, aquesta només té " + lines.size() + " línies");
            SaleLine saleLine = lines.get(posicions[pos]);
            if (saleLine.getProductId() != d.getTrigger().getId())
                throw new IllegalArgumentException("Els productes del descompte i de la línia no coincideixen");
            lines.add(pos, new SaleLine(d));
        }
    }

    public void addDiscount(Discount d) {
        lines.addAll(
                lines.stream()
                        .filter(saleLine -> saleLine.getProductId() == d.getTrigger().getId())
                        .map(saleLine -> new SaleLine(d))
                        .collect(Collectors.toList())
        );
    }

    public long getProductId(int prodLine) {
        return getLines().get(prodLine).getProductId();
    }

    public List<SaleLine> getLines() {
        if (lines.isEmpty()) throw new IllegalStateException("No hi ha cap venda");
        return Collections.unmodifiableList(lines);
    }

    public int getTotal() {
        int res = 0;
        for(SaleLine l : lines){
            res += l.getTotalPrice();
        }
        return res;
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public boolean hasProductByBarCode(int barCode) {
        for (SaleLine line : lines) {
            if (line.getBarCode() == barCode) {
                return true;
            }
        }
        return false;
    }

    public class SaleLine {

        private long productId;
        private String productName;
        private int unitPrice;
        private int amount;
        private int barCode;

        public SaleLine(Product product, int amount) {
            this.productId = product.getId();
            this.productName = product.getName();
            this.unitPrice = product.getPrice();
            this.barCode = product.getBarCode();
            this.amount = amount;
        }

        public SaleLine(Discount discount) {
            productId = discount.getId();
            productName = discount.getName();
            unitPrice = (int) discount.getDiscount(); //TODO: passem tots els preus a double plz ^^'
            amount = 1;
            barCode = discount.getBarCode();
        }

        public long getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public int getUnitPrice() {
            return unitPrice;
        }

        public int getAmount() {
            return amount;
        }

        public int getTotalPrice() {
            return unitPrice * amount;
        }

        public int getBarCode() {
            return barCode;
        }

    }

}
