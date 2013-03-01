package com.qksnap.www.snap.gui.tabpane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import com.qksnap.www.snap.gui.SplashScreen;
import com.qksnap.www.snap.gui.tabpane.componet.ScreenShotImage;
import com.qksnap.www.snap.gui.tabpane.data.ImageLink;
import com.qksnap.www.snap.gui.tabpane.data.ImgCache;
public class Links extends JPanel implements ClipboardOwner {
	/** Screen Shot Left Btn **/
	private JButton leftArrowBtn;
	/** Screen Shot Right Logo **/
	private JButton rightArrowBtn;
	private JLabel pageLbl;
	private JLabel selectLbl;
	/** Screen Shot Upload Btn **/
	private JButton uploadBtn;
	/** Screen Shot Image head **/
	/** Screen Shot Image Cache **/
	private ImgCache<Integer,ScreenShotImage> cache;
	
	/** change this to a new struct later **/
	private List<String> selected;
	
	private int page = 1;
	public void addScreenshot() throws IOException{
		cache.set(size,new ScreenShotImage(size,SplashScreen.getSingleton().getUserSettings().getLinks().get(SplashScreen.getSingleton().getUserSettings().getLinks().size()-1)));
		size++;
		viewPage();
	}
	
	public void addSelect(String in){
		selected.add(in);
	}
	
	public void removeSelect(String in){
		for(Iterator<String> rem = selected.iterator(); rem.hasNext();){
			String s = (String) rem.next();
			if(s.equals(in)){
				rem.remove();
			}
		}
	}
	public Links(){
		super(null);
		cache = new ImgCache<Integer,ScreenShotImage>(60);
		selected = new ArrayList<String>();
		setBackground(new Color(232,232,232));
		try {
			createScreenShotCache();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createOtherButton();
		viewPage();
		setVisible(true);
	}
	private JPopupMenu popup;
	public void createPop(){
		popup = new JPopupMenu();
	    JMenuItem menuItem = new JMenuItem("Copy to Clipboard.");
	    menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				StringBuilder sb = new StringBuilder();
				for(String in : selected){
						sb.append(in+",");
				}
				setClipboardContents(sb.toString());
			}
	    });
	    JMenuItem menuItem2 = new JMenuItem("Export URL to a.txt file.");
	    popup.add(menuItem);
	  //  popup.add(menuItem2);
	}
	public void setPageTxt(String txt){
		pageLbl.setText("page ("+txt+")");
		revalidate();
	}
	
	public void setSelectTxt(String txt){
		selectLbl.setText(txt+" Image(s) Selected");
		repaint();
		revalidate();
	}
	public void createOtherButton(){
		createPop();
		pageLbl = new JLabel();
		pageLbl.setBounds(100,125,128,32);
		add(pageLbl);
		selectLbl = new JLabel("0 Image(s) Selected");
		selectLbl.setBounds(400,125,128,32);
		add(selectLbl);
		uploadBtn = new JButton(new ImageIcon(Links.class.getResource("/share_button.png")));
		uploadBtn.setBounds(238, 125, 125,24);

		uploadBtn.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				popup.show(e.getComponent(),
	                       e.getX(), e.getY());
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
			
		});
		add(uploadBtn);
		
		leftArrowBtn = new JButton(new ImageIcon(Links.class.getResource("/left_arrow.png")));
		leftArrowBtn.setBounds(5, 60, 32,36);
		leftArrowBtn.setBorderPainted(false);
		leftArrowBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(page == 1){
					page = 1;
				} else {
					page--;
				}
				viewPage();
			}
			
		});
		add(leftArrowBtn);
		
		rightArrowBtn = new JButton(new ImageIcon(Links.class.getResource("/right_arrow.png")));
		rightArrowBtn.setBounds(530, 60, 32,36);
		rightArrowBtn.setBorderPainted(false);
		rightArrowBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(page < size)
					page++;
				viewPage();
			}
			
		});
		add(rightArrowBtn);
	}

	public void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}
	
	private int size = 0;
	public void createScreenShotCache() throws IOException{
		/** Lets loop through all the files **/
		for (int index = ((page * 3) - 3); index < ((page * 3) + 3); index++){
			   /** Create a new Button **/
				if(index > SplashScreen.getSingleton().getUserSettings().getLinks().size()-1){
					break;
				}
				 cache.set(size,new ScreenShotImage(size,SplashScreen.getSingleton().getUserSettings().getLinks().get(index)));
				 size++;
				 SplashScreen.getSingleton().drawText((index+1)*17, "Loading Image from cache.");
		}
		for(int i = -1; i > -4; i--){
			cache.set(i,new ScreenShotImage(i));
		}
		SplashScreen.getSingleton().drawText(100, "Loading images - 100%"); 
	}
	

	/** View the page **/
	public void viewPage(){
		removeAll();
		createOtherButton();
		int x = 40;
		int y = 15;
		int imageIndex = (page * 3) - 3; // 6 items per page, arrays start at 0.
		for(int i = imageIndex; (i < imageIndex+3); i++){
			ScreenShotImage in = cache.get(i);
			if(in == null){
				in = cache.get(-1-(i%3));
			}
			 in.setBounds(x, y,  161,106);
			 in.setVisible(true);
			 add(in);
			 x += 165;
		}
		
		setPageTxt(Integer.toString(page));
		revalidate();
		repaint();
		
	}
	public void setClipboardContents( String aString ){
		StringSelection stringSelection = new StringSelection( aString );
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents( stringSelection, this );
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// TODO Auto-generated method stub

	}

}