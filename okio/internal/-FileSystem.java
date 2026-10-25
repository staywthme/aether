/*
 * Decompiled with CFR 0.152.
 */
package okio.internal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.ArrayDeque;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.BaseContinuationImpl;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequenceScope;
import kotlin.sequences.SequencesKt;
import okio.BufferedSink;
import okio.FileMetadata;
import okio.FileSystem;
import okio.Okio;
import okio.Path;
import okio.Source;
import okio.internal.-FileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=2, xi=48, d1={"\u00004\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\r\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001aI\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u00022\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00030\u00072\u0006\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0080@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\f\u001a\u001c\u0010\r\u001a\u00020\u0001*\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0003H\u0000\u001a\u001c\u0010\u0010\u001a\u00020\u0001*\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\nH\u0000\u001a\u001c\u0010\u0013\u001a\u00020\u0001*\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\nH\u0000\u001a\u0014\u0010\u0016\u001a\u00020\n*\u00020\u00052\u0006\u0010\b\u001a\u00020\u0003H\u0000\u001a\"\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00030\u0018*\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\nH\u0000\u001a\u0014\u0010\u0019\u001a\u00020\u001a*\u00020\u00052\u0006\u0010\b\u001a\u00020\u0003H\u0000\u001a\u0016\u0010\u001b\u001a\u0004\u0018\u00010\u0003*\u00020\u00052\u0006\u0010\b\u001a\u00020\u0003H\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001c"}, d2={"collectRecursively", "", "Lkotlin/sequences/SequenceScope;", "Lokio/Path;", "fileSystem", "Lokio/FileSystem;", "stack", "Lkotlin/collections/ArrayDeque;", "path", "followSymlinks", "", "postorder", "(Lkotlin/sequences/SequenceScope;Lokio/FileSystem;Lkotlin/collections/ArrayDeque;Lokio/Path;ZZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "commonCopy", "source", "target", "commonCreateDirectories", "dir", "mustCreate", "commonDeleteRecursively", "fileOrDirectory", "mustExist", "commonExists", "commonListRecursively", "Lkotlin/sequences/Sequence;", "commonMetadata", "Lokio/FileMetadata;", "symlinkTarget", "okio"})
@JvmName(name="-FileSystem")
@SourceDebugExtension(value={"SMAP\nFileSystem.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FileSystem.kt\nokio/internal/-FileSystem\n+ 2 Okio.kt\nokio/Okio__OkioKt\n*L\n1#1,155:1\n52#2,5:156\n52#2,21:161\n60#2,10:182\n57#2,2:192\n71#2,2:194\n*S KotlinDebug\n*F\n+ 1 FileSystem.kt\nokio/internal/-FileSystem\n*L\n65#1:156,5\n66#1:161,21\n65#1:182,10\n65#1:192,2\n65#1:194,2\n*E\n"})
public final class -FileSystem {
    @NotNull
    public static final FileMetadata commonMetadata(@NotNull FileSystem fileSystem, @NotNull Path path) throws IOException {
        Intrinsics.checkNotNullParameter(fileSystem, "");
        Intrinsics.checkNotNullParameter(path, "");
        FileMetadata fileMetadata = fileSystem.metadataOrNull(path);
        if (fileMetadata == null) {
            throw new FileNotFoundException("no such file: " + path);
        }
        return fileMetadata;
    }

    public static final boolean commonExists(@NotNull FileSystem fileSystem, @NotNull Path path) throws IOException {
        Intrinsics.checkNotNullParameter(fileSystem, "");
        Intrinsics.checkNotNullParameter(path, "");
        return fileSystem.metadataOrNull(path) != null;
    }

    public static final void commonCreateDirectories(@NotNull FileSystem fileSystem, @NotNull Path object, boolean bl) throws IOException {
        Intrinsics.checkNotNullParameter(fileSystem, "");
        Intrinsics.checkNotNullParameter(object, "");
        ArrayDeque<Path> arrayDeque = new ArrayDeque<Path>();
        for (Path path = object; path != null && !fileSystem.exists(path); path = path.parent()) {
            arrayDeque.addFirst(path);
        }
        if (bl && arrayDeque.isEmpty()) {
            throw new IOException(object + " already exists.");
        }
        for (Path path : arrayDeque) {
            fileSystem.createDirectory(path);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final void commonCopy(@NotNull FileSystem object, @NotNull Path object2, @NotNull Path comparable) throws IOException {
        Throwable throwable;
        Long l2;
        block25: {
            Object object3;
            Intrinsics.checkNotNullParameter(object, "");
            Intrinsics.checkNotNullParameter(object2, "");
            Intrinsics.checkNotNullParameter(comparable, "");
            object2 = ((FileSystem)object).source((Path)object2);
            l2 = null;
            throwable = null;
            try {
                Throwable throwable2;
                block24: {
                    Object object4;
                    object3 = (Source)object2;
                    object = Okio.buffer(((FileSystem)object).sink((Path)comparable));
                    comparable = null;
                    throwable2 = null;
                    try {
                        object4 = (BufferedSink)object;
                        comparable = object4.writeAll((Source)object3);
                    }
                    catch (Throwable throwable3) {
                        Throwable throwable5 = throwable3;
                        throwable2 = throwable3;
                        try {
                            Object object5 = object;
                            if (object5 != null) {
                                object5.close();
                            }
                            break block24;
                        }
                        catch (Throwable throwable6) {
                            ExceptionsKt.addSuppressed(throwable2, throwable6);
                        }
                        break block24;
                    }
                    catch (Throwable throwable7) {
                        try {
                            Object object6 = object;
                            if (object6 == null) throw throwable7;
                            object6.close();
                            throw throwable7;
                        }
                        catch (Throwable throwable4) {}
                        throw throwable7;
                    }
                    try {
                        Object object7 = object;
                        if (object7 != null) {
                            object7.close();
                        }
                    }
                    catch (Throwable throwable5) {
                        object4 = throwable5;
                        throwable2 = throwable5;
                    }
                }
                Throwable throwable6 = throwable2;
                if (throwable6 != null) {
                    throw throwable6;
                }
                Comparable<Path> comparable2 = comparable;
                Intrinsics.checkNotNull(comparable2);
                l2 = ((Number)((Object)comparable2)).longValue();
            }
            catch (Throwable throwable7) {
                Throwable throwable12 = throwable7;
                throwable = throwable7;
                try {
                    Object object8 = object2;
                    if (object8 != null) {
                        object8.close();
                    }
                    break block25;
                }
                catch (Throwable throwable13) {
                    ExceptionsKt.addSuppressed(throwable, throwable13);
                }
                break block25;
            }
            catch (Throwable throwable14) {
                try {
                    Object object9 = object2;
                    if (object9 == null) throw throwable14;
                    object9.close();
                    throw throwable14;
                }
                catch (Throwable throwable8) {}
                throw throwable14;
            }
            try {
                Object object10 = object2;
                if (object10 != null) {
                    object10.close();
                }
            }
            catch (Throwable throwable9) {
                object3 = throwable9;
                throwable = throwable9;
            }
        }
        Throwable throwable10 = throwable;
        if (throwable10 != null) {
            throw throwable10;
        }
        Intrinsics.checkNotNull(l2);
    }

    public static final void commonDeleteRecursively(@NotNull FileSystem fileSystem, @NotNull Path iterator2, boolean bl) throws IOException {
        Intrinsics.checkNotNullParameter(fileSystem, "");
        Intrinsics.checkNotNullParameter(iterator2, "");
        iterator2 = SequencesKt.sequence((Function2)new Function2<SequenceScope<? super Path>, Continuation<? super Unit>, Object>(fileSystem, (Path)((Object)iterator2), null){
            int label;
            private /* synthetic */ Object L$0;
            final /* synthetic */ FileSystem $this_commonDeleteRecursively;
            final /* synthetic */ Path $fileOrDirectory;
            {
                this.$this_commonDeleteRecursively = fileSystem;
                this.$fileOrDirectory = path;
                super(2, continuation);
            }

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            @Nullable
            public final Object invokeSuspend(@NotNull Object object) {
                Object object2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0: {
                        ResultKt.throwOnFailure(object);
                        object = (SequenceScope)this.L$0;
                        this.label = 1;
                        if (-FileSystem.collectRecursively((SequenceScope<? super Path>)object, this.$this_commonDeleteRecursively, new ArrayDeque<Path>(), this.$fileOrDirectory, false, true, this) != object2) return Unit.INSTANCE;
                        return object2;
                    }
                    case 1: {
                        ResultKt.throwOnFailure(object);
                        return Unit.INSTANCE;
                    }
                }
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            @NotNull
            public final Continuation<Unit> create(@Nullable Object object, @NotNull Continuation<?> function2) {
                function2 = new /* invalid duplicate definition of identical inner class */;
                v0.L$0 = object;
                return (Continuation)((Object)function2);
            }

            @Nullable
            public final Object invoke(@NotNull SequenceScope<? super Path> sequenceScope, @Nullable Continuation<? super Unit> continuation) {
                return (((BaseContinuationImpl)this).create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
            }
        });
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            Path path = (Path)iterator2.next();
            fileSystem.delete(path, bl && !iterator2.hasNext());
        }
    }

    @NotNull
    public static final Sequence<Path> commonListRecursively(@NotNull FileSystem fileSystem, @NotNull Path path, boolean bl) throws IOException {
        Intrinsics.checkNotNullParameter(fileSystem, "");
        Intrinsics.checkNotNullParameter(path, "");
        return SequencesKt.sequence((Function2)new Function2<SequenceScope<? super Path>, Continuation<? super Unit>, Object>(path, fileSystem, bl, null){
            Object L$1;
            Object L$2;
            int label;
            private /* synthetic */ Object L$0;
            final /* synthetic */ Path $dir;
            final /* synthetic */ FileSystem $this_commonListRecursively;
            final /* synthetic */ boolean $followSymlinks;
            {
                this.$dir = path;
                this.$this_commonListRecursively = fileSystem;
                this.$followSymlinks = bl;
                super(2, continuation);
            }

            /*
             * Unable to fully structure code
             */
            @Nullable
            public final Object invokeSuspend(@NotNull Object var1_1) {
                var5_2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0: {
                        ResultKt.throwOnFailure(var1_1);
                        var2_3 = (SequenceScope)this.L$0;
                        var3_4 = new ArrayDeque<Path>();
                        var3_4.addLast(this.$dir);
                        var4_5 = this.$this_commonListRecursively.list(this.$dir).iterator();
lbl9:
                        // 3 sources

                        while (var4_5.hasNext()) {
                            var1_1 = (Path)var4_5.next();
                            this.L$0 = var2_3;
                            this.L$1 = var3_4;
                            this.L$2 = var4_5;
                            this.label = 1;
                            if (-FileSystem.collectRecursively(var2_3, this.$this_commonListRecursively, var3_4, (Path)var1_1, this.$followSymlinks, false, this) != var5_2) continue;
                            return var5_2;
                        }
                        break;
                    }
                    case 1: {
                        var4_5 = (Iterator<Path>)this.L$2;
                        var3_4 = (ArrayDeque<Path>)this.L$1;
                        var2_3 = (SequenceScope)this.L$0;
                        ResultKt.throwOnFailure(var1_1);
                        ** GOTO lbl9
                    }
                }
                return Unit.INSTANCE;
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            @NotNull
            public final Continuation<Unit> create(@Nullable Object object, @NotNull Continuation<?> function2) {
                function2 = new /* invalid duplicate definition of identical inner class */;
                v0.L$0 = object;
                return (Continuation)((Object)function2);
            }

            @Nullable
            public final Object invoke(@NotNull SequenceScope<? super Path> sequenceScope, @Nullable Continuation<? super Unit> continuation) {
                return (((BaseContinuationImpl)this).create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
            }
        });
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Nullable
    public static final Object collectRecursively(@NotNull SequenceScope<? super Path> var0, @NotNull FileSystem var1_1, @NotNull ArrayDeque<Path> var2_2, @NotNull Path var3_3, boolean var4_4, boolean var5_5, @NotNull Continuation<? super Unit> var6_6) {
        block20: {
            block19: {
                if (!(var6_6 instanceof collectRecursively.1)) ** GOTO lbl-1000
                var9_8 = var6_6;
                if ((var9_8.label & -2147483648) != 0) {
                    var9_8.label -= -2147483648;
                } else lbl-1000:
                // 2 sources

                {
                    var9_8 = new ContinuationImpl((Continuation<? super collectRecursively.1>)var6_6){
                        Object L$0;
                        Object L$1;
                        Object L$2;
                        Object L$3;
                        Object L$4;
                        boolean Z$0;
                        boolean Z$1;
                        /* synthetic */ Object result;
                        int label;

                        @Nullable
                        public final Object invokeSuspend(@NotNull Object object) {
                            this.result = object;
                            this.label |= Integer.MIN_VALUE;
                            return -FileSystem.collectRecursively(null, null, null, null, false, false, this);
                        }
                    };
                }
                var7_9 = var9_8.result;
                var10_10 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (var9_8.label) {
                    case 0: {
                        ResultKt.throwOnFailure(var7_9);
                        if (!var5_5) {
                            var9_8.L$0 = var0;
                            var9_8.L$1 = var1_1;
                            var9_8.L$2 = var2_2;
                            var9_8.L$3 = var3_3;
                            var9_8.Z$0 = var4_4;
                            var9_8.Z$1 = var5_5;
                            var9_8.label = 1;
                            if (var0.yield(var3_3, var9_8) == var10_10) {
                                return var10_10;
                            }
                        }
                        ** GOTO lbl31
                    }
                    case 1: {
                        var5_5 = var9_8.Z$1;
                        var4_4 = var9_8.Z$0;
                        var3_3 = (Path)var9_8.L$3;
                        var2_2 = (ArrayDeque)var9_8.L$2;
                        var1_1 = (FileSystem)var9_8.L$1;
                        var0 = (SequenceScope)var9_8.L$0;
                        ResultKt.throwOnFailure(var7_9);
lbl31:
                        // 2 sources

                        if ((v0 = var1_1.listOrNull(var3_3)) == null) {
                            v0 = var6_6 = CollectionsKt.emptyList();
                        }
                        if (!(((Collection)v0).isEmpty() == false)) break block20;
                        var7_9 = var3_3;
                        var8_11 = 0;
                        while (true) {
                            if (var4_4 && var2_2.contains(var7_9)) {
                                throw new IOException("symlink cycle at " + var3_3);
                            }
                            if (-FileSystem.symlinkTarget(var1_1, (Path)var7_9) != null) ** GOTO lbl43
                            if (!var4_4) {
                                break;
                            }
                            ** GOTO lbl46
lbl43:
                            // 1 sources

                            ++var8_11;
                        }
                        if (var8_11 != 0) break block20;
lbl46:
                        // 2 sources

                        var2_2.addLast(var7_9);
                        var6_6 = var6_6.iterator();
                        break block19;
                    }
                    case 2: {
                        var5_5 = var9_8.Z$1;
                        var4_4 = var9_8.Z$0;
                        var6_6 = (Iterator)var9_8.L$4;
                        var3_3 = (Path)var9_8.L$3;
                        var2_2 = (ArrayDeque)var9_8.L$2;
                        var1_1 = (FileSystem)var9_8.L$1;
                        var0 = (SequenceScope)var9_8.L$0;
                        ResultKt.throwOnFailure(var7_9);
                        break block19;
                    }
                    case 3: {
                        ResultKt.throwOnFailure(var7_9);
                        return Unit.INSTANCE;
                    }
                }
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            while (var6_6.hasNext()) {
                var7_9 = (Path)var6_6.next();
                var9_8.L$0 = var0;
                var9_8.L$1 = var1_1;
                var9_8.L$2 = var2_2;
                var9_8.L$3 = var3_3;
                var9_8.L$4 = var6_6;
                var9_8.Z$0 = var4_4;
                var9_8.Z$1 = var5_5;
                var9_8.label = 2;
                if (-FileSystem.collectRecursively(var0, var1_1, var2_2, (Path)var7_9, var4_4 != false, var5_5 != false, var9_8) != var10_10) continue;
                return var10_10;
            }
            break block20;
            finally {
                var2_2.removeLast();
            }
        }
        if (var5_5 == false) return Unit.INSTANCE;
        var9_8.L$0 = null;
        var9_8.L$1 = null;
        var9_8.L$2 = null;
        var9_8.L$3 = null;
        var9_8.L$4 = null;
        var9_8.label = 3;
        if (var0.yield(var3_3, var9_8) != var10_10) return Unit.INSTANCE;
        return var10_10;
    }

    @Nullable
    public static final Path symlinkTarget(@NotNull FileSystem object, @NotNull Path path) throws IOException {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(path, "");
        Path path2 = ((FileSystem)object).metadata(path).getSymlinkTarget();
        if (path2 == null) {
            return null;
        }
        object = path2;
        Path path3 = path.parent();
        Intrinsics.checkNotNull(path3);
        return path3.resolve((Path)object);
    }
}

