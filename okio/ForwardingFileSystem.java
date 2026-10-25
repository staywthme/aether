/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import okio.FileHandle;
import okio.FileMetadata;
import okio.FileSystem;
import okio.Path;
import okio.Sink;
import okio.Source;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0016J\u0010\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\bH\u0016J\u0018\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\nH\u0016J\u0018\u0010\u0014\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0016J\u0018\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0016\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\b0\u00172\u0006\u0010\u0012\u001a\u00020\bH\u0016J\u0018\u0010\u0018\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00172\u0006\u0010\u0012\u001a\u00020\bH\u0016J\u001e\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\b0\u001a2\u0006\u0010\u0012\u001a\u00020\b2\u0006\u0010\u001b\u001a\u00020\nH\u0016J\u0012\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u0010\u001a\u00020\bH\u0016J \u0010\u001e\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\b2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020 H\u0016J\u0018\u0010\"\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\b2\u0006\u0010\u001f\u001a\u00020 H\u0016J\u0010\u0010#\u001a\u00020$2\u0006\u0010\u0007\u001a\u00020\bH\u0016J \u0010%\u001a\u00020$2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010&\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\nH\u0016J\u0010\u0010\r\u001a\u00020'2\u0006\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010(\u001a\u00020 H\u0016R\u0013\u0010\u0002\u001a\u00020\u00018\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0004\u00a8\u0006)"}, d2={"Lokio/ForwardingFileSystem;", "Lokio/FileSystem;", "delegate", "(Lokio/FileSystem;)V", "()Lokio/FileSystem;", "appendingSink", "Lokio/Sink;", "file", "Lokio/Path;", "mustExist", "", "atomicMove", "", "source", "target", "canonicalize", "path", "createDirectory", "dir", "mustCreate", "createSymlink", "delete", "list", "", "listOrNull", "listRecursively", "Lkotlin/sequences/Sequence;", "followSymlinks", "metadataOrNull", "Lokio/FileMetadata;", "onPathParameter", "functionName", "", "parameterName", "onPathResult", "openReadOnly", "Lokio/FileHandle;", "openReadWrite", "sink", "Lokio/Source;", "toString", "okio"})
@SourceDebugExtension(value={"SMAP\nForwardingFileSystem.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ForwardingFileSystem.kt\nokio/ForwardingFileSystem\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,243:1\n1620#2,3:244\n1620#2,3:247\n*S KotlinDebug\n*F\n+ 1 ForwardingFileSystem.kt\nokio/ForwardingFileSystem\n*L\n166#1:244,3\n174#1:247,3\n*E\n"})
public abstract class ForwardingFileSystem
extends FileSystem {
    @NotNull
    private final FileSystem delegate;

    public ForwardingFileSystem(@NotNull FileSystem fileSystem) {
        Intrinsics.checkNotNullParameter(fileSystem, "");
        this.delegate = fileSystem;
    }

    @JvmName(name="delegate")
    @NotNull
    public final FileSystem delegate() {
        return this.delegate;
    }

    @NotNull
    public Path onPathParameter(@NotNull Path path, @NotNull String string, @NotNull String string2) {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        return path;
    }

    @NotNull
    public Path onPathResult(@NotNull Path path, @NotNull String string) {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(string, "");
        return path;
    }

    @Override
    @NotNull
    public Path canonicalize(@NotNull Path path) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        path = this.onPathParameter(path, "canonicalize", "path");
        path = this.delegate.canonicalize(path);
        return this.onPathResult(path, "canonicalize");
    }

    @Override
    @Nullable
    public FileMetadata metadataOrNull(@NotNull Path object) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        object = this.onPathParameter((Path)object, "metadataOrNull", "path");
        FileMetadata fileMetadata = this.delegate.metadataOrNull((Path)object);
        if (fileMetadata == null) {
            return null;
        }
        object = fileMetadata;
        if (fileMetadata.getSymlinkTarget() == null) {
            return object;
        }
        Path path = this.onPathResult(((FileMetadata)object).getSymlinkTarget(), "metadataOrNull");
        return FileMetadata.copy$default((FileMetadata)object, false, false, path, null, null, null, null, null, 251, null);
    }

    @Override
    @NotNull
    public List<Path> list(@NotNull Path object) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        object = this.onPathParameter((Path)object, "list", "dir");
        object = this.delegate.list((Path)object);
        object = (Iterable)object;
        Collection collection = new ArrayList();
        object = object.iterator();
        while (object.hasNext()) {
            Object object2 = object.next();
            object2 = (Path)object2;
            Collection collection2 = collection;
            collection2.add(this.onPathResult((Path)object2, "list"));
        }
        object = (List)collection;
        CollectionsKt.sort(object);
        return object;
    }

    @Override
    @Nullable
    public List<Path> listOrNull(@NotNull Path object) {
        Intrinsics.checkNotNullParameter(object, "");
        object = this.onPathParameter((Path)object, "listOrNull", "dir");
        List<Path> list2 = this.delegate.listOrNull((Path)object);
        if (list2 == null) {
            return null;
        }
        object = list2;
        object = list2;
        Collection collection = new ArrayList();
        object = object.iterator();
        while (object.hasNext()) {
            Object object2 = object.next();
            object2 = (Path)object2;
            Collection collection2 = collection;
            collection2.add(this.onPathResult((Path)object2, "listOrNull"));
        }
        object = (List)collection;
        CollectionsKt.sort(object);
        return object;
    }

    @Override
    @NotNull
    public Sequence<Path> listRecursively(@NotNull Path object, boolean bl) {
        Intrinsics.checkNotNullParameter(object, "");
        object = this.onPathParameter((Path)object, "listRecursively", "dir");
        object = this.delegate.listRecursively((Path)object, bl);
        return SequencesKt.map(object, (Function1)new Function1<Path, Path>(this){
            final /* synthetic */ ForwardingFileSystem this$0;
            {
                this.this$0 = forwardingFileSystem;
                super(1);
            }

            @NotNull
            public final Path invoke(@NotNull Path path) {
                Intrinsics.checkNotNullParameter(path, "");
                return this.this$0.onPathResult(path, "listRecursively");
            }
        });
    }

    @Override
    @NotNull
    public FileHandle openReadOnly(@NotNull Path path) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        path = this.onPathParameter(path, "openReadOnly", "file");
        return this.delegate.openReadOnly(path);
    }

    @Override
    @NotNull
    public FileHandle openReadWrite(@NotNull Path path, boolean bl, boolean bl2) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        path = this.onPathParameter(path, "openReadWrite", "file");
        return this.delegate.openReadWrite(path, bl, bl2);
    }

    @Override
    @NotNull
    public Source source(@NotNull Path path) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        path = this.onPathParameter(path, "source", "file");
        return this.delegate.source(path);
    }

    @Override
    @NotNull
    public Sink sink(@NotNull Path path, boolean bl) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        path = this.onPathParameter(path, "sink", "file");
        return this.delegate.sink(path, bl);
    }

    @Override
    @NotNull
    public Sink appendingSink(@NotNull Path path, boolean bl) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        path = this.onPathParameter(path, "appendingSink", "file");
        return this.delegate.appendingSink(path, bl);
    }

    @Override
    public void createDirectory(@NotNull Path path, boolean bl) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        path = this.onPathParameter(path, "createDirectory", "dir");
        this.delegate.createDirectory(path, bl);
    }

    @Override
    public void atomicMove(@NotNull Path path, @NotNull Path path2) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(path2, "");
        path = this.onPathParameter(path, "atomicMove", "source");
        path2 = this.onPathParameter(path2, "atomicMove", "target");
        this.delegate.atomicMove(path, path2);
    }

    @Override
    public void delete(@NotNull Path path, boolean bl) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        path = this.onPathParameter(path, "delete", "path");
        this.delegate.delete(path, bl);
    }

    @Override
    public void createSymlink(@NotNull Path path, @NotNull Path path2) throws IOException {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(path2, "");
        path = this.onPathParameter(path, "createSymlink", "source");
        path2 = this.onPathParameter(path2, "createSymlink", "target");
        this.delegate.createSymlink(path, path2);
    }

    @NotNull
    public String toString() {
        return Reflection.getOrCreateKotlinClass(this.getClass()).getSimpleName() + '(' + this.delegate + ')';
    }
}

