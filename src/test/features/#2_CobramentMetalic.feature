# language: ca

#noinspection SpellCheckingInspection
Característica: Cobrar una venta en metàlic

  Rerefons:
    Donat un producte amb nom "Optimus Prime", preu 23€, iva 21% i codi de barres 1234567
    I que estem al tpv número 1 de la botiga "Girona 1"
    Donat que 123 ha iniciat el torn al tpv amb password "asdf"

  Escenari: Cobrar una venta en metàlic
    Donat que hi ha una venta iniciada
    I que he afegit el producte de codi de barres 1234567 a la venta
    Quan indico que el client ha entregat 30€ per a pagar en metàlic
    Aleshores el tpv m'indica que el canvi a retornar és de 7€
    I el tpv tanca la venda actual

  Escenari: Error si intentem cobrar una venta sense productes
    Donat que hi ha una venta iniciada
    Quan indico que el client ha entregat 30€ per a pagar en metàlic
    Aleshores obtinc un error que diu: "No es pot cobrar una venta sense cap producte"

  Escenari: Error si intentem cobrar una venta no iniciada
    Quan indico que el client ha entregat 30€ per a pagar en metàlic
    Aleshores obtinc un error que diu: "No es pot cobrar una venta si no està iniciada"
