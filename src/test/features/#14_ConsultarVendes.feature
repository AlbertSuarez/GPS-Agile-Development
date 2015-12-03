# language: ca

#noinspection SpellCheckingInspection
Característica: Consultar vendes

  Rerefons:
    Donat que estem al panell de gestió del product manager
    I un producte amb nom "Optimus Prime", preu €23€, iva %21% i codi de barres 1234567
    I un producte amb nom "Playmobil", preu €23€, iva %21% i codi de barres 12121212

  Escenari: llista totes les vendes
    Donat que hi ha hagut una venda del producte amb codi de barres 1234567 pagat en metode "card"
    Aleshores obtinc el producte amb nom "Optimus Prime"

  Escenari: no hi ha vendes
    Quan consulto les vendes
    Aleshores obtinc un error que diu: "No hi ha cap venda enregistrada al sistema"

  Escenari: llistar vendes per mètode de pagament
    Donat que hi ha hagut una venda del producte amb codi de barres 1234567 pagat en metode "card"
    Quan vull llistar les vendes pagades en "card"
    Aleshores obtinc el producte amb nom "Optimus Prime" pagat en "card"


