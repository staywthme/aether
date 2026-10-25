/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.util.jh;

import com.github.sarxos.webcam.util.ImageUtils;
import com.github.sarxos.webcam.util.jh.JHFilter;
import java.awt.image.BufferedImage;

public class JHNormalizeFilter
extends JHFilter {
    @Override
    public BufferedImage filter(BufferedImage bufferedImage, BufferedImage bufferedImage2) {
        int n2;
        int n3;
        int n4;
        int n5 = bufferedImage.getWidth();
        int n6 = bufferedImage.getHeight();
        int n7 = 1;
        for (n4 = 0; n4 < n5; ++n4) {
            for (n3 = 0; n3 < n6; ++n3) {
                n2 = bufferedImage.getRGB(n4, n3);
                int n8 = ImageUtils.clamp(n2 >>> 24);
                int n9 = ImageUtils.clamp(n2 >> 16 & 0xFF);
                int n10 = ImageUtils.clamp(n2 >> 8 & 0xFF);
                n2 = ImageUtils.clamp(n2 & 0xFF);
                if ((n2 = n8 << 24 | n9 << 16 | n10 << 8 | n2) <= n7) continue;
                n7 = n2;
            }
        }
        for (n4 = 0; n4 < n5; ++n4) {
            for (n3 = 0; n3 < n6; ++n3) {
                n2 = bufferedImage.getRGB(n4, n3);
                n2 = (n2 << 8) / n7;
                bufferedImage2.setRGB(n4, n3, n2);
            }
        }
        return bufferedImage2;
    }
}

