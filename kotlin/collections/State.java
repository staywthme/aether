/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import kotlin.Metadata;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0082\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2={"Lkotlin/collections/State;", "", "(Ljava/lang/String;I)V", "Ready", "NotReady", "Done", "Failed", "kotlin-stdlib"})
final class State
extends Enum<State> {
    public static final /* enum */ State Ready = new State();
    public static final /* enum */ State NotReady = new State();
    public static final /* enum */ State Done = new State();
    public static final /* enum */ State Failed = new State();
    private static final /* synthetic */ State[] $VALUES;

    public static State[] values() {
        return (State[])$VALUES.clone();
    }

    public static State valueOf(String string) {
        return Enum.valueOf(State.class, string);
    }

    private static final /* synthetic */ State[] $values() {
        State[] stateArray = new State[4];
        State[] stateArray2 = stateArray;
        stateArray[0] = Ready;
        stateArray2[1] = NotReady;
        stateArray2[2] = Done;
        stateArray2[3] = Failed;
        return stateArray2;
    }

    static {
        $VALUES = State.$values();
    }
}

