/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite;

import java.sql.Connection;
import java.sql.SQLException;
import org.sqlite.SQLiteConnection;

public abstract class BusyHandler {
    private static void commitHandler(Connection connection, BusyHandler busyHandler) throws SQLException {
        if (!(connection instanceof SQLiteConnection)) {
            throw new SQLException("connection must be to an SQLite db");
        }
        if (connection.isClosed()) {
            throw new SQLException("connection closed");
        }
        connection = (SQLiteConnection)connection;
        ((SQLiteConnection)connection).getDatabase().busy_handler(busyHandler);
    }

    public static final void setHandler(Connection connection, BusyHandler busyHandler) throws SQLException {
        BusyHandler.commitHandler(connection, busyHandler);
    }

    public static final void clearHandler(Connection connection) throws SQLException {
        BusyHandler.commitHandler(connection, null);
    }

    protected abstract int callback(int var1) throws SQLException;
}

