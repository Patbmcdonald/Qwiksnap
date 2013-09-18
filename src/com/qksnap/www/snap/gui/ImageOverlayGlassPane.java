package com.qksnap.www.snap.gui;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.qksnap.www.snap.gui.events.*;
import com.sun.awt.AWTUtilities;

public class ImageOverlayGlassPane extends JPanel implements FocusListener {
	public Point start = new Point();
	public Point down = new Point();
	private Rectangle2D rect;
	private boolean drawing;
	private AreaDragListener ml3;
	private Area area = new Area();
	private AlphaComposite ta;
	private AlphaComposite old;
	private SoftReference<BufferedImage> in;
	public ImageOverlayGlassPane() {
		setOpaque(false);
		ml3 = new AreaDragListener(true);
		addMouseListener(ml3);
		addMouseMotionListener(ml3);
		addFocusListener(this);
		addComponentListener(new ComponentAdapter() {
			public void componentMoved(ComponentEvent e) {
				setLocation(0, 0);
			}
		});
		
		
	}
	 public void setVisible(boolean v) {
		    // Make sure we grab the focus so that key events don't go astray.
		    if (v)
		      requestFocus();
			addComponentListener(new ComponentAdapter() {
				public void componentMoved(ComponentEvent e) {
					setLocation(0, 0);
				}
			});
		    super.setVisible(v);
		  }

		  // Once we have focus, keep it if we're visible
		  public void focusLost(FocusEvent fe) {
		    if (isVisible())
		      requestFocus();
		  }

		  public void focusGained(FocusEvent fe) {
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

	protected void paintComponent(Graphics g) {
		super.printComponents(g);
		long start = System.currentTimeMillis();
		Graphics2D g2 = (Graphics2D) g;
		BufferedImage img = getBufferedImage();
		g.drawImage(img , 0, 0, null);
	//	g2.clearRect(0, 0, img.getWidth(), img.getHeight());
		area.add(new Area(new Rectangle2D.Float(0, 0, img.getWidth(),
				img.getHeight())));
		ta = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.5f);
		old = (AlphaComposite) g2.getComposite();
		Font saved = g2.getFont();
		g2.setColor(Color.black.darker());
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
		g2.setFont(saved);
		g2.dispose();
		area.reset();
		ta = null;
		old = null;
		try {
			// Delay depending on how far behind current time we are.
			start += (1000 / 45);
			Thread.sleep(Math.max(0, start - System.currentTimeMillis()));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public boolean needUpdate = false;
	
	public BufferedImage getBufferedImage() {
		if (in != null && !needUpdate) {
			final BufferedImage cachedImg = in.get();
			if (cachedImg != null) {
				return cachedImg;
			}
		}

		// released by the Garbage Collector
		// OR, never loaded
		try {
			in = new SoftReference<BufferedImage>(ImageIO.read(new ByteArrayInputStream(SplashScreen.getSingleton().getMainFrame().getImg())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return in.get();
	}
}