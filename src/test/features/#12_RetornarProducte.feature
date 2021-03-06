# language: ca

  #noinspection SpellCheckingInspection
Característica: Retornar un producte

  Rerefons:
    Donat que estem al tpv número 1 de la botiga "Girona 1"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i rep l'identificador 555
    I que s'inica el torn al tpv amb identificador 555 i password "asdf", amb un efectiu inicial de €37€
    I un producte amb nom "Optimus Prime", preu €20€, iva %21% i codi de barres 1234567
    I un producte amb nom "Nintendo choripan", preu €150€, iva %21% i codi de barres 7777777
    I un producte amb nom "Nintendo", preu €100,5€, iva %21% i codi de barres 45
    I que es fa una venda amb id 123 del producte amb codi de barres 1234567
    I que es fa una venda amb id 456 amb 5 unitats del producte amb codi de barres 7777777
    I que es fa una venda amb id 45 amb 3 unitats del producte amb codi de barres 45

  Escenari: Un client retorna un producte d'una venda concreta
    Quan faig una devolució de 1 unitat/s del producte amb codi de barres 1234567 de la venta 123 amb el motiu "No funciona"
    Aleshores el sistema enregistra una devolució amb 1 linia/es de devolució de 1 unitats del producte amb codi de barres 1234567 amb motiu "No funciona"
    I el TPV m'indica que haig de retornar una quantitat de €20€

  Escenari: Retorn de més d'una unitat amb decimals
    Quan faig una devolució de 3 unitat/s del producte amb codi de barres 45 de la venta 45 amb el motiu "No funciona"
    Aleshores el sistema enregistra una devolució amb 1 linia/es de devolució de 3 unitats del producte amb codi de barres 45 amb motiu "No funciona"
    I el TPV m'indica que haig de retornar una quantitat de €301,5€

  Escenari: Retorn de més d'una unitat
    Quan faig una devolució de 2 unitat/s del producte amb codi de barres 7777777 de la venta 456 amb el motiu "No funciona"
    Aleshores el sistema enregistra una devolució amb 1 linia/es de devolució de 2 unitats del producte amb codi de barres 7777777 amb motiu "No funciona"
    I el TPV m'indica que haig de retornar una quantitat de €300€

  Escenari: Retorn de totes les unitats
    Quan faig una devolució de 5 unitat/s del producte amb codi de barres 7777777 de la venta 456 amb el motiu "No funciona"
    Aleshores el sistema enregistra una devolució amb 1 linia/es de devolució de 5 unitats del producte amb codi de barres 7777777 amb motiu "No funciona"
    I el TPV m'indica que haig de retornar una quantitat de €750€

  Escenari: Retorn de massa unitats
    Quan faig una devolució de 2 unitat/s del producte amb codi de barres 1234567 de la venta 123 amb el motiu "No funciona"
    Aleshores obtinc un error que diu: "La quantitat de producte retornat es major a la que es va vendre"

  Escenari: Retorn venda no existent
    Quan faig una devolució de 1 unitat/s del producte amb codi de barres 1234567 de la venta 13 amb el motiu "No funciona"
    Aleshores obtinc un error que diu: "La venda que es vol retornar no existeix"

  Escenari: Producte no existent
    Quan faig una devolució de 1 unitat/s del producte amb codi de barres 12121212 de la venta 123 amb el motiu "No funciona"
    Aleshores obtinc un error que diu: "El producte que es vol retornar no s'ha venut amb anterioritat"
