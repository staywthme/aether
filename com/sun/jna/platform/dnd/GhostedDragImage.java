/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.dnd;

import com.sun.jna.platform.WindowUtils;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GhostedDragImage {
    private static final float DEFAULT_ALPHA = 0.5f;
    private Window dragImage;
    private Point origin;
    private static final int SLIDE_INTERVAL = 33;

    public GhostedDragImage(Component object, final Icon icon, Point point, final Point point2) {
        object = object instanceof Window ? (Window)object : SwingUtilities.getWindowAncestor((Component)object);
        object = ((Component)object).getGraphicsConfiguration();
        this.dragImage = new Window(JOptionPane.getRootFrame(), (GraphicsConfiguration)object){
            private static final long serialVersionUID = 1L;

            @Override
            public void paint(Graphics graphics) {
                icon.paintIcon(this, graphics, 0, 0);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(icon.getIconWidth(), icon.getIconHeight());
            }

            @Override
            public Dimension getMinimumSize() {
                return this.getPreferredSize();
            }

            @Override
            public Dimension getMaximumSize() {
                return this.getPreferredSize();
            }
        };
        this.dragImage.setFocusableWindowState(false);
        this.dragImage.setName("###overrideRedirect###");
        object = new Icon(){

            @Override
            public int getIconHeight() {
                return icon.getIconHeight();
            }

            @Override
            public int getIconWidth() {
                return icon.getIconWidth();
            }

            @Override
            public void paintIcon(Component component, Graphics graphics, int n2, int n3) {
                graphics = graphics.create();
                Area area = new Area(new Rectangle(n2, n3, this.getIconWidth(), this.getIconHeight()));
                area.subtract(new Area(new Rectangle(n2 + point2.x - 1, n3 + point2.y - 1, 3, 3)));
                graphics.setClip(area);
                icon.paintIcon(component, graphics, n2, n3);
                graphics.dispose();
            }
        };
        this.dragImage.pack();
        WindowUtils.setWindowMask(this.dragImage, (Icon)object);
        WindowUtils.setWindowAlpha(this.dragImage, 0.5f);
        this.move(point);
        this.dragImage.setVisible(true);
    }

    public void setAlpha(float f2) {
        WindowUtils.setWindowAlpha(this.dragImage, f2);
    }

    public void dispose() {
        this.dragImage.dispose();
        this.dragImage = null;
    }

    public void move(Point point) {
        if (this.origin == null) {
            this.origin = point;
        }
        this.dragImage.setLocation(point.x, point.y);
    }

    public void returnToOrigin() {
        final Timer timer = new Timer(33, null);
        timer.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent serializable) {
                serializable = GhostedDragImage.this.dragImage.getLocationOnScreen();
                Point point = new Point(GhostedDragImage.this.origin);
                int n2 = (point.x - ((Point)serializable).x) / 2;
                int n3 = (point.y - ((Point)serializable).y) / 2;
                if (n2 != 0 || n3 != 0) {
                    ((Point)serializable).translate(n2, n3);
                    GhostedDragImage.this.move((Point)serializable);
                    return;
                }
                timer.stop();
                GhostedDragImage.this.dispose();
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }
}

