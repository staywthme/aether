/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.Callback;
import com.sun.jna.CallbackReference;
import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.FromNativeContext;
import com.sun.jna.FromNativeConverter;
import com.sun.jna.Function;
import com.sun.jna.IntegerType;
import com.sun.jna.JNIEnv;
import com.sun.jna.LastErrorException;
import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.MethodResultContext;
import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeMapped;
import com.sun.jna.NativeMappedConverter;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;
import com.sun.jna.ToNativeContext;
import com.sun.jna.ToNativeConverter;
import com.sun.jna.TypeMapper;
import com.sun.jna.Version;
import com.sun.jna.WString;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Native
implements Version {
    private static final Logger LOG = Logger.getLogger(Native.class.getName());
    public static final Charset DEFAULT_CHARSET;
    public static final String DEFAULT_ENCODING;
    public static final boolean DEBUG_LOAD;
    public static final boolean DEBUG_JNA_LOAD;
    private static final Level DEBUG_JNA_LOAD_LEVEL;
    static String jnidispatchPath;
    private static final Map<Class<?>, Map<String, Object>> typeOptions;
    private static final Map<Class<?>, Reference<?>> libraries;
    private static final String _OPTION_ENCLOSING_LIBRARY = "enclosing-library";
    private static final Callback.UncaughtExceptionHandler DEFAULT_HANDLER;
    private static Callback.UncaughtExceptionHandler callbackExceptionHandler;
    public static final int POINTER_SIZE;
    public static final int LONG_SIZE;
    public static final int WCHAR_SIZE;
    public static final int SIZE_T_SIZE;
    public static final int BOOL_SIZE;
    public static final int LONG_DOUBLE_SIZE;
    private static final int TYPE_VOIDP = 0;
    private static final int TYPE_LONG = 1;
    private static final int TYPE_WCHAR_T = 2;
    private static final int TYPE_SIZE_T = 3;
    private static final int TYPE_BOOL = 4;
    private static final int TYPE_LONG_DOUBLE = 5;
    static final int MAX_ALIGNMENT;
    static final int MAX_PADDING;
    private static final Object finalizer;
    static final String JNA_TMPLIB_PREFIX = "jna";
    private static final Map<Class<?>, long[]> registeredClasses;
    private static final Map<Class<?>, NativeLibrary> registeredLibraries;
    static final int CB_HAS_INITIALIZER = 1;
    private static final int CVT_UNSUPPORTED = -1;
    private static final int CVT_DEFAULT = 0;
    private static final int CVT_POINTER = 1;
    private static final int CVT_STRING = 2;
    private static final int CVT_STRUCTURE = 3;
    private static final int CVT_STRUCTURE_BYVAL = 4;
    private static final int CVT_BUFFER = 5;
    private static final int CVT_ARRAY_BYTE = 6;
    private static final int CVT_ARRAY_SHORT = 7;
    private static final int CVT_ARRAY_CHAR = 8;
    private static final int CVT_ARRAY_INT = 9;
    private static final int CVT_ARRAY_LONG = 10;
    private static final int CVT_ARRAY_FLOAT = 11;
    private static final int CVT_ARRAY_DOUBLE = 12;
    private static final int CVT_ARRAY_BOOLEAN = 13;
    private static final int CVT_BOOLEAN = 14;
    private static final int CVT_CALLBACK = 15;
    private static final int CVT_FLOAT = 16;
    private static final int CVT_NATIVE_MAPPED = 17;
    private static final int CVT_NATIVE_MAPPED_STRING = 18;
    private static final int CVT_NATIVE_MAPPED_WSTRING = 19;
    private static final int CVT_WSTRING = 20;
    private static final int CVT_INTEGER_TYPE = 21;
    private static final int CVT_POINTER_TYPE = 22;
    private static final int CVT_TYPE_MAPPER = 23;
    private static final int CVT_TYPE_MAPPER_STRING = 24;
    private static final int CVT_TYPE_MAPPER_WSTRING = 25;
    private static final int CVT_OBJECT = 26;
    private static final int CVT_JNIENV = 27;
    private static final int CVT_SHORT = 28;
    private static final int CVT_BYTE = 29;
    static final int CB_OPTION_DIRECT = 1;
    static final int CB_OPTION_IN_DLL = 2;
    private static final ThreadLocal<Memory> nativeThreadTerminationFlag;
    private static final Map<Thread, Pointer> nativeThreads;

    static boolean isCompatibleVersion(String stringArray, String stringArray2) {
        stringArray = stringArray.split("\\.");
        stringArray2 = stringArray2.split("\\.");
        if (stringArray.length < 3 || stringArray2.length < 3) {
            return false;
        }
        int n2 = Integer.parseInt(stringArray[0]);
        int n3 = Integer.parseInt(stringArray2[0]);
        int n4 = Integer.parseInt(stringArray[1]);
        int n5 = Integer.parseInt(stringArray2[1]);
        if (n2 != n3) {
            return false;
        }
        return n4 <= n5;
    }

    private static void dispose() {
        CallbackReference.disposeAll();
        Memory.disposeAll();
        NativeLibrary.disposeAll();
        Native.unregisterAll();
        jnidispatchPath = null;
        System.setProperty("jna.loaded", "false");
    }

    static boolean deleteLibrary(File file) {
        if (file.delete()) {
            return true;
        }
        Native.markTemporaryFile(file);
        return false;
    }

    private Native() {
    }

    private static native void initIDs();

    public static synchronized native void setProtected(boolean var0);

    public static synchronized native boolean isProtected();

    public static long getWindowID(Window window) throws HeadlessException {
        return AWT.getWindowID(window);
    }

    public static long getComponentID(Component component) throws HeadlessException {
        return AWT.getComponentID(component);
    }

    public static Pointer getWindowPointer(Window window) throws HeadlessException {
        return new Pointer(AWT.getWindowID(window));
    }

    public static Pointer getComponentPointer(Component component) throws HeadlessException {
        return new Pointer(AWT.getComponentID(component));
    }

    static native long getWindowHandle0(Component var0);

    public static Pointer getDirectBufferPointer(Buffer buffer) {
        long l2 = Native._getDirectBufferPointer(buffer);
        if (l2 == 0L) {
            return null;
        }
        return new Pointer(l2);
    }

    private static native long _getDirectBufferPointer(Buffer var0);

    private static Charset getCharset(String string) {
        Charset charset = null;
        if (string != null) {
            try {
                charset = Charset.forName(string);
            }
            catch (IllegalCharsetNameException | UnsupportedCharsetException illegalArgumentException) {
                LOG.log(Level.WARNING, "JNA Warning: Encoding ''{0}'' is unsupported ({1})", new Object[]{string, illegalArgumentException.getMessage()});
            }
        }
        if (charset == null) {
            LOG.log(Level.WARNING, "JNA Warning: Using fallback encoding {0}", DEFAULT_CHARSET);
            charset = DEFAULT_CHARSET;
        }
        return charset;
    }

    public static String toString(byte[] byArray) {
        return Native.toString(byArray, Native.getDefaultStringEncoding());
    }

    public static String toString(byte[] byArray, String string) {
        return Native.toString(byArray, Native.getCharset(string));
    }

    public static String toString(byte[] byArray, Charset charset) {
        int n2 = byArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (byArray[i2] != 0) continue;
            n2 = i2;
            break;
        }
        if (n2 == 0) {
            return "";
        }
        return new String(byArray, 0, n2, charset);
    }

    public static String toString(char[] cArray) {
        int n2 = cArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (cArray[i2] != '\u0000') continue;
            n2 = i2;
            break;
        }
        if (n2 == 0) {
            return "";
        }
        return new String(cArray, 0, n2);
    }

    public static List<String> toStringList(char[] cArray) {
        return Native.toStringList(cArray, 0, cArray.length);
    }

    public static List<String> toStringList(char[] cArray, int n2, int n3) {
        ArrayList<String> arrayList = new ArrayList<String>();
        int n4 = n2;
        n3 = n2 + n3;
        while (n2 < n3) {
            if (cArray[n2] == '\u0000') {
                if (n4 == n2) {
                    return arrayList;
                }
                String string = new String(cArray, n4, n2 - n4);
                arrayList.add(string);
                n4 = n2 + 1;
            }
            ++n2;
        }
        if (n4 < n3) {
            String string = new String(cArray, n4, n3 - n4);
            arrayList.add(string);
        }
        return arrayList;
    }

    public static <T extends Library> T load(Class<T> clazz) {
        return Native.load(null, clazz);
    }

    public static <T extends Library> T load(Class<T> clazz, Map<String, ?> map) {
        return Native.load(null, clazz, map);
    }

    public static <T extends Library> T load(String string, Class<T> clazz) {
        return Native.load(string, clazz, Collections.emptyMap());
    }

    public static <T extends Library> T load(String object, Class<T> clazz, Map<String, ?> map) {
        if (!Library.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Interface (" + clazz.getSimpleName() + ") of library=" + (String)object + " does not extend " + Library.class.getSimpleName());
        }
        object = new Library.Handler((String)object, clazz, map);
        ClassLoader classLoader = clazz.getClassLoader();
        object = Proxy.newProxyInstance(classLoader, new Class[]{clazz}, (InvocationHandler)object);
        Native.cacheOptions(clazz, map, object);
        return (T)((Library)clazz.cast(object));
    }

    @Deprecated
    public static <T> T loadLibrary(Class<T> clazz) {
        return Native.loadLibrary(null, clazz);
    }

    @Deprecated
    public static <T> T loadLibrary(Class<T> clazz, Map<String, ?> map) {
        return Native.loadLibrary(null, clazz, map);
    }

    @Deprecated
    public static <T> T loadLibrary(String string, Class<T> clazz) {
        return Native.loadLibrary(string, clazz, Collections.emptyMap());
    }

    @Deprecated
    public static <T> T loadLibrary(String object, Class<T> clazz, Map<String, ?> map) {
        if (!Library.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Interface (" + clazz.getSimpleName() + ") of library=" + (String)object + " does not extend " + Library.class.getSimpleName());
        }
        object = new Library.Handler((String)object, clazz, map);
        ClassLoader classLoader = clazz.getClassLoader();
        object = Proxy.newProxyInstance(classLoader, new Class[]{clazz}, (InvocationHandler)object);
        Native.cacheOptions(clazz, map, object);
        return clazz.cast(object);
    }

    private static void loadLibraryInstance(Class<?> clazz) {
        if (clazz != null && !libraries.containsKey(clazz)) {
            try {
                Field field;
                block5: {
                    Field[] fieldArray = clazz.getFields();
                    for (int i2 = 0; i2 < fieldArray.length; ++i2) {
                        field = fieldArray[i2];
                        if (field.getType() != clazz || !Modifier.isStatic(field.getModifiers())) {
                            continue;
                        }
                        break block5;
                    }
                    return;
                }
                field.setAccessible(true);
                libraries.put(clazz, new WeakReference<Object>(field.get(null)));
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("Could not access instance of " + clazz + " (" + exception + ")");
            }
        }
    }

    static Class<?> findEnclosingLibraryClass(Class<?> clazz) {
        while (clazz != null) {
            Object object = typeOptions.get(clazz);
            if (object != null) {
                if ((object = (Class)object.get(_OPTION_ENCLOSING_LIBRARY)) != null) {
                    return object;
                }
                return clazz;
            }
            if (Library.class.isAssignableFrom(clazz)) {
                return clazz;
            }
            if (Callback.class.isAssignableFrom(clazz)) {
                clazz = CallbackReference.findCallbackClass(clazz);
            }
            object = clazz.getDeclaringClass();
            if ((object = Native.findEnclosingLibraryClass(object)) != null) {
                return object;
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    public static Map<String, Object> getLibraryOptions(Class<?> clazz) {
        Map<Object, Object> map = typeOptions.get(clazz);
        if (map != null) {
            return map;
        }
        Class<?> clazz2 = Native.findEnclosingLibraryClass(clazz);
        if (clazz2 != null) {
            Native.loadLibraryInstance(clazz2);
        } else {
            clazz2 = clazz;
        }
        map = typeOptions.get(clazz2);
        if (map != null) {
            typeOptions.put(clazz, map);
            return map;
        }
        try {
            map = clazz2.getField("OPTIONS");
            ((Field)((Object)map)).setAccessible(true);
            map = (Map)((Field)((Object)map)).get(null);
            if (map == null) {
                throw new IllegalStateException("Null options field");
            }
        }
        catch (NoSuchFieldException noSuchFieldException) {
            map = Collections.emptyMap();
        }
        catch (Exception exception) {
            throw new IllegalArgumentException("OPTIONS must be a public field of type java.util.Map (" + exception + "): " + clazz2);
        }
        map = new HashMap(map);
        if (!map.containsKey("type-mapper")) {
            map.put("type-mapper", Native.lookupField(clazz2, "TYPE_MAPPER", TypeMapper.class));
        }
        if (!map.containsKey("structure-alignment")) {
            map.put("structure-alignment", Native.lookupField(clazz2, "STRUCTURE_ALIGNMENT", Integer.class));
        }
        if (!map.containsKey("string-encoding")) {
            map.put("string-encoding", Native.lookupField(clazz2, "STRING_ENCODING", String.class));
        }
        map = Native.cacheOptions(clazz2, map, null);
        if (clazz != clazz2) {
            typeOptions.put(clazz, map);
        }
        return map;
    }

    private static Object lookupField(Class<?> clazz, String string, Class<?> clazz2) {
        try {
            Field field = clazz.getField(string);
            field.setAccessible(true);
            return field.get(null);
        }
        catch (NoSuchFieldException noSuchFieldException) {
            return null;
        }
        catch (Exception exception) {
            throw new IllegalArgumentException(string + " must be a public field of type " + clazz2.getName() + " (" + exception + "): " + clazz);
        }
    }

    public static TypeMapper getTypeMapper(Class<?> object) {
        object = Native.getLibraryOptions(object);
        return (TypeMapper)object.get("type-mapper");
    }

    public static String getStringEncoding(Class<?> object) {
        object = Native.getLibraryOptions(object);
        if ((object = (String)object.get("string-encoding")) != null) {
            return object;
        }
        return Native.getDefaultStringEncoding();
    }

    public static String getDefaultStringEncoding() {
        return System.getProperty("jna.encoding", DEFAULT_ENCODING);
    }

    public static int getStructureAlignment(Class<?> constable) {
        if ((constable = (Integer)Native.getLibraryOptions(constable).get("structure-alignment")) == null) {
            return 0;
        }
        return (Integer)constable;
    }

    static byte[] getBytes(String string) {
        return Native.getBytes(string, Native.getDefaultStringEncoding());
    }

    static byte[] getBytes(String string, String string2) {
        return Native.getBytes(string, Native.getCharset(string2));
    }

    static byte[] getBytes(String string, Charset charset) {
        return string.getBytes(charset);
    }

    public static byte[] toByteArray(String string) {
        return Native.toByteArray(string, Native.getDefaultStringEncoding());
    }

    public static byte[] toByteArray(String string, String string2) {
        return Native.toByteArray(string, Native.getCharset(string2));
    }

    public static byte[] toByteArray(String object, Charset object2) {
        byte[] byArray = Native.getBytes((String)object, (Charset)object2);
        object = byArray;
        object2 = new byte[byArray.length + 1];
        System.arraycopy(object, 0, object2, 0, ((Object)object).length);
        return object2;
    }

    public static char[] toCharArray(String object) {
        char[] cArray = ((String)object).toCharArray();
        object = cArray;
        char[] cArray2 = new char[cArray.length + 1];
        System.arraycopy(object, 0, cArray2, 0, ((Object)object).length);
        return cArray2;
    }

    private static void loadNativeDispatchLibrary() {
        if (!Boolean.getBoolean("jna.nounpack")) {
            try {
                Native.removeTemporaryFiles();
            }
            catch (IOException iOException) {
                LOG.log(Level.WARNING, "JNA Warning: IOException removing temporary files", iOException);
            }
        }
        String string = System.getProperty("jna.boot.library.name", "jnidispatch");
        Object object = System.getProperty("jna.boot.library.path");
        if (object != null) {
            object = new StringTokenizer((String)object, File.pathSeparator);
            while (((StringTokenizer)object).hasMoreTokens()) {
                String string2;
                Object object2 = ((StringTokenizer)object).nextToken();
                object2 = new File(new File((String)object2), System.mapLibraryName(string).replace(".dylib", ".jnilib"));
                String string3 = ((File)object2).getAbsolutePath();
                LOG.log(DEBUG_JNA_LOAD_LEVEL, "Looking in {0}", string3);
                if (((File)object2).exists()) {
                    try {
                        LOG.log(DEBUG_JNA_LOAD_LEVEL, "Trying {0}", string3);
                        System.setProperty("jnidispatch.path", string3);
                        System.load(string3);
                        jnidispatchPath = string3;
                        LOG.log(DEBUG_JNA_LOAD_LEVEL, "Found jnidispatch at {0}", string3);
                        return;
                    }
                    catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
                }
                if (!Platform.isMac()) continue;
                if (string3.endsWith("dylib")) {
                    object2 = "dylib";
                    string2 = "jnilib";
                } else {
                    object2 = "jnilib";
                    string2 = "dylib";
                }
                string3 = string3.substring(0, string3.lastIndexOf((String)object2)) + string2;
                LOG.log(DEBUG_JNA_LOAD_LEVEL, "Looking in {0}", string3);
                if (!new File(string3).exists()) continue;
                try {
                    LOG.log(DEBUG_JNA_LOAD_LEVEL, "Trying {0}", string3);
                    System.setProperty("jnidispatch.path", string3);
                    System.load(string3);
                    jnidispatchPath = string3;
                    LOG.log(DEBUG_JNA_LOAD_LEVEL, "Found jnidispatch at {0}", string3);
                    return;
                }
                catch (UnsatisfiedLinkError unsatisfiedLinkError) {
                    LOG.log(Level.WARNING, "File found at " + string3 + " but not loadable: " + unsatisfiedLinkError.getMessage(), unsatisfiedLinkError);
                }
            }
        }
        if (!Boolean.parseBoolean((String)(object = System.getProperty("jna.nosys", "true"))) || Platform.isAndroid()) {
            try {
                LOG.log(DEBUG_JNA_LOAD_LEVEL, "Trying (via loadLibrary) {0}", string);
                System.loadLibrary(string);
                LOG.log(DEBUG_JNA_LOAD_LEVEL, "Found jnidispatch on system path");
                return;
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
        }
        if (!Boolean.getBoolean("jna.noclasspath")) {
            Native.loadNativeDispatchLibraryFromClasspath();
            return;
        }
        throw new UnsatisfiedLinkError("Unable to locate JNA native support library");
    }

    private static void loadNativeDispatchLibraryFromClasspath() {
        try {
            Object object = System.mapLibraryName("jnidispatch").replace(".dylib", ".jnilib");
            if (Platform.isAIX()) {
                object = "libjnidispatch.a";
            }
            object = "/com/sun/jna/" + Platform.RESOURCE_PREFIX + "/" + (String)object;
            if ((object = Native.extractFromResourcePath((String)object, Native.class.getClassLoader())) == null && object == null) {
                throw new UnsatisfiedLinkError("Could not find JNA native support");
            }
            LOG.log(DEBUG_JNA_LOAD_LEVEL, "Trying {0}", ((File)object).getAbsolutePath());
            System.setProperty("jnidispatch.path", ((File)object).getAbsolutePath());
            System.load(((File)object).getAbsolutePath());
            jnidispatchPath = ((File)object).getAbsolutePath();
            LOG.log(DEBUG_JNA_LOAD_LEVEL, "Found jnidispatch at {0}", jnidispatchPath);
            if (Native.isUnpacked((File)object) && !Boolean.getBoolean("jnidispatch.preserve")) {
                Native.deleteLibrary((File)object);
            }
            return;
        }
        catch (IOException iOException) {
            throw new UnsatisfiedLinkError(iOException.getMessage());
        }
    }

    static boolean isUnpacked(File file) {
        return file.getName().startsWith(JNA_TMPLIB_PREFIX);
    }

    public static File extractFromResourcePath(String string) throws IOException {
        return Native.extractFromResourcePath(string, null);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static File extractFromResourcePath(String string, ClassLoader object) throws IOException {
        Object object3;
        Level level;
        Level level2 = level = DEBUG_LOAD || DEBUG_JNA_LOAD && string.contains("jnidispatch") ? Level.INFO : Level.FINE;
        if (object == null && (object = Thread.currentThread().getContextClassLoader()) == null) {
            object = Native.class.getClassLoader();
        }
        LOG.log(level, "Looking in classpath from {0} for {1}", new Object[]{object, string});
        Object object2 = string.startsWith("/") ? string : NativeLibrary.mapSharedLibraryName(string);
        Object object4 = string.startsWith("/") ? string : Platform.RESOURCE_PREFIX + "/" + (String)object2;
        if (((String)object4).startsWith("/")) {
            object4 = ((String)object4).substring(1);
        }
        if ((object3 = ((ClassLoader)object).getResource((String)object4)) == null) {
            if (((String)object4).startsWith(Platform.RESOURCE_PREFIX)) {
                if (Platform.RESOURCE_PREFIX.startsWith("darwin")) {
                    object3 = ((ClassLoader)object).getResource("darwin/" + ((String)object4).substring(Platform.RESOURCE_PREFIX.length() + 1));
                }
                if (object3 == null) {
                    object3 = ((ClassLoader)object).getResource((String)object2);
                }
            } else if (((String)object4).startsWith("com/sun/jna/" + Platform.RESOURCE_PREFIX + "/")) {
                if (Platform.RESOURCE_PREFIX.startsWith("com/sun/jna/darwin")) {
                    object3 = ((ClassLoader)object).getResource("com/sun/jna/darwin" + ((String)object4).substring(("com/sun/jna/" + Platform.RESOURCE_PREFIX).length() + 1));
                }
                if (object3 == null) {
                    object3 = ((ClassLoader)object).getResource((String)object2);
                }
            }
        }
        if (object3 == null) {
            object2 = System.getProperty("java.class.path");
            if (!(object instanceof URLClassLoader)) throw new IOException("Native library (" + (String)object4 + ") not found in resource path (" + (String)object2 + ")");
            object2 = Arrays.asList(((URLClassLoader)object).getURLs()).toString();
            throw new IOException("Native library (" + (String)object4 + ") not found in resource path (" + (String)object2 + ")");
        }
        LOG.log(level, "Found library resource at {0}", object3);
        object2 = null;
        if (((URL)object3).getProtocol().toLowerCase().equals("file")) {
            try {
                object2 = new File(new URI(((URL)object3).toString()));
            }
            catch (URISyntaxException uRISyntaxException) {
                object2 = new File(((URL)object3).getPath());
            }
            LOG.log(level, "Looking in {0}", ((File)object2).getAbsolutePath());
            if (((File)object2).exists()) return object2;
            throw new IOException("File URL " + object3 + " could not be properly decoded");
        }
        if (Boolean.getBoolean("jna.nounpack")) return object2;
        object = ((URL)object3).openStream();
        if (object == null) {
            throw new IOException("Can't obtain InputStream for " + (String)object4);
        }
        object4 = null;
        try {
            int n2;
            object2 = Native.getTempDir();
            object2 = File.createTempFile(JNA_TMPLIB_PREFIX, Platform.isWindows() ? ".dll" : null, (File)object2);
            if (!Boolean.getBoolean("jnidispatch.preserve")) {
                ((File)object2).deleteOnExit();
            }
            LOG.log(level, "Extracting library to {0}", ((File)object2).getAbsolutePath());
            object4 = new FileOutputStream((File)object2);
            object3 = new byte[1024];
            while ((n2 = ((InputStream)object).read((byte[])object3, 0, ((Object)object3).length)) > 0) {
                ((FileOutputStream)object4).write((byte[])object3, 0, n2);
            }
        }
        catch (IOException iOException) {
            try {
                throw new IOException("Failed to create temporary file for " + string + " library: " + iOException.getMessage());
            }
            catch (Throwable throwable) {
                try {
                    ((InputStream)object).close();
                }
                catch (IOException iOException2) {}
                if (object4 == null) throw throwable;
                try {
                    ((FileOutputStream)object4).close();
                    throw throwable;
                }
                catch (IOException iOException3) {}
                throw throwable;
            }
        }
        try {
            ((InputStream)object).close();
        }
        catch (IOException iOException) {}
        try {
            ((FileOutputStream)object4).close();
            return object2;
        }
        catch (IOException iOException) {
            return object2;
        }
    }

    private static native int sizeof(int var0);

    private static native String getNativeVersion();

    private static native String getAPIChecksum();

    public static native int getLastError();

    public static native void setLastError(int var0);

    public static Library synchronizedLibrary(Library object) {
        Class<?> clazz = object.getClass();
        if (!Proxy.isProxyClass(clazz)) {
            throw new IllegalArgumentException("Library must be a proxy class");
        }
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(object);
        if (!(invocationHandler instanceof Library.Handler)) {
            throw new IllegalArgumentException("Unrecognized proxy handler: " + invocationHandler);
        }
        invocationHandler = (Library.Handler)invocationHandler;
        object = new InvocationHandler((Library.Handler)invocationHandler, (Library)object){
            final /* synthetic */ Library.Handler val$handler;
            final /* synthetic */ Library val$library;
            {
                this.val$handler = handler;
                this.val$library = library;
            }

            @Override
            public final Object invoke(Object object, Method method, Object[] objectArray) throws Throwable {
                object = this.val$handler.getNativeLibrary();
                synchronized (object) {
                    return this.val$handler.invoke(this.val$library, method, objectArray);
                }
            }
        };
        return (Library)Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), (InvocationHandler)object);
    }

    public static String getWebStartLibraryPath(String string) {
        if (System.getProperty("javawebstart.version") == null) {
            return null;
        }
        try {
            ClassLoader classLoader = Native.class.getClassLoader();
            Method method = AccessController.doPrivileged(new PrivilegedAction<Method>(){

                @Override
                public final Method run() {
                    try {
                        Method method = ClassLoader.class.getDeclaredMethod("findLibrary", String.class);
                        method.setAccessible(true);
                        return method;
                    }
                    catch (Exception exception) {
                        return null;
                    }
                }
            });
            string = (String)method.invoke(classLoader, string);
            if (string != null) {
                return new File(string).getParent();
            }
            return null;
        }
        catch (Exception exception) {
            return null;
        }
    }

    static void markTemporaryFile(File serializable) {
        try {
            serializable = new File(((File)serializable).getParentFile(), ((File)serializable).getName() + ".x");
            ((File)serializable).createNewFile();
            return;
        }
        catch (IOException iOException) {
            serializable = iOException;
            iOException.printStackTrace();
            return;
        }
    }

    static File getTempDir() throws IOException {
        Object object = System.getProperty("jna.tmpdir");
        if (object != null) {
            object = new File((String)object);
            ((File)object).mkdirs();
        } else {
            File file = new File(System.getProperty("java.io.tmpdir"));
            if (Platform.isMac()) {
                object = new File(System.getProperty("user.home"), "Library/Caches/JNA/temp");
            } else if (Platform.isLinux() || Platform.isSolaris() || Platform.isAIX() || Platform.isFreeBSD() || Platform.isNetBSD() || Platform.isOpenBSD() || Platform.iskFreeBSD()) {
                object = System.getenv("XDG_CACHE_HOME");
                object = object == null || ((String)object).trim().isEmpty() ? new File(System.getProperty("user.home"), ".cache") : new File((String)object);
                object = new File((File)object, "JNA/temp");
            } else {
                object = new File(file, "jna-" + System.getProperty("user.name").hashCode());
            }
            ((File)object).mkdirs();
            if (!((File)object).exists() || !((File)object).canWrite()) {
                object = file;
            }
        }
        if (!((File)object).exists()) {
            throw new IOException("JNA temporary directory '" + object + "' does not exist");
        }
        if (!((File)object).canWrite()) {
            throw new IOException("JNA temporary directory '" + object + "' is not writable");
        }
        return object;
    }

    static void removeTemporaryFiles() throws IOException {
        File[] fileArray = Native.getTempDir();
        FilenameFilter filenameFilter = new FilenameFilter(){

            @Override
            public final boolean accept(File file, String string) {
                return string.endsWith(".x") && string.startsWith(Native.JNA_TMPLIB_PREFIX);
            }
        };
        fileArray = fileArray.listFiles(filenameFilter);
        for (int i2 = 0; fileArray != null && i2 < fileArray.length; ++i2) {
            File file = fileArray[i2];
            Object object = file.getName();
            object = ((String)object).substring(0, ((String)object).length() - 2);
            object = new File(file.getParentFile(), (String)object);
            if (((File)object).exists() && !((File)object).delete()) continue;
            file.delete();
        }
    }

    public static int getNativeSize(Class<?> clazz, Object object) {
        if (clazz.isArray()) {
            int n2 = Array.getLength(object);
            if (n2 > 0) {
                object = Array.get(object, 0);
                return n2 * Native.getNativeSize(clazz.getComponentType(), object);
            }
            throw new IllegalArgumentException("Arrays of length zero not allowed: " + clazz);
        }
        if (Structure.class.isAssignableFrom(clazz) && !Structure.ByReference.class.isAssignableFrom(clazz)) {
            return Structure.size(clazz, (Structure)object);
        }
        try {
            return Native.getNativeSize(clazz);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new IllegalArgumentException("The type \"" + clazz.getName() + "\" is not supported: " + illegalArgumentException.getMessage());
        }
    }

    public static int getNativeSize(Class<?> clazz) {
        if (NativeMapped.class.isAssignableFrom(clazz)) {
            clazz = NativeMappedConverter.getInstance(clazz).nativeType();
        }
        if (clazz == Boolean.TYPE || clazz == Boolean.class) {
            return 4;
        }
        if (clazz == Byte.TYPE || clazz == Byte.class) {
            return 1;
        }
        if (clazz == Short.TYPE || clazz == Short.class) {
            return 2;
        }
        if (clazz == Character.TYPE || clazz == Character.class) {
            return WCHAR_SIZE;
        }
        if (clazz == Integer.TYPE || clazz == Integer.class) {
            return 4;
        }
        if (clazz == Long.TYPE || clazz == Long.class) {
            return 8;
        }
        if (clazz == Float.TYPE || clazz == Float.class) {
            return 4;
        }
        if (clazz == Double.TYPE || clazz == Double.class) {
            return 8;
        }
        if (Structure.class.isAssignableFrom(clazz)) {
            if (Structure.ByValue.class.isAssignableFrom(clazz)) {
                return Structure.size(clazz);
            }
            return POINTER_SIZE;
        }
        if (Pointer.class.isAssignableFrom(clazz) || Platform.HAS_BUFFERS && Buffers.isBuffer(clazz) || Callback.class.isAssignableFrom(clazz) || String.class == clazz || WString.class == clazz) {
            return POINTER_SIZE;
        }
        throw new IllegalArgumentException("Native size for type \"" + clazz.getName() + "\" is unknown");
    }

    public static boolean isSupportedNativeType(Class<?> clazz) {
        if (Structure.class.isAssignableFrom(clazz)) {
            return true;
        }
        try {
            return Native.getNativeSize(clazz) != 0;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return false;
        }
    }

    public static void setCallbackExceptionHandler(Callback.UncaughtExceptionHandler uncaughtExceptionHandler) {
        callbackExceptionHandler = uncaughtExceptionHandler == null ? DEFAULT_HANDLER : uncaughtExceptionHandler;
    }

    public static Callback.UncaughtExceptionHandler getCallbackExceptionHandler() {
        return callbackExceptionHandler;
    }

    public static void register(String string) {
        Native.register(Native.findDirectMappedClass(Native.getCallingClass()), string);
    }

    public static void register(NativeLibrary nativeLibrary) {
        Native.register(Native.findDirectMappedClass(Native.getCallingClass()), nativeLibrary);
    }

    static Class<?> findDirectMappedClass(Class<?> clazz) {
        Method[] methodArray = clazz.getDeclaredMethods();
        Method[] methodArray2 = methodArray;
        methodArray2 = methodArray;
        int n2 = methodArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Method method = methodArray2[i2];
            if ((method.getModifiers() & 0x100) == 0) continue;
            return clazz;
        }
        int n3 = clazz.getName().lastIndexOf("$");
        if (n3 != -1) {
            String string = clazz.getName().substring(0, n3);
            try {
                return Native.findDirectMappedClass(Class.forName(string, true, clazz.getClassLoader()));
            }
            catch (ClassNotFoundException classNotFoundException) {}
        }
        throw new IllegalArgumentException("Can't determine class with native methods from the current context (" + clazz + ")");
    }

    static Class<?> getCallingClass() {
        Class<?>[] classArray = new SecurityManager(){

            @Override
            public final Class<?>[] getClassContext() {
                return super.getClassContext();
            }
        }.getClassContext();
        if (classArray == null) {
            throw new IllegalStateException("The SecurityManager implementation on this platform is broken; you must explicitly provide the class to register");
        }
        if (classArray.length < 4) {
            throw new IllegalStateException("This method must be called from the static initializer of a class");
        }
        return classArray[3];
    }

    public static void setCallbackThreadInitializer(Callback callback, CallbackThreadInitializer callbackThreadInitializer) {
        CallbackReference.setCallbackThreadInitializer(callback, callbackThreadInitializer);
    }

    private static void unregisterAll() {
        Map<Class<?>, long[]> map = registeredClasses;
        synchronized (map) {
            for (Map.Entry<Class<?>, long[]> entry : registeredClasses.entrySet()) {
                Native.unregister(entry.getKey(), entry.getValue());
            }
            registeredClasses.clear();
            return;
        }
    }

    public static void unregister() {
        Native.unregister(Native.findDirectMappedClass(Native.getCallingClass()));
    }

    public static void unregister(Class<?> clazz) {
        Map<Class<?>, long[]> map = registeredClasses;
        synchronized (map) {
            long[] lArray = registeredClasses.get(clazz);
            if (lArray != null) {
                Native.unregister(clazz, lArray);
                registeredClasses.remove(clazz);
                registeredLibraries.remove(clazz);
            }
            return;
        }
    }

    public static boolean registered(Class<?> clazz) {
        Map<Class<?>, long[]> map = registeredClasses;
        synchronized (map) {
            return registeredClasses.containsKey(clazz);
        }
    }

    private static native void unregister(Class<?> var0, long[] var1);

    static String getSignature(Class<?> clazz) {
        if (clazz.isArray()) {
            return "[" + Native.getSignature(clazz.getComponentType());
        }
        if (clazz.isPrimitive()) {
            if (clazz == Void.TYPE) {
                return "V";
            }
            if (clazz == Boolean.TYPE) {
                return "Z";
            }
            if (clazz == Byte.TYPE) {
                return "B";
            }
            if (clazz == Short.TYPE) {
                return "S";
            }
            if (clazz == Character.TYPE) {
                return "C";
            }
            if (clazz == Integer.TYPE) {
                return "I";
            }
            if (clazz == Long.TYPE) {
                return "J";
            }
            if (clazz == Float.TYPE) {
                return "F";
            }
            if (clazz == Double.TYPE) {
                return "D";
            }
        }
        return "L" + Native.replace(".", "/", clazz.getName()) + ";";
    }

    static String replace(String string, String string2, String string3) {
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            int n2;
            if ((n2 = string3.indexOf(string)) == -1) break;
            stringBuilder.append(string3.substring(0, n2));
            stringBuilder.append(string2);
            string3 = string3.substring(n2 + string.length());
        }
        stringBuilder.append(string3);
        return stringBuilder.toString();
    }

    private static int getConversion(Class<?> clazz, TypeMapper object, boolean bl) {
        Class<?> clazz2;
        if (clazz == Void.class) {
            clazz = Void.TYPE;
        }
        if (object != null) {
            clazz2 = object.getFromNativeConverter(clazz);
            object = object.getToNativeConverter(clazz);
            if (clazz2 != null) {
                clazz = clazz2.nativeType();
                if (clazz == String.class) {
                    return 24;
                }
                if (clazz == WString.class) {
                    return 25;
                }
                return 23;
            }
            if (object != null) {
                clazz = object.nativeType();
                if (clazz == String.class) {
                    return 24;
                }
                if (clazz == WString.class) {
                    return 25;
                }
                return 23;
            }
        }
        if (Pointer.class.isAssignableFrom(clazz)) {
            return 1;
        }
        if (String.class == clazz) {
            return 2;
        }
        if (WString.class.isAssignableFrom(clazz)) {
            return 20;
        }
        if (Platform.HAS_BUFFERS && Buffers.isBuffer(clazz)) {
            return 5;
        }
        if (Structure.class.isAssignableFrom(clazz)) {
            if (Structure.ByValue.class.isAssignableFrom(clazz)) {
                return 4;
            }
            return 3;
        }
        if (clazz.isArray()) {
            switch (clazz.getName().charAt(1)) {
                case 'Z': {
                    return 13;
                }
                case 'B': {
                    return 6;
                }
                case 'S': {
                    return 7;
                }
                case 'C': {
                    return 8;
                }
                case 'I': {
                    return 9;
                }
                case 'J': {
                    return 10;
                }
                case 'F': {
                    return 11;
                }
                case 'D': {
                    return 12;
                }
            }
        }
        if (clazz.isPrimitive()) {
            if (clazz == Boolean.TYPE) {
                return 14;
            }
            return 0;
        }
        if (Callback.class.isAssignableFrom(clazz)) {
            return 15;
        }
        if (IntegerType.class.isAssignableFrom(clazz)) {
            return 21;
        }
        if (PointerType.class.isAssignableFrom(clazz)) {
            return 22;
        }
        if (NativeMapped.class.isAssignableFrom(clazz)) {
            clazz2 = NativeMappedConverter.getInstance(clazz).nativeType();
            if (clazz2 == String.class) {
                return 18;
            }
            if (clazz2 == WString.class) {
                return 19;
            }
            return 17;
        }
        if (JNIEnv.class == clazz) {
            return 27;
        }
        if (bl) {
            return 26;
        }
        return -1;
    }

    public static void register(Class<?> clazz, String object) {
        object = NativeLibrary.getInstance((String)object, Collections.singletonMap("classloader", clazz.getClassLoader()));
        Native.register(clazz, (NativeLibrary)object);
    }

    /*
     * WARNING - void declaration
     */
    public static void register(Class<?> clazz, NativeLibrary nativeLibrary) {
        Method[] objectArray = clazz.getDeclaredMethods();
        ArrayList<Method> arrayList = new ArrayList<Method>();
        Map<String, ?> map2 = nativeLibrary.getOptions();
        TypeMapper typeMapper = (TypeMapper)map2.get("type-mapper");
        boolean bl = Boolean.TRUE.equals(map2.get("allow-objects"));
        Native.cacheOptions(clazz, map2, null);
        Method[] methodArray = objectArray;
        int n2 = objectArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Method method = methodArray[i2];
            if ((method.getModifiers() & 0x100) == 0) continue;
            arrayList.add(method);
        }
        long[] lArray = new long[arrayList.size()];
        for (n2 = 0; n2 < lArray.length; ++n2) {
            void var8_17;
            Class<Pointer> clazz2;
            long l2;
            long l3;
            Method method = (Method)arrayList.get(n2);
            String string = "(";
            Class<?> clazz3 = method.getReturnType();
            Class<?>[] classArray = method.getParameterTypes();
            long[] lArray2 = new long[classArray.length];
            long[] lArray3 = new long[classArray.length];
            int[] nArray = new int[classArray.length];
            ToNativeConverter[] toNativeConverterArray = new ToNativeConverter[classArray.length];
            FromNativeConverter fromNativeConverter = null;
            int n3 = Native.getConversion(clazz3, typeMapper, bl);
            boolean bl2 = false;
            switch (n3) {
                case -1: {
                    throw new IllegalArgumentException(clazz3 + " is not a supported return type (in method " + method.getName() + " in " + clazz + ")");
                }
                case 23: 
                case 24: 
                case 25: {
                    fromNativeConverter = typeMapper.getFromNativeConverter(clazz3);
                    l3 = Structure.FFIType.get(clazz3.isPrimitive() ? clazz3 : Pointer.class).getPointer().peer;
                    l2 = Structure.FFIType.get(fromNativeConverter.nativeType()).getPointer().peer;
                    break;
                }
                case 17: 
                case 18: 
                case 19: 
                case 21: 
                case 22: {
                    l3 = Structure.FFIType.get(Pointer.class).getPointer().peer;
                    l2 = Structure.FFIType.get(NativeMappedConverter.getInstance(clazz3).nativeType()).getPointer().peer;
                    break;
                }
                case 3: 
                case 26: {
                    l3 = l2 = Structure.FFIType.get(Pointer.class).getPointer().peer;
                    break;
                }
                case 4: {
                    l3 = Structure.FFIType.get(Pointer.class).getPointer().peer;
                    l2 = Structure.FFIType.get(clazz3).getPointer().peer;
                    break;
                }
                default: {
                    l3 = l2 = Structure.FFIType.get(clazz3).getPointer().peer;
                }
            }
            block19: for (int i3 = 0; i3 < classArray.length; ++i3) {
                int n4;
                clazz2 = classArray[i3];
                String string2 = (String)var8_17 + Native.getSignature(clazz2);
                nArray[i3] = n4 = Native.getConversion(clazz2, typeMapper, bl);
                if (n4 == -1) {
                    throw new IllegalArgumentException(clazz2 + " is not a supported argument type (in method " + method.getName() + " in " + clazz + ")");
                }
                if (n4 == 17 || n4 == 18 || n4 == 19 || n4 == 21) {
                    clazz2 = NativeMappedConverter.getInstance(clazz2).nativeType();
                } else if (n4 == 23 || n4 == 24 || n4 == 25) {
                    toNativeConverterArray[i3] = typeMapper.getToNativeConverter(clazz2);
                }
                switch (n4) {
                    case 4: 
                    case 17: 
                    case 18: 
                    case 19: 
                    case 21: 
                    case 22: {
                        lArray2[i3] = Structure.FFIType.get((Object)clazz2).getPointer().peer;
                        lArray3[i3] = Structure.FFIType.get(Pointer.class).getPointer().peer;
                        continue block19;
                    }
                    case 23: 
                    case 24: 
                    case 25: {
                        lArray3[i3] = Structure.FFIType.get(clazz2.isPrimitive() ? clazz2 : Pointer.class).getPointer().peer;
                        lArray2[i3] = Structure.FFIType.get(toNativeConverterArray[i3].nativeType()).getPointer().peer;
                        continue block19;
                    }
                    case 0: {
                        lArray3[i3] = lArray2[i3] = Structure.FFIType.get(clazz2).getPointer().peer;
                        continue block19;
                    }
                    default: {
                        lArray3[i3] = lArray2[i3] = Structure.FFIType.get(Pointer.class).getPointer().peer;
                    }
                }
            }
            String string4 = (String)var8_17 + ")";
            string4 = string4 + Native.getSignature(clazz3);
            Class<?>[] classArray2 = method.getExceptionTypes();
            for (int i4 = 0; i4 < classArray2.length; ++i4) {
                if (!LastErrorException.class.isAssignableFrom(classArray2[i4])) continue;
                bl2 = true;
                break;
            }
            clazz2 = nativeLibrary.getFunction(method.getName(), method);
            try {
                lArray[n2] = Native.registerMethod(clazz, method.getName(), string4, nArray, lArray3, lArray2, n3, l3, l2, method, ((Pointer)((Object)clazz2)).peer, ((Function)((Object)clazz2)).getCallingConvention(), bl2, toNativeConverterArray, fromNativeConverter, ((Function)((Object)clazz2)).encoding);
                continue;
            }
            catch (NoSuchMethodError noSuchMethodError) {
                throw new UnsatisfiedLinkError("No method " + method.getName() + " with signature " + string4 + " in " + clazz);
            }
        }
        Map<Class<?>, long[]> map = registeredClasses;
        synchronized (map) {
            registeredClasses.put(clazz, lArray);
            registeredLibraries.put(clazz, nativeLibrary);
            return;
        }
    }

    private static Map<String, Object> cacheOptions(Class<?> classArray, Map<String, ?> map, Object object) {
        map = new HashMap(map);
        map.put(_OPTION_ENCLOSING_LIBRARY, classArray);
        typeOptions.put((Class<?>)classArray, map);
        if (object != null) {
            libraries.put((Class<?>)classArray, (Reference<?>)new WeakReference<Object>(object));
        }
        if (!classArray.isInterface() && Library.class.isAssignableFrom((Class<?>)classArray)) {
            Class<?>[] classArray2 = classArray.getInterfaces();
            classArray = classArray2;
            classArray = classArray2;
            int n2 = classArray2.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                Class<?> clazz = classArray[i2];
                if (!Library.class.isAssignableFrom(clazz)) continue;
                Native.cacheOptions(clazz, map, object);
                break;
            }
        }
        return map;
    }

    private static native long registerMethod(Class<?> var0, String var1, String var2, int[] var3, long[] var4, long[] var5, int var6, long var7, long var9, Method var11, long var12, int var14, boolean var15, ToNativeConverter[] var16, FromNativeConverter var17, String var18);

    private static NativeMapped fromNative(Class<?> clazz, Object object) {
        return (NativeMapped)NativeMappedConverter.getInstance(clazz).fromNative(object, new FromNativeContext(clazz));
    }

    private static NativeMapped fromNative(Method method, Object object) {
        Class<?> clazz = method.getReturnType();
        return (NativeMapped)NativeMappedConverter.getInstance(clazz).fromNative(object, new MethodResultContext(clazz, null, null, method));
    }

    private static Class<?> nativeType(Class<?> clazz) {
        return NativeMappedConverter.getInstance(clazz).nativeType();
    }

    private static Object toNative(ToNativeConverter toNativeConverter, Object object) {
        return toNativeConverter.toNative(object, new ToNativeContext());
    }

    private static Object fromNative(FromNativeConverter fromNativeConverter, Object object, Method method) {
        return fromNativeConverter.fromNative(object, new MethodResultContext(method.getReturnType(), null, null, method));
    }

    public static native long ffi_prep_cif(int var0, int var1, long var2, long var4);

    public static native void ffi_call(long var0, long var2, long var4, long var6);

    public static native long ffi_prep_closure(long var0, ffi_callback var2);

    public static native void ffi_free_closure(long var0);

    static native int initialize_ffi_type(long var0);

    public static void main(String[] object) {
        String string;
        object = Native.class.getPackage();
        String string2 = object != null ? ((Package)object).getSpecificationTitle() : "Java Native Access (JNA)";
        if (string2 == null) {
            string2 = "Java Native Access (JNA)";
        }
        if ((string = object != null ? ((Package)object).getSpecificationVersion() : "5.14.0") == null) {
            string = "5.14.0";
        }
        string2 = string2 + " API Version " + string;
        System.out.println(string2);
        string = object != null ? ((Package)object).getImplementationVersion() : "5.14.0 (package information missing)";
        if (string == null) {
            string = "5.14.0 (package information missing)";
        }
        System.out.println("Version: " + string);
        System.out.println(" Native: " + Native.getNativeVersion() + " (" + Native.getAPIChecksum() + ")");
        System.out.println(" Prefix: " + Platform.RESOURCE_PREFIX);
    }

    static synchronized native void freeNativeCallback(long var0);

    static synchronized native long createNativeCallback(Callback var0, Method var1, Class<?>[] var2, Class<?> var3, int var4, int var5, String var6);

    static native int invokeInt(Function var0, long var1, int var3, Object[] var4);

    static native long invokeLong(Function var0, long var1, int var3, Object[] var4);

    static native void invokeVoid(Function var0, long var1, int var3, Object[] var4);

    static native float invokeFloat(Function var0, long var1, int var3, Object[] var4);

    static native double invokeDouble(Function var0, long var1, int var3, Object[] var4);

    static native long invokePointer(Function var0, long var1, int var3, Object[] var4);

    private static native void invokeStructure(Function var0, long var1, int var3, Object[] var4, long var5, long var7);

    static Structure invokeStructure(Function function, long l2, int n2, Object[] objectArray, Structure structure) {
        Native.invokeStructure(function, l2, n2, objectArray, structure.getPointer().peer, structure.getTypeInfo().peer);
        return structure;
    }

    static native Object invokeObject(Function var0, long var1, int var3, Object[] var4);

    static long open(String string) {
        return Native.open(string, -1);
    }

    static native long open(String var0, int var1);

    static native void close(long var0);

    static native long findSymbol(long var0, String var2);

    static native long indexOf(Pointer var0, long var1, long var3, byte var5);

    static native void read(Pointer var0, long var1, long var3, byte[] var5, int var6, int var7);

    static native void read(Pointer var0, long var1, long var3, short[] var5, int var6, int var7);

    static native void read(Pointer var0, long var1, long var3, char[] var5, int var6, int var7);

    static native void read(Pointer var0, long var1, long var3, int[] var5, int var6, int var7);

    static native void read(Pointer var0, long var1, long var3, long[] var5, int var6, int var7);

    static native void read(Pointer var0, long var1, long var3, float[] var5, int var6, int var7);

    static native void read(Pointer var0, long var1, long var3, double[] var5, int var6, int var7);

    static native void write(Pointer var0, long var1, long var3, byte[] var5, int var6, int var7);

    static native void write(Pointer var0, long var1, long var3, short[] var5, int var6, int var7);

    static native void write(Pointer var0, long var1, long var3, char[] var5, int var6, int var7);

    static native void write(Pointer var0, long var1, long var3, int[] var5, int var6, int var7);

    static native void write(Pointer var0, long var1, long var3, long[] var5, int var6, int var7);

    static native void write(Pointer var0, long var1, long var3, float[] var5, int var6, int var7);

    static native void write(Pointer var0, long var1, long var3, double[] var5, int var6, int var7);

    static native byte getByte(Pointer var0, long var1, long var3);

    static native char getChar(Pointer var0, long var1, long var3);

    static native short getShort(Pointer var0, long var1, long var3);

    static native int getInt(Pointer var0, long var1, long var3);

    static native long getLong(Pointer var0, long var1, long var3);

    static native float getFloat(Pointer var0, long var1, long var3);

    static native double getDouble(Pointer var0, long var1, long var3);

    static Pointer getPointer(long l2) {
        long l3 = Native._getPointer(l2);
        if (l3 == 0L) {
            return null;
        }
        return new Pointer(l3);
    }

    private static native long _getPointer(long var0);

    static native String getWideString(Pointer var0, long var1, long var3);

    static String getString(Pointer pointer, long l2) {
        return Native.getString(pointer, l2, Native.getDefaultStringEncoding());
    }

    static String getString(Pointer object, long l2, String string) {
        Pointer pointer = object;
        object = Native.getStringBytes(pointer, pointer.peer, l2);
        if (string != null) {
            try {
                return new String((byte[])object, string);
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {}
        }
        return new String((byte[])object);
    }

    static native byte[] getStringBytes(Pointer var0, long var1, long var3);

    static native void setMemory(Pointer var0, long var1, long var3, long var5, byte var7);

    static native void setByte(Pointer var0, long var1, long var3, byte var5);

    static native void setShort(Pointer var0, long var1, long var3, short var5);

    static native void setChar(Pointer var0, long var1, long var3, char var5);

    static native void setInt(Pointer var0, long var1, long var3, int var5);

    static native void setLong(Pointer var0, long var1, long var3, long var5);

    static native void setFloat(Pointer var0, long var1, long var3, float var5);

    static native void setDouble(Pointer var0, long var1, long var3, double var5);

    static native void setPointer(Pointer var0, long var1, long var3, long var5);

    static native void setWideString(Pointer var0, long var1, long var3, String var5);

    static native ByteBuffer getDirectByteBuffer(Pointer var0, long var1, long var3, long var5);

    public static native long malloc(long var0);

    public static native void free(long var0);

    public static void detach(boolean bl) {
        Thread thread2 = Thread.currentThread();
        if (bl) {
            nativeThreads.remove(thread2);
            Pointer cfr_ignored_0 = nativeThreadTerminationFlag.get();
            Native.setDetachState(true, 0L);
            return;
        }
        if (!nativeThreads.containsKey(thread2)) {
            Pointer pointer = nativeThreadTerminationFlag.get();
            nativeThreads.put(thread2, pointer);
            Native.setDetachState(false, pointer.peer);
        }
    }

    static Pointer getTerminationFlag(Thread thread2) {
        return nativeThreads.get(thread2);
    }

    private static native void setDetachState(boolean var0, long var1);

    static {
        String string = System.getProperty("native.encoding");
        Charset charset = null;
        if (string != null) {
            try {
                charset = Charset.forName(string);
            }
            catch (Exception exception) {
                LOG.log(Level.WARNING, "Failed to get charset for native.encoding value : '" + string + "'", exception);
            }
        }
        if (charset == null) {
            charset = Charset.defaultCharset();
        }
        DEFAULT_CHARSET = charset;
        DEFAULT_ENCODING = charset.name();
        DEBUG_LOAD = Boolean.getBoolean("jna.debug_load");
        DEBUG_JNA_LOAD = Boolean.getBoolean("jna.debug_load.jna");
        DEBUG_JNA_LOAD_LEVEL = DEBUG_JNA_LOAD ? Level.INFO : Level.FINE;
        jnidispatchPath = null;
        typeOptions = Collections.synchronizedMap(new WeakHashMap());
        libraries = Collections.synchronizedMap(new WeakHashMap());
        callbackExceptionHandler = DEFAULT_HANDLER = new Callback.UncaughtExceptionHandler(){

            @Override
            public final void uncaughtException(Callback callback, Throwable throwable) {
                LOG.log(Level.WARNING, "JNA: Callback " + callback + " threw the following exception", throwable);
            }
        };
        Native.loadNativeDispatchLibrary();
        if (!Native.isCompatibleVersion("7.0.0", Native.getNativeVersion())) {
            string = System.lineSeparator();
            throw new Error(string + string + "There is an incompatible JNA native library installed on this system" + string + "Expected: 7.0.0" + string + "Found:    " + Native.getNativeVersion() + string + (jnidispatchPath != null ? "(at " + jnidispatchPath + ")" : System.getProperty("java.library.path")) + "." + string + "To resolve this issue you may do one of the following:" + string + " - remove or uninstall the offending library" + string + " - set the system property jna.nosys=true" + string + " - set jna.boot.library.path to include the path to the version of the " + string + "   jnidispatch library included with the JNA jar file you are using" + string);
        }
        POINTER_SIZE = Native.sizeof(0);
        LONG_SIZE = Native.sizeof(1);
        WCHAR_SIZE = Native.sizeof(2);
        SIZE_T_SIZE = Native.sizeof(3);
        BOOL_SIZE = Native.sizeof(4);
        LONG_DOUBLE_SIZE = Native.sizeof(5);
        Native.initIDs();
        if (Boolean.getBoolean("jna.protected")) {
            Native.setProtected(true);
        }
        MAX_ALIGNMENT = Platform.isSPARC() || Platform.isWindows() || Platform.isLinux() && (Platform.isARM() || Platform.isPPC() || Platform.isMIPS() || Platform.isLoongArch()) || Platform.isAIX() || Platform.isAndroid() && !Platform.isIntel() ? 8 : LONG_SIZE;
        MAX_PADDING = Platform.isMac() && Platform.isPPC() ? 8 : MAX_ALIGNMENT;
        System.setProperty("jna.loaded", "true");
        finalizer = new Object(){

            protected final void finalize() throws Throwable {
                Native.dispose();
                super.finalize();
            }
        };
        registeredClasses = new WeakHashMap();
        registeredLibraries = new WeakHashMap();
        nativeThreadTerminationFlag = new ThreadLocal<Memory>(){

            @Override
            protected final Memory initialValue() {
                Memory memory = new Memory(4L);
                memory.clear();
                return memory;
            }
        };
        nativeThreads = Collections.synchronizedMap(new WeakHashMap());
    }

    static class AWT {
        private AWT() {
        }

        static long getWindowID(Window window) throws HeadlessException {
            return AWT.getComponentID(window);
        }

        static long getComponentID(Object object) throws HeadlessException {
            if (GraphicsEnvironment.isHeadless()) {
                throw new HeadlessException("No native windows when headless");
            }
            if (((Component)(object = (Component)object)).isLightweight()) {
                throw new IllegalArgumentException("Component must be heavyweight");
            }
            if (!((Component)object).isDisplayable()) {
                throw new IllegalStateException("Component must be displayable");
            }
            if (Platform.isX11() && System.getProperty("java.version").startsWith("1.4") && !((Component)object).isVisible()) {
                throw new IllegalStateException("Component must be visible");
            }
            return Native.getWindowHandle0((Component)object);
        }
    }

    static class Buffers {
        private Buffers() {
        }

        static boolean isBuffer(Class<?> clazz) {
            return Buffer.class.isAssignableFrom(clazz);
        }
    }

    public static interface ffi_callback {
        public void invoke(long var1, long var3, long var5);
    }
}

