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
- Se agregó el sistema de combate, junto a la diferenciación de este en cada piso.
- Se implementó la habitación con tesoro y con jefe.
- Se agregó la textura de la fogata en el inicio del juego.
  ### Fixed
- Se arregló problema en los dialogos.
- Se arregló la aparición de pisos sin sala de boss.
- Se arregló la creación de un mapa muy corto en los pisos.
- Se arregló un problema en el movimiento.
  
## [0.3.1] 22-09-2026
### Added
- Se implementó un sistema en el cual se pueden agregar flags sin que estas nuevas interfieran en un juego guardado antiguo.
  
## [0.3.2] 23-09-2026
### Added 
- Se agregaron variables que indican los valores maximos de vida, maná y estamina en los combates.
  ### Fixed
  - Se arregló un error critico que al momento de iniciar una bossfight en la respectiva sala el juego crasheaba.
    
