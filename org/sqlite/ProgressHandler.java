/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite;

import java.sql.Connection;
import java.sql.SQLException;
import org.sqlite.SQLiteConnection;

public abstract class ProgressHandler {
    public static final void setHandler(Connection connection, int n2, ProgressHandler progressHandler) throws SQLException {
        if (!(connection instanceof SQLiteConnection)) {
            throw new SQLException("connection must be to an SQLite db");
        }
        if (connection.isClosed()) {
            throw new SQLException("connection closed");
        }
        connection = (SQLiteConnection)connection;
        ((SQLiteConnection)connection).getDatabase().register_progress_handler(n2, progressHandler);
    }

    public static final void clearHandler(Connection connection) throws SQLException {
        connection = (SQLiteConnection)connection;
        ((SQLiteConnection)connection).getDatabase().clear_progress_handler();
    }

    protected abstract int progress() throws SQLException;
}

