# language: ca

  #noinspection SpellCheckingInspection
Característica: Consultar els desquadraments

  Rerefons:
    Donat que estem al panell de gestió del product manager

  Escenari: no hi ha desquadraments al sistema
    Quan consulto els desquadraments
    Aleshores obtinc un error que diu: "No hi ha cap desquadrament enregistrat al sistema"

  Escenari: no hi ha desquadraments a la botiga
    Quan consulto els desquadraments de la botiga "Girona"
    Aleshores obtinc un error que diu: "No hi ha cap desquadrament enregistrat al sistema de la botiga Girona"

  Escenari: obtenim desquadraments del sistema
    Donat un desquadrament del caixer amb nom "Joan" a la botiga "Girona" d'una quantitat de €10,40€
    I un desquadrament del caixer amb nom "Pere" a la botiga "Barcelona" d'una quantitat de €15,05€
    Quan consulto els desquadraments
    Aleshores obtinc 2 desquadrament/s
    I obtinc un desquadrament número 1 del caixer amb nom "Joan" a la botiga "Girona" d'una quantitat de €10,40€
    I obtinc un desquadrament número 2 del caixer amb nom "Pere" a la botiga "Barcelona" d'una quantitat de €15,05€
    
  Escenari: obtenim desquadraments per botiga
    Donat un desquadrament del caixer amb nom "Joan" a la botiga "Girona" d'una quantitat de €10,40€
    I un desquadrament del caixer amb nom "Pere" a la botiga "Barcelona" d'una quantitat de €15,05€
    Quan consulto els desquadraments de la botiga "Barcelona"
    Aleshores obtinc 1 desquadrament/s
    I obtinc un desquadrament número 1 del caixer amb nom "Pere" a la botiga "Barcelona" d'una quantitat de €15,05€
