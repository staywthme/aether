/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.util.jh;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;

public abstract class JHFilter
implements BufferedImageOp {
    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage bufferedImage, ColorModel colorModel) {
        if (colorModel == null) {
            colorModel = bufferedImage.getColorModel();
        }
        ColorModel colorModel2 = colorModel;
        return new BufferedImage(colorModel2, colorModel2.createCompatibleWritableRaster(bufferedImage.getWidth(), bufferedImage.getHeight()), colorModel.isAlphaPremultiplied(), null);
    }

    @Override
    public Rectangle2D getBounds2D(BufferedImage bufferedImage) {
        return new Rectangle(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
    }

    @Override
    public Point2D getPoint2D(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            point2D2 = new Point2D.Double();
        }
        point2D2.setLocation(point2D.getX(), point2D.getY());
        return point2D2;
    }

    @Override
    public RenderingHints getRenderingHints() {
        return null;
    }

    public int[] getRGB(BufferedImage bufferedImage, int n2, int n3, int n4, int n5, int[] nArray) {
        int n6 = bufferedImage.getType();
        if (n6 == 2 || n6 == 1) {
            return (int[])bufferedImage.getRaster().getDataElements(n2, n3, n4, n5, nArray);
        }
        return bufferedImage.getRGB(n2, n3, n4, n5, nArray, 0, n4);
    }

    public void setRGB(BufferedImage bufferedImage, int n2, int n3, int n4, int n5, int[] nArray) {
        int n6 = bufferedImage.getType();
        if (n6 == 2 || n6 == 1) {
            bufferedImage.getRaster().setDataElements(n2, n3, n4, n5, nArray);
            return;
        }
        bufferedImage.setRGB(n2, n3, n4, n5, nArray, 0, n4);
    }
}

