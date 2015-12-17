# language: ca

  #noinspection SpellCheckingInspection
Característica: Alta d'un caixer

  Rerefons:
    Donat que estem al panell de gestió del product manager
    
  Escenari: no hi ha cap caixer donat d'alta
    Quan consulto els caixers del sistema
    Aleshores obtinc un error que diu: "No hi ha caixers donats d'alta al sistema"

  Escenari: alta d'un nou caixer
    Quan afegeixo un caixer amb nom "Jesus Del Piero" i contrasenya "neveraroja7"
    Aleshores hi ha 1 caixer al sistema
    I el caixer té per nom "Jesus Del Piero" i la seva contrasenya és "neveraroja7"
    
  Escenari: error d'alta d'un caixer amb nom ja existent
    Donat un caixer amb nom "Jesus Del Piero" i contrasenya "neveraroja7"
    Quan afegeixo un caixer amb nom "Jesus Del Piero" i contrasenya "barcelona7"
    Aleshores obtinc un error que diu: "Ja existeix un caixer amb aquest nom"
