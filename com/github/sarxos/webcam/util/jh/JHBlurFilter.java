/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.util.jh;

import com.github.sarxos.webcam.util.jh.JHFilter;
import java.awt.image.BufferedImage;

public class JHBlurFilter
extends JHFilter {
    private float hRadius;
    private float vRadius;
    private int iterations = 1;
    private boolean premultiplyAlpha = true;

    public JHBlurFilter() {
    }

    public JHBlurFilter(float f2, float f3, int n2) {
        this.hRadius = f2;
        this.vRadius = f3;
        this.iterations = n2;
    }

    public void setPremultiplyAlpha(boolean bl) {
        this.premultiplyAlpha = bl;
    }

    public boolean getPremultiplyAlpha() {
        return this.premultiplyAlpha;
    }

    @Override
    public BufferedImage filter(BufferedImage bufferedImage, BufferedImage bufferedImage2) {
        int n2 = bufferedImage.getWidth();
        int n3 = bufferedImage.getHeight();
        if (bufferedImage2 == null) {
            bufferedImage2 = this.createCompatibleDestImage(bufferedImage, null);
        }
        int[] nArray = new int[n2 * n3];
        int[] nArray2 = new int[n2 * n3];
        this.getRGB(bufferedImage, 0, 0, n2, n3, nArray);
        if (this.premultiplyAlpha) {
            JHBlurFilter.premultiply(nArray, 0, nArray.length);
        }
        for (int i2 = 0; i2 < this.iterations; ++i2) {
            JHBlurFilter.blur(nArray, nArray2, n2, n3, this.hRadius);
            JHBlurFilter.blur(nArray2, nArray, n3, n2, this.vRadius);
        }
        JHBlurFilter.blurFractional(nArray, nArray2, n2, n3, this.hRadius);
        JHBlurFilter.blurFractional(nArray2, nArray, n3, n2, this.vRadius);
        if (this.premultiplyAlpha) {
            JHBlurFilter.unpremultiply(nArray, 0, nArray.length);
        }
        this.setRGB(bufferedImage2, 0, 0, n2, n3, nArray);
        return bufferedImage2;
    }

    public static void blur(int[] nArray, int[] nArray2, int n2, int n3, float f2) {
        int n4 = n2 - 1;
        int n5 = (int)f2;
        int n6 = 2 * n5 + 1;
        int[] nArray3 = new int[n6 * 256];
        int n7 = 0;
        while (n7 < n6 * 256) {
            int n8 = n7++;
            nArray3[n8] = n8 / n6;
        }
        n7 = 0;
        for (n6 = 0; n6 < n3; ++n6) {
            int n9;
            int n10;
            int n11 = n6;
            int n12 = 0;
            int n13 = 0;
            int n14 = 0;
            int n15 = 0;
            for (n10 = -n5; n10 <= n5; ++n10) {
                n9 = nArray[n7 + JHBlurFilter.clamp(n10, 0, n4)];
                n12 += n9 >>> 24;
                n13 += n9 >> 16 & 0xFF;
                n14 += n9 >> 8 & 0xFF;
                n15 += n9 & 0xFF;
            }
            for (n10 = 0; n10 < n2; ++n10) {
                int n16;
                nArray2[n11] = nArray3[n12] << 24 | nArray3[n13] << 16 | nArray3[n14] << 8 | nArray3[n15];
                n9 = n10 + n5 + 1;
                if (n9 > n4) {
                    n9 = n4;
                }
                if ((n16 = n10 - n5) < 0) {
                    n16 = 0;
                }
                n9 = nArray[n7 + n9];
                n16 = nArray[n7 + n16];
                n12 += (n9 >>> 24) - (n16 >>> 24);
                n13 += (n9 & 0xFF0000) - (n16 & 0xFF0000) >> 16;
                n14 += (n9 & 0xFF00) - (n16 & 0xFF00) >> 8;
                n15 += (n9 & 0xFF) - (n16 & 0xFF);
                n11 += n3;
            }
            n7 += n2;
        }
    }

    public static void blurFractional(int[] nArray, int[] nArray2, int n2, int n3, float f2) {
        float f3 = f2;
        f2 = f3 - (float)((int)f3);
        float f4 = 1.0f / (1.0f + f2 * 2.0f);
        int n4 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            int n5 = i2;
            nArray2[n5] = nArray[0];
            n5 += n3;
            for (int i3 = 1; i3 < n2 - 1; ++i3) {
                int n6 = n4 + i3;
                int n7 = nArray[n6 - 1];
                int n8 = nArray[n6];
                n6 = nArray[n6 + 1];
                int n9 = n7 >>> 24;
                int n10 = n7 >> 16 & 0xFF;
                int n11 = n7 >> 8 & 0xFF;
                n7 &= 0xFF;
                int n12 = n8 >>> 24;
                int n13 = n8 >> 16 & 0xFF;
                int n14 = n8 >> 8 & 0xFF;
                n8 &= 0xFF;
                int n15 = n6 >>> 24;
                int n16 = n6 >> 16 & 0xFF;
                int n17 = n6 >> 8 & 0xFF;
                n9 = n12 + (int)((float)(n9 + n15) * f2);
                n10 = n13 + (int)((float)(n10 + n16) * f2);
                n11 = n14 + (int)((float)(n11 + n17) * f2);
                n7 = n8 + (int)((float)(n7 + (n6 &= 0xFF)) * f2);
                n9 = (int)((float)n9 * f4);
                n10 = (int)((float)n10 * f4);
                n11 = (int)((float)n11 * f4);
                n7 = (int)((float)n7 * f4);
                nArray2[n5] = n9 << 24 | n10 << 16 | n11 << 8 | n7;
                n5 += n3;
            }
            nArray2[n5] = nArray[n2 - 1];
            n4 += n2;
        }
    }

    public void setHRadius(float f2) {
        this.hRadius = f2;
    }

    public float getHRadius() {
        return this.hRadius;
    }

    public void setVRadius(float f2) {
        this.vRadius = f2;
    }

    public float getVRadius() {
        return this.vRadius;
    }

    public void setRadius(float f2) {
        JHBlurFilter jHBlurFilter = this;
        jHBlurFilter.hRadius = jHBlurFilter.vRadius = f2;
    }

    public float getRadius() {
        return this.hRadius;
    }

    public void setIterations(int n2) {
        this.iterations = n2;
    }

    public int getIterations() {
        return this.iterations;
    }

    public String toString() {
        return "Blur/Box Blur...";
    }

    public static void premultiply(int[] nArray, int n2, int n3) {
        n3 += n2;
        while (n2 < n3) {
            int n4 = nArray[n2];
            int n5 = n4 >>> 24;
            int n6 = n4 >> 16 & 0xFF;
            int n7 = n4 >> 8 & 0xFF;
            n4 &= 0xFF;
            float f2 = (float)n5 * 0.003921569f;
            n6 = (int)((float)n6 * f2);
            n7 = (int)((float)n7 * f2);
            n4 = (int)((float)n4 * f2);
            nArray[n2] = n5 << 24 | n6 << 16 | n7 << 8 | n4;
            ++n2;
        }
    }

    public static void unpremultiply(int[] nArray, int n2, int n3) {
        n3 += n2;
        while (n2 < n3) {
            int n4 = nArray[n2];
            int n5 = n4 >>> 24;
            int n6 = n4 >> 16 & 0xFF;
            int n7 = n4 >> 8 & 0xFF;
            n4 &= 0xFF;
            if (n5 != 0 && n5 != 255) {
                float f2 = 255.0f / (float)n5;
                n6 = (int)((float)n6 * f2);
                n7 = (int)((float)n7 * f2);
                n4 = (int)((float)n4 * f2);
                if (n6 > 255) {
                    n6 = 255;
                }
                if (n7 > 255) {
                    n7 = 255;
                }
                if (n4 > 255) {
                    n4 = 255;
                }
                nArray[n2] = n5 << 24 | n6 << 16 | n7 << 8 | n4;
            }
            ++n2;
        }
    }

    public static int clamp(int n2, int n3, int n4) {
        if (n2 < n3) {
            return n3;
        }
        if (n2 > n4) {
            return n4;
        }
        return n2;
    }
}

