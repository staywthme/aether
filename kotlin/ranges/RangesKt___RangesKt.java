/*
 * Decompiled with CFR 0.152.
 */
package kotlin.ranges;

import java.util.NoSuchElementException;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.ExperimentalStdlibApi;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.WasExperimental;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.random.Random;
import kotlin.random.RandomKt;
import kotlin.ranges.CharProgression;
import kotlin.ranges.CharRange;
import kotlin.ranges.ClosedFloatingPointRange;
import kotlin.ranges.ClosedRange;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.LongProgression;
import kotlin.ranges.LongRange;
import kotlin.ranges.OpenEndRange;
import kotlin.ranges.RangesKt;
import kotlin.ranges.RangesKt__RangesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000t\n\u0002\b\u0002\n\u0002\u0010\u000f\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u001d\u001a'\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\u0006\u0010\u0003\u001a\u0002H\u0001\u00a2\u0006\u0002\u0010\u0004\u001a\u0012\u0010\u0000\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005\u001a\u0012\u0010\u0000\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0006\u001a\u0012\u0010\u0000\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0007\u001a\u0012\u0010\u0000\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\b\u001a\u0012\u0010\u0000\u001a\u00020\t*\u00020\t2\u0006\u0010\u0003\u001a\u00020\t\u001a\u0012\u0010\u0000\u001a\u00020\n*\u00020\n2\u0006\u0010\u0003\u001a\u00020\n\u001a'\u0010\u000b\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\u0006\u0010\f\u001a\u0002H\u0001\u00a2\u0006\u0002\u0010\u0004\u001a\u0012\u0010\u000b\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\f\u001a\u00020\u0005\u001a\u0012\u0010\u000b\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006\u001a\u0012\u0010\u000b\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\f\u001a\u00020\u0007\u001a\u0012\u0010\u000b\u001a\u00020\b*\u00020\b2\u0006\u0010\f\u001a\u00020\b\u001a\u0012\u0010\u000b\u001a\u00020\t*\u00020\t2\u0006\u0010\f\u001a\u00020\t\u001a\u0012\u0010\u000b\u001a\u00020\n*\u00020\n2\u0006\u0010\f\u001a\u00020\n\u001a3\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\b\u0010\u0003\u001a\u0004\u0018\u0001H\u00012\b\u0010\f\u001a\u0004\u0018\u0001H\u0001\u00a2\u0006\u0002\u0010\u000e\u001a/\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0010H\u0007\u00a2\u0006\u0002\u0010\u0011\u001a-\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0012\u00a2\u0006\u0002\u0010\u0013\u001a\u001a\u0010\r\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u0005\u001a\u001a\u0010\r\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006\u001a\u001a\u0010\r\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0007\u001a\u001a\u0010\r\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b\u001a\u0018\u0010\r\u001a\u00020\b*\u00020\b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\b0\u0012\u001a\u001a\u0010\r\u001a\u00020\t*\u00020\t2\u0006\u0010\u0003\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\t\u001a\u0018\u0010\r\u001a\u00020\t*\u00020\t2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\t0\u0012\u001a\u001a\u0010\r\u001a\u00020\n*\u00020\n2\u0006\u0010\u0003\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\n\u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0087\n\u00a2\u0006\u0002\u0010\u0019\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002\u00a2\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002\u00a2\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002\u00a2\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002\u00a2\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002\u00a2\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002\u00a2\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002\u00a2\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002\u00a2\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002\u00a2\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002\u00a2\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002\u00a2\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002\u00a2\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002\u00a2\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002\u00a2\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002\u00a2\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002\u00a2\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002\u00a2\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002\u00a2\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002\u00a2\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002\u00a2\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002\u00a2\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002\u00a2\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002\u00a2\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002\u00a2\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002\u00a2\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002\u00a2\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002\u00a2\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002\u00a2\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002\u00a2\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002\u00a2\u0006\u0002\b \u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020!2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\n\u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020!2\b\u0010\u0017\u001a\u0004\u0018\u00010\bH\u0087\n\u00a2\u0006\u0002\u0010\"\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020!2\u0006\u0010\u001a\u001a\u00020\tH\u0087\n\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020!2\u0006\u0010\u001a\u001a\u00020\nH\u0087\n\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020#2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\n\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020#2\u0006\u0010\u001a\u001a\u00020\bH\u0087\n\u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020#2\b\u0010\u0017\u001a\u0004\u0018\u00010\tH\u0087\n\u00a2\u0006\u0002\u0010$\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020#2\u0006\u0010\u001a\u001a\u00020\nH\u0087\n\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050%2\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002\u00a2\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050%2\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002\u00a2\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050%2\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002\u00a2\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060%2\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002\u00a2\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0%2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002\u00a2\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0%2\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002\u00a2\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0%2\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002\u00a2\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0%2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002\u00a2\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0%2\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002\u00a2\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0%2\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002\u00a2\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0%2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002\u00a2\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0%2\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002\u00a2\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0%2\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002\u00a2\u0006\u0002\b \u001a\u0015\u0010&\u001a\u00020'*\u00020\u00052\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\u00052\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\u00052\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\u00052\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020**\u00020\u00182\u0006\u0010(\u001a\u00020\u0018H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\b2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\b2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\b2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\b2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\t2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\t2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\t2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\t2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\n2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\n2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\n2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\n2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\f\u0010+\u001a\u00020\u0018*\u00020*H\u0007\u001a\f\u0010+\u001a\u00020\b*\u00020'H\u0007\u001a\f\u0010+\u001a\u00020\t*\u00020)H\u0007\u001a\u0013\u0010,\u001a\u0004\u0018\u00010\u0018*\u00020*H\u0007\u00a2\u0006\u0002\u0010-\u001a\u0013\u0010,\u001a\u0004\u0018\u00010\b*\u00020'H\u0007\u00a2\u0006\u0002\u0010.\u001a\u0013\u0010,\u001a\u0004\u0018\u00010\t*\u00020)H\u0007\u00a2\u0006\u0002\u0010/\u001a\f\u00100\u001a\u00020\u0018*\u00020*H\u0007\u001a\f\u00100\u001a\u00020\b*\u00020'H\u0007\u001a\f\u00100\u001a\u00020\t*\u00020)H\u0007\u001a\u0013\u00101\u001a\u0004\u0018\u00010\u0018*\u00020*H\u0007\u00a2\u0006\u0002\u0010-\u001a\u0013\u00101\u001a\u0004\u0018\u00010\b*\u00020'H\u0007\u00a2\u0006\u0002\u0010.\u001a\u0013\u00101\u001a\u0004\u0018\u00010\t*\u00020)H\u0007\u00a2\u0006\u0002\u0010/\u001a\r\u00102\u001a\u00020\u0018*\u00020\u0016H\u0087\b\u001a\u0014\u00102\u001a\u00020\u0018*\u00020\u00162\u0006\u00102\u001a\u000203H\u0007\u001a\r\u00102\u001a\u00020\b*\u00020!H\u0087\b\u001a\u0014\u00102\u001a\u00020\b*\u00020!2\u0006\u00102\u001a\u000203H\u0007\u001a\r\u00102\u001a\u00020\t*\u00020#H\u0087\b\u001a\u0014\u00102\u001a\u00020\t*\u00020#2\u0006\u00102\u001a\u000203H\u0007\u001a\u0014\u00104\u001a\u0004\u0018\u00010\u0018*\u00020\u0016H\u0087\b\u00a2\u0006\u0002\u00105\u001a\u001b\u00104\u001a\u0004\u0018\u00010\u0018*\u00020\u00162\u0006\u00102\u001a\u000203H\u0007\u00a2\u0006\u0002\u00106\u001a\u0014\u00104\u001a\u0004\u0018\u00010\b*\u00020!H\u0087\b\u00a2\u0006\u0002\u00107\u001a\u001b\u00104\u001a\u0004\u0018\u00010\b*\u00020!2\u0006\u00102\u001a\u000203H\u0007\u00a2\u0006\u0002\u00108\u001a\u0014\u00104\u001a\u0004\u0018\u00010\t*\u00020#H\u0087\b\u00a2\u0006\u0002\u00109\u001a\u001b\u00104\u001a\u0004\u0018\u00010\t*\u00020#2\u0006\u00102\u001a\u000203H\u0007\u00a2\u0006\u0002\u0010:\u001a\n\u0010;\u001a\u00020**\u00020*\u001a\n\u0010;\u001a\u00020'*\u00020'\u001a\n\u0010;\u001a\u00020)*\u00020)\u001a\u0015\u0010<\u001a\u00020**\u00020*2\u0006\u0010<\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010<\u001a\u00020'*\u00020'2\u0006\u0010<\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010<\u001a\u00020)*\u00020)2\u0006\u0010<\u001a\u00020\tH\u0086\u0004\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\u0006H\u0000\u00a2\u0006\u0002\u0010>\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\u0007H\u0000\u00a2\u0006\u0002\u0010?\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\bH\u0000\u00a2\u0006\u0002\u0010@\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\tH\u0000\u00a2\u0006\u0002\u0010A\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\nH\u0000\u00a2\u0006\u0002\u0010B\u001a\u0013\u0010C\u001a\u0004\u0018\u00010\b*\u00020\u0006H\u0000\u00a2\u0006\u0002\u0010D\u001a\u0013\u0010C\u001a\u0004\u0018\u00010\b*\u00020\u0007H\u0000\u00a2\u0006\u0002\u0010E\u001a\u0013\u0010C\u001a\u0004\u0018\u00010\b*\u00020\tH\u0000\u00a2\u0006\u0002\u0010F\u001a\u0013\u0010G\u001a\u0004\u0018\u00010\t*\u00020\u0006H\u0000\u00a2\u0006\u0002\u0010H\u001a\u0013\u0010G\u001a\u0004\u0018\u00010\t*\u00020\u0007H\u0000\u00a2\u0006\u0002\u0010I\u001a\u0013\u0010J\u001a\u0004\u0018\u00010\n*\u00020\u0006H\u0000\u00a2\u0006\u0002\u0010K\u001a\u0013\u0010J\u001a\u0004\u0018\u00010\n*\u00020\u0007H\u0000\u00a2\u0006\u0002\u0010L\u001a\u0013\u0010J\u001a\u0004\u0018\u00010\n*\u00020\bH\u0000\u00a2\u0006\u0002\u0010M\u001a\u0013\u0010J\u001a\u0004\u0018\u00010\n*\u00020\tH\u0000\u00a2\u0006\u0002\u0010N\u001a\u0015\u0010O\u001a\u00020!*\u00020\u00052\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\u00052\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\u00052\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\u00052\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020\u0016*\u00020\u00182\u0006\u0010(\u001a\u00020\u0018H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\b2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\b2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\b2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\b2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\t2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\t2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\t2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\t2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\n2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\n2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\n2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\n2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u00a8\u0006P"}, d2={"coerceAtLeast", "T", "", "minimumValue", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "", "", "", "", "", "", "coerceAtMost", "maximumValue", "coerceIn", "(Ljava/lang/Comparable;Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "range", "Lkotlin/ranges/ClosedFloatingPointRange;", "(Ljava/lang/Comparable;Lkotlin/ranges/ClosedFloatingPointRange;)Ljava/lang/Comparable;", "Lkotlin/ranges/ClosedRange;", "(Ljava/lang/Comparable;Lkotlin/ranges/ClosedRange;)Ljava/lang/Comparable;", "contains", "", "Lkotlin/ranges/CharRange;", "element", "", "(Lkotlin/ranges/CharRange;Ljava/lang/Character;)Z", "value", "byteRangeContains", "doubleRangeContains", "floatRangeContains", "intRangeContains", "longRangeContains", "shortRangeContains", "Lkotlin/ranges/IntRange;", "(Lkotlin/ranges/IntRange;Ljava/lang/Integer;)Z", "Lkotlin/ranges/LongRange;", "(Lkotlin/ranges/LongRange;Ljava/lang/Long;)Z", "Lkotlin/ranges/OpenEndRange;", "downTo", "Lkotlin/ranges/IntProgression;", "to", "Lkotlin/ranges/LongProgression;", "Lkotlin/ranges/CharProgression;", "first", "firstOrNull", "(Lkotlin/ranges/CharProgression;)Ljava/lang/Character;", "(Lkotlin/ranges/IntProgression;)Ljava/lang/Integer;", "(Lkotlin/ranges/LongProgression;)Ljava/lang/Long;", "last", "lastOrNull", "random", "Lkotlin/random/Random;", "randomOrNull", "(Lkotlin/ranges/CharRange;)Ljava/lang/Character;", "(Lkotlin/ranges/CharRange;Lkotlin/random/Random;)Ljava/lang/Character;", "(Lkotlin/ranges/IntRange;)Ljava/lang/Integer;", "(Lkotlin/ranges/IntRange;Lkotlin/random/Random;)Ljava/lang/Integer;", "(Lkotlin/ranges/LongRange;)Ljava/lang/Long;", "(Lkotlin/ranges/LongRange;Lkotlin/random/Random;)Ljava/lang/Long;", "reversed", "step", "toByteExactOrNull", "(D)Ljava/lang/Byte;", "(F)Ljava/lang/Byte;", "(I)Ljava/lang/Byte;", "(J)Ljava/lang/Byte;", "(S)Ljava/lang/Byte;", "toIntExactOrNull", "(D)Ljava/lang/Integer;", "(F)Ljava/lang/Integer;", "(J)Ljava/lang/Integer;", "toLongExactOrNull", "(D)Ljava/lang/Long;", "(F)Ljava/lang/Long;", "toShortExactOrNull", "(D)Ljava/lang/Short;", "(F)Ljava/lang/Short;", "(I)Ljava/lang/Short;", "(J)Ljava/lang/Short;", "until", "kotlin-stdlib"}, xs="kotlin/ranges/RangesKt")
@SourceDebugExtension(value={"SMAP\n_Ranges.kt\nKotlin\n*S Kotlin\n*F\n+ 1 _Ranges.kt\nkotlin/ranges/RangesKt___RangesKt\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,1537:1\n1#2:1538\n*E\n"})
public class RangesKt___RangesKt
extends RangesKt__RangesKt {
    @SinceKotlin(version="1.7")
    public static final int first(@NotNull IntProgression intProgression) {
        Intrinsics.checkNotNullParameter(intProgression, "");
        if (intProgression.isEmpty()) {
            throw new NoSuchElementException("Progression " + intProgression + " is empty.");
        }
        return intProgression.getFirst();
    }

    @SinceKotlin(version="1.7")
    public static final long first(@NotNull LongProgression longProgression) {
        Intrinsics.checkNotNullParameter(longProgression, "");
        if (longProgression.isEmpty()) {
            throw new NoSuchElementException("Progression " + longProgression + " is empty.");
        }
        return longProgression.getFirst();
    }

    @SinceKotlin(version="1.7")
    public static final char first(@NotNull CharProgression charProgression) {
        Intrinsics.checkNotNullParameter(charProgression, "");
        if (charProgression.isEmpty()) {
            throw new NoSuchElementException("Progression " + charProgression + " is empty.");
        }
        return charProgression.getFirst();
    }

    @SinceKotlin(version="1.7")
    @Nullable
    public static final Integer firstOrNull(@NotNull IntProgression intProgression) {
        Intrinsics.checkNotNullParameter(intProgression, "");
        if (intProgression.isEmpty()) {
            return null;
        }
        return intProgression.getFirst();
    }

    @SinceKotlin(version="1.7")
    @Nullable
    public static final Long firstOrNull(@NotNull LongProgression longProgression) {
        Intrinsics.checkNotNullParameter(longProgression, "");
        if (longProgression.isEmpty()) {
            return null;
        }
        return longProgression.getFirst();
    }

    @SinceKotlin(version="1.7")
    @Nullable
    public static final Character firstOrNull(@NotNull CharProgression charProgression) {
        Intrinsics.checkNotNullParameter(charProgression, "");
        if (charProgression.isEmpty()) {
            return null;
        }
        return Character.valueOf(charProgression.getFirst());
    }

    @SinceKotlin(version="1.7")
    public static final int last(@NotNull IntProgression intProgression) {
        Intrinsics.checkNotNullParameter(intProgression, "");
        if (intProgression.isEmpty()) {
            throw new NoSuchElementException("Progression " + intProgression + " is empty.");
        }
        return intProgression.getLast();
    }

    @SinceKotlin(version="1.7")
    public static final long last(@NotNull LongProgression longProgression) {
        Intrinsics.checkNotNullParameter(longProgression, "");
        if (longProgression.isEmpty()) {
            throw new NoSuchElementException("Progression " + longProgression + " is empty.");
        }
        return longProgression.getLast();
    }

    @SinceKotlin(version="1.7")
    public static final char last(@NotNull CharProgression charProgression) {
        Intrinsics.checkNotNullParameter(charProgression, "");
        if (charProgression.isEmpty()) {
            throw new NoSuchElementException("Progression " + charProgression + " is empty.");
        }
        return charProgression.getLast();
    }

    @SinceKotlin(version="1.7")
    @Nullable
    public static final Integer lastOrNull(@NotNull IntProgression intProgression) {
        Intrinsics.checkNotNullParameter(intProgression, "");
        if (intProgression.isEmpty()) {
            return null;
        }
        return intProgression.getLast();
    }

    @SinceKotlin(version="1.7")
    @Nullable
    public static final Long lastOrNull(@NotNull LongProgression longProgression) {
        Intrinsics.checkNotNullParameter(longProgression, "");
        if (longProgression.isEmpty()) {
            return null;
        }
        return longProgression.getLast();
    }

    @SinceKotlin(version="1.7")
    @Nullable
    public static final Character lastOrNull(@NotNull CharProgression charProgression) {
        Intrinsics.checkNotNullParameter(charProgression, "");
        if (charProgression.isEmpty()) {
            return null;
        }
        return Character.valueOf(charProgression.getLast());
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final int random(IntRange intRange) {
        Intrinsics.checkNotNullParameter(intRange, "");
        return RangesKt.random(intRange, (Random)Random.Default);
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final long random(LongRange longRange) {
        Intrinsics.checkNotNullParameter(longRange, "");
        return RangesKt.random(longRange, (Random)Random.Default);
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final char random(CharRange charRange) {
        Intrinsics.checkNotNullParameter(charRange, "");
        return RangesKt.random(charRange, (Random)Random.Default);
    }

    @SinceKotlin(version="1.3")
    public static final int random(@NotNull IntRange intRange, @NotNull Random random) {
        Intrinsics.checkNotNullParameter(intRange, "");
        Intrinsics.checkNotNullParameter(random, "");
        try {
            return RandomKt.nextInt(random, intRange);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new NoSuchElementException(illegalArgumentException.getMessage());
        }
    }

    @SinceKotlin(version="1.3")
    public static final long random(@NotNull LongRange longRange, @NotNull Random random) {
        Intrinsics.checkNotNullParameter(longRange, "");
        Intrinsics.checkNotNullParameter(random, "");
        try {
            return RandomKt.nextLong(random, longRange);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new NoSuchElementException(illegalArgumentException.getMessage());
        }
    }

    @SinceKotlin(version="1.3")
    public static final char random(@NotNull CharRange charRange, @NotNull Random random) {
        Intrinsics.checkNotNullParameter(charRange, "");
        Intrinsics.checkNotNullParameter(random, "");
        try {
            return (char)random.nextInt(charRange.getFirst(), charRange.getLast() + '\u0001');
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new NoSuchElementException(illegalArgumentException.getMessage());
        }
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final Integer randomOrNull(IntRange intRange) {
        Intrinsics.checkNotNullParameter(intRange, "");
        return RangesKt.randomOrNull(intRange, (Random)Random.Default);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final Long randomOrNull(LongRange longRange) {
        Intrinsics.checkNotNullParameter(longRange, "");
        return RangesKt.randomOrNull(longRange, (Random)Random.Default);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final Character randomOrNull(CharRange charRange) {
        Intrinsics.checkNotNullParameter(charRange, "");
        return RangesKt.randomOrNull(charRange, (Random)Random.Default);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @Nullable
    public static final Integer randomOrNull(@NotNull IntRange intRange, @NotNull Random random) {
        Intrinsics.checkNotNullParameter(intRange, "");
        Intrinsics.checkNotNullParameter(random, "");
        if (((IntProgression)intRange).isEmpty()) {
            return null;
        }
        return RandomKt.nextInt(random, intRange);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @Nullable
    public static final Long randomOrNull(@NotNull LongRange longRange, @NotNull Random random) {
        Intrinsics.checkNotNullParameter(longRange, "");
        Intrinsics.checkNotNullParameter(random, "");
        if (((LongProgression)longRange).isEmpty()) {
            return null;
        }
        return RandomKt.nextLong(random, longRange);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @Nullable
    public static final Character randomOrNull(@NotNull CharRange charRange, @NotNull Random random) {
        Intrinsics.checkNotNullParameter(charRange, "");
        Intrinsics.checkNotNullParameter(random, "");
        if (((CharProgression)charRange).isEmpty()) {
            return null;
        }
        return Character.valueOf((char)random.nextInt(charRange.getFirst(), charRange.getLast() + '\u0001'));
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final boolean contains(IntRange intRange, Integer n2) {
        Intrinsics.checkNotNullParameter(intRange, "");
        return n2 != null && intRange.contains(n2);
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final boolean contains(LongRange longRange, Long l2) {
        Intrinsics.checkNotNullParameter(longRange, "");
        return l2 != null && longRange.contains(l2);
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final boolean contains(CharRange charRange, Character c2) {
        Intrinsics.checkNotNullParameter(charRange, "");
        return c2 != null && charRange.contains(c2.charValue());
    }

    @JvmName(name="intRangeContains")
    public static final boolean intRangeContains(@NotNull ClosedRange<Integer> closedRange, byte by) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        return closedRange.contains((Integer)((Comparable)Integer.valueOf(by)));
    }

    @JvmName(name="longRangeContains")
    public static final boolean longRangeContains(@NotNull ClosedRange<Long> closedRange, byte by) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        return closedRange.contains((Long)((Comparable)Long.valueOf(by)));
    }

    @JvmName(name="shortRangeContains")
    public static final boolean shortRangeContains(@NotNull ClosedRange<Short> closedRange, byte by) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        return closedRange.contains((Short)((Comparable)Short.valueOf(by)));
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(warningSince="1.3", errorSince="1.4", hiddenSince="1.5")
    @JvmName(name="doubleRangeContains")
    public static final /* synthetic */ boolean doubleRangeContains(ClosedRange closedRange, byte by) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        return closedRange.contains((Comparable)Double.valueOf(by));
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(warningSince="1.3", errorSince="1.4", hiddenSince="1.5")
    @JvmName(name="floatRangeContains")
    public static final /* synthetic */ boolean floatRangeContains(ClosedRange closedRange, byte by) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        return closedRange.contains((Comparable)Float.valueOf(by));
    }

    @JvmName(name="intRangeContains")
    @SinceKotlin(version="1.7")
    @ExperimentalStdlibApi
    public static final boolean intRangeContains(@NotNull OpenEndRange<Integer> openEndRange, byte by) {
        Intrinsics.checkNotNullParameter(openEndRange, "");
        return openEndRange.contains((Integer)((Comparable)Integer.valueOf(by)));
    }

    @JvmName(name="longRangeContains")
    @SinceKotlin(version="1.7")
    @ExperimentalStdlibApi
    public static final boolean longRangeContains(@NotNull OpenEndRange<Long> openEndRange, byte by) {
        Intrinsics.checkNotNullParameter(openEndRange, "");
        return openEndRange.contains((Long)((Comparable)Long.valueOf(by)));
    }

    @JvmName(name="shortRangeContains")
    @SinceKotlin(version="1.7")
    @ExperimentalStdlibApi
    public static final boolean shortRangeContains(@NotNull OpenEndRange<Short> openEndRange, byte by) {
        Intrinsics.checkNotNullParameter(openEndRange, "");
        return openEndRange.contains((Short)((Comparable)Short.valueOf(by)));
    }

    @InlineOnly
    private static final boolean contains(IntRange intRange, byte by) {
        Intrinsics.checkNotNullParameter(intRange, "");
        return RangesKt.intRangeContains((ClosedRange<Integer>)intRange, by);
    }

    @InlineOnly
    private static final boolean contains(LongRange longRange, byte by) {
        Intrinsics.checkNotNullParameter(longRange, "");
        return RangesKt.longRangeContains((ClosedRange<Long>)longRange, by);
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(warningSince="1.3", errorSince="1.4", hiddenSince="1.5")
    @JvmName(name="intRangeContains")
    public static final /* synthetic */ boolean intRangeContains(ClosedRange closedRange, double d2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        Integer n2 = RangesKt.toIntExactOrNull(d2);
        Integer n3 = n2;
        n3 = n2;
        if (n2 != null) {
            return closedRange.contains((Comparable)n2);
        }
        return false;
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(warningSince="1.3", errorSince="1.4", hiddenSince="1.5")
    @JvmName(name="longRangeContains")
    public static final /* synthetic */ boolean longRangeContains(ClosedRange closedRange, double d2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        Long l2 = RangesKt.toLongExactOrNull(d2);
        Long l3 = l2;
        l3 = l2;
        if (l2 != null) {
            return closedRange.contains((Comparable)l2);
        }
        return false;
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(warningSince="1.3", errorSince="1.4", hiddenSince="1.5")
    @JvmName(name="byteRangeContains")
    public static final /* synthetic */ boolean byteRangeContains(ClosedRange closedRange, double d2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        Byte by = RangesKt.toByteExactOrNull(d2);
        Byte by2 = by;
        by2 = by;
        if (by != null) {
            return closedRange.contains((Comparable)by);
        }
        return false;
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(warningSince="1.3", errorSince="1.4", hiddenSince="1.5")
    @JvmName(name="shortRangeContains")
    public static final /* synthetic */ boolean shortRangeContains(ClosedRange closedRange, double d2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        Short s2 = RangesKt.toShortExactOrNull(d2);
        Short s3 = s2;
        s3 = s2;
        if (s2 != null) {
            return closedRange.contains((Comparable)s2);
        }
        return false;
    }

    @JvmName(name="floatRangeContains")
    public static final boolean floatRangeContains(@NotNull ClosedRange<Float> closedRange, double d2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        return closedRange.contains((Float)((Comparable)Float.valueOf((float)d2)));
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(warningSince="1.3", errorSince="1.4", hiddenSince="1.5")
    @JvmName(name="intRangeContains")
    public static final /* synthetic */ boolean intRangeContains(ClosedRange closedRange, float f2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        Integer n2 = RangesKt.toIntExactOrNull(f2);
        Integer n3 = n2;
        n3 = n2;
        if (n2 != null) {
            return closedRange.contains((Comparable)n2);
        }
        return false;
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(warningSince="1.3", errorSince="1.4", hiddenSince="1.5")
    @JvmName(name="longRangeContains")
    public static final /* synthetic */ boolean longRangeContains(ClosedRange closedRange, float f2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        Long l2 = RangesKt.toLongExactOrNull(f2);
        Long l3 = l2;
        l3 = l2;
        if (l2 != null) {
            return closedRange.contains((Comparable)l2);
        }
        return false;
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(warningSince="1.3", errorSince="1.4", hiddenSince="1.5")
    @JvmName(name="byteRangeContains")
    public static final /* synthetic */ boolean byteRangeContains(ClosedRange closedRange, float f2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        Byte by = RangesKt.toByteExactOrNull(f2);
        Byte by2 = by;
        by2 = by;
        if (by != null) {
            return closedRange.contains((Comparable)by);
        }
        return false;
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(warningSince="1.3", errorSince="1.4", hiddenSince="1.5")
    @JvmName(name="shortRangeContains")
    public static final /* synthetic */ boolean shortRangeContains(ClosedRange closedRange, float f2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        Short s2 = RangesKt.toShortExactOrNull(f2);
        Short s3 = s2;
        s3 = s2;
        if (s2 != null) {
            return closedRange.contains((Comparable)s2);
        }
        return false;
    }

    @JvmName(name="doubleRangeContains")
    public static final boolean doubleRangeContains(@NotNull ClosedRange<Double> closedRange, float f2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        return closedRange.contains((Double)((Comparable)Double.valueOf(f2)));
    }

    @JvmName(name="doubleRangeContains")
    @SinceKotlin(version="1.7")
    @ExperimentalStdlibApi
    public static final boolean doubleRangeContains(@NotNull OpenEndRange<Double> openEndRange, float f2) {
        Intrinsics.checkNotNullParameter(openEndRange, "");
        return openEndRange.contains((Double)((Comparable)Double.valueOf(f2)));
    }

    @JvmName(name="longRangeContains")
    public static final boolean longRangeContains(@NotNull ClosedRange<Long> closedRange, int n2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        return closedRange.contains((Long)((Comparable)Long.valueOf(n2)));
    }

    @JvmName(name="byteRangeContains")
    public static final boolean byteRangeContains(@NotNull ClosedRange<Byte> closedRange, int n2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        Byte by = RangesKt.toByteExactOrNull(n2);
        Byte by2 = by;
        by2 = by;
        if (by != null) {
            return closedRange.contains((Byte)((Comparable)by));
        }
        return false;
    }

    @JvmName(name="shortRangeContains")
    public static final boolean shortRangeContains(@NotNull ClosedRange<Short> closedRange, int n2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        Short s2 = RangesKt.toShortExactOrNull(n2);
        Short s3 = s2;
        s3 = s2;
        if (s2 != null) {
            return closedRange.contains((Short)((Comparable)s2));
        }
        return false;
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(warningSince="1.3", errorSince="1.4", hiddenSince="1.5")
    @JvmName(name="doubleRangeContains")
    public static final /* synthetic */ boolean doubleRangeContains(ClosedRange closedRange, int n2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        return closedRange.contains((Comparable)Double.valueOf(n2));
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(warningSince="1.3", errorSince="1.4", hiddenSince="1.5")
    @JvmName(name="floatRangeContains")
    public static final /* synthetic */ boolean floatRangeContains(ClosedRange closedRange, int n2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        return closedRange.contains((Comparable)Float.valueOf(n2));
    }

    @JvmName(name="longRangeContains")
    @SinceKotlin(version="1.7")
    @ExperimentalStdlibApi
    public static final boolean longRangeContains(@NotNull OpenEndRange<Long> openEndRange, int n2) {
        Intrinsics.checkNotNullParameter(openEndRange, "");
        return openEndRange.contains((Long)((Comparable)Long.valueOf(n2)));
    }

    @JvmName(name="byteRangeContains")
    @SinceKotlin(version="1.7")
    @ExperimentalStdlibApi
    public static final boolean byteRangeContains(@NotNull OpenEndRange<Byte> openEndRange, int n2) {
        Intrinsics.checkNotNullParameter(openEndRange, "");
        Byte by = RangesKt.toByteExactOrNull(n2);
        Byte by2 = by;
        by2 = by;
        if (by != null) {
            return openEndRange.contains((Byte)((Comparable)by));
        }
        return false;
    }

    @JvmName(name="shortRangeContains")
    @SinceKotlin(version="1.7")
    @ExperimentalStdlibApi
    public static final boolean shortRangeContains(@NotNull OpenEndRange<Short> openEndRange, int n2) {
        Intrinsics.checkNotNullParameter(openEndRange, "");
        Short s2 = RangesKt.toShortExactOrNull(n2);
        Short s3 = s2;
        s3 = s2;
        if (s2 != null) {
            return openEndRange.contains((Short)((Comparable)s2));
        }
        return false;
    }

    @InlineOnly
    private static final boolean contains(LongRange longRange, int n2) {
        Intrinsics.checkNotNullParameter(longRange, "");
        return RangesKt.longRangeContains((ClosedRange<Long>)longRange, n2);
    }

    @JvmName(name="intRangeContains")
    public static final boolean intRangeContains(@NotNull ClosedRange<Integer> closedRange, long l2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        Integer n2 = RangesKt.toIntExactOrNull(l2);
        Integer n3 = n2;
        n3 = n2;
        if (n2 != null) {
            return closedRange.contains((Integer)((Comparable)n2));
        }
        return false;
    }

    @JvmName(name="byteRangeContains")
    public static final boolean byteRangeContains(@NotNull ClosedRange<Byte> closedRange, long l2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        Byte by = RangesKt.toByteExactOrNull(l2);
        Byte by2 = by;
        by2 = by;
        if (by != null) {
            return closedRange.contains((Byte)((Comparable)by));
        }
        return false;
    }

    @JvmName(name="shortRangeContains")
    public static final boolean shortRangeContains(@NotNull ClosedRange<Short> closedRange, long l2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        Short s2 = RangesKt.toShortExactOrNull(l2);
        Short s3 = s2;
        s3 = s2;
        if (s2 != null) {
            return closedRange.contains((Short)((Comparable)s2));
        }
        return false;
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(warningSince="1.3", errorSince="1.4", hiddenSince="1.5")
    @JvmName(name="doubleRangeContains")
    public static final /* synthetic */ boolean doubleRangeContains(ClosedRange closedRange, long l2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        return closedRange.contains((Comparable)Double.valueOf(l2));
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(warningSince="1.3", errorSince="1.4", hiddenSince="1.5")
    @JvmName(name="floatRangeContains")
    public static final /* synthetic */ boolean floatRangeContains(ClosedRange closedRange, long l2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        return closedRange.contains((Comparable)Float.valueOf(l2));
    }

    @JvmName(name="intRangeContains")
    @SinceKotlin(version="1.7")
    @ExperimentalStdlibApi
    public static final boolean intRangeContains(@NotNull OpenEndRange<Integer> openEndRange, long l2) {
        Intrinsics.checkNotNullParameter(openEndRange, "");
        Integer n2 = RangesKt.toIntExactOrNull(l2);
        Integer n3 = n2;
        n3 = n2;
        if (n2 != null) {
            return openEndRange.contains((Integer)((Comparable)n2));
        }
        return false;
    }

    @JvmName(name="byteRangeContains")
    @SinceKotlin(version="1.7")
    @ExperimentalStdlibApi
    public static final boolean byteRangeContains(@NotNull OpenEndRange<Byte> openEndRange, long l2) {
        Intrinsics.checkNotNullParameter(openEndRange, "");
        Byte by = RangesKt.toByteExactOrNull(l2);
        Byte by2 = by;
        by2 = by;
        if (by != null) {
            return openEndRange.contains((Byte)((Comparable)by));
        }
        return false;
    }

    @JvmName(name="shortRangeContains")
    @SinceKotlin(version="1.7")
    @ExperimentalStdlibApi
    public static final boolean shortRangeContains(@NotNull OpenEndRange<Short> openEndRange, long l2) {
        Intrinsics.checkNotNullParameter(openEndRange, "");
        Short s2 = RangesKt.toShortExactOrNull(l2);
        Short s3 = s2;
        s3 = s2;
        if (s2 != null) {
            return openEndRange.contains((Short)((Comparable)s2));
        }
        return false;
    }

    @InlineOnly
    private static final boolean contains(IntRange intRange, long l2) {
        Intrinsics.checkNotNullParameter(intRange, "");
        return RangesKt.intRangeContains((ClosedRange<Integer>)intRange, l2);
    }

    @JvmName(name="intRangeContains")
    public static final boolean intRangeContains(@NotNull ClosedRange<Integer> closedRange, short s2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        return closedRange.contains((Integer)((Comparable)Integer.valueOf(s2)));
    }

    @JvmName(name="longRangeContains")
    public static final boolean longRangeContains(@NotNull ClosedRange<Long> closedRange, short s2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        return closedRange.contains((Long)((Comparable)Long.valueOf(s2)));
    }

    @JvmName(name="byteRangeContains")
    public static final boolean byteRangeContains(@NotNull ClosedRange<Byte> closedRange, short s2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        Byte by = RangesKt.toByteExactOrNull(s2);
        Byte by2 = by;
        by2 = by;
        if (by != null) {
            return closedRange.contains((Byte)((Comparable)by));
        }
        return false;
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(warningSince="1.3", errorSince="1.4", hiddenSince="1.5")
    @JvmName(name="doubleRangeContains")
    public static final /* synthetic */ boolean doubleRangeContains(ClosedRange closedRange, short s2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        return closedRange.contains((Comparable)Double.valueOf(s2));
    }

    @Deprecated(message="This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(warningSince="1.3", errorSince="1.4", hiddenSince="1.5")
    @JvmName(name="floatRangeContains")
    public static final /* synthetic */ boolean floatRangeContains(ClosedRange closedRange, short s2) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        return closedRange.contains((Comparable)Float.valueOf(s2));
    }

    @JvmName(name="intRangeContains")
    @SinceKotlin(version="1.7")
    @ExperimentalStdlibApi
    public static final boolean intRangeContains(@NotNull OpenEndRange<Integer> openEndRange, short s2) {
        Intrinsics.checkNotNullParameter(openEndRange, "");
        return openEndRange.contains((Integer)((Comparable)Integer.valueOf(s2)));
    }

    @JvmName(name="longRangeContains")
    @SinceKotlin(version="1.7")
    @ExperimentalStdlibApi
    public static final boolean longRangeContains(@NotNull OpenEndRange<Long> openEndRange, short s2) {
        Intrinsics.checkNotNullParameter(openEndRange, "");
        return openEndRange.contains((Long)((Comparable)Long.valueOf(s2)));
    }

    @JvmName(name="byteRangeContains")
    @SinceKotlin(version="1.7")
    @ExperimentalStdlibApi
    public static final boolean byteRangeContains(@NotNull OpenEndRange<Byte> openEndRange, short s2) {
        Intrinsics.checkNotNullParameter(openEndRange, "");
        Byte by = RangesKt.toByteExactOrNull(s2);
        Byte by2 = by;
        by2 = by;
        if (by != null) {
            return openEndRange.contains((Byte)((Comparable)by));
        }
        return false;
    }

    @InlineOnly
    private static final boolean contains(IntRange intRange, short s2) {
        Intrinsics.checkNotNullParameter(intRange, "");
        return RangesKt.intRangeContains((ClosedRange<Integer>)intRange, s2);
    }

    @InlineOnly
    private static final boolean contains(LongRange longRange, short s2) {
        Intrinsics.checkNotNullParameter(longRange, "");
        return RangesKt.longRangeContains((ClosedRange<Long>)longRange, s2);
    }

    @NotNull
    public static final IntProgression downTo(int n2, byte by) {
        return IntProgression.Companion.fromClosedRange(n2, by, -1);
    }

    @NotNull
    public static final LongProgression downTo(long l2, byte by) {
        return LongProgression.Companion.fromClosedRange(l2, by, -1L);
    }

    @NotNull
    public static final IntProgression downTo(byte by, byte by2) {
        return IntProgression.Companion.fromClosedRange(by, by2, -1);
    }

    @NotNull
    public static final IntProgression downTo(short s2, byte by) {
        return IntProgression.Companion.fromClosedRange(s2, by, -1);
    }

    @NotNull
    public static final CharProgression downTo(char c2, char c3) {
        return CharProgression.Companion.fromClosedRange(c2, c3, -1);
    }

    @NotNull
    public static final IntProgression downTo(int n2, int n3) {
        return IntProgression.Companion.fromClosedRange(n2, n3, -1);
    }

    @NotNull
    public static final LongProgression downTo(long l2, int n2) {
        return LongProgression.Companion.fromClosedRange(l2, n2, -1L);
    }

    @NotNull
    public static final IntProgression downTo(byte by, int n2) {
        return IntProgression.Companion.fromClosedRange(by, n2, -1);
    }

    @NotNull
    public static final IntProgression downTo(short s2, int n2) {
        return IntProgression.Companion.fromClosedRange(s2, n2, -1);
    }

    @NotNull
    public static final LongProgression downTo(int n2, long l2) {
        return LongProgression.Companion.fromClosedRange(n2, l2, -1L);
    }

    @NotNull
    public static final LongProgression downTo(long l2, long l3) {
        return LongProgression.Companion.fromClosedRange(l2, l3, -1L);
    }

    @NotNull
    public static final LongProgression downTo(byte by, long l2) {
        return LongProgression.Companion.fromClosedRange(by, l2, -1L);
    }

    @NotNull
    public static final LongProgression downTo(short s2, long l2) {
        return LongProgression.Companion.fromClosedRange(s2, l2, -1L);
    }

    @NotNull
    public static final IntProgression downTo(int n2, short s2) {
        return IntProgression.Companion.fromClosedRange(n2, s2, -1);
    }

    @NotNull
    public static final LongProgression downTo(long l2, short s2) {
        return LongProgression.Companion.fromClosedRange(l2, s2, -1L);
    }

    @NotNull
    public static final IntProgression downTo(byte by, short s2) {
        return IntProgression.Companion.fromClosedRange(by, s2, -1);
    }

    @NotNull
    public static final IntProgression downTo(short s2, short s3) {
        return IntProgression.Companion.fromClosedRange(s2, s3, -1);
    }

    @NotNull
    public static final IntProgression reversed(@NotNull IntProgression intProgression) {
        Intrinsics.checkNotNullParameter(intProgression, "");
        return IntProgression.Companion.fromClosedRange(intProgression.getLast(), intProgression.getFirst(), -intProgression.getStep());
    }

    @NotNull
    public static final LongProgression reversed(@NotNull LongProgression longProgression) {
        Intrinsics.checkNotNullParameter(longProgression, "");
        return LongProgression.Companion.fromClosedRange(longProgression.getLast(), longProgression.getFirst(), -longProgression.getStep());
    }

    @NotNull
    public static final CharProgression reversed(@NotNull CharProgression charProgression) {
        Intrinsics.checkNotNullParameter(charProgression, "");
        return CharProgression.Companion.fromClosedRange(charProgression.getLast(), charProgression.getFirst(), -charProgression.getStep());
    }

    @NotNull
    public static final IntProgression step(@NotNull IntProgression intProgression, int n2) {
        Intrinsics.checkNotNullParameter(intProgression, "");
        RangesKt.checkStepIsPositive(n2 > 0, n2);
        return IntProgression.Companion.fromClosedRange(intProgression.getFirst(), intProgression.getLast(), intProgression.getStep() > 0 ? n2 : -n2);
    }

    @NotNull
    public static final LongProgression step(@NotNull LongProgression longProgression, long l2) {
        Intrinsics.checkNotNullParameter(longProgression, "");
        RangesKt.checkStepIsPositive(l2 > 0L, l2);
        return LongProgression.Companion.fromClosedRange(longProgression.getFirst(), longProgression.getLast(), longProgression.getStep() > 0L ? l2 : -l2);
    }

    @NotNull
    public static final CharProgression step(@NotNull CharProgression charProgression, int n2) {
        Intrinsics.checkNotNullParameter(charProgression, "");
        RangesKt.checkStepIsPositive(n2 > 0, n2);
        return CharProgression.Companion.fromClosedRange(charProgression.getFirst(), charProgression.getLast(), charProgression.getStep() > 0 ? n2 : -n2);
    }

    @Nullable
    public static final Byte toByteExactOrNull(int n2) {
        if (new IntRange(-128, 127).contains(n2)) {
            return (byte)n2;
        }
        return null;
    }

    @Nullable
    public static final Byte toByteExactOrNull(long l2) {
        if (new LongRange(-128L, 127L).contains(l2)) {
            return (byte)l2;
        }
        return null;
    }

    @Nullable
    public static final Byte toByteExactOrNull(short s2) {
        if (RangesKt.intRangeContains((ClosedRange<Integer>)new IntRange(-128, 127), s2)) {
            return (byte)s2;
        }
        return null;
    }

    @Nullable
    public static final Byte toByteExactOrNull(double d2) {
        boolean bl = -128.0 <= d2 ? d2 <= 127.0 : false;
        if (bl) {
            return (byte)d2;
        }
        return null;
    }

    @Nullable
    public static final Byte toByteExactOrNull(float f2) {
        boolean bl = -128.0f <= f2 ? f2 <= 127.0f : false;
        if (bl) {
            return (byte)f2;
        }
        return null;
    }

    @Nullable
    public static final Integer toIntExactOrNull(long l2) {
        if (new LongRange(Integer.MIN_VALUE, Integer.MAX_VALUE).contains(l2)) {
            return (int)l2;
        }
        return null;
    }

    @Nullable
    public static final Integer toIntExactOrNull(double d2) {
        boolean bl = -2.147483648E9 <= d2 ? d2 <= 2.147483647E9 : false;
        if (bl) {
            return (int)d2;
        }
        return null;
    }

    @Nullable
    public static final Integer toIntExactOrNull(float f2) {
        boolean bl = -2.14748365E9f <= f2 ? f2 <= 2.14748365E9f : false;
        if (bl) {
            return (int)f2;
        }
        return null;
    }

    @Nullable
    public static final Long toLongExactOrNull(double d2) {
        boolean bl = -9.223372036854776E18 <= d2 ? d2 <= 9.223372036854776E18 : false;
        if (bl) {
            return (long)d2;
        }
        return null;
    }

    @Nullable
    public static final Long toLongExactOrNull(float f2) {
        boolean bl = -9.223372E18f <= f2 ? f2 <= 9.223372E18f : false;
        if (bl) {
            return (long)f2;
        }
        return null;
    }

    @Nullable
    public static final Short toShortExactOrNull(int n2) {
        if (new IntRange(Short.MIN_VALUE, Short.MAX_VALUE).contains(n2)) {
            return (short)n2;
        }
        return null;
    }

    @Nullable
    public static final Short toShortExactOrNull(long l2) {
        if (new LongRange(-32768L, 32767L).contains(l2)) {
            return (short)l2;
        }
        return null;
    }

    @Nullable
    public static final Short toShortExactOrNull(double d2) {
        boolean bl = -32768.0 <= d2 ? d2 <= 32767.0 : false;
        if (bl) {
            return (short)d2;
        }
        return null;
    }

    @Nullable
    public static final Short toShortExactOrNull(float f2) {
        boolean bl = -32768.0f <= f2 ? f2 <= 32767.0f : false;
        if (bl) {
            return (short)f2;
        }
        return null;
    }

    @NotNull
    public static final IntRange until(int n2, byte by) {
        return new IntRange(n2, by - 1);
    }

    @NotNull
    public static final LongRange until(long l2, byte by) {
        return new LongRange(l2, (long)by - 1L);
    }

    @NotNull
    public static final IntRange until(byte by, byte by2) {
        return new IntRange(by, by2 - 1);
    }

    @NotNull
    public static final IntRange until(short s2, byte by) {
        return new IntRange(s2, by - 1);
    }

    @NotNull
    public static final CharRange until(char c2, char c3) {
        if (Intrinsics.compare(c3, 0) <= 0) {
            return CharRange.Companion.getEMPTY();
        }
        return new CharRange(c2, (char)(c3 - '\u0001'));
    }

    @NotNull
    public static final IntRange until(int n2, int n3) {
        if (n3 <= Integer.MIN_VALUE) {
            return IntRange.Companion.getEMPTY();
        }
        return new IntRange(n2, n3 - 1);
    }

    @NotNull
    public static final LongRange until(long l2, int n2) {
        return new LongRange(l2, (long)n2 - 1L);
    }

    @NotNull
    public static final IntRange until(byte by, int n2) {
        if (n2 <= Integer.MIN_VALUE) {
            return IntRange.Companion.getEMPTY();
        }
        return new IntRange(by, n2 - 1);
    }

    @NotNull
    public static final IntRange until(short s2, int n2) {
        if (n2 <= Integer.MIN_VALUE) {
            return IntRange.Companion.getEMPTY();
        }
        return new IntRange(s2, n2 - 1);
    }

    @NotNull
    public static final LongRange until(int n2, long l2) {
        if (l2 <= Long.MIN_VALUE) {
            return LongRange.Companion.getEMPTY();
        }
        return new LongRange(n2, l2 - 1L);
    }

    @NotNull
    public static final LongRange until(long l2, long l3) {
        if (l3 <= Long.MIN_VALUE) {
            return LongRange.Companion.getEMPTY();
        }
        return new LongRange(l2, l3 - 1L);
    }

    @NotNull
    public static final LongRange until(byte by, long l2) {
        if (l2 <= Long.MIN_VALUE) {
            return LongRange.Companion.getEMPTY();
        }
        return new LongRange(by, l2 - 1L);
    }

    @NotNull
    public static final LongRange until(short s2, long l2) {
        if (l2 <= Long.MIN_VALUE) {
            return LongRange.Companion.getEMPTY();
        }
        return new LongRange(s2, l2 - 1L);
    }

    @NotNull
    public static final IntRange until(int n2, short s2) {
        return new IntRange(n2, s2 - 1);
    }

    @NotNull
    public static final LongRange until(long l2, short s2) {
        return new LongRange(l2, (long)s2 - 1L);
    }

    @NotNull
    public static final IntRange until(byte by, short s2) {
        return new IntRange(by, s2 - 1);
    }

    @NotNull
    public static final IntRange until(short s2, short s3) {
        return new IntRange(s2, s3 - 1);
    }

    @NotNull
    public static final <T extends Comparable<? super T>> T coerceAtLeast(@NotNull T t2, @NotNull T t3) {
        Intrinsics.checkNotNullParameter(t2, "");
        Intrinsics.checkNotNullParameter(t3, "");
        if (t2.compareTo(t3) < 0) {
            return t3;
        }
        return t2;
    }

    public static final byte coerceAtLeast(byte by, byte by2) {
        if (by < by2) {
            return by2;
        }
        return by;
    }

    public static final short coerceAtLeast(short s2, short s3) {
        if (s2 < s3) {
            return s3;
        }
        return s2;
    }

    public static final int coerceAtLeast(int n2, int n3) {
        if (n2 < n3) {
            return n3;
        }
        return n2;
    }

    public static final long coerceAtLeast(long l2, long l3) {
        if (l2 < l3) {
            return l3;
        }
        return l2;
    }

    public static final float coerceAtLeast(float f2, float f3) {
        if (f2 < f3) {
            return f3;
        }
        return f2;
    }

    public static final double coerceAtLeast(double d2, double d3) {
        if (d2 < d3) {
            return d3;
        }
        return d2;
    }

    @NotNull
    public static final <T extends Comparable<? super T>> T coerceAtMost(@NotNull T t2, @NotNull T t3) {
        Intrinsics.checkNotNullParameter(t2, "");
        Intrinsics.checkNotNullParameter(t3, "");
        if (t2.compareTo(t3) > 0) {
            return t3;
        }
        return t2;
    }

    public static final byte coerceAtMost(byte by, byte by2) {
        if (by > by2) {
            return by2;
        }
        return by;
    }

    public static final short coerceAtMost(short s2, short s3) {
        if (s2 > s3) {
            return s3;
        }
        return s2;
    }

    public static final int coerceAtMost(int n2, int n3) {
        if (n2 > n3) {
            return n3;
        }
        return n2;
    }

    public static final long coerceAtMost(long l2, long l3) {
        if (l2 > l3) {
            return l3;
        }
        return l2;
    }

    public static final float coerceAtMost(float f2, float f3) {
        if (f2 > f3) {
            return f3;
        }
        return f2;
    }

    public static final double coerceAtMost(double d2, double d3) {
        if (d2 > d3) {
            return d3;
        }
        return d2;
    }

    @NotNull
    public static final <T extends Comparable<? super T>> T coerceIn(@NotNull T t2, @Nullable T t3, @Nullable T t4) {
        Intrinsics.checkNotNullParameter(t2, "");
        if (t3 != null && t4 != null) {
            if (t3.compareTo(t4) > 0) {
                throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + t4 + " is less than minimum " + t3 + '.');
            }
            if (t2.compareTo(t3) < 0) {
                return t3;
            }
            if (t2.compareTo(t4) > 0) {
                return t4;
            }
        } else {
            if (t3 != null && t2.compareTo(t3) < 0) {
                return t3;
            }
            if (t4 != null && t2.compareTo(t4) > 0) {
                return t4;
            }
        }
        return t2;
    }

    public static final byte coerceIn(byte by, byte by2, byte by3) {
        if (by2 > by3) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + by3 + " is less than minimum " + by2 + '.');
        }
        if (by < by2) {
            return by2;
        }
        if (by > by3) {
            return by3;
        }
        return by;
    }

    public static final short coerceIn(short s2, short s3, short s4) {
        if (s3 > s4) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + s4 + " is less than minimum " + s3 + '.');
        }
        if (s2 < s3) {
            return s3;
        }
        if (s2 > s4) {
            return s4;
        }
        return s2;
    }

    public static final int coerceIn(int n2, int n3, int n4) {
        if (n3 > n4) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + n4 + " is less than minimum " + n3 + '.');
        }
        if (n2 < n3) {
            return n3;
        }
        if (n2 > n4) {
            return n4;
        }
        return n2;
    }

    public static final long coerceIn(long l2, long l3, long l4) {
        if (l3 > l4) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + l4 + " is less than minimum " + l3 + '.');
        }
        if (l2 < l3) {
            return l3;
        }
        if (l2 > l4) {
            return l4;
        }
        return l2;
    }

    public static final float coerceIn(float f2, float f3, float f4) {
        if (f3 > f4) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + f4 + " is less than minimum " + f3 + '.');
        }
        if (f2 < f3) {
            return f3;
        }
        if (f2 > f4) {
            return f4;
        }
        return f2;
    }

    public static final double coerceIn(double d2, double d3, double d4) {
        if (d3 > d4) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + d4 + " is less than minimum " + d3 + '.');
        }
        if (d2 < d3) {
            return d3;
        }
        if (d2 > d4) {
            return d4;
        }
        return d2;
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <T extends Comparable<? super T>> T coerceIn(@NotNull T t2, @NotNull ClosedFloatingPointRange<T> closedFloatingPointRange) {
        Intrinsics.checkNotNullParameter(t2, "");
        Intrinsics.checkNotNullParameter(closedFloatingPointRange, "");
        if (closedFloatingPointRange.isEmpty()) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: " + closedFloatingPointRange + '.');
        }
        if (closedFloatingPointRange.lessThanOrEquals(t2, closedFloatingPointRange.getStart())) {
            ClosedFloatingPointRange<T> closedFloatingPointRange2 = closedFloatingPointRange;
            if (!closedFloatingPointRange2.lessThanOrEquals(closedFloatingPointRange2.getStart(), t2)) {
                return closedFloatingPointRange.getStart();
            }
        }
        ClosedFloatingPointRange<T> closedFloatingPointRange3 = closedFloatingPointRange;
        if (closedFloatingPointRange3.lessThanOrEquals(closedFloatingPointRange3.getEndInclusive(), t2) && !closedFloatingPointRange.lessThanOrEquals(t2, closedFloatingPointRange.getEndInclusive())) {
            return closedFloatingPointRange.getEndInclusive();
        }
        return t2;
    }

    @NotNull
    public static final <T extends Comparable<? super T>> T coerceIn(@NotNull T object, @NotNull ClosedRange<T> closedRange) {
        Intrinsics.checkNotNullParameter(object, "");
        Intrinsics.checkNotNullParameter(closedRange, "");
        if (closedRange instanceof ClosedFloatingPointRange) {
            return RangesKt.coerceIn(object, (ClosedFloatingPointRange)closedRange);
        }
        if (closedRange.isEmpty()) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: " + closedRange + '.');
        }
        if (object.compareTo(closedRange.getStart()) < 0) {
            return closedRange.getStart();
        }
        if (object.compareTo(closedRange.getEndInclusive()) > 0) {
            return closedRange.getEndInclusive();
        }
        return (T)object;
    }

    public static final int coerceIn(int n2, @NotNull ClosedRange<Integer> closedRange) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        if (closedRange instanceof ClosedFloatingPointRange) {
            return ((Number)((Object)RangesKt.coerceIn((Comparable)Integer.valueOf(n2), (ClosedFloatingPointRange)closedRange))).intValue();
        }
        if (closedRange.isEmpty()) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: " + closedRange + '.');
        }
        if (n2 < ((Number)closedRange.getStart()).intValue()) {
            return ((Number)closedRange.getStart()).intValue();
        }
        if (n2 > ((Number)closedRange.getEndInclusive()).intValue()) {
            return ((Number)closedRange.getEndInclusive()).intValue();
        }
        return n2;
    }

    public static final long coerceIn(long l2, @NotNull ClosedRange<Long> closedRange) {
        Intrinsics.checkNotNullParameter(closedRange, "");
        if (closedRange instanceof ClosedFloatingPointRange) {
            return ((Number)((Object)RangesKt.coerceIn((Comparable)Long.valueOf(l2), (ClosedFloatingPointRange)closedRange))).longValue();
        }
        if (closedRange.isEmpty()) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: " + closedRange + '.');
        }
        if (l2 < ((Number)closedRange.getStart()).longValue()) {
            return ((Number)closedRange.getStart()).longValue();
        }
        if (l2 > ((Number)closedRange.getEndInclusive()).longValue()) {
            return ((Number)closedRange.getEndInclusive()).longValue();
        }
        return l2;
    }
}

