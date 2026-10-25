/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.jdbc4;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.util.Properties;
import org.sqlite.SQLiteConnection;
import org.sqlite.jdbc3.JDBC3Connection;
import org.sqlite.jdbc4.JDBC4PreparedStatement;
import org.sqlite.jdbc4.JDBC4Statement;

public class JDBC4Connection
extends JDBC3Connection {
    public JDBC4Connection(String string, String string2, Properties properties) throws SQLException {
        super(string, string2, properties);
    }

    @Override
    public Statement createStatement(int n2, int n3, int n4) throws SQLException {
        this.checkOpen();
        this.checkCursor(n2, n3, n4);
        return new JDBC4Statement(this);
    }

    @Override
    public PreparedStatement prepareStatement(String string, int n2, int n3, int n4) throws SQLException {
        this.checkOpen();
        this.checkCursor(n2, n3, n4);
        return new JDBC4PreparedStatement(this, string);
    }

    @Override
    public boolean isClosed() throws SQLException {
        return super.isClosed();
    }

    @Override
    public <T> T unwrap(Class<T> clazz) throws ClassCastException {
        return clazz.cast(this);
    }

    @Override
    public boolean isWrapperFor(Class<?> clazz) {
        return clazz.isInstance(this);
    }

    @Override
    public Clob createClob() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public Blob createBlob() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public NClob createNClob() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public boolean isValid(int n2) throws SQLException {
        if (((SQLiteConnection)this).isClosed()) {
            return false;
        }
        try (Statement statement = this.createStatement();){
            boolean bl = statement.execute("select 1");
            return bl;
        }
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
}

