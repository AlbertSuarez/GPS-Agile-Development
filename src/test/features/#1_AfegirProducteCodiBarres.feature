# language: ca

  #noinspection SpellCheckingInspection
Característica: Afegir producte per codi de barres

  Rerefons:
    Donat un producte amb nom "Hipopotamo traga-bolas", preu 23€, iva 21% i codi de barres 1234567
    I que estem al tpv número 1 de la botiga "Girona 1"
    I que en "Joan" ha iniciat el torn al tpv

  Escenari: Afegim producte sense iniciar venda
    Quan afegeixo el producte de codi de barres 1234567 a la venta
    Aleshores la venta esta iniciada
    I el total de la venta actual és de 23€
    I la venda conté el producte amb codi de barres 1234567
    I la venta té 1 línia

