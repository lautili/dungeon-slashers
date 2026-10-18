## [0.1.0] 21-08-2026
### Added 
- Se creó toda la estructura del juego.
- Se realizaron la mayoría de las clases necesarias para la funcionalidad del juego.

## [0.1.1] 23-08-2026
### Added
- Se agregó un sistema de colisiones funcional.
- Se agregó un sistema de flags para diferentes clases.
### Changed
- Se cambio el inicio del primer dialogo de la firstscreen para que se active si su flag no está activa.
- Se elimino el array de booleanos que indicaba los bosses eliminados en la clase player y fue remplazado por flags generales.

## [0.1.2] 27-08-2026
### Added
- Se agregó la tienda con su respectivo funcionamiento.
- Se agregó el portrait del personaje Sage/curandero.

## [0.2.0] 07-09-2026
### Added
- Se agregó la interacción con objetos.
- Se agregó la interacción con puertas y sus respectivos cambios de pantalla entre habitación y habitación.
- Se implementó la lógica para elegir los personajes para la aventura (Se pulira más adelante).
- Se implementó la generación de las habitaciones en los pisos (Se pulirá más adelante).
- Se implementó la interacción con la tienda al entrar a su respectiva autorización.
- Se agregó el portrait del explorador.
- Se implementó la acción de guardado.
  ### Fixed
- Se arregló un error en los dialogos donde estos no devolvian el ID correspondiente. 

## [0.3.0] 21-09-2026
### Added
- Se implementó el sistema de combate por turnos. Basado en cada piso con sus respectivos enemigos.
- Se implementaron habitaciones con tesoros y habitaciones de jefes.
- Se agregó la textura de la fogata del inicio.
  ### Fixed
- Se arregló un problema con los dialogos.
- Se arregló un error que hacia que no se creasen las habitaciones de los jefes en algunos pisos.
- Se arregló el problema de tener un mapa con muy pocas habitaciones.
- Se arregló un problema de movimiento contra las paredes.

## [0.3.1] 22-09-2026
  ### Fixed
- Se arregló el sistema de cargado de flags para que si una partida antigua se carga no interfiera con las nuevas flags.

## [0.3.2] 22-09-2026
### Added
- Se agregó indicadores maximos y actuales de la salud, la stamina y el maná en el combate.
  ### Fixed
- Se arregló un error critico al iniciar una bossfight, el cual crasheaba el juego.

## [0.4.0] 03-10-2026
### Added
- Se agregó: Un bestiario, el portrait de la exploradora junto con sprites para ella, ajustes, Todos los pisos faltantes, todos los enemigos, sonidos, la opcion de analizar enemigos con la C.
### Changed
- Se reworkeo el sistema de combate para darle más dinamismo al mismo y que esté más optimizado.
- Se cambio el guardado de la tienda, ahora se guarda en store.JSON.
- El sistema de niveles fue cambiado.
- El mapa ahora muestra el tipo de habitación.
  ### Fixed
- Se arreglaron problemas en el sistema de dialogos.
- Se arreglaron problemas en el sistema de combate.

## [0.4.1] 06-10-2026
### Added
- Se agregaron nuevas opciones en el menu de ajustes.

## [0.4.2] 07-10-2026
### Changed
- Se cancela el análisis con X.
- Cuando una acción tiene a un enemigo derrotado esta acción cambia a un enemigo aleatorio, esto antes se salteaba, (depende de la acción también puede afectar a aliados).
- Se cambió que los enemigos no mostrasen el daño recibido al morir y que sus sprites desaparecian al instante.
  ### Fixed
- Se arregló que en ocasiones especiales (muy especificas), las acciones del primer turno de una batalla que no sea la primera en hacerse podian saltear completamente y pasar al proximo turno.
- Se arregló que al accionarse un item y morir antes de usarlo se pueda usar aun en el combate.
- Se arregló el precio y la cantidad maximas de venenos, ya que estaban intercambiadas.
- Los enemigos confundidos mostraban el indicador de daño en el objetivo previo a estar confundidos. Se cambio por completo el como se muestran los enemigos afectados en el sistema para arreglarlo.

## [0.4.3] 13-10-2026
### Added
- Se agregaron los sprites de los enemigos del 2do piso pero no el del boss.
- Se agregó un tutorial al inicio.
- Se agregó el guardado de las opciones de combate.

## [0.5.0] 18-10-2026
### Added
- Se agregó nuevo sistema de tipos para las armas. Uno de ellos tipo CUR, que permite al curandero usar su ataque para curar aliados.
- Se agregaron los sprites de los enemigos comunes del piso 3.
- Se agregó una pantalla final al terminar el juego. 

Beta Testers:
- Macarena Panasiuk.
- Lautili.
- Felopo.
Agradecimientos especiales a Macarena Panasiuk por la ayuda en el beta testing.