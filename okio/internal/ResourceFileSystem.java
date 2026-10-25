/*
 * Decompiled with CFR 0.152.
 */
package okio.internal;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import okio.FileHandle;
import okio.FileMetadata;
import okio.FileSystem;
import okio.Okio;
import okio.Path;
import okio.Sink;
import okio.Source;
import okio.internal.ResourceFileSystem;
import okio.internal.ZipFilesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0000\u0018\u0000 /2\u00020\u0001:\u0001/B!\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0001\u00a2\u0006\u0002\u0010\u0007J\u0018\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u0005H\u0016J\u0018\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u0017\u001a\u00020\u000bH\u0016J\u0010\u0010\u0018\u001a\u00020\u000b2\u0006\u0010\u0019\u001a\u00020\u000bH\u0016J\u0010\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\u0019\u001a\u00020\u000bH\u0002J\u0018\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u000b2\u0006\u0010\u001d\u001a\u00020\u0005H\u0016J\u0018\u0010\u001e\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u0017\u001a\u00020\u000bH\u0016J\u0018\u0010\u001f\u001a\u00020\u00152\u0006\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u0005H\u0016J\u0016\u0010 \u001a\b\u0012\u0004\u0012\u00020\u000b0\t2\u0006\u0010\u001c\u001a\u00020\u000bH\u0016J\u0018\u0010!\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\t2\u0006\u0010\u001c\u001a\u00020\u000bH\u0016J\u0012\u0010\"\u001a\u0004\u0018\u00010#2\u0006\u0010\u0019\u001a\u00020\u000bH\u0016J\u0010\u0010$\u001a\u00020%2\u0006\u0010\u0012\u001a\u00020\u000bH\u0016J \u0010&\u001a\u00020%2\u0006\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u0005H\u0016J\u0018\u0010'\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u001d\u001a\u00020\u0005H\u0016J\u0010\u0010\u0016\u001a\u00020(2\u0006\u0010\u0012\u001a\u00020\u000bH\u0016J\u001e\u0010)\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u000b0\n0\t*\u00020\u0003H\u0002J\u001a\u0010*\u001a\u0010\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u000b\u0018\u00010\n*\u00020+H\u0002J\u001a\u0010,\u001a\u0010\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u000b\u0018\u00010\n*\u00020+H\u0002J\f\u0010-\u001a\u00020.*\u00020\u000bH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R-\u0010\b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u000b0\n0\t8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\f\u0010\rR\u000e\u0010\u0006\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00060"}, d2={"Lokio/internal/ResourceFileSystem;", "Lokio/FileSystem;", "classLoader", "Ljava/lang/ClassLoader;", "indexEagerly", "", "systemFileSystem", "(Ljava/lang/ClassLoader;ZLokio/FileSystem;)V", "roots", "", "Lkotlin/Pair;", "Lokio/Path;", "getRoots", "()Ljava/util/List;", "roots$delegate", "Lkotlin/Lazy;", "appendingSink", "Lokio/Sink;", "file", "mustExist", "atomicMove", "", "source", "target", "canonicalize", "path", "canonicalizeInternal", "createDirectory", "dir", "mustCreate", "createSymlink", "delete", "list", "listOrNull", "metadataOrNull", "Lokio/FileMetadata;", "openReadOnly", "Lokio/FileHandle;", "openReadWrite", "sink", "Lokio/Source;", "toClasspathRoots", "toFileRoot", "Ljava/net/URL;", "toJarRoot", "toRelativePath", "", "Companion", "okio"})
@SourceDebugExtension(value={"SMAP\nResourceFileSystem.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ResourceFileSystem.kt\nokio/internal/ResourceFileSystem\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,210:1\n766#2:211\n857#2,2:212\n1549#2:214\n1620#2,3:215\n766#2:218\n857#2,2:219\n1549#2:221\n1620#2,3:222\n1603#2,9:225\n1855#2:234\n1856#2:236\n1612#2:237\n1603#2,9:238\n1855#2:247\n1856#2:249\n1612#2:250\n1#3:235\n1#3:248\n*S KotlinDebug\n*F\n+ 1 ResourceFileSystem.kt\nokio/internal/ResourceFileSystem\n*L\n74#1:211\n74#1:212,2\n75#1:214\n75#1:215,3\n90#1:218\n90#1:219,2\n91#1:221\n91#1:222,3\n173#1:225,9\n173#1:234\n173#1:236\n173#1:237\n174#1:238,9\n174#1:247\n174#1:249\n174#1:250\n173#1:235\n174#1:248\n*E\n"})
public final class ResourceFileSystem
extends FileSystem {
    @NotNull
    private static final Companion Companion = new Companion(null);
    @NotNull
    private final ClassLoader classLoader;
    @NotNull
    private final FileSystem systemFileSystem;
    @NotNull
    private final Lazy roots$delegate;
    @NotNull
    private static final Path ROOT = Path.Companion.get$default(Path.Companion, "/", false, 1, null);

    public ResourceFileSystem(@NotNull ClassLoader classLoader, boolean bl, @NotNull FileSystem fileSystem) {
        Intrinsics.checkNotNullParameter(classLoader, "");
        Intrinsics.checkNotNullParameter(fileSystem, "");
        this.classLoader = classLoader;
        this.systemFileSystem = fileSystem;
        this.roots$delegate = LazyKt.lazy((Function0)new Function0<List<? extends Pair<? extends FileSystem, ? extends Path>>>(this){
            final /* synthetic */ ResourceFileSystem this$0;
            {
                this.this$0 = resourceFileSystem;
                super(0);
            }

            @NotNull
            public final List<Pair<FileSystem, Path>> invoke() {
                return ResourceFileSystem.access$toClasspathRoots(this.this$0, ResourceFileSystem.access$getClassLoader$p(this.this$0));
            }
        });
        if (bl) {
            this.getRoots().size();
        }
    }

    public /* synthetic */ ResourceFileSystem(ClassLoader classLoader, boolean bl, FileSystem fileSystem, int n2, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n2 & 4) != 0) {
            fileSystem = FileSystem.SYSTEM;
        }
        this(classLoader, bl, fileSystem);
    }

    private final List<Pair<FileSystem, Path>> getRoots() {
        Lazy lazy = this.roots$delegate;
        return (List)lazy.getValue();
    }

    @Override
    @NotNull
    public final Path canonicalize(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        return this.canonicalizeInternal(path);
    }

    private final Path canonicalizeInternal(Path path) {
        return ROOT.resolve(path, true);
    }

    @Override
    @NotNull
    public final List<Path> list(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        String string = this.toRelativePath(path);
        Set set = new LinkedHashSet();
        boolean bl = false;
        for (Pair<FileSystem, Path> pair : this.getRoots()) {
            Object object = pair.component1();
            Path object2 = pair.component2();
            try {
                Path path2;
                Object object3;
                Collection collection = set;
                object = ((FileSystem)object).list(object2.resolve(string));
                Iterator iterator2 = object;
                object = new ArrayList();
                iterator2 = iterator2.iterator();
                while (iterator2.hasNext()) {
                    object3 = iterator2.next();
                    path2 = (Path)object3;
                    if (!ResourceFileSystem.Companion.keepPath(path2)) continue;
                    object.add(object3);
                }
                object = (List)object;
                iterator2 = object;
                object = new ArrayList(CollectionsKt.collectionSizeOrDefault(object, 10));
                iterator2 = iterator2.iterator();
                while (iterator2.hasNext()) {
                    object3 = iterator2.next();
                    path2 = (Path)object3;
                    object3 = object;
                    object3.add((Path)Companion.removeBase(path2, object2));
                }
                object = (List)object;
                CollectionsKt.addAll(collection, object);
                bl = true;
            }
            catch (IOException iOException) {}
        }
        if (!bl) {
            throw new FileNotFoundException("file not found: " + path);
        }
        return CollectionsKt.toList(set);
    }

    @Override
    @Nullable
    public final List<Path> listOrNull(@NotNull Path object) {
        Intrinsics.checkNotNullParameter(object, "");
        object = this.toRelativePath((Path)object);
        Set set = new LinkedHashSet();
        boolean bl = false;
        for (Pair<FileSystem, Path> pair : this.getRoots()) {
            List object2;
            List list2;
            Object object3 = pair.component1();
            Path path = pair.component2();
            if ((object3 = ((FileSystem)object3).listOrNull(path.resolve((String)object))) != null) {
                Path path2;
                Object object4;
                object3 = (Iterable)object3;
                Iterator iterator2 = object3;
                object3 = new ArrayList();
                iterator2 = iterator2.iterator();
                while (iterator2.hasNext()) {
                    object4 = iterator2.next();
                    path2 = (Path)object4;
                    if (!ResourceFileSystem.Companion.keepPath(path2)) continue;
                    object3.add(object4);
                }
                object3 = (List)object3;
                iterator2 = object3;
                object3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(object3, 10));
                iterator2 = iterator2.iterator();
                while (iterator2.hasNext()) {
                    object4 = iterator2.next();
                    path2 = (Path)object4;
                    object4 = object3;
                    object4.add((Path)Companion.removeBase(path2, path));
                }
                list2 = (List)object3;
            } else {
                list2 = object2 = null;
            }
            if (list2 == null) continue;
            CollectionsKt.addAll((Collection)set, object2);
            bl = true;
        }
        if (bl) {
            return CollectionsKt.toList(set);
        }
        return null;
    }

    @Override
    @NotNull
    public final FileHandle openReadOnly(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        if (!ResourceFileSystem.Companion.keepPath(path)) {
            throw new FileNotFoundException("file not found: " + path);
        }
        String string = this.toRelativePath(path);
        for (Pair<FileSystem, Path> pair : this.getRoots()) {
            FileSystem fileSystem = pair.component1();
            Path object = pair.component2();
            try {
                return fileSystem.openReadOnly(object.resolve(string));
            }
            catch (FileNotFoundException fileNotFoundException) {
            }
        }
        throw new FileNotFoundException("file not found: " + path);
    }

    @Override
    @NotNull
    public final FileHandle openReadWrite(@NotNull Path path, boolean bl, boolean bl2) {
        Intrinsics.checkNotNullParameter(path, "");
        throw new IOException("resources are not writable");
    }

    @Override
    @Nullable
    public final FileMetadata metadataOrNull(@NotNull Path object) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!ResourceFileSystem.Companion.keepPath((Path)object)) {
            return null;
        }
        object = this.toRelativePath((Path)object);
        for (Pair<FileSystem, Path> pair : this.getRoots()) {
            FileSystem fileSystem = pair.component1();
            Path object2 = pair.component2();
            FileMetadata fileMetadata = fileSystem.metadataOrNull(object2.resolve((String)object));
            if (fileMetadata == null) continue;
            return fileMetadata;
        }
        return null;
    }

    @Override
    @NotNull
    public final Source source(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        if (!ResourceFileSystem.Companion.keepPath(path)) {
            throw new FileNotFoundException("file not found: " + path);
        }
        Path path2 = Path.resolve$default(ROOT, path, false, 2, null).relativeTo(ROOT);
        Closeable closeable = this.classLoader.getResourceAsStream(path2.toString());
        if (closeable == null || (closeable = Okio.source(closeable)) == null) {
            throw new FileNotFoundException("file not found: " + path);
        }
        return closeable;
    }

    @Override
    @NotNull
    public final Sink sink(@NotNull Path path, boolean bl) {
        Intrinsics.checkNotNullParameter(path, "");
        throw new IOException(this + " is read-only");
    }

    @Override
    @NotNull
    public final Sink appendingSink(@NotNull Path path, boolean bl) {
        Intrinsics.checkNotNullParameter(path, "");
        throw new IOException(this + " is read-only");
    }

    @Override
    public final void createDirectory(@NotNull Path path, boolean bl) {
        Intrinsics.checkNotNullParameter(path, "");
        throw new IOException(this + " is read-only");
    }

    @Override
    public final void atomicMove(@NotNull Path path, @NotNull Path path2) {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(path2, "");
        throw new IOException(this + " is read-only");
    }

    @Override
    public final void delete(@NotNull Path path, boolean bl) {
        Intrinsics.checkNotNullParameter(path, "");
        throw new IOException(this + " is read-only");
    }

    @Override
    public final void createSymlink(@NotNull Path path, @NotNull Path path2) {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(path2, "");
        throw new IOException(this + " is read-only");
    }

    private final String toRelativePath(Path path) {
        path = this.canonicalizeInternal(path);
        return path.relativeTo(ROOT).toString();
    }

    private final List<Pair<FileSystem, Path>> toClasspathRoots(ClassLoader object) {
        Object object2;
        Enumeration<URL> enumeration = ((ClassLoader)object).getResources("");
        Intrinsics.checkNotNullExpressionValue(enumeration, "");
        ArrayList<URL> arrayList = Collections.list(enumeration);
        Intrinsics.checkNotNullExpressionValue(arrayList, "");
        Iterator iterator2 = arrayList;
        Collection collection = new ArrayList();
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            Object e2 = iterator2.next();
            object2 = e2;
            object2 = e2;
            object2 = (URL)e2;
            Intrinsics.checkNotNull(object2);
            Pair<FileSystem, Path> pair = this.toFileRoot((URL)object2);
            if (pair == null) continue;
            object2 = pair;
            collection.add(object2);
        }
        Collection collection2 = (List)collection;
        Enumeration<URL> enumeration2 = ((ClassLoader)object).getResources("META-INF/MANIFEST.MF");
        Intrinsics.checkNotNullExpressionValue(enumeration2, "");
        ArrayList<URL> arrayList2 = Collections.list(enumeration2);
        Intrinsics.checkNotNullExpressionValue(arrayList2, "");
        iterator2 = arrayList2;
        object = collection2;
        collection = new ArrayList();
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            Object t2 = iterator2.next();
            object2 = t2;
            object2 = t2;
            object2 = (URL)t2;
            Intrinsics.checkNotNull(object2);
            Pair<FileSystem, Path> pair = this.toJarRoot((URL)object2);
            if (pair == null) continue;
            object2 = pair;
            collection.add(object2);
        }
        return CollectionsKt.plus(object, (Iterable)((List)collection));
    }

    private final Pair<FileSystem, Path> toFileRoot(URL uRL) {
        if (!Intrinsics.areEqual(uRL.getProtocol(), "file")) {
            return null;
        }
        return TuplesKt.to(this.systemFileSystem, Path.Companion.get$default(Path.Companion, new File(uRL.toURI()), false, 1, null));
    }

    private final Pair<FileSystem, Path> toJarRoot(URL object) {
        String string = ((URL)object).toString();
        Intrinsics.checkNotNullExpressionValue(string, "");
        object = string;
        if (!StringsKt.startsWith$default(string, "jar:file:", false, 2, null)) {
            return null;
        }
        int n2 = StringsKt.lastIndexOf$default((CharSequence)object, "!", 0, false, 6, null);
        if (n2 == -1) {
            return null;
        }
        String string2 = ((String)object).substring(4, n2);
        Intrinsics.checkNotNullExpressionValue(string2, "");
        object = Path.Companion.get$default(Path.Companion, new File(URI.create(string2)), false, 1, null);
        object = ZipFilesKt.openZip((Path)object, this.systemFileSystem, toJarRoot.zip.1.INSTANCE);
        return TuplesKt.to(object, ROOT);
    }

    public static final /* synthetic */ Companion access$getCompanion$p() {
        return Companion;
    }

    public static final /* synthetic */ List access$toClasspathRoots(ResourceFileSystem resourceFileSystem, ClassLoader classLoader) {
        return resourceFileSystem.toClasspathRoots(classLoader);
    }

    public static final /* synthetic */ ClassLoader access$getClassLoader$p(ResourceFileSystem resourceFileSystem) {
        return resourceFileSystem.classLoader;
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0004H\u0002J\u0012\u0010\n\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\f"}, d2={"Lokio/internal/ResourceFileSystem$Companion;", "", "()V", "ROOT", "Lokio/Path;", "getROOT", "()Lokio/Path;", "keepPath", "", "path", "removeBase", "base", "okio"})
    static final class Companion {
        private Companion() {
        }

        @NotNull
        public final Path getROOT() {
            return ROOT;
        }

        @NotNull
        public final Path removeBase(@NotNull Path path, @NotNull Path object) {
            Intrinsics.checkNotNullParameter(path, "");
            Intrinsics.checkNotNullParameter(object, "");
            object = ((Path)object).toString();
            return this.getROOT().resolve(StringsKt.replace$default(StringsKt.removePrefix(path.toString(), (CharSequence)object), '\\', '/', false, 4, null));
        }

        private final boolean keepPath(Path path) {
            return !StringsKt.endsWith(path.name(), ".class", true);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

