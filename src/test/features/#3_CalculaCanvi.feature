# language: ca

#noinspection SpellCheckingInspection
Característica: Obtenir el canvi d'una venta

  Rerefons:
    Donat un producte amb nom "Optimus Prime", preu 23€, iva 21% i codi de barres 1234567
    I que estem al tpv número 1 de la botiga "Girona 1"
    Donat que 123 ha iniciat el torn al tpv amb password "asdf"

  Escenari: Cobrar una venta en metàlic
    Donat que hi ha una venta iniciada
    I que he afegit el producte de codi de barres 1234567 a la venta
    Quan indico que el client ha entregat 30€ per a pagar en metàlic
    Aleshores el tpv m'indica que el canvi a retornar és de 7€

  Escenari: Error si intentem cobrar una venta amb un import inferior al total de la venta
    Donat que hi ha una venta iniciada
    I que he afegit el producte de codi de barres 1234567 a la venta
    Quan indico que el client ha entregat 20€ per a pagar en metàlic
    Aleshores obtinc un error que diu: "No es pot cobrar una venta amb un import inferior al total de la venta"
