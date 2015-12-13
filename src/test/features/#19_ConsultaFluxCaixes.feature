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

  Escenari: consulta increments de caixa
    Donat un increment de caixa de €30€
    Quan consulto els fluxos de diners entre caixes
    Aleshores obtinc 1 flux de tipus "InFlow" de total €30€
    I el tpv 1 de la botiga "Girona" té un efectiu total de €80€

  Escenari: consulta decrements de caixa
    Donat un decrement de caixa de €15€
    Quan consulto els fluxos de diners entre caixes
    Aleshores obtinc 1 flux de tipus "OutFlow" de total €15€
    I el tpv 1 de la botiga "Girona" té un efectiu total de €35€

  Escenari: consulta de flux entre dos caixes
    Donat que estem al tpv número 2 de la botiga "Girona"
    I que el "Pere" s'ha registrat al sistema amb password "qwerty" i rep l'identificador 456
    I que s'inica el torn al tpv amb identificador 456 i password "qwerty", amb un efectiu inicial de €30€
    I un traspàs €40€ de la botiga "Girona" del tpv número 1 al tpv número 2
    Quan consulto els fluxos de diners entre caixes
    Aleshores obtinc 1 flux de tipus "TPVFlow" de total €40€
    I el tpv 1 de la botiga "Girona" té un efectiu total de €10€
    I el tpv 2 de la botiga "Girona" té un efectiu total de €70€

  Escenari: consulta de fluxos de diferents tipus
    Donat un increment de caixa de €10€
    I un increment de caixa de €25€
    I un decrement de caixa de €40€
    Quan consulto els fluxos de diners entre caixes
    Aleshores obtinc 2 flux de tipus "InFlow" de total €35€
    I obtinc 1 flux de tipus "OutFlow" de total €40€
    I el tpv 1 de la botiga "Girona" té un efectiu total de €45€
