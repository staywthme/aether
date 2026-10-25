/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.publicsuffix;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.IDN;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;
import okhttp3.internal.Util;
import okhttp3.internal.platform.Platform;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001c\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0002J\u0010\u0010\u000e\u001a\u0004\u0018\u00010\f2\u0006\u0010\u000f\u001a\u00020\fJ\b\u0010\u0010\u001a\u00020\u0011H\u0002J\b\u0010\u0012\u001a\u00020\u0011H\u0002J\u0016\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0006J\u0016\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\u000f\u001a\u00020\fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2={"Lokhttp3/internal/publicsuffix/PublicSuffixDatabase;", "", "()V", "listRead", "Ljava/util/concurrent/atomic/AtomicBoolean;", "publicSuffixExceptionListBytes", "", "publicSuffixListBytes", "readCompleteLatch", "Ljava/util/concurrent/CountDownLatch;", "findMatchingRule", "", "", "domainLabels", "getEffectiveTldPlusOne", "domain", "readTheList", "", "readTheListUninterruptibly", "setListBytes", "splitDomain", "Companion", "okhttp"})
public final class PublicSuffixDatabase {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final AtomicBoolean listRead = new AtomicBoolean(false);
    @NotNull
    private final CountDownLatch readCompleteLatch = new CountDownLatch(1);
    private byte[] publicSuffixListBytes;
    private byte[] publicSuffixExceptionListBytes;
    @NotNull
    public static final String PUBLIC_SUFFIX_RESOURCE = "publicsuffixes.gz";
    @NotNull
    private static final byte[] WILDCARD_LABEL;
    @NotNull
    private static final List<String> PREVAILING_RULE;
    private static final char EXCEPTION_MARKER = '!';
    @NotNull
    private static final PublicSuffixDatabase instance;

    @Nullable
    public final String getEffectiveTldPlusOne(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        Object object = IDN.toUnicode(string);
        Intrinsics.checkNotNullExpressionValue(object, "");
        object = this.splitDomain((String)object);
        List<String> list2 = this.findMatchingRule((List<String>)object);
        if (object.size() == list2.size() && list2.get(0).charAt(0) != '!') {
            return null;
        }
        int n2 = list2.get(0).charAt(0) == '!' ? object.size() - list2.size() : object.size() - (list2.size() + 1);
        return SequencesKt.joinToString$default(SequencesKt.drop(CollectionsKt.asSequence((Iterable)this.splitDomain(string)), n2), ".", null, null, 0, null, null, 62, null);
    }

    private final List<String> splitDomain(String object) {
        CharSequence charSequence = (CharSequence)object;
        char[] cArray = new char[1];
        object = cArray;
        cArray[0] = 46;
        if (Intrinsics.areEqual(CollectionsKt.last(object = StringsKt.split$default(charSequence, (char[])object, false, 0, 6, null)), "")) {
            return CollectionsKt.dropLast(object, 1);
        }
        return object;
    }

    private final List<String> findMatchingRule(List<String> object) {
        List list2;
        Object object2;
        block28: {
            List list3;
            block27: {
                Object object3;
                block26: {
                    Object object4;
                    block25: {
                        String string;
                        if (!this.listRead.get() && this.listRead.compareAndSet(false, true)) {
                            this.readTheListUninterruptibly();
                        } else {
                            try {
                                this.readCompleteLatch.await();
                            }
                            catch (InterruptedException interruptedException) {
                                Thread.currentThread().interrupt();
                            }
                        }
                        if (!(this.publicSuffixListBytes != null)) {
                            String string2 = "Unable to load publicsuffixes.gz resource from the classpath.";
                            throw new IllegalStateException(string2.toString());
                        }
                        int n2 = 0;
                        int n3 = object.size();
                        byte[][] byArrayArray = new byte[n3][];
                        while (n2 < n3) {
                            int n4 = n2++;
                            String string3 = (String)object.get(n4);
                            Charset charset = StandardCharsets.UTF_8;
                            Intrinsics.checkNotNullExpressionValue(charset, "");
                            Intrinsics.checkNotNullExpressionValue(string3.getBytes(charset), "");
                        }
                        object = byArrayArray;
                        String string4 = null;
                        int n5 = ((Object[])object).length;
                        for (n3 = 0; n3 < n5; ++n3) {
                            String string5;
                            byte[] byArray = this.publicSuffixListBytes;
                            if (this.publicSuffixListBytes == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("");
                                byArray = null;
                            }
                            if ((string5 = PublicSuffixDatabase.Companion.binarySearch(byArray, (byte[][])object, n3)) == null) continue;
                            string4 = string5;
                            break;
                        }
                        object3 = null;
                        if (((Object[])object).length > 1) {
                            byte[][] byArray = (byte[][])((Object[])object).clone();
                            int n6 = ((Object[])byArray).length - 1;
                            for (int i2 = 0; i2 < n6; ++i2) {
                                byArray[i2] = WILDCARD_LABEL;
                                byte[] byArray2 = this.publicSuffixListBytes;
                                if (this.publicSuffixListBytes == null) {
                                    Intrinsics.throwUninitializedPropertyAccessException("");
                                    byArray2 = null;
                                }
                                if ((string = PublicSuffixDatabase.Companion.binarySearch(byArray2, byArray, i2)) == null) continue;
                                object3 = string;
                                break;
                            }
                        }
                        String string6 = null;
                        if (object3 != null) {
                            int n7 = ((Object[])object).length - 1;
                            for (int i3 = 0; i3 < n7; ++i3) {
                                byte[] byArray = this.publicSuffixExceptionListBytes;
                                if (this.publicSuffixExceptionListBytes == null) {
                                    Intrinsics.throwUninitializedPropertyAccessException("");
                                    byArray = null;
                                }
                                if ((string = PublicSuffixDatabase.Companion.binarySearch(byArray, (byte[][])object, i3)) == null) continue;
                                string6 = string;
                                break;
                            }
                        }
                        if (string6 != null) {
                            string6 = "!" + string6;
                            char[] cArray = new char[1];
                            char[] cArray2 = cArray;
                            cArray[0] = 46;
                            return StringsKt.split$default((CharSequence)string6, cArray2, false, 0, 6, null);
                        }
                        if (string4 == null && object3 == null) {
                            return PREVAILING_RULE;
                        }
                        object4 = string4;
                        if (object4 == null) break block25;
                        char[] cArray = new char[1];
                        object = cArray;
                        cArray[0] = 46;
                        if ((object4 = StringsKt.split$default((CharSequence)object4, (char[])object, false, 0, 6, null)) != null) break block26;
                    }
                    object4 = object2 = CollectionsKt.emptyList();
                }
                if ((list3 = object3) == null) break block27;
                char[] cArray = new char[1];
                object = cArray;
                cArray[0] = 46;
                if ((list3 = StringsKt.split$default((CharSequence)((Object)list3), (char[])object, false, 0, 6, null)) != null) break block28;
            }
            list3 = list2 = CollectionsKt.emptyList();
        }
        if (object2.size() > list2.size()) {
            return object2;
        }
        return list2;
    }

    private final void readTheListUninterruptibly() {
        boolean bl = false;
        while (true) {
            try {
                this.readTheList();
                return;
            }
            catch (InterruptedIOException interruptedIOException) {
                Thread.interrupted();
                bl = true;
                continue;
            }
            catch (IOException iOException) {
                Platform.Companion.get().log("Failed to read public suffix list", 5, iOException);
                return;
            }
            break;
        }
        finally {
            if (bl) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private final void readTheList() throws IOException {
        try {
            InputStream inputStream2;
            Ref.ObjectRef objectRef = new Ref.ObjectRef();
            Ref.ObjectRef objectRef2 = new Ref.ObjectRef();
            if (PublicSuffixDatabase.class.getResourceAsStream(PUBLIC_SUFFIX_RESOURCE) == null) {
                return;
            }
            Object object = inputStream2;
            object = Okio.buffer(new GzipSource(Okio.source((InputStream)object)));
            Throwable throwable = null;
            try {
                BufferedSource bufferedSource = (BufferedSource)object;
                int n2 = bufferedSource.readInt();
                objectRef.element = bufferedSource.readByteArray(n2);
                n2 = bufferedSource.readInt();
                objectRef2.element = bufferedSource.readByteArray(n2);
            }
            catch (Throwable throwable2) {
                try {
                    Throwable throwable3 = throwable2;
                    throwable = throwable2;
                    throw throwable3;
                }
                catch (Throwable throwable4) {
                    CloseableKt.closeFinally((Closeable)object, throwable);
                    throw throwable4;
                }
            }
            CloseableKt.closeFinally((Closeable)object, null);
            object = this;
            synchronized (object) {
                Object t2 = objectRef.element;
                Intrinsics.checkNotNull(t2);
                this.publicSuffixListBytes = (byte[])t2;
                Object t3 = objectRef2.element;
                Intrinsics.checkNotNull(t3);
                this.publicSuffixExceptionListBytes = (byte[])t3;
            }
            return;
        }
        finally {
            this.readCompleteLatch.countDown();
        }
    }

    public final void setListBytes(@NotNull byte[] byArray, @NotNull byte[] byArray2) {
        Intrinsics.checkNotNullParameter(byArray, "");
        Intrinsics.checkNotNullParameter(byArray2, "");
        this.publicSuffixListBytes = byArray;
        this.publicSuffixExceptionListBytes = byArray2;
        this.listRead.set(true);
        this.readCompleteLatch.countDown();
    }

    static {
        byte[] byArray = new byte[1];
        byte[] byArray2 = byArray;
        byArray[0] = 42;
        WILDCARD_LABEL = byArray2;
        PREVAILING_RULE = CollectionsKt.listOf("*");
        instance = new PublicSuffixDatabase();
    }

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\f\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\r\u001a\u00020\fJ)\u0010\u000e\u001a\u0004\u0018\u00010\u0007*\u00020\n2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\n0\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002\u00a2\u0006\u0002\u0010\u0013R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2={"Lokhttp3/internal/publicsuffix/PublicSuffixDatabase$Companion;", "", "()V", "EXCEPTION_MARKER", "", "PREVAILING_RULE", "", "", "PUBLIC_SUFFIX_RESOURCE", "WILDCARD_LABEL", "", "instance", "Lokhttp3/internal/publicsuffix/PublicSuffixDatabase;", "get", "binarySearch", "labels", "", "labelIndex", "", "([B[[BI)Ljava/lang/String;", "okhttp"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final PublicSuffixDatabase get() {
            return instance;
        }

        private final String binarySearch(byte[] byArray, byte[][] byArray2, int n2) {
            int n3 = 0;
            int n4 = byArray.length;
            String string = null;
            while (n3 < n4) {
                int n5;
                int n6;
                int n7;
                for (n7 = (n3 + n4) / 2; n7 >= 0 && byArray[n7] != 10; --n7) {
                }
                ++n7;
                int n8 = 1;
                while (byArray[n7 + n8] != 10) {
                    ++n8;
                }
                int n9 = n7 + n8 - n7;
                int n10 = n2;
                int n11 = 0;
                int n12 = 0;
                boolean bl = false;
                while (true) {
                    if (bl) {
                        n6 = 46;
                        bl = false;
                    } else {
                        n6 = Util.and(byArray2[n10][n11], 255);
                    }
                    n5 = Util.and(byArray[n7 + n12], 255);
                    if ((n6 -= n5) != 0) break;
                    ++n11;
                    if (++n12 == n9) break;
                    if (byArray2[n10].length != n11) continue;
                    if (n10 == ((Object[])byArray2).length - 1) break;
                    ++n10;
                    n11 = -1;
                    bl = true;
                }
                if (n6 < 0) {
                    n4 = n7 - 1;
                    continue;
                }
                if (n6 > 0) {
                    n3 = n7 + n8 + 1;
                    continue;
                }
                n6 = n9 - n12;
                n5 = byArray2[n10].length - n11;
                ++n10;
                n11 = ((Object[])byArray2).length;
                while (n10 < n11) {
                    n5 += byArray2[n10].length;
                    ++n10;
                }
                if (n5 < n6) {
                    n4 = n7 - 1;
                    continue;
                }
                if (n5 > n6) {
                    n3 = n7 + n8 + 1;
                    continue;
                }
                Charset charset = StandardCharsets.UTF_8;
                Intrinsics.checkNotNullExpressionValue(charset, "");
                Charset charset2 = charset;
                string = new String(byArray, n7, n9, charset2);
                break;
            }
            return string;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

