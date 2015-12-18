# language: ca

  #noinspection SpellCheckingInspection
Característica: Consulta flux de diners entre caixes

  Rerefons:
    Donat que estem al panell de gestió del product manager
    I que estem al tpv número 1 de la botiga "Girona"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i rep l'identificador 123
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de €50€

  Escenari: no hi ha flux de diners entre caixes
    Quan consulto els fluxos de diners entre caixes
    Aleshores obtinc un error que diu: "No hi ha cap flux de diners enregistrat al sistema"

  Escenari: no hi ha flux de diners d'un tipus determinat
    Quan consulto els fluxos de diners entre caixes del tipus "TPVFlow"
    Aleshores obtinc un error que diu: "No hi ha cap flux de diners de tipus TPVFlow enregistrat al sistema"

  Escenari: consulta increments de caixa
    Donat un increment de caixa de €30€
    Quan consulto els fluxos de diners entre caixes
    Aleshores obtinc 1 fluxes de diners
    I obtinc un flux 1 de tipus "InFlow" d'una quantitat de €30€ de "origen" a "Girona 1"
    I el tpv 1 de la botiga "Girona" té un efectiu total de €80€

  Escenari: consulta decrements de caixa
    Donat un decrement de caixa de €15€
    Quan consulto els fluxos de diners entre caixes
    Aleshores obtinc 1 fluxes de diners
    I obtinc un flux 1 de tipus "OutFlow" d'una quantitat de €15€ de "Girona 1" a "desti"
    I el tpv 1 de la botiga "Girona" té un efectiu total de €35€

  Escenari: consulta de flux entre dos caixes
    Donat que estem al tpv número 2 de la botiga "Girona"
    I que el "Pere" s'ha registrat al sistema amb password "qwerty" i rep l'identificador 456
    I que s'inica el torn al tpv amb identificador 456 i password "qwerty", amb un efectiu inicial de €30€
    I un traspàs €40€ de la botiga "Girona" del tpv número 1 al tpv número 2
    Quan consulto els fluxos de diners entre caixes
    Aleshores obtinc 1 fluxes de diners
    I obtinc un flux 1 de tipus "TPVFlow" d'una quantitat de €40€ de "Girona 1" a "Girona 2"
    I el tpv 1 de la botiga "Girona" té un efectiu total de €10€
    I el tpv 2 de la botiga "Girona" té un efectiu total de €70€

  Escenari: consulta de fluxos de diferents tipus
    Donat que estem al tpv número 2 de la botiga "Girona"
    I que el "Pere" s'ha registrat al sistema amb password "qwerty" i rep l'identificador 456
    I que s'inica el torn al tpv amb identificador 456 i password "qwerty", amb un efectiu inicial de €30€
    I un increment de caixa de €10€
    I un increment de caixa de €25€
    I un decrement de caixa de €40€
    I un traspàs €20€ de la botiga "Girona" del tpv número 1 al tpv número 2
    Quan consulto els fluxos de diners entre caixes
    Aleshores obtinc 4 fluxes de diners
    I obtinc un flux 1 de tipus "InFlow" d'una quantitat de €10€ de "origen" a "Girona 2"
    I obtinc un flux 2 de tipus "InFlow" d'una quantitat de €25€ de "origen" a "Girona 2"
    I obtinc un flux 3 de tipus "OutFlow" d'una quantitat de €40€ de "Girona 2" a "desti"
    I obtinc un flux 4 de tipus "TPVFlow" d'una quantitat de €20€ de "Girona 1" a "Girona 2"
    I el tpv 1 de la botiga "Girona" té un efectiu total de €30€
    I el tpv 2 de la botiga "Girona" té un efectiu total de €45€

  Escenari: consulta d'un tipus concret de fluxes de diners
    Donat que estem al tpv número 2 de la botiga "Girona"
    I que el "Pere" s'ha registrat al sistema amb password "qwerty" i rep l'identificador 456
    I que s'inica el torn al tpv amb identificador 456 i password "qwerty", amb un efectiu inicial de €30€
    I un increment de caixa de €10€
    I un increment de caixa de €25€
    I un decrement de caixa de €40€
    I un traspàs €20€ de la botiga "Girona" del tpv número 1 al tpv número 2
    Quan consulto els fluxos de diners entre caixes del tipus "InFlow"
    Aleshores obtinc 2 fluxes de diners
    I obtinc un flux 1 de tipus "InFlow" d'una quantitat de €10€ de "origen" a "Girona 2"
    I obtinc un flux 2 de tipus "InFlow" d'una quantitat de €25€ de "origen" a "Girona 2"
    I el tpv 1 de la botiga "Girona" té un efectiu total de €30€
    I el tpv 2 de la botiga "Girona" té un efectiu total de €45€
