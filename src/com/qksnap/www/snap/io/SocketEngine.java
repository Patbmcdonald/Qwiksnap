package com.qksnap.www.snap.io;

import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.qksnap.www.snap.gui.SplashScreen;
import com.qksnap.www.snap.gui.tabpane.Links;
import com.qksnap.www.snap.gui.tabpane.data.ImageLink;

/**
 * Author: Patrick McDonald <zeroeh@gmail.com> Date: 7/13/12
 * 
 * SocketEngine is our communcation gateway to the server.
 * 
 */
public class SocketEngine implements Runnable, ClipboardOwner {
	/** our thread **/
	private Thread listener;
	
    private final String CrLf = "\r\n";


	/**
	 * Going to input the image and two parents of the frame
	 * 
	 * @param img
	 *            - Our Image
	 * @param settings
	 *            - Link's instance
	 * @param mainFrame
	 *            - Main Frame instance
	 */
	public SocketEngine() {

	}

	/** Start our qwiksend thread **/
	private volatile File outputfile;
	public void start() {
		try {
			File loc = new File("quikkimgs/");
			String name = "quikkimgs/"+getCreateDate()+".png";
			outputfile = new File(name);
			if (!loc.exists()) {
				loc.mkdirs();
			}
			ImageIO.write(ImageIO.read((InputStream)new ByteArrayInputStream(SplashScreen.getSingleton().getMainFrame().getImg())), "PNG", outputfile);
			listener = new Thread(this);
			listener.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getCreateDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h mm ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
	private String link;
	public String getLink(){
		return this.link;
	}

	/** Stop our thread **/
	public void stop() {
		listener = null;
	}

    private String getMessage2(){
    	StringBuilder sb = new StringBuilder();
    	sb.append(CrLf + "-----------------------------4664151417711--"
                    + CrLf);
    	return sb.toString();
    }
    private String getMessage1(String mimetype, String filename){
    	 StringBuilder sb = new StringBuilder();
    	 sb.append("-----------------------------4664151417711" + CrLf);
         sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""+filename+"\""
                    + CrLf);
         sb.append("Content-Type: "+mimetype+"" + CrLf);
         sb.append(CrLf);
         return sb.toString();
    }

	/** Our main thread **/
	public void run() {
		try {
			URLConnection conn = null;
	        OutputStream os = null;
	        InputStream is = null;
	        	URL url = new URL("http://www.qksnap.com/upload.php");
	            conn = url.openConnection();
	            conn.setDoOutput(true);;
	            FileInputStream imgIs = new FileInputStream(outputfile);
	            byte[] imgData = new byte[imgIs.available()];
	            imgIs.read(imgData);
	            imgIs.close();
	            conn.setRequestProperty("Content-Type",
	                    "multipart/form-data; boundary=---------------------------4664151417711");
	            // might not need to specify the content-length when sending chunked
	            // data
	            
	            String part1 = getMessage1(new MimetypesFileTypeMap().getContentType(outputfile),outputfile.getName());
	            String part2 = getMessage2();
	            conn.setRequestProperty("Content-Length", String.valueOf((part1
	                    .length() + part2.length() + imgData.length)));
	            os = conn.getOutputStream();
	            os.write(part1.getBytes());

	            // SEND THE IMAGE
	            int index = 0;
	            int size = 1024;
	            do {
	                if ((index + size) > imgData.length) {
	                    size = imgData.length - index;
	                }
	                os.write(imgData, index, size);
	                index += size;
	            } while (index < imgData.length);
	            os.write(part2.getBytes());
	            os.flush();

	            is = conn.getInputStream();

	            char buff = 512;
	            int len;
	            byte[] data = new byte[buff];
	            do {
	                len = is.read(data);

	                if (len > 0) {
	                	JSONObject jsonObject = (JSONObject) JSONValue.parse(new String(data, 0, len));
	                	link = (String) jsonObject.get("link");
	                	String date = (String) jsonObject.get("date");
	                	String named = (String) jsonObject.get("name");
	                	double sizein = (Double) jsonObject.get("size");
	                	String sizetxt = round(sizein, 2, BigDecimal.ROUND_HALF_UP) +" KB";
	                	SplashScreen.getSingleton().getUserSettings().insertLink(link, date, sizetxt, named,outputfile.getPath());
					//	Links.getModel().addData(new ImageLink(link,date,sizetxt));
						setClipboardContents(link);
						new Thread(new Runnable() { // the wrapper thread is
													// unnecessary, unless it blocks on
													// the Clip finishing, see comments
									public void run() {
										try {
											Clip clip = AudioSystem.getClip();
											AudioInputStream inputStream = AudioSystem
													.getAudioInputStream(getClass()
															.getResource("/ding.wav"));
											clip.open(inputStream);
											clip.start();
										} catch (Exception e) {
											System.err.println(e.getMessage());
										}
									}
								}).start();
						SplashScreen.getSingleton().getMainFrame()
								.getTrayIcon()
								.displayMessage(
										"QwikSnap",
										"Your screenshot has been uploaded! Check ctrl-v for your link",
										MessageType.INFO);
	                }
	            } while (len > 0);

	        } catch (Exception e) {
	            e.printStackTrace();
	        } 
	   // }
		/*	switch (returnCode) {
			case 0:
				String link = new String(readBytes());
				this.link = link;
				settings.getModel().addData(new NRImage(link));
				setClipboardContents(link);
				new Thread(new Runnable() { // the wrapper thread is
											// unnecessary, unless it blocks on
											// the Clip finishing, see comments
							public void run() {
								try {
									Clip clip = AudioSystem.getClip();
									AudioInputStream inputStream = AudioSystem
											.getAudioInputStream(getClass()
													.getResource("/ding.wav"));
									clip.open(inputStream);
									clip.start();
								} catch (Exception e) {
									System.err.println(e.getMessage());
								}
							}
						}).start();
				mainFrame
						.getTrayIcon()
						.displayMessage(
								"QwikSnap",
								"Your screenshot has been uploaded! Check ctrl-v for your link",
								TrayIcon.MessageType.INFO);
				link = null;
				break;
			*/

	}

	private StringSelection stringSelection;
	private Clipboard clipboard;

	public void setClipboardContents(String aString) {
		stringSelection = new StringSelection(aString);
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, this);
	}
	public String round(double sizein, int precision, int roundingMode)
	{
	    BigDecimal bd = new BigDecimal(sizein);
	    BigDecimal rounded = bd.setScale(precision, roundingMode);
	    return Double.toString(rounded.doubleValue());
	}
	
	@Override
	public void lostOwnership(Clipboard arg0, Transferable arg1) {
		// TODO Auto-generated method stub

	}

}