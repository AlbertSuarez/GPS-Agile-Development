# language: ca

  #noinspection SpellCheckingInspection
Característica: Crear nous descomptes del tipus regal per a conjunts

  Rerefons:
    Donat que estem al tpv número 1 de la botiga "Girona 1"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i rep l'identificador 123
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de €37€
    I un producte amb nom "Optimus Prime", preu €20€, iva %21% i codi de barres 1234567
    I un producte amb nom "Chewbacca de peluche", preu €60€, iva %21% i codi de barres 1111111
    I un producte amb nom "Nintendo choripan", preu €160€, iva %21% i codi de barres 7777777
    I un producte amb nom "PlayStation 5", preu €400€, iva %21% i codi de barres 7654321
    I creo una nova categoria amb nom "Consola"
    I creo una nova categoria amb nom "Altres"
    I afegeixo el producte amb codi de barres 1234567 a la categoria "Altres"
    I afegeixo el producte amb codi de barres 1111111 a la categoria "Altres"
    I afegeixo el producte amb codi de barres 7777777 a la categoria "Consola"
    I afegeixo el producte amb codi de barres 7654321 a la categoria "Consola"

  Escenari: aplicar un descompte de regals per a un conjunt d'elements
    Donat creo un nou descompte del tipus regal anomenat "ConsolaXAltres", on amb la compra del producte d'algun dels productes amb codi de barres "7777777,7654321" es regala una unitat d'algun dels poductes amb codi de barres "1234567,1111111"
    Quan afegeixo 1 unitats del producte amb codi de barres 1234567 a la venta
    I afegeixo 1 unitats del producte amb codi de barres 1111111 a la venta
    I afegeixo 1 unitats del producte amb codi de barres 7777777 a la venta
    Aleshores la venta té 3 línies
    I el total de la venta actual és de €220€
    
  Escenari: aplicar un descompte de regals per a una categoria
    Donat creo un nou descompte del tipus regal anomenat "ConsolaXAltres", on amb la compra del producte d'algun dels productes de la categoria "Consola" es regala una unitat d'algun dels poductes de la categoria "Altres"
    Quan afegeixo 1 unitats del producte amb codi de barres 1234567 a la venta
    I afegeixo 1 unitats del producte amb codi de barres 1111111 a la venta
    I afegeixo 1 unitats del producte amb codi de barres 7777777 a la venta
    I afegeixo 1 unitats del producte amb codi de barres 7654321 a la venta
    Aleshores la venta té 4 línies
    I el total de la venta actual és de €560€
