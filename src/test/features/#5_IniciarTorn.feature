# language: ca

#noinspection SpellCheckingInspection
Característica: Iniciar el torn al tpv

  Rerefons:
    Donat que estem al tpv número 1 de la botiga "Girona 1"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i reb l'identificador 123

  Escenari: Iniciar el torn
    Quan inicio el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de 25.47€
    Aleshores el tpv es troba en estat "ocupat"
    I el tpv està en ús per en "Joan"
    I el tpv té un efectiu inicial de 25.47€

  Escenari: Un venedor no pot inicar el torn si ja hi ha un altre venedor al tpv
    Quan inicio el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de 25.47€
    I inicio el torn al tpv amb identificador 111 i password "qwerty", amb un efectiu inicial de 25.47€
    Aleshores obtinc un error que diu: "Aquest tpv està en ús per Joan"

  Escenari: Usuari i/o password incorrectes
    Quan inicio el torn al tpv amb identificador 111 i password "qwerty", amb un efectiu inicial de 25.47€
    Aleshores el tpv es troba en estat "disponible"

  Escenari: Excés d'intents d'inici
    Quan inicio el torn al tpv amb identificador 123 i password "asdfg", amb un efectiu inicial de 25.47€ 5 cops
    Aleshores el tpv es troba en estat "bloquejat"

  Escenari: No es pot iniciar un torn en un tpv bloquejar
    Quan inicio el torn al tpv amb identificador 123 i password "asdfg", amb un efectiu inicial de 25.47€ 5 cops
    I inicio el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de 25.47€
    Aleshores obtinc un error que diu: "Aquest tpv està bloquejat"

  Escenari: Desbloquejar un tpv bloquejat
    Quan inicio el torn al tpv amb identificador 123 i password "asdfg", amb un efectiu inicial de 25.47€ 5 cops
    I desbloquejo el tpv amb el password "someMasterPass"
    Aleshores el tpv es troba en estat "disponible"

  Escenari: No es pot desbloquejar un tpv no bloquejat
    Quan desbloquejo el tpv amb el password "someMasterPass"
    Aleshores obtinc un error que diu: "Aquest tpv no està bloquejat"

  Escenari: Usuari d'administrador incorrecte
    Quan inicio el torn al tpv amb identificador 123 i password "asdfg", amb un efectiu inicial de 25.47€ 5 cops
    I desbloquejo el tpv amb el password "qwerty"
    Aleshores obtinc un error que diu: "Password d'administrador incorrecte"