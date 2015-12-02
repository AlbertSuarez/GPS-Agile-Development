# language: ca

  #noinspection SpellCheckingInspection
Característica: Retornar un producte

  Rerefons:
    Donat que estem al tpv número 1 de la botiga "Girona 1"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i rep l'identificador 123
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de €37€
    I un producte amb nom "Optimus Prime", preu €20€, iva %21% i codi de barres 1234567
    I un producte amb nom "Nintendo choripan", preu €150€, iva %21% i codi de barres 7777777

  Escenari: Un client retorna un producte d'una venda concreta
    Donat que he afegit el producte de codi de barres 1234567 a la venta
    I indico que el client paga el total de la venda amb targeta i registro la venda amb codi de venda 123456789
    Quan creo una devolució de 1 unitat/s del producte amb codi de barres 1234567 de la venta 123456789 amb el motiu "No funciona"
    Aleshores el TPV m'indica que haig de retornar una quantitat de €20€ per a 1 unitat/s del producte amb codi de barres 1234567
