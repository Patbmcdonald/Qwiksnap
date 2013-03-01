package com.qksnap.www.snap.gui.tabpane.data;

import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageLink implements Comparable {

	private String link;
	private String date;
	private String size;
	private String name;
	private String imgLoc;
	
	public ImageLink(String link, String string, String size, String name,String imgloc) {
		super();
		this.link = link;
		this.date = string;
		this.size = size;
		this.name = name;
		this.imgLoc = imgloc;
	}
	public String getLink() {
		return link;
	}
	public String getDate() {
		return date;
	}
	public String getSize(){
		return size;
	}
	public String getImgLoc(){
		return this.imgLoc;
	}
	public String getName() {
		return name;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return (this.getDate().compareTo(((ImageLink)o).getDate()));
	}
}
