package com.qksnap.www.snap.gui.tabpane.ui;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TabUI extends BasicTabbedPaneUI {

	private static final Insets NO_INSETS = new Insets(0, 0, 0, 0);
	private ColorSet selectedColorSet;
	private ColorSet defaultColorSet;
	private ColorSet hoverColorSet;
	private boolean contentTopBorderDrawn = true;
	private Color lineColor = new Color(158, 158, 158);
	private Color dividerColor = new Color(200, 200, 200);
	private Insets contentInsets = new Insets(10, 10, 10, 10);
	private int lastRollOverTab = -1;
	private BufferedImage tabImg;
	public static ComponentUI createUI(JComponent c) {
		return new TabUI();
	}

	public TabUI() {

		selectedColorSet = new ColorSet();
		selectedColorSet.topGradColor1 = new Color(247, 247, 247);
		selectedColorSet.topGradColor2 = new  Color(247, 247, 247);
		try {
			tabImg = ImageIO.read(getClass().getResource("/tabimage.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		selectedColorSet.bottomGradColor1 = new  Color(247, 247, 247);
		selectedColorSet.bottomGradColor2 = new Color(247, 247, 247);
		
		defaultColorSet = new ColorSet();
		defaultColorSet.topGradColor1= new Color(247, 247, 247);
		defaultColorSet.topGradColor2 = new Color(247, 247, 247);

		defaultColorSet.bottomGradColor1 = new Color(247, 247, 247);
		defaultColorSet.bottomGradColor2 = new Color(247, 247, 247);

		hoverColorSet = new ColorSet();
		hoverColorSet.topGradColor1 = new Color(247, 247, 247);
		hoverColorSet.topGradColor2 = new Color(247, 247, 247);

		hoverColorSet.bottomGradColor1 = new Color(247, 247, 247);
		hoverColorSet.bottomGradColor2 = new Color(247, 247, 247);
		maxTabHeight = 40;

		setContentInsets(0);
	}

	public void setContentTopBorderDrawn(boolean b) {
		contentTopBorderDrawn = b;
	}

	public void setContentInsets(Insets i) {
		contentInsets = i;
	}

	public void setContentInsets(int i) {
		contentInsets = new Insets(i, i, i, i);
	}

	public int getTabRunCount(JTabbedPane pane) {
		return 1;
	}
	  @Override
	  protected void layoutLabel(int tabPlacement,
	                             FontMetrics metrics, int tabIndex,
	                             String title, Icon icon,
	                             Rectangle tabRect, Rectangle iconRect,
	                             Rectangle textRect, boolean isSelected ) {
	    textRect.x = textRect.y = iconRect.x = iconRect.y = 0;
	    View v = getTextViewForTab(tabIndex);
	    if (v != null) {
	      tabPane.putClientProperty("html", v);
	    }
	    SwingUtilities.layoutCompoundLabel((JComponent) tabPane,
	                                       metrics, title, icon,
	                                       SwingUtilities.CENTER,
	                                       SwingUtilities.CENTER, //CENTER, <----
	                                       SwingUtilities.CENTER,
	                                       SwingUtilities.CENTER,
	                                       tabRect,
	                                       iconRect,
	                                       textRect,
	                                       textIconGap);
	    tabPane.putClientProperty("html", null);
	    switch(tabIndex){
	    case 0:
	    	tabRect.x = 110;
	    	textRect.x = 110;
	    	tabRect.y = 7;
	    	textRect.y = 7;
	    	break;
	    case 1:
	    	tabRect.x = 170;
	    	textRect.x = 170;
	    	tabRect.y = 7;
	    	textRect.y = 7;
	    	break;
	    case 2:
	    	tabRect.x = 245;
	    	textRect.x = 245;
	    	tabRect.y = 7;
	    	textRect.y = 7;
	    	break;
	    case 3:
	    	tabRect.x = 355;
	    	textRect.x = 355;
	    	tabRect.y = 7;
	    	textRect.y = 7;
	     	break;
	    case 4:
	    	tabRect.x = 435;
	    	textRect.x = 435;
	    	tabRect.y = 7;
	    	textRect.y = 7;
	    	break;
	    }
	    int xNudge = getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
	    int yNudge = getTabLabelShiftY(tabPlacement, tabIndex, isSelected);
	    iconRect.x += xNudge;
	    iconRect.y += yNudge;
	    textRect.x += xNudge;
	    textRect.y += yNudge;
	    tabPane.repaint();
	  }

	protected void installDefaults() {
		super.installDefaults();

		RollOverListener l = new RollOverListener();
		tabPane.addMouseListener(l);
		tabPane.addMouseMotionListener(l);
		boldFont = tabPane.getFont().deriveFont(Font.BOLD);
		tabAreaInsets = NO_INSETS;
		tabInsets = new Insets(0, 0, 0, 1);
	}

	protected boolean scrollableTabLayoutEnabled() {
		return false;
	}

	protected Insets getContentBorderInsets(int tabPlacement) {
		return contentInsets;
	}

	protected int calculateTabHeight(int tabPlacement, int tabIndex,
			int fontHeight) {
		return 25;
	}

	protected int calculateTabWidth(int tabPlacement, int tabIndex,
			FontMetrics metrics) {
		int w = super.calculateTabWidth(tabPlacement, tabIndex, metrics);
		int wid = metrics.charWidth('M');
		w += wid * 2;
		return w;
	}

	protected int calculateMaxTabHeight(int tabPlacement) {
		return 25;
	}

	protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
		super.paintTabArea(g, tabPlacement, selectedIndex);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(tabImg, 0, 0,tabPane.getWidth(),0,null);
		//super.paintTabArea(g, tabPlacement, selectedIndex);
	}

	protected void paintTabBackground(Graphics g, int tabPlacement,
			int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		Graphics2D g2d = (Graphics2D) g;
		ColorSet colorSet;

		Rectangle rect = rects[tabIndex];

		if (isSelected) {
			colorSet = selectedColorSet;
		} else if (getRolloverTab() == tabIndex) {
			colorSet = hoverColorSet;
		} else {
			colorSet = defaultColorSet;
		}

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		int width = rect.width;
		int xpos = rect.x;
		if (tabIndex > 0) {
			width--;
			xpos++;
		}

	}

	protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
			int x, int y, int w, int h, boolean isSelected) {
		Rectangle rect = getTabBounds(tabIndex, new Rectangle(x, y, w, h));
	}

	protected void paintContentBorderTopEdge(Graphics g, int tabPlacement,
			int selectedIndex, int x, int y, int w, int h) {

	}

	protected void paintContentBorderRightEdge(Graphics g, int tabPlacement,
			int selectedIndex, int x, int y, int w, int h) {
		// Do nothing
	}

	protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement,
			int selectedIndex, int x, int y, int w, int h) {
		// Do nothing
	}

	protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement,
			int selectedIndex, int x, int y, int w, int h) {
		// Do nothing
	}

	protected void paintFocusIndicator(Graphics g, int tabPlacement,
			Rectangle[] rects, int tabIndex, Rectangle iconRect,
			Rectangle textRect, boolean isSelected) {
		// Do nothing
	}

	protected int getTabLabelShiftY(int tabPlacement, int tabIndex,
			boolean isSelected) {
		return 0;
	}
	
	private Font boldFont = null;
	 
	protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected){
		if (isSelected){
			super.paintText(g, tabPlacement, boldFont, metrics, tabIndex, title, textRect, isSelected);
		}else{
			super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
		}
	}

	private class ColorSet {
		Color topGradColor1;
		Color topGradColor2;

		Color bottomGradColor1;
		Color bottomGradColor2;
	}

	private class RollOverListener implements MouseMotionListener,
			MouseListener {

		public void mouseDragged(MouseEvent e) {
		}

		public void mouseMoved(MouseEvent e) {
			checkRollOver();
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
			checkRollOver();
		}

		public void mouseExited(MouseEvent e) {
			tabPane.repaint();
		}

		private void checkRollOver() {
			int currentRollOver = getRolloverTab();
			if (currentRollOver != lastRollOverTab) {
				lastRollOverTab = currentRollOver;
				Rectangle tabsRect = new Rectangle(0, 0, tabPane.getWidth(), 20);
				tabPane.repaint(tabsRect);
			}
		}
	}
}
