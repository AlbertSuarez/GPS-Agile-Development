# language: ca

#noinspection SpellCheckingInspection
Característica: Iniciar el torn al tpv

  Rerefons:
    Donat que estem al tpv número 1 de la botiga "Girona 1"

  Escenari: Iniciar el torn
    Quan inicio el torn al tpv amb identificador 123 i password "asdf"
    Aleshores el tpv està en ús per en "Joan"

  Escenari: Iniciar el torn
    Quan inicio el torn al tpv amb identificador 123 i password "asdf"
    Aleshores el tpv està en ús per en "Joan"

  Escenari: Un venedor no pot inicar el torn si ja hi ha un altre venedor al tpv
    Donat que 123 ha iniciat el torn al tpv amb password "asdf"
    Quan inicio el torn al tpv amb identificador 111 i password "qwerty"
    Aleshores obtinc un error que diu: "Aquest tpv està en ús per Joan"