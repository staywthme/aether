/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.io.FilePathComponents;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000$\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0011\u0010\u000b\u001a\u00020\f*\u00020\bH\u0002\u00a2\u0006\u0002\b\r\u001a\u001c\u0010\u000e\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\fH\u0000\u001a\f\u0010\u0011\u001a\u00020\u0012*\u00020\u0002H\u0000\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0000\u0010\u0003\"\u0018\u0010\u0004\u001a\u00020\u0002*\u00020\u00028@X\u0080\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\"\u0018\u0010\u0007\u001a\u00020\b*\u00020\u00028@X\u0080\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0013"}, d2={"isRooted", "", "Ljava/io/File;", "(Ljava/io/File;)Z", "root", "getRoot", "(Ljava/io/File;)Ljava/io/File;", "rootName", "", "getRootName", "(Ljava/io/File;)Ljava/lang/String;", "getRootLength", "", "getRootLength$FilesKt__FilePathComponentsKt", "subPath", "beginIndex", "endIndex", "toComponents", "Lkotlin/io/FilePathComponents;", "kotlin-stdlib"}, xs="kotlin/io/FilesKt")
@SourceDebugExtension(value={"SMAP\nFilePathComponents.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FilePathComponents.kt\nkotlin/io/FilesKt__FilePathComponentsKt\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,148:1\n1549#2:149\n1620#2,3:150\n*S KotlinDebug\n*F\n+ 1 FilePathComponents.kt\nkotlin/io/FilesKt__FilePathComponentsKt\n*L\n133#1:149\n133#1:150,3\n*E\n"})
class FilesKt__FilePathComponentsKt {
    private static final int getRootLength$FilesKt__FilePathComponentsKt(String string) {
        int n2 = StringsKt.indexOf$default((CharSequence)string, File.separatorChar, 0, false, 4, null);
        if (n2 == 0) {
            if (string.length() > 1 && string.charAt(1) == File.separatorChar && (n2 = StringsKt.indexOf$default((CharSequence)string, File.separatorChar, 2, false, 4, null)) >= 0) {
                if ((n2 = StringsKt.indexOf$default((CharSequence)string, File.separatorChar, n2 + 1, false, 4, null)) >= 0) {
                    return n2 + 1;
                }
                return string.length();
            }
            return 1;
        }
        if (n2 > 0 && string.charAt(n2 - 1) == ':') {
            return ++n2;
        }
        if (n2 == -1 && StringsKt.endsWith$default((CharSequence)string, ':', false, 2, null)) {
            return string.length();
        }
        return 0;
    }

    @NotNull
    public static final String getRootName(@NotNull File file) {
        Intrinsics.checkNotNullParameter(file, "");
        String string = file.getPath();
        Intrinsics.checkNotNullExpressionValue(string, "");
        String string2 = string;
        String string3 = file.getPath();
        Intrinsics.checkNotNullExpressionValue(string3, "");
        int n2 = FilesKt__FilePathComponentsKt.getRootLength$FilesKt__FilePathComponentsKt(string3);
        String string4 = string2.substring(0, n2);
        Intrinsics.checkNotNullExpressionValue(string4, "");
        return string4;
    }

    @NotNull
    public static final File getRoot(@NotNull File file) {
        Intrinsics.checkNotNullParameter(file, "");
        return new File(FilesKt.getRootName(file));
    }

    public static final boolean isRooted(@NotNull File file) {
        Intrinsics.checkNotNullParameter(file, "");
        String string = file.getPath();
        Intrinsics.checkNotNullExpressionValue(string, "");
        return FilesKt__FilePathComponentsKt.getRootLength$FilesKt__FilePathComponentsKt(string) > 0;
    }

    @NotNull
    public static final FilePathComponents toComponents(@NotNull File object) {
        List list2;
        Intrinsics.checkNotNullParameter(object, "");
        object = ((File)object).getPath();
        Intrinsics.checkNotNullExpressionValue(object, "");
        int n2 = FilesKt__FilePathComponentsKt.getRootLength$FilesKt__FilePathComponentsKt((String)object);
        Object object2 = object;
        String string = ((String)object2).substring(0, n2);
        Intrinsics.checkNotNullExpressionValue(string, "");
        String string2 = string;
        String string3 = ((String)object).substring(n2);
        Intrinsics.checkNotNullExpressionValue(string3, "");
        object2 = string3;
        if (((CharSequence)string3).length() == 0) {
            list2 = CollectionsKt.emptyList();
        } else {
            char[] cArray = new char[1];
            object = cArray;
            cArray[0] = File.separatorChar;
            Object object3 = object = (Iterable)StringsKt.split$default((CharSequence)object2, (char[])object, false, 0, 6, null);
            object = new ArrayList(CollectionsKt.collectionSizeOrDefault(object, 10));
            object3 = object3.iterator();
            while (object3.hasNext()) {
                object2 = object3.next();
                object2 = (String)object2;
                Object object4 = object;
                object4.add(new File((String)object2));
            }
            list2 = (List)object;
        }
        object = list2;
        return new FilePathComponents(new File(string2), (List<? extends File>)object);
    }

    @NotNull
    public static final File subPath(@NotNull File file, int n2, int n3) {
        Intrinsics.checkNotNullParameter(file, "");
        return FilesKt.toComponents(file).subPath(n2, n3);
    }
}

