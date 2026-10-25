/*
 * Decompiled with CFR 0.152.
 */
package com.github.sarxos.webcam.util.jh;

import com.github.sarxos.webcam.util.jh.JHFilter;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class JHFlipFilter
extends JHFilter {
    public static final int FLIP_90CW = 4;
    public static final int FLIP_90CCW = 5;
    public static final int FLIP_180 = 6;
    private int operation;

    public JHFlipFilter() {
        this(4);
    }

    public JHFlipFilter(int n2) {
        this.setOperation(n2);
    }

    public void setOperation(int n2) {
        this.operation = n2;
    }

    public int getOperation() {
        return this.operation;
    }

    @Override
    public BufferedImage filter(BufferedImage bufferedImage, BufferedImage bufferedImage2) {
        int n2 = bufferedImage.getWidth();
        int n3 = bufferedImage.getHeight();
        int[] nArray = this.getRGB(bufferedImage, 0, 0, n2, n3, null);
        int n4 = n2;
        int n5 = n3;
        switch (this.operation) {
            case 4: {
                n4 = n3;
                n5 = n2;
                break;
            }
            case 5: {
                n4 = n3;
                n5 = n2;
            }
        }
        int[] nArray2 = new int[n4 * n5];
        for (int i2 = 0; i2 < n3; ++i2) {
            for (int i3 = 0; i3 < n2; ++i3) {
                int n6 = i2 * n2 + i3;
                int n7 = i2;
                int n8 = i3;
                switch (this.operation) {
                    case 4: {
                        n7 = i3;
                        n8 = n3 - i2 - 1;
                        break;
                    }
                    case 5: {
                        n7 = n2 - i3 - 1;
                        n8 = i2;
                        break;
                    }
                    case 6: {
                        n7 = n3 - i2 - 1;
                        n8 = n2 - i3 - 1;
                    }
                }
                n7 = n7 * n4 + n8;
                nArray2[n7] = nArray[n6];
            }
        }
        if (bufferedImage2 == null) {
            ColorModel colorModel;
            ColorModel colorModel2 = colorModel = bufferedImage.getColorModel();
            bufferedImage2 = new BufferedImage(colorModel2, colorModel2.createCompatibleWritableRaster(n4, n5), colorModel.isAlphaPremultiplied(), null);
        }
        this.setRGB(bufferedImage2, 0, 0, n4, n5, nArray2);
        return bufferedImage2;
    }

    public String toString() {
        switch (this.operation) {
            case 4: {
                return "Rotate 90";
            }
            case 5: {
                return "Rotate -90";
            }
            case 6: {
                return "Rotate 180";
            }
        }
        return "Flip";
    }
}

