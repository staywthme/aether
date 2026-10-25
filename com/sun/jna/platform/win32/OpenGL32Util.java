/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Function;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.OpenGL32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.User32Util;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinGDI;

public abstract class OpenGL32Util {
    public static Function wglGetProcAddress(String object) {
        if ((object = OpenGL32.INSTANCE.wglGetProcAddress((String)object)) == null) {
            return null;
        }
        return Function.getFunction((Pointer)object);
    }

    public static int countGpusNV() {
        PointerType pointerType = User32Util.createWindow("Message", null, 0, 0, 0, 0, 0, null, null, null, null);
        WinDef.HDC hDC = User32.INSTANCE.GetDC((WinDef.HWND)pointerType);
        Object object = new WinGDI.PIXELFORMATDESCRIPTOR.ByReference();
        new WinGDI.PIXELFORMATDESCRIPTOR.ByReference().nVersion = 1;
        ((WinGDI.PIXELFORMATDESCRIPTOR)object).dwFlags = 37;
        ((WinGDI.PIXELFORMATDESCRIPTOR)object).iPixelType = 0;
        ((WinGDI.PIXELFORMATDESCRIPTOR)object).cColorBits = (byte)24;
        ((WinGDI.PIXELFORMATDESCRIPTOR)object).cDepthBits = (byte)16;
        ((WinGDI.PIXELFORMATDESCRIPTOR)object).iLayerType = 0;
        GDI32.INSTANCE.SetPixelFormat(hDC, GDI32.INSTANCE.ChoosePixelFormat(hDC, (WinGDI.PIXELFORMATDESCRIPTOR.ByReference)object), (WinGDI.PIXELFORMATDESCRIPTOR.ByReference)object);
        object = OpenGL32.INSTANCE.wglCreateContext(hDC);
        OpenGL32.INSTANCE.wglMakeCurrent(hDC, (WinDef.HGLRC)object);
        Pointer pointer = OpenGL32.INSTANCE.wglGetProcAddress("wglEnumGpusNV");
        pointer = pointer == null ? null : Function.getFunction(pointer);
        OpenGL32.INSTANCE.wglDeleteContext((WinDef.HGLRC)object);
        User32.INSTANCE.ReleaseDC((WinDef.HWND)pointerType, hDC);
        User32Util.destroyWindow(pointerType);
        if (pointer == null) {
            return 0;
        }
        pointerType = new WinDef.HGLRCByReference();
        for (int i2 = 0; i2 < 16; ++i2) {
            object = (Boolean)((Function)pointer).invoke(Boolean.class, new Object[]{i2, pointerType});
            if (((Boolean)object).booleanValue()) continue;
            return i2;
        }
        return 0;
    }
}

