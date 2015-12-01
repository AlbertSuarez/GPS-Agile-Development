package edu.upc.essi.gps.domain;

/**
 * Classe que representa un descompte del tipus amb el producte A et regalem B.
 * */
public class Present extends Discount{

    /**
     * Producte que cal comprar per a que es regali el disparador.
     * */
    private final Product required;

    /**
     * Nom que identifica aquesta classe com a un tipus concret de descompte.
     * */
    public static final String TYPE_NAME = "present";

    /**
     * Crea una nova instància d'un descompte per regal a partir d'un producte.
     * @param product producte amb el qual s'asocia el descompte.
     * @param name nom del descompte.
     * @param id identificador del descompte al sistema.
     * @param required producte que cal comprar per a que es regali el disparador.
     * */
    public Present(Product product, String name, long id, Product required) {
        super(product, name, id);
        this.required = required;
    }

    @Override
    public double getDiscount() {
        return -trigger.getPrice();
    }

    @Override
    public int getAmount(Sale currentSale) {
        final int[] triggers = {0};
        final int[] requireds = {0};

        currentSale
                .getLines()
                .stream()
                .filter((l) -> l.getId() == trigger.getId())
                .map(Sale.SaleLine::getAmount)
                .forEach((i) -> triggers[0] += i);

        currentSale
                .getLines()
                .stream()
                .filter((l) -> l.getId() == required.getId())
                .map(Sale.SaleLine::getAmount)
                .forEach((i) -> requireds[0] += i);

        return Math.min(requireds[0], triggers[0]);
    }

}
