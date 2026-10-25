/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal;

import java.net.IDN;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.internal.Util;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000&\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a0\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0005H\u0002\u001a\"\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0002\u001a\u0010\u0010\f\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0002\u001a\f\u0010\r\u001a\u00020\u0001*\u00020\u0003H\u0002\u001a\f\u0010\u000e\u001a\u0004\u0018\u00010\u0003*\u00020\u0003\u00a8\u0006\u000f"}, d2={"decodeIpv4Suffix", "", "input", "", "pos", "", "limit", "address", "", "addressOffset", "decodeIpv6", "Ljava/net/InetAddress;", "inet6AddressToAscii", "containsInvalidHostnameAsciiCodes", "toCanonicalHost", "okhttp"})
public final class HostnamesKt {
    @Nullable
    public static final String toCanonicalHost(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        if (StringsKt.contains$default((CharSequence)string, ":", false, 2, null)) {
            InetAddress inetAddress = StringsKt.startsWith$default(string, "[", false, 2, null) && StringsKt.endsWith$default(string, "]", false, 2, null) ? HostnamesKt.decodeIpv6(string, 1, string.length() - 1) : HostnamesKt.decodeIpv6(string, 0, string.length());
            if (inetAddress == null) {
                return null;
            }
            InetAddress inetAddress2 = inetAddress;
            byte[] byArray = inetAddress.getAddress();
            if (byArray.length == 16) {
                Intrinsics.checkNotNullExpressionValue(byArray, "");
                return HostnamesKt.inet6AddressToAscii(byArray);
            }
            if (byArray.length == 4) {
                return inetAddress2.getHostAddress();
            }
            throw new AssertionError((Object)("Invalid IPv6 address: '" + string + '\''));
        }
        try {
            String string2 = IDN.toASCII(string);
            Intrinsics.checkNotNullExpressionValue(string2, "");
            String string3 = string2;
            Locale locale = Locale.US;
            Intrinsics.checkNotNullExpressionValue(locale, "");
            String string4 = string3.toLowerCase(locale);
            Intrinsics.checkNotNullExpressionValue(string4, "");
            String string5 = string4;
            if (((CharSequence)string4).length() == 0) {
                return null;
            }
            if (HostnamesKt.containsInvalidHostnameAsciiCodes(string5)) {
                return null;
            }
            return string5;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }
    }

    private static final boolean containsInvalidHostnameAsciiCodes(String string) {
        int n2 = string.length();
        for (int i2 = 0; i2 < n2; ++i2) {
            char c2 = string.charAt(i2);
            if (Intrinsics.compare(c2, 31) <= 0 || Intrinsics.compare(c2, 127) >= 0) {
                return true;
            }
            if (StringsKt.indexOf$default((CharSequence)" #%/:?@[\\]", c2, 0, false, 6, null) == -1) continue;
            return true;
        }
        return false;
    }

    private static final InetAddress decodeIpv6(String string, int n2, int n3) {
        byte[] byArray = new byte[16];
        int n4 = 0;
        int n5 = -1;
        int n6 = -1;
        while (n2 < n3) {
            int n7;
            if (n4 == byArray.length) {
                return null;
            }
            if (n2 + 2 <= n3 && StringsKt.startsWith$default(string, "::", n2, false, 4, null)) {
                if (n5 != -1) {
                    return null;
                }
                n5 = n4 += 2;
                if ((n2 += 2) == n3) {
                    break;
                }
            } else if (n4 != 0 && !StringsKt.startsWith$default(string, ":", n2, false, 4, null)) {
                if (StringsKt.startsWith$default(string, ".", n2, false, 4, null)) {
                    if (!HostnamesKt.decodeIpv4Suffix(string, n6, n3, byArray, n4 - 2)) {
                        return null;
                    }
                    n4 += 2;
                    break;
                }
                return null;
            }
            int n8 = 0;
            n6 = ++n2;
            while (n2 < n3 && (n7 = Util.parseHexDigit(string.charAt(n2))) != -1) {
                n8 = (n8 << 4) + n7;
                ++n2;
            }
            n7 = n2 - n6;
            if (n7 == 0 || n7 > 4) {
                return null;
            }
            byArray[n4++] = (byte)(n8 >>> 8);
            byArray[n4++] = (byte)n8;
        }
        if (n4 != byArray.length) {
            if (n5 == -1) {
                return null;
            }
            System.arraycopy(byArray, n5, byArray, byArray.length - (n4 - n5), n4 - n5);
            int n9 = n5;
            Arrays.fill(byArray, n9, n9 + (byArray.length - n4), (byte)0);
        }
        return InetAddress.getByAddress(byArray);
    }

    private static final boolean decodeIpv4Suffix(String string, int n2, int n3, byte[] byArray, int n4) {
        int n5 = n4;
        while (n2 < n3) {
            int n6;
            if (n5 == byArray.length) {
                return false;
            }
            if (n5 != n4 && string.charAt(n2) != '.') {
                return false;
            }
            int n7 = 0;
            int n8 = ++n2;
            while (n2 < n3 && Intrinsics.compare(n6 = string.charAt(n2), 48) >= 0 && Intrinsics.compare(n6, 57) <= 0) {
                if (n7 == 0 && n8 != n2) {
                    return false;
                }
                if ((n7 = n7 * 10 + n6 - 48) > 255) {
                    return false;
                }
                ++n2;
            }
            n6 = n2 - n8;
            if (n6 == 0) {
                return false;
            }
            byArray[n5++] = (byte)n7;
        }
        return n5 == n4 + 4;
    }

    private static final String inet6AddressToAscii(byte[] byArray) {
        int n2;
        int n3;
        int n4 = -1;
        int n5 = 0;
        for (n3 = 0; n3 < byArray.length; n3 += 2) {
            n2 = n3;
            while (n3 < 16 && byArray[n3] == 0 && byArray[n3 + 1] == 0) {
                n3 += 2;
            }
            int n6 = n3 - n2;
            if (n6 <= n5 || n6 < 4) continue;
            n4 = n2;
            n5 = n6;
        }
        Buffer buffer = new Buffer();
        n3 = 0;
        while (n3 < byArray.length) {
            if (n3 == n4) {
                buffer.writeByte(58);
                if ((n3 += n5) != 16) continue;
                buffer.writeByte(58);
                continue;
            }
            if (n3 > 0) {
                buffer.writeByte(58);
            }
            n2 = Util.and(byArray[n3], 255) << 8 | Util.and(byArray[n3 + 1], 255);
            buffer.writeHexadecimalUnsignedLong(n2);
            n3 += 2;
        }
        return buffer.readUtf8();
    }
}

