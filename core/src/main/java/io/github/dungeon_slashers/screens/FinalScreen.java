package io.github.dungeon_slashers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.MenuScrollType;
import io.github.dungeon_slashers.controllers.DialMan;
import io.github.dungeon_slashers.controllers.InputMan;
import io.github.dungeon_slashers.controllers.Menu;
import io.github.dungeon_slashers.controllers.Save;
import io.github.dungeon_slashers.entities.Hero;

/** First screen of the application. Displayed after the application is created. */
public class FinalScreen implements Screen {
    private Main game;
    private Hero[] chars;
    private boolean check;
    private OrthographicCamera camera;
    private FitViewport viewport;
    
    public FinalScreen(Main game) {
		this.game = game;
		check = false;
		chars = Main.player.getCharacters();
	}
    
	@Override
    public void show() {
        // Prepare your screen here.
		Main.player.currScreen = "CHAR_SELECT_SCREEN";
		camera = new OrthographicCamera();
		viewport = game.viewport;
		viewport.setCamera(camera);
		camera.setToOrtho(false, 320, 180);
		camera.zoom = 1f;
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
    	ScreenUtils.clear(0, 0, 0, 1); //limpia el buffer de colores
    	Hero char1 = chars[0];
    	Hero char2 = chars[1];
    	Hero char3 = chars[2];
    	Hero char4 = chars[3];
    	camera.update();
    	game.batch.setProjectionMatrix(camera.combined);
    	game.batch.begin();
    	if(!check) {
    		DialMan.addDialogue(0, 1, "Dante", null, "Conque esta es la raiz dorada...", 20, null);
    		DialMan.addDialogue(1, 2, "Dante", null, 
    				"Lo conseguimos."
    				, 20, null);
    		DialMan.addDialogue(2, 3, char1.getName(), char1.getPortrait(), 
    				"Y no gracias a " + char3.getName() + ", definitivamente."
    				, 20, null);
    		DialMan.addDialogue(3, 4, char3.getName(), char3.getPortrait(), 
    				"Hice mucho mas que tu!"
    				, 20, null);
    		DialMan.addDialogue(4, 5, null, null, 
    				char2.getName() + " se rie. Dante les da una palmada en la espalda."
    				, 20, null);
    		DialMan.addDialogue(5, 6, char4.getName(), char4.getPortrait(), 
    				"Cuesta creer que llegamos a esto juntos... vivos."
    				, 20, null);
    		DialMan.addDialogue(6, 7, "Dante", null, 
    				"Pero lo hicimos, es lo importante."
    				, 20, null);
    		DialMan.addDialogue(7, 8, char1.getName(), char1.getPortrait(), 
    				"Creen que... lo que dijo es verdad?"
    				, 20, null);
    		DialMan.addDialogue(8, 9, char1.getName(), char1.getPortrait(), 
    				"Que la organizacion que nos contrato lo hizo para mandarnos al muere?"
    				, 20, null);
    		DialMan.addDialogue(9, 10, "Dante", null, 
    				"No lo se..."
    				, 20, null);
    		DialMan.addDialogue(10, 11, "Dante", null, 
    				"Lo unico que se es que esta raiz no puede quedarse aqui."
    				, 20, null);
    		DialMan.addDialogue(11, 12, char1.getName(), char1.getPortrait(), 
    				"Estas loco? Nos van a perseguir por toda la vida!"
    				, 20, null);
    		DialMan.addDialogue(12, 13, "Dante", null, 
    				"ME van a perseguir. Oficialmente, desmantelo el equipo. Por vuestra seguridad."
    				, 20, null);
    		DialMan.addDialogue(13, 14, "Dante", null, 
    				char1.getName() + ", te designo ahora Manager del equipo."
    				, 20, null);
    		DialMan.addDialogue(14, 15, char1.getName(), char1.getPortrait(), 
    				"Que...?"
    				, 20, null);
    		DialMan.addDialogue(15, 16, "Dante", null, 
    				"No voy a permitir que los involucren en esto. Dijo que tenian mas, pero..."
    				, 20, null);
    		DialMan.addDialogue(16, 17, "Dante", null, 
    				"Si tan solo puedo retrasarlos aunque sea un poco..."
    				, 20, null);
    		DialMan.addDialogue(17, 18, "Dante", null, 
    				"Habra valido la pena."
    				, 20, null);
    		DialMan.addDialogue(18, 19, char2.getName(), char2.getPortrait(), 
    				"Dante, no."
    				, 20, null);
    		DialMan.addDialogue(19, 20, char3.getName(), char3.getPortrait(), 
    				"Pudimos con la mazmorra, podemos con cualquier cosa."
    				, 20, null);
    		DialMan.addDialogue(20, 21, "Dante", null, 
    				"El reloj parece haberse fusionado con mi brazo."
    				, 20, null);
    		DialMan.addDialogue(21, 22, "Dante", null, 
    				"No hay nada que yo pueda hacer. me he fusionado con una raiz."
    				, 20, null);
    		DialMan.addDialogue(22, 23, "Dante", null, 
    				"Pero... eso significa que resueno en ustedes. Con ustedes."
    				, 20, null);
    		DialMan.addDialogue(23, 24, "Dante", null, 
    				"Si algun dia necesitan mi ayuda... busquen esta luz."
    				, 20, null);
    		DialMan.addDialogue(24, 25, "Dante", null, 
    				"Aquel brillo, tan radiante como opaco..."
    				, 20, null);
    		DialMan.addDialogue(25, 26, "Dante", null, 
    				"En las estrellas que reluzcan con ese fulgor..."
    				, 20, null);
    		DialMan.addDialogue(26, 27, "Dante", null, 
    				"Sigan ese camino... y busquenme."
    				, 20, null);
    		DialMan.addDialogue(27, 28, "Dante", null, 
    				"Porque mi momento de partir ha llegado."
    				, 20, null);
    		DialMan.addDialogue(28, 29, "Dante", null, 
    				"Me llevare al caballo del Jinete."
    				, 20, null);
    		DialMan.addDialogue(29, 30, "Dante", null, 
    				"Porque hay animales que nunca duermen. Al menos, yo no lo he visto."
    				, 20, null);
    		DialMan.addDialogue(30, 100, "Dante", null, 
    				"Nos vemos, camaradas."
    				, 20, null);
    		DialMan.addDialogue(100, -1, "Dante", null, 
    				"y, adelante, Rocinante!"
    				, 20, null);
    	}
    	int resp = DialMan.showDialogues(game, delta);
       	switch(resp) {
       	case(100):
       		Gdx.app.exit();
       		break;
       	}
       	game.batch.end();
    }

	@Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;
        
        // Resize your screen here. The parameters represent the new window size.
        game.viewport.update(width, height);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    	game.lastScreen = this;
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    	game.lastScreen = this;
    }
}