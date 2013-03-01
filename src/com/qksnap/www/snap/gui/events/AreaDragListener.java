package com.qksnap.www.snap.gui.events;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.qksnap.www.snap.gui.RectGlassPane;
import com.qksnap.www.snap.gui.SelectGlassPane;
import com.qksnap.www.snap.gui.SplashScreen;
import com.sun.awt.AWTUtilities;

@SuppressWarnings("restriction")
public class AreaDragListener extends MouseAdapter {
	private boolean isRect;
	private Dimension screenSize;
	public AreaDragListener(boolean isRect){
		this.isRect=isRect;
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	}

	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		// our start coords
		if(!this.isRect){
			SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().setStart(e.getPoint());
			// We are drawing the rectangle
			SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().setDrawing(true);
			return;
		} 
		SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().setStart(e.getPoint());
		// We are drawing the rectangle
		SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().setDrawing(true);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e);
		// check for left mouse button
		if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) == 0) {
			return;
		}
		if(this.isRect){
			// set the end point to our mouse point
			SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().setFinish(e.getPoint());
			// calculate the size of the selection rectangle
			int x = Math.min(SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().start.x, SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().down.x);
			int y = Math.min(SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().start.y, SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().down.y);
			int width = Math.abs(SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().start.x - SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().down.x);
			int height = Math.abs(SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().start.y - SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().down.y);
			// draw the rectangle
			SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().setRect(new Rectangle2D.Double(x, y, width, height));
			// repaint the panel every drag
			SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().repaint();
		} else {
			// set the end point to our mouse point
			SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().setFinish(e.getPoint());
			// calculate the size of the selection rectangle
			int x = Math.min(SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().start.x, SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().down.x);
			int y = Math.min(SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().start.y, SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().down.y);
			int width = Math.abs(SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().start.x - SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().down.x);
			int height = Math.abs(SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().start.y - SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().down.y);
			// draw the rectangle
			SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().setRect(new Rectangle2D.Double(x, y, width, height));
			// repaint the panel every drag
			SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//System.out.println(SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane());
		super.mouseReleased(e);
		if(!this.isRect){
			int x = Math.min(SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().start.x, SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().down.x);
			int y = Math.min(SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().start.y, SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().down.y);
			int width = Math.abs(SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().start.x - SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().down.x);
			int height = Math.abs(SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().start.y - SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().down.y);
			SplashScreen.getSingleton().getMainFrame().getNRFrame().setVisible(false);
			SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().repaint();
			try {
				Thread.sleep(500); // sleep for half a second
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			//select our location 
			SplashScreen.getSingleton().getMainFrame().setRect(new Rectangle(x, y+20, width+1, height+1));
			SplashScreen.getSingleton().getMainFrame().getNRFrame().dispose();
			SplashScreen.getSingleton().getMainFrame().getTrayIcon().displayMessage("QwikSnap", "Your pre-defined snap region has been saved.\nPress ("+SplashScreen.getSingleton().getUserSettings().getSnapHK() +") to take a picture.", TrayIcon.MessageType.INFO);
			SplashScreen.getSingleton().getMainFrame().getNRFrame().getSelectGlassPane().setRect(null);
			SplashScreen.getSingleton().getMainFrame().setIsDrawingAlready(false);
	
		} else {
			int x = Math.min(SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().start.x, SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().down.x);
			int y = Math.min(SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().start.y, SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().down.y);
			int width = Math.abs(SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().start.x - SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().down.x);
			int height = Math.abs(SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().start.y - SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().down.y);
			SplashScreen.getSingleton().getMainFrame().sendRectScreen(new Rectangle(x, y, width, height));
			SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().repaint();
			try {
				Thread.sleep(500); // sleep for half a second
			} catch (InterruptedException e1) {
			}
			SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().needUpdate = true;
			AWTUtilities.setWindowOpaque(SplashScreen.getSingleton().getMainFrame().getNRFrame(), false);
			SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().setRect(null);
			SplashScreen.getSingleton().getMainFrame().getNRFrame().dispose();
			SplashScreen.getSingleton().getMainFrame().setIsDrawingAlready(false);
			SplashScreen.getSingleton().getMainFrame().getNRFrame().getSnapGlassPane().repaint();
		}
	}
}
