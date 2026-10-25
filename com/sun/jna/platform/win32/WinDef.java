/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.IntegerType;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.ByReference;
import java.awt.Rectangle;

public interface WinDef {
    public static final int MAX_PATH = 260;

    public static class HGLRCByReference
    extends WinNT.HANDLEByReference {
        public HGLRCByReference() {
        }

        public HGLRCByReference(HGLRC hGLRC) {
            super(hGLRC);
        }
    }

    public static class HGLRC
    extends WinNT.HANDLE {
        public HGLRC() {
        }

        public HGLRC(Pointer pointer) {
            super(pointer);
        }
    }

    public static class CHARByReference
    extends ByReference {
        public CHARByReference() {
            this(new CHAR(0L));
        }

        public CHARByReference(CHAR cHAR) {
            super(1);
            this.setValue(cHAR);
        }

        public void setValue(CHAR cHAR) {
            this.getPointer().setByte(0L, cHAR.byteValue());
        }

        public CHAR getValue() {
            return new CHAR(this.getPointer().getByte(0L));
        }
    }

    public static class CHAR
    extends IntegerType
    implements Comparable<CHAR> {
        public static final int SIZE = 1;

        public CHAR() {
            this(0L);
        }

        public CHAR(byte by) {
            this((long)(by & 0xFF));
        }

        public CHAR(long l2) {
            super(1, l2, false);
        }

        @Override
        public int compareTo(CHAR cHAR) {
            return CHAR.compare(this, cHAR);
        }
    }

    public static class BYTE
    extends UCHAR {
        public BYTE() {
            this(0L);
        }

        public BYTE(long l2) {
            super(l2);
        }
    }

    public static class UCHAR
    extends IntegerType
    implements Comparable<UCHAR> {
        public static final int SIZE = 1;

        public UCHAR() {
            this(0L);
        }

        public UCHAR(char c2) {
            this((long)(c2 & 0xFF));
        }

        public UCHAR(long l2) {
            super(1, l2, true);
        }

        @Override
        public int compareTo(UCHAR uCHAR) {
            return UCHAR.compare(this, uCHAR);
        }
    }

    public static class BOOLByReference
    extends ByReference {
        public BOOLByReference() {
            this(new BOOL(0L));
        }

        public BOOLByReference(BOOL bOOL) {
            super(4);
            this.setValue(bOOL);
        }

        public void setValue(BOOL bOOL) {
            this.getPointer().setInt(0L, bOOL.intValue());
        }

        public BOOL getValue() {
            return new BOOL((long)this.getPointer().getInt(0L));
        }
    }

    public static class BOOL
    extends IntegerType
    implements Comparable<BOOL> {
        public static final int SIZE = 4;

        public BOOL() {
            this(0L);
        }

        public BOOL(boolean bl) {
            this(bl ? 1L : 0L);
        }

        public BOOL(long l2) {
            super(4, l2, false);
            assert (l2 == 0L || l2 == 1L);
        }

        public boolean booleanValue() {
            return this.intValue() > 0;
        }

        @Override
        public String toString() {
            return Boolean.toString(this.booleanValue());
        }

        @Override
        public int compareTo(BOOL bOOL) {
            return BOOL.compare(this, bOOL);
        }

        public static int compare(BOOL bOOL, BOOL bOOL2) {
            if (bOOL == bOOL2) {
                return 0;
            }
            if (bOOL == null) {
                return 1;
            }
            if (bOOL2 == null) {
                return -1;
            }
            return BOOL.compare(bOOL.booleanValue(), bOOL2.booleanValue());
        }

        public static int compare(BOOL bOOL, boolean bl) {
            if (bOOL == null) {
                return 1;
            }
            return BOOL.compare(bOOL.booleanValue(), bl);
        }

        public static int compare(boolean bl, boolean bl2) {
            if (bl == bl2) {
                return 0;
            }
            if (bl) {
                return 1;
            }
            return -1;
        }
    }

    public static class LCID
    extends DWORD {
        public LCID() {
            super(0L);
        }

        public LCID(long l2) {
            super(l2);
        }
    }

    public static class SCODEByReference
    extends ByReference {
        public SCODEByReference() {
            this(new SCODE(0L));
        }

        public SCODEByReference(SCODE sCODE) {
            super(SCODE.SIZE);
            this.setValue(sCODE);
        }

        public void setValue(SCODE sCODE) {
            this.getPointer().setInt(0L, sCODE.intValue());
        }

        public SCODE getValue() {
            return new SCODE((long)this.getPointer().getInt(0L));
        }
    }

    public static class SCODE
    extends ULONG {
        public SCODE() {
            this(0L);
        }

        public SCODE(long l2) {
            super(l2);
        }
    }

    public static class UINTByReference
    extends ByReference {
        public UINTByReference() {
            this(new UINT(0L));
        }

        public UINTByReference(UINT uINT) {
            super(4);
            this.setValue(uINT);
        }

        public void setValue(UINT uINT) {
            this.getPointer().setInt(0L, uINT.intValue());
        }

        public UINT getValue() {
            return new UINT((long)this.getPointer().getInt(0L));
        }
    }

    public static class UINT
    extends IntegerType
    implements Comparable<UINT> {
        public static final int SIZE = 4;

        public UINT() {
            this(0L);
        }

        public UINT(long l2) {
            super(4, l2, true);
        }

        @Override
        public int compareTo(UINT uINT) {
            return UINT.compare(this, uINT);
        }
    }

    public static class SHORT
    extends IntegerType
    implements Comparable<SHORT> {
        public static final int SIZE = 2;

        public SHORT() {
            this(0L);
        }

        public SHORT(long l2) {
            super(2, l2, false);
        }

        @Override
        public int compareTo(SHORT sHORT) {
            return SHORT.compare(this, sHORT);
        }
    }

    public static class USHORTByReference
    extends ByReference {
        public USHORTByReference() {
            this(new USHORT(0L));
        }

        public USHORTByReference(USHORT uSHORT) {
            super(2);
            this.setValue(uSHORT);
        }

        public USHORTByReference(short s2) {
            super(2);
            this.setValue(new USHORT((long)s2));
        }

        public void setValue(USHORT uSHORT) {
            this.getPointer().setShort(0L, uSHORT.shortValue());
        }

        public USHORT getValue() {
            return new USHORT((long)this.getPointer().getShort(0L));
        }
    }

    public static class USHORT
    extends IntegerType
    implements Comparable<USHORT> {
        public static final int SIZE = 2;

        public USHORT() {
            this(0L);
        }

        public USHORT(long l2) {
            super(2, l2, true);
        }

        @Override
        public int compareTo(USHORT uSHORT) {
            return USHORT.compare(this, uSHORT);
        }
    }

    @Structure.FieldOrder(value={"x", "y"})
    public static class POINT
    extends Structure {
        public int x;
        public int y;

        public POINT() {
        }

        public POINT(Pointer pointer) {
            super(pointer);
            this.read();
        }

        public POINT(int n2, int n3) {
            this.x = n2;
            this.y = n3;
        }

        public static class ByValue
        extends POINT
        implements Structure.ByValue {
            public ByValue() {
            }

            public ByValue(Pointer pointer) {
                super(pointer);
            }

            public ByValue(int n2, int n3) {
                super(n2, n3);
            }
        }

        public static class ByReference
        extends POINT
        implements Structure.ByReference {
            public ByReference() {
            }

            public ByReference(Pointer pointer) {
                super(pointer);
            }

            public ByReference(int n2, int n3) {
                super(n2, n3);
            }
        }
    }

    public static class LPVOID
    extends PointerType {
        public LPVOID() {
        }

        public LPVOID(Pointer pointer) {
            super(pointer);
        }
    }

    public static class PVOID
    extends PointerType {
        public PVOID() {
        }

        public PVOID(Pointer pointer) {
            super(pointer);
        }
    }

    public static class ATOM
    extends WORD {
        public ATOM() {
            this(0L);
        }

        public ATOM(long l2) {
            super(l2);
        }
    }

    public static class HBRUSH
    extends WinNT.HANDLE {
        public HBRUSH() {
        }

        public HBRUSH(Pointer pointer) {
            super(pointer);
        }
    }

    public static class DWORDLONG
    extends IntegerType
    implements Comparable<DWORDLONG> {
        public static final int SIZE = 8;

        public DWORDLONG() {
            this(0L);
        }

        public DWORDLONG(long l2) {
            super(8, l2, true);
        }

        @Override
        public int compareTo(DWORDLONG dWORDLONG) {
            return DWORDLONG.compare(this, dWORDLONG);
        }
    }

    public static class ULONGLONGByReference
    extends ByReference {
        public ULONGLONGByReference() {
            this(new ULONGLONG(0L));
        }

        public ULONGLONGByReference(ULONGLONG uLONGLONG) {
            super(8);
            this.setValue(uLONGLONG);
        }

        public void setValue(ULONGLONG uLONGLONG) {
            this.getPointer().setLong(0L, uLONGLONG.longValue());
        }

        public ULONGLONG getValue() {
            return new ULONGLONG(this.getPointer().getLong(0L));
        }
    }

    public static class ULONGLONG
    extends IntegerType
    implements Comparable<ULONGLONG> {
        public static final int SIZE = 8;

        public ULONGLONG() {
            this(0L);
        }

        public ULONGLONG(long l2) {
            super(8, l2, true);
        }

        @Override
        public int compareTo(ULONGLONG uLONGLONG) {
            return ULONGLONG.compare(this, uLONGLONG);
        }
    }

    public static class ULONGByReference
    extends ByReference {
        public ULONGByReference() {
            this(new ULONG(0L));
        }

        public ULONGByReference(ULONG uLONG) {
            super(ULONG.SIZE);
            this.setValue(uLONG);
        }

        public void setValue(ULONG uLONG) {
            this.getPointer().setInt(0L, uLONG.intValue());
        }

        public ULONG getValue() {
            return new ULONG((long)this.getPointer().getInt(0L));
        }
    }

    public static class ULONG
    extends IntegerType
    implements Comparable<ULONG> {
        public static final int SIZE = Native.LONG_SIZE;

        public ULONG() {
            this(0L);
        }

        public ULONG(long l2) {
            super(SIZE, l2, true);
        }

        @Override
        public int compareTo(ULONG uLONG) {
            return ULONG.compare(this, uLONG);
        }
    }

    @Structure.FieldOrder(value={"left", "top", "right", "bottom"})
    public static class RECT
    extends Structure {
        public int left;
        public int top;
        public int right;
        public int bottom;

        public Rectangle toRectangle() {
            return new Rectangle(this.left, this.top, this.right - this.left, this.bottom - this.top);
        }

        @Override
        public String toString() {
            return "[(" + this.left + "," + this.top + ")(" + this.right + "," + this.bottom + ")]";
        }
    }

    public static class WPARAM
    extends UINT_PTR {
        public WPARAM() {
            this(0L);
        }

        public WPARAM(long l2) {
            super(l2);
        }
    }

    public static class UINT_PTR
    extends IntegerType {
        public UINT_PTR() {
            super(Native.POINTER_SIZE);
        }

        public UINT_PTR(long l2) {
            super(Native.POINTER_SIZE, l2, true);
        }

        public Pointer toPointer() {
            return Pointer.createConstant(this.longValue());
        }
    }

    public static class INT_PTR
    extends IntegerType {
        public INT_PTR() {
            super(Native.POINTER_SIZE);
        }

        public INT_PTR(long l2) {
            super(Native.POINTER_SIZE, l2);
        }

        public Pointer toPointer() {
            return Pointer.createConstant(this.longValue());
        }
    }

    public static class LRESULT
    extends BaseTSD.LONG_PTR {
        public LRESULT() {
            this(0L);
        }

        public LRESULT(long l2) {
            super(l2);
        }
    }

    public static class LPARAM
    extends BaseTSD.LONG_PTR {
        public LPARAM() {
            this(0L);
        }

        public LPARAM(long l2) {
            super(l2);
        }
    }

    public static class HKL
    extends WinNT.HANDLE {
        public HKL() {
        }

        public HKL(Pointer pointer) {
            super(pointer);
        }

        public HKL(int n2) {
            super(Pointer.createConstant(n2));
        }

        public int getLanguageIdentifier() {
            return (int)(Pointer.nativeValue(this.getPointer()) & 0xFFFFL);
        }

        public int getDeviceHandle() {
            return (int)(Pointer.nativeValue(this.getPointer()) >> 16 & 0xFFFFL);
        }

        @Override
        public String toString() {
            return String.format("%08x", Pointer.nativeValue(this.getPointer()));
        }
    }

    public static class HFONT
    extends WinNT.HANDLE {
        public HFONT() {
        }

        public HFONT(Pointer pointer) {
            super(pointer);
        }
    }

    public static class HMODULE
    extends HINSTANCE {
    }

    public static class HINSTANCE
    extends WinNT.HANDLE {
    }

    public static class HWND
    extends WinNT.HANDLE {
        public HWND() {
        }

        public HWND(Pointer pointer) {
            super(pointer);
        }
    }

    public static class HRGN
    extends WinNT.HANDLE {
        public HRGN() {
        }

        public HRGN(Pointer pointer) {
            super(pointer);
        }
    }

    public static class HBITMAP
    extends WinNT.HANDLE {
        public HBITMAP() {
        }

        public HBITMAP(Pointer pointer) {
            super(pointer);
        }
    }

    public static class HPALETTE
    extends WinNT.HANDLE {
        public HPALETTE() {
        }

        public HPALETTE(Pointer pointer) {
            super(pointer);
        }
    }

    public static class HRSRC
    extends WinNT.HANDLE {
        public HRSRC() {
        }

        public HRSRC(Pointer pointer) {
            super(pointer);
        }
    }

    public static class HPEN
    extends WinNT.HANDLE {
        public HPEN() {
        }

        public HPEN(Pointer pointer) {
            super(pointer);
        }
    }

    public static class HMENU
    extends WinNT.HANDLE {
        public HMENU() {
        }

        public HMENU(Pointer pointer) {
            super(pointer);
        }
    }

    public static class HCURSOR
    extends HICON {
        public HCURSOR() {
        }

        public HCURSOR(Pointer pointer) {
            super(pointer);
        }
    }

    public static class HICON
    extends WinNT.HANDLE {
        public HICON() {
        }

        public HICON(WinNT.HANDLE hANDLE) {
            this(hANDLE.getPointer());
        }

        public HICON(Pointer pointer) {
            super(pointer);
        }
    }

    public static class HDC
    extends WinNT.HANDLE {
        public HDC() {
        }

        public HDC(Pointer pointer) {
            super(pointer);
        }
    }

    public static class LONGLONGByReference
    extends ByReference {
        public LONGLONGByReference() {
            this(new LONGLONG(0L));
        }

        public LONGLONGByReference(LONGLONG lONGLONG) {
            super(LONGLONG.SIZE);
            this.setValue(lONGLONG);
        }

        public void setValue(LONGLONG lONGLONG) {
            this.getPointer().setLong(0L, lONGLONG.longValue());
        }

        public LONGLONG getValue() {
            return new LONGLONG(this.getPointer().getLong(0L));
        }
    }

    public static class LONGLONG
    extends IntegerType
    implements Comparable<LONGLONG> {
        public static final int SIZE = Native.LONG_SIZE << 1;

        public LONGLONG() {
            this(0L);
        }

        public LONGLONG(long l2) {
            super(8, l2, false);
        }

        @Override
        public int compareTo(LONGLONG lONGLONG) {
            return LONGLONG.compare(this, lONGLONG);
        }
    }

    public static class LONGByReference
    extends ByReference {
        public LONGByReference() {
            this(new LONG(0L));
        }

        public LONGByReference(LONG lONG) {
            super(LONG.SIZE);
            this.setValue(lONG);
        }

        public void setValue(LONG lONG) {
            this.getPointer().setInt(0L, lONG.intValue());
        }

        public LONG getValue() {
            return new LONG((long)this.getPointer().getInt(0L));
        }
    }

    public static class LONG
    extends IntegerType
    implements Comparable<LONG> {
        public static final int SIZE = Native.LONG_SIZE;

        public LONG() {
            this(0L);
        }

        public LONG(long l2) {
            super(SIZE, l2);
        }

        @Override
        public int compareTo(LONG lONG) {
            return LONG.compare(this, lONG);
        }
    }

    public static class DWORDByReference
    extends ByReference {
        public DWORDByReference() {
            this(new DWORD(0L));
        }

        public DWORDByReference(DWORD dWORD) {
            super(4);
            this.setValue(dWORD);
        }

        public void setValue(DWORD dWORD) {
            this.getPointer().setInt(0L, dWORD.intValue());
        }

        public DWORD getValue() {
            return new DWORD((long)this.getPointer().getInt(0L));
        }
    }

    public static class DWORD
    extends IntegerType
    implements Comparable<DWORD> {
        public static final int SIZE = 4;

        public DWORD() {
            this(0L);
        }

        public DWORD(long l2) {
            super(4, l2, true);
        }

        public WORD getLow() {
            return new WORD(this.longValue() & 0xFFFFL);
        }

        public WORD getHigh() {
            return new WORD(this.longValue() >> 16 & 0xFFFFL);
        }

        @Override
        public int compareTo(DWORD dWORD) {
            return DWORD.compare(this, dWORD);
        }
    }

    public static class WORDByReference
    extends ByReference {
        public WORDByReference() {
            this(new WORD(0L));
        }

        public WORDByReference(WORD wORD) {
            super(2);
            this.setValue(wORD);
        }

        public void setValue(WORD wORD) {
            this.getPointer().setShort(0L, wORD.shortValue());
        }

        public WORD getValue() {
            return new WORD((long)this.getPointer().getShort(0L));
        }
    }

    public static class WORD
    extends IntegerType
    implements Comparable<WORD> {
        public static final int SIZE = 2;

        public WORD() {
            this(0L);
        }

        public WORD(long l2) {
            super(2, l2, true);
        }

        @Override
        public int compareTo(WORD wORD) {
            return WORD.compare(this, wORD);
        }
    }
}

