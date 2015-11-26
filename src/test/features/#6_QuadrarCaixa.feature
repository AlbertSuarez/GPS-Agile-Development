# language: ca

  #noinspection SpellCheckingInspection
Característica: Quadrar la caixa en finalitzar torn

  Rerefons:
    Donat que estem al tpv número 1 de la botiga "Girona 1"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i rep l'identificador 123
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de 37€

  Escenari: La caixa quadra o el quadrament és positiu
    Quan incremento l'efectiu de la caixa en 11€
    I finalitzo el meu torn, amb un efectiu final de 48€
    Aleshores el tpv tanca el torn actual

  Escenari: La caixa no quadra
    Quan incremento l'efectiu de la caixa en 11€
    I finalitzo el meu torn, amb un efectiu final de 47€
    Aleshores s'emmagatzema el desquadrament de caixa amb un desquadrament de 01€
    I el tpv tanca el torn actual
