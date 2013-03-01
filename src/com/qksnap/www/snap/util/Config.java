package com.qksnap.www.snap.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.List;

import javax.imageio.ImageIO;

public class Config {
	public static final String JSON_LINK = "http://www.qksnap.com/update.json";
	
	private static List<String> dlLinks;
	
	private static String OS = System.getProperty("os.name").toLowerCase();

	public static String currentVersion = "1.0.8";
	
	public static String getArchivedName(String link) {
		int lastSlashIndex = link.lastIndexOf('/');
		if (lastSlashIndex >= 0
				&& lastSlashIndex < link.length() - 1) {
			return link.substring(lastSlashIndex + 1);
		} else {
		}
		return "";
	}
	 public static byte[] getUrlImageAsByteArray(String ins) throws IOException {
		 	URL url = new URL(ins);
	        URLConnection connection = url.openConnection();
	        // Since you get a URLConnection, use it to get the InputStream
	        InputStream in = connection.getInputStream();
	        // Now that the InputStream is open, get the content length
	        int contentLength = connection.getContentLength();

	        // To avoid having to resize the array over and over and over as
	        // bytes are written to the array, provide an accurate estimate of
	        // the ultimate size of the byte array
	        ByteArrayOutputStream tmpOut;
	        if (contentLength != -1) {
	            tmpOut = new ByteArrayOutputStream(contentLength);
	        } else {
	            tmpOut = new ByteArrayOutputStream(16384); // Pick some appropriate size
	        }

	        byte[] buf = new byte[512];
	        while (true) {
	            int len = in.read(buf);
	            if (len == -1) {
	                break;
	            }
	            tmpOut.write(buf, 0, len);
	        }
	        in.close();
	        tmpOut.close(); // No effect, but good to do anyway to keep the metaphor alive

	        byte[] array = tmpOut.toByteArray();

	        //Lines below used to test if file is corrupt
	        //FileOutputStream fos = new FileOutputStream("C:\\abc.pdf");
	        //fos.write(array);
	        //fos.close();

	        return array;
	 }
	 
	 public static byte[] scaleImage (byte[] fileData, int width, int height) {
	    	ByteArrayInputStream in = new ByteArrayInputStream(fileData);
	    	try {
	    		BufferedImage img = ImageIO.read(in);
	    		if(height == 0) {
	    			height = (width * img.getHeight())/ img.getWidth(); 
	    		}
	    		if(width == 0) {
	    			width = (height * img.getWidth())/ img.getHeight();
	    		}
	    		Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	    		BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    		imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0,0,0), null);

	    		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

	    		ImageIO.write(imageBuff, "png", buffer);

	    		return buffer.toByteArray();
	    	} catch (IOException e) {

	    	}
	    	return null;
	    }
	 public static byte[] getAsByteArray(URL url) throws IOException {
	        URLConnection connection = url.openConnection();
	        // Since you get a URLConnection, use it to get the InputStream
	        InputStream in = connection.getInputStream();
	        // Now that the InputStream is open, get the content length
	        int contentLength = connection.getContentLength();

	        // To avoid having to resize the array over and over and over as
	        // bytes are written to the array, provide an accurate estimate of
	        // the ultimate size of the byte array
	        ByteArrayOutputStream tmpOut;
	        if (contentLength != -1) {
	            tmpOut = new ByteArrayOutputStream(contentLength);
	        } else {
	            tmpOut = new ByteArrayOutputStream(16384); // Pick some appropriate size
	        }

	        byte[] buf = new byte[512];
	        while (true) {
	            int len = in.read(buf);
	            if (len == -1) {
	                break;
	            }
	            tmpOut.write(buf, 0, len);
	        }
	        in.close();
	        tmpOut.close(); // No effect, but good to do anyway to keep the metaphor alive

	        byte[] array = tmpOut.toByteArray();

	        //Lines below used to test if file is corrupt
	        //FileOutputStream fos = new FileOutputStream("C:\\abc.pdf");
	        //fos.write(array);
	        //fos.close();

	        return array;
	    }

	public static void setDLLinks(List<String> in){
		dlLinks = in;
	}
	public static String getLaunchURL() {
		if (isMac()) {
			return dlLinks.get(0);
		} else {
			return dlLinks.get(1);
		}
	}
	public static BufferedImage toCompatibleImage(BufferedImage image)
	{
		// obtain the current system graphical settings
		GraphicsConfiguration gfx_config = GraphicsEnvironment.
			getLocalGraphicsEnvironment().getDefaultScreenDevice().
			getDefaultConfiguration();

		/*
		 * if image is already compatible and optimized for current system 
		 * settings, simply return it
		 */
		if (image.getColorModel().equals(gfx_config.getColorModel()))
			return image;

		// image is not optimized, so create a new image that is
		BufferedImage new_image = gfx_config.createCompatibleImage(
				image.getWidth(), image.getHeight(), image.getTransparency());

		// get the graphics context of the new image to draw the old image on
		Graphics2D g2d = (Graphics2D) new_image.getGraphics();

		// actually draw the image and dispose of context no longer needed
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();

		// return the new optimized image
		return new_image; 
	}
	
	public static boolean isWindows() {
		 
		return (OS.indexOf("win") >= 0);
 
	}
 
	public static boolean isMac() {
 
		return (OS.indexOf("mac") >= 0);
 
	}
 
	public static boolean isUnix() {
 
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
 
	}
 
	public static boolean isSolaris() {
 
		return (OS.indexOf("sunos") >= 0);
 
	}
}
