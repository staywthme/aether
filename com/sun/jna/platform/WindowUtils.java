/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna.platform;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;
import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.RasterRangesUtils;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.PsapiUtil;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.awt.AWTEvent;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

public class WindowUtils {
    private static final Logger LOG = Logger.getLogger(WindowUtils.class.getName());
    private static final String TRANSPARENT_OLD_BG = "transparent-old-bg";
    private static final String TRANSPARENT_OLD_OPAQUE = "transparent-old-opaque";
    private static final String TRANSPARENT_ALPHA = "transparent-alpha";
    public static final Shape MASK_NONE = null;

    private static NativeWindowUtils getInstance() {
        return Holder.INSTANCE;
    }

    public static void setWindowMask(Window window, Shape shape) {
        WindowUtils.getInstance().setWindowMask((Component)window, shape);
    }

    public static void setComponentMask(Component component, Shape shape) {
        WindowUtils.getInstance().setWindowMask(component, shape);
    }

    public static void setWindowMask(Window window, Icon icon) {
        WindowUtils.getInstance().setWindowMask((Component)window, icon);
    }

    public static boolean isWindowAlphaSupported() {
        return WindowUtils.getInstance().isWindowAlphaSupported();
    }

    public static GraphicsConfiguration getAlphaCompatibleGraphicsConfiguration() {
        return WindowUtils.getInstance().getAlphaCompatibleGraphicsConfiguration();
    }

    public static void setWindowAlpha(Window window, float f2) {
        WindowUtils.getInstance().setWindowAlpha(window, Math.max(0.0f, Math.min(f2, 1.0f)));
    }

    public static void setWindowTransparent(Window window, boolean bl) {
        WindowUtils.getInstance().setWindowTransparent(window, bl);
    }

    public static BufferedImage getWindowIcon(WinDef.HWND hWND) {
        return WindowUtils.getInstance().getWindowIcon(hWND);
    }

    public static Dimension getIconSize(WinDef.HICON hICON) {
        return WindowUtils.getInstance().getIconSize(hICON);
    }

    public static List<DesktopWindow> getAllWindows(boolean bl) {
        return WindowUtils.getInstance().getAllWindows(bl);
    }

    public static String getWindowTitle(WinDef.HWND hWND) {
        return WindowUtils.getInstance().getWindowTitle(hWND);
    }

    public static String getProcessFilePath(WinDef.HWND hWND) {
        return WindowUtils.getInstance().getProcessFilePath(hWND);
    }

    public static Rectangle getWindowLocationAndSize(WinDef.HWND hWND) {
        return WindowUtils.getInstance().getWindowLocationAndSize(hWND);
    }

    static class X11WindowUtils
    extends NativeWindowUtils {
        private boolean didCheck;
        private long[] alphaVisualIDs = new long[0];
        private static final long OPAQUE = 0xFFFFFFFFL;
        private static final String OPACITY = "_NET_WM_WINDOW_OPACITY";

        private X11WindowUtils() {
        }

        private static X11.Pixmap createBitmap(X11.Display display, X11.Window drawable, Raster xRectangleArray) {
            X11 x11 = X11.INSTANCE;
            Rectangle rectangle = xRectangleArray.getBounds();
            int n2 = rectangle.x + rectangle.width;
            int n3 = rectangle.y + rectangle.height;
            X11.GC gC = x11.XCreateGC(display, drawable = x11.XCreatePixmap(display, drawable, n2, n3, 1), new NativeLong(0L), null);
            if (gC == null) {
                return null;
            }
            x11.XSetForeground(display, gC, new NativeLong(0L));
            x11.XFillRectangle(display, drawable, gC, 0, 0, n2, n3);
            final ArrayList arrayList = new ArrayList();
            try {
                RasterRangesUtils.outputOccupiedRanges((Raster)xRectangleArray, new RasterRangesUtils.RangesOutput(){

                    @Override
                    public final boolean outputRange(int n2, int n3, int n4, int n5) {
                        arrayList.add(new Rectangle(n2, n3, n4, n5));
                        return true;
                    }
                });
                xRectangleArray = (X11.XRectangle[])new X11.XRectangle().toArray(arrayList.size());
                for (n2 = 0; n2 < xRectangleArray.length; ++n2) {
                    Rectangle rectangle2 = (Rectangle)arrayList.get(n2);
                    xRectangleArray[n2].x = (short)rectangle2.x;
                    xRectangleArray[n2].y = (short)rectangle2.y;
                    xRectangleArray[n2].width = (short)rectangle2.width;
                    xRectangleArray[n2].height = (short)rectangle2.height;
                    Pointer pointer = xRectangleArray[n2].getPointer();
                    pointer.setShort(0L, (short)rectangle2.x);
                    pointer.setShort(2L, (short)rectangle2.y);
                    pointer.setShort(4L, (short)rectangle2.width);
                    pointer.setShort(6L, (short)rectangle2.height);
                    xRectangleArray[n2].setAutoSynch(false);
                }
                x11.XSetForeground(display, gC, new NativeLong(1L));
                x11.XFillRectangles(display, drawable, gC, xRectangleArray, xRectangleArray.length);
            }
            finally {
                x11.XFreeGC(display, gC);
            }
            return drawable;
        }

        @Override
        public boolean isWindowAlphaSupported() {
            return this.getAlphaVisualIDs().length > 0;
        }

        private static long getVisualID(GraphicsConfiguration object) {
            try {
                object = object.getClass().getMethod("getVisual", null).invoke(object, null);
                return ((Number)object).longValue();
            }
            catch (Exception exception) {
                object = exception;
                exception.printStackTrace();
                return -1L;
            }
        }

        @Override
        public GraphicsConfiguration getAlphaCompatibleGraphicsConfiguration() {
            if (((NativeWindowUtils)this).isWindowAlphaSupported()) {
                GraphicsDevice[] graphicsDeviceArray = GraphicsEnvironment.getLocalGraphicsEnvironment();
                graphicsDeviceArray = graphicsDeviceArray.getScreenDevices();
                for (int i2 = 0; i2 < graphicsDeviceArray.length; ++i2) {
                    GraphicsConfiguration[] graphicsConfigurationArray = graphicsDeviceArray[i2].getConfigurations();
                    for (int i3 = 0; i3 < graphicsConfigurationArray.length; ++i3) {
                        long l2 = X11WindowUtils.getVisualID(graphicsConfigurationArray[i3]);
                        long[] lArray = this.getAlphaVisualIDs();
                        for (int i4 = 0; i4 < lArray.length; ++i4) {
                            if (l2 != lArray[i4]) continue;
                            return graphicsConfigurationArray[i3];
                        }
                    }
                }
            }
            return super.getAlphaCompatibleGraphicsConfiguration();
        }

        private synchronized long[] getAlphaVisualIDs() {
            if (this.didCheck) {
                return this.alphaVisualIDs;
            }
            this.didCheck = true;
            X11 x11 = X11.INSTANCE;
            X11.Display display = x11.XOpenDisplay(null);
            if (display == null) {
                return this.alphaVisualIDs;
            }
            Structure structure = null;
            try {
                int n2 = x11.XDefaultScreen(display);
                X11.XVisualInfo[] xVisualInfoArray = new X11.XVisualInfo();
                new X11.XVisualInfo().screen = n2;
                xVisualInfoArray.depth = 32;
                xVisualInfoArray.c_class = 4;
                Serializable serializable = new NativeLong(14L);
                IntByReference intByReference = new IntByReference();
                structure = x11.XGetVisualInfo(display, (NativeLong)serializable, (X11.XVisualInfo)xVisualInfoArray, intByReference);
                if (structure != null) {
                    int n3;
                    serializable = new ArrayList();
                    xVisualInfoArray = (X11.XVisualInfo[])structure.toArray(intByReference.getValue());
                    for (n3 = 0; n3 < xVisualInfoArray.length; ++n3) {
                        X11.Xrender.XRenderPictFormat xRenderPictFormat = X11.Xrender.INSTANCE.XRenderFindVisualFormat(display, xVisualInfoArray[n3].visual);
                        if (xRenderPictFormat.type != 1 || xRenderPictFormat.direct.alphaMask == 0) continue;
                        serializable.add(xVisualInfoArray[n3].visualid);
                    }
                    this.alphaVisualIDs = new long[serializable.size()];
                    for (n3 = 0; n3 < this.alphaVisualIDs.length; ++n3) {
                        this.alphaVisualIDs[n3] = ((Number)serializable.get(n3)).longValue();
                    }
                    long[] lArray = this.alphaVisualIDs;
                    return lArray;
                }
            }
            finally {
                if (structure != null) {
                    x11.XFree(structure.getPointer());
                }
                x11.XCloseDisplay(display);
            }
            return this.alphaVisualIDs;
        }

        private static X11.Window getContentWindow(Window object, X11.Display display, X11.Window window, Point point) {
            if (object instanceof Frame && !((Frame)object).isUndecorated() || object instanceof Dialog && !((Dialog)object).isUndecorated()) {
                object = X11.INSTANCE;
                Object object2 = new X11.WindowByReference();
                Object object3 = new X11.WindowByReference();
                PointerByReference pointerByReference = new PointerByReference();
                IntByReference intByReference = new IntByReference();
                object.XQueryTree(display, window, (X11.WindowByReference)object2, (X11.WindowByReference)object3, pointerByReference, intByReference);
                object2 = pointerByReference.getValue();
                int[] nArray = ((Pointer)object2).getIntArray(0L, intByReference.getValue());
                object3 = nArray;
                object3 = nArray;
                int n2 = nArray.length;
                if (n2 > 0) {
                    Object object4 = object3[0];
                    window = new X11.Window((long)object4);
                    object3 = new X11.XWindowAttributes();
                    object.XGetWindowAttributes(display, window, (X11.XWindowAttributes)object3);
                    point.x = -((X11.XWindowAttributes)object3).x;
                    point.y = -((X11.XWindowAttributes)object3).y;
                }
                if (object2 != null) {
                    object.XFree((Pointer)object2);
                }
            }
            return window;
        }

        private static X11.Window getDrawable(Component component) {
            int n2 = (int)Native.getComponentID(component);
            if (n2 == 0) {
                return null;
            }
            return new X11.Window((long)n2);
        }

        @Override
        public void setWindowAlpha(final Window window, final float f2) {
            if (!((NativeWindowUtils)this).isWindowAlphaSupported()) {
                throw new UnsupportedOperationException("This X11 display does not provide a 32-bit visual");
            }
            Runnable runnable2 = new Runnable(){

                @Override
                public void run() {
                    X11 x11 = X11.INSTANCE;
                    X11.Display display = x11.XOpenDisplay(null);
                    if (display == null) {
                        return;
                    }
                    try {
                        X11.Window window2 = X11WindowUtils.getDrawable(window);
                        if (f2 == 1.0f) {
                            x11.XDeleteProperty(display, window2, x11.XInternAtom(display, X11WindowUtils.OPACITY, false));
                        } else {
                            int n2 = (int)(f2 * 4.2949673E9f);
                            IntByReference intByReference = new IntByReference(n2);
                            x11.XChangeProperty(display, window2, x11.XInternAtom(display, X11WindowUtils.OPACITY, false), X11.XA_CARDINAL, 32, 0, intByReference.getPointer(), 1);
                        }
                        return;
                    }
                    finally {
                        x11.XCloseDisplay(display);
                    }
                }
            };
            this.whenDisplayable(window, runnable2);
        }

        @Override
        public void setWindowTransparent(final Window window, final boolean bl) {
            boolean bl2;
            if (!(window instanceof RootPaneContainer)) {
                throw new IllegalArgumentException("Window must be a RootPaneContainer");
            }
            if (!((NativeWindowUtils)this).isWindowAlphaSupported()) {
                throw new UnsupportedOperationException("This X11 display does not provide a 32-bit visual");
            }
            if (!window.getGraphicsConfiguration().equals(((NativeWindowUtils)this).getAlphaCompatibleGraphicsConfiguration())) {
                throw new IllegalArgumentException("Window GraphicsConfiguration '" + window.getGraphicsConfiguration() + "' does not support transparency");
            }
            boolean bl3 = bl2 = window.getBackground() != null && window.getBackground().getAlpha() == 0;
            if (bl == bl2) {
                return;
            }
            this.whenDisplayable(window, new Runnable(){

                @Override
                public void run() {
                    JRootPane jRootPane = ((RootPaneContainer)((Object)window)).getRootPane();
                    JLayeredPane jLayeredPane = jRootPane.getLayeredPane();
                    Container container = jRootPane.getContentPane();
                    if (container instanceof X11TransparentContentPane) {
                        ((X11TransparentContentPane)container).setTransparent(bl);
                    } else if (bl) {
                        container = new X11TransparentContentPane(container);
                        jRootPane.setContentPane(container);
                        jLayeredPane.add((Component)new RepaintTrigger((JComponent)container), JLayeredPane.DRAG_LAYER);
                    }
                    this.setLayersTransparent(window, bl);
                    this.setForceHeavyweightPopups(window, bl);
                    this.setDoubleBuffered(window, !bl);
                }
            });
        }

        private void setWindowShape(final Window window, PixmapSource object) {
            object = new Runnable((PixmapSource)object){
                final /* synthetic */ PixmapSource val$src;
                {
                    this.val$src = pixmapSource;
                }

                @Override
                public void run() {
                    X11.Pixmap pixmap;
                    X11.Display display;
                    X11 x11;
                    block4: {
                        x11 = X11.INSTANCE;
                        display = x11.XOpenDisplay(null);
                        if (display == null) {
                            return;
                        }
                        pixmap = null;
                        try {
                            X11.Window window2 = X11WindowUtils.getDrawable(window);
                            pixmap = this.val$src.getPixmap(display, window2);
                            X11.Xext xext = X11.Xext.INSTANCE;
                            xext.XShapeCombineMask(display, window2, 0, 0, 0, pixmap == null ? X11.Pixmap.None : pixmap, 0);
                            if (pixmap == null) break block4;
                            x11.XFreePixmap(display, pixmap);
                        }
                        catch (Throwable throwable) {
                            if (pixmap != null) {
                                x11.XFreePixmap(display, pixmap);
                            }
                            x11.XCloseDisplay(display);
                            throw throwable;
                        }
                    }
                    x11.XCloseDisplay(display);
                    this.setForceHeavyweightPopups(this.getWindow(window), pixmap != null);
                }
            };
            this.whenDisplayable(window, (Runnable)object);
        }

        @Override
        protected void setMask(Component component, final Raster raster) {
            X11WindowUtils x11WindowUtils = this;
            x11WindowUtils.setWindowShape(x11WindowUtils.getWindow(component), new PixmapSource(){

                @Override
                public X11.Pixmap getPixmap(X11.Display display, X11.Window window) {
                    if (raster != null) {
                        return X11WindowUtils.createBitmap(display, window, raster);
                    }
                    return null;
                }
            });
        }

        static interface PixmapSource {
            public X11.Pixmap getPixmap(X11.Display var1, X11.Window var2);
        }

        class X11TransparentContentPane
        extends NativeWindowUtils.TransparentContentPane {
            private static final long serialVersionUID = 1L;
            private Memory buffer;
            private int[] pixels;
            private final int[] pixel;

            public X11TransparentContentPane(Container container) {
                super(container);
                this.pixel = new int[4];
            }

            @Override
            protected void paintDirect(BufferedImage object, Rectangle rectangle) {
                Object object2 = SwingUtilities.getWindowAncestor(this);
                X11 x11 = X11.INSTANCE;
                X11.Display display = x11.XOpenDisplay(null);
                X11.Window window = X11WindowUtils.getDrawable((Component)object2);
                Point point = new Point();
                window = X11WindowUtils.getContentWindow((Window)object2, display, window, point);
                object2 = x11.XCreateGC(display, window, new NativeLong(0L), null);
                object = ((BufferedImage)object).getData();
                int n2 = rectangle.width;
                int n3 = rectangle.height;
                if (this.buffer == null || this.buffer.size() != (long)(n2 * n3 << 2)) {
                    this.buffer = new Memory(n2 * n3 << 2);
                    this.pixels = new int[n2 * n3];
                }
                for (int i2 = 0; i2 < n3; ++i2) {
                    for (int i3 = 0; i3 < n2; ++i3) {
                        ((Raster)object).getPixel(i3, i2, this.pixel);
                        int n4 = this.pixel[3] & 0xFF;
                        int n5 = this.pixel[2] & 0xFF;
                        int n6 = this.pixel[1] & 0xFF;
                        int n7 = this.pixel[0] & 0xFF;
                        this.pixels[i2 * n2 + i3] = n4 << 24 | n7 << 16 | n6 << 8 | n5;
                    }
                }
                X11.XWindowAttributes xWindowAttributes = new X11.XWindowAttributes();
                x11.XGetWindowAttributes(display, window, xWindowAttributes);
                X11.XImage xImage = x11.XCreateImage(display, xWindowAttributes.visual, 32, 2, 0, this.buffer, n2, n3, 32, n2 << 2);
                ((Pointer)this.buffer).write(0L, this.pixels, 0, this.pixels.length);
                point.x += rectangle.x;
                point.y += rectangle.y;
                x11.XPutImage(display, window, (X11.GC)object2, xImage, 0, 0, point.x, point.y, n2, n3);
                x11.XFree(xImage.getPointer());
                x11.XFreeGC(display, (X11.GC)object2);
                x11.XCloseDisplay(display);
            }
        }
    }

    static class MacWindowUtils
    extends NativeWindowUtils {
        private static final String WDRAG = "apple.awt.draggableWindowBackground";

        private MacWindowUtils() {
        }

        @Override
        public boolean isWindowAlphaSupported() {
            return true;
        }

        private OSXMaskingContentPane installMaskingPane(Window container) {
            OSXMaskingContentPane oSXMaskingContentPane;
            if (container instanceof RootPaneContainer) {
                RootPaneContainer rootPaneContainer = (RootPaneContainer)((Object)container);
                if ((container = rootPaneContainer.getContentPane()) instanceof OSXMaskingContentPane) {
                    oSXMaskingContentPane = (OSXMaskingContentPane)container;
                } else {
                    oSXMaskingContentPane = new OSXMaskingContentPane(container);
                    rootPaneContainer.setContentPane(oSXMaskingContentPane);
                }
            } else {
                Component component = container.getComponentCount() > 0 ? container.getComponent(0) : null;
                if (component instanceof OSXMaskingContentPane) {
                    oSXMaskingContentPane = (OSXMaskingContentPane)component;
                } else {
                    oSXMaskingContentPane = new OSXMaskingContentPane(component);
                    container.add(oSXMaskingContentPane);
                }
            }
            return oSXMaskingContentPane;
        }

        @Override
        public void setWindowTransparent(Window window, boolean bl) {
            boolean bl2;
            boolean bl3 = bl2 = window.getBackground() != null && window.getBackground().getAlpha() == 0;
            if (bl != bl2) {
                this.setBackgroundTransparent(window, bl, "setWindowTransparent");
            }
        }

        private void fixWindowDragging(Window window, String string) {
            JRootPane jRootPane;
            Boolean bl;
            if (window instanceof RootPaneContainer && (bl = (Boolean)(jRootPane = ((RootPaneContainer)((Object)window)).getRootPane()).getClientProperty(WDRAG)) == null) {
                jRootPane.putClientProperty(WDRAG, Boolean.FALSE);
                if (window.isDisplayable()) {
                    LOG.log(Level.WARNING, "{0}(): To avoid content dragging, {1}() must be called before the window is realized, or apple.awt.draggableWindowBackground must be set to Boolean.FALSE before the window is realized.  If you really want content dragging, set apple.awt.draggableWindowBackground on the window''s root pane to Boolean.TRUE before calling {2}() to hide this message.", new Object[]{string, string, string});
                }
            }
        }

        @Override
        public void setWindowAlpha(final Window window, final float f2) {
            if (window instanceof RootPaneContainer) {
                JRootPane jRootPane = ((RootPaneContainer)((Object)window)).getRootPane();
                jRootPane.putClientProperty("Window.alpha", Float.valueOf(f2));
                this.fixWindowDragging(window, "setWindowAlpha");
            }
            this.whenDisplayable(window, new Runnable(){

                @Override
                public void run() {
                    try {
                        Object object = window.getClass().getMethod("getPeer", new Class[0]);
                        object = ((Method)object).invoke(window, new Object[0]);
                        Method method = object.getClass().getMethod("setAlpha", Float.TYPE);
                        method.invoke(object, Float.valueOf(f2));
                        return;
                    }
                    catch (Exception exception) {
                        return;
                    }
                }
            });
        }

        @Override
        protected void setWindowMask(Component component, Raster raster) {
            if (raster != null) {
                ((NativeWindowUtils)this).setWindowMask(component, this.toShape(raster));
                return;
            }
            ((NativeWindowUtils)this).setWindowMask(component, new Rectangle(0, 0, component.getWidth(), component.getHeight()));
        }

        @Override
        public void setWindowMask(Component component, Shape shape) {
            if (component instanceof Window) {
                component = (Window)component;
                OSXMaskingContentPane oSXMaskingContentPane = this.installMaskingPane((Window)component);
                oSXMaskingContentPane.setMask(shape);
                this.setBackgroundTransparent((Window)component, shape != MASK_NONE, "setWindowMask");
            }
        }

        private void setBackgroundTransparent(Window window, boolean bl, String string) {
            JRootPane jRootPane;
            JRootPane jRootPane2 = jRootPane = window instanceof RootPaneContainer ? ((RootPaneContainer)((Object)window)).getRootPane() : null;
            if (bl) {
                if (jRootPane != null) {
                    jRootPane.putClientProperty(WindowUtils.TRANSPARENT_OLD_BG, window.getBackground());
                }
                window.setBackground(new Color(0, 0, 0, 0));
            } else if (jRootPane != null) {
                Color color = (Color)jRootPane.getClientProperty(WindowUtils.TRANSPARENT_OLD_BG);
                if (color != null) {
                    color = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
                }
                window.setBackground(color);
                jRootPane.putClientProperty(WindowUtils.TRANSPARENT_OLD_BG, null);
            } else {
                window.setBackground(null);
            }
            this.fixWindowDragging(window, string);
        }

        static class OSXMaskingContentPane
        extends JPanel {
            private static final long serialVersionUID = 1L;
            private Shape shape;

            public OSXMaskingContentPane(Component component) {
                super(new BorderLayout());
                if (component != null) {
                    this.add(component, "Center");
                }
            }

            public void setMask(Shape shape) {
                this.shape = shape;
                this.repaint();
            }

            @Override
            public void paint(Graphics graphics) {
                Graphics2D graphics2D = (Graphics2D)graphics.create();
                graphics2D.setComposite(AlphaComposite.Clear);
                graphics2D.fillRect(0, 0, this.getWidth(), this.getHeight());
                graphics2D.dispose();
                if (this.shape != null) {
                    graphics2D = (Graphics2D)graphics.create();
                    graphics2D.setClip(this.shape);
                    super.paint(graphics2D);
                    graphics2D.dispose();
                    return;
                }
                super.paint(graphics);
            }
        }
    }

    static class W32WindowUtils
    extends NativeWindowUtils {
        private W32WindowUtils() {
        }

        private WinDef.HWND getHWnd(Component component) {
            WinDef.HWND hWND = new WinDef.HWND();
            ((PointerType)hWND).setPointer(Native.getComponentPointer(component));
            return hWND;
        }

        @Override
        public boolean isWindowAlphaSupported() {
            return Boolean.getBoolean("sun.java2d.noddraw");
        }

        private boolean usingUpdateLayeredWindow(Window container) {
            if (container instanceof RootPaneContainer) {
                return ((JComponent)(container = ((RootPaneContainer)((Object)container)).getRootPane())).getClientProperty(WindowUtils.TRANSPARENT_OLD_BG) != null;
            }
            return false;
        }

        private void storeAlpha(Window container, byte by) {
            if (container instanceof RootPaneContainer) {
                container = ((RootPaneContainer)((Object)container)).getRootPane();
                Byte by2 = by == -1 ? null : Byte.valueOf(by);
                ((JComponent)container).putClientProperty(WindowUtils.TRANSPARENT_ALPHA, by2);
            }
        }

        private byte getAlpha(Window serializable) {
            if (serializable instanceof RootPaneContainer) {
                serializable = ((RootPaneContainer)((Object)serializable)).getRootPane();
                if ((serializable = (Byte)((JComponent)serializable).getClientProperty(WindowUtils.TRANSPARENT_ALPHA)) != null) {
                    return (Byte)serializable;
                }
            }
            return -1;
        }

        @Override
        public void setWindowAlpha(final Window window, final float f2) {
            if (!((NativeWindowUtils)this).isWindowAlphaSupported()) {
                throw new UnsupportedOperationException("Set sun.java2d.noddraw=true to enable transparent windows");
            }
            this.whenDisplayable(window, new Runnable(){

                @Override
                public void run() {
                    WinDef.HWND hWND = this.getHWnd(window);
                    User32 user32 = User32.INSTANCE;
                    int n2 = user32.GetWindowLong(hWND, -20);
                    byte by = (byte)(255.0f * f2);
                    if (this.usingUpdateLayeredWindow(window)) {
                        WinUser.BLENDFUNCTION bLENDFUNCTION = new WinUser.BLENDFUNCTION();
                        new WinUser.BLENDFUNCTION().SourceConstantAlpha = by;
                        bLENDFUNCTION.AlphaFormat = 1;
                        user32.UpdateLayeredWindow(hWND, null, null, null, null, null, 0, bLENDFUNCTION, 2);
                    } else if (f2 == 1.0f) {
                        user32.SetWindowLong(hWND, -20, n2 &= 0xFFF7FFFF);
                    } else {
                        user32.SetWindowLong(hWND, -20, n2 |= 0x80000);
                        user32.SetLayeredWindowAttributes(hWND, 0, by, 2);
                    }
                    this.setForceHeavyweightPopups(window, f2 != 1.0f);
                    this.storeAlpha(window, by);
                }
            });
        }

        @Override
        public void setWindowTransparent(final Window window, final boolean bl) {
            boolean bl2;
            if (!(window instanceof RootPaneContainer)) {
                throw new IllegalArgumentException("Window must be a RootPaneContainer");
            }
            if (!((NativeWindowUtils)this).isWindowAlphaSupported()) {
                throw new UnsupportedOperationException("Set sun.java2d.noddraw=true to enable transparent windows");
            }
            boolean bl3 = bl2 = window.getBackground() != null && window.getBackground().getAlpha() == 0;
            if (bl == bl2) {
                return;
            }
            this.whenDisplayable(window, new Runnable(){

                @Override
                public void run() {
                    User32 user32 = User32.INSTANCE;
                    WinDef.HWND hWND = this.getHWnd(window);
                    int n2 = user32.GetWindowLong(hWND, -20);
                    JRootPane jRootPane = ((RootPaneContainer)((Object)window)).getRootPane();
                    JLayeredPane jLayeredPane = jRootPane.getLayeredPane();
                    Container container = jRootPane.getContentPane();
                    if (container instanceof W32TransparentContentPane) {
                        ((NativeWindowUtils.TransparentContentPane)((W32TransparentContentPane)container)).setTransparent(bl);
                    } else if (bl) {
                        container = new W32TransparentContentPane(container);
                        jRootPane.setContentPane(container);
                        jLayeredPane.add((Component)new RepaintTrigger((JComponent)container), JLayeredPane.DRAG_LAYER);
                    }
                    if (bl && !this.usingUpdateLayeredWindow(window)) {
                        user32.SetWindowLong(hWND, -20, n2 |= 0x80000);
                    } else if (!bl && this.usingUpdateLayeredWindow(window)) {
                        user32.SetWindowLong(hWND, -20, n2 &= 0xFFF7FFFF);
                    }
                    this.setLayersTransparent(window, bl);
                    this.setForceHeavyweightPopups(window, bl);
                    this.setDoubleBuffered(window, !bl);
                }
            });
        }

        @Override
        public void setWindowMask(Component component, Shape shape) {
            if (shape instanceof Area && ((Area)shape).isPolygonal()) {
                this.setMask(component, (Area)shape);
                return;
            }
            super.setWindowMask(component, shape);
        }

        private void setWindowRegion(final Component component, final WinDef.HRGN hRGN) {
            this.whenDisplayable(component, new Runnable(){

                @Override
                public void run() {
                    GDI32 gDI32 = GDI32.INSTANCE;
                    User32 user32 = User32.INSTANCE;
                    WinDef.HWND hWND = this.getHWnd(component);
                    try {
                        user32.SetWindowRgn(hWND, hRGN, true);
                        this.setForceHeavyweightPopups(this.getWindow(component), hRGN != null);
                        return;
                    }
                    finally {
                        gDI32.DeleteObject(hRGN);
                    }
                }
            });
        }

        private void setMask(Component component, Area pOINTArray) {
            GDI32 gDI32 = GDI32.INSTANCE;
            PathIterator pathIterator = pOINTArray.getPathIterator(null);
            int n2 = pathIterator.getWindingRule() == 1 ? 2 : 1;
            float[] fArray = new float[6];
            ArrayList<WinDef.POINT> arrayList = new ArrayList<WinDef.POINT>();
            int n3 = 0;
            ArrayList<Integer> arrayList2 = new ArrayList<Integer>();
            while (!pathIterator.isDone()) {
                int n4 = pathIterator.currentSegment(fArray);
                if (n4 == 0) {
                    n3 = 1;
                    arrayList.add(new WinDef.POINT((int)fArray[0], (int)fArray[1]));
                } else if (n4 == 1) {
                    ++n3;
                    arrayList.add(new WinDef.POINT((int)fArray[0], (int)fArray[1]));
                } else if (n4 == 4) {
                    arrayList2.add(n3);
                } else {
                    throw new RuntimeException("Area is not polygonal: " + pOINTArray);
                }
                pathIterator.next();
            }
            WinDef.POINT[] pOINTArray2 = (WinDef.POINT[])new WinDef.POINT().toArray(arrayList.size());
            ArrayList<WinDef.POINT> arrayList3 = arrayList;
            pOINTArray = arrayList3.toArray(new WinDef.POINT[arrayList3.size()]);
            for (int i2 = 0; i2 < pOINTArray2.length; ++i2) {
                pOINTArray2[i2].x = pOINTArray[i2].x;
                pOINTArray2[i2].y = pOINTArray[i2].y;
            }
            int[] nArray = new int[arrayList2.size()];
            for (int i3 = 0; i3 < nArray.length; ++i3) {
                nArray[i3] = (Integer)arrayList2.get(i3);
            }
            WinDef.HRGN hRGN = gDI32.CreatePolyPolygonRgn(pOINTArray2, nArray, nArray.length, n2);
            this.setWindowRegion(component, hRGN);
        }

        @Override
        protected void setMask(Component component, Raster raster) {
            GDI32 gDI32 = GDI32.INSTANCE;
            final WinDef.HRGN hRGN = raster != null ? gDI32.CreateRectRgn(0, 0, 0, 0) : null;
            if (hRGN != null) {
                final WinDef.HRGN hRGN2 = gDI32.CreateRectRgn(0, 0, 0, 0);
                try {
                    RasterRangesUtils.outputOccupiedRanges(raster, new RasterRangesUtils.RangesOutput(){

                        @Override
                        public boolean outputRange(int n2, int n3, int n4, int n5) {
                            GDI32 gDI32 = GDI32.INSTANCE;
                            gDI32.SetRectRgn(hRGN2, n2, n3, n2 + n4, n3 + n5);
                            return gDI32.CombineRgn(hRGN, hRGN, hRGN2, 2) != 0;
                        }
                    });
                }
                finally {
                    gDI32.DeleteObject(hRGN2);
                }
            }
            this.setWindowRegion(component, hRGN);
        }

        @Override
        public BufferedImage getWindowIcon(WinDef.HWND object) {
            Object object2 = new WinDef.DWORDByReference();
            WinDef.LRESULT lRESULT = User32.INSTANCE.SendMessageTimeout((WinDef.HWND)object, 127, new WinDef.WPARAM(1L), new WinDef.LPARAM(0L), 2, 500, (WinDef.DWORDByReference)object2);
            if (lRESULT.intValue() == 0) {
                lRESULT = User32.INSTANCE.SendMessageTimeout((WinDef.HWND)object, 127, new WinDef.WPARAM(0L), new WinDef.LPARAM(0L), 2, 500, (WinDef.DWORDByReference)object2);
            }
            if (lRESULT.intValue() == 0) {
                lRESULT = User32.INSTANCE.SendMessageTimeout((WinDef.HWND)object, 127, new WinDef.WPARAM(2L), new WinDef.LPARAM(0L), 2, 500, (WinDef.DWORDByReference)object2);
            }
            if (lRESULT.intValue() == 0) {
                lRESULT = new WinDef.LRESULT((long)User32.INSTANCE.GetClassLongPtr((WinDef.HWND)object, -14).intValue());
                ((WinDef.DWORDByReference)object2).getValue().setValue(lRESULT.intValue());
            }
            if (lRESULT.intValue() == 0) {
                lRESULT = new WinDef.LRESULT((long)User32.INSTANCE.GetClassLongPtr((WinDef.HWND)object, -34).intValue());
                ((WinDef.DWORDByReference)object2).getValue().setValue(lRESULT.intValue());
            }
            if (lRESULT.intValue() == 0) {
                return null;
            }
            object = new WinDef.HICON(new Pointer(((WinDef.DWORDByReference)object2).getValue().longValue()));
            object2 = this.getIconSize((WinDef.HICON)object);
            if (((Dimension)object2).width == 0 || ((Dimension)object2).height == 0) {
                return null;
            }
            int n2 = ((Dimension)object2).width;
            int n3 = ((Dimension)object2).height;
            byte[] byArray = new byte[n2 * n3 * 24 / 8];
            Memory memory = new Memory(byArray.length);
            byte[] byArray2 = new byte[n2 * n3 * 24 / 8];
            Memory memory2 = new Memory(byArray2.length);
            WinGDI.BITMAPINFO bITMAPINFO = new WinGDI.BITMAPINFO();
            Object object3 = new WinGDI.BITMAPINFOHEADER();
            bITMAPINFO.bmiHeader = object3;
            ((WinGDI.BITMAPINFOHEADER)object3).biWidth = n2;
            ((WinGDI.BITMAPINFOHEADER)object3).biHeight = n3;
            ((WinGDI.BITMAPINFOHEADER)object3).biPlanes = 1;
            ((WinGDI.BITMAPINFOHEADER)object3).biBitCount = (short)24;
            ((WinGDI.BITMAPINFOHEADER)object3).biCompression = 0;
            ((Structure)object3).write();
            bITMAPINFO.write();
            object3 = User32.INSTANCE.GetDC(null);
            WinGDI.ICONINFO iCONINFO = new WinGDI.ICONINFO();
            User32.INSTANCE.GetIconInfo((WinDef.HICON)object, iCONINFO);
            iCONINFO.read();
            GDI32.INSTANCE.GetDIBits((WinDef.HDC)object3, iCONINFO.hbmColor, 0, n3, memory, bITMAPINFO, 0);
            ((Pointer)memory).read(0L, byArray, 0, byArray.length);
            GDI32.INSTANCE.GetDIBits((WinDef.HDC)object3, iCONINFO.hbmMask, 0, n3, memory2, bITMAPINFO, 0);
            ((Pointer)memory2).read(0L, byArray2, 0, byArray2.length);
            object = new BufferedImage(n2, n3, 2);
            int n4 = 0;
            int n5 = n3 - 1;
            for (int i2 = 0; i2 < byArray.length; i2 += 3) {
                int n6 = byArray[i2] & 0xFF;
                int n7 = byArray[i2 + 1] & 0xFF;
                n3 = byArray[i2 + 2] & 0xFF;
                int n8 = 255 - byArray2[i2] & 0xFF;
                n3 = n8 << 24 | n3 << 16 | n7 << 8 | n6;
                ((BufferedImage)object).setRGB(n4, n5, n3);
                n4 = (n4 + 1) % n2;
                if (n4 != 0) continue;
                --n5;
            }
            User32.INSTANCE.ReleaseDC(null, (WinDef.HDC)object3);
            return object;
        }

        @Override
        public Dimension getIconSize(WinDef.HICON object) {
            WinGDI.ICONINFO iCONINFO = new WinGDI.ICONINFO();
            try {
                if (!User32.INSTANCE.GetIconInfo((WinDef.HICON)object, iCONINFO)) {
                    object = new Dimension();
                    return object;
                }
                iCONINFO.read();
                object = new WinGDI.BITMAP();
                if (iCONINFO.hbmColor != null && iCONINFO.hbmColor.getPointer() != Pointer.NULL) {
                    int n2 = GDI32.INSTANCE.GetObject(iCONINFO.hbmColor, ((Structure)object).size(), ((Structure)object).getPointer());
                    ((Structure)object).read();
                    if (n2 > 0) {
                        object = new Dimension(((WinGDI.BITMAP)object).bmWidth.intValue(), ((WinGDI.BITMAP)object).bmHeight.intValue());
                        return object;
                    }
                } else if (iCONINFO.hbmMask != null && iCONINFO.hbmMask.getPointer() != Pointer.NULL) {
                    int n3 = GDI32.INSTANCE.GetObject(iCONINFO.hbmMask, ((Structure)object).size(), ((Structure)object).getPointer());
                    ((Structure)object).read();
                    if (n3 > 0) {
                        object = new Dimension(((WinGDI.BITMAP)object).bmWidth.intValue(), ((WinGDI.BITMAP)object).bmHeight.intValue() / 2);
                        return object;
                    }
                }
            }
            finally {
                if (iCONINFO.hbmColor != null && iCONINFO.hbmColor.getPointer() != Pointer.NULL) {
                    GDI32.INSTANCE.DeleteObject(iCONINFO.hbmColor);
                }
                if (iCONINFO.hbmMask != null && iCONINFO.hbmMask.getPointer() != Pointer.NULL) {
                    GDI32.INSTANCE.DeleteObject(iCONINFO.hbmMask);
                }
            }
            return new Dimension();
        }

        @Override
        public List<DesktopWindow> getAllWindows(final boolean bl) {
            final LinkedList<DesktopWindow> linkedList = new LinkedList<DesktopWindow>();
            WinUser.WNDENUMPROC wNDENUMPROC = new WinUser.WNDENUMPROC(){

                @Override
                public boolean callback(WinDef.HWND hWND, Pointer object) {
                    try {
                        boolean bl2 = !bl || User32.INSTANCE.IsWindowVisible(hWND);
                        if (bl2) {
                            String string = this.getWindowTitle(hWND);
                            String string2 = this.getProcessFilePath(hWND);
                            Rectangle rectangle = this.getWindowLocationAndSize(hWND);
                            linkedList.add(new DesktopWindow(hWND, string, string2, rectangle));
                        }
                    }
                    catch (Exception exception) {
                        object = exception;
                        exception.printStackTrace();
                    }
                    return true;
                }
            };
            if (!User32.INSTANCE.EnumWindows(wNDENUMPROC, null)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            return linkedList;
        }

        @Override
        public String getWindowTitle(WinDef.HWND hWND) {
            int n2 = User32.INSTANCE.GetWindowTextLength(hWND) + 1;
            char[] cArray = new char[n2];
            int n3 = User32.INSTANCE.GetWindowText(hWND, cArray, cArray.length);
            return Native.toString(Arrays.copyOfRange(cArray, 0, n3));
        }

        @Override
        public String getProcessFilePath(WinDef.HWND hANDLE) {
            Object object = new IntByReference();
            User32.INSTANCE.GetWindowThreadProcessId((WinDef.HWND)hANDLE, (IntByReference)object);
            hANDLE = Kernel32.INSTANCE.OpenProcess(1024, false, ((IntByReference)object).getValue());
            if (hANDLE == null) {
                if (Kernel32.INSTANCE.GetLastError() != 5) {
                    throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                }
                hANDLE = Kernel32.INSTANCE.OpenProcess(4096, false, ((IntByReference)object).getValue());
                if (hANDLE == null) {
                    if (Kernel32.INSTANCE.GetLastError() != 5) {
                        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                    }
                    return "";
                }
            }
            try {
                Object object2;
                object = PsapiUtil.GetProcessImageFileName(hANDLE);
                if (((String)object).startsWith("\\Device\\Mup\\")) {
                    String string = "\\" + ((String)object).substring(11);
                    return string;
                }
                char[] cArray = new char[50];
                WinNT.HANDLE hANDLE2 = Kernel32.INSTANCE.FindFirstVolume(cArray, 50);
                if (hANDLE2 == null || hANDLE2.equals(WinBase.INVALID_HANDLE_VALUE)) {
                    throw new Win32Exception(Native.getLastError());
                }
                try {
                    do {
                        object2 = Native.toString(cArray);
                        for (String string : Kernel32Util.getVolumePathNamesForVolumeName((String)object2)) {
                            if (!string.matches("[a-zA-Z]:\\\\")) continue;
                            for (String string2 : Kernel32Util.queryDosDevice(string.substring(0, 2), 1024)) {
                                if (!((String)object).startsWith(string2)) continue;
                                object = string + ((String)object).substring(string2.length() + 1);
                                return object;
                            }
                        }
                    } while (Kernel32.INSTANCE.FindNextVolume(hANDLE2, cArray, 50));
                    if (Native.getLastError() != 18) {
                        throw new Win32Exception(Native.getLastError());
                    }
                }
                finally {
                    Kernel32.INSTANCE.FindVolumeClose(hANDLE2);
                }
                object2 = object;
                return object2;
            }
            finally {
                Kernel32.INSTANCE.CloseHandle(hANDLE);
            }
        }

        @Override
        public Rectangle getWindowLocationAndSize(WinDef.HWND hWND) {
            WinDef.RECT rECT = new WinDef.RECT();
            if (!User32.INSTANCE.GetWindowRect(hWND, rECT)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            return new Rectangle(rECT.left, rECT.top, Math.abs(rECT.right - rECT.left), Math.abs(rECT.bottom - rECT.top));
        }

        class W32TransparentContentPane
        extends NativeWindowUtils.TransparentContentPane {
            private static final long serialVersionUID = 1L;
            private WinDef.HDC memDC;
            private WinDef.HBITMAP hBitmap;
            private Pointer pbits;
            private Dimension bitmapSize;

            public W32TransparentContentPane(Container container) {
                super(container);
            }

            private void disposeBackingStore() {
                GDI32 gDI32 = GDI32.INSTANCE;
                if (this.hBitmap != null) {
                    gDI32.DeleteObject(this.hBitmap);
                    this.hBitmap = null;
                }
                if (this.memDC != null) {
                    gDI32.DeleteDC(this.memDC);
                    this.memDC = null;
                }
            }

            @Override
            public void removeNotify() {
                super.removeNotify();
                this.disposeBackingStore();
            }

            @Override
            public void setTransparent(boolean bl) {
                super.setTransparent(bl);
                if (!bl) {
                    this.disposeBackingStore();
                }
            }

            @Override
            protected void paintDirect(BufferedImage object, Rectangle rectangle) {
                Window window = SwingUtilities.getWindowAncestor(this);
                GDI32 gDI32 = GDI32.INSTANCE;
                User32 user32 = User32.INSTANCE;
                int n2 = rectangle.x;
                int n3 = rectangle.y;
                Point point = SwingUtilities.convertPoint(this, n2, n3, window);
                n3 = rectangle.width;
                int n4 = rectangle.height;
                int n5 = window.getWidth();
                int n6 = window.getHeight();
                WinDef.HDC hDC = user32.GetDC(null);
                WinNT.HANDLE hANDLE = null;
                try {
                    Object object2;
                    Object object3;
                    if (this.memDC == null) {
                        this.memDC = gDI32.CreateCompatibleDC(hDC);
                    }
                    if (this.hBitmap == null || !window.getSize().equals(this.bitmapSize)) {
                        if (this.hBitmap != null) {
                            gDI32.DeleteObject(this.hBitmap);
                            this.hBitmap = null;
                        }
                        object3 = new WinGDI.BITMAPINFO();
                        new WinGDI.BITMAPINFO().bmiHeader.biWidth = n5;
                        ((WinGDI.BITMAPINFO)object3).bmiHeader.biHeight = n6;
                        ((WinGDI.BITMAPINFO)object3).bmiHeader.biPlanes = 1;
                        ((WinGDI.BITMAPINFO)object3).bmiHeader.biBitCount = (short)32;
                        ((WinGDI.BITMAPINFO)object3).bmiHeader.biCompression = 0;
                        ((WinGDI.BITMAPINFO)object3).bmiHeader.biSizeImage = n5 * n6 << 2;
                        object2 = new PointerByReference();
                        this.hBitmap = gDI32.CreateDIBSection(this.memDC, (WinGDI.BITMAPINFO)object3, 0, (PointerByReference)object2, null, 0);
                        this.pbits = ((PointerByReference)object2).getValue();
                        this.bitmapSize = new Dimension(n5, n6);
                    }
                    hANDLE = gDI32.SelectObject(this.memDC, this.hBitmap);
                    object3 = ((BufferedImage)object).getData();
                    object2 = new int[4];
                    object = new int[n3];
                    for (int i2 = 0; i2 < n4; ++i2) {
                        int n7;
                        for (n7 = 0; n7 < n3; ++n7) {
                            ((Raster)object3).getPixel(n7, i2, (int[])object2);
                            int n8 = (object2[3] & 0xFF) << 24;
                            int n9 = object2[2] & 0xFF;
                            int n10 = (object2[1] & 0xFF) << 8;
                            int n11 = (object2[0] & 0xFF) << 16;
                            object[n7] = n8 | n9 | n10 | n11;
                        }
                        n7 = n6 - (point.y + i2) - 1;
                        this.pbits.write((long)(n7 * n5 + point.x << 2), (int[])object, 0, ((Object)object).length);
                    }
                    WinUser.SIZE sIZE = new WinUser.SIZE();
                    new WinUser.SIZE().cx = window.getWidth();
                    sIZE.cy = window.getHeight();
                    WinDef.POINT pOINT = new WinDef.POINT();
                    new WinDef.POINT().x = window.getX();
                    pOINT.y = window.getY();
                    WinDef.POINT pOINT2 = new WinDef.POINT();
                    WinUser.BLENDFUNCTION bLENDFUNCTION = new WinUser.BLENDFUNCTION();
                    WinDef.HWND hWND = W32WindowUtils.this.getHWnd(window);
                    ByteByReference byteByReference = new ByteByReference();
                    object = new IntByReference();
                    n4 = W32WindowUtils.this.getAlpha(window);
                    try {
                        if (user32.GetLayeredWindowAttributes(hWND, null, byteByReference, (IntByReference)object) && (((IntByReference)object).getValue() & 2) != 0) {
                            n4 = byteByReference.getValue();
                        }
                    }
                    catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
                    bLENDFUNCTION.SourceConstantAlpha = (byte)n4;
                    bLENDFUNCTION.AlphaFormat = 1;
                    user32.UpdateLayeredWindow(hWND, hDC, pOINT, sIZE, this.memDC, pOINT2, 0, bLENDFUNCTION, 2);
                    user32.ReleaseDC(null, hDC);
                }
                catch (Throwable throwable) {
                    user32.ReleaseDC(null, hDC);
                    if (this.memDC != null && hANDLE != null) {
                        gDI32.SelectObject(this.memDC, hANDLE);
                    }
                    throw throwable;
                }
                if (this.memDC != null && hANDLE != null) {
                    gDI32.SelectObject(this.memDC, hANDLE);
                    return;
                }
            }
        }
    }

    static class Holder {
        public static boolean requiresVisible;
        public static final NativeWindowUtils INSTANCE;

        private Holder() {
        }

        static {
            if (Platform.isWindows()) {
                INSTANCE = new W32WindowUtils();
                return;
            }
            if (Platform.isMac()) {
                INSTANCE = new MacWindowUtils();
                return;
            }
            if (Platform.isX11()) {
                INSTANCE = new X11WindowUtils();
                requiresVisible = System.getProperty("java.version").matches("^1\\.4\\..*");
                return;
            }
            String string = System.getProperty("os.name");
            throw new UnsupportedOperationException("No support for " + string);
        }
    }

    public static abstract class NativeWindowUtils {
        protected Window getWindow(Component component) {
            if (component instanceof Window) {
                return (Window)component;
            }
            return SwingUtilities.getWindowAncestor(component);
        }

        protected void whenDisplayable(Component component, final Runnable runnable2) {
            if (component.isDisplayable() && (!Holder.requiresVisible || component.isVisible())) {
                runnable2.run();
                return;
            }
            if (Holder.requiresVisible) {
                this.getWindow(component).addWindowListener(new WindowAdapter(){

                    @Override
                    public void windowOpened(WindowEvent windowEvent) {
                        windowEvent.getWindow().removeWindowListener(this);
                        runnable2.run();
                    }

                    @Override
                    public void windowClosed(WindowEvent windowEvent) {
                        windowEvent.getWindow().removeWindowListener(this);
                    }
                });
                return;
            }
            component.addHierarchyListener(new HierarchyListener(){

                @Override
                public void hierarchyChanged(HierarchyEvent hierarchyEvent) {
                    if ((hierarchyEvent.getChangeFlags() & 2L) != 0L && hierarchyEvent.getComponent().isDisplayable()) {
                        hierarchyEvent.getComponent().removeHierarchyListener(this);
                        runnable2.run();
                    }
                }
            });
        }

        protected Raster toRaster(Shape shape) {
            Object object = null;
            if (shape != MASK_NONE) {
                Rectangle rectangle = shape.getBounds();
                if (rectangle.width > 0 && rectangle.height > 0) {
                    object = new BufferedImage(rectangle.x + rectangle.width, rectangle.y + rectangle.height, 12);
                    Graphics2D graphics2D = ((BufferedImage)object).createGraphics();
                    graphics2D.setColor(Color.black);
                    graphics2D.fillRect(0, 0, rectangle.x + rectangle.width, rectangle.y + rectangle.height);
                    graphics2D.setColor(Color.white);
                    graphics2D.fill(shape);
                    object = ((BufferedImage)object).getRaster();
                }
            }
            return object;
        }

        protected Raster toRaster(Component component, Icon icon) {
            Object object = null;
            if (icon != null) {
                object = new Rectangle(0, 0, icon.getIconWidth(), icon.getIconHeight());
                BufferedImage bufferedImage = new BufferedImage(((Rectangle)object).width, ((Rectangle)object).height, 2);
                Graphics2D graphics2D = bufferedImage.createGraphics();
                graphics2D.setComposite(AlphaComposite.Clear);
                graphics2D.fillRect(0, 0, ((Rectangle)object).width, ((Rectangle)object).height);
                graphics2D.setComposite(AlphaComposite.SrcOver);
                icon.paintIcon(component, graphics2D, 0, 0);
                object = bufferedImage.getAlphaRaster();
            }
            return object;
        }

        protected Shape toShape(Raster raster) {
            final Area area = new Area(new Rectangle(0, 0, 0, 0));
            RasterRangesUtils.outputOccupiedRanges(raster, new RasterRangesUtils.RangesOutput(){

                @Override
                public boolean outputRange(int n2, int n3, int n4, int n5) {
                    area.add(new Area(new Rectangle(n2, n3, n4, n5)));
                    return true;
                }
            });
            return area;
        }

        public void setWindowAlpha(Window window, float f2) {
        }

        public boolean isWindowAlphaSupported() {
            return false;
        }

        public GraphicsConfiguration getAlphaCompatibleGraphicsConfiguration() {
            Object object = GraphicsEnvironment.getLocalGraphicsEnvironment();
            object = ((GraphicsEnvironment)object).getDefaultScreenDevice();
            return ((GraphicsDevice)object).getDefaultConfiguration();
        }

        public void setWindowTransparent(Window window, boolean bl) {
        }

        protected void setDoubleBuffered(Component componentArray, boolean bl) {
            if (componentArray instanceof JComponent) {
                ((JComponent)componentArray).setDoubleBuffered(bl);
            }
            if (componentArray instanceof JRootPane && bl) {
                ((JRootPane)componentArray).setDoubleBuffered(true);
                return;
            }
            if (componentArray instanceof Container) {
                componentArray = ((Container)componentArray).getComponents();
                for (int i2 = 0; i2 < componentArray.length; ++i2) {
                    this.setDoubleBuffered(componentArray[i2], bl);
                }
            }
        }

        protected void setLayersTransparent(Window window, boolean bl) {
            Color color;
            Color color2 = color = bl ? new Color(0, 0, 0, 0) : null;
            if (window instanceof RootPaneContainer) {
                Object object = (RootPaneContainer)((Object)window);
                object = object.getRootPane();
                JLayeredPane jLayeredPane = ((JRootPane)object).getLayeredPane();
                Container container = ((JRootPane)object).getContentPane();
                Container container2 = container = container instanceof JComponent ? (JComponent)container : null;
                if (bl) {
                    jLayeredPane.putClientProperty(WindowUtils.TRANSPARENT_OLD_OPAQUE, jLayeredPane.isOpaque());
                    jLayeredPane.setOpaque(false);
                    ((JComponent)object).putClientProperty(WindowUtils.TRANSPARENT_OLD_OPAQUE, ((JComponent)object).isOpaque());
                    ((JComponent)object).setOpaque(false);
                    if (container != null) {
                        ((JComponent)container).putClientProperty(WindowUtils.TRANSPARENT_OLD_OPAQUE, ((JComponent)container).isOpaque());
                        ((JComponent)container).setOpaque(false);
                    }
                    ((JComponent)object).putClientProperty(WindowUtils.TRANSPARENT_OLD_BG, ((Component)object).getParent().getBackground());
                } else {
                    jLayeredPane.setOpaque(Boolean.TRUE.equals(jLayeredPane.getClientProperty(WindowUtils.TRANSPARENT_OLD_OPAQUE)));
                    jLayeredPane.putClientProperty(WindowUtils.TRANSPARENT_OLD_OPAQUE, null);
                    ((JComponent)object).setOpaque(Boolean.TRUE.equals(((JComponent)object).getClientProperty(WindowUtils.TRANSPARENT_OLD_OPAQUE)));
                    ((JComponent)object).putClientProperty(WindowUtils.TRANSPARENT_OLD_OPAQUE, null);
                    if (container != null) {
                        ((JComponent)container).setOpaque(Boolean.TRUE.equals(((JComponent)container).getClientProperty(WindowUtils.TRANSPARENT_OLD_OPAQUE)));
                        ((JComponent)container).putClientProperty(WindowUtils.TRANSPARENT_OLD_OPAQUE, null);
                    }
                    color = (Color)((JComponent)object).getClientProperty(WindowUtils.TRANSPARENT_OLD_BG);
                    ((JComponent)object).putClientProperty(WindowUtils.TRANSPARENT_OLD_BG, null);
                }
            }
            window.setBackground(color);
        }

        protected void setMask(Component component, Raster raster) {
            throw new UnsupportedOperationException("Window masking is not available");
        }

        protected void setWindowMask(Component component, Raster raster) {
            if (component.isLightweight()) {
                throw new IllegalArgumentException("Component must be heavyweight: " + component);
            }
            this.setMask(component, raster);
        }

        public void setWindowMask(Component component, Shape shape) {
            this.setWindowMask(component, this.toRaster(shape));
        }

        public void setWindowMask(Component component, Icon icon) {
            this.setWindowMask(component, this.toRaster(component, icon));
        }

        protected void setForceHeavyweightPopups(Window window, boolean bl) {
            if (!(window instanceof HeavyweightForcer)) {
                Window[] windowArray = window.getOwnedWindows();
                for (int i2 = 0; i2 < windowArray.length; ++i2) {
                    if (!(windowArray[i2] instanceof HeavyweightForcer)) continue;
                    if (bl) {
                        return;
                    }
                    windowArray[i2].dispose();
                }
                Boolean bl2 = Boolean.valueOf(System.getProperty("jna.force_hw_popups", "true"));
                if (bl && bl2.booleanValue()) {
                    new HeavyweightForcer(window);
                }
            }
        }

        protected BufferedImage getWindowIcon(WinDef.HWND hWND) {
            throw new UnsupportedOperationException("This platform is not supported, yet.");
        }

        protected Dimension getIconSize(WinDef.HICON hICON) {
            throw new UnsupportedOperationException("This platform is not supported, yet.");
        }

        protected List<DesktopWindow> getAllWindows(boolean bl) {
            throw new UnsupportedOperationException("This platform is not supported, yet.");
        }

        protected String getWindowTitle(WinDef.HWND hWND) {
            throw new UnsupportedOperationException("This platform is not supported, yet.");
        }

        protected String getProcessFilePath(WinDef.HWND hWND) {
            throw new UnsupportedOperationException("This platform is not supported, yet.");
        }

        protected Rectangle getWindowLocationAndSize(WinDef.HWND hWND) {
            throw new UnsupportedOperationException("This platform is not supported, yet.");
        }

        protected abstract class TransparentContentPane
        extends JPanel
        implements AWTEventListener {
            private static final long serialVersionUID = 1L;
            private boolean transparent;

            public TransparentContentPane(Container container) {
                super(new BorderLayout());
                this.add((Component)container, "Center");
                this.setTransparent(true);
                if (container instanceof JPanel) {
                    ((JComponent)container).setOpaque(false);
                }
            }

            @Override
            public void addNotify() {
                super.addNotify();
                Toolkit.getDefaultToolkit().addAWTEventListener(this, 2L);
            }

            @Override
            public void removeNotify() {
                Toolkit.getDefaultToolkit().removeAWTEventListener(this);
                super.removeNotify();
            }

            public void setTransparent(boolean bl) {
                this.transparent = bl;
                this.setOpaque(!bl);
                this.setDoubleBuffered(!bl);
                this.repaint();
            }

            @Override
            public void eventDispatched(AWTEvent serializable) {
                if (serializable.getID() == 300 && SwingUtilities.isDescendingFrom(((ContainerEvent)serializable).getChild(), this)) {
                    serializable = ((ContainerEvent)serializable).getChild();
                    NativeWindowUtils.this.setDoubleBuffered((Component)serializable, false);
                }
            }

            @Override
            public void paint(Graphics object) {
                if (this.transparent) {
                    object = ((Graphics)object).getClipBounds();
                    int n2 = ((Rectangle)object).width;
                    int n3 = ((Rectangle)object).height;
                    if (this.getWidth() > 0 && this.getHeight() > 0) {
                        BufferedImage bufferedImage = new BufferedImage(n2, n3, 3);
                        Graphics2D graphics2D = bufferedImage.createGraphics();
                        graphics2D.setComposite(AlphaComposite.Clear);
                        graphics2D.fillRect(0, 0, n2, n3);
                        graphics2D.dispose();
                        graphics2D = bufferedImage.createGraphics();
                        graphics2D.translate(-((Rectangle)object).x, -((Rectangle)object).y);
                        super.paint(graphics2D);
                        graphics2D.dispose();
                        this.paintDirect(bufferedImage, (Rectangle)object);
                    }
                    return;
                }
                super.paint((Graphics)object);
            }

            protected abstract void paintDirect(BufferedImage var1, Rectangle var2);
        }
    }

    protected static class RepaintTrigger
    extends JComponent {
        private static final long serialVersionUID = 1L;
        private final Listener listener = this.createListener();
        private final JComponent content;
        private Rectangle dirty;

        public RepaintTrigger(JComponent jComponent) {
            this.content = jComponent;
        }

        @Override
        public void addNotify() {
            super.addNotify();
            Window window = SwingUtilities.getWindowAncestor(this);
            RepaintTrigger repaintTrigger = this;
            repaintTrigger.setSize(repaintTrigger.getParent().getSize());
            window.addComponentListener(this.listener);
            window.addWindowListener(this.listener);
            Toolkit.getDefaultToolkit().addAWTEventListener(this.listener, 48L);
        }

        @Override
        public void removeNotify() {
            Toolkit.getDefaultToolkit().removeAWTEventListener(this.listener);
            Window window = SwingUtilities.getWindowAncestor(this);
            window.removeComponentListener(this.listener);
            window.removeWindowListener(this.listener);
            super.removeNotify();
        }

        @Override
        protected void paintComponent(Graphics object) {
            object = ((Graphics)object).getClipBounds();
            if (this.dirty == null || !this.dirty.contains((Rectangle)object)) {
                this.dirty = this.dirty == null ? object : this.dirty.union((Rectangle)object);
                this.content.repaint(this.dirty);
                return;
            }
            this.dirty = null;
        }

        protected Listener createListener() {
            return new Listener();
        }

        protected class Listener
        extends WindowAdapter
        implements AWTEventListener,
        ComponentListener,
        HierarchyListener {
            protected Listener() {
            }

            @Override
            public void windowOpened(WindowEvent windowEvent) {
                RepaintTrigger.this.repaint();
            }

            @Override
            public void componentHidden(ComponentEvent componentEvent) {
            }

            @Override
            public void componentMoved(ComponentEvent componentEvent) {
            }

            @Override
            public void componentResized(ComponentEvent componentEvent) {
                RepaintTrigger.this.setSize(RepaintTrigger.this.getParent().getSize());
                RepaintTrigger.this.repaint();
            }

            @Override
            public void componentShown(ComponentEvent componentEvent) {
                RepaintTrigger.this.repaint();
            }

            @Override
            public void hierarchyChanged(HierarchyEvent hierarchyEvent) {
                RepaintTrigger.this.repaint();
            }

            @Override
            public void eventDispatched(AWTEvent serializable) {
                Component component;
                if (serializable instanceof MouseEvent && (component = ((MouseEvent)serializable).getComponent()) != null && SwingUtilities.isDescendingFrom(component, RepaintTrigger.this.content)) {
                    serializable = SwingUtilities.convertMouseEvent(component, (MouseEvent)serializable, RepaintTrigger.this.content);
                    serializable = SwingUtilities.getDeepestComponentAt(RepaintTrigger.this.content, ((MouseEvent)serializable).getX(), ((MouseEvent)serializable).getY());
                    if (serializable != null) {
                        RepaintTrigger.this.setCursor(((Component)serializable).getCursor());
                    }
                }
            }
        }
    }

    static class HeavyweightForcer
    extends Window {
        private static final long serialVersionUID = 1L;
        private final boolean packed;

        public HeavyweightForcer(Window window) {
            super(window);
            this.pack();
            this.packed = true;
        }

        @Override
        public boolean isVisible() {
            return this.packed;
        }

        @Override
        public Rectangle getBounds() {
            return this.getOwner().getBounds();
        }
    }
}

