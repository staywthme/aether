/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.collections.Grouping;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.jvm.internal.TypeIntrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000&\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010&\n\u0000\u001a0\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u0005H\u0007\u001aZ\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\t\"\u0004\b\u0002\u0010\b*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\t0\u00072\u001e\u0010\n\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\t0\f\u0012\u0004\u0012\u0002H\b0\u000bH\u0081\b\u00f8\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006\r"}, d2={"eachCount", "", "K", "", "T", "Lkotlin/collections/Grouping;", "mapValuesInPlace", "", "R", "V", "f", "Lkotlin/Function1;", "", "kotlin-stdlib"}, xs="kotlin/collections/GroupingKt")
@SourceDebugExtension(value={"SMAP\nGroupingJVM.kt\nKotlin\n*S Kotlin\n*F\n+ 1 GroupingJVM.kt\nkotlin/collections/GroupingKt__GroupingJVMKt\n+ 2 Grouping.kt\nkotlin/collections/GroupingKt__GroupingKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,52:1\n143#2:53\n80#2,4:54\n85#2:59\n1#3:58\n1855#4,2:60\n*S KotlinDebug\n*F\n+ 1 GroupingJVM.kt\nkotlin/collections/GroupingKt__GroupingJVMKt\n*L\n22#1:53\n22#1:54,4\n22#1:59\n48#1:60,2\n*E\n"})
class GroupingKt__GroupingJVMKt {
    @SinceKotlin(version="1.1")
    @NotNull
    public static final <T, K> Map<K, Integer> eachCount(@NotNull Grouping<T, ? extends K> object) {
        Object object2;
        Intrinsics.checkNotNullParameter(object, "");
        Iterator iterator2 = new LinkedHashMap();
        Object object3 = object;
        object = object3.sourceIterator();
        while (object.hasNext()) {
            Object object4;
            object2 = object.next();
            object2 = object3.keyOf(object2);
            Object v2 = object4 = iterator2.get(object2);
            boolean bl = v2 == null && !iterator2.containsKey(object2);
            object4 = v2;
            Object object5 = object2;
            Iterator iterator3 = iterator2;
            Ref.IntRef intRef = bl ? new Ref.IntRef() : object4;
            object2 = intRef;
            object2 = intRef;
            object4 = intRef;
            intRef.element = ((Ref.IntRef)object4).element + 1;
            iterator3.put(object5, object2);
        }
        object = iterator2;
        iterator2 = object.entrySet();
        iterator2 = iterator2.iterator();
        while (iterator2.hasNext()) {
            object3 = (Map.Entry)iterator2.next();
            Intrinsics.checkNotNull(object3);
            Map.Entry entry = TypeIntrinsics.asMutableMapEntry(object3);
            object2 = entry;
            object2.setValue(((Ref.IntRef)object3.getValue()).element);
        }
        return TypeIntrinsics.asMutableMap(object);
    }

    @PublishedApi
    @InlineOnly
    private static final <K, V, R> Map<K, R> mapValuesInPlace(Map<K, V> map, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(map, "");
        Intrinsics.checkNotNullParameter(function1, "");
        Object object = map.entrySet();
        object = object.iterator();
        while (object.hasNext()) {
            Object object2 = object.next();
            object2 = (Map.Entry)object2;
            Intrinsics.checkNotNull(object2);
            TypeIntrinsics.asMutableMapEntry(object2).setValue(function1.invoke((Map.Entry<K, V>)object2));
        }
        return TypeIntrinsics.asMutableMap(map);
    }
}

