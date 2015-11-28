package edu.upc.essi.gps.domain;

import java.util.List;

/**
 * Classe que representa un descompte del tipus x% (10% de descompte, 23% de descompte...).
 * */
public class Percent extends Discount {

    /**
     * Tant per cent de descompte que s'ha d'aplicar al producte.
     * */
    private final double percent;

    /**
     * Nom que identifica aquesta classe com a un tipus concret de descompte.
     * */
    public static final String TYPE_NAME = "percent";

    /**
     * Crea una nova instància d'un descompte per percentatge a partir d'un producte.
     * @param product producte amb el qual s'asocia el descompte.
     * @param name nom del descompte.
     * @param id identificador del descompte al sistema.
     * @param percent tant per cent de descompte que s'ha d'aplicar al producte.
     * */
    public Percent(Product product, String name, long id, double percent) {
        super(product, name, id);
        this.percent = percent;
    }

    @Override
    public double getDiscount() {
        return -trigger.getPrice()*percent/100;
    }

    @Override
    public boolean checkSale(Sale currentSale) {
        List<Sale.SaleLine> list = currentSale.getLines();
        Sale.SaleLine line = list.get(list.size()-1);
        return line.getId() == trigger.getId();
    }

    @Override
    public int getAmount(Sale currentSale) {
        if (!checkSale(currentSale)) return 0;
        List<Sale.SaleLine> list = currentSale.getLines();
        Sale.SaleLine line = list.get(list.size()-1);
        return line.getAmount();
    }

}
