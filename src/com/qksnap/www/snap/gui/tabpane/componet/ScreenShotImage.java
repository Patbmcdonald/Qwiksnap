package com.qksnap.www.snap.gui.tabpane.componet;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

import com.qksnap.www.snap.gui.SplashScreen;
import com.qksnap.www.snap.gui.tabpane.Links;
import com.qksnap.www.snap.gui.tabpane.data.ImageLink;
import com.qksnap.www.snap.util.Config;

public class ScreenShotImage extends JButton {
	/** Paint our Btn **/
    private Painter painter;
    
    private ImageLink link;
    
    /** our background **/ 
	private SoftReference<BufferedImage> image;
    /** Are we Selected? **/
    private boolean selected = false;
    /** id **/
    private int id = -1;
    
    public static volatile long start;
    
	public static int totalSelected = 0;
    
	private boolean needUpdate = false;
	public BufferedImage getBufferedImage() {
		if (image != null && !needUpdate){
			final BufferedImage cachedImg = image.get();
			if (cachedImg != null) {
				return cachedImg;
			}
		}

		System.out.println("Updating cache.");
   		 try {

   		   if(id < 0)
   			   image = new SoftReference<BufferedImage>(ImageIO.read(new ByteArrayInputStream(Config.getAsByteArray(Links.class.getResource("/noimage.png")))));
   		   else
   		       image = new SoftReference<BufferedImage>(ImageIO.read(new ByteArrayInputStream(Config.scaleImage(Config.getUrlImageAsByteArray("http://www.qksnap.com/imgs/"+getLink().getName()), 161,106))));
   		
   		 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   		 needUpdate = false;
		return image.get();
	}
   
   
    /** 
     *  Create a screenshot image
     * @throws IOException 
     */
    
    
    public MouseListener getMouseListener() {
    	return new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(getLink() == null)
					return;
				switch(e.getButton()) {
					case MouseEvent.BUTTON1:
						if( !java.awt.Desktop.isDesktopSupported() ) {
						  		JOptionPane.showMessageDialog(null,"You computer does not support AWT Desktop :(","Desktop is not supported!", 2);
						}   
						java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
						if( !desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) {
							JOptionPane.showMessageDialog(null,"You computer does not support AWT Desktop Browsing :(","Desktop Browse is not supported!", 2);
							
						}
						    try {
						         desktop.browse( new URI(getLink().getLink()));
						    }
						    catch ( Exception ed ) {
						 
						    }
						
						//JOptionPane.showMessageDialog(null, "", getLink().getLink(), JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getFileLocation()));
					break;
					case MouseEvent.BUTTON3:
			           selected = !selected;
			           changeUpload(selected);
					break;
					
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
    		
    		
    	};
    }
    
    public ScreenShotImage(int id,ImageLink img) throws IOException{
    	this.setBorder(BorderFactory.createMatteBorder(
                2, 2, 2, 2, Color.white));
    	this.id = id;
    	setLink(img);
        final String html =
                "<html><body>" +
                "<p><b>To View Image</b>: Left Click Image<br>" +
                "<b>To Select from export</b>: Right Click Image<br>"+
                "<b>URL Link</b>: " + getLink().getLink() +"<br>"+
                "<b>Upload Date</b>: "+ getLink().getDate()+"<br>"+
                "<b>Size</b>: "+ getLink().getSize()+"</p>";
        setToolTipText(html);
    	addMouseListener(getMouseListener());
    	setupImageElements();
    	setVisible(true);
    	needUpdate = true;
    }
    
    
    public ScreenShotImage(int id) throws IOException{
    	this.setBorder(BorderFactory.createMatteBorder(
                2, 2, 2, 2, Color.white));
    	this.id = id;        
    	addMouseListener(getMouseListener());
    	setupImageElements();
    	setVisible(true);
    	needUpdate = true;
    }

	public void changeUpload(boolean in){
		
		if(in){
			ScreenShotImage.totalSelected++;
			SplashScreen.getSingleton().getMainFrame().getLinks().setSelectTxt(Integer.toString(ScreenShotImage.totalSelected));
			SplashScreen.getSingleton().getMainFrame().getLinks().addSelect(getLink().getLink());
		}else {
			ScreenShotImage.totalSelected--;
			SplashScreen.getSingleton().getMainFrame().getLinks().setSelectTxt(Integer.toString(ScreenShotImage.totalSelected));
			SplashScreen.getSingleton().getMainFrame().getLinks().removeSelect(getLink().getLink());
    
		}
	}
    public void setupImageElements(){
    	painter = new Painter(){
			@Override
			public void paint(Graphics g) {
				
				Graphics2D g2d = (Graphics2D)g;
				BufferedImage img = getBufferedImage();
				g.drawImage(img , 0, 0, null);
				if(selected){
					g2d.setColor(Color.RED);
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 4 * 0.1f));
					g2d.fillRect(0, 0, 161,106);
				}
				g2d.setColor(Color.black);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 6 * 0.1f));
			    g2d.fillRect(0,88, 90, 16);
			    g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
			    g2d.setColor(Color.white);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 8 * 0.1f));
				g2d.drawString(getLink() != null ? getLink().getName().replaceAll(".png", "") : "No IMG" , 10, 101);
				try {
					g2d.drawImage(ImageIO.read(ScreenShotImage.class.getResource("/zoom.png")), 140,84,null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				start = System.currentTimeMillis();
				try{
				// Delay depending on how far behind current time we are.
	              start += (1000/45);
	              Thread.sleep(Math.max(0, start - System.currentTimeMillis()));
	          } catch(Exception e){
	        	  e.printStackTrace();
	          }
			}
    		
    	};
    }
    
    public void paintComponent(Graphics g) {
       super.paintComponent(g);
       painter.paint(g);
		start = System.currentTimeMillis();
		try{
		// Delay depending on how far behind current time we are.
         start += (1000/45);
         Thread.sleep(Math.max(0, start - System.currentTimeMillis()));
     } catch(Exception e){
   	  e.printStackTrace();
     }
    }

    public int getId() {
    	return id;
    }
    
    public void setID(int id){
    	this.id = id;
    }


	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public ImageLink getLink() {
		return link;
	}

	public void setLink(ImageLink link) {
		this.link = link;
	}

}
interface Painter {
    public void paint(Graphics g);
}
