/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite;

import java.sql.Connection;
import java.sql.SQLException;
import org.sqlite.SQLiteConnection;
import org.sqlite.core.DB;

public abstract class Function {
    public static final int FLAG_DETERMINISTIC = 2048;
    private SQLiteConnection conn;
    private DB db;
    long context = 0L;
    long value = 0L;
    int args = 0;

    public static void create(Connection connection, String string, Function function) throws SQLException {
        Function.create(connection, string, function, 0);
    }

    public static void create(Connection connection, String string, Function function, int n2) throws SQLException {
        Function.create(connection, string, function, -1, n2);
    }

    public static void create(Connection connection, String string, Function function, int n2, int n3) throws SQLException {
        if (!(connection instanceof SQLiteConnection)) {
            throw new SQLException("connection must be to an SQLite db");
        }
        if (connection.isClosed()) {
            throw new SQLException("connection closed");
        }
        function.conn = (SQLiteConnection)connection;
        function.db = function.conn.getDatabase();
        if (n2 < -1 || n2 > 127) {
            throw new SQLException("invalid args provided: " + n2);
        }
        if (function.db.create_function(string, function, n2, n3) != 0) {
            throw new SQLException("error creating function");
        }
    }

    public static void destroy(Connection connection, String string, int n2) throws SQLException {
        if (!(connection instanceof SQLiteConnection)) {
            throw new SQLException("connection must be to an SQLite db");
        }
        ((SQLiteConnection)connection).getDatabase().destroy_function(string);
    }

    public static void destroy(Connection connection, String string) throws SQLException {
        Function.destroy(connection, string, -1);
    }

    protected abstract void xFunc() throws SQLException;

    protected final synchronized int args() throws SQLException {
        this.checkContext();
        return this.args;
    }

    protected final synchronized void result(byte[] byArray) throws SQLException {
        this.checkContext();
        this.db.result_blob(this.context, byArray);
    }

    protected final synchronized void result(double d2) throws SQLException {
        this.checkContext();
        this.db.result_double(this.context, d2);
    }

    protected final synchronized void result(int n2) throws SQLException {
        this.checkContext();
        this.db.result_int(this.context, n2);
    }

    protected final synchronized void result(long l2) throws SQLException {
        this.checkContext();
        this.db.result_long(this.context, l2);
    }

    protected final synchronized void result() throws SQLException {
        this.checkContext();
        this.db.result_null(this.context);
    }

    protected final synchronized void result(String string) throws SQLException {
        this.checkContext();
        this.db.result_text(this.context, string);
    }

    protected final synchronized void error(String string) throws SQLException {
        this.checkContext();
        this.db.result_error(this.context, string);
    }

    protected final synchronized String value_text(int n2) throws SQLException {
        this.checkValue(n2);
        return this.db.value_text(this, n2);
    }

    protected final synchronized byte[] value_blob(int n2) throws SQLException {
        this.checkValue(n2);
        return this.db.value_blob(this, n2);
    }

    protected final synchronized double value_double(int n2) throws SQLException {
        this.checkValue(n2);
        return this.db.value_double(this, n2);
    }

    protected final synchronized int value_int(int n2) throws SQLException {
        this.checkValue(n2);
        return this.db.value_int(this, n2);
    }

    protected final synchronized long value_long(int n2) throws SQLException {
        this.checkValue(n2);
        return this.db.value_long(this, n2);
    }

    protected final synchronized int value_type(int n2) throws SQLException {
        this.checkValue(n2);
        return this.db.value_type(this, n2);
    }

    private void checkContext() throws SQLException {
        if (this.conn == null || this.conn.getDatabase() == null || this.context == 0L) {
            throw new SQLException("no context, not allowed to read value");
        }
    }

    private void checkValue(int n2) throws SQLException {
        if (this.conn == null || this.conn.getDatabase() == null || this.value == 0L) {
            throw new SQLException("not in value access state");
        }
        if (n2 >= this.args) {
            throw new SQLException("arg " + n2 + " out bounds [0," + this.args + ")");
        }
    }

    public static abstract class Window
    extends Aggregate {
        protected abstract void xInverse() throws SQLException;

        protected abstract void xValue() throws SQLException;
    }

    public static abstract class Aggregate
    extends Function
    implements Cloneable {
        @Override
        protected final void xFunc() {
        }

        protected abstract void xStep() throws SQLException;

        protected abstract void xFinal() throws SQLException;

        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }
}

