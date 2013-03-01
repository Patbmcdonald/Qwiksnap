package com.qksnap.www.snap.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
	
	
	public enum Level {
		ERROR,WARNING,INFORMATION,SYSTEM,EXCEPTION;
	}

	public static void writeLog(Level level, String text){
		switch(level){
			case ERROR:
			case WARNING:
			case EXCEPTION:
				PrintWriter pw = null;
				try {
					pw = new PrintWriter(new FileWriter(System.getProperty("user.dir") +"/qksnaplog.log",true));
				} catch (IOException e) {
						e.printStackTrace();
				}
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				String log = "["+dateFormat.format(cal.getTime())+"] - ["+level+"]: " + text + "\n";
				pw.append(log);
				pw.flush();
				pw.close();
				dateFormat = null;
				cal = null;
				pw = null;
				break;
		default: // we don't write others for now 
			break;
		}
	}
}
