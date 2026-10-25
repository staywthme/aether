/*
 * Decompiled with CFR 0.152.
 */
package kotlin.sequences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.SinceKotlin;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.BaseContinuationImpl;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.internal.InlineOnly;
import kotlin.internal.LowPriorityInOverloadResolution;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.sequences.ConstrainedOnceSequence;
import kotlin.sequences.EmptySequence;
import kotlin.sequences.FlatteningSequence;
import kotlin.sequences.GeneratorSequence;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequenceScope;
import kotlin.sequences.SequencesKt;
import kotlin.sequences.SequencesKt__SequencesJVMKt;
import kotlin.sequences.SequencesKt__SequencesKt;
import kotlin.sequences.TransformingSequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=5, xi=49, d1={"\u0000L\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0010\u001c\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\u001a.\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0014\b\u0004\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00050\u0004H\u0087\b\u00f8\u0001\u0000\u001a\u0012\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002\u001ab\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\b0\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\t\"\u0004\b\u0002\u0010\b2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0018\u0010\u000b\u001a\u0014\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\t0\f2\u0018\u0010\u0003\u001a\u0014\u0012\u0004\u0012\u0002H\t\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00050\u000eH\u0000\u001a&\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u00102\u000e\u0010\u0011\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0004\u001a<\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u00102\u000e\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u00042\u0014\u0010\u0011\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u000e\u001a=\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u00102\b\u0010\u0013\u001a\u0004\u0018\u0001H\u00022\u0014\u0010\u0011\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u000eH\u0007\u00a2\u0006\u0002\u0010\u0014\u001a+\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0016\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0017\"\u0002H\u0002\u00a2\u0006\u0002\u0010\u0018\u001a\u001c\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0005\u001a\u001c\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001\u001aC\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\b0\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\b*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0018\u0010\u0003\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00050\u000eH\u0002\u00a2\u0006\u0002\b\u001c\u001a)\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u001d0\u0001H\u0007\u00a2\u0006\u0002\b\u001e\u001a\"\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0001\u001a2\u0010\u001f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0012\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0004H\u0007\u001a!\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0004\u0012\u0002H\u0002\u0018\u00010\u0001H\u0087\b\u001a\u001e\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0007\u001a&\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010#\u001a\u00020$H\u0007\u001a@\u0010%\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020'\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0'0&\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\b*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\b0&0\u0001\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006("}, d2={"Sequence", "Lkotlin/sequences/Sequence;", "T", "iterator", "Lkotlin/Function0;", "", "emptySequence", "flatMapIndexed", "R", "C", "source", "transform", "Lkotlin/Function2;", "", "Lkotlin/Function1;", "generateSequence", "", "nextFunction", "seedFunction", "seed", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Lkotlin/sequences/Sequence;", "sequenceOf", "elements", "", "([Ljava/lang/Object;)Lkotlin/sequences/Sequence;", "asSequence", "constrainOnce", "flatten", "flatten$SequencesKt__SequencesKt", "", "flattenSequenceOfIterable", "ifEmpty", "defaultValue", "orEmpty", "shuffled", "random", "Lkotlin/random/Random;", "unzip", "Lkotlin/Pair;", "", "kotlin-stdlib"}, xs="kotlin/sequences/SequencesKt")
public class SequencesKt__SequencesKt
extends SequencesKt__SequencesJVMKt {
    @InlineOnly
    private static final <T> Sequence<T> Sequence(Function0<? extends Iterator<? extends T>> function0) {
        Intrinsics.checkNotNullParameter(function0, "");
        return new Sequence<T>(function0){
            final /* synthetic */ Function0<Iterator<T>> $iterator;
            {
                this.$iterator = function0;
            }

            @NotNull
            public final Iterator<T> iterator() {
                return this.$iterator.invoke();
            }
        };
    }

    @NotNull
    public static final <T> Sequence<T> asSequence(@NotNull Iterator<? extends T> iterator2) {
        Intrinsics.checkNotNullParameter(iterator2, "");
        return SequencesKt.constrainOnce(new Sequence<T>(iterator2){
            final /* synthetic */ Iterator $this_asSequence$inlined;
            {
                this.$this_asSequence$inlined = iterator2;
            }

            @NotNull
            public final Iterator<T> iterator() {
                return this.$this_asSequence$inlined;
            }
        });
    }

    @NotNull
    public static final <T> Sequence<T> sequenceOf(T ... TArray) {
        Intrinsics.checkNotNullParameter(TArray, "");
        if (TArray.length == 0) {
            return SequencesKt.emptySequence();
        }
        return ArraysKt.asSequence(TArray);
    }

    @NotNull
    public static final <T> Sequence<T> emptySequence() {
        return EmptySequence.INSTANCE;
    }

    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final <T> Sequence<T> orEmpty(Sequence<? extends T> sequence2) {
        Sequence<Object> sequence3 = sequence2;
        if (sequence3 == null) {
            sequence3 = SequencesKt.emptySequence();
        }
        return sequence3;
    }

    @SinceKotlin(version="1.3")
    @NotNull
    public static final <T> Sequence<T> ifEmpty(@NotNull Sequence<? extends T> sequence2, @NotNull Function0<? extends Sequence<? extends T>> function0) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        Intrinsics.checkNotNullParameter(function0, "");
        return SequencesKt.sequence(new Function2<SequenceScope<? super T>, Continuation<? super Unit>, Object>(sequence2, function0, null){
            int label;
            private /* synthetic */ Object L$0;
            final /* synthetic */ Sequence<T> $this_ifEmpty;
            final /* synthetic */ Function0<Sequence<T>> $defaultValue;
            {
                this.$this_ifEmpty = sequence2;
                this.$defaultValue = function0;
                super(2, continuation);
            }

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            @Nullable
            public final Object invokeSuspend(@NotNull Object object) {
                Object object2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0: {
                        ResultKt.throwOnFailure(object);
                        object = (SequenceScope)this.L$0;
                        Iterator<T> iterator2 = this.$this_ifEmpty.iterator();
                        if (!iterator2.hasNext()) break;
                        this.label = 1;
                        if (((SequenceScope)object).yieldAll(iterator2, (Continuation<? super Unit>)this) != object2) return Unit.INSTANCE;
                        return object2;
                    }
                    case 1: {
                        ResultKt.throwOnFailure(object);
                        return Unit.INSTANCE;
                    }
                }
                this.label = 2;
                if (((SequenceScope)object).yieldAll(this.$defaultValue.invoke(), (Continuation<? super Unit>)this) != object2) return Unit.INSTANCE;
                return object2;
                {
                    case 2: {
                        ResultKt.throwOnFailure(object);
                        return Unit.INSTANCE;
                    }
                }
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            @NotNull
            public final Continuation<Unit> create(@Nullable Object object, @NotNull Continuation<?> function2) {
                function2 = new /* invalid duplicate definition of identical inner class */;
                v0.L$0 = object;
                return (Continuation)((Object)function2);
            }

            @Nullable
            public final Object invoke(@NotNull SequenceScope<? super T> sequenceScope, @Nullable Continuation<? super Unit> continuation) {
                return (((BaseContinuationImpl)this).create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
            }
        });
    }

    @NotNull
    public static final <T> Sequence<T> flatten(@NotNull Sequence<? extends Sequence<? extends T>> sequence2) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        return SequencesKt__SequencesKt.flatten$SequencesKt__SequencesKt(sequence2, flatten.1.INSTANCE);
    }

    @JvmName(name="flattenSequenceOfIterable")
    @NotNull
    public static final <T> Sequence<T> flattenSequenceOfIterable(@NotNull Sequence<? extends Iterable<? extends T>> sequence2) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        return SequencesKt__SequencesKt.flatten$SequencesKt__SequencesKt(sequence2, flatten.2.INSTANCE);
    }

    private static final <T, R> Sequence<R> flatten$SequencesKt__SequencesKt(Sequence<? extends T> sequence2, Function1<? super T, ? extends Iterator<? extends R>> function1) {
        if (sequence2 instanceof TransformingSequence) {
            return ((TransformingSequence)sequence2).flatten$kotlin_stdlib(function1);
        }
        return new FlatteningSequence(sequence2, flatten.3.INSTANCE, function1);
    }

    @NotNull
    public static final <T, R> Pair<List<T>, List<R>> unzip(@NotNull Sequence<? extends Pair<? extends T, ? extends R>> object) {
        Intrinsics.checkNotNullParameter(object, "");
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        object = object.iterator();
        while (object.hasNext()) {
            Pair pair = (Pair)object.next();
            arrayList.add(pair.getFirst());
            arrayList2.add(pair.getSecond());
        }
        return TuplesKt.to(arrayList, arrayList2);
    }

    @SinceKotlin(version="1.4")
    @NotNull
    public static final <T> Sequence<T> shuffled(@NotNull Sequence<? extends T> sequence2) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        return SequencesKt.shuffled(sequence2, Random.Default);
    }

    @SinceKotlin(version="1.4")
    @NotNull
    public static final <T> Sequence<T> shuffled(@NotNull Sequence<? extends T> sequence2, @NotNull Random random) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        Intrinsics.checkNotNullParameter(random, "");
        return SequencesKt.sequence(new Function2<SequenceScope<? super T>, Continuation<? super Unit>, Object>(sequence2, random, null){
            Object L$1;
            int label;
            private /* synthetic */ Object L$0;
            final /* synthetic */ Sequence<T> $this_shuffled;
            final /* synthetic */ Random $random;
            {
                this.$this_shuffled = sequence2;
                this.$random = random;
                super(2, continuation);
            }

            /*
             * Enabled aggressive block sorting
             */
            @Nullable
            public final Object invokeSuspend(@NotNull Object object) {
                List<T> list2;
                SequenceScope sequenceScope;
                Object object2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0: {
                        ResultKt.throwOnFailure(object);
                        sequenceScope = (SequenceScope)this.L$0;
                        list2 = SequencesKt.toMutableList(this.$this_shuffled);
                        break;
                    }
                    case 1: {
                        list2 = (List<T>)this.L$1;
                        sequenceScope = (SequenceScope)this.L$0;
                        ResultKt.throwOnFailure(object);
                        break;
                    }
                    default: {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                }
                while (!((Collection)list2).isEmpty()) {
                    int n2 = this.$random.nextInt(list2.size());
                    T t2 = CollectionsKt.removeLast(list2);
                    object = n2 < list2.size() ? list2.set(n2, t2) : t2;
                    this.L$0 = sequenceScope;
                    this.L$1 = list2;
                    this.label = 1;
                    if (sequenceScope.yield(object, this) != object2) continue;
                    return object2;
                }
                return Unit.INSTANCE;
            }

            @NotNull
            public final Continuation<Unit> create(@Nullable Object object, @NotNull Continuation<?> function2) {
                function2 = new /* invalid duplicate definition of identical inner class */;
                v0.L$0 = object;
                return (Continuation)((Object)function2);
            }

            @Nullable
            public final Object invoke(@NotNull SequenceScope<? super T> sequenceScope, @Nullable Continuation<? super Unit> continuation) {
                return (((BaseContinuationImpl)this).create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
            }
        });
    }

    @NotNull
    public static final <T, C, R> Sequence<R> flatMapIndexed(@NotNull Sequence<? extends T> sequence2, @NotNull Function2<? super Integer, ? super T, ? extends C> function2, @NotNull Function1<? super C, ? extends Iterator<? extends R>> function1) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        Intrinsics.checkNotNullParameter(function2, "");
        Intrinsics.checkNotNullParameter(function1, "");
        return SequencesKt.sequence(new Function2<SequenceScope<? super R>, Continuation<? super Unit>, Object>(sequence2, function2, function1, null){
            Object L$1;
            int I$0;
            int label;
            private /* synthetic */ Object L$0;
            final /* synthetic */ Sequence<T> $source;
            final /* synthetic */ Function2<Integer, T, C> $transform;
            final /* synthetic */ Function1<C, Iterator<R>> $iterator;
            {
                this.$source = sequence2;
                this.$transform = function2;
                this.$iterator = function1;
                super(2, continuation);
            }

            /*
             * Unable to fully structure code
             */
            @Nullable
            public final Object invokeSuspend(@NotNull Object var1_1) {
                var6_2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0: {
                        ResultKt.throwOnFailure(var1_1);
                        var2_3 = (SequenceScope)this.L$0;
                        var3_4 = 0;
                        var4_5 = this.$source.iterator();
lbl8:
                        // 3 sources

                        while (var4_5.hasNext()) {
                            var1_1 = var4_5.next();
                            if ((var5_6 = var3_4++) < 0) {
                                CollectionsKt.throwIndexOverflow();
                            }
                            var1_1 = this.$transform.invoke(Boxing.boxInt(var5_6), var1_1);
                            this.L$0 = var2_3;
                            this.L$1 = var4_5;
                            this.I$0 = var3_4;
                            this.label = 1;
                            if (var2_3.yieldAll(this.$iterator.invoke(var1_1), (Continuation<Unit>)this) != var6_2) continue;
                            return var6_2;
                        }
                        break;
                    }
                    case 1: {
                        var3_4 = this.I$0;
                        var4_5 = (Iterator<T>)this.L$1;
                        var2_3 = (SequenceScope)this.L$0;
                        ResultKt.throwOnFailure(var1_1);
                        ** GOTO lbl8
                    }
                }
                return Unit.INSTANCE;
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            @NotNull
            public final Continuation<Unit> create(@Nullable Object object, @NotNull Continuation<?> function2) {
                function2 = new /* invalid duplicate definition of identical inner class */;
                v0.L$0 = object;
                return (Continuation)((Object)function2);
            }

            @Nullable
            public final Object invoke(@NotNull SequenceScope<? super R> sequenceScope, @Nullable Continuation<? super Unit> continuation) {
                return (((BaseContinuationImpl)this).create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
            }
        });
    }

    @NotNull
    public static final <T> Sequence<T> constrainOnce(@NotNull Sequence<? extends T> sequence2) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        if (sequence2 instanceof ConstrainedOnceSequence) {
            return sequence2;
        }
        return new ConstrainedOnceSequence<T>(sequence2);
    }

    @NotNull
    public static final <T> Sequence<T> generateSequence(@NotNull Function0<? extends T> function0) {
        Intrinsics.checkNotNullParameter(function0, "");
        return SequencesKt.constrainOnce((Sequence)new GeneratorSequence<T>(function0, new Function1<T, T>(function0){
            final /* synthetic */ Function0<T> $nextFunction;
            {
                this.$nextFunction = function0;
                super(1);
            }

            @Nullable
            public final T invoke(@NotNull T t2) {
                Intrinsics.checkNotNullParameter(t2, "");
                return this.$nextFunction.invoke();
            }
        }));
    }

    @LowPriorityInOverloadResolution
    @NotNull
    public static final <T> Sequence<T> generateSequence(@Nullable T t2, @NotNull Function1<? super T, ? extends T> function1) {
        Intrinsics.checkNotNullParameter(function1, "");
        if (t2 == null) {
            return EmptySequence.INSTANCE;
        }
        return new GeneratorSequence<T>(new Function0<T>(t2){
            final /* synthetic */ T $seed;
            {
                this.$seed = t2;
                super(0);
            }

            @Nullable
            public final T invoke() {
                return this.$seed;
            }
        }, function1);
    }

    @NotNull
    public static final <T> Sequence<T> generateSequence(@NotNull Function0<? extends T> function0, @NotNull Function1<? super T, ? extends T> function1) {
        Intrinsics.checkNotNullParameter(function0, "");
        Intrinsics.checkNotNullParameter(function1, "");
        return new GeneratorSequence<T>(function0, function1);
    }
}

