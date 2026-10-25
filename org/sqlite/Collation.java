/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite;

import java.sql.Connection;
import java.sql.SQLException;
import org.sqlite.SQLiteConnection;
import org.sqlite.core.DB;

public abstract class Collation {
    private SQLiteConnection conn;
    private DB db;

    public static final void create(Connection connection, String string, Collation collation) throws SQLException {
        if (connection == null || !(connection instanceof SQLiteConnection)) {
            throw new SQLException("connection must be to an SQLite db");
        }
        if (connection.isClosed()) {
            throw new SQLException("connection closed");
        }
        collation.conn = (SQLiteConnection)connection;
        collation.db = collation.conn.getDatabase();
        if (collation.db.create_collation(string, collation) != 0) {
            throw new SQLException("error creating collation");
        }
    }

    public static final void destroy(Connection connection, String string) throws SQLException {
        if (connection == null || !(connection instanceof SQLiteConnection)) {
            throw new SQLException("connection must be to an SQLite db");
        }
        ((SQLiteConnection)connection).getDatabase().destroy_collation(string);
    }

    protected abstract int xCompare(String var1, String var2);
}

