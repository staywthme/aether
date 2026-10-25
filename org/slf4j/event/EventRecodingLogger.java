/*
 * Decompiled with CFR 0.152.
 */
package org.slf4j.event;

import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.event.SubstituteLoggingEvent;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.helpers.SubstituteLogger;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class EventRecodingLogger
implements Logger {
    String name;
    SubstituteLogger logger;
    Queue<SubstituteLoggingEvent> eventQueue;
    static final boolean RECORD_ALL_EVENTS = true;

    public EventRecodingLogger(SubstituteLogger substituteLogger, Queue<SubstituteLoggingEvent> queue) {
        this.logger = substituteLogger;
        this.name = substituteLogger.getName();
        this.eventQueue = queue;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public void trace(String string) {
        this.recordEvent_0Args(Level.TRACE, null, string, null);
    }

    @Override
    public void trace(String string, Object object) {
        this.recordEvent_1Args(Level.TRACE, null, string, object);
    }

    @Override
    public void trace(String string, Object object, Object object2) {
        this.recordEvent2Args(Level.TRACE, null, string, object, object2);
    }

    @Override
    public void trace(String string, Object ... objectArray) {
        this.recordEventArgArray(Level.TRACE, null, string, objectArray);
    }

    @Override
    public void trace(String string, Throwable throwable) {
        this.recordEvent_0Args(Level.TRACE, null, string, throwable);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return true;
    }

    @Override
    public void trace(Marker marker, String string) {
        this.recordEvent_0Args(Level.TRACE, marker, string, null);
    }

    @Override
    public void trace(Marker marker, String string, Object object) {
        this.recordEvent_1Args(Level.TRACE, marker, string, object);
    }

    @Override
    public void trace(Marker marker, String string, Object object, Object object2) {
        this.recordEvent2Args(Level.TRACE, marker, string, object, object2);
    }

    @Override
    public void trace(Marker marker, String string, Object ... objectArray) {
        this.recordEventArgArray(Level.TRACE, marker, string, objectArray);
    }

    @Override
    public void trace(Marker marker, String string, Throwable throwable) {
        this.recordEvent_0Args(Level.TRACE, marker, string, throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public void debug(String string) {
        this.recordEvent_0Args(Level.DEBUG, null, string, null);
    }

    @Override
    public void debug(String string, Object object) {
        this.recordEvent_1Args(Level.DEBUG, null, string, object);
    }

    @Override
    public void debug(String string, Object object, Object object2) {
        this.recordEvent2Args(Level.DEBUG, null, string, object, object2);
    }

    @Override
    public void debug(String string, Object ... objectArray) {
        this.recordEventArgArray(Level.DEBUG, null, string, objectArray);
    }

    @Override
    public void debug(String string, Throwable throwable) {
        this.recordEvent_0Args(Level.DEBUG, null, string, throwable);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return true;
    }

    @Override
    public void debug(Marker marker, String string) {
        this.recordEvent_0Args(Level.DEBUG, marker, string, null);
    }

    @Override
    public void debug(Marker marker, String string, Object object) {
        this.recordEvent_1Args(Level.DEBUG, marker, string, object);
    }

    @Override
    public void debug(Marker marker, String string, Object object, Object object2) {
        this.recordEvent2Args(Level.DEBUG, marker, string, object, object2);
    }

    @Override
    public void debug(Marker marker, String string, Object ... objectArray) {
        this.recordEventArgArray(Level.DEBUG, marker, string, objectArray);
    }

    @Override
    public void debug(Marker marker, String string, Throwable throwable) {
        this.recordEvent_0Args(Level.DEBUG, marker, string, throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public void info(String string) {
        this.recordEvent_0Args(Level.INFO, null, string, null);
    }

    @Override
    public void info(String string, Object object) {
        this.recordEvent_1Args(Level.INFO, null, string, object);
    }

    @Override
    public void info(String string, Object object, Object object2) {
        this.recordEvent2Args(Level.INFO, null, string, object, object2);
    }

    @Override
    public void info(String string, Object ... objectArray) {
        this.recordEventArgArray(Level.INFO, null, string, objectArray);
    }

    @Override
    public void info(String string, Throwable throwable) {
        this.recordEvent_0Args(Level.INFO, null, string, throwable);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return true;
    }

    @Override
    public void info(Marker marker, String string) {
        this.recordEvent_0Args(Level.INFO, marker, string, null);
    }

    @Override
    public void info(Marker marker, String string, Object object) {
        this.recordEvent_1Args(Level.INFO, marker, string, object);
    }

    @Override
    public void info(Marker marker, String string, Object object, Object object2) {
        this.recordEvent2Args(Level.INFO, marker, string, object, object2);
    }

    @Override
    public void info(Marker marker, String string, Object ... objectArray) {
        this.recordEventArgArray(Level.INFO, marker, string, objectArray);
    }

    @Override
    public void info(Marker marker, String string, Throwable throwable) {
        this.recordEvent_0Args(Level.INFO, marker, string, throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public void warn(String string) {
        this.recordEvent_0Args(Level.WARN, null, string, null);
    }

    @Override
    public void warn(String string, Object object) {
        this.recordEvent_1Args(Level.WARN, null, string, object);
    }

    @Override
    public void warn(String string, Object object, Object object2) {
        this.recordEvent2Args(Level.WARN, null, string, object, object2);
    }

    @Override
    public void warn(String string, Object ... objectArray) {
        this.recordEventArgArray(Level.WARN, null, string, objectArray);
    }

    @Override
    public void warn(String string, Throwable throwable) {
        this.recordEvent_0Args(Level.WARN, null, string, throwable);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return true;
    }

    @Override
    public void warn(Marker marker, String string) {
        this.recordEvent_0Args(Level.WARN, marker, string, null);
    }

    @Override
    public void warn(Marker marker, String string, Object object) {
        this.recordEvent_1Args(Level.WARN, marker, string, object);
    }

    @Override
    public void warn(Marker marker, String string, Object object, Object object2) {
        this.recordEvent2Args(Level.WARN, marker, string, object, object2);
    }

    @Override
    public void warn(Marker marker, String string, Object ... objectArray) {
        this.recordEventArgArray(Level.WARN, marker, string, objectArray);
    }

    @Override
    public void warn(Marker marker, String string, Throwable throwable) {
        this.recordEvent_0Args(Level.WARN, marker, string, throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public void error(String string) {
        this.recordEvent_0Args(Level.ERROR, null, string, null);
    }

    @Override
    public void error(String string, Object object) {
        this.recordEvent_1Args(Level.ERROR, null, string, object);
    }

    @Override
    public void error(String string, Object object, Object object2) {
        this.recordEvent2Args(Level.ERROR, null, string, object, object2);
    }

    @Override
    public void error(String string, Object ... objectArray) {
        this.recordEventArgArray(Level.ERROR, null, string, objectArray);
    }

    @Override
    public void error(String string, Throwable throwable) {
        this.recordEvent_0Args(Level.ERROR, null, string, throwable);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return true;
    }

    @Override
    public void error(Marker marker, String string) {
        this.recordEvent_0Args(Level.ERROR, marker, string, null);
    }

    @Override
    public void error(Marker marker, String string, Object object) {
        this.recordEvent_1Args(Level.ERROR, marker, string, object);
    }

    @Override
    public void error(Marker marker, String string, Object object, Object object2) {
        this.recordEvent2Args(Level.ERROR, marker, string, object, object2);
    }

    @Override
    public void error(Marker marker, String string, Object ... objectArray) {
        this.recordEventArgArray(Level.ERROR, marker, string, objectArray);
    }

    @Override
    public void error(Marker marker, String string, Throwable throwable) {
        this.recordEvent_0Args(Level.ERROR, marker, string, throwable);
    }

    private void recordEvent_0Args(Level level, Marker marker, String string, Throwable throwable) {
        this.recordEvent(level, marker, string, null, throwable);
    }

    private void recordEvent_1Args(Level level, Marker marker, String string, Object object) {
        this.recordEvent(level, marker, string, new Object[]{object}, null);
    }

    private void recordEvent2Args(Level level, Marker marker, String string, Object object, Object object2) {
        if (object2 instanceof Throwable) {
            this.recordEvent(level, marker, string, new Object[]{object}, (Throwable)object2);
            return;
        }
        this.recordEvent(level, marker, string, new Object[]{object, object2}, null);
    }

    private void recordEventArgArray(Level level, Marker marker, String string, Object[] objectArray) {
        Throwable throwable = MessageFormatter.getThrowableCandidate(objectArray);
        if (throwable != null) {
            objectArray = MessageFormatter.trimmedCopy(objectArray);
            this.recordEvent(level, marker, string, objectArray, throwable);
            return;
        }
        this.recordEvent(level, marker, string, objectArray, null);
    }

    private void recordEvent(Level level, Marker marker, String string, Object[] objectArray, Throwable throwable) {
        SubstituteLoggingEvent substituteLoggingEvent = new SubstituteLoggingEvent();
        substituteLoggingEvent.setTimeStamp(System.currentTimeMillis());
        substituteLoggingEvent.setLevel(level);
        substituteLoggingEvent.setLogger(this.logger);
        substituteLoggingEvent.setLoggerName(this.name);
        substituteLoggingEvent.setMarker(marker);
        substituteLoggingEvent.setMessage(string);
        substituteLoggingEvent.setThreadName(Thread.currentThread().getName());
        substituteLoggingEvent.setArgumentArray(objectArray);
        substituteLoggingEvent.setThrowable(throwable);
        this.eventQueue.add(substituteLoggingEvent);
    }
}

