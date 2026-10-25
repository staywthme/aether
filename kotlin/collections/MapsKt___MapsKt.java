/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.OverloadResolutionByLambdaReturnType;
import kotlin.Pair;
import kotlin.SinceKotlin;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt___MapsJvmKt;
import kotlin.internal.HidesMembers;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000\u0082\u0001\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010&\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u001f\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000f\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u001aJ\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\b\u00f8\u0001\u0000\u001a$\u0010\b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004\u001aJ\u0010\b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\b\u00f8\u0001\u0000\u001a9\u0010\t\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00070\n\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004H\u0087\b\u001a6\u0010\u000b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00070\f\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004\u001a'\u0010\r\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004H\u0087\b\u001aJ\u0010\r\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\b\u00f8\u0001\u0000\u001a[\u0010\u000f\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\b\b\u0002\u0010\u0010*\u00020\u0011*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042 \u0010\u0012\u001a\u001c\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0006\u0012\u0004\u0018\u0001H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013\u001a]\u0010\u0014\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\b\b\u0002\u0010\u0010*\u00020\u0011*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042 \u0010\u0012\u001a\u001c\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0006\u0012\u0004\u0018\u0001H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013\u001a\\\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00100\u0016\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042$\u0010\u0012\u001a \u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100\n0\u0006H\u0086\b\u00f8\u0001\u0000\u001aa\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00100\u0016\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042$\u0010\u0012\u001a \u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100\f0\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\b\u0017\u001au\u0010\u0018\u001a\u0002H\u0019\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010\"\u0010\b\u0003\u0010\u0019*\n\u0012\u0006\b\u0000\u0012\u0002H\u00100\u001a*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u001b\u001a\u0002H\u00192$\u0010\u0012\u001a \u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100\n0\u0006H\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001c\u001aw\u0010\u0018\u001a\u0002H\u0019\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010\"\u0010\b\u0003\u0010\u0019*\n\u0012\u0006\b\u0000\u0012\u0002H\u00100\u001a*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u001b\u001a\u0002H\u00192$\u0010\u0012\u001a \u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100\f0\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001d\u0010\u001c\u001aJ\u0010\u001e\u001a\u00020\u001f\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010 \u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u001f0\u0006H\u0087\b\u00f8\u0001\u0000\u001aV\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00100\u0016\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0012\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0086\b\u00f8\u0001\u0000\u001a\\\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00100\u0016\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\b\b\u0002\u0010\u0010*\u00020\u0011*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042 \u0010\u0012\u001a\u001c\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0006\u0012\u0004\u0018\u0001H\u00100\u0006H\u0086\b\u00f8\u0001\u0000\u001au\u0010#\u001a\u0002H\u0019\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\b\b\u0002\u0010\u0010*\u00020\u0011\"\u0010\b\u0003\u0010\u0019*\n\u0012\u0006\b\u0000\u0012\u0002H\u00100\u001a*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u001b\u001a\u0002H\u00192 \u0010\u0012\u001a\u001c\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0006\u0012\u0004\u0018\u0001H\u00100\u0006H\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001c\u001ao\u0010$\u001a\u0002H\u0019\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010\"\u0010\b\u0003\u0010\u0019*\n\u0012\u0006\b\u0000\u0012\u0002H\u00100\u001a*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u001b\u001a\u0002H\u00192\u001e\u0010\u0012\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001c\u001ak\u0010%\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\b(\u001ah\u0010)\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u001a_\u0010*\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010+\u001aJ\u0010*\u001a\u00020,\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020,0\u0006H\u0087\b\u00f8\u0001\u0000\u001aJ\u0010*\u001a\u00020-\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020-0\u0006H\u0087\b\u00f8\u0001\u0000\u001aa\u0010.\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010+\u001aQ\u0010.\u001a\u0004\u0018\u00010,\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020,0\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010/\u001aQ\u0010.\u001a\u0004\u0018\u00010-\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020-0\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u00100\u001aq\u00101\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001a\u00102\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u001003j\n\u0012\u0006\b\u0000\u0012\u0002H\u0010`42\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u00105\u001as\u00106\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001a\u00102\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u001003j\n\u0012\u0006\b\u0000\u0012\u0002H\u0010`42\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u00105\u001al\u00107\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000422\u00102\u001a.\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000703j\u0016\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007`4H\u0087\b\u00a2\u0006\u0002\b8\u001ai\u00109\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000422\u00102\u001a.\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000703j\u0016\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007`4H\u0087\b\u001ak\u0010:\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\b;\u001ah\u0010<\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u001a_\u0010=\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010+\u001aJ\u0010=\u001a\u00020,\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020,0\u0006H\u0087\b\u00f8\u0001\u0000\u001aJ\u0010=\u001a\u00020-\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020-0\u0006H\u0087\b\u00f8\u0001\u0000\u001aa\u0010>\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010+\u001aQ\u0010>\u001a\u0004\u0018\u00010,\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020,0\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010/\u001aQ\u0010>\u001a\u0004\u0018\u00010-\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020-0\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u00100\u001aq\u0010?\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001a\u00102\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u001003j\n\u0012\u0006\b\u0000\u0012\u0002H\u0010`42\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u00105\u001as\u0010@\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001a\u00102\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u001003j\n\u0012\u0006\b\u0000\u0012\u0002H\u0010`42\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u00105\u001al\u0010A\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000422\u00102\u001a.\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000703j\u0016\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007`4H\u0087\b\u00a2\u0006\u0002\bB\u001ai\u0010C\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000422\u00102\u001a.\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000703j\u0016\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007`4H\u0087\b\u001a$\u0010D\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004\u001aJ\u0010D\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\b\u00f8\u0001\u0000\u001aY\u0010E\u001a\u0002HF\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0016\b\u0002\u0010F*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004*\u0002HF2\u001e\u0010 \u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u001f0\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010G\u001an\u0010H\u001a\u0002HF\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0016\b\u0002\u0010F*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004*\u0002HF23\u0010 \u001a/\u0012\u0013\u0012\u00110\u000e\u00a2\u0006\f\bJ\u0012\b\bK\u0012\u0004\b\b(L\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u001f0IH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010M\u001a6\u0010N\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030O0\u0016\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006P"}, d2={"all", "", "K", "V", "", "predicate", "Lkotlin/Function1;", "", "any", "asIterable", "", "asSequence", "Lkotlin/sequences/Sequence;", "count", "", "firstNotNullOf", "R", "", "transform", "(Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "firstNotNullOfOrNull", "flatMap", "", "flatMapSequence", "flatMapTo", "C", "", "destination", "(Ljava/util/Map;Ljava/util/Collection;Lkotlin/jvm/functions/Function1;)Ljava/util/Collection;", "flatMapSequenceTo", "forEach", "", "action", "map", "mapNotNull", "mapNotNullTo", "mapTo", "maxBy", "", "selector", "maxByOrThrow", "maxByOrNull", "maxOf", "(Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/lang/Comparable;", "", "", "maxOfOrNull", "(Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/lang/Double;", "(Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/lang/Float;", "maxOfWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/util/Map;Ljava/util/Comparator;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "maxOfWithOrNull", "maxWith", "maxWithOrThrow", "maxWithOrNull", "minBy", "minByOrThrow", "minByOrNull", "minOf", "minOfOrNull", "minOfWith", "minOfWithOrNull", "minWith", "minWithOrThrow", "minWithOrNull", "none", "onEach", "M", "(Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "onEachIndexed", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "index", "(Ljava/util/Map;Lkotlin/jvm/functions/Function2;)Ljava/util/Map;", "toList", "Lkotlin/Pair;", "kotlin-stdlib"}, xs="kotlin/collections/MapsKt")
@SourceDebugExtension(value={"SMAP\n_Maps.kt\nKotlin\n*S Kotlin\n*F\n+ 1 _Maps.kt\nkotlin/collections/MapsKt___MapsKt\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,594:1\n96#1,5:595\n111#1,5:600\n152#1,3:605\n143#1:608\n215#1:609\n216#1:611\n144#1:612\n215#1:613\n216#1:615\n1#2:610\n1#2:614\n1940#3,14:616\n1963#3,14:630\n2310#3,14:644\n2333#3,14:658\n1864#3,3:672\n*S KotlinDebug\n*F\n+ 1 _Maps.kt\nkotlin/collections/MapsKt___MapsKt\n*L\n76#1:595,5\n89#1:600,5\n125#1:605,3\n135#1:608\n135#1:609\n135#1:611\n135#1:612\n143#1:613\n143#1:615\n135#1:610\n230#1:616,14\n241#1:630,14\n390#1:644,14\n401#1:658,14\n574#1:672,3\n*E\n"})
class MapsKt___MapsKt
extends MapsKt___MapsJvmKt {
    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final <K, V, R> R firstNotNullOf(Map<? extends K, ? extends V> object, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        R r2;
        block3: {
            Intrinsics.checkNotNullParameter(object, "");
            Intrinsics.checkNotNullParameter(function1, "");
            object = object.entrySet().iterator();
            while (object.hasNext()) {
                R r3 = function1.invoke((Map.Entry)object.next());
                r2 = r3;
                if (r3 == null) {
                    continue;
                }
                break block3;
            }
            r2 = null;
        }
        if (r2 == null) {
            throw new NoSuchElementException("No element of the map was transformed to a non-null value.");
        }
        return r2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final <K, V, R> R firstNotNullOfOrNull(Map<? extends K, ? extends V> object, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        for (Map.Entry entry : object.entrySet()) {
            R entry2 = function1.invoke(entry);
            if (entry2 == null) continue;
            return entry2;
        }
        return null;
    }

    @NotNull
    public static final <K, V> List<Pair<K, V>> toList(@NotNull Map<? extends K, ? extends V> arrayList) {
        Intrinsics.checkNotNullParameter(arrayList, "");
        if (arrayList.size() == 0) {
            return CollectionsKt.emptyList();
        }
        Iterator<Map.Entry<K, V>> iterator2 = arrayList.entrySet().iterator();
        if (!iterator2.hasNext()) {
            return CollectionsKt.emptyList();
        }
        Map.Entry<K, V> entry = iterator2.next();
        if (!iterator2.hasNext()) {
            arrayList = entry;
            return CollectionsKt.listOf(new Pair(arrayList.getKey(), arrayList.getValue()));
        }
        arrayList = new ArrayList<Pair<K, V>>(arrayList.size());
        arrayList.add(new Pair<K, V>(entry.getKey(), entry.getValue()));
        do {
            entry = iterator2.next();
            arrayList.add(new Pair<K, V>(entry.getKey(), entry.getValue()));
        } while (iterator2.hasNext());
        return arrayList;
    }

    @NotNull
    public static final <K, V, R> List<R> flatMap(@NotNull Map<? extends K, ? extends V> object, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends Iterable<? extends R>> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        Collection collection = new ArrayList();
        for (Map.Entry entry : object.entrySet()) {
            Iterable<R> object2 = function1.invoke(entry);
            CollectionsKt.addAll(collection, object2);
        }
        return (List)collection;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name="flatMapSequence")
    @NotNull
    public static final <K, V, R> List<R> flatMapSequence(@NotNull Map<? extends K, ? extends V> object, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends Sequence<? extends R>> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        Collection collection = new ArrayList();
        for (Map.Entry entry : object.entrySet()) {
            Sequence<R> object2 = function1.invoke(entry);
            CollectionsKt.addAll(collection, object2);
        }
        return (List)collection;
    }

    @NotNull
    public static final <K, V, R, C extends Collection<? super R>> C flatMapTo(@NotNull Map<? extends K, ? extends V> object, @NotNull C c2, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends Iterable<? extends R>> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(c2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        for (Map.Entry entry : object.entrySet()) {
            Iterable<R> object2 = function1.invoke(entry);
            CollectionsKt.addAll(c2, object2);
        }
        return c2;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name="flatMapSequenceTo")
    @NotNull
    public static final <K, V, R, C extends Collection<? super R>> C flatMapSequenceTo(@NotNull Map<? extends K, ? extends V> object, @NotNull C c2, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends Sequence<? extends R>> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(c2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        for (Map.Entry entry : object.entrySet()) {
            Sequence<R> object2 = function1.invoke(entry);
            CollectionsKt.addAll(c2, object2);
        }
        return c2;
    }

    @NotNull
    public static final <K, V, R> List<R> map(@NotNull Map<? extends K, ? extends V> object, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        Iterator object2 = object;
        object = new ArrayList(object.size());
        for (Map.Entry entry : object2.entrySet()) {
            object.add(function1.invoke(entry));
        }
        return (List)((Object)object);
    }

    @NotNull
    public static final <K, V, R> List<R> mapNotNull(@NotNull Map<? extends K, ? extends V> object, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        Collection collection = new ArrayList();
        for (Map.Entry entry : object.entrySet()) {
            R r2;
            Map.Entry entry2 = entry;
            if (function1.invoke(entry2) == null) continue;
            R r3 = r2;
            collection.add(r3);
        }
        return (List)collection;
    }

    @NotNull
    public static final <K, V, R, C extends Collection<? super R>> C mapNotNullTo(@NotNull Map<? extends K, ? extends V> object, @NotNull C c2, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(c2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        for (Map.Entry entry : object.entrySet()) {
            R r2;
            Map.Entry entry2 = entry;
            if (function1.invoke(entry2) == null) continue;
            R r3 = r2;
            c2.add(r3);
        }
        return c2;
    }

    @NotNull
    public static final <K, V, R, C extends Collection<? super R>> C mapTo(@NotNull Map<? extends K, ? extends V> object, @NotNull C c2, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(c2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        for (Map.Entry entry : object.entrySet()) {
            c2.add(function1.invoke(entry));
        }
        return c2;
    }

    public static final <K, V> boolean all(@NotNull Map<? extends K, ? extends V> object, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        if (object.isEmpty()) {
            return true;
        }
        for (Map.Entry entry : object.entrySet()) {
            if (function1.invoke(entry).booleanValue()) continue;
            return false;
        }
        return true;
    }

    public static final <K, V> boolean any(@NotNull Map<? extends K, ? extends V> map) {
        Intrinsics.checkNotNullParameter(map, "");
        return !map.isEmpty();
    }

    public static final <K, V> boolean any(@NotNull Map<? extends K, ? extends V> object, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        if (object.isEmpty()) {
            return false;
        }
        for (Map.Entry entry : object.entrySet()) {
            if (!function1.invoke(entry).booleanValue()) continue;
            return true;
        }
        return false;
    }

    @InlineOnly
    private static final <K, V> int count(Map<? extends K, ? extends V> map) {
        Intrinsics.checkNotNullParameter(map, "");
        return map.size();
    }

    public static final <K, V> int count(@NotNull Map<? extends K, ? extends V> object, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        if (object.isEmpty()) {
            return 0;
        }
        int n2 = 0;
        for (Map.Entry entry : object.entrySet()) {
            if (!function1.invoke(entry).booleanValue()) continue;
            ++n2;
        }
        return n2;
    }

    @HidesMembers
    public static final <K, V> void forEach(@NotNull Map<? extends K, ? extends V> object, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Unit> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        for (Map.Entry entry : object.entrySet()) {
            function1.invoke(entry);
        }
    }

    @SinceKotlin(version="1.7")
    @JvmName(name="maxByOrThrow")
    @InlineOnly
    private static final <K, V, R extends Comparable<? super R>> Map.Entry<K, V> maxByOrThrow(Map<? extends K, ? extends V> iterator2, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Object t2;
        Intrinsics.checkNotNullParameter(iterator2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        iterator2 = iterator2.entrySet();
        iterator2 = iterator2.iterator();
        if (!iterator2.hasNext()) {
            throw new NoSuchElementException();
        }
        Object t3 = iterator2.next();
        if (!iterator2.hasNext()) {
            t2 = t3;
        } else {
            Comparable comparable = (Comparable)function1.invoke((Map.Entry<K, V>)t3);
            do {
                Object t4;
                Comparable comparable2;
                if (comparable.compareTo(comparable2 = (Comparable)function1.invoke((Map.Entry<K, V>)(t4 = iterator2.next()))) >= 0) continue;
                t3 = t4;
                comparable = comparable2;
            } while (iterator2.hasNext());
            t2 = t3;
        }
        return (Map.Entry)t2;
    }

    @SinceKotlin(version="1.4")
    @InlineOnly
    private static final <K, V, R extends Comparable<? super R>> Map.Entry<K, V> maxByOrNull(Map<? extends K, ? extends V> iterator2, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Object v0;
        Intrinsics.checkNotNullParameter(iterator2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        iterator2 = iterator2.entrySet();
        iterator2 = iterator2.iterator();
        if (!iterator2.hasNext()) {
            v0 = null;
        } else {
            Object t2 = iterator2.next();
            if (!iterator2.hasNext()) {
                v0 = t2;
            } else {
                Comparable comparable = (Comparable)function1.invoke((Map.Entry<K, V>)t2);
                do {
                    Object t3;
                    Comparable comparable2;
                    if (comparable.compareTo(comparable2 = (Comparable)function1.invoke((Map.Entry<K, V>)(t3 = iterator2.next()))) >= 0) continue;
                    t2 = t3;
                    comparable = comparable2;
                } while (iterator2.hasNext());
                v0 = t2;
            }
        }
        return v0;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V> double maxOf(Map<? extends K, ? extends V> object, Function1<? super Map.Entry<? extends K, ? extends V>, Double> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        object = ((Iterable)object.entrySet()).iterator();
        if (!object.hasNext()) {
            throw new NoSuchElementException();
        }
        double d2 = ((Number)function1.invoke((Map.Entry<K, V>)object.next())).doubleValue();
        while (object.hasNext()) {
            double d3 = ((Number)function1.invoke((Map.Entry<K, V>)object.next())).doubleValue();
            d2 = Math.max(d2, d3);
        }
        return d2;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V> float maxOf(Map<? extends K, ? extends V> object, Function1<? super Map.Entry<? extends K, ? extends V>, Float> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        object = ((Iterable)object.entrySet()).iterator();
        if (!object.hasNext()) {
            throw new NoSuchElementException();
        }
        float f2 = ((Number)function1.invoke((Map.Entry<K, V>)object.next())).floatValue();
        while (object.hasNext()) {
            float f3 = ((Number)function1.invoke((Map.Entry<K, V>)object.next())).floatValue();
            f2 = Math.max(f2, f3);
        }
        return f2;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V, R extends Comparable<? super R>> R maxOf(Map<? extends K, ? extends V> object, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        object = ((Iterable)object.entrySet()).iterator();
        if (!object.hasNext()) {
            throw new NoSuchElementException();
        }
        Comparable comparable = (Comparable)function1.invoke((Map.Entry<K, V>)object.next());
        while (object.hasNext()) {
            Comparable comparable2 = (Comparable)function1.invoke((Map.Entry<K, V>)object.next());
            if (comparable.compareTo(comparable2) >= 0) continue;
            comparable = comparable2;
        }
        return (R)comparable;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V> Double maxOfOrNull(Map<? extends K, ? extends V> object, Function1<? super Map.Entry<? extends K, ? extends V>, Double> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        object = ((Iterable)object.entrySet()).iterator();
        if (!object.hasNext()) {
            return null;
        }
        double d2 = ((Number)function1.invoke((Map.Entry<K, V>)object.next())).doubleValue();
        while (object.hasNext()) {
            double d3 = ((Number)function1.invoke((Map.Entry<K, V>)object.next())).doubleValue();
            d2 = Math.max(d2, d3);
        }
        return d2;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V> Float maxOfOrNull(Map<? extends K, ? extends V> object, Function1<? super Map.Entry<? extends K, ? extends V>, Float> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        object = ((Iterable)object.entrySet()).iterator();
        if (!object.hasNext()) {
            return null;
        }
        float f2 = ((Number)function1.invoke((Map.Entry<K, V>)object.next())).floatValue();
        while (object.hasNext()) {
            float f3 = ((Number)function1.invoke((Map.Entry<K, V>)object.next())).floatValue();
            f2 = Math.max(f2, f3);
        }
        return Float.valueOf(f2);
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V, R extends Comparable<? super R>> R maxOfOrNull(Map<? extends K, ? extends V> object, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        object = ((Iterable)object.entrySet()).iterator();
        if (!object.hasNext()) {
            return null;
        }
        Comparable comparable = (Comparable)function1.invoke((Map.Entry<K, V>)object.next());
        while (object.hasNext()) {
            Comparable comparable2 = (Comparable)function1.invoke((Map.Entry<K, V>)object.next());
            if (comparable.compareTo(comparable2) >= 0) continue;
            comparable = comparable2;
        }
        return (R)comparable;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V, R> R maxOfWith(Map<? extends K, ? extends V> object, Comparator<? super R> comparator, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        Intrinsics.checkNotNullParameter(function1, "");
        object = ((Iterable)object.entrySet()).iterator();
        if (!object.hasNext()) {
            throw new NoSuchElementException();
        }
        R r2 = function1.invoke((Map.Entry<K, V>)object.next());
        while (object.hasNext()) {
            R r3 = function1.invoke((Map.Entry<K, V>)object.next());
            if (comparator.compare(r2, r3) >= 0) continue;
            r2 = r3;
        }
        return r2;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V, R> R maxOfWithOrNull(Map<? extends K, ? extends V> object, Comparator<? super R> comparator, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        Intrinsics.checkNotNullParameter(function1, "");
        object = ((Iterable)object.entrySet()).iterator();
        if (!object.hasNext()) {
            return null;
        }
        R r2 = function1.invoke((Map.Entry<K, V>)object.next());
        while (object.hasNext()) {
            R r3 = function1.invoke((Map.Entry<K, V>)object.next());
            if (comparator.compare(r2, r3) >= 0) continue;
            r2 = r3;
        }
        return r2;
    }

    @SinceKotlin(version="1.7")
    @JvmName(name="maxWithOrThrow")
    @InlineOnly
    private static final <K, V> Map.Entry<K, V> maxWithOrThrow(Map<? extends K, ? extends V> map, Comparator<? super Map.Entry<? extends K, ? extends V>> comparator) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        return CollectionsKt.maxWithOrThrow((Iterable)map.entrySet(), comparator);
    }

    @SinceKotlin(version="1.4")
    @InlineOnly
    private static final <K, V> Map.Entry<K, V> maxWithOrNull(Map<? extends K, ? extends V> map, Comparator<? super Map.Entry<? extends K, ? extends V>> comparator) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        return CollectionsKt.maxWithOrNull((Iterable)map.entrySet(), comparator);
    }

    @SinceKotlin(version="1.7")
    @JvmName(name="minByOrThrow")
    @InlineOnly
    private static final <K, V, R extends Comparable<? super R>> Map.Entry<K, V> minByOrThrow(Map<? extends K, ? extends V> iterator2, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Object t2;
        Intrinsics.checkNotNullParameter(iterator2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        iterator2 = iterator2.entrySet();
        iterator2 = iterator2.iterator();
        if (!iterator2.hasNext()) {
            throw new NoSuchElementException();
        }
        Object t3 = iterator2.next();
        if (!iterator2.hasNext()) {
            t2 = t3;
        } else {
            Comparable comparable = (Comparable)function1.invoke((Map.Entry<K, V>)t3);
            do {
                Object t4;
                Comparable comparable2;
                if (comparable.compareTo(comparable2 = (Comparable)function1.invoke((Map.Entry<K, V>)(t4 = iterator2.next()))) <= 0) continue;
                t3 = t4;
                comparable = comparable2;
            } while (iterator2.hasNext());
            t2 = t3;
        }
        return (Map.Entry)t2;
    }

    @SinceKotlin(version="1.4")
    @InlineOnly
    private static final <K, V, R extends Comparable<? super R>> Map.Entry<K, V> minByOrNull(Map<? extends K, ? extends V> iterator2, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Object v0;
        Intrinsics.checkNotNullParameter(iterator2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        iterator2 = iterator2.entrySet();
        iterator2 = iterator2.iterator();
        if (!iterator2.hasNext()) {
            v0 = null;
        } else {
            Object t2 = iterator2.next();
            if (!iterator2.hasNext()) {
                v0 = t2;
            } else {
                Comparable comparable = (Comparable)function1.invoke((Map.Entry<K, V>)t2);
                do {
                    Object t3;
                    Comparable comparable2;
                    if (comparable.compareTo(comparable2 = (Comparable)function1.invoke((Map.Entry<K, V>)(t3 = iterator2.next()))) <= 0) continue;
                    t2 = t3;
                    comparable = comparable2;
                } while (iterator2.hasNext());
                v0 = t2;
            }
        }
        return v0;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V> double minOf(Map<? extends K, ? extends V> object, Function1<? super Map.Entry<? extends K, ? extends V>, Double> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        object = ((Iterable)object.entrySet()).iterator();
        if (!object.hasNext()) {
            throw new NoSuchElementException();
        }
        double d2 = ((Number)function1.invoke((Map.Entry<K, V>)object.next())).doubleValue();
        while (object.hasNext()) {
            double d3 = ((Number)function1.invoke((Map.Entry<K, V>)object.next())).doubleValue();
            d2 = Math.min(d2, d3);
        }
        return d2;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V> float minOf(Map<? extends K, ? extends V> object, Function1<? super Map.Entry<? extends K, ? extends V>, Float> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        object = ((Iterable)object.entrySet()).iterator();
        if (!object.hasNext()) {
            throw new NoSuchElementException();
        }
        float f2 = ((Number)function1.invoke((Map.Entry<K, V>)object.next())).floatValue();
        while (object.hasNext()) {
            float f3 = ((Number)function1.invoke((Map.Entry<K, V>)object.next())).floatValue();
            f2 = Math.min(f2, f3);
        }
        return f2;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V, R extends Comparable<? super R>> R minOf(Map<? extends K, ? extends V> object, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        object = ((Iterable)object.entrySet()).iterator();
        if (!object.hasNext()) {
            throw new NoSuchElementException();
        }
        Comparable comparable = (Comparable)function1.invoke((Map.Entry<K, V>)object.next());
        while (object.hasNext()) {
            Comparable comparable2 = (Comparable)function1.invoke((Map.Entry<K, V>)object.next());
            if (comparable.compareTo(comparable2) <= 0) continue;
            comparable = comparable2;
        }
        return (R)comparable;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V> Double minOfOrNull(Map<? extends K, ? extends V> object, Function1<? super Map.Entry<? extends K, ? extends V>, Double> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        object = ((Iterable)object.entrySet()).iterator();
        if (!object.hasNext()) {
            return null;
        }
        double d2 = ((Number)function1.invoke((Map.Entry<K, V>)object.next())).doubleValue();
        while (object.hasNext()) {
            double d3 = ((Number)function1.invoke((Map.Entry<K, V>)object.next())).doubleValue();
            d2 = Math.min(d2, d3);
        }
        return d2;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V> Float minOfOrNull(Map<? extends K, ? extends V> object, Function1<? super Map.Entry<? extends K, ? extends V>, Float> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        object = ((Iterable)object.entrySet()).iterator();
        if (!object.hasNext()) {
            return null;
        }
        float f2 = ((Number)function1.invoke((Map.Entry<K, V>)object.next())).floatValue();
        while (object.hasNext()) {
            float f3 = ((Number)function1.invoke((Map.Entry<K, V>)object.next())).floatValue();
            f2 = Math.min(f2, f3);
        }
        return Float.valueOf(f2);
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V, R extends Comparable<? super R>> R minOfOrNull(Map<? extends K, ? extends V> object, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        object = ((Iterable)object.entrySet()).iterator();
        if (!object.hasNext()) {
            return null;
        }
        Comparable comparable = (Comparable)function1.invoke((Map.Entry<K, V>)object.next());
        while (object.hasNext()) {
            Comparable comparable2 = (Comparable)function1.invoke((Map.Entry<K, V>)object.next());
            if (comparable.compareTo(comparable2) <= 0) continue;
            comparable = comparable2;
        }
        return (R)comparable;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V, R> R minOfWith(Map<? extends K, ? extends V> object, Comparator<? super R> comparator, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        Intrinsics.checkNotNullParameter(function1, "");
        object = ((Iterable)object.entrySet()).iterator();
        if (!object.hasNext()) {
            throw new NoSuchElementException();
        }
        R r2 = function1.invoke((Map.Entry<K, V>)object.next());
        while (object.hasNext()) {
            R r3 = function1.invoke((Map.Entry<K, V>)object.next());
            if (comparator.compare(r2, r3) <= 0) continue;
            r2 = r3;
        }
        return r2;
    }

    @SinceKotlin(version="1.4")
    @OverloadResolutionByLambdaReturnType
    @InlineOnly
    private static final <K, V, R> R minOfWithOrNull(Map<? extends K, ? extends V> object, Comparator<? super R> comparator, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        Intrinsics.checkNotNullParameter(function1, "");
        object = ((Iterable)object.entrySet()).iterator();
        if (!object.hasNext()) {
            return null;
        }
        R r2 = function1.invoke((Map.Entry<K, V>)object.next());
        while (object.hasNext()) {
            R r3 = function1.invoke((Map.Entry<K, V>)object.next());
            if (comparator.compare(r2, r3) <= 0) continue;
            r2 = r3;
        }
        return r2;
    }

    @SinceKotlin(version="1.7")
    @JvmName(name="minWithOrThrow")
    @InlineOnly
    private static final <K, V> Map.Entry<K, V> minWithOrThrow(Map<? extends K, ? extends V> map, Comparator<? super Map.Entry<? extends K, ? extends V>> comparator) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        return CollectionsKt.minWithOrThrow((Iterable)map.entrySet(), comparator);
    }

    @SinceKotlin(version="1.4")
    @InlineOnly
    private static final <K, V> Map.Entry<K, V> minWithOrNull(Map<? extends K, ? extends V> map, Comparator<? super Map.Entry<? extends K, ? extends V>> comparator) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(comparator, "");
        return CollectionsKt.minWithOrNull((Iterable)map.entrySet(), comparator);
    }

    public static final <K, V> boolean none(@NotNull Map<? extends K, ? extends V> map) {
        Intrinsics.checkNotNullParameter(map, "");
        return map.isEmpty();
    }

    public static final <K, V> boolean none(@NotNull Map<? extends K, ? extends V> object, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        if (object.isEmpty()) {
            return true;
        }
        for (Map.Entry entry : object.entrySet()) {
            if (!function1.invoke(entry).booleanValue()) continue;
            return false;
        }
        return true;
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <K, V, M extends Map<? extends K, ? extends V>> M onEach(@NotNull M m2, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Unit> function1) {
        Intrinsics.checkNotNullParameter(m2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        M m3 = m2;
        for (Map.Entry entry : m2.entrySet()) {
            function1.invoke(entry);
        }
        return m2;
    }

    @SinceKotlin(version="1.4")
    @NotNull
    public static final <K, V, M extends Map<? extends K, ? extends V>> M onEachIndexed(@NotNull M m2, @NotNull Function2<? super Integer, ? super Map.Entry<? extends K, ? extends V>, Unit> function2) {
        Intrinsics.checkNotNullParameter(m2, "");
        Intrinsics.checkNotNullParameter(function2, "");
        Object object = m2;
        object = m2.entrySet();
        int n2 = 0;
        object = object.iterator();
        while (object.hasNext()) {
            int n3;
            Object e2 = object.next();
            if ((n3 = n2++) < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            function2.invoke((Integer)((Integer)Integer.valueOf(n3)), (Map.Entry<K, V>)e2);
        }
        return m2;
    }

    @InlineOnly
    private static final <K, V> Iterable<Map.Entry<K, V>> asIterable(Map<? extends K, ? extends V> map) {
        Intrinsics.checkNotNullParameter(map, "");
        return map.entrySet();
    }

    @NotNull
    public static final <K, V> Sequence<Map.Entry<K, V>> asSequence(@NotNull Map<? extends K, ? extends V> map) {
        Intrinsics.checkNotNullParameter(map, "");
        return CollectionsKt.asSequence((Iterable)map.entrySet());
    }
}

