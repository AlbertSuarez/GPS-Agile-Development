# language: ca

  #noinspection SpellCheckingInspection
Característica: Flux de diners de caixes
  
  Rerefons:
    Donat que estem al tpv número 1 de la botiga "Girona 1"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i rep l'identificador 123
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de €50€
    
  Escenari: Afegim efectiu a la caixa
    Quan incremento l'efectiu de la caixa en €30€
    Aleshores el tpv té un efectiu total de €80€
    
  Escenari: Retirem efectiu de la caixa
    Quan decremento l'efectiu de la caixa en €30€
    Aleshores el tpv té un efectiu total de €20€
    
  Escenari: Intentem retirar més efectiu del disponible
    Quan decremento l'efectiu de la caixa en €80€
    Aleshores obtinc un error que diu: "El tpv no disposa de prou efectiu per a realitzar la retirada"

