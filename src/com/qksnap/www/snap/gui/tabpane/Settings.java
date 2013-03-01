package com.qksnap.www.snap.gui.tabpane;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.qksnap.www.snap.autostart.WinRegistry;
import com.qksnap.www.snap.gui.MainFrame;
import com.qksnap.www.snap.gui.SplashScreen;

/** Holds a checkbox and drop down to set hotkeys 
 * 
 * @author zeroeh
 *
 */
public class Settings extends JPanel {
	private ImageIcon cup = new ImageIcon(getClass().getResource("/Save_Button.png"));
	private JButton saveBtn = new JButton(cup);
	private JTextField ssHotkey;
	private JTextField ssRectkey;
	private JTextField ssCutkey;
	private JCheckBox autoStart;
	private BufferedImage img;
	private boolean hkpressed;
	private JLabel ssHotLbl = new JLabel("Snap Area");
	private JLabel ssRectLbl = new JLabel("Select Area");
	private JLabel ssCutLbl = new JLabel("Draw Snap");
	public final List<Integer> MODIFIERS = Arrays.asList(KeyEvent.VK_ALT, KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_META);

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
	public Settings(){
		super(null);
		try {
			img = ImageIO.read(getClass().getResource("/hksetbg.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		setSize(500,250);
		autoStart = new JCheckBox("Auto-Start");
	    autoStart.addItemListener(new ItemListener(){
	        public void itemStateChanged(ItemEvent e) {
	            if(e.getSource()==autoStart){
	                if(autoStart.isSelected()) {
	                	try {
							WinRegistry.writeStringValue(0x80000002, "\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run", "QwikSnap", System.getProperty("user.dir")+"/start.bat");
						} catch (IllegalArgumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IllegalAccessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InvocationTargetException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	                	} else {
	                	}
	            }
	        }
	    });

	 //   autoStart.setBounds(225, 120, 128, 32);
	   // add(autoStart);
		setBorder(BorderFactory.createLineBorder(new Color(218,218,218)));
		ssHotkey = new JTextField("ctrl B");
		ssHotkey.setHorizontalAlignment(JTextField.CENTER);
		ssHotkey.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
				SplashScreen.getSingleton().getMainFrame().getProvider().reset();
				hkpressed = true;
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		ssHotkey.setBounds(220,60,75,32);
		ssHotkey.setForeground(Color.WHITE);
		ssHotkey.setBackground(Color.BLACK);
		ssHotkey.setBorder(BorderFactory.createLineBorder(new Color(218,218,218)));
		ssHotLbl.setBounds(225, 100, 250, 16);

		ssRectkey = new JTextField("ctrl N");
		ssRectkey.setBounds(320,60,75,32);
		ssRectkey.setForeground(Color.WHITE);
		ssRectkey.setBackground(Color.BLACK);
		ssRectkey.setToolTipText("<html><body>" +
                "<p>Set a hotkey for selecting a predefined region.</p>");
		ssRectkey.setHorizontalAlignment(JTextField.CENTER);
		ssRectkey.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
				SplashScreen.getSingleton().getMainFrame().getProvider().reset();
				hkpressed = true;
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		ssCutkey = new JTextField("ctrl M");
		ssCutkey.setForeground(Color.WHITE);
		ssCutkey.setBackground(Color.BLACK);
		ssCutkey.setBorder(BorderFactory.createLineBorder(new Color(218,218,218)));
		ssCutkey.setBounds(120,60,75,32);
		ssHotkey.setToolTipText("<html><body>" +
                "<p>Set a hotkey for snaping a pre-defined region screenshot.</p>");
		ssCutkey.setToolTipText("<html><body>" +
                "<p>Set a hotkey for Snap Freeze Screenshots</p>");
		ssCutkey.setHorizontalAlignment(JTextField.CENTER);
		ssCutkey.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
				SplashScreen.getSingleton().getMainFrame().getProvider().reset();
				hkpressed = true;
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		ssCutkey.setEditable(false);
		ssCutkey.addKeyListener(new KeyAdapt(this));
		ssHotkey.setText(SplashScreen.getSingleton().getUserSettings().getSnapHK());
		ssCutkey.setText(SplashScreen.getSingleton().getUserSettings().getSelsnapHK());
		ssRectkey.setText(SplashScreen.getSingleton().getUserSettings().getSelHK());
		ssHotkey.setEditable(false);
		ssRectkey.setEditable(false);
		ssRectkey.setBorder(BorderFactory.createLineBorder(new Color(218,218,218)));
		ssHotkey.addKeyListener(new KeyAdapt(this));
		ssRectkey.addKeyListener(new KeyAdapt(this));
		ssCutLbl.setBounds(125, 100, 250, 16);
		ssRectLbl.setBounds(320, 100, 250, 16);
		
		
		saveBtn.setBounds(445,35,74,105);
		saveBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				SplashScreen.getSingleton().getUserSettings().update(ssRectkey.getText(),ssHotkey.getText(),ssCutkey.getText(),1);
				SplashScreen.getSingleton().getMainFrame().getMainPane().updateLbl();
				SplashScreen.getSingleton().getMainFrame().getMainPane().repaint();
				SplashScreen.getSingleton().getMainFrame().getProvider().reset();
				hkpressed = false;
				// register ctrl S as hot key
				SplashScreen.getSingleton().getMainFrame().getProvider().register(KeyStroke.getKeyStroke(ssHotkey.getText()), SplashScreen.getSingleton().getMainFrame().hkListener());
				SplashScreen.getSingleton().getMainFrame().getProvider().register(KeyStroke.getKeyStroke(ssRectkey.getText()), SplashScreen.getSingleton().getMainFrame().hkListener2());
				//System.out.println(ssCutkey.getText());
				SplashScreen.getSingleton().getMainFrame().getProvider().register(KeyStroke.getKeyStroke(ssCutkey.getText()), SplashScreen.getSingleton().getMainFrame().hkListener3());
				SplashScreen.getSingleton().getMainFrame().getTrayIcon().displayMessage("QwikSnap", "Your settings have been saved.", TrayIcon.MessageType.INFO);
				if(autoStart.isSelected()){
					SplashScreen.getSingleton().getUserSettings().update(ssRectkey.getText(),ssHotkey.getText(),ssCutkey.getText(),1);
				} else {
					SplashScreen.getSingleton().getUserSettings().update(ssRectkey.getText(),ssHotkey.getText(),ssCutkey.getText(),0);
				}
			}
			
		});
		add(saveBtn);
		add(ssRectLbl);
		add(ssRectkey);
		add(ssHotLbl);
		add(ssHotkey);
		add(ssCutkey);
		add(ssCutLbl);
	}

	public JTextField getSsHotkey() {
		return ssHotkey;
	}

	public JTextField getSsRectkey() {
		return ssRectkey;
	}

	public JTextField getSsCutkey() {
		return ssCutkey;
	}
	public void setSsHotkey(JTextField ssHotkey) {
		this.ssHotkey = ssHotkey;
	}

	public void setSsRectkey(JTextField ssRectkey) {
		this.ssRectkey = ssRectkey;
	}
	public void updateSettings(){
		ssHotkey.setText(SplashScreen.getSingleton().getUserSettings().getSnapHK());
		ssCutkey.setText(SplashScreen.getSingleton().getUserSettings().getSelsnapHK());
		ssRectkey.setText(SplashScreen.getSingleton().getUserSettings().getSelHK());
	}
	public boolean isHKPressed() {
		return hkpressed;
	}
	public void setHKPressed(boolean s){
		this.hkpressed = s;
	}
}
class KeyAdapt extends KeyAdapter {
	private Settings s;
	
	public KeyAdapt(Settings s){
		this.s = s;
	}
	@Override
    public void keyPressed(KeyEvent e) {
    	if(s.getSsHotkey().hasFocus()){
    		if (s.MODIFIERS.contains(e.getKeyCode()))
    			s.getSsHotkey().setText("");
    		else
    			s.getSsHotkey().setText(KeyStroke.getKeyStrokeForEvent(e).toString().replaceAll("pressed ", ""));
    	} else if(s.getSsRectkey().hasFocus()){
    		if (s.MODIFIERS.contains(e.getKeyCode()))
    			s.getSsRectkey().setText("");
    		else
    			s.getSsRectkey().setText(KeyStroke.getKeyStrokeForEvent(e).toString().replaceAll("pressed ", ""));
    	} else if(s.getSsCutkey().hasFocus()){
    		if (s.MODIFIERS.contains(e.getKeyCode()))
    			s.getSsCutkey().setText("");
    		else
    			s.getSsCutkey().setText(KeyStroke.getKeyStrokeForEvent(e).toString().replaceAll("pressed ", ""));
    	}else {
    		System.out.println("Not focused");
    	}
	}
    	
}