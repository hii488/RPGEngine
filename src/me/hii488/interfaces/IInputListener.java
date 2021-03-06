package me.hii488.interfaces;

import java.awt.event.MouseEvent;

import me.hii488.dataTypes.KeyBind;
import me.hii488.dataTypes.Vector;

public interface IInputListener {
	
	// The default keyword is used to avoid bloating classes which do not need the methods.
	public default void keyPressed(KeyBind arg0) {}
	public default void keyReleased(KeyBind arg0) {}
	public default void keyTyped(KeyBind arg0) {}
	
	public default boolean mouseClicked(MouseEvent arg0) {return false;}
	public default boolean mouseClicked(MouseEvent arg0, Vector ingameLocation, boolean b) {return mouseClicked(arg0);}
	public default void mouseEntered(MouseEvent arg0) {}
	public default void mouseExited(MouseEvent arg0) {}
	public default void mousePressed(MouseEvent arg0, Vector ingameLocation) {}
	public default void mouseReleased(MouseEvent arg0, Vector ingameLocation) {}
	
	public default void mouseDragged(MouseEvent arg0) {}
	public default void mouseMoved(MouseEvent arg0) {}
	
}
