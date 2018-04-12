package me.hii488.graphics;

import java.awt.Canvas;
import java.awt.Graphics;

import me.hii488.EngineSettings;

@SuppressWarnings("serial")
public class Display extends Canvas{

	public Display(Window window) {
		setBounds(0, 0, window.width, window.height);
	}
	
	public void render(Graphics g){
		g.setColor(EngineSettings.Texture.background);
		g.fillRect(0, 0, getWidth(), getHeight());
		try{
			//ContainerHandler.getRenderContainer().render(g);
		}
		catch(Exception e){
			System.err.println("Error rendering current world container:");
			e.printStackTrace();
		}
	}
	
}
