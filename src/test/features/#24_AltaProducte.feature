# language: ca

  #noinspection SpellCheckingInspection
Característica: Alta d'un producte

  Rerefons:
    Donat que estem al panell de gestió del product manager
    
  Escenari: no hi ha cap producte donat d'alta
    Quan consulto els productes del sistema
    Aleshores obtinc un error que diu: "No hi ha productes donats d'alta al sistema"

  Escenari: alta d'un nou producte
    Quan afegeixo un producte amb nom "Halcón Milenario de LEGO", preu €49,95€, iva %21% i codi de barres 1234567
    Aleshores hi ha 1 productes al sistema
    I el producte número 1 té per nom "Halcón Milenario de LEGO", preu €49,95€, IVA %21% i codi de barres 1234567
    
  Escenari: error d'alta d'un producte amb codi de barres ja existent
    Donat un producte amb nom "Halcón Milenario de LEGO", preu €49,95€, iva %21% i codi de barres 1234567
    Quan afegeixo un producte amb nom "Estrella de la muerte de LEGO", preu €99,95€, iva %21% i codi de barres 1234567
    Aleshores obtinc un error que diu: "Ja existeix un producte amb aquest codi de barres"
