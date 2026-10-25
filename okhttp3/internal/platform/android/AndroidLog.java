/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package okhttp3.internal.platform.android;

import android.util.Log;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;
import kotlin.Metadata;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.OkHttpClient;
import okhttp3.internal.SuppressSignatureCheck;
import okhttp3.internal.concurrent.TaskRunner;
import okhttp3.internal.http2.Http2;
import okhttp3.internal.platform.android.AndroidLogHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 8, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0002\b\u0007\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J/\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\n2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0000\u00a2\u0006\u0002\b\u0012J\u0006\u0010\u0013\u001a\u00020\fJ\u0018\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\n2\u0006\u0010\u0016\u001a\u00020\nH\u0002J\u0010\u0010\u0017\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\nH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2={"Lokhttp3/internal/platform/android/AndroidLog;", "", "()V", "MAX_LOG_LENGTH", "", "configuredLoggers", "Ljava/util/concurrent/CopyOnWriteArraySet;", "Ljava/util/logging/Logger;", "knownLoggers", "", "", "androidLog", "", "loggerName", "logLevel", "message", "t", "", "androidLog$okhttp", "enable", "enableLogging", "logger", "tag", "loggerTag", "okhttp"})
@SuppressSignatureCheck
public final class AndroidLog {
    @NotNull
    public static final AndroidLog INSTANCE;
    private static final int MAX_LOG_LENGTH = 4000;
    @NotNull
    private static final CopyOnWriteArraySet<Logger> configuredLoggers;
    @NotNull
    private static final Map<String, String> knownLoggers;

    private AndroidLog() {
    }

    public final void androidLog$okhttp(@NotNull String string, int n2, @NotNull String string2, @Nullable Throwable throwable) {
        Intrinsics.checkNotNullParameter(string, "");
        Intrinsics.checkNotNullParameter(string2, "");
        string = this.loggerTag(string);
        if (Log.isLoggable((String)string, (int)n2)) {
            if (throwable != null) {
                string2 = string2 + '\n' + Log.getStackTraceString((Throwable)throwable);
            }
            int n3 = string2.length();
            for (int i2 = 0; i2 < n3; ++i2) {
                int n4;
                int n5 = StringsKt.indexOf$default((CharSequence)string2, '\n', i2, false, 4, null);
                n5 = n5 != -1 ? n5 : n3;
                do {
                    n4 = Math.min(n5, i2 + 4000);
                    String string3 = string2.substring(i2, n4);
                    Intrinsics.checkNotNullExpressionValue(string3, "");
                    Log.println((int)n2, (String)string, (String)string3);
                } while ((i2 = n4) < n5);
            }
        }
    }

    private final String loggerTag(String string) {
        String string2 = knownLoggers.get(string);
        if (string2 == null) {
            string2 = StringsKt.take(string, 23);
        }
        return string2;
    }

    public final void enable() {
        for (Map.Entry<String, String> entry : knownLoggers.entrySet()) {
            String string = entry.getKey();
            String object = entry.getValue();
            this.enableLogging(string, object);
        }
    }

    private final void enableLogging(String object, String string) {
        if (configuredLoggers.add((Logger)(object = Logger.getLogger((String)object)))) {
            ((Logger)object).setUseParentHandlers(false);
            ((Logger)object).setLevel(Log.isLoggable((String)string, (int)3) ? Level.FINE : (Log.isLoggable((String)string, (int)4) ? Level.INFO : Level.WARNING));
            ((Logger)object).addHandler(AndroidLogHandler.INSTANCE);
        }
    }

    static {
        LinkedHashMap linkedHashMap;
        INSTANCE = new AndroidLog();
        configuredLoggers = new CopyOnWriteArraySet();
        LinkedHashMap linkedHashMap2 = linkedHashMap = new LinkedHashMap();
        Package package_ = OkHttpClient.class.getPackage();
        Object object = package_ != null ? package_.getName() : null;
        if (object != null) {
            ((Map)linkedHashMap2).put(object, "OkHttp");
        }
        object = linkedHashMap2;
        String string = OkHttpClient.class.getName();
        Intrinsics.checkNotNullExpressionValue(string, "");
        String string2 = string;
        String string3 = "okhttp.OkHttpClient";
        object.put(string2, string3);
        object = linkedHashMap2;
        String string4 = Http2.class.getName();
        Intrinsics.checkNotNullExpressionValue(string4, "");
        string2 = string4;
        string3 = "okhttp.Http2";
        object.put(string2, string3);
        object = linkedHashMap2;
        String string5 = TaskRunner.class.getName();
        Intrinsics.checkNotNullExpressionValue(string5, "");
        string2 = string5;
        string3 = "okhttp.TaskRunner";
        object.put(string2, string3);
        ((Map)linkedHashMap2).put("okhttp3.mockwebserver.MockWebServer", "okhttp.MockWebServer");
        knownLoggers = MapsKt.toMap(linkedHashMap);
    }
}

