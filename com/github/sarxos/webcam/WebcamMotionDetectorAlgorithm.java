/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public interface WebcamMotionDetectorAlgorithm {
    public BufferedImage filter(BufferedImage var1);

    public boolean detect(BufferedImage var1, BufferedImage var2);

    public Point getCog();

    public double getArea();

    public void setPointRange(int var1);

    public void setMaxPoints(int var1);

    public int getPointRange();

    public int getMaxPoints();

    public ArrayList<Point> getPoints();

    public void setDoNotEngageZones(List<Rectangle> var1);
}

