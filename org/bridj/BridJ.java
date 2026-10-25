/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bridj.BridJRuntime;
import org.bridj.CLong;
import org.bridj.CRuntime;
import org.bridj.CallbackNativeImplementer;
import org.bridj.HeadersReconstructor;
import org.bridj.IntValuedEnum;
import org.bridj.NativeEntities;
import org.bridj.NativeLibrary;
import org.bridj.NativeObject;
import org.bridj.Platform;
import org.bridj.PlatformSupport;
import org.bridj.Pointer;
import org.bridj.SizeT;
import org.bridj.StructUtils;
import org.bridj.TimeT;
import org.bridj.ann.Library;
import org.bridj.ann.Runtime;
import org.bridj.demangling.Demangler;
import org.bridj.util.ASMUtils;
import org.bridj.util.AnnotationUtils;
import org.bridj.util.StringUtils;
import org.bridj.util.Utils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class BridJ {
    static final Map<AnnotatedElement, NativeLibrary> librariesByClass = new HashMap<AnnotatedElement, NativeLibrary>();
    static final Map<String, File> librariesFilesByName = new HashMap<String, File>();
    static final Map<File, NativeLibrary> librariesByFile = new HashMap<File, NativeLibrary>();
    private static NativeEntities orphanEntities = new NativeEntities();
    static final Map<Class<?>, BridJRuntime> classRuntimes = new HashMap();
    static final Map<Long, NativeObject> strongNativeObjects = new HashMap<Long, NativeObject>();
    static ThreadLocal<Stack<CastingType>> currentlyCastingNativeObject = new ThreadLocal<Stack<CastingType>>(){

        @Override
        protected final Stack<CastingType> initialValue() {
            Stack<CastingType> stack = new Stack<CastingType>();
            stack.push(CastingType.None);
            return stack;
        }
    };
    private static final ConcurrentHashMap<Long, NativeObject> registeredObjects = new ConcurrentHashMap();
    private static Map<Class<? extends BridJRuntime>, BridJRuntime> runtimes = new HashMap<Class<? extends BridJRuntime>, BridJRuntime>();
    static Map<Type, BridJRuntime.TypeInfo<?>> typeInfos = new HashMap();
    public static final boolean debug;
    public static final boolean debugNeverFree;
    public static final boolean debugPointers;
    public static final boolean debugPointerReleases;
    public static final boolean veryVerbose;
    public static final boolean verbose;
    public static final boolean quiet;
    public static final boolean logCalls;
    public static final boolean warnStructFields;
    public static final boolean protectedMode;
    public static final boolean enableDestructors;
    public static final boolean alignDoubles;
    public static final boolean cachePointers;
    static volatile int minLogLevelValue;
    static Logger logger;
    static Map<String, NativeLibrary> libHandles;
    static volatile List<String> nativeLibraryPaths;
    static List<String> additionalPaths;
    static Map<String, String> libraryActualNames;
    static Map<String, List<String>> libraryAliases;
    static Map<String, List<String>> libraryDependencies;
    private static final Pattern numPat;
    static Map<String, File> nativeLibraryFiles;
    static Boolean directModeEnabled;

    public static long sizeOf(Type type) {
        Class clazz = Utils.getClass(type);
        if (clazz.isPrimitive()) {
            return StructUtils.primTypeLength(clazz);
        }
        if (Pointer.class.isAssignableFrom(clazz)) {
            return Pointer.SIZE;
        }
        if (clazz == CLong.class) {
            return CLong.SIZE;
        }
        if (clazz == TimeT.class) {
            return TimeT.SIZE;
        }
        if (clazz == SizeT.class) {
            return SizeT.SIZE;
        }
        if (clazz == Integer.class || clazz == Float.class) {
            return 4L;
        }
        if (clazz == Character.class || clazz == Short.class) {
            return 2L;
        }
        if (clazz == Long.class || clazz == Double.class) {
            return 8L;
        }
        if (clazz == Boolean.class || clazz == Byte.class) {
            return 1L;
        }
        if (NativeObject.class.isAssignableFrom(clazz)) {
            return BridJ.getRuntime(clazz).getTypeInfo(type).sizeOf();
        }
        if (IntValuedEnum.class.isAssignableFrom(clazz)) {
            return 4L;
        }
        throw new RuntimeException("Unable to compute size of type " + Utils.toString(type));
    }

    public static synchronized <T extends NativeObject> T protectFromGC(T t2) {
        long l2 = Pointer.getAddress(t2, null);
        strongNativeObjects.put(l2, t2);
        return t2;
    }

    public static synchronized <T extends NativeObject> T unprotectFromGC(T t2) {
        long l2 = Pointer.getAddress(t2, null);
        NativeObject nativeObject = strongNativeObjects.remove(l2);
        if (nativeObject != t2) {
            throw new IllegalStateException("Unprotected object " + nativeObject + " instead of " + t2 + " for address " + l2);
        }
        return t2;
    }

    public static void delete(NativeObject nativeObject) {
        BridJ.setJavaObjectFromNativePeer(Pointer.getAddress(nativeObject, null), null);
        Pointer.getPointer(nativeObject, null).release();
    }

    public static synchronized void register() {
        Object object = new Exception().getStackTrace();
        if (((StackTraceElement[])object).length < 2) {
            throw new RuntimeException("No useful stack trace : cannot register with register(), please use register(Class) instead.");
        }
        object = object[1].getClassName();
        try {
            Class<?> clazz = Class.forName((String)object, false, Platform.getClassLoader());
            BridJ.register(clazz);
            return;
        }
        catch (Exception exception) {
            throw new RuntimeException("Failed to register class " + (String)object, exception);
        }
    }

    public static <T> Class<? extends T> subclassWithSynchronizedNativeMethods(Class<T> clazz) throws IOException {
        CallbackNativeImplementer callbackNativeImplementer = BridJ.getRuntimeByRuntimeClass(CRuntime.class).getCallbackNativeImplementer();
        return ASMUtils.createSubclassWithSynchronizedNativeMethodsAndNoStaticFields(clazz, callbackNativeImplementer);
    }

    @Deprecated
    public static boolean isCastingNativeObjectInCurrentThread() {
        return currentlyCastingNativeObject.get().peek() != CastingType.None;
    }

    @Deprecated
    public static boolean isCastingNativeObjectReturnTypeInCurrentThread() {
        return currentlyCastingNativeObject.get().peek() == CastingType.CastingNativeObjectReturnType;
    }

    public static synchronized <O extends NativeObject> void setJavaObjectFromNativePeer(long l2, O o2) {
        if (o2 == null) {
            registeredObjects.remove(l2);
            return;
        }
        registeredObjects.put(l2, o2);
    }

    public static synchronized Object getJavaObjectFromNativePeer(long l2) {
        return registeredObjects.get(l2);
    }

    private static <O extends NativeObject> O createNativeObjectFromPointer(Pointer<? super O> object, Type type, CastingType object2) {
        Stack<CastingType> stack = currentlyCastingNativeObject.get();
        stack.push((CastingType)((Object)object2));
        try {
            object2 = BridJ.getRuntime(Utils.getClass(type));
            object2 = BridJ.getTypeInfo((BridJRuntime)object2, type);
            object2 = object2.cast((Pointer)object);
            if (debug) {
                BridJ.info("Created native object from pointer " + object);
            }
            object = object2;
            return (O)object;
        }
        catch (Exception exception) {
            throw new RuntimeException("Failed to cast pointer to native object of type " + Utils.getClass(type).getName(), exception);
        }
        finally {
            stack.pop();
        }
    }

    public static <O extends NativeObject> void copyNativeObjectToAddress(O o2, Type type, Pointer<O> pointer) {
        BridJRuntime bridJRuntime = BridJ.getRuntime(Utils.getClass(type));
        BridJ.getTypeInfo(bridJRuntime, type).copyNativeObjectToAddress(o2, pointer);
    }

    public static <O extends NativeObject> O createNativeObjectFromPointer(Pointer<? super O> pointer, Type type) {
        return BridJ.createNativeObjectFromPointer(pointer, type, CastingType.CastingNativeObject);
    }

    public static <O extends NativeObject> O createNativeObjectFromReturnValuePointer(Pointer<? super O> pointer, Type type) {
        return BridJ.createNativeObjectFromPointer(pointer, type, CastingType.CastingNativeObjectReturnType);
    }

    public static synchronized <R extends BridJRuntime> R getRuntimeByRuntimeClass(Class<R> clazz) {
        BridJRuntime bridJRuntime = runtimes.get(clazz);
        if (bridJRuntime == null) {
            try {
                Class<R> clazz2 = clazz;
                bridJRuntime = (BridJRuntime)clazz2.newInstance();
                runtimes.put(clazz2, bridJRuntime);
            }
            catch (Exception exception) {
                throw new RuntimeException("Failed to instantiate runtime " + clazz.getName(), exception);
            }
        }
        return (R)bridJRuntime;
    }

    public static Class<? extends BridJRuntime> getRuntimeClass(Class<?> clazz) {
        clazz = (clazz = AnnotationUtils.getInheritableAnnotation(Runtime.class, clazz, new Annotation[0])) != null ? clazz.value() : CRuntime.class;
        return clazz;
    }

    public static BridJRuntime getRuntime(Class<?> clazz) {
        Map<Class<?>, BridJRuntime> map = classRuntimes;
        synchronized (map) {
            BridJRuntime bridJRuntime = classRuntimes.get(clazz);
            if (bridJRuntime == null) {
                Class<? extends BridJRuntime> clazz2 = BridJ.getRuntimeClass(clazz);
                bridJRuntime = BridJ.getRuntimeByRuntimeClass(clazz2);
                classRuntimes.put(clazz, bridJRuntime);
                if (veryVerbose) {
                    BridJ.info("Runtime for " + clazz.getName() + " : " + clazz2.getName());
                }
            }
            return bridJRuntime;
        }
    }

    public static BridJRuntime register(Class<?> classArray) {
        BridJRuntime bridJRuntime = BridJ.getRuntime(classArray);
        if (bridJRuntime == null) {
            for (Class<?> clazz : classArray.getClasses()) {
                BridJ.register(clazz);
            }
        } else {
            bridJRuntime.register((Type)classArray);
        }
        return bridJRuntime;
    }

    public static void unregister(Class<?> classArray) {
        BridJRuntime bridJRuntime = BridJ.getRuntime(classArray);
        if (bridJRuntime == null) {
            for (Class<?> clazz : classArray.getClasses()) {
                BridJ.register(clazz);
            }
            return;
        }
        bridJRuntime.unregister((Type)classArray);
    }

    static <T extends NativeObject> BridJRuntime.TypeInfo<T> getTypeInfo(BridJRuntime bridJRuntime, Type type) {
        Map<Type, BridJRuntime.TypeInfo<?>> map = typeInfos;
        synchronized (map) {
            BridJRuntime.TypeInfo<Object> typeInfo = typeInfos.get(type);
            if (typeInfo == null) {
                typeInfo = bridJRuntime.getTypeInfo(type);
                typeInfos.put(type, typeInfo);
            }
            return typeInfo;
        }
    }

    static void checkOptions() {
        HashSet<String> hashSet = new HashSet<String>();
        HashSet<String> object3 = new HashSet<String>();
        for (Switch switch_ : Switch.values()) {
            hashSet.add(switch_.propertyName);
            object3.add(switch_.envName);
        }
        boolean bl = false;
        for (String string : System.getenv().keySet()) {
            if (!string.startsWith("BRIDJ_") || object3.contains(string) || string.endsWith("_LIBRARY") || string.endsWith("_DEPENDENCIES")) continue;
            BridJ.error("Unknown environment variable : " + string + "=\"" + System.getenv(string) + "\"");
            bl = true;
        }
        Object object = System.getProperties().propertyNames();
        while (object.hasMoreElements()) {
            String string = (String)object.nextElement();
            if (!string.startsWith("bridj.") || hashSet.contains(string) || string.endsWith(".library") || string.endsWith(".dependencies")) continue;
            BridJ.error("Unknown property : " + string + "=\"" + System.getProperty(string) + "\"");
            bl = true;
        }
        if (bl) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Available options (ENVIRONMENT_VAR_NAME / javaPropertyName) :\n");
            for (Switch switch_ : Switch.values()) {
                ((StringBuilder)object).append(switch_.getFullDescription() + "\n");
            }
            BridJ.error(((StringBuilder)object).toString());
        }
    }

    public static void setMinLogLevel(Level level) {
        minLogLevelValue = level.intValue();
    }

    static boolean shouldLog(Level level) {
        return !quiet && (verbose || level.intValue() >= minLogLevelValue);
    }

    static synchronized Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger(BridJ.class.getName());
        }
        return logger;
    }

    public static boolean info(String string) {
        return BridJ.info(string, null);
    }

    public static boolean info(String string, Throwable throwable) {
        return BridJ.log(Level.INFO, string, throwable);
    }

    public static boolean debug(String string) {
        if (!debug) {
            return true;
        }
        return BridJ.info(string, null);
    }

    public static boolean error(String string) {
        return BridJ.error(string, null);
    }

    public static boolean error(String string, Throwable throwable) {
        return BridJ.log(Level.INFO, string, throwable);
    }

    public static boolean warning(String string) {
        return BridJ.warning(string, null);
    }

    public static boolean warning(String string, Throwable throwable) {
        return BridJ.log(Level.INFO, string, throwable);
    }

    private static boolean log(Level level, String string, Throwable throwable) {
        if (!BridJ.shouldLog(level)) {
            return true;
        }
        BridJ.getLogger().log(level, string, throwable);
        return true;
    }

    static void logCall(Method method) {
        BridJ.info("Calling method " + method);
    }

    public static synchronized NativeEntities getNativeEntities(AnnotatedElement object) throws IOException {
        if ((object = BridJ.getNativeLibrary((AnnotatedElement)object)) != null) {
            return ((NativeLibrary)object).getNativeEntities();
        }
        return BridJ.getOrphanEntities();
    }

    public static synchronized NativeLibrary getNativeLibrary(AnnotatedElement annotatedElement) throws IOException {
        Iterator iterator2;
        Object object = librariesByClass.get(annotatedElement);
        if (object == null && (iterator2 = BridJ.getLibrary(annotatedElement)) != null) {
            object = iterator2.value();
            String string = BridJ.getDependenciesEnv((String)object);
            Object object2 = libraryDependencies.get(object);
            iterator2 = Arrays.asList(string == null ? iterator2.dependencies() : string.split(","));
            if (object2 == null) {
                object2 = iterator2;
            } else {
                object2.addAll((Collection<String>)((Object)iterator2));
            }
            iterator2 = object2.iterator();
            while (iterator2.hasNext()) {
                string = (String)iterator2.next();
                if (verbose) {
                    BridJ.info("Trying to load dependency '" + string + "' of '" + (String)object + "'");
                }
                if ((object2 = BridJ.getNativeLibrary(string)) != null) continue;
                throw new RuntimeException("Failed to load dependency '" + string + "' of library '" + (String)object + "'");
            }
            if ((object = BridJ.getNativeLibrary((String)object)) != null) {
                librariesByClass.put(annotatedElement, (NativeLibrary)object);
            }
        }
        return object;
    }

    public static synchronized void releaseAll() {
        strongNativeObjects.clear();
        System.gc();
        for (NativeLibrary nativeLibrary : librariesByFile.values()) {
            nativeLibrary.release();
        }
        librariesByFile.clear();
        librariesByClass.clear();
        BridJ.getOrphanEntities().release();
        System.gc();
    }

    public static synchronized void releaseLibrary(String object) {
        if ((object = librariesFilesByName.remove(object)) != null) {
            BridJ.releaseLibrary((File)object);
        }
    }

    public static synchronized void releaseLibrary(File object) {
        if ((object = librariesByFile.remove(object)) != null) {
            ((NativeLibrary)object).release();
        }
    }

    public static synchronized void addLibraryPath(String string) {
        additionalPaths.add(string);
        nativeLibraryPaths = null;
    }

    private static void addPathsFromEnv(List<String> list2, String string) {
        String string2 = System.getenv(string);
        if (verbose) {
            BridJ.info("Environment var " + string + " = " + string2);
        }
        BridJ.addPaths(list2, string2);
    }

    private static void addPathsFromProperty(List<String> list2, String string) {
        String string2 = System.getProperty(string);
        if (verbose) {
            BridJ.info("Property " + string + " = " + string2);
        }
        BridJ.addPaths(list2, string2);
    }

    private static void addPaths(List<String> list2, String stringArray) {
        if (stringArray == null) {
            return;
        }
        if ((stringArray = stringArray.split(File.pathSeparator)).length == 0) {
            return;
        }
        if (stringArray.length == 1) {
            list2.add(stringArray[0]);
            return;
        }
        list2.addAll(Arrays.asList(stringArray));
    }

    static synchronized List<String> getNativeLibraryPaths() {
        if (nativeLibraryPaths == null) {
            String[] stringArray;
            nativeLibraryPaths = new ArrayList<String>();
            nativeLibraryPaths.addAll(additionalPaths);
            nativeLibraryPaths.add(null);
            nativeLibraryPaths.add(".");
            BridJ.addPathsFromEnv(nativeLibraryPaths, "LD_LIBRARY_PATH");
            BridJ.addPathsFromEnv(nativeLibraryPaths, "DYLD_LIBRARY_PATH");
            BridJ.addPathsFromEnv(nativeLibraryPaths, "PATH");
            BridJ.addPathsFromProperty(nativeLibraryPaths, "java.library.path");
            BridJ.addPathsFromProperty(nativeLibraryPaths, "sun.boot.library.path");
            BridJ.addPathsFromProperty(nativeLibraryPaths, "gnu.classpath.boot.library.path");
            Object object = new File(System.getProperty("java.home"));
            nativeLibraryPaths.add(new File((File)object, "bin").toString());
            if (Platform.isMacOSX()) {
                nativeLibraryPaths.add(new File((File)object, "../Libraries").toString());
            }
            if (Platform.isUnix()) {
                Object object2 = object = Platform.is64Bits() ? "64" : "32";
                if (Platform.isLinux()) {
                    String[] stringArray2;
                    if (Platform.isArm()) {
                        String[] stringArray3 = new String[2];
                        stringArray3[0] = "gnueabi";
                        stringArray2 = stringArray3;
                        stringArray3[1] = "gnueabihf";
                    } else {
                        String[] stringArray4 = new String[1];
                        stringArray2 = stringArray4;
                        stringArray4[0] = "gnu";
                    }
                    stringArray = stringArray2;
                    stringArray = stringArray2;
                    int n2 = stringArray2.length;
                    for (int i2 = 0; i2 < n2; ++i2) {
                        String string = stringArray[i2];
                        string = Platform.getMachine() + "-linux-" + string;
                        nativeLibraryPaths.add("/lib/" + string);
                        nativeLibraryPaths.add("/usr/lib/" + string);
                    }
                    nativeLibraryPaths.add("/usr/lib" + (String)object);
                    nativeLibraryPaths.add("/lib" + (String)object);
                } else if (Platform.isSolaris()) {
                    nativeLibraryPaths.add("/usr/lib/" + (String)object);
                    nativeLibraryPaths.add("/lib/" + (String)object);
                }
                nativeLibraryPaths.add("/usr/lib");
                nativeLibraryPaths.add("/lib");
                nativeLibraryPaths.add("/usr/local/lib");
            }
            object = nativeLibraryPaths.iterator();
            while (object.hasNext()) {
                stringArray = (String[])object.next();
                if (stringArray != null && new File((String)stringArray).isDirectory()) continue;
                object.remove();
            }
        }
        return nativeLibraryPaths;
    }

    public static synchronized void setNativeLibraryActualName(String string, String string2) {
        libraryActualNames.put(string, string2);
    }

    public static synchronized void addNativeLibraryAlias(String string, String string2) {
        List<String> list2 = libraryAliases.get(string);
        if (list2 == null) {
            list2 = new ArrayList<String>();
            libraryAliases.put(string, list2);
        }
        if (!list2.contains(string2)) {
            list2.add(string2);
        }
    }

    public static synchronized void addNativeLibraryDependencies(String stringArray, String ... stringArray2) {
        List<String> list2 = libraryDependencies.get(stringArray);
        if (list2 == null) {
            list2 = new ArrayList<String>();
            libraryDependencies.put((String)stringArray, list2);
        }
        stringArray = stringArray2;
        int n2 = stringArray2.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            String string = stringArray[i2];
            if (list2.contains(string)) continue;
            list2.add(string);
        }
    }

    static double parseVersion(String object) {
        object = numPat.matcher((CharSequence)object);
        double d2 = 0.0;
        double d3 = 1.0;
        while (((Matcher)object).find()) {
            d2 += (double)Integer.parseInt(((Matcher)object).group(1)) * d3;
            d3 /= 1000.0;
        }
        return d2;
    }

    static File findFileWithGreaterVersion(File file, String[] stringArray, String object) {
        object = Pattern.compile(Pattern.quote((String)object) + "((:?\\.\\d+)+)");
        double d2 = 0.0;
        String string = null;
        for (String string2 : stringArray) {
            Matcher matcher = ((Pattern)object).matcher(string2);
            if (!matcher.matches()) continue;
            double d3 = BridJ.parseVersion(matcher.group(1));
            if (string != null && !(d3 > d2)) continue;
            string = string2;
            d2 = d3;
        }
        if (string == null) {
            return null;
        }
        return new File(file, string);
    }

    public static File getNativeLibraryFile(String string) {
        if (string == null) {
            return null;
        }
        try {
            Map<String, File> map = nativeLibraryFiles;
            synchronized (map) {
                File file = nativeLibraryFiles.get(string);
                if (debug) {
                    BridJ.info("Library named '" + string + "' is associated to file '" + nativeLibraryFiles + "'", null);
                }
                if (file == null) {
                    String string2 = string;
                    file = BridJ.findNativeLibraryFile(string2);
                    nativeLibraryFiles.put(string2, file);
                }
                return file;
            }
        }
        catch (Throwable throwable) {
            BridJ.warning("Library not found : " + string, debug ? throwable : null);
            return null;
        }
    }

    public static void setNativeLibraryFile(String string, File file) {
        if (string == null) {
            return;
        }
        Map<String, File> map = nativeLibraryFiles;
        synchronized (map) {
            nativeLibraryFiles.put(string, file);
            return;
        }
    }

    private static String getLibraryEnv(String string) {
        String string2 = System.getenv("BRIDJ_" + string.toUpperCase() + "_LIBRARY");
        if (string2 == null) {
            string2 = System.getProperty("bridj." + string + ".library");
        }
        return string2;
    }

    private static String getDependenciesEnv(String string) {
        String string2 = System.getenv("BRIDJ_" + string.toUpperCase() + "_DEPENDENCIES");
        if (string2 == null) {
            string2 = System.getProperty("bridj." + string + ".dependencies");
        }
        return string2;
    }

    static File findNativeLibraryFile(String string) throws IOException {
        String string2 = libraryActualNames.get(string);
        List<String> list2 = libraryAliases.get(string);
        ArrayList<String> arrayList = new ArrayList<String>();
        if (Platform.isWindows() && (string.equals("c") || string.equals("m"))) {
            arrayList.add("msvcrt");
        }
        if (list2 != null) {
            arrayList.addAll(list2);
        }
        arrayList.add((String)(string2 == null ? string : string2));
        list2 = BridJ.getNativeLibraryPaths();
        if (debug) {
            BridJ.info("Looking for library '" + string + "' " + (string2 != null ? "('" + (String)string2 + "') " : "") + "in paths " + list2, null);
        }
        for (String string3 : arrayList) {
            Object object;
            Object object2;
            Object object3 = BridJ.getLibraryEnv(string3);
            if (object3 != null && ((File)(object3 = new File((String)object3))).exists()) {
                try {
                    return ((File)object3).getCanonicalFile();
                }
                catch (IOException iOException) {
                    BridJ.error(null, iOException);
                }
            }
            object3 = Platform.getPossibleFileNames(string3);
            if (debug) {
                BridJ.info("Possible file names for library '" + string + "' with name '" + string3 + "': " + object3, null);
            }
            Object object4 = list2.iterator();
            while (object4.hasNext()) {
                Object object5 = object4.next();
                object5 = object5 == null ? null : new File((String)object5);
                object2 = new File(string3);
                if (!((File)object2).isFile() && object5 != null) {
                    Object object6;
                    object = object3.iterator();
                    while (object.hasNext() && !((File)(object2 = new File((File)object5, (String)(object6 = (String)object.next())))).isFile()) {
                    }
                    if (!((File)object2).isFile() && Platform.isLinux() && (object = ((File)object5).list()) != null) {
                        object6 = object3.iterator();
                        while (object6.hasNext()) {
                            String string4 = (String)object6.next();
                            File file = BridJ.findFileWithGreaterVersion((File)object5, (String[])object, string4);
                            if (file == null || !((File)(object2 = file)).isFile()) continue;
                            if (!verbose) break;
                            BridJ.info("File '" + string4 + "' was not found, used versioned file '" + object2 + "' instead.");
                            break;
                        }
                    }
                }
                if (!((File)object2).isFile()) continue;
                try {
                    return ((File)object2).getCanonicalFile();
                }
                catch (IOException iOException) {
                    BridJ.error(null, iOException);
                }
            }
            if (Platform.isMacOSX()) {
                object4 = new String[]{"/System/Library/Frameworks", "/System/Library/Frameworks/ApplicationServices.framework/Frameworks", new File(System.getProperty("user.home"), "Library/Frameworks").toString()};
                int cfr_ignored_0 = ((String[])object4).length;
                for (int i2 = 0; i2 < 3; ++i2) {
                    object2 = object4[i2];
                    try {
                        object = new File(new File((String)object2, string3 + ".framework"), string3);
                        if (!((File)object).isFile()) continue;
                        return ((File)object).getCanonicalFile();
                    }
                    catch (IOException iOException) {
                        object = iOException;
                        iOException.printStackTrace();
                        return null;
                    }
                }
            }
            if ((object4 = Platform.isAndroid() ? new File("lib" + string3 + ".so") : Platform.extractEmbeddedLibraryResource(string3)) == null || !object4.isFile()) continue;
            return object4;
        }
        throw new FileNotFoundException(StringUtils.implode(arrayList, (Object)", "));
    }

    public static boolean isDirectModeEnabled() {
        if (directModeEnabled == null) {
            directModeEnabled = Switch.Direct.enabled && !logCalls && !protectedMode;
            if (veryVerbose) {
                BridJ.info("directModeEnabled = " + directModeEnabled);
            }
        }
        return directModeEnabled;
    }

    static void setDirectModeEnabled(boolean bl) {
        directModeEnabled = bl;
    }

    public static synchronized NativeLibrary getNativeLibrary(String string) throws IOException {
        if (string == null) {
            return null;
        }
        Object object = libHandles.get(string);
        if (object != null) {
            return object;
        }
        object = BridJ.getNativeLibraryFile(string);
        return BridJ.getNativeLibrary(string, (File)object);
    }

    public static NativeLibrary getNativeLibrary(String string, File file) throws IOException {
        NativeLibrary nativeLibrary = NativeLibrary.load(file == null ? string : file.toString());
        if (nativeLibrary == null && (nativeLibrary = PlatformSupport.getInstance().loadNativeLibrary(string)) == null) {
            boolean bl = Platform.isWindows();
            if ("c".equals(string) || "m".equals(string) && bl) {
                nativeLibrary = new NativeLibrary(bl ? "msvcrt" : null, 0L, 0L);
            }
        }
        if (nativeLibrary == null) {
            if (file != null && file.exists()) {
                throw new RuntimeException("Library '" + string + "' was not loaded successfully from file '" + file + "'");
            }
            throw new FileNotFoundException("Library '" + string + "' was not found in path '" + BridJ.getNativeLibraryPaths() + "'" + (file != null && file.exists() ? " (failed to load " + file + ")" : ""));
        }
        if (verbose) {
            BridJ.info("Loaded library '" + string + "' from '" + file + "'", null);
        }
        libHandles.put(string, nativeLibrary);
        return nativeLibrary;
    }

    public static String getNativeLibraryName(AnnotatedElement object) {
        if ((object = BridJ.getLibrary((AnnotatedElement)object)) == null) {
            return null;
        }
        return object.value();
    }

    static Library getLibrary(AnnotatedElement annotatedElement) {
        return AnnotationUtils.getInheritableAnnotation(Library.class, annotatedElement, new Annotation[0]);
    }

    public static Demangler.Symbol getSymbolByAddress(long l2) {
        for (NativeLibrary nativeLibrary : libHandles.values()) {
            Demangler.Symbol object = nativeLibrary.getSymbol(l2);
            if (object == null) continue;
            return object;
        }
        return null;
    }

    public static void setOrphanEntities(NativeEntities nativeEntities) {
        orphanEntities = nativeEntities;
    }

    public static NativeEntities getOrphanEntities() {
        return orphanEntities;
    }

    static void initialize(NativeObject nativeObject) {
        BridJRuntime.TypeInfo<NativeObject> typeInfo = nativeObject.getClass();
        typeInfo = BridJ.getRuntime(typeInfo);
        Type type = typeInfo.getType(nativeObject);
        nativeObject.typeInfo = typeInfo = BridJ.getTypeInfo((BridJRuntime)((Object)typeInfo), type);
        typeInfo.initialize(nativeObject);
    }

    static void initialize(NativeObject nativeObject, Pointer pointer, Object ... object) {
        Class<?> clazz = nativeObject.getClass();
        BridJRuntime bridJRuntime = BridJ.getRuntime(clazz);
        object = bridJRuntime.getType(clazz, (Object[])object, null);
        nativeObject.typeInfo = object = BridJ.getTypeInfo(bridJRuntime, (Type)object);
        object.initialize(nativeObject, pointer);
    }

    static void initialize(NativeObject nativeObject, int n2, Object[] objectArray) {
        Object object = nativeObject.getClass();
        BridJRuntime bridJRuntime = BridJ.getRuntime(object);
        int[] nArray = new int[1];
        object = bridJRuntime.getType((Class<?>)object, objectArray, nArray);
        int n3 = nArray[0];
        objectArray = Utils.takeRight(objectArray, objectArray.length - n3);
        nativeObject.typeInfo = object = BridJ.getTypeInfo(bridJRuntime, object);
        object.initialize(nativeObject, n2, objectArray);
    }

    static <T extends NativeObject> T clone(T t2) throws CloneNotSupportedException {
        return t2.typeInfo.clone(t2);
    }

    public static <T extends NativeObject> T readFromNative(T t2) {
        t2.typeInfo.readFromNative(t2);
        return t2;
    }

    public static <T extends NativeObject> T writeToNative(T t2) {
        t2.typeInfo.writeToNative(t2);
        return t2;
    }

    public static String describe(NativeObject nativeObject) {
        return nativeObject.typeInfo.describe(nativeObject);
    }

    public static String describe(Type type) {
        Object object = BridJ.getRuntime(Utils.getClass(type));
        if ((object = BridJ.getTypeInfo((BridJRuntime)object, type)) == null) {
            return Utils.toString(type);
        }
        return object.describe();
    }

    public static void main(String[] stringArray) {
        ArrayList<NativeLibrary> arrayList = new ArrayList<NativeLibrary>();
        try {
            File file = new File(".");
            int n2 = stringArray.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                Iterator<Demangler.Symbol> iterator2;
                Object object = stringArray[i2];
                if (((String)object).equals("-d")) {
                    file = new File(stringArray[++i2]);
                    continue;
                }
                try {
                    iterator2 = BridJ.getNativeLibrary((String)object);
                    arrayList.add((NativeLibrary)((Object)iterator2));
                    object = new PrintWriter(new File(file, new File((String)object).getName() + ".symbols.txt"));
                    for (Demangler.Symbol symbol : ((NativeLibrary)((Object)iterator2)).getSymbols()) {
                        ((PrintWriter)object).print(symbol.getSymbol());
                        ((PrintWriter)object).print(" // ");
                        try {
                            Demangler.MemberRef memberRef = symbol.getParsedRef();
                            ((PrintWriter)object).print(" // " + memberRef);
                        }
                        catch (Throwable throwable) {
                            ((PrintWriter)object).print("?");
                        }
                        ((PrintWriter)object).println();
                    }
                    ((PrintWriter)object).close();
                    continue;
                }
                catch (Throwable throwable) {
                    iterator2 = throwable;
                    throwable.printStackTrace();
                }
            }
            PrintWriter printWriter = new PrintWriter(new File(file, "out.h"));
            HeadersReconstructor.reconstructHeaders(arrayList, printWriter);
            printWriter.close();
            return;
        }
        catch (Exception exception) {
            Exception exception2 = exception;
            exception.printStackTrace();
            System.exit(1);
            return;
        }
    }

    static {
        BridJ.checkOptions();
        debug = Switch.Debug.enabled;
        debugNeverFree = Switch.DebugNeverFree.enabled;
        debugPointers = Switch.DebugPointers.enabled;
        debugPointerReleases = Switch.DebugPointerReleases.enabled || debugPointers;
        veryVerbose = Switch.VeryVerbose.enabled;
        verbose = debug || veryVerbose || Switch.Verbose.enabled;
        quiet = Switch.Quiet.enabled;
        logCalls = Switch.LogCalls.enabled;
        warnStructFields = Switch.LogCalls.enabled;
        protectedMode = Switch.Protected.enabled;
        enableDestructors = Switch.Destructors.enabled;
        alignDoubles = Switch.AlignDouble.enabled;
        cachePointers = Switch.CachePointers.enabled;
        minLogLevelValue = (verbose ? Level.WARNING : Level.INFO).intValue();
        libHandles = new HashMap<String, NativeLibrary>();
        additionalPaths = new ArrayList<String>();
        libraryActualNames = new HashMap<String, String>();
        libraryAliases = new HashMap<String, List<String>>();
        libraryDependencies = new HashMap<String, List<String>>();
        numPat = Pattern.compile("\\b(\\d+)\\b");
        nativeLibraryFiles = new HashMap<String, File>();
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static enum Switch {
        Debug("bridj.debug", "BRIDJ_DEBUG", false, "Debug mode (implies high verbosity)"),
        DebugNeverFree("bridj.debug.neverFree", "BRIDJ_DEBUG_NEVER_FREE", false, "Never free allocated pointers (deprecated)"),
        DebugPointers("bridj.debug.pointers", "BRIDJ_DEBUG_POINTERS", false, "Trace pointer allocations & deallocations (to debug memory issues)"),
        DebugPointerReleases("bridj.debug.pointer.releases", "BRIDJ_DEBUG_POINTER_RELEASES", false, "Prevent double releases of pointers and keep the trace of their first release (to debug memory issues)"),
        VeryVerbose("bridj.veryVerbose", "BRIDJ_VERY_VERBOSE", false, "Highly verbose mode"),
        Verbose("bridj.verbose", "BRIDJ_VERBOSE", false, "Verbose mode"),
        Quiet("bridj.quiet", "BRIDJ_QUIET", false, "Quiet mode"),
        CachePointers("bridj.cache.pointers", "BRIDJ_CACHE_POINTERS", true, "Cache last recently used pointers in each thread"),
        AlignDouble("bridj.alignDouble", "BRIDJ_ALIGN_DOUBLE", false, "Align doubles on 8 bytes boundaries even on Linux 32 bits (see -malign-double GCC option)."),
        LogCalls("bridj.logCalls", "BRIDJ_LOG_CALLS", false, "Log each native call performed (or call from native to Java callback)"),
        WarnStructFields("bridj.warnStructFields", "BRIDJ_WARN_STRUCT_FIELDS", true, "Warn when struct fields are implemented with Java fields instead of methods"),
        Protected("bridj.protected", "BRIDJ_PROTECTED", false, "Protect all native calls (including memory accesses) against native crashes (disables assembly optimizations and adds quite some overhead)."),
        Destructors("bridj.destructors", "BRIDJ_DESTRUCTORS", true, "Enable destructors (in languages that support them, such as C++)"),
        Direct("bridj.direct", "BRIDJ_DIRECT", true, "Direct mode (uses optimized assembler glue when possible to speed up calls)"),
        StructsByValue("bridj.structsByValue", "BRIDJ_STRUCT_BY_VALUE", false, "Enable experimental support for structs-by-value arguments and return values for C/C++ functions and methods.");

        public final boolean enabled;
        public final boolean enabledByDefault;
        public final String propertyName;
        public final String envName;
        public final String description;

        private Switch(String string2, String string3, boolean bl, String string4) {
            this.enabled = bl ? !"false".equals(System.getProperty(string2)) && !"0".equals(System.getenv(string3)) : "true".equals(System.getProperty(string2)) || "1".equals(System.getenv(string3));
            this.enabledByDefault = bl;
            this.propertyName = string2;
            this.envName = string3;
            this.description = string4;
        }

        public final String getFullDescription() {
            return this.envName + " / " + this.propertyName + " (" + (this.enabledByDefault ? "enabled" : "disabled") + " by default) :\n\t" + this.description.replaceAll("\n", "\n\t");
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static enum CastingType {
        None,
        CastingNativeObject,
        CastingNativeObjectReturnType;

    }
}

