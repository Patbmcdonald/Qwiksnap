package com.qksnap.www.snap.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.qksnap.www.snap.gui.SelectGlassPane;
import com.sun.awt.AWTUtilities;
import com.sun.jna.Native;
import com.sun.jna.PointerType;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser.POINT;
import com.sun.jna.win32.StdCallLibrary;
/**
 * 	 Our full screen frame to allow rectangle drawing on a glass pane
 *  @author Patrick "zeroeh"
 *
 */
public class GlassFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private Component mprevGlassPane = null; 
	private ImageOverlayGlassPane snapGlassPane = null;
	private SelectGlassPane selectGlassPane = null;
	private static GlassFrame instance;
	private int width;
	private int hieght;

	public GlassFrame() {
		/** full screen please **/
		int width = 0;
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		int height = 0;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();      
		GraphicsDevice[] gs = ge.getScreenDevices(); 
		for (GraphicsDevice curGs : gs) { 
			DisplayMode mode = curGs.getDisplayMode();  
			width += mode.getWidth();
			height = mode.getHeight(); 
		} 
		Dimension dim  = new Dimension(width,height);
		this.setSize(dim);
	    this.setBounds(0,0,width, height);
		this.setBackground(new Color(53,53,53));
		this.requestFocusInWindow();
		this.addKeyListener(new KeyListener(){

			public void keyPressed( KeyEvent e )
		     {
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
						// set this window to opaque
		
						SplashScreen.getSingleton().getMainFrame().setIsDrawingAlready(false);
						setVisible(false);
						GlassFrame.getInstance().getSnapGlassPane().needUpdate = true;
						GlassFrame.getInstance().getSnapGlassPane().setRect(null);
						GlassFrame.getInstance().dispose();
						SplashScreen.getSingleton().getMainFrame().setIsDrawingAlready(false);
						GlassFrame.getInstance().getSnapGlassPane().repaint();
					}
					
		    }
		     
		     public void keyReleased( KeyEvent e )
		     {
		   
		     }
		     public void keyTyped( KeyEvent e )
		     {
		       
		     }
		});
		snapGlassPane = new ImageOverlayGlassPane();
		selectGlassPane = new SelectGlassPane();
		this.setUndecorated(true);
		setResizable(false);
		
	}
	/**  Set up our window to draw on **/
	public void setUpWindow(boolean rect){
		if(rect){
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			setGlassPane(snapGlassPane);
			getGlassPane().setVisible(true);
			this.setAlwaysOnTop(true);
			AWTUtilities.setWindowOpaque(GlassFrame.this, true);
			this.setUndecorated(true);
			setResizable(false);
			dispose();
		} else {
		//	mprevGlassPane = getGlassPane();
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			setGlassPane(selectGlassPane);
			getGlassPane().setVisible(true);
			this.setAlwaysOnTop(true);
			AWTUtilities.setWindowOpaque(GlassFrame.this, true);
			this.setUndecorated(true);
			setResizable(false);
			dispose();
			
		}
	}
	
	public Component getOldGlass(){
		return mprevGlassPane;
	}
	public ImageOverlayGlassPane getSnapGlassPane() {
		return snapGlassPane;
	}
	public SelectGlassPane getSelectGlassPane() {
		return selectGlassPane;
	}
	public int getVWidth() {
		return width;
	}
	public int getVHeight() {
		return hieght;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHieght(int hieght) {
		this.hieght = hieght;
	}
	
	public static GlassFrame getInstance(){
		if(instance != null)
			return instance;
		instance = new GlassFrame();
		return instance;
	}
}