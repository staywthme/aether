/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32.COM;

import com.sun.jna.Function;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public interface IEnumIDList {
    public static final Guid.IID IID_IEnumIDList = new Guid.IID("{000214F2-0000-0000-C000-000000000046}");

    public WinNT.HRESULT QueryInterface(Guid.REFIID var1, PointerByReference var2);

    public int AddRef();

    public int Release();

    public WinNT.HRESULT Next(int var1, PointerByReference var2, IntByReference var3);

    public WinNT.HRESULT Skip(int var1);

    public WinNT.HRESULT Reset();

    public WinNT.HRESULT Clone(PointerByReference var1);

    public static class Converter {
        public static IEnumIDList PointerToIEnumIDList(PointerByReference object) {
            object = ((PointerByReference)object).getValue();
            Pointer pointer = ((Pointer)object).getPointer(0L);
            final Pointer[] pointerArray = new Pointer[7];
            pointer.read(0L, pointerArray, 0, 7);
            return new IEnumIDList((Pointer)object){
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
                public final WinNT.HRESULT Next(int n2, PointerByReference pointerByReference, IntByReference intByReference) {
                    Function function = Function.getFunction(pointerArray[3], 63);
                    return new WinNT.HRESULT(function.invokeInt(new Object[]{this.val$interfacePointer, n2, pointerByReference, intByReference}));
                }

                @Override
                public final WinNT.HRESULT Skip(int n2) {
                    Function function = Function.getFunction(pointerArray[4], 63);
                    return new WinNT.HRESULT(function.invokeInt(new Object[]{this.val$interfacePointer, n2}));
                }

                @Override
                public final WinNT.HRESULT Reset() {
                    Function function = Function.getFunction(pointerArray[5], 63);
                    return new WinNT.HRESULT(function.invokeInt(new Object[]{this.val$interfacePointer}));
                }

                @Override
                public final WinNT.HRESULT Clone(PointerByReference pointerByReference) {
                    Function function = Function.getFunction(pointerArray[6], 63);
                    return new WinNT.HRESULT(function.invokeInt(new Object[]{this.val$interfacePointer, pointerByReference}));
                }
            };
        }
    }
}

