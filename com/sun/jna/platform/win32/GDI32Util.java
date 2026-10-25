/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinNT;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class GDI32Util {
    private static final DirectColorModel SCREENSHOT_COLOR_MODEL = new DirectColorModel(24, 0xFF0000, 65280, 255);
    private static final int[] SCREENSHOT_BAND_MASKS = new int[]{SCREENSHOT_COLOR_MODEL.getRedMask(), SCREENSHOT_COLOR_MODEL.getGreenMask(), SCREENSHOT_COLOR_MODEL.getBlueMask()};

    public static BufferedImage getScreenshot(WinDef.HWND hWND) {
        Object object;
        Object object2;
        Object object3 = new WinDef.RECT();
        if (!User32.INSTANCE.GetWindowRect(hWND, (WinDef.RECT)object3)) {
            throw new Win32Exception(Native.getLastError());
        }
        object3 = ((WinDef.RECT)object3).toRectangle();
        int n2 = ((Rectangle)object3).width;
        int n3 = ((Rectangle)object3).height;
        if (n2 == 0 || n3 == 0) {
            throw new IllegalStateException("Window width and/or height were 0 even though GetWindowRect did not appear to fail.");
        }
        WinDef.HDC hDC = User32.INSTANCE.GetDC(hWND);
        if (hDC == null) {
            throw new Win32Exception(Native.getLastError());
        }
        Object object4 = null;
        WinDef.HDC hDC2 = null;
        WinDef.HBITMAP hBITMAP = null;
        WinNT.HANDLE hANDLE = null;
        BufferedImage bufferedImage = null;
        try {
            hDC2 = GDI32.INSTANCE.CreateCompatibleDC(hDC);
            if (hDC2 == null) {
                throw new Win32Exception(Native.getLastError());
            }
            hBITMAP = GDI32.INSTANCE.CreateCompatibleBitmap(hDC, n2, n3);
            if (hBITMAP == null) {
                throw new Win32Exception(Native.getLastError());
            }
            hANDLE = GDI32.INSTANCE.SelectObject(hDC2, hBITMAP);
            if (hANDLE == null) {
                throw new Win32Exception(Native.getLastError());
            }
            if (!GDI32.INSTANCE.BitBlt(hDC2, 0, 0, n2, n3, hDC, 0, 0, 0xCC0020)) {
                throw new Win32Exception(Native.getLastError());
            }
            WinGDI.BITMAPINFO bITMAPINFO = new WinGDI.BITMAPINFO();
            new WinGDI.BITMAPINFO().bmiHeader.biWidth = n2;
            bITMAPINFO.bmiHeader.biHeight = -n3;
            bITMAPINFO.bmiHeader.biPlanes = 1;
            bITMAPINFO.bmiHeader.biBitCount = (short)32;
            bITMAPINFO.bmiHeader.biCompression = 0;
            object2 = new Memory(n2 * n3 << 2);
            int n4 = GDI32.INSTANCE.GetDIBits(hDC, hBITMAP, 0, n3, (Pointer)object2, bITMAPINFO, 0);
            if (n4 == 0 || n4 == 87) {
                throw new Win32Exception(Native.getLastError());
            }
            n4 = n2 * n3;
            object = new DataBufferInt(((Pointer)object2).getIntArray(0L, n4), n4);
            WritableRaster writableRaster = Raster.createPackedRaster((DataBuffer)object, n2, n3, n2, SCREENSHOT_BAND_MASKS, null);
            bufferedImage = new BufferedImage(SCREENSHOT_COLOR_MODEL, writableRaster, false, null);
        }
        catch (Win32Exception win32Exception) {
            Object object5 = win32Exception;
            object4 = win32Exception;
            if (hANDLE != null && ((object5 = GDI32.INSTANCE.SelectObject(hDC2, hANDLE)) == null || WinGDI.HGDI_ERROR.equals(object5))) {
                Win32Exception win32Exception2 = new Win32Exception(Native.getLastError());
                win32Exception2.addSuppressedReflected((Throwable)object4);
                object4 = win32Exception2;
            }
            if (hBITMAP != null && !GDI32.INSTANCE.DeleteObject(hBITMAP)) {
                object5 = new Win32Exception(Native.getLastError());
                ((Win32Exception)object5).addSuppressedReflected((Throwable)object4);
                object4 = object5;
            }
            if (hDC2 != null && !GDI32.INSTANCE.DeleteDC(hDC2)) {
                object5 = new Win32Exception(Native.getLastError());
                ((Win32Exception)object5).addSuppressedReflected((Throwable)object4);
                object4 = object5;
            }
            if (hDC != null && 0 == User32.INSTANCE.ReleaseDC(hWND, hDC)) {
                throw new IllegalStateException("Device context did not release properly.");
            }
        }
        catch (Throwable throwable) {
            Object object6;
            if (hANDLE != null && ((object6 = GDI32.INSTANCE.SelectObject(hDC2, hANDLE)) == null || WinGDI.HGDI_ERROR.equals(object6))) {
                object4 = object6 = new Win32Exception(Native.getLastError());
            }
            if (hBITMAP != null && !GDI32.INSTANCE.DeleteObject(hBITMAP)) {
                Win32Exception win32Exception = new Win32Exception(Native.getLastError());
                if (object4 != null) {
                    win32Exception.addSuppressedReflected((Throwable)object4);
                }
                object4 = win32Exception;
            }
            if (hDC2 != null && !GDI32.INSTANCE.DeleteDC(hDC2)) {
                Win32Exception win32Exception = new Win32Exception(Native.getLastError());
                if (object4 != null) {
                    win32Exception.addSuppressedReflected((Throwable)object4);
                }
            }
            if (hDC != null && 0 == User32.INSTANCE.ReleaseDC(hWND, hDC)) {
                throw new IllegalStateException("Device context did not release properly.");
            }
            throw throwable;
        }
        if (hANDLE != null && ((object = GDI32.INSTANCE.SelectObject(hDC2, hANDLE)) == null || WinGDI.HGDI_ERROR.equals(object))) {
            object4 = object2 = new Win32Exception(Native.getLastError());
        }
        if (hBITMAP != null && !GDI32.INSTANCE.DeleteObject(hBITMAP)) {
            object = new Win32Exception(Native.getLastError());
            if (object4 != null) {
                ((Win32Exception)object).addSuppressedReflected((Throwable)object4);
            }
            object4 = object;
        }
        if (hDC2 != null && !GDI32.INSTANCE.DeleteDC(hDC2)) {
            object = new Win32Exception(Native.getLastError());
            if (object4 != null) {
                ((Win32Exception)object).addSuppressedReflected((Throwable)object4);
            }
            object4 = object;
        }
        if (hDC != null && 0 == User32.INSTANCE.ReleaseDC(hWND, hDC)) {
            throw new IllegalStateException("Device context did not release properly.");
        }
        if (object4 != null) {
            throw object4;
        }
        return bufferedImage;
    }
}

