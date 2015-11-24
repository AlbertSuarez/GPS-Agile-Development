# language: ca

  #noinspection SpellCheckingInspection
Característica: Quadrar la caixa en finalitzar torn

  Rerefons:
    Donat que estem al tpv número 1 de la botiga "Girona 1"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i reb l'identificador 123
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de 37.45€

  Escenari: La caixa quadra
    Quan incremento l'efectiu de la caixa en 2.97€
    I finalitzo el meu torn, amb un efectiu final de 40.42€
    Aleshores s'emmagatzema el quadrament de caixa

  Escenari: La caixa no quadra
    Quan incremento l'efectiu de la caixa en 2.97€
    I finalitzo el meu torn, amb un efectiu final de 40.43€
    Aleshores s'emmagatzema el desquadrament de caixa