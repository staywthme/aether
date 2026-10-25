/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.WebcamMotionDetectorAlgorithm;
import com.github.sarxos.webcam.util.jh.JHBlurFilter;
import com.github.sarxos.webcam.util.jh.JHGrayFilter;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WebcamMotionDetectorDefaultAlgorithm
implements WebcamMotionDetectorAlgorithm {
    public static final int DEFAULT_PIXEL_THREASHOLD = 25;
    public static final double DEFAULT_AREA_THREASHOLD = 0.2;
    public static final double DEFAULT_AREA_THREASHOLD_MAX = 100.0;
    private volatile int pixelThreshold = 25;
    private volatile double areaThreshold = 0.2;
    private volatile double areaThresholdMax = 100.0;
    private double area = 0.0;
    private Point cog = null;
    private final JHBlurFilter blur = new JHBlurFilter(6.0f, 6.0f, 1);
    private final JHGrayFilter gray = new JHGrayFilter();
    private List<Rectangle> doNotEnganeZones = Collections.emptyList();
    ArrayList<Point> points = new ArrayList();
    ArrayList<Integer> thresholds = new ArrayList();
    public static final int DEFAULT_RANGE = 50;
    public static final int DEFAULT_MAX_POINTS = 100;
    private int range = 50;
    private int maxPoints = 100;

    public WebcamMotionDetectorDefaultAlgorithm() {
        this(25, 0.2);
    }

    public WebcamMotionDetectorDefaultAlgorithm(int n2, double d2) {
        this.setPixelThreshold(n2);
        this.setAreaThreshold(d2);
    }

    @Override
    public BufferedImage filter(BufferedImage bufferedImage) {
        bufferedImage = this.blur.filter(bufferedImage, null);
        bufferedImage = this.gray.filter(bufferedImage, null);
        return bufferedImage;
    }

    @Override
    public boolean detect(BufferedImage bufferedImage, BufferedImage bufferedImage2) {
        this.points.clear();
        this.thresholds.clear();
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = bufferedImage2.getWidth();
        int n6 = bufferedImage2.getHeight();
        int n7 = 0;
        if (bufferedImage != null) {
            for (int i2 = 0; i2 < n5; ++i2) {
                for (int i3 = 0; i3 < n6; ++i3) {
                    if (this.isInDoNotEngageZone(i2, i3)) continue;
                    int n8 = bufferedImage2.getRGB(i2, i3);
                    int n9 = bufferedImage.getRGB(i2, i3);
                    if ((n8 = WebcamMotionDetectorDefaultAlgorithm.combinePixels(n8, n9) & 0xFF) < this.pixelThreshold) continue;
                    Point point = new Point(i2, i3);
                    boolean bl = n7 < this.maxPoints;
                    if (bl) {
                        for (Point point2 : this.points) {
                            if (point2.x == point.x && point2.y == point.y || !(point.distance(point2) <= (double)this.range)) continue;
                            bl = false;
                            break;
                        }
                    }
                    if (bl) {
                        this.points.add(new Point(i2, i3));
                        ++n7;
                    }
                    n3 += i2;
                    n4 += i3;
                    ++n2;
                    this.thresholds.add(n8);
                }
            }
        }
        this.area = (double)n2 * 100.0 / (double)(n5 * n6);
        if (this.area >= this.areaThreshold && this.area <= this.areaThresholdMax) {
            this.cog = new Point(n3 / n2, n4 / n2);
            return true;
        }
        this.cog = new Point(n5 / 2, n6 / 2);
        return false;
    }

    private boolean isInDoNotEngageZone(int n2, int n3) {
        for (Rectangle rectangle : this.doNotEnganeZones) {
            if (!rectangle.contains(n2, n3)) continue;
            return true;
        }
        return false;
    }

    @Override
    public Point getCog() {
        return this.cog;
    }

    @Override
    public double getArea() {
        return this.area;
    }

    public void setPixelThreshold(int n2) {
        if (n2 < 0) {
            throw new IllegalArgumentException("Pixel intensity threshold cannot be negative!");
        }
        if (n2 > 255) {
            throw new IllegalArgumentException("Pixel intensity threshold cannot be higher than 255!");
        }
        this.pixelThreshold = n2;
    }

    public void setAreaThreshold(double d2) {
        if (d2 < 0.0) {
            throw new IllegalArgumentException("Area fraction threshold cannot be negative!");
        }
        if (d2 > 100.0) {
            throw new IllegalArgumentException("Area fraction threshold cannot be higher than 100!");
        }
        this.areaThreshold = d2;
    }

    public void setMaxAreaThreshold(double d2) {
        if (d2 < 0.0) {
            throw new IllegalArgumentException("Area fraction threshold cannot be negative!");
        }
        if (d2 > 100.0) {
            throw new IllegalArgumentException("Area fraction threshold cannot be higher than 100!");
        }
        this.areaThresholdMax = d2;
    }

    private static int combinePixels(int n2, int n3) {
        int n4 = n2 >>> 24;
        int n5 = n2 >> 16 & 0xFF;
        int n6 = n2 >> 8 & 0xFF;
        n2 &= 0xFF;
        int n7 = n3 >>> 24;
        int n8 = n3 >> 16 & 0xFF;
        int n9 = n3 >> 8 & 0xFF;
        n5 = WebcamMotionDetectorDefaultAlgorithm.clamp(Math.abs(n5 - n8));
        n6 = WebcamMotionDetectorDefaultAlgorithm.clamp(Math.abs(n6 - n9));
        n2 = WebcamMotionDetectorDefaultAlgorithm.clamp(Math.abs(n2 - (n3 &= 0xFF)));
        if (n4 != 255) {
            n4 = n4 * 255 / 255;
            n7 = (255 - n4) * n7 / 255;
            n5 = WebcamMotionDetectorDefaultAlgorithm.clamp((n5 * n4 + n8 * n7) / 255);
            n6 = WebcamMotionDetectorDefaultAlgorithm.clamp((n6 * n4 + n9 * n7) / 255);
            n2 = WebcamMotionDetectorDefaultAlgorithm.clamp((n2 * n4 + n3 * n7) / 255);
            n4 = WebcamMotionDetectorDefaultAlgorithm.clamp(n4 + n7);
        }
        return n4 << 24 | n5 << 16 | n6 << 8 | n2;
    }

    private static int clamp(int n2) {
        if (n2 < 0) {
            return 0;
        }
        if (n2 > 255) {
            return 255;
        }
        return n2;
    }

    public ArrayList<Integer> getThresholds() {
        return this.thresholds;
    }

    @Override
    public void setPointRange(int n2) {
        this.range = n2;
    }

    @Override
    public int getPointRange() {
        return this.range;
    }

    @Override
    public void setMaxPoints(int n2) {
        this.maxPoints = n2;
    }

    @Override
    public int getMaxPoints() {
        return this.maxPoints;
    }

    @Override
    public ArrayList<Point> getPoints() {
        return this.points;
    }

    @Override
    public void setDoNotEngageZones(List<Rectangle> list2) {
        this.doNotEnganeZones = list2;
    }
}

