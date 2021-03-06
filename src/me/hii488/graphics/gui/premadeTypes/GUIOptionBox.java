package me.hii488.graphics.gui.premadeTypes;

import java.awt.Graphics;
import java.util.ArrayList;

import me.hii488.dataTypes.KeyBind;
import me.hii488.dataTypes.Vector;
import me.hii488.dataTypes.VectorBox;
import me.hii488.graphics.gui.GUIElement;
import me.hii488.graphics.gui.style.GUIStyle;

public class GUIOptionBox extends GUIElement{
	
	protected int currentSelection = 0;
	protected ArrayList<GUIOption> options = new ArrayList<GUIOption>();
	
	public GUIOptionBox() {}
	public GUIOptionBox(GUIStyle s) {
		setStyle(s, true);
	}
	
	public void show() {
		super.show();
		options.forEach(e-> e.show());
	}
	
	public VectorBox getBoundingBox() {
		int minX = position.getIX(), maxX = position.getIX() + dimensions.getIX();
		int minY = position.getIY(), maxY = position.getIY() + dimensions.getIY();
		
		for(GUIOption option : options) {
			if(option.getPosition().getIX() + position.getIX() < minX) minX = option.getPosition().getIX() + position.getIX();
			if(option.getPosition().getIY() + position.getIY() < minY) minY = option.getPosition().getIY() + position.getIY();
			
			if(option.getDimensions().getIX() + position.getIX() + option.getPosition().getIX() > maxX) maxX = option.getDimensions().getIX() + position.getIX() + option.getPosition().getIX();
			if(option.getDimensions().getIY() + position.getIY() + option.getPosition().getIY() > maxY) maxY = option.getDimensions().getIY() + position.getIY() + option.getPosition().getIY();
		}
		
		return new VectorBox(minX, minY, maxX, maxY);
	}
	
	public void addOption(GUIOption o) {
		options.add(o);
		o.setParentGUISet(getParentGuiSet());
		o.parentBox = this;
		o.setAnchor(this);
	}
	
	public ArrayList<GUIOption> getOptions() {
		return options;
	}
	
	public void positionOptions(boolean vertical) { // TODO: Test this
		if(vertical) {
			Vector offset = new Vector(0,0);
			double yDist = (- options.stream().mapToDouble(o -> (o.getBoundingBox().getHeight())).sum()) / (options.size() + 1);
			
			for(GUIOption option : options) {
				offset.setX((this.getBoundingBox().getWidth() - option.getBoundingBox().getWidth())/2);
				offset.translate(0, yDist);
				
				option.setPosition(position.getCopy().translate(offset));
			}
		}
		else {
			Vector offset = new Vector(0,0);
			double xDist = (- options.stream().mapToDouble(o -> (o.getBoundingBox().getWidth())).sum()) / (options.size() + 1);
			
			for(GUIOption option : options) {
				offset.setY((this.getBoundingBox().getHeight() - option.getBoundingBox().getHeight())/2);
				offset.translate(xDist, 0);
				
				option.setPosition(position.getCopy().translate(offset));
			}
		}
	}
	
	@Override
	public void onKeyTyped(KeyBind e){

		if (e.equals(KeyBind.MOVE_RIGHT) || e.equals(KeyBind.MOVE_DOWN))
			currentSelection++;
		
		else if (e.equals(KeyBind.MOVE_LEFT) || e.equals(KeyBind.MOVE_UP))
			currentSelection--;
		
		currentSelection %= options.size();
		
		if (currentSelection < 0)
			currentSelection += options.size();
		
		if(e.equals(KeyBind.INTERACT))
			options.get(currentSelection).onSelect();

	}
	
	public void setSelected(int selection) {
		currentSelection = selection;
	}

	@Override
	public void render(Graphics g) {
		options.forEach(o -> o.render(g));
	}
	
}
