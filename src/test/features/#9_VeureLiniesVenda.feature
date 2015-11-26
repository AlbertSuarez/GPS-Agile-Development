# language: ca

  #noinspection SpellCheckingInspection
Característica: Veure les linies de venda

  Rerefons:
    Donat que estem al tpv número 1 de la botiga "Girona 1"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i rep l'identificador 123
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de 37€
    I un producte amb nom "Optimus Prime", preu 23€, iva 21% i codi de barres 1234567

  Escenari: Consulta de linies de venda
    Donat que hi ha una venta iniciada
    I que he afegit el producte de codi de barres 1234567 a la venta
    Quan indico que vull consultar la linia de venda
    Aleshores obtinc una linia de venda amb 1 venda

  Escenari: Error si no hi ha cap venda i consultem les linies de venda
    Donat que hi ha una venta iniciada
    I que no hi ha cap venda a la linia de venda
    Quan indico que vull consultar la linia de venda
    Aleshores obtinc un error que diu: "No hi ha cap venda"
