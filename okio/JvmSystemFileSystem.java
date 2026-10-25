/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.FileHandle;
import okio.FileMetadata;
import okio.FileSystem;
import okio.JvmFileHandle;
import okio.Okio;
import okio.Path;
import okio.Sink;
import okio.Source;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0010\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006H\u0016J\u0010\u0010\r\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u0006H\u0016J\u0018\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\bH\u0016J\u0018\u0010\u0012\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006H\u0016J\u0018\u0010\u0013\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0016\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00060\u00152\u0006\u0010\u0010\u001a\u00020\u0006H\u0016J \u0010\u0014\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u00152\u0006\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\bH\u0002J\u0018\u0010\u0017\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u00152\u0006\u0010\u0010\u001a\u00020\u0006H\u0016J\u0012\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u000e\u001a\u00020\u0006H\u0016J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J \u0010\u001c\u001a\u00020\u001b2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0018\u0010\u001d\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\bH\u0016J\u0010\u0010\u000b\u001a\u00020\u001e2\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u001f\u001a\u00020 H\u0016J\f\u0010!\u001a\u00020\n*\u00020\u0006H\u0002J\f\u0010\"\u001a\u00020\n*\u00020\u0006H\u0002\u00a8\u0006#"}, d2={"Lokio/JvmSystemFileSystem;", "Lokio/FileSystem;", "()V", "appendingSink", "Lokio/Sink;", "file", "Lokio/Path;", "mustExist", "", "atomicMove", "", "source", "target", "canonicalize", "path", "createDirectory", "dir", "mustCreate", "createSymlink", "delete", "list", "", "throwOnFailure", "listOrNull", "metadataOrNull", "Lokio/FileMetadata;", "openReadOnly", "Lokio/FileHandle;", "openReadWrite", "sink", "Lokio/Source;", "toString", "", "requireCreate", "requireExist", "okio"})
@SourceDebugExtension(value={"SMAP\nJvmSystemFileSystem.kt\nKotlin\n*S Kotlin\n*F\n+ 1 JvmSystemFileSystem.kt\nokio/JvmSystemFileSystem\n+ 2 _Arrays.kt\nkotlin/collections/ArraysKt___ArraysKt\n*L\n1#1,158:1\n11400#2,3:159\n*S KotlinDebug\n*F\n+ 1 JvmSystemFileSystem.kt\nokio/JvmSystemFileSystem\n*L\n77#1:159,3\n*E\n"})
public class JvmSystemFileSystem
extends FileSystem {
    @Override
    @NotNull
    public Path canonicalize(@NotNull Path comparable) {
        Intrinsics.checkNotNullParameter(comparable, "");
        comparable = comparable.toFile().getCanonicalFile();
        if (!((File)comparable).exists()) {
            throw new FileNotFoundException("no such file");
        }
        Intrinsics.checkNotNull(comparable);
        return Path.Companion.get$default(Path.Companion, (File)comparable, false, 1, null);
    }

    @Override
    @Nullable
    public FileMetadata metadataOrNull(@NotNull Path comparable) {
        Intrinsics.checkNotNullParameter(comparable, "");
        comparable = ((Path)comparable).toFile();
        boolean bl = ((File)comparable).isFile();
        boolean bl2 = ((File)comparable).isDirectory();
        long l2 = ((File)comparable).lastModified();
        long l3 = ((File)comparable).length();
        if (!(bl || bl2 || l2 != 0L || l3 != 0L || ((File)comparable).exists())) {
            return null;
        }
        return new FileMetadata(bl, bl2, null, l3, null, l2, null, null, 128, null);
    }

    @Override
    @NotNull
    public List<Path> list(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        List<Path> list2 = this.list(path, true);
        Intrinsics.checkNotNull(list2);
        return list2;
    }

    @Override
    @Nullable
    public List<Path> listOrNull(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        return this.list(path, false);
    }

    private final List<Path> list(Path object, boolean bl) {
        Object object2 = ((Path)object).toFile();
        String[] stringArray = ((File)object2).list();
        if (stringArray == null) {
            if (bl) {
                if (!((File)object2).exists()) {
                    throw new FileNotFoundException("no such file: " + object);
                }
                throw new IOException("failed to list " + object);
            }
            return null;
        }
        String[] stringArray2 = stringArray;
        object2 = new ArrayList();
        for (String string : stringArray2) {
            Object object3 = object2;
            Intrinsics.checkNotNull(string);
            object3.add(((Path)object).resolve(string));
        }
        object = (List)object2;
        CollectionsKt.sort(object);
        return object;
    }

    @Override
    @NotNull
    public FileHandle openReadOnly(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        return new JvmFileHandle(false, new RandomAccessFile(path.toFile(), "r"));
    }

    @Override
    @NotNull
    public FileHandle openReadWrite(@NotNull Path object, boolean bl, boolean bl2) {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(!bl || !bl2)) {
            object = "Cannot require mustCreate and mustExist at the same time.";
            throw new IllegalArgumentException(object.toString());
        }
        if (bl) {
            this.requireCreate((Path)object);
        }
        if (bl2) {
            this.requireExist((Path)object);
        }
        return new JvmFileHandle(true, new RandomAccessFile(((Path)object).toFile(), "rw"));
    }

    @Override
    @NotNull
    public Source source(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        return Okio.source(path.toFile());
    }

    @Override
    @NotNull
    public Sink sink(@NotNull Path path, boolean bl) {
        Intrinsics.checkNotNullParameter(path, "");
        if (bl) {
            this.requireCreate(path);
        }
        return Okio.sink$default(path.toFile(), false, 1, null);
    }

    @Override
    @NotNull
    public Sink appendingSink(@NotNull Path path, boolean bl) {
        Intrinsics.checkNotNullParameter(path, "");
        if (bl) {
            this.requireExist(path);
        }
        return Okio.sink(path.toFile(), true);
    }

    @Override
    public void createDirectory(@NotNull Path path, boolean bl) {
        Intrinsics.checkNotNullParameter(path, "");
        if (!path.toFile().mkdir()) {
            boolean bl2;
            FileMetadata fileMetadata = ((FileSystem)this).metadataOrNull(path);
            boolean bl3 = fileMetadata != null ? fileMetadata.isDirectory() : (bl2 = false);
            if (bl3) {
                if (bl) {
                    throw new IOException(path + " already exists.");
                }
                return;
            }
            throw new IOException("failed to create directory: " + path);
        }
    }

    @Override
    public void atomicMove(@NotNull Path path, @NotNull Path path2) {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(path2, "");
        boolean bl = path.toFile().renameTo(path2.toFile());
        if (!bl) {
            throw new IOException("failed to move " + path + " to " + path2);
        }
    }

    @Override
    public void delete(@NotNull Path path, boolean bl) {
        Intrinsics.checkNotNullParameter(path, "");
        if (Thread.interrupted()) {
            throw new InterruptedIOException("interrupted");
        }
        File file = path.toFile();
        boolean bl2 = file.delete();
        if (!bl2) {
            if (file.exists()) {
                throw new IOException("failed to delete " + path);
            }
            if (bl) {
                throw new FileNotFoundException("no such file: " + path);
            }
        }
    }

    @Override
    public void createSymlink(@NotNull Path path, @NotNull Path path2) {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(path2, "");
        throw new IOException("unsupported");
    }

    @NotNull
    public String toString() {
        return "JvmSystemFileSystem";
    }

    private final void requireExist(Path path) {
        if (!this.exists(path)) {
            throw new IOException(path + " doesn't exist.");
        }
    }

    private final void requireCreate(Path path) {
        if (this.exists(path)) {
            throw new IOException(path + " already exists.");
        }
    }
}

