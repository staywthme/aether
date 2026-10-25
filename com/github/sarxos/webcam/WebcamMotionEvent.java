/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EventObject;

public class WebcamMotionEvent
extends EventObject {
    private static final long serialVersionUID = -7245768099221999443L;
    private final double strength;
    private final Point cog;
    private final BufferedImage previousImage;
    private final BufferedImage currentImage;
    private ArrayList<Point> points;

    public WebcamMotionEvent(WebcamMotionDetector webcamMotionDetector, double d2, Point point) {
        this(webcamMotionDetector, null, null, d2, point);
    }

    public WebcamMotionEvent(WebcamMotionDetector webcamMotionDetector, double d2, Point point, ArrayList<Point> arrayList) {
        this(webcamMotionDetector, null, null, d2, point, arrayList);
    }

    public WebcamMotionEvent(WebcamMotionDetector webcamMotionDetector, BufferedImage bufferedImage, BufferedImage bufferedImage2, double d2, Point point) {
        super(webcamMotionDetector);
        this.previousImage = bufferedImage;
        this.currentImage = bufferedImage2;
        this.strength = d2;
        this.cog = point;
    }

    public WebcamMotionEvent(WebcamMotionDetector webcamMotionDetector, BufferedImage bufferedImage, BufferedImage bufferedImage2, double d2, Point point, ArrayList<Point> arrayList) {
        this(webcamMotionDetector, bufferedImage, bufferedImage2, d2, point);
        this.points = arrayList;
    }

    public ArrayList<Point> getPoints() {
        return this.points;
    }

    public double getArea() {
        return this.strength;
    }

    public Point getCog() {
        return this.cog;
    }

    public Webcam getWebcam() {
        return this.getSource().getWebcam();
    }

    public BufferedImage getPreviousImage() {
        return this.previousImage;
    }

    public BufferedImage getCurrentImage() {
        return this.currentImage;
    }

    @Override
    public WebcamMotionDetector getSource() {
        return (WebcamMotionDetector)super.getSource();
    }
}

