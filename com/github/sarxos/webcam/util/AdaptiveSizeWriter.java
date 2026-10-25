/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.MemoryCacheImageOutputStream;

public class AdaptiveSizeWriter {
    private static final float INITIAL_QUALITY = 1.0f;
    private volatile int size;
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private float quality = 1.0f;

    public AdaptiveSizeWriter(int n2) {
        this.size = n2;
    }

    public byte[] write(BufferedImage bufferedImage) {
        int n2;
        int n3 = this.size;
        int n4 = 0;
        do {
            if ((n2 = this.compress(bufferedImage, this.quality)) <= n3) continue;
            this.quality = (float)((double)this.quality * 0.75);
            if (n4++ >= 20) break;
        } while (n2 > n3);
        return this.baos.toByteArray();
    }

    private int compress(BufferedImage bufferedImage, float f2) {
        this.baos.reset();
        JPEGImageWriteParam jPEGImageWriteParam = new JPEGImageWriteParam(null);
        jPEGImageWriteParam.setCompressionMode(2);
        jPEGImageWriteParam.setCompressionQuality(f2);
        try {
            MemoryCacheImageOutputStream memoryCacheImageOutputStream = new MemoryCacheImageOutputStream(this.baos);
            Throwable throwable = null;
            try {
                ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpg").next();
                imageWriter.setOutput(memoryCacheImageOutputStream);
                imageWriter.write(null, new IIOImage(bufferedImage, null, null), jPEGImageWriteParam);
            }
            catch (Throwable throwable2) {
                try {
                    Throwable throwable3 = throwable2;
                    throwable = throwable2;
                    throw throwable3;
                }
                catch (Throwable throwable4) {
                    if (throwable != null) {
                        try {
                            memoryCacheImageOutputStream.close();
                        }
                        catch (Throwable throwable5) {
                            throwable.addSuppressed(throwable5);
                        }
                    } else {
                        memoryCacheImageOutputStream.close();
                    }
                    throw throwable4;
                }
            }
            memoryCacheImageOutputStream.close();
        }
        catch (IOException iOException) {
            throw new IllegalStateException(iOException);
        }
        return this.baos.size();
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int n2) {
        if (this.size != n2) {
            this.size = n2;
            this.quality = 1.0f;
        }
    }
}

