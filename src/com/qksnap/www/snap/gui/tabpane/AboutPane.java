package com.qksnap.www.snap.gui.tabpane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.qksnap.www.snap.gui.tabpane.componet.Marquee;
import com.qksnap.www.snap.util.Config;

public class AboutPane extends JPanel {
	private String version = Config.currentVersion;
	private BufferedImage img;
	private JLabel about = new Marquee("<html><center><u>QwikSnap</u><br>" +
			"http://www.qksnap.com<br><br>" +
			"<strong>Version</strong>:<br> "+version+"<br><br>" +
			"<b>Special Thanks</b>:<br> Denis Tulskiy<br>" +
			"Renee K<br>" +
			"Aaron 'mp'<br>" +
			"<br><b>Privacy Terms and Usage<b><br>" +
			"QwikSnap and QwikSoftware LLC respects your privacy.<br>" +
			"We do not collect personally identifiable information<br>" +
			"about you unless you voluntarily provide it, <br>" +
			"such as when you provide email contact information to<br>" +
			"subscribe to the QwikSnap Forums,<br>" +
			"send feedback to QwikSnap.<br>" +
			"<br><br>If you voluntarily provide your email address or other contact information,<br>" +
			"we might also use it the following ways:<br>" +
			"to inform you of changes to QwikSnap<br>" +
			"to get bug reproduction information<br>" +
			"for feature suggestions<br>" +
			"to ask for your support.<br><br>" +
			"At your request, we will remove your contact information from our files");
	
	private JScrollPane sp;
	protected void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
		long start = System.currentTimeMillis();
		try {
              // Delay depending on how far behind current time we are.
              start += (1000/30);
              Thread.sleep(Math.max(0, start - System.currentTimeMillis()));
          } catch(Exception e){
        	  e.printStackTrace();
          }
		img.flush();
	}
	public AboutPane(){
		super(null);
		try {
			img = ImageIO.read(getClass().getResource("/mpbg.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		repaint();
		setBackground(new Color(232,232,232));
		setBorder(BorderFactory.createLineBorder(new Color(232,232,232)));
		sp = new JScrollPane(about);
		sp.setBackground(new Color(232,232,232));
		sp.setBounds(25, 5, 525, 125);
		sp.repaint();
		add(sp);
	}
}


