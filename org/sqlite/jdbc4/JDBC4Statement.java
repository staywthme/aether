/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.jdbc4;

import java.sql.SQLException;
import java.sql.Statement;
import org.sqlite.SQLiteConnection;
import org.sqlite.jdbc3.JDBC3Statement;

public class JDBC4Statement
extends JDBC3Statement
implements Statement {
    private boolean closed = false;
    boolean closeOnCompletion;

    public JDBC4Statement(SQLiteConnection sQLiteConnection) {
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
    public void close() throws SQLException {
        super.close();
        this.closed = true;
    }

    @Override
    public boolean isClosed() {
        return this.closed;
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        if (this.closed) {
            throw new SQLException("statement is closed");
        }
        this.closeOnCompletion = true;
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        if (this.closed) {
            throw new SQLException("statement is closed");
        }
        return this.closeOnCompletion;
    }

    @Override
    public void setPoolable(boolean bl) throws SQLException {
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return false;
    }
}

