package io.github.dungeon_slashers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.dungeon_slashers.controllers.Battle;
import io.github.dungeon_slashers.controllers.Config;
import io.github.dungeon_slashers.controllers.InputMan;
import io.github.dungeon_slashers.controllers.Menu;
import io.github.dungeon_slashers.controllers.Save;
import io.github.dungeon_slashers.controllers.Store;
import io.github.dungeon_slashers.entities.Boss;
import io.github.dungeon_slashers.entities.BossEvent;
import io.github.dungeon_slashers.entities.Enemy;
import io.github.dungeon_slashers.entities.Hero;
import io.github.dungeon_slashers.floors.Floor;
import io.github.dungeon_slashers.item.Armor;
import io.github.dungeon_slashers.item.Item;
import io.github.dungeon_slashers.item.Weapon;
import io.github.dungeon_slashers.screens.BattleScreen;
import io.github.dungeon_slashers.screens.CharSelectScreen;
import io.github.dungeon_slashers.screens.FirstScreen;
import io.github.dungeon_slashers.screens.MainMenuScreen;
import io.github.dungeon_slashers.screens.MenuScreen;
import io.github.dungeon_slashers.screens.StoreScreen;
import io.github.dungeon_slashers.screens.floorScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

	public static Player player = new Player();
	public static Config config;
	public static Battle[] battles;
	public static Enemy[] enemies;
	public static Item[] items;
	public static Hero[] characters = new Hero[5];

	public SpriteBatch batch;
	public FitViewport viewport;
	public double roomDelay;

	public BitmapFont mainFont;
	public BitmapFont invFont;
	public BitmapFont dialFont;
	public BitmapFont titleFont;

	public Texture colBox;
	public Texture intBox;
	public Texture doorBox;

	public Texture roomDisc;
	public Texture roomUnd;
	public Texture roomCurr;
	public Texture roomStart;
	public Texture roomTreasure;
	public Texture roomBoss;
	public Texture roomSecret;
	public Texture mapBackground;

	public Texture battleBar;
	public Texture HPbar;
	public Texture MPbar;
	public Texture SPbar;
	public Texture behindCharactersBattle;
	public Texture battleMenu;

	public Texture dialogueBox;
	public Texture dialogueBBox;
	public Texture nameBox;
	public Texture nameBBox;
	public Texture atkBox;
	public Texture portraitBox;
	public Texture choiceBox;
	public Texture fightOrFleeBox;

	public Texture chestClosed;
	public Texture chestOpen;
	public Texture workedFireplace;

	//screens
	public FirstScreen firstScreen;
	public CharSelectScreen charSelectScreen;
	public MenuScreen menuScreen;
	public StoreScreen storeScreen;

	public BattleScreen defBattleScreen;
	public BattleScreen firstBossFight;
	public BattleScreen defBattle2Screen;
	public BattleScreen secondBossFight;
	public BattleScreen defBattle3Screen;
	public BattleScreen thirdBossFight;
	public BattleScreen defBattle4Screen;
	public BattleScreen fourthBossFight;
	public BattleScreen defBattle5Screen;
	public BattleScreen fifthBossFight;

	public Screen lastScreen;

	public Floor firstFloor;
	public floorScreen firstFloorScreen;

	public Floor secondFloor;
	public floorScreen secondFloorScreen;
	
	public Floor thirdFloor;
	public floorScreen thirdFloorScreen;
	
	public Floor fourthFloor;
	public floorScreen fourthFloorScreen;
	
	public Floor fifthFloor;
	public floorScreen fifthFloorScreen;

	public Texture currentChar;
	public Texture selection;

    @Override
    public void create() {
    	
    	batch = new SpriteBatch();
		mainFont = new BitmapFont(Gdx.files.internal("ui/fonts/main.fnt"));
		dialFont = new BitmapFont(Gdx.files.internal("ui/fonts/dialogue.fnt"));
		titleFont = new BitmapFont(Gdx.files.internal("ui/fonts/title.fnt"));
		invFont = new BitmapFont(Gdx.files.internal("ui/fonts/inventory.fnt"));
		viewport = new FitViewport(320, 180);
		colBox = new Texture("collision_box.png");
		intBox = new Texture("interaction_box.png");
		doorBox = new Texture("door_box.png");

		roomUnd = new Texture("ui/map/room_und.jpg");
		roomDisc = new Texture("ui/map/room_disc.jpg");
		roomCurr = new Texture("ui/map/room_curr.jpg");
		roomStart = new Texture("ui/map/room_start.png");
		roomTreasure = new Texture("ui/map/room_treasure.png");
		roomSecret = new Texture("ui/map/room_secret.png");
		roomBoss = new Texture("ui/map/room_boss.png");
		mapBackground = new Texture("ui/map/map_background.png");

		roomDelay = 0.5f;

		Menu.viewport = viewport;
		Save.game = this;
		InputMan.game = this;

		//font has 15pt, but we need to scale it to our viewport by ratio of viewport height to screen height
		mainFont.setUseIntegerPositions(false);
		mainFont.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight() * 1f);
		dialFont.setUseIntegerPositions(false);
		titleFont.setUseIntegerPositions(false);
		titleFont.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight() * 3f);
		dialFont.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight() * 1.5f);
		invFont.setUseIntegerPositions(false);
		invFont.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight() * 3);

    	initializeGame();

    	chestClosed = new Texture("sprites/objects/chest_closed.jpg");
    	chestOpen = new Texture("sprites/objects/chest_open.png");
    	workedFireplace = new Texture("sprites/objects/worked-fireplace.png");

    	battleBar = new Texture("ui/battleBar.png");
    	HPbar = new Texture("ui/HPbar.png");
    	MPbar = new Texture("ui/MPbar.png");
    	SPbar = new Texture("ui/SPbar.png");
    	behindCharactersBattle = new Texture("ui/behindCharactersBattle.jpg");
    	currentChar = new Texture("ui/currentCharBox.png");
    	selection = new Texture("ui/selection.png");
    	battleMenu = new Texture("ui/battle-menu.png");

    	dialogueBox = new Texture("ui/box-dialogue.png");
    	dialogueBBox = new Texture("ui/box-battle-dialogue.png");
    	nameBox = new Texture("ui/box-name.png");
    	nameBBox = new Texture("ui/box-battle-name.png");
    	choiceBox = new Texture("ui/box-choice.png");
    	portraitBox = new Texture("ui/box-portrait.png");
    	fightOrFleeBox = new Texture("ui/box-fightorflee.png");
    	atkBox = new Texture("ui/box-attack.png");


    	firstScreen = new FirstScreen(this);
    	menuScreen = new MenuScreen(this);
    	storeScreen = new StoreScreen(this);
    	charSelectScreen = new CharSelectScreen(this);
    	
    	fifthFloor = new Floor(25, 25, 200, 300, defBattle5Screen, fifthBossFight, 1, 25,
    			getItem("minHPot"), getItem("minMPot"), getItem("minSPot"),
    			getItem("minHPven"), getItem("minSPven"), getItem("minMPven"),
    			getItem("ironSword"), getItem("woodenStaff"),
    			getItem("leatherArmor"), getItem("ironArmor"), getItem("wizardRobes"));
    	fifthFloorScreen = new floorScreen(this, fifthFloor, firstScreen, firstScreen, Flags.FLAG_FIFTHBOSS_DIALOGUE);
    	
    	fourthFloor = new Floor(25, 25, 200, 250, defBattle4Screen, fourthBossFight, 1, 25,
    			getItem("minHPot"), getItem("minMPot"), getItem("minSPot"),
    			getItem("minHPven"), getItem("minSPven"), getItem("minMPven"),
    			getItem("ironSword"), getItem("woodenStaff"),
    			getItem("leatherArmor"), getItem("ironArmor"), getItem("wizardRobes"));
    	fourthFloorScreen = new floorScreen(this, fourthFloor, fifthFloorScreen, firstScreen, Flags.FLAG_FOURTHBOSS_DIALOGUE);
    	
    	thirdFloor = new Floor(20, 20, 150, 200, defBattle3Screen, thirdBossFight, 1, 25,
    			getItem("minHPot"), getItem("minMPot"), getItem("minSPot"),
    			getItem("minHPven"), getItem("minSPven"), getItem("minMPven"),
    			getItem("ironSword"), getItem("woodenStaff"),
    			getItem("leatherArmor"), getItem("ironArmor"), getItem("wizardRobes"));
    	thirdFloorScreen = new floorScreen(this, thirdFloor, fourthFloorScreen, firstScreen, Flags.FLAG_THIRDBOSS_DIALOGUE);
    	
    	secondFloor = new Floor(20, 20, 50, 100, defBattle2Screen, secondBossFight, 1, 25,
    			getItem("minHPot"), getItem("minMPot"), getItem("minSPot"),
    			getItem("minHPven"), getItem("minSPven"), getItem("minMPven"),
    			getItem("ironSword"), getItem("woodenStaff"),
    			getItem("leatherArmor"), getItem("ironArmor"), getItem("wizardRobes"));
    	secondFloorScreen = new floorScreen(this, secondFloor, thirdFloorScreen, firstScreen, Flags.FLAG_SECONDBOSS_DIALOGUE);

    	firstFloor = new Floor(15, 15, 25, 50, defBattleScreen, firstBossFight, 1, 25,
    			getItem("minHPot"), getItem("minMPot"), getItem("minSPot"),
    			getItem("minHPven"), getItem("minSPven"), getItem("minMPven"),
    			getItem("ironSword"), getItem("woodenStaff"),
    			getItem("leatherArmor"), getItem("ironArmor"), getItem("wizardRobes"));
    	firstFloorScreen = new floorScreen(this, firstFloor, secondFloorScreen, firstScreen, Flags.FLAG_FIRSTBOSS_DIALOGUE);
    	
    	Save.loadConfig();
    	if(config == null) {
    		config = new Config(Config.RES_1280720);
    		Save.saveConfig(config);
    	}
    	config.switchRes(this);
    	config.switchKeys();
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
    	super.render();
    	if(InputMan.checkKey("F", null)) {
    		toggleFullscreen();
    	}
    }

    @Override
    public void dispose() {
		batch.dispose();
		mainFont.dispose();
		invFont.dispose();
	}

    public Item getItem(String IDname) {
    	for(Item item : items) {
    		if (item.getIDName() == IDname) {
    			return item;
    		}
    	}
    	return null;
    }


	private void initializeGame() {
		//initialize armas
				Weapon dullSword = new Weapon("Espada Desafilada", "dullSword", "Espada antigua y desafilada.", 0, 0, 0, 15, "Warrior");
				Weapon dullDaggers = new Weapon("Dagas Desgastadas", "dullDaggers", "Un par de dagas desgastadas.", 0, 0, 0, 15, "Thief");
				Weapon brokenStaff = new Weapon("Baculo Roto", "brokenStaff", "Un baculo que apenas funciona.", 0, 0, 0, 15, "Mage", "Sage");
				Weapon oldBow = new Weapon("Arco Viejo", "oldBow", "Un arco que ya tiene sus años.", 0, 0, 0, 15, "Explorer");

				Weapon ironSword = new Weapon("Espada de Hierro", "ironSword", "Una espada comun de hierro. no destaca en nada.", 5, 0, 0, 30, "Warrior");
				Weapon woodenStaff = new Weapon("Baston de Madera", "woodenStaff", "Un baston de roble de segunda mano.", 0, 5, 0, 30, "Mage", "Sage");

				//initialize armaduras
				Armor rags = new Armor("Harapos", "rags", "Ropas viejas.", 0, 0, 0, 0, 0, 5);
				Armor leatherArmor = new Armor("Armadura de Cuero", "leatherArmor", "Armadura de cuero. Da algo de resistencia.", 0, 0, 5, 0, 0, 30);
				Armor ironArmor = new Armor("Armadura de Hierro", "ironArmor", "Armadura de hierro. Resistente, pero lenta.", 0, 0, 10, 0, -5, 50);
				Armor wizRobes = new Armor("Bata de Mago", "wizRobes", "Una bata de mago. Da resistencia magica.", 0, 0, 0, 5, 0, 40);

				player.addArmors(ironArmor);
				player.addArmors(wizRobes);

				player.addWeapons(woodenStaff);
				player.addWeapons(ironSword);

				//initialize items
				Item minHPot = new Item("Pocion menor de salud", "minHPot", "Cura poca cantidad de salud.", 20, 5, 3, false, true);
				Item minMPot = new Item("Pocion menor de mana", "minMPot", "Cura una poca cantidad de mana.", 20, 5, 3, false, true);
				Item minSPot = new Item("Pocion menor de stamina", "minSPot", "Cura una poca cantidad de stamina.", 5, 20, 3, false, true);
				Item minHPven = new Item("Veneno menor de salud", "minHPven", "Quita una poca cantidad de salud.", 30, 3, 1, false, false);
				Item minSPven = new Item("Veneno menor de stamina", "minSPven", "Quita una poca cantidad de stamina.", 2, 20, 1, false, false);
				Item minMPven = new Item("Veneno menor de mana", "minMPven", "Quita una poca cantidad de mana.", 20, 2, 1, false, false);

				initItems(dullSword, dullDaggers, brokenStaff, oldBow, ironSword, woodenStaff,

						rags, leatherArmor, ironArmor, wizRobes,

						minHPot, minMPot, minSPot, minHPven, minSPven, minMPven);

				Store.addItems(minHPot, minMPot, minHPven, ironSword, woodenStaff, leatherArmor, ironArmor, wizRobes);

				//initialize habilidades
				Skill defAtt = new Skill("defAtt", "Ataque comun", "Un ataque fisico basico.", null, " ataca a ", 1, 0, false, 1);
				Skill defMat = new Skill("defMat", "Ataque magico comun", "Un ataque magico basico.", null,  " ataca a ", 1, 0, false, 1);
				Skill defend = new Skill("defend", "Defender", "Se protege de los proximos ataques.", "NONE", " se defiende.", 0, 999, false, 1);

				//guerrero
				Skill charAtk = new Skill("charAtk", "Ataque cargado", "Inflige daño medio a un enemigo.", "PHY",
						" lanza un ataque cargado a ",
						1, 0, false, 1, 0, 30);
				Skill deepCut = new Skill("deepCut", "Corte profundo", "Inflige daño bajo con chances de aplicar sangrado.", "PHY",
						" corta profundamente a ",
						1, 0, false, 1, 0, 30);
				Skill knockout = new Skill("knockout", "Golpe de Gracia", "Inflinge daño bajo con bajas chances de aplicar confusion.", "PHY",
						" le da un golpe de gracia a ",
						1, 0, false, 1, 0, 40, 2);
				Skill crossCut = new Skill("crossCut", "Corte cruzado", "Inflinge daño bajo a todos los enemigos.", "PHY",
						" corta a traves de los enemigos.",
						2, 5, false, 1, 0, 50, 2);
				Skill skullCracker = new Skill("skullCracker", "Rompecraneos", "Inflinge daño medio a un enemigo con chances de confusion.", "PHY",
						" le destruye el craneo a  ",
						1, 0, false, 1, 0, 45, 3);
				Skill brutalBlow = new Skill("brutalBlow", "Golpe brutal", "Inflinge daño elevado a un enemigo.", "PHY",
						" destruye a ",
						1, -10, false, 1, 0, 80, 4);
				Skill moralDest = new Skill("moralDest", "Desestabilizador de Moral", "Inflinge daño medio a todos los enemigos con chances de atacar de vuelta.", "PHY",
						" desestabiliza a sus enemigos.",
						2, 0, false, 1, 0, 80, 4);
				Skill lunge = new Skill("lunge", "Embestida", "Inflinge daño medio a un enemigo y lo confunde.", "PHY",
						" embiste contra ",
						1, 5, false, 1, 0, 100, 5);
				Skill heavyTackle = new Skill("heavyTackle", "Barrida contundente", "Inflinge daño medio a todos los enemigos con altas chances de aplicar cansancio.", "PHY",
						" Barre a los enemigos.",
						2, -10, false, 1, 60, 120, 6);
				Skill hustle = new Skill("hustle", "Chicaneo", "Inflinge daño elevado a un enemigo con chances de aplicar Ira.", "PHY",
						" hace un movimiento rastrero contra ",
						1, 5, false, 1, 50, 120, 7);
				Skill backhand = new Skill("backhand", "Golpe del reves", "Inflinge daño medio a un enemigo y lo duerme.", "PHY",
						" Le pega con el pomo a ",
						1, 0, false, 1, 80, 120, 8);
				Skill heavyLand = new Skill("heavyLand", "Impacto pesado", "Inflinge daño elevado a todos los enemigos con chance de confundirlos.", "PHY",
						" destruye a sus enemigos.",
						2, -15, false, 1, 40, 140, 8);
				Skill warCry = new Skill("warCry", "Grito de batalla", "Aplica confusion, ira o silencio a todos los enemigos.", "NONE",
						" Grita a todo pulmon. ",
						2, 0, false, 1, 80, 140, 9);
				Skill crushAtk = new Skill("crushAtk", "Ataque Aplastante", "Inflinge daño elevado a todos los enemigos y aplica confusion y cansancio. ignora "
						+ "debilidades y fortalezas.", "UNI",
						" Acaba con los enemigos.",
						2, 0, false, 1, 150, 250, 10);

				//mago
				Skill fireBall = new Skill("fireBall", "Bola de Fuego", "Ataque magico de fuego a un enemigo. debil.", "FIR",
						" le lanza una bola de fuego a ",
						1, 0, false, 1, 25, 0);
				Skill windBurst = new Skill("windBurst", "Rafaga de Viento", "Ataque magico de viento a un enemigo. debil.", "WIN",
						" lanza una rafaga de viento a ",
						1, 0, false, 1, 25, 0);
				Skill terrAttack = new Skill("terrAttack", "Terra-taque", "Ataque magico de tierra a un enemigo. debil.", "EAR",
						" lanza fragmentos de tierra a ",
						1, 0, false, 1, 25, 0);
				Skill splatter = new Skill("splatter", "Salpicadura", "Ataque magico de agua a un enemigo. debil.", "WAT",
						" saplica con agua a ",
						1, 0, false, 1, 25, 0);
				Skill fireplace = new Skill("fireplace", "Fogata", "Ataque magico de fuego a todos los enemigos. debil.", "FIR",
						" hace una fogata con los enemigos.",
						2, 0, false, 1, 50, 0, 2);
				Skill blizzard = new Skill("blizzard", "Ventisca", "Ataque magico de viento a todos los enemigos. debil.", "WIN",
						" sopla a los enemigos.",
						2, 0, false, 1, 50, 0, 2);
				Skill poisoning = new Skill("poisoning", "Envenenamiento", "Envenena a un objetivo.", "NONE",
						" envena a ",
						1, 0, false, 1, 45, 0, 3);
				Skill pressure = new Skill("pressure", "Presion", "Ataque magico de tierra a todos los enemigos. debil.", "EAR",
						" aumenta la presion en los enemigos.",
						2, 0, false, 1, 50, 0, 3);
				Skill incantation = new Skill("incantation", "Encantacion", "Aplica Encantado a un enemigo.", "NONE",
						" encanta a ",
						1, 0, false, 1, 40, 0, 4);
				Skill tides = new Skill("tides", "Marea", "Ataque magico de agua a todos los enemigos. debil.", "WAT",
						" empapa a los enemigos.",
						2, 0, false, 1, 50, 0, 4);
				Skill tiresome = new Skill("tiresome", "Des-canso", "Aplica Cansancio a todos los enemigos.", "NONE",
						" hechiza a sus enemigos.",
						2, 0, false, 1, 70, 0, 5);
				Skill fireHur = new Skill("fireHur", "Huracan de Fuego", "Ataque magico de fuego a todos los enemigos "
						+ "con chances de aplicar Ira. fuerte.", "FIR",
						" calcina a sus objetivos.",
						2, 0, false, 1, 130, 0, 6);
				Skill collapse = new Skill("collapse", "Derrumbe", "Ataque magico de tierra a todos los enemigos "
						+ "con chances de aplicar silencio. fuerte.", "EAR",
						" entierra a los enemigos.",
						2, 0, false, 1, 130, 0, 6);
				Skill decibels = new Skill("decibels", "De-cibelios", "Aplica Silencio a un enemigo.", "NONE",
						" hechizo las palabras de ",
						2, 0, false, 1, 75, 0, 7);
				Skill seaquake = new Skill("seaquake", "Maremoto", "Ataque magico de agua a todos los enemigos "
						+ "con chances de aplicar Sueño. fuerte.", "WAT",
						" inunda a los enemigos.",
						2, 0, false, 1, 140, 0, 8);
				Skill tornado = new Skill("tornado", "Tornado", "Ataque magico de viento a todos los enemigos "
						+ "con chances de aplicar Confusion. fuerte.", "WIN",
						" lanza un tornado a los enemigos.",
						2, 0, false, 1, 140, 0, 8);
				Skill curse = new Skill("curse", "Maldicion", "Aplica los estados Envenenado, Encantado y "
						+ "Cansado a todos los enemigos.", "NONE",
						" maldice a sus oponentes.",
						2, 0, false, 1, 180, 0, 9);
				Skill blessing = new Skill("blessing", "Bendicion", "Aplica el estado Bendicion a todos sus aliados.", "NONE",
						" bendice a todos sus amigos.",
						4, 0, false, 1, 190, 0, 9);
				Skill lastPrism = new Skill("lastPrism", "Ultimo Prisma", "Ataque de daño universal a todos los enemigos. Muy fuerte.", "UNI",
						" combina el espectro de colores en un ultimo ataque devastador.",
						2, 0, false, 1, 200, 150, 10);

				//ladron
				Skill decCut = new Skill("decCut", "Corte Embustero", "Inflinge daño medio a 1 enemigo con chances de aplicar sangrado.", "PHY",
						" corta a ",
						1, 0, false, 1, 0, 30);
				Skill fastAtk = new Skill("fastAtk", "Ataque veloz", "Inflinge daño bajo a 1 enemigo y ataca 2 veces.", "PHY",
						" corta velozmente a ",
						1, 10, false, 2, 0, 35);
				Skill smokeBomb = new Skill("smokeBomb", "Bomba de Humo", "Aplica silencio y chances de confusion a 1 enemigo.", "NONE",
						" le lanza una bomba de humo a ",
						1, 0, false, 1, 40, 40, 2);
				Skill sneakAtk = new Skill("sneakAtk", "Ataque furtivo", "Inflinge daño medio a 1 enemigo e ignora un 40% de su armadura.", "PHY",
						" sorprende sigilosamente a ",
						1, 0, false, 1, 0, 70, 2);
				Skill venomEdge = new Skill("venomEdge", "Filo venenoso", "Inflinge daño bajo a 1 enemigo y aplica veneno.", "PHY",
						" ataca con su daga envenenada a ",
						1, 0, false, 1, 35, 40, 3);
				Skill bladeRain = new Skill("bladeRain", "LLuvia de Cuchillas", "Inflinge daño medio a todos los enemigos "
						+ "con chances de aplicar sangrado.", "RAN",
						" hace que lluevan cuchillas.",
						2, 0, false, 1, 15, 70, 4);
				Skill magicTheft = new Skill("magicTheft", "Hurto Magico", "Roba un 10% de la vitalidad y la magia del enemigo.", "NONE",
						" le roba la escencia vital y magica a ",
						1, 0, false, 1, 0, 90, 4);
				Skill vitalTheft = new Skill("vitalTheft", "Hurto Vital", "Roba un 10% de la vitalidad y la stamina del enemigo.", "NONE",
						" le roba la escencia vital y estamina a ",
						1, 0, false, 1, 90, 0, 5);
				Skill bladeSweep = new Skill("bladeSweep", "Barrido de Cuchillas", "Inflinge daño medio a todos los enemigos y ataca 3 veces con"
						+ "chances muy bajas de aplicar sangrado.", "PHY",
						" barre con sus cuchillas a los enemigos!",
						2, 15, false, 3, 50, 110, 6);
				Skill sleepPll = new Skill("sleepPll", "Somnifero", "Duerme a un enemigo.", "PHY",
						" duerme a ",
						1, 0, false, 1, 80, 50, 6);
				Skill finisher = new Skill("finisher", "Remate", "Ataque de daño elevado que hace un 50% mas de daño si el objetivo sangra.", "PHY",
						" reabre las heridas de ",
						1, 0, false, 1, 60, 120, 7);
				Skill bladeTornado = new Skill("bladeTornado", "Tornado de cuchillas", "Inflinge daño medio a todos los enemigos, "
						+ "ataca 2 veces y altas probabilidades de aplicar sangrado.", "PHY",
						" desata una furia de cuchillas",
						2, 0, false, 2, 100, 150, 8);
				Skill hitman = new Skill("hitman", "Hitman", "Ataque de daño elevado que ignora toda la defensa.", "PHY",
						" ataca por detras a la nuca de ",
						1, 0, false, 1, 100, 200, 8);
				Skill chaos = new Skill("chaos", "Caos, Caos!", "Aplica cansancio y sangrado a todos los enemigos. ademas, les roba un "
						+ "5% de la vitalidad a cada uno.", "PHY",
						" desata el caos en el campo de batalla. Puede hacer lo que sea!",
						2, 0, false, 1, 100, 180, 9);
				Skill throatSlice = new Skill("throatSlice", "Corta-gargantas", "Ataque de daño elevado universal "
						+ "que silencia a todos los enemigos.", "UNI",
						" se prepara para cortar gargantas.",
						2, 50, false, 1, 200, 230, 10);

				//explorador
				Skill deadShot = new Skill("deadShot", "Disparo certero", "Inflinge daño medio a un enemigo. ", "RAN",
						" le lanza una flecha poderosa a ",
						1, 0, false, 1, 0, 30);
				Skill fireArrow = new Skill("fireArrow", "Flecha Ignifuga", "Inflinge daño medio de fuego a un enemigo. ", "FIR",
						" le lanza una flecha en fuego a ",
						1, 0, false, 1, 10, 25);
				Skill iceArrow = new Skill("iceArrow", "Flecha Escarchada", "Inflinge daño medio de agua a un enemigo. ", "WAT",
						" le lanza una flecha congelada a ",
						1, 0, false, 1, 10, 25);
				Skill arrowRain = new Skill("arrowRain", "LLuvia de flechas", "Inflinge daño bajo a todos los enemigos. ", "RAN",
						" nubla el cielo de flechas.",
						2, 0, false, 1, 0, 45, 2);
				Skill calTrap = new Skill("calTrap", "Trampa de Abrojos", "Envenena a un enemigo. ", "NONE",
						" posiciona una trampa cerca de ",
						1, 0, false, 1, 30, 30, 2);
				Skill decoy = new Skill("decoy", "Señuelo", "Aplica el estado confusion a un enemigo. ", "NONE",
						" enga a con un se uelo a ",
						1, 0, false, 1, 40, 40, 3);
				Skill slimeDust = new Skill("slimeDust", "Polvo de Slime", "Aplica el estado Envenenado a todos los enemigos. ", "NONE",
						" lanza un polvo de slime a sus enemigos!",
						2, 0, false, 1, 80, 80, 4);
				Skill nailIt = new Skill("nailIt", "Tiro al Clavo", "Inflinge daño elevado a un enemigo. ", "RAN",
						" lanza una poderosa flecha cargada a ",
						1, 0, false, 1, 10, 100, 4);
				Skill expTorment = new Skill("expTorment", "Tormento del Explorador", "Aplica el estado confusion y cansancio a todos los enemigos. ", "NONE",
						" atormenta a sus enemigos...",
						2, 0, false, 1, 80, 90, 5);
				Skill tarPit = new Skill("tarPit", "Trampa de Alquitran", "Aplica el estado envenenado y cansancio a todos los enemigos. ", "NONE",
						" prepara una fuerte pocima para sus enemigos!",
						2, 0, false, 1, 110, 110, 6);
				Skill debrisShower = new Skill("debrisShower", "LLuvia de Escombros", "Inflinge daño de tierra elevado a los enemigos. ", "EAR",
						" hace que caigan los escombros.",
						2, 0, false, 1, 100, 150, 6);
				Skill worldRevolving = new Skill("worldRevolving", "Girando el mundo", "Aplica Ira a todos los enemigos. ", "NONE",
						" hace que caigan los escombros.",
						2, 0, false, 1, 125, 150, 7);
				Skill sleepGas = new Skill("sleepGas", "Gas Somnifero", "Aplica el estado Sue o a todos los enemigos. ", "NONE",
						" duerme a todo el mundo. Buenas noches!",
						2, 0, false, 1, 180, 200, 8);
				Skill allyTotem = new Skill("allyTotem", "Totem Aliado", "Bendice a un aliado. ", "NONE",
						" le presta un totem a ",
						3, 0, false, 1, 100, 180, 9);
				Skill finalTrial = new Skill("finalTrial", "Flecha del Juicio Final", "Inflinge daño elevado universal a todos los enemigos y los hace sangrar. ", "UNI",
						" dispara una rafaga de flechas en todas las direcciones.",
						2, 0, false, 1, 180, 220, 10);

				//Sage
				Skill healing = new Skill("healing", "Curacion", "Cura ligeramente a un miembro de la party. ", "NONE",
						" cura a ",
						3, 0, true, 1, 35, 0);
				Skill mulHeal = new Skill("mulHeal", "Curacion Multiple", "Cura muy ligeramente a todos los aliados. ", "NONE",
						" cura a todos los miembros de la party!",
						4, 0, true, 1, 50, 0);
				Skill staThief = new Skill("staThief", "Robo vital", "Roba Stamina para darsela a un aliado. ", "NONE",
						" roba la fuerza vital de ",
						5, 0, false, 1, 55, 0, 2);
				Skill incant = new Skill("incant", "Encantacion", "Aplica el estado Encantado a todos los enemigos. ", "NONE",
						" hechiza a los enemigos!",
						2, 0, false, 1, 90, 0, 2);
				Skill silence = new Skill("silence", "Orden en la corte!", "Aplica el estado Silencio a todos los enemigos. ", "NONE",
						" calla a la multitud.",
						2, 0, false, 1, 105, 0, 3);
				Skill deaftones = new Skill("deaftones", "Sordera", "Aplica el estado Confusion a un enemigo "
						+ "con altas chances de aplicar envenenamiento", "NONE",
						" deja sordo a ",
						1, 0, false, 1, 70, 0, 3);
				Skill toxicDust = new Skill("toxicDust", "Polvo toxico", "Inflinge daño de viento medio a todos los enemigos"
						+ " con altas chances de envenenamiento.", "WIN",
						" sopla un viento toxico a los enemigos.",
						2, 0, false, 1, 120, 0, 4);
				Skill bardSong = new Skill("bardSong", "Cancion de Bardo", "Cura bastante a un aliado. ", "NONE",
						" cura bastante a ",
						3, 0, true, 1, 100, 0, 4);
				Skill shadowSpell = new Skill("shadowSpell", "Conjuro de las Sombras", "Aplica Encantado y Cansado a todos los enemigos. ", "NONE",
						" maldice desde las sombras a los enemigos.",
						2, 0, false, 1, 140, 0, 5);
				Skill godOffering = new Skill("godOffering", "Ofrenda a los Dioses", "Bendice a todos los aliados. ", "NONE",
						" le pide ayuda a los Divinos.",
						4, 0, false, 1, 160, 0, 6);
				Skill healingRitual = new Skill("healingRitual", "Ritual Curativo", "Cura enormemente a todos los aliados. ", "NONE",
						" lleva a cabo un ritual curativo.",
						4, 0, true, 1, 160, 0, 6);
				Skill purification = new Skill("purification", "Purificacion", "Elimina todos los efectos negativos de un aliado. ", "NONE",
						" quita los males que atormentan a ",
						3, 0, false, 1, 160, 0, 7);
				Skill vitalLust = new Skill("vitalLust", "Lujuria Vital", "absorbe un 15% de la vida de un enemigo y le aplica ira. ", "NONE",
						" le absorbe la vida a ",
						1, 0, false, 1, 180, 0, 8);
				Skill revive = new Skill("revive", "Revivir", "Revive a un aliado con el 50% de su vida. ", "NONE",
						" le da una mano a ",
						3, 0, false, 1, 220, 0, 8);
				Skill divineEx = new Skill("divineEx", "Exorcismo Divino", "Purifica todos los efectos negativos de sus aliados. ", "NONE",
						" exorcisa al equipo.",
						4, 0, false, 1, 200, 0, 9);
				Skill sacrifice = new Skill("sacrifice", "El Sacrificio", "Cura toda la vida de sus aliados y revive a los muertos, quita sus efectos negativos "
						+ "y los bendice ademas de aplicar envenenado, cansancio y encanto a todos los enemigos, pero el curandero muere. ", "NONE",
						" se sacrifica.",
						6, 0, false, 1, 260, 0, 10);

				//initialize personajes
				Hero warrior = new Hero("Robert", "warrior", "Warrior", "PHY", 150, 20, 120, 30, 25, 10, 15, 15, dullSword, rags, defAtt, defend,
						knockout, crossCut, skullCracker, brutalBlow, moralDest, lunge, heavyTackle, hustle, backhand, heavyLand, warCry, crushAtk);
				Hero mage = new Hero("Noelle", "mage", "Mage", "FIR", 100, 100, 30, 10, 15, 15, 25, 20, brokenStaff, rags, defMat, defend,
						fireplace, blizzard, poisoning, pressure, incantation, tides, tiresome, fireHur, collapse, decibels, seaquake, tornado, curse,
						blessing, lastPrism);
				Hero thief = new Hero("Myriam", "thief", "Thief", "PHY", 100, 60, 90, 20, 20, 15, 15, 30, dullDaggers, rags, defAtt, defend,
						smokeBomb, sneakAtk, venomEdge, bladeRain, magicTheft, vitalTheft, bladeSweep, sleepPll, finisher, bladeTornado, hitman, chaos,
						throatSlice);
				Hero explorer = new Hero("Reed", "explorer", "Explorer", "RAN", 125, 40, 100, 20, 20, 15, 15, 25, oldBow, rags, defAtt, defend,
						arrowRain, calTrap, decoy, slimeDust, nailIt, expTorment, tarPit, debrisShower, worldRevolving, sleepGas, allyTotem, finalTrial);
				Hero sage = new Hero("Dough", "sage", "Sage", "WIN", 120, 100, 30, 10, 10, 15, 15, 35, brokenStaff, rags, defMat, defend,
						staThief, incant, silence, deaftones, toxicDust, bardSong, shadowSpell, godOffering, healingRitual, purification, vitalLust, revive,
						divineEx, sacrifice);

				characters[0] = warrior;
				characters[1] = mage;
				characters[2] = thief;
				characters[3] = explorer;
				characters[4] = sage;

				warrior.addSkills(charAtk, deepCut);
				mage.addSkills(fireBall, windBurst, terrAttack, splatter);
				thief.addSkills(decCut, fastAtk);
				explorer.addSkills(deadShot, fireArrow, iceArrow);
				sage.addSkills(healing, mulHeal);

				//initialize enemigos
				// Piso 1: criaturas de entrada. Para el equipo son la primera señal de que la mazmorra
				// no es un lugar abandonado: alguien o algo sigue manteniendo una fuerza organizada aqui.
				Enemy slime = new Enemy("Slime", "slime", "WAT",
						90, 0, 40,
						12, 10, 0, 10, 8,
						18, 8, 50,
						1.0, 1.0, 2.0, 0.75, 0.5, 1.0,
						defAtt, defend);
				slime.setDesc("Una masa de una sustancia... similar al moco (no voy a anotar por que).\n"
		        + "No se que esperaba encontrar al entrar a una mazmorra, pero supongo que esto estaba en la lista.\n"
		        + "No parece demasiado peligrosa. Uno de los miembros intento tocarla. Le dije que no lo hiciera.\n"
		        + "Lo hizo igual. Ahora sabemos que tambien es pegajosa.\n"
		        + "Y que sabe a moco. No voy a anotar como averiguamos eso.");
				slime.addSkill(charAtk);

				Enemy goblin = new Enemy("Goblin", "goblin", "PHY",
						120, 40, 80, 20, 15, 8, 12, 20,
						24, 12, 30,
						1.5, 0.75, 1.0, 1.5, 0.5, 1.0,
						defAtt, defend);
				goblin.setDesc("Esta graciosa criatura proviene de los bosques del Pucat.\n"
		        + "Su especie no suele tener contacto con otras especies, asi que me sorprende bastante que este trabajando para esta organizacion.\n"
		        + "Creo que es mas organizado que nosotros.");
				goblin.addSkills(decCut, fastAtk);

				Enemy skeleton = new Enemy("Esqueleto", "skeleton", "RAN",
						105, 50, 60, 22, 15, 17, 16, 18,
						26, 15, 15,
						1.0, 2.0, 0.75, 0.75, 1.5, 1.0,
						defAtt, defend);
				skeleton.setDesc("No sabiamos que la organizacion a la que vinimos a eliminar practicaba la necromancia.\n"
		        + "Estos parecen ser cuerpos de personas que perecieron en esta mazmorra hace ya cientos de años.\n"
		        + "Me pregunto si sus almas vuelven del inframundo en contra de su voluntad para luchar contra nosotros.\n"
		        + "Si es asi, espero que sepan que nosotros tampoco queriamos estar aqui.");
				skeleton.addSkills(arrowRain, fireArrow);

				Enemy mimic = new Enemy("Mimico", "mimic", "PHY",
						180, 0, 0, 28, 25, 0, 10, 12,
						50, 90, 5,
						0.5, 0.5, 2.0, 0.5, 0.25, 1.0,
						defAtt, defend);
				mimic.setDesc("Odio a estos tipos.\n"
		        + "No hay mucho mas que decir. Es una caja. Tiene dientes. Me comio la mano.\n"
		        + "Actualizacion: la mano sigue ahi. Supongo que no fue tan grave.");

				Boss ogre = new Boss("Ogro", "ogre", "PHY",
						950, 40, 220, 34, 24, 8, 18, 12,
						180, 120, 0,
						0.5, 1.0, 1.0, 0.25, 0.5, 1.5,
						defAtt, defend, Flags.FLAG_FIRSTBOSS_DEFEATED);
				ogre.setDesc("Este ogro parece haber sido mutado para tener mas fuerza que uno comun.\n"
		        + "No parecia poseer ningun tipo de aptitud magica importante, ni siquiera un nivel cognitivo capaz de entender palabras complejas.\n"
		        + "Es una lastima. Nos podria haber dicho algo sobre esta organizacion.\n"
		        + "En cambio nos dijo algo mucho mas simple: que nos iba a matar.\n"
		        + "Un hombre de pocas palabras. Lo respeto.");
				ogre.addSkills(charAtk, deepCut, crossCut);
				ogre.setEvents(
		                new BossEvent(false, 0,
		                        "Asi que ustedes son el equipo que mandaron al piso uno...", "Perfecto.",
		                        "Veamos cuanto duran los mercenarios."),
		                new BossEvent(true, 50, crossCut,
		                        "De verdad creen que pueden derrotarme..?", "Les demostrare que se equivocan."),
		                new BossEvent(true, 25, charAtk,
		                        "Uno de ustedes ya esta cayendo.", "...o soy yo?"),
		                new BossEvent(true, 0,
		                        "Vaya...",
		                        "C-con que... me han derrotado...",
		                        "Heh... esta bien...",
		                        "S-Suerte...",
		                        "La...",
		                        "Necesitaran...")
		        );

				// Piso 2: la necromancia deja de ser una sospecha y pasa a ser una estructura.
				Enemy armoredSkel = new Enemy("Esqueleto Acorazado", "armoredSkel", "RAN",
						260, 0, 0,
						38, 40, 10, 25, 16,
						45, 22, 50,
						0.5, 1.0, 0.75, 1.0, 1.0, 1.0,
						defAtt, defend);
				armoredSkel.setDesc("Otro esqueleto, pero esta vez con armadura.\n"
		        + "Viendo la destreza y proeza de este tipo de enemigo en comparacion a los del piso anterior, parece que estos fueron cadaveres de valientes que malaventuraron hasta esta zona.\n"
		        + "Estaban mejor armados y preparados para un desafio que los que habitaban el piso anterior.\n"
		        + "Seguramente encontremos no-muertos mas fuertes conforme descendamos.\n"
		        + "No me gusta como suena.");
				
				Enemy undeadKnight = new Enemy("Caballero no-muerto", "undeadKnight", "PHY",
						240, 0, 0,
						40, 32, 20, 24, 18,
						50, 25, 40,
						0.75, 0.5, 0.75, 1.5, 1.5, 0.25,
						defAtt, defend);
				undeadKnight.setDesc("Aunque cuesta separar la armadura sin arrancarle la carne muerta y podrida del rostro, estos parecen ser otros cadaveres, solo que mas frescos.\n"
		        + "Poseen restos de piel, carne y un olor putrido del cual desearia tener palabras para describir.\n"
		        + "Lastimosamente, el diccionario no tiene nada ni remotamente cercano a lo que cabria para explicarlo.\n"
		        + "Voy a dejar de respirar por unos minutos. Si alguien pregunta, es una tecnica de meditacion.");
				
				Enemy corrWizard = new Enemy("Hechicero Corrompido", "corrWizard", "FIR",
						180, 100, 80,
						10, 18, 42, 36, 22,
						55, 50, 5,
						1.0, 1.0, 0.5, 0.25, 1.5, 2.0,
						defAtt, defend);
				corrWizard.setDesc("Las primeras personas vivas que encontramos.\n"
		        + "Es una lastima que esten bajo el efecto de algun tipo de hechizo de control mental, y no quede de otra que darles un descanso eterno enviandolos directos al Pandemonio.\n"
		        + "Nadie del grupo parece reconocer el logo de la casa a la que pertenecen.\n"
		        + "Asi que no podemos hacer mas que asumir que provienen de un grupo irreconocido en los libros oficiales de hechiceria.\n"
		        + "Si alguien sabe como romper el hechizo, por favor digamelo antes de que tenga que volver a escribir esto.");
				corrWizard.addSkills(pressure, fireplace);

				Enemy ghostKnight = new Enemy("Caballero Fantasma", "ghostKnight", "WIN",
						220, 60, 80,
						34, 28, 30, 32, 28,
						50, 55, 5,
						1.0, 0.75, 0.5, 1.0, 1.5, 1.0,
						defAtt, defend);
				ghostKnight.setDesc("Esto si que es una sorpresa...\n"
		        + "Se consideran no-muertos? muerto-vivos? vivo-muertos?\n"
		        + "Siempre quise probar el sabor del ectoplasma, y por suerte este mismo se desplomo al suelo el momento en el que murio, "
		        + "sin necesidad de separarlo de su armadura.\n"
		        + "RECOMENDACION: NO comer ectoplasma.\n"
		        + "REPITO: NO comer ectoplasma. Tendria que haber escuchado al equipo de profesionales...");
				ghostKnight.addSkills(venomEdge, blizzard);

				Boss headless = new Boss("Jinete sin Cabeza", "headless", "PHY",
						1800, 80, 300,
						50, 42, 30, 36, 28,
						350, 250, 0,
						0.5, 1.0, 0.75, 0.75, 1.5, 1.5,
						defAtt, defend, Flags.FLAG_SECONDBOSS_DEFEATED);
				headless.setDesc("Un individuo interesante, desde luego.\n"
		        + "Su forma de hablar es bastante refinada para alguien que acaba de intentar matarnos. Me recuerda que deberia leer mas.\n"
		        + "Tambien parece tener un conocimiento bastante amplio de la mazmorra y de lo que habita mas abajo.\n"
		        + "No se si admirarlo, temerle o apuñalarlo por la espalda mientras no mira. Aunque parece que, no importa donde estes, siempre "
		        + "te esta observando.\n"
		        + "Actualizacion: el caballo tambien parece no tener cabeza.\n"
		        + "Definitivamente deberia leer mas libros.");
				headless.addSkills(lunge, bladeRain, finisher);
				headless.setEvents(
		                new BossEvent(false, 0,
		                        "Camaradas... han llegado bastante lejos.",
		                        "No suelo recibir visitantes con tanta determinacion.",
		                        "Es una pena que nuestro encuentro vaya a ser tan efimero."),
		                new BossEvent(false, 1,
		                        "Hay cosas mucho peores mas abajo.",
		                        "Todavia estan a tiempo de volver."),
		                new BossEvent(false, 2,
		                        "Entonces siguen avanzando.",
		                        "Bien. Al menos sabre que su equipo no vino a improvisar."),
		                new BossEvent(true, 75, finisher,
		                        "Me han empezado a herir.", "Se ve que tengo que dejar de juguetear.", "Voy a empezar a partir cabezas.",
		                        "Nada personal, camaradas."),
		                new BossEvent(true, 50, bladeRain,
		                        "Mantienen la linea.", "Y...", "Aguantan mas de lo que creia...",
		                        "Parece que me tendre que esforzar mas..."),
		                new BossEvent(true, 25, bladeSweep,
		                        "No necesito matarlos rapido...",
		                        "Solo necesito... lograr este ataque...",
		                        "Si no... se acabo..."),
		                new BossEvent(true, 0,
		                        "Pues...",
		                        "Supongo...",
		                        "Que los subestime...",
		                        "Bueno...",
		                        "Fue divertido, no lo voy a negar.",
		                        "Realmente queria ese ascenso.",
		                        "Pero parece que este es mi final...",
		                        "Escuchen...",
		                        "Su proximo rival...",
		                        "El Multitauro.",
		                        "No podran dañarlo...",
		                        "Pero tiene una... pequeña... debilidad.",
		                        "El viento... es su aliado...",
		                        "...",
		                        "(El de ustedes no el de el...)",
		                        "...Adelante, Rocinante..!")
		        );

				// Piso 3: la dungeon ya parece un experimento de combate.
				Enemy ghostBow = new Enemy("Arquero Fantasma", "ghostBow", "RAN",
						340, 60, 120,
						52, 35, 30, 34, 42,
						80, 40, 30,
						0.75, 0.75, 1.0, 1.0, 1.0, 1.0,
						defAtt, defend);
				ghostBow.setDesc("Primeros fantasmas conscientes que nos encontramos.\n"
		        + "Son habiles con el arco, pero no hay mucho mas para decir.\n"
		        + "Disparan como si llevaran toda la vida haciendolo. Lo cual tiene sentido, considerando que probablemente llevan toda la muerte haciendolo.\n"
		        + "El chiste no fue gracioso. Voy a quitarlo despues.\n"
		        + "Alguien del equipo se rio mientras lo anotaba en voz alta. Lo voy a dejar.");
				ghostBow.addSkills(deadShot, arrowRain);

				Enemy ebonyWar = new Enemy("Guerrero del Ebano", "ebonyWar", "PHY",
						400, 40, 180,
						58, 48, 15, 25, 28,
						90, 45, 30,
						0.75, 0.75, 1.0, 1.0, 1.0, 1.0,
						defAtt, defend);
				ebonyWar.setDesc("Este caballero de oscura armadura seria un imponente rival... si no estuviesemos preparados para cosas peores.\n"
		        + "Eso si, nada pudimos hacer para quitarle la armadura. Parece que esta pegada a el, o que el propio concepto de su persona es la armadura.\n"
		        + "Quizas no hay nada debajo. Quizas contenga los secretos del universo.\n"
		        + "O el oro suficiente para retirarme a una choza en las playas de Gosfelt.\n"
		        + "De todos modos, no me pagan para averiguarlo.");
				ebonyWar.addSkills(bladeRain, brutalBlow);

				Enemy centaur = new Enemy("Centauro", "centaur", "PHY",
						320, 60, 150,
						60, 36, 20, 28, 55,
						95, 40, 25,
						0.75, 0.75, 4.0, 1.5, 0.25, 0.25,
						defAtt, defend);
				centaur.setDesc("Un centauro... o Minotauro? No se diferenciarlos.\n"
		        + "Tiene el torso y la cabeza de un hombre.\n"
		        + "Actualizacion: el torso y la cabeza de una mujer..?\n"
		        + "Actualizacion: el torso y la cabeza de un humano. No voy a seguir investigandolo.\n"
		        + "Actualizacion: Es un centauro. El lider me lo dijo.\n"
		        + "Actualizacion: Ahora uno de los miembros dijo que es un minotauro y de vuelta entramos en duda.\n"
		        + "Actualizacion: tras el pelaje, tenia escrito con magia negra 'Centauro'. Parece que ellos tampoco querian equivocarse.");
				centaur.addSkills(arrowRain, nailIt);

				Enemy minotaur = new Enemy("Minotauro", "minotaur", "PHY",
						520, 80, 220,
						70, 52, 10, 25, 18,
						105, 50, 15,
						1.0, 0.5, 0.25, 0.25, 2.0, 0.25,
						defAtt, defend);
				minotaur.setDesc("Con que esto es un Minotauro. Ahora entiendo las diferencias.");
				minotaur.addSkills(backhand, skullCracker);

				Boss multitaur = new Boss("Multitauro", "multitaur", "PHY",
						3300, 120, 350,
						78, 60, 45, 50, 32,
						650, 500, 0,
						0.75, 0.5, 0.75, 0.25, 1.5, 0.5,
						defAtt, defend, Flags.FLAG_THIRDBOSS_DEFEATED);
				multitaur.setDesc("Un hibrido entre Minotauros y Centauros con una pizca de humanidad que creo a esta abominacion.\n"
		        + "Tiene cinco cabezas y, por alguna razon, cada una parece tener una personalidad completamente distinta.\n"
		        + "Cometauro habla como si toda la vida fuese una broma.\n "
		        + "Tragetauro parece estar en eterno sufrimiento... o melancolia. Es... dificil de describirlo. Es lo contrario al primero.\n"
		        + "Absutauro... no se como describirlo. Creo que ni el sabe que esta haciendo.\n"
		        + "Poetauro tiene... algo en la voz. las frases que dice se sienten como el sonido suave de un laud en una posada de "
		        + "la Capital una noche del festival, incluso aunque este diciendo como nos va a descuartizar.\n"
		        + "Y Directauro parece ser quien intenta mantener toda esa discordia a flote.\n"
		        + "No se quien decidio convertir a una bestia en una obra de teatro, pero admito que es dificil apartar la mirada.\n"
		        + "Tambien es dificil apartar la mirada cuando cinco cabezas intentan arrancartela.");
				multitaur.addSkills(shadowSpell, heavyTackle, moralDest);
				multitaur.setEvents(
		                new BossEvent(false, 0,
		                        "Directauro: Damas y caballeros, la funcion esta por comenzar.",
		                        "Cometauro: Espero que hayan traido risas desde sus casas.",
		                        "Tragetauro: Porque esta historia solo puede terminar en desgracia.",
		                        "Absutauro: O en evasion de impuestos.",
		                        "Poetauro: Dichosa, la habilidad,",
		                        "Poetauro: Que poseen los supuestos.",
		                        "Directauro: Acto I, Destino Fatale. Se abre el telon."),
		                new BossEvent(true, 75, shadowSpell,
		                        "Cometauro: Ja, ja, ja! Miren sus caras!",
		                        "Tragetauro: No rias... esto termina mal.",
		                        "Poetauro: Y para nos es normal.",
		                        "Cometauro: No existe lo normal en nos!",
		                        "Cometauro: Me corrijo. Existe lo A-normal",
		                        "Directauro: Acto II, Ludicra Comedia. Se reabre el telon."),
		                new BossEvent(true, 50, heavyTackle,
		                        "Absutauro: No le encuentro sentido.",
		                        "Absutauro: Por que luchamos, bestia y hombre,",
		                        "Absutauro: Cuando tenemos un enemigo en comun?",
		                        "Tragetauro: Le encajaron las neuronas a golpes?",
		                        "Poetauro: Escuchemos, con redobles,",
		                        "Poetauro: La respuesta de este noble.",
		                        "Absutauro: El pancreas.",
		                        "Directauro: Acto III, Gravite Absurde. Se revela la escena."),
		                new BossEvent(true, 25, moralDest,
		                        "Tragetauro: El cansancio los consume.",
		                        "Tragetauro: Aunque a nos igual.",
		                        "Cometauro: Que tragedia! Que absolutamente tragica tragedia!",
		                        "Tragetauro: Nuestra vida es una comedia.",
		                        "Absutauro: Que delicia! Que absoluta delicia!",
		                        "Poetauro: Y que absurda situacion!",
		                        "Poetauro: Pero no deben temer.",
		                        "Poetauro: Tan solo mi presentacion, ",
		                        "Poetauro: A estos los hara caer.",
		                        "Directauro: Mantengan el ritmo. No pierdan el acto.",
		                        "Poetauro: Ay, pero que poco tacto!",
		                        "Poetauro: Con mis rimas y mis versos,",
		                        "Poetauro: Caeran estos perversos.",
		                        "Poetauro: Los perdedores de facto.",
		                        "Directauro: Acto IV, Bellissima Poesia. Se muestran los actores."),
		                new BossEvent(true, 0,
		                        "Tragetauro: Pero que tragedia!",
		                        "Cometauro: Pero que comedia!",
		                        "Poetauro: Una verdadera acedia.",
		                        "Absutauro: La cena esta lista.",
		                        "Directauro: Fue una buena vida.",
		                        "Directauro: No lo suficientemente corta para pasar como un cuento,",
		                        "Directauro: Ni lo suficientemente larga para aburrir al lector casual.",
		                        "Directauro: Una historia dura cuanto deba durar.",
		                        "Directauro: Si se acorta... siempre le faltara algo.",
		                        "Directauro: Si se alarga... se pierde el ritmo. Y el rumbo.",
		                        "Directauro: Lo importante es que la vivi junto a mis actores.",
		                        "Absutauro: La vida esta llena de manzanas. Pero de peras tambien. Sobre todo nunca perder el rumbo.",
		                        "Poetauro: Ha sido una aventura.",
		                        "Poetauro: Pero no perdais la calma.",
		                        "Poetauro: Porque estoy casi segura,",
		                        "Poetauro: Que los llevare en el alma.",
		                        "Tragetauro: Actuar junto al bromista fue estresante.",
		                        "Cometauro: Lo ordinario puede ser agobiante!",
		                        "Absurdo: Lo ordinario nunca sera la norma.",
		                        "Directauro: Adios, comadres.",
		                        "Directauro: Y a ustedes, los 'heroes'.",
		                        "Directauro: Les deseo suerte en su odisea.",
		                        "Directauro: Epilogo, Ars Litterae. Se cierra el telon.")
		        );

				Enemy ghostSwordsman = new Enemy("Espadachin Fantasma", "ghostSwordsman", "PHY",
						520, 80, 220,
						80, 62, 20, 35, 60,
						130, 70, 30,
						0.75, 0.5, 0.75, 1.5, 0.75, 1.5,
						defAtt, defend);
				ghostSwordsman.setDesc("Un espadachin que parece recordar perfectamente como peleaba antes de morir.\n"
		        + "No reconozco estas espadas... quizas deberia llevarme una para mostrarsela a un experto.\n"
		        + "Me olvide que el ectoplasma es... dificil de llevar.\n"
		        + "Me gustaria preguntarle quien le enseño.\n"
		        + "No tiene boca y debe gritar.");
				ghostSwordsman.addSkills(venomEdge, fastAtk);

				Enemy etherealArmor = new Enemy("Armadura Eterea", "etherealArmor", "PHY",
						700, 40, 180,
						70, 88, 15, 40, 18,
						140, 80, 30,
						0.75, 0.5, 1.0, 0.75, 1.0, 0.75,
						defAtt, defend);
				etherealArmor.setDesc("Una armadura sin cuerpo.\n"
		        + "No sabemos quien la llevaba, donde esta el cuerpo, ni por que sigue moviendose.\n"
		        + "Uno de los miembros dijo que tal vez la armadura esta embrujada. Otro dijo que tal vez el cuerpo es la armadura.\n"
		        + "No tenemos pruebas para ninguna de las dos.\n"
		        + "Personalmente, me preocupa mas que ninguna de las dos sea la respuesta.");
				etherealArmor.addSkills(sneakAtk, vitalTheft);

				Enemy ghostMaster = new Enemy("Maestro Fantasmal", "ghostMaster", "RAN",
						470, 120, 180,
						76, 55, 72, 60, 58,
						150, 85, 20,
						0.75, 0.5, 1.0, 0.75, 0.75, 2.0,
						defAtt, defend);
				ghostMaster.setDesc("Un combatiente que parece dirigir a los otros fantasmas incluso despues de muerto.\n"
		        + "Parece que no funciona mediante palabras, sino mas como una mente colmena.\n"
		        + "Como manager, deberia admirar ese nivel de coordinacion.\n"
		        + "Como objetivo de sus ataques, preferiria que trabajara peor.");
				ghostMaster.addSkills(vitalTheft, bladeSweep);

				Enemy ghostWizard = new Enemy("Hechicero Fantasma", "ghostWizard", "WIN",
						430, 180, 120,
						30, 35, 85, 78, 52,
						145, 80, 20,
						0.75, 0.5, 0.75, 1.5, 2.0, 0.75,
						defAtt, defend);
				ghostWizard.setDesc("Un hechicero con una gran proficiencia en las artes oscuras.\n"
		        + "Guarda hechizos y rituales que no aparecen en los apuntes de nuestros magos.\n"
		        + "Eso me interesa bastante. Tambien me preocupa bastante.\n"
		        + "Le pedi al equipo que recuperara sus notas. Me miraron como si acabara de pedirles que tocaran una bomba.\n"
		        + "Creo que tienen razon.");
				ghostWizard.addSkills(magicTheft, tornado);

				Boss ghostKnightBoss = new Boss("Caballero Fantasma", "ghostKnightBoss", "WIN",
						6500, 250, 400,
						92, 84, 90, 88, 62,
						1100, 900, 0,
						0.5, 1.0, 0.75, 0.75, 0.75, 1.0,
						defAtt, defend, Flags.FLAG_FOURTHBOSS_DEFEATED);
				ghostKnightBoss.setDesc("Este no es simplemente otro caballero muerto.\n"
		        + "Si tuviera que apostar, diria que alguna vez fue alguien importante.\n"
		        + "Se mueve como un comandante, conoce nuestras formaciones y parece saber exactamente donde golpear para desarmar al equipo.\n"
		        + "No pudimos verle el rostro. Tal vez sea mejor asi.\n"
		        + "Hay personas a las que uno prefiere no conocer demasiado.");
				ghostKnightBoss.addSkills(hitman, heavyLand, vitalTheft);
				ghostKnightBoss.setEvents(
		                new BossEvent(false, 0,
		                        "Han llegado tan lejos...",
		                        "Es mas de lo que cualquiera podria haber llegado.",
		                        "Pero... ya es muy tarde.",
		                        "Yo no tengo un deseo de vivir como los otros.",
		                        "Mi contrato con esta organizacion es tan solo comprar algo de tiempo.",
		                        "Luego de eso, volvere a mi eterno letargo.",
		                        "Veamos lo que tienen guardado para este caballero."),
		                new BossEvent(false, 1,
		                		"Lamento mucho si esperaban a alguien con mas conocimiento sobre la organizacion.",
		                		"Realmente les hubiese contado si supiese, no lo explica en ninguna parte del contrato.",
		                		"Espero que sepan disculparme. o no, no me importa.",
		                		"Pero algo raro ocurre aqui..."),
		                new BossEvent(false, 2,
		                		"Luego de mi, solo tendran un piso mas. El Jefe los espera en el fondo.",
		                		"No es alguien con quien quieran encontrarse...",
		                		"Pero note algo especial en ustedes."),
		                new BossEvent(false, 3,
		                		"Siempre parecian volver, sin importar que tan dañados quedasen.",
		                		"Quemados, cortados, aplastados, asfixiados...",
		                		"Parece que siempre vuelven al punto donde murieron.",
		                		"Y creo que eso se relaciona al hombre que se encuentra detras suya."),
		                new BossEvent(false, 5, 
		                		"No se que tiene de especial...",
		                		"Pero...",
		                		"Me pidieron especificamente que no lo matase.",
		                		"No les parece raro..?"),
		                new BossEvent(false, 10, 
		                		"Creo que con esto es suficiente...",
		                		"Mi contrato ha sido cumplido."),
		                new BossEvent(true, 75, hitman,
		                        "El objetivo ha sido elegido.",
		                        "Una baja puede romper una formacion perfecta."),
		                new BossEvent(true, 50, heavyLand,
		                        "No necesitan caer todos.",
		                        "Solo necesito que se tomen su dulce tiempo."),
		                new BossEvent(true, 25, vitalTheft,
		                        "Su propia fuerza servira para mantenerme de pie.",
		                        "No desperdicien recursos contra mi."),
		                new BossEvent(true, 0, 
		                		"Vaya...",
		                		"Heh... me derrotaron...",
		                		"Felicidades.",
		                		"Fue un gusto luchar con ustedes, aunque si siguiese vivo...",
		                		"No habrian durado ni un minuto.",
		                		"Hasta luego, mercenarios.",
		                		"Espero que ahi debajo no les esperen unas buenas luchas.")
		        );

				Enemy evilHenchman = new Enemy("Secuaz Malvado", "evilHenchman", "PHY",
						700, 120, 220,
						95, 75, 40, 45, 60,
						200, 120, 40,
						0.5, 1.0, 0.5, 0.75, 0.5, 2.0,
						defAtt, defend);
				evilHenchman.setDesc("El nombre no le hace justicia.\n"
		        + "Es un soldado entrenado y parece conocer exactamente las debilidades de una formacion de mercenarios.\n"
		        + "No habla mucho. Cada movimiento suyo parece decirnos que ya ha hecho esto antes.\n"
		        + "Empiezo a sospechar que nosotros tampoco somos el primer equipo enviado hasta aqui.");
				evilHenchman.addSkills(decCut, smokeBomb);

				Enemy herculeanVeteran = new Enemy("Veterano Herculeo", "herculeanVeteran", "PHY",
						920, 80, 280,
						105, 92, 30, 40, 38,
						210, 130, 30,
						1.0, 0.5, 0.5, 0.5, 1.5, 2.0,
						defAtt, defend);
				herculeanVeteran.setDesc("Un veterano demasiado competente como para seguir trabajando de guardia.\n"
		        + "Sus marcas y su tecnica sugieren que sobrevivio a muchas batallas.\n"
		        + "Me pregunto cuantas fueron por voluntad propia y cuantas por necesidad.\n"
		        + "Para ser sincero, me cae bien. Es una lastima que estemos en lados distintos.");
				herculeanVeteran.addSkills(lunge, brutalBlow);

				Enemy wanderingGiant = new Enemy("Gigante Errante", "wanderingGiant", "EAR",
						1300, 0, 350,
						118, 95, 20, 35, 20,
						220, 150, 20,
						0.5, 0.75, 0.25, 1.5, 0.25, 2.0,
						defAtt, defend);
				wanderingGiant.setDesc("No parece pertenecer a la organizacion ni cuidar este lugar.\n"
		        + "Simplemente deambula por el ultimo piso y ataca todo lo que se mueve.\n"
		        + "Le dimos varias oportunidades para que se calmara. No funciono.\n"
		        + "Actualizacion: tampoco funciono ofrecerle comida.\n"
		        + "Amenaza ambiental con piernas. Una muy grande.");
				wanderingGiant.addSkills(brutalBlow, heavyTackle);

				Enemy grandMaster = new Enemy("Gran Maestro del Combate", "grandMaster", "PHY",
						850, 200, 250,
						110, 88, 85, 82, 75,
						350, 200, 10,
						0.75, 0.5, 0.5, 0.5, 0.5, 1.0,
						defAtt, defend);
				grandMaster.setDesc("Un guerrero que parece haber estudiado todos los estilos que usamos.\n"
		        + "Bloquea, responde y cambia de objetivo con una precision preocupante. Incluso parece anticiparse a nuestras decisiones.\n"
		        + "Si fue contratado por la organizacion, seguramente sabe que tipo de mercenarios llegan hasta aqui y prepara la defensa para ellos.\n"
		        + "Si no fue contratado... entonces tenemos otro problema.");
				grandMaster.addSkills(warCry, backhand);

				Boss nameless = new Boss("El Innombrable", "nameless", "PHY",
						9500, 500, 500,
						125, 110, 120, 115, 90,
						2000, 2500, 0,
						0.5, 0.25, 0.5, 0.25, 0.5, 0.25,
						defAtt, defend, Flags.FLAG_FIFTHBOSS_DEFEATED);
				nameless.setDesc("El 'Jefe'... y sin nombre.\n"
		        + "Solo lleva una designacion que aparece repetida en los registros destruidos que encontramos durante la bajada.\n"
		        + "Nadie del equipo consigue describirlo de la misma manera dos veces. Yo tampoco.\n"
		        + "Lo unico constante es que parece ser la razon por la que la dungeon sigue funcionando. Bueno, seguia.\n"
		        + "Supongo que este es el momento donde un manager deberia decir algo inspirador.\n"
		        + "No tengo nada. Asi que... terminemos el trabajo y volvamos a casa.");
				nameless.addSkills(crushAtk, heavyLand, sleepPll);
				nameless.setEvents(
		                new BossEvent(false, 0,
		                        "Han llegado al final.",
		                        "No porque fueran especiales.",
		                        "Simplemente les permitimos llegar hasta aqui."),
		                new BossEvent(false, 1,
		                        "Por alguna razon... no logro recordar si esta es la primera vez que luchamos o no.",
		                        "Parece que uno de los efectos secundarios es el no recordar los ciclos...",
		                        "Pero no va a ser un problema."),
		                new BossEvent(false, 2,
		                        "Aunque me derroten... no podran vivir una vida tranquila ahi fuera.",
		                        "Si mato a su manager, podrian morir en paz.",
		                        "Por que no se rinden? Me encargare de que no sufran."),
		                new BossEvent(false, 5,
		                        "Me estan haciendo enojar...",
		                        "No pueden vencerme. No pueden vencer a la organizacion.",
		                        "Por que siguen luchando? Por que no se rinden?"),
		                new BossEvent(false, 10, chaos,
		                        "Por que... aguantan tanto...",
		                        "Por que no me dejan matarlos!?"),
		                new BossEvent(false, 15, finalTrial,
		                        "..."),
		                new BossEvent(true, 75, heavyLand,
		                        "Todavia creen que esto es un combate?",
		                        "Entonces sufran en la ignorancia."),
		                new BossEvent(true, 50, sleepPll,
		                        "El cansancio siempre alcanza a nuestros cientificos.",
		                        "Es hora de que tambien los alcance a ustedes."),
		                new BossEvent(true, 25, crushAtk,
		                        "Ya vi todo lo que necesitaba...",
		                        "Ahora termina la expedicion."),
		                new BossEvent(true, 10, lastPrism,
		                        "No deberian seguir de pie...",
		                        "Esta vez no voy a medir la fuerza."),
		                new BossEvent(true, 0,
		                        "...",
		                        "V-Vaya...",
		                        "E-Esto... no deberia... de...",
		                        "...",
		                        "E-Esta bien...",
		                        "Antes que... un mercenario... soy un luchador...",
		                        "Un... luchador?",
		                        "Soy... un arma?",
		                        "Eso soy...?",
		                        "No... un alma...",
		                        "Y una persona...",
		                        "Lo consiguieron...",
		                        "Pero... les aviso...",
		                        "Si... S-se llevan la raiz dorada...",
		                        "Los buscaran... por todos lados...",
		                        "Los van a encontrar... y...",
		                        "Ahi si... que van a estar en problemas...",
		                        "No es... la unica... raiz...",
		                        "P-pero...",
		                        "Vale... la pena?",
		                        "Valio... la pena?",
		                        "...",
		                        "Te tendria que haber hecho caso... Marie...",
		                        "Que sentido tiene...?",
		                        "Por que... hay honor... en la muerte..?",
		                        "No tiene... nada de honorifico... arrebatarle la vida a otro ser...",
		                        "Ojala lo hubiese entendido...",
		                        "El significado...",
		                        "No existe...",
		                        "La Muerte...",
		                        "La vida...",
		                        "Es todo...",
		                        "Absurdo...",
		                        "Tengo un nombre...",
		                        "Pero a nadie le importa...",
		                        "No existen hombres, ni sus nombres...",
		                        "Existen armas, sin almas...",
		                        "Eso somos... para ellos...",
		                		"Mi nombre...",
		                		"...")
		        );

				initEnemies(
						slime, goblin, skeleton, mimic, ogre,
						armoredSkel, undeadKnight, corrWizard, ghostKnight, headless,
						ghostBow, ebonyWar, centaur, minotaur, multitaur,
						ghostSwordsman, etherealArmor, ghostMaster, ghostWizard, ghostKnightBoss,
						evilHenchman, herculeanVeteran, wanderingGiant, grandMaster, nameless
				);
				Main.player.setBestiary(enemies);
				//initialize battles
				Battle defBattle = new Battle(false, 3, slime, goblin, skeleton, mimic);
				Battle boss1 = new Battle(true, 1, ogre);
				Battle defBattle2 = new Battle(false, 3, armoredSkel, undeadKnight, corrWizard, ghostKnight);
				Battle boss2 = new Battle(true, 1, headless);
				Battle defBattle3 = new Battle(false, 3, ghostBow, ebonyWar, centaur, minotaur);
				Battle boss3 = new Battle(true, 1, multitaur);
				Battle defBattle4 = new Battle(false, 3, ghostSwordsman, etherealArmor, ghostMaster, ghostWizard);
				Battle boss4 = new Battle(true, 1, ghostKnightBoss);
				Battle defBattle5 = new Battle(false, 3, evilHenchman, herculeanVeteran, wanderingGiant, grandMaster);
				Battle boss5 = new Battle(true, 1, nameless);

				initBattles(
						defBattle, boss1,
						defBattle2, boss2,
						defBattle3, boss3,
						defBattle4, boss4,
						defBattle5, boss5
				);

				defBattleScreen = new BattleScreen(this, defBattle, new Texture("sprites/background/background_battle_1.jpg"));
				firstBossFight = new BattleScreen(this, boss1, new Texture("sprites/background/background_battle_boss.jpg"));
				defBattle2Screen = new BattleScreen(this, defBattle2, new Texture("sprites/background/background_battle_1.jpg"));
				secondBossFight = new BattleScreen(this, boss2, new Texture("sprites/background/background_battle_boss.jpg"));
				defBattle3Screen = new BattleScreen(this, defBattle3, new Texture("sprites/background/background_battle_1.jpg"));
				thirdBossFight = new BattleScreen(this, boss3, new Texture("sprites/background/background_battle_boss.jpg"));
				defBattle4Screen = new BattleScreen(this, defBattle4, new Texture("sprites/background/background_battle_1.jpg"));
				fourthBossFight = new BattleScreen(this, boss4, new Texture("sprites/background/background_battle_boss.jpg"));
				defBattle5Screen = new BattleScreen(this, defBattle5, new Texture("sprites/background/background_battle_1.jpg"));
				fifthBossFight = new BattleScreen(this, boss5, new Texture("sprites/background/background_battle_boss.jpg"));

	}
	private void initBattles(Battle... battles1) {
		battles = battles1;
	}
	private void initEnemies(Enemy... enemies1) {
		enemies = enemies1;
	}
	private static void initItems(Item... items1) {
		items = items1;
	}

	public static void updateArrays() {
		for(int i = 0; i < player.getInventory().length; i++) {
			for(int j = 0; j < items.length; j++) {
				if(player.getInventory()[i].getIDName().equals(items[j].getIDName())) {
					items[j] = player.getInventory()[i];
				}
			}
		}
		for(int i = 0; i < player.getWeapons().length; i++) {
			for(int j = 0; j < items.length; j++) {
				if(player.getWeapons()[i].getIDName().equals(items[j].getIDName())) {
					items[j] = player.getWeapons()[i];
				}
			}
		}
		for(int i = 0; i < player.getArmors().length; i++) {
			for(int j = 0; j < items.length; j++) {
				if(player.getArmors()[i].getIDName().equals(items[j].getIDName())) {
					items[j] = player.getArmors()[i];
				}
			}
		}
		for(int i = 0; i < player.getCharacters().length; i++) {
			for(int j = 0; j < items.length; j++) {
				if(player.getCharacters()[i].getArmor().getIDName().equals(items[j].getIDName())) {
					items[j] = player.getCharacters()[i].getArmor();
				}
				if(player.getCharacters()[i].getWeapon().getIDName().equals(items[j].getIDName())) {
					items[j] = player.getCharacters()[i].getWeapon();
				}
			}
		}
		for(int i = 0; i < enemies.length; i++) {
			if(player.getBestiary() != null) {
				for(int j = 0; j < player.getBestiary().length; j++) {
						if(player.getBestiary()[j].getIDName().equals(enemies[i].getIDName())) {
							enemies[i].discovered = player.getBestiary()[j].discovered;
			                enemies[i].defeated = player.getBestiary()[j].defeated;
			                enemies[i].setWeaknesses(player.getBestiary()[j].getWeaknesses());
			                player.getBestiary()[j] = enemies[i];enemies[i] = player.getBestiary()[j];
						}
				}
			}else {
				player.setBestiary(enemies);
				break;
			}
		}
	}
	
	public void toggleFullscreen() {
	    if (config.fullScreen) {
	        Gdx.graphics.setWindowedMode(config.resX, config.resY);
	        config.fullScreen = false;
	    } else {
	        Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
	        Gdx.graphics.setFullscreenMode(currentMode);
	        config.fullScreen = true;
	    }
	    Save.saveConfig(config);
	}
	public void checkFullscreen() {
		if (!config.fullScreen) {
	        Gdx.graphics.setWindowedMode(config.resX, config.resY);
	    } else {
	        Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
	        Gdx.graphics.setFullscreenMode(currentMode);
	    }
	}
	public void saveConfig(Config config) {
		Main.config = config;
		Save.saveConfig(config);
	}
}
