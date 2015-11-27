# language: ca

  #noinspection SpellCheckingInspection
Característica: Afegir un descompte manual

  Rerefons:
    Donat que estem al tpv número 1 de la botiga "Girona 1"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i rep l'identificador 123
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de €37€
    I un producte amb nom "Optimus Prime", preu €20€, iva 21% i codi de barres 1234567
    I un producte amb nom "Nintendo choripan", preu €150€, iva 21% i codi de barres 7777777

  Escenari: Afegir un descompte manual (percentatge)
    Donat que hi ha una venta iniciada
    I que he afegit el producte de codi de barres 1234567 a la venta
    Quan Aplico un descompte manual que anomeno "opt-25%" al producte 1 de la venda amb valor 25%
    Aleshores la venta té 2 línies
    I el total de la venta actual és de €15€

  Escenari: Descomptem una línia que no existeix
    Donat que hi ha una venta iniciada
    I que he afegit el producte de codi de barres 1234567 a la venta
    I que he afegit el producte de codi de barres 7777777 a la venta
    Quan Aplico un descompte manual que anomeno "foo-99%" al producte 3 de la venda amb valor 99%
    Aleshores obtinc un error que diu: "No es pot accedir a la línia 3 de la venta, aquesta només té 2 línies"

  Escenari: No hi ha cap venda
    Quan Aplico un descompte manual que anomeno "no-se-que-descompto-si-no-tinc-res-a-descomptar" al producte 1 de la venda amb valor 150%
    Aleshores obtinc un error que diu: "No hi ha cap venda iniciada"
