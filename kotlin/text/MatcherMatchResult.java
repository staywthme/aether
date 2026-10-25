/*
 * Decompiled with CFR 0.152.
 */
package kotlin.text;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import kotlin.Metadata;
import kotlin.collections.AbstractList;
import kotlin.collections.CollectionsKt;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.sequences.SequencesKt;
import kotlin.text.MatchGroup;
import kotlin.text.MatchGroupCollection;
import kotlin.text.MatchNamedGroupCollection;
import kotlin.text.MatchResult;
import kotlin.text.MatcherMatchResult;
import kotlin.text.RegexKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\n\u0010\u001c\u001a\u0004\u0018\u00010\u0001H\u0016R\u001a\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\f\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u00020\u000eX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\u00020\u00128BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\u00020\u00168VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018R\u0014\u0010\u0019\u001a\u00020\t8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001a\u0010\u001b\u00a8\u0006\u001d"}, d2={"Lkotlin/text/MatcherMatchResult;", "Lkotlin/text/MatchResult;", "matcher", "Ljava/util/regex/Matcher;", "input", "", "(Ljava/util/regex/Matcher;Ljava/lang/CharSequence;)V", "groupValues", "", "", "getGroupValues", "()Ljava/util/List;", "groupValues_", "groups", "Lkotlin/text/MatchGroupCollection;", "getGroups", "()Lkotlin/text/MatchGroupCollection;", "matchResult", "Ljava/util/regex/MatchResult;", "getMatchResult", "()Ljava/util/regex/MatchResult;", "range", "Lkotlin/ranges/IntRange;", "getRange", "()Lkotlin/ranges/IntRange;", "value", "getValue", "()Ljava/lang/String;", "next", "kotlin-stdlib"})
final class MatcherMatchResult
implements MatchResult {
    @NotNull
    private final Matcher matcher;
    @NotNull
    private final CharSequence input;
    @NotNull
    private final MatchGroupCollection groups;
    @Nullable
    private List<String> groupValues_;

    public MatcherMatchResult(@NotNull Matcher matcher, @NotNull CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(matcher, "");
        Intrinsics.checkNotNullParameter(charSequence, "");
        this.matcher = matcher;
        this.input = charSequence;
        this.groups = new MatchNamedGroupCollection(this){
            final /* synthetic */ MatcherMatchResult this$0;
            {
                this.this$0 = matcherMatchResult;
            }

            public final int getSize() {
                return MatcherMatchResult.access$getMatchResult(this.this$0).groupCount() + 1;
            }

            public final boolean isEmpty() {
                return false;
            }

            @NotNull
            public final Iterator<MatchGroup> iterator() {
                return SequencesKt.map(CollectionsKt.asSequence(CollectionsKt.getIndices(this)), (Function1)new Function1<Integer, MatchGroup>(this){
                    final /* synthetic */ groups.1 this$0;
                    {
                        this.this$0 = var1_1;
                        super(1);
                    }

                    @Nullable
                    public final MatchGroup invoke(int n2) {
                        return this.this$0.get(n2);
                    }

                    public final /* synthetic */ Object invoke(Object object) {
                        return this.invoke(((Number)object).intValue());
                    }
                }).iterator();
            }

            @Nullable
            public final MatchGroup get(int n2) {
                IntRange intRange = RegexKt.access$range(MatcherMatchResult.access$getMatchResult(this.this$0), n2);
                if (intRange.getStart() >= 0) {
                    String string = MatcherMatchResult.access$getMatchResult(this.this$0).group(n2);
                    Intrinsics.checkNotNullExpressionValue(string, "");
                    return new MatchGroup(string, intRange);
                }
                return null;
            }

            @Nullable
            public final MatchGroup get(@NotNull String string) {
                Intrinsics.checkNotNullParameter(string, "");
                return PlatformImplementationsKt.IMPLEMENTATIONS.getMatchResultNamedGroup(MatcherMatchResult.access$getMatchResult(this.this$0), string);
            }
        };
    }

    private final java.util.regex.MatchResult getMatchResult() {
        return this.matcher;
    }

    @Override
    @NotNull
    public final IntRange getRange() {
        return RegexKt.access$range(this.getMatchResult());
    }

    @Override
    @NotNull
    public final String getValue() {
        String string = this.getMatchResult().group();
        Intrinsics.checkNotNullExpressionValue(string, "");
        return string;
    }

    @Override
    @NotNull
    public final MatchGroupCollection getGroups() {
        return this.groups;
    }

    @Override
    @NotNull
    public final List<String> getGroupValues() {
        if (this.groupValues_ == null) {
            this.groupValues_ = new AbstractList<String>(this){
                final /* synthetic */ MatcherMatchResult this$0;
                {
                    this.this$0 = matcherMatchResult;
                }

                public final int getSize() {
                    return MatcherMatchResult.access$getMatchResult(this.this$0).groupCount() + 1;
                }

                @NotNull
                public final String get(int n2) {
                    String string = MatcherMatchResult.access$getMatchResult(this.this$0).group(n2);
                    if (string == null) {
                        string = "";
                    }
                    return string;
                }
            };
        }
        List<String> list2 = this.groupValues_;
        Intrinsics.checkNotNull(list2);
        return list2;
    }

    @Override
    @Nullable
    public final MatchResult next() {
        int n2 = this.getMatchResult().end() + (this.getMatchResult().end() == this.getMatchResult().start() ? 1 : 0);
        if (n2 <= this.input.length()) {
            Matcher matcher = this.matcher.pattern().matcher(this.input);
            Intrinsics.checkNotNullExpressionValue(matcher, "");
            return RegexKt.access$findNext(matcher, n2, this.input);
        }
        return null;
    }

    @Override
    @NotNull
    public final MatchResult.Destructured getDestructured() {
        return MatchResult.DefaultImpls.getDestructured(this);
    }

    public static final /* synthetic */ java.util.regex.MatchResult access$getMatchResult(MatcherMatchResult matcherMatchResult) {
        return matcherMatchResult.getMatchResult();
    }
}

