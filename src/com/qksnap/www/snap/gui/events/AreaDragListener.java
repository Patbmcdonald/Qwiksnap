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

import com.qksnap.www.snap.gui.GlassFrame;
import com.qksnap.www.snap.gui.ImageOverlayGlassPane;
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
			GlassFrame.getInstance().getSelectGlassPane().setStart(e.getPoint());
			// We are drawing the rectangle
			GlassFrame.getInstance().getSelectGlassPane().setDrawing(true);
			return;
		} 
		GlassFrame.getInstance().getSnapGlassPane().setStart(e.getPoint());
		// We are drawing the rectangle
		GlassFrame.getInstance().getSnapGlassPane().setDrawing(true);
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
			GlassFrame.getInstance().getSnapGlassPane().setFinish(e.getPoint());
			// calculate the size of the selection rectangle
			int x = Math.min(GlassFrame.getInstance().getSnapGlassPane().start.x, GlassFrame.getInstance().getSnapGlassPane().down.x);
			int y = Math.min(GlassFrame.getInstance().getSnapGlassPane().start.y, GlassFrame.getInstance().getSnapGlassPane().down.y);
			int width = Math.abs(GlassFrame.getInstance().getSnapGlassPane().start.x - GlassFrame.getInstance().getSnapGlassPane().down.x);
			int height = Math.abs(GlassFrame.getInstance().getSnapGlassPane().start.y - GlassFrame.getInstance().getSnapGlassPane().down.y);
			// draw the rectangle
			GlassFrame.getInstance().getSnapGlassPane().setRect(new Rectangle2D.Double(x, y, width, height));
			// repaint the panel every drag
			GlassFrame.getInstance().getSnapGlassPane().repaint();
		} else {
			// set the end point to our mouse point
			GlassFrame.getInstance().getSelectGlassPane().setFinish(e.getPoint());
			// calculate the size of the selection rectangle
			int x = Math.min(GlassFrame.getInstance().getSelectGlassPane().start.x, GlassFrame.getInstance().getSelectGlassPane().down.x);
			int y = Math.min(GlassFrame.getInstance().getSelectGlassPane().start.y, GlassFrame.getInstance().getSelectGlassPane().down.y);
			int width = Math.abs(GlassFrame.getInstance().getSelectGlassPane().start.x - GlassFrame.getInstance().getSelectGlassPane().down.x);
			int height = Math.abs(GlassFrame.getInstance().getSelectGlassPane().start.y - GlassFrame.getInstance().getSelectGlassPane().down.y);
			// draw the rectangle
			GlassFrame.getInstance().getSelectGlassPane().setRect(new Rectangle2D.Double(x, y, width, height));
			// repaint the panel every drag
			GlassFrame.getInstance().getSelectGlassPane().repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//System.out.println(GlassFrame.getInstance().getSnapGlassPane());
		super.mouseReleased(e);
		if(!this.isRect){
			
			int x = Math.min(GlassFrame.getInstance().getSelectGlassPane().start.x, GlassFrame.getInstance().getSelectGlassPane().down.x);
			int y = Math.min(GlassFrame.getInstance().getSelectGlassPane().start.y, GlassFrame.getInstance().getSelectGlassPane().down.y);
			int width = Math.abs(GlassFrame.getInstance().getSelectGlassPane().start.x - GlassFrame.getInstance().getSelectGlassPane().down.x);
			int height = Math.abs(GlassFrame.getInstance().getSelectGlassPane().start.y - GlassFrame.getInstance().getSelectGlassPane().down.y);
			GlassFrame.getInstance().setVisible(false);
			GlassFrame.getInstance().getSelectGlassPane().repaint();
			try {
				Thread.sleep(500); // sleep for half a second
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			//select our location 
			SplashScreen.getSingleton().getMainFrame().setRect(new Rectangle(x, y+20, width+1, height+1));
			GlassFrame.getInstance().dispose();
			SplashScreen.getSingleton().getMainFrame().getTrayIcon().displayMessage("QwikSnap", "Your pre-defined snap region has been saved.\nPress ("+SplashScreen.getSingleton().getUserSettings().getSnapHK() +") to take a picture.", TrayIcon.MessageType.INFO);
			GlassFrame.getInstance().getSelectGlassPane().setRect(null);
			SplashScreen.getSingleton().getMainFrame().setIsDrawingAlready(false);
	
		} else {
			
			int x = Math.min(GlassFrame.getInstance().getSnapGlassPane().start.x, GlassFrame.getInstance().getSnapGlassPane().down.x);
			int y = Math.min(GlassFrame.getInstance().getSnapGlassPane().start.y, GlassFrame.getInstance().getSnapGlassPane().down.y);
			int width = Math.abs(GlassFrame.getInstance().getSnapGlassPane().start.x - GlassFrame.getInstance().getSnapGlassPane().down.x);
			int height = Math.abs(GlassFrame.getInstance().getSnapGlassPane().start.y - GlassFrame.getInstance().getSnapGlassPane().down.y);
			SplashScreen.getSingleton().getMainFrame().sendRectScreen(new Rectangle(x, y, width, height));
			GlassFrame.getInstance().getSnapGlassPane().repaint();
			try {
				Thread.sleep(500); // sleep for half a second
			} catch (InterruptedException e1) {
			}
			GlassFrame.getInstance().getSnapGlassPane().needUpdate = true;
			AWTUtilities.setWindowOpaque(GlassFrame.getInstance(), false);
			GlassFrame.getInstance().getSnapGlassPane().setRect(null);
			GlassFrame.getInstance().dispose();
			SplashScreen.getSingleton().getMainFrame().setIsDrawingAlready(false);
			GlassFrame.getInstance().getSnapGlassPane().repaint();
		}
	}
}
