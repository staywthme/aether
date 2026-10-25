/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.jdbc4;

import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import org.sqlite.SQLiteConnection;
import org.sqlite.jdbc3.JDBC3DatabaseMetaData;

public class JDBC4DatabaseMetaData
extends JDBC3DatabaseMetaData {
    public JDBC4DatabaseMetaData(SQLiteConnection sQLiteConnection) {
        super(sQLiteConnection);
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
    public RowIdLifetime getRowIdLifetime() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public ResultSet getSchemas(String string, String string2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public ResultSet getClientInfoProperties() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public ResultSet getFunctions(String string, String string2, String string3) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public ResultSet getPseudoColumns(String string, String string2, String string3, String string4) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public boolean generatedKeyAlwaysReturned() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
}

