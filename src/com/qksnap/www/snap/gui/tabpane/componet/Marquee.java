package com.qksnap.www.snap.gui.tabpane.componet;

import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;

public class Marquee extends JLabel {
    public static final int MARQUEE_SPEED_DIV = 25;
    public static final int REPAINT_WITHIN_MS = 5;

    /**
     * 
     */
    private static final long serialVersionUID = -7737312573505856484L;

    /**
     * 
     */
    public Marquee() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param image
     * @param horizontalAlignment
     */
    public Marquee(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param image
     */
    public Marquee(Icon image) {
        super(image);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param text
     * @param icon
     * @param horizontalAlignment
     */
    public Marquee(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param text
     * @param horizontalAlignment
     */
    public Marquee(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param text
     */
    public Marquee(String text) {
        super(text);
    }



    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        g.translate(0, (int)((System.currentTimeMillis() / MARQUEE_SPEED_DIV) % (getHeight() * 2)) - getHeight());
        super.paintComponent(g);
        repaint(REPAINT_WITHIN_MS);
    }
}