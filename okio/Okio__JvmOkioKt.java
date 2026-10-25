/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import kotlin.Metadata;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okio.CipherSink;
import okio.CipherSource;
import okio.FileSystem;
import okio.HashingSink;
import okio.HashingSource;
import okio.InputStreamSource;
import okio.Okio;
import okio.OutputStreamSink;
import okio.Path;
import okio.Sink;
import okio.SocketAsyncTimeout;
import okio.Source;
import okio.Timeout;
import okio.internal.ResourceFileSystem;
import okio.internal.ZipFilesKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=5, xi=48, d1={"\u0000\u0088\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\n\u0010\b\u001a\u00020\t*\u00020\n\u001a\n\u0010\u000b\u001a\u00020\f*\u00020\r\u001a\u0012\u0010\u000e\u001a\u00020\u000f*\u00020\t2\u0006\u0010\u0010\u001a\u00020\u0011\u001a\u0012\u0010\u0012\u001a\u00020\u0013*\u00020\u00142\u0006\u0010\u0010\u001a\u00020\u0011\u001a\u0012\u0010\u0015\u001a\u00020\u0016*\u00020\t2\u0006\u0010\u0017\u001a\u00020\u0018\u001a\u0012\u0010\u0015\u001a\u00020\u0016*\u00020\t2\u0006\u0010\u0019\u001a\u00020\u001a\u001a\u0012\u0010\u001b\u001a\u00020\u001c*\u00020\u00142\u0006\u0010\u0017\u001a\u00020\u0018\u001a\u0012\u0010\u001b\u001a\u00020\u001c*\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u001a\u001a\u0012\u0010\u001d\u001a\u00020\f*\u00020\f2\u0006\u0010\u001e\u001a\u00020\u001f\u001a\u0016\u0010 \u001a\u00020\t*\u00020\n2\b\b\u0002\u0010!\u001a\u00020\u0004H\u0007\u001a\n\u0010 \u001a\u00020\t*\u00020\"\u001a\n\u0010 \u001a\u00020\t*\u00020#\u001a#\u0010 \u001a\u00020\t*\u00020$2\u0012\u0010%\u001a\n\u0012\u0006\b\u0001\u0012\u00020'0&\"\u00020'\u00a2\u0006\u0002\u0010(\u001a\n\u0010)\u001a\u00020\u0014*\u00020\n\u001a\n\u0010)\u001a\u00020\u0014*\u00020*\u001a\n\u0010)\u001a\u00020\u0014*\u00020#\u001a#\u0010)\u001a\u00020\u0014*\u00020$2\u0012\u0010%\u001a\n\u0012\u0006\b\u0001\u0012\u00020'0&\"\u00020'\u00a2\u0006\u0002\u0010+\"\u0016\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u001c\u0010\u0003\u001a\u00020\u0004*\u00060\u0005j\u0002`\u00068@X\u0080\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0003\u0010\u0007\u00a8\u0006,"}, d2={"logger", "Ljava/util/logging/Logger;", "kotlin.jvm.PlatformType", "isAndroidGetsocknameError", "", "Ljava/lang/AssertionError;", "Lkotlin/AssertionError;", "(Ljava/lang/AssertionError;)Z", "appendingSink", "Lokio/Sink;", "Ljava/io/File;", "asResourceFileSystem", "Lokio/FileSystem;", "Ljava/lang/ClassLoader;", "cipherSink", "Lokio/CipherSink;", "cipher", "Ljavax/crypto/Cipher;", "cipherSource", "Lokio/CipherSource;", "Lokio/Source;", "hashingSink", "Lokio/HashingSink;", "digest", "Ljava/security/MessageDigest;", "mac", "Ljavax/crypto/Mac;", "hashingSource", "Lokio/HashingSource;", "openZip", "zipPath", "Lokio/Path;", "sink", "append", "Ljava/io/OutputStream;", "Ljava/net/Socket;", "Ljava/nio/file/Path;", "options", "", "Ljava/nio/file/OpenOption;", "(Ljava/nio/file/Path;[Ljava/nio/file/OpenOption;)Lokio/Sink;", "source", "Ljava/io/InputStream;", "(Ljava/nio/file/Path;[Ljava/nio/file/OpenOption;)Lokio/Source;", "okio"}, xs="okio/Okio")
final class Okio__JvmOkioKt {
    private static final Logger logger = Logger.getLogger("okio.Okio");

    @NotNull
    public static final Sink sink(@NotNull OutputStream outputStream2) {
        Intrinsics.checkNotNullParameter(outputStream2, "");
        return new OutputStreamSink(outputStream2, new Timeout());
    }

    @NotNull
    public static final Source source(@NotNull InputStream inputStream2) {
        Intrinsics.checkNotNullParameter(inputStream2, "");
        return new InputStreamSource(inputStream2, new Timeout());
    }

    @NotNull
    public static final Sink sink(@NotNull Socket closeable) throws IOException {
        Intrinsics.checkNotNullParameter(closeable, "");
        SocketAsyncTimeout socketAsyncTimeout = new SocketAsyncTimeout((Socket)closeable);
        OutputStream outputStream2 = closeable.getOutputStream();
        Intrinsics.checkNotNullExpressionValue(outputStream2, "");
        closeable = new OutputStreamSink(outputStream2, socketAsyncTimeout);
        return socketAsyncTimeout.sink((Sink)closeable);
    }

    @NotNull
    public static final Source source(@NotNull Socket closeable) throws IOException {
        Intrinsics.checkNotNullParameter(closeable, "");
        SocketAsyncTimeout socketAsyncTimeout = new SocketAsyncTimeout((Socket)closeable);
        InputStream inputStream2 = closeable.getInputStream();
        Intrinsics.checkNotNullExpressionValue(inputStream2, "");
        closeable = new InputStreamSource(inputStream2, socketAsyncTimeout);
        return socketAsyncTimeout.source((Source)closeable);
    }

    @JvmOverloads
    @NotNull
    public static final Sink sink(@NotNull File file, boolean bl) throws FileNotFoundException {
        Intrinsics.checkNotNullParameter(file, "");
        return Okio.sink(new FileOutputStream(file, bl));
    }

    public static /* synthetic */ Sink sink$default(File file, boolean bl, int n2, Object object) throws FileNotFoundException {
        if ((n2 & 1) != 0) {
            bl = false;
        }
        return Okio.sink(file, bl);
    }

    @NotNull
    public static final Sink appendingSink(@NotNull File file) throws FileNotFoundException {
        Intrinsics.checkNotNullParameter(file, "");
        return Okio.sink(new FileOutputStream(file, true));
    }

    @NotNull
    public static final Source source(@NotNull File file) throws FileNotFoundException {
        Intrinsics.checkNotNullParameter(file, "");
        return new InputStreamSource(new FileInputStream(file), Timeout.NONE);
    }

    @NotNull
    public static final Sink sink(@NotNull java.nio.file.Path path, OpenOption ... openOptionArray) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        OutputStream outputStream2 = Files.newOutputStream(path, Arrays.copyOf(openOptionArray, openOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(outputStream2, "");
        return Okio.sink(outputStream2);
    }

    @NotNull
    public static final Source source(@NotNull java.nio.file.Path path, OpenOption ... openOptionArray) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        InputStream inputStream2 = Files.newInputStream(path, Arrays.copyOf(openOptionArray, openOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(inputStream2, "");
        return Okio.source(inputStream2);
    }

    @NotNull
    public static final CipherSink cipherSink(@NotNull Sink sink2, @NotNull Cipher cipher) {
        Intrinsics.checkNotNullParameter(sink2, "");
        Intrinsics.checkNotNullParameter(cipher, "");
        return new CipherSink(Okio.buffer(sink2), cipher);
    }

    @NotNull
    public static final CipherSource cipherSource(@NotNull Source source2, @NotNull Cipher cipher) {
        Intrinsics.checkNotNullParameter(source2, "");
        Intrinsics.checkNotNullParameter(cipher, "");
        return new CipherSource(Okio.buffer(source2), cipher);
    }

    @NotNull
    public static final HashingSink hashingSink(@NotNull Sink sink2, @NotNull Mac mac) {
        Intrinsics.checkNotNullParameter(sink2, "");
        Intrinsics.checkNotNullParameter(mac, "");
        return new HashingSink(sink2, mac);
    }

    @NotNull
    public static final HashingSource hashingSource(@NotNull Source source2, @NotNull Mac mac) {
        Intrinsics.checkNotNullParameter(source2, "");
        Intrinsics.checkNotNullParameter(mac, "");
        return new HashingSource(source2, mac);
    }

    @NotNull
    public static final HashingSink hashingSink(@NotNull Sink sink2, @NotNull MessageDigest messageDigest) {
        Intrinsics.checkNotNullParameter(sink2, "");
        Intrinsics.checkNotNullParameter(messageDigest, "");
        return new HashingSink(sink2, messageDigest);
    }

    @NotNull
    public static final HashingSource hashingSource(@NotNull Source source2, @NotNull MessageDigest messageDigest) {
        Intrinsics.checkNotNullParameter(source2, "");
        Intrinsics.checkNotNullParameter(messageDigest, "");
        return new HashingSource(source2, messageDigest);
    }

    @NotNull
    public static final FileSystem openZip(@NotNull FileSystem fileSystem, @NotNull Path path) throws IOException {
        Intrinsics.checkNotNullParameter(fileSystem, "");
        Intrinsics.checkNotNullParameter(path, "");
        return ZipFilesKt.openZip$default(path, fileSystem, null, 4, null);
    }

    @NotNull
    public static final FileSystem asResourceFileSystem(@NotNull ClassLoader classLoader) {
        Intrinsics.checkNotNullParameter(classLoader, "");
        return new ResourceFileSystem(classLoader, true, null, 4, null);
    }

    public static final boolean isAndroidGetsocknameError(@NotNull AssertionError assertionError) {
        Intrinsics.checkNotNullParameter(assertionError, "");
        if (((Throwable)((Object)assertionError)).getCause() != null) {
            String string = ((Throwable)((Object)assertionError)).getMessage();
            if (string != null ? StringsKt.contains$default((CharSequence)string, "getsockname failed", false, 2, null) : false) {
                return true;
            }
        }
        return false;
    }

    @JvmOverloads
    @NotNull
    public static final Sink sink(@NotNull File file) throws FileNotFoundException {
        Intrinsics.checkNotNullParameter(file, "");
        return Okio.sink$default(file, false, 1, null);
    }

    public static final /* synthetic */ Logger access$getLogger$p() {
        return logger;
    }
}

