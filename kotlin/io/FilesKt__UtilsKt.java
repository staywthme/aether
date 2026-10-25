/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.io.FileAlreadyExistsException;
import kotlin.io.FilePathComponents;
import kotlin.io.FileSystemException;
import kotlin.io.FilesKt;
import kotlin.io.FilesKt__FileTreeWalkKt;
import kotlin.io.FilesKt__UtilsKt;
import kotlin.io.NoSuchFileException;
import kotlin.io.OnErrorAction;
import kotlin.io.TerminateException;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000<\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\u001a*\u0010\t\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002H\u0007\u001a*\u0010\r\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002H\u0007\u001a8\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\u001a\b\u0002\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00150\u0013\u001a&\u0010\u0016\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\b\b\u0002\u0010\u0017\u001a\u00020\u0018\u001a\n\u0010\u0019\u001a\u00020\u000f*\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\n\u0010\u001c\u001a\u00020\u0002*\u00020\u0002\u001a\u001d\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00020\u001d*\b\u0012\u0004\u0012\u00020\u00020\u001dH\u0002\u00a2\u0006\u0002\b\u001e\u001a\u0011\u0010\u001c\u001a\u00020\u001f*\u00020\u001fH\u0002\u00a2\u0006\u0002\b\u001e\u001a\u0012\u0010 \u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0014\u0010\"\u001a\u0004\u0018\u00010\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010#\u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\u0012\u0010(\u001a\u00020\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u001b\u0010)\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002H\u0002\u00a2\u0006\u0002\b*\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0004\"\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\u0004\u00a8\u0006+"}, d2={"extension", "", "Ljava/io/File;", "getExtension", "(Ljava/io/File;)Ljava/lang/String;", "invariantSeparatorsPath", "getInvariantSeparatorsPath", "nameWithoutExtension", "getNameWithoutExtension", "createTempDir", "prefix", "suffix", "directory", "createTempFile", "copyRecursively", "", "target", "overwrite", "onError", "Lkotlin/Function2;", "Ljava/io/IOException;", "Lkotlin/io/OnErrorAction;", "copyTo", "bufferSize", "", "deleteRecursively", "endsWith", "other", "normalize", "", "normalize$FilesKt__UtilsKt", "Lkotlin/io/FilePathComponents;", "relativeTo", "base", "relativeToOrNull", "relativeToOrSelf", "resolve", "relative", "resolveSibling", "startsWith", "toRelativeString", "toRelativeStringOrNull", "toRelativeStringOrNull$FilesKt__UtilsKt", "kotlin-stdlib"}, xs="kotlin/io/FilesKt")
@SourceDebugExtension(value={"SMAP\nUtils.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Utils.kt\nkotlin/io/FilesKt__UtilsKt\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 _Sequences.kt\nkotlin/sequences/SequencesKt___SequencesKt\n*L\n1#1,473:1\n1#2:474\n1266#3,3:475\n*S KotlinDebug\n*F\n+ 1 Utils.kt\nkotlin/io/FilesKt__UtilsKt\n*L\n347#1:475,3\n*E\n"})
class FilesKt__UtilsKt
extends FilesKt__FileTreeWalkKt {
    @Deprecated(message="Avoid creating temporary directories in the default temp location with this function due to too wide permissions on the newly created directory. Use kotlin.io.path.createTempDirectory instead.")
    @NotNull
    public static final File createTempDir(@NotNull String object, @Nullable String string, @Nullable File file) {
        Intrinsics.checkNotNullParameter(object, "");
        object = File.createTempFile((String)object, string, file);
        ((File)object).delete();
        if (((File)object).mkdir()) {
            Intrinsics.checkNotNullExpressionValue(object, "");
            return object;
        }
        throw new IOException("Unable to create temporary directory " + object + '.');
    }

    public static /* synthetic */ File createTempDir$default(String string, String string2, File file, int n2, Object object) {
        if ((n2 & 1) != 0) {
            string = "tmp";
        }
        if ((n2 & 2) != 0) {
            string2 = null;
        }
        if ((n2 & 4) != 0) {
            file = null;
        }
        return FilesKt.createTempDir(string, string2, file);
    }

    @Deprecated(message="Avoid creating temporary files in the default temp location with this function due to too wide permissions on the newly created file. Use kotlin.io.path.createTempFile instead or resort to java.io.File.createTempFile.")
    @NotNull
    public static final File createTempFile(@NotNull String string, @Nullable String string2, @Nullable File file) {
        Intrinsics.checkNotNullParameter(string, "");
        File file2 = File.createTempFile(string, string2, file);
        Intrinsics.checkNotNullExpressionValue(file2, "");
        return file2;
    }

    public static /* synthetic */ File createTempFile$default(String string, String string2, File file, int n2, Object object) {
        if ((n2 & 1) != 0) {
            string = "tmp";
        }
        if ((n2 & 2) != 0) {
            string2 = null;
        }
        if ((n2 & 4) != 0) {
            file = null;
        }
        return FilesKt.createTempFile(string, string2, file);
    }

    @NotNull
    public static final String getExtension(@NotNull File file) {
        Intrinsics.checkNotNullParameter(file, "");
        String string = file.getName();
        Intrinsics.checkNotNullExpressionValue(string, "");
        return StringsKt.substringAfterLast(string, '.', "");
    }

    @NotNull
    public static final String getInvariantSeparatorsPath(@NotNull File file) {
        Intrinsics.checkNotNullParameter(file, "");
        if (File.separatorChar != '/') {
            String string = file.getPath();
            Intrinsics.checkNotNullExpressionValue(string, "");
            return StringsKt.replace$default(string, File.separatorChar, '/', false, 4, null);
        }
        String string = file.getPath();
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    @NotNull
    public static final String getNameWithoutExtension(@NotNull File file) {
        Intrinsics.checkNotNullParameter(file, "");
        String string = file.getName();
        Intrinsics.checkNotNullExpressionValue(string, "");
        return StringsKt.substringBeforeLast$default(string, ".", null, 2, null);
    }

    @NotNull
    public static final String toRelativeString(@NotNull File file, @NotNull File file2) {
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(file2, "");
        String string = FilesKt__UtilsKt.toRelativeStringOrNull$FilesKt__UtilsKt(file, file2);
        if (string == null) {
            throw new IllegalArgumentException("this and base files have different roots: " + file + " and " + file2 + '.');
        }
        return string;
    }

    @NotNull
    public static final File relativeTo(@NotNull File file, @NotNull File file2) {
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(file2, "");
        return new File(FilesKt.toRelativeString(file, file2));
    }

    @NotNull
    public static final File relativeToOrSelf(@NotNull File object, @NotNull File file) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(file, "");
        String string = FilesKt__UtilsKt.toRelativeStringOrNull$FilesKt__UtilsKt((File)object, file);
        if (string != null) {
            object = string;
            object = string;
            return new File((String)object);
        }
        return object;
    }

    @Nullable
    public static final File relativeToOrNull(@NotNull File object, @NotNull File file) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(file, "");
        String string = FilesKt__UtilsKt.toRelativeStringOrNull$FilesKt__UtilsKt((File)object, file);
        if (string != null) {
            object = string;
            object = string;
            return new File((String)object);
        }
        return null;
    }

    private static final String toRelativeStringOrNull$FilesKt__UtilsKt(File object, File object2) {
        int n2;
        object = FilesKt__UtilsKt.normalize$FilesKt__UtilsKt(FilesKt.toComponents((File)object));
        object2 = FilesKt__UtilsKt.normalize$FilesKt__UtilsKt(FilesKt.toComponents((File)object2));
        if (!Intrinsics.areEqual(((FilePathComponents)object).getRoot(), ((FilePathComponents)object2).getRoot())) {
            return null;
        }
        int n3 = ((FilePathComponents)object2).getSize();
        int n4 = ((FilePathComponents)object).getSize();
        int n5 = Math.min(n4, n3);
        for (n2 = 0; n2 < n5 && Intrinsics.areEqual(((FilePathComponents)object).getSegments().get(n2), ((FilePathComponents)object2).getSegments().get(n2)); ++n2) {
        }
        StringBuilder stringBuilder = new StringBuilder();
        int n6 = n3 - 1;
        if (n2 <= n6) {
            while (true) {
                if (Intrinsics.areEqual(((FilePathComponents)object2).getSegments().get(n6).getName(), "..")) {
                    return null;
                }
                stringBuilder.append("..");
                if (n6 != n2) {
                    stringBuilder.append(File.separatorChar);
                }
                if (n6 == n2) break;
                --n6;
            }
        }
        if (n2 < n4) {
            if (n2 < n3) {
                stringBuilder.append(File.separatorChar);
            }
            Iterable iterable = CollectionsKt.drop((Iterable)((FilePathComponents)object).getSegments(), n2);
            Appendable appendable = stringBuilder;
            String string = File.separator;
            Intrinsics.checkNotNullExpressionValue(string, "");
            CollectionsKt.joinTo$default(iterable, appendable, string, null, null, 0, null, null, 124, null);
        }
        return stringBuilder.toString();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @NotNull
    public static final File copyTo(@NotNull File object, @NotNull File file, boolean bl, int n2) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(file, "");
        if (!((File)object).exists()) {
            throw new NoSuchFileException((File)object, null, "The source file doesn't exist.", 2, null);
        }
        if (file.exists()) {
            if (!bl) {
                throw new FileAlreadyExistsException((File)object, file, "The destination file already exists.");
            }
            if (!file.delete()) {
                throw new FileAlreadyExistsException((File)object, file, "Tried to overwrite the destination, but failed to delete it.");
            }
        }
        if (((File)object).isDirectory()) {
            if (file.mkdirs()) return file;
            throw new FileSystemException((File)object, file, "Failed to create target directory.");
        }
        File file2 = file.getParentFile();
        if (file2 != null) {
            file2.mkdirs();
        }
        object = new FileInputStream((File)object);
        Throwable throwable = null;
        try {
            FileInputStream fileInputStream = (FileInputStream)object;
            Closeable closeable = new FileOutputStream(file);
            Throwable throwable2 = null;
            try {
                FileOutputStream fileOutputStream = (FileOutputStream)closeable;
                ByteStreamsKt.copyTo(fileInputStream, fileOutputStream, n2);
            }
            catch (Throwable throwable3) {
                try {
                    Throwable throwable4 = throwable3;
                    throwable2 = throwable3;
                    throw throwable4;
                }
                catch (Throwable throwable5) {
                    CloseableKt.closeFinally(closeable, throwable2);
                    throw throwable5;
                }
            }
            CloseableKt.closeFinally(closeable, null);
        }
        catch (Throwable throwable4) {
            try {
                Throwable throwable7 = throwable4;
                throwable = throwable4;
                throw throwable7;
            }
            catch (Throwable throwable8) {
                CloseableKt.closeFinally((Closeable)object, throwable);
                throw throwable8;
            }
        }
        CloseableKt.closeFinally((Closeable)object, null);
        return file;
    }

    public static /* synthetic */ File copyTo$default(File file, File file2, boolean bl, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            bl = false;
        }
        if ((n3 & 4) != 0) {
            n2 = 8192;
        }
        return FilesKt.copyTo(file, file2, bl, n2);
    }

    public static final boolean copyRecursively(@NotNull File file, @NotNull File file2, boolean bl, @NotNull Function2<? super File, ? super IOException, ? extends OnErrorAction> function2) {
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(file2, "");
        Intrinsics.checkNotNullParameter(function2, "");
        if (!file.exists()) {
            return function2.invoke(file, new NoSuchFileException(file, null, "The source file doesn't exist.", 2, null)) != OnErrorAction.TERMINATE;
        }
        try {
            Iterator<File> iterator2 = FilesKt.walkTopDown(file).onFail((Function2<? super File, ? super IOException, Unit>)new Function2<File, IOException, Unit>(function2){
                final /* synthetic */ Function2<File, IOException, OnErrorAction> $onError;
                {
                    this.$onError = function2;
                    super(2);
                }

                public final void invoke(@NotNull File file, @NotNull IOException iOException) {
                    Intrinsics.checkNotNullParameter(file, "");
                    Intrinsics.checkNotNullParameter(iOException, "");
                    if (this.$onError.invoke(file, iOException) == OnErrorAction.TERMINATE) {
                        throw new TerminateException(file);
                    }
                }
            }).iterator();
            while (iterator2.hasNext()) {
                File file3 = iterator2.next();
                if (!file3.exists()) {
                    if (function2.invoke(file3, new NoSuchFileException(file3, null, "The source file doesn't exist.", 2, null)) != OnErrorAction.TERMINATE) continue;
                    return false;
                }
                Object object = FilesKt.toRelativeString(file3, file);
                if (!(!((File)(object = new File(file2, (String)object))).exists() || file3.isDirectory() && ((File)object).isDirectory())) {
                    boolean bl2;
                    boolean bl3 = !bl ? true : (((File)object).isDirectory() ? !FilesKt.deleteRecursively((File)object) : (bl2 = !((File)object).delete()));
                    if (bl3) {
                        if (function2.invoke((File)object, new FileAlreadyExistsException(file3, (File)object, "The destination file already exists.")) != OnErrorAction.TERMINATE) continue;
                        return false;
                    }
                }
                if (file3.isDirectory()) {
                    ((File)object).mkdirs();
                    continue;
                }
                if (FilesKt.copyTo$default(file3, (File)object, bl, 0, 4, null).length() == file3.length() || function2.invoke(file3, new IOException("Source file wasn't copied completely, length of destination file differs.")) != OnErrorAction.TERMINATE) continue;
                return false;
            }
            return true;
        }
        catch (TerminateException terminateException) {
            return false;
        }
    }

    public static /* synthetic */ boolean copyRecursively$default(File file, File file2, boolean bl, Function2 function2, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        if ((n2 & 4) != 0) {
            function2 = copyRecursively.1.INSTANCE;
        }
        return FilesKt.copyRecursively(file, file2, bl, function2);
    }

    public static final boolean deleteRecursively(@NotNull File iterator2) {
        Intrinsics.checkNotNullParameter(iterator2, "");
        iterator2 = FilesKt.walkBottomUp((File)((Object)iterator2));
        boolean bl = true;
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            Object t2 = iterator2.next();
            File file = (File)t2;
            boolean bl2 = bl;
            bl = (file.delete() || !file.exists()) && bl2;
        }
        return bl;
    }

    public static final boolean startsWith(@NotNull File object, @NotNull File object2) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(object2, "");
        object = FilesKt.toComponents((File)object);
        object2 = FilesKt.toComponents((File)object2);
        if (!Intrinsics.areEqual(((FilePathComponents)object).getRoot(), ((FilePathComponents)object2).getRoot())) {
            return false;
        }
        if (((FilePathComponents)object).getSize() < ((FilePathComponents)object2).getSize()) {
            return false;
        }
        return ((Object)((FilePathComponents)object).getSegments().subList(0, ((FilePathComponents)object2).getSize())).equals(((FilePathComponents)object2).getSegments());
    }

    public static final boolean startsWith(@NotNull File file, @NotNull String string) {
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(string, "");
        return FilesKt.startsWith(file, new File(string));
    }

    public static final boolean endsWith(@NotNull File file, @NotNull File file2) {
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(file2, "");
        FilePathComponents filePathComponents = FilesKt.toComponents(file);
        FilePathComponents filePathComponents2 = FilesKt.toComponents(file2);
        if (filePathComponents2.isRooted()) {
            return Intrinsics.areEqual(file, file2);
        }
        int n2 = filePathComponents.getSize() - filePathComponents2.getSize();
        if (n2 < 0) {
            return false;
        }
        return ((Object)filePathComponents.getSegments().subList(n2, filePathComponents.getSize())).equals(filePathComponents2.getSegments());
    }

    public static final boolean endsWith(@NotNull File file, @NotNull String string) {
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(string, "");
        return FilesKt.endsWith(file, new File(string));
    }

    @NotNull
    public static final File normalize(@NotNull File object) {
        Intrinsics.checkNotNullParameter(object, "");
        FilePathComponents filePathComponents = FilesKt.toComponents((File)object);
        object = filePathComponents;
        object = filePathComponents;
        File file = filePathComponents.getRoot();
        Iterable iterable = FilesKt__UtilsKt.normalize$FilesKt__UtilsKt(((FilePathComponents)object).getSegments());
        String string = File.separator;
        Intrinsics.checkNotNullExpressionValue(string, "");
        return FilesKt.resolve(file, CollectionsKt.joinToString$default(iterable, string, null, null, 0, null, null, 62, null));
    }

    private static final FilePathComponents normalize$FilesKt__UtilsKt(FilePathComponents filePathComponents) {
        return new FilePathComponents(filePathComponents.getRoot(), FilesKt__UtilsKt.normalize$FilesKt__UtilsKt(filePathComponents.getSegments()));
    }

    private static final List<File> normalize$FilesKt__UtilsKt(List<? extends File> object) {
        List list2 = new ArrayList(object.size());
        object = object.iterator();
        while (object.hasNext()) {
            File file = (File)object.next();
            String string = file.getName();
            if (Intrinsics.areEqual(string, ".")) continue;
            if (Intrinsics.areEqual(string, "..")) {
                if (!list2.isEmpty() && !Intrinsics.areEqual(((File)CollectionsKt.last(list2)).getName(), "..")) {
                    List list3 = list2;
                    list3.remove(list3.size() - 1);
                    continue;
                }
                list2.add(file);
                continue;
            }
            list2.add(file);
        }
        return list2;
    }

    @NotNull
    public static final File resolve(@NotNull File object, @NotNull File file) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(file, "");
        if (FilesKt.isRooted(file)) {
            return file;
        }
        String string = ((File)object).toString();
        Intrinsics.checkNotNullExpressionValue(string, "");
        object = string;
        if (((CharSequence)string).length() == 0 || StringsKt.endsWith$default((CharSequence)object, File.separatorChar, false, 2, null)) {
            return new File((String)object + file);
        }
        return new File((String)object + File.separatorChar + file);
    }

    @NotNull
    public static final File resolve(@NotNull File file, @NotNull String string) {
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(string, "");
        return FilesKt.resolve(file, new File(string));
    }

    @NotNull
    public static final File resolveSibling(@NotNull File object, @NotNull File file) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(file, "");
        object = FilesKt.toComponents((File)object);
        File file2 = ((FilePathComponents)object).getSize() == 0 ? new File("..") : ((FilePathComponents)object).subPath(0, ((FilePathComponents)object).getSize() - 1);
        return FilesKt.resolve(FilesKt.resolve(((FilePathComponents)object).getRoot(), file2), file);
    }

    @NotNull
    public static final File resolveSibling(@NotNull File file, @NotNull String string) {
        Intrinsics.checkNotNullParameter(file, "");
        Intrinsics.checkNotNullParameter(string, "");
        return FilesKt.resolveSibling(file, new File(string));
    }
}

