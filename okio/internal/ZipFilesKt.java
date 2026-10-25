/*
 * Decompiled with CFR 0.152.
 */
package okio.internal;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;
import okio.BufferedSource;
import okio.FileHandle;
import okio.FileMetadata;
import okio.FileSystem;
import okio.Okio;
import okio.Path;
import okio.ZipFileSystem;
import okio.internal.EocdRecord;
import okio.internal.ZipEntry;
import okio.internal.ZipFilesKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=2, xi=48, d1={"\u0000j\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a\"\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00150\u00132\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00150\u0017H\u0002\u001a\u001f\u0010\u0018\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0019\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u00020\u0001H\u0002\u00a2\u0006\u0002\u0010\u001b\u001a.\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u00142\u0006\u0010\u001f\u001a\u00020 2\u0014\b\u0002\u0010!\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020#0\"H\u0000\u001a\f\u0010$\u001a\u00020\u0015*\u00020%H\u0000\u001a\f\u0010&\u001a\u00020'*\u00020%H\u0002\u001a.\u0010(\u001a\u00020)*\u00020%2\u0006\u0010*\u001a\u00020\u00012\u0018\u0010+\u001a\u0014\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020)0,H\u0002\u001a\u0014\u0010-\u001a\u00020.*\u00020%2\u0006\u0010/\u001a\u00020.H\u0000\u001a\u0018\u00100\u001a\u0004\u0018\u00010.*\u00020%2\b\u0010/\u001a\u0004\u0018\u00010.H\u0002\u001a\u0014\u00101\u001a\u00020'*\u00020%2\u0006\u00102\u001a\u00020'H\u0002\u001a\f\u00103\u001a\u00020)*\u00020%H\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001X\u0080T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0001X\u0080T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\t\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u000bX\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\f\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\r\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u0018\u0010\u000e\u001a\u00020\u000f*\u00020\u00018BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011\u00a8\u00064"}, d2={"BIT_FLAG_ENCRYPTED", "", "BIT_FLAG_UNSUPPORTED_MASK", "CENTRAL_FILE_HEADER_SIGNATURE", "COMPRESSION_METHOD_DEFLATED", "COMPRESSION_METHOD_STORED", "END_OF_CENTRAL_DIRECTORY_SIGNATURE", "HEADER_ID_EXTENDED_TIMESTAMP", "HEADER_ID_ZIP64_EXTENDED_INFO", "LOCAL_FILE_HEADER_SIGNATURE", "MAX_ZIP_ENTRY_AND_ARCHIVE_SIZE", "", "ZIP64_EOCD_RECORD_SIGNATURE", "ZIP64_LOCATOR_SIGNATURE", "hex", "", "getHex", "(I)Ljava/lang/String;", "buildIndex", "", "Lokio/Path;", "Lokio/internal/ZipEntry;", "entries", "", "dosDateTimeToEpochMillis", "date", "time", "(II)Ljava/lang/Long;", "openZip", "Lokio/ZipFileSystem;", "zipPath", "fileSystem", "Lokio/FileSystem;", "predicate", "Lkotlin/Function1;", "", "readEntry", "Lokio/BufferedSource;", "readEocdRecord", "Lokio/internal/EocdRecord;", "readExtra", "", "extraSize", "block", "Lkotlin/Function2;", "readLocalHeader", "Lokio/FileMetadata;", "basicMetadata", "readOrSkipLocalHeader", "readZip64EocdRecord", "regularRecord", "skipLocalHeader", "okio"})
@SourceDebugExtension(value={"SMAP\nZipFiles.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ZipFiles.kt\nokio/internal/ZipFilesKt\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,459:1\n1045#2:460\n*S KotlinDebug\n*F\n+ 1 ZipFiles.kt\nokio/internal/ZipFilesKt\n*L\n156#1:460\n*E\n"})
public final class ZipFilesKt {
    private static final int LOCAL_FILE_HEADER_SIGNATURE = 67324752;
    private static final int CENTRAL_FILE_HEADER_SIGNATURE = 33639248;
    private static final int END_OF_CENTRAL_DIRECTORY_SIGNATURE = 101010256;
    private static final int ZIP64_LOCATOR_SIGNATURE = 117853008;
    private static final int ZIP64_EOCD_RECORD_SIGNATURE = 101075792;
    public static final int COMPRESSION_METHOD_DEFLATED = 8;
    public static final int COMPRESSION_METHOD_STORED = 0;
    private static final int BIT_FLAG_ENCRYPTED = 1;
    private static final int BIT_FLAG_UNSUPPORTED_MASK = 1;
    private static final long MAX_ZIP_ENTRY_AND_ARCHIVE_SIZE = 0xFFFFFFFFL;
    private static final int HEADER_ID_ZIP64_EXTENDED_INFO = 1;
    private static final int HEADER_ID_EXTENDED_TIMESTAMP = 21589;

    @NotNull
    public static final ZipFileSystem openZip(@NotNull Path object, @NotNull FileSystem fileSystem, @NotNull Function1<? super ZipEntry, Boolean> function1) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(fileSystem, "");
        Intrinsics.checkNotNullParameter(function1, "");
        Closeable closeable = fileSystem.openReadOnly((Path)object);
        Throwable throwable = null;
        try {
            Object object2;
            long l2;
            Map<Path, ZipEntry> map;
            String string;
            EocdRecord eocdRecord;
            long l3;
            Object object3;
            Object object4;
            block26: {
                object4 = (FileHandle)closeable;
                long l4 = ((FileHandle)object4).size() - 22L;
                if (l4 < 0L) {
                    throw new IOException("not a zip: size=" + ((FileHandle)object4).size());
                }
                long l5 = Math.max(l4 - 65536L, 0L);
                do {
                    object3 = Okio.buffer(((FileHandle)object4).source(l4));
                    try {
                        if (object3.readIntLe() != 101010256) continue;
                        l3 = l4;
                        eocdRecord = ZipFilesKt.readEocdRecord((BufferedSource)object3);
                        string = object3.readUtf8(eocdRecord.getCommentByteCount());
                        break block26;
                    }
                    finally {
                        object3.close();
                    }
                } while ((l4 += -1L) >= l5);
                throw new IOException("not a zip: end of central directory signature not found");
            }
            long l6 = l3 - 20L;
            if (l6 > 0L) {
                Closeable closeable2;
                block27: {
                    closeable2 = Okio.buffer(((FileHandle)object4).source(l6));
                    map = null;
                    try {
                        Closeable closeable3 = (BufferedSource)closeable2;
                        if (closeable3.readIntLe() != 117853008) break block27;
                        int n2 = closeable3.readIntLe();
                        l2 = closeable3.readLongLe();
                        int n3 = closeable3.readIntLe();
                        if (n3 != 1 || n2 != 0) {
                            throw new IOException("unsupported zip: spanned");
                        }
                        closeable3 = Okio.buffer(((FileHandle)object4).source(l2));
                        object2 = null;
                        try {
                            object3 = closeable3;
                            int n4 = object3.readIntLe();
                            if (n4 != 101075792) {
                                throw new IOException("bad zip: expected " + ZipFilesKt.getHex(101075792) + " but was " + ZipFilesKt.getHex(n4));
                            }
                            eocdRecord = ZipFilesKt.readZip64EocdRecord((BufferedSource)object3, eocdRecord);
                        }
                        catch (Throwable throwable2) {
                            try {
                                object3 = throwable2;
                                object2 = throwable2;
                                throw object3;
                            }
                            catch (Throwable throwable3) {
                                CloseableKt.closeFinally(closeable3, (Throwable)object2);
                                throw throwable3;
                            }
                        }
                        CloseableKt.closeFinally(closeable3, null);
                    }
                    catch (Throwable throwable4) {
                        try {
                            Throwable throwable5 = throwable4;
                            map = throwable4;
                            throw throwable5;
                        }
                        catch (Throwable throwable6) {
                            CloseableKt.closeFinally(closeable2, (Throwable)((Object)map));
                            throw throwable6;
                        }
                    }
                }
                CloseableKt.closeFinally(closeable2, null);
            }
            List list2 = new ArrayList();
            map = Okio.buffer(((FileHandle)object4).source(eocdRecord.getCentralDirectoryOffset()));
            Throwable throwable7 = null;
            try {
                object4 = (BufferedSource)((Object)map);
                long l7 = eocdRecord.getEntryCount();
                for (l2 = 0L; l2 < l7; ++l2) {
                    object2 = ZipFilesKt.readEntry((BufferedSource)object4);
                    if (((ZipEntry)object2).getOffset() >= eocdRecord.getCentralDirectoryOffset()) {
                        throw new IOException("bad zip: local file header offset >= central directory offset");
                    }
                    if (!function1.invoke((ZipEntry)object2).booleanValue()) continue;
                    ((Collection)list2).add(object2);
                }
            }
            catch (Throwable throwable8) {
                try {
                    object4 = throwable8;
                    throwable7 = throwable8;
                    throw object4;
                }
                catch (Throwable throwable9) {
                    CloseableKt.closeFinally((Closeable)((Object)map), throwable7);
                    throw throwable9;
                }
            }
            CloseableKt.closeFinally((Closeable)((Object)map), null);
            map = ZipFilesKt.buildIndex(list2);
            object = new ZipFileSystem((Path)object, fileSystem, map, string);
        }
        catch (Throwable throwable10) {
            try {
                Throwable throwable11 = throwable10;
                throwable = throwable10;
                throw throwable11;
            }
            catch (Throwable throwable12) {
                CloseableKt.closeFinally(closeable, throwable);
                throw throwable12;
            }
        }
        CloseableKt.closeFinally(closeable, null);
        return object;
    }

    public static /* synthetic */ ZipFileSystem openZip$default(Path path, FileSystem fileSystem, Function1 function1, int n2, Object object) throws IOException {
        if ((n2 & 4) != 0) {
            function1 = openZip.1.INSTANCE;
        }
        return ZipFilesKt.openZip(path, fileSystem, function1);
    }

    private static final Map<Path, ZipEntry> buildIndex(List<ZipEntry> object32) {
        Object object2 = Path.Companion.get$default(Path.Companion, "/", false, 1, null);
        Pair[] pairArray = new Pair[1];
        Pair[] pairArray2 = pairArray;
        pairArray[0] = TuplesKt.to(object2, new ZipEntry((Path)object2, true, null, 0L, 0L, 0L, 0, null, 0L, 508, null));
        object2 = MapsKt.mutableMapOf(pairArray2);
        object32 = (Iterable)object32;
        block0: for (Object object32 : CollectionsKt.sortedWith(object32, new Comparator(){

            public final int compare(T object, T object2) {
                ZipEntry zipEntry = (ZipEntry)object;
                object = zipEntry;
                object = (ZipEntry)object2;
                object2 = zipEntry.getCanonicalPath();
                return ComparisonsKt.compareValues(object2, (Comparable)((ZipEntry)object).getCanonicalPath());
            }
        })) {
            ZipEntry zipEntry = (ZipEntry)object2.put(((ZipEntry)object32).getCanonicalPath(), object32);
            if (zipEntry != null) continue;
            while (((ZipEntry)object32).getCanonicalPath().parent() != null) {
                ZipEntry zipEntry2 = (ZipEntry)object2.get(zipEntry);
                if (zipEntry2 != null) {
                    ((Collection)zipEntry2.getChildren()).add(((ZipEntry)object32).getCanonicalPath());
                    continue block0;
                }
                zipEntry2 = new ZipEntry((Path)((Object)zipEntry), true, null, 0L, 0L, 0L, 0, null, 0L, 508, null);
                object2.put(zipEntry, zipEntry2);
                ((Collection)zipEntry2.getChildren()).add(((ZipEntry)object32).getCanonicalPath());
                object32 = zipEntry2;
            }
        }
        return object2;
    }

    @NotNull
    public static final ZipEntry readEntry(@NotNull BufferedSource object) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        int n2 = object.readIntLe();
        if (n2 != 33639248) {
            throw new IOException("bad zip: expected " + ZipFilesKt.getHex(33639248) + " but was " + ZipFilesKt.getHex(n2));
        }
        object.skip(4L);
        n2 = object.readShortLe() & 0xFFFF;
        if ((n2 & 1) != 0) {
            throw new IOException("unsupported zip: general purpose bit flag=" + ZipFilesKt.getHex(n2));
        }
        n2 = object.readShortLe() & 0xFFFF;
        int n3 = object.readShortLe() & 0xFFFF;
        int n4 = object.readShortLe() & 0xFFFF;
        Long l2 = ZipFilesKt.dosDateTimeToEpochMillis(n4, n3);
        long l3 = (long)object.readIntLe() & 0xFFFFFFFFL;
        Ref.LongRef longRef = new Ref.LongRef();
        new Ref.LongRef().element = (long)object.readIntLe() & 0xFFFFFFFFL;
        Ref.LongRef longRef2 = new Ref.LongRef();
        new Ref.LongRef().element = (long)object.readIntLe() & 0xFFFFFFFFL;
        int n5 = object.readShortLe() & 0xFFFF;
        int n6 = object.readShortLe() & 0xFFFF;
        int n7 = object.readShortLe() & 0xFFFF;
        object.skip(8L);
        Ref.LongRef longRef3 = new Ref.LongRef();
        new Ref.LongRef().element = (long)object.readIntLe() & 0xFFFFFFFFL;
        String string = object.readUtf8(n5);
        if (StringsKt.contains$default((CharSequence)string, '\u0000', false, 2, null)) {
            throw new IOException("bad zip: filename contains 0x00");
        }
        long l4 = 0L;
        if (longRef2.element == 0xFFFFFFFFL) {
            l4 = 8L;
        }
        if (longRef.element == 0xFFFFFFFFL) {
            l4 += 8L;
        }
        if (longRef3.element == 0xFFFFFFFFL) {
            l4 += 8L;
        }
        long l5 = l4;
        Ref.BooleanRef booleanRef = new Ref.BooleanRef();
        ZipFilesKt.readExtra((BufferedSource)object, n6, (Function2<? super Integer, ? super Long, Unit>)new Function2<Integer, Long, Unit>(booleanRef, l5, longRef2, (BufferedSource)object, longRef, longRef3){
            final /* synthetic */ Ref.BooleanRef $hasZip64Extra;
            final /* synthetic */ long $requiredZip64ExtraSize;
            final /* synthetic */ Ref.LongRef $size;
            final /* synthetic */ BufferedSource $this_readEntry;
            final /* synthetic */ Ref.LongRef $compressedSize;
            final /* synthetic */ Ref.LongRef $offset;
            {
                this.$hasZip64Extra = booleanRef;
                this.$requiredZip64ExtraSize = l2;
                this.$size = longRef;
                this.$this_readEntry = bufferedSource;
                this.$compressedSize = longRef2;
                this.$offset = longRef3;
                super(2);
            }

            public final void invoke(int n2, long l2) {
                if (n2 == 1) {
                    if (this.$hasZip64Extra.element) {
                        throw new IOException("bad zip: zip64 extra repeated");
                    }
                    this.$hasZip64Extra.element = true;
                    if (l2 < this.$requiredZip64ExtraSize) {
                        throw new IOException("bad zip: zip64 extra too short");
                    }
                    this.$size.element = this.$size.element == 0xFFFFFFFFL ? this.$this_readEntry.readLongLe() : this.$size.element;
                    this.$compressedSize.element = this.$compressedSize.element == 0xFFFFFFFFL ? this.$this_readEntry.readLongLe() : 0L;
                    this.$offset.element = this.$offset.element == 0xFFFFFFFFL ? this.$this_readEntry.readLongLe() : 0L;
                }
            }

            public final /* synthetic */ Object invoke(Object object, Object object2) {
                this.invoke(((Number)object).intValue(), ((Number)object2).longValue());
                return Unit.INSTANCE;
            }
        });
        if (l5 > 0L && !booleanRef.element) {
            throw new IOException("bad zip: zip64 extra required but absent");
        }
        object = object.readUtf8(n7);
        Path path = Path.Companion.get$default(Path.Companion, "/", false, 1, null).resolve(string);
        boolean bl = StringsKt.endsWith$default(string, "/", false, 2, null);
        return new ZipEntry(path, bl, (String)object, l3, longRef.element, longRef2.element, n2, l2, longRef3.element);
    }

    private static final EocdRecord readEocdRecord(BufferedSource bufferedSource) throws IOException {
        long l2;
        int n2 = bufferedSource.readShortLe() & 0xFFFF;
        int n3 = bufferedSource.readShortLe() & 0xFFFF;
        long l3 = bufferedSource.readShortLe() & 0xFFFF;
        if (l3 != (l2 = (long)(bufferedSource.readShortLe() & 0xFFFF)) || n2 != 0 || n3 != 0) {
            throw new IOException("unsupported zip: spanned");
        }
        bufferedSource.skip(4L);
        long l4 = (long)bufferedSource.readIntLe() & 0xFFFFFFFFL;
        int n4 = bufferedSource.readShortLe() & 0xFFFF;
        return new EocdRecord(l3, l4, n4);
    }

    private static final EocdRecord readZip64EocdRecord(BufferedSource bufferedSource, EocdRecord eocdRecord) throws IOException {
        bufferedSource.skip(12L);
        int n2 = bufferedSource.readIntLe();
        int n3 = bufferedSource.readIntLe();
        long l2 = bufferedSource.readLongLe();
        long l3 = bufferedSource.readLongLe();
        if (l2 != l3 || n2 != 0 || n3 != 0) {
            throw new IOException("unsupported zip: spanned");
        }
        bufferedSource.skip(8L);
        long l4 = bufferedSource.readLongLe();
        return new EocdRecord(l2, l4, eocdRecord.getCommentByteCount());
    }

    private static final void readExtra(BufferedSource bufferedSource, int n2, Function2<? super Integer, ? super Long, Unit> function2) {
        long l2;
        for (long i2 = (long)n2; i2 != 0L; i2 -= l2) {
            if (i2 < 4L) {
                throw new IOException("bad zip: truncated header in extra field");
            }
            n2 = bufferedSource.readShortLe() & 0xFFFF;
            l2 = (long)bufferedSource.readShortLe() & 0xFFFFL;
            if ((i2 -= 4L) < l2) {
                throw new IOException("bad zip: truncated value in extra field");
            }
            bufferedSource.require(l2);
            long l3 = bufferedSource.getBuffer().size();
            function2.invoke((Integer)n2, (Long)l2);
            long l4 = l2 + bufferedSource.getBuffer().size() - l3;
            if (l4 < 0L) {
                throw new IOException("unsupported zip: too many bytes processed for " + n2);
            }
            if (l4 <= 0L) continue;
            bufferedSource.getBuffer().skip(l4);
        }
    }

    public static final void skipLocalHeader(@NotNull BufferedSource bufferedSource) {
        Intrinsics.checkNotNullParameter(bufferedSource, "");
        ZipFilesKt.readOrSkipLocalHeader(bufferedSource, null);
    }

    @NotNull
    public static final FileMetadata readLocalHeader(@NotNull BufferedSource bufferedSource, @NotNull FileMetadata fileMetadata) {
        Intrinsics.checkNotNullParameter(bufferedSource, "");
        Intrinsics.checkNotNullParameter(fileMetadata, "");
        FileMetadata fileMetadata2 = ZipFilesKt.readOrSkipLocalHeader(bufferedSource, fileMetadata);
        Intrinsics.checkNotNull(fileMetadata2);
        return fileMetadata2;
    }

    private static final FileMetadata readOrSkipLocalHeader(BufferedSource bufferedSource, FileMetadata fileMetadata) {
        Ref.ObjectRef<Long> objectRef = new Ref.ObjectRef<Long>();
        FileMetadata fileMetadata2 = fileMetadata;
        new Ref.ObjectRef<Long>().element = fileMetadata2 != null ? fileMetadata2.getLastModifiedAtMillis() : null;
        Ref.ObjectRef<Long> objectRef2 = new Ref.ObjectRef<Long>();
        Ref.ObjectRef<Long> objectRef3 = new Ref.ObjectRef<Long>();
        int n2 = bufferedSource.readIntLe();
        if (n2 != 67324752) {
            throw new IOException("bad zip: expected " + ZipFilesKt.getHex(67324752) + " but was " + ZipFilesKt.getHex(n2));
        }
        bufferedSource.skip(2L);
        n2 = bufferedSource.readShortLe() & 0xFFFF;
        if ((n2 & 1) != 0) {
            throw new IOException("unsupported zip: general purpose bit flag=" + ZipFilesKt.getHex(n2));
        }
        bufferedSource.skip(18L);
        long l2 = (long)bufferedSource.readShortLe() & 0xFFFFL;
        n2 = bufferedSource.readShortLe() & 0xFFFF;
        bufferedSource.skip(l2);
        if (fileMetadata == null) {
            bufferedSource.skip(n2);
            return null;
        }
        ZipFilesKt.readExtra(bufferedSource, n2, (Function2<? super Integer, ? super Long, Unit>)new Function2<Integer, Long, Unit>(bufferedSource, objectRef, objectRef2, objectRef3){
            final /* synthetic */ BufferedSource $this_readOrSkipLocalHeader;
            final /* synthetic */ Ref.ObjectRef<Long> $lastModifiedAtMillis;
            final /* synthetic */ Ref.ObjectRef<Long> $lastAccessedAtMillis;
            final /* synthetic */ Ref.ObjectRef<Long> $createdAtMillis;
            {
                this.$this_readOrSkipLocalHeader = bufferedSource;
                this.$lastModifiedAtMillis = objectRef;
                this.$lastAccessedAtMillis = objectRef2;
                this.$createdAtMillis = objectRef3;
                super(2);
            }

            public final void invoke(int n2, long l2) {
                if (n2 == 21589) {
                    long l3;
                    if (l2 < 1L) {
                        throw new IOException("bad zip: extended timestamp extra too short");
                    }
                    n2 = this.$this_readOrSkipLocalHeader.readByte() & 0xFF;
                    boolean bl = (n2 & 1) == 1;
                    boolean bl2 = (n2 & 2) == 2;
                    n2 = (n2 & 4) == 4 ? 1 : 0;
                    long l4 = 1L;
                    if (bl) {
                        l4 = 5L;
                    }
                    if (bl2) {
                        l4 += 4L;
                    }
                    if (n2 != 0) {
                        l4 += 4L;
                    }
                    if (l2 < (l3 = l4)) {
                        throw new IOException("bad zip: extended timestamp extra too short");
                    }
                    if (bl) {
                        this.$lastModifiedAtMillis.element = (long)this.$this_readOrSkipLocalHeader.readIntLe() * 1000L;
                    }
                    if (bl2) {
                        this.$lastAccessedAtMillis.element = (long)this.$this_readOrSkipLocalHeader.readIntLe() * 1000L;
                    }
                    if (n2 != 0) {
                        this.$createdAtMillis.element = (long)this.$this_readOrSkipLocalHeader.readIntLe() * 1000L;
                    }
                }
            }

            public final /* synthetic */ Object invoke(Object object, Object object2) {
                this.invoke(((Number)object).intValue(), ((Number)object2).longValue());
                return Unit.INSTANCE;
            }
        });
        return new FileMetadata(fileMetadata.isRegularFile(), fileMetadata.isDirectory(), null, fileMetadata.getSize(), (Long)objectRef3.element, (Long)objectRef.element, (Long)objectRef2.element, null, 128, null);
    }

    private static final Long dosDateTimeToEpochMillis(int n2, int n3) {
        if (n3 == -1) {
            return null;
        }
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(14, 0);
        int n4 = 1980 + (n2 >> 9 & 0x7F);
        int n5 = n2 >> 5 & 0xF;
        int n6 = n3 >> 11 & 0x1F;
        int n7 = n3 >> 5 & 0x3F;
        n3 = (n3 & 0x1F) << 1;
        gregorianCalendar.set(n4, n5 - 1, n2 &= 0x1F, n6, n7, n3);
        return gregorianCalendar.getTime().getTime();
    }

    private static final String getHex(int n2) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        String string = Integer.toString(n2, CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "");
        return stringBuilder.append(string).toString();
    }
}

