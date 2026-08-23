package io.github.dungeon_slashers;

import java.util.Random;

import io.github.dungeon_slashers.entities.Entity;

/*
		CLASE EFFECT

	será la clase encargada de controlar los efectos de estado.

*/

public class Effect {
	
	private String name; // nombre completo
	private String shortName; //nombre corto
	private String msg; //mensaje al aplicarse
	private String turnMsg; //mensaje por turno (puede ser null)
	private String endMsg; //mensaje al terminar el efecto
	private int minTurns; //los turnos minimos que durar�
	private int maxTurns; //los turnos maximos que durar�
	private transient Random rand = new Random();
	int turn; //los turnos que lleva activo
	
	//constructor
	public Effect(String shortName) {
		this.shortName = shortName;
		initVars();
	}
	
	//para inicializar los estados
	private void initVars() {
		switch(shortName) {
		case "DWN":
			name = "Noqueado";
			msg = " cay�!";
			endMsg = " revivio!";
		break;
		case "BLE":
			name = "Sangrado";
			msg = " esta sangrando!";
			turnMsg = " sufre por desangrarse.";
			endMsg = " ya no esta sangrando.";
			minTurns = 4;
			maxTurns = 7;
		break;
		case "RAG":
			name = "Ira";
			msg = " esta cegado por la ira!";
			turnMsg = " ataca a cualquiera por su ira!";
			endMsg = " deja de estar enojado.";
			minTurns = 3;
			maxTurns = 4;
			break;
		case "CON":
			name = "Confusion";
			msg = " esta confundido! quizas se ataque a si mismo!";
			turnMsg = " se ve afectado por la confusion...";
			endMsg = " ya no esta confundido!";
			minTurns = 3;
			maxTurns = 5;
			break;
		case "SLE":
			name = "Sue�o";
			msg = " se quedo dormido!";
			turnMsg = " duerme...";
			endMsg = " desperto!";
			minTurns = 4;
			maxTurns = 6;
			break;
		case "SIL":
			name = "Silencio";
			msg = " esta silenciado! no puede usar habilidades!";
			endMsg = " rompio el silencio!";
			minTurns = 3;
			maxTurns = 5;
			break;
		case "POI":
			name = "Envenenamiento";
			msg = " esta envenenado!";
			turnMsg = " sufre da�os por el veneno...";
			endMsg = " se curo del veneno!";
			minTurns = 5;
			maxTurns = 9;
			break;
		case "TIR":
			name = "Cansancio";
			msg = " esta cansado...";
			turnMsg = " pierde estamina por el cansancio!";
			endMsg = " no esta mas cansado!";
			minTurns = 4;
			maxTurns = 7;
			break;
		case "ENC":
			name = "Encantacion";
			msg = " fue encantado!";
			turnMsg = " pierde mana por el hechizo...";
			endMsg = " dejo de estar hechizado!";
			minTurns = 4;
			maxTurns = 7;
			break;
		case "BEN":
			name = "Bendicion";
			msg = " fue bendecido!";
			turnMsg = " siente los efectos del cari�o de los Dioses.";
			endMsg = " perdio sus beneficios divinos!";
			minTurns = 4;
			maxTurns = 7;
			break;
		}
	}
		
	//getters
	public String getName() {
		return name;
	}
	public String getMSG() {
		return msg;
	}
	public String getTurnMsg() {
		return turnMsg;
	}
	public String getEndMsg() {
		return endMsg;
	}
	public int getMinTurns() {
		return minTurns;
	}
	public int getMaxTurns() {
		return maxTurns;
	}
	public String getShortName() {
		return shortName;
	}

	public boolean checkTurns() {
		turn++;
		if(turn > minTurns) {
			if(rand.nextInt(50) < 40) {
				return true;
			}else if(turn == maxTurns){
				return true;
			}
		}
		return false;
	}

	public void use(Entity a) {
		switch(shortName) {
		case "BLE":
			a.modHP(- (int) (a.getHP() * 0.08));
		break;
		case "POI":
			a.modHP(- (int) (a.getHP() * 0.05));
			break;
		case "TIR":
			a.modSP(- (int) (a.getSP() * 0.1));
			break;
		case "ENC":
			a.modHP(- (int) (a.getHP() * 0.1));
			break;
		case "BEN":
			a.modHP((int) (a.getHP() * 0.15));
			a.modMP((int) (a.getMP() * 0.15));
			a.modSP((int) (a.getSP() * 0.15));
			break;
		}
	}
}
