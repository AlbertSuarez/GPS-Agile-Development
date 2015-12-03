# language: ca

  #noinspection SpellCheckingInspection
Característica: Retornar un producte

  Rerefons:
    Donat que estem al tpv número 1 de la botiga "Girona 1"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i rep l'identificador 123
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de €37€
    I un producte amb nom "Optimus Prime", preu €20€, iva %21% i codi de barres 1234567
    I un producte amb nom "Nintendo choripan", preu €150€, iva %21% i codi de barres 7777777
    I que es fa una venda amb id 123 del producte amb codi de barres 1234567

  Escenari: Un client retorna un producte d'una venda concreta
    Quan faig una devolució de 1 unitat/s del producte amb codi de barres 1234567 de la venta 123 amb el motiu "No funciona"
    Aleshores el TPV m'indica que haig de retornar una quantitat de €20€


  #Escenari: Retorn de més d'una unitat

  Escenari: Retorn de massa unitats
    Quan faig una devolució de 2 unitat/s del producte amb codi de barres 1234567 de la venta 123 amb el motiu "No funciona"
    Aleshores obtinc un error que diu: "La quantitat de producte retornat es major a la que es va vendre"

  #Escenari: Retorn venda no existent

  Escenari: Producte no existent
    Quan faig una devolució de 1 unitat/s del producte amb codi de barres 12121212 de la venta 123 amb el motiu "No funciona"
    Aleshores obtinc un error que diu: "El producte que es vol retornar no s'ha venut amb anterioritat"