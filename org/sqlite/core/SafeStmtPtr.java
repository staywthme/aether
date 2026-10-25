/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.core;

import java.sql.SQLException;
import org.sqlite.core.DB;

public class SafeStmtPtr {
    private final DB db;
    private final long ptr;
    private volatile boolean closed = false;
    private int closedRC;
    private SQLException closeException;

    public SafeStmtPtr(DB dB, long l2) {
        this.db = dB;
        this.ptr = l2;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public int close() throws SQLException {
        DB dB = this.db;
        synchronized (dB) {
            return this.internalClose();
        }
    }

    private int internalClose() throws SQLException {
        try {
            if (this.closed) {
                if (this.closeException != null) {
                    throw this.closeException;
                }
                int n2 = this.closedRC;
                return n2;
            }
            SafeStmtPtr safeStmtPtr = this;
            int n3 = this.closedRC = this.db.finalize(safeStmtPtr, safeStmtPtr.ptr);
            return n3;
        }
        catch (SQLException sQLException) {
            this.closeException = sQLException;
            throw sQLException;
        }
        finally {
            this.closed = true;
        }
    }

    public <E extends Throwable> int safeRunInt(SafePtrIntFunction<E> safePtrIntFunction) throws SQLException, E {
        DB dB = this.db;
        synchronized (dB) {
            this.ensureOpen();
            return safePtrIntFunction.run(this.db, this.ptr);
        }
    }

    public <E extends Throwable> long safeRunLong(SafePtrLongFunction<E> safePtrLongFunction) throws SQLException, E {
        DB dB = this.db;
        synchronized (dB) {
            this.ensureOpen();
            return safePtrLongFunction.run(this.db, this.ptr);
        }
    }

    public <E extends Throwable> double safeRunDouble(SafePtrDoubleFunction<E> safePtrDoubleFunction) throws SQLException, E {
        DB dB = this.db;
        synchronized (dB) {
            this.ensureOpen();
            return safePtrDoubleFunction.run(this.db, this.ptr);
        }
    }

    public <T, E extends Throwable> T safeRun(SafePtrFunction<T, E> safePtrFunction) throws SQLException, E {
        DB dB = this.db;
        synchronized (dB) {
            this.ensureOpen();
            return safePtrFunction.run(this.db, this.ptr);
        }
    }

    public <E extends Throwable> void safeRunConsume(SafePtrConsumer<E> safePtrConsumer) throws SQLException, E {
        DB dB = this.db;
        synchronized (dB) {
            this.ensureOpen();
            safePtrConsumer.run(this.db, this.ptr);
            return;
        }
    }

    private void ensureOpen() throws SQLException {
        if (this.closed) {
            throw new SQLException("stmt pointer is closed");
        }
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        object = (SafeStmtPtr)object;
        return this.ptr == ((SafeStmtPtr)object).ptr;
    }

    public int hashCode() {
        return Long.hashCode(this.ptr);
    }

    @FunctionalInterface
    public static interface SafePtrConsumer<E extends Throwable> {
        public void run(DB var1, long var2) throws E;
    }

    @FunctionalInterface
    public static interface SafePtrFunction<T, E extends Throwable> {
        public T run(DB var1, long var2) throws E;
    }

    @FunctionalInterface
    public static interface SafePtrDoubleFunction<E extends Throwable> {
        public double run(DB var1, long var2) throws E;
    }

    @FunctionalInterface
    public static interface SafePtrLongFunction<E extends Throwable> {
        public long run(DB var1, long var2) throws E;
    }

    @FunctionalInterface
    public static interface SafePtrIntFunction<E extends Throwable> {
        public int run(DB var1, long var2) throws E;
    }
}

