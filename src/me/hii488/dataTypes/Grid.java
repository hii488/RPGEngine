package me.hii488.dataTypes;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import me.hii488.controllers.GameController;
import me.hii488.gameObjects.levels.BaseLevel;
import me.hii488.interfaces.IGameObject;
import me.hii488.interfaces.IRenderable;
import me.hii488.interfaces.ITickable;
import me.hii488.logging.LogSeverity;
import me.hii488.logging.Logger;

public class Grid<T extends IGridObject> implements ITickable, IGameObject, IRenderable{
	
	// TODO: Might need to change this as I may need multiple T's in the same location
	// TODO: Maybe have both <T,Vector> and <Vector, T> ? then we have an easy way of finding either from the other?
	private Map<Vector, T> map, additionMap, deletionMap;
	private Vector dimensions;
	private int gridScale;
	
	private BaseLevel parentLevel;
	
	public Grid() {
		dimensions = new Vector(0,0);
		map = new HashMap<Vector, T>();
		additionMap = new HashMap<Vector, T>();
		deletionMap = new HashMap<Vector, T>();
		gridScale = -1;
	}
	
	public Grid(Grid<T> g) {
		this.dimensions = g.dimensions;
				
		g.map.forEach((v, t) -> map.put(v.getIV(), t));
	}
	
	public Grid(int x, int y) {
		this();
		setDimensions(x, y);
	}
	
	// Works out scale automatically based on window size and dimensions
	public void autoSetup(int width, int height) {
		setDimensions(width, height);
		setGridScale((GameController.getWindow().width/width));
	}
	
	// Works out width/height based on window size and scale
	public void autoSetup(int scale) {
		setGridScale(scale);
		setDimensions(GameController.getWindow().width/scale, GameController.getWindow().height/scale);
	}
	
	public void setDimensions(int size) {
		setDimensions(size, size);
	}
	
	public void setDimensions(int sizeX, int sizeY) {
		dimensions.setX(sizeX); dimensions.setY(sizeY);
	}
	
	public Vector getDimensions() {
		return dimensions.getCopy();
	}

	@Override
	public void updateOnTick() {
		map.values().forEach(t -> {if(t instanceof ITickable) ((ITickable) t).updateOnTick();});
	}

	@Override
	public void updateOnSec() {
		map.values().forEach(t -> {if(t instanceof ITickable) ((ITickable) t).updateOnSec();});
	}
	
	@Override
	public void endOfTick() {
		deletionMap.entrySet().stream().forEach(e -> map.remove(e.getKey(), e.getValue()));
		deletionMap.clear();
		
		additionMap.entrySet().stream().forEach(e -> map.put(e.getKey(), e.getValue()));
		additionMap.clear();
	}

	@Override
	public void onLoad() {
		map.values().forEach(t -> {if(t instanceof IGameObject) ((IGameObject) t).onLoad();});
	}

	@Override
	public void onUnload() {
		map.values().forEach(t -> {if(t instanceof IGameObject) ((IGameObject) t).onUnload();});
	}
	
	public boolean hasObjectAt(int x, int y) {
		return hasObjectAt(new Vector(x,y));
	}
	
	public boolean hasObjectAt(Vector v) {
		return map.containsKey(v);
	}
	
	public boolean hasObject(T t) {
		return map.containsValue(t);
	}
	
	public T getObjectAt(int x, int y) {
		return getObjectAt(new Vector(x,y));
	}
	
	public T getObjectAt(Vector v) {
		return map.get(v);
	}
	
	public T getObjectAtRealPosition(Vector v) {
		return map.get(v.getCopy().scale(1D/this.gridScale).getIV());
	}
	
	public T getObjectAtRealPosition(int x, int y) {
		return getObjectAtRealPosition(new Vector(x, y));
	}
	
	public Vector getGridVectorFromRealPosition(Vector v) {
		return v.getCopy().scale(1D/this.gridScale).getIV();
	}
	
	public Vector getRealPositionFromGridVector(Vector v) {
		return v.getCopy().scale(this.gridScale).getIV();
	}
	
	public void setObjectAt(int x, int y, T t) {
		setObjectAt(new Vector(x,y), t);
	}
	
	public void setObjectAt(Vector v, T t) {
		if(hasObjectAt(v)) getObjectAt(v).onReplace();
		silentSetObjectAt(v, t);
		t.onPlace();
	}
	
	private void silentSetObjectAt(Vector v, T t) {
		t.setParentGrid(this);
		additionMap.put(v.getIV(), t);
	}
	
	public void removeObject(T t) {
		stream().forEach(e -> {if(e.getValue().equals(t)) {
			deletionMap.put(e.getKey(), e.getValue());
			e.getValue().onRemove();
		}});
	}
	
	// This is the same as removeObject, but does not call onRemove() for the object.
	private void silentRemoveObject(T t) {
		stream().forEach(e -> {if(e.getValue().equals(t)) deletionMap.put(e.getKey(), e.getValue());});
	}
	
	public void removeObject(Vector v) {
		stream().forEach(e -> {if(e.getKey().equals(v)) deletionMap.put(e.getKey(), e.getValue());});
	}
	
	public void moveObject(T t, Vector v) {
		t.onMove();
		silentRemoveObject(t);
		
		if(hasObjectAt(v)) getObjectAt(v).onReplace();
		silentSetObjectAt(v,t);
	}
	
	public void fillDimensionsWith(int x1, int y1, int x2, int y2, Class<? extends T> c) {
		int nx1, nx2, ny1, ny2;
		if(x1 < x2) {
			nx1 = x1;
			nx2 = x2;
		}
		else {
			nx1 = x2;
			nx2 = x1;
		}
		
		if(y1 < y2) {
			ny1 = y1;
			ny2 = y2;
		}
		else {
			ny1 = y2;
			ny2 = y1;
		}
		
		for(int i = nx1; i < nx2; i++) {
			for(int j = ny1; j < ny2; j++) {
				try {
					setObjectAt(new Vector(i,j), c.newInstance());
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void fillDimensionsWith(Vector a, Vector b, Class<? extends T> c) {
		fillDimensionsWith(a.getIX(), a.getIY(), b.getIX(), b.getIY(), c);
	}
	
	@SuppressWarnings("unchecked")
	public void fillDimensionsWith(int x1, int y1, int x2, int y2, T c) {
		fillDimensionsWith(x1, y1, x2, y2, (Class<? extends T>) c.getClass());
	}
	
	public void fillDimensionsWith(Vector a, Vector b, T c) {
		fillDimensionsWith(a.getIX(), a.getIY(), b.getIX(), b.getIY(), c);
	}
	
	public void wallDimensionsWith(int x1, int y1, int x2, int y2, Class<? extends T> c) {
		int nx1, nx2, ny1, ny2;
		if(x1 < x2) {
			nx1 = x1;
			nx2 = x2;
		}
		else {
			nx1 = x2;
			nx2 = x1;
		}
		
		if(y1 < y2) {
			ny1 = y1;
			ny2 = y2;
		}
		else {
			ny1 = y2;
			ny2 = y1;
		}
		
		for(int i = nx1; i < nx2; i++) {
			try {
				setObjectAt(new Vector(i,ny1), c.newInstance());
				setObjectAt(new Vector(i,ny2), c.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		
		}

		for(int j = ny1; j < ny2; j++) {
			try {
				setObjectAt(new Vector(nx1,j), c.newInstance());
				setObjectAt(new Vector(nx2,j), c.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void wallDimensionsWith(Vector a, Vector b, Class<? extends T> c) {
		wallDimensionsWith(a.getIX(), a.getIY(), b.getIX(), b.getIY(), c);
	}
	
	@SuppressWarnings("unchecked")
	public void wallDimensionsWith(int x1, int y1, int x2, int y2, T c) {
		wallDimensionsWith(x1, y1, x2, y2, (Class<? extends T>) c.getClass());
	}
	
	public void wallDimensionsWith(Vector a, Vector b, T c) {
		wallDimensionsWith(a.getIX(), a.getIY(), b.getIX(), b.getIY(), c);
	}
	
	public void clear() {
		this.map.clear();
		this.additionMap.clear();
	}
	
	public void markToClear() {
		this.additionMap.clear();
	}

	@Override
	public void render(Graphics g) {
		map.entrySet().stream().forEach(e -> {if(e.getValue() instanceof IRenderable) ((IRenderable) e.getValue()).render(g, e.getKey().getIV().scale(gridScale));});
	}
	
	public Stream<Entry<Vector, T>> stream() {
		return map.entrySet().stream();
	}
	
	public Stream<Entry<Vector, T>> streamUpdates() {
		return additionMap.entrySet().stream();
	}

	// TODO: Currently is unsafe, change so it throws an error properly or something
	public Vector getPositionOf(T t) {
		if(!hasObject(t)) throw new RuntimeException("Object not found within the grid.");
		return stream().filter(entry -> entry.getValue() == t).findFirst().get().getKey();
	}

	public int getGridScale() {
		return gridScale;
	}
	
	public void setGridScale(int scale) {
		if(gridScale == -1)
			gridScale = scale;
		else 
			Logger.getDefault().print(LogSeverity.MESSAGE, "Grid scale already set, it cannot be overriden.");
	}
	
	public int getWidth() {
		return dimensions.getIX();
	}
	
	public int getHeight() {
		return dimensions.getIY();
	}
	
	public BaseLevel getParentLevel() {
		return parentLevel;
	}
	
	public void setLevel(BaseLevel l) {
		parentLevel = l;
	}

}
