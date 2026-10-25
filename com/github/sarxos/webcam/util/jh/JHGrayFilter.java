/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.util.jh;

import com.github.sarxos.webcam.util.jh.JHFilter;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class JHGrayFilter
extends JHFilter {
    protected boolean canFilterIndexColorModel = true;

    @Override
    public BufferedImage filter(BufferedImage bufferedImage, BufferedImage bufferedImage2) {
        int n2 = bufferedImage.getWidth();
        int n3 = bufferedImage.getHeight();
        int n4 = bufferedImage.getType();
        WritableRaster writableRaster = bufferedImage.getRaster();
        if (bufferedImage2 == null) {
            bufferedImage2 = this.createCompatibleDestImage(bufferedImage, null);
        }
        WritableRaster writableRaster2 = bufferedImage2.getRaster();
        int[] nArray = new int[n2];
        for (int i2 = 0; i2 < n3; ++i2) {
            int n5;
            if (n4 == 2) {
                writableRaster.getDataElements(0, i2, n2, 1, nArray);
                for (n5 = 0; n5 < n2; ++n5) {
                    nArray[n5] = this.filterRGB(nArray[n5]);
                }
                writableRaster2.setDataElements(0, i2, n2, 1, nArray);
                continue;
            }
            bufferedImage.getRGB(0, i2, n2, 1, nArray, 0, n2);
            for (n5 = 0; n5 < n2; ++n5) {
                nArray[n5] = this.filterRGB(nArray[n5]);
            }
            bufferedImage2.setRGB(0, i2, n2, 1, nArray, 0, n2);
        }
        return bufferedImage2;
    }

    private int filterRGB(int n2) {
        int n3 = n2 & 0xFF000000;
        int n4 = n2 >> 16 & 0xFF;
        int n5 = n2 >> 8 & 0xFF;
        n2 &= 0xFF;
        n2 = n4 * 77 + n5 * 151 + n2 * 28 >> 8;
        return n3 | n2 << 16 | n2 << 8 | n2;
    }
}

