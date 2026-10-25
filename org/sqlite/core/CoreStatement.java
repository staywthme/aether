/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;
import org.sqlite.SQLiteConnection;
import org.sqlite.SQLiteConnectionConfig;
import org.sqlite.core.Codes;
import org.sqlite.core.CoreResultSet;
import org.sqlite.core.DB;
import org.sqlite.core.SafeStmtPtr;
import org.sqlite.jdbc3.JDBC3Connection;
import org.sqlite.jdbc4.JDBC4ResultSet;

public abstract class CoreStatement
implements Codes {
    public final SQLiteConnection conn;
    public final CoreResultSet rs;
    public SafeStmtPtr pointer;
    public String sql = null;
    public int batchPos;
    public Object[] batch = null;
    public boolean resultsWaiting = false;
    private Statement generatedKeysStat = null;
    private ResultSet generatedKeysRs = null;
    private final Pattern insertPattern = Pattern.compile("^(with\\s+.+\\(.+?\\))*\\s*(insert|replace)", 34);

    public CoreStatement(SQLiteConnection sQLiteConnection) {
        this.conn = sQLiteConnection;
        this.rs = new JDBC4ResultSet(this);
    }

    public DB getDatabase() {
        return this.conn.getDatabase();
    }

    public SQLiteConnectionConfig getConnectionConfig() {
        return this.conn.getConnectionConfig();
    }

    protected final void checkOpen() throws SQLException {
        if (this.pointer.isClosed()) {
            throw new SQLException("statement is not executing");
        }
    }

    boolean isOpen() throws SQLException {
        return !this.pointer.isClosed();
    }

    protected boolean exec() throws SQLException {
        if (this.sql == null) {
            throw new SQLException("SQLiteJDBC internal error: sql==null");
        }
        if (this.rs.isOpen()) {
            throw new SQLException("SQLite JDBC internal error: rs.isOpen() on exec.");
        }
        if (this.conn instanceof JDBC3Connection) {
            ((JDBC3Connection)this.conn).tryEnforceTransactionMode();
        }
        boolean bl = false;
        try {
            bl = this.conn.getDatabase().execute(this, null);
            this.notifyFirstStatementExecuted();
            this.resultsWaiting = bl;
        }
        catch (Throwable throwable) {
            this.notifyFirstStatementExecuted();
            this.resultsWaiting = bl;
            this.pointer.close();
            throw throwable;
        }
        return this.pointer.safeRunInt(DB::column_count) != 0;
    }

    protected boolean exec(String string) throws SQLException {
        if (string == null) {
            throw new SQLException("SQLiteJDBC internal error: sql==null");
        }
        if (this.rs.isOpen()) {
            throw new SQLException("SQLite JDBC internal error: rs.isOpen() on exec.");
        }
        if (this.conn instanceof JDBC3Connection) {
            ((JDBC3Connection)this.conn).tryEnforceTransactionMode();
        }
        boolean bl = false;
        try {
            bl = this.conn.getDatabase().execute(string, this.conn.getAutoCommit());
            this.notifyFirstStatementExecuted();
            this.resultsWaiting = bl;
        }
        catch (Throwable throwable) {
            this.notifyFirstStatementExecuted();
            this.resultsWaiting = bl;
            if (this.pointer != null) {
                this.pointer.close();
            }
            throw throwable;
        }
        return this.pointer.safeRunInt(DB::column_count) != 0;
    }

    protected void internalClose() throws SQLException {
        if (this.pointer != null && !this.pointer.isClosed()) {
            if (this.conn.isClosed()) {
                throw DB.newSQLException(1, "Connection is closed");
            }
            this.rs.close();
            this.batch = null;
            this.batchPos = 0;
            int n2 = this.pointer.close();
            if (n2 != 0 && n2 != 21) {
                this.conn.getDatabase().throwex(n2);
            }
        }
    }

    protected void notifyFirstStatementExecuted() {
        this.conn.setFirstStatementExecuted(true);
    }

    public abstract ResultSet executeQuery(String var1, boolean var2) throws SQLException;

    protected void checkIndex(int n2) throws SQLException {
        if (this.batch == null) {
            throw new SQLException("No parameter has been set yet");
        }
        if (n2 <= 0 || n2 > this.batch.length) {
            throw new SQLException("Parameter index is invalid");
        }
    }

    protected void clearGeneratedKeys() throws SQLException {
        if (this.generatedKeysRs != null && !this.generatedKeysRs.isClosed()) {
            this.generatedKeysRs.close();
        }
        this.generatedKeysRs = null;
        if (this.generatedKeysStat != null && !this.generatedKeysStat.isClosed()) {
            this.generatedKeysStat.close();
        }
        this.generatedKeysStat = null;
    }

    public void updateGeneratedKeys() throws SQLException {
        this.clearGeneratedKeys();
        if (this.sql != null && this.insertPattern.matcher(this.sql.trim().toLowerCase()).find()) {
            this.generatedKeysStat = this.conn.createStatement();
            this.generatedKeysRs = this.generatedKeysStat.executeQuery("SELECT last_insert_rowid();");
        }
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        if (this.generatedKeysRs == null) {
            this.generatedKeysStat = this.conn.createStatement();
            this.generatedKeysRs = this.generatedKeysStat.executeQuery("SELECT 1 WHERE 1 = 2;");
        }
        return this.generatedKeysRs;
    }
}

