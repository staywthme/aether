/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;
import kotlin.reflect.KClasses;
import okio.Path;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001Bo\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\b\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\b\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\b\u0012\u0018\b\u0002\u0010\f\u001a\u0012\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u000e\u0012\u0004\u0012\u00020\u00010\r\u00a2\u0006\u0002\u0010\u000fJu\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\b2\u0018\b\u0002\u0010\f\u001a\u0012\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u000e\u0012\u0004\u0012\u00020\u00010\r\u00a2\u0006\u0002\u0010\u001cJ'\u0010\u001d\u001a\u0004\u0018\u0001H\u001e\"\b\b\u0000\u0010\u001e*\u00020\u00012\u000e\u0010\u001f\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u001e0\u000e\u00a2\u0006\u0002\u0010 J\b\u0010!\u001a\u00020\"H\u0016R\u0015\u0010\t\u001a\u0004\u0018\u00010\b\u00a2\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0010\u0010\u0011R!\u0010\f\u001a\u0012\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u000e\u0012\u0004\u0012\u00020\u00010\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0015R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0015R\u0015\u0010\u000b\u001a\u0004\u0018\u00010\b\u00a2\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0016\u0010\u0011R\u0015\u0010\n\u001a\u0004\u0018\u00010\b\u00a2\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0017\u0010\u0011R\u0015\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0018\u0010\u0011R\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001a\u00a8\u0006#"}, d2={"Lokio/FileMetadata;", "", "isRegularFile", "", "isDirectory", "symlinkTarget", "Lokio/Path;", "size", "", "createdAtMillis", "lastModifiedAtMillis", "lastAccessedAtMillis", "extras", "", "Lkotlin/reflect/KClass;", "(ZZLokio/Path;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/Long;Ljava/util/Map;)V", "getCreatedAtMillis", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getExtras", "()Ljava/util/Map;", "()Z", "getLastAccessedAtMillis", "getLastModifiedAtMillis", "getSize", "getSymlinkTarget", "()Lokio/Path;", "copy", "(ZZLokio/Path;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/Long;Ljava/util/Map;)Lokio/FileMetadata;", "extra", "T", "type", "(Lkotlin/reflect/KClass;)Ljava/lang/Object;", "toString", "", "okio"})
public final class FileMetadata {
    private final boolean isRegularFile;
    private final boolean isDirectory;
    @Nullable
    private final Path symlinkTarget;
    @Nullable
    private final Long size;
    @Nullable
    private final Long createdAtMillis;
    @Nullable
    private final Long lastModifiedAtMillis;
    @Nullable
    private final Long lastAccessedAtMillis;
    @NotNull
    private final Map<KClass<?>, Object> extras;

    public FileMetadata(boolean bl, boolean bl2, @Nullable Path path, @Nullable Long l2, @Nullable Long l3, @Nullable Long l4, @Nullable Long l5, @NotNull Map<KClass<?>, ? extends Object> map) {
        Intrinsics.checkNotNullParameter(map, "");
        this.isRegularFile = bl;
        this.isDirectory = bl2;
        this.symlinkTarget = path;
        this.size = l2;
        this.createdAtMillis = l3;
        this.lastModifiedAtMillis = l4;
        this.lastAccessedAtMillis = l5;
        this.extras = MapsKt.toMap(map);
    }

    public /* synthetic */ FileMetadata(boolean bl, boolean bl2, Path path, Long l2, Long l3, Long l4, Long l5, Map map, int n2, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n2 & 1) != 0) {
            bl = false;
        }
        if ((n2 & 2) != 0) {
            bl2 = false;
        }
        if ((n2 & 4) != 0) {
            path = null;
        }
        if ((n2 & 8) != 0) {
            l2 = null;
        }
        if ((n2 & 0x10) != 0) {
            l3 = null;
        }
        if ((n2 & 0x20) != 0) {
            l4 = null;
        }
        if ((n2 & 0x40) != 0) {
            l5 = null;
        }
        if ((n2 & 0x80) != 0) {
            map = MapsKt.emptyMap();
        }
        this(bl, bl2, path, l2, l3, l4, l5, map);
    }

    public final boolean isRegularFile() {
        return this.isRegularFile;
    }

    public final boolean isDirectory() {
        return this.isDirectory;
    }

    @Nullable
    public final Path getSymlinkTarget() {
        return this.symlinkTarget;
    }

    @Nullable
    public final Long getSize() {
        return this.size;
    }

    @Nullable
    public final Long getCreatedAtMillis() {
        return this.createdAtMillis;
    }

    @Nullable
    public final Long getLastModifiedAtMillis() {
        return this.lastModifiedAtMillis;
    }

    @Nullable
    public final Long getLastAccessedAtMillis() {
        return this.lastAccessedAtMillis;
    }

    @NotNull
    public final Map<KClass<?>, Object> getExtras() {
        return this.extras;
    }

    @Nullable
    public final <T> T extra(@NotNull KClass<? extends T> kClass) {
        Intrinsics.checkNotNullParameter(kClass, "");
        Object object = this.extras.get(kClass);
        if (object == null) {
            return null;
        }
        Object object2 = object;
        return KClasses.cast(kClass, object2);
    }

    @NotNull
    public final FileMetadata copy(boolean bl, boolean bl2, @Nullable Path path, @Nullable Long l2, @Nullable Long l3, @Nullable Long l4, @Nullable Long l5, @NotNull Map<KClass<?>, ? extends Object> map) {
        Intrinsics.checkNotNullParameter(map, "");
        return new FileMetadata(bl, bl2, path, l2, l3, l4, l5, map);
    }

    public static /* synthetic */ FileMetadata copy$default(FileMetadata fileMetadata, boolean bl, boolean bl2, Path path, Long l2, Long l3, Long l4, Long l5, Map map, int n2, Object object) {
        if ((n2 & 1) != 0) {
            bl = fileMetadata.isRegularFile;
        }
        if ((n2 & 2) != 0) {
            bl2 = fileMetadata.isDirectory;
        }
        if ((n2 & 4) != 0) {
            path = fileMetadata.symlinkTarget;
        }
        if ((n2 & 8) != 0) {
            l2 = fileMetadata.size;
        }
        if ((n2 & 0x10) != 0) {
            l3 = fileMetadata.createdAtMillis;
        }
        if ((n2 & 0x20) != 0) {
            l4 = fileMetadata.lastModifiedAtMillis;
        }
        if ((n2 & 0x40) != 0) {
            l5 = fileMetadata.lastAccessedAtMillis;
        }
        if ((n2 & 0x80) != 0) {
            map = fileMetadata.extras;
        }
        return fileMetadata.copy(bl, bl2, path, l2, l3, l4, l5, map);
    }

    @NotNull
    public final String toString() {
        List list2 = new ArrayList();
        if (this.isRegularFile) {
            ((Collection)list2).add("isRegularFile");
        }
        if (this.isDirectory) {
            ((Collection)list2).add("isDirectory");
        }
        if (this.size != null) {
            ((Collection)list2).add("byteCount=" + this.size);
        }
        if (this.createdAtMillis != null) {
            ((Collection)list2).add("createdAt=" + this.createdAtMillis);
        }
        if (this.lastModifiedAtMillis != null) {
            ((Collection)list2).add("lastModifiedAt=" + this.lastModifiedAtMillis);
        }
        if (this.lastAccessedAtMillis != null) {
            ((Collection)list2).add("lastAccessedAt=" + this.lastAccessedAtMillis);
        }
        if (!this.extras.isEmpty()) {
            ((Collection)list2).add("extras=" + this.extras);
        }
        return CollectionsKt.joinToString$default(list2, ", ", "FileMetadata(", ")", 0, null, null, 56, null);
    }

    public FileMetadata() {
        this(false, false, null, null, null, null, null, null, 255, null);
    }
}

