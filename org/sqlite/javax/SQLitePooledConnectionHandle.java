/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.javax;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import org.sqlite.SQLiteConnection;
import org.sqlite.core.DB;
import org.sqlite.javax.SQLitePooledConnection;
import org.sqlite.jdbc4.JDBC4PreparedStatement;
import org.sqlite.jdbc4.JDBC4Statement;

class SQLitePooledConnectionHandle
extends SQLiteConnection {
    private final SQLitePooledConnection parent;
    private final AtomicBoolean isClosed = new AtomicBoolean(false);

    public SQLitePooledConnectionHandle(SQLitePooledConnection sQLitePooledConnection) {
        super(sQLitePooledConnection.getPhysicalConn().getDatabase());
        this.parent = sQLitePooledConnection;
    }

    @Override
    public Statement createStatement() throws SQLException {
        return new JDBC4Statement(this);
    }

    @Override
    public PreparedStatement prepareStatement(String string) throws SQLException {
        return new JDBC4PreparedStatement(this, string);
    }

    @Override
    public CallableStatement prepareCall(String string) throws SQLException {
        return null;
    }

    @Override
    public String nativeSQL(String string) throws SQLException {
        return null;
    }

    @Override
    public void setAutoCommit(boolean bl) throws SQLException {
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return false;
    }

    @Override
    public void commit() throws SQLException {
    }

    @Override
    public void rollback() throws SQLException {
    }

    @Override
    public void close() throws SQLException {
        ConnectionEvent connectionEvent = new ConnectionEvent(this.parent);
        List<ConnectionEventListener> list2 = this.parent.getListeners();
        for (int i2 = list2.size() - 1; i2 >= 0; --i2) {
            list2.get(i2).connectionClosed(connectionEvent);
        }
        if (!this.parent.getPhysicalConn().getAutoCommit()) {
            this.parent.getPhysicalConn().rollback();
        }
        this.parent.getPhysicalConn().setAutoCommit(true);
        this.isClosed.set(true);
    }

    @Override
    public boolean isClosed() {
        return this.isClosed.get();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return null;
    }

    @Override
    public void setReadOnly(boolean bl) throws SQLException {
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return false;
    }

    @Override
    public void setCatalog(String string) throws SQLException {
    }

    @Override
    public String getCatalog() throws SQLException {
        return null;
    }

    @Override
    public void setTransactionIsolation(int n2) throws SQLException {
    }

    @Override
    public int getTransactionIsolation() {
        return 0;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
    }

    @Override
    public Statement createStatement(int n2, int n3) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String string, int n2, int n3) throws SQLException {
        return null;
    }

    @Override
    public CallableStatement prepareCall(String string, int n2, int n3) throws SQLException {
        return null;
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return null;
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
    }

    @Override
    public void setHoldability(int n2) throws SQLException {
    }

    @Override
    public int getHoldability() throws SQLException {
        return 0;
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return null;
    }

    @Override
    public Savepoint setSavepoint(String string) throws SQLException {
        return null;
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    }

    @Override
    public Statement createStatement(int n2, int n3, int n4) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String string, int n2, int n3, int n4) throws SQLException {
        return null;
    }

    @Override
    public CallableStatement prepareCall(String string, int n2, int n3, int n4) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String string, int n2) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String string, int[] nArray) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String string, String[] stringArray) throws SQLException {
        return null;
    }

    @Override
    public Clob createClob() throws SQLException {
        return null;
    }

    @Override
    public Blob createBlob() throws SQLException {
        return null;
    }

    @Override
    public NClob createNClob() throws SQLException {
        return null;
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return null;
    }

    @Override
    public boolean isValid(int n2) throws SQLException {
        return false;
    }

    @Override
    public void setClientInfo(String string, String string2) throws SQLClientInfoException {
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
    }

    @Override
    public String getClientInfo(String string) throws SQLException {
        return null;
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return null;
    }

    @Override
    public Array createArrayOf(String string, Object[] objectArray) throws SQLException {
        return null;
    }

    @Override
    public Struct createStruct(String string, Object[] objectArray) throws SQLException {
        return null;
    }

    @Override
    public void setSchema(String string) throws SQLException {
    }

    @Override
    public String getSchema() throws SQLException {
        return null;
    }

    @Override
    public void abort(Executor executor) throws SQLException {
    }

    @Override
    public void setNetworkTimeout(Executor executor, int n2) throws SQLException {
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return 0;
    }

    @Override
    public <T> T unwrap(Class<T> clazz) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> clazz) throws SQLException {
        return false;
    }

    @Override
    public int getBusyTimeout() {
        return 0;
    }

    @Override
    public void setBusyTimeout(int n2) {
    }

    @Override
    public DB getDatabase() {
        return null;
    }
}

