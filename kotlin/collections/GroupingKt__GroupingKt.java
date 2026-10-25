/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.collections.Grouping;
import kotlin.collections.GroupingKt__GroupingJVMKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000@\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\u001a\u009e\u0001\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052b\u0010\u0006\u001a^\u0012\u0013\u0012\u0011H\u0002\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0015\u0012\u0013\u0018\u0001H\u0003\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\r\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u0002H\u00030\u0007H\u0087\b\u00f8\u0001\u0000\u001a\u00b7\u0001\u0010\u000f\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u00102b\u0010\u0006\u001a^\u0012\u0013\u0012\u0011H\u0002\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0015\u0012\u0013\u0018\u0001H\u0003\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\r\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u0002H\u00030\u0007H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013\u001aI\u0010\u0014\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0016\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00150\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u0010H\u0007\u00a2\u0006\u0002\u0010\u0016\u001a\u00bf\u0001\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u000526\u0010\u0018\u001a2\u0012\u0013\u0012\u0011H\u0002\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0004\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u00192K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0003\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u001aH\u0087\b\u00f8\u0001\u0000\u001a\u007f\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u001b\u001a\u0002H\u000326\u0010\u0006\u001a2\u0012\u0013\u0012\u0011H\u0003\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u0019H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001c\u001a\u00d8\u0001\u0010\u001d\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u001026\u0010\u0018\u001a2\u0012\u0013\u0012\u0011H\u0002\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0004\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u00192K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H\u0003\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u001aH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001e\u001a\u0093\u0001\u0010\u001d\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002\"\u0004\b\u0002\u0010\u0003\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u00102\u0006\u0010\u001b\u001a\u0002H\u000326\u0010\u0006\u001a2\u0012\u0013\u0012\u0011H\u0003\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H\u00030\u0019H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001f\u001a\u008b\u0001\u0010 \u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H!0\u0001\"\u0004\b\u0000\u0010!\"\b\b\u0001\u0010\u0004*\u0002H!\"\u0004\b\u0002\u0010\u0002*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H!\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H!0\u001aH\u0087\b\u00f8\u0001\u0000\u001a\u00a4\u0001\u0010\"\u001a\u0002H\u0010\"\u0004\b\u0000\u0010!\"\b\b\u0001\u0010\u0004*\u0002H!\"\u0004\b\u0002\u0010\u0002\"\u0016\b\u0003\u0010\u0010*\u0010\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H!0\u0011*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\u0012\u001a\u0002H\u00102K\u0010\u0006\u001aG\u0012\u0013\u0012\u0011H\u0002\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0013\u0012\u0011H!\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u0011H\u0004\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\f\u0012\u0004\u0012\u0002H!0\u001aH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010#\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006$"}, d2={"aggregate", "", "K", "R", "T", "Lkotlin/collections/Grouping;", "operation", "Lkotlin/Function4;", "Lkotlin/ParameterName;", "name", "key", "accumulator", "element", "", "first", "aggregateTo", "M", "", "destination", "(Lkotlin/collections/Grouping;Ljava/util/Map;Lkotlin/jvm/functions/Function4;)Ljava/util/Map;", "eachCountTo", "", "(Lkotlin/collections/Grouping;Ljava/util/Map;)Ljava/util/Map;", "fold", "initialValueSelector", "Lkotlin/Function2;", "Lkotlin/Function3;", "initialValue", "(Lkotlin/collections/Grouping;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/util/Map;", "foldTo", "(Lkotlin/collections/Grouping;Ljava/util/Map;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function3;)Ljava/util/Map;", "(Lkotlin/collections/Grouping;Ljava/util/Map;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/util/Map;", "reduce", "S", "reduceTo", "(Lkotlin/collections/Grouping;Ljava/util/Map;Lkotlin/jvm/functions/Function3;)Ljava/util/Map;", "kotlin-stdlib"}, xs="kotlin/collections/GroupingKt")
@SourceDebugExtension(value={"SMAP\nGrouping.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Grouping.kt\nkotlin/collections/GroupingKt__GroupingKt\n*L\n1#1,291:1\n80#1,6:292\n53#1:298\n80#1,6:299\n80#1,6:305\n53#1:311\n80#1,6:312\n80#1,6:318\n53#1:324\n80#1,6:325\n80#1,6:331\n189#1:337\n80#1,6:338\n*S KotlinDebug\n*F\n+ 1 Grouping.kt\nkotlin/collections/GroupingKt__GroupingKt\n*L\n53#1:292,6\n112#1:298\n112#1:299,6\n143#1:305,6\n164#1:311\n164#1:312,6\n189#1:318,6\n211#1:324\n211#1:325,6\n239#1:331,6\n257#1:337\n257#1:338,6\n*E\n"})
class GroupingKt__GroupingKt
extends GroupingKt__GroupingJVMKt {
    @SinceKotlin(version="1.1")
    @NotNull
    public static final <T, K, R> Map<K, R> aggregate(@NotNull Grouping<T, ? extends K> grouping, @NotNull Function4<? super K, ? super R, ? super T, ? super Boolean, ? extends R> function4) {
        Intrinsics.checkNotNullParameter(grouping, "");
        Intrinsics.checkNotNullParameter(function4, "");
        Map map = new LinkedHashMap();
        Iterator<T> iterator2 = grouping.sourceIterator();
        while (iterator2.hasNext()) {
            T t2;
            Object v2;
            K k2;
            map.put(k2, function4.invoke(k2, v2, t2, (v2 = map.get(k2 = grouping.keyOf(t2 = iterator2.next()))) == null && !map.containsKey(k2)));
        }
        return map;
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <T, K, R, M extends Map<? super K, R>> M aggregateTo(@NotNull Grouping<T, ? extends K> grouping, @NotNull M m2, @NotNull Function4<? super K, ? super R, ? super T, ? super Boolean, ? extends R> function4) {
        Intrinsics.checkNotNullParameter(grouping, "");
        Intrinsics.checkNotNullParameter(m2, "");
        Intrinsics.checkNotNullParameter(function4, "");
        Iterator<T> iterator2 = grouping.sourceIterator();
        while (iterator2.hasNext()) {
            T t2;
            R r2;
            K k2;
            m2.put(k2, function4.invoke(k2, r2, t2, (r2 = m2.get(k2 = grouping.keyOf(t2 = iterator2.next()))) == null && !m2.containsKey(k2)));
        }
        return m2;
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <T, K, R> Map<K, R> fold(@NotNull Grouping<T, ? extends K> grouping, @NotNull Function2<? super K, ? super T, ? extends R> function2, @NotNull Function3<? super K, ? super R, ? super T, ? extends R> function3) {
        Intrinsics.checkNotNullParameter(grouping, "");
        Intrinsics.checkNotNullParameter(function2, "");
        Intrinsics.checkNotNullParameter(function3, "");
        Map map = new LinkedHashMap();
        Iterator<T> iterator2 = grouping.sourceIterator();
        while (iterator2.hasNext()) {
            Object object = iterator2.next();
            Object object2 = grouping.keyOf(object);
            Object v2 = map.get(object2);
            K k2 = object2;
            boolean bl = v2 == null && !map.containsKey(object2);
            object2 = object;
            K k3 = k2;
            K k4 = k2;
            Map map2 = map;
            object = function3.invoke(k3, bl ? function2.invoke(k3, object2) : v2, object2);
            map2.put(k4, object);
        }
        return map;
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <T, K, R, M extends Map<? super K, R>> M foldTo(@NotNull Grouping<T, ? extends K> grouping, @NotNull M m2, @NotNull Function2<? super K, ? super T, ? extends R> function2, @NotNull Function3<? super K, ? super R, ? super T, ? extends R> function3) {
        Intrinsics.checkNotNullParameter(grouping, "");
        Intrinsics.checkNotNullParameter(m2, "");
        Intrinsics.checkNotNullParameter(function2, "");
        Intrinsics.checkNotNullParameter(function3, "");
        Iterator<T> iterator2 = grouping.sourceIterator();
        while (iterator2.hasNext()) {
            Object object = iterator2.next();
            Object object2 = grouping.keyOf(object);
            R r2 = m2.get(object2);
            K k2 = object2;
            boolean bl = r2 == null && !m2.containsKey(object2);
            object2 = object;
            K k3 = k2;
            K k4 = k2;
            M m3 = m2;
            object = function3.invoke(k3, bl ? function2.invoke(k3, object2) : r2, object2);
            m3.put(k4, object);
        }
        return m2;
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <T, K, R> Map<K, R> fold(@NotNull Grouping<T, ? extends K> grouping, R r2, @NotNull Function2<? super R, ? super T, ? extends R> function2) {
        Intrinsics.checkNotNullParameter(grouping, "");
        Intrinsics.checkNotNullParameter(function2, "");
        Map map = new LinkedHashMap();
        Iterator<T> iterator2 = grouping.sourceIterator();
        while (iterator2.hasNext()) {
            Object object = iterator2.next();
            Object object2 = grouping.keyOf(object);
            Object v2 = map.get(object2);
            K k2 = object2;
            boolean bl = v2 == null && !map.containsKey(object2);
            object2 = object;
            K k3 = k2;
            Map map2 = map;
            object = function2.invoke(bl ? r2 : v2, object2);
            map2.put(k3, object);
        }
        return map;
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <T, K, R, M extends Map<? super K, R>> M foldTo(@NotNull Grouping<T, ? extends K> grouping, @NotNull M m2, R r2, @NotNull Function2<? super R, ? super T, ? extends R> function2) {
        Intrinsics.checkNotNullParameter(grouping, "");
        Intrinsics.checkNotNullParameter(m2, "");
        Intrinsics.checkNotNullParameter(function2, "");
        Iterator<T> iterator2 = grouping.sourceIterator();
        while (iterator2.hasNext()) {
            Object object = iterator2.next();
            Object object2 = grouping.keyOf(object);
            R r3 = m2.get(object2);
            K k2 = object2;
            boolean bl = r3 == null && !m2.containsKey(object2);
            object2 = object;
            K k3 = k2;
            M m3 = m2;
            object = function2.invoke(bl ? r2 : r3, object2);
            m3.put(k3, object);
        }
        return m2;
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <S, T extends S, K> Map<K, S> reduce(@NotNull Grouping<T, ? extends K> grouping, @NotNull Function3<? super K, ? super S, ? super T, ? extends S> function3) {
        Intrinsics.checkNotNullParameter(grouping, "");
        Intrinsics.checkNotNullParameter(function3, "");
        Map map = new LinkedHashMap();
        Iterator<T> iterator2 = grouping.sourceIterator();
        while (iterator2.hasNext()) {
            Object object = iterator2.next();
            Object object2 = grouping.keyOf(object);
            Object v2 = map.get(object2);
            K k2 = object2;
            boolean bl = v2 == null && !map.containsKey(object2);
            object2 = object;
            K k3 = k2;
            K k4 = k2;
            Map map2 = map;
            object = bl ? object2 : function3.invoke(k3, v2, object2);
            map2.put(k4, object);
        }
        return map;
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <S, T extends S, K, M extends Map<? super K, S>> M reduceTo(@NotNull Grouping<T, ? extends K> grouping, @NotNull M m2, @NotNull Function3<? super K, ? super S, ? super T, ? extends S> function3) {
        Intrinsics.checkNotNullParameter(grouping, "");
        Intrinsics.checkNotNullParameter(m2, "");
        Intrinsics.checkNotNullParameter(function3, "");
        Iterator<T> iterator2 = grouping.sourceIterator();
        while (iterator2.hasNext()) {
            Object object = iterator2.next();
            Object object2 = grouping.keyOf(object);
            S s2 = m2.get(object2);
            K k2 = object2;
            boolean bl = s2 == null && !m2.containsKey(object2);
            object2 = object;
            K k3 = k2;
            K k4 = k2;
            M m3 = m2;
            object = bl ? object2 : function3.invoke(k3, s2, object2);
            m3.put(k4, object);
        }
        return m2;
    }

    @SinceKotlin(version="1.1")
    @NotNull
    public static final <T, K, M extends Map<? super K, Integer>> M eachCountTo(@NotNull Grouping<T, ? extends K> grouping, @NotNull M m2) {
        Intrinsics.checkNotNullParameter(grouping, "");
        Intrinsics.checkNotNullParameter(m2, "");
        Integer n2 = 0;
        Iterator<T> iterator2 = grouping.sourceIterator();
        while (iterator2.hasNext()) {
            Integer n3;
            Object object = iterator2.next();
            object = grouping.keyOf(object);
            Integer n4 = n3 = m2.get(object);
            int n5 = n4 == null && !m2.containsKey(object) ? 1 : 0;
            n3 = n4;
            Object object2 = object;
            M m3 = m2;
            n5 = ((Number)(n5 != 0 ? n2 : n3)).intValue();
            object = n5 + 1;
            m3.put(object2, object);
        }
        return m2;
    }
}

