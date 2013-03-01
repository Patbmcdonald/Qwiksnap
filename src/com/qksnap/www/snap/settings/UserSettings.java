package com.qksnap.www.snap.settings;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;

import com.qksnap.www.snap.gui.MainFrame;
import com.qksnap.www.snap.gui.SplashScreen;
import com.qksnap.www.snap.gui.tabpane.data.ImageLink;

/** This class will store our sqlite database values and initalize us **/

public class UserSettings {
	private String snapHK = "";
	private String selHK = "";
	private String selsnapHK = "";
	private Connection con;

	public UserSettings() {
		setup();
		SplashScreen.getSingleton().drawText(50, "Loading User settings - 50%");
		loadHotKeys();
		SplashScreen.getSingleton().drawText(100, "Loading User settings - 100%");
	}

	public void loadHotKeys() {
		try {
			try {
				Class.forName("org.sqlite.JDBC");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			con = DriverManager.getConnection("jdbc:sqlite:settings.db");
			Statement stat = con.createStatement();
			ResultSet res = stat.executeQuery("select * from hotkeys");
			while (res.next()) {
				snapHK = res.getString("ss");
				selsnapHK = res.getString("cs");
				selHK = res.getString("selss");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setup() {
		File file = new File("settings.db");
		if (file.exists()) {
			// if(file.delete());
			return;
		} else {
			try {
				Class.forName("org.sqlite.JDBC");

				// database path, if it's new database,
				// it will be created in the project folder
				con = DriverManager.getConnection("jdbc:sqlite:settings.db");
				Statement stat = con.createStatement();
				stat.executeUpdate("create table links(id INT,"
						+ "link varchar(255)," + "date varchar(30), size varchar(30), name varchar(30),imgloc varchar(30),"
						+ "primary key (id));");

				stat.executeUpdate("create table hotkeys(id INT,"
						+ "ss varchar(30)," + "cs varchar(30),"
						+ "selss varchar(30)," + "primary key (id));");

				//stat.executeUpdate("create table settings(id INT, value INT, primary key(id));");
				
				stat.executeUpdate("create table imagesaved(id INT, img blob, date varchar(30), primary key(id));");
				PreparedStatement prep = con
						.prepareStatement("insert into hotkeys values(?,?,?,?);");
				// default hot keys
				prep.setString(3, "ctrl shift 1");
				prep.setString(2, "ctrl shift 3");
				prep.setString(4, "ctrl shift 2");
				prep.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String getSnapHK() {
		return snapHK;
	}

	public String getSelHK() {
		return selHK;
	}

	public String getSelsnapHK() {
		return selsnapHK;
	}

	public void insertLink(String link, String time, String size,String name,String imgloc) {
		try {
			PreparedStatement prep = con
					.prepareStatement("insert into links values(?,?,?,?,?,?);");
			// default hot keys
			prep.setString(2, link);
			prep.setString(3, time);
			prep.setString(4, size);
			prep.setString(5, name);
			prep.setString(6, imgloc);
			prep.execute();
			SplashScreen.getSingleton().getMainFrame().getLinks().addScreenshot();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<ImageLink> getLinks() {
		ArrayList<ImageLink> links = new ArrayList<ImageLink>();
		try {
			con = DriverManager.getConnection("jdbc:sqlite:settings.db");
			Statement stat = con.createStatement();
			ResultSet res = stat.executeQuery("select * from links");
			while (res.next()) {
				links.add(new ImageLink(res.getString("link"), res
						.getString("date"), res.getString("size"),res.getString("name"),res.getString("imgloc")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return links;
	}

	public void update(String selHKK, String snapHKK, String selsnapHKK,int i) {
		try {
			Statement stat = con.createStatement();
			stat.executeUpdate("update hotkeys set ss='" + snapHKK + "';");
			stat.executeUpdate("update hotkeys set selss='" + selHKK + "';");
			stat.executeUpdate("update hotkeys set cs='" + selsnapHKK + "';");
			ResultSet res = stat.executeQuery("select * from hotkeys");
			while (res.next()) {
				this.snapHK = res.getString("ss");
				this.selsnapHK = res.getString("cs");
				this.selHK = res.getString("selss");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delLink(String link) {
	try {
			Statement stat = con.createStatement();
			stat.executeUpdate("delete from links where link='"+link+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}

	public boolean autoStartEnabled() {
		try {
			Statement stat = con.createStatement();
			ResultSet res = stat.executeQuery("select * from settings");
			while (res.next()) {
				if(res.getInt("value") == 1){
					res.close();
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
