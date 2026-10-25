/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io.path;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.Unit;
import kotlin.WasExperimental;
import kotlin.internal.InlineOnly;
import kotlin.io.CloseableKt;
import kotlin.io.TextStreamsKt;
import kotlin.io.path.ExperimentalPathApi;
import kotlin.io.path.PathsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000\u0082\u0001\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a%\u0010\u0005\u001a\u00020\u0002*\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a%\u0010\u0005\u001a\u00020\u0002*\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u000b2\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u001e\u0010\f\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\r\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001a:\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b\u00a2\u0006\u0002\u0010\u0015\u001a:\u0010\u0016\u001a\u00020\u0017*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b\u00a2\u0006\u0002\u0010\u0018\u001a=\u0010\u0019\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2!\u0010\u001a\u001a\u001d\u0012\u0013\u0012\u00110\u001c\u00a2\u0006\f\b\u001d\u0012\b\b\u001e\u0012\u0004\b\b(\u001f\u0012\u0004\u0012\u00020\u00010\u001bH\u0087\b\u00f8\u0001\u0000\u001a&\u0010 \u001a\u00020!*\u00020\u00022\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b\u00a2\u0006\u0002\u0010\"\u001a&\u0010#\u001a\u00020$*\u00020\u00022\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b\u00a2\u0006\u0002\u0010%\u001a\r\u0010&\u001a\u00020\u0004*\u00020\u0002H\u0087\b\u001a\u001d\u0010'\u001a\b\u0012\u0004\u0012\u00020\u001c0(*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0016\u0010)\u001a\u00020\u001c*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001a0\u0010*\u001a\u00020+*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b\u00a2\u0006\u0002\u0010,\u001a?\u0010-\u001a\u0002H.\"\u0004\b\u0000\u0010.*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\u0018\u0010/\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001c0\u000b\u0012\u0004\u0012\u0002H.0\u001bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u00100\u001a.\u00101\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b\u00a2\u0006\u0002\u00102\u001a>\u00103\u001a\u00020\u0002*\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b\u00a2\u0006\u0002\u00104\u001a>\u00103\u001a\u00020\u0002*\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u000b2\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b\u00a2\u0006\u0002\u00105\u001a7\u00106\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\r\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0007\u00a2\u0006\u0002\u00107\u001a0\u00108\u001a\u000209*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b\u00a2\u0006\u0002\u0010:\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006;"}, d2={"appendBytes", "", "Ljava/nio/file/Path;", "array", "", "appendLines", "lines", "", "", "charset", "Ljava/nio/charset/Charset;", "Lkotlin/sequences/Sequence;", "appendText", "text", "bufferedReader", "Ljava/io/BufferedReader;", "bufferSize", "", "options", "", "Ljava/nio/file/OpenOption;", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;I[Ljava/nio/file/OpenOption;)Ljava/io/BufferedReader;", "bufferedWriter", "Ljava/io/BufferedWriter;", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;I[Ljava/nio/file/OpenOption;)Ljava/io/BufferedWriter;", "forEachLine", "action", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "line", "inputStream", "Ljava/io/InputStream;", "(Ljava/nio/file/Path;[Ljava/nio/file/OpenOption;)Ljava/io/InputStream;", "outputStream", "Ljava/io/OutputStream;", "(Ljava/nio/file/Path;[Ljava/nio/file/OpenOption;)Ljava/io/OutputStream;", "readBytes", "readLines", "", "readText", "reader", "Ljava/io/InputStreamReader;", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)Ljava/io/InputStreamReader;", "useLines", "T", "block", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "writeBytes", "(Ljava/nio/file/Path;[B[Ljava/nio/file/OpenOption;)V", "writeLines", "(Ljava/nio/file/Path;Ljava/lang/Iterable;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)Ljava/nio/file/Path;", "(Ljava/nio/file/Path;Lkotlin/sequences/Sequence;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)Ljava/nio/file/Path;", "writeText", "(Ljava/nio/file/Path;Ljava/lang/CharSequence;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)V", "writer", "Ljava/io/OutputStreamWriter;", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)Ljava/io/OutputStreamWriter;", "kotlin-stdlib-jdk7"}, xs="kotlin/io/path/PathsKt")
@SourceDebugExtension(value={"SMAP\nPathReadWrite.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PathReadWrite.kt\nkotlin/io/path/PathsKt__PathReadWriteKt\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 ReadWrite.kt\nkotlin/io/TextStreamsKt\n+ 4 _Sequences.kt\nkotlin/sequences/SequencesKt___SequencesKt\n*L\n1#1,326:1\n1#2:327\n1#2:329\n52#3:328\n1295#4,2:330\n*S KotlinDebug\n*F\n+ 1 PathReadWrite.kt\nkotlin/io/path/PathsKt__PathReadWriteKt\n*L\n202#1:329\n202#1:328\n202#1:330,2\n*E\n"})
class PathsKt__PathReadWriteKt {
    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalPathApi.class})
    @InlineOnly
    private static final InputStreamReader reader(Path path, Charset charset, OpenOption ... openOptionArray) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(charset, "");
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        return new InputStreamReader(Files.newInputStream(path, Arrays.copyOf(openOptionArray, openOptionArray.length)), charset);
    }

    static /* synthetic */ InputStreamReader reader$default(Path path, Charset charset, OpenOption[] openOptionArray, int n2, Object object) throws IOException {
        if ((n2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(charset, "");
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        return new InputStreamReader(Files.newInputStream(path, Arrays.copyOf(openOptionArray, openOptionArray.length)), charset);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalPathApi.class})
    @InlineOnly
    private static final BufferedReader bufferedReader(Path path, Charset charset, int n2, OpenOption ... openOptionArray) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(charset, "");
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        return new BufferedReader(new InputStreamReader(Files.newInputStream(path, Arrays.copyOf(openOptionArray, openOptionArray.length)), charset), n2);
    }

    static /* synthetic */ BufferedReader bufferedReader$default(Path path, Charset charset, int n2, OpenOption[] openOptionArray, int n3, Object object) throws IOException {
        if ((n3 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        if ((n3 & 2) != 0) {
            n2 = 8192;
        }
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(charset, "");
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        return new BufferedReader(new InputStreamReader(Files.newInputStream(path, Arrays.copyOf(openOptionArray, openOptionArray.length)), charset), n2);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalPathApi.class})
    @InlineOnly
    private static final OutputStreamWriter writer(Path path, Charset charset, OpenOption ... openOptionArray) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(charset, "");
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        return new OutputStreamWriter(Files.newOutputStream(path, Arrays.copyOf(openOptionArray, openOptionArray.length)), charset);
    }

    static /* synthetic */ OutputStreamWriter writer$default(Path path, Charset charset, OpenOption[] openOptionArray, int n2, Object object) throws IOException {
        if ((n2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(charset, "");
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        return new OutputStreamWriter(Files.newOutputStream(path, Arrays.copyOf(openOptionArray, openOptionArray.length)), charset);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalPathApi.class})
    @InlineOnly
    private static final BufferedWriter bufferedWriter(Path path, Charset charset, int n2, OpenOption ... openOptionArray) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(charset, "");
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        return new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(path, Arrays.copyOf(openOptionArray, openOptionArray.length)), charset), n2);
    }

    static /* synthetic */ BufferedWriter bufferedWriter$default(Path path, Charset charset, int n2, OpenOption[] openOptionArray, int n3, Object object) throws IOException {
        if ((n3 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        if ((n3 & 2) != 0) {
            n2 = 8192;
        }
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(charset, "");
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        return new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(path, Arrays.copyOf(openOptionArray, openOptionArray.length)), charset), n2);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalPathApi.class})
    @InlineOnly
    private static final byte[] readBytes(Path path) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        byte[] byArray = Files.readAllBytes(path);
        Intrinsics.checkNotNullExpressionValue(byArray, "");
        return byArray;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalPathApi.class})
    @InlineOnly
    private static final void writeBytes(Path path, byte[] byArray, OpenOption ... openOptionArray) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        Files.write(path, byArray, Arrays.copyOf(openOptionArray, openOptionArray.length));
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalPathApi.class})
    @InlineOnly
    private static final void appendBytes(Path openOptionArray, byte[] byArray) throws IOException {
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        Intrinsics.checkNotNullParameter(byArray, "");
        OpenOption[] openOptionArray2 = openOptionArray;
        OpenOption[] openOptionArray3 = new OpenOption[1];
        openOptionArray = openOptionArray3;
        openOptionArray3[0] = StandardOpenOption.APPEND;
        Files.write((Path)openOptionArray2, byArray, openOptionArray);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalPathApi.class})
    @NotNull
    public static final String readText(@NotNull Path object, @NotNull Charset object2) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(object2, "");
        Object object3 = new OpenOption[]{};
        object = new InputStreamReader(Files.newInputStream((Path)object, Arrays.copyOf(object3, ((OpenOption[])object3).length)), (Charset)object2);
        object3 = null;
        try {
            object2 = (InputStreamReader)object;
            object2 = TextStreamsKt.readText((Reader)object2);
        }
        catch (Throwable throwable) {
            try {
                object2 = throwable;
                object3 = throwable;
                throw object2;
            }
            catch (Throwable throwable2) {
                CloseableKt.closeFinally((Closeable)object, (Throwable)object3);
                throw throwable2;
            }
        }
        CloseableKt.closeFinally((Closeable)object, null);
        return object2;
    }

    public static /* synthetic */ String readText$default(Path path, Charset charset, int n2, Object object) throws IOException {
        if ((n2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return PathsKt.readText(path, charset);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalPathApi.class})
    public static final void writeText(@NotNull Path object, @NotNull CharSequence charSequence, @NotNull Charset object2, OpenOption ... object3) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(object2, "");
        Intrinsics.checkNotNullParameter(object3, "");
        OutputStream outputStream2 = Files.newOutputStream((Path)object, Arrays.copyOf(object3, ((OpenOption[])object3).length));
        Intrinsics.checkNotNullExpressionValue(outputStream2, "");
        object = outputStream2;
        object = new OutputStreamWriter((OutputStream)object, (Charset)object2);
        object2 = null;
        try {
            object3 = (OutputStreamWriter)object;
            object3.append(charSequence);
        }
        catch (Throwable throwable) {
            try {
                object3 = throwable;
                object2 = throwable;
                throw object3;
            }
            catch (Throwable throwable2) {
                CloseableKt.closeFinally((Closeable)object, (Throwable)object2);
                throw throwable2;
            }
        }
        CloseableKt.closeFinally((Closeable)object, null);
        return;
    }

    public static /* synthetic */ void writeText$default(Path path, CharSequence charSequence, Charset charset, OpenOption[] openOptionArray, int n2, Object object) throws IOException {
        if ((n2 & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        PathsKt.writeText(path, charSequence, charset, openOptionArray);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalPathApi.class})
    public static final void appendText(@NotNull Path object, @NotNull CharSequence charSequence, @NotNull Charset object2) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(object2, "");
        OpenOption[] openOptionArray = new OpenOption[1];
        Object object3 = openOptionArray;
        openOptionArray[0] = StandardOpenOption.APPEND;
        OutputStream outputStream2 = Files.newOutputStream((Path)object, object3);
        Intrinsics.checkNotNullExpressionValue(outputStream2, "");
        object = outputStream2;
        object = new OutputStreamWriter((OutputStream)object, (Charset)object2);
        object3 = null;
        try {
            object2 = (OutputStreamWriter)object;
            ((OutputStreamWriter)object2).append(charSequence);
        }
        catch (Throwable throwable) {
            try {
                object2 = throwable;
                object3 = throwable;
                throw object2;
            }
            catch (Throwable throwable2) {
                CloseableKt.closeFinally((Closeable)object, (Throwable)object3);
                throw throwable2;
            }
        }
        CloseableKt.closeFinally((Closeable)object, null);
        return;
    }

    public static /* synthetic */ void appendText$default(Path path, CharSequence charSequence, Charset charset, int n2, Object object) throws IOException {
        if ((n2 & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        PathsKt.appendText(path, charSequence, charset);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalPathApi.class})
    @InlineOnly
    private static final void forEachLine(Path object, Charset object2, Function1<? super String, Unit> function1) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(object2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        BufferedReader bufferedReader = Files.newBufferedReader((Path)object, (Charset)object2);
        Intrinsics.checkNotNullExpressionValue(bufferedReader, "");
        Reader reader = bufferedReader;
        object = reader;
        object = reader;
        object = (BufferedReader)reader;
        object2 = null;
        try {
            Object object3 = (BufferedReader)object;
            Sequence<String> sequence2 = TextStreamsKt.lineSequence((BufferedReader)object3);
            object3 = sequence2;
            object3 = sequence2;
            object3 = sequence2.iterator();
            while (object3.hasNext()) {
                Object e2 = object3.next();
                function1.invoke((String)e2);
            }
        }
        catch (Throwable throwable) {
            try {
                Throwable throwable2 = throwable;
                object2 = throwable;
                throw throwable2;
            }
            catch (Throwable throwable3) {
                InlineMarker.finallyStart(1);
                CloseableKt.closeFinally((Closeable)object, (Throwable)object2);
                InlineMarker.finallyEnd(1);
                throw throwable3;
            }
        }
        InlineMarker.finallyStart(1);
        CloseableKt.closeFinally((Closeable)object, null);
        InlineMarker.finallyEnd(1);
        return;
    }

    static /* synthetic */ void forEachLine$default(Path object, Charset object2, Function1 function1, int n2, Object object3) throws IOException {
        Object object4;
        if ((n2 & 1) != 0) {
            object2 = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(object2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        BufferedReader bufferedReader = Files.newBufferedReader((Path)object, (Charset)object2);
        Intrinsics.checkNotNullExpressionValue(bufferedReader, "");
        object = object4 = (Reader)bufferedReader;
        object = (BufferedReader)object4;
        object2 = null;
        try {
            object4 = (BufferedReader)object;
            Sequence<String> sequence2 = TextStreamsKt.lineSequence((BufferedReader)object4);
            object4 = sequence2;
            object4 = sequence2;
            object4 = sequence2.iterator();
            while (object4.hasNext()) {
                object3 = object4.next();
                function1.invoke(object3);
            }
        }
        catch (Throwable throwable) {
            try {
                object4 = throwable;
                object2 = throwable;
                throw object4;
            }
            catch (Throwable throwable2) {
                InlineMarker.finallyStart(1);
                CloseableKt.closeFinally((Closeable)object, (Throwable)object2);
                InlineMarker.finallyEnd(1);
                throw throwable2;
            }
        }
        InlineMarker.finallyStart(1);
        CloseableKt.closeFinally((Closeable)object, null);
        InlineMarker.finallyEnd(1);
        return;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalPathApi.class})
    @InlineOnly
    private static final InputStream inputStream(Path path, OpenOption ... openOptionArray) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        InputStream inputStream2 = Files.newInputStream(path, Arrays.copyOf(openOptionArray, openOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(inputStream2, "");
        return inputStream2;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalPathApi.class})
    @InlineOnly
    private static final OutputStream outputStream(Path path, OpenOption ... openOptionArray) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        OutputStream outputStream2 = Files.newOutputStream(path, Arrays.copyOf(openOptionArray, openOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(outputStream2, "");
        return outputStream2;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalPathApi.class})
    @InlineOnly
    private static final List<String> readLines(Path path, Charset charset) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(charset, "");
        List<String> list2 = Files.readAllLines(path, charset);
        Intrinsics.checkNotNullExpressionValue(list2, "");
        return list2;
    }

    static /* synthetic */ List readLines$default(Path path, Charset charset, int n2, Object object) throws IOException {
        if ((n2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(charset, "");
        List<String> list2 = Files.readAllLines(path, charset);
        Intrinsics.checkNotNullExpressionValue(list2, "");
        return list2;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalPathApi.class})
    @InlineOnly
    private static final <T> T useLines(Path object, Charset object2, Function1<? super Sequence<String>, ? extends T> function1) throws IOException {
        BufferedReader bufferedReader;
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(object2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        object = Files.newBufferedReader((Path)object, (Charset)object2);
        object2 = null;
        try {
            bufferedReader = (BufferedReader)object;
            Intrinsics.checkNotNullExpressionValue(bufferedReader, "");
            bufferedReader = function1.invoke(TextStreamsKt.lineSequence(bufferedReader));
        }
        catch (Throwable throwable) {
            try {
                Throwable throwable2 = throwable;
                object2 = throwable;
                throw throwable2;
            }
            catch (Throwable throwable3) {
                InlineMarker.finallyStart(1);
                CloseableKt.closeFinally((Closeable)object, (Throwable)object2);
                InlineMarker.finallyEnd(1);
                throw throwable3;
            }
        }
        InlineMarker.finallyStart(1);
        CloseableKt.closeFinally((Closeable)object, null);
        InlineMarker.finallyEnd(1);
        return (T)bufferedReader;
    }

    static /* synthetic */ Object useLines$default(Path object, Charset charset, Function1 function1, int n2, Object object2) throws IOException {
        if ((n2 & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(charset, "");
        Intrinsics.checkNotNullParameter(function1, "");
        Closeable closeable = Files.newBufferedReader((Path)object, charset);
        object2 = null;
        try {
            object = (BufferedReader)closeable;
            Intrinsics.checkNotNullExpressionValue(object, "");
            object = function1.invoke(TextStreamsKt.lineSequence((BufferedReader)object));
        }
        catch (Throwable throwable) {
            try {
                object = throwable;
                object2 = throwable;
                throw object;
            }
            catch (Throwable throwable2) {
                InlineMarker.finallyStart(1);
                CloseableKt.closeFinally(closeable, (Throwable)object2);
                InlineMarker.finallyEnd(1);
                throw throwable2;
            }
        }
        InlineMarker.finallyStart(1);
        CloseableKt.closeFinally(closeable, null);
        InlineMarker.finallyEnd(1);
        return object;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalPathApi.class})
    @InlineOnly
    private static final Path writeLines(Path path, Iterable<? extends CharSequence> iterable, Charset charset, OpenOption ... openOptionArray) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(iterable, "");
        Intrinsics.checkNotNullParameter(charset, "");
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        Path path2 = Files.write(path, iterable, charset, Arrays.copyOf(openOptionArray, openOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(path2, "");
        return path2;
    }

    static /* synthetic */ Path writeLines$default(Path path, Iterable iterable, Charset charset, OpenOption[] openOptionArray, int n2, Object object) throws IOException {
        if ((n2 & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(iterable, "");
        Intrinsics.checkNotNullParameter(charset, "");
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        Path path2 = Files.write(path, (Iterable<? extends CharSequence>)iterable, charset, Arrays.copyOf(openOptionArray, openOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(path2, "");
        return path2;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalPathApi.class})
    @InlineOnly
    private static final Path writeLines(Path path, Sequence<? extends CharSequence> sequence2, Charset charset, OpenOption ... openOptionArray) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(sequence2, "");
        Intrinsics.checkNotNullParameter(charset, "");
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        Path path2 = Files.write(path, SequencesKt.asIterable(sequence2), charset, Arrays.copyOf(openOptionArray, openOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(path2, "");
        return path2;
    }

    static /* synthetic */ Path writeLines$default(Path path, Sequence sequence2, Charset charset, OpenOption[] openOptionArray, int n2, Object object) throws IOException {
        if ((n2 & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(sequence2, "");
        Intrinsics.checkNotNullParameter(charset, "");
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        Path path2 = Files.write(path, SequencesKt.asIterable(sequence2), charset, Arrays.copyOf(openOptionArray, openOptionArray.length));
        Intrinsics.checkNotNullExpressionValue(path2, "");
        return path2;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalPathApi.class})
    @InlineOnly
    private static final Path appendLines(Path openOptionArray, Iterable<? extends CharSequence> iterable, Charset charset) throws IOException {
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        Intrinsics.checkNotNullParameter(iterable, "");
        Intrinsics.checkNotNullParameter(charset, "");
        OpenOption[] openOptionArray2 = openOptionArray;
        OpenOption[] openOptionArray3 = new OpenOption[1];
        openOptionArray = openOptionArray3;
        openOptionArray3[0] = StandardOpenOption.APPEND;
        Path path = Files.write((Path)openOptionArray2, iterable, charset, openOptionArray);
        Intrinsics.checkNotNullExpressionValue(path, "");
        return path;
    }

    static /* synthetic */ Path appendLines$default(Path path, Iterable iterable, Charset charset, int n2, Object openOptionArray) throws IOException {
        if ((n2 & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(iterable, "");
        Intrinsics.checkNotNullParameter(charset, "");
        OpenOption[] openOptionArray2 = new OpenOption[1];
        openOptionArray = openOptionArray2;
        openOptionArray2[0] = StandardOpenOption.APPEND;
        Path path2 = Files.write(path, (Iterable<? extends CharSequence>)iterable, charset, openOptionArray);
        Intrinsics.checkNotNullExpressionValue(path2, "");
        return path2;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalPathApi.class})
    @InlineOnly
    private static final Path appendLines(Path openOptionArray, Sequence<? extends CharSequence> sequence2, Charset charset) throws IOException {
        Intrinsics.checkNotNullParameter(openOptionArray, "");
        Intrinsics.checkNotNullParameter(sequence2, "");
        Intrinsics.checkNotNullParameter(charset, "");
        OpenOption[] openOptionArray2 = openOptionArray;
        OpenOption[] openOptionArray3 = new OpenOption[1];
        openOptionArray = openOptionArray3;
        openOptionArray3[0] = StandardOpenOption.APPEND;
        Path path = Files.write((Path)openOptionArray2, SequencesKt.asIterable(sequence2), charset, openOptionArray);
        Intrinsics.checkNotNullExpressionValue(path, "");
        return path;
    }

    static /* synthetic */ Path appendLines$default(Path path, Sequence sequence2, Charset charset, int n2, Object openOptionArray) throws IOException {
        if ((n2 & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(sequence2, "");
        Intrinsics.checkNotNullParameter(charset, "");
        OpenOption[] openOptionArray2 = new OpenOption[1];
        openOptionArray = openOptionArray2;
        openOptionArray2[0] = StandardOpenOption.APPEND;
        Path path2 = Files.write(path, SequencesKt.asIterable(sequence2), charset, openOptionArray);
        Intrinsics.checkNotNullExpressionValue(path2, "");
        return path2;
    }
}

