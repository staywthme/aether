/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.javax;

import java.sql.SQLException;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import org.sqlite.javax.SQLitePooledConnection;

public class SQLiteConnectionPoolDataSource
extends SQLiteDataSource
implements ConnectionPoolDataSource {
    public SQLiteConnectionPoolDataSource() {
    }

    public SQLiteConnectionPoolDataSource(SQLiteConfig sQLiteConfig) {
        super(sQLiteConfig);
    }

    @Override
    public PooledConnection getPooledConnection() throws SQLException {
        return this.getPooledConnection(null, null);
    }

    @Override
    public PooledConnection getPooledConnection(String string, String string2) throws SQLException {
        return new SQLitePooledConnection(this.getConnection(string, string2));
    }
}

