/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.OpenOption;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import kotlin.Metadata;
import kotlin.jvm.JvmName;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.functions.Function1;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.CipherSink;
import okio.CipherSource;
import okio.FileSystem;
import okio.HashingSink;
import okio.HashingSource;
import okio.Okio__JvmOkioKt;
import okio.Okio__OkioKt;
import okio.Path;
import okio.Sink;
import okio.Source;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=4, xi=48, d1={"okio/Okio__JvmOkioKt", "okio/Okio__OkioKt"})
public final class Okio {
    @NotNull
    public static final Sink sink(@NotNull OutputStream outputStream2) {
        return Okio__JvmOkioKt.sink(outputStream2);
    }

    @NotNull
    public static final Source source(@NotNull InputStream inputStream2) {
        return Okio__JvmOkioKt.source(inputStream2);
    }

    @NotNull
    public static final Sink sink(@NotNull Socket socket) throws IOException {
        return Okio__JvmOkioKt.sink(socket);
    }

    @NotNull
    public static final Source source(@NotNull Socket socket) throws IOException {
        return Okio__JvmOkioKt.source(socket);
    }

    @JvmOverloads
    @NotNull
    public static final Sink sink(@NotNull File file, boolean bl) throws FileNotFoundException {
        return Okio__JvmOkioKt.sink(file, bl);
    }

    public static /* synthetic */ Sink sink$default(File file, boolean bl, int n2, Object object) throws FileNotFoundException {
        return Okio__JvmOkioKt.sink$default(file, bl, n2, object);
    }

    @NotNull
    public static final Sink appendingSink(@NotNull File file) throws FileNotFoundException {
        return Okio__JvmOkioKt.appendingSink(file);
    }

    @NotNull
    public static final Source source(@NotNull File file) throws FileNotFoundException {
        return Okio__JvmOkioKt.source(file);
    }

    @NotNull
    public static final Sink sink(@NotNull java.nio.file.Path path, OpenOption ... openOptionArray) throws IOException {
        return Okio__JvmOkioKt.sink(path, openOptionArray);
    }

    @NotNull
    public static final Source source(@NotNull java.nio.file.Path path, OpenOption ... openOptionArray) throws IOException {
        return Okio__JvmOkioKt.source(path, openOptionArray);
    }

    @NotNull
    public static final CipherSink cipherSink(@NotNull Sink sink2, @NotNull Cipher cipher) {
        return Okio__JvmOkioKt.cipherSink(sink2, cipher);
    }

    @NotNull
    public static final CipherSource cipherSource(@NotNull Source source2, @NotNull Cipher cipher) {
        return Okio__JvmOkioKt.cipherSource(source2, cipher);
    }

    @NotNull
    public static final HashingSink hashingSink(@NotNull Sink sink2, @NotNull Mac mac) {
        return Okio__JvmOkioKt.hashingSink(sink2, mac);
    }

    @NotNull
    public static final HashingSource hashingSource(@NotNull Source source2, @NotNull Mac mac) {
        return Okio__JvmOkioKt.hashingSource(source2, mac);
    }

    @NotNull
    public static final HashingSink hashingSink(@NotNull Sink sink2, @NotNull MessageDigest messageDigest) {
        return Okio__JvmOkioKt.hashingSink(sink2, messageDigest);
    }

    @NotNull
    public static final HashingSource hashingSource(@NotNull Source source2, @NotNull MessageDigest messageDigest) {
        return Okio__JvmOkioKt.hashingSource(source2, messageDigest);
    }

    @NotNull
    public static final FileSystem openZip(@NotNull FileSystem fileSystem, @NotNull Path path) throws IOException {
        return Okio__JvmOkioKt.openZip(fileSystem, path);
    }

    @NotNull
    public static final FileSystem asResourceFileSystem(@NotNull ClassLoader classLoader) {
        return Okio__JvmOkioKt.asResourceFileSystem(classLoader);
    }

    public static final boolean isAndroidGetsocknameError(@NotNull AssertionError assertionError) {
        return Okio__JvmOkioKt.isAndroidGetsocknameError(assertionError);
    }

    @JvmOverloads
    @NotNull
    public static final Sink sink(@NotNull File file) throws FileNotFoundException {
        return Okio__JvmOkioKt.sink(file);
    }

    @NotNull
    public static final BufferedSource buffer(@NotNull Source source2) {
        return Okio__OkioKt.buffer(source2);
    }

    @NotNull
    public static final BufferedSink buffer(@NotNull Sink sink2) {
        return Okio__OkioKt.buffer(sink2);
    }

    @JvmName(name="blackhole")
    @NotNull
    public static final Sink blackhole() {
        return Okio__OkioKt.blackhole();
    }

    public static final <T extends Closeable, R> R use(T t2, @NotNull Function1<? super T, ? extends R> function1) {
        return Okio__OkioKt.use(t2, function1);
    }
}

