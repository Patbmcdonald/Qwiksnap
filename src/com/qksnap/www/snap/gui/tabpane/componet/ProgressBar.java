package com.qksnap.www.snap.gui.tabpane.componet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicProgressBarUI;

/**
 * 
 * Custom Progress Bar that uses an image as the background
 * @author Patrick "zeroeh"
 *
 */
public class ProgressBar extends BasicProgressBarUI {
	
        @Override
        public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                // for antialiasing geometric shapes
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

                // for antialiasing text
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                // to go for quality over speed
                g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                                RenderingHints.VALUE_RENDER_QUALITY);
                super.paint(g, c);
        }

        protected void paintDeterminate(Graphics g, JComponent c) {
                if (progressBar.getOrientation() == JProgressBar.VERTICAL) {
                        super.paintDeterminate(g, c);
                        return;
                }
                Insets b = progressBar.getInsets(); // area for border
                int width = progressBar.getWidth();
                int height = progressBar.getHeight();
                int barRectWidth = width - (b.right + b.left);
                int barRectHeight = height - (b.top + b.bottom);
                // amount of progress to draw
                int amountFull = getAmountFull(b, barRectWidth, barRectHeight);

                Graphics2D g2 = (Graphics2D) g;

                g2.setColor(progressBar.getBackground());
                g2.fillRect(0, 0, width - 1, height - 1);

                g2.setColor(Color.white);
                try {
					g2.drawImage(ImageIO.read(getClass().getResource("/progressbar.png")), b.left, b.top, amountFull - 1, barRectHeight - 1,null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                if (progressBar.isStringPainted()) {
                        paintString(g, b.left, b.top, barRectWidth, barRectHeight,
                                        amountFull, b);
                }
        }

        @Override
        public Dimension getPreferredSize(JComponent c) {
                Dimension dim = super.getPreferredSize(c);
                if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
                        if (dim.width < dim.height * 4)
                                dim.width = dim.height * 4;
                }
                return dim;
        }

}