package me.hii488.graphics.gui.premadeTypes;

import me.hii488.graphics.gui.GUISet;
import me.hii488.graphics.gui.style.GUIStyle;

public class GUIOption extends GUIStandardBox {

	protected GUIOptionBox parentBox;
	
	public GUIOption() {super();}
	public GUIOption(GUIStyle s) {
		super(s);
	}
	
	public void onSelect() {}
	
	protected void setParentGUISet(GUISet s) {
		this.parentGuiSet = s;
	}
	
}