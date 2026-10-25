/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.mac;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.mac.XAttr;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class XAttrUtil {
    public static List<String> listXAttr(String string) {
        long l2 = XAttr.INSTANCE.listxattr(string, null, 0L, 0);
        if (l2 < 0L) {
            return null;
        }
        if (l2 == 0L) {
            return new ArrayList<String>(0);
        }
        Memory memory = new Memory(l2);
        long l3 = XAttr.INSTANCE.listxattr(string, memory, l2, 0);
        if (l3 < 0L) {
            return null;
        }
        return XAttrUtil.decodeStringSequence(((Pointer)memory).getByteBuffer(0L, l3));
    }

    public static String getXAttr(String string, String string2) {
        long l2 = XAttr.INSTANCE.getxattr(string, string2, null, 0L, 0, 0);
        if (l2 < 0L) {
            return null;
        }
        if (l2 == 0L) {
            return "";
        }
        Memory memory = new Memory(l2);
        memory.clear();
        long l3 = XAttr.INSTANCE.getxattr(string, string2, memory, l2, 0, 0);
        if (l3 < 0L) {
            return null;
        }
        return Native.toString(memory.getByteArray(0L, (int)l2), "UTF-8");
    }

    public static int setXAttr(String string, String string2, String object) {
        Object object2 = object = XAttrUtil.encodeString((String)object);
        return XAttr.INSTANCE.setxattr(string, string2, (Pointer)object2, ((Memory)object2).size(), 0, 0);
    }

    public static int removeXAttr(String string, String string2) {
        return XAttr.INSTANCE.removexattr(string, string2, 0);
    }

    protected static Memory encodeString(String object) {
        object = ((String)object).getBytes(Charset.forName("UTF-8"));
        Memory memory = new Memory(((Object)object).length);
        ((Pointer)memory).write(0L, (byte[])object, 0, ((Object)object).length);
        return memory;
    }

    protected static String decodeString(ByteBuffer byteBuffer) {
        return Charset.forName("UTF-8").decode(byteBuffer).toString();
    }

    protected static List<String> decodeStringSequence(ByteBuffer byteBuffer) {
        ArrayList<String> arrayList = new ArrayList<String>();
        byteBuffer.mark();
        while (byteBuffer.hasRemaining()) {
            if (byteBuffer.get() != 0) continue;
            ByteBuffer byteBuffer2 = (ByteBuffer)byteBuffer.duplicate().limit(byteBuffer.position() - 1).reset();
            if (byteBuffer2.hasRemaining()) {
                arrayList.add(XAttrUtil.decodeString(byteBuffer2));
            }
            byteBuffer.mark();
        }
        return arrayList;
    }
}

