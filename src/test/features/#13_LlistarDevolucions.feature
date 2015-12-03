# language: ca

  #noinspection SpellCheckingInspection
Característica: Llistar devolucions

  Rerefons:
    Donat que estem al panell de gestió del product manager
    I un producte amb nom "Barbie programadora informatica", preu €20€, iva %21% i codi de barres 1234567
    I un producte amb nom "Libro: El programa de Caillou no compila", preu €5€, iva %21% i codi de barres 456789

  Escenari: No s'ha fet cap devolució
    Quan consulto les devolucions
    Aleshores obtinc un error que diu: "No hi ha cap devolució enregistrada al sistema"

  Escenari: obtenim devolucions
    Donada una devolució de 2 unitats del producte amb codi de barres 1234567 amb motiu "No funciona"
    I una devolució de 1 unitats del producte amb codi de barres 456789 amb motiu "Altres"
    Quan consulto les devolucions
    Aleshores obtinc 2 devolucions
    I obtinc una devolució número 1  amb 1 linia de devolucio de 2 unitats del producte amb nom "Barbie programadora informatica" amb motiu "No funciona"
    I obtinc una devolució número 2  amb 1 linia de devolucio de 1 unitats del producte amb nom "Libro: El programa de Caillou no compila" amb motiu "Altres"
