/*
 * Decompiled with CFR 0.152.
 */
package org.bridj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bridj.BridJ;
import org.bridj.JNI;
import org.bridj.NativeEntities;
import org.bridj.Platform;
import org.bridj.Pointer;
import org.bridj.ann.Name;
import org.bridj.ann.Symbol;
import org.bridj.demangling.Demangler;
import org.bridj.demangling.GCC4Demangler;
import org.bridj.demangling.VC9Demangler;
import org.bridj.util.AnnotationUtils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class NativeLibrary {
    volatile long handle;
    volatile long symbols;
    String path;
    final File canonicalFile;
    NativeEntities nativeEntities = new NativeEntities();
    Map<Long, Demangler.Symbol> addrToName;
    Map<String, Demangler.Symbol> nameToSym;

    protected NativeLibrary(String string, long l2, long l3) throws IOException {
        this.path = string;
        this.handle = l2;
        this.symbols = l3;
        this.canonicalFile = string == null ? null : new File(string).getCanonicalFile();
        Platform.addNativeLibrary(this);
    }

    long getSymbolsHandle() {
        return this.symbols;
    }

    NativeEntities getNativeEntities() {
        return this.nativeEntities;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static String followGNULDScript(String string) {
        try {
            Reader reader = new FileReader(string);
            try {
                String string2;
                char c2;
                while ((c2 = (char)reader.read()) == ' ' || c2 == '\t' || c2 == '\n') {
                }
                if (c2 != '/') return string;
                if (reader.read() != 42) return string;
                Object object = new BufferedReader(reader);
                reader = object;
                Serializable serializable = new StringBuilder("/*");
                while ((string2 = ((BufferedReader)object).readLine()) != null) {
                    serializable.append(string2).append('\n');
                }
                object = serializable.toString();
                serializable = Pattern.compile("GROUP\\s*\\(\\s*([^\\s)]+)[^)]*\\)");
                object = ((Pattern)serializable).matcher((CharSequence)object);
                if (((Matcher)object).find()) {
                    object = ((Matcher)object).group(1);
                    if (!BridJ.verbose) return object;
                    BridJ.info("Parsed LD script '" + string + "', found absolute reference to '" + (String)object + "'");
                    return object;
                }
                BridJ.error("Failed to parse LD script '" + string + "' !");
                return string;
            }
            finally {
                reader.close();
            }
        }
        catch (Throwable throwable) {
            BridJ.error("Unexpected error: " + throwable, throwable);
        }
        return string;
    }

    public static NativeLibrary load(String string) throws IOException {
        long l2;
        File file = new File(string);
        boolean bl = file.exists();
        if (file.isAbsolute() && !bl) {
            return null;
        }
        if (Platform.isUnix() && bl) {
            string = NativeLibrary.followGNULDScript(string);
        }
        if ((l2 = JNI.loadLibrary(string)) == 0L) {
            return null;
        }
        long l3 = JNI.loadLibrarySymbols(string);
        return new NativeLibrary(string, l2, l3);
    }

    long getHandle() {
        if (this.path != null && this.handle == 0L) {
            throw new RuntimeException("Library was released and cannot be used anymore");
        }
        return this.handle;
    }

    protected void finalize() throws Throwable {
        this.release();
    }

    public synchronized void release() {
        if (this.handle == 0L) {
            return;
        }
        if (BridJ.verbose) {
            BridJ.info("Releasing library '" + this.path + "'");
        }
        this.nativeEntities.release();
        JNI.freeLibrarySymbols(this.symbols);
        JNI.freeLibrary(this.handle);
        this.handle = 0L;
        if (this.canonicalFile != null && Platform.temporaryExtractedLibraryCanonicalFiles.remove(this.canonicalFile)) {
            if (this.canonicalFile.delete()) {
                if (BridJ.verbose) {
                    BridJ.info("Deleted temporary library file '" + this.canonicalFile + "'");
                    return;
                }
            } else {
                BridJ.error("Failed to delete temporary library file '" + this.canonicalFile + "'");
            }
        }
    }

    public Pointer<?> getSymbolPointer(String string) {
        return Pointer.pointerToAddress(this.getSymbolAddress(string));
    }

    public long getSymbolAddress(String string) {
        Demangler.Symbol symbol;
        if (this.nameToSym != null && (symbol = this.nameToSym.get(string)) != null) {
            return symbol.getAddress();
        }
        long l2 = JNI.findSymbolInLibrary(this.getHandle(), string);
        if (l2 == 0L) {
            l2 = JNI.findSymbolInLibrary(this.getHandle(), "_" + string);
        }
        return l2;
    }

    /*
     * WARNING - void declaration
     */
    public synchronized Demangler.Symbol getSymbol(AnnotatedElement annotatedElement) throws FileNotFoundException {
        Object object = AnnotationUtils.getAnnotation(Symbol.class, annotatedElement, new Annotation[0]);
        Object object22 = null;
        Name object32 = annotatedElement.getAnnotation(Name.class);
        if (object32 != null) {
            object22 = object32.value();
        } else if (annotatedElement instanceof Member) {
            object22 = ((Member)((Object)annotatedElement)).getName();
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        if (object != null) {
            arrayList.addAll(Arrays.asList(object.value()));
        }
        if (object22 != null) {
            arrayList.add((String)object22);
        }
        for (Object object22 : arrayList) {
            void var4_11;
            void var4_9;
            Demangler.Symbol symbol = this.getSymbol((String)object22);
            if (symbol == null) {
                Demangler.Symbol symbol2 = this.getSymbol("_" + (String)object22);
            }
            if (var4_9 == null) {
                Demangler.Symbol symbol3 = this.getSymbol((String)object22 + (Platform.useUnicodeVersionOfWindowsAPIs ? "W" : "A"));
            }
            if (var4_11 == null) continue;
            return var4_11;
        }
        if (annotatedElement instanceof Method) {
            object = (Method)annotatedElement;
            for (Demangler.Symbol symbol : this.getSymbols()) {
                if (!symbol.matches((Method)object)) continue;
                return symbol;
            }
        }
        return null;
    }

    public boolean isMSVC() {
        return Platform.isWindows();
    }

    public Demangler.Symbol getFirstMatchingSymbol(SymbolAccepter symbolAccepter) {
        for (Demangler.Symbol symbol : this.getSymbols()) {
            if (!symbolAccepter.accept(symbol)) continue;
            return symbol;
        }
        return null;
    }

    public Collection<Demangler.Symbol> getSymbols() {
        block3: {
            try {
                this.scanSymbols();
            }
            catch (Exception exception) {
                if ($assertionsDisabled || BridJ.error("Failed to scan symbols of library '" + this.path + "'", exception)) break block3;
                throw new AssertionError();
            }
        }
        if (this.nameToSym == null) {
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableCollection(this.nameToSym.values());
    }

    public String getSymbolName(long l2) {
        if (this.addrToName == null && this.getSymbolsHandle() != 0L) {
            return JNI.findSymbolName(this.getHandle(), this.getSymbolsHandle(), l2);
        }
        Demangler.Symbol symbol = this.getSymbol(l2);
        if (symbol == null) {
            return null;
        }
        return symbol.getSymbol();
    }

    public Demangler.Symbol getSymbol(long l2) {
        try {
            this.scanSymbols();
            Demangler.Symbol symbol = this.addrToName.get(l2);
            return symbol;
        }
        catch (Exception exception) {
            throw new RuntimeException("Failed to get name of address " + l2, exception);
        }
    }

    public Demangler.Symbol getSymbol(String string) {
        try {
            long l2;
            if (this.nameToSym == null && (l2 = JNI.findSymbolInLibrary(this.getHandle(), string)) != 0L) {
                Demangler.Symbol symbol = new Demangler.Symbol(string, this);
                symbol.setAddress(l2);
                return symbol;
            }
            this.scanSymbols();
            if (this.nameToSym == null) {
                return null;
            }
            Demangler.Symbol symbol = this.nameToSym.get(string);
            if (this.addrToName == null && symbol == null && (l2 = JNI.findSymbolInLibrary(this.getHandle(), string)) != 0L) {
                symbol = new Demangler.Symbol(string, this);
                symbol.setAddress(l2);
                this.nameToSym.put(string, symbol);
            }
            return symbol;
        }
        catch (Exception exception) {
            Exception exception2 = exception;
            exception.printStackTrace();
            return null;
        }
    }

    void scanSymbols() throws Exception {
        if (this.addrToName != null) {
            return;
        }
        this.nameToSym = new HashMap<String, Demangler.Symbol>();
        Object object = null;
        object = JNI.getLibrarySymbols(this.getHandle(), this.getSymbolsHandle());
        if (object == null) {
            return;
        }
        this.addrToName = new HashMap<Long, Demangler.Symbol>();
        Platform.is64Bits();
        for (Object object2 : object) {
            Object object3;
            if (object2 == null) continue;
            long l2 = JNI.findSymbolInLibrary(this.getHandle(), (String)object2);
            if (l2 == 0L && ((String)object2).startsWith("_")) {
                object3 = ((String)object2).substring(1);
                l2 = JNI.findSymbolInLibrary(this.getHandle(), (String)object3);
                if (l2 == 0L) {
                    object3 = "_" + (String)object2;
                    l2 = JNI.findSymbolInLibrary(this.getHandle(), (String)object3);
                }
                if (l2 != 0L) {
                    object2 = object3;
                }
            }
            if (l2 == 0L) {
                if (!BridJ.verbose) continue;
                BridJ.warning("Symbol '" + (String)object2 + "' not found.");
                continue;
            }
            object3 = new Demangler.Symbol((String)object2, this);
            ((Demangler.Symbol)object3).setAddress(l2);
            this.addrToName.put(l2, (Demangler.Symbol)object3);
            this.nameToSym.put((String)object2, (Demangler.Symbol)object3);
        }
        if (BridJ.debug) {
            BridJ.info("Found " + this.nameToSym.size() + " symbols in '" + this.path + "' :");
            for (Demangler.Symbol symbol : this.nameToSym.values()) {
                BridJ.info("DEBUG(BridJ): library=\"" + this.path + "\", symbol=\"" + symbol.getSymbol() + "\", address=" + Long.toHexString(symbol.getAddress()) + ", demangled=\"" + symbol.getParsedRef() + "\"");
            }
        }
    }

    public Demangler.MemberRef parseSymbol(String string) throws Demangler.DemanglingException {
        if ("__cxa_pure_virtual".equals(string)) {
            return null;
        }
        if (Platform.isWindows()) {
            try {
                Demangler.MemberRef memberRef = ((Demangler)new VC9Demangler(this, string)).parseSymbol();
                if (memberRef != null) {
                    return memberRef;
                }
            }
            catch (Throwable throwable) {}
        }
        return ((Demangler)new GCC4Demangler(this, string)).parseSymbol();
    }

    public static interface SymbolAccepter {
        public boolean accept(Demangler.Symbol var1);
    }
}

