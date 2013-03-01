package com.qksnap.www.snap.io;

import com.qksnap.www.snap.gui.MainFrame;

public class CheckOffline implements Runnable {
	private MainFrame mf;
	private SocketEngine eng;
	private Thread listener;
	public CheckOffline(MainFrame main){
		this.mf = main;
		listener = new Thread(this);
		listener.start();
	}
    public void run() {
    	try {
    		while(true){
        		
    		}
        } catch (Exception e) {
            //threadMessage("I wasn't done!");
        	e.printStackTrace();
        }
       }
    
   
}
