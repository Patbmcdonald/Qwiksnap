package com.qksnap.www.snap.gui.tabpane;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.qksnap.www.snap.gui.SplashScreen;

public class MainPane extends JPanel{
	private JLabel Label;
	private JLabel Label2;
	private JLabel Label3;
	private Image img;
	private Image capimg;
	private JButton caBtn;
	private JButton saBtn;
	private JButton daBtn;
	protected void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
		g.drawImage(capimg,75,15,null);
		long start = System.currentTimeMillis();
		try {
              // Delay depending on how far behind current time we are.
              start += (1000/30);
              Thread.sleep(Math.max(0, start - System.currentTimeMillis()));
          } catch(Exception e){
        	  e.printStackTrace();
          }
	}
	public MainPane(){
		try {
			img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/mpbg.png"));
			capimg = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/capturetxt.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		repaint();
		setLayout(null);
		Label = new JLabel("<html><u>"+SplashScreen.getSingleton().getUserSettings().getSnapHK()+" - Area Screen Snap</u></html>");
		Label2 = new JLabel("<html><u>"+SplashScreen.getSingleton().getUserSettings().getSelHK()+" - Area Selection for future Screen Snap</u></html>");
		Label3 = new JLabel("<html><u>"+SplashScreen.getSingleton().getUserSettings().getSelsnapHK()+" - Free-Form Selection and Screen Snap</u></html>");
		caBtn = new JButton(new ImageIcon(getClass().getResource("/cuareabtn.png")));
		caBtn.setContentAreaFilled(false);
		caBtn.setBorderPainted(false);
		 final String html =
	                "<html><body>" +
	                "<p><b>Select Pre-Defined Region</b>:<br>" +
	                "<b>This feature is used for defining a screenshot region from the Pre-Defined Screenshot Button.<br>" +
	                "Usage: Select the area on your screen you wish to use for screenshots</html>";
	    caBtn.setToolTipText(html);
		caBtn.setFocusPainted(false);
		caBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!SplashScreen.getSingleton().getMainFrame().isDrawingAlready())
					SplashScreen.getSingleton().getMainFrame().setState(SplashScreen.getSingleton().getMainFrame().ICONIFIED);
					SplashScreen.getSingleton().getMainFrame().nrF.setUpWindow(false);
					SplashScreen.getSingleton().getMainFrame().nrF.setVisible(true);
					SplashScreen.getSingleton().getMainFrame().setIsDrawingAlready(true);
			}

		});
	
		caBtn.setBounds(240, 40, 73, 88);
		 final String html2 =
	                "<html><body>" +
	                "<p><b>Select Pre-Defined Screenshot</b>:<br>" +
	                "<b>This feature is used for rapid screenshot snapping. You do not need to define a screenshot region<br>"+
	                "<b>as it is pre-defined using the Pre-Defined Select Area button.<br>" +
	                "Usage: Are you not moving your screenshot region?<br>" +
	                "Save time and use the pre-defined region tool to take screenshots of pre-defined areas selected by you!";
		saBtn = new JButton(new ImageIcon(getClass().getResource("/selwinbtn.png")));
		saBtn.setContentAreaFilled(false);
		saBtn.setBorderPainted(false);
	    saBtn.setToolTipText(html2);
		saBtn.setFocusPainted(false);
		saBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!SplashScreen.getSingleton().getMainFrame().isDrawingAlready()){
					SplashScreen.getSingleton().getMainFrame().screenshot();
				}
			}

		});
		saBtn.setBounds(340, 40, 80, 88);
		add(saBtn);
		daBtn = new JButton(new ImageIcon(getClass().getResource("/daBtn.png")));
		daBtn.setContentAreaFilled(false);
		daBtn.setBorderPainted(false);
		daBtn.setFocusPainted(false);
		 final String html3 =
	                "<html><body>" +
	                "<p><b>Freeze Screenshot:</b>:<br>" +
	                "<b>This feature is used to freeze your screen and allow for croping of area you wish to share.<br>" +
	                "Usage: Select the area on your frozen screen you wish to crop for a screenshot!</html>";
	    daBtn.setToolTipText(html3);
		daBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!SplashScreen.getSingleton().getMainFrame().isDrawingAlready()){
						 SplashScreen.getSingleton().getMainFrame().setState(SplashScreen.getSingleton().getMainFrame().ICONIFIED);
						 SplashScreen.getSingleton().getMainFrame().snapscreenshot();
					SplashScreen.getSingleton().getMainFrame().setIsDrawingAlready(true);
				}
			}

		});
		daBtn.setBounds(140, 40, 80, 88);
		add(daBtn);
		Label3.setText("<html><font face=\"verdana\">"+capitalize(SplashScreen.getSingleton().getUserSettings().getSelsnapHK())+"</font></html>");
		Label2.setText("<html><font face=\"verdana\">"+capitalize(SplashScreen.getSingleton().getUserSettings().getSelHK())+"</font></html>");
		Label.setText("<html><font face=\"verdana\">"+capitalize(SplashScreen.getSingleton().getUserSettings().getSnapHK())+"</font></html>");	
		repaint();
	
		setBackground(Color.white);
		setBorder(BorderFactory.createLineBorder(new Color(218,218,218)));
		Label3.setBounds(150,125,128,32);
		Label2.setBounds(240,125,128,32);
		Label.setBounds(345, 125,128,32);
		add(Label);
		add(Label2);
		add(Label3);
		add(caBtn);
	}
	private String capitalize(String line) {   return Character.toUpperCase(line.charAt(0)) + line.substring(1); } 
	public void updateLbl() {
		
		Label3.setText("<html><font face=\"verdana\">"+capitalize(SplashScreen.getSingleton().getUserSettings().getSelsnapHK())+"</font></html>");
		Label2.setText("<html><font face=\"verdana\">"+capitalize(SplashScreen.getSingleton().getUserSettings().getSelHK())+"</font></html>");
		Label.setText("<html><font face=\"verdana\">"+capitalize(SplashScreen.getSingleton().getUserSettings().getSnapHK())+"</font></html>");	
		repaint();
		validate();
	}
}

