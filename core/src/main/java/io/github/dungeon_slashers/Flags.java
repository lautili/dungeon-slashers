package io.github.dungeon_slashers;

import io.github.dungeon_slashers.controllers.DialMan;
import io.github.dungeon_slashers.entities.Entity;

public class Flags {
	public static final int FLAG_FIRSTSCREEN_DIALOGUE_START = 0;
	public static final int FLAG_FIRSTBOSS_DEFEATED = 1;
	public static final int FLAG_FIRSTBOSS_DIALOGUE = 2;
	public static final int FLAG_SECONDBOSS_DEFEATED = 3;
	public static final int FLAG_THIRDBOSS_DEFEATED = 4;
	public static final int FLAG_FOURTHBOSS_DEFEATED = 5;
	public static final int FLAG_FIFTHBOSS_DEFEATED = 6;
	public static final int FLAG_SECONDBOSS_DIALOGUE = 7;
	public static final int FLAG_THIRDBOSS_DIALOGUE = 8;
	public static final int FLAG_FOURTHBOSS_DIALOGUE = 9;
	public static final int FLAG_FIFTHBOSS_DIALOGUE = 10;
	
	public static void DialogueFlag(int flag, Entity entity) {
		switch(flag) {
		case FLAG_FIRSTBOSS_DIALOGUE:
			DialMan.addDialogue(0, 1, "???", null, "Hola...", 20, null);
        	DialMan.addDialogue(1, 2, entity.getName(), null, "Soy... Ogro...", 20, null);
        	DialMan.addDialogue(2, 100, entity.getName(), null, "Preparense... porque lo que se viene...", 20, null);
        	DialMan.addDialogue(100, -1, entity.getName(), null, "No es para nada bonito.", 100, null);
			break;
		case FLAG_SECONDBOSS_DIALOGUE:
			DialMan.addDialogue(0, 1, "???", null, "...", 40, null);
			DialMan.addDialogue(1, 2, "???", null, "Se han tomado su tiempo.", 40, null);
			DialMan.addDialogue(2, 3, "???", null, "Que mi apariencia no les engañe, camaradas.", 40, null);
			DialMan.addDialogue(3, 4, "???", null, "La falta de un rostro visible no implica la incapacidad de observar.", 40, null);
			DialMan.addDialogue(4, 5, "Jinete", null, "Soy el Jinete sin Cabeza.", 40, null);
			DialMan.addDialogue(5, 6, "Jinete", null, "Es una lastima...", 40, null);
			DialMan.addDialogue(6, 7, "Jinete", null, "Que nuestra reunion vaya a ser tan... efimera.", 40, null);
			DialMan.addDialogue(7, 8, "Jinete", null, "Pues tras este punto, no pasarán.", 40, null);
			DialMan.addDialogue(8, 100, "Jinete", null, "Desenfunden sus armas, o rindanse al dolor.", 40, null);
			DialMan.addDialogue(100, -1, "Jinete", null, "De todas maneras... Es el final del trayecto.", 40, null);
			break;
		case FLAG_THIRDBOSS_DIALOGUE:
			DialMan.addDialogue(0, 1, "Directauro", null, "Damas y caballeros... bienvenidos al tercer acto.", 40, null);
			DialMan.addDialogue(1, 2, "Cometauro", null, "Espero que hayan venido a reirse.", 15, null);
			DialMan.addDialogue(2, 3, "Tragetauro", null, "Porque toda buena historia necesita una tragedia.", 40, null);
			DialMan.addDialogue(3, 4, "Absutauro", null, "Tragedia? necesita un redoble de tambores. O la vuelta al trueque.", 20, null);
			DialMan.addDialogue(4, 5, "Poetauro", null, "O la cabeza de un Jeque.", 20, null);
			DialMan.addDialogue(5, 6, "Directauro", null, "Poetauro, por favor. Todavia no hemos de rimar.", 40, null);
			DialMan.addDialogue(6, 7, "Poetauro", null, "Que me has de reclamar? Si es un buen ejemplar.", 20, null);
			DialMan.addDialogue(7, 8, "Poetauro", null, "La cabeza de algun Jeque,", 20, null);
			DialMan.addDialogue(8, 9, "Poetauro", null, "Ayuda a representar", 20, null);
			DialMan.addDialogue(9, 10, "Poetauro", null, "Que a ningun reino que llegue, nos hemos de arrodillar.", 20, null);
			DialMan.addDialogue(10, 11, "Cometauro", null, "La poesia no seria poesia sin Poe.", 20, null);
			DialMan.addDialogue(11, 12, "Directauro", null, "Poe?", 40, null);
			DialMan.addDialogue(12, 13, "Cometauro", null, "Poes claro! sin un poco de comedia.", 20, null);
			DialMan.addDialogue(13, 14, "Directauro", null, "Habria sido mas gracioso si lo relacionabas a Edgar Allan Poe...", 40, null);
			DialMan.addDialogue(14, 15, "Absutauro", null, "Poe Dameron?", 20, null);
			DialMan.addDialogue(15, 16, "Tragetauro", null, "La Antigona se quedara corta...", 40, null);
			DialMan.addDialogue(16, 17, "Cometauro", null, "(aunque preferimos Las Nubes)", 20, null);
			DialMan.addDialogue(17, 18, "Poetauro", null, "cuando cortemos su arteria aorta...", 20, null);
			DialMan.addDialogue(18, 19, "Absutauro", null, "(pagaremos por adelantado, muchas gracias)", 20, null);
			DialMan.addDialogue(19, 20, "Cometauro", null, "Aunque no lo hagamos adrede!", 20, null);
			DialMan.addDialogue(20, 21, "Poetauro", null, "(y manchemos las paredes.)", 20, null);
			DialMan.addDialogue(21, 22, "Tragetauro", null, "Y Aunque puede que a nuestro final conlleve.", 40, null);
			DialMan.addDialogue(22, 23, "Directauro", null, "Posiciones. El telon esta por abrirse.", 20, null);
			DialMan.addDialogue(23, 24, "Cometauro", null, "Posiciones? Pero si somos cabezas", 20, null);
			DialMan.addDialogue(24, 100, "Tragetauro", null, "Y luego de esto quizas ni eso...", 20, null);
			DialMan.addDialogue(100, -1, "Directauro", null, "Que comience la funcion.", 40, null);
			break;
		case FLAG_FOURTHBOSS_DIALOGUE:
			DialMan.addDialogue(0, 1, "???", null, "Detenganse.", 50, null);
			DialMan.addDialogue(1, 2, entity.getName(), null, "He observado su avance desde que entraron.", 50, null);
			DialMan.addDialogue(2, 3, entity.getName(), null, "Han cambiado su forma de pelear desde el inicio.", 50, null);
			DialMan.addDialogue(3, 4, Main.player.getCharacters()[0].getName(), Main.player.getCharacters()[0].getTexture(), 
					"No perdimos el tiempo.", 20, null);;
			DialMan.addDialogue(4, 5, entity.getName(), null, "Eso es lo que diferencia a un grupo de mercenarios de un verdadero equipo.", 50, null);
			DialMan.addDialogue(5, 6, entity.getName(), null, "Pero un equipo tambien puede ser desarmado.", 50, null);
			DialMan.addDialogue(6, 7, entity.getName(), null, "Voy a demostrarles como.", 50, null);
			DialMan.addDialogue(7, 100, entity.getName(), null, "Preparen sus armas.", 50, null);
			DialMan.addDialogue(100, -1, entity.getName(), null, "Veamos si realmente aprendieron algo.", 50, null);
			break;
		case FLAG_FIFTHBOSS_DIALOGUE:
			DialMan.addDialogue(0, 1, "???", null, "...", 60, null);
			DialMan.addDialogue(1, 2, entity.getName(), null, "Asi que llegaron.", 40, null);
			DialMan.addDialogue(2, 3, entity.getName(), null, "Cinco pisos. Decenas de Secuaces. Y aqui siguen.", 40, null);
			DialMan.addDialogue(3, 4, entity.getName(), null, "Me pregunto, que esperaban encontrar al final?", 40, null);
			DialMan.addDialogue(4, 5, entity.getName(), null, "Una recompensa? Una respuesta? Un enemigo digno?", 40, null);
			DialMan.addDialogue(5, 6, entity.getName(), null, "No importa.", 40, null);
			DialMan.addDialogue(6, 7, entity.getName(), null, "Ya estan aqui.", 40, null);
			DialMan.addDialogue(7, 8, entity.getName(), null, "Y yo tambien.", 40, null);
			DialMan.addDialogue(8, 9, Main.player.getCharacters()[0].getName(), Main.player.getCharacters()[0].getTexture(),
					"Antes de luchar, queremos respuestas... Por que fuimos enviados aqui?", 20, null);
			DialMan.addDialogue(9, 10, entity.getName(), null, "Oh, por que?", 40, null);
			DialMan.addDialogue(10, 11, entity.getName(), null, "Veras...", 40, null);
			DialMan.addDialogue(11, 12, entity.getName(), null, "Esta organizacion lleva actuando desde las sombras por siglos ya.", 40, null);
			DialMan.addDialogue(12, 13, entity.getName(), null, "Funciona conmigo o sin mi, ni siquiera soy el rango mas alto del lugar.", 40, null);
			DialMan.addDialogue(13, 14, entity.getName(), null, "Fui designado a esta mazmorra porque contenia algo muy importante...", 40, null);
			DialMan.addDialogue(14, 15, entity.getName(), null, "Una Raiz Dorada.", 40, null);
			DialMan.addDialogue(15, 16, Main.player.getCharacters()[2].getName(), Main.player.getCharacters()[2].getTexture(),
					"Raiz dorada?", 20, null);
			DialMan.addDialogue(16, 17, entity.getName(), null, "Si, exactamente. una Raiz dorada.", 40, null);
			DialMan.addDialogue(17, 18, entity.getName(), null, "Esta... cosa... que se encuentra en la habitacion detras de mi.", 40, null);
			DialMan.addDialogue(18, 19, entity.getName(), null, "Una fuente de energia gigante... con poder casi ilimitado.", 40, null);
			DialMan.addDialogue(19, 20, entity.getName(), null, "Parte de la raiz se encuentra en su lider. su Manager.", 40, null);
			DialMan.addDialogue(20, 21, entity.getName(), null, "Es lo que permite que ustedes puedan revivir sin ningun tipo de "
					+ "consecuencia como las de un hechizo de necromancia.", 40, null);
			DialMan.addDialogue(21, 22, Main.player.getCharacters()[1].getName(), Main.player.getCharacters()[1].getTexture(),
					"El... Manager?", 20, null);
			DialMan.addDialogue(22, 23, entity.getName(), null, "Ustedes estan bajo contrato de nuestra organizacion.", 40, null);
			DialMan.addDialogue(23, 24, entity.getName(), null, "Pense que seria obvio... siendo que esta mazmorra no se comporta de forma"
					+ " comun.", 40, null);
			DialMan.addDialogue(24, 26, Main.player.getCharacters()[2].getName(), Main.player.getCharacters()[2].getTexture(),
					"Pero...", 40, null);
			DialMan.addDialogue(26, 27, entity.getName(), null, "Necesitabamos ver los efectos bajo sujetos no informados sobre las propiedades "
					+ "de la raiz.", 40, null);
			DialMan.addDialogue(27, 28, entity.getName(), null, "A la Dorada no parece gustarle cuando se es consciente de la misma.", 40, null);
			DialMan.addDialogue(28, 29, entity.getName(), null, "Sus propiedades no actuan con tanta fuerza cuando es asi...", 40, null);
			DialMan.addDialogue(29, 30, entity.getName(), null, "Pero gracias a eso se volvieron tan fuerte como lo son ahora.", 40, null);
			DialMan.addDialogue(30, 31, entity.getName(), null, "Aunque... ya no tenemos uso para ustedes.", 40, null);
			DialMan.addDialogue(31, 100, entity.getName(), null, "Mis ordenes son las de acabar con ustedes. Asi que...", 40, null);
			DialMan.addDialogue(100, -1, entity.getName(), null, "Terminemos con esto.", 40, null);
			break;
		}
	}
}
