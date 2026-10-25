/*
 * Decompiled with CFR 0.152.
 */
package org.conscrypt.ct;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import org.conscrypt.ct.SerializationException;

public class Serialization {
    private static final int DER_TAG_MASK = 63;
    private static final int DER_TAG_OCTET_STRING = 4;
    private static final int DER_LENGTH_LONG_FORM_FLAG = 128;

    private Serialization() {
    }

    public static byte[] readDEROctetString(byte[] byArray) throws SerializationException {
        return Serialization.readDEROctetString(new ByteArrayInputStream(byArray));
    }

    public static byte[] readDEROctetString(InputStream inputStream2) throws SerializationException {
        int n2;
        block1: {
            n2 = Serialization.readByte(inputStream2) & 0x3F;
            if (n2 != 4) {
                throw new SerializationException("Wrong DER tag, expected OCTET STRING, got " + n2);
            }
            n2 = Serialization.readNumber(inputStream2, 1);
            if ((n2 & 0x80) == 0) break block1;
            n2 = Serialization.readNumber(inputStream2, n2 & 0xFFFFFF7F);
        }
        return Serialization.readFixedBytes(inputStream2, n2);
    }

    public static byte[][] readList(byte[] byArray, int n2, int n3) throws SerializationException {
        return Serialization.readList(new ByteArrayInputStream(byArray), n2, n3);
    }

    public static byte[][] readList(InputStream object, int n2, int n3) throws SerializationException {
        ArrayList<byte[]> arrayList = new ArrayList<byte[]>();
        object = Serialization.readVariableBytes((InputStream)object, n2);
        object = new ByteArrayInputStream((byte[])object);
        try {
            while (((InputStream)object).available() > 0) {
                arrayList.add(Serialization.readVariableBytes((InputStream)object, n3));
            }
        }
        catch (IOException iOException) {
            throw new SerializationException(iOException);
        }
        ArrayList<byte[]> arrayList2 = arrayList;
        return (byte[][])arrayList2.toArray((T[])new byte[arrayList2.size()][]);
    }

    public static byte[] readVariableBytes(InputStream inputStream2, int n2) throws SerializationException {
        n2 = Serialization.readNumber(inputStream2, n2);
        return Serialization.readFixedBytes(inputStream2, n2);
    }

    public static byte[] readFixedBytes(InputStream inputStream2, int n2) throws SerializationException {
        try {
            if (n2 < 0) {
                throw new SerializationException("Negative length: " + n2);
            }
            byte[] byArray = new byte[n2];
            int n3 = inputStream2.read(byArray);
            if (n3 < n2) {
                throw new SerializationException("Premature end of input, expected " + n2 + " bytes, only read " + n3);
            }
            return byArray;
        }
        catch (IOException iOException) {
            throw new SerializationException(iOException);
        }
    }

    public static int readNumber(InputStream inputStream2, int n2) throws SerializationException {
        if (n2 > 4 || n2 < 0) {
            throw new SerializationException("Invalid width: " + n2);
        }
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            n3 = n3 << 8 | Serialization.readByte(inputStream2) & 0xFF;
        }
        return n3;
    }

    public static long readLong(InputStream inputStream2, int n2) throws SerializationException {
        if (n2 > 8 || n2 < 0) {
            throw new IllegalArgumentException("Invalid width: " + n2);
        }
        long l2 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            l2 = l2 << 8 | (long)(Serialization.readByte(inputStream2) & 0xFF);
        }
        return l2;
    }

    public static byte readByte(InputStream inputStream2) throws SerializationException {
        try {
            int n2 = inputStream2.read();
            if (n2 == -1) {
                throw new SerializationException("Premature end of input, could not read byte.");
            }
            return (byte)n2;
        }
        catch (IOException iOException) {
            throw new SerializationException(iOException);
        }
    }

    public static void writeVariableBytes(OutputStream outputStream2, byte[] byArray, int n2) throws SerializationException {
        Serialization.writeNumber(outputStream2, byArray.length, n2);
        Serialization.writeFixedBytes(outputStream2, byArray);
    }

    public static void writeFixedBytes(OutputStream outputStream2, byte[] byArray) throws SerializationException {
        try {
            outputStream2.write(byArray);
            return;
        }
        catch (IOException iOException) {
            throw new SerializationException(iOException);
        }
    }

    public static void writeNumber(OutputStream outputStream2, long l2, int n2) throws SerializationException {
        if (n2 < 0) {
            throw new SerializationException("Negative width: " + n2);
        }
        if (n2 < 8 && l2 >= 1L << n2 * 8) {
            throw new SerializationException("Number too large, " + l2 + " does not fit in " + n2 + " bytes");
        }
        try {
            while (n2 > 0) {
                long l3 = (long)(n2 - 1) << 3;
                if (l3 < 64L) {
                    outputStream2.write((byte)(l2 >> (int)l3 & 0xFFL));
                } else {
                    outputStream2.write(0);
                }
                --n2;
            }
            return;
        }
        catch (IOException iOException) {
            throw new SerializationException(iOException);
        }
    }
}

