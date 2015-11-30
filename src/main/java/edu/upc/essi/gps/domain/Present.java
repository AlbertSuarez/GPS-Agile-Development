package edu.upc.essi.gps.domain;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe que representa un descompte del tipus amb el producte A et regalem B.
 * */
public class Present extends Discount{

    /**
     * Producte que es regala amb la compra del disparador.
     * */
    private final Product present;

    /**
     * Nom que identifica aquesta classe com a un tipus concret de descompte.
     * */
    public static final String TYPE_NAME = "present";

    /**
     * Crea una nova instància d'un descompte per regal a partir d'un producte.
     * @param product producte amb el qual s'asocia el descompte.
     * @param name nom del descompte.
     * @param id identificador del descompte al sistema.
     * @param present producte que es regala amb la compra del producte anterior.
     * */
    public Present(Product product, String name, long id, Product present) {
        super(product, name, id);
        this.present = present;
    }

    @Override
    public double getDiscount() {
        return -present.getPrice();
    }

    @Override
    public int getAmount(Sale currentSale) {
        List<Integer> list;
        int triggers = 0;
        int presents = 0;

        list = currentSale
                .getLines()
                .stream()
                .filter((l) -> l.getId() == trigger.getId())
                .map(Sale.SaleLine::getAmount)
                .collect(Collectors.toList());

        for (Integer i : list) {
            triggers += i;
        }

        list = currentSale
                .getLines()
                .stream()
                .filter((l) -> l.getId() == present.getId())
                .map(Sale.SaleLine::getAmount)
                .collect(Collectors.toList());

        for (Integer i : list) {
            presents += i;
        }

        return Math.min(presents, triggers);
    }

}
