/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.jdbc4;

import java.io.InputStream;
import java.io.Reader;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLXML;
import java.util.Arrays;
import org.sqlite.SQLiteConnection;
import org.sqlite.jdbc3.JDBC3PreparedStatement;

public class JDBC4PreparedStatement
extends JDBC3PreparedStatement
implements ParameterMetaData,
PreparedStatement {
    public String toString() {
        return this.sql + " \n parameters=" + Arrays.toString(this.batch);
    }

    public JDBC4PreparedStatement(SQLiteConnection sQLiteConnection, String string) throws SQLException {
        super(sQLiteConnection, string);
    }

    @Override
    public void setRowId(int n2, RowId rowId) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setNString(int n2, String string) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setNCharacterStream(int n2, Reader reader, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setNClob(int n2, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setClob(int n2, Reader reader, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setBlob(int n2, InputStream inputStream2, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setNClob(int n2, Reader reader, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setSQLXML(int n2, SQLXML sQLXML) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setAsciiStream(int n2, InputStream inputStream2, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setBinaryStream(int n2, InputStream inputStream2, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setCharacterStream(int n2, Reader reader, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setAsciiStream(int n2, InputStream inputStream2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setBinaryStream(int n2, InputStream inputStream2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setCharacterStream(int n2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setNCharacterStream(int n2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setClob(int n2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setBlob(int n2, InputStream inputStream2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setNClob(int n2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
}

