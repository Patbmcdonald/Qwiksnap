package com.qksnap.www.snap.autoupdater;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


import com.qksnap.www.snap.gui.SplashScreen;
import com.qksnap.www.snap.gui.events.Download;
import com.qksnap.www.snap.gui.events.Download.DownloadType;
import com.qksnap.www.snap.util.Config;



public class Updater {
	/**
	 *  Start our setup for checking for updates.
	 * @param frame
	 */
	private String serverVer;
	public Updater() {
		init();
	}
	/** Begin checking for updates **/
	private void init() {
		getJson();
		SplashScreen.getSingleton().drawText(0,"Checking Launcher Version.");
		if (launcherUpdateNeeded()) {
			System.out.println("yes" + Config.currentVersion + " " + serverVer);
			new Download().startDownload(DownloadType.LAUNCHER);
		}
	}
	/** Is a launcher update needed? **/
	private boolean launcherUpdateNeeded() {
		return !Config.currentVersion.equals(serverVer);
	}

	private void getJson(){
		JSONObject jsonObject;
		try {
			jsonObject = readJsonFromUrl(Config.JSON_LINK);
		serverVer = (String) jsonObject.get("launchv");
		List<String> dlLinks = new ArrayList<String>();
		JSONArray array = (JSONArray)jsonObject.get("contents"); 
	        for (int i=0; i < array.size(); i++) { 
	          JSONObject list = (JSONObject) ((JSONObject)array.get(i)); 
	          dlLinks.add(list.get("link").toString());
	        }
	        
	        Config.setDLLinks(dlLinks);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }

	public JSONObject readJsonFromUrl(String url) throws IOException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);	 
	      JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonText);
	     
	      return jsonObject;
	    } finally {
	      is.close();
	    }
	  }

	
}