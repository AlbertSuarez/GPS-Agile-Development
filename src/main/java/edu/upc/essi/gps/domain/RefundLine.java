package edu.upc.essi.gps.domain;

public class RefundLine{
    private long id;

    private int quantitat;

    public RefundLine(Product product, int unitats){
        id=product.getId();
        quantitat=unitats;
    }


}
