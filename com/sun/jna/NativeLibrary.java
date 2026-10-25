/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.Function;
import com.sun.jna.FunctionMapper;
import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.SymbolProvider;
import com.sun.jna.internal.Cleaner;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NativeLibrary
implements Closeable {
    private static final Logger LOG = Logger.getLogger(NativeLibrary.class.getName());
    private static final Level DEBUG_LOAD_LEVEL = Native.DEBUG_LOAD ? Level.INFO : Level.FINE;
    private static final SymbolProvider NATIVE_SYMBOL_PROVIDER = new SymbolProvider(){

        @Override
        public final long getSymbolAddress(long l2, String string, SymbolProvider symbolProvider) {
            return Native.findSymbol(l2, string);
        }
    };
    private final Cleaner.Cleanable cleanable;
    private volatile long handle;
    private final String libraryName;
    private final String libraryPath;
    private final Map<String, Function> functions = new HashMap<String, Function>();
    private final SymbolProvider symbolProvider;
    private final int callFlags;
    private final String encoding;
    private final Map<String, ?> options;
    private static final Map<String, Reference<NativeLibrary>> libraries = new HashMap<String, Reference<NativeLibrary>>();
    private static final Map<String, List<String>> searchPaths = new ConcurrentHashMap<String, List<String>>();
    private static final LinkedHashSet<String> librarySearchPath = new LinkedHashSet();
    private static final int DEFAULT_OPEN_OPTIONS = -1;
    private static Method addSuppressedMethod;

    private static String functionKey(String string, int n2, String string2) {
        return string + "|" + n2 + "|" + string2;
    }

    private NativeLibrary(String string, String object, long l2, Map<String, ?> map) {
        int n2;
        this.libraryName = this.getLibraryName(string);
        this.libraryPath = object;
        this.handle = l2;
        this.cleanable = Cleaner.getCleaner().register(this, new NativeLibraryDisposer(l2));
        string = map.get("calling-convention");
        this.callFlags = n2 = string instanceof Number ? ((Number)((Object)string)).intValue() : 0;
        this.options = map;
        Map<String, Function> map2 = (SymbolProvider)map.get("symbol-provider");
        this.symbolProvider = map2 == null ? NATIVE_SYMBOL_PROVIDER : map2;
        map2 = (String)map.get("string-encoding");
        if (map2 == null) {
            map2 = Native.getDefaultStringEncoding();
        }
        this.encoding = map2;
        if (Platform.isWindows() && "kernel32".equals(this.libraryName.toLowerCase())) {
            map2 = this.functions;
            synchronized (map2) {
                NativeLibrary nativeLibrary = this;
                object = new Function(nativeLibrary, "GetLastError", 63, this.encoding){

                    @Override
                    Object invoke(Object[] objectArray, Class<?> clazz, boolean bl, int n2) {
                        return Native.getLastError();
                    }

                    @Override
                    Object invoke(Method method, Class<?>[] classArray, Class<?> clazz, Object[] objectArray, Map<String, ?> map) {
                        return Native.getLastError();
                    }
                };
                this.functions.put(NativeLibrary.functionKey("GetLastError", this.callFlags, this.encoding), (Function)object);
                return;
            }
        }
    }

    private static int openFlags(Map<String, ?> map) {
        if ((map = map.get("open-flags")) instanceof Number) {
            return ((Number)((Object)map)).intValue();
        }
        return -1;
    }

    private static NativeLibrary loadLibrary(String string, Map<String, ?> map) {
        long l2;
        Object object;
        block40: {
            Object object2;
            LOG.log(DEBUG_LOAD_LEVEL, "Looking for library '" + string + "'");
            ArrayList<Throwable> arrayList = new ArrayList<Throwable>();
            boolean bl = new File(string).isAbsolute();
            LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
            int n2 = NativeLibrary.openFlags(map);
            object = searchPaths.get(string);
            if (object != null) {
                object2 = object;
                synchronized (object2) {
                    linkedHashSet.addAll((Collection<String>)object);
                }
            }
            if ((object2 = Native.getWebStartLibraryPath(string)) != null) {
                LOG.log(DEBUG_LOAD_LEVEL, "Adding web start path " + (String)object2);
                linkedHashSet.add((String)object2);
            }
            LOG.log(DEBUG_LOAD_LEVEL, "Adding paths from jna.library.path: " + System.getProperty("jna.library.path"));
            linkedHashSet.addAll(NativeLibrary.initPaths("jna.library.path"));
            object = NativeLibrary.findLibraryPath(string, linkedHashSet);
            l2 = 0L;
            try {
                LOG.log(DEBUG_LOAD_LEVEL, "Trying " + (String)object);
                l2 = Native.open((String)object, n2);
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError) {
                LOG.log(DEBUG_LOAD_LEVEL, "Loading failed with message: " + unsatisfiedLinkError.getMessage());
                LOG.log(DEBUG_LOAD_LEVEL, "Adding system paths: " + librarySearchPath);
                arrayList.add(unsatisfiedLinkError);
                linkedHashSet.addAll(librarySearchPath);
            }
            try {
                if (l2 == 0L) {
                    object = NativeLibrary.findLibraryPath(string, linkedHashSet);
                    LOG.log(DEBUG_LOAD_LEVEL, "Trying " + (String)object);
                    l2 = Native.open((String)object, n2);
                    if (l2 == 0L) {
                        throw new UnsatisfiedLinkError("Failed to load library '" + string + "'");
                    }
                }
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError) {
                block39: {
                    LOG.log(DEBUG_LOAD_LEVEL, "Loading failed with message: " + unsatisfiedLinkError.getMessage());
                    arrayList.add(unsatisfiedLinkError);
                    if (Platform.isAndroid()) {
                        try {
                            LOG.log(DEBUG_LOAD_LEVEL, "Preload (via System.loadLibrary) " + string);
                            System.loadLibrary(string);
                            l2 = Native.open((String)object, n2);
                        }
                        catch (UnsatisfiedLinkError unsatisfiedLinkError2) {
                            LOG.log(DEBUG_LOAD_LEVEL, "Loading failed with message: " + unsatisfiedLinkError2.getMessage());
                            arrayList.add(unsatisfiedLinkError2);
                        }
                    } else if (Platform.isLinux() || Platform.isFreeBSD()) {
                        LOG.log(DEBUG_LOAD_LEVEL, "Looking for version variants");
                        object = NativeLibrary.matchLibrary(string, linkedHashSet);
                        if (object != null) {
                            LOG.log(DEBUG_LOAD_LEVEL, "Trying " + (String)object);
                            try {
                                l2 = Native.open((String)object, n2);
                            }
                            catch (UnsatisfiedLinkError unsatisfiedLinkError3) {
                                LOG.log(DEBUG_LOAD_LEVEL, "Loading failed with message: " + unsatisfiedLinkError3.getMessage());
                                arrayList.add(unsatisfiedLinkError3);
                            }
                        }
                    } else if (Platform.isMac() && !string.endsWith(".dylib")) {
                        for (String object3 : NativeLibrary.matchFramework(string)) {
                            try {
                                LOG.log(DEBUG_LOAD_LEVEL, "Trying " + object3);
                                l2 = Native.open(object3, n2);
                                break;
                            }
                            catch (UnsatisfiedLinkError unsatisfiedLinkError4) {
                                LOG.log(DEBUG_LOAD_LEVEL, "Loading failed with message: " + unsatisfiedLinkError4.getMessage());
                                arrayList.add(unsatisfiedLinkError4);
                            }
                        }
                    } else if (Platform.isWindows() && !bl) {
                        LOG.log(DEBUG_LOAD_LEVEL, "Looking for lib- prefix");
                        object = NativeLibrary.findLibraryPath("lib" + string, linkedHashSet);
                        if (object != null) {
                            LOG.log(DEBUG_LOAD_LEVEL, "Trying " + (String)object);
                            try {
                                l2 = Native.open((String)object, n2);
                            }
                            catch (UnsatisfiedLinkError unsatisfiedLinkError5) {
                                LOG.log(DEBUG_LOAD_LEVEL, "Loading failed with message: " + unsatisfiedLinkError5.getMessage());
                                arrayList.add(unsatisfiedLinkError5);
                            }
                        }
                    }
                    if (l2 == 0L) {
                        try {
                            File file = Native.extractFromResourcePath(string, (ClassLoader)map.get("classloader"));
                            if (file == null) break block39;
                            try {
                                l2 = Native.open(file.getAbsolutePath(), n2);
                                object = file.getAbsolutePath();
                            }
                            finally {
                                if (Native.isUnpacked(file)) {
                                    Native.deleteLibrary(file);
                                }
                            }
                        }
                        catch (IOException iOException) {
                            LOG.log(DEBUG_LOAD_LEVEL, "Loading failed with message: " + iOException.getMessage());
                            arrayList.add(iOException);
                        }
                    }
                }
                if (l2 != 0L) break block40;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to load library '");
                stringBuilder.append(string);
                stringBuilder.append("':");
                for (Throwable throwable : arrayList) {
                    stringBuilder.append("\n");
                    stringBuilder.append(throwable.getMessage());
                }
                UnsatisfiedLinkError unsatisfiedLinkError6 = new UnsatisfiedLinkError(stringBuilder.toString());
                for (Throwable throwable : arrayList) {
                    NativeLibrary.addSuppressedReflected(unsatisfiedLinkError6, throwable);
                }
                throw unsatisfiedLinkError6;
            }
        }
        LOG.log(DEBUG_LOAD_LEVEL, "Found library '" + string + "' at " + (String)object);
        return new NativeLibrary(string, (String)object, l2, map);
    }

    private static void addSuppressedReflected(Throwable throwable, Throwable throwable2) {
        if (addSuppressedMethod == null) {
            return;
        }
        try {
            addSuppressedMethod.invoke(throwable, throwable2);
            return;
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
            throw new RuntimeException("Failed to call addSuppressedMethod", exception);
        }
    }

    static String[] matchFramework(String string) {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        Object object = new File(string);
        if (((File)object).isAbsolute()) {
            if (string.contains(".framework")) {
                if (((File)object).exists()) {
                    return new String[]{((File)object).getAbsolutePath()};
                }
                linkedHashSet.add(((File)object).getAbsolutePath());
            } else {
                if (((File)(object = new File(new File(((File)object).getParentFile(), ((File)object).getName() + ".framework"), ((File)object).getName()))).exists()) {
                    return new String[]{((File)object).getAbsolutePath()};
                }
                linkedHashSet.add(((File)object).getAbsolutePath());
            }
        } else {
            object = new String[]{System.getProperty("user.home"), "", "/System"};
            string = !string.contains(".framework") ? string + ".framework/" + string : string;
            String[] stringArray = object;
            int cfr_ignored_0 = ((String[])object).length;
            for (int i2 = 0; i2 < 3; ++i2) {
                object = stringArray[i2];
                if (((File)(object = new File((String)object + "/Library/Frameworks/" + string))).exists()) {
                    return new String[]{((File)object).getAbsolutePath()};
                }
                linkedHashSet.add(((File)object).getAbsolutePath());
            }
        }
        return linkedHashSet.toArray(new String[0]);
    }

    private String getLibraryName(String string) {
        int n2;
        String string2 = NativeLibrary.mapSharedLibraryName("---");
        int n3 = string2.indexOf("---");
        if (n3 > 0 && string.startsWith(string2.substring(0, n3))) {
            string = string.substring(n3);
        }
        if ((n2 = string.indexOf(string2 = string2.substring(n3 + 3))) != -1) {
            string = string.substring(0, n2);
        }
        return string;
    }

    public static final NativeLibrary getInstance(String string) {
        return NativeLibrary.getInstance(string, Collections.emptyMap());
    }

    public static final NativeLibrary getInstance(String string, ClassLoader classLoader) {
        return NativeLibrary.getInstance(string, Collections.singletonMap("classloader", classLoader));
    }

    public static final NativeLibrary getInstance(String object, Map<String, ?> map) {
        if ((map = new HashMap(map)).get("calling-convention") == null) {
            map.put("calling-convention", 0);
        }
        if ((Platform.isLinux() || Platform.isFreeBSD() || Platform.isAIX()) && Platform.C_LIBRARY_NAME.equals(object)) {
            object = null;
        }
        Map<String, Reference<NativeLibrary>> map2 = libraries;
        synchronized (map2) {
            Reference<NativeLibrary> reference = libraries.get((String)object + map);
            NativeLibrary nativeLibrary = reference != null ? reference.get() : null;
            if (nativeLibrary == null) {
                nativeLibrary = object == null ? new NativeLibrary("<process>", null, Native.open(null, NativeLibrary.openFlags(map)), map) : NativeLibrary.loadLibrary((String)object, map);
                reference = new WeakReference<NativeLibrary>(nativeLibrary);
                libraries.put(nativeLibrary.getName() + map, reference);
                object = nativeLibrary.getFile();
                if (object != null) {
                    libraries.put(((File)object).getAbsolutePath() + map, reference);
                    libraries.put(((File)object).getName() + map, reference);
                }
            }
            return nativeLibrary;
        }
    }

    public static final synchronized NativeLibrary getProcess() {
        return NativeLibrary.getInstance(null);
    }

    public static final synchronized NativeLibrary getProcess(Map<String, ?> map) {
        return NativeLibrary.getInstance(null, map);
    }

    public static final void addSearchPath(String string, String string2) {
        List<String> list2 = searchPaths.get(string);
        if (list2 == null) {
            list2 = Collections.synchronizedList(new ArrayList());
            searchPaths.put(string, list2);
        }
        list2.add(string2);
    }

    public Function getFunction(String string) {
        return this.getFunction(string, this.callFlags);
    }

    Function getFunction(String string, Method classArray) {
        Object object = (FunctionMapper)this.options.get("function-mapper");
        if (object != null) {
            string = object.getFunctionName(this, (Method)classArray);
        }
        if (string.startsWith((String)(object = System.getProperty("jna.profiler.prefix", "$$YJP$$")))) {
            string = string.substring(((String)object).length());
        }
        int n2 = this.callFlags;
        classArray = classArray.getExceptionTypes();
        for (int i2 = 0; i2 < classArray.length; ++i2) {
            if (!LastErrorException.class.isAssignableFrom(classArray[i2])) continue;
            n2 |= 0x40;
        }
        return this.getFunction(string, n2);
    }

    public Function getFunction(String string, int n2) {
        return this.getFunction(string, n2, this.encoding);
    }

    public Function getFunction(String string, int n2, String string2) {
        if (string == null) {
            throw new NullPointerException("Function name may not be null");
        }
        Map<String, Function> map = this.functions;
        synchronized (map) {
            String string3 = NativeLibrary.functionKey(string, n2, string2);
            Function function = this.functions.get(string3);
            if (function == null) {
                function = new Function(this, string, n2, string2);
                this.functions.put(string3, function);
            }
            return function;
        }
    }

    public Map<String, ?> getOptions() {
        return this.options;
    }

    public Pointer getGlobalVariableAddress(String string) {
        try {
            return new Pointer(this.getSymbolAddress(string));
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            throw new UnsatisfiedLinkError("Error looking up '" + string + "': " + unsatisfiedLinkError.getMessage());
        }
    }

    long getSymbolAddress(String string) {
        if (this.handle == 0L) {
            throw new UnsatisfiedLinkError("Library has been unloaded");
        }
        return this.symbolProvider.getSymbolAddress(this.handle, string, NATIVE_SYMBOL_PROVIDER);
    }

    public String toString() {
        return "Native Library <" + this.libraryPath + "@" + this.handle + ">";
    }

    public String getName() {
        return this.libraryName;
    }

    public File getFile() {
        if (this.libraryPath == null) {
            return null;
        }
        return new File(this.libraryPath);
    }

    static void disposeAll() {
        Object object;
        Object object2 = libraries;
        synchronized (object2) {
            object = new LinkedHashSet<Reference<NativeLibrary>>(libraries.values());
        }
        object2 = object.iterator();
        while (object2.hasNext()) {
            object = (Reference)object2.next();
            if ((object = (NativeLibrary)((Reference)object).get()) == null) continue;
            ((NativeLibrary)object).close();
        }
    }

    @Override
    public void close() {
        HashSet hashSet = new HashSet();
        Object object = libraries;
        synchronized (object) {
            for (Map.Entry<String, Reference<NativeLibrary>> entry : libraries.entrySet()) {
                Reference<NativeLibrary> reference = entry.getValue();
                if (reference.get() != this) continue;
                hashSet.add(entry.getKey());
            }
            for (Map.Entry<String, Reference<NativeLibrary>> entry : hashSet) {
                libraries.remove(entry);
            }
        }
        object = this;
        synchronized (object) {
            if (this.handle != 0L) {
                this.handle = 0L;
                this.cleanable.clean();
            }
            return;
        }
    }

    @Deprecated
    public void dispose() {
        this.close();
    }

    private static List<String> initPaths(String object) {
        if ("".equals(object = System.getProperty((String)object, ""))) {
            return Collections.emptyList();
        }
        object = new StringTokenizer((String)object, File.pathSeparator);
        ArrayList<String> arrayList = new ArrayList<String>();
        while (((StringTokenizer)object).hasMoreTokens()) {
            String string = ((StringTokenizer)object).nextToken();
            if ("".equals(string)) continue;
            arrayList.add(string);
        }
        return arrayList;
    }

    private static String findLibraryPath(String string, Collection<String> object) {
        if (new File(string).isAbsolute()) {
            return string;
        }
        string = NativeLibrary.mapSharedLibraryName(string);
        object = object.iterator();
        while (object.hasNext()) {
            String string2 = (String)object.next();
            File file = new File(string2, string);
            if (file.exists()) {
                return file.getAbsolutePath();
            }
            if (!Platform.isMac() || !string.endsWith(".dylib") || !(file = new File(string2, string.substring(0, string.lastIndexOf(".dylib")) + ".jnilib")).exists()) continue;
            return file.getAbsolutePath();
        }
        return string;
    }

    static String mapSharedLibraryName(String string) {
        if (Platform.isMac()) {
            if (string.startsWith("lib") && (string.endsWith(".dylib") || string.endsWith(".jnilib"))) {
                return string;
            }
            if ((string = System.mapLibraryName(string)).endsWith(".jnilib")) {
                return string.substring(0, string.lastIndexOf(".jnilib")) + ".dylib";
            }
            return string;
        }
        if (Platform.isLinux() || Platform.isFreeBSD() ? NativeLibrary.isVersionedName(string) || string.endsWith(".so") : (Platform.isAIX() ? NativeLibrary.isVersionedName(string) || string.endsWith(".so") || string.startsWith("lib") || string.endsWith(".a") : Platform.isWindows() && (string.endsWith(".drv") || string.endsWith(".dll") || string.endsWith(".ocx")))) {
            return string;
        }
        string = System.mapLibraryName(string);
        if (Platform.isAIX() && string.endsWith(".so")) {
            return string.replaceAll(".so$", ".a");
        }
        return string;
    }

    private static boolean isVersionedName(String string) {
        int n2;
        if (string.startsWith("lib") && (n2 = string.lastIndexOf(".so.")) != -1 && n2 + 4 < string.length()) {
            n2 += 4;
            while (n2 < string.length()) {
                char c2 = string.charAt(n2);
                if (!Character.isDigit(c2) && c2 != '.') {
                    return false;
                }
                ++n2;
            }
            return true;
        }
        return false;
    }

    static String matchLibrary(String iterator2, Collection<String> object) {
        Object object2 = new File((String)((Object)iterator2));
        if (((File)object2).isAbsolute()) {
            object = Arrays.asList(((File)object2).getParent());
        }
        iterator2 = new FilenameFilter((String)((Object)iterator2)){
            final /* synthetic */ String val$libName;
            {
                this.val$libName = string;
            }

            @Override
            public final boolean accept(File file, String string) {
                return (string.startsWith("lib" + this.val$libName + ".so") || string.startsWith(this.val$libName + ".so") && this.val$libName.startsWith("lib")) && NativeLibrary.isVersionedName(string);
            }
        };
        object2 = new LinkedList();
        Iterator iterator3 = object.iterator();
        while (iterator3.hasNext()) {
            String string = (String)iterator3.next();
            object = new File(string).listFiles((FilenameFilter)((Object)iterator2));
            if (object == null || ((Object)object).length <= 0) continue;
            object2.addAll(Arrays.asList(object));
        }
        double d2 = -1.0;
        object = null;
        iterator2 = object2.iterator();
        while (iterator2.hasNext()) {
            double d3;
            object2 = (File)iterator2.next();
            object2 = ((File)object2).getAbsolutePath();
            String string = ((String)object2).substring(((String)object2).lastIndexOf(".so.") + 4);
            double d4 = NativeLibrary.parseVersion(string);
            if (!(d3 > d2)) continue;
            d2 = d4;
            object = object2;
        }
        return object;
    }

    static double parseVersion(String string) {
        double d2 = 0.0;
        double d3 = 1.0;
        int n2 = string.indexOf(".");
        while (string != null) {
            String string2;
            if (n2 != -1) {
                string2 = string.substring(0, n2);
                string = string.substring(n2 + 1);
                n2 = string.indexOf(".");
            } else {
                string2 = string;
                string = null;
            }
            try {
                d2 += (double)Integer.parseInt(string2) / d3;
            }
            catch (NumberFormatException numberFormatException) {
                return 0.0;
            }
            d3 *= 100.0;
        }
        return d2;
    }

    private static String getMultiArchPath() {
        String string = Platform.ARCH;
        String string2 = Platform.iskFreeBSD() ? "-kfreebsd" : (Platform.isGNU() ? "" : "-linux");
        String string3 = "-gnu";
        if (Platform.isIntel()) {
            string = Platform.is64Bit() ? "x86_64" : "i386";
        } else if (Platform.isPPC()) {
            string = Platform.is64Bit() ? "powerpc64" : "powerpc";
        } else if (Platform.isARM()) {
            string = "arm";
            string3 = "-gnueabi";
        } else if (Platform.ARCH.equals("mips64el")) {
            string3 = "-gnuabi64";
        }
        return string + string2 + string3;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static ArrayList<String> getLinuxLdPaths() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Process process = null;
        BufferedReader bufferedReader = null;
        try {
            String string;
            process = Runtime.getRuntime().exec("/sbin/ldconfig -p");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((string = bufferedReader.readLine()) != null) {
                int n2 = string.indexOf(" => ");
                int n3 = string.lastIndexOf(47);
                if (n2 == -1 || n3 == -1 || n2 >= n3 || arrayList.contains(string = string.substring(n2 + 4, n3))) continue;
                arrayList.add(string);
            }
        }
        catch (Exception exception) {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                }
                catch (IOException iOException) {}
            }
            if (process == null) return arrayList;
            try {
                process.waitFor();
                return arrayList;
            }
            catch (InterruptedException interruptedException) {
                return arrayList;
            }
        }
        catch (Throwable throwable) {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                }
                catch (IOException iOException) {}
            }
            if (process == null) throw throwable;
            try {
                process.waitFor();
                throw throwable;
            }
            catch (InterruptedException interruptedException) {}
            throw throwable;
        }
        try {
            bufferedReader.close();
        }
        catch (IOException iOException) {}
        if (process == null) return arrayList;
        try {
            process.waitFor();
            return arrayList;
        }
        catch (InterruptedException interruptedException) {
            return arrayList;
        }
    }

    static {
        if (Native.POINTER_SIZE == 0) {
            throw new Error("Native library not initialized");
        }
        addSuppressedMethod = null;
        try {
            addSuppressedMethod = Throwable.class.getMethod("addSuppressed", Throwable.class);
        }
        catch (NoSuchMethodException noSuchMethodException) {
        }
        catch (SecurityException securityException) {
            Logger.getLogger(NativeLibrary.class.getName()).log(Level.SEVERE, "Failed to initialize 'addSuppressed' method", securityException);
        }
        String string = Native.getWebStartLibraryPath("jnidispatch");
        if (string != null) {
            librarySearchPath.add(string);
        }
        if (System.getProperty("jna.platform.library.path") == null && !Platform.isWindows()) {
            Object object;
            string = "";
            String string2 = "";
            String string3 = "";
            if (Platform.isLinux() || Platform.isSolaris() || Platform.isFreeBSD() || Platform.iskFreeBSD()) {
                string3 = (Platform.isSolaris() ? "/" : "") + (Native.POINTER_SIZE << 3);
            }
            String[] stringArray = new String[]{"/usr/lib" + string3, "/lib" + string3, "/usr/lib", "/lib"};
            if (Platform.isLinux() || Platform.iskFreeBSD() || Platform.isGNU()) {
                object = NativeLibrary.getMultiArchPath();
                stringArray = new String[]{"/usr/lib/" + (String)object, "/lib/" + (String)object, "/usr/lib" + string3, "/lib" + string3, "/usr/lib", "/lib"};
            }
            if (Platform.isLinux()) {
                object = NativeLibrary.getLinuxLdPaths();
                for (int i2 = stringArray.length - 1; i2 >= 0; --i2) {
                    int n2 = ((ArrayList)object).indexOf(stringArray[i2]);
                    if (n2 != -1) {
                        ((ArrayList)object).remove(n2);
                    }
                    ((ArrayList)object).add(0, stringArray[i2]);
                }
                stringArray = ((ArrayList)object).toArray(new String[0]);
            }
            for (int i3 = 0; i3 < stringArray.length; ++i3) {
                File file = new File(stringArray[i3]);
                if (!file.exists() || !file.isDirectory()) continue;
                string = string + string2 + stringArray[i3];
                string2 = File.pathSeparator;
            }
            if (!"".equals(string)) {
                System.setProperty("jna.platform.library.path", string);
            }
        }
        librarySearchPath.addAll(NativeLibrary.initPaths("jna.platform.library.path"));
    }

    static final class NativeLibraryDisposer
    implements Runnable {
        private long handle;

        public NativeLibraryDisposer(long l2) {
            this.handle = l2;
        }

        @Override
        public final synchronized void run() {
            if (this.handle != 0L) {
                try {
                    Native.close(this.handle);
                    return;
                }
                finally {
                    this.handle = 0L;
                }
            }
        }
    }
}

