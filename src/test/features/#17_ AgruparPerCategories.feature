# language: ca

  #noinspection SpellCheckingInspection
Característica: Agrupar productes per categoria

  Rerefons:
    Donat que estem al tpv número 1 de la botiga "Girona"
    I un producte amb nom "monopopopoly", preu €23€, iva %21% i codi de barres 12345
    I un producte amb nom "parchischis", preu €20€, iva %21% i codi de barres 23456
    I un producte amb nom "Pilota", preu €10€, iva %21% i codi de barres 34567

  Escenari: Creo una categoria inexistent
    Quan creo una nova categoria amb nom "jocs de taula"
    I consulto les categories
    Aleshores obtinc una categoria número 1 amb nom "jocs de taula"

  Escenari: Intento crear una categoria que ja existeix
    Quan creo una nova categoria amb nom "jocs de taula"
    I creo una nova categoria amb nom "jocs de taula"
    Aleshores obtinc un error que diu: "Ja existeix una categoria amb aquest nom"

  Escenari: LListo els productes d'una categoria
    Quan creo una nova categoria amb nom "jocs de taula"
    Quan creo una nova categoria amb nom "Altres"
    I afegeixo el producte amb codi de barres 12345 a la categoria "jocs de taula"
    I afegeixo el producte amb codi de barres 23456 a la categoria "jocs de taula"
    I afegeixo el producte amb codi de barres 34567 a la categoria "Altres"
    I consulto els productes de la categoria "jocs de taula"
    Aleshores obtinc un producte número 1 amb nom "monopopopoly"
    I obtinc un producte número 2 amb nom "parchischis"

