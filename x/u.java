/*
 * Decompiled with CFR 0.152.
 */
package x;

import java.io.File;
import java.nio.file.Files;

public final class u {
    public static a a() {
        try {
            int n2;
            Object object = System.getenv($S.d(211));
            if (object == null) {
                return null;
            }
            if (!((File)(object = new File((String)object + $S.d(470)))).exists()) {
                return null;
            }
            object = new String(Files.readAllBytes(((File)object).toPath()), $S.d(399));
            String string = null;
            String string2 = null;
            int n3 = ((String)object).indexOf($S.d(471));
            if (n3 >= 0 && (n2 = ((String)object).indexOf("|", n3 += 11)) > n3) {
                string = ((String)object).substring(n3, n2);
            }
            if ((n3 = ((String)object).indexOf($S.d(472))) >= 0 && (n3 = ((String)object).indexOf("|", n2 = n3 + 11)) > n2) {
                string2 = ((String)object).substring(n2, n3);
            }
            if (string == null && string2 == null) {
                return null;
            }
            a a2 = new a();
            new a().a = true;
            a2.b = string != null ? string : "";
            a2.c = string2 != null ? string2 : "";
            return a2;
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static final class a {
        public boolean a;
        public String b;
        public String c;
    }
}

