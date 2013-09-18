package com.qksnap.www.snap.gui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.qksnap.www.snap.gui.tabpane.AboutPane;
import com.qksnap.www.snap.gui.tabpane.Links;
import com.qksnap.www.snap.gui.tabpane.MainPane;
import com.qksnap.www.snap.gui.tabpane.Settings;
import com.qksnap.www.snap.gui.tabpane.Uploader;
import com.qksnap.www.snap.gui.tabpane.data.ImageLink;
import com.qksnap.www.snap.gui.tabpane.ui.TabUI;
import com.qksnap.www.snap.hotkeys.common.HotKey;
import com.qksnap.www.snap.hotkeys.common.HotKeyListener;
import com.qksnap.www.snap.hotkeys.common.Provider;
import com.qksnap.www.snap.io.SocketEngine;
import com.qksnap.www.snap.settings.UserSettings;
import com.qksnap.www.snap.util.Config;

public class MainFrame extends JFrame implements ClipboardOwner {
	/** TODO: Need to really clean this up **/

	/** global Hotkey provider **/
	private Provider provider = Provider.getCurrentProvider(true);
	/** Instance to this class **/
	private MainFrame instance;
	/** Links instance **/
	private Links links;
	/** Settings instance **/
	private Settings settings;
	/** Screenshot Image **/
	private byte[] img;
	
	private Uploader upload;
	/** Main Tabbed pane instance **/
	private MainPane mainPane;
	/** Our tab pane **/
	private JTabbedPane pane;
	/** Area used to capture selected screenshots **/
	private Rectangle SS;
	/** Parent Frame **/
	private JFrame frame;
	/** Our Tray icon **/
	private TrayIcon trayIcon;
	private boolean isDrawingAlready;
	/** our Hotkey Listener **/
	private HotKeyListener[] listener = new HotKeyListener[3];
	/** Our Socket Engine **/
	private SocketEngine se;
	private Dimension screenSize;
	private Rectangle screenRectangle;
	private Robot robot;
	/**
	 * Create the System Tray Icon and menu
	 */

	public void createTray() {

		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		try {
			final PopupMenu pop = new PopupMenu();
			java.net.URL imageURL = getClass().getResource("/icon.png");
			trayIcon = new TrayIcon(new ImageIcon(imageURL).getImage(),
					"QwikSnap");

			final SystemTray tray = SystemTray.getSystemTray();
			tray.add(trayIcon);
			/** Click Tray Icon **/
			trayIcon.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					setVisible(true);
					setExtendedState(NORMAL);
					repaint();
				}
			});
			/** Frame Listener **/
			addWindowListener(new WindowAdapter() {
				@Override
				public void windowDeiconified(WindowEvent e) {
					setVisible(true);
					validate();
					repaint();
				}

				@Override
				public void windowIconified(WindowEvent e) {
					setVisible(false);
					try {
						// tray.add(trayIcon);
						repaint();
					} catch (Exception ed) {
						// TODO Auto-generated catch block
						ed.printStackTrace();
					}
				}

				@Override
				public void windowClosing(WindowEvent e) {
					provider.reset();
					provider.stop();
					System.exit(0);
				}
			});
			// Create a pop-up menu components
			MenuItem aboutItem = new MenuItem("Open QwikSnap");
			aboutItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(true);
					setExtendedState(NORMAL);
				}

			});
			pop.add(aboutItem);
			MenuItem exitItem = new MenuItem("Exit QwikSnap");
			exitItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(2);
				}

			});
			pop.add(exitItem);

			trayIcon.setPopupMenu(pop);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create and initalize our tabbed pane
	 */
	private void createTabPane() {
		pane = new JTabbedPane(JTabbedPane.TOP);
		mainPane = new MainPane();
		links = new Links();
		upload = new Uploader();
		settings = new Settings();
		pane.setUI(new TabUI());
		pane.setForeground(Color.white);
		pane.setFont(new Font("verdana", Font.CENTER_BASELINE, 12));
		pane.addTab("Main", null, mainPane, "Main Panel");
		pane.setMnemonicAt(0, KeyEvent.VK_1);
		pane.addTab("Upload", null, upload, "Offline Upload");
		pane.setMnemonicAt(1, KeyEvent.VK_2);
		pane.addTab("Saved Image", null, links, "View Saved Links");
		pane.setMnemonicAt(2, KeyEvent.VK_3);
		pane.addTab("Settings", null, settings, "Settings");
		pane.setMnemonicAt(3, KeyEvent.VK_4);
		pane.addTab("About", null, new AboutPane(), "About Qwik Snap");
		pane.setMnemonicAt(4, KeyEvent.VK_5);
		pane.addChangeListener(new ChangeListener() {
			// This method is called whenever the selected tab changes
			public void stateChanged(ChangeEvent evt) {
				JTabbedPane pane = (JTabbedPane) evt.getSource();
				Object[] options = { "Save", "Reset Hotkeys" };
				// Get current tab
				int sel = pane.getSelectedIndex();
				if (sel != 2)
					upload.reset();
				if (sel != 4 && settings.isHKPressed()) {
					int n = JOptionPane
							.showOptionDialog(
									frame,
									"You did not save your hot key changes! Would you like to save now?",
									"Error 10",
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE, null,
									options, options[1]);
					switch (n) {
					case 0:
						getProvider().reset();
						getProvider().register(
								KeyStroke.getKeyStroke(settings.getSsHotkey()
										.getText()), hkListener());
						getProvider().register(
								KeyStroke.getKeyStroke(settings.getSsRectkey()
										.getText()), hkListener2());
						getProvider().register(
								KeyStroke.getKeyStroke(settings.getSsCutkey()
										.getText()), hkListener3());
						SplashScreen.getSingleton().getUserSettings().update(
								settings.getSsRectkey().getText(),
								settings.getSsHotkey().getText(),
								settings.getSsCutkey().getText(), 1);
						getMainPane().updateLbl();
						getMainPane().repaint();
						getTrayIcon().displayMessage("QwikSnap",
								"Your settings have been saved.",
								TrayIcon.MessageType.INFO);
						settings.setHKPressed(false);
						break;
					default:
						getProvider().reset();
						provider.register(KeyStroke
								.getKeyStroke(SplashScreen.getSingleton().getUserSettings().getSnapHK()),
								listener[0]);
						provider.register(KeyStroke
								.getKeyStroke(SplashScreen.getSingleton().getUserSettings().getSelHK()),
								listener[1]);
						provider.register(
								KeyStroke.getKeyStroke(SplashScreen.getSingleton().getUserSettings()
										.getSelsnapHK()), listener[2]);
						settings.updateSettings();
						getMainPane().updateLbl();
						getMainPane().repaint();
						settings.setHKPressed(false);
						getTrayIcon().displayMessage("QwikSnap",
								"Your hotkey settings were reset to default..",
								TrayIcon.MessageType.INFO);
					}
				}
			}
		});
		pane.validate();
	}

	public void createGui() throws AWTException {
		frame = new JFrame("QwikSnap");
		final Point point = new Point();
		int width = 0; 
		int height = 0; 
		/** Duel Monitor Support **/
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();      
		GraphicsDevice[] gs = ge.getScreenDevices(); 
		for (GraphicsDevice curGs : gs) { 
			DisplayMode mode = curGs.getDisplayMode();  
			width += mode.getWidth();
			height = mode.getHeight(); 
		} 
		screenSize = new Dimension(width,height);
		screenRectangle = new Rectangle(screenSize);
		robot = new Robot();
		setSize(564, 240);
		setFont(new Font("verdana", Font.PLAIN, '9'));
		setResizable(false);
		setLayout(null);

		setUndecorated(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				point.x = e.getX();
				point.y = e.getY();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				Point p = getLocation();
				setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
			}
		});
		repaint();
	}

	public MainFrame() throws IOException {
		setSize(564, 240);
		setLayout(null);

		try {
			createGui();
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		createTray();
		createTabPane();
		// pane.setBorder(BorderFactory.createLineBorder(Color.black));
		setBackground(new Color(53, 53, 53));
		// pane.setBackground(new Color(53,53,53));
		pane.setForeground(Color.black);
		pane.setBounds(0, 57, 564, 180);

		JButton min = new JButton(new ImageIcon(getClass().getResource(
				"/min.png")));
		min.setContentAreaFilled(false);
		min.setBorderPainted(false);
		min.setFocusPainted(false);

		JButton exit = new JButton(new ImageIcon(getClass().getResource(
				"/exit.png")));
		exit.setContentAreaFilled(false);
		exit.setContentAreaFilled(false);
		exit.setBorderPainted(false);
		exit.setFocusPainted(false);
		exit.setBounds(540, 3, 16, 16);
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(1);
			}

		});
		min.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setState(ICONIFIED);
			}

		});
		min.setContentAreaFilled(false);
		min.setBounds(520, 3, 16, 16);
		add(min);
		add(pane);
		add(exit);
		JLabel bg = new JLabel(new ImageIcon(getClass().getResource("/bg.png")));
		bg.setBounds(0, 0, 564, 240);
		add(bg);
		bg = null;
		// predefined area screenshot
		listener[0] = new HotKeyListener() {
			public void onHotKey(final HotKey hotKey) {
				if (!isDrawingAlready())
					screenshot();
			}
		};
		// clip then snap
		listener[1] = new HotKeyListener() {
			public void onHotKey(final HotKey hotKey) {
				if (!isDrawingAlready()) {
					GlassFrame.getInstance().setUpWindow(false);
					GlassFrame.getInstance().setVisible(true);
					setIsDrawingAlready(true);
				}
			}
		};
		// snap then freeze
		listener[2] = new HotKeyListener() {
			public void onHotKey(final HotKey hotKey) {
				if (!isDrawingAlready()) {
					snapscreenshot();
					setIsDrawingAlready(true);
				}
			}
		};
		provider.reset();
		provider.register(KeyStroke.getKeyStroke(SplashScreen.getSingleton().getUserSettings().getSnapHK()), listener[0]);
		provider.register(KeyStroke.getKeyStroke(SplashScreen.getSingleton().getUserSettings().getSelHK()), listener[1]);
		provider.register(KeyStroke.getKeyStroke(SplashScreen.getSingleton().getUserSettings().getSelsnapHK()),
				listener[2]);
		validate();
	}

	
	public void snapscreenshot(){
		setImg(convertImg(robot.createScreenCapture(screenRectangle)));
		GlassFrame.getInstance().setUpWindow(true);
		GlassFrame.getInstance().setVisible(true);
	}
	
	public void screenshot() {
		try {
			 robot = new Robot();
			if (SS == null) {
				getTrayIcon().displayMessage(
						"QwikSnap",
						"You must select screen coordinates first!\nPress ("
								+ SplashScreen.getSingleton().getUserSettings().getSelHK()
								+ ") to select a region",
						TrayIcon.MessageType.ERROR);
				return;
			}
			setImg(convertImg(robot.createScreenCapture(SS)));
			getSE().start();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendRectScreen(Rectangle s){
		setImg(convertImg(crop(s)));
		getSE().start();
	}
	public SocketEngine getSE() {
		if (se == null)
			se = new SocketEngine();
		return se;
	}
	
	public byte[] getImg(){
		return this.img;
	}
	public void setImg(byte[] in){
		this.img = in;
	}
	
	public byte[] convertImg(BufferedImage in){
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		try {
			ImageIO.write(in, "png", baos );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] imageInByte=baos.toByteArray();
		return imageInByte;
	}
	public BufferedImage crop(Rectangle rect){
			BufferedImage sml = new BufferedImage((int) rect.getWidth(), (int) rect.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = (Graphics2D) sml.getGraphics();
			try {
				g2d.drawImage(ImageIO.read((InputStream)new ByteArrayInputStream(getImg())), 0, 0,
						(int) rect.getWidth(), (int) rect.getHeight(),
						(int) rect.getX(), (int) rect.getY(),
						(int) (rect.getX() + rect.getWidth()),
						(int) (rect.getY() + rect.getHeight()), null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			g2d.dispose();
			return Config.toCompatibleImage(sml);
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// TODO Auto-generated method stub

	}
	public Links getLinks() {
		return links;
	}

	protected ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public HotKeyListener hkListener() {
		return listener[0];
	}

	public HotKeyListener hkListener2() {
		return listener[1];
	}

	public HotKeyListener hkListener3() {
		return listener[2];
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setRect(Rectangle ss) {
		this.SS = ss;
	}

	public boolean isDrawingAlready() {
		return this.isDrawingAlready;
	}

	public void setIsDrawingAlready(boolean s) {
		this.isDrawingAlready = s;
	}

	public Provider getProvider() {
		return provider;
	}

	public MainPane getMainPane() {
		return mainPane;
	}

	public TrayIcon getTrayIcon() {
		return trayIcon;
	}

	public Settings getSettings() {
		// TODO Auto-generated method stub
		return settings;
	}
}
