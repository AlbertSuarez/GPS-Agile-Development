# language: ca

#noinspection SpellCheckingInspection
Característica: Cobrar una venta en metàlic

  Rerefons:
    Donat un producte amb nom "Optimus Prime", preu €23€, iva %21% i codi de barres 1234567
    I que estem al tpv número 1 de la botiga "Girona 1"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i rep l'identificador 123
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de €25,47€

  Escenari: Cobrar una venta en metàlic
    Donat que hi ha una venta iniciada
    I que he afegit el producte de codi de barres 1234567 a la venta
    Quan indico que el client ha entregat €30,10€ per a pagar en metàlic
    Aleshores la venta no esta iniciada
    I queda registrat una venta d'una quantitat de €23€

  Escenari: Error si intentem cobrar una venta sense productes
    Donat que hi ha una venta iniciada
    Quan indico que el client ha entregat €30,10€ per a pagar en metàlic
    Aleshores obtinc un error que diu: "No es pot cobrar una venta sense cap producte"

  Escenari: Error si intentem cobrar una venta no iniciada
    Quan indico que el client ha entregat €30,10€ per a pagar en metàlic
    Aleshores obtinc un error que diu: "No es pot cobrar una venta si no està iniciada"
