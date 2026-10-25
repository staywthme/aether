/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.BuilderInference;
import kotlin.ExperimentalStdlibApi;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.SinceKotlin;
import kotlin.Unit;
import kotlin.WasExperimental;
import kotlin.collections.CollectionsKt;
import kotlin.collections.EmptyMap;
import kotlin.collections.MapsKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000~\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010%\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010&\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010(\n\u0002\u0010)\n\u0002\u0010'\n\u0002\b\n\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0017\u001a`\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0006\u0010\u0004\u001a\u00020\u00052%\b\u0001\u0010\u0006\u001a\u001f\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b\u0012\u0004\u0012\u00020\t0\u0007\u00a2\u0006\u0002\b\nH\u0087\b\u00f8\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0001\u001aX\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032%\b\u0001\u0010\u0006\u001a\u001f\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b\u0012\u0004\u0012\u00020\t0\u0007\u00a2\u0006\u0002\b\nH\u0087\b\u00f8\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u001a\u001e\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\u001a1\u0010\f\u001a\u001e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\rj\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003`\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003H\u0087\b\u001a_\u0010\f\u001a\u001e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\rj\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003`\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032*\u0010\u000f\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110\u0010\"\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011\u00a2\u0006\u0002\u0010\u0012\u001a1\u0010\u0013\u001a\u001e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0014j\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003`\u0015\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003H\u0087\b\u001a_\u0010\u0013\u001a\u001e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0014j\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003`\u0015\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032*\u0010\u000f\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110\u0010\"\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011\u00a2\u0006\u0002\u0010\u0016\u001a!\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003H\u0087\b\u001aO\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032*\u0010\u000f\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110\u0010\"\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011\u00a2\u0006\u0002\u0010\u0018\u001a!\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003H\u0087\b\u001aO\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032*\u0010\u000f\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110\u0010\"\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011\u00a2\u0006\u0002\u0010\u0018\u001a*\u0010\u001a\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001bH\u0087\n\u00a2\u0006\u0002\u0010\u001c\u001a*\u0010\u001d\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001bH\u0087\n\u00a2\u0006\u0002\u0010\u001c\u001a9\u0010\u001e\u001a\u00020\u001f\"\t\b\u0000\u0010\u0002\u00a2\u0006\u0002\b \"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010!\u001a\u0002H\u0002H\u0087\n\u00a2\u0006\u0002\u0010\"\u001a1\u0010#\u001a\u00020\u001f\"\t\b\u0000\u0010\u0002\u00a2\u0006\u0002\b *\u000e\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0002\b\u00030\u00012\u0006\u0010!\u001a\u0002H\u0002H\u0087\b\u00a2\u0006\u0002\u0010\"\u001a7\u0010$\u001a\u00020\u001f\"\u0004\b\u0000\u0010\u0002\"\t\b\u0001\u0010\u0003\u00a2\u0006\u0002\b *\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010%\u001a\u0002H\u0003H\u0087\b\u00a2\u0006\u0002\u0010\"\u001aV\u0010&\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001b\u0012\u0004\u0012\u00020\u001f0\u0007H\u0086\b\u00f8\u0001\u0000\u001aJ\u0010(\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0012\u0010'\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u001f0\u0007H\u0086\b\u00f8\u0001\u0000\u001aV\u0010)\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001b\u0012\u0004\u0012\u00020\u001f0\u0007H\u0086\b\u00f8\u0001\u0000\u001aq\u0010*\u001a\u0002H+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0018\b\u0002\u0010+*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010,\u001a\u0002H+2\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001b\u0012\u0004\u0012\u00020\u001f0\u0007H\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010-\u001aq\u0010.\u001a\u0002H+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0018\b\u0002\u0010+*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010,\u001a\u0002H+2\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001b\u0012\u0004\u0012\u00020\u001f0\u0007H\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010-\u001aJ\u0010/\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0012\u0010'\u001a\u000e\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\u001f0\u0007H\u0086\b\u00f8\u0001\u0000\u001a;\u00100\u001a\u0004\u0018\u0001H\u0003\"\t\b\u0000\u0010\u0002\u00a2\u0006\u0002\b \"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010!\u001a\u0002H\u0002H\u0087\n\u00a2\u0006\u0002\u00101\u001aC\u00102\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010!\u001a\u0002H\u00022\f\u00103\u001a\b\u0012\u0004\u0012\u0002H\u000304H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u00105\u001aC\u00106\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010!\u001a\u0002H\u00022\f\u00103\u001a\b\u0012\u0004\u0012\u0002H\u000304H\u0080\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u00105\u001aC\u00107\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b2\u0006\u0010!\u001a\u0002H\u00022\f\u00103\u001a\b\u0012\u0004\u0012\u0002H\u000304H\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u00105\u001a1\u00108\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010!\u001a\u0002H\u0002H\u0007\u00a2\u0006\u0002\u00101\u001a?\u00109\u001a\u0002H:\"\u0014\b\u0000\u0010+*\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0001*\u0002H:\"\u0004\b\u0001\u0010:*\u0002H+2\f\u00103\u001a\b\u0012\u0004\u0012\u0002H:04H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010;\u001a'\u0010<\u001a\u00020\u001f\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\b\u001a:\u0010=\u001a\u00020\u001f\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0012\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0001H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a9\u0010>\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001b0?\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\n\u001a<\u0010>\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030A0@\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\bH\u0087\n\u00a2\u0006\u0002\bB\u001a\\\u0010C\u001a\u000e\u0012\u0004\u0012\u0002H:\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010:*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u001e\u0010D\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001b\u0012\u0004\u0012\u0002H:0\u0007H\u0086\b\u00f8\u0001\u0000\u001aw\u0010E\u001a\u0002H+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010:\"\u0018\b\u0003\u0010+*\u0012\u0012\u0006\b\u0000\u0012\u0002H:\u0012\u0006\b\u0000\u0012\u0002H\u00030\b*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010,\u001a\u0002H+2\u001e\u0010D\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001b\u0012\u0004\u0012\u0002H:0\u0007H\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010-\u001a\\\u0010F\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H:0\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010:*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u001e\u0010D\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001b\u0012\u0004\u0012\u0002H:0\u0007H\u0086\b\u00f8\u0001\u0000\u001aw\u0010G\u001a\u0002H+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010:\"\u0018\b\u0003\u0010+*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H:0\b*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010,\u001a\u0002H+2\u001e\u0010D\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001b\u0012\u0004\u0012\u0002H:0\u0007H\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010-\u001a@\u0010H\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010!\u001a\u0002H\u0002H\u0087\u0002\u00a2\u0006\u0002\u0010I\u001aH\u0010H\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u000e\u0010J\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0010H\u0087\u0002\u00a2\u0006\u0002\u0010K\u001aA\u0010H\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\f\u0010J\u001a\b\u0012\u0004\u0012\u0002H\u00020LH\u0087\u0002\u001aA\u0010H\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\f\u0010J\u001a\b\u0012\u0004\u0012\u0002H\u00020MH\u0087\u0002\u001a2\u0010N\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b2\u0006\u0010!\u001a\u0002H\u0002H\u0087\n\u00a2\u0006\u0002\u0010O\u001a:\u0010N\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b2\u000e\u0010J\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0010H\u0087\n\u00a2\u0006\u0002\u0010P\u001a3\u0010N\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b2\f\u0010J\u001a\b\u0012\u0004\u0012\u0002H\u00020LH\u0087\n\u001a3\u0010N\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b2\f\u0010J\u001a\b\u0012\u0004\u0012\u0002H\u00020MH\u0087\n\u001a0\u0010Q\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001H\u0000\u001a3\u0010R\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0001H\u0087\b\u001aT\u0010S\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u001a\u0010\u000f\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110\u0010H\u0086\u0002\u00a2\u0006\u0002\u0010T\u001aG\u0010S\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0012\u0010U\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011H\u0086\u0002\u001aM\u0010S\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0018\u0010\u000f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110LH\u0086\u0002\u001aI\u0010S\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0014\u0010V\u001a\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001H\u0086\u0002\u001aM\u0010S\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0018\u0010\u000f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110MH\u0086\u0002\u001aJ\u0010W\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b2\u001a\u0010\u000f\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110\u0010H\u0087\n\u00a2\u0006\u0002\u0010X\u001a=\u0010W\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b2\u0012\u0010U\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011H\u0087\n\u001aC\u0010W\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b2\u0018\u0010\u000f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110LH\u0087\n\u001a=\u0010W\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b2\u0012\u0010V\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\n\u001aC\u0010W\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b2\u0018\u0010\u000f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110MH\u0087\n\u001aG\u0010Y\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b2\u001a\u0010\u000f\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110\u0010\u00a2\u0006\u0002\u0010X\u001a@\u0010Y\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b2\u0018\u0010\u000f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110L\u001a@\u0010Y\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b2\u0018\u0010\u000f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110M\u001a;\u0010Z\u001a\u0004\u0018\u0001H\u0003\"\t\b\u0000\u0010\u0002\u00a2\u0006\u0002\b \"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b2\u0006\u0010!\u001a\u0002H\u0002H\u0087\b\u00a2\u0006\u0002\u00101\u001a:\u0010[\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b2\u0006\u0010!\u001a\u0002H\u00022\u0006\u0010%\u001a\u0002H\u0003H\u0087\n\u00a2\u0006\u0002\u0010\\\u001a;\u0010]\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110\u0010\u00a2\u0006\u0002\u0010\u0018\u001aQ\u0010]\u001a\u0002H+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0018\b\u0002\u0010+*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b*\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110\u00102\u0006\u0010,\u001a\u0002H+\u00a2\u0006\u0002\u0010^\u001a4\u0010]\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110L\u001aO\u0010]\u001a\u0002H+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0018\b\u0002\u0010+*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110L2\u0006\u0010,\u001a\u0002H+\u00a2\u0006\u0002\u0010_\u001a2\u0010]\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007\u001aM\u0010]\u001a\u0002H+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0018\b\u0002\u0010+*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010,\u001a\u0002H+H\u0007\u00a2\u0006\u0002\u0010`\u001a4\u0010]\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110M\u001aO\u0010]\u001a\u0002H+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0018\b\u0002\u0010+*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110M2\u0006\u0010,\u001a\u0002H+\u00a2\u0006\u0002\u0010a\u001a2\u0010b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007\u001a1\u0010c\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001bH\u0087\b\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006d"}, d2={"buildMap", "", "K", "V", "capacity", "", "builderAction", "Lkotlin/Function1;", "", "", "Lkotlin/ExtensionFunctionType;", "emptyMap", "hashMapOf", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "pairs", "", "Lkotlin/Pair;", "([Lkotlin/Pair;)Ljava/util/HashMap;", "linkedMapOf", "Ljava/util/LinkedHashMap;", "Lkotlin/collections/LinkedHashMap;", "([Lkotlin/Pair;)Ljava/util/LinkedHashMap;", "mapOf", "([Lkotlin/Pair;)Ljava/util/Map;", "mutableMapOf", "component1", "", "(Ljava/util/Map$Entry;)Ljava/lang/Object;", "component2", "contains", "", "Lkotlin/internal/OnlyInputTypes;", "key", "(Ljava/util/Map;Ljava/lang/Object;)Z", "containsKey", "containsValue", "value", "filter", "predicate", "filterKeys", "filterNot", "filterNotTo", "M", "destination", "(Ljava/util/Map;Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "filterTo", "filterValues", "get", "(Ljava/util/Map;Ljava/lang/Object;)Ljava/lang/Object;", "getOrElse", "defaultValue", "Lkotlin/Function0;", "(Ljava/util/Map;Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "getOrElseNullable", "getOrPut", "getValue", "ifEmpty", "R", "(Ljava/util/Map;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "isNotEmpty", "isNullOrEmpty", "iterator", "", "", "", "mutableIterator", "mapKeys", "transform", "mapKeysTo", "mapValues", "mapValuesTo", "minus", "(Ljava/util/Map;Ljava/lang/Object;)Ljava/util/Map;", "keys", "(Ljava/util/Map;[Ljava/lang/Object;)Ljava/util/Map;", "", "Lkotlin/sequences/Sequence;", "minusAssign", "(Ljava/util/Map;Ljava/lang/Object;)V", "(Ljava/util/Map;[Ljava/lang/Object;)V", "optimizeReadOnlyMap", "orEmpty", "plus", "(Ljava/util/Map;[Lkotlin/Pair;)Ljava/util/Map;", "pair", "map", "plusAssign", "(Ljava/util/Map;[Lkotlin/Pair;)V", "putAll", "remove", "set", "(Ljava/util/Map;Ljava/lang/Object;Ljava/lang/Object;)V", "toMap", "([Lkotlin/Pair;Ljava/util/Map;)Ljava/util/Map;", "(Ljava/lang/Iterable;Ljava/util/Map;)Ljava/util/Map;", "(Ljava/util/Map;Ljava/util/Map;)Ljava/util/Map;", "(Lkotlin/sequences/Sequence;Ljava/util/Map;)Ljava/util/Map;", "toMutableMap", "toPair", "kotlin-stdlib"}, xs="kotlin/collections/MapsKt")
@SourceDebugExtension(value={"SMAP\nMaps.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Maps.kt\nkotlin/collections/MapsKt__MapsKt\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,793:1\n392#1:803\n403#1:808\n500#1,6:813\n525#1,6:819\n1#2:794\n1238#3,4:795\n1238#3,4:799\n1238#3,4:804\n1238#3,4:809\n*S KotlinDebug\n*F\n+ 1 Maps.kt\nkotlin/collections/MapsKt__MapsKt\n*L\n442#1:803\n457#1:808\n515#1:813,6\n540#1:819,6\n392#1:795,4\n403#1:799,4\n442#1:804,4\n457#1:809,4\n*E\n"})
public class MapsKt__MapsKt
extends MapsKt__MapsJVMKt {
    @NotNull
    public static final <K, V> Map<K, V> emptyMap() {
        EmptyMap emptyMap = EmptyMap.INSTANCE;
        Intrinsics.checkNotNull(emptyMap);
        return emptyMap;
    }

    @NotNull
    public static final <K, V> Map<K, V> mapOf(Pair<? extends K, ? extends V> ... pairArray) {
        Intrinsics.checkNotNullParameter(pairArray, "");
        if (pairArray.length > 0) {
            return MapsKt.toMap(pairArray, (Map)new LinkedHashMap(MapsKt.mapCapacity(pairArray.length)));
        }
        return MapsKt.emptyMap();
    }

    @InlineOnly
    private static final <K, V> Map<K, V> mapOf() {
        return MapsKt.emptyMap();
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final <K, V> Map<K, V> mutableMapOf() {
        return new LinkedHashMap();
    }

    @NotNull
    public static final <K, V> Map<K, V> mutableMapOf(Pair<? extends K, ? extends V> ... pairArray) {
        LinkedHashMap linkedHashMap;
        Intrinsics.checkNotNullParameter(pairArray, "");
        LinkedHashMap linkedHashMap2 = linkedHashMap = new LinkedHashMap(MapsKt.mapCapacity(pairArray.length));
        MapsKt.putAll((Map)linkedHashMap, pairArray);
        return linkedHashMap;
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final <K, V> HashMap<K, V> hashMapOf() {
        return new HashMap();
    }

    @NotNull
    public static final <K, V> HashMap<K, V> hashMapOf(Pair<? extends K, ? extends V> ... pairArray) {
        HashMap hashMap;
        Intrinsics.checkNotNullParameter(pairArray, "");
        HashMap hashMap2 = hashMap = new HashMap(MapsKt.mapCapacity(pairArray.length));
        MapsKt.putAll((Map)hashMap, pairArray);
        return hashMap;
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final <K, V> LinkedHashMap<K, V> linkedMapOf() {
        return new LinkedHashMap();
    }

    @NotNull
    public static final <K, V> LinkedHashMap<K, V> linkedMapOf(Pair<? extends K, ? extends V> ... pairArray) {
        Intrinsics.checkNotNullParameter(pairArray, "");
        return (LinkedHashMap)MapsKt.toMap(pairArray, (Map)new LinkedHashMap(MapsKt.mapCapacity(pairArray.length)));
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final <K, V> Map<K, V> buildMap(@BuilderInference Function1<? super Map<K, V>, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "");
        Map map = MapsKt.createMapBuilder();
        function1.invoke(map);
        return MapsKt.build(map);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final <K, V> Map<K, V> buildMap(int n2, @BuilderInference Function1<? super Map<K, V>, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "");
        Map map = MapsKt.createMapBuilder(n2);
        function1.invoke(map);
        return MapsKt.build(map);
    }

    @InlineOnly
    private static final <K, V> boolean isNotEmpty(Map<? extends K, ? extends V> map) {
        Intrinsics.checkNotNullParameter(map, "");
        return !map.isEmpty();
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <K, V> boolean isNullOrEmpty(Map<? extends K, ? extends V> map) {
        return map == null || map.isEmpty();
    }

    @InlineOnly
    private static final <K, V> Map<K, V> orEmpty(Map<K, ? extends V> map) {
        Map<K, Object> map2 = map;
        if (map2 == null) {
            map2 = MapsKt.emptyMap();
        }
        return map2;
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <M extends Map<?, ?> & R, R> R ifEmpty(M m2, Function0<? extends R> function0) {
        Intrinsics.checkNotNullParameter(function0, "");
        if (m2.isEmpty()) {
            return function0.invoke();
        }
        return m2;
    }

    @InlineOnly
    private static final <K, V> boolean contains(Map<? extends K, ? extends V> map, K k2) {
        Intrinsics.checkNotNullParameter(map, "");
        return map.containsKey(k2);
    }

    @InlineOnly
    private static final <K, V> V get(Map<? extends K, ? extends V> map, K k2) {
        Intrinsics.checkNotNullParameter(map, "");
        return map.get(k2);
    }

    @InlineOnly
    private static final <K, V> void set(Map<K, V> map, K k2, V v2) {
        Intrinsics.checkNotNullParameter(map, "");
        map.put(k2, v2);
    }

    @InlineOnly
    private static final <K> boolean containsKey(Map<? extends K, ?> map, K k2) {
        Intrinsics.checkNotNullParameter(map, "");
        return map.containsKey(k2);
    }

    @InlineOnly
    private static final <K, V> boolean containsValue(Map<K, ? extends V> map, V v2) {
        Intrinsics.checkNotNullParameter(map, "");
        return map.containsValue(v2);
    }

    @InlineOnly
    private static final <K, V> V remove(Map<? extends K, V> map, K k2) {
        Intrinsics.checkNotNullParameter(map, "");
        return TypeIntrinsics.asMutableMap(map).remove(k2);
    }

    @InlineOnly
    private static final <K, V> K component1(Map.Entry<? extends K, ? extends V> entry) {
        Intrinsics.checkNotNullParameter(entry, "");
        return entry.getKey();
    }

    @InlineOnly
    private static final <K, V> V component2(Map.Entry<? extends K, ? extends V> entry) {
        Intrinsics.checkNotNullParameter(entry, "");
        return entry.getValue();
    }

    @InlineOnly
    private static final <K, V> Pair<K, V> toPair(Map.Entry<? extends K, ? extends V> entry) {
        Intrinsics.checkNotNullParameter(entry, "");
        return new Pair<K, V>(entry.getKey(), entry.getValue());
    }

    @InlineOnly
    private static final <K, V> V getOrElse(Map<K, ? extends V> map, K k2, Function0<? extends V> function0) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(function0, "");
        V v2 = map.get(k2);
        if (v2 == null) {
            v2 = function0.invoke();
        }
        return v2;
    }

    public static final <K, V> V getOrElseNullable(@NotNull Map<K, ? extends V> map, K k2, @NotNull Function0<? extends V> function0) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(function0, "");
        V v2 = map.get(k2);
        if (v2 == null && !map.containsKey(k2)) {
            return function0.invoke();
        }
        return v2;
    }

    @SinceKotlin(version="1.1")
    public static final <K, V> V getValue(@NotNull Map<K, ? extends V> map, K k2) {
        Intrinsics.checkNotNullParameter(map, "");
        return MapsKt.getOrImplicitDefaultNullable(map, k2);
    }

    public static final <K, V> V getOrPut(@NotNull Map<K, V> map, K k2, @NotNull Function0<? extends V> function0) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(function0, "");
        V v2 = map.get(k2);
        if (v2 == null) {
            function0 = function0.invoke();
            map.put(k2, function0);
            return (V)function0;
        }
        return v2;
    }

    @InlineOnly
    private static final <K, V> Iterator<Map.Entry<K, V>> iterator(Map<? extends K, ? extends V> map) {
        Intrinsics.checkNotNullParameter(map, "");
        return map.entrySet().iterator();
    }

    @JvmName(name="mutableIterator")
    @InlineOnly
    private static final <K, V> Iterator<Map.Entry<K, V>> mutableIterator(Map<K, V> map) {
        Intrinsics.checkNotNullParameter(map, "");
        return map.entrySet().iterator();
    }

    @NotNull
    public static final <K, V, R, M extends Map<? super K, ? super R>> M mapValuesTo(@NotNull Map<? extends K, ? extends V> iterator2, @NotNull M m2, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(iterator2, "");
        Intrinsics.checkNotNullParameter(m2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        iterator2 = iterator2.entrySet();
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            Object t2 = iterator2.next();
            Map.Entry entry = (Map.Entry)t2;
            M m3 = m2;
            m3.put(entry.getKey(), function1.invoke((Map.Entry<K, V>)t2));
        }
        return m2;
    }

    @NotNull
    public static final <K, V, R, M extends Map<? super R, ? super V>> M mapKeysTo(@NotNull Map<? extends K, ? extends V> iterator2, @NotNull M m2, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(iterator2, "");
        Intrinsics.checkNotNullParameter(m2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        iterator2 = iterator2.entrySet();
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            Object object = iterator2.next();
            R r2 = function1.invoke((Map.Entry<K, V>)object);
            object = (Map.Entry)object;
            R r3 = r2;
            M m3 = m2;
            object = object.getValue();
            m3.put(r3, object);
        }
        return m2;
    }

    public static final <K, V> void putAll(@NotNull Map<? super K, ? super V> map, @NotNull Pair<? extends K, ? extends V>[] pairArray) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(pairArray, "");
        for (Pair<K, V> pair : pairArray) {
            K k2 = pair.component1();
            V pair2 = pair.component2();
            map.put(k2, (Pair<K, V>)pair2);
        }
    }

    public static final <K, V> void putAll(@NotNull Map<? super K, ? super V> map, @NotNull Iterable<? extends Pair<? extends K, ? extends V>> object) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(object, "");
        object = object.iterator();
        while (object.hasNext()) {
            Pair pair = (Pair)object.next();
            Object a2 = pair.component1();
            pair = pair.component2();
            map.put(a2, pair);
        }
    }

    public static final <K, V> void putAll(@NotNull Map<? super K, ? super V> map, @NotNull Sequence<? extends Pair<? extends K, ? extends V>> object) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(object, "");
        object = object.iterator();
        while (object.hasNext()) {
            Pair pair = (Pair)object.next();
            Object a2 = pair.component1();
            pair = pair.component2();
            map.put(a2, pair);
        }
    }

    @NotNull
    public static final <K, V, R> Map<K, R> mapValues(@NotNull Map<? extends K, ? extends V> map, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(function1, "");
        Iterator iterator2 = map;
        map = new LinkedHashMap(MapsKt.mapCapacity(map.size()));
        iterator2 = iterator2.entrySet();
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            Object t2 = iterator2.next();
            Map.Entry entry = (Map.Entry)t2;
            Map map2 = map;
            map2.put(entry.getKey(), function1.invoke((Map.Entry<K, V>)t2));
        }
        return map;
    }

    @NotNull
    public static final <K, V, R> Map<R, V> mapKeys(@NotNull Map<? extends K, ? extends V> map, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(function1, "");
        Iterator iterator2 = map;
        map = new LinkedHashMap(MapsKt.mapCapacity(map.size()));
        iterator2 = iterator2.entrySet();
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            Object object = iterator2.next();
            R r2 = function1.invoke((Map.Entry<K, V>)object);
            object = (Map.Entry)object;
            R r3 = r2;
            Map map2 = map;
            object = object.getValue();
            map2.put(r3, object);
        }
        return map;
    }

    @NotNull
    public static final <K, V> Map<K, V> filterKeys(@NotNull Map<? extends K, ? extends V> object, @NotNull Function1<? super K, Boolean> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Map.Entry entry : object.entrySet()) {
            if (!function1.invoke(entry.getKey()).booleanValue()) continue;
            linkedHashMap.put(entry.getKey(), entry.getValue());
        }
        return linkedHashMap;
    }

    @NotNull
    public static final <K, V> Map<K, V> filterValues(@NotNull Map<? extends K, ? extends V> object, @NotNull Function1<? super V, Boolean> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Map.Entry entry : object.entrySet()) {
            if (!function1.invoke(entry.getValue()).booleanValue()) continue;
            linkedHashMap.put(entry.getKey(), entry.getValue());
        }
        return linkedHashMap;
    }

    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M filterTo(@NotNull Map<? extends K, ? extends V> object, @NotNull M m2, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(m2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        for (Map.Entry entry : object.entrySet()) {
            if (!function1.invoke(entry).booleanValue()) continue;
            m2.put(entry.getKey(), entry.getValue());
        }
        return m2;
    }

    @NotNull
    public static final <K, V> Map<K, V> filter(@NotNull Map<? extends K, ? extends V> object, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        Map map = new LinkedHashMap();
        for (Map.Entry entry : object.entrySet()) {
            if (!function1.invoke(entry).booleanValue()) continue;
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M filterNotTo(@NotNull Map<? extends K, ? extends V> object, @NotNull M m2, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(m2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        for (Map.Entry entry : object.entrySet()) {
            if (function1.invoke(entry).booleanValue()) continue;
            m2.put(entry.getKey(), entry.getValue());
        }
        return m2;
    }

    @NotNull
    public static final <K, V> Map<K, V> filterNot(@NotNull Map<? extends K, ? extends V> object, @NotNull Function1<? super Map.Entry<? extends K, ? extends V>, Boolean> function1) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(function1, "");
        Map map = new LinkedHashMap();
        for (Map.Entry entry : object.entrySet()) {
            if (function1.invoke(entry).booleanValue()) continue;
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    @NotNull
    public static final <K, V> Map<K, V> toMap(@NotNull Iterable<? extends Pair<? extends K, ? extends V>> iterable) {
        Intrinsics.checkNotNullParameter(iterable, "");
        if (iterable instanceof Collection) {
            switch (((Collection)iterable).size()) {
                case 0: {
                    return MapsKt.emptyMap();
                }
                case 1: {
                    return MapsKt.mapOf(iterable instanceof List ? (Pair)((List)iterable).get(0) : iterable.iterator().next());
                }
            }
            return MapsKt.toMap(iterable, (Map)new LinkedHashMap(MapsKt.mapCapacity(((Collection)iterable).size())));
        }
        return MapsKt.optimizeReadOnlyMap(MapsKt.toMap(iterable, (Map)new LinkedHashMap()));
    }

    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M toMap(@NotNull Iterable<? extends Pair<? extends K, ? extends V>> iterable, @NotNull M m2) {
        Intrinsics.checkNotNullParameter(iterable, "");
        Intrinsics.checkNotNullParameter(m2, "");
        M m3 = m2;
        MapsKt.putAll(m2, iterable);
        return m2;
    }

    @NotNull
    public static final <K, V> Map<K, V> toMap(@NotNull Pair<? extends K, ? extends V>[] pairArray) {
        Intrinsics.checkNotNullParameter(pairArray, "");
        switch (pairArray.length) {
            case 0: {
                return MapsKt.emptyMap();
            }
            case 1: {
                return MapsKt.mapOf(pairArray[0]);
            }
        }
        return MapsKt.toMap(pairArray, (Map)new LinkedHashMap(MapsKt.mapCapacity(pairArray.length)));
    }

    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M toMap(@NotNull Pair<? extends K, ? extends V>[] pairArray, @NotNull M m2) {
        Intrinsics.checkNotNullParameter(pairArray, "");
        Intrinsics.checkNotNullParameter(m2, "");
        M m3 = m2;
        MapsKt.putAll(m2, pairArray);
        return m2;
    }

    @NotNull
    public static final <K, V> Map<K, V> toMap(@NotNull Sequence<? extends Pair<? extends K, ? extends V>> sequence2) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        return MapsKt.optimizeReadOnlyMap(MapsKt.toMap(sequence2, (Map)new LinkedHashMap()));
    }

    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M toMap(@NotNull Sequence<? extends Pair<? extends K, ? extends V>> sequence2, @NotNull M m2) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        Intrinsics.checkNotNullParameter(m2, "");
        M m3 = m2;
        MapsKt.putAll(m2, sequence2);
        return m2;
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <K, V> Map<K, V> toMap(@NotNull Map<? extends K, ? extends V> map) {
        Intrinsics.checkNotNullParameter(map, "");
        switch (map.size()) {
            case 0: {
                return MapsKt.emptyMap();
            }
            case 1: {
                return MapsKt.toSingletonMap(map);
            }
        }
        return MapsKt.toMutableMap(map);
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <K, V> Map<K, V> toMutableMap(@NotNull Map<? extends K, ? extends V> map) {
        Intrinsics.checkNotNullParameter(map, "");
        return new LinkedHashMap<K, V>(map);
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M toMap(@NotNull Map<? extends K, ? extends V> map, @NotNull M m2) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(m2, "");
        M m3 = m2;
        m2.putAll(map);
        return m2;
    }

    @NotNull
    public static final <K, V> Map<K, V> plus(@NotNull Map<? extends K, ? extends V> map, @NotNull Pair<? extends K, ? extends V> pair) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(pair, "");
        if (map.isEmpty()) {
            return MapsKt.mapOf(pair);
        }
        map = new LinkedHashMap<K, V>(map);
        LinkedHashMap<K, V> linkedHashMap = map;
        ((HashMap)map).put(pair.getFirst(), pair.getSecond());
        return map;
    }

    @NotNull
    public static final <K, V> Map<K, V> plus(@NotNull Map<? extends K, ? extends V> map, @NotNull Iterable<? extends Pair<? extends K, ? extends V>> iterable) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(iterable, "");
        if (map.isEmpty()) {
            return MapsKt.toMap(iterable);
        }
        map = new LinkedHashMap<K, V>(map);
        LinkedHashMap<? extends K, ? extends V> linkedHashMap = map;
        MapsKt.putAll((Map)map, iterable);
        return map;
    }

    @NotNull
    public static final <K, V> Map<K, V> plus(@NotNull Map<? extends K, ? extends V> map, @NotNull Pair<? extends K, ? extends V>[] pairArray) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(pairArray, "");
        if (map.isEmpty()) {
            return MapsKt.toMap(pairArray);
        }
        map = new LinkedHashMap<K, V>(map);
        LinkedHashMap<? extends K, ? extends V> linkedHashMap = map;
        MapsKt.putAll((Map)map, pairArray);
        return map;
    }

    @NotNull
    public static final <K, V> Map<K, V> plus(@NotNull Map<? extends K, ? extends V> map, @NotNull Sequence<? extends Pair<? extends K, ? extends V>> sequence2) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(sequence2, "");
        map = new LinkedHashMap<K, V>(map);
        LinkedHashMap<? extends K, ? extends V> linkedHashMap = map;
        MapsKt.putAll((Map)map, sequence2);
        return MapsKt.optimizeReadOnlyMap(map);
    }

    @NotNull
    public static final <K, V> Map<K, V> plus(@NotNull Map<? extends K, ? extends V> map, @NotNull Map<? extends K, ? extends V> map2) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(map2, "");
        map = new LinkedHashMap<K, V>(map);
        LinkedHashMap<? extends K, ? extends V> linkedHashMap = map;
        ((HashMap)map).putAll(map2);
        return map;
    }

    @InlineOnly
    private static final <K, V> void plusAssign(Map<? super K, ? super V> map, Pair<? extends K, ? extends V> pair) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(pair, "");
        map.put(pair.getFirst(), pair.getSecond());
    }

    @InlineOnly
    private static final <K, V> void plusAssign(Map<? super K, ? super V> map, Iterable<? extends Pair<? extends K, ? extends V>> iterable) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(iterable, "");
        MapsKt.putAll(map, iterable);
    }

    @InlineOnly
    private static final <K, V> void plusAssign(Map<? super K, ? super V> map, Pair<? extends K, ? extends V>[] pairArray) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(pairArray, "");
        MapsKt.putAll(map, pairArray);
    }

    @InlineOnly
    private static final <K, V> void plusAssign(Map<? super K, ? super V> map, Sequence<? extends Pair<? extends K, ? extends V>> sequence2) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(sequence2, "");
        MapsKt.putAll(map, sequence2);
    }

    @InlineOnly
    private static final <K, V> void plusAssign(Map<? super K, ? super V> map, Map<K, ? extends V> map2) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(map2, "");
        map.putAll(map2);
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <K, V> Map<K, V> minus(@NotNull Map<? extends K, ? extends V> map, K k2) {
        Intrinsics.checkNotNullParameter(map, "");
        map = MapsKt.toMutableMap(map);
        Map<K, V> map2 = map;
        map.remove(k2);
        return MapsKt.optimizeReadOnlyMap(map);
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <K, V> Map<K, V> minus(@NotNull Map<? extends K, ? extends V> map, @NotNull Iterable<? extends K> iterable) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(iterable, "");
        map = MapsKt.toMutableMap(map);
        Map<K, V> map2 = map;
        CollectionsKt.removeAll((Collection)map.keySet(), iterable);
        return MapsKt.optimizeReadOnlyMap(map);
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <K, V> Map<K, V> minus(@NotNull Map<? extends K, ? extends V> map, @NotNull K[] KArray) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(KArray, "");
        map = MapsKt.toMutableMap(map);
        Map<K, V> map2 = map;
        CollectionsKt.removeAll((Collection)map.keySet(), KArray);
        return MapsKt.optimizeReadOnlyMap(map);
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <K, V> Map<K, V> minus(@NotNull Map<? extends K, ? extends V> map, @NotNull Sequence<? extends K> sequence2) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(sequence2, "");
        map = MapsKt.toMutableMap(map);
        Map<K, V> map2 = map;
        CollectionsKt.removeAll((Collection)map.keySet(), sequence2);
        return MapsKt.optimizeReadOnlyMap(map);
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final <K, V> void minusAssign(Map<K, V> map, K k2) {
        Intrinsics.checkNotNullParameter(map, "");
        map.remove(k2);
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final <K, V> void minusAssign(Map<K, V> map, Iterable<? extends K> iterable) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(iterable, "");
        CollectionsKt.removeAll((Collection)map.keySet(), iterable);
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final <K, V> void minusAssign(Map<K, V> map, K[] KArray) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(KArray, "");
        CollectionsKt.removeAll((Collection)map.keySet(), KArray);
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final <K, V> void minusAssign(Map<K, V> map, Sequence<? extends K> sequence2) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(sequence2, "");
        CollectionsKt.removeAll((Collection)map.keySet(), sequence2);
    }

    @NotNull
    public static final <K, V> Map<K, V> optimizeReadOnlyMap(@NotNull Map<K, ? extends V> map) {
        Intrinsics.checkNotNullParameter(map, "");
        switch (map.size()) {
            case 0: {
                return MapsKt.emptyMap();
            }
            case 1: {
                return MapsKt.toSingletonMap(map);
            }
        }
        return map;
    }
}

