package io.github.dungeon_slashers.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.dungeon_slashers.Skill;
import io.github.dungeon_slashers.controllers.Menu;
import io.github.dungeon_slashers.item.Armor;
import io.github.dungeon_slashers.item.Weapon;

/*
		CLASE HERO

	será la clase encargada de controlar a los heroes, sus niveles y sus estadisticas.

*/

public class Hero extends Entity{
	
	private String direction;
	private transient Animation<TextureRegion> walkDown;
	private transient Animation<TextureRegion> walkUp;
	private transient Animation<TextureRegion> walkLeft;
	private transient Animation<TextureRegion> walkRight;

	private transient TextureRegion currentFrame;
	private transient Texture portrait;

	private float stateTime = 0;

	// experiencia
	public int xp;
	private int nextXp;
	private int lvl;
	
	//armas y armaduras
	private Weapon weapon;
	private Armor armor;
	
	
	private Skill[] posSkills;
	
	//constructora
	public Hero(String name, String IDname, String className, String baseType, int maxhp, int maxmp, int maxsp, int atk, 
			int def, int mat, int mdf, int spd, Weapon weapon, Armor armor, Skill attack, Skill defend, Skill... posSkills) {
		this.name = name;
		this.IDname = IDname;
		this.className = className;
		this.baseType = baseType;
		this.maxhp = maxhp;
		this.hpIn = maxhp;
		this.hp = maxhp;
		this.maxsp = maxsp;
		this.spIn = maxsp;
		this.sp = maxsp;
		this.maxmp = maxmp;
		this.mpIn = maxmp;
		this.mp = maxmp;
		this.atkIn = atk;
		this.defIn = def;
		this.matIn = mat;
		this.mdfIn = mdf;
		this.spdIn = spd;
		this.atk = atk;
		this.def = def;
		this.mat = mat;
		this.mdf = mdf;
		this.spd = spd;
		this.atkF = atk;
		this.defF = def;
		this.matF = mat;
		this.mdfF = mdf;
		this.spdF = spd;
		this.xp = 0;
		this.lvl = 1;
		this.nextXp = (int) (100*lvl+(100*lvl*(0.1*lvl)));
		this.armor = armor;
		this.weapon = weapon;
		this.skills = new Skill[3];
		this.skills[0] = attack;
		this.skills[1] = defend;
		this.posSkills = posSkills;
		skills[2] = new Skill(0); //Habilidad para usar items en combate
		prot = 1;
		loadTextures();
	}
	
	public void loadTextures() {
		String path = "sprites/heroes/" + IDname + "-idle-down.png";
		if (Gdx.files.internal(path).exists()) {
		    texture = new Texture(path);
		} else {
		    texture = new Texture("sprites/something.png");
		}
		direction = "down";
		portrait = new Texture("sprites/heroes/" + IDname + "-portrait.png");
		walkDown = createAnimation("down");
		walkUp = createAnimation("up");
		walkLeft = createAnimation("left");
		walkRight = createAnimation("right");
		currentFrame = walkDown.getKeyFrame(0.15f);
	}

	//Getters
	public int getXPleft() {
		return (nextXp - xp);
	}
	public int getLVL() {
		return this.lvl;
	}
	public Weapon getWeapon() {
		return this.weapon;
	}
	public Armor getArmor() {
		return this.armor;
	}
	public TextureRegion getCurrentFrame(){
	    return currentFrame;
	}
	public Texture getPortrait() {
		return portrait;
	}
	
	//Verifica si la XP ya pasó el límite del nivel, y si lo hace sube el nivel y updatea las stats y las skills
	public void checkLvl() {
		if(xp >= nextXp) {
			lvl++;
			Menu.msg(name + " subio al nivel " + lvl + "!");
			this.updateStats();
			hp = maxhp;
			mp = maxmp;
			sp = maxsp;
			this.updateSkills();
			Menu.msg("\n");
			nextXp = (int) (100*lvl+(100*lvl*(0.1*lvl)));
		}
	}
	
	private void updateSkills() { //Las skills por nivel
		for(int i = 0; i < posSkills.length; i++) {
			if(posSkills[i].getLvl() == lvl) {
				addSkill(posSkills[i]);
				Menu.msg(name + " aprendio " + posSkills[i].getName() + "!");
			}
		}
	}
	
	public Skill[] getRealSkills() {
		Skill[] temp = new Skill[skills.length - 3];
		for(int i = 3; i < skills.length; i++) {
			temp[i - 3] = skills[i];
		}
		return temp;
	}

	//Actualizar las stats
	public void updateStats() {
		//Math.pow es para hacer potencias. el 1er parametro es la base, el 2do la potencia
		atk   = atkIn*( (int)Math.pow(1.15,(lvl-1)) );
		def   = defIn*( (int)Math.pow(1.15,(lvl-1)) );
		mat   = matIn*( (int)Math.pow(1.15,(lvl-1)) );
		mdf   = mdfIn*( (int)Math.pow(1.15,(lvl-1)) );
		spd   = spdIn*( (int)Math.pow(1.15,(lvl-1)) );
		maxhp = hpIn*( (int)Math.pow(1.15,(lvl-1)) );
		maxsp = spIn + (lvl-1) * 20;
		maxmp = mpIn + (lvl-1) * 20;
		updateFinalStats();
	}
	
	//calcular las stats con armas y armaduras agregado
	public void updateFinalStats() {
		atkF = atk + weapon.getATK() + armor.getATK();
		matF = mat + weapon.getMAT() + armor.getMAT();
		defF = def + armor.getDEF();
		mdfF = mdf + armor.getMDF();
		spdF = spd + weapon.getSPD() + armor.getSPD();
	}
	
	//cambiar el arma del heroe por otra
	public Weapon changeWeapon(Weapon weapon) {
		Weapon oldWpn = null;
		for(int i = 0; i < weapon.getclassName().length; i++) {
			if (className.equals(weapon.getclassName()[i])) {
				oldWpn = this.weapon;
				this.weapon = weapon; //se asegura de que el arma sea de la clase
				updateFinalStats();
			}
		}
		return oldWpn;
	}
	
	//cambiar la armadura
	public Armor changeArmor(Armor armor) {
		Armor oldArm = this.armor;
		this.armor = armor;
		updateFinalStats();
		return oldArm;
	}

	//creador de animaciones de movimiento
	private Animation<TextureRegion> createAnimation(String dir) {

		TextureRegion[] frames = new TextureRegion[3];

	    frames[0] = loadFrame(
	        "sprites/heroes/" + IDname + "-walking-" + dir + "-1.png" //carga el frame 1
	    );

	    frames[1] = loadFrame(
	        "sprites/heroes/" + IDname + "-idle-" + dir + ".png" //carga el frame 2
	    );

	    frames[2] = loadFrame(
	        "sprites/heroes/" + IDname + "-walking-" + dir + "-2.png" //carga el frame 3
	    );
	    return new Animation<TextureRegion>(0.1f, frames); //los devuelve
	}
	
	private TextureRegion loadFrame(String path) {

	    if(Gdx.files.internal(path).exists()) {
	        return new TextureRegion(new Texture(path));
	    }

	    return new TextureRegion(new Texture("sprites/something.png"));
	}
	
	public void aniManager(float[] floats, float delta) {

	    stateTime += delta;

	    boolean moving = floats[0] != 0 || floats[1] != 0;


	    if(floats[0] < 0) {
	        direction = "left";
	    }
	    else if(floats[0] > 0) {
	        direction = "right";
	    }


	    if(floats[1] < 0) {
	        direction = "down";
	    }
	    else if(floats[1] > 0) {
	        direction = "up";
	    }


	    if(moving) {
	    	
	    	//si se esta moviendo, hace la animacion
	    	
	        switch(direction) {

	        case "down":
	            currentFrame = walkDown.getKeyFrame(stateTime, true); 
	            break;

	        case "up":
	            currentFrame = walkUp.getKeyFrame(stateTime, true);
	            break;

	        case "left":
	            currentFrame = walkLeft.getKeyFrame(stateTime, true);
	            break;

	        case "right":
	            currentFrame = walkRight.getKeyFrame(stateTime, true);
	            break;
	        }

	    }else {
	    	switch(direction) {

	        case "down":
	            currentFrame = walkDown.getKeyFrame(0.15f, false);
	            break;

	        case "up":
	            currentFrame = walkUp.getKeyFrame(0.15f, false);
	            break;

	        case "left":
	            currentFrame = walkLeft.getKeyFrame(0.15f, false);
	            break;

	        case "right":
	            currentFrame = walkRight.getKeyFrame(0.15f, false);
	            break;
	        }
	    }
	}
}
