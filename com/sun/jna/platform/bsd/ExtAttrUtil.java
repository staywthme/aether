/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.bsd;

import com.sun.jna.Native;
import com.sun.jna.platform.bsd.ExtAttr;
import com.sun.jna.platform.unix.LibCAPI;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExtAttrUtil {
    public static List<String> list(String string) throws IOException {
        long l2 = ExtAttr.INSTANCE.extattr_list_file(string, 1, null, new LibCAPI.size_t(0L)).longValue();
        if (l2 < 0L) {
            throw new IOException("errno: " + Native.getLastError());
        }
        if (l2 == 0L) {
            return Collections.emptyList();
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)l2);
        long l3 = ExtAttr.INSTANCE.extattr_list_file(string, 1, byteBuffer, new LibCAPI.size_t(l2)).longValue();
        if (l3 < 0L) {
            throw new IOException("errno: " + Native.getLastError());
        }
        return ExtAttrUtil.decodeStringList(byteBuffer);
    }

    public static ByteBuffer get(String string, String string2) throws IOException {
        long l2 = ExtAttr.INSTANCE.extattr_get_file(string, 1, string2, null, new LibCAPI.size_t(0L)).longValue();
        if (l2 < 0L) {
            throw new IOException("errno: " + Native.getLastError());
        }
        if (l2 == 0L) {
            return ByteBuffer.allocate(0);
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)l2);
        long l3 = ExtAttr.INSTANCE.extattr_get_file(string, 1, string2, byteBuffer, new LibCAPI.size_t(l2)).longValue();
        if (l3 < 0L) {
            throw new IOException("errno: " + Native.getLastError());
        }
        return byteBuffer;
    }

    public static void set(String string, String string2, ByteBuffer byteBuffer) throws IOException {
        long l2 = ExtAttr.INSTANCE.extattr_set_file(string, 1, string2, byteBuffer, new LibCAPI.size_t((long)byteBuffer.remaining())).longValue();
        if (l2 < 0L) {
            throw new IOException("errno: " + Native.getLastError());
        }
    }

    public static void delete(String string, String string2) throws IOException {
        int n2 = ExtAttr.INSTANCE.extattr_delete_file(string, 1, string2);
        if (n2 < 0) {
            throw new IOException("errno: " + Native.getLastError());
        }
    }

    private static List<String> decodeStringList(ByteBuffer byteBuffer) {
        ArrayList<String> arrayList = new ArrayList<String>();
        while (byteBuffer.hasRemaining()) {
            int n2 = byteBuffer.get() & 0xFF;
            byte[] byArray = new byte[n2];
            byteBuffer.get(byArray);
            try {
                arrayList.add(new String(byArray, "UTF-8"));
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                throw new RuntimeException(unsupportedEncodingException);
            }
        }
        return arrayList;
    }
}

