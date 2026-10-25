/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.core;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.sqlite.BusyHandler;
import org.sqlite.Collation;
import org.sqlite.Function;
import org.sqlite.ProgressHandler;
import org.sqlite.SQLiteCommitListener;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;
import org.sqlite.SQLiteUpdateListener;
import org.sqlite.core.Codes;
import org.sqlite.core.CoreStatement;
import org.sqlite.core.SafeStmtPtr;

public abstract class DB
implements Codes {
    private final String url;
    private final String fileName;
    private final SQLiteConfig config;
    private final AtomicBoolean closed = new AtomicBoolean(true);
    volatile SafeStmtPtr begin;
    volatile SafeStmtPtr commit;
    private final Set<SafeStmtPtr> stmts = ConcurrentHashMap.newKeySet();
    private final Set<SQLiteUpdateListener> updateListeners = new HashSet<SQLiteUpdateListener>();
    private final Set<SQLiteCommitListener> commitListeners = new HashSet<SQLiteCommitListener>();

    public DB(String string, String string2, SQLiteConfig sQLiteConfig) throws SQLException {
        this.url = string;
        this.fileName = string2;
        this.config = sQLiteConfig;
    }

    public String getUrl() {
        return this.url;
    }

    public boolean isClosed() {
        return this.closed.get();
    }

    public SQLiteConfig getConfig() {
        return this.config;
    }

    public abstract void interrupt() throws SQLException;

    public abstract void busy_timeout(int var1) throws SQLException;

    public abstract void busy_handler(BusyHandler var1) throws SQLException;

    abstract String errmsg() throws SQLException;

    public abstract String libversion() throws SQLException;

    public abstract long changes() throws SQLException;

    public abstract long total_changes() throws SQLException;

    public abstract int shared_cache(boolean var1) throws SQLException;

    public abstract int enable_load_extension(boolean var1) throws SQLException;

    public final synchronized void exec(String object, boolean bl) throws SQLException {
        object = this.prepare((String)object);
        try {
            int n2 = ((SafeStmtPtr)object).safeRunInt(DB::step);
            switch (n2) {
                case 101: {
                    this.ensureAutoCommit(bl);
                    return;
                }
                case 100: {
                    return;
                }
            }
            this.throwex(n2);
            return;
        }
        finally {
            ((SafeStmtPtr)object).close();
        }
    }

    public final synchronized void open(String string, int n2) throws SQLException {
        this._open(string, n2);
        this.closed.set(false);
        if (this.fileName.startsWith("file:") && !this.fileName.contains("cache=")) {
            DB dB = this;
            dB.shared_cache(dB.config.isEnabledSharedCache());
        }
        DB dB = this;
        dB.enable_load_extension(dB.config.isEnabledLoadExtension());
        DB dB2 = this;
        dB2.busy_timeout(dB2.config.getBusyTimeout());
    }

    public final synchronized void close() throws SQLException {
        for (SafeStmtPtr safeStmtPtr : this.stmts) {
            safeStmtPtr.close();
        }
        if (this.begin != null) {
            this.begin.close();
        }
        if (this.commit != null) {
            this.commit.close();
        }
        this.closed.set(true);
        this._close();
    }

    public final synchronized void prepare(CoreStatement coreStatement) throws SQLException {
        if (coreStatement.sql == null) {
            throw new NullPointerException();
        }
        if (coreStatement.pointer != null) {
            coreStatement.pointer.close();
        }
        coreStatement.pointer = this.prepare(coreStatement.sql);
        boolean bl = this.stmts.add(coreStatement.pointer);
        if (!bl) {
            throw new IllegalStateException("Already added pointer to statements set");
        }
    }

    public synchronized int finalize(SafeStmtPtr safeStmtPtr, long l2) throws SQLException {
        try {
            int n2 = this.finalize(l2);
            return n2;
        }
        finally {
            this.stmts.remove(safeStmtPtr);
        }
    }

    protected abstract void _open(String var1, int var2) throws SQLException;

    protected abstract void _close() throws SQLException;

    public abstract int _exec(String var1) throws SQLException;

    protected abstract SafeStmtPtr prepare(String var1) throws SQLException;

    protected abstract int finalize(long var1) throws SQLException;

    public abstract int step(long var1) throws SQLException;

    public abstract int reset(long var1) throws SQLException;

    public abstract int clear_bindings(long var1) throws SQLException;

    abstract int bind_parameter_count(long var1) throws SQLException;

    public abstract int column_count(long var1) throws SQLException;

    public abstract int column_type(long var1, int var3) throws SQLException;

    public abstract String column_decltype(long var1, int var3) throws SQLException;

    public abstract String column_table_name(long var1, int var3) throws SQLException;

    public abstract String column_name(long var1, int var3) throws SQLException;

    public abstract String column_text(long var1, int var3) throws SQLException;

    public abstract byte[] column_blob(long var1, int var3) throws SQLException;

    public abstract double column_double(long var1, int var3) throws SQLException;

    public abstract long column_long(long var1, int var3) throws SQLException;

    public abstract int column_int(long var1, int var3) throws SQLException;

    abstract int bind_null(long var1, int var3) throws SQLException;

    abstract int bind_int(long var1, int var3, int var4) throws SQLException;

    abstract int bind_long(long var1, int var3, long var4) throws SQLException;

    abstract int bind_double(long var1, int var3, double var4) throws SQLException;

    abstract int bind_text(long var1, int var3, String var4) throws SQLException;

    abstract int bind_blob(long var1, int var3, byte[] var4) throws SQLException;

    public abstract void result_null(long var1) throws SQLException;

    public abstract void result_text(long var1, String var3) throws SQLException;

    public abstract void result_blob(long var1, byte[] var3) throws SQLException;

    public abstract void result_double(long var1, double var3) throws SQLException;

    public abstract void result_long(long var1, long var3) throws SQLException;

    public abstract void result_int(long var1, int var3) throws SQLException;

    public abstract void result_error(long var1, String var3) throws SQLException;

    public abstract String value_text(Function var1, int var2) throws SQLException;

    public abstract byte[] value_blob(Function var1, int var2) throws SQLException;

    public abstract double value_double(Function var1, int var2) throws SQLException;

    public abstract long value_long(Function var1, int var2) throws SQLException;

    public abstract int value_int(Function var1, int var2) throws SQLException;

    public abstract int value_type(Function var1, int var2) throws SQLException;

    public abstract int create_function(String var1, Function var2, int var3, int var4) throws SQLException;

    public abstract int destroy_function(String var1) throws SQLException;

    public abstract int create_collation(String var1, Collation var2) throws SQLException;

    public abstract int destroy_collation(String var1) throws SQLException;

    public abstract int backup(String var1, String var2, ProgressObserver var3) throws SQLException;

    public abstract int backup(String var1, String var2, ProgressObserver var3, int var4, int var5, int var6) throws SQLException;

    public abstract int restore(String var1, String var2, ProgressObserver var3) throws SQLException;

    public abstract int restore(String var1, String var2, ProgressObserver var3, int var4, int var5, int var6) throws SQLException;

    public abstract int limit(int var1, int var2) throws SQLException;

    public abstract void register_progress_handler(int var1, ProgressHandler var2) throws SQLException;

    public abstract void clear_progress_handler() throws SQLException;

    abstract boolean[][] column_metadata(long var1) throws SQLException;

    public final synchronized String[] column_names(long l2) throws SQLException {
        String[] stringArray = new String[this.column_count(l2)];
        for (int i2 = 0; i2 < stringArray.length; ++i2) {
            stringArray[i2] = this.column_name(l2, i2);
        }
        return stringArray;
    }

    final synchronized int sqlbind(long l2, int n2, Object object) throws SQLException {
        ++n2;
        if (object == null) {
            return this.bind_null(l2, n2);
        }
        if (object instanceof Integer) {
            return this.bind_int(l2, n2, (Integer)object);
        }
        if (object instanceof Short) {
            return this.bind_int(l2, n2, ((Short)object).intValue());
        }
        if (object instanceof Long) {
            return this.bind_long(l2, n2, (Long)object);
        }
        if (object instanceof Float) {
            return this.bind_double(l2, n2, ((Float)object).doubleValue());
        }
        if (object instanceof Double) {
            return this.bind_double(l2, n2, (Double)object);
        }
        if (object instanceof String) {
            return this.bind_text(l2, n2, (String)object);
        }
        if (object instanceof byte[]) {
            return this.bind_blob(l2, n2, (byte[])object);
        }
        throw new SQLException("unexpected param type: " + object.getClass());
    }

    final synchronized long[] executeBatch(SafeStmtPtr safeStmtPtr, int n2, Object[] objectArray, boolean bl) throws SQLException {
        return safeStmtPtr.safeRun((dB, l2) -> this.executeBatch(l2, n2, objectArray, bl));
    }

    private synchronized long[] executeBatch(long l2, int n2, Object[] objectArray, boolean bl) throws SQLException {
        if (n2 <= 0) {
            throw new SQLException("count (" + n2 + ") < 1");
        }
        int n3 = this.bind_parameter_count(l2);
        long[] lArray = new long[n2];
        try {
            for (int i2 = 0; i2 < n2; ++i2) {
                int n4;
                this.reset(l2);
                for (int i3 = 0; i3 < n3; ++i3) {
                    n4 = this.sqlbind(l2, i3, objectArray[i2 * n3 + i3]);
                    if (n4 == 0) continue;
                    this.throwex(n4);
                }
                n4 = this.step(l2);
                if (n4 != 101) {
                    this.reset(l2);
                    if (n4 == 100) {
                        throw new BatchUpdateException("batch entry " + i2 + ": query returns results", null, 0, lArray, null);
                    }
                    this.throwex(n4);
                }
                lArray[i2] = this.changes();
            }
        }
        finally {
            this.ensureAutoCommit(bl);
        }
        this.reset(l2);
        return lArray;
    }

    public final synchronized boolean execute(CoreStatement coreStatement, Object[] objectArray) throws SQLException {
        int n2 = coreStatement.pointer.safeRunInt((dB, l2) -> this.execute(l2, objectArray));
        switch (n2 & 0xFF) {
            case 101: {
                this.ensureAutoCommit(coreStatement.conn.getAutoCommit());
                return false;
            }
            case 100: {
                return true;
            }
            case 5: 
            case 6: 
            case 19: 
            case 21: {
                throw this.newSQLException(n2);
            }
        }
        coreStatement.pointer.close();
        throw this.newSQLException(n2);
    }

    private synchronized int execute(long l2, Object[] objectArray) throws SQLException {
        int n2;
        if (objectArray != null) {
            n2 = this.bind_parameter_count(l2);
            if (n2 > objectArray.length) {
                throw new SQLException("assertion failure: param count (" + n2 + ") > value count (" + objectArray.length + ")");
            }
            for (int i2 = 0; i2 < n2; ++i2) {
                int n3 = this.sqlbind(l2, i2, objectArray[i2]);
                if (n3 == 0) continue;
                this.throwex(n3);
            }
        }
        if (((n2 = this.step(l2)) & 0xFF) == 101) {
            this.reset(l2);
        }
        return n2;
    }

    final synchronized boolean execute(String string, boolean bl) throws SQLException {
        int n2 = this._exec(string);
        switch (n2) {
            case 0: {
                return false;
            }
            case 101: {
                this.ensureAutoCommit(bl);
                return false;
            }
            case 100: {
                return true;
            }
        }
        throw this.newSQLException(n2);
    }

    public final synchronized long executeUpdate(CoreStatement coreStatement, Object[] objectArray) throws SQLException {
        try {
            if (this.execute(coreStatement, objectArray)) {
                throw new SQLException("query returns results");
            }
        }
        finally {
            if (!coreStatement.pointer.isClosed()) {
                coreStatement.pointer.safeRunInt(DB::reset);
            }
        }
        return this.changes();
    }

    abstract void set_commit_listener(boolean var1);

    abstract void set_update_listener(boolean var1);

    public synchronized void addUpdateListener(SQLiteUpdateListener sQLiteUpdateListener) {
        if (this.updateListeners.add(sQLiteUpdateListener) && this.updateListeners.size() == 1) {
            this.set_update_listener(true);
        }
    }

    public synchronized void addCommitListener(SQLiteCommitListener sQLiteCommitListener) {
        if (this.commitListeners.add(sQLiteCommitListener) && this.commitListeners.size() == 1) {
            this.set_commit_listener(true);
        }
    }

    public synchronized void removeUpdateListener(SQLiteUpdateListener sQLiteUpdateListener) {
        if (this.updateListeners.remove(sQLiteUpdateListener) && this.updateListeners.isEmpty()) {
            this.set_update_listener(false);
        }
    }

    public synchronized void removeCommitListener(SQLiteCommitListener sQLiteCommitListener) {
        if (this.commitListeners.remove(sQLiteCommitListener) && this.commitListeners.isEmpty()) {
            this.set_commit_listener(false);
        }
    }

    void onUpdate(int n2, String string, String string2, long l2) {
        Object object;
        Object object2 = this;
        synchronized (object2) {
            object = new HashSet<SQLiteUpdateListener>(this.updateListeners);
        }
        object2 = object.iterator();
        while (object2.hasNext()) {
            SQLiteUpdateListener.Type type;
            object = (SQLiteUpdateListener)object2.next();
            switch (n2) {
                case 18: {
                    type = SQLiteUpdateListener.Type.INSERT;
                    break;
                }
                case 9: {
                    type = SQLiteUpdateListener.Type.DELETE;
                    break;
                }
                case 23: {
                    type = SQLiteUpdateListener.Type.UPDATE;
                    break;
                }
                default: {
                    throw new AssertionError((Object)("Unknown type: " + n2));
                }
            }
            object.onUpdate(type, string, string2, l2);
        }
    }

    void onCommit(boolean bl) {
        Object object;
        Object object2 = this;
        synchronized (object2) {
            object = new HashSet<SQLiteCommitListener>(this.commitListeners);
        }
        object2 = object.iterator();
        while (object2.hasNext()) {
            object = (SQLiteCommitListener)object2.next();
            if (bl) {
                object.onCommit();
                continue;
            }
            object.onRollback();
        }
    }

    final void throwex() throws SQLException {
        throw new SQLException(this.errmsg());
    }

    public final void throwex(int n2) throws SQLException {
        throw this.newSQLException(n2);
    }

    static void throwex(int n2, String string) throws SQLException {
        throw DB.newSQLException(n2, string);
    }

    public static SQLiteException newSQLException(int n2, String string) {
        SQLiteErrorCode sQLiteErrorCode = SQLiteErrorCode.getErrorCode(n2);
        String string2 = sQLiteErrorCode == SQLiteErrorCode.UNKNOWN_ERROR ? String.format("%s:%s (%s)", new Object[]{sQLiteErrorCode, n2, string}) : String.format("%s (%s)", new Object[]{sQLiteErrorCode, string});
        return new SQLiteException(string2, sQLiteErrorCode);
    }

    private SQLiteException newSQLException(int n2) throws SQLException {
        return DB.newSQLException(n2, this.errmsg());
    }

    final void ensureAutoCommit(boolean bl) throws SQLException {
        if (!bl) {
            return;
        }
        this.ensureBeginAndCommit();
        this.begin.safeRunConsume((dB2, l2) -> this.commit.safeRunConsume((dB, l3) -> this.ensureAutocommit(l2, l3)));
    }

    private void ensureBeginAndCommit() throws SQLException {
        DB dB;
        if (this.begin == null) {
            dB = this;
            synchronized (dB) {
                if (this.begin == null) {
                    this.begin = this.prepare("begin;");
                }
            }
        }
        if (this.commit == null) {
            dB = this;
            synchronized (dB) {
                if (this.commit == null) {
                    this.commit = this.prepare("commit;");
                }
                return;
            }
        }
    }

    private void ensureAutocommit(long l2, long l3) throws SQLException {
        try {
            if (this.step(l2) != 101) {
                return;
            }
            int n2 = this.step(l3);
            if (n2 != 101) {
                this.reset(l3);
                this.throwex(n2);
            }
            return;
        }
        finally {
            this.reset(l2);
            this.reset(l3);
        }
    }

    public abstract byte[] serialize(String var1) throws SQLException;

    public abstract void deserialize(String var1, byte[] var2) throws SQLException;

    public static interface ProgressObserver {
        public void progress(int var1, int var2);
    }
}

