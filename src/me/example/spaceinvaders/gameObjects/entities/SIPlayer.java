package me.example.spaceinvaders.gameObjects.entities;

import java.awt.Graphics;

import me.hii488.gameObjects.entities.FreeEntity;
import me.hii488.interfaces.IInputListener;

public class SIPlayer extends FreeEntity implements IInputListener{

	@Override
	public String getTextureKey() {
		return null;
	}

	@Override
	public String getTextureLocation() {
		return null;
	}

	@Override
	public int getTextureState() {
		return 0;
	}

	@Override
	public int getHighestState() {
		return 0;
	}

	@Override
	public void render(Graphics g) {
		
	}

	@Override
	public void onLoad() {
		
	}

	@Override
	public void onUnload() {
		
	}

}