/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.util.ImageUtils;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;

public class WebcamUtils {
    public static final void capture(Webcam webcam, File file) {
        if (!webcam.isOpen()) {
            webcam.open();
        }
        try {
            ImageIO.write((RenderedImage)webcam.getImage(), "JPG", file);
            return;
        }
        catch (IOException iOException) {
            throw new WebcamException(iOException);
        }
    }

    public static final void capture(Webcam webcam, File file, String string) {
        if (!webcam.isOpen()) {
            webcam.open();
        }
        try {
            ImageIO.write((RenderedImage)webcam.getImage(), string, file);
            return;
        }
        catch (IOException iOException) {
            throw new WebcamException(iOException);
        }
    }

    public static final void capture(Webcam webcam, String string) {
        if (!string.endsWith(".jpg")) {
            string = string + ".jpg";
        }
        WebcamUtils.capture(webcam, new File(string));
    }

    public static final void capture(Webcam webcam, String string, String string2) {
        String string3 = "." + string2.toLowerCase();
        if (!string.endsWith(string3)) {
            string = string + string3;
        }
        WebcamUtils.capture(webcam, new File(string), string2);
    }

    public static final byte[] getImageBytes(Webcam webcam, String string) {
        return ImageUtils.toByteArray(webcam.getImage(), string);
    }

    public static final ByteBuffer getImageByteBuffer(Webcam webcam, String string) {
        return ByteBuffer.wrap(WebcamUtils.getImageBytes(webcam, string));
    }

    public static final ResourceBundle loadRB(Class<?> clazz, Locale object) {
        object = WebcamUtils.class.getPackage().getName().replaceAll("\\.", "/");
        return PropertyResourceBundle.getBundle(String.format("%s/i18n/%s", object, clazz.getSimpleName()));
    }
}

