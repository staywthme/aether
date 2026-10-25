/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.objc;

import java.nio.charset.Charset;
import org.bridj.BridJ;
import org.bridj.CRuntime;
import org.bridj.Pointer;
import org.bridj.ann.Library;
import org.bridj.ann.Ptr;
import org.bridj.ann.Runtime;
import org.bridj.objc.NSString;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Library(value="Foundation")
@Runtime(value=CRuntime.class)
public class FoundationLibrary {
    public static final int kCFStringEncodingASCII = 1536;
    public static final int kCFStringEncodingUnicode = 256;
    public static final int kCFStringEncodingUTF8 = 0x8000100;

    public static native Pointer<NSString> CFStringCreateWithBytes(Pointer<?> var0, Pointer<Byte> var1, @Ptr long var2, int var4, boolean var5);

    public static Pointer<NSString> pointerToNSString(String pointer) {
        pointer = Pointer.pointerToString((String)((Object)pointer), Pointer.StringType.C, Charset.forName("utf-8"));
        assert (pointer != null);
        Pointer<NSString> pointer2 = pointer;
        pointer = FoundationLibrary.CFStringCreateWithBytes(null, (Pointer<Byte>)pointer2, pointer2.getValidBytes() - 1L, 0x8000100, false);
        assert (pointer != null);
        return pointer;
    }

    static {
        BridJ.register();
    }
}

