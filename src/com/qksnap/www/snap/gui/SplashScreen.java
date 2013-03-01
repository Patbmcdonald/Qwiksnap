package com.qksnap.www.snap.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.qksnap.www.snap.autoupdater.Updater;
import com.qksnap.www.snap.settings.UserSettings;
import com.qksnap.www.snap.util.Logger;
import com.qksnap.www.snap.util.Logger.Level;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

public class SplashScreen extends JWindow {

	/**
	 * Splash screen duration (milliseconds).
	 */

	private static SplashScreen singleton;
	
	public static void setSingleton(SplashScreen singleton) {
		if (SplashScreen.singleton != null) {
			throw new IllegalStateException("Singleton already set!");
		}
		SplashScreen.singleton = singleton;
	}

	/**
	 * Gets the server singleton object.
	 * 
	 * @return the singleton
	 */
	public static SplashScreen getSingleton() {
		return singleton;
	}

	private int splashDuration = 1500;
	
	/** Progress Bar **/
	private JProgressBar progressBar;
	
	/** Description Lbl **/
	private JLabel descLbl;

	/** static instances **/
	public MainFrame mainframe;
	
	public MainFrame getMainFrame(){
		return mainframe;
	}
	public UserSettings usersettings;
	
	public UserSettings getUserSettings(){
		return usersettings;
	}
	public void showTos() {
			SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
			      // Here, we can safely update the GUI
			      // because we'll be called from the
			      // event dispatch thread
			usersettings = new UserSettings();
			try {
				mainframe = new MainFrame();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			    }
			});

	}

	/** Create Splash Screen **/
	public void createSplash(){
		JPanel content = (JPanel) getContentPane();
		
		/** Background Image **/
		Image img = new ImageIcon(getClass().getResource("/splashbg.png")).getImage();
		
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2;
		setBounds(x, y, width, height);
		
		JLabel label = new JLabel(new ImageIcon(getClass().getResource("/splashbg.png")));
		JLabel logo = new JLabel(new ImageIcon(getClass().getResource("/logotext.png")));
		JLabel progressBarBg = new JLabel(new ImageIcon(getClass().getResource("/progressbarplaceholder.png")));
		progressBarBg.setBounds(82, 175, 422, 40);
		logo.setBounds(165,60,241,55);
		label.setOpaque(false);
		/** Some Progress Bar editting **/
		progressBar = new JProgressBar();
        progressBar.setValue(0);
        UIManager.put("ProgressBar.selectionForeground", Color.white);
        UIManager.put("ProgressBar.selectionBackground", Color.white);
        progressBar.setUI(new com.qksnap.www.snap.gui.tabpane.componet.ProgressBar());
        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.white);
        progressBar.setFont(new Font("Tahoma", Font.PLAIN, 10).deriveFont(Font.BOLD));
        progressBar.setOpaque(false);
        progressBar.setBorderPainted(false);
        progressBar.setBackground(Color.black);
        progressBar.setBounds(82,175, 422, 49);
        /** DescLbl **/
        descLbl = new JLabel("Loading Gui Elements");
        descLbl.setForeground(Color.white);
		content.setOpaque(false);
        descLbl.setFont(new Font("Tahoma", Font.PLAIN, 10).deriveFont(Font.BOLD));
        descLbl.setBounds(245, 215, 512, 32);
        content.add(descLbl);
        content.add(progressBar);
		content.add(logo);
		content.add(progressBarBg);
		content.add(label, BorderLayout.CENTER);
	}
	/** Update our progress bar **/
    public void drawText(int i, String string) {
    	getSingleton().getProgressBar().setValue(i);
    	getSingleton().getDescLbl().setText(string);
    	getSingleton().validate();
    	getSingleton().repaint();
	}
	public void showSplash() {
		createSplash();
		setVisible(true);
		getProgressBar().setValue(100); 
		repaint();
		validate();
		// Check for Update
		Logger.writeLog(Level.INFORMATION, "Preparing to check for update.");
		drawText(0,"Preparing to check for updates.");
		new Updater();
		// Load sqlite settings
		Logger.writeLog(Level.INFORMATION, "Preparing to load local settings.");
		drawText(0,"Preparing to load local settings.");
		usersettings = new UserSettings();
		// Load saved images 
		try {
			drawText(0,"Preparing to load saved images.");
			mainframe = new MainFrame();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Logger.writeLog(Level.INFORMATION, "Preparing to load saved images.");
		// load qwiksnap
		Logger.writeLog(Level.INFORMATION, "Starting QwikSnap...");
		drawText(0,"Starting QwikSnap...");
		try {
			Thread.sleep(splashDuration);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		drawText(100,"Starting QwikSnap...");
		setVisible(false);
		getSingleton().getMainFrame().setVisible(true);
		
	}
	
	
	public static void main(String[] args) {
		//Launcher splash = new Launcher();
		try {
		     if(System.getProperty("os.name").startsWith("Win"))
                 System.setProperty("sun.java2d.d3d","true");
		     else
                 System.setProperty("sun.java2d.opengl", "true");
         
			ServerSocket ss = new ServerSocket(8000);
		setSingleton(new SplashScreen());    
				  getSingleton().showSplash();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"You already have a instance of Qwiksnap running!", "Error 0",
					JOptionPane.ERROR_MESSAGE);
			System.exit(4);
			return;
		}
	}
	
	public JProgressBar getProgressBar() {
		return progressBar;
	}
	public JLabel getDescLbl() {
		return descLbl;
	}
	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}
	public void setDescLbl(JLabel descLbl) {
		this.descLbl = descLbl;
	}

}