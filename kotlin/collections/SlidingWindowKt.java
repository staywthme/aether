/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.EmptyIterator;
import kotlin.collections.RingBuffer;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.BaseContinuationImpl;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequenceScope;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0000\u001aH\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\u0006\"\u0004\b\u0000\u0010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\b0\u00062\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0000\u001aD\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\u000e\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\u000e2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0000\u00a8\u0006\u000f"}, d2={"checkWindowSizeStep", "", "size", "", "step", "windowedIterator", "", "", "T", "iterator", "partialWindows", "", "reuseBuffer", "windowedSequence", "Lkotlin/sequences/Sequence;", "kotlin-stdlib"})
public final class SlidingWindowKt {
    public static final void checkWindowSizeStep(int n2, int n3) {
        boolean bl = n2 > 0 && n3 > 0;
        if (!bl) {
            String string = n2 != n3 ? "Both size " + n2 + " and step " + n3 + " must be greater than zero." : "size " + n2 + " must be greater than zero.";
            throw new IllegalArgumentException(string.toString());
        }
    }

    @NotNull
    public static final <T> Sequence<List<T>> windowedSequence(@NotNull Sequence<? extends T> sequence2, int n2, int n3, boolean bl, boolean bl2) {
        Intrinsics.checkNotNullParameter(sequence2, "");
        SlidingWindowKt.checkWindowSizeStep(n2, n3);
        return new Sequence<List<? extends T>>(sequence2, n2, n3, bl, bl2){
            final /* synthetic */ Sequence $this_windowedSequence$inlined;
            final /* synthetic */ int $size$inlined;
            final /* synthetic */ int $step$inlined;
            final /* synthetic */ boolean $partialWindows$inlined;
            final /* synthetic */ boolean $reuseBuffer$inlined;
            {
                this.$this_windowedSequence$inlined = sequence2;
                this.$size$inlined = n2;
                this.$step$inlined = n3;
                this.$partialWindows$inlined = bl;
                this.$reuseBuffer$inlined = bl2;
            }

            @NotNull
            public final Iterator<List<? extends T>> iterator() {
                return SlidingWindowKt.windowedIterator(this.$this_windowedSequence$inlined.iterator(), this.$size$inlined, this.$step$inlined, this.$partialWindows$inlined, this.$reuseBuffer$inlined);
            }
        };
    }

    @NotNull
    public static final <T> Iterator<List<T>> windowedIterator(@NotNull Iterator<? extends T> iterator2, int n2, int n3, boolean bl, boolean bl2) {
        Intrinsics.checkNotNullParameter(iterator2, "");
        if (!iterator2.hasNext()) {
            return EmptyIterator.INSTANCE;
        }
        return SequencesKt.iterator(new Function2<SequenceScope<? super List<? extends T>>, Continuation<? super Unit>, Object>(n2, n3, iterator2, bl2, bl, null){
            Object L$1;
            Object L$2;
            int I$0;
            int label;
            private /* synthetic */ Object L$0;
            final /* synthetic */ int $size;
            final /* synthetic */ int $step;
            final /* synthetic */ Iterator<T> $iterator;
            final /* synthetic */ boolean $reuseBuffer;
            final /* synthetic */ boolean $partialWindows;
            {
                this.$size = n2;
                this.$step = n3;
                this.$iterator = iterator2;
                this.$reuseBuffer = bl;
                this.$partialWindows = bl2;
                super(2, continuation);
            }

            /*
             * Unable to fully structure code
             */
            @Nullable
            public final Object invokeSuspend(@NotNull Object var1_1) {
                var7_3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0: {
                        ResultKt.throwOnFailure(var1_1);
                        var2_4 = (SequenceScope)this.L$0;
                        var1_2 = RangesKt.coerceAtMost(this.$size, 1024);
                        var3_5 = this.$step - this.$size;
                        if (var3_5 < 0) break;
                        var4_6 = new ArrayList<Object>(var1_2);
                        var5_8 = 0;
                        var6_10 = this.$iterator;
lbl12:
                        // 4 sources

                        while (var6_10.hasNext()) {
                            var1_1 = var6_10.next();
                            if (var5_8 > 0) {
                                --var5_8;
                                continue;
                            }
                            var4_6.add(var1_1);
                            if (var4_6.size() != this.$size) continue;
                            this.L$0 = var2_4;
                            this.L$1 = var4_6;
                            this.L$2 = var6_10;
                            this.I$0 = var3_5;
                            this.label = 1;
                            if (var2_4.yield(var4_6, this) == var7_3) {
                                return var7_3;
                            }
                            ** GOTO lbl35
                        }
                        break;
                    }
                    case 1: {
                        var3_5 = this.I$0;
                        var6_10 = (Iterator<T>)this.L$2;
                        var4_6 = (ArrayList<Object>)this.L$1;
                        var2_4 = (SequenceScope)this.L$0;
                        ResultKt.throwOnFailure(var1_1);
lbl35:
                        // 2 sources

                        if (this.$reuseBuffer) {
                            var4_6.clear();
                        } else {
                            var4_6 = new ArrayList<E>(this.$size);
                        }
                        var5_8 = var3_5;
                        ** GOTO lbl12
                    }
                }
                if (((Collection)var4_6).isEmpty() == false && (this.$partialWindows || var4_6.size() == this.$size)) {
                    this.L$0 = null;
                    this.L$1 = null;
                    this.L$2 = null;
                    this.label = 2;
                    if (var2_4.yield(var4_6, this) == var7_3) {
                        return var7_3;
                    }
                }
                ** GOTO lbl102
                {
                    case 2: {
                        ResultKt.throwOnFailure(var1_1);
                        ** GOTO lbl102
                    }
                }
                var4_7 = new RingBuffer<E>(var1_2);
                var5_9 = this.$iterator;
lbl54:
                // 4 sources

                while (var5_9.hasNext()) {
                    var6_11 = var5_9.next();
                    var4_7.add(var6_11);
                    if (!var4_7.isFull()) continue;
                    if (var4_7.size() < this.$size) {
                        var4_7 = var4_7.expanded(this.$size);
                        continue;
                    }
                    this.L$0 = var2_4;
                    this.L$1 = var4_7;
                    this.L$2 = var5_9;
                    this.label = 3;
                    if (var2_4.yield(this.$reuseBuffer != false ? (List)var4_7 : (List)new ArrayList<E>((Collection)var4_7), this) == var7_3) {
                        return var7_3;
                    }
                    ** GOTO lbl74
                }
                {
                    break;
                    case 3: {
                        var5_9 = (Iterator<T>)this.L$2;
                        var4_7 = (RingBuffer)this.L$1;
                        var2_4 = (SequenceScope)this.L$0;
                        ResultKt.throwOnFailure(var1_1);
lbl74:
                        // 2 sources

                        var4_7.removeFirst(this.$step);
                        ** GOTO lbl54
                    }
                }
                if (!this.$partialWindows) ** GOTO lbl102
lbl77:
                // 2 sources

                while (var4_7.size() > this.$step) {
                    this.L$0 = var2_4;
                    this.L$1 = var4_7;
                    this.L$2 = null;
                    this.label = 4;
                    if (var2_4.yield(this.$reuseBuffer != false ? (List)var4_7 : (List)new ArrayList<E>((Collection)var4_7), this) == var7_3) {
                        return var7_3;
                    }
                    ** GOTO lbl90
                }
                {
                    break;
                    case 4: {
                        var4_7 = (RingBuffer<E>)this.L$1;
                        var2_4 = (SequenceScope)this.L$0;
                        ResultKt.throwOnFailure(var1_1);
lbl90:
                        // 2 sources

                        var4_7.removeFirst(this.$step);
                        ** GOTO lbl77
                    }
                }
                if (((Collection)var4_7).isEmpty() == false) {
                    this.L$0 = null;
                    this.L$1 = null;
                    this.L$2 = null;
                    this.label = 5;
                    if (var2_4.yield(var4_7, this) == var7_3) {
                        return var7_3;
                    }
                }
                ** GOTO lbl102
                {
                    case 5: {
                        ResultKt.throwOnFailure(var1_1);
lbl102:
                        // 5 sources

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
            public final Object invoke(@NotNull SequenceScope<? super List<? extends T>> sequenceScope, @Nullable Continuation<? super Unit> continuation) {
                return (((BaseContinuationImpl)this).create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
            }
        });
    }
}

