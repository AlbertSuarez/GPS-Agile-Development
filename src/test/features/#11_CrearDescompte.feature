# language: ca

  #noinspection SpellCheckingInspection
Característica: Crear nous descomptes

  Rerefons:
    Donat que estem al tpv número 1 de la botiga "Girona 1"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i rep l'identificador 123
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de €37€
    I un producte amb nom "Optimus Prime", preu €20€, iva %21% i codi de barres 1234567
    I un producte amb nom "Nintendo choripan", preu €150€, iva %21% i codi de barres 7777777

  Escenari: Crear un nou descompte (percentatge)
    Quan creo un nou descompte del tipus percentatge anomenat "20%-Nintendo" del %20% sobre el producte amb codi de barres 7777777
    Aleshores el descompte "20%-Nintendo" té un valor de €-30€

  Escenari: Crear un nou descompte (promoció)
    Quan creo un nou descompte del tipus promoció anomenat "3x2OptPri" de 3x2 sobre el producte amb codi de barres 1234567
    Aleshores el descompte "3x2OptPri" té un valor de €-20€

  Escenari: Crear un nou descompte (regal)
    Quan creo un nou descompte del tipus regal anomenat "Choripan+OptPrime", on amb la compra del producte amb codi de barres 7777777 es regala una unitat del producte amb codi de barres 1234567
    Aleshores el descompte "Choripan+OptPrime" té un valor de €-20€

  Escenari: Aplicar un descompte (percentatge)
    Quan creo un nou descompte del tipus percentatge anomenat "20%-Nintendo" del %20% sobre el producte amb codi de barres 7777777
    I inicio una nova venta
    I afegeixo 2 unitats del producte amb codi de barres 7777777 a la venta
    Aleshores la venta té 2 línies
    I el total de la venta actual és de €240€

  Escenari: Aplicar un descompte (promoció)
    Quan creo un nou descompte del tipus promoció anomenat "3x2OptPri" de 3x2 sobre el producte amb codi de barres 1234567
    I inicio una nova venta
    I afegeixo 6 unitats del producte amb codi de barres 1234567 a la venta
    Aleshores la venta té 2 línies
    I el total de la venta actual és de €80€

  Escenari: Aplicar un descompte (regal)
    Quan creo un nou descompte del tipus regal anomenat "Choripan+OptPrime", on amb la compra del producte amb codi de barres 7777777 es regala una unitat del producte amb codi de barres 1234567
    I inicio una nova venta
    I afegeixo 1 unitats del producte amb codi de barres 1234567 a la venta
    I afegeixo 1 unitats del producte amb codi de barres 7777777 a la venta
    Aleshores la venta té 3 línies
    I el total de la venta actual és de €150€

  Escenari: Aplicar un descompte (combinat)
    Quan creo un nou descompte del tipus percentatge anomenat "20%-Nintendo" del %20% sobre el producte amb codi de barres 7777777
    I creo un nou descompte del tipus regal anomenat "Choripan+OptPrime", on amb la compra del producte amb codi de barres 7777777 es regala una unitat del producte amb codi de barres 1234567
    I inicio una nova venta
    I afegeixo 1 unitats del producte amb codi de barres 1234567 a la venta
    I afegeixo 1 unitats del producte amb codi de barres 7777777 a la venta
    Aleshores la venta té 4 línies
    I el total de la venta actual és de €120€
