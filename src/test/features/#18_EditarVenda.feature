# language: ca

  #noinspection SpellCheckingInspection
Característica: Editar una venda
  
  Rerefons:
    Donat que estem al tpv número 1 de la botiga "Girona"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i rep l'identificador 123
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de €50€
    I un producte amb nom "Hipopotamo traga-bolas", preu €20€, iva %21% i codi de barres 1234567
    I un producte amb nom "Nintendo choripan", preu €150€, iva %21% i codi de barres 7777777
    
  Escenari: cancelem una venda iniciada
    Donat que he afegit el producte de codi de barres 1234567 a la venta
    Quan indico que vull cancelar la venta actual
    Aleshores el tpv es troba en estat "disponible"
    
  Escenari: intento iniciar una venda actual sense haver-la iniciat
    Quan indico que vull cancelar la venta actual
    Aleshores obtinc un error que diu: "No hi ha cap venta iniciada"

  Escenari: afegim unitats d'un producte a una línia de venda
    Donat que he afegit el producte de codi de barres 1234567 a la venta
    I que he afegit el producte de codi de barres 7777777 a la venta
    Quan afegeixo 2 unitat/s de la línia de venda 1
    Aleshores la venta té 2 línies
    I el total de la venta actual és de €210€
    
  Escenari: intentem afegir unitats d'un producte a una línia de venda no existent
    Donat que he afegit el producte de codi de barres 1234567 a la venta
    Quan afegeixo 2 unitat/s de la línia de venda 2
    Aleshores obtinc un error que diu: "La línia de venta no existeix a la venta actual"

  Escenari: treiem unitats d'un producte a una línia de venda
    Donat que he afegit el producte de codi de barres 1234567 a la venta
    I afegeixo 2 unitats del producte amb codi de barres 7777777 a la venta
    Quan elimino 1 unitat/s de la línia de venda 2
    Aleshores la venta té 2 línies
    I el total de la venta actual és de €170€
    
  Escenari: intentem treure unitats d'un producte a una línia de venda no existent
    Donat que he afegit el producte de codi de barres 1234567 a la venta
    Quan elimino 1 unitat/s de la línia de venda 2
    Aleshores obtinc un error que diu: "La línia de venta no existeix a la venta actual"

  Escenari: treiem totes les unitats d'un producte a una línia de venda
    Donat que he afegit el producte de codi de barres 1234567 a la venta
    I que he afegit el producte de codi de barres 7777777 a la venta
    Quan elimino 1 unitat/s de la línia de venda 2
    Aleshores la venta té 1 línia
    I el total de la venta actual és de €20€

  Escenari: intentem treure més unitats de les possibles a una línia
    Donat que he afegit el producte de codi de barres 1234567 a la venta
    I que he afegit el producte de codi de barres 7777777 a la venta
    Quan elimino 3 unitat/s de la línia de venda 2
    Aleshores obtinc un error que diu: "No es poden eliminar més unitats de les que hi ha a la línia de venda"

  Escenari: elimino una línia de venda
    Donat que he afegit el producte de codi de barres 1234567 a la venta
    I que he afegit el producte de codi de barres 7777777 a la venta
    Quan elimino la línia de venda 1
    Aleshores la venta té 1 línia
    I el total de la venta actual és de €150€
    
  Escenari: intento eliminar una línia de venda no existent
    Donat que he afegit el producte de codi de barres 1234567 a la venta
    Quan elimino la línia de venda 2
    Aleshores obtinc un error que diu: "La línia de venta no existeix a la venta actual"
