/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.util;

import com.github.sarxos.webcam.WebcamException;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ImageUtils {
    public static final String FORMAT_GIF = "GIF";
    public static final String FORMAT_PNG = "PNG";
    public static final String FORMAT_JPG = "JPG";
    public static final String FORMAT_BMP = "BMP";
    public static final String FORMAT_WBMP = "WBMP";

    public static byte[] toByteArray(BufferedImage object, String string) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write((RenderedImage)object, string, byteArrayOutputStream);
            object = byteArrayOutputStream.toByteArray();
        }
        catch (IOException iOException) {
            throw new WebcamException(iOException);
        }
        finally {
            try {
                byteArrayOutputStream.close();
            }
            catch (IOException iOException) {
                throw new WebcamException(iOException);
            }
        }
        return object;
    }

    public static BufferedImage readFromResource(String object) {
        InputStream inputStream2 = null;
        try {
            inputStream2 = ImageUtils.class.getClassLoader().getResourceAsStream((String)object);
            object = ImageIO.read(inputStream2);
            return object;
        }
        catch (IOException iOException) {
            throw new IllegalStateException(iOException);
        }
        finally {
            if (inputStream2 != null) {
                try {
                    inputStream2.close();
                }
                catch (IOException iOException) {
                    throw new IllegalStateException(iOException);
                }
            }
        }
    }

    public static BufferedImage createEmptyImage(BufferedImage bufferedImage) {
        return new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), 1);
    }

    public static int clamp(int n2) {
        if (n2 < 0) {
            return 0;
        }
        if (n2 > 255) {
            return 255;
        }
        return n2;
    }

    public static byte[] imageToBytes(BufferedImage bufferedImage) {
        return ((DataBufferByte)bufferedImage.getData().getDataBuffer()).getData();
    }
}

