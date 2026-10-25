/*
 * Decompiled with CFR 0.152.
 */
package okio.internal;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import okio.-Base64;
import okio.-SegmentedByteString;
import okio.Buffer;
import okio.ByteString;
import okio._JvmPlatformKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=2, xi=48, d1={"\u0000R\n\u0000\n\u0002\u0010\u0019\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0017\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0007H\u0002\u001a\u0011\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\tH\u0080\b\u001a\u0010\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0010H\u0002\u001a\r\u0010\u0011\u001a\u00020\u0012*\u00020\fH\u0080\b\u001a\r\u0010\u0013\u001a\u00020\u0012*\u00020\fH\u0080\b\u001a\u0015\u0010\u0014\u001a\u00020\u0007*\u00020\f2\u0006\u0010\u0015\u001a\u00020\fH\u0080\b\u001a-\u0010\u0016\u001a\u00020\u0017*\u00020\f2\u0006\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u0019\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0007H\u0080\b\u001a\u000f\u0010\u001c\u001a\u0004\u0018\u00010\f*\u00020\u0012H\u0080\b\u001a\r\u0010\u001d\u001a\u00020\f*\u00020\u0012H\u0080\b\u001a\r\u0010\u001e\u001a\u00020\f*\u00020\u0012H\u0080\b\u001a\u0015\u0010\u001f\u001a\u00020 *\u00020\f2\u0006\u0010!\u001a\u00020\tH\u0080\b\u001a\u0015\u0010\u001f\u001a\u00020 *\u00020\f2\u0006\u0010!\u001a\u00020\fH\u0080\b\u001a\u0017\u0010\"\u001a\u00020 *\u00020\f2\b\u0010\u0015\u001a\u0004\u0018\u00010#H\u0080\b\u001a\u0015\u0010$\u001a\u00020%*\u00020\f2\u0006\u0010&\u001a\u00020\u0007H\u0080\b\u001a\r\u0010'\u001a\u00020\u0007*\u00020\fH\u0080\b\u001a\r\u0010(\u001a\u00020\u0007*\u00020\fH\u0080\b\u001a\r\u0010)\u001a\u00020\u0012*\u00020\fH\u0080\b\u001a\u001d\u0010*\u001a\u00020\u0007*\u00020\f2\u0006\u0010\u0015\u001a\u00020\t2\u0006\u0010+\u001a\u00020\u0007H\u0080\b\u001a\r\u0010,\u001a\u00020\t*\u00020\fH\u0080\b\u001a\u001d\u0010-\u001a\u00020\u0007*\u00020\f2\u0006\u0010\u0015\u001a\u00020\t2\u0006\u0010+\u001a\u00020\u0007H\u0080\b\u001a\u001d\u0010-\u001a\u00020\u0007*\u00020\f2\u0006\u0010\u0015\u001a\u00020\f2\u0006\u0010+\u001a\u00020\u0007H\u0080\b\u001a-\u0010.\u001a\u00020 *\u00020\f2\u0006\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\t2\u0006\u0010/\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0007H\u0080\b\u001a-\u0010.\u001a\u00020 *\u00020\f2\u0006\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\f2\u0006\u0010/\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0007H\u0080\b\u001a\u0015\u00100\u001a\u00020 *\u00020\f2\u0006\u00101\u001a\u00020\tH\u0080\b\u001a\u0015\u00100\u001a\u00020 *\u00020\f2\u0006\u00101\u001a\u00020\fH\u0080\b\u001a\u001d\u00102\u001a\u00020\f*\u00020\f2\u0006\u00103\u001a\u00020\u00072\u0006\u00104\u001a\u00020\u0007H\u0080\b\u001a\r\u00105\u001a\u00020\f*\u00020\fH\u0080\b\u001a\r\u00106\u001a\u00020\f*\u00020\fH\u0080\b\u001a\r\u00107\u001a\u00020\t*\u00020\fH\u0080\b\u001a\u001d\u00108\u001a\u00020\f*\u00020\t2\u0006\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0007H\u0080\b\u001a\r\u00109\u001a\u00020\u0012*\u00020\fH\u0080\b\u001a\r\u0010:\u001a\u00020\u0012*\u00020\fH\u0080\b\u001a$\u0010;\u001a\u00020\u0017*\u00020\f2\u0006\u0010<\u001a\u00020=2\u0006\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0007H\u0000\"\u001c\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004\u00a2\u0006\u000e\n\u0000\u0012\u0004\b\u0002\u0010\u0003\u001a\u0004\b\u0004\u0010\u0005\u00a8\u0006>"}, d2={"HEX_DIGIT_CHARS", "", "getHEX_DIGIT_CHARS$annotations", "()V", "getHEX_DIGIT_CHARS", "()[C", "codePointIndexToCharIndex", "", "s", "", "codePointCount", "commonOf", "Lokio/ByteString;", "data", "decodeHexDigit", "c", "", "commonBase64", "", "commonBase64Url", "commonCompareTo", "other", "commonCopyInto", "", "offset", "target", "targetOffset", "byteCount", "commonDecodeBase64", "commonDecodeHex", "commonEncodeUtf8", "commonEndsWith", "", "suffix", "commonEquals", "", "commonGetByte", "", "pos", "commonGetSize", "commonHashCode", "commonHex", "commonIndexOf", "fromIndex", "commonInternalArray", "commonLastIndexOf", "commonRangeEquals", "otherOffset", "commonStartsWith", "prefix", "commonSubstring", "beginIndex", "endIndex", "commonToAsciiLowercase", "commonToAsciiUppercase", "commonToByteArray", "commonToByteString", "commonToString", "commonUtf8", "commonWrite", "buffer", "Lokio/Buffer;", "okio"})
@JvmName(name="-ByteString")
@SourceDebugExtension(value={"SMAP\nByteString.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ByteString.kt\nokio/internal/-ByteString\n+ 2 Util.kt\nokio/-SegmentedByteString\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 Utf8.kt\nokio/Utf8\n*L\n1#1,363:1\n131#1,2:369\n133#1,9:372\n68#2:364\n74#2:365\n74#2:367\n74#2:368\n68#2:396\n74#2:408\n1#3:366\n1#3:371\n212#4,7:381\n122#4:388\n219#4,5:389\n122#4:394\n226#4:395\n228#4:397\n397#4,2:398\n122#4:400\n400#4,6:401\n127#4:407\n406#4:409\n122#4:410\n407#4,13:411\n122#4:424\n422#4:425\n122#4:426\n425#4:427\n230#4,3:428\n440#4,3:431\n122#4:434\n443#4:435\n127#4:436\n446#4,10:437\n127#4:447\n456#4:448\n122#4:449\n457#4,4:450\n127#4:454\n461#4:455\n122#4:456\n462#4,14:457\n122#4:471\n477#4,2:472\n122#4:474\n481#4:475\n122#4:476\n484#4:477\n234#4,3:478\n500#4,3:481\n122#4:484\n503#4:485\n127#4:486\n506#4,2:487\n127#4:489\n510#4,10:490\n127#4:500\n520#4:501\n122#4:502\n521#4,4:503\n127#4:507\n525#4:508\n122#4:509\n526#4,4:510\n127#4:514\n530#4:515\n122#4:516\n531#4,15:517\n122#4:532\n547#4,2:533\n122#4:535\n550#4,2:536\n122#4:538\n554#4:539\n122#4:540\n557#4:541\n241#4:542\n122#4:543\n242#4,5:544\n*S KotlinDebug\n*F\n+ 1 ByteString.kt\nokio/internal/-ByteString\n*L\n329#1:369,2\n329#1:372,9\n67#1:364\n68#1:365\n258#1:367\n259#1:368\n348#1:396\n348#1:408\n329#1:371\n348#1:381,7\n353#1:388\n348#1:389,5\n353#1:394\n348#1:395\n348#1:397\n348#1:398,2\n353#1:400\n348#1:401,6\n348#1:407\n348#1:409\n353#1:410\n348#1:411,13\n353#1:424\n348#1:425\n353#1:426\n348#1:427\n348#1:428,3\n348#1:431,3\n353#1:434\n348#1:435\n348#1:436\n348#1:437,10\n348#1:447\n348#1:448\n353#1:449\n348#1:450,4\n348#1:454\n348#1:455\n353#1:456\n348#1:457,14\n353#1:471\n348#1:472,2\n353#1:474\n348#1:475\n353#1:476\n348#1:477\n348#1:478,3\n348#1:481,3\n353#1:484\n348#1:485\n348#1:486\n348#1:487,2\n348#1:489\n348#1:490,10\n348#1:500\n348#1:501\n353#1:502\n348#1:503,4\n348#1:507\n348#1:508\n353#1:509\n348#1:510,4\n348#1:514\n348#1:515\n353#1:516\n348#1:517,15\n353#1:532\n348#1:533,2\n353#1:535\n348#1:536,2\n353#1:538\n348#1:539\n353#1:540\n348#1:541\n348#1:542\n353#1:543\n348#1:544,5\n*E\n"})
public final class -ByteString {
    @NotNull
    private static final char[] HEX_DIGIT_CHARS;

    @NotNull
    public static final String commonUtf8(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        String string = byteString.getUtf8$okio();
        if (string == null) {
            string = _JvmPlatformKt.toUtf8String(byteString.internalArray$okio());
            byteString.setUtf8$okio(string);
        }
        return string;
    }

    @NotNull
    public static final String commonBase64(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return -Base64.encodeBase64$default(byteString.getData$okio(), null, 1, null);
    }

    @NotNull
    public static final String commonBase64Url(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return -Base64.encodeBase64(byteString.getData$okio(), -Base64.getBASE64_URL_SAFE());
    }

    @NotNull
    public static final char[] getHEX_DIGIT_CHARS() {
        return HEX_DIGIT_CHARS;
    }

    public static /* synthetic */ void getHEX_DIGIT_CHARS$annotations() {
    }

    @NotNull
    public static final String commonHex(@NotNull ByteString object) {
        Intrinsics.checkNotNullParameter(object, "");
        char[] cArray = new char[((ByteString)object).getData$okio().length << 1];
        int n2 = 0;
        for (Object object2 : (Object)((ByteString)object).getData$okio()) {
            int n3 = n2++;
            Object object3 = object2;
            cArray[n3] = -ByteString.getHEX_DIGIT_CHARS()[object3 >> 4 & 0xF];
            int n4 = n2++;
            object3 = object2;
            cArray[n4] = -ByteString.getHEX_DIGIT_CHARS()[object3 & 0xF];
        }
        return StringsKt.concatToString(cArray);
    }

    @NotNull
    public static final ByteString commonToAsciiLowercase(@NotNull ByteString object) {
        Intrinsics.checkNotNullParameter(object, "");
        for (int i2 = 0; i2 < ((ByteString)object).getData$okio().length; ++i2) {
            Object object2 = ((ByteString)object).getData$okio()[i2];
            if (object2 < 65 || object2 > 90) {
                continue;
            }
            byte[] byArray = ((ByteString)object).getData$okio();
            byte[] byArray2 = Arrays.copyOf(byArray, byArray.length);
            Intrinsics.checkNotNullExpressionValue(byArray2, "");
            object = byArray2;
            byArray2[i2++] = (byte)(object2 - -32);
            while (i2 < ((Object)object).length) {
                Object object3 = object[i2];
                object2 = object3;
                if (object3 < 65 || object2 > 90) {
                    ++i2;
                    continue;
                }
                object[i2] = (byte)(object2 - -32);
                ++i2;
            }
            return new ByteString((byte[])object);
        }
        return object;
    }

    @NotNull
    public static final ByteString commonToAsciiUppercase(@NotNull ByteString object) {
        Intrinsics.checkNotNullParameter(object, "");
        for (int i2 = 0; i2 < ((ByteString)object).getData$okio().length; ++i2) {
            Object object2 = ((ByteString)object).getData$okio()[i2];
            if (object2 < 97 || object2 > 122) {
                continue;
            }
            byte[] byArray = ((ByteString)object).getData$okio();
            byte[] byArray2 = Arrays.copyOf(byArray, byArray.length);
            Intrinsics.checkNotNullExpressionValue(byArray2, "");
            object = byArray2;
            byArray2[i2++] = (byte)(object2 - 32);
            while (i2 < ((Object)object).length) {
                Object object3 = object[i2];
                object2 = object3;
                if (object3 < 97 || object2 > 122) {
                    ++i2;
                    continue;
                }
                object[i2] = (byte)(object2 - 32);
                ++i2;
            }
            return new ByteString((byte[])object);
        }
        return object;
    }

    @NotNull
    public static final ByteString commonSubstring(@NotNull ByteString object, int n2, int n3) {
        Intrinsics.checkNotNullParameter(object, "");
        n3 = -SegmentedByteString.resolveDefaultParameter((ByteString)object, n3);
        if (!(n2 >= 0)) {
            object = "beginIndex < 0";
            throw new IllegalArgumentException(object.toString());
        }
        if (!(n3 <= ((ByteString)object).getData$okio().length)) {
            object = "endIndex > length(" + ((ByteString)object).getData$okio().length + ')';
            throw new IllegalArgumentException(object.toString());
        }
        int n4 = n3 - n2;
        if (!(n4 >= 0)) {
            object = "endIndex < beginIndex";
            throw new IllegalArgumentException(object.toString());
        }
        if (n2 == 0 && n3 == ((ByteString)object).getData$okio().length) {
            return object;
        }
        byte[] byArray = ((ByteString)object).getData$okio();
        object = byArray;
        return new ByteString(ArraysKt.copyOfRange(byArray, n2, n3));
    }

    public static final byte commonGetByte(@NotNull ByteString byteString, int n2) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return byteString.getData$okio()[n2];
    }

    public static final int commonGetSize(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return byteString.getData$okio().length;
    }

    @NotNull
    public static final byte[] commonToByteArray(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        byte[] byArray = byteString.getData$okio();
        byte[] byArray2 = Arrays.copyOf(byArray, byArray.length);
        Intrinsics.checkNotNullExpressionValue(byArray2, "");
        return byArray2;
    }

    @NotNull
    public static final byte[] commonInternalArray(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        return byteString.getData$okio();
    }

    public static final boolean commonRangeEquals(@NotNull ByteString byteString, int n2, @NotNull ByteString byteString2, int n3, int n4) {
        Intrinsics.checkNotNullParameter(byteString, "");
        Intrinsics.checkNotNullParameter(byteString2, "");
        return byteString2.rangeEquals(n3, byteString.getData$okio(), n2, n4);
    }

    public static final boolean commonRangeEquals(@NotNull ByteString byteString, int n2, @NotNull byte[] byArray, int n3, int n4) {
        Intrinsics.checkNotNullParameter(byteString, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        return n2 >= 0 && n2 <= byteString.getData$okio().length - n4 && n3 >= 0 && n3 <= byArray.length - n4 && -SegmentedByteString.arrayRangeEquals(byteString.getData$okio(), n2, byArray, n3, n4);
    }

    public static final void commonCopyInto(@NotNull ByteString byteString, int n2, @NotNull byte[] byArray, int n3, int n4) {
        Intrinsics.checkNotNullParameter(byteString, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        int n5 = n2;
        ArraysKt.copyInto(byteString.getData$okio(), byArray, n3, n5, n5 + n4);
    }

    public static final boolean commonStartsWith(@NotNull ByteString byteString, @NotNull ByteString byteString2) {
        Intrinsics.checkNotNullParameter(byteString, "");
        Intrinsics.checkNotNullParameter(byteString2, "");
        return byteString.rangeEquals(0, byteString2, 0, byteString2.size());
    }

    public static final boolean commonStartsWith(@NotNull ByteString byteString, @NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(byteString, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        return byteString.rangeEquals(0, byArray, 0, byArray.length);
    }

    public static final boolean commonEndsWith(@NotNull ByteString byteString, @NotNull ByteString byteString2) {
        Intrinsics.checkNotNullParameter(byteString, "");
        Intrinsics.checkNotNullParameter(byteString2, "");
        ByteString byteString3 = byteString;
        return byteString3.rangeEquals(byteString3.size() - byteString2.size(), byteString2, 0, byteString2.size());
    }

    public static final boolean commonEndsWith(@NotNull ByteString byteString, @NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(byteString, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        ByteString byteString2 = byteString;
        return byteString2.rangeEquals(byteString2.size() - byArray.length, byArray, 0, byArray.length);
    }

    public static final int commonIndexOf(@NotNull ByteString byteString, @NotNull byte[] byArray, int n2) {
        Intrinsics.checkNotNullParameter(byteString, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        int n3 = byteString.getData$okio().length - byArray.length;
        n2 = Math.max(n2, 0);
        if (n2 <= n3) {
            while (true) {
                if (-SegmentedByteString.arrayRangeEquals(byteString.getData$okio(), n2, byArray, 0, byArray.length)) {
                    return n2;
                }
                if (n2 == n3) break;
                ++n2;
            }
        }
        return -1;
    }

    public static final int commonLastIndexOf(@NotNull ByteString byteString, @NotNull ByteString byteString2, int n2) {
        Intrinsics.checkNotNullParameter(byteString, "");
        Intrinsics.checkNotNullParameter(byteString2, "");
        return byteString.lastIndexOf(byteString2.internalArray$okio(), n2);
    }

    public static final int commonLastIndexOf(@NotNull ByteString byteString, @NotNull byte[] byArray, int n2) {
        Intrinsics.checkNotNullParameter(byteString, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        n2 = -SegmentedByteString.resolveDefaultParameter(byteString, n2);
        int n3 = byteString.getData$okio().length - byArray.length;
        for (n2 = Math.min(n2, n3); n2 >= 0; --n2) {
            if (!-SegmentedByteString.arrayRangeEquals(byteString.getData$okio(), n2, byArray, 0, byArray.length)) continue;
            return n2;
        }
        return -1;
    }

    public static final boolean commonEquals(@NotNull ByteString byteString, @Nullable Object object) {
        Intrinsics.checkNotNullParameter(byteString, "");
        if (object == byteString) {
            return true;
        }
        if (object instanceof ByteString) {
            return ((ByteString)object).size() == byteString.getData$okio().length && ((ByteString)object).rangeEquals(0, byteString.getData$okio(), 0, byteString.getData$okio().length);
        }
        return false;
    }

    public static final int commonHashCode(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        int n2 = byteString.getHashCode$okio();
        if (n2 != 0) {
            return n2;
        }
        n2 = Arrays.hashCode(byteString.getData$okio());
        byteString.setHashCode$okio(n2);
        return n2;
    }

    public static final int commonCompareTo(@NotNull ByteString byteString, @NotNull ByteString byteString2) {
        Intrinsics.checkNotNullParameter(byteString, "");
        Intrinsics.checkNotNullParameter(byteString2, "");
        int n2 = byteString.size();
        int n3 = byteString2.size();
        int n4 = Math.min(n2, n3);
        for (int i2 = 0; i2 < n4; ++i2) {
            int n5 = byteString.getByte(i2);
            int n6 = n5 & 0xFF;
            n5 = byteString2.getByte(i2);
            if (n6 == (n5 &= 0xFF)) {
                continue;
            }
            if (n6 < n5) {
                return -1;
            }
            return 1;
        }
        if (n2 == n3) {
            return 0;
        }
        if (n2 < n3) {
            return -1;
        }
        return 1;
    }

    @NotNull
    public static final ByteString commonOf(@NotNull byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "");
        byte[] byArray2 = Arrays.copyOf(byArray, byArray.length);
        Intrinsics.checkNotNullExpressionValue(byArray2, "");
        return new ByteString(byArray2);
    }

    @NotNull
    public static final ByteString commonToByteString(@NotNull byte[] byArray, int n2, int n3) {
        Intrinsics.checkNotNullParameter(byArray, "");
        n3 = -SegmentedByteString.resolveDefaultParameter(byArray, n3);
        -SegmentedByteString.checkOffsetAndCount(byArray.length, n2, n3);
        n3 = n2 + n3;
        return new ByteString(ArraysKt.copyOfRange(byArray, n2, n3));
    }

    @NotNull
    public static final ByteString commonEncodeUtf8(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        ByteString byteString = new ByteString(_JvmPlatformKt.asUtf8ToByteArray(string));
        byteString.setUtf8$okio(string);
        return byteString;
    }

    @Nullable
    public static final ByteString commonDecodeBase64(@NotNull String object) {
        Intrinsics.checkNotNullParameter(object, "");
        byte[] byArray = -Base64.decodeBase64ToArray((String)object);
        object = byArray;
        if (byArray != null) {
            return new ByteString((byte[])object);
        }
        return null;
    }

    @NotNull
    public static final ByteString commonDecodeHex(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        if (!(string.length() % 2 == 0)) {
            String string2 = "Unexpected hex string: " + string;
            throw new IllegalArgumentException(string2.toString());
        }
        byte[] byArray = new byte[string.length() / 2];
        int n2 = byArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3 = -ByteString.decodeHexDigit(string.charAt(i2 << 1)) << 4;
            int n4 = -ByteString.decodeHexDigit(string.charAt((i2 << 1) + 1));
            byArray[i2] = (byte)(n3 + n4);
        }
        return new ByteString(byArray);
    }

    public static final void commonWrite(@NotNull ByteString byteString, @NotNull Buffer buffer, int n2, int n3) {
        Intrinsics.checkNotNullParameter(byteString, "");
        Intrinsics.checkNotNullParameter(buffer, "");
        buffer.write(byteString.getData$okio(), n2, n3);
    }

    private static final int decodeHexDigit(char c2) {
        boolean bl = '0' <= c2 ? c2 < ':' : false;
        if (bl) {
            return c2 - 48;
        }
        boolean bl2 = 'a' <= c2 ? c2 < 'g' : false;
        if (bl2) {
            return c2 - 97 + 10;
        }
        boolean bl3 = 'A' <= c2 ? c2 < 'G' : false;
        if (bl3) {
            return c2 - 65 + 10;
        }
        throw new IllegalArgumentException("Unexpected hex digit: " + c2);
    }

    @NotNull
    public static final String commonToString(@NotNull ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "");
        if (byteString.getData$okio().length == 0) {
            return "[size=0]";
        }
        int n2 = -ByteString.codePointIndexToCharIndex(byteString.getData$okio(), 64);
        if (n2 == -1) {
            ByteString byteString2;
            if (byteString.getData$okio().length <= 64) {
                return "[hex=" + byteString.hex() + ']';
            }
            StringBuilder stringBuilder = new StringBuilder("[size=").append(byteString.getData$okio().length).append(" hex=");
            ByteString byteString3 = byteString;
            int n3 = -SegmentedByteString.resolveDefaultParameter(byteString3, 64);
            if (!(n3 <= byteString3.getData$okio().length)) {
                String string = "endIndex > length(" + byteString3.getData$okio().length + ')';
                throw new IllegalArgumentException(string.toString());
            }
            n2 = n3;
            if (!(n2 >= 0)) {
                String string = "endIndex < beginIndex";
                throw new IllegalArgumentException(string.toString());
            }
            if (n3 == byteString3.getData$okio().length) {
                byteString2 = byteString3;
            } else {
                byte[] byArray = byteString3.getData$okio();
                ByteString byteString4 = new ByteString(ArraysKt.copyOfRange(byArray, 0, n3));
                byteString2 = byteString4;
            }
            return stringBuilder.append(byteString2.hex()).append("\u2026]").toString();
        }
        String string = byteString.utf8();
        String string2 = string.substring(0, n2);
        Intrinsics.checkNotNullExpressionValue(string2, "");
        String string3 = StringsKt.replace$default(StringsKt.replace$default(StringsKt.replace$default(string2, "\\", "\\\\", false, 4, null), "\n", "\\n", false, 4, null), "\r", "\\r", false, 4, null);
        if (n2 < string.length()) {
            return "[size=" + byteString.getData$okio().length + " text=" + string3 + "\u2026]";
        }
        return "[text=" + string3 + ']';
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static final int codePointIndexToCharIndex(byte[] byArray, int n2) {
        int n3;
        int n4;
        int n5;
        block71: {
            n5 = 0;
            n4 = 0;
            byte[] byArray2 = byArray;
            int n6 = byArray.length;
            int n7 = 0;
            block0: while (n7 < n6) {
                int n8;
                int n9;
                int n10;
                int n11 = byArray2[n7];
                if (n11 >= 0) {
                    n3 = n4;
                    n4 = n3 + 1;
                    if (n3 == n2) {
                        return n5;
                    }
                    if (n11 != 10 && n11 != 13) {
                        boolean bl;
                        boolean bl2;
                        if (n11 >= 0) {
                            if (n11 < 32) {
                                return -1;
                            }
                            bl2 = false;
                        } else {
                            bl2 = false;
                        }
                        if (bl2) return -1;
                        if (127 <= n11) {
                            if (n11 < 160) {
                                return -1;
                            }
                            bl = false;
                        } else {
                            bl = false;
                        }
                        if (bl) {
                            return -1;
                        }
                        boolean bl3 = false;
                        if (bl3) return -1;
                    }
                    if (n11 == 65533) {
                        return -1;
                    }
                    n5 += n11 < 65536 ? 1 : 2;
                    ++n7;
                    while (true) {
                        if (n7 >= n6 || byArray2[n7] < 0) continue block0;
                        n11 = byArray2[n7++];
                        n3 = n4;
                        n4 = n3 + 1;
                        if (n3 == n2) {
                            return n5;
                        }
                        if (n11 != 10 && n11 != 13) {
                            boolean bl;
                            boolean bl4;
                            if (n11 >= 0) {
                                if (n11 < 32) {
                                    return -1;
                                }
                                bl4 = false;
                            } else {
                                bl4 = false;
                            }
                            if (bl4) return -1;
                            if (127 <= n11) {
                                if (n11 < 160) {
                                    return -1;
                                }
                                bl = false;
                            } else {
                                bl = false;
                            }
                            if (bl) {
                                return -1;
                            }
                            boolean bl5 = false;
                            if (bl5) return -1;
                        }
                        if (n11 == 65533) {
                            return -1;
                        }
                        n5 += n11 < 65536 ? 1 : 2;
                    }
                }
                n3 = n11;
                if (n3 >> 5 == -2) {
                    int n12 = n7;
                    byte[] byArray3 = byArray2;
                    if (n6 <= n7 + 1) {
                        int n13 = n4;
                        if (n13 != n2) return -1;
                        return n5;
                    }
                    n11 = byArray3[n7];
                    n9 = n10 = byArray3[n7 + 1];
                    if (!((n10 & 0xC0) == 128)) {
                        int n14 = n4;
                        if (n14 != n2) return -1;
                        return n5;
                    }
                    n8 = 0xF80 ^ n10 ^ n11 << 6;
                    if (n8 < 128) {
                        int n15 = n4;
                        if (n15 != n2) return -1;
                        return n5;
                    }
                    n7 = n8;
                    n10 = n12;
                    n11 = n7;
                    n3 = n4;
                    n4 = n3 + 1;
                    if (n3 == n2) {
                        return n5;
                    }
                    if (n11 != 10 && n11 != 13) {
                        boolean bl;
                        boolean bl6;
                        if (n11 >= 0) {
                            if (n11 < 32) {
                                return -1;
                            }
                            bl6 = false;
                        } else {
                            bl6 = false;
                        }
                        if (bl6) return -1;
                        if (127 <= n11) {
                            if (n11 < 160) {
                                return -1;
                            }
                            bl = false;
                        } else {
                            bl = false;
                        }
                        if (bl) {
                            return -1;
                        }
                        boolean bl7 = false;
                        if (bl7) return -1;
                    }
                    if (n11 == 65533) {
                        return -1;
                    }
                    n5 += n11 < 65536 ? 1 : 2;
                    n7 = n10 + 2;
                    continue;
                }
                n3 = n11;
                if (n3 >> 4 == -2) {
                    int n16 = n7;
                    byte[] byArray4 = byArray2;
                    if (n6 <= n7 + 2) {
                        int n17 = n4;
                        if (n17 != n2) return -1;
                        return n5;
                    }
                    n11 = byArray4[n7];
                    n9 = n10 = byArray4[n7 + 1];
                    if (!((n10 & 0xC0) == 128)) {
                        int n18 = n4;
                        if (n18 != n2) return -1;
                        return n5;
                    }
                    n9 = n8 = byArray4[n7 + 2];
                    if (!((n8 & 0xC0) == 128)) {
                        int n19 = n4;
                        if (n19 != n2) return -1;
                        return n5;
                    }
                    n9 = 0xFFFE1F80 ^ n8 ^ n10 << 6 ^ n11 << 12;
                    if (n9 < 2048) {
                        int n20 = n4;
                        if (n20 != n2) return -1;
                        return n5;
                    }
                    boolean bl = 55296 <= n9 ? n9 < 57344 : false;
                    if (bl) {
                        int n21 = n4;
                        if (n21 != n2) return -1;
                        return n5;
                    }
                    n7 = n9;
                    n10 = n16;
                    n11 = n7;
                    n3 = n4;
                    n4 = n3 + 1;
                    if (n3 == n2) {
                        return n5;
                    }
                    if (n11 != 10 && n11 != 13) {
                        boolean bl8;
                        boolean bl9;
                        if (n11 >= 0) {
                            if (n11 < 32) {
                                return -1;
                            }
                            bl9 = false;
                        } else {
                            bl9 = false;
                        }
                        if (bl9) return -1;
                        if (127 <= n11) {
                            if (n11 < 160) {
                                return -1;
                            }
                            bl8 = false;
                        } else {
                            bl8 = false;
                        }
                        if (bl8) {
                            return -1;
                        }
                        boolean bl10 = false;
                        if (bl10) return -1;
                    }
                    if (n11 == 65533) {
                        return -1;
                    }
                    n5 += n11 < 65536 ? 1 : 2;
                    n7 = n10 + 3;
                    continue;
                }
                n3 = n11;
                if (n3 >> 3 == -2) {
                    int n22 = n7;
                    byte[] byArray5 = byArray2;
                    if (n6 <= n7 + 3) {
                        int n23 = n4;
                        if (n23 != n2) return -1;
                        return n5;
                    }
                    n11 = byArray5[n7];
                    n9 = n10 = byArray5[n7 + 1];
                    if (!((n10 & 0xC0) == 128)) {
                        int n24 = n4;
                        if (n24 != n2) return -1;
                        return n5;
                    }
                    n9 = n8 = byArray5[n7 + 2];
                    if (!((n8 & 0xC0) == 128)) {
                        int n25 = n4;
                        if (n25 != n2) return -1;
                        return n5;
                    }
                    n7 = n9 = byArray5[n7 + 3];
                    if (!((n9 & 0xC0) == 128)) {
                        int n26 = n4;
                        if (n26 != n2) return -1;
                        return n5;
                    }
                    if ((n9 = 0x381F80 ^ n9 ^ n8 << 6 ^ n10 << 12 ^ n11 << 18) > 0x10FFFF) {
                        int n27 = n4;
                        if (n27 != n2) return -1;
                        return n5;
                    }
                    boolean bl = 55296 <= n9 ? n9 < 57344 : false;
                    if (bl) {
                        int n28 = n4;
                        if (n28 != n2) return -1;
                        return n5;
                    }
                    if (n9 < 65536) {
                        int n29 = n4;
                        if (n29 != n2) return -1;
                        return n5;
                    }
                    n7 = n9;
                    n10 = n22;
                    n11 = n7;
                    n3 = n4;
                    n4 = n3 + 1;
                    if (n3 == n2) {
                        return n5;
                    }
                    if (n11 != 10 && n11 != 13) {
                        boolean bl11;
                        boolean bl12;
                        if (n11 >= 0) {
                            if (n11 < 32) {
                                return -1;
                            }
                            bl12 = false;
                        } else {
                            bl12 = false;
                        }
                        if (bl12) return -1;
                        if (127 <= n11) {
                            if (n11 < 160) {
                                return -1;
                            }
                            bl11 = false;
                        } else {
                            bl11 = false;
                        }
                        if (bl11) {
                            return -1;
                        }
                        boolean bl13 = false;
                        if (bl13) return -1;
                    }
                    if (n11 == 65533) {
                        return -1;
                    }
                    n5 += n11 < 65536 ? 1 : 2;
                    n7 = n10 + 4;
                    continue;
                }
                break block71;
            }
            return n5;
        }
        n3 = n4;
        if (n3 != n2) return -1;
        return n5;
    }

    static {
        char[] cArray = new char[16];
        char[] cArray2 = cArray;
        cArray[0] = 48;
        cArray2[1] = 49;
        cArray2[2] = 50;
        cArray2[3] = 51;
        cArray2[4] = 52;
        cArray2[5] = 53;
        cArray2[6] = 54;
        cArray2[7] = 55;
        cArray2[8] = 56;
        cArray2[9] = 57;
        cArray2[10] = 97;
        cArray2[11] = 98;
        cArray2[12] = 99;
        cArray2[13] = 100;
        cArray2[14] = 101;
        cArray2[15] = 102;
        HEX_DIGIT_CHARS = cArray2;
    }
}

