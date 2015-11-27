# language: ca

  #noinspection SpellCheckingInspection
Característica: Quadrar la caixa en finalitzar torn

  Rerefons:
    Donat que estem al tpv número 1 de la botiga "Girona 1"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i rep l'identificador 123
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de €37€
    I un producte amb nom "Hipopotamo traga-bolas", preu €23€, iva %21% i codi de barres 1234567

  Escenari: El caixer comprova el quadrament i aquest és positiu o igual a 0
    Donat que he afegit el producte de codi de barres 1234567 a la venta
    I indico que el client ha entregat €30€ per a pagar en metàlic
    Quan intento finalitzar el meu torn, indicant un efectiu final de €60€
    Aleshores el tpv es troba en estat "disponible"

  Escenari: El caixer comprova el quadrament i aquest és negatiu
    Donat que he afegit el producte de codi de barres 1234567 a la venta
    I indico que el client ha entregat €30€ per a pagar en metàlic
    Quan intento finalitzar el meu torn, indicant un efectiu final de €56€
    Aleshores obtinc un error que diu: "La caixa no quadra: hi ha un desquadrament de 4.0€"
    I el tpv es troba en estat "ocupat"

  Escenari: El caixer decideix tancar torn tot i haver-hi desquadrament
    Donat que he afegit el producte de codi de barres 1234567 a la venta
    I indico que el client ha entregat €30€ per a pagar en metàlic
    Quan finalitzo el meu torn amb un desquadrament, amb un efectiu final de €56€
    Aleshores queda registrat un desquadrament del caixer amb nom "Joan" a la botiga "Girona 1" d'una quantitat de €4€
    I el tpv es troba en estat "disponible"
