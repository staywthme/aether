/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmName;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.Charsets;
import okio.-SegmentedByteString;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import okio.Options;
import okio.PeekSource;
import okio.Segment;
import okio.SegmentPool;
import okio.SegmentedByteString;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import okio.internal.-Buffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000\u00aa\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u001a\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0005\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0017\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004:\u0002\u0090\u0001B\u0005\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0000H\u0016J\u0006\u0010\u0011\u001a\u00020\u0012J\b\u0010\u0013\u001a\u00020\u0000H\u0016J\b\u0010\u0014\u001a\u00020\u0012H\u0016J\u0006\u0010\u0015\u001a\u00020\fJ\u0006\u0010\u0016\u001a\u00020\u0000J$\u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u00192\b\b\u0002\u0010\u001a\u001a\u00020\f2\b\b\u0002\u0010\u001b\u001a\u00020\fH\u0007J\u0018\u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u00002\b\b\u0002\u0010\u001a\u001a\u00020\fJ \u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u00002\b\b\u0002\u0010\u001a\u001a\u00020\f2\u0006\u0010\u001b\u001a\u00020\fJ\u0010\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\b\u0010 \u001a\u00020\u0000H\u0016J\b\u0010!\u001a\u00020\u0000H\u0016J\u0013\u0010\"\u001a\u00020#2\b\u0010$\u001a\u0004\u0018\u00010%H\u0096\u0002J\b\u0010&\u001a\u00020#H\u0016J\b\u0010'\u001a\u00020\u0012H\u0016J\u0016\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020\fH\u0087\u0002\u00a2\u0006\u0002\b+J\u0015\u0010+\u001a\u00020)2\u0006\u0010,\u001a\u00020\fH\u0007\u00a2\u0006\u0002\b-J\b\u0010.\u001a\u00020/H\u0016J\u0018\u00100\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u00101\u001a\u00020\u001dH\u0002J\u000e\u00102\u001a\u00020\u001d2\u0006\u00101\u001a\u00020\u001dJ\u000e\u00103\u001a\u00020\u001d2\u0006\u00101\u001a\u00020\u001dJ\u000e\u00104\u001a\u00020\u001d2\u0006\u00101\u001a\u00020\u001dJ\u0010\u00105\u001a\u00020\f2\u0006\u00106\u001a\u00020)H\u0016J\u0018\u00105\u001a\u00020\f2\u0006\u00106\u001a\u00020)2\u0006\u00107\u001a\u00020\fH\u0016J \u00105\u001a\u00020\f2\u0006\u00106\u001a\u00020)2\u0006\u00107\u001a\u00020\f2\u0006\u00108\u001a\u00020\fH\u0016J\u0010\u00105\u001a\u00020\f2\u0006\u00109\u001a\u00020\u001dH\u0016J\u0018\u00105\u001a\u00020\f2\u0006\u00109\u001a\u00020\u001d2\u0006\u00107\u001a\u00020\fH\u0016J\u0010\u0010:\u001a\u00020\f2\u0006\u0010;\u001a\u00020\u001dH\u0016J\u0018\u0010:\u001a\u00020\f2\u0006\u0010;\u001a\u00020\u001d2\u0006\u00107\u001a\u00020\fH\u0016J\b\u0010<\u001a\u00020=H\u0016J\b\u0010>\u001a\u00020#H\u0016J\u0006\u0010?\u001a\u00020\u001dJ\b\u0010@\u001a\u00020\u0019H\u0016J\b\u0010A\u001a\u00020\u0001H\u0016J\u0018\u0010B\u001a\u00020#2\u0006\u0010\u001a\u001a\u00020\f2\u0006\u00109\u001a\u00020\u001dH\u0016J(\u0010B\u001a\u00020#2\u0006\u0010\u001a\u001a\u00020\f2\u0006\u00109\u001a\u00020\u001d2\u0006\u0010C\u001a\u00020/2\u0006\u0010\u001b\u001a\u00020/H\u0016J\u0010\u0010D\u001a\u00020/2\u0006\u0010E\u001a\u00020FH\u0016J\u0010\u0010D\u001a\u00020/2\u0006\u0010E\u001a\u00020GH\u0016J \u0010D\u001a\u00020/2\u0006\u0010E\u001a\u00020G2\u0006\u0010\u001a\u001a\u00020/2\u0006\u0010\u001b\u001a\u00020/H\u0016J\u0018\u0010D\u001a\u00020\f2\u0006\u0010E\u001a\u00020\u00002\u0006\u0010\u001b\u001a\u00020\fH\u0016J\u0010\u0010H\u001a\u00020\f2\u0006\u0010E\u001a\u00020IH\u0016J\u0012\u0010J\u001a\u00020K2\b\b\u0002\u0010L\u001a\u00020KH\u0007J\b\u0010M\u001a\u00020)H\u0016J\b\u0010N\u001a\u00020GH\u0016J\u0010\u0010N\u001a\u00020G2\u0006\u0010\u001b\u001a\u00020\fH\u0016J\b\u0010O\u001a\u00020\u001dH\u0016J\u0010\u0010O\u001a\u00020\u001d2\u0006\u0010\u001b\u001a\u00020\fH\u0016J\b\u0010P\u001a\u00020\fH\u0016J\u000e\u0010Q\u001a\u00020\u00002\u0006\u0010R\u001a\u00020=J\u0016\u0010Q\u001a\u00020\u00002\u0006\u0010R\u001a\u00020=2\u0006\u0010\u001b\u001a\u00020\fJ \u0010Q\u001a\u00020\u00122\u0006\u0010R\u001a\u00020=2\u0006\u0010\u001b\u001a\u00020\f2\u0006\u0010S\u001a\u00020#H\u0002J\u0010\u0010T\u001a\u00020\u00122\u0006\u0010E\u001a\u00020GH\u0016J\u0018\u0010T\u001a\u00020\u00122\u0006\u0010E\u001a\u00020\u00002\u0006\u0010\u001b\u001a\u00020\fH\u0016J\b\u0010U\u001a\u00020\fH\u0016J\b\u0010V\u001a\u00020/H\u0016J\b\u0010W\u001a\u00020/H\u0016J\b\u0010X\u001a\u00020\fH\u0016J\b\u0010Y\u001a\u00020\fH\u0016J\b\u0010Z\u001a\u00020[H\u0016J\b\u0010\\\u001a\u00020[H\u0016J\u0010\u0010]\u001a\u00020\u001f2\u0006\u0010^\u001a\u00020_H\u0016J\u0018\u0010]\u001a\u00020\u001f2\u0006\u0010\u001b\u001a\u00020\f2\u0006\u0010^\u001a\u00020_H\u0016J\u0012\u0010`\u001a\u00020K2\b\b\u0002\u0010L\u001a\u00020KH\u0007J\b\u0010a\u001a\u00020\u001fH\u0016J\u0010\u0010a\u001a\u00020\u001f2\u0006\u0010\u001b\u001a\u00020\fH\u0016J\b\u0010b\u001a\u00020/H\u0016J\n\u0010c\u001a\u0004\u0018\u00010\u001fH\u0016J\b\u0010d\u001a\u00020\u001fH\u0016J\u0010\u0010d\u001a\u00020\u001f2\u0006\u0010e\u001a\u00020\fH\u0016J\u0010\u0010f\u001a\u00020#2\u0006\u0010\u001b\u001a\u00020\fH\u0016J\u0010\u0010g\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\fH\u0016J\u0010\u0010h\u001a\u00020/2\u0006\u0010i\u001a\u00020jH\u0016J\u0006\u0010k\u001a\u00020\u001dJ\u0006\u0010l\u001a\u00020\u001dJ\u0006\u0010m\u001a\u00020\u001dJ\r\u0010\r\u001a\u00020\fH\u0007\u00a2\u0006\u0002\bnJ\u0010\u0010o\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\fH\u0016J\u0006\u0010p\u001a\u00020\u001dJ\u000e\u0010p\u001a\u00020\u001d2\u0006\u0010\u001b\u001a\u00020/J\b\u0010q\u001a\u00020rH\u0016J\b\u0010s\u001a\u00020\u001fH\u0016J\u0015\u0010t\u001a\u00020\n2\u0006\u0010u\u001a\u00020/H\u0000\u00a2\u0006\u0002\bvJ\u0010\u0010w\u001a\u00020/2\u0006\u0010x\u001a\u00020FH\u0016J\u0010\u0010w\u001a\u00020\u00002\u0006\u0010x\u001a\u00020GH\u0016J \u0010w\u001a\u00020\u00002\u0006\u0010x\u001a\u00020G2\u0006\u0010\u001a\u001a\u00020/2\u0006\u0010\u001b\u001a\u00020/H\u0016J\u0018\u0010w\u001a\u00020\u00122\u0006\u0010x\u001a\u00020\u00002\u0006\u0010\u001b\u001a\u00020\fH\u0016J\u0010\u0010w\u001a\u00020\u00002\u0006\u0010y\u001a\u00020\u001dH\u0016J \u0010w\u001a\u00020\u00002\u0006\u0010y\u001a\u00020\u001d2\u0006\u0010\u001a\u001a\u00020/2\u0006\u0010\u001b\u001a\u00020/H\u0016J\u0018\u0010w\u001a\u00020\u00002\u0006\u0010x\u001a\u00020z2\u0006\u0010\u001b\u001a\u00020\fH\u0016J\u0010\u0010{\u001a\u00020\f2\u0006\u0010x\u001a\u00020zH\u0016J\u0010\u0010|\u001a\u00020\u00002\u0006\u00106\u001a\u00020/H\u0016J\u0010\u0010}\u001a\u00020\u00002\u0006\u0010~\u001a\u00020\fH\u0016J\u0010\u0010\u007f\u001a\u00020\u00002\u0006\u0010~\u001a\u00020\fH\u0016J\u0012\u0010\u0080\u0001\u001a\u00020\u00002\u0007\u0010\u0081\u0001\u001a\u00020/H\u0016J\u0012\u0010\u0082\u0001\u001a\u00020\u00002\u0007\u0010\u0081\u0001\u001a\u00020/H\u0016J\u0011\u0010\u0083\u0001\u001a\u00020\u00002\u0006\u0010~\u001a\u00020\fH\u0016J\u0011\u0010\u0084\u0001\u001a\u00020\u00002\u0006\u0010~\u001a\u00020\fH\u0016J\u0012\u0010\u0085\u0001\u001a\u00020\u00002\u0007\u0010\u0086\u0001\u001a\u00020/H\u0016J\u0012\u0010\u0087\u0001\u001a\u00020\u00002\u0007\u0010\u0086\u0001\u001a\u00020/H\u0016J\u001a\u0010\u0088\u0001\u001a\u00020\u00002\u0007\u0010\u0089\u0001\u001a\u00020\u001f2\u0006\u0010^\u001a\u00020_H\u0016J,\u0010\u0088\u0001\u001a\u00020\u00002\u0007\u0010\u0089\u0001\u001a\u00020\u001f2\u0007\u0010\u008a\u0001\u001a\u00020/2\u0007\u0010\u008b\u0001\u001a\u00020/2\u0006\u0010^\u001a\u00020_H\u0016J\u001b\u0010\u008c\u0001\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u00192\b\b\u0002\u0010\u001b\u001a\u00020\fH\u0007J\u0012\u0010\u008d\u0001\u001a\u00020\u00002\u0007\u0010\u0089\u0001\u001a\u00020\u001fH\u0016J$\u0010\u008d\u0001\u001a\u00020\u00002\u0007\u0010\u0089\u0001\u001a\u00020\u001f2\u0007\u0010\u008a\u0001\u001a\u00020/2\u0007\u0010\u008b\u0001\u001a\u00020/H\u0016J\u0012\u0010\u008e\u0001\u001a\u00020\u00002\u0007\u0010\u008f\u0001\u001a\u00020/H\u0016R\u0014\u0010\u0006\u001a\u00020\u00008VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u0004\u0018\u00010\n8\u0000@\u0000X\u0081\u000e\u00a2\u0006\u0002\n\u0000R&\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f8G@@X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010\u00a8\u0006\u0091\u0001"}, d2={"Lokio/Buffer;", "Lokio/BufferedSource;", "Lokio/BufferedSink;", "", "Ljava/nio/channels/ByteChannel;", "()V", "buffer", "getBuffer", "()Lokio/Buffer;", "head", "Lokio/Segment;", "<set-?>", "", "size", "()J", "setSize$okio", "(J)V", "clear", "", "clone", "close", "completeSegmentByteCount", "copy", "copyTo", "out", "Ljava/io/OutputStream;", "offset", "byteCount", "digest", "Lokio/ByteString;", "algorithm", "", "emit", "emitCompleteSegments", "equals", "", "other", "", "exhausted", "flush", "get", "", "pos", "getByte", "index", "-deprecated_getByte", "hashCode", "", "hmac", "key", "hmacSha1", "hmacSha256", "hmacSha512", "indexOf", "b", "fromIndex", "toIndex", "bytes", "indexOfElement", "targetBytes", "inputStream", "Ljava/io/InputStream;", "isOpen", "md5", "outputStream", "peek", "rangeEquals", "bytesOffset", "read", "sink", "Ljava/nio/ByteBuffer;", "", "readAll", "Lokio/Sink;", "readAndWriteUnsafe", "Lokio/Buffer$UnsafeCursor;", "unsafeCursor", "readByte", "readByteArray", "readByteString", "readDecimalLong", "readFrom", "input", "forever", "readFully", "readHexadecimalUnsignedLong", "readInt", "readIntLe", "readLong", "readLongLe", "readShort", "", "readShortLe", "readString", "charset", "Ljava/nio/charset/Charset;", "readUnsafe", "readUtf8", "readUtf8CodePoint", "readUtf8Line", "readUtf8LineStrict", "limit", "request", "require", "select", "options", "Lokio/Options;", "sha1", "sha256", "sha512", "-deprecated_size", "skip", "snapshot", "timeout", "Lokio/Timeout;", "toString", "writableSegment", "minimumCapacity", "writableSegment$okio", "write", "source", "byteString", "Lokio/Source;", "writeAll", "writeByte", "writeDecimalLong", "v", "writeHexadecimalUnsignedLong", "writeInt", "i", "writeIntLe", "writeLong", "writeLongLe", "writeShort", "s", "writeShortLe", "writeString", "string", "beginIndex", "endIndex", "writeTo", "writeUtf8", "writeUtf8CodePoint", "codePoint", "UnsafeCursor", "okio"})
@SourceDebugExtension(value={"SMAP\nBuffer.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Buffer.kt\nokio/Buffer\n+ 2 Util.kt\nokio/-SegmentedByteString\n+ 3 Buffer.kt\nokio/internal/-Buffer\n+ 4 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,641:1\n89#2:642\n86#2:675\n86#2:677\n74#2:737\n74#2:763\n83#2:802\n77#2:813\n89#2:1003\n74#2:1018\n86#2:1122\n89#2:1615\n244#3,32:643\n279#3,10:678\n292#3,18:688\n414#3,2:706\n112#3:708\n416#3:709\n114#3,18:710\n313#3,9:728\n322#3,15:738\n340#3,10:753\n350#3,3:764\n348#3,25:767\n376#3,10:792\n386#3:803\n384#3,9:804\n393#3,7:814\n391#3,20:821\n682#3,60:841\n745#3,56:901\n803#3:957\n806#3:958\n807#3,6:960\n817#3,7:966\n827#3,6:973\n835#3,5:979\n867#3,6:984\n877#3:990\n878#3,11:992\n889#3,5:1004\n898#3,9:1009\n908#3,61:1019\n633#3:1080\n636#3:1081\n637#3,5:1083\n644#3:1088\n647#3,7:1089\n656#3,20:1096\n420#3:1116\n423#3,5:1117\n428#3,10:1123\n439#3,7:1133\n444#3,2:1140\n973#3:1142\n974#3,87:1144\n1064#3,48:1231\n603#3:1279\n610#3,21:1280\n1115#3,7:1301\n1125#3,7:1308\n1135#3,4:1315\n1142#3,8:1319\n1153#3,10:1327\n1166#3,14:1337\n449#3,91:1351\n543#3,40:1442\n586#3:1482\n588#3,13:1484\n1183#3:1497\n1234#3:1498\n1235#3,39:1500\n1276#3,2:1539\n1278#3,4:1542\n1285#3,3:1546\n1289#3,4:1550\n112#3:1554\n1293#3,22:1555\n114#3,18:1577\n1319#3,2:1595\n1321#3,3:1598\n112#3:1601\n1324#3,13:1602\n1337#3,13:1616\n114#3,18:1629\n1354#3,2:1647\n1357#3:1650\n112#3:1651\n1358#3,50:1652\n114#3,18:1702\n1417#3,14:1720\n1434#3,32:1734\n1469#3,12:1766\n1484#3,18:1778\n1506#3:1796\n1507#3:1798\n1512#3,34:1799\n1#4:676\n1#4:959\n1#4:991\n1#4:1082\n1#4:1143\n1#4:1483\n1#4:1499\n1#4:1541\n1#4:1549\n1#4:1597\n1#4:1649\n1#4:1797\n*S KotlinDebug\n*F\n+ 1 Buffer.kt\nokio/Buffer\n*L\n167#1:642\n197#1:675\n235#1:677\n261#1:737\n264#1:763\n267#1:802\n267#1:813\n335#1:1003\n338#1:1018\n374#1:1122\n483#1:1615\n181#1:643,32\n252#1:678,10\n255#1:688,18\n258#1:706,2\n258#1:708\n258#1:709\n258#1:710,18\n261#1:728,9\n261#1:738,15\n264#1:753,10\n264#1:764,3\n264#1:767,25\n267#1:792,10\n267#1:803\n267#1:804,9\n267#1:814,7\n267#1:821,20\n279#1:841,60\n282#1:901,56\n284#1:957\n287#1:958\n287#1:960,6\n289#1:966,7\n292#1:973,6\n295#1:979,5\n329#1:984,6\n335#1:990\n335#1:992,11\n335#1:1004,5\n338#1:1009,9\n338#1:1019,61\n340#1:1080\n343#1:1081\n343#1:1083,5\n345#1:1088\n348#1:1089,7\n351#1:1096,20\n371#1:1116\n374#1:1117,5\n374#1:1123,10\n376#1:1133,7\n379#1:1140,2\n384#1:1142\n384#1:1144,87\n387#1:1231,48\n410#1:1279\n416#1:1280,21\n437#1:1301,7\n441#1:1308,7\n443#1:1315,4\n445#1:1319,8\n449#1:1327,10\n453#1:1337,14\n457#1:1351,91\n460#1:1442,40\n463#1:1482\n463#1:1484,13\n465#1:1497\n465#1:1498\n465#1:1500,39\n467#1:1539,2\n467#1:1542,4\n477#1:1546,3\n477#1:1550,4\n477#1:1554\n477#1:1555,22\n477#1:1577,18\n483#1:1595,2\n483#1:1598,3\n483#1:1601\n483#1:1602,13\n483#1:1616,13\n483#1:1629,18\n488#1:1647,2\n488#1:1650\n488#1:1651\n488#1:1652,50\n488#1:1702,18\n498#1:1720,14\n568#1:1734,32\n570#1:1766,12\n578#1:1778,18\n586#1:1796\n586#1:1798\n588#1:1799,34\n287#1:959\n335#1:991\n343#1:1082\n384#1:1143\n463#1:1483\n465#1:1499\n467#1:1541\n477#1:1549\n483#1:1597\n488#1:1649\n586#1:1797\n*E\n"})
public final class Buffer
implements Cloneable,
ByteChannel,
BufferedSink,
BufferedSource {
    @JvmField
    @Nullable
    public Segment head;
    private long size;

    @JvmName(name="size")
    public final long size() {
        return this.size;
    }

    public final void setSize$okio(long l2) {
        this.size = l2;
    }

    @Override
    @NotNull
    public final Buffer buffer() {
        return this;
    }

    @Override
    @NotNull
    public final Buffer getBuffer() {
        return this;
    }

    @Override
    @NotNull
    public final OutputStream outputStream() {
        return new OutputStream(this){
            final /* synthetic */ Buffer this$0;
            {
                this.this$0 = buffer;
            }

            public final void write(int n2) {
                this.this$0.writeByte(n2);
            }

            public final void write(@NotNull byte[] byArray, int n2, int n3) {
                Intrinsics.checkNotNullParameter(byArray, "");
                this.this$0.write(byArray, n2, n3);
            }

            public final void flush() {
            }

            public final void close() {
            }

            @NotNull
            public final String toString() {
                return this.this$0 + ".outputStream()";
            }
        };
    }

    @Override
    @NotNull
    public final Buffer emitCompleteSegments() {
        return this;
    }

    @Override
    @NotNull
    public final Buffer emit() {
        return this;
    }

    @Override
    public final boolean exhausted() {
        return this.size == 0L;
    }

    @Override
    public final void require(long l2) throws EOFException {
        if (this.size < l2) {
            throw new EOFException();
        }
    }

    @Override
    public final boolean request(long l2) {
        return this.size >= l2;
    }

    @Override
    @NotNull
    public final BufferedSource peek() {
        return Okio.buffer(new PeekSource(this));
    }

    @Override
    @NotNull
    public final InputStream inputStream() {
        return new InputStream(this){
            final /* synthetic */ Buffer this$0;
            {
                this.this$0 = buffer;
            }

            public final int read() {
                if (this.this$0.size() > 0L) {
                    byte by = this.this$0.readByte();
                    return by & 0xFF;
                }
                return -1;
            }

            public final int read(@NotNull byte[] byArray, int n2, int n3) {
                Intrinsics.checkNotNullParameter(byArray, "");
                return this.this$0.read(byArray, n2, n3);
            }

            public final int available() {
                long l2 = this.this$0.size();
                return (int)Math.min(l2, Integer.MAX_VALUE);
            }

            public final void close() {
            }

            @NotNull
            public final String toString() {
                return this.this$0 + ".inputStream()";
            }
        };
    }

    @JvmOverloads
    @NotNull
    public final Buffer copyTo(@NotNull OutputStream outputStream2, long l2, long l3) throws IOException {
        Intrinsics.checkNotNullParameter(outputStream2, "");
        long l4 = l2;
        long l5 = l3;
        -SegmentedByteString.checkOffsetAndCount(this.size, l2, l3);
        if (l3 == 0L) {
            return this;
        }
        Segment segment = this.head;
        while (true) {
            Segment segment2 = segment;
            Intrinsics.checkNotNull(segment2);
            if (l4 < (long)(segment2.limit - segment.pos)) break;
            l4 -= (long)(segment.limit - segment.pos);
            segment = segment.next;
        }
        while (l5 > 0L) {
            Segment segment3 = segment;
            Intrinsics.checkNotNull(segment3);
            int n2 = (int)((long)segment3.pos + l4);
            int n3 = segment.limit - n2;
            n3 = (int)Math.min((long)n3, l5);
            outputStream2.write(segment.data, n2, n3);
            l5 -= (long)n3;
            l4 = 0L;
            segment = segment.next;
        }
        return this;
    }

    public static /* synthetic */ Buffer copyTo$default(Buffer buffer, OutputStream outputStream2, long l2, long l3, int n2, Object object) throws IOException {
        if ((n2 & 2) != 0) {
            l2 = 0L;
        }
        if ((n2 & 4) != 0) {
            l3 = buffer.size - l2;
        }
        return buffer.copyTo(outputStream2, l2, l3);
    }

    @NotNull
    public final Buffer copyTo(@NotNull Buffer buffer, long l2, long l3) {
        Intrinsics.checkNotNullParameter(buffer, "");
        Buffer buffer2 = this;
        long l4 = l2;
        long l5 = l3;
        -SegmentedByteString.checkOffsetAndCount(buffer2.size(), l2, l3);
        if (l3 != 0L) {
            Buffer buffer3 = buffer;
            buffer3.setSize$okio(buffer3.size() + l3);
            Segment segment = buffer2.head;
            while (true) {
                Segment segment2 = segment;
                Intrinsics.checkNotNull(segment2);
                if (l4 < (long)(segment2.limit - segment.pos)) break;
                l4 -= (long)(segment.limit - segment.pos);
                segment = segment.next;
            }
            while (l5 > 0L) {
                Segment segment3 = segment;
                Intrinsics.checkNotNull(segment3);
                Segment segment4 = segment3.sharedCopy();
                segment3.sharedCopy().pos = segment4.pos + (int)l4;
                segment4.limit = Math.min(segment4.pos + (int)l5, segment4.limit);
                if (buffer.head == null) {
                    buffer.head = segment4.next = (segment4.prev = segment4);
                } else {
                    Segment segment5 = buffer.head;
                    Intrinsics.checkNotNull(segment5);
                    Segment segment6 = segment5.prev;
                    Intrinsics.checkNotNull(segment6);
                    segment6.push(segment4);
                }
                l5 -= (long)(segment4.limit - segment4.pos);
                l4 = 0L;
                segment = segment.next;
            }
        }
        return buffer2;
    }

    public static /* synthetic */ Buffer copyTo$default(Buffer buffer, Buffer buffer2, long l2, long l3, int n2, Object object) {
        if ((n2 & 2) != 0) {
            l2 = 0L;
        }
        return buffer.copyTo(buffer2, l2, l3);
    }

    @NotNull
    public final Buffer copyTo(@NotNull Buffer buffer, long l2) {
        Intrinsics.checkNotNullParameter(buffer, "");
        return this.copyTo(buffer, l2, this.size - l2);
    }

    public static /* synthetic */ Buffer copyTo$default(Buffer buffer, Buffer buffer2, long l2, int n2, Object object) {
        if ((n2 & 2) != 0) {
            l2 = 0L;
        }
        return buffer.copyTo(buffer2, l2);
    }

    @JvmOverloads
    @NotNull
    public final Buffer writeTo(@NotNull OutputStream outputStream2, long l2) throws IOException {
        int n2;
        Intrinsics.checkNotNullParameter(outputStream2, "");
        -SegmentedByteString.checkOffsetAndCount(this.size, 0L, l2);
        Segment segment = this.head;
        for (long i2 = l2; i2 > 0L; i2 -= (long)n2) {
            Segment segment2 = segment;
            Intrinsics.checkNotNull(segment2);
            int n3 = segment2.limit - segment.pos;
            n2 = (int)Math.min(i2, (long)n3);
            outputStream2.write(segment.data, segment.pos, n2);
            Segment segment3 = segment;
            segment.pos = segment3.pos + n2;
            this.size -= (long)n2;
            if (segment.pos != segment.limit) continue;
            segment3 = segment;
            this.head = segment = segment3.pop();
            SegmentPool.recycle(segment3);
        }
        return this;
    }

    public static /* synthetic */ Buffer writeTo$default(Buffer buffer, OutputStream outputStream2, long l2, int n2, Object object) throws IOException {
        if ((n2 & 2) != 0) {
            l2 = buffer.size;
        }
        return buffer.writeTo(outputStream2, l2);
    }

    @NotNull
    public final Buffer readFrom(@NotNull InputStream inputStream2) throws IOException {
        Intrinsics.checkNotNullParameter(inputStream2, "");
        this.readFrom(inputStream2, Long.MAX_VALUE, true);
        return this;
    }

    @NotNull
    public final Buffer readFrom(@NotNull InputStream object, long l2) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(l2 >= 0L)) {
            object = "byteCount < 0: " + l2;
            throw new IllegalArgumentException(object.toString());
        }
        this.readFrom((InputStream)object, l2, false);
        return this;
    }

    private final void readFrom(InputStream inputStream2, long l2, boolean bl) throws IOException {
        int n2;
        for (long i2 = l2; i2 > 0L || bl; i2 -= (long)n2) {
            Segment segment = this.writableSegment$okio(1);
            n2 = 8192 - segment.limit;
            n2 = (int)Math.min(i2, (long)n2);
            if ((n2 = inputStream2.read(segment.data, segment.limit, n2)) == -1) {
                if (segment.pos == segment.limit) {
                    this.head = segment.pop();
                    SegmentPool.recycle(segment);
                }
                if (bl) {
                    return;
                }
                throw new EOFException();
            }
            segment.limit += n2;
            this.size += (long)n2;
        }
    }

    public final long completeSegmentByteCount() {
        Object object = this;
        long l2 = ((Buffer)object).size();
        if (l2 == 0L) {
            return 0L;
        }
        Segment segment = ((Buffer)object).head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment.prev;
        Intrinsics.checkNotNull(segment2);
        object = segment2;
        if (segment2.limit < 8192 && ((Segment)object).owner) {
            l2 -= (long)(((Segment)object).limit - ((Segment)object).pos);
        }
        return l2;
    }

    @Override
    public final byte readByte() throws EOFException {
        Buffer buffer = this;
        if (buffer.size() == 0L) {
            throw new EOFException();
        }
        Segment segment = buffer.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment;
        int n2 = segment.pos;
        int n3 = segment2.limit;
        byte[] byArray = segment2.data;
        byte by = segment2.data[n2++];
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() - 1L);
        if (n2 == n3) {
            buffer.head = segment2.pop();
            SegmentPool.recycle(segment2);
        } else {
            segment2.pos = n2;
        }
        return by;
    }

    @JvmName(name="getByte")
    public final byte getByte(long l2) {
        long l3;
        Object object = this;
        -SegmentedByteString.checkOffsetAndCount(((Buffer)object).size(), l2, 1L);
        Segment segment = ((Buffer)object).head;
        if (segment == null) {
            Intrinsics.checkNotNull(null);
            return null.data[(int)((long)null.pos + l2 - -1L)];
        }
        Segment segment2 = segment;
        if (((Buffer)object).size() - l2 < l2) {
            long l4;
            for (l4 = ((Buffer)object).size(); l4 > l2; l4 -= (long)(segment2.limit - segment2.pos)) {
                Intrinsics.checkNotNull(segment2.prev);
            }
            long l5 = l4;
            object = segment2;
            Intrinsics.checkNotNull(object);
            return ((Segment)object).data[(int)((long)((Segment)object).pos + l2 - l5)];
        }
        long l6 = 0L;
        while ((l3 = l6 + (long)(segment2.limit - segment2.pos)) <= l2) {
            Intrinsics.checkNotNull(segment2.next);
            l6 = l3;
        }
        long l7 = l6;
        object = segment2;
        Intrinsics.checkNotNull(object);
        return ((Segment)object).data[(int)((long)((Segment)object).pos + l2 - l7)];
    }

    @Override
    public final short readShort() throws EOFException {
        Buffer buffer = this;
        if (buffer.size() < 2L) {
            throw new EOFException();
        }
        Segment segment = buffer.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment;
        int n2 = segment.pos;
        int n3 = segment2.limit;
        if (n3 - n2 < 2) {
            byte by = buffer.readByte();
            byte by2 = by;
            by2 = buffer.readByte();
            int n4 = (by & 0xFF) << 8 | by2 & 0xFF;
            return (short)n4;
        }
        byte[] byArray = segment2.data;
        int n5 = segment2.data[n2++];
        int n6 = n5;
        n6 = byArray[n2++];
        n6 = (n5 & 0xFF) << 8 | n6 & 0xFF;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() - 2L);
        if (n2 == n3) {
            buffer.head = segment2.pop();
            SegmentPool.recycle(segment2);
        } else {
            segment2.pos = n2;
        }
        return (short)n6;
    }

    @Override
    public final int readInt() throws EOFException {
        Buffer buffer = this;
        if (buffer.size() < 4L) {
            throw new EOFException();
        }
        Segment segment = buffer.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment;
        int n2 = segment.pos;
        int n3 = segment2.limit;
        if ((long)(n3 - n2) < 4L) {
            byte by = buffer.readByte();
            byte by2 = by;
            byte by3 = buffer.readByte();
            by2 = by3;
            byte by4 = buffer.readByte();
            by2 = by4;
            by2 = buffer.readByte();
            return (by & 0xFF) << 24 | (by3 & 0xFF) << 16 | (by4 & 0xFF) << 8 | by2 & 0xFF;
        }
        byte[] byArray = segment2.data;
        byte by = segment2.data[n2++];
        byte by5 = by;
        byte by6 = byArray[n2++];
        by5 = by6;
        byte by7 = byArray[n2++];
        by5 = by7;
        by5 = byArray[n2++];
        int n4 = (by & 0xFF) << 24 | (by6 & 0xFF) << 16 | (by7 & 0xFF) << 8 | by5 & 0xFF;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() - 4L);
        if (n2 == n3) {
            buffer.head = segment2.pop();
            SegmentPool.recycle(segment2);
        } else {
            segment2.pos = n2;
        }
        return n4;
    }

    @Override
    public final long readLong() throws EOFException {
        Buffer buffer = this;
        if (buffer.size() < 8L) {
            throw new EOFException();
        }
        Segment segment = buffer.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment;
        int n2 = segment.pos;
        int n3 = segment2.limit;
        if ((long)(n3 - n2) < 8L) {
            int n4 = buffer.readInt();
            int n5 = n4;
            n5 = buffer.readInt();
            return ((long)n4 & 0xFFFFFFFFL) << 32 | (long)n5 & 0xFFFFFFFFL;
        }
        byte[] byArray = segment2.data;
        byte by = segment2.data[n2++];
        byte by2 = by;
        byte by3 = byArray[n2++];
        by2 = by3;
        byte by4 = byArray[n2++];
        by2 = by4;
        byte by5 = byArray[n2++];
        by2 = by5;
        byte by6 = byArray[n2++];
        by2 = by6;
        byte by7 = byArray[n2++];
        by2 = by7;
        byte by8 = byArray[n2++];
        by2 = by8;
        by2 = byArray[n2++];
        long l2 = ((long)by & 0xFFL) << 56 | ((long)by3 & 0xFFL) << 48 | ((long)by4 & 0xFFL) << 40 | ((long)by5 & 0xFFL) << 32 | ((long)by6 & 0xFFL) << 24 | ((long)by7 & 0xFFL) << 16 | ((long)by8 & 0xFFL) << 8 | (long)by2 & 0xFFL;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() - 8L);
        if (n2 == n3) {
            buffer.head = segment2.pop();
            SegmentPool.recycle(segment2);
        } else {
            segment2.pos = n2;
        }
        return l2;
    }

    @Override
    public final short readShortLe() throws EOFException {
        return -SegmentedByteString.reverseBytes(this.readShort());
    }

    @Override
    public final int readIntLe() throws EOFException {
        return -SegmentedByteString.reverseBytes(this.readInt());
    }

    @Override
    public final long readLongLe() throws EOFException {
        return -SegmentedByteString.reverseBytes(this.readLong());
    }

    @Override
    public final long readDecimalLong() throws EOFException {
        int n2;
        Object object;
        Buffer buffer = this;
        if (buffer.size() == 0L) {
            throw new EOFException();
        }
        long l2 = 0L;
        int n3 = 0;
        boolean bl = false;
        boolean bl2 = false;
        long l3 = -7L;
        do {
            Segment segment;
            Intrinsics.checkNotNull(buffer.head);
            object = segment.data;
            int n4 = segment.pos;
            int n5 = segment.limit;
            while (n4 < n5) {
                byte by = object[n4];
                if (by >= 48 && by <= 57) {
                    int n6 = 48 - by;
                    if (l2 < -922337203685477580L || l2 == -922337203685477580L && (long)n6 < l3) {
                        buffer = new Buffer().writeDecimalLong(l2).writeByte(by);
                        if (!bl) {
                            buffer.readByte();
                        }
                        throw new NumberFormatException("Number too large: " + buffer.readUtf8());
                    }
                    l2 *= 10L;
                    l2 += (long)n6;
                } else if (by == 45 && n3 == 0) {
                    bl = true;
                    --l3;
                } else {
                    bl2 = true;
                    break;
                }
                ++n4;
                ++n3;
            }
            if (n4 == n5) {
                buffer.head = segment.pop();
                SegmentPool.recycle(segment);
                continue;
            }
            segment.pos = n4;
        } while (!bl2 && buffer.head != null);
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() - (long)n3);
        int n7 = n2 = bl ? 2 : 1;
        if (n3 < n2) {
            if (buffer.size() == 0L) {
                throw new EOFException();
            }
            object = bl ? "Expected a digit" : "Expected a digit or '-'";
            throw new NumberFormatException((String)object + " but was 0x" + -SegmentedByteString.toHexString(buffer.getByte(0L)));
        }
        if (bl) {
            return l2;
        }
        return -l2;
    }

    @Override
    public final long readHexadecimalUnsignedLong() throws EOFException {
        Buffer buffer = this;
        if (buffer.size() == 0L) {
            throw new EOFException();
        }
        long l2 = 0L;
        int n2 = 0;
        boolean bl = false;
        do {
            Segment segment;
            Intrinsics.checkNotNull(buffer.head);
            byte[] byArray = segment.data;
            int n3 = segment.pos;
            int n4 = segment.limit;
            while (n3 < n4) {
                int n5;
                byte by = byArray[n3];
                if (by >= 48 && by <= 57) {
                    n5 = by - 48;
                } else if (by >= 97 && by <= 102) {
                    n5 = by - 97 + 10;
                } else if (by >= 65 && by <= 70) {
                    n5 = by - 65 + 10;
                } else {
                    if (n2 == 0) {
                        throw new NumberFormatException("Expected leading [0-9a-fA-F] character but was 0x" + -SegmentedByteString.toHexString(by));
                    }
                    bl = true;
                    break;
                }
                if ((l2 & 0xF000000000000000L) != 0L) {
                    buffer = new Buffer().writeHexadecimalUnsignedLong(l2).writeByte(by);
                    throw new NumberFormatException("Number too large: " + buffer.readUtf8());
                }
                l2 <<= 4;
                l2 |= (long)n5;
                ++n3;
                ++n2;
            }
            if (n3 == n4) {
                buffer.head = segment.pop();
                SegmentPool.recycle(segment);
                continue;
            }
            segment.pos = n3;
        } while (!bl && buffer.head != null);
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() - (long)n2);
        return l2;
    }

    @Override
    @NotNull
    public final ByteString readByteString() {
        Buffer buffer = this;
        return buffer.readByteString(buffer.size());
    }

    @Override
    @NotNull
    public final ByteString readByteString(long l2) throws EOFException {
        Buffer buffer = this;
        if (!(l2 >= 0L && l2 <= Integer.MAX_VALUE)) {
            String string = "byteCount: " + l2;
            throw new IllegalArgumentException(string.toString());
        }
        if (buffer.size() < l2) {
            throw new EOFException();
        }
        if (l2 >= 4096L) {
            ByteString byteString = buffer.snapshot((int)l2);
            buffer.skip(l2);
            return byteString;
        }
        return new ByteString(buffer.readByteArray(l2));
    }

    @Override
    public final int select(@NotNull Options options) {
        Intrinsics.checkNotNullParameter(options, "");
        Buffer buffer = this;
        int n2 = -Buffer.selectPrefix$default(buffer, options, false, 2, null);
        if (n2 == -1) {
            return -1;
        }
        int n3 = options.getByteStrings$okio()[n2].size();
        buffer.skip(n3);
        return n2;
    }

    @Override
    public final void readFully(@NotNull Buffer buffer, long l2) throws EOFException {
        Intrinsics.checkNotNullParameter(buffer, "");
        Buffer buffer2 = this;
        if (buffer2.size() < l2) {
            Buffer buffer3 = buffer2;
            buffer.write(buffer3, buffer3.size());
            throw new EOFException();
        }
        buffer.write(buffer2, l2);
    }

    @Override
    public final long readAll(@NotNull Sink sink2) throws IOException {
        Intrinsics.checkNotNullParameter(sink2, "");
        Buffer buffer = this;
        long l2 = buffer.size();
        if (l2 > 0L) {
            sink2.write(buffer, l2);
        }
        return l2;
    }

    @Override
    @NotNull
    public final String readUtf8() {
        Buffer buffer = this;
        return buffer.readString(buffer.size, Charsets.UTF_8);
    }

    @Override
    @NotNull
    public final String readUtf8(long l2) throws EOFException {
        return this.readString(l2, Charsets.UTF_8);
    }

    @Override
    @NotNull
    public final String readString(@NotNull Charset charset) {
        Intrinsics.checkNotNullParameter(charset, "");
        Buffer buffer = this;
        return buffer.readString(buffer.size, charset);
    }

    @Override
    @NotNull
    public final String readString(long l2, @NotNull Charset object) throws EOFException {
        Intrinsics.checkNotNullParameter(object, "");
        if (!(l2 >= 0L && l2 <= Integer.MAX_VALUE)) {
            object = "byteCount: " + l2;
            throw new IllegalArgumentException(object.toString());
        }
        if (this.size < l2) {
            throw new EOFException();
        }
        if (l2 == 0L) {
            return "";
        }
        Segment segment = this.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment;
        if ((long)segment.pos + l2 > (long)segment2.limit) {
            return new String(this.readByteArray(l2), (Charset)object);
        }
        object = new String(segment2.data, segment2.pos, (int)l2, (Charset)object);
        segment2.pos += (int)l2;
        this.size -= l2;
        if (segment2.pos == segment2.limit) {
            this.head = segment2.pop();
            SegmentPool.recycle(segment2);
        }
        return object;
    }

    @Override
    @Nullable
    public final String readUtf8Line() throws EOFException {
        Buffer buffer = this;
        long l2 = buffer.indexOf((byte)10);
        if (l2 != -1L) {
            return -Buffer.readUtf8Line(buffer, l2);
        }
        if (buffer.size() != 0L) {
            Buffer buffer2 = buffer;
            return buffer2.readUtf8(buffer2.size());
        }
        return null;
    }

    @Override
    @NotNull
    public final String readUtf8LineStrict() throws EOFException {
        return this.readUtf8LineStrict(Long.MAX_VALUE);
    }

    @Override
    @NotNull
    public final String readUtf8LineStrict(long l2) throws EOFException {
        Buffer buffer = this;
        if (!(l2 >= 0L)) {
            String string = "limit < 0: " + l2;
            throw new IllegalArgumentException(string.toString());
        }
        long l3 = l2 == Long.MAX_VALUE ? Long.MAX_VALUE : l2 + 1L;
        long l4 = buffer.indexOf((byte)10, 0L, l3);
        if (l4 != -1L) {
            return -Buffer.readUtf8Line(buffer, l4);
        }
        if (l3 < buffer.size() && buffer.getByte(l3 - 1L) == 13 && buffer.getByte(l3) == 10) {
            return -Buffer.readUtf8Line(buffer, l3);
        }
        Buffer buffer2 = new Buffer();
        long l5 = buffer.size();
        buffer.copyTo(buffer2, 0L, Math.min(32L, l5));
        throw new EOFException("\\n not found: limit=" + Math.min(buffer.size(), l2) + " content=" + buffer2.readByteString().hex() + '\u2026');
    }

    @Override
    public final int readUtf8CodePoint() throws EOFException {
        int n2;
        int n3;
        int n4;
        Buffer buffer = this;
        if (buffer.size() == 0L) {
            throw new EOFException();
        }
        int n5 = buffer.getByte(0L);
        if ((n5 & 0x80) == 0) {
            n4 = n5 & 0x7F;
            n3 = 1;
            n2 = 0;
        } else if ((n5 & 0xE0) == 192) {
            n4 = n5 & 0x1F;
            n3 = 2;
            n2 = 128;
        } else if ((n5 & 0xF0) == 224) {
            n4 = n5 & 0xF;
            n3 = 3;
            n2 = 2048;
        } else if ((n5 & 0xF8) == 240) {
            n4 = n5 & 7;
            n3 = 4;
            n2 = 65536;
        } else {
            buffer.skip(1L);
            return 65533;
        }
        if (buffer.size() < (long)n3) {
            throw new EOFException("size < " + n3 + ": " + buffer.size() + " (to read code point prefixed 0x" + -SegmentedByteString.toHexString((byte)n5) + ')');
        }
        for (n5 = 1; n5 < n3; ++n5) {
            byte by;
            byte by2 = by = buffer.getByte(n5);
            if ((by & 0xC0) == 128) {
                n4 <<= 6;
                by2 = by;
                n4 |= by2 & 0x3F;
                continue;
            }
            buffer.skip(n5);
            return 65533;
        }
        buffer.skip(n3);
        if (n4 > 0x10FFFF) {
            return 65533;
        }
        boolean bl = 55296 <= n4 ? n4 < 57344 : false;
        if (bl) {
            return 65533;
        }
        if (n4 < n2) {
            return 65533;
        }
        return n4;
    }

    @Override
    @NotNull
    public final byte[] readByteArray() {
        Buffer buffer = this;
        return buffer.readByteArray(buffer.size());
    }

    @Override
    @NotNull
    public final byte[] readByteArray(long l2) throws EOFException {
        Buffer buffer = this;
        if (!(l2 >= 0L && l2 <= Integer.MAX_VALUE)) {
            String string = "byteCount: " + l2;
            throw new IllegalArgumentException(string.toString());
        }
        if (buffer.size() < l2) {
            throw new EOFException();
        }
        byte[] byArray = new byte[(int)l2];
        buffer.readFully(byArray);
        return byArray;
    }

    @Override
    public final int read(@NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Buffer buffer = this;
        return buffer.read(byArray, 0, byArray.length);
    }

    @Override
    public final void readFully(@NotNull byte[] byArray) throws EOFException {
        int n2;
        Intrinsics.checkNotNullParameter(byArray, "");
        Buffer buffer = this;
        for (int i2 = 0; i2 < byArray.length; i2 += n2) {
            n2 = buffer.read(byArray, i2, byArray.length - i2);
            if (n2 != -1) continue;
            throw new EOFException();
        }
    }

    @Override
    public final int read(@NotNull byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Buffer buffer = this;
        -SegmentedByteString.checkOffsetAndCount(byArray.length, n2, n3);
        Segment segment = buffer.head;
        if (segment == null) {
            return -1;
        }
        Segment segment2 = segment;
        n3 = Math.min(n3, segment2.limit - segment2.pos);
        ArraysKt.copyInto(segment2.data, byArray, n2, segment2.pos, segment2.pos + n3);
        segment2.pos += n3;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() - (long)n3);
        if (segment2.pos == segment2.limit) {
            buffer.head = segment2.pop();
            SegmentPool.recycle(segment2);
        }
        return n3;
    }

    @Override
    public final int read(@NotNull ByteBuffer byteBuffer) throws IOException {
        Intrinsics.checkNotNullParameter(byteBuffer, "");
        Segment segment = this.head;
        if (segment == null) {
            return -1;
        }
        Segment segment2 = segment;
        int n2 = Math.min(byteBuffer.remaining(), segment2.limit - segment2.pos);
        byteBuffer.put(segment2.data, segment2.pos, n2);
        segment2.pos += n2;
        this.size -= (long)n2;
        if (segment2.pos == segment2.limit) {
            this.head = segment2.pop();
            SegmentPool.recycle(segment2);
        }
        return n2;
    }

    public final void clear() {
        Buffer buffer = this;
        buffer.skip(buffer.size());
    }

    @Override
    public final void skip(long l2) throws EOFException {
        Buffer buffer = this;
        long l3 = l2;
        while (l3 > 0L) {
            Segment segment;
            if (buffer.head == null) {
                throw new EOFException();
            }
            int n2 = segment.limit - segment.pos;
            n2 = (int)Math.min(l3, (long)n2);
            Buffer buffer2 = buffer;
            buffer2.setSize$okio(buffer2.size() - (long)n2);
            l3 -= (long)n2;
            segment.pos += n2;
            if (segment.pos != segment.limit) continue;
            buffer.head = segment.pop();
            SegmentPool.recycle(segment);
        }
    }

    @Override
    @NotNull
    public final Buffer write(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        Buffer buffer = this;
        int n2 = byteString.size();
        byteString.write$okio(buffer, 0, n2);
        return buffer;
    }

    @Override
    @NotNull
    public final Buffer write(@NotNull ByteString byteString, int n2, int n3) {
        Intrinsics.checkNotNullParameter(byteString, "");
        Buffer buffer = this;
        byteString.write$okio(buffer, n2, n3);
        return buffer;
    }

    @Override
    @NotNull
    public final Buffer writeUtf8(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        return this.writeUtf8(string, 0, string.length());
    }

    @Override
    @NotNull
    public final Buffer writeUtf8(@NotNull String string, int n2, int n3) {
        Intrinsics.checkNotNullParameter(string, "");
        Buffer buffer = this;
        if (!(n2 >= 0)) {
            String string2 = "beginIndex < 0: " + n2;
            throw new IllegalArgumentException(string2.toString());
        }
        if (!(n3 >= n2)) {
            String string3 = "endIndex < beginIndex: " + n3 + " < " + n2;
            throw new IllegalArgumentException(string3.toString());
        }
        if (!(n3 <= string.length())) {
            String string4 = "endIndex > string.length: " + n3 + " > " + string.length();
            throw new IllegalArgumentException(string4.toString());
        }
        int n4 = n2;
        while (n4 < n3) {
            char c2;
            block12: {
                block11: {
                    char c3 = string.charAt(n4);
                    n2 = c3;
                    if (c3 < '\u0080') {
                        Segment segment = buffer.writableSegment$okio(1);
                        byte[] byArray = segment.data;
                        int n5 = segment.limit - n4;
                        int n6 = Math.min(n3, 8192 - n5);
                        byArray[n5 + n4++] = (byte)n2;
                        while (n4 < n6) {
                            char c4 = string.charAt(n4);
                            n2 = c4;
                            if (c4 >= '\u0080') break;
                            byArray[n5 + n4++] = (byte)n2;
                        }
                        n2 = n4 + n5 - segment.limit;
                        segment.limit += n2;
                        Buffer buffer2 = buffer;
                        buffer2.setSize$okio(buffer2.size() + (long)n2);
                        continue;
                    }
                    if (n2 < 2048) {
                        Segment segment = buffer.writableSegment$okio(2);
                        segment.data[segment.limit] = (byte)(n2 >> 6 | 0xC0);
                        segment.data[segment.limit + 1] = (byte)(n2 & 0x3F | 0x80);
                        segment.limit += 2;
                        Buffer buffer3 = buffer;
                        buffer3.setSize$okio(buffer3.size() + 2L);
                        ++n4;
                        continue;
                    }
                    if (n2 < 55296 || n2 > 57343) {
                        Segment segment = buffer.writableSegment$okio(3);
                        segment.data[segment.limit] = (byte)(n2 >> 12 | 0xE0);
                        segment.data[segment.limit + 1] = (byte)(n2 >> 6 & 0x3F | 0x80);
                        segment.data[segment.limit + 2] = (byte)(n2 & 0x3F | 0x80);
                        segment.limit += 3;
                        Buffer buffer4 = buffer;
                        buffer4.setSize$okio(buffer4.size() + 3L);
                        ++n4;
                        continue;
                    }
                    char c5 = c2 = n4 + 1 < n3 ? string.charAt(n4 + 1) : (char)'\u0000';
                    if (n2 > 56319) break block11;
                    if ('\udc00' <= c2 ? c2 < '\ue000' : false) break block12;
                }
                buffer.writeByte(63);
                ++n4;
                continue;
            }
            int n7 = 65536 + ((n2 & 0x3FF) << 10 | c2 & 0x3FF);
            Segment segment = buffer.writableSegment$okio(4);
            segment.data[segment.limit] = (byte)(n7 >> 18 | 0xF0);
            segment.data[segment.limit + 1] = (byte)(n7 >> 12 & 0x3F | 0x80);
            segment.data[segment.limit + 2] = (byte)(n7 >> 6 & 0x3F | 0x80);
            segment.data[segment.limit + 3] = (byte)(n7 & 0x3F | 0x80);
            segment.limit += 4;
            Buffer buffer5 = buffer;
            buffer5.setSize$okio(buffer5.size() + 4L);
            n4 += 2;
        }
        return buffer;
    }

    @Override
    @NotNull
    public final Buffer writeUtf8CodePoint(int n2) {
        Buffer buffer = this;
        if (n2 < 128) {
            buffer.writeByte(n2);
        } else if (n2 < 2048) {
            Segment segment = buffer.writableSegment$okio(2);
            segment.data[segment.limit] = (byte)(n2 >> 6 | 0xC0);
            segment.data[segment.limit + 1] = (byte)(n2 & 0x3F | 0x80);
            segment.limit += 2;
            Buffer buffer2 = buffer;
            buffer2.setSize$okio(buffer2.size() + 2L);
        } else {
            boolean bl = 55296 <= n2 ? n2 < 57344 : false;
            if (bl) {
                buffer.writeByte(63);
            } else if (n2 < 65536) {
                Segment segment = buffer.writableSegment$okio(3);
                segment.data[segment.limit] = (byte)(n2 >> 12 | 0xE0);
                segment.data[segment.limit + 1] = (byte)(n2 >> 6 & 0x3F | 0x80);
                segment.data[segment.limit + 2] = (byte)(n2 & 0x3F | 0x80);
                segment.limit += 3;
                Buffer buffer3 = buffer;
                buffer3.setSize$okio(buffer3.size() + 3L);
            } else if (n2 <= 0x10FFFF) {
                Segment segment = buffer.writableSegment$okio(4);
                segment.data[segment.limit] = (byte)(n2 >> 18 | 0xF0);
                segment.data[segment.limit + 1] = (byte)(n2 >> 12 & 0x3F | 0x80);
                segment.data[segment.limit + 2] = (byte)(n2 >> 6 & 0x3F | 0x80);
                segment.data[segment.limit + 3] = (byte)(n2 & 0x3F | 0x80);
                segment.limit += 4;
                Buffer buffer4 = buffer;
                buffer4.setSize$okio(buffer4.size() + 4L);
            } else {
                throw new IllegalArgumentException("Unexpected code point: 0x" + -SegmentedByteString.toHexString(n2));
            }
        }
        return buffer;
    }

    @Override
    @NotNull
    public final Buffer writeString(@NotNull String string, @NotNull Charset charset) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(charset, "");
        return this.writeString(string, 0, string.length(), charset);
    }

    @Override
    @NotNull
    public final Buffer writeString(@NotNull String object, int n2, int n3, @NotNull Charset charset) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(charset, "");
        if (!(n2 >= 0)) {
            object = "beginIndex < 0: " + n2;
            throw new IllegalArgumentException(object.toString());
        }
        if (!(n3 >= n2)) {
            object = "endIndex < beginIndex: " + n3 + " < " + n2;
            throw new IllegalArgumentException(object.toString());
        }
        if (!(n3 <= ((String)object).length())) {
            object = "endIndex > string.length: " + n3 + " > " + ((String)object).length();
            throw new IllegalArgumentException(object.toString());
        }
        if (Intrinsics.areEqual(charset, Charsets.UTF_8)) {
            return this.writeUtf8((String)object, n2, n3);
        }
        String string = ((String)object).substring(n2, n3);
        Intrinsics.checkNotNullExpressionValue(string, "");
        byte[] byArray = string.getBytes(charset);
        Intrinsics.checkNotNullExpressionValue(byArray, "");
        object = byArray;
        return this.write((byte[])object, 0, ((Object)object).length);
    }

    @Override
    @NotNull
    public final Buffer write(@NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Buffer buffer = this;
        return buffer.write(byArray, 0, byArray.length);
    }

    @Override
    @NotNull
    public final Buffer write(@NotNull byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Buffer buffer = this;
        int n4 = n2;
        -SegmentedByteString.checkOffsetAndCount(byArray.length, n2, n3);
        n2 += n3;
        while (n4 < n2) {
            Segment segment = buffer.writableSegment$okio(1);
            int n5 = Math.min(n2 - n4, 8192 - segment.limit);
            int n6 = n4;
            ArraysKt.copyInto(byArray, segment.data, segment.limit, n6, n6 + n5);
            n4 += n5;
            segment.limit += n5;
        }
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() + (long)n3);
        return buffer;
    }

    @Override
    public final int write(@NotNull ByteBuffer byteBuffer) throws IOException {
        int n2;
        Intrinsics.checkNotNullParameter(byteBuffer, "");
        int n3 = n2 = byteBuffer.remaining();
        while (n3 > 0) {
            Segment segment = this.writableSegment$okio(1);
            int n4 = Math.min(n3, 8192 - segment.limit);
            byteBuffer.get(segment.data, segment.limit, n4);
            n3 -= n4;
            segment.limit += n4;
        }
        this.size += (long)n2;
        return n2;
    }

    @Override
    public final long writeAll(@NotNull Source source2) throws IOException {
        long l2;
        Intrinsics.checkNotNullParameter(source2, "");
        Buffer buffer = this;
        long l3 = 0L;
        while ((l2 = source2.read(buffer, 8192L)) != -1L) {
            l3 += l2;
        }
        return l3;
    }

    @Override
    @NotNull
    public final Buffer write(@NotNull Source source2, long l2) throws IOException {
        long l3;
        Intrinsics.checkNotNullParameter(source2, "");
        Buffer buffer = this;
        for (long i2 = l2; i2 > 0L; i2 -= l3) {
            l3 = source2.read(buffer, i2);
            if (l3 != -1L) continue;
            throw new EOFException();
        }
        return buffer;
    }

    @Override
    @NotNull
    public final Buffer writeByte(int n2) {
        Buffer buffer = this;
        Segment segment = buffer.writableSegment$okio(1);
        int n3 = segment.limit;
        segment.limit = n3 + 1;
        segment.data[n3] = (byte)n2;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() + 1L);
        return buffer;
    }

    @Override
    @NotNull
    public final Buffer writeShort(int n2) {
        Buffer buffer = this;
        Segment segment = buffer.writableSegment$okio(2);
        byte[] byArray = segment.data;
        int n3 = segment.limit;
        byArray[n3++] = (byte)(n2 >>> 8);
        byArray[n3++] = (byte)n2;
        segment.limit = n3;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() + 2L);
        return buffer;
    }

    @Override
    @NotNull
    public final Buffer writeShortLe(int n2) {
        return this.writeShort(-SegmentedByteString.reverseBytes((short)n2));
    }

    @Override
    @NotNull
    public final Buffer writeInt(int n2) {
        Buffer buffer = this;
        Segment segment = buffer.writableSegment$okio(4);
        byte[] byArray = segment.data;
        int n3 = segment.limit;
        byArray[n3++] = (byte)(n2 >>> 24);
        byArray[n3++] = (byte)(n2 >>> 16);
        byArray[n3++] = (byte)(n2 >>> 8);
        byArray[n3++] = (byte)n2;
        segment.limit = n3;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() + 4L);
        return buffer;
    }

    @Override
    @NotNull
    public final Buffer writeIntLe(int n2) {
        return this.writeInt(-SegmentedByteString.reverseBytes(n2));
    }

    @Override
    @NotNull
    public final Buffer writeLong(long l2) {
        Buffer buffer = this;
        Segment segment = buffer.writableSegment$okio(8);
        byte[] byArray = segment.data;
        int n2 = segment.limit;
        byArray[n2++] = (byte)(l2 >>> 56 & 0xFFL);
        byArray[n2++] = (byte)(l2 >>> 48 & 0xFFL);
        byArray[n2++] = (byte)(l2 >>> 40 & 0xFFL);
        byArray[n2++] = (byte)(l2 >>> 32 & 0xFFL);
        byArray[n2++] = (byte)(l2 >>> 24 & 0xFFL);
        byArray[n2++] = (byte)(l2 >>> 16 & 0xFFL);
        byArray[n2++] = (byte)(l2 >>> 8 & 0xFFL);
        byArray[n2++] = (byte)(l2 & 0xFFL);
        segment.limit = n2;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() + 8L);
        return buffer;
    }

    @Override
    @NotNull
    public final Buffer writeLongLe(long l2) {
        return this.writeLong(-SegmentedByteString.reverseBytes(l2));
    }

    @Override
    @NotNull
    public final Buffer writeDecimalLong(long l2) {
        int n2;
        Buffer buffer = this;
        long l3 = l2;
        if (l2 == 0L) {
            return buffer.writeByte(48);
        }
        boolean bl = false;
        if (l2 < 0L) {
            l3 = -l2;
            if (l3 < 0L) {
                return buffer.writeUtf8("-9223372036854775808");
            }
            bl = true;
        }
        int n3 = l3 < 100000000L ? (l3 < 10000L ? (l3 < 100L ? (l3 < 10L ? 1 : 2) : (l3 < 1000L ? 3 : 4)) : (l3 < 1000000L ? (l3 < 100000L ? 5 : 6) : (l3 < 10000000L ? 7 : 8))) : (l3 < 1000000000000L ? (l3 < 10000000000L ? (l3 < 1000000000L ? 9 : 10) : (l3 < 100000000000L ? 11 : 12)) : (l3 < 1000000000000000L ? (l3 < 10000000000000L ? 13 : (l3 < 100000000000000L ? 14 : 15)) : (l3 < 100000000000000000L ? (l3 < 10000000000000000L ? 16 : 17) : (n2 = l3 < 1000000000000000000L ? 18 : 19))));
        if (bl) {
            ++n2;
        }
        Segment segment = buffer.writableSegment$okio(n2);
        byte[] byArray = segment.data;
        int n4 = segment.limit + n2;
        while (l3 != 0L) {
            int n5 = (int)(l3 % 10L);
            byArray[--n4] = -Buffer.getHEX_DIGIT_BYTES()[n5];
            l3 /= 10L;
        }
        if (bl) {
            byArray[--n4] = 45;
        }
        segment.limit += n2;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() + (long)n2);
        return buffer;
    }

    @Override
    @NotNull
    public final Buffer writeHexadecimalUnsignedLong(long l2) {
        Buffer buffer = this;
        long l3 = l2;
        if (l2 == 0L) {
            return buffer.writeByte(48);
        }
        long l4 = l3;
        long l5 = l4 | l4 >>> 1;
        l5 |= l5 >>> 2;
        l5 |= l5 >>> 4;
        l5 |= l5 >>> 8;
        l5 |= l5 >>> 16;
        l5 |= l5 >>> 32;
        l5 -= l5 >>> 1 & 0x5555555555555555L;
        l5 = (l5 >>> 2 & 0x3333333333333333L) + (l5 & 0x3333333333333333L);
        l5 = (l5 >>> 4) + l5 & 0xF0F0F0F0F0F0F0FL;
        l5 += l5 >>> 8;
        l5 += l5 >>> 16;
        l5 = (l5 & 0x3FL) + (l5 >>> 32 & 0x3FL);
        int n2 = (int)((l5 + 3L) / 4L);
        Segment segment = buffer.writableSegment$okio(n2);
        byte[] byArray = segment.data;
        int n3 = segment.limit;
        for (int i2 = segment.limit + n2 - 1; i2 >= n3; --i2) {
            byArray[i2] = -Buffer.getHEX_DIGIT_BYTES()[(int)(l3 & 0xFL)];
            l3 >>>= 4;
        }
        segment.limit += n2;
        Buffer buffer2 = buffer;
        buffer2.setSize$okio(buffer2.size() + (long)n2);
        return buffer;
    }

    @NotNull
    public final Segment writableSegment$okio(int n2) {
        Segment segment;
        Buffer buffer = this;
        if (!(n2 > 0 && n2 <= 8192)) {
            String string = "unexpected capacity";
            throw new IllegalArgumentException(string.toString());
        }
        if (buffer.head == null) {
            buffer.head = segment = SegmentPool.take();
            segment.prev = segment;
            segment.next = segment;
        } else {
            Segment segment2 = buffer.head;
            Intrinsics.checkNotNull(segment2);
            segment = segment2.prev;
            Intrinsics.checkNotNull(segment);
            if (segment.limit + n2 > 8192 || !segment.owner) {
                segment = segment.push(SegmentPool.take());
            }
        }
        return segment;
    }

    @Override
    public final void write(@NotNull Buffer object, long l2) {
        long l3;
        Intrinsics.checkNotNullParameter(object, "");
        Buffer buffer = this;
        if (!(object != buffer)) {
            object = "source == this";
            throw new IllegalArgumentException(object.toString());
        }
        -SegmentedByteString.checkOffsetAndCount(((Buffer)object).size(), 0L, l2);
        for (long i2 = l2; i2 > 0L; i2 -= l3) {
            Segment segment = ((Buffer)object).head;
            Intrinsics.checkNotNull(segment);
            int n2 = segment.limit;
            Segment segment2 = ((Buffer)object).head;
            Intrinsics.checkNotNull(segment2);
            if (i2 < (long)(n2 - segment2.pos)) {
                Segment segment3;
                Segment segment4;
                if (buffer.head != null) {
                    Segment segment5 = buffer.head;
                    Intrinsics.checkNotNull(segment5);
                    segment4 = segment5.prev;
                } else {
                    segment4 = segment3 = null;
                }
                if (segment4 != null && segment3.owner && i2 + (long)segment3.limit - (long)(segment3.shared ? 0 : segment3.pos) <= 8192L) {
                    Segment segment6 = ((Buffer)object).head;
                    Intrinsics.checkNotNull(segment6);
                    segment6.writeTo(segment3, (int)i2);
                    Object object2 = object;
                    ((Buffer)object2).setSize$okio(((Buffer)object2).size() - i2);
                    Buffer buffer2 = buffer;
                    buffer2.setSize$okio(buffer2.size() + i2);
                    return;
                }
                Segment segment7 = ((Buffer)object).head;
                Intrinsics.checkNotNull(segment7);
                ((Buffer)object).head = segment7.split((int)i2);
            }
            Segment segment8 = ((Buffer)object).head;
            Intrinsics.checkNotNull(segment8);
            l3 = segment8.limit - segment8.pos;
            ((Buffer)object).head = segment8.pop();
            if (buffer.head == null) {
                buffer.head = segment8;
                segment8.next = segment8.prev = segment8;
            } else {
                Segment segment9 = buffer.head;
                Intrinsics.checkNotNull(segment9);
                Segment segment10 = segment9.prev;
                Intrinsics.checkNotNull(segment10);
                segment10 = segment10.push(segment8);
                segment10.compact();
            }
            Object object3 = object;
            ((Buffer)object3).setSize$okio(((Buffer)object3).size() - l3);
            Buffer buffer3 = buffer;
            buffer3.setSize$okio(buffer3.size() + l3);
        }
    }

    @Override
    public final long read(@NotNull Buffer object, long l2) {
        Intrinsics.checkNotNullParameter(object, "");
        Buffer buffer = this;
        long l3 = l2;
        if (!(l2 >= 0L)) {
            object = "byteCount < 0: " + l2;
            throw new IllegalArgumentException(object.toString());
        }
        if (buffer.size() == 0L) {
            return -1L;
        }
        if (l2 > buffer.size()) {
            l3 = buffer.size();
        }
        ((Buffer)object).write(buffer, l3);
        return l3;
    }

    @Override
    public final long indexOf(byte by) {
        return this.indexOf(by, 0L, Long.MAX_VALUE);
    }

    @Override
    public final long indexOf(byte by, long l2) {
        return this.indexOf(by, l2, Long.MAX_VALUE);
    }

    @Override
    public final long indexOf(byte by, long l2, long l3) {
        Buffer buffer = this;
        long l4 = l2;
        long l5 = l3;
        if (!(0L <= l2 ? l2 <= l3 : false)) {
            String string = "size=" + buffer.size() + " fromIndex=" + l2 + " toIndex=" + l3;
            throw new IllegalArgumentException(string.toString());
        }
        if (l3 > buffer.size()) {
            l5 = buffer.size();
        }
        if (l2 != l5) {
            Buffer buffer2 = buffer;
            Segment segment = buffer2.head;
            if (segment != null) {
                Object object = segment;
                if (buffer2.size() - l2 < l2) {
                    long l6;
                    for (l6 = buffer2.size(); l6 > l2; l6 -= (long)(((Segment)object).limit - ((Segment)object).pos)) {
                        Intrinsics.checkNotNull(((Segment)object).prev);
                    }
                    long l7 = l6;
                    Object object2 = object;
                    if (object2 != null) {
                        long l8 = l7;
                        while (l8 < l5) {
                            byte[] byArray = ((Segment)object2).data;
                            int n2 = (int)Math.min((long)((Segment)object2).limit, (long)((Segment)object2).pos + l5 - l8);
                            for (int i2 = (int)((long)((Segment)object2).pos + l4 - l8); i2 < n2; ++i2) {
                                if (byArray[i2] != by) continue;
                                return (long)(i2 - ((Segment)object2).pos) + l8;
                            }
                            l4 = l8 += (long)(((Segment)object2).limit - ((Segment)object2).pos);
                            Intrinsics.checkNotNull(((Segment)object2).next);
                        }
                        return -1L;
                    }
                } else {
                    long l9;
                    long l10 = 0L;
                    while ((l9 = l10 + (long)(((Segment)object).limit - ((Segment)object).pos)) <= l2) {
                        Intrinsics.checkNotNull(((Segment)object).next);
                        l10 = l9;
                    }
                    long l11 = l10;
                    Object object3 = object;
                    if (object3 != null) {
                        long l12 = l11;
                        while (l12 < l5) {
                            object = ((Segment)object3).data;
                            int n3 = (int)Math.min((long)((Segment)object3).limit, (long)((Segment)object3).pos + l5 - l12);
                            for (int i3 = (int)((long)((Segment)object3).pos + l4 - l12); i3 < n3; ++i3) {
                                if (object[i3] != by) continue;
                                return (long)(i3 - ((Segment)object3).pos) + l12;
                            }
                            l4 = l12 += (long)(((Segment)object3).limit - ((Segment)object3).pos);
                            Intrinsics.checkNotNull(((Segment)object3).next);
                        }
                    }
                }
            }
        }
        return -1L;
    }

    @Override
    public final long indexOf(@NotNull ByteString byteString) throws IOException {
        Intrinsics.checkNotNullParameter(byteString, "");
        return this.indexOf(byteString, 0L);
    }

    @Override
    public final long indexOf(@NotNull ByteString object, long l2) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        Object object2 = this;
        long l3 = l2;
        if (!(((ByteString)object).size() > 0)) {
            object = "bytes is empty";
            throw new IllegalArgumentException(object.toString());
        }
        if (!(l2 >= 0L)) {
            object = "fromIndex < 0: " + l2;
            throw new IllegalArgumentException(object.toString());
        }
        Buffer buffer = object2;
        Segment segment = buffer.head;
        if (segment != null) {
            Object object3 = segment;
            if (buffer.size() - l2 < l2) {
                long l4;
                for (l4 = buffer.size(); l4 > l2; l4 -= (long)(((Segment)object3).limit - ((Segment)object3).pos)) {
                    Intrinsics.checkNotNull(((Segment)object3).prev);
                }
                long l5 = l4;
                Object object4 = object3;
                if (object4 != null) {
                    long l6 = l5;
                    byte[] byArray = ((ByteString)object).internalArray$okio();
                    byte by = byArray[0];
                    int n2 = ((ByteString)object).size();
                    long l7 = ((Buffer)object2).size() - (long)n2 + 1L;
                    while (l6 < l7) {
                        object = ((Segment)object4).data;
                        int n3 = ((Segment)object4).limit;
                        long l8 = (long)((Segment)object4).pos + l7 - l6;
                        int n4 = (int)Math.min((long)n3, l8);
                        for (n3 = (int)((long)((Segment)object4).pos + l3 - l6); n3 < n4; ++n3) {
                            if (object[n3] != by || !-Buffer.rangeEquals((Segment)object4, n3 + 1, byArray, 1, n2)) continue;
                            return (long)(n3 - ((Segment)object4).pos) + l6;
                        }
                        l3 = l6 += (long)(((Segment)object4).limit - ((Segment)object4).pos);
                        Intrinsics.checkNotNull(((Segment)object4).next);
                    }
                    return -1L;
                }
            } else {
                long l9;
                long l10 = 0L;
                while ((l9 = l10 + (long)(((Segment)object3).limit - ((Segment)object3).pos)) <= l2) {
                    Intrinsics.checkNotNull(((Segment)object3).next);
                    l10 = l9;
                }
                long l11 = l10;
                Object object5 = object3;
                if (object5 != null) {
                    long l12 = l11;
                    byte[] byArray = ((ByteString)object).internalArray$okio();
                    object3 = byArray;
                    byte by = byArray[0];
                    int n5 = ((ByteString)object).size();
                    long l13 = ((Buffer)object2).size() - (long)n5 + 1L;
                    while (l12 < l13) {
                        object2 = ((Segment)object5).data;
                        int n6 = ((Segment)object5).limit;
                        long l14 = (long)((Segment)object5).pos + l13 - l12;
                        int n7 = (int)Math.min((long)n6, l14);
                        for (n6 = (int)((long)((Segment)object5).pos + l3 - l12); n6 < n7; ++n6) {
                            if (object2[n6] != by || !-Buffer.rangeEquals((Segment)object5, n6 + 1, (byte[])object3, 1, n5)) continue;
                            return (long)(n6 - ((Segment)object5).pos) + l12;
                        }
                        l3 = l12 += (long)(((Segment)object5).limit - ((Segment)object5).pos);
                        Intrinsics.checkNotNull(((Segment)object5).next);
                    }
                }
            }
        }
        return -1L;
    }

    @Override
    public final long indexOfElement(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return this.indexOfElement(byteString, 0L);
    }

    @Override
    public final long indexOfElement(@NotNull ByteString object, long l2) {
        block18: {
            long l3;
            Object object2;
            Object object3;
            long l4;
            Buffer buffer;
            block19: {
                long l5;
                Intrinsics.checkNotNullParameter(object, "");
                buffer = this;
                l4 = l2;
                if (!(l2 >= 0L)) {
                    object = "fromIndex < 0: " + l2;
                    throw new IllegalArgumentException(object.toString());
                }
                object3 = buffer;
                Segment segment = ((Buffer)object3).head;
                if (segment == null) break block18;
                object2 = segment;
                if (((Buffer)object3).size() - l2 >= l2) break block19;
                for (l5 = ((Buffer)object3).size(); l5 > l2; l5 -= (long)(((Segment)object2).limit - ((Segment)object2).pos)) {
                    Intrinsics.checkNotNull(((Segment)object2).prev);
                }
                long l6 = l5;
                Object object4 = object2;
                if (object4 != null) {
                    long l7 = l6;
                    if (((ByteString)object).size() == 2) {
                        byte by = ((ByteString)object).getByte(0);
                        byte by2 = ((ByteString)object).getByte(1);
                        while (l7 < buffer.size()) {
                            object3 = ((Segment)object4).data;
                            int n2 = ((Segment)object4).limit;
                            for (int i2 = (int)((long)((Segment)object4).pos + l4 - l7); i2 < n2; ++i2) {
                                Object object5 = object3[i2];
                                if (object5 != by && object5 != by2) continue;
                                return (long)(i2 - ((Segment)object4).pos) + l7;
                            }
                            l4 = l7 += (long)(((Segment)object4).limit - ((Segment)object4).pos);
                            Intrinsics.checkNotNull(((Segment)object4).next);
                        }
                    } else {
                        byte[] byArray = ((ByteString)object).internalArray$okio();
                        while (l7 < buffer.size()) {
                            object = ((Segment)object4).data;
                            int n3 = ((Segment)object4).limit;
                            for (int i3 = (int)((long)((Segment)object4).pos + l4 - l7); i3 < n3; ++i3) {
                                Object object6 = object[i3];
                                for (byte by : byArray) {
                                    if (object6 != by) continue;
                                    return (long)(i3 - ((Segment)object4).pos) + l7;
                                }
                            }
                            l4 = l7 += (long)(((Segment)object4).limit - ((Segment)object4).pos);
                            Intrinsics.checkNotNull(((Segment)object4).next);
                        }
                    }
                    return -1L;
                }
                break block18;
            }
            long l8 = 0L;
            while ((l3 = l8 + (long)(((Segment)object2).limit - ((Segment)object2).pos)) <= l2) {
                Intrinsics.checkNotNull(((Segment)object2).next);
                l8 = l3;
            }
            long l9 = l8;
            Object object7 = object2;
            if (object7 == null) break block18;
            long l10 = l9;
            if (((ByteString)object).size() == 2) {
                byte by = ((ByteString)object).getByte(0);
                byte by3 = ((ByteString)object).getByte(1);
                while (l10 < buffer.size()) {
                    byte[] byArray = ((Segment)object7).data;
                    int n4 = ((Segment)object7).limit;
                    for (int i4 = (int)((long)((Segment)object7).pos + l4 - l10); i4 < n4; ++i4) {
                        byte by4 = byArray[i4];
                        if (by4 != by && by4 != by3) continue;
                        return (long)(i4 - ((Segment)object7).pos) + l10;
                    }
                    l4 = l10 += (long)(((Segment)object7).limit - ((Segment)object7).pos);
                    Intrinsics.checkNotNull(((Segment)object7).next);
                }
            } else {
                object3 = ((ByteString)object).internalArray$okio();
                while (l10 < buffer.size()) {
                    object2 = ((Segment)object7).data;
                    int n5 = ((Segment)object7).limit;
                    for (int i5 = (int)((long)((Segment)object7).pos + l4 - l10); i5 < n5; ++i5) {
                        Object object8 = object2[i5];
                        for (Object object9 : object3) {
                            if (object8 != object9) continue;
                            return (long)(i5 - ((Segment)object7).pos) + l10;
                        }
                    }
                    l4 = l10 += (long)(((Segment)object7).limit - ((Segment)object7).pos);
                    Intrinsics.checkNotNull(((Segment)object7).next);
                }
            }
        }
        return -1L;
    }

    @Override
    public final boolean rangeEquals(long l2, @NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return this.rangeEquals(l2, byteString, 0, byteString.size());
    }

    @Override
    public final boolean rangeEquals(long l2, @NotNull ByteString byteString, int n2, int n3) {
        Intrinsics.checkNotNullParameter(byteString, "");
        Buffer buffer = this;
        if (l2 < 0L || n2 < 0 || n3 < 0 || buffer.size() - l2 < (long)n3 || byteString.size() - n2 < n3) {
            return false;
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            if (buffer.getByte(l2 + (long)i2) == byteString.getByte(n2 + i2)) continue;
            return false;
        }
        return true;
    }

    @Override
    public final void flush() {
    }

    @Override
    public final boolean isOpen() {
        return true;
    }

    @Override
    public final void close() {
    }

    @Override
    @NotNull
    public final Timeout timeout() {
        return Timeout.NONE;
    }

    @NotNull
    public final ByteString md5() {
        return this.digest("MD5");
    }

    @NotNull
    public final ByteString sha1() {
        return this.digest("SHA-1");
    }

    @NotNull
    public final ByteString sha256() {
        return this.digest("SHA-256");
    }

    @NotNull
    public final ByteString sha512() {
        return this.digest("SHA-512");
    }

    private final ByteString digest(String object) {
        object = MessageDigest.getInstance((String)object);
        Segment segment = this.head;
        if (segment != null) {
            Segment segment2 = segment;
            ((MessageDigest)object).update(segment2.data, segment2.pos, segment2.limit - segment2.pos);
            Segment segment3 = segment2.next;
            Intrinsics.checkNotNull(segment3);
            Segment segment4 = segment3;
            while (segment4 != segment2) {
                ((MessageDigest)object).update(segment4.data, segment4.pos, segment4.limit - segment4.pos);
                Intrinsics.checkNotNull(segment4.next);
            }
        }
        byte[] byArray = ((MessageDigest)object).digest();
        Intrinsics.checkNotNullExpressionValue(byArray, "");
        return new ByteString(byArray);
    }

    @NotNull
    public final ByteString hmacSha1(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return this.hmac("HmacSHA1", byteString);
    }

    @NotNull
    public final ByteString hmacSha256(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return this.hmac("HmacSHA256", byteString);
    }

    @NotNull
    public final ByteString hmacSha512(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return this.hmac("HmacSHA512", byteString);
    }

    private final ByteString hmac(String object, ByteString object2) {
        try {
            Mac mac = Mac.getInstance((String)object);
            mac.init(new SecretKeySpec(((ByteString)object2).internalArray$okio(), (String)object));
            Segment segment = this.head;
            if (segment != null) {
                object = segment;
                mac.update(((Segment)object).data, ((Segment)object).pos, ((Segment)object).limit - ((Segment)object).pos);
                Segment segment2 = ((Segment)object).next;
                Intrinsics.checkNotNull(segment2);
                object2 = segment2;
                while (object2 != object) {
                    mac.update(((Segment)object2).data, ((Segment)object2).pos, ((Segment)object2).limit - ((Segment)object2).pos);
                    Intrinsics.checkNotNull(((Segment)object2).next);
                }
            }
            byte[] byArray = mac.doFinal();
            Intrinsics.checkNotNullExpressionValue(byArray, "");
            return new ByteString(byArray);
        }
        catch (InvalidKeyException invalidKeyException) {
            throw new IllegalArgumentException(invalidKeyException);
        }
    }

    public final boolean equals(@Nullable Object object) {
        Buffer buffer = this;
        if (buffer != object) {
            if (!(object instanceof Buffer)) {
                return false;
            }
            if (buffer.size() != ((Buffer)object).size()) {
                return false;
            }
            if (buffer.size() != 0L) {
                long l2;
                Segment segment = buffer.head;
                Intrinsics.checkNotNull(segment);
                Segment segment2 = segment;
                Segment segment3 = ((Buffer)object).head;
                Intrinsics.checkNotNull(segment3);
                object = segment3;
                int n2 = segment2.pos;
                int n3 = ((Segment)object).pos;
                for (long i2 = 0L; i2 < buffer.size(); i2 += l2) {
                    l2 = Math.min(segment2.limit - n2, ((Segment)object).limit - n3);
                    long l3 = l2;
                    for (long i3 = 0L; i3 < l3; ++i3) {
                        if (segment2.data[n2++] == ((Segment)object).data[n3++]) continue;
                        return false;
                    }
                    if (n2 == segment2.limit) {
                        Intrinsics.checkNotNull(segment2.next);
                        n2 = segment2.pos;
                    }
                    if (n3 != ((Segment)object).limit) continue;
                    Intrinsics.checkNotNull(((Segment)object).next);
                    n3 = ((Segment)object).pos;
                }
            }
        }
        return true;
    }

    public final int hashCode() {
        Buffer buffer = this;
        Segment segment = buffer.head;
        if (segment == null) {
            return 0;
        }
        Segment segment2 = segment;
        int n2 = 1;
        do {
            int n3 = segment2.limit;
            for (int i2 = segment2.pos; i2 < n3; ++i2) {
                n2 = n2 * 31 + segment2.data[i2];
            }
            Intrinsics.checkNotNull(segment2.next);
        } while (segment2 != buffer.head);
        return n2;
    }

    @NotNull
    public final String toString() {
        return this.snapshot().toString();
    }

    @NotNull
    public final Buffer copy() {
        Segment segment;
        Buffer buffer = this;
        Buffer buffer2 = new Buffer();
        if (buffer.size() == 0L) {
            return buffer2;
        }
        Segment segment2 = buffer.head;
        Intrinsics.checkNotNull(segment2);
        Segment segment3 = segment2;
        segment.next = segment.prev = (buffer2.head = (segment = segment2.sharedCopy()));
        Segment segment4 = segment3.next;
        while (segment4 != segment3) {
            Segment segment5 = segment.prev;
            Intrinsics.checkNotNull(segment5);
            Segment segment6 = segment4;
            Intrinsics.checkNotNull(segment6);
            segment5.push(segment6.sharedCopy());
            segment4 = segment4.next;
        }
        buffer2.setSize$okio(buffer.size());
        return buffer2;
    }

    @NotNull
    public final Buffer clone() {
        return this.copy();
    }

    @NotNull
    public final ByteString snapshot() {
        Object object = this;
        if (!(((Buffer)object).size() <= Integer.MAX_VALUE)) {
            object = "size > Int.MAX_VALUE: " + ((Buffer)object).size();
            throw new IllegalStateException(object.toString());
        }
        Buffer buffer = object;
        return buffer.snapshot((int)buffer.size());
    }

    @NotNull
    public final ByteString snapshot(int n2) {
        Buffer buffer = this;
        if (n2 == 0) {
            return ByteString.EMPTY;
        }
        -SegmentedByteString.checkOffsetAndCount(buffer.size(), 0L, n2);
        int n3 = 0;
        int n4 = 0;
        Segment segment = buffer.head;
        while (n3 < n2) {
            Segment segment2 = segment;
            Intrinsics.checkNotNull(segment2);
            if (segment2.limit == segment.pos) {
                throw new AssertionError((Object)"s.limit == s.pos");
            }
            n3 += segment.limit - segment.pos;
            ++n4;
            segment = segment.next;
        }
        byte[][] byArrayArray = new byte[n4][];
        int[] nArray = new int[n4 << 1];
        n3 = 0;
        n4 = 0;
        segment = buffer.head;
        while (n3 < n2) {
            Segment segment3 = segment;
            Intrinsics.checkNotNull(segment3);
            byArrayArray[n4] = segment3.data;
            nArray[n4] = Math.min(n3 += segment.limit - segment.pos, n2);
            nArray[n4 + ((Object[])byArrayArray).length] = segment.pos;
            segment.shared = true;
            ++n4;
            segment = segment.next;
        }
        return new SegmentedByteString(byArrayArray, nArray);
    }

    @JvmOverloads
    @NotNull
    public final UnsafeCursor readUnsafe(@NotNull UnsafeCursor unsafeCursor) {
        Intrinsics.checkNotNullParameter(unsafeCursor, "");
        return -Buffer.commonReadUnsafe(this, unsafeCursor);
    }

    public static /* synthetic */ UnsafeCursor readUnsafe$default(Buffer buffer, UnsafeCursor unsafeCursor, int n2, Object object) {
        if ((n2 & 1) != 0) {
            unsafeCursor = -SegmentedByteString.getDEFAULT__new_UnsafeCursor();
        }
        return buffer.readUnsafe(unsafeCursor);
    }

    @JvmOverloads
    @NotNull
    public final UnsafeCursor readAndWriteUnsafe(@NotNull UnsafeCursor unsafeCursor) {
        Intrinsics.checkNotNullParameter(unsafeCursor, "");
        return -Buffer.commonReadAndWriteUnsafe(this, unsafeCursor);
    }

    public static /* synthetic */ UnsafeCursor readAndWriteUnsafe$default(Buffer buffer, UnsafeCursor unsafeCursor, int n2, Object object) {
        if ((n2 & 1) != 0) {
            unsafeCursor = -SegmentedByteString.getDEFAULT__new_UnsafeCursor();
        }
        return buffer.readAndWriteUnsafe(unsafeCursor);
    }

    @Deprecated(message="moved to operator function", replaceWith=@ReplaceWith(expression="this[index]", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_getByte")
    public final byte -deprecated_getByte(long l2) {
        return this.getByte(l2);
    }

    @Deprecated(message="moved to val", replaceWith=@ReplaceWith(expression="size", imports={}), level=DeprecationLevel.ERROR)
    @JvmName(name="-deprecated_size")
    public final long -deprecated_size() {
        return this.size;
    }

    @JvmOverloads
    @NotNull
    public final Buffer copyTo(@NotNull OutputStream outputStream2, long l2) throws IOException {
        Intrinsics.checkNotNullParameter(outputStream2, "");
        return Buffer.copyTo$default(this, outputStream2, l2, 0L, 4, null);
    }

    @JvmOverloads
    @NotNull
    public final Buffer copyTo(@NotNull OutputStream outputStream2) throws IOException {
        Intrinsics.checkNotNullParameter(outputStream2, "");
        return Buffer.copyTo$default(this, outputStream2, 0L, 0L, 6, null);
    }

    @JvmOverloads
    @NotNull
    public final Buffer writeTo(@NotNull OutputStream outputStream2) throws IOException {
        Intrinsics.checkNotNullParameter(outputStream2, "");
        return Buffer.writeTo$default(this, outputStream2, 0L, 2, null);
    }

    @JvmOverloads
    @NotNull
    public final UnsafeCursor readUnsafe() {
        return Buffer.readUnsafe$default(this, null, 1, null);
    }

    @JvmOverloads
    @NotNull
    public final UnsafeCursor readAndWriteUnsafe() {
        return Buffer.readAndWriteUnsafe$default(this, null, 1, null);
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0014\u001a\u00020\u0015H\u0016J\u000e\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0017\u001a\u00020\bJ\u0006\u0010\u0018\u001a\u00020\bJ\u000e\u0010\u0019\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\nJ\u000e\u0010\u001b\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00068\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0007\u001a\u00020\b8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\t\u001a\u00020\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u000b\u001a\u00020\f8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0080\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u0012\u0010\u0013\u001a\u00020\b8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2={"Lokio/Buffer$UnsafeCursor;", "Ljava/io/Closeable;", "()V", "buffer", "Lokio/Buffer;", "data", "", "end", "", "offset", "", "readWrite", "", "segment", "Lokio/Segment;", "getSegment$okio", "()Lokio/Segment;", "setSegment$okio", "(Lokio/Segment;)V", "start", "close", "", "expandBuffer", "minByteCount", "next", "resizeBuffer", "newSize", "seek", "okio"})
    @SourceDebugExtension(value={"SMAP\nBuffer.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Buffer.kt\nokio/Buffer$UnsafeCursor\n+ 2 Buffer.kt\nokio/internal/-Buffer\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 Util.kt\nokio/-SegmentedByteString\n*L\n1#1,641:1\n1567#2:642\n1568#2:644\n1572#2:645\n1573#2,68:647\n1644#2:715\n1645#2,32:717\n1677#2,18:750\n1698#2:768\n1699#2,18:770\n1721#2:788\n1723#2,7:790\n1#3:643\n1#3:646\n1#3:716\n1#3:769\n1#3:789\n86#4:749\n*S KotlinDebug\n*F\n+ 1 Buffer.kt\nokio/Buffer$UnsafeCursor\n*L\n628#1:642\n628#1:644\n630#1:645\n630#1:647,68\n632#1:715\n632#1:717,32\n632#1:750,18\n634#1:768\n634#1:770,18\n637#1:788\n637#1:790,7\n628#1:643\n630#1:646\n632#1:716\n634#1:769\n637#1:789\n632#1:749\n*E\n"})
    public static final class UnsafeCursor
    implements Closeable {
        @JvmField
        @Nullable
        public Buffer buffer;
        @JvmField
        public boolean readWrite;
        @Nullable
        private Segment segment;
        @JvmField
        public long offset = -1L;
        @JvmField
        @Nullable
        public byte[] data;
        @JvmField
        public int start = -1;
        @JvmField
        public int end = -1;

        @Nullable
        public final Segment getSegment$okio() {
            return this.segment;
        }

        public final void setSegment$okio(@Nullable Segment segment) {
            this.segment = segment;
        }

        public final int next() {
            Object object = this;
            long l2 = ((UnsafeCursor)object).offset;
            Buffer buffer = ((UnsafeCursor)object).buffer;
            Intrinsics.checkNotNull(buffer);
            if (!(l2 != buffer.size())) {
                object = "no more bytes";
                throw new IllegalStateException(object.toString());
            }
            if (((UnsafeCursor)object).offset == -1L) {
                return ((UnsafeCursor)object).seek(0L);
            }
            Object object2 = object;
            return ((UnsafeCursor)object2).seek(((UnsafeCursor)object2).offset + (long)(((UnsafeCursor)object).end - ((UnsafeCursor)object).start));
        }

        public final int seek(long l2) {
            long l3;
            UnsafeCursor unsafeCursor = this;
            Buffer buffer = unsafeCursor.buffer;
            if (buffer == null) {
                String string = "not attached to a buffer";
                throw new IllegalStateException(string.toString());
            }
            Buffer buffer2 = buffer;
            if (l2 < -1L || l2 > buffer2.size()) {
                throw new ArrayIndexOutOfBoundsException("offset=" + l2 + " > size=" + buffer2.size());
            }
            if (l2 == -1L || l2 == buffer2.size()) {
                unsafeCursor.setSegment$okio(null);
                unsafeCursor.offset = l2;
                unsafeCursor.data = null;
                unsafeCursor.start = -1;
                unsafeCursor.end = -1;
                return -1;
            }
            long l4 = 0L;
            long l5 = buffer2.size();
            Segment segment = buffer2.head;
            Segment segment2 = buffer2.head;
            if (unsafeCursor.getSegment$okio() != null) {
                long l6 = unsafeCursor.offset;
                int n2 = unsafeCursor.start;
                Segment segment3 = unsafeCursor.getSegment$okio();
                Intrinsics.checkNotNull(segment3);
                long l7 = l6 - (long)(n2 - segment3.pos);
                if (l7 > l2) {
                    l5 = l7;
                    segment2 = unsafeCursor.getSegment$okio();
                } else {
                    l4 = l7;
                    segment = unsafeCursor.getSegment$okio();
                }
            }
            if (l5 - l2 > l2 - l4) {
                l3 = l4;
                while (true) {
                    Segment segment4 = segment;
                    Intrinsics.checkNotNull(segment4);
                    if (l2 >= l3 + (long)(segment4.limit - segment.pos)) {
                        l3 += (long)(segment.limit - segment.pos);
                        segment = segment.next;
                        continue;
                    }
                    break;
                }
            } else {
                Segment segment5;
                segment = segment2;
                for (l3 = l5; l3 > l2; l3 -= (long)(segment5.limit - segment.pos)) {
                    Segment segment6 = segment;
                    Intrinsics.checkNotNull(segment6);
                    segment5 = segment = segment6.prev;
                    Intrinsics.checkNotNull(segment5);
                }
            }
            if (unsafeCursor.readWrite) {
                Segment segment7 = segment;
                Intrinsics.checkNotNull(segment7);
                if (segment7.shared) {
                    Segment segment8 = segment.unsharedCopy();
                    if (buffer2.head == segment) {
                        buffer2.head = segment8;
                    }
                    segment = segment.push(segment8);
                    Segment segment9 = segment.prev;
                    Intrinsics.checkNotNull(segment9);
                    segment9.pop();
                }
            }
            unsafeCursor.setSegment$okio(segment);
            unsafeCursor.offset = l2;
            Segment segment10 = segment;
            Intrinsics.checkNotNull(segment10);
            unsafeCursor.data = segment10.data;
            unsafeCursor.start = segment.pos + (int)(l2 - l3);
            unsafeCursor.end = segment.limit;
            return unsafeCursor.end - unsafeCursor.start;
        }

        public final long resizeBuffer(long l2) {
            UnsafeCursor unsafeCursor = this;
            Buffer buffer = unsafeCursor.buffer;
            if (buffer == null) {
                String string = "not attached to a buffer";
                throw new IllegalStateException(string.toString());
            }
            Buffer buffer2 = buffer;
            if (!unsafeCursor.readWrite) {
                String string = "resizeBuffer() only permitted for read/write buffers";
                throw new IllegalStateException(string.toString());
            }
            long l3 = buffer2.size();
            if (l2 <= l3) {
                int n2;
                if (!(l2 >= 0L)) {
                    String string = "newSize < 0: " + l2;
                    throw new IllegalArgumentException(string.toString());
                }
                for (long i2 = l3 - l2; i2 > 0L; i2 -= (long)n2) {
                    Segment segment = buffer2.head;
                    Intrinsics.checkNotNull(segment);
                    Segment segment2 = segment.prev;
                    Intrinsics.checkNotNull(segment2);
                    n2 = segment2.limit - segment2.pos;
                    if ((long)n2 <= i2) {
                        buffer2.head = segment2.pop();
                        SegmentPool.recycle(segment2);
                        continue;
                    }
                    segment2.limit -= (int)i2;
                    break;
                }
                unsafeCursor.setSegment$okio(null);
                unsafeCursor.offset = l2;
                unsafeCursor.data = null;
                unsafeCursor.start = -1;
                unsafeCursor.end = -1;
            } else if (l2 > l3) {
                int n3;
                boolean bl = true;
                for (long i3 = l2 - l3; i3 > 0L; i3 -= (long)n3) {
                    Segment segment = buffer2.writableSegment$okio(1);
                    n3 = 8192 - segment.limit;
                    n3 = (int)Math.min(i3, (long)n3);
                    segment.limit += n3;
                    if (!bl) continue;
                    unsafeCursor.setSegment$okio(segment);
                    unsafeCursor.offset = l3;
                    unsafeCursor.data = segment.data;
                    unsafeCursor.start = segment.limit - n3;
                    unsafeCursor.end = segment.limit;
                    bl = false;
                }
            }
            buffer2.setSize$okio(l2);
            return l3;
        }

        public final long expandBuffer(int n2) {
            UnsafeCursor unsafeCursor = this;
            if (!(n2 > 0)) {
                String string = "minByteCount <= 0: " + n2;
                throw new IllegalArgumentException(string.toString());
            }
            if (!(n2 <= 8192)) {
                String string = "minByteCount > Segment.SIZE: " + n2;
                throw new IllegalArgumentException(string.toString());
            }
            Buffer buffer = unsafeCursor.buffer;
            if (buffer == null) {
                String string = "not attached to a buffer";
                throw new IllegalStateException(string.toString());
            }
            Buffer buffer2 = buffer;
            if (!unsafeCursor.readWrite) {
                String string = "expandBuffer() only permitted for read/write buffers";
                throw new IllegalStateException(string.toString());
            }
            long l2 = buffer2.size();
            Segment segment = buffer2.writableSegment$okio(n2);
            int n3 = 8192 - segment.limit;
            segment.limit = 8192;
            buffer2.setSize$okio(l2 + (long)n3);
            unsafeCursor.setSegment$okio(segment);
            unsafeCursor.offset = l2;
            unsafeCursor.data = segment.data;
            unsafeCursor.start = 8192 - n3;
            unsafeCursor.end = 8192;
            return n3;
        }

        @Override
        public final void close() {
            Object object = this;
            if (!(((UnsafeCursor)object).buffer != null)) {
                object = "not attached to a buffer";
                throw new IllegalStateException(object.toString());
            }
            ((UnsafeCursor)object).buffer = null;
            ((UnsafeCursor)object).setSegment$okio(null);
            ((UnsafeCursor)object).offset = -1L;
            ((UnsafeCursor)object).data = null;
            ((UnsafeCursor)object).start = -1;
            ((UnsafeCursor)object).end = -1;
        }
    }
}

