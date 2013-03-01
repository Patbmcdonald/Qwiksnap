package com.qksnap.www.snap.gui.tabpane;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
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
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import com.qksnap.www.snap.gui.MainFrame;
import com.qksnap.www.snap.gui.SplashScreen;
import com.qksnap.www.snap.gui.tabpane.AboutPane;
import com.qksnap.www.snap.gui.tabpane.Links;
import com.qksnap.www.snap.gui.tabpane.MainPane;
import com.qksnap.www.snap.gui.tabpane.Settings;
import com.qksnap.www.snap.gui.tabpane.data.ImageLink;
import com.qksnap.www.snap.gui.tabpane.ui.TabUI;
import com.qksnap.www.snap.hotkeys.common.HotKey;
import com.qksnap.www.snap.hotkeys.common.HotKeyListener;
import com.qksnap.www.snap.hotkeys.common.Provider;
import com.qksnap.www.snap.io.SocketEngine;

public class Export extends JPanel implements ClipboardOwner {
	private Provider provider = Provider.getCurrentProvider(true);
	private JFrame frame;
	private JLabel taLbl = new JLabel("<html>Enter syntax to export as (Ex. [img]%img%[/img]):<br></html>");
	private JTextArea selection = new JTextArea();
	private JLabel taLbl2 = new JLabel("<html>Legend:<br> %img% - replaced with your direct image.<br> %link% - replaced with your image link</html>");
	private JButton ctcBtn = new JButton("Save To Clip Board");
	private JButton stpBtn = new JButton("Save To .txt");
	
	public void parseFile(){
		StringBuilder sb = new StringBuilder();
		/*for(ImageLink s : Links.getModel().getItems()){
			String line = selection.getText();
			line = line.replaceAll("%img%",s.getLink()+"screenshot.png");
			line = line.replaceAll("%link%",s.getLink());
			System.out.println(line);
			sb.append(line + "\n");
		}*/
		FileWriter fw;
		JFileChooser chooser = new JFileChooser("links.txt");
		chooser.addChoosableFileFilter(new Filter());
		chooser.setSelectedFile(new File("fileToSave.txt"));
	    int returnVal = chooser.showSaveDialog(frame);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	try {
				fw = new FileWriter(chooser.getSelectedFile().getCanonicalPath());
				fw.write(sb.toString());
				fw.flush();
				fw.close();
				SplashScreen.getSingleton().getMainFrame().getTrayIcon().displayMessage("QwikSnap", "You have saved your links to "+chooser.getSelectedFile().getCanonicalPath(), TrayIcon.MessageType.INFO);
			frame.setVisible(false);
	    	} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     }
		}
	public void parseClip(){
		StringBuilder sb = new StringBuilder();
		/*for(ImageLink s : Links.getModel().getItems()){
			String line = selection.getText();
			line = line.replaceAll("%img%",s.getLink()+"screenshot.png");
			line = line.replaceAll("%link%",s.getLink());
			System.out.println(line);
			sb.append(line + "\n");
		}*/
		setClipboardContents(sb.toString());
		SplashScreen.getSingleton().getMainFrame().getTrayIcon().displayMessage("QwikSnap", "Your links have been saved to your clipboard!", TrayIcon.MessageType.INFO);
		frame.setVisible(false);
	}
	 public void setClipboardContents( String aString ){
		    StringSelection stringSelection = new StringSelection( aString );
		    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		    clipboard.setContents( stringSelection, this );
		  }
	public void setInit() throws IOException{
		setSize(475,360);
		setLayout(null);
		setBackground(new Color(53,53,53));
		taLbl.setForeground(Color.white);
		taLbl2.setForeground(Color.white);
		taLbl.setBounds(20,30,400,16);
		taLbl2.setBounds(20,250,400,64);
		selection.setBounds(20, 50, 440, 200);
		ctcBtn.setBounds(100, 310, 150, 32);
		stpBtn.setBounds(300, 310, 100, 32);
		ctcBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				parseClip();
			}
			
			
		});
		stpBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				parseFile();
			}
			
			
		});
		add(taLbl);
		add(taLbl2);
		add(selection);
		add(ctcBtn);
		add(stpBtn);
		JLabel title =  new JLabel("Qwik Snap Export Links");
		title.setForeground(Color.white);
		title.setBounds(175,0,200,16);
		add(title);
		
		BufferedImage source = ImageIO.read(getClass().getResource("/min.png"));
		Image imageWithTransparency = makeColorTransparent(source, new Color(53,53,53));
		ImageIcon cup = new ImageIcon(imageWithTransparency);
		JButton min = new JButton(cup);
		
		source = ImageIO.read(getClass().getResource("/exit.png"));
		imageWithTransparency = makeColorTransparent(source, new Color(53,53,53));
		ImageIcon cup2 = new ImageIcon(imageWithTransparency);
		JButton exit = new JButton(cup2);
		exit.setContentAreaFilled(false);
		exit.setBounds(450,3,16,16);
		exit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(false);
			}
			
			
		});
		add(min);
		add(exit);
	}
	public Export() throws IOException {
		setInit();
		frame = new JFrame("QwikSnap Export");
		final Point point = new Point();
		frame.setSize(475, 350);
		frame.setFont(new Font("LucidaSans ", Font.PLAIN, '9'));
		frame.setResizable(false);
		frame.setBackground(new Color(53,53,53));
		frame.setForeground(Color.white);
		frame.getContentPane().add(this);
		frame.setLayout(null);
		frame.setUndecorated(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);

	    frame.addMouseListener(new MouseAdapter() {
	      public void mousePressed(MouseEvent e) {
	        point.x = e.getX();
	        point.y = e.getY();
	      }
	    });
	    frame.addMouseMotionListener(new MouseMotionAdapter() {
	      public void mouseDragged(MouseEvent e) {
	        Point p = frame.getLocation();
	        frame.setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
	      }
	    });
	

	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// TODO Auto-generated method stub

	}
	/**
	    * Convert Image to BufferedImage.
	    *
	    * @param image Image to be converted to BufferedImage.
	    * @return BufferedImage corresponding to provided Image.
	    */
	   private BufferedImage imageToBufferedImage(final Image image)
	   {
	      final BufferedImage bufferedImage =
	         new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	      final Graphics2D g2 = bufferedImage.createGraphics();
	      g2.drawImage(image, 0, 0, null);
	      g2.dispose();
	      return bufferedImage;
	    }

	   /**
	    * Make provided image transparent wherever color matches the provided color.
	    *
	    * @param im BufferedImage whose color will be made transparent.
	    * @param color Color in provided image which will be made transparent.
	    * @return Image with transparency applied.
	    */
	   public Image makeColorTransparent(final BufferedImage im, final Color color)
	   {
	      final ImageFilter filter = new RGBImageFilter()
	      {
	         // the color we are looking for (white)... Alpha bits are set to opaque
	         public int markerRGB = color.getRGB() | 0xFFFFFFFF;

	         public final int filterRGB(final int x, final int y, final int rgb)
	         {
	            if ((rgb | 0xFF000000) == markerRGB)
	            {
	               // Mark the alpha bits as zero - transparent
	               return 0x00FFFFFF & rgb;
	            }
	            else
	            {
	               // nothing to do
	               return rgb;
	            }
	         }
	      };

	      final ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
	      return Toolkit.getDefaultToolkit().createImage(ip);
	   }
}

class Filter extends javax.swing.filechooser.FileFilter {
    public boolean accept(File file) {
        String filename = file.getName();
        return filename.endsWith(".txt");
    }
    public String getDescription() {
        return "*.txt";
    }
}
