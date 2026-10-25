/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.jdbc3;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConnection;
import org.sqlite.SQLiteOpenMode;
import org.sqlite.jdbc3.JDBC3Savepoint;

public abstract class JDBC3Connection
extends SQLiteConnection {
    private final AtomicInteger savePoint = new AtomicInteger(0);
    private Map<String, Class<?>> typeMap;
    private boolean readOnly = false;

    public JDBC3Connection(String string, String string2, Properties properties) throws SQLException {
        super(string, string2, properties);
    }

    public void tryEnforceTransactionMode() throws SQLException {
        if (this.getDatabase().getConfig().isExplicitReadOnly() && !this.getAutoCommit() && this.getCurrentTransactionMode() != null) {
            if (this.isReadOnly()) {
                this.getDatabase()._exec("PRAGMA query_only = true;");
                return;
            }
            if (this.getCurrentTransactionMode() == SQLiteConfig.TransactionMode.DEFERRED) {
                if (this.isFirstStatementExecuted()) {
                    throw new SQLException("A statement has already been executed on this connection; cannot upgrade to write transaction");
                }
                this.getDatabase()._exec("commit; /* need to explicitly upgrade transaction */");
                this.getDatabase()._exec("PRAGMA query_only = false;");
                this.getDatabase()._exec("BEGIN IMMEDIATE; /* explicitly upgrade transaction */");
                this.setCurrentTransactionMode(SQLiteConfig.TransactionMode.IMMEDIATE);
            }
        }
    }

    @Override
    public String getCatalog() throws SQLException {
        this.checkOpen();
        return null;
    }

    @Override
    public void setCatalog(String string) throws SQLException {
        this.checkOpen();
    }

    @Override
    public int getHoldability() throws SQLException {
        this.checkOpen();
        return 2;
    }

    @Override
    public void setHoldability(int n2) throws SQLException {
        this.checkOpen();
        if (n2 != 2) {
            throw new SQLException("SQLite only supports CLOSE_CURSORS_AT_COMMIT");
        }
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        JDBC3Connection jDBC3Connection = this;
        synchronized (jDBC3Connection) {
            if (this.typeMap == null) {
                this.typeMap = new HashMap();
            }
            return this.typeMap;
        }
    }

    public void setTypeMap(Map map) throws SQLException {
        JDBC3Connection jDBC3Connection = this;
        synchronized (jDBC3Connection) {
            this.typeMap = map;
            return;
        }
    }

    @Override
    public boolean isReadOnly() {
        SQLiteConfig sQLiteConfig = this.getDatabase().getConfig();
        return (sQLiteConfig.getOpenModeFlags() & SQLiteOpenMode.READONLY.flag) != 0 || sQLiteConfig.isExplicitReadOnly() && this.readOnly;
    }

    @Override
    public void setReadOnly(boolean bl) throws SQLException {
        if (this.getDatabase().getConfig().isExplicitReadOnly()) {
            if (bl != this.readOnly && this.isFirstStatementExecuted()) {
                throw new SQLException("Cannot change Read-Only status of this connection: the first statement was already executed and the transaction is open.");
            }
        } else if (bl != this.isReadOnly()) {
            throw new SQLException("Cannot change read-only flag after establishing a connection. Use SQLiteConfig#setReadOnly and SQLiteConfig.createConnection().");
        }
        this.readOnly = bl;
    }

    @Override
    public String nativeSQL(String string) {
        return string;
    }

    @Override
    public void clearWarnings() throws SQLException {
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public Statement createStatement() throws SQLException {
        return this.createStatement(1003, 1007, 2);
    }

    @Override
    public Statement createStatement(int n2, int n3) throws SQLException {
        return this.createStatement(n2, n3, 2);
    }

    @Override
    public abstract Statement createStatement(int var1, int var2, int var3) throws SQLException;

    @Override
    public CallableStatement prepareCall(String string) throws SQLException {
        return this.prepareCall(string, 1003, 1007, 2);
    }

    @Override
    public CallableStatement prepareCall(String string, int n2, int n3) throws SQLException {
        return this.prepareCall(string, n2, n3, 2);
    }

    @Override
    public CallableStatement prepareCall(String string, int n2, int n3, int n4) throws SQLException {
        throw new SQLException("SQLite does not support Stored Procedures");
    }

    @Override
    public PreparedStatement prepareStatement(String string) throws SQLException {
        return this.prepareStatement(string, 1003, 1007);
    }

    @Override
    public PreparedStatement prepareStatement(String string, int n2) throws SQLException {
        return this.prepareStatement(string);
    }

    @Override
    public PreparedStatement prepareStatement(String string, int[] nArray) throws SQLException {
        return this.prepareStatement(string);
    }

    @Override
    public PreparedStatement prepareStatement(String string, String[] stringArray) throws SQLException {
        return this.prepareStatement(string);
    }

    @Override
    public PreparedStatement prepareStatement(String string, int n2, int n3) throws SQLException {
        return this.prepareStatement(string, n2, n3, 2);
    }

    @Override
    public abstract PreparedStatement prepareStatement(String var1, int var2, int var3, int var4) throws SQLException;

    @Override
    public Savepoint setSavepoint() throws SQLException {
        this.checkOpen();
        if (this.getAutoCommit()) {
            this.getConnectionConfig().setAutoCommit(false);
        }
        JDBC3Savepoint jDBC3Savepoint = new JDBC3Savepoint(this.savePoint.incrementAndGet());
        this.getDatabase().exec(String.format("SAVEPOINT %s", jDBC3Savepoint.getSavepointName()), false);
        return jDBC3Savepoint;
    }

    @Override
    public Savepoint setSavepoint(String object) throws SQLException {
        this.checkOpen();
        if (this.getAutoCommit()) {
            this.getConnectionConfig().setAutoCommit(false);
        }
        object = new JDBC3Savepoint(this.savePoint.incrementAndGet(), (String)object);
        this.getDatabase().exec(String.format("SAVEPOINT %s", object.getSavepointName()), false);
        return object;
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        this.checkOpen();
        if (this.getAutoCommit()) {
            throw new SQLException("database in auto-commit mode");
        }
        this.getDatabase().exec(String.format("RELEASE SAVEPOINT %s", savepoint.getSavepointName()), false);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        this.checkOpen();
        if (this.getAutoCommit()) {
            throw new SQLException("database in auto-commit mode");
        }
        this.getDatabase().exec(String.format("ROLLBACK TO SAVEPOINT %s", savepoint.getSavepointName()), this.getAutoCommit());
    }

    @Override
    public Struct createStruct(String string, Object[] objectArray) throws SQLException {
        throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
    }
}

