package com.qksnap.www.snap.gui.events;

/**
 * Copyright (c) 2012-2015 Patrick "Zeroeh"
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

import java.awt.Desktop;
import java.io.File;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLConnection;
import java.net.URL;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import com.qksnap.www.snap.gui.SplashScreen;
import com.qksnap.www.snap.util.Config;
import com.qksnap.www.snap.util.Logger;
import com.qksnap.www.snap.util.Logger.Level;

public class Download {
	
	/** Start our download based on the download type
	 * 
	 * @param type - Download Type
	 * @param up - Updater instance value
	 */
    public void startDownload(DownloadType type){
    		for(DownloadInstance di : DownloadInstance.values()){
    			if(di.getType() == type){
    				di.wget(type);
    			}
    		}
    	}
    	/** Download Enums **/
		public enum DownloadInstance {
			LAUNCHER((String)Config.getLaunchURL(), System.getProperty("user.dir")+"/update/", DownloadType.LAUNCHER);
			;
			/** Our file buffer **/
			private final int BUFFER = 1024;
	        
	        /** Download Type **/
	        private DownloadType type;
	        
	        /** URL Link **/
	        private String urlLink;
	        
	        /** Output Location **/
	        private String output;
	       
	        DownloadInstance(String link, String output, DownloadType type){
	        	this.urlLink = link; 
	        	this.output = output;
	        	this.type = type;
	        }
	        /** Download Listeners **/
	        private List<DownloadListener> downloadListeners = new ArrayList<DownloadListener>();
	       
	        	
	        /** Get Download Type **/
	        public DownloadType getType(){
	        	return type;
	        }
	        
			 /**
			  *  Download the files and activate downloadComplete when complete
			  * @param type - DownloadType
			  * @return
			  */
	        public boolean wget(final DownloadType type) {
	        	this.addDownloadListener(new DownloadListener(){
					@Override
					public void downloadComplete() {
						switch(type){
			        		case LAUNCHER:
			        			
			        			SplashScreen.getSingleton().drawText(100, "QwikSnap Updated.");
			        			try {
									Desktop.getDesktop().open(new File(output+Config.getArchivedName(urlLink)));
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (IllegalArgumentException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (SecurityException e1) {
								// TODO Auto-generated catch block
									e1.printStackTrace();
							}
			        			System.exit(3);
			        			break;
			        	}
					}

					@Override
					public void downloadInterrupted() {
						SplashScreen.getSingleton().drawText(100, "Connection dropped...");
					}
	        		
	        	});
	        	try { 
	        		 URL url = new URL(urlLink);
	            	 URLConnection conn = url.openConnection();
	            	 InputStream inStream = conn.getInputStream();
	            	 BufferedOutputStream bufOut = new BufferedOutputStream(new FileOutputStream(new File(output+Config.getArchivedName(urlLink))));
	            	 byte buffer[] = new byte[BUFFER];
	            	 int writtenTotal = 0;
	                 int length = conn.getContentLength();
	            	 while (true) {
	            		 int nRead = inStream.read(buffer, 0, buffer.length);
	            		 if (nRead <= 0)
	            			 break;
	            		 writtenTotal += nRead;
	            		 bufOut.write(buffer, 0, nRead);
	            		 
	            		 int percentage = (int)(((double)writtenTotal / (double)length) * 100D);
	                    SplashScreen.getSingleton().drawText(percentage, "Downloading "+getDownloadName(type)+": " + percentage + "%");
	            	 }
	            	    bufOut.flush();
	            	    inStream.close();
	        	} catch (Exception e) {
	        		StringWriter sw = new StringWriter();
	        		PrintWriter pw = new PrintWriter(sw);
	        		e.printStackTrace(pw);
	        		Logger.writeLog(Level.EXCEPTION,sw.toString());
	        	} 	
	        	fireDownloadCompletedEvent();
				return true;
	        }
	        
	    	/** Write stream **/
	    	public void writeStream(InputStream In, OutputStream Out)
	    			throws IOException {
	    		byte Buffer[] = new byte[4096];
	    		int Len;
	    		while ((Len = In.read(Buffer)) >= 0) {
	    			Out.write(Buffer, 0, Len);
	    		}
	    		In.close();
	    		Out.close();
	    	}
	        /**
	         *  Return our Download Name bsed on Download Type
	         * @param type
	         * @return
	         */
	        public String getDownloadName(DownloadType type){
	        	switch(type){
	    		case LAUNCHER:		
	    			return "QwikSnap";
	    	}
	        	return null;
	   
	        }
	        
	        /** 
	         *  Fire our Download complete Event to all event listeners
	         */
	        public void fireDownloadCompletedEvent(){
	        	Iterator<DownloadListener> itr = downloadListeners.iterator(); 
	        	while(itr.hasNext()) {
	        		DownloadListener element = (DownloadListener) itr.next();
	        		element.downloadComplete();
	        	}
	        }
	        /** 
	         *  Fire our Download Intrupption  Event to all event listeners
	         */
	        public void fireDownloadInterruptedEvent(){
	        	Iterator<DownloadListener> itr = downloadListeners.iterator(); 
	        	while(itr.hasNext()) {
	        		DownloadListener element = (DownloadListener) itr.next();
	        		element.downloadInterrupted();
	        	}
	        }
	        
	        public void addDownloadListener(DownloadListener in){
	        	if(!downloadListeners.contains(in)){
	        		downloadListeners.add(in);
	        	}
	        }
	        
	        public void removeDownloadListener(DownloadListener in){
	        	Iterator<DownloadListener> itr = downloadListeners.iterator(); 
	        	while(itr.hasNext()) {
	        		DownloadListener element = (DownloadListener) itr.next();
	        		if(element == in){	
	        			itr.remove();
	        		}
	        	} 
	        }

		
		}

		public enum DownloadType {
		    LAUNCHER
		}
		
		public interface DownloadListener {
			public void downloadComplete();
			public void downloadInterrupted();
		}
		class DownloadEvent extends EventObject {
		      public DownloadEvent(Object source) {
		          super(source);
		      }
		  }


}