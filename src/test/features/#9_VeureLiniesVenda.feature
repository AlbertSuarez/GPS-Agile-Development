# language: ca

  #noinspection SpellCheckingInspection
Característica: Quadrar la caixa en finalitzar torn

  Rerefons:
    Donat que estem al tpv número 1 de la botiga "Girona 1"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i reb l'identificador 123
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de 37€
    I un producte amb nom "Optimus Prime", preu 23€, iva 21% i codi de barres 1234567

  Escenari: Consulta de linies de venda
    Donat un producte venut amb codi de barres 1234567
    Aleshores es consulta les linies de venda