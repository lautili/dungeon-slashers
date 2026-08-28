package io.github.dungeon_slashers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.dungeon_slashers.controllers.Battle;
import io.github.dungeon_slashers.controllers.InputMan;
import io.github.dungeon_slashers.controllers.Menu;
import io.github.dungeon_slashers.controllers.Save;
import io.github.dungeon_slashers.controllers.Store;
import io.github.dungeon_slashers.entities.Boss;
import io.github.dungeon_slashers.entities.BossEvent;
import io.github.dungeon_slashers.entities.Enemy;
import io.github.dungeon_slashers.entities.Hero;
import io.github.dungeon_slashers.item.Armor;
import io.github.dungeon_slashers.item.Item;
import io.github.dungeon_slashers.item.Weapon;
import io.github.dungeon_slashers.screens.FirstScreen;
import io.github.dungeon_slashers.screens.MainMenuScreen;
import io.github.dungeon_slashers.screens.MenuScreen;
import io.github.dungeon_slashers.screens.StoreScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
	
	public static Player player = new Player();
	static Battle[] battles;
	static Enemy[] enemies;
	static Item[] items;
	
	public SpriteBatch batch;
	public FitViewport viewport;
	
	public BitmapFont mainFont;
	public BitmapFont invFont;
	public BitmapFont dialFont;
	public BitmapFont titleFont;
	
	public Texture colBox;
	
	//screens
	public FirstScreen firstScreen;
	public MenuScreen menuScreen;
	public StoreScreen storeScreen;
	
    @Override
    public void create() {
    	batch = new SpriteBatch();
		mainFont = new BitmapFont(Gdx.files.internal("ui/fonts/main.fnt"));
		dialFont = new BitmapFont(Gdx.files.internal("ui/fonts/dialogue.fnt"));
		titleFont = new BitmapFont(Gdx.files.internal("ui/fonts/title.fnt"));
		invFont = new BitmapFont(Gdx.files.internal("ui/fonts/inventory.fnt"));
		viewport = new FitViewport(320, 180);
		colBox = new Texture("collision_box.png");
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
    	
    	firstScreen = new FirstScreen(this);
    	menuScreen = new MenuScreen(this);
    	storeScreen = new StoreScreen(this);
    	
        setScreen(new MainMenuScreen(this));
    }
    
    @Override 
    public void render() {
    	super.render();
    }
    
    @Override
    public void dispose() {
		batch.dispose();
		mainFont.dispose();
		invFont.dispose();
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
				Item minHPot = new Item("Pocion menor de salud", "minHPot", "Cura poca cantidad de salud.", 20, 3, false, true);
				Item minMPot = new Item("Pocion menor de mana", "minMPot", "Cura una poca cantidad de mana.", 20, 3, false, true);
				Item minSPot = new Item("Pocion menor de stamina", "minSPot", "Cura una poca cantidad de stamina.", 20, 3, false, true);
				Item minHPven = new Item("Veneno menor de salud", "minHPven", "Quita una poca cantidad de salud.", 20, 1, false, false);
				
				initItems(dullSword, dullDaggers, brokenStaff, oldBow, ironSword, woodenStaff,
						
						rags, leatherArmor, ironArmor, wizRobes,
						
						minHPot, minMPot, minSPot, minHPven);
				
				player.addItems(minHPot, 5);
				player.addItems(minMPot, 5);
				player.addItems(minSPot, 5);
				
				Store.addItems(minHPot, minMPot, minHPven, ironSword, woodenStaff, leatherArmor, ironArmor, wizRobes);
				
				//initialize habilidades
				Skill defAtt = new Skill("defAtt", "Ataque comun", "Un ataque fisico basico.", null, " ataca a ", 1, 0, false, 1);
				Skill defMat = new Skill("defMat", "Ataque magico comun", "Un ataque magico basico.", null,  " ataca a ", 1, 0, false, 1);
				Skill defend = new Skill("defend", "Defender", "Se protege de los proximos ataques.", "NONE", " se defiende.", 0, 999, false, 1);
				
				//guerrero
				Skill charAtk = new Skill("charAtk", "Ataque cargado", "Inflige da o medio a un enemigo.", "PHY", 
						" lanza un ataque cargado a ",
						1, 0, false, 1, 0, 30);
				Skill deepCut = new Skill("deepCut", "Corte profundo", "Inflige da o bajo con chances de aplicar sangrado.", "PHY", 
						" corta profundamente a ",
						1, 0, false, 1, 0, 30);
				Skill knockout = new Skill("knockout", "Golpe de Gracia", "Inflinge da o bajo con bajas chances de aplicar confusion.", "PHY",
						" le da un golpe de gracia a ", 
						1, 0, false, 1, 0, 40, 2);
				Skill crossCut = new Skill("crossCut", "Corte cruzado", "Inflinge da o bajo a todos los enemigos.", "PHY",
						" corta a traves de los enemigos.", 
						2, 5, false, 1, 0, 50, 2);
				Skill skullCracker = new Skill("skullCracker", "Rompecraneos", "Inflinge da o medio a un enemigo con chances de confusion.", "PHY",
						" le destruye el craneo a  ", 
						1, 0, false, 1, 0, 45, 3);
				Skill brutalBlow = new Skill("brutalBlow", "Golpe brutal", "Inflinge da o elevado a un enemigo.", "PHY",
						" destruye a ", 
						1, -10, false, 1, 0, 80, 4);
				Skill moralDest = new Skill("moralDest", "Desestabilizador de Moral", "Inflinge da o medio a todos los enemigos con chances de atacar de vuelta.", "PHY",
						" desestabiliza a sus enemigos.", 
						2, 0, false, 1, 0, 80, 4);
				Skill lunge = new Skill("lunge", "Embestida", "Inflinge da o medio a un enemigo y lo confunde.", "PHY",
						" embiste contra ", 
						1, 5, false, 1, 0, 100, 5);
				Skill heavyTackle = new Skill("heavyTackle", "Barrida contundente", "Inflinge da o medio a todos los enemigos con altas chances de aplicar cansancio.", "PHY",
						" Barre a los enemigos.", 
						2, -10, false, 1, 60, 120, 6);
				Skill hustle = new Skill("hustle", "Chicaneo", "Inflinge da o elevado a un enemigo con chances de aplicar Ira.", "PHY",
						" hace un movimiento rastrero contra ", 
						1, 5, false, 1, 50, 120, 7);
				Skill backhand = new Skill("backhand", "Golpe del reves", "Inflinge da o medio a un enemigo y lo duerme.", "PHY",
						" Le pega con el pomo a ", 
						1, 0, false, 1, 80, 120, 8);
				Skill heavyLand = new Skill("heavyLand", "Impacto pesado", "Inflinge da o elevado a todos los enemigos con chance de confundirlos.", "PHY",
						" destruye a sus enemigos.", 
						2, -15, false, 1, 40, 140, 8);
				Skill warCry = new Skill("warCry", "Grito de batalla", "Aplica confusion, ira o silencio a todos los enemigos.", "NONE",
						" Grita a todo pulmon. ", 
						2, 0, false, 1, 80, 140, 9);
				Skill crushAtk = new Skill("crushAtk", "Ataque Aplastante", "Inflinge da o elevado a todos los enemigos y aplica confusion y cansancio. ignora "
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
						" ataca con su daga envenenada a ",
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
				Skill deadShot = new Skill("deadShot", "Disparo certero", "Inflinge da o medio a un enemigo. ", "RAN", 
						" le lanza una flecha poderosa a ",
						1, 0, false, 1, 0, 30);
				Skill fireArrow = new Skill("fireArrow", "Flecha Ignifuga", "Inflinge da o medio de fuego a un enemigo. ", "FIR", 
						" le lanza una flecha en fuego a ",
						1, 0, false, 1, 10, 25);
				Skill iceArrow = new Skill("iceArrow", "Flecha Escarchada", "Inflinge da o medio de agua a un enemigo. ", "WAT", 
						" le lanza una flecha congelada a ",
						1, 0, false, 1, 10, 25);
				Skill arrowRain = new Skill("arrowRain", "LLuvia de flechas", "Inflinge da o bajo a todos los enemigos. ", "RAN", 
						" nubla el cielo de flechas.",
						2, 0, false, 1, 0, 45, 2);
				Skill calTrap = new Skill("calTrap", "Trampa de Abrojos", "Envenena a un enemigo. ", "NONE", 
						" posiciona una trampa cerca de ",
						1, 0, false, 1, 30, 30, 2);
				Skill decoy = new Skill("decoy", "Se uelo", "Aplica el estado confusion a un enemigo. ", "NONE", 
						" enga a con un se uelo a ",
						1, 0, false, 1, 40, 40, 3);
				Skill slimeDust = new Skill("slimeDust", "Polvo de Slime", "Aplica el estado Envenenado a todos los enemigos. ", "NONE", 
						" lanza un polvo de slime a sus enemigos!",
						2, 0, false, 1, 80, 80, 4);
				Skill nailIt = new Skill("nailIt", "Tiro al Clavo", "Inflinge da o elevado a un enemigo. ", "RAN", 
						" lanza una poderosa flecha cargada a ",
						1, 0, false, 1, 10, 100, 4);
				Skill expTorment = new Skill("expTorment", "Tormento del Explorador", "Aplica el estado confusion y cansancio a todos los enemigos. ", "NONE", 
						" atormenta a sus enemigos...",
						2, 0, false, 1, 80, 90, 5);
				Skill tarPit = new Skill("tarPit", "Trampa de Alquitran", "Aplica el estado envenenado y cansancio a todos los enemigos. ", "NONE", 
						" prepara una fuerte pocima para sus enemigos!",
						2, 0, false, 1, 110, 110, 6);
				Skill debrisShower = new Skill("debrisShower", "LLuvia de Escombros", "Inflinge da o de tierra elevado a los enemigos. ", "EAR", 
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
				Skill finalTrial = new Skill("finalTrial", "Flecha del Juicio Final", "Inflinge da o elevado universal a todos los enemigos y los hace sangrar. ", "UNI", 
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
				Skill toxicDust = new Skill("toxicDust", "Polvo toxico", "Inflinge da o de viento medio a todos los enemigos"
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
				
				Hero[] chars = player.getCharacters();
				chars[0] = warrior;
				chars[1] = mage;
				chars[2] = explorer;
				chars[3] = sage;
				
				warrior.addSkills(charAtk, deepCut);
				mage.addSkills(fireBall, windBurst, terrAttack, splatter);
				thief.addSkills(decCut, fastAtk);
				explorer.addSkills(deadShot, fireArrow, iceArrow);
				sage.addSkills(healing, mulHeal);
				
				//initialize enemigos
				Enemy slime = new Enemy("Slime", "slime", "WAT", 90, 0, 40, 12, 10, 0, 10, 8, 18, 8, 50, 1.0, 1.0, 2.0, 0.75, 0.5, 1.0, defAtt, defend);
				slime.addSkill(charAtk);
				Enemy goblin = new Enemy("Goblin", "goblin", "PHY", 120, 40, 80, 20, 15, 8, 12, 20, 24, 12, 30, 1.5, 0.75, 1.0, 1.5, 0.5, 1.0, defAtt, defend);
				goblin.addSkills(decCut, fastAtk);
				Enemy skeleton = new Enemy("Esqueleto", "skeleton", "RAN", 105, 50, 60, 22, 15, 17, 16, 18, 26, 15, 15, 1.0, 2.0, 0.75, 0.75, 1.5, 1.0, defAtt, defend);
				skeleton.addSkills(arrowRain, fireArrow);
				Enemy mimic = new Enemy("Mimico", "mimic", "PHY", 180, 0, 0, 28, 25, 0, 10, 12, 50, 90, 5, 0.5, 0.5, 2.0, 0.5, 0.25, 1.0, defAtt, defend);
				
				Boss ogre = new Boss("Ogro", "ogre", "PHY", 950, 40, 220, 34, 24, 8, 18, 12, 180, 120, 0, 0.5, 1.0, 1.0, 0.25, 0.5, 1.5, defAtt, defend, 0);
				ogre.addSkills(charAtk, deepCut, crossCut);
					ogre.setEvents(
							new BossEvent(false, 0, "Muajajaja...", "Hola...", "Soy el ogro malvado...", "Tu primer desafio comienza aqui..."),
							new BossEvent(true, 50, crossCut, "De verdad creen que pueden derrotarme..?", "Les demostrare que se equivocan..."),
							new BossEvent(true, 0, "Vaya...", "C-con que... me han derrotado...", "Heh... esta bien...", 
									"S-Suerte...", "La...", "Necesitaran...")
					);
				
				Enemy dummy = new Enemy("Maniqui de practicas", "dummy", "PHY", 99999, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, null, null);
				
				initEnemies(slime, goblin, skeleton, mimic,
						
						ogre,
						
						dummy);
				
				//initialize battles
				Battle defBattle = new Battle(false, 3, slime, goblin, skeleton, mimic);
				Battle practice = new Battle(false, 3, dummy);
				Battle boss1 = new Battle(true, 1, ogre);
				
				initBattles(defBattle, 
						
						boss1,
						
						practice);
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

	public void setScreenFromSave(String currScreen) {
		switch(currScreen){
		case "MAIN_MENU_SCREEN":
			setScreen(new MainMenuScreen(this));
			break;
		case "FIRST_SCREEN":
			setScreen(new FirstScreen(this));
			break;
		}
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
	}
}