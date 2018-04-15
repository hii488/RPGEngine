package me.hii488.gameObjects.levels;

import java.awt.Graphics;
import java.util.ArrayList;

import me.hii488.dataTypes.Grid;
import me.hii488.gameObjects.entities.BaseEntity;
import me.hii488.gameObjects.entities.FreeEntity;
import me.hii488.gameObjects.entities.GridEntity;
import me.hii488.gameObjects.tiles.BaseTile;
import me.hii488.interfaces.IGameObject;
import me.hii488.interfaces.IRenderable;
import me.hii488.interfaces.ITickable;
import me.hii488.interfaces.ITicking;

public abstract class BaseLevel implements ITicking, IGameObject, IRenderable{
	
	// GRID
	private Grid<BaseTile> tileGrid;
	
	// ENTITY GRID - TODO: determine if this is really necessary, the constraints could be in GridEntity itself?
	private Grid<GridEntity> entityGrid;
	
	// FREE FLOATING ENTITIES
	private ArrayList<FreeEntity> entities;
	private ArrayList<FreeEntity> entitiesToAdd;
	private ArrayList<FreeEntity> entitiesToDelete;
	
	public BaseLevel() {
		tileGrid = new Grid<BaseTile>();
		entityGrid = new Grid<GridEntity>();
		
		entities = new ArrayList<FreeEntity>();
		entitiesToAdd = new ArrayList<FreeEntity>();
		entitiesToDelete = new ArrayList<FreeEntity>();
	}
	
	@Override
	public void onLoad() {
		tileGrid.onLoad();
		entityGrid.onLoad();
		entities.forEach(e -> e.onLoad());
	}
	
	@Override
	public void onUnload() {
		tileGrid.onUnload();
		entityGrid.onUnload();
		entities.forEach(e -> e.onUnload());
	}
	
	@Override
	public void updateOnTick() {
		tileGrid.updateOnTick();
		entityGrid.updateOnTick();
		entities.forEach(e -> {if(e instanceof ITickable) ((ITicking) e).updateOnTick();});
	}
	
	@Override
	public void updateOnSec() {
		tileGrid.updateOnSec();
		entityGrid.updateOnSec();
		entities.forEach(e -> {if(e instanceof ITickable) ((ITicking) e).updateOnSec();});
	}
	
	@Override
	public void endOfTick() {
		tileGrid.endOfTick();
		entityGrid.endOfTick();
		entities.forEach(e -> {if(e instanceof ITickable) ((ITicking) e).endOfTick();});
		
		entities.removeAll(entitiesToDelete);
		entities.addAll(entitiesToAdd);
		
		entitiesToAdd.clear();
		entitiesToDelete.clear();
	}
	
	public void addEntity(BaseEntity e) {
		if(e instanceof FreeEntity)	entitiesToAdd.add((FreeEntity) e);
		else entityGrid.setObjectAt((GridEntity) e, ((GridEntity) e).getPosition());
	}
	
	public void removeEntity(BaseEntity e) {
		if(e instanceof FreeEntity)	entitiesToDelete.add((FreeEntity) e);
		else entityGrid.setObjectAt((GridEntity) e, ((GridEntity) e).getPosition());
	}
	
	public void render(Graphics g) {
		tileGrid.render(g);
		entityGrid.render(g);
	}
	

	public Grid<BaseTile> getTileGrid() {
		return tileGrid;
	}

	public Grid<GridEntity> getEntityGrid() {
		return entityGrid;
	}

	public ArrayList<FreeEntity> getFreeEntities() {
		return entities;
	}
	
}
