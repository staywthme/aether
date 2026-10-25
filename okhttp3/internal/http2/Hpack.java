/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.Util;
import okhttp3.internal.http2.Header;
import okhttp3.internal.http2.Huffman;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import okio.Source;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\t\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0002\u0018\u0019B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u0005J\u0014\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004H\u0002R\u001d\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010\u00a2\u0006\n\n\u0002\u0010\u0014\u001a\u0004\b\u0012\u0010\u0013\u00a8\u0006\u001a"}, d2={"Lokhttp3/internal/http2/Hpack;", "", "()V", "NAME_TO_FIRST_INDEX", "", "Lokio/ByteString;", "", "getNAME_TO_FIRST_INDEX", "()Ljava/util/Map;", "PREFIX_4_BITS", "PREFIX_5_BITS", "PREFIX_6_BITS", "PREFIX_7_BITS", "SETTINGS_HEADER_TABLE_SIZE", "SETTINGS_HEADER_TABLE_SIZE_LIMIT", "STATIC_HEADER_TABLE", "", "Lokhttp3/internal/http2/Header;", "getSTATIC_HEADER_TABLE", "()[Lokhttp3/internal/http2/Header;", "[Lokhttp3/internal/http2/Header;", "checkLowercase", "name", "nameToFirstIndex", "Reader", "Writer", "okhttp"})
public final class Hpack {
    @NotNull
    public static final Hpack INSTANCE = new Hpack();
    private static final int PREFIX_4_BITS = 15;
    private static final int PREFIX_5_BITS = 31;
    private static final int PREFIX_6_BITS = 63;
    private static final int PREFIX_7_BITS = 127;
    private static final int SETTINGS_HEADER_TABLE_SIZE = 4096;
    private static final int SETTINGS_HEADER_TABLE_SIZE_LIMIT = 16384;
    @NotNull
    private static final Header[] STATIC_HEADER_TABLE;
    @NotNull
    private static final Map<ByteString, Integer> NAME_TO_FIRST_INDEX;

    private Hpack() {
    }

    @NotNull
    public final Header[] getSTATIC_HEADER_TABLE() {
        return STATIC_HEADER_TABLE;
    }

    @NotNull
    public final Map<ByteString, Integer> getNAME_TO_FIRST_INDEX() {
        return NAME_TO_FIRST_INDEX;
    }

    private final Map<ByteString, Integer> nameToFirstIndex() {
        LinkedHashMap linkedHashMap = new LinkedHashMap(STATIC_HEADER_TABLE.length);
        int n2 = STATIC_HEADER_TABLE.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (linkedHashMap.containsKey(Hpack.STATIC_HEADER_TABLE[i2].name)) continue;
            Integer n3 = i2;
            ((Map)linkedHashMap).put(Hpack.STATIC_HEADER_TABLE[i2].name, n3);
        }
        Map<ByteString, Integer> map = Collections.unmodifiableMap(linkedHashMap);
        Intrinsics.checkNotNullExpressionValue(map, "");
        return map;
    }

    @NotNull
    public final ByteString checkLowercase(@NotNull ByteString byteString) throws IOException {
        Intrinsics.checkNotNullParameter(byteString, "");
        int n2 = byteString.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            byte by = byteString.getByte(i2);
            boolean bl = 65 <= by ? by < 91 : false;
            if (!bl) continue;
            throw new IOException("PROTOCOL_ERROR response malformed: mixed case name: " + byteString.utf8());
        }
        return byteString;
    }

    static {
        Header[] headerArray = new Header[61];
        Header[] headerArray2 = headerArray;
        headerArray[0] = new Header(Header.TARGET_AUTHORITY, "");
        headerArray2[1] = new Header(Header.TARGET_METHOD, "GET");
        headerArray2[2] = new Header(Header.TARGET_METHOD, "POST");
        headerArray2[3] = new Header(Header.TARGET_PATH, "/");
        headerArray2[4] = new Header(Header.TARGET_PATH, "/index.html");
        headerArray2[5] = new Header(Header.TARGET_SCHEME, "http");
        headerArray2[6] = new Header(Header.TARGET_SCHEME, "https");
        headerArray2[7] = new Header(Header.RESPONSE_STATUS, "200");
        headerArray2[8] = new Header(Header.RESPONSE_STATUS, "204");
        headerArray2[9] = new Header(Header.RESPONSE_STATUS, "206");
        headerArray2[10] = new Header(Header.RESPONSE_STATUS, "304");
        headerArray2[11] = new Header(Header.RESPONSE_STATUS, "400");
        headerArray2[12] = new Header(Header.RESPONSE_STATUS, "404");
        headerArray2[13] = new Header(Header.RESPONSE_STATUS, "500");
        headerArray2[14] = new Header("accept-charset", "");
        headerArray2[15] = new Header("accept-encoding", "gzip, deflate");
        headerArray2[16] = new Header("accept-language", "");
        headerArray2[17] = new Header("accept-ranges", "");
        headerArray2[18] = new Header("accept", "");
        headerArray2[19] = new Header("access-control-allow-origin", "");
        headerArray2[20] = new Header("age", "");
        headerArray2[21] = new Header("allow", "");
        headerArray2[22] = new Header("authorization", "");
        headerArray2[23] = new Header("cache-control", "");
        headerArray2[24] = new Header("content-disposition", "");
        headerArray2[25] = new Header("content-encoding", "");
        headerArray2[26] = new Header("content-language", "");
        headerArray2[27] = new Header("content-length", "");
        headerArray2[28] = new Header("content-location", "");
        headerArray2[29] = new Header("content-range", "");
        headerArray2[30] = new Header("content-type", "");
        headerArray2[31] = new Header("cookie", "");
        headerArray2[32] = new Header("date", "");
        headerArray2[33] = new Header("etag", "");
        headerArray2[34] = new Header("expect", "");
        headerArray2[35] = new Header("expires", "");
        headerArray2[36] = new Header("from", "");
        headerArray2[37] = new Header("host", "");
        headerArray2[38] = new Header("if-match", "");
        headerArray2[39] = new Header("if-modified-since", "");
        headerArray2[40] = new Header("if-none-match", "");
        headerArray2[41] = new Header("if-range", "");
        headerArray2[42] = new Header("if-unmodified-since", "");
        headerArray2[43] = new Header("last-modified", "");
        headerArray2[44] = new Header("link", "");
        headerArray2[45] = new Header("location", "");
        headerArray2[46] = new Header("max-forwards", "");
        headerArray2[47] = new Header("proxy-authenticate", "");
        headerArray2[48] = new Header("proxy-authorization", "");
        headerArray2[49] = new Header("range", "");
        headerArray2[50] = new Header("referer", "");
        headerArray2[51] = new Header("refresh", "");
        headerArray2[52] = new Header("retry-after", "");
        headerArray2[53] = new Header("server", "");
        headerArray2[54] = new Header("set-cookie", "");
        headerArray2[55] = new Header("strict-transport-security", "");
        headerArray2[56] = new Header("transfer-encoding", "");
        headerArray2[57] = new Header("user-agent", "");
        headerArray2[58] = new Header("vary", "");
        headerArray2[59] = new Header("via", "");
        headerArray2[60] = new Header("www-authenticate", "");
        STATIC_HEADER_TABLE = headerArray2;
        NAME_TO_FIRST_INDEX = INSTANCE.nameToFirstIndex();
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\r\u0018\u00002\u00020\u0001B!\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0007J\b\u0010\u0012\u001a\u00020\u0013H\u0002J\b\u0010\u0014\u001a\u00020\u0013H\u0002J\u0010\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u0005H\u0002J\u0010\u0010\u0017\u001a\u00020\u00052\u0006\u0010\u0018\u001a\u00020\u0005H\u0002J\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\n0\u001aJ\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0016\u001a\u00020\u0005H\u0002J\u0018\u0010\u001d\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\nH\u0002J\u0010\u0010\u001f\u001a\u00020 2\u0006\u0010\u0016\u001a\u00020\u0005H\u0002J\u0006\u0010\u0006\u001a\u00020\u0005J\b\u0010!\u001a\u00020\u0005H\u0002J\u0006\u0010\"\u001a\u00020\u001cJ\u0006\u0010#\u001a\u00020\u0013J\u0010\u0010$\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u0005H\u0002J\u0016\u0010%\u001a\u00020\u00052\u0006\u0010&\u001a\u00020\u00052\u0006\u0010'\u001a\u00020\u0005J\u0010\u0010(\u001a\u00020\u00132\u0006\u0010)\u001a\u00020\u0005H\u0002J\b\u0010*\u001a\u00020\u0013H\u0002J\u0010\u0010+\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u0005H\u0002J\b\u0010,\u001a\u00020\u0013H\u0002R\u001c\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\t8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0004\n\u0002\u0010\u000bR\u0012\u0010\f\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\r\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\n0\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006-"}, d2={"Lokhttp3/internal/http2/Hpack$Reader;", "", "source", "Lokio/Source;", "headerTableSizeSetting", "", "maxDynamicTableByteCount", "(Lokio/Source;II)V", "dynamicTable", "", "Lokhttp3/internal/http2/Header;", "[Lokhttp3/internal/http2/Header;", "dynamicTableByteCount", "headerCount", "headerList", "", "nextHeaderIndex", "Lokio/BufferedSource;", "adjustDynamicTableByteCount", "", "clearDynamicTable", "dynamicTableIndex", "index", "evictToRecoverBytes", "bytesToRecover", "getAndResetHeaderList", "", "getName", "Lokio/ByteString;", "insertIntoDynamicTable", "entry", "isStaticHeader", "", "readByte", "readByteString", "readHeaders", "readIndexedHeader", "readInt", "firstByte", "prefixMask", "readLiteralHeaderWithIncrementalIndexingIndexedName", "nameIndex", "readLiteralHeaderWithIncrementalIndexingNewName", "readLiteralHeaderWithoutIndexingIndexedName", "readLiteralHeaderWithoutIndexingNewName", "okhttp"})
    public static final class Reader {
        private final int headerTableSizeSetting;
        private int maxDynamicTableByteCount;
        @NotNull
        private final List<Header> headerList;
        @NotNull
        private final BufferedSource source;
        @JvmField
        @NotNull
        public Header[] dynamicTable;
        private int nextHeaderIndex;
        @JvmField
        public int headerCount;
        @JvmField
        public int dynamicTableByteCount;

        @JvmOverloads
        public Reader(@NotNull Source source2, int n2, int n3) {
            Intrinsics.checkNotNullParameter(source2, "");
            this.headerTableSizeSetting = n2;
            this.maxDynamicTableByteCount = n3;
            this.headerList = new ArrayList();
            this.source = Okio.buffer(source2);
            this.dynamicTable = new Header[8];
            this.nextHeaderIndex = this.dynamicTable.length - 1;
        }

        public /* synthetic */ Reader(Source source2, int n2, int n3, int n4, DefaultConstructorMarker defaultConstructorMarker) {
            if ((n4 & 4) != 0) {
                n3 = n2;
            }
            this(source2, n2, n3);
        }

        @NotNull
        public final List<Header> getAndResetHeaderList() {
            List<Header> list2 = CollectionsKt.toList((Iterable)this.headerList);
            this.headerList.clear();
            return list2;
        }

        public final int maxDynamicTableByteCount() {
            return this.maxDynamicTableByteCount;
        }

        private final void adjustDynamicTableByteCount() {
            if (this.maxDynamicTableByteCount < this.dynamicTableByteCount) {
                if (this.maxDynamicTableByteCount == 0) {
                    this.clearDynamicTable();
                    return;
                }
                Reader reader = this;
                reader.evictToRecoverBytes(reader.dynamicTableByteCount - this.maxDynamicTableByteCount);
            }
        }

        private final void clearDynamicTable() {
            ArraysKt.fill$default(this.dynamicTable, null, 0, 0, 6, null);
            this.nextHeaderIndex = this.dynamicTable.length - 1;
            this.headerCount = 0;
            this.dynamicTableByteCount = 0;
        }

        private final int evictToRecoverBytes(int n2) {
            int n3 = n2;
            int n4 = 0;
            if (n2 > 0) {
                for (n2 = this.dynamicTable.length - 1; n2 >= this.nextHeaderIndex && n3 > 0; --n2) {
                    Header header;
                    Intrinsics.checkNotNull(this.dynamicTable[n2]);
                    n3 -= header.hpackSize;
                    this.dynamicTableByteCount -= header.hpackSize;
                    int n5 = this.headerCount;
                    this.headerCount = n5 + -1;
                    ++n4;
                }
                System.arraycopy(this.dynamicTable, this.nextHeaderIndex + 1, this.dynamicTable, this.nextHeaderIndex + 1 + n4, this.headerCount);
                this.nextHeaderIndex += n4;
            }
            return n4;
        }

        public final void readHeaders() throws IOException {
            while (!this.source.exhausted()) {
                int n2 = Util.and(this.source.readByte(), 255);
                if (n2 == 128) {
                    throw new IOException("index == 0");
                }
                if ((n2 & 0x80) == 128) {
                    n2 = this.readInt(n2, 127);
                    this.readIndexedHeader(n2 - 1);
                    continue;
                }
                if (n2 == 64) {
                    this.readLiteralHeaderWithIncrementalIndexingNewName();
                    continue;
                }
                if ((n2 & 0x40) == 64) {
                    n2 = this.readInt(n2, 63);
                    this.readLiteralHeaderWithIncrementalIndexingIndexedName(n2 - 1);
                    continue;
                }
                if ((n2 & 0x20) == 32) {
                    this.maxDynamicTableByteCount = this.readInt(n2, 31);
                    if (this.maxDynamicTableByteCount < 0 || this.maxDynamicTableByteCount > this.headerTableSizeSetting) {
                        throw new IOException("Invalid dynamic table size update " + this.maxDynamicTableByteCount);
                    }
                    this.adjustDynamicTableByteCount();
                    continue;
                }
                if (n2 == 16 || n2 == 0) {
                    this.readLiteralHeaderWithoutIndexingNewName();
                    continue;
                }
                n2 = this.readInt(n2, 15);
                this.readLiteralHeaderWithoutIndexingIndexedName(n2 - 1);
            }
        }

        private final void readIndexedHeader(int n2) throws IOException {
            if (this.isStaticHeader(n2)) {
                Header header = INSTANCE.getSTATIC_HEADER_TABLE()[n2];
                this.headerList.add(header);
                return;
            }
            int n3 = this.dynamicTableIndex(n2 - INSTANCE.getSTATIC_HEADER_TABLE().length);
            if (n3 < 0 || n3 >= this.dynamicTable.length) {
                throw new IOException("Header index too large " + (n2 + 1));
            }
            Collection collection = this.headerList;
            Header header = this.dynamicTable[n3];
            Intrinsics.checkNotNull(header);
            collection.add(header);
        }

        private final int dynamicTableIndex(int n2) {
            return this.nextHeaderIndex + 1 + n2;
        }

        private final void readLiteralHeaderWithoutIndexingIndexedName(int n2) throws IOException {
            ByteString byteString = this.getName(n2);
            ByteString byteString2 = this.readByteString();
            this.headerList.add(new Header(byteString, byteString2));
        }

        private final void readLiteralHeaderWithoutIndexingNewName() throws IOException {
            ByteString byteString = INSTANCE.checkLowercase(this.readByteString());
            ByteString byteString2 = this.readByteString();
            this.headerList.add(new Header(byteString, byteString2));
        }

        private final void readLiteralHeaderWithIncrementalIndexingIndexedName(int n2) throws IOException {
            ByteString byteString = this.getName(n2);
            ByteString byteString2 = this.readByteString();
            this.insertIntoDynamicTable(-1, new Header(byteString, byteString2));
        }

        private final void readLiteralHeaderWithIncrementalIndexingNewName() throws IOException {
            ByteString byteString = INSTANCE.checkLowercase(this.readByteString());
            ByteString byteString2 = this.readByteString();
            this.insertIntoDynamicTable(-1, new Header(byteString, byteString2));
        }

        private final ByteString getName(int n2) throws IOException {
            if (this.isStaticHeader(n2)) {
                return Hpack.INSTANCE.getSTATIC_HEADER_TABLE()[n2].name;
            }
            int n3 = this.dynamicTableIndex(n2 - INSTANCE.getSTATIC_HEADER_TABLE().length);
            if (n3 < 0 || n3 >= this.dynamicTable.length) {
                throw new IOException("Header index too large " + (n2 + 1));
            }
            Header header = this.dynamicTable[n3];
            Intrinsics.checkNotNull(header);
            return header.name;
        }

        private final boolean isStaticHeader(int n2) {
            return n2 >= 0 && n2 <= INSTANCE.getSTATIC_HEADER_TABLE().length - 1;
        }

        private final void insertIntoDynamicTable(int n2, Header header) {
            this.headerList.add(header);
            int n3 = header.hpackSize;
            if (n2 != -1) {
                Header header2 = this.dynamicTable[this.dynamicTableIndex(n2)];
                Intrinsics.checkNotNull(header2);
                n3 -= header2.hpackSize;
            }
            if (n3 > this.maxDynamicTableByteCount) {
                this.clearDynamicTable();
                return;
            }
            int n4 = this.dynamicTableByteCount + n3 - this.maxDynamicTableByteCount;
            n4 = this.evictToRecoverBytes(n4);
            if (n2 == -1) {
                if (this.headerCount + 1 > this.dynamicTable.length) {
                    Header[] headerArray = new Header[this.dynamicTable.length << 1];
                    System.arraycopy(this.dynamicTable, 0, headerArray, this.dynamicTable.length, this.dynamicTable.length);
                    this.nextHeaderIndex = this.dynamicTable.length - 1;
                    this.dynamicTable = headerArray;
                }
                n2 = this.nextHeaderIndex;
                this.nextHeaderIndex = n2 + -1;
                this.dynamicTable[n2] = header;
                n2 = this.headerCount;
                this.headerCount = n2 + 1;
            } else {
                n2 += this.dynamicTableIndex(n2) + n4;
                this.dynamicTable[n2] = header;
            }
            this.dynamicTableByteCount += n3;
        }

        private final int readByte() throws IOException {
            return Util.and(this.source.readByte(), 255);
        }

        public final int readInt(int n2, int n3) throws IOException {
            int n4;
            if ((n2 &= n3) < n3) {
                return n2;
            }
            n2 = n3;
            n3 = 0;
            while (((n4 = this.readByte()) & 0x80) != 0) {
                n2 += (n4 & 0x7F) << n3;
                n3 += 7;
            }
            return n2 += n4 << n3;
        }

        @NotNull
        public final ByteString readByteString() throws IOException {
            int n2 = this.readByte();
            boolean bl = (n2 & 0x80) == 128;
            long l2 = this.readInt(n2, 127);
            if (bl) {
                Buffer buffer = new Buffer();
                Huffman.INSTANCE.decode(this.source, l2, buffer);
                return buffer.readByteString();
            }
            return this.source.readByteString(l2);
        }

        @JvmOverloads
        public Reader(@NotNull Source source2, int n2) {
            Intrinsics.checkNotNullParameter(source2, "");
            this(source2, n2, 0, 4, null);
        }
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0005\u0018\u00002\u00020\u0001B#\b\u0007\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\b\u0010\u0013\u001a\u00020\u0014H\u0002J\b\u0010\u0015\u001a\u00020\u0014H\u0002J\u0010\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u0003H\u0002J\u0010\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u000bH\u0002J\u000e\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u0002\u001a\u00020\u0003J\u000e\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u001dJ\u0014\u0010\u001e\u001a\u00020\u00142\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u000b0 J\u001e\u0010!\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020\u00032\u0006\u0010#\u001a\u00020\u00032\u0006\u0010$\u001a\u00020\u0003R\u001c\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0004\n\u0002\u0010\fR\u0012\u0010\r\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u000f\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0010\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2={"Lokhttp3/internal/http2/Hpack$Writer;", "", "headerTableSizeSetting", "", "useCompression", "", "out", "Lokio/Buffer;", "(IZLokio/Buffer;)V", "dynamicTable", "", "Lokhttp3/internal/http2/Header;", "[Lokhttp3/internal/http2/Header;", "dynamicTableByteCount", "emitDynamicTableSizeUpdate", "headerCount", "maxDynamicTableByteCount", "nextHeaderIndex", "smallestHeaderTableSizeSetting", "adjustDynamicTableByteCount", "", "clearDynamicTable", "evictToRecoverBytes", "bytesToRecover", "insertIntoDynamicTable", "entry", "resizeHeaderTable", "writeByteString", "data", "Lokio/ByteString;", "writeHeaders", "headerBlock", "", "writeInt", "value", "prefixMask", "bits", "okhttp"})
    public static final class Writer {
        @JvmField
        public int headerTableSizeSetting;
        private final boolean useCompression;
        @NotNull
        private final Buffer out;
        private int smallestHeaderTableSizeSetting;
        private boolean emitDynamicTableSizeUpdate;
        @JvmField
        public int maxDynamicTableByteCount;
        @JvmField
        @NotNull
        public Header[] dynamicTable;
        private int nextHeaderIndex;
        @JvmField
        public int headerCount;
        @JvmField
        public int dynamicTableByteCount;

        @JvmOverloads
        public Writer(int n2, boolean bl, @NotNull Buffer buffer) {
            Intrinsics.checkNotNullParameter(buffer, "");
            this.headerTableSizeSetting = n2;
            this.useCompression = bl;
            this.out = buffer;
            this.smallestHeaderTableSizeSetting = Integer.MAX_VALUE;
            this.maxDynamicTableByteCount = this.headerTableSizeSetting;
            this.dynamicTable = new Header[8];
            this.nextHeaderIndex = this.dynamicTable.length - 1;
        }

        public /* synthetic */ Writer(int n2, boolean bl, Buffer buffer, int n3, DefaultConstructorMarker defaultConstructorMarker) {
            if ((n3 & 1) != 0) {
                n2 = 4096;
            }
            if ((n3 & 2) != 0) {
                bl = true;
            }
            this(n2, bl, buffer);
        }

        private final void clearDynamicTable() {
            ArraysKt.fill$default(this.dynamicTable, null, 0, 0, 6, null);
            this.nextHeaderIndex = this.dynamicTable.length - 1;
            this.headerCount = 0;
            this.dynamicTableByteCount = 0;
        }

        private final int evictToRecoverBytes(int n2) {
            int n3 = n2;
            int n4 = 0;
            if (n2 > 0) {
                for (n2 = this.dynamicTable.length - 1; n2 >= this.nextHeaderIndex && n3 > 0; --n2) {
                    Header header = this.dynamicTable[n2];
                    Intrinsics.checkNotNull(header);
                    n3 -= header.hpackSize;
                    Header header2 = this.dynamicTable[n2];
                    Intrinsics.checkNotNull(header2);
                    this.dynamicTableByteCount -= header2.hpackSize;
                    int n5 = this.headerCount;
                    this.headerCount = n5 + -1;
                    ++n4;
                }
                System.arraycopy(this.dynamicTable, this.nextHeaderIndex + 1, this.dynamicTable, this.nextHeaderIndex + 1 + n4, this.headerCount);
                Arrays.fill(this.dynamicTable, this.nextHeaderIndex + 1, this.nextHeaderIndex + 1 + n4, null);
                this.nextHeaderIndex += n4;
            }
            return n4;
        }

        private final void insertIntoDynamicTable(Header header) {
            int n2 = header.hpackSize;
            if (n2 > this.maxDynamicTableByteCount) {
                this.clearDynamicTable();
                return;
            }
            int n3 = this.dynamicTableByteCount + n2 - this.maxDynamicTableByteCount;
            this.evictToRecoverBytes(n3);
            if (this.headerCount + 1 > this.dynamicTable.length) {
                Header[] headerArray = new Header[this.dynamicTable.length << 1];
                System.arraycopy(this.dynamicTable, 0, headerArray, this.dynamicTable.length, this.dynamicTable.length);
                this.nextHeaderIndex = this.dynamicTable.length - 1;
                this.dynamicTable = headerArray;
            }
            n3 = this.nextHeaderIndex;
            this.nextHeaderIndex = n3 + -1;
            this.dynamicTable[n3] = header;
            n3 = this.headerCount;
            this.headerCount = n3 + 1;
            this.dynamicTableByteCount += n2;
        }

        public final void writeHeaders(@NotNull List<Header> list2) throws IOException {
            Intrinsics.checkNotNullParameter(list2, "");
            if (this.emitDynamicTableSizeUpdate) {
                if (this.smallestHeaderTableSizeSetting < this.maxDynamicTableByteCount) {
                    Writer writer = this;
                    writer.writeInt(writer.smallestHeaderTableSizeSetting, 31, 32);
                }
                this.emitDynamicTableSizeUpdate = false;
                this.smallestHeaderTableSizeSetting = Integer.MAX_VALUE;
                Writer writer = this;
                writer.writeInt(writer.maxDynamicTableByteCount, 31, 32);
            }
            int n2 = list2.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Header header = list2.get(i2);
                ByteString byteString = header.name.toAsciiLowercase();
                ByteString byteString2 = header.value;
                int n3 = -1;
                int n4 = -1;
                Integer n5 = INSTANCE.getNAME_TO_FIRST_INDEX().get(byteString);
                if (n5 != null) {
                    n4 = n5 + 1;
                    boolean bl = 2 <= n4 ? n4 < 8 : false;
                    if (bl) {
                        if (Intrinsics.areEqual(Hpack.INSTANCE.getSTATIC_HEADER_TABLE()[n4 - 1].value, byteString2)) {
                            n3 = n4;
                        } else if (Intrinsics.areEqual(Hpack.INSTANCE.getSTATIC_HEADER_TABLE()[n4].value, byteString2)) {
                            n3 = n4 + 1;
                        }
                    }
                }
                if (n3 == -1) {
                    int n6 = this.dynamicTable.length;
                    for (int i3 = this.nextHeaderIndex + 1; i3 < n6; ++i3) {
                        Header header2 = this.dynamicTable[i3];
                        Intrinsics.checkNotNull(header2);
                        if (!Intrinsics.areEqual(header2.name, byteString)) continue;
                        Header header3 = this.dynamicTable[i3];
                        Intrinsics.checkNotNull(header3);
                        if (Intrinsics.areEqual(header3.value, byteString2)) {
                            n3 = i3 - this.nextHeaderIndex + INSTANCE.getSTATIC_HEADER_TABLE().length;
                            break;
                        }
                        if (n4 != -1) continue;
                        n4 = i3 - this.nextHeaderIndex + INSTANCE.getSTATIC_HEADER_TABLE().length;
                    }
                }
                if (n3 != -1) {
                    this.writeInt(n3, 127, 128);
                    continue;
                }
                if (n4 == -1) {
                    this.out.writeByte(64);
                    this.writeByteString(byteString);
                    this.writeByteString(byteString2);
                    this.insertIntoDynamicTable(header);
                    continue;
                }
                if (byteString.startsWith(Header.PSEUDO_PREFIX) && !Intrinsics.areEqual(Header.TARGET_AUTHORITY, byteString)) {
                    this.writeInt(n4, 15, 0);
                    this.writeByteString(byteString2);
                    continue;
                }
                this.writeInt(n4, 63, 64);
                this.writeByteString(byteString2);
                this.insertIntoDynamicTable(header);
            }
        }

        public final void writeInt(int n2, int n3, int n4) {
            if (n2 < n3) {
                this.out.writeByte(n4 | n2);
                return;
            }
            this.out.writeByte(n4 | n3);
            n2 -= n3;
            while (n2 >= 128) {
                n3 = n2 & 0x7F;
                this.out.writeByte(n3 | 0x80);
                n2 >>>= 7;
            }
            this.out.writeByte(n2);
        }

        public final void writeByteString(@NotNull ByteString byteString) throws IOException {
            Intrinsics.checkNotNullParameter(byteString, "");
            if (this.useCompression && Huffman.INSTANCE.encodedLength(byteString) < byteString.size()) {
                Buffer buffer = new Buffer();
                Huffman.INSTANCE.encode(byteString, buffer);
                byteString = buffer.readByteString();
                this.writeInt(byteString.size(), 127, 128);
                this.out.write(byteString);
                return;
            }
            this.writeInt(byteString.size(), 127, 0);
            this.out.write(byteString);
        }

        public final void resizeHeaderTable(int n2) {
            this.headerTableSizeSetting = n2;
            if (this.maxDynamicTableByteCount == (n2 = Math.min(n2, 16384))) {
                return;
            }
            if (n2 < this.maxDynamicTableByteCount) {
                this.smallestHeaderTableSizeSetting = Math.min(this.smallestHeaderTableSizeSetting, n2);
            }
            this.emitDynamicTableSizeUpdate = true;
            this.maxDynamicTableByteCount = n2;
            this.adjustDynamicTableByteCount();
        }

        private final void adjustDynamicTableByteCount() {
            if (this.maxDynamicTableByteCount < this.dynamicTableByteCount) {
                if (this.maxDynamicTableByteCount == 0) {
                    this.clearDynamicTable();
                    return;
                }
                Writer writer = this;
                writer.evictToRecoverBytes(writer.dynamicTableByteCount - this.maxDynamicTableByteCount);
            }
        }

        @JvmOverloads
        public Writer(int n2, @NotNull Buffer buffer) {
            Intrinsics.checkNotNullParameter(buffer, "");
            this(n2, false, buffer, 2, null);
        }

        @JvmOverloads
        public Writer(@NotNull Buffer buffer) {
            Intrinsics.checkNotNullParameter(buffer, "");
            this(0, false, buffer, 3, null);
        }
    }
}

