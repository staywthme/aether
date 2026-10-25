/*
 * Decompiled with CFR 0.152.
 */
package x;

import aether.browser.Win32Abe;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import java.util.ArrayList;
import java.util.List;

public final class m {
    public static List<Integer> a(String string) {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        Pointer pointer = Win32Abe.K.INSTANCE.CreateToolhelp32Snapshot(2, 0);
        if (pointer == null || Pointer.nativeValue(pointer) == -1L) {
            return arrayList;
        }
        try {
            Memory memory = new Memory(568L);
            ((Pointer)memory).setInt(0L, 568);
            if (!Win32Abe.K.INSTANCE.Process32FirstW(pointer, memory)) {
                ArrayList<Integer> arrayList2 = arrayList;
                return arrayList2;
            }
            do {
                String string2;
                int n2;
                if ((n2 = ((Pointer)memory).getInt(8L)) == 0 || !(string2 = m.a(memory, 48, 260)).equalsIgnoreCase(string)) continue;
                arrayList.add(n2);
            } while (Win32Abe.K.INSTANCE.Process32NextW(pointer, memory));
        }
        finally {
            Win32Abe.K.INSTANCE.CloseHandle(pointer);
        }
        return arrayList;
    }

    public static String a(Memory memory, int n2, int n3) {
        char c2;
        StringBuilder stringBuilder = new StringBuilder(260);
        for (n3 = 0; n3 < 260 && (c2 = (char)(((Pointer)memory).getByte(48 + (n3 << 1)) & 0xFF | (((Pointer)memory).getByte(48 + (n3 << 1) + 1) & 0xFF) << 8)) != '\u0000'; ++n3) {
            stringBuilder.append(c2);
        }
        return stringBuilder.toString();
    }
}

