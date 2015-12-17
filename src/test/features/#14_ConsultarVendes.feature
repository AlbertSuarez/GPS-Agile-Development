# language: ca

#noinspection SpellCheckingInspection
Característica: Consultar vendes

  Rerefons:
    Donat que estem al panell de gestió del product manager
    I un producte amb nom "Optimus Prime", preu €23€, iva %21% i codi de barres 1234567
    I un producte amb nom "Playmobil", preu €23€, iva %21% i codi de barres 12121212
    I un producte amb nom "Barbie", preu €30€, iva %21% i codi de barres 01010101

  Escenari: llista totes les vendes
    Donat que hi ha hagut una venda amb id 1 del producte amb codi de barres 1234567 pagat metode "card" el dia "12/02/2013" a les 11 hores, 20 minuts i 30 segons
    Quan vull llistar totes les vendes
    Aleshores la venda 1 conté el producte amb nom "Optimus Prime" venut el dia "12/02/2013" amb import total €23€

  Escenari: no hi ha vendes
    Quan consulto les vendes
    Aleshores obtinc un error que diu: "No hi ha cap venda enregistrada al sistema"

  Escenari: llistar vendes per mètode de pagament
    Donat que hi ha hagut una venda amb id 1 del producte amb codi de barres 1234567 pagat metode "card" el dia "12/02/2013" a les 11 hores, 20 minuts i 30 segons
    I que hi ha hagut una venda amb id 2 del producte amb codi de barres 12121212 pagat metode "cash" el dia "13/02/2013" a les 11 hores, 20 minuts i 30 segons
    Quan vull llistar les vendes pagades en "card"
    Aleshores la venda 1 conté el producte amb nom "Optimus Prime" venut el dia "12/02/2013" amb import total €23€

  Escenari: llistar vendes d'un dia determinat
    Donat que hi ha hagut una venda amb id 1 del producte amb codi de barres 1234567 pagat metode "card" el dia "12/02/2013" a les 11 hores, 20 minuts i 30 segons
    I que hi ha hagut una venda amb id 2 del producte amb codi de barres 12121212 pagat metode "card" el dia "13/02/2013" a les 11 hores, 20 minuts i 30 segons
    Quan vull llistar les vendes del dia "12/02/2013"
    Aleshores obtinc 1 venda
    I la venda 1 conté el producte amb nom "Optimus Prime" venut el dia "12/02/2013" amb import total €23€

  Escenari: llistar vendes d'un periode
    Donat que hi ha hagut una venda amb id 1 del producte amb codi de barres 1234567 pagat metode "card" el dia "12/02/2013" a les 11 hores, 20 minuts i 30 segons
    I que hi ha hagut una venda amb id 2 del producte amb codi de barres 12121212 pagat metode "card" el dia "13/02/2013" a les 11 hores, 20 minuts i 30 segons
    I que hi ha hagut una venda amb id 3 del producte amb codi de barres 01010101 pagat metode "card" el dia "15/02/2013" a les 11 hores, 20 minuts i 30 segons
    Quan vui llistar les vendes entre el dia "12/02/2013" i el dia "13/02/2013"
    Aleshores obtinc 2 venda
    I la venda 1 conté el producte amb nom "Optimus Prime" venut el dia "12/02/2013" amb import total €23€
    I la venda 2 conté el producte amb nom "Playmobil" venut el dia "13/02/2013" amb import total €23€


  Escenari: llistar vendes amb més d'una linia de venda
    Donat que hi ha hagut una venda amb id 1 paga en "card" el dia "12/02/2013" a les 11 hores 20 minuts i 30 segons
    I la venta conté el producte amb codi de barres 1234567
    I la venta conté el producte amb codi de barres 12121212
    Quan vull llistar les vendes del dia "12/02/2013"
    Aleshores obtinc 1 venda
    I la venda 1 esta feta el dia "12/02/2013" amb import total €46€
    I la venda 1 conté el producte 1 amb nom "Optimus Prime"
    I la venda 1 conté el producte 2 amb nom "Playmobil"
