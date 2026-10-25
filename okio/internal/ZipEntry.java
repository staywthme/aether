/*
 * Decompiled with CFR 0.152.
 */
package okio.internal;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okio.Path;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010!\n\u0002\b\u0010\b\u0000\u0018\u00002\u00020\u0001B_\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\t\u0012\b\b\u0002\u0010\u000b\u001a\u00020\t\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\t\u0012\b\b\u0002\u0010\u000f\u001a\u00020\t\u00a2\u0006\u0002\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\n\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001aR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u001eR\u0015\u0010\u000e\u001a\u0004\u0018\u00010\t\u00a2\u0006\n\n\u0002\u0010!\u001a\u0004\b\u001f\u0010 R\u0011\u0010\u000f\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001aR\u0011\u0010\u000b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001a\u00a8\u0006$"}, d2={"Lokio/internal/ZipEntry;", "", "canonicalPath", "Lokio/Path;", "isDirectory", "", "comment", "", "crc", "", "compressedSize", "size", "compressionMethod", "", "lastModifiedAtMillis", "offset", "(Lokio/Path;ZLjava/lang/String;JJJILjava/lang/Long;J)V", "getCanonicalPath", "()Lokio/Path;", "children", "", "getChildren", "()Ljava/util/List;", "getComment", "()Ljava/lang/String;", "getCompressedSize", "()J", "getCompressionMethod", "()I", "getCrc", "()Z", "getLastModifiedAtMillis", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getOffset", "getSize", "okio"})
public final class ZipEntry {
    @NotNull
    private final Path canonicalPath;
    private final boolean isDirectory;
    @NotNull
    private final String comment;
    private final long crc;
    private final long compressedSize;
    private final long size;
    private final int compressionMethod;
    @Nullable
    private final Long lastModifiedAtMillis;
    private final long offset;
    @NotNull
    private final List<Path> children;

    public ZipEntry(@NotNull Path path, boolean bl, @NotNull String string, long l2, long l3, long l4, int n2, @Nullable Long l5, long l6) {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(string, "");
        this.canonicalPath = path;
        this.isDirectory = bl;
        this.comment = string;
        this.crc = l2;
        this.compressedSize = l3;
        this.size = l4;
        this.compressionMethod = n2;
        this.lastModifiedAtMillis = l5;
        this.offset = l6;
        this.children = new ArrayList();
    }

    public /* synthetic */ ZipEntry(Path path, boolean bl, String string, long l2, long l3, long l4, int n2, Long l5, long l6, int n3, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n3 & 2) != 0) {
            bl = false;
        }
        if ((n3 & 4) != 0) {
            string = "";
        }
        if ((n3 & 8) != 0) {
            l2 = -1L;
        }
        if ((n3 & 0x10) != 0) {
            l3 = -1L;
        }
        if ((n3 & 0x20) != 0) {
            l4 = -1L;
        }
        if ((n3 & 0x40) != 0) {
            n2 = -1;
        }
        if ((n3 & 0x80) != 0) {
            l5 = null;
        }
        if ((n3 & 0x100) != 0) {
            l6 = -1L;
        }
        this(path, bl, string, l2, l3, l4, n2, l5, l6);
    }

    @NotNull
    public final Path getCanonicalPath() {
        return this.canonicalPath;
    }

    public final boolean isDirectory() {
        return this.isDirectory;
    }

    @NotNull
    public final String getComment() {
        return this.comment;
    }

    public final long getCrc() {
        return this.crc;
    }

    public final long getCompressedSize() {
        return this.compressedSize;
    }

    public final long getSize() {
        return this.size;
    }

    public final int getCompressionMethod() {
        return this.compressionMethod;
    }

    @Nullable
    public final Long getLastModifiedAtMillis() {
        return this.lastModifiedAtMillis;
    }

    public final long getOffset() {
        return this.offset;
    }

    @NotNull
    public final List<Path> getChildren() {
        return this.children;
    }
}

