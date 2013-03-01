package com.qksnap.www.snap.gui.tabpane;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.image.DataBufferByte;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import com.qksnap.www.snap.gui.MainFrame;
import com.qksnap.www.snap.gui.SplashScreen;
import com.qksnap.www.snap.gui.tabpane.data.ImgFilter;
import com.qksnap.www.snap.io.SocketEngine;

public class Uploader extends JPanel implements ActionListener {
	/** background Image **/
	private Image img;
	/** our upload field **/
	private JTextField[] uploadField;
	/** our select field **/
	private JButton[] selectBtn;
	/** our check mark **/
	private JLabel[] checkMark;
	/** Upload Button **/
	private JButton upload;
	
	private JLabel restrict = new JLabel("Restrictions(?)");
	/** add item Button **/
	private JButton add;
	/** Y- Coord **/
	private int y = 75;
	/** index  **/
	private int index = 0;
	
	private JLabel sp;
	private final String headtxt = "<html>When the upload is finished, a sound will play and you may retrieve your upload via " +
									"ctrl-v or in the saved images tab!</html>";
	public Uploader(){
		setLayout(null);
		uploadField = new JTextField[3];
		selectBtn = new JButton[3];
		checkMark = new JLabel[3];
		setupGui();
	}

	public void setupGui(){
		try{
			sp = new JLabel(headtxt);
			sp.setBounds(25, 0, 525, 50);
			restrict.setBounds(280,13,128,42);
			restrict.setForeground(Color.blue);
		     final String html =
		                "<html><body>" +
		                "<p><b>Restrictions</b>:<br>" +
		                "<b>1) .jpg, .png or .gif only. (More will come soon!)<br>"+
		                "<b>2) 2mb size. (Future versions will offer more space)<br>";
		    restrict.setToolTipText(html);
			add(restrict);
			sp.repaint();
			add(sp);
			selectBtn[index] = new JButton("Select");
			selectBtn[index].setActionCommand(""+index);
			selectBtn[index].setBackground(Color.BLACK);
			selectBtn[index].setOpaque(true);
			selectBtn[index].setForeground(Color.WHITE);
			selectBtn[index].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			selectBtn[index].setHorizontalAlignment(JTextField.CENTER);
			selectBtn[index].addActionListener(this);
			uploadField[index] = new JTextField(" ");
			checkMark[index] = new JLabel(new ImageIcon(makeColorTransparent(
					Toolkit.getDefaultToolkit().getImage(getClass().getResource("/hitbox.png")), new Color(
							53, 53, 53))));
			uploadField[index].setEditable(false);
			checkMark[index].setVisible(false);
			checkMark[index].setBounds(40, y, 32, 32);
			selectBtn[index].setBounds(430,y+10,64,16);
			uploadField[index].setBounds(80, y, 350,32);
			upload = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/upload_button.png"))));

			upload.setHorizontalAlignment(JTextField.CENTER);
			upload.setBounds(240,125,125,24);
			upload.addActionListener(this);
			add(uploadField[index]);
			add(checkMark[index]);
			add(selectBtn[index]);
			index++;
			y += 25;
			add(upload);
			img = ImageIO.read(getClass().getResource("/mpbg.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public Image makeColorTransparent(Image image, final Color color)
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

		final ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}
	public void reset(){
		while(index > 1 ){
			remove(selectBtn[index-1]);
			remove(checkMark[index-1]);
			remove(uploadField[index-1]);
			index--;
		}
		checkMark[0].setVisible(false);
		uploadField[0].setText(" ");
		revalidate();
	}
	@Override
	public void actionPerformed(ActionEvent  e) {
		System.out.println(e.getActionCommand());
		if(e.getActionCommand().equalsIgnoreCase("Add Item") && index < 3){
			try{
				selectBtn[index] = new JButton("Select");
				selectBtn[index].setBackground(Color.BLACK);
				selectBtn[index].setOpaque(true);
				selectBtn[index].setForeground(Color.WHITE);
				selectBtn[index].setActionCommand(""+index);
				selectBtn[index].addActionListener(this);
				selectBtn[index].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				selectBtn[index].setHorizontalAlignment(JTextField.CENTER);
				uploadField[index] = new JTextField("");
				checkMark[index] = new JLabel(new ImageIcon(makeColorTransparent(
					ImageIO.read(getClass().getResource("/hitbox.png")), new Color(
							53, 53, 53))));
				checkMark[index].setVisible(false);
				uploadField[index].setEditable(false);
				checkMark[index].setBounds(115, y, 32, 32);
				selectBtn[index].setBounds(410,y+10,64,16);
				uploadField[index].setBounds(150, y, 275,32);
				add(uploadField[index]);
				add(checkMark[index]);
				add(selectBtn[index]);
				revalidate();
				index++;
				y += 25;
			} catch(Exception ex){
				ex.printStackTrace();
			}
		} else if(e.getActionCommand().equals("")){
			//System.out.println("upload posted");
			int items = index - 1;
			JOptionPane.showMessageDialog(this, "Your files will upload shorty\nthis will take a few seconds!");
			//SplashScreen.getSingleton().getMainFrame().getTrayIcon().displayMessage("QwikSnap", "Uploading your files now.", TrayIcon.MessageType.INFO);
			
			revalidate();
			while(items > -1){
				if(uploadField[items].getText().equals(" ")){
					items--;
					continue;
				}
				try {
				File uploaded = new File(uploadField[items].getText());
				if(uploaded.length() >  2000000){
					SplashScreen.getSingleton().getMainFrame().getTrayIcon().displayMessage("QwikSnap", "The file you are uploading exceeds our 1mb limit.", TrayIcon.MessageType.ERROR);
					continue;
				}
				SplashScreen.getSingleton().getMainFrame().setImg(extractBytes(uploaded));
				SplashScreen.getSingleton().getMainFrame().getSE().start();
				Thread.sleep(1500);
				uploadField[items].setText(SplashScreen.getSingleton().getMainFrame().getSE().getLink());
				uploadField[items].setEditable(true);
				checkMark[items].setVisible(true);
				items--;
				revalidate();
				Thread.sleep(500);
				} catch (IOException e1) {
					break;
				} catch (InterruptedException e2) {
					break;
				}
				
			}
		} else {
			if(e.getActionCommand().equals("Add Item")){
				SplashScreen.getSingleton().getMainFrame().getTrayIcon().displayMessage("QwikSnap", "You are at the file limit of uploads allowed!", TrayIcon.MessageType.ERROR);
				return;
			}
			JFileChooser jc = new JFileChooser();
			checkMark[0].setVisible(false);
			jc.addChoosableFileFilter(new ImgFilter());
		    int returnVal = jc.showOpenDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	try {
		    		uploadField[Integer.parseInt(e.getActionCommand())].setText(jc.getSelectedFile().getAbsolutePath());
		    		revalidate();
		    	} catch (Exception ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
		     }
		}
	}
	public byte[] extractBytes (File ImageName) throws IOException {
		 // open image
		 File imgPath = ImageName;
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			try {
				ImageIO.write(ImageIO.read(imgPath), "png", baos );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] imageInByte=baos.toByteArray();
			return imageInByte;
		}
	protected void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
		long start = System.currentTimeMillis();
		try {
			start += (1000/30);
			Thread.sleep(Math.max(0, start - System.currentTimeMillis()));
		} catch(Exception e){
			e.printStackTrace();
		}
		img.flush();
	}
}