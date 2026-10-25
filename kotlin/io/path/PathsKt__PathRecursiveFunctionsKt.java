/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io.path;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystemException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.SecureDirectoryStream;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.SinceKotlin;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.io.path.CopyActionContext;
import kotlin.io.path.CopyActionResult;
import kotlin.io.path.DefaultCopyActionContext;
import kotlin.io.path.ExceptionsCollector;
import kotlin.io.path.ExperimentalPathApi;
import kotlin.io.path.FileVisitorBuilder;
import kotlin.io.path.LinkFollowing;
import kotlin.io.path.OnErrorResult;
import kotlin.io.path.PathsKt;
import kotlin.io.path.PathsKt__PathReadWriteKt;
import kotlin.io.path.PathsKt__PathRecursiveFunctionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.jvm.internal.SpreadBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000v\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a$\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0082\b\u00a2\u0006\u0002\b\u0006\u001a\u001d\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002\u00a2\u0006\u0002\b\n\u001a\u001d\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002\u00a2\u0006\u0002\b\r\u001a&\u0010\u000e\u001a\u0004\u0018\u0001H\u000f\"\u0004\b\u0000\u0010\u000f2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u0005H\u0082\b\u00a2\u0006\u0004\b\u0010\u0010\u0011\u001aw\u0010\u0012\u001a\u00020\t*\u00020\t2\u0006\u0010\u0013\u001a\u00020\t2Q\b\u0002\u0010\u0014\u001aK\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0013\u0012\u0017\u0012\u00150\u0019j\u0002`\u001a\u00a2\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u001b\u0012\u0004\u0012\u00020\u001c0\u00152\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001eH\u0007\u001a\u00b4\u0001\u0010\u0012\u001a\u00020\t*\u00020\t2\u0006\u0010\u0013\u001a\u00020\t2Q\b\u0002\u0010\u0014\u001aK\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0013\u0012\u0017\u0012\u00150\u0019j\u0002`\u001a\u00a2\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u001b\u0012\u0004\u0012\u00020\u001c0\u00152\u0006\u0010\u001d\u001a\u00020\u001e2C\b\u0002\u0010 \u001a=\u0012\u0004\u0012\u00020!\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0013\u0012\u0004\u0012\u00020\"0\u0015\u00a2\u0006\u0002\b#H\u0007\u001a\f\u0010$\u001a\u00020\u0001*\u00020\tH\u0007\u001a\u001b\u0010%\u001a\f\u0012\b\u0012\u00060\u0019j\u0002`\u001a0&*\u00020\tH\u0002\u00a2\u0006\u0002\b'\u001a'\u0010(\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\t0)2\u0006\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002\u00a2\u0006\u0002\b*\u001a'\u0010+\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\t0)2\u0006\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002\u00a2\u0006\u0002\b,\u001a5\u0010-\u001a\u00020\u001e*\b\u0012\u0004\u0012\u00020\t0)2\u0006\u0010.\u001a\u00020\t2\u0012\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u00020100\"\u000201H\u0002\u00a2\u0006\u0004\b2\u00103\u001a\u0011\u00104\u001a\u000205*\u00020\"H\u0003\u00a2\u0006\u0002\b6\u001a\u0011\u00104\u001a\u000205*\u00020\u001cH\u0003\u00a2\u0006\u0002\b6\u00a8\u00067"}, d2={"collectIfThrows", "", "collector", "Lkotlin/io/path/ExceptionsCollector;", "function", "Lkotlin/Function0;", "collectIfThrows$PathsKt__PathRecursiveFunctionsKt", "insecureEnterDirectory", "path", "Ljava/nio/file/Path;", "insecureEnterDirectory$PathsKt__PathRecursiveFunctionsKt", "insecureHandleEntry", "entry", "insecureHandleEntry$PathsKt__PathRecursiveFunctionsKt", "tryIgnoreNoSuchFileException", "R", "tryIgnoreNoSuchFileException$PathsKt__PathRecursiveFunctionsKt", "(Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "copyToRecursively", "target", "onError", "Lkotlin/Function3;", "Lkotlin/ParameterName;", "name", "source", "Ljava/lang/Exception;", "Lkotlin/Exception;", "exception", "Lkotlin/io/path/OnErrorResult;", "followLinks", "", "overwrite", "copyAction", "Lkotlin/io/path/CopyActionContext;", "Lkotlin/io/path/CopyActionResult;", "Lkotlin/ExtensionFunctionType;", "deleteRecursively", "deleteRecursivelyImpl", "", "deleteRecursivelyImpl$PathsKt__PathRecursiveFunctionsKt", "enterDirectory", "Ljava/nio/file/SecureDirectoryStream;", "enterDirectory$PathsKt__PathRecursiveFunctionsKt", "handleEntry", "handleEntry$PathsKt__PathRecursiveFunctionsKt", "isDirectory", "entryName", "options", "", "Ljava/nio/file/LinkOption;", "isDirectory$PathsKt__PathRecursiveFunctionsKt", "(Ljava/nio/file/SecureDirectoryStream;Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Z", "toFileVisitResult", "Ljava/nio/file/FileVisitResult;", "toFileVisitResult$PathsKt__PathRecursiveFunctionsKt", "kotlin-stdlib-jdk7"}, xs="kotlin/io/path/PathsKt")
@SourceDebugExtension(value={"SMAP\nPathRecursiveFunctions.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PathRecursiveFunctions.kt\nkotlin/io/path/PathsKt__PathRecursiveFunctionsKt\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,420:1\n336#1,2:424\n344#1:426\n344#1:427\n338#1,4:428\n336#1,2:432\n344#1:434\n338#1,4:435\n344#1:439\n336#1,6:440\n336#1,2:446\n344#1:448\n338#1,4:449\n1#2:421\n1855#3,2:422\n*S KotlinDebug\n*F\n+ 1 PathRecursiveFunctions.kt\nkotlin/io/path/PathsKt__PathRecursiveFunctionsKt\n*L\n352#1:424,2\n361#1:426\n364#1:427\n352#1:428,4\n372#1:432,2\n373#1:434\n372#1:435,4\n384#1:439\n392#1:440,6\n410#1:446,2\n411#1:448\n410#1:449,4\n274#1:422,2\n*E\n"})
class PathsKt__PathRecursiveFunctionsKt
extends PathsKt__PathReadWriteKt {
    @ExperimentalPathApi
    @SinceKotlin(version="1.8")
    @NotNull
    public static final Path copyToRecursively(@NotNull Path path, @NotNull Path path2, @NotNull Function3<? super Path, ? super Path, ? super Exception, ? extends OnErrorResult> function3, boolean bl, boolean bl2) {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(path2, "");
        Intrinsics.checkNotNullParameter(function3, "");
        if (bl2) {
            return PathsKt.copyToRecursively(path, path2, function3, bl, (Function3<? super CopyActionContext, ? super Path, ? super Path, ? extends CopyActionResult>)new Function3<CopyActionContext, Path, Path, CopyActionResult>(bl){
                final /* synthetic */ boolean $followLinks;
                {
                    this.$followLinks = bl;
                    super(3);
                }

                @NotNull
                public final CopyActionResult invoke(@NotNull CopyActionContext linkOptionArray, @NotNull Path copyOptionArray, @NotNull Path path) {
                    Intrinsics.checkNotNullParameter(linkOptionArray, "");
                    Intrinsics.checkNotNullParameter(copyOptionArray, "");
                    Intrinsics.checkNotNullParameter(path, "");
                    linkOptionArray = LinkFollowing.INSTANCE.toLinkOptions(this.$followLinks);
                    Path path2 = path;
                    LinkOption[] linkOptionArray2 = new LinkOption[1];
                    LinkOption[] linkOptionArray3 = linkOptionArray2;
                    linkOptionArray2[0] = LinkOption.NOFOLLOW_LINKS;
                    boolean bl = Files.isDirectory(path2, Arrays.copyOf(linkOptionArray3, linkOptionArray3.length));
                    LinkOption[] linkOptionArray4 = Arrays.copyOf(linkOptionArray, linkOptionArray.length);
                    boolean bl2 = Files.isDirectory((Path)copyOptionArray, Arrays.copyOf(linkOptionArray4, linkOptionArray4.length));
                    if (!bl2 || !bl) {
                        if (bl) {
                            PathsKt.deleteRecursively(path);
                        }
                        Path path3 = copyOptionArray;
                        copyOptionArray = new SpreadBuilder(2);
                        copyOptionArray.addSpread(linkOptionArray);
                        copyOptionArray.add(StandardCopyOption.REPLACE_EXISTING);
                        copyOptionArray = (CopyOption[])copyOptionArray.toArray(new CopyOption[copyOptionArray.size()]);
                        Intrinsics.checkNotNullExpressionValue(Files.copy(path3, path, Arrays.copyOf(copyOptionArray, copyOptionArray.length)), "");
                    }
                    return CopyActionResult.CONTINUE;
                }
            });
        }
        return PathsKt.copyToRecursively$default(path, path2, function3, bl, null, 8, null);
    }

    public static /* synthetic */ Path copyToRecursively$default(Path path, Path path2, Function3 function3, boolean bl, boolean bl2, int n2, Object object) {
        if ((n2 & 2) != 0) {
            function3 = copyToRecursively.1.INSTANCE;
        }
        return PathsKt.copyToRecursively(path, path2, (Function3<? super Path, ? super Path, ? super Exception, ? extends OnErrorResult>)function3, bl, bl2);
    }

    @ExperimentalPathApi
    @SinceKotlin(version="1.8")
    @NotNull
    public static final Path copyToRecursively(@NotNull Path path, @NotNull Path path2, @NotNull Function3<? super Path, ? super Path, ? super Exception, ? extends OnErrorResult> function3, boolean bl, @NotNull Function3<? super CopyActionContext, ? super Path, ? super Path, ? extends CopyActionResult> function32) {
        Intrinsics.checkNotNullParameter(path, "");
        Intrinsics.checkNotNullParameter(path2, "");
        Intrinsics.checkNotNullParameter(function3, "");
        Intrinsics.checkNotNullParameter(function32, "");
        Path path3 = path;
        LinkOption[] linkOptionArray = LinkFollowing.INSTANCE.toLinkOptions(bl);
        linkOptionArray = Arrays.copyOf(linkOptionArray, linkOptionArray.length);
        if (!Files.exists(path3, Arrays.copyOf(linkOptionArray, linkOptionArray.length))) {
            throw new NoSuchFileException(((Object)path).toString(), ((Object)path2).toString(), "The source file doesn't exist.");
        }
        LinkOption[] linkOptionArray2 = new LinkOption[]{};
        if (Files.exists(path, Arrays.copyOf(linkOptionArray2, linkOptionArray2.length)) && (bl || !Files.isSymbolicLink(path))) {
            LinkOption[] linkOptionArray3 = new LinkOption[]{};
            boolean bl2 = Files.exists(path2, Arrays.copyOf(linkOptionArray3, linkOptionArray3.length)) && !Files.isSymbolicLink(path2);
            if (!bl2 || !Files.isSameFile(path, path2)) {
                boolean bl3;
                if (!Intrinsics.areEqual(path.getFileSystem(), path2.getFileSystem())) {
                    bl3 = false;
                } else if (bl2) {
                    bl3 = path2.toRealPath(new LinkOption[0]).startsWith(path.toRealPath(new LinkOption[0]));
                } else {
                    Path path4 = path2.getParent();
                    if (path4 != null) {
                        Path path5 = path4;
                        LinkOption[] linkOptionArray4 = new LinkOption[]{};
                        bl3 = Files.exists(path4, Arrays.copyOf(linkOptionArray4, linkOptionArray4.length)) && path5.toRealPath(new LinkOption[0]).startsWith(path.toRealPath(new LinkOption[0]));
                    } else {
                        boolean bl4;
                        bl3 = bl4 = false;
                    }
                }
                if (bl3) {
                    throw new FileSystemException(((Object)path).toString(), ((Object)path2).toString(), "Recursively copying a directory into its subdirectory is prohibited.");
                }
            }
        }
        PathsKt.visitFileTree$default(path, 0, bl, new Function1<FileVisitorBuilder, Unit>(function32, path, path2, function3){
            final /* synthetic */ Function3<CopyActionContext, Path, Path, CopyActionResult> $copyAction;
            final /* synthetic */ Path $this_copyToRecursively;
            final /* synthetic */ Path $target;
            final /* synthetic */ Function3<Path, Path, Exception, OnErrorResult> $onError;
            {
                this.$copyAction = function3;
                this.$this_copyToRecursively = path;
                this.$target = path2;
                this.$onError = function32;
                super(1);
            }

            public final void invoke(@NotNull FileVisitorBuilder fileVisitorBuilder) {
                Intrinsics.checkNotNullParameter(fileVisitorBuilder, "");
                fileVisitorBuilder.onPreVisitDirectory((Function2<? super Path, ? super BasicFileAttributes, ? extends FileVisitResult>)new Function2<Path, BasicFileAttributes, FileVisitResult>(this.$copyAction, this.$this_copyToRecursively, this.$target, this.$onError){
                    final /* synthetic */ Function3<CopyActionContext, Path, Path, CopyActionResult> $copyAction;
                    final /* synthetic */ Path $this_copyToRecursively;
                    final /* synthetic */ Path $target;
                    final /* synthetic */ Function3<Path, Path, Exception, OnErrorResult> $onError;
                    {
                        this.$copyAction = function3;
                        this.$this_copyToRecursively = path;
                        this.$target = path2;
                        this.$onError = function32;
                        super(2, Intrinsics.Kotlin.class, "copy", "copyToRecursively$copy$PathsKt__PathRecursiveFunctionsKt(Lkotlin/jvm/functions/Function3;Ljava/nio/file/Path;Ljava/nio/file/Path;Lkotlin/jvm/functions/Function3;Ljava/nio/file/Path;Ljava/nio/file/attribute/BasicFileAttributes;)Ljava/nio/file/FileVisitResult;", 0);
                    }

                    @NotNull
                    public final FileVisitResult invoke(@NotNull Path path, @NotNull BasicFileAttributes basicFileAttributes) {
                        Intrinsics.checkNotNullParameter(path, "");
                        Intrinsics.checkNotNullParameter(basicFileAttributes, "");
                        return PathsKt__PathRecursiveFunctionsKt.access$copyToRecursively$copy(this.$copyAction, this.$this_copyToRecursively, this.$target, this.$onError, path, basicFileAttributes);
                    }
                });
                fileVisitorBuilder.onVisitFile((Function2<? super Path, ? super BasicFileAttributes, ? extends FileVisitResult>)new Function2<Path, BasicFileAttributes, FileVisitResult>(this.$copyAction, this.$this_copyToRecursively, this.$target, this.$onError){
                    final /* synthetic */ Function3<CopyActionContext, Path, Path, CopyActionResult> $copyAction;
                    final /* synthetic */ Path $this_copyToRecursively;
                    final /* synthetic */ Path $target;
                    final /* synthetic */ Function3<Path, Path, Exception, OnErrorResult> $onError;
                    {
                        this.$copyAction = function3;
                        this.$this_copyToRecursively = path;
                        this.$target = path2;
                        this.$onError = function32;
                        super(2, Intrinsics.Kotlin.class, "copy", "copyToRecursively$copy$PathsKt__PathRecursiveFunctionsKt(Lkotlin/jvm/functions/Function3;Ljava/nio/file/Path;Ljava/nio/file/Path;Lkotlin/jvm/functions/Function3;Ljava/nio/file/Path;Ljava/nio/file/attribute/BasicFileAttributes;)Ljava/nio/file/FileVisitResult;", 0);
                    }

                    @NotNull
                    public final FileVisitResult invoke(@NotNull Path path, @NotNull BasicFileAttributes basicFileAttributes) {
                        Intrinsics.checkNotNullParameter(path, "");
                        Intrinsics.checkNotNullParameter(basicFileAttributes, "");
                        return PathsKt__PathRecursiveFunctionsKt.access$copyToRecursively$copy(this.$copyAction, this.$this_copyToRecursively, this.$target, this.$onError, path, basicFileAttributes);
                    }
                });
                fileVisitorBuilder.onVisitFileFailed((Function2<? super Path, ? super IOException, ? extends FileVisitResult>)new Function2<Path, Exception, FileVisitResult>(this.$onError, this.$this_copyToRecursively, this.$target){
                    final /* synthetic */ Function3<Path, Path, Exception, OnErrorResult> $onError;
                    final /* synthetic */ Path $this_copyToRecursively;
                    final /* synthetic */ Path $target;
                    {
                        this.$onError = function3;
                        this.$this_copyToRecursively = path;
                        this.$target = path2;
                        super(2, Intrinsics.Kotlin.class, "error", "copyToRecursively$error$PathsKt__PathRecursiveFunctionsKt(Lkotlin/jvm/functions/Function3;Ljava/nio/file/Path;Ljava/nio/file/Path;Ljava/nio/file/Path;Ljava/lang/Exception;)Ljava/nio/file/FileVisitResult;", 0);
                    }

                    @NotNull
                    public final FileVisitResult invoke(@NotNull Path path, @NotNull Exception exception) {
                        Intrinsics.checkNotNullParameter(path, "");
                        Intrinsics.checkNotNullParameter(exception, "");
                        return PathsKt__PathRecursiveFunctionsKt.access$copyToRecursively$error(this.$onError, this.$this_copyToRecursively, this.$target, path, exception);
                    }
                });
                fileVisitorBuilder.onPostVisitDirectory((Function2<? super Path, ? super IOException, ? extends FileVisitResult>)new Function2<Path, IOException, FileVisitResult>(this.$onError, this.$this_copyToRecursively, this.$target){
                    final /* synthetic */ Function3<Path, Path, Exception, OnErrorResult> $onError;
                    final /* synthetic */ Path $this_copyToRecursively;
                    final /* synthetic */ Path $target;
                    {
                        this.$onError = function3;
                        this.$this_copyToRecursively = path;
                        this.$target = path2;
                        super(2);
                    }

                    @NotNull
                    public final FileVisitResult invoke(@NotNull Path path, @Nullable IOException iOException) {
                        Intrinsics.checkNotNullParameter(path, "");
                        if (iOException == null) {
                            return FileVisitResult.CONTINUE;
                        }
                        return PathsKt__PathRecursiveFunctionsKt.access$copyToRecursively$error(this.$onError, this.$this_copyToRecursively, this.$target, path, iOException);
                    }
                });
            }
        }, 1, null);
        return path2;
    }

    public static /* synthetic */ Path copyToRecursively$default(Path path, Path path2, Function3 function3, boolean bl, Function3 function32, int n2, Object object) {
        if ((n2 & 2) != 0) {
            function3 = copyToRecursively.3.INSTANCE;
        }
        if ((n2 & 8) != 0) {
            function32 = new Function3<CopyActionContext, Path, Path, CopyActionResult>(bl){
                final /* synthetic */ boolean $followLinks;
                {
                    this.$followLinks = bl;
                    super(3);
                }

                @NotNull
                public final CopyActionResult invoke(@NotNull CopyActionContext copyActionContext, @NotNull Path path, @NotNull Path path2) {
                    Intrinsics.checkNotNullParameter(copyActionContext, "");
                    Intrinsics.checkNotNullParameter(path, "");
                    Intrinsics.checkNotNullParameter(path2, "");
                    return copyActionContext.copyToIgnoringExistingDirectory(path, path2, this.$followLinks);
                }
            };
        }
        return PathsKt.copyToRecursively(path, path2, (Function3<? super Path, ? super Path, ? super Exception, ? extends OnErrorResult>)function3, bl, function32);
    }

    @ExperimentalPathApi
    private static final FileVisitResult toFileVisitResult$PathsKt__PathRecursiveFunctionsKt(CopyActionResult copyActionResult) {
        switch (WhenMappings.$EnumSwitchMapping$0[copyActionResult.ordinal()]) {
            case 1: {
                return FileVisitResult.CONTINUE;
            }
            case 2: {
                return FileVisitResult.TERMINATE;
            }
            case 3: {
                return FileVisitResult.SKIP_SUBTREE;
            }
        }
        throw new NoWhenBranchMatchedException();
    }

    @ExperimentalPathApi
    private static final FileVisitResult toFileVisitResult$PathsKt__PathRecursiveFunctionsKt(OnErrorResult onErrorResult) {
        switch (WhenMappings.$EnumSwitchMapping$1[onErrorResult.ordinal()]) {
            case 1: {
                return FileVisitResult.TERMINATE;
            }
            case 2: {
                return FileVisitResult.SKIP_SUBTREE;
            }
        }
        throw new NoWhenBranchMatchedException();
    }

    @ExperimentalPathApi
    @SinceKotlin(version="1.8")
    public static final void deleteRecursively(@NotNull Path object) {
        Intrinsics.checkNotNullParameter(object, "");
        object = PathsKt__PathRecursiveFunctionsKt.deleteRecursivelyImpl$PathsKt__PathRecursiveFunctionsKt((Path)object);
        if (!((Collection)object).isEmpty()) {
            FileSystemException fileSystemException;
            FileSystemException fileSystemException2 = fileSystemException = new FileSystemException("Failed to delete one or more files. See suppressed exceptions for details.");
            object = (Iterable)object;
            object = object.iterator();
            while (object.hasNext()) {
                Object object2 = object.next();
                object2 = (Exception)object2;
                ExceptionsKt.addSuppressed(fileSystemException2, (Throwable)object2);
            }
            throw (Throwable)fileSystemException;
        }
    }

    private static final List<Exception> deleteRecursivelyImpl$PathsKt__PathRecursiveFunctionsKt(Path path) {
        ExceptionsCollector exceptionsCollector = new ExceptionsCollector(0, 1, null);
        boolean bl = true;
        Path path2 = path.getParent();
        if (path2 != null) {
            Closeable closeable;
            Path path3 = path2;
            try {
                closeable = Files.newDirectoryStream(path3);
            }
            catch (Throwable throwable) {
                closeable = null;
            }
            if (closeable != null) {
                block9: {
                    closeable = closeable;
                    Throwable throwable = null;
                    try {
                        Closeable closeable2 = closeable;
                        if (!(closeable2 instanceof SecureDirectoryStream)) break block9;
                        bl = false;
                        exceptionsCollector.setPath(path3);
                        SecureDirectoryStream secureDirectoryStream = (SecureDirectoryStream)closeable2;
                        Path path4 = path.getFileName();
                        Intrinsics.checkNotNullExpressionValue(path4, "");
                        PathsKt__PathRecursiveFunctionsKt.handleEntry$PathsKt__PathRecursiveFunctionsKt(secureDirectoryStream, path4, exceptionsCollector);
                    }
                    catch (Throwable throwable2) {
                        try {
                            Throwable throwable3 = throwable2;
                            throwable = throwable2;
                            throw throwable3;
                        }
                        catch (Throwable throwable4) {
                            CloseableKt.closeFinally(closeable, throwable);
                            throw throwable4;
                        }
                    }
                }
                CloseableKt.closeFinally(closeable, null);
            }
        }
        if (bl) {
            PathsKt__PathRecursiveFunctionsKt.insecureHandleEntry$PathsKt__PathRecursiveFunctionsKt(path, exceptionsCollector);
        }
        return exceptionsCollector.getCollectedExceptions();
    }

    private static final void collectIfThrows$PathsKt__PathRecursiveFunctionsKt(ExceptionsCollector exceptionsCollector, Function0<Unit> function0) {
        try {
            function0.invoke();
            return;
        }
        catch (Exception exception) {
            exceptionsCollector.collect(exception);
            return;
        }
    }

    private static final <R> R tryIgnoreNoSuchFileException$PathsKt__PathRecursiveFunctionsKt(Function0<? extends R> function0) {
        try {
            function0 = function0.invoke();
        }
        catch (NoSuchFileException noSuchFileException) {
            function0 = null;
        }
        return (R)function0;
    }

    private static final void handleEntry$PathsKt__PathRecursiveFunctionsKt(SecureDirectoryStream<Path> secureDirectoryStream, Path path, ExceptionsCollector exceptionsCollector) {
        exceptionsCollector.enterEntry(path);
        try {
            LinkOption[] linkOptionArray = new LinkOption[1];
            LinkOption[] linkOptionArray2 = linkOptionArray;
            linkOptionArray[0] = LinkOption.NOFOLLOW_LINKS;
            if (PathsKt__PathRecursiveFunctionsKt.isDirectory$PathsKt__PathRecursiveFunctionsKt(secureDirectoryStream, path, linkOptionArray2)) {
                int n2 = exceptionsCollector.getTotalExceptions();
                PathsKt__PathRecursiveFunctionsKt.enterDirectory$PathsKt__PathRecursiveFunctionsKt(secureDirectoryStream, path, exceptionsCollector);
                if (n2 == exceptionsCollector.getTotalExceptions()) {
                    try {
                        secureDirectoryStream.deleteDirectory(path);
                    }
                    catch (NoSuchFileException noSuchFileException) {}
                }
            } else {
                try {
                    secureDirectoryStream.deleteFile(path);
                }
                catch (NoSuchFileException noSuchFileException) {}
            }
        }
        catch (Exception exception) {
            exceptionsCollector.collect(exception);
        }
        exceptionsCollector.exitEntry(path);
    }

    private static final void enterDirectory$PathsKt__PathRecursiveFunctionsKt(SecureDirectoryStream<Path> closeable, Path object, ExceptionsCollector exceptionsCollector) {
        try {
            block10: {
                block9: {
                    Object object2;
                    try {
                        LinkOption[] linkOptionArray = new LinkOption[1];
                        object2 = linkOptionArray;
                        linkOptionArray[0] = LinkOption.NOFOLLOW_LINKS;
                        closeable = closeable.newDirectoryStream(object, (LinkOption)object2);
                    }
                    catch (NoSuchFileException noSuchFileException) {
                        closeable = null;
                    }
                    if (closeable == null) break block9;
                    object = null;
                    try {
                        object2 = (SecureDirectoryStream)closeable;
                        for (Path path : object2) {
                            Path path2 = path.getFileName();
                            Intrinsics.checkNotNullExpressionValue(path2, "");
                            PathsKt__PathRecursiveFunctionsKt.handleEntry$PathsKt__PathRecursiveFunctionsKt((SecureDirectoryStream<Path>)object2, path2, exceptionsCollector);
                        }
                    }
                    catch (Throwable throwable) {
                        try {
                            object2 = throwable;
                            object = throwable;
                            throw object2;
                        }
                        catch (Throwable throwable2) {
                            CloseableKt.closeFinally(closeable, (Throwable)object);
                            throw throwable2;
                        }
                    }
                    break block10;
                }
                return;
            }
            CloseableKt.closeFinally(closeable, null);
        }
        catch (Exception exception) {
            exceptionsCollector.collect(exception);
        }
    }

    private static final boolean isDirectory$PathsKt__PathRecursiveFunctionsKt(SecureDirectoryStream<Path> object, Path path, LinkOption ... linkOptionArray) {
        try {
            object = object.getFileAttributeView((Path)path, BasicFileAttributeView.class, Arrays.copyOf(linkOptionArray, linkOptionArray.length)).readAttributes().isDirectory();
        }
        catch (NoSuchFileException noSuchFileException) {
            object = null;
        }
        SecureDirectoryStream<Path> secureDirectoryStream = object;
        if (secureDirectoryStream != null) {
            return (Boolean)((Object)secureDirectoryStream);
        }
        return false;
    }

    private static final void insecureHandleEntry$PathsKt__PathRecursiveFunctionsKt(Path path, ExceptionsCollector exceptionsCollector) {
        try {
            block4: {
                block3: {
                    block2: {
                        Path path2 = path;
                        LinkOption[] linkOptionArray = new LinkOption[1];
                        LinkOption[] linkOptionArray2 = linkOptionArray;
                        linkOptionArray[0] = LinkOption.NOFOLLOW_LINKS;
                        if (!Files.isDirectory(path2, Arrays.copyOf(linkOptionArray2, linkOptionArray2.length))) break block2;
                        int n2 = exceptionsCollector.getTotalExceptions();
                        PathsKt__PathRecursiveFunctionsKt.insecureEnterDirectory$PathsKt__PathRecursiveFunctionsKt(path, exceptionsCollector);
                        if (n2 != exceptionsCollector.getTotalExceptions()) break block3;
                        break block4;
                    }
                    Files.deleteIfExists(path);
                }
                return;
            }
            Files.deleteIfExists(path);
        }
        catch (Exception exception) {
            exceptionsCollector.collect(exception);
        }
    }

    private static final void insecureEnterDirectory$PathsKt__PathRecursiveFunctionsKt(Path object, ExceptionsCollector exceptionsCollector) {
        try {
            block10: {
                block9: {
                    Iterator iterator2;
                    try {
                        iterator2 = Files.newDirectoryStream((Path)object);
                    }
                    catch (NoSuchFileException noSuchFileException) {
                        iterator2 = null;
                    }
                    object = iterator2;
                    if (object == null) break block9;
                    object = (Closeable)object;
                    Throwable throwable = null;
                    try {
                        iterator2 = (DirectoryStream)object;
                        iterator2 = iterator2.iterator();
                        while (iterator2.hasNext()) {
                            Path path = (Path)iterator2.next();
                            Intrinsics.checkNotNullExpressionValue(path, "");
                            PathsKt__PathRecursiveFunctionsKt.insecureHandleEntry$PathsKt__PathRecursiveFunctionsKt(path, exceptionsCollector);
                        }
                    }
                    catch (Throwable throwable2) {
                        try {
                            iterator2 = throwable2;
                            throwable = throwable2;
                            throw iterator2;
                        }
                        catch (Throwable throwable3) {
                            CloseableKt.closeFinally((Closeable)object, throwable);
                            throw throwable3;
                        }
                    }
                    break block10;
                }
                return;
            }
            CloseableKt.closeFinally((Closeable)object, null);
        }
        catch (Exception exception) {
            exceptionsCollector.collect(exception);
        }
    }

    private static final Path copyToRecursively$destination$PathsKt__PathRecursiveFunctionsKt(Path path, Path path2, Path path3) {
        path = PathsKt.relativeTo(path3, path);
        Path path4 = path2.resolve(((Object)path).toString());
        Intrinsics.checkNotNullExpressionValue(path4, "");
        return path4;
    }

    private static final FileVisitResult copyToRecursively$error$PathsKt__PathRecursiveFunctionsKt(Function3<? super Path, ? super Path, ? super Exception, ? extends OnErrorResult> function3, Path path, Path path2, Path path3, Exception exception) {
        return PathsKt__PathRecursiveFunctionsKt.toFileVisitResult$PathsKt__PathRecursiveFunctionsKt(function3.invoke(path3, PathsKt__PathRecursiveFunctionsKt.copyToRecursively$destination$PathsKt__PathRecursiveFunctionsKt(path, path2, path3), exception));
    }

    private static final FileVisitResult copyToRecursively$copy$PathsKt__PathRecursiveFunctionsKt(Function3<? super CopyActionContext, ? super Path, ? super Path, ? extends CopyActionResult> object, Path path, Path path2, Function3<? super Path, ? super Path, ? super Exception, ? extends OnErrorResult> function3, Path path3, BasicFileAttributes basicFileAttributes) {
        try {
            object = PathsKt__PathRecursiveFunctionsKt.toFileVisitResult$PathsKt__PathRecursiveFunctionsKt(object.invoke(DefaultCopyActionContext.INSTANCE, path3, PathsKt__PathRecursiveFunctionsKt.copyToRecursively$destination$PathsKt__PathRecursiveFunctionsKt(path, path2, path3)));
        }
        catch (Exception exception) {
            object = PathsKt__PathRecursiveFunctionsKt.copyToRecursively$error$PathsKt__PathRecursiveFunctionsKt(function3, path, path2, path3, exception);
        }
        return object;
    }

    public static final /* synthetic */ FileVisitResult access$copyToRecursively$copy(Function3 function3, Path path, Path path2, Function3 function32, Path path3, BasicFileAttributes basicFileAttributes) {
        return PathsKt__PathRecursiveFunctionsKt.copyToRecursively$copy$PathsKt__PathRecursiveFunctionsKt(function3, path, path2, function32, path3, basicFileAttributes);
    }

    public static final /* synthetic */ FileVisitResult access$copyToRecursively$error(Function3 function3, Path path, Path path2, Path path3, Exception exception) {
        return PathsKt__PathRecursiveFunctionsKt.copyToRecursively$error$PathsKt__PathRecursiveFunctionsKt(function3, path, path2, path3, exception);
    }

    @Metadata(mv={1, 8, 0}, k=3, xi=48)
    public final class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;
        public static final /* synthetic */ int[] $EnumSwitchMapping$1;

        static {
            int[] nArray = new int[CopyActionResult.values().length];
            try {
                nArray[CopyActionResult.CONTINUE.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                nArray[CopyActionResult.TERMINATE.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                nArray[CopyActionResult.SKIP_SUBTREE.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            $EnumSwitchMapping$0 = nArray;
            nArray = new int[OnErrorResult.values().length];
            try {
                nArray[OnErrorResult.TERMINATE.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                nArray[OnErrorResult.SKIP_SUBTREE.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            $EnumSwitchMapping$1 = nArray;
        }
    }
}

