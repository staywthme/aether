/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import okio.Okio;
import okio.Sink;
import okio.Source;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u0000 \u00142\u00020\u0001:\u0001\u0014J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u0005H&J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0018\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u0005H&J\u0010\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0015"}, d2={"Lokhttp3/internal/io/FileSystem;", "", "appendingSink", "Lokio/Sink;", "file", "Ljava/io/File;", "delete", "", "deleteContents", "directory", "exists", "", "rename", "from", "to", "sink", "size", "", "source", "Lokio/Source;", "Companion", "okhttp"})
public interface FileSystem {
    @NotNull
    public static final Companion Companion = okhttp3.internal.io.FileSystem$Companion.$$INSTANCE;
    @JvmField
    @NotNull
    public static final FileSystem SYSTEM = new Companion.SystemFileSystem();

    @NotNull
    public Source source(@NotNull File var1) throws FileNotFoundException;

    @NotNull
    public Sink sink(@NotNull File var1) throws FileNotFoundException;

    @NotNull
    public Sink appendingSink(@NotNull File var1) throws FileNotFoundException;

    public void delete(@NotNull File var1) throws IOException;

    public boolean exists(@NotNull File var1);

    public long size(@NotNull File var1);

    public void rename(@NotNull File var1, @NotNull File var2) throws IOException;

    public void deleteContents(@NotNull File var1) throws IOException;

    @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001:\u0001\u0005B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0013\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0001\u00a8\u0006\u0006"}, d2={"Lokhttp3/internal/io/FileSystem$Companion;", "", "()V", "SYSTEM", "Lokhttp3/internal/io/FileSystem;", "SystemFileSystem", "okhttp"})
    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE;

        private Companion() {
        }

        static {
            $$INSTANCE = new Companion();
        }

        @Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u0006H\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0018\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0006H\u0016J\u0010\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016\u00a8\u0006\u0017"}, d2={"Lokhttp3/internal/io/FileSystem$Companion$SystemFileSystem;", "Lokhttp3/internal/io/FileSystem;", "()V", "appendingSink", "Lokio/Sink;", "file", "Ljava/io/File;", "delete", "", "deleteContents", "directory", "exists", "", "rename", "from", "to", "sink", "size", "", "source", "Lokio/Source;", "toString", "", "okhttp"})
        static final class SystemFileSystem
        implements FileSystem {
            @Override
            @NotNull
            public final Source source(@NotNull File file) throws FileNotFoundException {
                Intrinsics.checkNotNullParameter(file, "");
                return Okio.source(file);
            }

            @Override
            @NotNull
            public final Sink sink(@NotNull File file) throws FileNotFoundException {
                Sink sink2;
                Intrinsics.checkNotNullParameter(file, "");
                try {
                    sink2 = Okio.sink$default(file, false, 1, null);
                }
                catch (FileNotFoundException fileNotFoundException) {
                    file.getParentFile().mkdirs();
                    sink2 = Okio.sink$default(file, false, 1, null);
                }
                return sink2;
            }

            @Override
            @NotNull
            public final Sink appendingSink(@NotNull File file) throws FileNotFoundException {
                Sink sink2;
                Intrinsics.checkNotNullParameter(file, "");
                try {
                    sink2 = Okio.appendingSink(file);
                }
                catch (FileNotFoundException fileNotFoundException) {
                    file.getParentFile().mkdirs();
                    sink2 = Okio.appendingSink(file);
                }
                return sink2;
            }

            @Override
            public final void delete(@NotNull File file) throws IOException {
                Intrinsics.checkNotNullParameter(file, "");
                if (!file.delete() && file.exists()) {
                    throw new IOException("failed to delete " + file);
                }
            }

            @Override
            public final boolean exists(@NotNull File file) {
                Intrinsics.checkNotNullParameter(file, "");
                return file.exists();
            }

            @Override
            public final long size(@NotNull File file) {
                Intrinsics.checkNotNullParameter(file, "");
                return file.length();
            }

            @Override
            public final void rename(@NotNull File file, @NotNull File file2) throws IOException {
                Intrinsics.checkNotNullParameter(file, "");
                Intrinsics.checkNotNullParameter(file2, "");
                this.delete(file2);
                if (!file.renameTo(file2)) {
                    throw new IOException("failed to rename " + file + " to " + file2);
                }
            }

            @Override
            public final void deleteContents(@NotNull File fileArray) throws IOException {
                Intrinsics.checkNotNullParameter(fileArray, "");
                File[] fileArray2 = fileArray.listFiles();
                if (fileArray2 == null) {
                    throw new IOException("not a readable directory: " + fileArray);
                }
                for (File file : fileArray2) {
                    if (file.isDirectory()) {
                        Intrinsics.checkNotNullExpressionValue(file, "");
                        this.deleteContents(file);
                    }
                    if (file.delete()) continue;
                    throw new IOException("failed to delete " + file);
                }
            }

            @NotNull
            public final String toString() {
                return "FileSystem.SYSTEM";
            }
        }
    }
}

