/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.ds.dummy;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

public class WebcamDummyDevice
implements WebcamDevice {
    private static final Dimension[] DIMENSIONS = new Dimension[]{WebcamResolution.QQVGA.getSize(), WebcamResolution.QVGA.getSize(), WebcamResolution.VGA.getSize()};
    private AtomicBoolean open = new AtomicBoolean(false);
    private Dimension resolution = DIMENSIONS[0];
    private final String name;
    byte r = (byte)(Math.random() * 127.0);
    byte g = (byte)(Math.random() * 127.0);
    byte b = (byte)(Math.random() * 127.0);

    public WebcamDummyDevice(int n2) {
        this.name = "Dummy Webcam " + n2;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Dimension[] getResolutions() {
        return DIMENSIONS;
    }

    @Override
    public Dimension getResolution() {
        return this.resolution;
    }

    @Override
    public void setResolution(Dimension dimension) {
        this.resolution = dimension;
    }

    private void drawRect(Graphics2D graphics2D, int n2, int n3) {
        int n4 = (int)((double)n2 * Math.random() / 1.5);
        n3 = (int)((double)n3 * Math.random() / 1.5);
        int n5 = (int)((double)n2 * Math.random() / 1.5);
        n2 = (int)((double)n2 * Math.random() / 1.5);
        graphics2D.setColor(new Color((int)(2.147483647E9 * Math.random())));
        graphics2D.fillRect(n4, n3, n5, n2);
    }

    @Override
    public BufferedImage getImage() {
        if (!this.isOpen()) {
            throw new WebcamException("Webcam is not open");
        }
        try {
            Thread.sleep(33L);
        }
        catch (InterruptedException interruptedException) {
            return null;
        }
        Dimension dimension = this.getResolution();
        int n2 = dimension.width;
        int n3 = dimension.height;
        String string = this.getName();
        Object object = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Object object2 = ((GraphicsEnvironment)object).getDefaultScreenDevice().getDefaultConfiguration();
        object2 = ((GraphicsConfiguration)object2).createCompatibleImage(n2, n3);
        object = ((GraphicsEnvironment)object).createGraphics((BufferedImage)object2);
        byte by = this.r;
        this.r = (byte)(by + 1);
        byte by2 = this.g;
        this.g = (byte)(by2 + 1);
        byte by3 = this.b;
        this.b = (byte)(by3 + 1);
        ((Graphics2D)object).setBackground(new Color(Math.abs(by), Math.abs(by2), Math.abs(by3)));
        ((Graphics)object).clearRect(0, 0, n2, n3);
        this.drawRect((Graphics2D)object, n2, n3);
        this.drawRect((Graphics2D)object, n2, n3);
        this.drawRect((Graphics2D)object, n2, n3);
        this.drawRect((Graphics2D)object, n2, n3);
        this.drawRect((Graphics2D)object, n2, n3);
        Serializable serializable = new Font("sans-serif", 1, 16);
        ((Graphics)object).setFont((Font)serializable);
        serializable = ((Graphics)object).getFontMetrics((Font)serializable);
        n2 = (n2 - ((FontMetrics)serializable).stringWidth(string)) / 2;
        n3 = (n3 - ((FontMetrics)serializable).getHeight()) / 2 + ((FontMetrics)serializable).getHeight() / 2;
        ((Graphics2D)object).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics)object).setColor(Color.BLACK);
        ((Graphics2D)object).drawString(string, n2 + 1, n3 + 1);
        ((Graphics)object).setColor(Color.WHITE);
        ((Graphics2D)object).drawString(string, n2, n3);
        ((Graphics)object).dispose();
        ((Image)object2).flush();
        return object2;
    }

    @Override
    public void open() {
        this.open.compareAndSet(false, true);
    }

    @Override
    public void close() {
        this.open.compareAndSet(true, false);
    }

    @Override
    public void dispose() {
        this.close();
    }

    @Override
    public boolean isOpen() {
        return this.open.get();
    }
}

