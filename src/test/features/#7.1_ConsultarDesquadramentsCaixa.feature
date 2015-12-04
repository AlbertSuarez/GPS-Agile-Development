# language: ca

  #noinspection SpellCheckingInspection
Característica: Consultar els desquadraments fets a un tpv

  Rerefons:
    Donat un producte amb nom "Hipopotamo traga-bolas", preu €23€, iva %21% i codi de barres 1234567
    I  un producte amb nom "Hipopotamo volador", preu €19,99€, iva %18% i codi de barres 42
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i rep l'identificador 123
    I que estem al tpv número 1 de la botiga "Girona 1"
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de €37€
    I que he afegit el producte de codi de barres 1234567 a la venta
    I indico que el client ha entregat €30€ per a pagar en metàlic
    I que el TPV es troba en procés de quadrament
    I finalitzo el meu torn amb un desquadrament, amb un efectiu final de €56€
    I que estem al tpv número 2 de la botiga "Barcelona"
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de €5,52€
    I que he afegit el producte de codi de barres 42 a la venta
    I indico que el client ha entregat €35,1€ per a pagar en metàlic
    I que el TPV es troba en procés de quadrament
    I finalitzo el meu torn amb un desquadrament, amb un efectiu final de €20€
    I que estem al panell de gestió del product manager


  Escenari: no hi ha desquadraments a la botiga
    Quan consulto els desquadraments de la botiga "Badalona"
    Aleshores obtinc un error que diu: "No hi ha cap desquadrament enregistrat al sistema de la botiga Badalona"

  Escenari: obtenim desquadraments del sistema
    Quan consulto els desquadraments
    Aleshores obtinc 2 desquadrament/s
    I obtinc un desquadrament número 1 del caixer amb nom "Joan" a la botiga "Girona 1" d'una quantitat de €4€

  Escenari: obtenim desquadraments per botiga Girona 1
    Quan consulto els desquadraments de la botiga "Girona 1"
    Aleshores obtinc 1 desquadrament/s
    I obtinc un desquadrament número 1 del caixer amb nom "Joan" a la botiga "Girona 1" d'una quantitat de €4€

  Escenari: obtenim desquadraments per botiga Barcelona
    Quan consulto els desquadraments de la botiga "Barcelona"
    Aleshores obtinc 1 desquadrament/s
    I obtinc un desquadrament número 1 del caixer amb nom "Joan" a la botiga "Barcelona" d'una quantitat de €5,51€
