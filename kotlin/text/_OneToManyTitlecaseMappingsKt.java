/*
 * Decompiled with CFR 0.152.
 */
package kotlin.text;

import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 8, 0}, k=2, xi=48, d1={"\u0000\f\n\u0000\n\u0002\u0010\u000e\n\u0002\u0010\f\n\u0000\u001a\f\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0000\u00a8\u0006\u0003"}, d2={"titlecaseImpl", "", "", "kotlin-stdlib"})
public final class _OneToManyTitlecaseMappingsKt {
    @NotNull
    public static final String titlecaseImpl(char c2) {
        String string = String.valueOf(c2);
        Intrinsics.checkNotNull(string);
        String string2 = string.toUpperCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(string2, "");
        String string3 = string2;
        if (string2.length() > 1) {
            if (c2 == '\u0149') {
                return string3;
            }
            c2 = string3.charAt(0);
            Intrinsics.checkNotNull(string3);
            String string4 = string3.substring(1);
            Intrinsics.checkNotNullExpressionValue(string4, "");
            string3 = string4;
            Intrinsics.checkNotNull(string4);
            String string5 = string3.toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(string5, "");
            string3 = string5;
            return c2 + string3;
        }
        return String.valueOf(Character.toTitleCase(c2));
    }
}

