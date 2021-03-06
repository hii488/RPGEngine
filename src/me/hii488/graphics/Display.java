package me.hii488.graphics;

import java.awt.Canvas;
import java.awt.Graphics;

import me.hii488.EngineSettings;
import me.hii488.handlers.LevelHandler;
import me.hii488.registries.TextureRegistry;

@SuppressWarnings("serial")
public class Display extends Canvas{

	public Display(Window window) {
		setBounds(0, 0, window.width, window.height);
	}
	
	public void render(Graphics g){
		g.translate(-Camera.getCamPosition().getIX(), -Camera.getCamPosition().getIY());
		g.setColor(EngineSettings.Texture.background);
		g.fillRect(Camera.getCamPosition().getIX(), Camera.getCamPosition().getIY(), getWidth(), getHeight());
		try{
			LevelHandler.getCurrentLevel().render(g);
			TextureRegistry.afterRender();
		}
		catch(Exception e){
			System.err.println("Error rendering current level:");
			e.printStackTrace();
		}
	}
	
}
