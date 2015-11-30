package edu.upc.essi.gps.domain;

import java.util.List;
import java.util.stream.Collectors;

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
    public int getAmount(Sale currentSale) {
        List<Integer> list = currentSale
                .getLines()
                .stream()
                .filter((l) -> l.getId() == trigger.getId())
                .map(Sale.SaleLine::getAmount)
                .collect(Collectors.toList());

        int total = 0;
        for (Integer i : list) {
            total += i;
        }
        return total;
    }

}
