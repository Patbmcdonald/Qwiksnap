package com.qksnap.www.snap.gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.qksnap.www.snap.gui.events.*;
import com.sun.awt.AWTUtilities;

public class SelectGlassPane extends JPanel {
	/** Our start point for rectangle drawing **/ 
	public Point start = new Point();
	/** Our finished point **/
	public Point down = new Point();
	/** Our rectangle **/
	private Rectangle2D rect;
	/** Are we drawing? **/
	private boolean drawing;
	/** Mouse Listener **/
	private AreaDragListener ml2;
	/** Constructor **/ 
	public SelectGlassPane() {
		super(null);
		setOpaque(false);
		ml2 = new AreaDragListener(false);
		addMouseListener(ml2);
		addMouseMotionListener(ml2);
		addComponentListener(new ComponentAdapter() {
			public void componentMoved(ComponentEvent e) {
				setLocation(0, 0);
			}
		});
	}

	public Rectangle2D getRect() {
		return rect;
	}

	public void setRect(Rectangle2D rect) {
		this.rect = rect;
	}

	public void setFinish(Point point) {
		this.down = point;
	}

	public void setStart(Point point) {
		this.start = point;
	}

	public boolean isDrawing() {
		return drawing;
	}

	public void setDrawing(boolean s) {
		this.drawing = s;
	}
	/** Override our paint Component **/
	protected void paintComponent(Graphics g) {
		 super.paintComponent(g);
		 // Set Window to 50% Opacity 
		AWTUtilities.setWindowOpacity(SplashScreen.getSingleton().getMainFrame().getNRFrame(), 0.5f);
		Graphics2D g2 = (Graphics2D) g.create();
		Area area = new Area();
		AlphaComposite old = (AlphaComposite) g2.getComposite();
		Font saved = g2.getFont();
		area.add(new Area(new Rectangle2D.Float(0,0,getWidth(),getHeight())));
		AlphaComposite ta = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
		g2.setComposite(ta);
		if (getRect() != null && isDrawing()) {
			area.subtract(new Area(getRect()));
			g2.setColor(Color.GREEN.darker());
			g2.setStroke(new BasicStroke(5));
			g2.draw(getRect());
			g2.setStroke(g2.getStroke()); // reset our stroke
			g2.setColor(Color.black.darker());
			g2.fill(area);
		} else {
			g2.setColor(Color.black.darker());
			g2.fill(area);
			g2.setColor(Color.red.darker());
			g2.setComposite(old);
			g2.setFont(new Font("Serif", Font.PLAIN, 36));
			g2.drawString("Click and drag to make selection!",
					(getWidth() / 2) - 150, getHeight() / 2);
		}
		area.reset();
		area = null;
		g2.setFont(saved);
		g2.dispose();
	}
}