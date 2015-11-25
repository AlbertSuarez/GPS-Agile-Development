# language: ca

  #noinspection SpellCheckingInspection
Característica: Afegir producte per codi de barres

  Rerefons:
    Donat un producte amb nom "Hipopotamo traga-bolas", preu 23€, iva 21% i codi de barres 1234567
    I que estem al tpv número 1 de la botiga "Girona 1"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i rep l'identificador 123
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de 25.47€

  Escenari: Afegim producte sense iniciar venda
    Quan afegeixo el producte de codi de barres 1234567 a la venta
    Aleshores la venta esta iniciada
    I el total de la venta actual és de 23€
    I la venda conté el producte amb codi de barres 1234567
    I la venta té 1 línia

  Escenari: Obtinc preu producte
    Quan afegeixo el producte de codi de barres 1234567 a la venta
    Aleshores la pantalla del client del tpv mostra
    """
    Hipopotamo traga-bolas - 23€/u x 1u = 23€
    ---
    Total: 23€
    """

  Escenari: Afegim producte amb 2 unitats
    Quan afegeixo 2 unitats del producte amb codi de barres 1234567 a la venta
    Aleshores la venta esta iniciada
    I el total de la venta actual és de 46€
    I la venda conté el producte amb codi de barres 1234567
    I la venta té 1 línia

  Escenari: Afegim producte amb 2 unitats amb la venda iniciada
    Quan inicio una nova venta
    I afegeixo 2 unitats del producte amb codi de barres 1234567 a la venta
    Aleshores el total de la venta actual és de 46€
    I la venda conté el producte amb codi de barres 1234567
    I la venta té 1 línia

  Escenari: Afegim producte amb la venda iniciada
    Quan inicio una nova venta
    I afegeixo el producte de codi de barres 1234567 a la venta
    Aleshores el total de la venta actual és de 23€
    I la venda conté el producte amb codi de barres 1234567
    I la venta té 1 línia



