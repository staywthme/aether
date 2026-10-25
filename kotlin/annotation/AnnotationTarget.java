/*
 * Decompiled with CFR 0.152.
 */
package kotlin.annotation;

import kotlin.Metadata;
import kotlin.SinceKotlin;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0011\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011\u00a8\u0006\u0012"}, d2={"Lkotlin/annotation/AnnotationTarget;", "", "(Ljava/lang/String;I)V", "CLASS", "ANNOTATION_CLASS", "TYPE_PARAMETER", "PROPERTY", "FIELD", "LOCAL_VARIABLE", "VALUE_PARAMETER", "CONSTRUCTOR", "FUNCTION", "PROPERTY_GETTER", "PROPERTY_SETTER", "TYPE", "EXPRESSION", "FILE", "TYPEALIAS", "kotlin-stdlib"})
public final class AnnotationTarget
extends Enum<AnnotationTarget> {
    public static final /* enum */ AnnotationTarget CLASS = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget ANNOTATION_CLASS = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget TYPE_PARAMETER = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget PROPERTY = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget FIELD = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget LOCAL_VARIABLE = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget VALUE_PARAMETER = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget CONSTRUCTOR = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget FUNCTION = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget PROPERTY_GETTER = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget PROPERTY_SETTER = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget TYPE = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget EXPRESSION = new AnnotationTarget();
    public static final /* enum */ AnnotationTarget FILE = new AnnotationTarget();
    @SinceKotlin(version="1.1")
    public static final /* enum */ AnnotationTarget TYPEALIAS = new AnnotationTarget();
    private static final /* synthetic */ AnnotationTarget[] $VALUES;

    public static AnnotationTarget[] values() {
        return (AnnotationTarget[])$VALUES.clone();
    }

    public static AnnotationTarget valueOf(String string) {
        return Enum.valueOf(AnnotationTarget.class, string);
    }

    private static final /* synthetic */ AnnotationTarget[] $values() {
        AnnotationTarget[] annotationTargetArray = new AnnotationTarget[15];
        AnnotationTarget[] annotationTargetArray2 = annotationTargetArray;
        annotationTargetArray[0] = CLASS;
        annotationTargetArray2[1] = ANNOTATION_CLASS;
        annotationTargetArray2[2] = TYPE_PARAMETER;
        annotationTargetArray2[3] = PROPERTY;
        annotationTargetArray2[4] = FIELD;
        annotationTargetArray2[5] = LOCAL_VARIABLE;
        annotationTargetArray2[6] = VALUE_PARAMETER;
        annotationTargetArray2[7] = CONSTRUCTOR;
        annotationTargetArray2[8] = FUNCTION;
        annotationTargetArray2[9] = PROPERTY_GETTER;
        annotationTargetArray2[10] = PROPERTY_SETTER;
        annotationTargetArray2[11] = TYPE;
        annotationTargetArray2[12] = EXPRESSION;
        annotationTargetArray2[13] = FILE;
        annotationTargetArray2[14] = TYPEALIAS;
        return annotationTargetArray2;
    }

    static {
        $VALUES = AnnotationTarget.$values();
    }
}

