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
    public boolean checkSale(Sale currentSale) {
        List<Sale.SaleLine> list = currentSale.getLines();
        Sale.SaleLine line = list.get(list.size() - 1);
        return line.getId() == trigger.getId()
                && list
                .stream()
                .filter(l -> l.getId() == present.getId() && l.getAmount() == line.getAmount())
                .collect(Collectors.toList())
                .size() > 0;
    }

    @Override
    public int getAmount(Sale currentSale) {
        if (!checkSale(currentSale)) return 0;
        List<Sale.SaleLine> list = currentSale.getLines();
        Sale.SaleLine line = list.get(list.size() - 1);

        //Nombre de triggers d'igual quantitat que el nostre
        int amountT = list
                .stream()
                .filter(l -> l.getId() == trigger.getId() && l.getAmount() == line.getAmount())
                .collect(Collectors.toList())
                .size();

        //Nombre de presents d'igual quantitat que el nostre
        int amountP = list
                .stream()
                .filter(l -> l.getId() == present.getId() && l.getAmount() == line.getAmount())
                .collect(Collectors.toList())
                .size();

        if (amountP < amountT) return 0;
        else return line.getAmount();
    }

}
