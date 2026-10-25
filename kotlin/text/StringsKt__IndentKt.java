/*
 * Decompiled with CFR 0.152.
 */
package kotlin.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.internal.IntrinsicConstEvaluation;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.sequences.SequencesKt;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;
import kotlin.text.StringsKt__AppendableKt;
import kotlin.text.StringsKt__IndentKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u000b\u001a!\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0002\u00a2\u0006\u0002\b\u0004\u001a\u0011\u0010\u0005\u001a\u00020\u0006*\u00020\u0002H\u0002\u00a2\u0006\u0002\b\u0007\u001a\u0014\u0010\b\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0002\u001aJ\u0010\t\u001a\u00020\u0002*\b\u0012\u0004\u0012\u00020\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0014\u0010\r\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001H\u0082\b\u00a2\u0006\u0002\b\u000e\u001a\u0014\u0010\u000f\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u0002\u001a\u001e\u0010\u0011\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002\u001a\f\u0010\u0013\u001a\u00020\u0002*\u00020\u0002H\u0007\u001a\u0016\u0010\u0014\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002H\u0007\u00a8\u0006\u0015"}, d2={"getIndentFunction", "Lkotlin/Function1;", "", "indent", "getIndentFunction$StringsKt__IndentKt", "indentWidth", "", "indentWidth$StringsKt__IndentKt", "prependIndent", "reindent", "", "resultSizeEstimate", "indentAddFunction", "indentCutFunction", "reindent$StringsKt__IndentKt", "replaceIndent", "newIndent", "replaceIndentByMargin", "marginPrefix", "trimIndent", "trimMargin", "kotlin-stdlib"}, xs="kotlin/text/StringsKt")
@SourceDebugExtension(value={"SMAP\nIndent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Indent.kt\nkotlin/text/StringsKt__IndentKt\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 4 _Strings.kt\nkotlin/text/StringsKt___StringsKt\n*L\n1#1,123:1\n113#1,2:125\n115#1,4:140\n120#1,2:153\n113#1,2:162\n115#1,4:177\n120#1,2:184\n1#2:124\n1#2:150\n1#2:181\n1#2:205\n1569#3,11:127\n1864#3,2:138\n1866#3:151\n1580#3:152\n766#3:155\n857#3,2:156\n1549#3:158\n1620#3,3:159\n1569#3,11:164\n1864#3,2:175\n1866#3:182\n1580#3:183\n1569#3,11:192\n1864#3,2:203\n1866#3:206\n1580#3:207\n151#4,6:144\n151#4,6:186\n*S KotlinDebug\n*F\n+ 1 Indent.kt\nkotlin/text/StringsKt__IndentKt\n*L\n38#1:125,2\n38#1:140,4\n38#1:153,2\n78#1:162,2\n78#1:177,4\n78#1:184,2\n38#1:150\n78#1:181\n114#1:205\n38#1:127,11\n38#1:138,2\n38#1:151\n38#1:152\n74#1:155\n74#1:156,2\n75#1:158\n75#1:159,3\n78#1:164,11\n78#1:175,2\n78#1:182\n78#1:183\n114#1:192,11\n114#1:203,2\n114#1:206\n114#1:207\n39#1:144,6\n101#1:186,6\n*E\n"})
public class StringsKt__IndentKt
extends StringsKt__AppendableKt {
    @IntrinsicConstEvaluation
    @NotNull
    public static final String trimMargin(@NotNull String string, @NotNull String string2) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        return StringsKt.replaceIndentByMargin(string, "", string2);
    }

    public static /* synthetic */ String trimMargin$default(String string, String string2, int n2, Object object) {
        if ((n2 & 1) != 0) {
            string2 = "|";
        }
        return StringsKt.trimMargin(string, string2);
    }

    @NotNull
    public static final String replaceIndentByMargin(@NotNull String string, @NotNull String object, @NotNull String string2) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(string2, "");
        boolean bl = !StringsKt.isBlank(string2);
        if (!bl) {
            String string3 = "marginPrefix must be non-blank string.";
            throw new IllegalArgumentException(string3.toString());
        }
        List<String> list2 = StringsKt.lines(string);
        Object object2 = list2;
        int n2 = string.length() + ((String)object).length() * list2.size();
        object = StringsKt__IndentKt.getIndentFunction$StringsKt__IndentKt((String)object);
        int n3 = CollectionsKt.getLastIndex(object2);
        object2 = (Iterable)object2;
        Collection collection = new ArrayList();
        int n4 = 0;
        object2 = object2.iterator();
        while (object2.hasNext()) {
            String string4;
            int n5;
            Object object3 = object2.next();
            if ((n5 = n4++) < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            int n6 = n5;
            n5 = n6;
            object3 = (String)object3;
            n5 = n6;
            if ((n6 == 0 || n5 == n3) && StringsKt.isBlank((CharSequence)object3)) {
                string4 = null;
            } else {
                String string5;
                String string6;
                int n7;
                int n8;
                CharSequence charSequence;
                Object object4;
                block9: {
                    object4 = object3;
                    charSequence = (CharSequence)object4;
                    int n9 = charSequence.length();
                    for (n8 = 0; n8 < n9; ++n8) {
                        char c2 = charSequence.charAt(n8);
                        if (!(!CharsKt.isWhitespace(c2))) continue;
                        n7 = n8;
                        break block9;
                    }
                    n7 = n8 = -1;
                }
                if (n7 != -1 && StringsKt.startsWith$default(object4, string2, n8, false, 4, null)) {
                    charSequence = object4;
                    int n10 = n8 + string2.length();
                    Intrinsics.checkNotNull(charSequence);
                    String string7 = ((String)charSequence).substring(n10);
                    string6 = string7;
                    Intrinsics.checkNotNullExpressionValue(string7, "");
                } else {
                    string6 = string4 = null;
                }
                if (string6 == null || (string4 = (String)object.invoke(string5 = string4)) == null) {
                    string4 = (String)object3;
                }
            }
            if (string4 == null) continue;
            object3 = string4;
            object3 = string4;
            collection.add(object3);
        }
        String string8 = ((StringBuilder)CollectionsKt.joinTo$default((List)collection, new StringBuilder(n2), "\n", null, null, 0, null, null, 124, null)).toString();
        Intrinsics.checkNotNullExpressionValue(string8, "");
        return string8;
    }

    public static /* synthetic */ String replaceIndentByMargin$default(String string, String string2, String string3, int n2, Object object) {
        if ((n2 & 1) != 0) {
            string2 = "";
        }
        if ((n2 & 2) != 0) {
            string3 = "|";
        }
        return StringsKt.replaceIndentByMargin(string, string2, string3);
    }

    @IntrinsicConstEvaluation
    @NotNull
    public static final String trimIndent(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        return StringsKt.replaceIndent(string, "");
    }

    @NotNull
    public static final String replaceIndent(@NotNull String object, @NotNull String iterator2) {
        Object object2;
        Object object3;
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(iterator2, "");
        List<String> list2 = StringsKt.lines((CharSequence)object);
        Iterable iterable = list2;
        Iterator iterator3 = iterable;
        Collection collection = new ArrayList();
        iterator3 = iterator3.iterator();
        while (iterator3.hasNext()) {
            object3 = iterator3.next();
            object2 = (String)object3;
            if (!(!StringsKt.isBlank((CharSequence)object2))) continue;
            collection.add(object3);
        }
        iterable = (List)collection;
        iterator3 = iterable;
        collection = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        iterator3 = iterator3.iterator();
        while (iterator3.hasNext()) {
            object3 = iterator3.next();
            object2 = (String)object3;
            object3 = collection;
            object3.add(StringsKt__IndentKt.indentWidth$StringsKt__IndentKt((String)object2));
        }
        Integer n2 = (Integer)CollectionsKt.minOrNull((List)collection);
        int n3 = n2 != null ? n2 : 0;
        object3 = list2;
        int n4 = ((String)object).length() + ((String)((Object)iterator2)).length() * list2.size();
        object = StringsKt__IndentKt.getIndentFunction$StringsKt__IndentKt((String)((Object)iterator2));
        int n5 = CollectionsKt.getLastIndex(object3);
        iterator2 = (Iterable)object3;
        object3 = iterator2;
        object2 = new ArrayList();
        iterator2 = object3;
        int n6 = 0;
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            Object object4;
            int n7;
            object3 = iterator2.next();
            if ((n7 = n6++) < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            int n8 = n7;
            n7 = n8;
            object3 = (String)object3;
            n7 = n8;
            if ((n8 == 0 || n7 == n5) && StringsKt.isBlank((CharSequence)object3)) {
                object4 = null;
            } else {
                Object object5 = object3;
                object4 = StringsKt.drop(object5, n3);
                if (object4 == null || (object4 = (String)object.invoke(object5 = object4)) == null) {
                    object4 = object3;
                }
            }
            if (object4 == null) continue;
            object3 = object4;
            object3 = object4;
            object2.add(object3);
        }
        String string = ((StringBuilder)CollectionsKt.joinTo$default((List)object2, new StringBuilder(n4), "\n", null, null, 0, null, null, 124, null)).toString();
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    public static /* synthetic */ String replaceIndent$default(String string, String string2, int n2, Object object) {
        if ((n2 & 1) != 0) {
            string2 = "";
        }
        return StringsKt.replaceIndent(string, string2);
    }

    @NotNull
    public static final String prependIndent(@NotNull String string, @NotNull String string2) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        return SequencesKt.joinToString$default(SequencesKt.map(StringsKt.lineSequence(string), (Function1)new Function1<String, String>(string2){
            final /* synthetic */ String $indent;
            {
                this.$indent = string;
                super(1);
            }

            @NotNull
            public final String invoke(@NotNull String string) {
                Intrinsics.checkNotNullParameter(string, "");
                if (StringsKt.isBlank(string)) {
                    if (string.length() < this.$indent.length()) {
                        return this.$indent;
                    }
                    return string;
                }
                return this.$indent + string;
            }
        }), "\n", null, null, 0, null, null, 62, null);
    }

    public static /* synthetic */ String prependIndent$default(String string, String string2, int n2, Object object) {
        if ((n2 & 1) != 0) {
            string2 = "    ";
        }
        return StringsKt.prependIndent(string, string2);
    }

    private static final int indentWidth$StringsKt__IndentKt(String string) {
        int n2;
        block2: {
            CharSequence charSequence = string;
            int n3 = charSequence.length();
            for (int i2 = 0; i2 < n3; ++i2) {
                char c2 = charSequence.charAt(i2);
                if (!(!CharsKt.isWhitespace(c2))) continue;
                n2 = i2;
                break block2;
            }
            n2 = -1;
        }
        int n4 = n2;
        n4 = n2;
        if (n2 == -1) {
            return string.length();
        }
        return n4;
    }

    private static final Function1<String, String> getIndentFunction$StringsKt__IndentKt(String string) {
        if (((CharSequence)string).length() == 0) {
            return getIndentFunction.1.INSTANCE;
        }
        return new Function1<String, String>(string){
            final /* synthetic */ String $indent;
            {
                this.$indent = string;
                super(1);
            }

            @NotNull
            public final String invoke(@NotNull String string) {
                Intrinsics.checkNotNullParameter(string, "");
                return this.$indent + string;
            }
        };
    }

    private static final String reindent$StringsKt__IndentKt(List<String> object, int n2, Function1<? super String, String> function1, Function1<? super String, String> function12) {
        int n3 = CollectionsKt.getLastIndex(object);
        object = (Iterable)object;
        Collection collection = new ArrayList();
        int n4 = 0;
        object = object.iterator();
        while (object.hasNext()) {
            String string;
            int n5;
            Object object2 = object.next();
            if ((n5 = n4++) < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            int n6 = n5;
            n5 = n6;
            object2 = (String)object2;
            n5 = n6;
            if ((n6 == 0 || n5 == n3) && StringsKt.isBlank((CharSequence)object2)) {
                string = null;
            } else {
                String string2;
                string = function12.invoke((String)object2);
                if (string == null || (string = function1.invoke(string2 = string)) == null) {
                    string = object2;
                }
            }
            if (string == null) continue;
            object2 = string;
            object2 = string;
            collection.add(object2);
        }
        String string = ((StringBuilder)CollectionsKt.joinTo$default((List)collection, new StringBuilder(n2), "\n", null, null, 0, null, null, 124, null)).toString();
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }
}

