/*
 * Decompiled with CFR 0.152.
 */
package org.bridj.objc;

import java.nio.charset.Charset;
import org.bridj.Pointer;
import org.bridj.ann.Library;
import org.bridj.objc.FoundationLibrary;
import org.bridj.objc.NSObject;
import org.bridj.objc.ObjCObject;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Library(value="Foundation")
public class NSString
extends NSObject {
    public native int length();

    public native boolean isAbsolutePath();

    public native Pointer<Byte> UTF8String();

    public NSString() {
    }

    public NSString(String string) {
        super((Pointer<? extends NSObject>)FoundationLibrary.pointerToNSString(string));
    }

    @Override
    public String toString() {
        return this.UTF8String().getString(Pointer.StringType.C, Charset.forName("utf-8"));
    }

    @Override
    public int hashCode() {
        return ((ObjCObject)this).toString().hashCode();
    }

    public static NSString valueOf(String string) {
        return FoundationLibrary.pointerToNSString(string).get();
    }
}

