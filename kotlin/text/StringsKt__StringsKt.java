/*
 * Decompiled with CFR 0.152.
 */
package kotlin.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Deprecated;
import kotlin.ExperimentalStdlibApi;
import kotlin.Metadata;
import kotlin.OverloadResolutionByLambdaReturnType;
import kotlin.Pair;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.TuplesKt;
import kotlin.WasExperimental;
import kotlin.collections.ArraysKt;
import kotlin.collections.CharIterator;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.CharsKt;
import kotlin.text.DelimitedRangesSequence;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import kotlin.text.StringsKt__StringsJVMKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000\u0084\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\r\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0019\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\b\n\u0002\u0010\u0011\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b!\u001a\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0006H\u0000\u001a\u001c\u0010\f\u001a\u00020\r*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001c\u0010\u0011\u001a\u00020\r*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001f\u0010\u0012\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0086\u0002\u001a\u001f\u0010\u0012\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0086\u0002\u001a\u0015\u0010\u0012\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0016H\u0087\n\u001a\u0018\u0010\u0017\u001a\u00020\u0010*\u0004\u0018\u00010\u00022\b\u0010\u000e\u001a\u0004\u0018\u00010\u0002H\u0000\u001a\u0018\u0010\u0018\u001a\u00020\u0010*\u0004\u0018\u00010\u00022\b\u0010\u000e\u001a\u0004\u0018\u00010\u0002H\u0000\u001a\u001c\u0010\u0019\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001c\u0010\u0019\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a:\u0010\u001b\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\r\u0018\u00010\u001c*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001aE\u0010\u001b\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\r\u0018\u00010\u001c*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010 \u001a\u00020\u0010H\u0002\u00a2\u0006\u0002\b!\u001a:\u0010\"\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\r\u0018\u00010\u001c*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u0012\u0010#\u001a\u00020\u0010*\u00020\u00022\u0006\u0010$\u001a\u00020\u0006\u001a7\u0010%\u001a\u0002H&\"\f\b\u0000\u0010'*\u00020\u0002*\u0002H&\"\u0004\b\u0001\u0010&*\u0002H'2\f\u0010(\u001a\b\u0012\u0004\u0012\u0002H&0)H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010*\u001a7\u0010+\u001a\u0002H&\"\f\b\u0000\u0010'*\u00020\u0002*\u0002H&\"\u0004\b\u0001\u0010&*\u0002H'2\f\u0010(\u001a\b\u0012\u0004\u0012\u0002H&0)H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010*\u001a&\u0010,\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a;\u0010,\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010 \u001a\u00020\u0010H\u0002\u00a2\u0006\u0002\b.\u001a&\u0010,\u001a\u00020\u0006*\u00020\u00022\u0006\u0010/\u001a\u00020\r2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a&\u00100\u001a\u00020\u0006*\u00020\u00022\u0006\u00101\u001a\u0002022\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a,\u00100\u001a\u00020\u0006*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\r\u00103\u001a\u00020\u0010*\u00020\u0002H\u0087\b\u001a\r\u00104\u001a\u00020\u0010*\u00020\u0002H\u0087\b\u001a\r\u00105\u001a\u00020\u0010*\u00020\u0002H\u0087\b\u001a \u00106\u001a\u00020\u0010*\u0004\u0018\u00010\u0002H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a \u00107\u001a\u00020\u0010*\u0004\u0018\u00010\u0002H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a\r\u00108\u001a\u000209*\u00020\u0002H\u0086\u0002\u001a&\u0010:\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a&\u0010:\u001a\u00020\u0006*\u00020\u00022\u0006\u0010/\u001a\u00020\r2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a&\u0010;\u001a\u00020\u0006*\u00020\u00022\u0006\u00101\u001a\u0002022\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a,\u0010;\u001a\u00020\u0006*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u0010\u0010<\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u0002\u001a\u0010\u0010>\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u0002\u001a\u0015\u0010@\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0016H\u0087\f\u001a\u000f\u0010A\u001a\u00020\r*\u0004\u0018\u00010\rH\u0087\b\u001a\u001c\u0010B\u001a\u00020\u0002*\u00020\u00022\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001a\u001c\u0010B\u001a\u00020\r*\u00020\r2\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001a\u001c\u0010E\u001a\u00020\u0002*\u00020\u00022\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001a\u001c\u0010E\u001a\u00020\r*\u00020\r2\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001aG\u0010F\u001a\b\u0012\u0004\u0012\u00020\u00010=*\u00020\u00022\u000e\u0010G\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0H2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\u0002\u00a2\u0006\u0004\bI\u0010J\u001a=\u0010F\u001a\b\u0012\u0004\u0012\u00020\u00010=*\u00020\u00022\u0006\u0010G\u001a\u0002022\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\u0002\u00a2\u0006\u0002\bI\u001a4\u0010K\u001a\u00020\u0010*\u00020\u00022\u0006\u0010L\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u00022\u0006\u0010M\u001a\u00020\u00062\u0006\u0010C\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0010H\u0000\u001a\u0012\u0010N\u001a\u00020\u0002*\u00020\u00022\u0006\u0010O\u001a\u00020\u0002\u001a\u0012\u0010N\u001a\u00020\r*\u00020\r2\u0006\u0010O\u001a\u00020\u0002\u001a\u001a\u0010P\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u0006\u001a\u0012\u0010P\u001a\u00020\u0002*\u00020\u00022\u0006\u0010Q\u001a\u00020\u0001\u001a\u001d\u0010P\u001a\u00020\r*\u00020\r2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u0006H\u0087\b\u001a\u0015\u0010P\u001a\u00020\r*\u00020\r2\u0006\u0010Q\u001a\u00020\u0001H\u0087\b\u001a\u0012\u0010R\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002\u001a\u0012\u0010R\u001a\u00020\r*\u00020\r2\u0006\u0010\u001a\u001a\u00020\u0002\u001a\u0012\u0010S\u001a\u00020\u0002*\u00020\u00022\u0006\u0010T\u001a\u00020\u0002\u001a\u001a\u0010S\u001a\u00020\u0002*\u00020\u00022\u0006\u0010O\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002\u001a\u0012\u0010S\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u0002\u001a\u001a\u0010S\u001a\u00020\r*\u00020\r2\u0006\u0010O\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002\u001a.\u0010U\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0014\b\b\u0010V\u001a\u000e\u0012\u0004\u0012\u00020X\u0012\u0004\u0012\u00020\u00020WH\u0087\b\u00f8\u0001\u0000\u001a\u001d\u0010U\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010Y\u001a\u00020\rH\u0087\b\u001a$\u0010Z\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010Z\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010\\\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010\\\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010]\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010]\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010^\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010^\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001d\u0010_\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010Y\u001a\u00020\rH\u0087\b\u001a)\u0010`\u001a\u00020\r*\u00020\r2\u0012\u0010V\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00140WH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\ba\u001a)\u0010`\u001a\u00020\r*\u00020\r2\u0012\u0010V\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00020WH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\bb\u001a\"\u0010c\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u00062\u0006\u0010Y\u001a\u00020\u0002\u001a\u001a\u0010c\u001a\u00020\u0002*\u00020\u00022\u0006\u0010Q\u001a\u00020\u00012\u0006\u0010Y\u001a\u00020\u0002\u001a%\u0010c\u001a\u00020\r*\u00020\r2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u00062\u0006\u0010Y\u001a\u00020\u0002H\u0087\b\u001a\u001d\u0010c\u001a\u00020\r*\u00020\r2\u0006\u0010Q\u001a\u00020\u00012\u0006\u0010Y\u001a\u00020\u0002H\u0087\b\u001a=\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\u0012\u0010G\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0H\"\u00020\r2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006\u00a2\u0006\u0002\u0010e\u001a0\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\n\u0010G\u001a\u000202\"\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006\u001a/\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\u0006\u0010T\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u000b\u001a\u00020\u0006H\u0002\u00a2\u0006\u0002\bf\u001a%\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\u0087\b\u001a=\u0010g\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u00022\u0012\u0010G\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0H\"\u00020\r2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006\u00a2\u0006\u0002\u0010h\u001a0\u0010g\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u00022\n\u0010G\u001a\u000202\"\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006\u001a%\u0010g\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\u0087\b\u001a\u001c\u0010i\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001c\u0010i\u001a\u00020\u0010*\u00020\u00022\u0006\u0010O\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a$\u0010i\u001a\u00020\u0010*\u00020\u00022\u0006\u0010O\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u0012\u0010j\u001a\u00020\u0002*\u00020\u00022\u0006\u0010Q\u001a\u00020\u0001\u001a\u001d\u0010j\u001a\u00020\u0002*\u00020\r2\u0006\u0010k\u001a\u00020\u00062\u0006\u0010l\u001a\u00020\u0006H\u0087\b\u001a\u001f\u0010m\u001a\u00020\r*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010-\u001a\u00020\u0006H\u0087\b\u001a\u0012\u0010m\u001a\u00020\r*\u00020\u00022\u0006\u0010Q\u001a\u00020\u0001\u001a\u0012\u0010m\u001a\u00020\r*\u00020\r2\u0006\u0010Q\u001a\u00020\u0001\u001a\u001c\u0010n\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010n\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010o\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010o\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010p\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010p\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010q\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010q\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\f\u0010r\u001a\u00020\u0010*\u00020\rH\u0007\u001a\u0013\u0010s\u001a\u0004\u0018\u00010\u0010*\u00020\rH\u0007\u00a2\u0006\u0002\u0010t\u001a\n\u0010u\u001a\u00020\u0002*\u00020\u0002\u001a$\u0010u\u001a\u00020\u0002*\u00020\u00022\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\b\u00f8\u0001\u0000\u001a\u0016\u0010u\u001a\u00020\u0002*\u00020\u00022\n\u00101\u001a\u000202\"\u00020\u0014\u001a\r\u0010u\u001a\u00020\r*\u00020\rH\u0087\b\u001a$\u0010u\u001a\u00020\r*\u00020\r2\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\b\u00f8\u0001\u0000\u001a\u0016\u0010u\u001a\u00020\r*\u00020\r2\n\u00101\u001a\u000202\"\u00020\u0014\u001a\n\u0010w\u001a\u00020\u0002*\u00020\u0002\u001a$\u0010w\u001a\u00020\u0002*\u00020\u00022\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\b\u00f8\u0001\u0000\u001a\u0016\u0010w\u001a\u00020\u0002*\u00020\u00022\n\u00101\u001a\u000202\"\u00020\u0014\u001a\r\u0010w\u001a\u00020\r*\u00020\rH\u0087\b\u001a$\u0010w\u001a\u00020\r*\u00020\r2\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\b\u00f8\u0001\u0000\u001a\u0016\u0010w\u001a\u00020\r*\u00020\r2\n\u00101\u001a\u000202\"\u00020\u0014\u001a\n\u0010x\u001a\u00020\u0002*\u00020\u0002\u001a$\u0010x\u001a\u00020\u0002*\u00020\u00022\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\b\u00f8\u0001\u0000\u001a\u0016\u0010x\u001a\u00020\u0002*\u00020\u00022\n\u00101\u001a\u000202\"\u00020\u0014\u001a\r\u0010x\u001a\u00020\r*\u00020\rH\u0087\b\u001a$\u0010x\u001a\u00020\r*\u00020\r2\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\b\u00f8\u0001\u0000\u001a\u0016\u0010x\u001a\u00020\r*\u00020\r2\n\u00101\u001a\u000202\"\u00020\u0014\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00028F\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\b\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006y"}, d2={"indices", "Lkotlin/ranges/IntRange;", "", "getIndices", "(Ljava/lang/CharSequence;)Lkotlin/ranges/IntRange;", "lastIndex", "", "getLastIndex", "(Ljava/lang/CharSequence;)I", "requireNonNegativeLimit", "", "limit", "commonPrefixWith", "", "other", "ignoreCase", "", "commonSuffixWith", "contains", "char", "", "regex", "Lkotlin/text/Regex;", "contentEqualsIgnoreCaseImpl", "contentEqualsImpl", "endsWith", "suffix", "findAnyOf", "Lkotlin/Pair;", "strings", "", "startIndex", "last", "findAnyOf$StringsKt__StringsKt", "findLastAnyOf", "hasSurrogatePairAt", "index", "ifBlank", "R", "C", "defaultValue", "Lkotlin/Function0;", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "ifEmpty", "indexOf", "endIndex", "indexOf$StringsKt__StringsKt", "string", "indexOfAny", "chars", "", "isEmpty", "isNotBlank", "isNotEmpty", "isNullOrBlank", "isNullOrEmpty", "iterator", "Lkotlin/collections/CharIterator;", "lastIndexOf", "lastIndexOfAny", "lineSequence", "Lkotlin/sequences/Sequence;", "lines", "", "matches", "orEmpty", "padEnd", "length", "padChar", "padStart", "rangesDelimitedBy", "delimiters", "", "rangesDelimitedBy$StringsKt__StringsKt", "(Ljava/lang/CharSequence;[Ljava/lang/String;IZI)Lkotlin/sequences/Sequence;", "regionMatchesImpl", "thisOffset", "otherOffset", "removePrefix", "prefix", "removeRange", "range", "removeSuffix", "removeSurrounding", "delimiter", "replace", "transform", "Lkotlin/Function1;", "Lkotlin/text/MatchResult;", "replacement", "replaceAfter", "missingDelimiterValue", "replaceAfterLast", "replaceBefore", "replaceBeforeLast", "replaceFirst", "replaceFirstChar", "replaceFirstCharWithChar", "replaceFirstCharWithCharSequence", "replaceRange", "split", "(Ljava/lang/CharSequence;[Ljava/lang/String;ZI)Ljava/util/List;", "split$StringsKt__StringsKt", "splitToSequence", "(Ljava/lang/CharSequence;[Ljava/lang/String;ZI)Lkotlin/sequences/Sequence;", "startsWith", "subSequence", "start", "end", "substring", "substringAfter", "substringAfterLast", "substringBefore", "substringBeforeLast", "toBooleanStrict", "toBooleanStrictOrNull", "(Ljava/lang/String;)Ljava/lang/Boolean;", "trim", "predicate", "trimEnd", "trimStart", "kotlin-stdlib"}, xs="kotlin/text/StringsKt")
@SourceDebugExtension(value={"SMAP\nStrings.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Strings.kt\nkotlin/text/StringsKt__StringsKt\n+ 2 _Arrays.kt\nkotlin/collections/ArraysKt___ArraysKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 4 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,1486:1\n79#1,22:1487\n113#1,5:1509\n130#1,5:1514\n79#1,22:1519\n107#1:1541\n79#1,22:1542\n113#1,5:1564\n124#1:1569\n113#1,5:1570\n130#1,5:1575\n141#1:1580\n130#1,5:1581\n79#1,22:1586\n113#1,5:1608\n130#1,5:1613\n12824#2,2:1618\n12824#2,2:1620\n288#3,2:1622\n288#3,2:1624\n1549#3:1627\n1620#3,3:1628\n1549#3:1631\n1620#3,3:1632\n1#4:1626\n*S KotlinDebug\n*F\n+ 1 Strings.kt\nkotlin/text/StringsKt__StringsKt\n*L\n107#1:1487,22\n124#1:1509,5\n141#1:1514,5\n146#1:1519,22\n151#1:1541\n151#1:1542,22\n156#1:1564,5\n161#1:1569\n161#1:1570,5\n166#1:1575,5\n171#1:1580\n171#1:1581,5\n176#1:1586,22\n187#1:1608,5\n198#1:1613,5\n940#1:1618,2\n964#1:1620,2\n1003#1:1622,2\n1009#1:1624,2\n1309#1:1627\n1309#1:1628,3\n1334#1:1631\n1334#1:1632,3\n*E\n"})
public class StringsKt__StringsKt
extends StringsKt__StringsJVMKt {
    @NotNull
    public static final CharSequence trim(@NotNull CharSequence charSequence, @NotNull Function1<? super Character, Boolean> function1) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(function1, "");
        int n2 = 0;
        int n3 = charSequence.length() - 1;
        boolean bl = false;
        while (n2 <= n3) {
            int n4 = !bl ? n2 : n3;
            n4 = function1.invoke(Character.valueOf(charSequence.charAt(n4))).booleanValue() ? 1 : 0;
            if (!bl) {
                if (n4 == 0) {
                    bl = true;
                    continue;
                }
                ++n2;
                continue;
            }
            if (n4 == 0) break;
            --n3;
        }
        return charSequence.subSequence(n2, n3 + 1);
    }

    @NotNull
    public static final String trim(@NotNull String charSequence, @NotNull Function1<? super Character, Boolean> function1) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(function1, "");
        charSequence = charSequence;
        int n2 = 0;
        int n3 = charSequence.length() - 1;
        boolean bl = false;
        while (n2 <= n3) {
            int n4 = !bl ? n2 : n3;
            n4 = function1.invoke(Character.valueOf(charSequence.charAt(n4))).booleanValue() ? 1 : 0;
            if (!bl) {
                if (n4 == 0) {
                    bl = true;
                    continue;
                }
                ++n2;
                continue;
            }
            if (n4 == 0) break;
            --n3;
        }
        return ((Object)charSequence.subSequence(n2, n3 + 1)).toString();
    }

    @NotNull
    public static final CharSequence trimStart(@NotNull CharSequence charSequence, @NotNull Function1<? super Character, Boolean> function1) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(function1, "");
        int n2 = charSequence.length();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (function1.invoke(Character.valueOf(charSequence.charAt(i2))).booleanValue()) continue;
            return charSequence.subSequence(i2, charSequence.length());
        }
        return "";
    }

    @NotNull
    public static final String trimStart(@NotNull String charSequence, @NotNull Function1<? super Character, Boolean> function1) {
        CharSequence charSequence2;
        block1: {
            Intrinsics.checkNotNullParameter(charSequence, "");
            Intrinsics.checkNotNullParameter(function1, "");
            charSequence = charSequence;
            int n2 = charSequence.length();
            for (int i2 = 0; i2 < n2; ++i2) {
                if (function1.invoke(Character.valueOf(charSequence.charAt(i2))).booleanValue()) continue;
                charSequence2 = charSequence.subSequence(i2, charSequence.length());
                break block1;
            }
            charSequence2 = "";
        }
        return ((Object)charSequence2).toString();
    }

    @NotNull
    public static final CharSequence trimEnd(@NotNull CharSequence charSequence, @NotNull Function1<? super Character, Boolean> function1) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(function1, "");
        int n2 = charSequence.length() + -1;
        if (n2 >= 0) {
            do {
                int n3;
                if (function1.invoke(Character.valueOf(charSequence.charAt(n3 = n2--))).booleanValue()) continue;
                return charSequence.subSequence(0, n3 + 1);
            } while (n2 >= 0);
        }
        return "";
    }

    @NotNull
    public static final String trimEnd(@NotNull String charSequence, @NotNull Function1<? super Character, Boolean> function1) {
        CharSequence charSequence2;
        block2: {
            Intrinsics.checkNotNullParameter(charSequence, "");
            Intrinsics.checkNotNullParameter(function1, "");
            charSequence = charSequence;
            int n2 = charSequence.length() + -1;
            if (n2 >= 0) {
                do {
                    int n3;
                    if (function1.invoke(Character.valueOf(charSequence.charAt(n3 = n2--))).booleanValue()) continue;
                    charSequence2 = charSequence.subSequence(0, n3 + 1);
                    break block2;
                } while (n2 >= 0);
            }
            charSequence2 = "";
        }
        return ((Object)charSequence2).toString();
    }

    @NotNull
    public static final CharSequence trim(@NotNull CharSequence charSequence, char ... cArray) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(cArray, "");
        int n2 = 0;
        int n3 = charSequence.length() - 1;
        boolean bl = false;
        while (n2 <= n3) {
            int n4 = !bl ? n2 : n3;
            n4 = charSequence.charAt(n4);
            n4 = ArraysKt.contains(cArray, (char)n4) ? 1 : 0;
            if (!bl) {
                if (n4 == 0) {
                    bl = true;
                    continue;
                }
                ++n2;
                continue;
            }
            if (n4 == 0) break;
            --n3;
        }
        return charSequence.subSequence(n2, n3 + 1);
    }

    @NotNull
    public static final String trim(@NotNull String charSequence, char ... cArray) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(cArray, "");
        charSequence = charSequence;
        int n2 = 0;
        int n3 = charSequence.length() - 1;
        boolean bl = false;
        while (n2 <= n3) {
            int n4 = !bl ? n2 : n3;
            n4 = charSequence.charAt(n4);
            n4 = ArraysKt.contains(cArray, (char)n4) ? 1 : 0;
            if (!bl) {
                if (n4 == 0) {
                    bl = true;
                    continue;
                }
                ++n2;
                continue;
            }
            if (n4 == 0) break;
            --n3;
        }
        return ((Object)charSequence.subSequence(n2, n3 + 1)).toString();
    }

    @NotNull
    public static final CharSequence trimStart(@NotNull CharSequence charSequence, char ... cArray) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(cArray, "");
        int n2 = charSequence.length();
        for (int i2 = 0; i2 < n2; ++i2) {
            char c2 = charSequence.charAt(i2);
            if (ArraysKt.contains(cArray, c2)) continue;
            return charSequence.subSequence(i2, charSequence.length());
        }
        return "";
    }

    @NotNull
    public static final String trimStart(@NotNull String charSequence, char ... cArray) {
        CharSequence charSequence2;
        block1: {
            Intrinsics.checkNotNullParameter(charSequence, "");
            Intrinsics.checkNotNullParameter(cArray, "");
            charSequence = charSequence;
            int n2 = charSequence.length();
            for (int i2 = 0; i2 < n2; ++i2) {
                char c2 = charSequence.charAt(i2);
                if (ArraysKt.contains(cArray, c2)) continue;
                charSequence2 = charSequence.subSequence(i2, charSequence.length());
                break block1;
            }
            charSequence2 = "";
        }
        return ((Object)charSequence2).toString();
    }

    @NotNull
    public static final CharSequence trimEnd(@NotNull CharSequence charSequence, char ... cArray) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(cArray, "");
        int n2 = charSequence.length() + -1;
        if (n2 >= 0) {
            do {
                int n3;
                char c2;
                if (ArraysKt.contains(cArray, c2 = charSequence.charAt(n3 = n2--))) continue;
                return charSequence.subSequence(0, n3 + 1);
            } while (n2 >= 0);
        }
        return "";
    }

    @NotNull
    public static final String trimEnd(@NotNull String charSequence, char ... cArray) {
        CharSequence charSequence2;
        block2: {
            Intrinsics.checkNotNullParameter(charSequence, "");
            Intrinsics.checkNotNullParameter(cArray, "");
            charSequence = charSequence;
            int n2 = charSequence.length() + -1;
            if (n2 >= 0) {
                do {
                    int n3;
                    char c2;
                    if (ArraysKt.contains(cArray, c2 = charSequence.charAt(n3 = n2--))) continue;
                    charSequence2 = charSequence.subSequence(0, n3 + 1);
                    break block2;
                } while (n2 >= 0);
            }
            charSequence2 = "";
        }
        return ((Object)charSequence2).toString();
    }

    @NotNull
    public static final CharSequence trim(@NotNull CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        int n2 = 0;
        int n3 = charSequence.length() - 1;
        boolean bl = false;
        while (n2 <= n3) {
            int n4 = !bl ? n2 : n3;
            char c2 = charSequence.charAt(n4);
            n4 = c2;
            n4 = CharsKt.isWhitespace(c2) ? 1 : 0;
            if (!bl) {
                if (n4 == 0) {
                    bl = true;
                    continue;
                }
                ++n2;
                continue;
            }
            if (n4 == 0) break;
            --n3;
        }
        return charSequence.subSequence(n2, n3 + 1);
    }

    @InlineOnly
    private static final String trim(String string) {
        Intrinsics.checkNotNullParameter(string, "");
        return ((Object)StringsKt.trim((CharSequence)string)).toString();
    }

    @NotNull
    public static final CharSequence trimStart(@NotNull CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        int n2 = charSequence.length();
        for (int i2 = 0; i2 < n2; ++i2) {
            char c2 = charSequence.charAt(i2);
            if (CharsKt.isWhitespace(c2)) continue;
            return charSequence.subSequence(i2, charSequence.length());
        }
        return "";
    }

    @InlineOnly
    private static final String trimStart(String string) {
        Intrinsics.checkNotNullParameter(string, "");
        return ((Object)StringsKt.trimStart((CharSequence)string)).toString();
    }

    @NotNull
    public static final CharSequence trimEnd(@NotNull CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        int n2 = charSequence.length() + -1;
        if (n2 >= 0) {
            do {
                int n3;
                char c2;
                if (CharsKt.isWhitespace(c2 = charSequence.charAt(n3 = n2--))) continue;
                return charSequence.subSequence(0, n3 + 1);
            } while (n2 >= 0);
        }
        return "";
    }

    @InlineOnly
    private static final String trimEnd(String string) {
        Intrinsics.checkNotNullParameter(string, "");
        return ((Object)StringsKt.trimEnd((CharSequence)string)).toString();
    }

    @NotNull
    public static final CharSequence padStart(@NotNull CharSequence charSequence, int n2, char c2) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        if (n2 < 0) {
            throw new IllegalArgumentException("Desired length " + n2 + " is less than zero.");
        }
        if (n2 <= charSequence.length()) {
            return charSequence.subSequence(0, charSequence.length());
        }
        StringBuilder stringBuilder = new StringBuilder(n2);
        IntIterator intIterator = new IntRange(1, n2 - charSequence.length()).iterator();
        while (intIterator.hasNext()) {
            intIterator.nextInt();
            stringBuilder.append(c2);
        }
        stringBuilder.append(charSequence);
        return stringBuilder;
    }

    public static /* synthetic */ CharSequence padStart$default(CharSequence charSequence, int n2, char c2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            c2 = (char)32;
        }
        return StringsKt.padStart(charSequence, n2, c2);
    }

    @NotNull
    public static final String padStart(@NotNull String string, int n2, char c2) {
        Intrinsics.checkNotNullParameter(string, "");
        return ((Object)StringsKt.padStart((CharSequence)string, n2, c2)).toString();
    }

    public static /* synthetic */ String padStart$default(String string, int n2, char c2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            c2 = (char)32;
        }
        return StringsKt.padStart(string, n2, c2);
    }

    @NotNull
    public static final CharSequence padEnd(@NotNull CharSequence object, int n2, char c2) {
        Intrinsics.checkNotNullParameter(object, "");
        if (n2 < 0) {
            throw new IllegalArgumentException("Desired length " + n2 + " is less than zero.");
        }
        if (n2 <= object.length()) {
            return object.subSequence(0, object.length());
        }
        StringBuilder stringBuilder = new StringBuilder(n2);
        stringBuilder.append((CharSequence)object);
        object = new IntRange(1, n2 - object.length()).iterator();
        while (object.hasNext()) {
            ((IntIterator)object).nextInt();
            stringBuilder.append(c2);
        }
        return stringBuilder;
    }

    public static /* synthetic */ CharSequence padEnd$default(CharSequence charSequence, int n2, char c2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            c2 = (char)32;
        }
        return StringsKt.padEnd(charSequence, n2, c2);
    }

    @NotNull
    public static final String padEnd(@NotNull String string, int n2, char c2) {
        Intrinsics.checkNotNullParameter(string, "");
        return ((Object)StringsKt.padEnd((CharSequence)string, n2, c2)).toString();
    }

    public static /* synthetic */ String padEnd$default(String string, int n2, char c2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            c2 = (char)32;
        }
        return StringsKt.padEnd(string, n2, c2);
    }

    @InlineOnly
    private static final boolean isNullOrEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    @InlineOnly
    private static final boolean isEmpty(CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        return charSequence.length() == 0;
    }

    @InlineOnly
    private static final boolean isNotEmpty(CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        return charSequence.length() > 0;
    }

    @InlineOnly
    private static final boolean isNotBlank(CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        return !StringsKt.isBlank(charSequence);
    }

    @InlineOnly
    private static final boolean isNullOrBlank(CharSequence charSequence) {
        return charSequence == null || StringsKt.isBlank(charSequence);
    }

    @NotNull
    public static final CharIterator iterator(@NotNull CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        return new CharIterator(charSequence){
            private int index;
            final /* synthetic */ CharSequence $this_iterator;
            {
                this.$this_iterator = charSequence;
            }

            public final char nextChar() {
                int n2 = this.index;
                this.index = n2 + 1;
                return this.$this_iterator.charAt(n2);
            }

            public final boolean hasNext() {
                return this.index < this.$this_iterator.length();
            }
        };
    }

    @InlineOnly
    private static final String orEmpty(String string) {
        String string2 = string;
        if (string2 == null) {
            string2 = "";
        }
        return string2;
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <C extends CharSequence & R, R> R ifEmpty(C c2, Function0<? extends R> function0) {
        Intrinsics.checkNotNullParameter(function0, "");
        if (c2.length() == 0) {
            return function0.invoke();
        }
        return c2;
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <C extends CharSequence & R, R> R ifBlank(C c2, Function0<? extends R> function0) {
        Intrinsics.checkNotNullParameter(function0, "");
        if (StringsKt.isBlank(c2)) {
            return function0.invoke();
        }
        return c2;
    }

    @NotNull
    public static final IntRange getIndices(@NotNull CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        return new IntRange(0, charSequence.length() - 1);
    }

    public static final int getLastIndex(@NotNull CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        return charSequence.length() - 1;
    }

    public static final boolean hasSurrogatePairAt(@NotNull CharSequence charSequence, int n2) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        return new IntRange(0, charSequence.length() - 2).contains(n2) && Character.isHighSurrogate(charSequence.charAt(n2)) && Character.isLowSurrogate(charSequence.charAt(n2 + 1));
    }

    @NotNull
    public static final String substring(@NotNull String string, @NotNull IntRange intRange) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(intRange, "");
        int n2 = intRange.getStart();
        int n3 = intRange.getEndInclusive() + 1;
        String string2 = string.substring(n2, n3);
        Intrinsics.checkNotNullExpressionValue(string2, "");
        return string2;
    }

    @NotNull
    public static final CharSequence subSequence(@NotNull CharSequence charSequence, @NotNull IntRange intRange) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(intRange, "");
        return charSequence.subSequence(intRange.getStart(), intRange.getEndInclusive() + 1);
    }

    @Deprecated(message="Use parameters named startIndex and endIndex.", replaceWith=@ReplaceWith(expression="subSequence(startIndex = start, endIndex = end)", imports={}))
    @InlineOnly
    private static final CharSequence subSequence(String string, int n2, int n3) {
        Intrinsics.checkNotNullParameter(string, "");
        return string.subSequence(n2, n3);
    }

    @InlineOnly
    private static final String substring(CharSequence charSequence, int n2, int n3) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        return ((Object)charSequence.subSequence(n2, n3)).toString();
    }

    static /* synthetic */ String substring$default(CharSequence charSequence, int n2, int n3, int n4, Object object) {
        if ((n4 & 2) != 0) {
            n3 = charSequence.length();
        }
        Intrinsics.checkNotNullParameter(charSequence, "");
        return ((Object)charSequence.subSequence(n2, n3)).toString();
    }

    @NotNull
    public static final String substring(@NotNull CharSequence charSequence, @NotNull IntRange intRange) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(intRange, "");
        return ((Object)charSequence.subSequence(intRange.getStart(), intRange.getEndInclusive() + 1)).toString();
    }

    @NotNull
    public static final String substringBefore(@NotNull String string, char c2, @NotNull String string2) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        int n2 = StringsKt.indexOf$default((CharSequence)string, c2, 0, false, 6, null);
        c2 = (char)n2;
        if (n2 == -1) {
            return string2;
        }
        String string3 = string.substring(0, c2);
        Intrinsics.checkNotNullExpressionValue(string3, "");
        return string3;
    }

    public static /* synthetic */ String substringBefore$default(String string, char c2, String string2, int n2, Object object) {
        if ((n2 & 2) != 0) {
            string2 = string;
        }
        return StringsKt.substringBefore(string, c2, string2);
    }

    @NotNull
    public static final String substringBefore(@NotNull String string, @NotNull String string2, @NotNull String string3) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        Intrinsics.checkNotNullParameter(string3, "");
        int n2 = StringsKt.indexOf$default((CharSequence)string, string2, 0, false, 6, null);
        if (n2 == -1) {
            return string3;
        }
        String string4 = string.substring(0, n2);
        Intrinsics.checkNotNullExpressionValue(string4, "");
        return string4;
    }

    public static /* synthetic */ String substringBefore$default(String string, String string2, String string3, int n2, Object object) {
        if ((n2 & 2) != 0) {
            string3 = string;
        }
        return StringsKt.substringBefore(string, string2, string3);
    }

    @NotNull
    public static final String substringAfter(@NotNull String string, char c2, @NotNull String string2) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        int n2 = StringsKt.indexOf$default((CharSequence)string, c2, 0, false, 6, null);
        c2 = (char)n2;
        if (n2 == -1) {
            return string2;
        }
        string2 = string;
        c2 = (char)(c2 + '\u0001');
        int n3 = string.length();
        String string3 = string2.substring(c2, n3);
        Intrinsics.checkNotNullExpressionValue(string3, "");
        return string3;
    }

    public static /* synthetic */ String substringAfter$default(String string, char c2, String string2, int n2, Object object) {
        if ((n2 & 2) != 0) {
            string2 = string;
        }
        return StringsKt.substringAfter(string, c2, string2);
    }

    @NotNull
    public static final String substringAfter(@NotNull String string, @NotNull String string2, @NotNull String string3) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        Intrinsics.checkNotNullParameter(string3, "");
        int n2 = StringsKt.indexOf$default((CharSequence)string, string2, 0, false, 6, null);
        if (n2 == -1) {
            return string3;
        }
        string3 = string;
        int n3 = n2 + string2.length();
        int n4 = string.length();
        String string4 = string3.substring(n3, n4);
        Intrinsics.checkNotNullExpressionValue(string4, "");
        return string4;
    }

    public static /* synthetic */ String substringAfter$default(String string, String string2, String string3, int n2, Object object) {
        if ((n2 & 2) != 0) {
            string3 = string;
        }
        return StringsKt.substringAfter(string, string2, string3);
    }

    @NotNull
    public static final String substringBeforeLast(@NotNull String string, char c2, @NotNull String string2) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        int n2 = StringsKt.lastIndexOf$default((CharSequence)string, c2, 0, false, 6, null);
        c2 = (char)n2;
        if (n2 == -1) {
            return string2;
        }
        String string3 = string.substring(0, c2);
        Intrinsics.checkNotNullExpressionValue(string3, "");
        return string3;
    }

    public static /* synthetic */ String substringBeforeLast$default(String string, char c2, String string2, int n2, Object object) {
        if ((n2 & 2) != 0) {
            string2 = string;
        }
        return StringsKt.substringBeforeLast(string, c2, string2);
    }

    @NotNull
    public static final String substringBeforeLast(@NotNull String string, @NotNull String string2, @NotNull String string3) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        Intrinsics.checkNotNullParameter(string3, "");
        int n2 = StringsKt.lastIndexOf$default((CharSequence)string, string2, 0, false, 6, null);
        if (n2 == -1) {
            return string3;
        }
        String string4 = string.substring(0, n2);
        Intrinsics.checkNotNullExpressionValue(string4, "");
        return string4;
    }

    public static /* synthetic */ String substringBeforeLast$default(String string, String string2, String string3, int n2, Object object) {
        if ((n2 & 2) != 0) {
            string3 = string;
        }
        return StringsKt.substringBeforeLast(string, string2, string3);
    }

    @NotNull
    public static final String substringAfterLast(@NotNull String string, char c2, @NotNull String string2) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        int n2 = StringsKt.lastIndexOf$default((CharSequence)string, c2, 0, false, 6, null);
        c2 = (char)n2;
        if (n2 == -1) {
            return string2;
        }
        string2 = string;
        c2 = (char)(c2 + '\u0001');
        int n3 = string.length();
        String string3 = string2.substring(c2, n3);
        Intrinsics.checkNotNullExpressionValue(string3, "");
        return string3;
    }

    public static /* synthetic */ String substringAfterLast$default(String string, char c2, String string2, int n2, Object object) {
        if ((n2 & 2) != 0) {
            string2 = string;
        }
        return StringsKt.substringAfterLast(string, c2, string2);
    }

    @NotNull
    public static final String substringAfterLast(@NotNull String string, @NotNull String string2, @NotNull String string3) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        Intrinsics.checkNotNullParameter(string3, "");
        int n2 = StringsKt.lastIndexOf$default((CharSequence)string, string2, 0, false, 6, null);
        if (n2 == -1) {
            return string3;
        }
        string3 = string;
        int n3 = n2 + string2.length();
        int n4 = string.length();
        String string4 = string3.substring(n3, n4);
        Intrinsics.checkNotNullExpressionValue(string4, "");
        return string4;
    }

    public static /* synthetic */ String substringAfterLast$default(String string, String string2, String string3, int n2, Object object) {
        if ((n2 & 2) != 0) {
            string3 = string;
        }
        return StringsKt.substringAfterLast(string, string2, string3);
    }

    @NotNull
    public static final CharSequence replaceRange(@NotNull CharSequence charSequence, int n2, int n3, @NotNull CharSequence charSequence2) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(charSequence2, "");
        if (n3 < n2) {
            throw new IndexOutOfBoundsException("End index (" + n3 + ") is less than start index (" + n2 + ").");
        }
        StringBuilder stringBuilder = new StringBuilder();
        Intrinsics.checkNotNullExpressionValue(stringBuilder.append(charSequence, 0, n2), "");
        stringBuilder.append(charSequence2);
        Intrinsics.checkNotNullExpressionValue(stringBuilder.append(charSequence, n3, charSequence.length()), "");
        return stringBuilder;
    }

    @InlineOnly
    private static final String replaceRange(String string, int n2, int n3, CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(charSequence, "");
        return ((Object)StringsKt.replaceRange((CharSequence)string, n2, n3, charSequence)).toString();
    }

    @NotNull
    public static final CharSequence replaceRange(@NotNull CharSequence charSequence, @NotNull IntRange intRange, @NotNull CharSequence charSequence2) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(intRange, "");
        Intrinsics.checkNotNullParameter(charSequence2, "");
        return StringsKt.replaceRange(charSequence, (int)intRange.getStart(), intRange.getEndInclusive() + 1, charSequence2);
    }

    @InlineOnly
    private static final String replaceRange(String string, IntRange intRange, CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(intRange, "");
        Intrinsics.checkNotNullParameter(charSequence, "");
        return ((Object)StringsKt.replaceRange((CharSequence)string, intRange, charSequence)).toString();
    }

    @NotNull
    public static final CharSequence removeRange(@NotNull CharSequence charSequence, int n2, int n3) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        if (n3 < n2) {
            throw new IndexOutOfBoundsException("End index (" + n3 + ") is less than start index (" + n2 + ").");
        }
        if (n3 == n2) {
            return charSequence.subSequence(0, charSequence.length());
        }
        StringBuilder stringBuilder = new StringBuilder(charSequence.length() - (n3 - n2));
        Intrinsics.checkNotNullExpressionValue(stringBuilder.append(charSequence, 0, n2), "");
        Intrinsics.checkNotNullExpressionValue(stringBuilder.append(charSequence, n3, charSequence.length()), "");
        return stringBuilder;
    }

    @InlineOnly
    private static final String removeRange(String string, int n2, int n3) {
        Intrinsics.checkNotNullParameter(string, "");
        return ((Object)StringsKt.removeRange((CharSequence)string, n2, n3)).toString();
    }

    @NotNull
    public static final CharSequence removeRange(@NotNull CharSequence charSequence, @NotNull IntRange intRange) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(intRange, "");
        return StringsKt.removeRange(charSequence, (int)intRange.getStart(), intRange.getEndInclusive() + 1);
    }

    @InlineOnly
    private static final String removeRange(String string, IntRange intRange) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(intRange, "");
        return ((Object)StringsKt.removeRange((CharSequence)string, intRange)).toString();
    }

    @NotNull
    public static final CharSequence removePrefix(@NotNull CharSequence charSequence, @NotNull CharSequence charSequence2) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(charSequence2, "");
        if (StringsKt.startsWith$default(charSequence, charSequence2, false, 2, null)) {
            return charSequence.subSequence(charSequence2.length(), charSequence.length());
        }
        return charSequence.subSequence(0, charSequence.length());
    }

    @NotNull
    public static final String removePrefix(@NotNull String string, @NotNull CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(charSequence, "");
        if (StringsKt.startsWith$default((CharSequence)string, charSequence, false, 2, null)) {
            int n2 = charSequence.length();
            String string2 = string.substring(n2);
            Intrinsics.checkNotNullExpressionValue(string2, "");
            return string2;
        }
        return string;
    }

    @NotNull
    public static final CharSequence removeSuffix(@NotNull CharSequence charSequence, @NotNull CharSequence charSequence2) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(charSequence2, "");
        if (StringsKt.endsWith$default(charSequence, charSequence2, false, 2, null)) {
            return charSequence.subSequence(0, charSequence.length() - charSequence2.length());
        }
        return charSequence.subSequence(0, charSequence.length());
    }

    @NotNull
    public static final String removeSuffix(@NotNull String string, @NotNull CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(charSequence, "");
        if (StringsKt.endsWith$default((CharSequence)string, charSequence, false, 2, null)) {
            String string2 = string;
            int n2 = string.length() - charSequence.length();
            String string3 = string2.substring(0, n2);
            Intrinsics.checkNotNullExpressionValue(string3, "");
            return string3;
        }
        return string;
    }

    @NotNull
    public static final CharSequence removeSurrounding(@NotNull CharSequence charSequence, @NotNull CharSequence charSequence2, @NotNull CharSequence charSequence3) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(charSequence2, "");
        Intrinsics.checkNotNullParameter(charSequence3, "");
        if (charSequence.length() >= charSequence2.length() + charSequence3.length() && StringsKt.startsWith$default(charSequence, charSequence2, false, 2, null) && StringsKt.endsWith$default(charSequence, charSequence3, false, 2, null)) {
            return charSequence.subSequence(charSequence2.length(), charSequence.length() - charSequence3.length());
        }
        return charSequence.subSequence(0, charSequence.length());
    }

    @NotNull
    public static final String removeSurrounding(@NotNull String string, @NotNull CharSequence charSequence, @NotNull CharSequence charSequence2) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(charSequence2, "");
        if (string.length() >= charSequence.length() + charSequence2.length() && StringsKt.startsWith$default((CharSequence)string, charSequence, false, 2, null) && StringsKt.endsWith$default((CharSequence)string, charSequence2, false, 2, null)) {
            String string2 = string;
            int n2 = charSequence.length();
            int n3 = string.length() - charSequence2.length();
            String string3 = string2.substring(n2, n3);
            Intrinsics.checkNotNullExpressionValue(string3, "");
            return string3;
        }
        return string;
    }

    @NotNull
    public static final CharSequence removeSurrounding(@NotNull CharSequence charSequence, @NotNull CharSequence charSequence2) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(charSequence2, "");
        CharSequence charSequence3 = charSequence2;
        return StringsKt.removeSurrounding(charSequence, charSequence3, charSequence3);
    }

    @NotNull
    public static final String removeSurrounding(@NotNull String string, @NotNull CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(charSequence, "");
        CharSequence charSequence2 = charSequence;
        return StringsKt.removeSurrounding(string, charSequence2, charSequence2);
    }

    @NotNull
    public static final String replaceBefore(@NotNull String string, char c2, @NotNull String string2, @NotNull String string3) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        Intrinsics.checkNotNullParameter(string3, "");
        int n2 = StringsKt.indexOf$default((CharSequence)string, c2, 0, false, 6, null);
        c2 = (char)n2;
        if (n2 == -1) {
            return string3;
        }
        return ((Object)StringsKt.replaceRange((CharSequence)string, 0, (int)c2, (CharSequence)string2)).toString();
    }

    public static /* synthetic */ String replaceBefore$default(String string, char c2, String string2, String string3, int n2, Object object) {
        if ((n2 & 4) != 0) {
            string3 = string;
        }
        return StringsKt.replaceBefore(string, c2, string2, string3);
    }

    @NotNull
    public static final String replaceBefore(@NotNull String string, @NotNull String string2, @NotNull String string3, @NotNull String string4) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        Intrinsics.checkNotNullParameter(string3, "");
        Intrinsics.checkNotNullParameter(string4, "");
        int n2 = StringsKt.indexOf$default((CharSequence)string, string2, 0, false, 6, null);
        if (n2 == -1) {
            return string4;
        }
        return ((Object)StringsKt.replaceRange((CharSequence)string, 0, n2, (CharSequence)string3)).toString();
    }

    public static /* synthetic */ String replaceBefore$default(String string, String string2, String string3, String string4, int n2, Object object) {
        if ((n2 & 4) != 0) {
            string4 = string;
        }
        return StringsKt.replaceBefore(string, string2, string3, string4);
    }

    @NotNull
    public static final String replaceAfter(@NotNull String string, char c2, @NotNull String string2, @NotNull String string3) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        Intrinsics.checkNotNullParameter(string3, "");
        int n2 = StringsKt.indexOf$default((CharSequence)string, c2, 0, false, 6, null);
        c2 = (char)n2;
        if (n2 == -1) {
            return string3;
        }
        string3 = string;
        c2 = (char)(c2 + '\u0001');
        int n3 = string.length();
        return ((Object)StringsKt.replaceRange((CharSequence)string3, (int)c2, n3, (CharSequence)string2)).toString();
    }

    public static /* synthetic */ String replaceAfter$default(String string, char c2, String string2, String string3, int n2, Object object) {
        if ((n2 & 4) != 0) {
            string3 = string;
        }
        return StringsKt.replaceAfter(string, c2, string2, string3);
    }

    @NotNull
    public static final String replaceAfter(@NotNull String string, @NotNull String string2, @NotNull String string3, @NotNull String string4) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        Intrinsics.checkNotNullParameter(string3, "");
        Intrinsics.checkNotNullParameter(string4, "");
        int n2 = StringsKt.indexOf$default((CharSequence)string, string2, 0, false, 6, null);
        if (n2 == -1) {
            return string4;
        }
        string4 = string;
        int n3 = n2 + string2.length();
        int n4 = string.length();
        return ((Object)StringsKt.replaceRange((CharSequence)string4, n3, n4, (CharSequence)string3)).toString();
    }

    public static /* synthetic */ String replaceAfter$default(String string, String string2, String string3, String string4, int n2, Object object) {
        if ((n2 & 4) != 0) {
            string4 = string;
        }
        return StringsKt.replaceAfter(string, string2, string3, string4);
    }

    @NotNull
    public static final String replaceAfterLast(@NotNull String string, @NotNull String string2, @NotNull String string3, @NotNull String string4) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        Intrinsics.checkNotNullParameter(string3, "");
        Intrinsics.checkNotNullParameter(string4, "");
        int n2 = StringsKt.lastIndexOf$default((CharSequence)string, string2, 0, false, 6, null);
        if (n2 == -1) {
            return string4;
        }
        string4 = string;
        int n3 = n2 + string2.length();
        int n4 = string.length();
        return ((Object)StringsKt.replaceRange((CharSequence)string4, n3, n4, (CharSequence)string3)).toString();
    }

    public static /* synthetic */ String replaceAfterLast$default(String string, String string2, String string3, String string4, int n2, Object object) {
        if ((n2 & 4) != 0) {
            string4 = string;
        }
        return StringsKt.replaceAfterLast(string, string2, string3, string4);
    }

    @NotNull
    public static final String replaceAfterLast(@NotNull String string, char c2, @NotNull String string2, @NotNull String string3) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        Intrinsics.checkNotNullParameter(string3, "");
        int n2 = StringsKt.lastIndexOf$default((CharSequence)string, c2, 0, false, 6, null);
        c2 = (char)n2;
        if (n2 == -1) {
            return string3;
        }
        string3 = string;
        c2 = (char)(c2 + '\u0001');
        int n3 = string.length();
        return ((Object)StringsKt.replaceRange((CharSequence)string3, (int)c2, n3, (CharSequence)string2)).toString();
    }

    public static /* synthetic */ String replaceAfterLast$default(String string, char c2, String string2, String string3, int n2, Object object) {
        if ((n2 & 4) != 0) {
            string3 = string;
        }
        return StringsKt.replaceAfterLast(string, c2, string2, string3);
    }

    @NotNull
    public static final String replaceBeforeLast(@NotNull String string, char c2, @NotNull String string2, @NotNull String string3) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        Intrinsics.checkNotNullParameter(string3, "");
        int n2 = StringsKt.lastIndexOf$default((CharSequence)string, c2, 0, false, 6, null);
        c2 = (char)n2;
        if (n2 == -1) {
            return string3;
        }
        return ((Object)StringsKt.replaceRange((CharSequence)string, 0, (int)c2, (CharSequence)string2)).toString();
    }

    public static /* synthetic */ String replaceBeforeLast$default(String string, char c2, String string2, String string3, int n2, Object object) {
        if ((n2 & 4) != 0) {
            string3 = string;
        }
        return StringsKt.replaceBeforeLast(string, c2, string2, string3);
    }

    @NotNull
    public static final String replaceBeforeLast(@NotNull String string, @NotNull String string2, @NotNull String string3, @NotNull String string4) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        Intrinsics.checkNotNullParameter(string3, "");
        Intrinsics.checkNotNullParameter(string4, "");
        int n2 = StringsKt.lastIndexOf$default((CharSequence)string, string2, 0, false, 6, null);
        if (n2 == -1) {
            return string4;
        }
        return ((Object)StringsKt.replaceRange((CharSequence)string, 0, n2, (CharSequence)string3)).toString();
    }

    public static /* synthetic */ String replaceBeforeLast$default(String string, String string2, String string3, String string4, int n2, Object object) {
        if ((n2 & 4) != 0) {
            string4 = string;
        }
        return StringsKt.replaceBeforeLast(string, string2, string3, string4);
    }

    @InlineOnly
    private static final String replace(CharSequence charSequence, Regex regex, String string) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(regex, "");
        Intrinsics.checkNotNullParameter(string, "");
        return regex.replace(charSequence, string);
    }

    @InlineOnly
    private static final String replace(CharSequence charSequence, Regex regex, Function1<? super MatchResult, ? extends CharSequence> function1) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(regex, "");
        Intrinsics.checkNotNullParameter(function1, "");
        return regex.replace(charSequence, function1);
    }

    @InlineOnly
    private static final String replaceFirst(CharSequence charSequence, Regex regex, String string) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(regex, "");
        Intrinsics.checkNotNullParameter(string, "");
        return regex.replaceFirst(charSequence, string);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @OverloadResolutionByLambdaReturnType
    @JvmName(name="replaceFirstCharWithChar")
    @InlineOnly
    private static final String replaceFirstCharWithChar(String string, Function1<? super Character, Character> function1) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(function1, "");
        if (((CharSequence)string).length() > 0) {
            char c2 = function1.invoke(Character.valueOf(string.charAt(0))).charValue();
            String string2 = string.substring(1);
            Intrinsics.checkNotNullExpressionValue(string2, "");
            string = string2;
            return c2 + string;
        }
        return string;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @OverloadResolutionByLambdaReturnType
    @JvmName(name="replaceFirstCharWithCharSequence")
    @InlineOnly
    private static final String replaceFirstCharWithCharSequence(String string, Function1<? super Character, ? extends CharSequence> function1) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(function1, "");
        if (((CharSequence)string).length() > 0) {
            StringBuilder stringBuilder = new StringBuilder().append((Object)function1.invoke(Character.valueOf(string.charAt(0))));
            String string2 = string.substring(1);
            Intrinsics.checkNotNullExpressionValue(string2, "");
            return stringBuilder.append(string2).toString();
        }
        return string;
    }

    @InlineOnly
    private static final boolean matches(CharSequence charSequence, Regex regex) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(regex, "");
        return regex.matches(charSequence);
    }

    public static final boolean regionMatchesImpl(@NotNull CharSequence charSequence, int n2, @NotNull CharSequence charSequence2, int n3, int n4, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(charSequence2, "");
        if (n3 < 0 || n2 < 0 || n2 > charSequence.length() - n4 || n3 > charSequence2.length() - n4) {
            return false;
        }
        for (int i2 = 0; i2 < n4; ++i2) {
            if (CharsKt.equals(charSequence.charAt(n2 + i2), charSequence2.charAt(n3 + i2), bl)) continue;
            return false;
        }
        return true;
    }

    public static final boolean startsWith(@NotNull CharSequence charSequence, char c2, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        return charSequence.length() > 0 && CharsKt.equals(charSequence.charAt(0), c2, bl);
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence charSequence, char c2, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return StringsKt.startsWith(charSequence, c2, bl);
    }

    public static final boolean endsWith(@NotNull CharSequence charSequence, char c2, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        if (charSequence.length() > 0) {
            CharSequence charSequence2 = charSequence;
            if (CharsKt.equals(charSequence2.charAt(StringsKt.getLastIndex(charSequence2)), c2, bl)) {
                return true;
            }
        }
        return false;
    }

    public static /* synthetic */ boolean endsWith$default(CharSequence charSequence, char c2, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return StringsKt.endsWith(charSequence, c2, bl);
    }

    public static final boolean startsWith(@NotNull CharSequence charSequence, @NotNull CharSequence charSequence2, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(charSequence2, "");
        if (!bl && charSequence instanceof String && charSequence2 instanceof String) {
            return StringsKt.startsWith$default((String)charSequence, (String)charSequence2, false, 2, null);
        }
        return StringsKt.regionMatchesImpl(charSequence, 0, charSequence2, 0, charSequence2.length(), bl);
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence charSequence, CharSequence charSequence2, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return StringsKt.startsWith(charSequence, charSequence2, bl);
    }

    public static final boolean startsWith(@NotNull CharSequence charSequence, @NotNull CharSequence charSequence2, int n2, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(charSequence2, "");
        if (!bl && charSequence instanceof String && charSequence2 instanceof String) {
            return StringsKt.startsWith$default((String)charSequence, (String)charSequence2, n2, false, 4, null);
        }
        return StringsKt.regionMatchesImpl(charSequence, n2, charSequence2, 0, charSequence2.length(), bl);
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence charSequence, CharSequence charSequence2, int n2, boolean bl, int n3, Object object) {
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.startsWith(charSequence, charSequence2, n2, bl);
    }

    public static final boolean endsWith(@NotNull CharSequence charSequence, @NotNull CharSequence charSequence2, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(charSequence2, "");
        if (!bl && charSequence instanceof String && charSequence2 instanceof String) {
            return StringsKt.endsWith$default((String)charSequence, (String)charSequence2, false, 2, null);
        }
        CharSequence charSequence3 = charSequence;
        return StringsKt.regionMatchesImpl(charSequence3, charSequence3.length() - charSequence2.length(), charSequence2, 0, charSequence2.length(), bl);
    }

    public static /* synthetic */ boolean endsWith$default(CharSequence charSequence, CharSequence charSequence2, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return StringsKt.endsWith(charSequence, charSequence2, bl);
    }

    @NotNull
    public static final String commonPrefixWith(@NotNull CharSequence charSequence, @NotNull CharSequence charSequence2, boolean bl) {
        int n2;
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(charSequence2, "");
        int n3 = Math.min(charSequence.length(), charSequence2.length());
        for (n2 = 0; n2 < n3 && CharsKt.equals(charSequence.charAt(n2), charSequence2.charAt(n2), bl); ++n2) {
        }
        if (StringsKt.hasSurrogatePairAt(charSequence, n2 - 1) || StringsKt.hasSurrogatePairAt(charSequence2, n2 - 1)) {
            --n2;
        }
        return ((Object)charSequence.subSequence(0, n2)).toString();
    }

    public static /* synthetic */ String commonPrefixWith$default(CharSequence charSequence, CharSequence charSequence2, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return StringsKt.commonPrefixWith(charSequence, charSequence2, bl);
    }

    @NotNull
    public static final String commonSuffixWith(@NotNull CharSequence charSequence, @NotNull CharSequence charSequence2, boolean bl) {
        int n2;
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(charSequence2, "");
        int n3 = charSequence.length();
        int n4 = charSequence2.length();
        int n5 = Math.min(n3, n4);
        for (n2 = 0; n2 < n5 && CharsKt.equals(charSequence.charAt(n3 - n2 - 1), charSequence2.charAt(n4 - n2 - 1), bl); ++n2) {
        }
        if (StringsKt.hasSurrogatePairAt(charSequence, n3 - n2 - 1) || StringsKt.hasSurrogatePairAt(charSequence2, n4 - n2 - 1)) {
            --n2;
        }
        return ((Object)charSequence.subSequence(n3 - n2, n3)).toString();
    }

    public static /* synthetic */ String commonSuffixWith$default(CharSequence charSequence, CharSequence charSequence2, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return StringsKt.commonSuffixWith(charSequence, charSequence2, bl);
    }

    public static final int indexOfAny(@NotNull CharSequence charSequence, @NotNull char[] cArray, int n2, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(cArray, "");
        if (!bl && cArray.length == 1 && charSequence instanceof String) {
            char c2 = ArraysKt.single(cArray);
            return ((String)charSequence).indexOf(c2, n2);
        }
        IntIterator intIterator = new IntRange(RangesKt.coerceAtLeast(n2, 0), StringsKt.getLastIndex(charSequence)).iterator();
        while (intIterator.hasNext()) {
            boolean bl2;
            block3: {
                n2 = intIterator.nextInt();
                char c3 = charSequence.charAt(n2);
                for (char c4 : cArray) {
                    char c5;
                    c4 = c4;
                    if (!CharsKt.equals(c5, c3, bl)) continue;
                    bl2 = true;
                    break block3;
                }
                bl2 = false;
            }
            if (!bl2) continue;
            return n2;
        }
        return -1;
    }

    public static /* synthetic */ int indexOfAny$default(CharSequence charSequence, char[] cArray, int n2, boolean bl, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n2 = 0;
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.indexOfAny(charSequence, cArray, n2, bl);
    }

    public static final int lastIndexOfAny(@NotNull CharSequence charSequence, @NotNull char[] cArray, int n2, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(cArray, "");
        if (!bl && cArray.length == 1 && charSequence instanceof String) {
            char c2 = ArraysKt.single(cArray);
            return ((String)charSequence).lastIndexOf(c2, n2);
        }
        for (int i2 = RangesKt.coerceAtMost(n2, StringsKt.getLastIndex(charSequence)); i2 >= 0; --i2) {
            boolean bl2;
            block3: {
                n2 = charSequence.charAt(i2);
                for (char c3 : cArray) {
                    char c4;
                    c3 = c3;
                    if (!CharsKt.equals(c4, (char)n2, bl)) continue;
                    bl2 = true;
                    break block3;
                }
                bl2 = false;
            }
            if (!bl2) continue;
            return i2;
        }
        return -1;
    }

    public static /* synthetic */ int lastIndexOfAny$default(CharSequence charSequence, char[] cArray, int n2, boolean bl, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n2 = StringsKt.getLastIndex(charSequence);
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.lastIndexOfAny(charSequence, cArray, n2, bl);
    }

    private static final int indexOf$StringsKt__StringsKt(CharSequence charSequence, CharSequence charSequence2, int n2, int n3, boolean bl, boolean n4) {
        IntProgression intProgression;
        IntProgression intProgression2 = intProgression = n4 == 0 ? (IntProgression)new IntRange(RangesKt.coerceAtLeast(n2, 0), RangesKt.coerceAtMost(n3, charSequence.length())) : RangesKt.downTo(RangesKt.coerceAtMost(n2, StringsKt.getLastIndex(charSequence)), RangesKt.coerceAtLeast(n3, 0));
        if (charSequence instanceof String && charSequence2 instanceof String) {
            n3 = intProgression.getFirst();
            n4 = intProgression.getLast();
            int n5 = intProgression.getStep();
            if (n5 > 0 && n3 <= n4 || n5 < 0 && n4 <= n3) {
                while (true) {
                    if (StringsKt.regionMatches((String)charSequence2, 0, (String)charSequence, n3, charSequence2.length(), bl)) {
                        return n3;
                    }
                    if (n3 != n4) {
                        n3 += n5;
                        continue;
                    }
                    break;
                }
            }
        } else {
            n3 = intProgression.getFirst();
            n4 = intProgression.getLast();
            int n6 = intProgression.getStep();
            if (n6 > 0 && n3 <= n4 || n6 < 0 && n4 <= n3) {
                while (true) {
                    if (StringsKt.regionMatchesImpl(charSequence2, 0, charSequence, n3, charSequence2.length(), bl)) {
                        return n3;
                    }
                    if (n3 == n4) break;
                    n3 += n6;
                }
            }
        }
        return -1;
    }

    static /* synthetic */ int indexOf$StringsKt__StringsKt$default(CharSequence charSequence, CharSequence charSequence2, int n2, int n3, boolean bl, boolean bl2, int n4, Object object) {
        if ((n4 & 0x10) != 0) {
            bl2 = false;
        }
        return StringsKt__StringsKt.indexOf$StringsKt__StringsKt(charSequence, charSequence2, n2, n3, bl, bl2);
    }

    private static final Pair<Integer, String> findAnyOf$StringsKt__StringsKt(CharSequence charSequence, Collection<String> collection, int n2, boolean bl, boolean n3) {
        IntProgression intProgression;
        if (!bl && collection.size() == 1) {
            String string = (String)CollectionsKt.single((Iterable)collection);
            n2 = n3 == 0 ? StringsKt.indexOf$default(charSequence, string, n2, false, 4, null) : StringsKt.lastIndexOf$default(charSequence, string, n2, false, 4, null);
            if (n2 < 0) {
                return null;
            }
            return TuplesKt.to(n2, string);
        }
        IntProgression intProgression2 = intProgression = n3 == 0 ? (IntProgression)new IntRange(RangesKt.coerceAtLeast(n2, 0), charSequence.length()) : RangesKt.downTo(RangesKt.coerceAtMost(n2, StringsKt.getLastIndex(charSequence)), 0);
        if (charSequence instanceof String) {
            n2 = intProgression.getFirst();
            n3 = intProgression.getLast();
            int n4 = intProgression.getStep();
            if (n4 > 0 && n2 <= n3 || n4 < 0 && n3 <= n2) {
                while (true) {
                    Object v1;
                    Object object;
                    block13: {
                        object = collection;
                        object = object.iterator();
                        while (object.hasNext()) {
                            Object e2 = object.next();
                            String string = (String)e2;
                            if (!StringsKt.regionMatches(string, 0, (String)charSequence, n2, string.length(), bl)) continue;
                            v1 = e2;
                            break block13;
                        }
                        v1 = null;
                    }
                    object = v1;
                    if (object != null) {
                        return TuplesKt.to(n2, object);
                    }
                    if (n2 != n3) {
                        n2 += n4;
                        continue;
                    }
                    break;
                }
            }
        } else {
            n2 = intProgression.getFirst();
            n3 = intProgression.getLast();
            int n5 = intProgression.getStep();
            if (n5 > 0 && n2 <= n3 || n5 < 0 && n3 <= n2) {
                while (true) {
                    Object v2;
                    Object object;
                    block15: {
                        object = collection;
                        object = object.iterator();
                        while (object.hasNext()) {
                            Object e3 = object.next();
                            String string = (String)e3;
                            if (!StringsKt.regionMatchesImpl(string, 0, charSequence, n2, string.length(), bl)) continue;
                            v2 = e3;
                            break block15;
                        }
                        v2 = null;
                    }
                    object = v2;
                    if (object != null) {
                        return TuplesKt.to(n2, object);
                    }
                    if (n2 == n3) break;
                    n2 += n5;
                }
            }
        }
        return null;
    }

    @Nullable
    public static final Pair<Integer, String> findAnyOf(@NotNull CharSequence charSequence, @NotNull Collection<String> collection, int n2, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(collection, "");
        return StringsKt__StringsKt.findAnyOf$StringsKt__StringsKt(charSequence, collection, n2, bl, false);
    }

    public static /* synthetic */ Pair findAnyOf$default(CharSequence charSequence, Collection collection, int n2, boolean bl, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n2 = 0;
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.findAnyOf(charSequence, collection, n2, bl);
    }

    @Nullable
    public static final Pair<Integer, String> findLastAnyOf(@NotNull CharSequence charSequence, @NotNull Collection<String> collection, int n2, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(collection, "");
        return StringsKt__StringsKt.findAnyOf$StringsKt__StringsKt(charSequence, collection, n2, bl, true);
    }

    public static /* synthetic */ Pair findLastAnyOf$default(CharSequence charSequence, Collection collection, int n2, boolean bl, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n2 = StringsKt.getLastIndex(charSequence);
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.findLastAnyOf(charSequence, collection, n2, bl);
    }

    public static final int indexOfAny(@NotNull CharSequence charSequence, @NotNull Collection<String> collection, int n2, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(collection, "");
        Pair<Integer, String> pair = StringsKt__StringsKt.findAnyOf$StringsKt__StringsKt(charSequence, collection, n2, bl, false);
        if (pair != null) {
            return ((Number)pair.getFirst()).intValue();
        }
        return -1;
    }

    public static /* synthetic */ int indexOfAny$default(CharSequence charSequence, Collection collection, int n2, boolean bl, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n2 = 0;
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.indexOfAny(charSequence, collection, n2, bl);
    }

    public static final int lastIndexOfAny(@NotNull CharSequence charSequence, @NotNull Collection<String> collection, int n2, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(collection, "");
        Pair<Integer, String> pair = StringsKt__StringsKt.findAnyOf$StringsKt__StringsKt(charSequence, collection, n2, bl, true);
        if (pair != null) {
            return ((Number)pair.getFirst()).intValue();
        }
        return -1;
    }

    public static /* synthetic */ int lastIndexOfAny$default(CharSequence charSequence, Collection collection, int n2, boolean bl, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n2 = StringsKt.getLastIndex(charSequence);
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.lastIndexOfAny(charSequence, collection, n2, bl);
    }

    public static final int indexOf(@NotNull CharSequence object, char c2, int n2, boolean bl) {
        Intrinsics.checkNotNullParameter(object, "");
        if (bl || !(object instanceof String)) {
            CharSequence charSequence = object;
            char[] cArray = new char[1];
            object = cArray;
            cArray[0] = c2;
            return StringsKt.indexOfAny(charSequence, (char[])object, n2, bl);
        }
        return ((String)object).indexOf(c2, n2);
    }

    public static /* synthetic */ int indexOf$default(CharSequence charSequence, char c2, int n2, boolean bl, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n2 = 0;
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.indexOf(charSequence, c2, n2, bl);
    }

    public static final int indexOf(@NotNull CharSequence charSequence, @NotNull String string, int n2, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(string, "");
        if (bl || !(charSequence instanceof String)) {
            return StringsKt__StringsKt.indexOf$StringsKt__StringsKt$default(charSequence, string, n2, charSequence.length(), bl, false, 16, null);
        }
        return ((String)charSequence).indexOf(string, n2);
    }

    public static /* synthetic */ int indexOf$default(CharSequence charSequence, String string, int n2, boolean bl, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n2 = 0;
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.indexOf(charSequence, string, n2, bl);
    }

    public static final int lastIndexOf(@NotNull CharSequence object, char c2, int n2, boolean bl) {
        Intrinsics.checkNotNullParameter(object, "");
        if (bl || !(object instanceof String)) {
            CharSequence charSequence = object;
            char[] cArray = new char[1];
            object = cArray;
            cArray[0] = c2;
            return StringsKt.lastIndexOfAny(charSequence, (char[])object, n2, bl);
        }
        return ((String)object).lastIndexOf(c2, n2);
    }

    public static /* synthetic */ int lastIndexOf$default(CharSequence charSequence, char c2, int n2, boolean bl, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n2 = StringsKt.getLastIndex(charSequence);
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.lastIndexOf(charSequence, c2, n2, bl);
    }

    public static final int lastIndexOf(@NotNull CharSequence charSequence, @NotNull String string, int n2, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(string, "");
        if (bl || !(charSequence instanceof String)) {
            return StringsKt__StringsKt.indexOf$StringsKt__StringsKt(charSequence, string, n2, 0, bl, true);
        }
        return ((String)charSequence).lastIndexOf(string, n2);
    }

    public static /* synthetic */ int lastIndexOf$default(CharSequence charSequence, String string, int n2, boolean bl, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n2 = StringsKt.getLastIndex(charSequence);
        }
        if ((n3 & 4) != 0) {
            bl = false;
        }
        return StringsKt.lastIndexOf(charSequence, string, n2, bl);
    }

    public static final boolean contains(@NotNull CharSequence charSequence, @NotNull CharSequence charSequence2, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(charSequence2, "");
        if (charSequence2 instanceof String) {
            return StringsKt.indexOf$default(charSequence, (String)charSequence2, 0, bl, 2, null) >= 0;
        }
        return StringsKt__StringsKt.indexOf$StringsKt__StringsKt$default(charSequence, charSequence2, 0, charSequence.length(), bl, false, 16, null) >= 0;
    }

    public static /* synthetic */ boolean contains$default(CharSequence charSequence, CharSequence charSequence2, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return StringsKt.contains(charSequence, charSequence2, bl);
    }

    public static final boolean contains(@NotNull CharSequence charSequence, char c2, boolean bl) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        return StringsKt.indexOf$default(charSequence, c2, 0, bl, 2, null) >= 0;
    }

    public static /* synthetic */ boolean contains$default(CharSequence charSequence, char c2, boolean bl, int n2, Object object) {
        if ((n2 & 2) != 0) {
            bl = false;
        }
        return StringsKt.contains(charSequence, c2, bl);
    }

    @InlineOnly
    private static final boolean contains(CharSequence charSequence, Regex regex) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(regex, "");
        return regex.containsMatchIn(charSequence);
    }

    private static final Sequence<IntRange> rangesDelimitedBy$StringsKt__StringsKt(CharSequence charSequence, char[] cArray, int n2, boolean bl, int n3) {
        StringsKt.requireNonNegativeLimit(n3);
        return new DelimitedRangesSequence(charSequence, n2, n3, (Function2<? super CharSequence, ? super Integer, Pair<Integer, Integer>>)new Function2<CharSequence, Integer, Pair<? extends Integer, ? extends Integer>>(cArray, bl){
            final /* synthetic */ char[] $delimiters;
            final /* synthetic */ boolean $ignoreCase;
            {
                this.$delimiters = cArray;
                this.$ignoreCase = bl;
                super(2);
            }

            @Nullable
            public final Pair<Integer, Integer> invoke(@NotNull CharSequence charSequence, int n2) {
                Intrinsics.checkNotNullParameter(charSequence, "");
                int n3 = StringsKt.indexOfAny(charSequence, this.$delimiters, n2, this.$ignoreCase);
                if (n3 < 0) {
                    return null;
                }
                return TuplesKt.to(n3, 1);
            }

            public final /* synthetic */ Object invoke(Object object, Object object2) {
                return this.invoke((CharSequence)object, ((Number)object2).intValue());
            }
        });
    }

    static /* synthetic */ Sequence rangesDelimitedBy$StringsKt__StringsKt$default(CharSequence charSequence, char[] cArray, int n2, boolean bl, int n3, int n4, Object object) {
        if ((n4 & 2) != 0) {
            n2 = 0;
        }
        if ((n4 & 4) != 0) {
            bl = false;
        }
        if ((n4 & 8) != 0) {
            n3 = 0;
        }
        return StringsKt__StringsKt.rangesDelimitedBy$StringsKt__StringsKt(charSequence, cArray, n2, bl, n3);
    }

    private static final Sequence<IntRange> rangesDelimitedBy$StringsKt__StringsKt(CharSequence charSequence, String[] object, int n2, boolean bl, int n3) {
        StringsKt.requireNonNegativeLimit(n3);
        object = ArraysKt.asList(object);
        return new DelimitedRangesSequence(charSequence, n2, n3, (Function2<? super CharSequence, ? super Integer, Pair<Integer, Integer>>)new Function2<CharSequence, Integer, Pair<? extends Integer, ? extends Integer>>((List<String>)object, bl){
            final /* synthetic */ List<String> $delimitersList;
            final /* synthetic */ boolean $ignoreCase;
            {
                this.$delimitersList = list2;
                this.$ignoreCase = bl;
                super(2);
            }

            @Nullable
            public final Pair<Integer, Integer> invoke(@NotNull CharSequence object, int n2) {
                Intrinsics.checkNotNullParameter(object, "");
                Pair pair = StringsKt__StringsKt.access$findAnyOf((CharSequence)object, this.$delimitersList, n2, this.$ignoreCase, false);
                if (pair != null) {
                    object = pair;
                    object = pair;
                    return TuplesKt.to(pair.getFirst(), ((String)((Pair)object).getSecond()).length());
                }
                return null;
            }

            public final /* synthetic */ Object invoke(Object object, Object object2) {
                return this.invoke((CharSequence)object, ((Number)object2).intValue());
            }
        });
    }

    static /* synthetic */ Sequence rangesDelimitedBy$StringsKt__StringsKt$default(CharSequence charSequence, String[] stringArray, int n2, boolean bl, int n3, int n4, Object object) {
        if ((n4 & 2) != 0) {
            n2 = 0;
        }
        if ((n4 & 4) != 0) {
            bl = false;
        }
        if ((n4 & 8) != 0) {
            n3 = 0;
        }
        return StringsKt__StringsKt.rangesDelimitedBy$StringsKt__StringsKt(charSequence, stringArray, n2, bl, n3);
    }

    public static final void requireNonNegativeLimit(int n2) {
        boolean bl = n2 >= 0;
        if (!bl) {
            String string = "Limit must be non-negative, but was " + n2;
            throw new IllegalArgumentException(string.toString());
        }
    }

    @NotNull
    public static final Sequence<String> splitToSequence(@NotNull CharSequence charSequence, @NotNull String[] stringArray, boolean bl, int n2) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(stringArray, "");
        return SequencesKt.map(StringsKt__StringsKt.rangesDelimitedBy$StringsKt__StringsKt$default(charSequence, stringArray, 0, bl, n2, 2, null), (Function1)new Function1<IntRange, String>(charSequence){
            final /* synthetic */ CharSequence $this_splitToSequence;
            {
                this.$this_splitToSequence = charSequence;
                super(1);
            }

            @NotNull
            public final String invoke(@NotNull IntRange intRange) {
                Intrinsics.checkNotNullParameter(intRange, "");
                return StringsKt.substring(this.$this_splitToSequence, intRange);
            }
        });
    }

    public static /* synthetic */ Sequence splitToSequence$default(CharSequence charSequence, String[] stringArray, boolean bl, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            bl = false;
        }
        if ((n3 & 4) != 0) {
            n2 = 0;
        }
        return StringsKt.splitToSequence(charSequence, stringArray, bl, n2);
    }

    @NotNull
    public static final List<String> split(@NotNull CharSequence charSequence, @NotNull String[] object, boolean bl, int n2) {
        Object object2;
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(object, "");
        if (((String[])object).length == 1 && !(((CharSequence)(object2 = object[0])).length() == 0)) {
            return StringsKt__StringsKt.split$StringsKt__StringsKt(charSequence, (String)object2, bl, n2);
        }
        object = object2 = SequencesKt.asIterable(StringsKt__StringsKt.rangesDelimitedBy$StringsKt__StringsKt$default(charSequence, (String[])object, 0, bl, n2, 2, null));
        Collection collection = new ArrayList(CollectionsKt.collectionSizeOrDefault(object2, 10));
        object = object.iterator();
        while (object.hasNext()) {
            Object object3 = object.next();
            object3 = (IntRange)object3;
            object2 = collection;
            object2.add(StringsKt.substring(charSequence, object3));
        }
        return (List)collection;
    }

    public static /* synthetic */ List split$default(CharSequence charSequence, String[] stringArray, boolean bl, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            bl = false;
        }
        if ((n3 & 4) != 0) {
            n2 = 0;
        }
        return StringsKt.split(charSequence, stringArray, bl, n2);
    }

    @NotNull
    public static final Sequence<String> splitToSequence(@NotNull CharSequence charSequence, @NotNull char[] cArray, boolean bl, int n2) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(cArray, "");
        return SequencesKt.map(StringsKt__StringsKt.rangesDelimitedBy$StringsKt__StringsKt$default(charSequence, cArray, 0, bl, n2, 2, null), (Function1)new Function1<IntRange, String>(charSequence){
            final /* synthetic */ CharSequence $this_splitToSequence;
            {
                this.$this_splitToSequence = charSequence;
                super(1);
            }

            @NotNull
            public final String invoke(@NotNull IntRange intRange) {
                Intrinsics.checkNotNullParameter(intRange, "");
                return StringsKt.substring(this.$this_splitToSequence, intRange);
            }
        });
    }

    public static /* synthetic */ Sequence splitToSequence$default(CharSequence charSequence, char[] cArray, boolean bl, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            bl = false;
        }
        if ((n3 & 4) != 0) {
            n2 = 0;
        }
        return StringsKt.splitToSequence(charSequence, cArray, bl, n2);
    }

    @NotNull
    public static final List<String> split(@NotNull CharSequence charSequence, @NotNull char[] object, boolean bl, int n2) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(object, "");
        if (((char[])object).length == 1) {
            return StringsKt__StringsKt.split$StringsKt__StringsKt(charSequence, String.valueOf(object[0]), bl, n2);
        }
        Iterable iterable = SequencesKt.asIterable(StringsKt__StringsKt.rangesDelimitedBy$StringsKt__StringsKt$default(charSequence, (char[])object, 0, bl, n2, 2, null));
        object = iterable;
        Object object2 = iterable;
        object = new ArrayList(CollectionsKt.collectionSizeOrDefault(object, 10));
        object2 = object2.iterator();
        while (object2.hasNext()) {
            Object object3 = object2.next();
            object3 = (IntRange)object3;
            Object object4 = object;
            object4.add(StringsKt.substring(charSequence, object3));
        }
        return (List)object;
    }

    public static /* synthetic */ List split$default(CharSequence charSequence, char[] cArray, boolean bl, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            bl = false;
        }
        if ((n3 & 4) != 0) {
            n2 = 0;
        }
        return StringsKt.split(charSequence, cArray, bl, n2);
    }

    private static final List<String> split$StringsKt__StringsKt(CharSequence charSequence, String string, boolean bl, int n2) {
        StringsKt.requireNonNegativeLimit(n2);
        int n3 = 0;
        int n4 = StringsKt.indexOf(charSequence, string, 0, bl);
        if (n4 == -1 || n2 == 1) {
            return CollectionsKt.listOf(((Object)charSequence).toString());
        }
        boolean bl2 = n2 > 0;
        ArrayList<String> arrayList = new ArrayList<String>(bl2 ? RangesKt.coerceAtMost(n2, 10) : 10);
        do {
            arrayList.add(((Object)charSequence.subSequence(n3, n4)).toString());
            n3 = n4 + string.length();
        } while ((!bl2 || arrayList.size() != n2 - 1) && (n4 = StringsKt.indexOf(charSequence, string, n3, bl)) != -1);
        arrayList.add(((Object)charSequence.subSequence(n3, charSequence.length())).toString());
        return arrayList;
    }

    @InlineOnly
    private static final List<String> split(CharSequence charSequence, Regex regex, int n2) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(regex, "");
        return regex.split(charSequence, n2);
    }

    static /* synthetic */ List split$default(CharSequence charSequence, Regex regex, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n2 = 0;
        }
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(regex, "");
        return regex.split(charSequence, n2);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final Sequence<String> splitToSequence(CharSequence charSequence, Regex regex, int n2) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(regex, "");
        return regex.splitToSequence(charSequence, n2);
    }

    static /* synthetic */ Sequence splitToSequence$default(CharSequence charSequence, Regex regex, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n2 = 0;
        }
        Intrinsics.checkNotNullParameter(charSequence, "");
        Intrinsics.checkNotNullParameter(regex, "");
        return regex.splitToSequence(charSequence, n2);
    }

    @NotNull
    public static final Sequence<String> lineSequence(@NotNull CharSequence stringArray) {
        Intrinsics.checkNotNullParameter(stringArray, "");
        String[] stringArray2 = stringArray;
        String[] stringArray3 = new String[3];
        stringArray = stringArray3;
        stringArray3[0] = "\r\n";
        stringArray[1] = "\n";
        stringArray[2] = "\r";
        return StringsKt.splitToSequence$default((CharSequence)stringArray2, stringArray, false, 0, 6, null);
    }

    @NotNull
    public static final List<String> lines(@NotNull CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "");
        return SequencesKt.toList(StringsKt.lineSequence(charSequence));
    }

    public static final boolean contentEqualsIgnoreCaseImpl(@Nullable CharSequence charSequence, @Nullable CharSequence charSequence2) {
        if (charSequence instanceof String && charSequence2 instanceof String) {
            return StringsKt.equals((String)charSequence, (String)charSequence2, true);
        }
        if (charSequence == charSequence2) {
            return true;
        }
        if (charSequence == null || charSequence2 == null || charSequence.length() != charSequence2.length()) {
            return false;
        }
        int n2 = charSequence.length();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (CharsKt.equals(charSequence.charAt(i2), charSequence2.charAt(i2), true)) continue;
            return false;
        }
        return true;
    }

    public static final boolean contentEqualsImpl(@Nullable CharSequence charSequence, @Nullable CharSequence charSequence2) {
        if (charSequence instanceof String && charSequence2 instanceof String) {
            return Intrinsics.areEqual(charSequence, charSequence2);
        }
        if (charSequence == charSequence2) {
            return true;
        }
        if (charSequence == null || charSequence2 == null || charSequence.length() != charSequence2.length()) {
            return false;
        }
        int n2 = charSequence.length();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (charSequence.charAt(i2) == charSequence2.charAt(i2)) continue;
            return false;
        }
        return true;
    }

    @SinceKotlin(version="1.5")
    public static final boolean toBooleanStrict(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        String string2 = string;
        if (Intrinsics.areEqual(string2, "true")) {
            return true;
        }
        if (Intrinsics.areEqual(string2, "false")) {
            return false;
        }
        throw new IllegalArgumentException("The string doesn't represent a boolean value: " + string);
    }

    @SinceKotlin(version="1.5")
    @Nullable
    public static final Boolean toBooleanStrictOrNull(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "");
        if (Intrinsics.areEqual(string, "true")) {
            return Boolean.TRUE;
        }
        if (Intrinsics.areEqual(string, "false")) {
            return Boolean.FALSE;
        }
        return null;
    }

    public static final /* synthetic */ Pair access$findAnyOf(CharSequence charSequence, Collection collection, int n2, boolean bl, boolean bl2) {
        return StringsKt__StringsKt.findAnyOf$StringsKt__StringsKt(charSequence, collection, n2, bl, bl2);
    }
}

