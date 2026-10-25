/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.jdbc3;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.ExtendedCommand;
import org.sqlite.SQLiteConnection;
import org.sqlite.core.CoreStatement;
import org.sqlite.core.DB;

public abstract class JDBC3Statement
extends CoreStatement {
    private int queryTimeout = 0;
    protected long updateCount;
    protected boolean exhaustedResults = false;

    public JDBC3Statement(SQLiteConnection sQLiteConnection) {
        super(sQLiteConnection);
    }

    public void close() throws SQLException {
        this.clearGeneratedKeys();
        this.internalClose();
    }

    public boolean execute(String string) throws SQLException {
        this.internalClose();
        JDBC3Statement jDBC3Statement = this;
        return jDBC3Statement.withConnectionTimeout(() -> {
            ExtendedCommand.SQLExtension sQLExtension = ExtendedCommand.parse(string);
            if (sQLExtension != null) {
                sQLExtension.execute(this.conn.getDatabase());
                return Boolean.FALSE;
            }
            this.sql = string;
            string = this.conn;
            synchronized (string) {
                this.conn.getDatabase().prepare(this);
                boolean bl = this.exec();
                this.updateGeneratedKeys();
                this.updateCount = this.getDatabase().changes();
                this.exhaustedResults = false;
                return bl;
            }
        });
    }

    public boolean execute(String string, int n2) throws SQLException {
        return this.execute(string);
    }

    @Override
    public ResultSet executeQuery(String string, boolean bl) throws SQLException {
        this.rs.closeStmt = bl;
        return this.executeQuery(string);
    }

    public ResultSet executeQuery(String string) throws SQLException {
        this.internalClose();
        this.sql = string;
        JDBC3Statement jDBC3Statement = this;
        return jDBC3Statement.withConnectionTimeout(() -> {
            this.conn.getDatabase().prepare(this);
            if (!this.exec()) {
                this.internalClose();
                throw new SQLException("query does not return ResultSet", "SQLITE_DONE", 101);
            }
            this.exhaustedResults = false;
            return this.getResultSet();
        });
    }

    public int executeUpdate(String string) throws SQLException {
        return (int)this.executeLargeUpdate(string);
    }

    public int executeUpdate(String string, int n2) throws SQLException {
        return this.executeUpdate(string);
    }

    public long executeLargeUpdate(String string) throws SQLException {
        this.internalClose();
        this.sql = string;
        JDBC3Statement jDBC3Statement = this;
        return jDBC3Statement.withConnectionTimeout(() -> {
            DB dB = this.conn.getDatabase();
            long l2 = 0L;
            Object object = ExtendedCommand.parse(string);
            if (object != null) {
                object.execute(dB);
            } else {
                try {
                    object = dB;
                    synchronized (object) {
                        l2 = dB.total_changes();
                        int n2 = dB._exec(string);
                        if (n2 != 0) {
                            throw DB.newSQLException(n2, "");
                        }
                        this.updateGeneratedKeys();
                        l2 = dB.total_changes() - l2;
                    }
                }
                finally {
                    this.internalClose();
                }
            }
            return l2;
        });
    }

    public long executeLargeUpdate(String string, int n2) throws SQLException {
        return this.executeLargeUpdate(string);
    }

    public ResultSet getResultSet() throws SQLException {
        this.checkOpen();
        if (this.exhaustedResults) {
            return null;
        }
        if (this.rs.isOpen()) {
            throw new SQLException("ResultSet already requested");
        }
        if (this.pointer.safeRunInt(DB::column_count) == 0) {
            return null;
        }
        if (this.rs.colsMeta == null) {
            this.rs.colsMeta = this.pointer.safeRun(DB::column_names);
        }
        this.rs.cols = this.rs.colsMeta;
        this.rs.emptyResultSet = !this.resultsWaiting;
        this.rs.open = true;
        this.resultsWaiting = false;
        return (ResultSet)((Object)this.rs);
    }

    public int getUpdateCount() throws SQLException {
        return (int)this.getLargeUpdateCount();
    }

    public long getLargeUpdateCount() throws SQLException {
        this.conn.getDatabase();
        if (!(this.pointer.isClosed() || this.rs.isOpen() || this.resultsWaiting || this.pointer.safeRunInt(DB::column_count) != 0)) {
            return this.updateCount;
        }
        return -1L;
    }

    public void addBatch(String string) throws SQLException {
        this.internalClose();
        if (this.batch == null || this.batchPos + 1 >= this.batch.length) {
            Object[] objectArray = new Object[Math.max(10, this.batchPos << 1)];
            if (this.batch != null) {
                System.arraycopy(this.batch, 0, objectArray, 0, this.batch.length);
            }
            this.batch = objectArray;
        }
        this.batch[this.batchPos++] = string;
    }

    public void clearBatch() throws SQLException {
        this.batchPos = 0;
        if (this.batch != null) {
            for (int i2 = 0; i2 < this.batch.length; ++i2) {
                this.batch[i2] = null;
            }
        }
    }

    public int[] executeBatch() throws SQLException {
        return Arrays.stream(this.executeLargeBatch()).mapToInt(l2 -> (int)l2).toArray();
    }

    public long[] executeLargeBatch() throws SQLException {
        DB dB;
        this.internalClose();
        if (this.batch == null || this.batchPos == 0) {
            return new long[0];
        }
        long[] lArray = new long[this.batchPos];
        DB dB2 = dB = this.conn.getDatabase();
        synchronized (dB) {
            try {
                for (int i2 = 0; i2 < lArray.length; ++i2) {
                    try {
                        this.sql = (String)this.batch[i2];
                        dB.prepare(this);
                        lArray[i2] = dB.executeUpdate(this, null);
                        continue;
                    }
                    catch (SQLException sQLException) {
                        throw new BatchUpdateException("batch entry " + i2 + ": " + sQLException.getMessage(), null, 0, lArray, (Throwable)sQLException);
                    }
                    finally {
                        if (this.pointer != null) {
                            this.pointer.close();
                        }
                    }
                }
            }
            finally {
                this.clearBatch();
            }
            // ** MonitorExit[var3_7] (shouldn't be in output)
            return lArray;
        }
    }

    public void setCursorName(String string) {
    }

    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    public void clearWarnings() throws SQLException {
    }

    public Connection getConnection() throws SQLException {
        return this.conn;
    }

    public void cancel() throws SQLException {
        this.conn.getDatabase().interrupt();
    }

    public int getQueryTimeout() throws SQLException {
        return this.queryTimeout;
    }

    public void setQueryTimeout(int n2) throws SQLException {
        if (n2 < 0) {
            throw new SQLException("query timeout must be >= 0");
        }
        this.queryTimeout = n2;
    }

    public int getMaxRows() throws SQLException {
        return (int)this.rs.maxRows;
    }

    public long getLargeMaxRows() throws SQLException {
        return this.rs.maxRows;
    }

    public void setMaxRows(int n2) throws SQLException {
        this.setLargeMaxRows(n2);
    }

    public void setLargeMaxRows(long l2) throws SQLException {
        if (l2 < 0L) {
            throw new SQLException("max row count must be >= 0");
        }
        this.rs.maxRows = l2;
    }

    public int getMaxFieldSize() throws SQLException {
        return 0;
    }

    public void setMaxFieldSize(int n2) throws SQLException {
        if (n2 < 0) {
            throw new SQLException("max field size " + n2 + " cannot be negative");
        }
    }

    public int getFetchSize() throws SQLException {
        return ((ResultSet)((Object)this.rs)).getFetchSize();
    }

    public void setFetchSize(int n2) throws SQLException {
        ((ResultSet)((Object)this.rs)).setFetchSize(n2);
    }

    public int getFetchDirection() throws SQLException {
        return 1000;
    }

    public void setFetchDirection(int n2) throws SQLException {
        switch (n2) {
            case 1000: 
            case 1001: 
            case 1002: {
                return;
            }
        }
        throw new SQLException("Unknown fetch direction " + n2 + ". Must be one of FETCH_FORWARD, FETCH_REVERSE, or FETCH_UNKNOWN in java.sql.ResultSet");
    }

    public boolean getMoreResults() throws SQLException {
        return this.getMoreResults(1);
    }

    public boolean getMoreResults(int n2) throws SQLException {
        this.checkOpen();
        if (n2 == 2 || n2 == 3) {
            throw new SQLFeatureNotSupportedException("Argument not supported: Statement.KEEP_CURRENT_RESULT or Statement.CLOSE_ALL_RESULTS");
        }
        if (n2 != 1) {
            throw new SQLException("Invalid argument");
        }
        this.rs.close();
        this.updateCount = -1L;
        this.exhaustedResults = true;
        return false;
    }

    public int getResultSetConcurrency() throws SQLException {
        return 1007;
    }

    public int getResultSetHoldability() throws SQLException {
        return 2;
    }

    public int getResultSetType() throws SQLException {
        return 1003;
    }

    public void setEscapeProcessing(boolean bl) {
    }

    protected SQLException unsupported() {
        return new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
    }

    public boolean execute(String string, int[] nArray) throws SQLException {
        throw this.unsupported();
    }

    public boolean execute(String string, String[] stringArray) throws SQLException {
        throw this.unsupported();
    }

    public int executeUpdate(String string, int[] nArray) throws SQLException {
        throw this.unsupported();
    }

    public int executeUpdate(String string, String[] stringArray) throws SQLException {
        throw this.unsupported();
    }

    public long executeLargeUpdate(String string, int[] nArray) throws SQLException {
        throw this.unsupported();
    }

    public long executeLargeUpdate(String string, String[] stringArray) throws SQLException {
        throw this.unsupported();
    }

    protected <T> T withConnectionTimeout(SQLCallable<T> sQLCallable) throws SQLException {
        int n2 = this.conn.getBusyTimeout();
        if (this.queryTimeout > 0) {
            this.conn.setBusyTimeout(1000 * this.queryTimeout);
        }
        try {
            sQLCallable = sQLCallable.call();
            return (T)sQLCallable;
        }
        finally {
            if (this.queryTimeout > 0) {
                this.conn.setBusyTimeout(n2);
            }
        }
    }

    @FunctionalInterface
    public static interface SQLCallable<T> {
        public T call() throws SQLException;
    }

    static class BackupObserver
    implements DB.ProgressObserver {
        private static final Logger logger = LoggerFactory.getLogger(BackupObserver.class);

        BackupObserver() {
        }

        @Override
        public void progress(int n2, int n3) {
            logger.info("remaining:{}, page count:{}", (Object)n2, (Object)n3);
        }
    }
}

