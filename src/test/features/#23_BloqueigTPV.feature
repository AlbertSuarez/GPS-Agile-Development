# language: ca

  #noinspection SpellCheckingInspection
Característica: Bloqueig d'un TPV

  Rerefons:
    Donat que estem al panell de gestió del product manager
    I que configurem el password mestre del sistema com a "someMasterPass"
    I que ens desconectem del panell de gestio del product manager
    I que estem al tpv número 1 de la botiga "Girona 1"
    I que el "Joan" s'ha registrat al sistema amb password "asdf" i rep l'identificador 123
    I un producte amb nom "Hipopotamo traga-bolas", preu €23€, iva %21% i codi de barres 1234567

  Escenari: bloquegem el TPV
    Quan bloquejo el tpv amb el password "someMasterPass"
    Aleshores el tpv es troba en estat "bloquejat"
    
  Escenari: intentem bloquejar un TPV ja bloquejat
    Donat que el TPV està bloquejat
    Quan bloquejo el tpv amb el password "someMasterPass"
    Aleshores obtinc un error que diu: "Aquest TPV ja està bloquejat"
    
  Escenari: intentem bloquejar un TPV amb una venta activa
    Donat que hi ha una venta iniciada
    Quan bloquejo el tpv amb el password "someMasterPass"
    Aleshores obtinc un error que diu: "Hi ha una venta activa"
   
  Escenari: intentem bloquejar un TPV amb una contrassenya incorrecta
    Quan bloquejo el tpv amb el password "wrongMasterPass"
    Aleshores obtinc un error que diu: "Password d'administrador incorrecte"
    
  Escenari: desbloquegem el TPV
    Donat que el TPV està bloquejat
    Quan desbloquejo el tpv amb el password "someMasterPass"
    Aleshores el tpv es troba en estat "disponible"
    
  Escenari: intentem desbloquejar un TPV no bloquejat
    Quan desbloquejo el tpv amb el password "someMasterPass"
    Aleshores obtinc un error que diu: "Aquest tpv no està bloquejat"
    
  Escenari: intentem desbloquejar un TPV amb una contrassenya incorrecta
    Donat que el TPV està bloquejat
    Quan desbloquejo el tpv amb el password "wrongMasterPass"

  Escenari: intentem iniciar una venda en un tpv bloquejat
    Donat que el TPV està bloquejat
    Quan afegeixo 1 unitats del producte amb codi de barres 1234567 a la venta
    Aleshores obtinc un error que diu: "Aquest tpv està bloquejat"

  Escenari: intentem extreure diners d'un tpv bloquejat
    Donat que el TPV està bloquejat
    Quan decremento l'efectiu de la caixa en €10€
    Aleshores obtinc un error que diu: "Aquest tpv està bloquejat"
