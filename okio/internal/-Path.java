/*
 * Decompiled with CFR 0.152.
 */
package okio.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import okio.Buffer;
import okio.ByteString;
import okio.Path;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=2, xi=48, d1={"\u0000H\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\f\n\u0002\b\u0006\n\u0002\u0010\u0005\n\u0000\u001a\u0015\u0010\u0014\u001a\u00020\r*\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u000eH\u0080\b\u001a\u0017\u0010\u0016\u001a\u00020\u0017*\u00020\u000e2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0018H\u0080\b\u001a\r\u0010\u0019\u001a\u00020\r*\u00020\u000eH\u0080\b\u001a\r\u0010\u001a\u001a\u00020\u0017*\u00020\u000eH\u0080\b\u001a\r\u0010\u001b\u001a\u00020\u0017*\u00020\u000eH\u0080\b\u001a\r\u0010\u001c\u001a\u00020\u0017*\u00020\u000eH\u0080\b\u001a\r\u0010\u001d\u001a\u00020\u001e*\u00020\u000eH\u0080\b\u001a\r\u0010\u001f\u001a\u00020\u0001*\u00020\u000eH\u0080\b\u001a\r\u0010 \u001a\u00020\u000e*\u00020\u000eH\u0080\b\u001a\u000f\u0010!\u001a\u0004\u0018\u00010\u000e*\u00020\u000eH\u0080\b\u001a\u0015\u0010\"\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u000eH\u0080\b\u001a\u001d\u0010#\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010$\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\u0017H\u0080\b\u001a\u001d\u0010#\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010$\u001a\u00020&2\u0006\u0010%\u001a\u00020\u0017H\u0080\b\u001a\u001d\u0010#\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010$\u001a\u00020\u00012\u0006\u0010%\u001a\u00020\u0017H\u0080\b\u001a\u001c\u0010#\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010$\u001a\u00020\u000e2\u0006\u0010%\u001a\u00020\u0017H\u0000\u001a\u000f\u0010'\u001a\u0004\u0018\u00010\u000e*\u00020\u000eH\u0080\b\u001a\u0013\u0010(\u001a\b\u0012\u0004\u0012\u00020\u001e0)*\u00020\u000eH\u0080\b\u001a\u0013\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00010)*\u00020\u000eH\u0080\b\u001a\u0014\u0010+\u001a\u00020\u000e*\u00020\u001e2\u0006\u0010%\u001a\u00020\u0017H\u0000\u001a\r\u0010,\u001a\u00020\u001e*\u00020\u000eH\u0080\b\u001a\u0014\u0010-\u001a\u0004\u0018\u00010.*\u00020\u000eH\u0080\b\u00a2\u0006\u0002\u0010/\u001a\f\u00100\u001a\u00020\u0017*\u00020\u000eH\u0002\u001a\f\u00101\u001a\u00020\r*\u00020\u000eH\u0002\u001a\u0014\u00102\u001a\u00020\u0017*\u00020&2\u0006\u0010\u0011\u001a\u00020\u0001H\u0002\u001a\u0014\u00103\u001a\u00020\u000e*\u00020&2\u0006\u0010%\u001a\u00020\u0017H\u0000\u001a\f\u00104\u001a\u00020\u0001*\u000205H\u0002\u001a\f\u00104\u001a\u00020\u0001*\u00020\u001eH\u0002\"\u0016\u0010\u0000\u001a\u00020\u00018\u0002X\u0083\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\u0002\u0010\u0003\"\u0016\u0010\u0004\u001a\u00020\u00018\u0002X\u0083\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\u0005\u0010\u0003\"\u0016\u0010\u0006\u001a\u00020\u00018\u0002X\u0083\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\u0007\u0010\u0003\"\u0016\u0010\b\u001a\u00020\u00018\u0002X\u0083\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\t\u0010\u0003\"\u0016\u0010\n\u001a\u00020\u00018\u0002X\u0083\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\u000b\u0010\u0003\"\u0018\u0010\f\u001a\u00020\r*\u00020\u000e8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010\"\u001a\u0010\u0011\u001a\u0004\u0018\u00010\u0001*\u00020\u000e8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013\u00a8\u00066"}, d2={"ANY_SLASH", "Lokio/ByteString;", "getANY_SLASH$annotations", "()V", "BACKSLASH", "getBACKSLASH$annotations", "DOT", "getDOT$annotations", "DOT_DOT", "getDOT_DOT$annotations", "SLASH", "getSLASH$annotations", "indexOfLastSlash", "", "Lokio/Path;", "getIndexOfLastSlash", "(Lokio/Path;)I", "slash", "getSlash", "(Lokio/Path;)Lokio/ByteString;", "commonCompareTo", "other", "commonEquals", "", "", "commonHashCode", "commonIsAbsolute", "commonIsRelative", "commonIsRoot", "commonName", "", "commonNameBytes", "commonNormalized", "commonParent", "commonRelativeTo", "commonResolve", "child", "normalize", "Lokio/Buffer;", "commonRoot", "commonSegments", "", "commonSegmentsBytes", "commonToPath", "commonToString", "commonVolumeLetter", "", "(Lokio/Path;)Ljava/lang/Character;", "lastSegmentIsDotDot", "rootLength", "startsWithVolumeLetterAndColon", "toPath", "toSlash", "", "okio"})
@JvmName(name="-Path")
@SourceDebugExtension(value={"SMAP\nPath.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Path.kt\nokio/internal/-Path\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,406:1\n59#1,22:407\n209#1:433\n209#1:434\n1549#2:429\n1620#2,3:430\n*S KotlinDebug\n*F\n+ 1 Path.kt\nokio/internal/-Path\n*L\n53#1:407,22\n199#1:433\n204#1:434\n53#1:429\n53#1:430,3\n*E\n"})
public final class -Path {
    @NotNull
    private static final ByteString SLASH = ByteString.Companion.encodeUtf8("/");
    @NotNull
    private static final ByteString BACKSLASH = ByteString.Companion.encodeUtf8("\\");
    @NotNull
    private static final ByteString ANY_SLASH = ByteString.Companion.encodeUtf8("/\\");
    @NotNull
    private static final ByteString DOT = ByteString.Companion.encodeUtf8(".");
    @NotNull
    private static final ByteString DOT_DOT = ByteString.Companion.encodeUtf8("..");

    private static /* synthetic */ void getSLASH$annotations() {
    }

    private static /* synthetic */ void getBACKSLASH$annotations() {
    }

    private static /* synthetic */ void getANY_SLASH$annotations() {
    }

    private static /* synthetic */ void getDOT$annotations() {
    }

    private static /* synthetic */ void getDOT_DOT$annotations() {
    }

    @Nullable
    public static final Path commonRoot(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        int n2 = -Path.rootLength(path);
        if (n2 == -1) {
            return null;
        }
        return new Path(path.getBytes$okio().substring(0, n2));
    }

    @NotNull
    public static final List<String> commonSegments(@NotNull Path object2) {
        Intrinsics.checkNotNullParameter(object2, "");
        Object object = object2;
        Iterable<String> iterable = new ArrayList();
        int n2 = -Path.rootLength((Path)object);
        if (n2 == -1) {
            n2 = 0;
        } else if (n2 < ((Path)object).getBytes$okio().size() && ((Path)object).getBytes$okio().getByte(n2) == 92) {
            ++n2;
        }
        int n3 = ((Path)object).getBytes$okio().size();
        for (int i2 = n2; i2 < n3; ++i2) {
            if (((Path)object).getBytes$okio().getByte(i2) != 47 && ((Path)object).getBytes$okio().getByte(i2) != 92) continue;
            ((Collection)iterable).add(((Path)object).getBytes$okio().substring(n2, i2));
            n2 = i2 + 1;
        }
        if (n2 < ((Path)object).getBytes$okio().size()) {
            ((Collection)iterable).add(((Path)object).getBytes$okio().substring(n2, ((Path)object).getBytes$okio().size()));
        }
        Iterable iterable2 = iterable;
        iterable = iterable2;
        Collection collection = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
        for (Object e2 : iterable) {
            ByteString byteString = (ByteString)e2;
            iterable = collection;
            iterable.add((String)byteString.utf8());
        }
        return (List)collection;
    }

    @NotNull
    public static final List<ByteString> commonSegmentsBytes(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        List list2 = new ArrayList();
        int n2 = -Path.rootLength(path);
        if (n2 == -1) {
            n2 = 0;
        } else if (n2 < path.getBytes$okio().size() && path.getBytes$okio().getByte(n2) == 92) {
            ++n2;
        }
        int n3 = path.getBytes$okio().size();
        for (int i2 = n2; i2 < n3; ++i2) {
            if (path.getBytes$okio().getByte(i2) != 47 && path.getBytes$okio().getByte(i2) != 92) continue;
            ((Collection)list2).add(path.getBytes$okio().substring(n2, i2));
            n2 = i2 + 1;
        }
        if (n2 < path.getBytes$okio().size()) {
            ((Collection)list2).add(path.getBytes$okio().substring(n2, path.getBytes$okio().size()));
        }
        return list2;
    }

    private static final int rootLength(Path path) {
        if (path.getBytes$okio().size() == 0) {
            return -1;
        }
        if (path.getBytes$okio().getByte(0) == 47) {
            return 1;
        }
        if (path.getBytes$okio().getByte(0) == 92) {
            if (path.getBytes$okio().size() > 2 && path.getBytes$okio().getByte(1) == 92) {
                int n2 = path.getBytes$okio().indexOf(BACKSLASH, 2);
                if (n2 == -1) {
                    n2 = path.getBytes$okio().size();
                }
                return n2;
            }
            return 1;
        }
        if (path.getBytes$okio().size() > 2 && path.getBytes$okio().getByte(1) == 58 && path.getBytes$okio().getByte(2) == 92) {
            char c2 = (char)path.getBytes$okio().getByte(0);
            if (!('a' <= c2 ? c2 < '{' : false)) {
                if (!('A' <= c2 ? c2 < '[' : false)) {
                    return -1;
                }
            }
            return 3;
        }
        return -1;
    }

    public static final boolean commonIsAbsolute(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        return -Path.rootLength(path) != -1;
    }

    public static final boolean commonIsRelative(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        return -Path.rootLength(path) == -1;
    }

    @Nullable
    public static final Character commonVolumeLetter(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        if (ByteString.indexOf$default(path.getBytes$okio(), -Path.SLASH, 0, 2, null) != -1) {
            return null;
        }
        if (path.getBytes$okio().size() < 2) {
            return null;
        }
        if (path.getBytes$okio().getByte(1) != 58) {
            return null;
        }
        char c2 = (char)path.getBytes$okio().getByte(0);
        if (!('a' <= c2 ? c2 < '{' : false)) {
            if (!('A' <= c2 ? c2 < '[' : false)) {
                return null;
            }
        }
        return Character.valueOf(c2);
    }

    private static final int getIndexOfLastSlash(Path path) {
        int n2 = ByteString.lastIndexOf$default(path.getBytes$okio(), SLASH, 0, 2, null);
        if (n2 != -1) {
            return n2;
        }
        return ByteString.lastIndexOf$default(path.getBytes$okio(), BACKSLASH, 0, 2, null);
    }

    @NotNull
    public static final ByteString commonNameBytes(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        int n2 = -Path.getIndexOfLastSlash(path);
        if (n2 != -1) {
            return ByteString.substring$default(path.getBytes$okio(), n2 + 1, 0, 2, null);
        }
        if (path.volumeLetter() != null && path.getBytes$okio().size() == 2) {
            return ByteString.EMPTY;
        }
        return path.getBytes$okio();
    }

    @NotNull
    public static final String commonName(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        return path.nameBytes().utf8();
    }

    @Nullable
    public static final Path commonParent(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        if (Intrinsics.areEqual(path.getBytes$okio(), -Path.DOT) || Intrinsics.areEqual(path.getBytes$okio(), -Path.SLASH) || Intrinsics.areEqual(path.getBytes$okio(), -Path.BACKSLASH) || -Path.lastSegmentIsDotDot(path)) {
            return null;
        }
        int n2 = -Path.getIndexOfLastSlash(path);
        if (n2 == 2 && path.volumeLetter() != null) {
            if (path.getBytes$okio().size() == 3) {
                return null;
            }
            return new Path(ByteString.substring$default(path.getBytes$okio(), 0, 3, 1, null));
        }
        if (n2 == 1 && path.getBytes$okio().startsWith(-Path.BACKSLASH)) {
            return null;
        }
        if (n2 == -1 && path.volumeLetter() != null) {
            if (path.getBytes$okio().size() == 2) {
                return null;
            }
            return new Path(ByteString.substring$default(path.getBytes$okio(), 0, 2, 1, null));
        }
        if (n2 == -1) {
            return new Path(-Path.DOT);
        }
        if (n2 == 0) {
            return new Path(ByteString.substring$default(path.getBytes$okio(), 0, 1, 1, null));
        }
        return new Path(ByteString.substring$default(path.getBytes$okio(), 0, n2, 1, null));
    }

    private static final boolean lastSegmentIsDotDot(Path path) {
        if (path.getBytes$okio().endsWith(DOT_DOT)) {
            if (path.getBytes$okio().size() == 2) {
                return true;
            }
            if (path.getBytes$okio().rangeEquals(path.getBytes$okio().size() - 3, SLASH, 0, 1)) {
                return true;
            }
            if (path.getBytes$okio().rangeEquals(path.getBytes$okio().size() - 3, BACKSLASH, 0, 1)) {
                return true;
            }
        }
        return false;
    }

    public static final boolean commonIsRoot(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        return -Path.rootLength(path) == path.getBytes$okio().size();
    }

    @NotNull
    public static final Path commonResolve(@NotNull Path path, @NotNull String object, boolean bl) {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(object, "");
        object = new Buffer().writeUtf8((String)object);
        return -Path.commonResolve(path, -Path.toPath((Buffer)object, false), bl);
    }

    @NotNull
    public static final Path commonResolve(@NotNull Path path, @NotNull ByteString object, boolean bl) {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(object, "");
        object = new Buffer().write((ByteString)object);
        return -Path.commonResolve(path, -Path.toPath((Buffer)object, false), bl);
    }

    @NotNull
    public static final Path commonResolve(@NotNull Path path, @NotNull Buffer buffer, boolean bl) {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(buffer, "");
        return -Path.commonResolve(path, -Path.toPath(buffer, false), bl);
    }

    @NotNull
    public static final Path commonResolve(@NotNull Path path, @NotNull Path path2, boolean bl) {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(path2, "");
        if (path2.isAbsolute() || path2.volumeLetter() != null) {
            return path2;
        }
        ByteString byteString = -Path.getSlash(path);
        if (byteString == null && (byteString = -Path.getSlash(path2)) == null) {
            byteString = -Path.toSlash(Path.DIRECTORY_SEPARATOR);
        }
        ByteString byteString2 = byteString;
        Buffer buffer = new Buffer();
        buffer.write(path.getBytes$okio());
        if (buffer.size() > 0L) {
            buffer.write(byteString2);
        }
        buffer.write(path2.getBytes$okio());
        return -Path.toPath(buffer, bl);
    }

    @NotNull
    public static final Path commonRelativeTo(@NotNull Path object, @NotNull Path path) {
        int n2;
        int n3;
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(path, "");
        if (!Intrinsics.areEqual(((Path)object).getRoot(), path.getRoot())) {
            String string = "Paths of different roots cannot be relative to each other: " + object + " and " + path;
            throw new IllegalArgumentException(string.toString());
        }
        List<ByteString> list2 = ((Path)object).getSegmentsBytes();
        List<ByteString> list3 = path.getSegmentsBytes();
        int n4 = Math.min(list2.size(), list3.size());
        for (n3 = 0; n3 < n4 && Intrinsics.areEqual(list2.get(n3), list3.get(n3)); ++n3) {
        }
        if (n3 == n4 && ((Path)object).getBytes$okio().size() == path.getBytes$okio().size()) {
            return Path.Companion.get$default(Path.Companion, ".", false, 1, null);
        }
        if (!(list3.subList(n3, list3.size()).indexOf(-Path.DOT_DOT) == -1)) {
            object = "Impossible relative path to resolve: " + object + " and " + path;
            throw new IllegalArgumentException(object.toString());
        }
        Buffer buffer = new Buffer();
        ByteString byteString = -Path.getSlash(path);
        if (byteString == null && (byteString = -Path.getSlash((Path)object)) == null) {
            byteString = -Path.toSlash(Path.DIRECTORY_SEPARATOR);
        }
        object = byteString;
        int n5 = list3.size();
        for (n2 = n3; n2 < n5; ++n2) {
            buffer.write(-Path.DOT_DOT);
            buffer.write((ByteString)object);
        }
        n5 = list2.size();
        for (n2 = n3; n2 < n5; ++n2) {
            buffer.write(list2.get(n2));
            buffer.write((ByteString)object);
        }
        return -Path.toPath(buffer, false);
    }

    @NotNull
    public static final Path commonNormalized(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        return Path.Companion.get(path.toString(), true);
    }

    private static final ByteString getSlash(Path path) {
        if (ByteString.indexOf$default(path.getBytes$okio(), SLASH, 0, 2, null) != -1) {
            return SLASH;
        }
        if (ByteString.indexOf$default(path.getBytes$okio(), BACKSLASH, 0, 2, null) != -1) {
            return BACKSLASH;
        }
        return null;
    }

    public static final int commonCompareTo(@NotNull Path path, @NotNull Path path2) {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(path2, "");
        return path.getBytes$okio().compareTo(path2.getBytes$okio());
    }

    public static final boolean commonEquals(@NotNull Path path, @Nullable Object object) {
        Intrinsics.checkNotNullParameter(path, "");
        return object instanceof Path && Intrinsics.areEqual(((Path)object).getBytes$okio(), path.getBytes$okio());
    }

    public static final int commonHashCode(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        return path.getBytes$okio().hashCode();
    }

    @NotNull
    public static final String commonToString(@NotNull Path path) {
        Intrinsics.checkNotNullParameter(path, "");
        return path.getBytes$okio().utf8();
    }

    @NotNull
    public static final Path commonToPath(@NotNull String string, boolean bl) {
        Intrinsics.checkNotNullParameter(string, "");
        return -Path.toPath(new Buffer().writeUtf8(string), bl);
    }

    @NotNull
    public static final Path toPath(@NotNull Buffer buffer, boolean bl) {
        byte by;
        Intrinsics.checkNotNullParameter(buffer, "");
        ByteString byteString = null;
        Buffer buffer2 = new Buffer();
        int n2 = 0;
        while (buffer.rangeEquals(0L, SLASH) || buffer.rangeEquals(0L, BACKSLASH)) {
            by = buffer.readByte();
            ByteString byteString2 = byteString;
            if (byteString2 == null) {
                byteString2 = -Path.toSlash(by);
            }
            byteString = byteString2;
            ++n2;
        }
        by = n2 >= 2 && Intrinsics.areEqual(byteString, BACKSLASH) ? (byte)1 : 0;
        if (by != 0) {
            ByteString byteString3 = byteString;
            Intrinsics.checkNotNull(byteString3);
            buffer2.write(byteString3);
            buffer2.write(byteString);
        } else if (n2 > 0) {
            ByteString byteString4 = byteString;
            Intrinsics.checkNotNull(byteString4);
            buffer2.write(byteString4);
        } else {
            long l2 = buffer.indexOfElement(ANY_SLASH);
            ByteString byteString5 = byteString;
            if (byteString5 == null) {
                byteString5 = l2 == -1L ? -Path.toSlash(Path.DIRECTORY_SEPARATOR) : -Path.toSlash(buffer.getByte(l2));
            }
            if (-Path.startsWithVolumeLetterAndColon(buffer, byteString = byteString5)) {
                if (l2 == 2L) {
                    buffer2.write(buffer, 3L);
                } else {
                    buffer2.write(buffer, 2L);
                }
            }
        }
        boolean bl2 = buffer2.size() > 0L;
        List list2 = new ArrayList();
        while (!buffer.exhausted()) {
            ByteString byteString6;
            long l3 = buffer.indexOfElement(ANY_SLASH);
            if (l3 == -1L) {
                byteString6 = buffer.readByteString();
            } else {
                byteString6 = buffer.readByteString(l3);
                buffer.readByte();
            }
            if (Intrinsics.areEqual(byteString6, DOT_DOT)) {
                if (bl2 && list2.isEmpty()) continue;
                if (!bl || !bl2 && (list2.isEmpty() || Intrinsics.areEqual(CollectionsKt.last(list2), DOT_DOT))) {
                    list2.add(byteString6);
                    continue;
                }
                if (by != 0 && list2.size() == 1) continue;
                CollectionsKt.removeLastOrNull(list2);
                continue;
            }
            if (Intrinsics.areEqual(byteString6, DOT) || Intrinsics.areEqual(byteString6, ByteString.EMPTY)) continue;
            list2.add(byteString6);
        }
        int n3 = list2.size();
        for (int i2 = 0; i2 < n3; ++i2) {
            if (i2 > 0) {
                buffer2.write(byteString);
            }
            buffer2.write((ByteString)list2.get(i2));
        }
        if (buffer2.size() == 0L) {
            buffer2.write(DOT);
        }
        return new Path(buffer2.readByteString());
    }

    private static final ByteString toSlash(String string) {
        String string2 = string;
        if (Intrinsics.areEqual(string2, "/")) {
            return SLASH;
        }
        if (Intrinsics.areEqual(string2, "\\")) {
            return BACKSLASH;
        }
        throw new IllegalArgumentException("not a directory separator: " + string);
    }

    private static final ByteString toSlash(byte by) {
        switch (by) {
            case 47: {
                return SLASH;
            }
            case 92: {
                return BACKSLASH;
            }
        }
        throw new IllegalArgumentException("not a directory separator: " + by);
    }

    private static final boolean startsWithVolumeLetterAndColon(Buffer buffer, ByteString byteString) {
        block7: {
            block6: {
                if (!Intrinsics.areEqual(byteString, BACKSLASH)) {
                    return false;
                }
                if (buffer.size() < 2L) {
                    return false;
                }
                if (buffer.getByte(1L) != 58) {
                    return false;
                }
                char c2 = (char)buffer.getByte(0L);
                if ('a' <= c2 ? c2 < '{' : false) break block6;
                boolean bl = 'A' <= c2 ? c2 < '[' : false;
                if (!bl) break block7;
            }
            return true;
        }
        return false;
    }
}

