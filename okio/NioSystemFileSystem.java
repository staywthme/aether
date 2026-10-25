/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.FileMetadata;
import okio.JvmSystemFileSystem;
import okio.Path;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0010\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\u0018\u0010\b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\u0012\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\fH\u0004J\u0012\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\r\u001a\u00020\u0006H\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u0013\u0010\u0010\u001a\u0004\u0018\u00010\u0011*\u00020\u0012H\u0002\u00a2\u0006\u0002\u0010\u0013\u00a8\u0006\u0014"}, d2={"Lokio/NioSystemFileSystem;", "Lokio/JvmSystemFileSystem;", "()V", "atomicMove", "", "source", "Lokio/Path;", "target", "createSymlink", "metadataOrNull", "Lokio/FileMetadata;", "nioPath", "Ljava/nio/file/Path;", "path", "toString", "", "zeroToNull", "", "Ljava/nio/file/attribute/FileTime;", "(Ljava/nio/file/attribute/FileTime;)Ljava/lang/Long;", "okio"})
@SourceDebugExtension(value={"SMAP\nNioSystemFileSystem.kt\nKotlin\n*S Kotlin\n*F\n+ 1 NioSystemFileSystem.kt\nokio/NioSystemFileSystem\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,92:1\n1#2:93\n*E\n"})
public class NioSystemFileSystem
extends JvmSystemFileSystem {
    @Override
    @Nullable
    public FileMetadata metadataOrNull(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        return this.metadataOrNull(path.toNioPath());
    }

    @Nullable
    protected final FileMetadata metadataOrNull(@NotNull java.nio.file.Path path) {
        Object object;
        Intrinsics.checkNotNullParameter(path, "");
        try {
            LinkOption[] linkOptionArray = new LinkOption[1];
            object = linkOptionArray;
            linkOptionArray[0] = LinkOption.NOFOLLOW_LINKS;
            object = Files.readAttributes(path, BasicFileAttributes.class, (LinkOption[])object);
        }
        catch (NoSuchFileException noSuchFileException) {
            return null;
        }
        catch (FileSystemException fileSystemException) {
            return null;
        }
        Object object2 = object;
        object = object2.isSymbolicLink() ? Files.readSymbolicLink(path) : null;
        Object object3 = object;
        FileTime fileTime = object2.creationTime();
        FileTime fileTime2 = object2.lastModifiedTime();
        FileTime fileTime3 = object2.lastAccessTime();
        return new FileMetadata(object2.isRegularFile(), object2.isDirectory(), object3 != null ? Path.Companion.get$default(Path.Companion, (java.nio.file.Path)object3, false, 1, null) : null, object2.size(), fileTime != null ? this.zeroToNull(fileTime) : null, fileTime2 != null ? this.zeroToNull(fileTime2) : null, fileTime3 != null ? this.zeroToNull(fileTime3) : null, null, 128, null);
    }

    private final Long zeroToNull(FileTime comparable) {
        long l2 = ((Number)((Object)(comparable = Long.valueOf(comparable.toMillis())))).longValue();
        if (l2 != 0L) {
            return comparable;
        }
        return null;
    }

    @Override
    public void atomicMove(@NotNull Path copyOptionArray, @NotNull Path path) {
        Intrinsics.checkNotNullParameter(copyOptionArray, "");
        Intrinsics.checkNotNullParameter(path, "");
        try {
            java.nio.file.Path path2 = copyOptionArray.toNioPath();
            CopyOption[] copyOptionArray2 = new CopyOption[2];
            copyOptionArray = copyOptionArray2;
            copyOptionArray2[0] = StandardCopyOption.ATOMIC_MOVE;
            copyOptionArray[1] = StandardCopyOption.REPLACE_EXISTING;
            Files.move(path2, path.toNioPath(), copyOptionArray);
            return;
        }
        catch (NoSuchFileException noSuchFileException) {
            throw new FileNotFoundException(noSuchFileException.getMessage());
        }
        catch (UnsupportedOperationException unsupportedOperationException) {
            throw new IOException("atomic move not supported");
        }
    }

    @Override
    public void createSymlink(@NotNull Path path, @NotNull Path path2) {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(path2, "");
        Files.createSymbolicLink(path.toNioPath(), path2.toNioPath(), new FileAttribute[0]);
    }

    @Override
    @NotNull
    public String toString() {
        return "NioSystemFileSystem";
    }
}

