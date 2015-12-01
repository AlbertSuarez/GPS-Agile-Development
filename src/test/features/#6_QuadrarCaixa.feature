# language: ca

  #noinspection SpellCheckingInspection
Característica: Quadrar la caixa en finalitzar torn

  Rerefons:
    Donat que estem al tpv número 1 de la botiga "Girona 1"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i rep l'identificador 123
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de €37€
    I un producte amb nom "Hipopotamo traga-bolas", preu €23€, iva %21% i codi de barres 1234567

  Escenari: El caixer indica l'inici del procés de quadrament
    Quan indico al TPV que inicio el procés de quadrament
    Aleshores el tpv es troba en estat "en quadrament"
    
  Escenari: El caixer intenta iniciar una venda durant el procés de quadrament
    Donat que el TPV es troba en procés de quadrament
    Quan afegeixo el producte de codi de barres 1234567 a la venta
    Aleshores obtinc un error que diu: "La caixa es troba en procés de quadrament"

  Escenari: El caixer prova un nou intent de quadrar la caixa amb èxit
    Donat que he afegit el producte de codi de barres 1234567 a la venta
    I indico que el client ha entregat €30€ per a pagar en metàlic, i registro la venda amb codi de venda 123456789
    I que el TPV es troba en procés de quadrament
    Quan intento finalitzar el meu torn, indicant un efectiu final de €60€
    Aleshores el tpv es troba en estat "disponible"

  Escenari: El caixer prova un nou intent de quadrar la caixa però hi ha un desquadrament negatiu
    Donat que he afegit el producte de codi de barres 1234567 a la venta
    I indico que el client ha entregat €30€ per a pagar en metàlic, i registro la venda amb codi de venda 123456789
    I que el TPV es troba en procés de quadrament
    Quan intento finalitzar el meu torn, indicant un efectiu final de €56€
    Aleshores obtinc un error que diu: "La caixa no quadra: hi ha un desquadrament de 4.0€"
    I el tpv es troba en estat "en quadrament"

  Escenari: El caixer decideix tancar torn tot i haver-hi desquadrament
    Donat que he afegit el producte de codi de barres 1234567 a la venta
    I indico que el client ha entregat €30€ per a pagar en metàlic, i registro la venda amb codi de venda 123456789
    I que el TPV es troba en procés de quadrament
    Quan finalitzo el meu torn amb un desquadrament, amb un efectiu final de €56€
    Aleshores queda registrat un desquadrament del caixer amb nom "Joan" a la botiga "Girona 1" d'una quantitat de €4€
    I el tpv es troba en estat "disponible"
