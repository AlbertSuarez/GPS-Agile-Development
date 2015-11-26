# language: ca

  #noinspection SpellCheckingInspection
Característica: Consultar els desquadraments

  Rerefons:
    Donat que estem al tpv número 1 de la botiga "Girona 1"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i rep l'identificador 123
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de 37€

  Escenari: enregistrem un desquadrament i el consultem
    Quan s'emmagatzema el desquadrament de caixa amb un desquadrament de 01€
    I consulto els desquadraments
    Aleshores obtinc un desquadrament número 0 del caixer amb nom "Joan" a la botiga "Girona 1" d'una quantitat de 01€

  Escenari: no hi ha desquadraments
    Quan consulto els desquadraments
    Aleshores obtinc un error que diu: "No hi ha cap desquadrament enregistrat al sistema"
