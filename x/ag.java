/*
 * Decompiled with CFR 0.152.
 */
package x;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;

public final class ag {
    public static a a() {
        try {
            Object object;
            Object object2;
            String object3 = System.getenv($S.d(211));
            if (object3 == null) {
                return null;
            }
            Object object4 = new File(object3 + $S.d(812));
            if (!((File)object4).exists()) {
                return null;
            }
            object4 = new a();
            try {
                object2 = new ProcessBuilder($S.d(813), $S.d(814), $S.d(815), "/s");
                ((ProcessBuilder)object2).redirectErrorStream(true);
                object2 = ((ProcessBuilder)object2).start();
                object = new BufferedReader(new InputStreamReader(((Process)object2).getInputStream(), $S.d(399)));
                try {
                    String string;
                    while ((string = ((BufferedReader)object).readLine()) != null) {
                        String[] stringArray;
                        String string2 = string.trim();
                        if (string2.contains($S.d(816)) && (stringArray = string2.split($S.d(417), 3)).length >= 3) {
                            ((a)object4).b = stringArray[2].trim();
                        }
                        if (!string2.contains($S.d(442)) || string2.startsWith($S.d(817)) || (stringArray = string2.split($S.d(417), 3)).length < 3) continue;
                        ((a)object4).c = stringArray[2].trim();
                    }
                }
                finally {
                    ((BufferedReader)object).close();
                }
                ((Process)object2).waitFor();
            }
            catch (Exception exception) {}
            if ((((a)object4).b == null || ((a)object4).b.isEmpty()) && ((File)(object2 = new File(object3 + $S.d(818)))).exists() && (object2 = ((File)object2).listFiles(File::isDirectory)) != null) {
                object = object2;
                int n2 = ((File[])object2).length;
                for (int i2 = 0; i2 < n2; ++i2) {
                    File file = object[i2];
                    File file2 = new File(file, $S.d(819));
                    if (!file2.exists()) continue;
                    try {
                        String string = new String(Files.readAllBytes(file2.toPath()), $S.d(399));
                        for (String string3 : string.split("\n")) {
                            if ((string3 = string3.trim()).startsWith($S.d(820))) {
                                String string4 = string3;
                                ((a)object4).b = string4.substring(string4.indexOf(":") + 1).trim().replaceAll($S.d(821), "");
                            }
                            if (!string3.startsWith($S.d(822))) continue;
                            String string5 = string3;
                            ((a)object4).c = string5.substring(string5.indexOf(":") + 1).trim().replaceAll($S.d(821), "");
                        }
                        if (((a)object4).b == null || ((a)object4).b.isEmpty()) continue;
                        break;
                    }
                    catch (Exception exception) {}
                }
            }
            if ((((a)object4).b == null || ((a)object4).b.isEmpty()) && (((a)object4).c == null || ((a)object4).c.isEmpty())) {
                return null;
            }
            ((a)object4).a = true;
            return object4;
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

