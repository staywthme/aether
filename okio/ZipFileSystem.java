/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.BufferedSource;
import okio.FileHandle;
import okio.FileMetadata;
import okio.FileSystem;
import okio.InflaterSource;
import okio.Okio;
import okio.Path;
import okio.Sink;
import okio.Source;
import okio.internal.FixedLengthSource;
import okio.internal.ZipEntry;
import okio.internal.ZipFilesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u0000 '2\u00020\u0001:\u0001'B5\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0001\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00070\u0006\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\u0002\u0010\nJ\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0018\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u0003H\u0016J\u0010\u0010\u0014\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0003H\u0016J\u0010\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0003H\u0002J\u0018\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u000fH\u0016J\u0018\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u0003H\u0016J\u0018\u0010\u001b\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0016\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00030\u001d2\u0006\u0010\u0018\u001a\u00020\u0003H\u0016J \u0010\u001c\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u001d2\u0006\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u001e\u001a\u00020\u000fH\u0002J\u0018\u0010\u001f\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u001d2\u0006\u0010\u0018\u001a\u00020\u0003H\u0016J\u0012\u0010 \u001a\u0004\u0018\u00010!2\u0006\u0010\u0015\u001a\u00020\u0003H\u0016J\u0010\u0010\"\u001a\u00020#2\u0006\u0010\r\u001a\u00020\u0003H\u0016J \u0010$\u001a\u00020#2\u0006\u0010\r\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u000f2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0018\u0010%\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u000fH\u0016J\u0010\u0010\u0012\u001a\u00020&2\u0006\u0010\r\u001a\u00020\u0003H\u0016R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006("}, d2={"Lokio/ZipFileSystem;", "Lokio/FileSystem;", "zipPath", "Lokio/Path;", "fileSystem", "entries", "", "Lokio/internal/ZipEntry;", "comment", "", "(Lokio/Path;Lokio/FileSystem;Ljava/util/Map;Ljava/lang/String;)V", "appendingSink", "Lokio/Sink;", "file", "mustExist", "", "atomicMove", "", "source", "target", "canonicalize", "path", "canonicalizeInternal", "createDirectory", "dir", "mustCreate", "createSymlink", "delete", "list", "", "throwOnFailure", "listOrNull", "metadataOrNull", "Lokio/FileMetadata;", "openReadOnly", "Lokio/FileHandle;", "openReadWrite", "sink", "Lokio/Source;", "Companion", "okio"})
@SourceDebugExtension(value={"SMAP\nZipFileSystem.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ZipFileSystem.kt\nokio/ZipFileSystem\n+ 2 Okio.kt\nokio/Okio__OkioKt\n*L\n1#1,175:1\n52#2,5:176\n52#2,21:181\n60#2,10:202\n57#2,2:212\n71#2,2:214\n52#2,21:216\n*S KotlinDebug\n*F\n+ 1 ZipFileSystem.kt\nokio/ZipFileSystem\n*L\n102#1:176,5\n103#1:181,21\n102#1:202,10\n102#1:212,2\n102#1:214,2\n132#1:216,21\n*E\n"})
public final class ZipFileSystem
extends FileSystem {
    @NotNull
    private static final Companion Companion = new Companion(null);
    @NotNull
    private final Path zipPath;
    @NotNull
    private final FileSystem fileSystem;
    @NotNull
    private final Map<Path, ZipEntry> entries;
    @Nullable
    private final String comment;
    @NotNull
    private static final Path ROOT = Path.Companion.get$default(Path.Companion, "/", false, 1, null);

    public ZipFileSystem(@NotNull Path path, @NotNull FileSystem fileSystem, @NotNull Map<Path, ZipEntry> map, @Nullable String string) {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(fileSystem, "");
        Intrinsics.checkNotNullParameter(map, "");
        this.zipPath = path;
        this.fileSystem = fileSystem;
        this.entries = map;
        this.comment = string;
    }

    @Override
    @NotNull
    public final Path canonicalize(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        Path path2 = this.canonicalizeInternal(path);
        if (!this.entries.containsKey(path2)) {
            throw new FileNotFoundException(String.valueOf(path));
        }
        return path2;
    }

    private final Path canonicalizeInternal(Path path) {
        return ROOT.resolve(path, true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    @Nullable
    public final FileMetadata metadataOrNull(@NotNull Path object) {
        Throwable throwable;
        Object object2;
        block27: {
            Object object3;
            Intrinsics.checkNotNullParameter(object, "");
            object = this.canonicalizeInternal((Path)object);
            ZipEntry zipEntry = this.entries.get(object);
            if (zipEntry == null) {
                return null;
            }
            object = zipEntry;
            FileMetadata fileMetadata = new FileMetadata(!((ZipEntry)object).isDirectory(), ((ZipEntry)object).isDirectory(), null, ((ZipEntry)object).isDirectory() ? null : Long.valueOf(((ZipEntry)object).getSize()), null, ((ZipEntry)object).getLastModifiedAtMillis(), null, null, 128, null);
            if (((ZipEntry)object).getOffset() == -1L) {
                return fileMetadata;
            }
            Closeable closeable = this.fileSystem.openReadOnly(this.zipPath);
            object2 = null;
            throwable = null;
            try {
                Throwable throwable2;
                block26: {
                    Object object4;
                    object3 = (FileHandle)closeable;
                    object = Okio.buffer(((FileHandle)object3).source(((ZipEntry)object).getOffset()));
                    object3 = null;
                    throwable2 = null;
                    try {
                        object4 = (BufferedSource)object;
                        object3 = ZipFilesKt.readLocalHeader((BufferedSource)object4, fileMetadata);
                    }
                    catch (Throwable throwable3) {
                        Throwable throwable5 = throwable3;
                        throwable2 = throwable3;
                        try {
                            Object object5 = object;
                            if (object5 != null) {
                                object5.close();
                            }
                            break block26;
                        }
                        catch (Throwable throwable6) {
                            ExceptionsKt.addSuppressed(throwable2, throwable6);
                        }
                        break block26;
                    }
                    catch (Throwable throwable7) {
                        try {
                            Object object6 = object;
                            if (object6 == null) throw throwable7;
                            object6.close();
                            throw throwable7;
                        }
                        catch (Throwable throwable4) {}
                        throw throwable7;
                    }
                    try {
                        Object object7 = object;
                        if (object7 != null) {
                            object7.close();
                        }
                    }
                    catch (Throwable throwable5) {
                        object4 = throwable5;
                        throwable2 = throwable5;
                    }
                }
                Throwable throwable6 = throwable2;
                if (throwable6 != null) {
                    throw throwable6;
                }
                Object object8 = object3;
                Intrinsics.checkNotNull(object8);
                object2 = object8;
            }
            catch (Throwable throwable7) {
                Throwable throwable12 = throwable7;
                throwable = throwable7;
                try {
                    Closeable closeable2 = closeable;
                    if (closeable2 != null) {
                        closeable2.close();
                    }
                    break block27;
                }
                catch (Throwable throwable13) {
                    ExceptionsKt.addSuppressed(throwable, throwable13);
                }
                break block27;
            }
            catch (Throwable throwable14) {
                try {
                    Closeable closeable3 = closeable;
                    if (closeable3 == null) throw throwable14;
                    closeable3.close();
                    throw throwable14;
                }
                catch (Throwable throwable8) {}
                throw throwable14;
            }
            try {
                Closeable closeable4 = closeable;
                if (closeable4 != null) {
                    closeable4.close();
                }
            }
            catch (Throwable throwable9) {
                object3 = throwable9;
                throwable = throwable9;
            }
        }
        Throwable throwable10 = throwable;
        if (throwable10 != null) {
            throw throwable10;
        }
        Object object9 = object2;
        Intrinsics.checkNotNull(object9);
        return object9;
    }

    @Override
    @NotNull
    public final FileHandle openReadOnly(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        throw new UnsupportedOperationException("not implemented yet!");
    }

    @Override
    @NotNull
    public final FileHandle openReadWrite(@NotNull Path path, boolean bl, boolean bl2) {
        Intrinsics.checkNotNullParameter(path, "");
        throw new IOException("zip entries are not writable");
    }

    @Override
    @NotNull
    public final List<Path> list(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        List<Path> list2 = this.list(path, true);
        Intrinsics.checkNotNull(list2);
        return list2;
    }

    @Override
    @Nullable
    public final List<Path> listOrNull(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        return this.list(path, false);
    }

    private final List<Path> list(Path object, boolean bl) {
        Path path = this.canonicalizeInternal((Path)object);
        ZipEntry zipEntry = this.entries.get(path);
        if (zipEntry == null) {
            if (bl) {
                throw new IOException("not a directory: " + object);
            }
            return null;
        }
        object = zipEntry;
        return CollectionsKt.toList((Iterable)zipEntry.getChildren());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    @NotNull
    public final Source source(@NotNull Path object) throws IOException {
        Throwable throwable;
        BufferedSource bufferedSource;
        Object object2;
        block14: {
            Object object3;
            Intrinsics.checkNotNullParameter(object, "");
            object2 = this.canonicalizeInternal((Path)object);
            ZipEntry zipEntry = this.entries.get(object2);
            if (zipEntry == null) {
                throw new FileNotFoundException("no such file: " + object);
            }
            object = zipEntry;
            object2 = this.fileSystem.openReadOnly(this.zipPath);
            bufferedSource = null;
            throwable = null;
            try {
                object3 = (FileHandle)object2;
                bufferedSource = Okio.buffer(((FileHandle)object3).source(((ZipEntry)object).getOffset()));
            }
            catch (Throwable throwable2) {
                Throwable throwable4 = throwable2;
                throwable = throwable2;
                try {
                    Object object4 = object2;
                    if (object4 != null) {
                        object4.close();
                    }
                    break block14;
                }
                catch (Throwable throwable5) {
                    ExceptionsKt.addSuppressed(throwable, throwable5);
                }
                break block14;
            }
            catch (Throwable throwable6) {
                try {
                    Object object5 = object2;
                    if (object5 == null) throw throwable6;
                    object5.close();
                    throw throwable6;
                }
                catch (Throwable throwable3) {}
                throw throwable6;
            }
            try {
                Object object6 = object2;
                if (object6 != null) {
                    object6.close();
                }
            }
            catch (Throwable throwable4) {
                object3 = throwable4;
                throwable = throwable4;
            }
        }
        Throwable throwable5 = throwable;
        if (throwable5 != null) {
            throw throwable5;
        }
        BufferedSource bufferedSource2 = bufferedSource;
        Intrinsics.checkNotNull(bufferedSource2);
        object2 = bufferedSource2;
        ZipFilesKt.skipLocalHeader(bufferedSource2);
        if (((ZipEntry)object).getCompressionMethod() == 0) {
            return new FixedLengthSource((Source)object2, ((ZipEntry)object).getSize(), true);
        }
        object2 = new InflaterSource(new FixedLengthSource((Source)object2, ((ZipEntry)object).getCompressedSize(), true), new Inflater(true));
        return new FixedLengthSource((Source)object2, ((ZipEntry)object).getSize(), false);
    }

    @Override
    @NotNull
    public final Sink sink(@NotNull Path path, boolean bl) {
        Intrinsics.checkNotNullParameter(path, "");
        throw new IOException("zip file systems are read-only");
    }

    @Override
    @NotNull
    public final Sink appendingSink(@NotNull Path path, boolean bl) {
        Intrinsics.checkNotNullParameter(path, "");
        throw new IOException("zip file systems are read-only");
    }

    @Override
    public final void createDirectory(@NotNull Path path, boolean bl) {
        Intrinsics.checkNotNullParameter(path, "");
        throw new IOException("zip file systems are read-only");
    }

    @Override
    public final void atomicMove(@NotNull Path path, @NotNull Path path2) {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(path2, "");
        throw new IOException("zip file systems are read-only");
    }

    @Override
    public final void delete(@NotNull Path path, boolean bl) {
        Intrinsics.checkNotNullParameter(path, "");
        throw new IOException("zip file systems are read-only");
    }

    @Override
    public final void createSymlink(@NotNull Path path, @NotNull Path path2) {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(path2, "");
        throw new IOException("zip file systems are read-only");
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2={"Lokio/ZipFileSystem$Companion;", "", "()V", "ROOT", "Lokio/Path;", "getROOT", "()Lokio/Path;", "okio"})
    static final class Companion {
        private Companion() {
        }

        @NotNull
        public final Path getROOT() {
            return ROOT;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

