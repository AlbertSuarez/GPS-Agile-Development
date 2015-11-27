# language: ca

  #noinspection SpellCheckingInspection
Característica: Afegir producte per nom

  Rerefons:
    Donat un producte amb nom "Hipopotamo traga-bolas", preu €23€, iva 21% i codi de barres 1234567
    I un producte amb nom "Hipopotamo volador", preu €3€, iva 19% i codi de barres 7890
    I que estem al tpv número 1 de la botiga "Girona 1"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i rep l'identificador 123
    I que s'inica el torn al tpv amb identificador 123 i password "asdf", amb un efectiu inicial de €25.47€

  Escenari: Afegim producte sense iniciar venda
    Quan afegeixo el producte per nom "Hipopotamo traga-bolas" a la venta
    Aleshores la venta esta iniciada
    I el total de la venta actual és de €23€
    I la venda conté el producte amb codi de barres 1234567
    I la venda conté el producte amb nom "Hipopotamo traga-bolas"
    I la venta té 1 línia


  Escenari: Afegim producte amb venda iniciada
    Quan inicio una nova venta
    I afegeixo el producte per nom "Hipopotamo traga-bolas" a la venta
    Aleshores el total de la venta actual és de €23€
    I la venda conté el producte amb codi de barres 1234567
    I la venda conté el producte amb nom "Hipopotamo traga-bolas"
    I la venta té 1 línia


  Escenari: Afegim producte amb 2 unitats
    Quan afegeixo 5 unitats del producte per nom "Hipopotamo volador" a la venta
    Aleshores la venta esta iniciada
    I el total de la venta actual és de €15€
    I la venda conté el producte amb nom "Hipopotamo volador"
    I la venta té 1 línia

  Escenari: Intentem afegir producte per una part del nom que no es unica d'un sol producte
    Quan afegeixo el producte per nom "Hipopotamo" a la venta
    Aleshores obtinc 2 noms de productes
    I el producte numero 1 es "Hipopotamo traga-bolas"
    I el producte numero 2 es "Hipopotamo volador"
    I la venta no esta iniciada

  Escenari: Obtinc preu producte amb 5 unitats
    Quan afegeixo 5 unitats del producte per nom "Hipopotamo traga-bola" a la venta
    Aleshores la pantalla del client del tpv mostra
    """
    Hipopotamo traga-bolas - 23.0€/u x 5u = 115.0€
    ---
    Total: 115.0€
    """

  Escenari: Intentem afegir producte que no existeix
    Quan afegeixo el producte per nom "ESTO NO EXISTE" a la venta
    Aleshores obtinc un error que diu: "No es pot afegir un producte inexistent"
