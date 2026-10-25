/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.ShTypes;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public interface IShellFolder {
    public static final Guid.IID IID_ISHELLFOLDER = new Guid.IID("{000214E6-0000-0000-C000-000000000046}");

    public WinNT.HRESULT QueryInterface(Guid.REFIID var1, PointerByReference var2);

    public int AddRef();

    public int Release();

    public WinNT.HRESULT ParseDisplayName(WinDef.HWND var1, Pointer var2, String var3, IntByReference var4, PointerByReference var5, IntByReference var6);

    public WinNT.HRESULT EnumObjects(WinDef.HWND var1, int var2, PointerByReference var3);

    public WinNT.HRESULT BindToObject(Pointer var1, Pointer var2, Guid.REFIID var3, PointerByReference var4);

    public WinNT.HRESULT BindToStorage(Pointer var1, Pointer var2, Guid.REFIID var3, PointerByReference var4);

    public WinNT.HRESULT CompareIDs(WinDef.LPARAM var1, Pointer var2, Pointer var3);

    public WinNT.HRESULT CreateViewObject(WinDef.HWND var1, Guid.REFIID var2, PointerByReference var3);

    public WinNT.HRESULT GetAttributesOf(int var1, Pointer var2, IntByReference var3);

    public WinNT.HRESULT GetUIObjectOf(WinDef.HWND var1, int var2, Pointer var3, Guid.REFIID var4, IntByReference var5, PointerByReference var6);

    public WinNT.HRESULT GetDisplayNameOf(Pointer var1, int var2, ShTypes.STRRET var3);

    public WinNT.HRESULT SetNameOf(WinDef.HWND var1, Pointer var2, String var3, int var4, PointerByReference var5);

    public static class Converter {
        public static IShellFolder PointerToIShellFolder(PointerByReference object) {
            object = ((PointerByReference)object).getValue();
            Pointer pointer = ((Pointer)object).getPointer(0L);
            final Pointer[] pointerArray = new Pointer[13];
            pointer.read(0L, pointerArray, 0, 13);
            return new IShellFolder((Pointer)object){
                final /* synthetic */ Pointer val$interfacePointer;
                {
                    this.val$interfacePointer = pointer;
                }

                @Override
                public final WinNT.HRESULT QueryInterface(Guid.REFIID rEFIID, PointerByReference pointerByReference) {
                    Function function = Function.getFunction(pointerArray[0], 63);
                    return new WinNT.HRESULT(function.invokeInt(new Object[]{this.val$interfacePointer, rEFIID, pointerByReference}));
                }

                @Override
                public final int AddRef() {
                    Function function = Function.getFunction(pointerArray[1], 63);
                    return function.invokeInt(new Object[]{this.val$interfacePointer});
                }

                @Override
                public final int Release() {
                    Function function = Function.getFunction(pointerArray[2], 63);
                    return function.invokeInt(new Object[]{this.val$interfacePointer});
                }

                @Override
                public final WinNT.HRESULT ParseDisplayName(WinDef.HWND hWND, Pointer pointer, String object, IntByReference intByReference, PointerByReference pointerByReference, IntByReference intByReference2) {
                    Function function = Function.getFunction(pointerArray[3], 63);
                    object = Native.toCharArray((String)object);
                    return new WinNT.HRESULT(function.invokeInt(new Object[]{this.val$interfacePointer, hWND, pointer, object, intByReference, pointerByReference, intByReference2}));
                }

                @Override
                public final WinNT.HRESULT EnumObjects(WinDef.HWND hWND, int n2, PointerByReference pointerByReference) {
                    Function function = Function.getFunction(pointerArray[4], 63);
                    return new WinNT.HRESULT(function.invokeInt(new Object[]{this.val$interfacePointer, hWND, n2, pointerByReference}));
                }

                @Override
                public final WinNT.HRESULT BindToObject(Pointer pointer, Pointer pointer2, Guid.REFIID rEFIID, PointerByReference pointerByReference) {
                    Function function = Function.getFunction(pointerArray[5], 63);
                    return new WinNT.HRESULT(function.invokeInt(new Object[]{this.val$interfacePointer, pointer, pointer2, rEFIID, pointerByReference}));
                }

                @Override
                public final WinNT.HRESULT BindToStorage(Pointer pointer, Pointer pointer2, Guid.REFIID rEFIID, PointerByReference pointerByReference) {
                    Function function = Function.getFunction(pointerArray[6], 63);
                    return new WinNT.HRESULT(function.invokeInt(new Object[]{this.val$interfacePointer, pointer, pointer2, rEFIID, pointerByReference}));
                }

                @Override
                public final WinNT.HRESULT CompareIDs(WinDef.LPARAM lPARAM, Pointer pointer, Pointer pointer2) {
                    Function function = Function.getFunction(pointerArray[7], 63);
                    return new WinNT.HRESULT(function.invokeInt(new Object[]{this.val$interfacePointer, lPARAM, pointer, pointer2}));
                }

                @Override
                public final WinNT.HRESULT CreateViewObject(WinDef.HWND hWND, Guid.REFIID rEFIID, PointerByReference pointerByReference) {
                    Function function = Function.getFunction(pointerArray[8], 63);
                    return new WinNT.HRESULT(function.invokeInt(new Object[]{this.val$interfacePointer, hWND, rEFIID, pointerByReference}));
                }

                @Override
                public final WinNT.HRESULT GetAttributesOf(int n2, Pointer pointer, IntByReference intByReference) {
                    Function function = Function.getFunction(pointerArray[9], 63);
                    return new WinNT.HRESULT(function.invokeInt(new Object[]{this.val$interfacePointer, n2, pointer, intByReference}));
                }

                @Override
                public final WinNT.HRESULT GetUIObjectOf(WinDef.HWND hWND, int n2, Pointer pointer, Guid.REFIID rEFIID, IntByReference intByReference, PointerByReference pointerByReference) {
                    Function function = Function.getFunction(pointerArray[10], 63);
                    return new WinNT.HRESULT(function.invokeInt(new Object[]{this.val$interfacePointer, hWND, n2, pointer, rEFIID, intByReference, pointerByReference}));
                }

                @Override
                public final WinNT.HRESULT GetDisplayNameOf(Pointer pointer, int n2, ShTypes.STRRET sTRRET) {
                    Function function = Function.getFunction(pointerArray[11], 63);
                    return new WinNT.HRESULT(function.invokeInt(new Object[]{this.val$interfacePointer, pointer, n2, sTRRET}));
                }

                @Override
                public final WinNT.HRESULT SetNameOf(WinDef.HWND hWND, Pointer pointer, String string, int n2, PointerByReference pointerByReference) {
                    Function function = Function.getFunction(pointerArray[12], 63);
                    return new WinNT.HRESULT(function.invokeInt(new Object[]{this.val$interfacePointer, hWND, pointer, string, n2, pointerByReference}));
                }
            };
        }
    }
}

