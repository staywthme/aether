/*
 * Decompiled with CFR 0.152.
 */
package kotlin.text;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Matcher;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.text.FlagEnum;
import kotlin.text.MatchResult;
import kotlin.text.MatcherMatchResult;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000>\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0014\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0082\b\u001a\u001e\u0010\u0007\u001a\u0004\u0018\u00010\b*\u00020\t2\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0002\u001a\u0016\u0010\r\u001a\u0004\u0018\u00010\b*\u00020\t2\u0006\u0010\u000b\u001a\u00020\fH\u0002\u001a\f\u0010\u000e\u001a\u00020\u000f*\u00020\u0010H\u0002\u001a\u0014\u0010\u000e\u001a\u00020\u000f*\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0006H\u0002\u001a\u0012\u0010\u0012\u001a\u00020\u0006*\b\u0012\u0004\u0012\u00020\u00030\u0013H\u0002\u00a8\u0006\u0014"}, d2={"fromInt", "", "T", "Lkotlin/text/FlagEnum;", "", "value", "", "findNext", "Lkotlin/text/MatchResult;", "Ljava/util/regex/Matcher;", "from", "input", "", "matchEntire", "range", "Lkotlin/ranges/IntRange;", "Ljava/util/regex/MatchResult;", "groupIndex", "toInt", "", "kotlin-stdlib"})
@SourceDebugExtension(value={"SMAP\nRegex.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Regex.kt\nkotlin/text/RegexKt\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,398:1\n1789#2,3:399\n*S KotlinDebug\n*F\n+ 1 Regex.kt\nkotlin/text/RegexKt\n*L\n19#1:399,3\n*E\n"})
public final class RegexKt {
    private static final int toInt(Iterable<? extends FlagEnum> object) {
        int n2 = 0;
        object = object.iterator();
        while (object.hasNext()) {
            Object e2 = object.next();
            FlagEnum flagEnum = (FlagEnum)e2;
            int n3 = n2;
            n2 = n3 | flagEnum.getValue();
        }
        return n2;
    }

    private static final /* synthetic */ <T extends Enum<T>> Set<T> fromInt(int n2) {
        EnumSet<Enum> enumSet;
        Intrinsics.reifiedOperationMarker(4, "T");
        EnumSet<Enum> enumSet2 = enumSet = EnumSet.allOf(Enum.class);
        Intrinsics.checkNotNullExpressionValue(enumSet, "");
        Iterable iterable = enumSet2;
        Intrinsics.needClassReification();
        CollectionsKt.retainAll(iterable, (Function1)new Function1<T, Boolean>(n2){
            final /* synthetic */ int $value;
            {
                this.$value = n2;
                super(1);
            }

            @NotNull
            public final Boolean invoke(T t2) {
                return (this.$value & ((FlagEnum)t2).getMask()) == ((FlagEnum)t2).getValue();
            }
        });
        Set set = Collections.unmodifiableSet((Set)enumSet);
        Intrinsics.checkNotNullExpressionValue(set, "");
        return set;
    }

    private static final MatchResult findNext(Matcher matcher, int n2, CharSequence charSequence) {
        if (!matcher.find(n2)) {
            return null;
        }
        return new MatcherMatchResult(matcher, charSequence);
    }

    private static final MatchResult matchEntire(Matcher matcher, CharSequence charSequence) {
        if (!matcher.matches()) {
            return null;
        }
        return new MatcherMatchResult(matcher, charSequence);
    }

    private static final IntRange range(java.util.regex.MatchResult matchResult) {
        return RangesKt.until(matchResult.start(), matchResult.end());
    }

    private static final IntRange range(java.util.regex.MatchResult matchResult, int n2) {
        return RangesKt.until(matchResult.start(n2), matchResult.end(n2));
    }

    public static final /* synthetic */ int access$toInt(Iterable iterable) {
        return RegexKt.toInt(iterable);
    }

    public static final /* synthetic */ MatchResult access$findNext(Matcher matcher, int n2, CharSequence charSequence) {
        return RegexKt.findNext(matcher, n2, charSequence);
    }

    public static final /* synthetic */ MatchResult access$matchEntire(Matcher matcher, CharSequence charSequence) {
        return RegexKt.matchEntire(matcher, charSequence);
    }

    public static final /* synthetic */ IntRange access$range(java.util.regex.MatchResult matchResult) {
        return RegexKt.range(matchResult);
    }

    public static final /* synthetic */ IntRange access$range(java.util.regex.MatchResult matchResult, int n2) {
        return RegexKt.range(matchResult, n2);
    }
}

