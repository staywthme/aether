/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform;

import java.awt.Rectangle;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class RasterRangesUtils {
    private static final int[] subColMasks = new int[]{128, 64, 32, 16, 8, 4, 2, 1};
    private static final Comparator<Object> COMPARATOR = new Comparator<Object>(){

        @Override
        public final int compare(Object object, Object object2) {
            return ((Rectangle)object).x - ((Rectangle)object2).x;
        }
    };

    public static boolean outputOccupiedRanges(Raster raster, RangesOutput rangesOutput) {
        Object object;
        boolean bl;
        Rectangle rectangle = raster.getBounds();
        SampleModel sampleModel = raster.getSampleModel();
        boolean bl2 = bl = sampleModel.getNumBands() == 4;
        if (raster.getParent() == null && rectangle.x == 0 && rectangle.y == 0 && ((DataBuffer)(object = raster.getDataBuffer())).getNumBanks() == 1) {
            if (sampleModel instanceof MultiPixelPackedSampleModel) {
                if (((MultiPixelPackedSampleModel)(sampleModel = (MultiPixelPackedSampleModel)sampleModel)).getPixelBitStride() == 1) {
                    return RasterRangesUtils.outputOccupiedRangesOfBinaryPixels(((DataBufferByte)object).getData(), rectangle.width, rectangle.height, rangesOutput);
                }
            } else if (sampleModel instanceof SinglePixelPackedSampleModel && sampleModel.getDataType() == 3) {
                return RasterRangesUtils.outputOccupiedRanges(((DataBufferInt)object).getData(), rectangle.width, rectangle.height, bl ? -16777216 : 0xFFFFFF, rangesOutput);
            }
        }
        int[] nArray = raster.getPixels(0, 0, rectangle.width, rectangle.height, (int[])null);
        object = nArray;
        return RasterRangesUtils.outputOccupiedRanges(nArray, rectangle.width, rectangle.height, bl ? -16777216 : 0xFFFFFF, rangesOutput);
    }

    public static boolean outputOccupiedRangesOfBinaryPixels(byte[] byArray, int n2, int n3, RangesOutput rangesOutput) {
        HashSet<Rectangle> hashSet = new HashSet<Rectangle>();
        Set<Rectangle> set = Collections.emptySet();
        int n4 = byArray.length / n3;
        for (int i2 = 0; i2 < n3; ++i2) {
            TreeSet<Object> cloneable = new TreeSet<Object>(COMPARATOR);
            int n5 = i2 * n4;
            int n6 = -1;
            for (int i3 = 0; i3 < n4; ++i3) {
                int n7 = i3 << 3;
                byte by = byArray[n5 + i3];
                if (by == 0) {
                    if (n6 < 0) continue;
                    cloneable.add(new Rectangle(n6, i2, n7 - n6, 1));
                    n6 = -1;
                    continue;
                }
                if (by == 255) {
                    if (n6 >= 0) continue;
                    n6 = n7;
                    continue;
                }
                for (int i4 = 0; i4 < 8; ++i4) {
                    int n8 = n7 | i4;
                    if ((by & subColMasks[i4]) != 0) {
                        if (n6 >= 0) continue;
                        n6 = n8;
                        continue;
                    }
                    if (n6 < 0) continue;
                    cloneable.add(new Rectangle(n6, i2, n8 - n6, 1));
                    n6 = -1;
                }
            }
            if (n6 >= 0) {
                cloneable.add(new Rectangle(n6, i2, n2 - n6, 1));
            }
            Set<Rectangle> set2 = RasterRangesUtils.mergeRects(set, cloneable);
            hashSet.addAll(set2);
            set = cloneable;
        }
        hashSet.addAll(set);
        for (Rectangle rectangle : hashSet) {
            if (rangesOutput.outputRange(rectangle.x, rectangle.y, rectangle.width, rectangle.height)) continue;
            return false;
        }
        return true;
    }

    public static boolean outputOccupiedRanges(int[] nArray, int n2, int n3, int n4, RangesOutput rangesOutput) {
        HashSet<Rectangle> hashSet = new HashSet<Rectangle>();
        Set<Rectangle> set = Collections.emptySet();
        for (int i2 = 0; i2 < n3; ++i2) {
            TreeSet<Object> cloneable = new TreeSet<Object>(COMPARATOR);
            int n5 = i2 * n2;
            int n6 = -1;
            for (int i3 = 0; i3 < n2; ++i3) {
                if ((nArray[n5 + i3] & n4) != 0) {
                    if (n6 >= 0) continue;
                    n6 = i3;
                    continue;
                }
                if (n6 < 0) continue;
                cloneable.add(new Rectangle(n6, i2, i3 - n6, 1));
                n6 = -1;
            }
            if (n6 >= 0) {
                cloneable.add(new Rectangle(n6, i2, n2 - n6, 1));
            }
            Set<Rectangle> set2 = RasterRangesUtils.mergeRects(set, cloneable);
            hashSet.addAll(set2);
            set = cloneable;
        }
        hashSet.addAll(set);
        for (Rectangle rectangle : hashSet) {
            if (rangesOutput.outputRange(rectangle.x, rectangle.y, rectangle.width, rectangle.height)) continue;
            return false;
        }
        return true;
    }

    private static Set<Rectangle> mergeRects(Set<Rectangle> rectangleArray, Set<Rectangle> rectangleArray2) {
        HashSet<Rectangle> hashSet = new HashSet<Rectangle>((Collection<Rectangle>)rectangleArray);
        if (!rectangleArray.isEmpty() && !rectangleArray2.isEmpty()) {
            rectangleArray = rectangleArray.toArray(new Rectangle[0]);
            rectangleArray2 = rectangleArray2.toArray(new Rectangle[0]);
            int n2 = 0;
            int n3 = 0;
            while (n2 < rectangleArray.length && n3 < rectangleArray2.length) {
                while (rectangleArray2[n3].x < rectangleArray[n2].x) {
                    if (++n3 != rectangleArray2.length) continue;
                    return hashSet;
                }
                if (rectangleArray2[n3].x == rectangleArray[n2].x && rectangleArray2[n3].width == rectangleArray[n2].width) {
                    hashSet.remove(rectangleArray[n2]);
                    rectangleArray2[n3].y = rectangleArray[n2].y;
                    rectangleArray2[n3].height = rectangleArray[n2].height + 1;
                    ++n3;
                    continue;
                }
                ++n2;
            }
        }
        return hashSet;
    }

    public static interface RangesOutput {
        public boolean outputRange(int var1, int var2, int var3, int var4);
    }
}

