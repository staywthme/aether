/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEventType;
import java.awt.image.BufferedImage;
import java.util.EventObject;

public class WebcamEvent
extends EventObject {
    private static final long serialVersionUID = 1L;
    private BufferedImage image = null;
    private WebcamEventType type = null;

    public WebcamEvent(WebcamEventType webcamEventType, Webcam webcam) {
        this(webcamEventType, webcam, null);
    }

    public WebcamEvent(WebcamEventType webcamEventType, Webcam webcam, BufferedImage bufferedImage) {
        super(webcam);
        this.type = webcamEventType;
        this.image = bufferedImage;
    }

    @Override
    public Webcam getSource() {
        return (Webcam)super.getSource();
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public WebcamEventType getType() {
        return this.type;
    }
}

