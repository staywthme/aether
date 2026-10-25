/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.core;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import org.sqlite.SQLiteConnection;
import org.sqlite.SQLiteConnectionConfig;
import org.sqlite.core.DB;
import org.sqlite.date.FastDateFormat;
import org.sqlite.jdbc3.JDBC3Connection;
import org.sqlite.jdbc3.JDBC3Statement;
import org.sqlite.jdbc4.JDBC4Statement;

public abstract class CorePreparedStatement
extends JDBC4Statement {
    public int columnCount;
    public int paramCount;
    public int batchQueryCount;

    public CorePreparedStatement(SQLiteConnection object, String string) throws SQLException {
        super((SQLiteConnection)object);
        this.sql = string;
        object = ((SQLiteConnection)object).getDatabase();
        ((DB)object).prepare(this);
        this.rs.colsMeta = this.pointer.safeRun(DB::column_names);
        this.columnCount = this.pointer.safeRunInt(DB::column_count);
        this.paramCount = this.pointer.safeRunInt(DB::bind_parameter_count);
        this.batchQueryCount = 0;
        this.batch = null;
        this.batchPos = 0;
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return Arrays.stream(((JDBC3Statement)this).executeLargeBatch()).mapToInt(l2 -> (int)l2).toArray();
    }

    @Override
    public long[] executeLargeBatch() throws SQLException {
        if (this.batchQueryCount == 0) {
            return new long[0];
        }
        if (this.conn instanceof JDBC3Connection) {
            ((JDBC3Connection)this.conn).tryEnforceTransactionMode();
        }
        CorePreparedStatement corePreparedStatement = this;
        return corePreparedStatement.withConnectionTimeout(() -> {
            try {
                long[] lArray = this.conn.getDatabase().executeBatch(this.pointer, this.batchQueryCount, this.batch, this.conn.getAutoCommit());
                return lArray;
            }
            finally {
                ((JDBC3Statement)this).clearBatch();
            }
        });
    }

    @Override
    public void clearBatch() throws SQLException {
        super.clearBatch();
        this.batchQueryCount = 0;
    }

    protected void batch(int n2, Object object) throws SQLException {
        this.checkOpen();
        if (this.batch == null) {
            this.batch = new Object[this.paramCount];
        }
        this.batch[this.batchPos + n2 - 1] = object;
    }

    protected void setDateByMilliseconds(int n2, Long l2, Calendar calendar) throws SQLException {
        SQLiteConnectionConfig sQLiteConnectionConfig = this.conn.getConnectionConfig();
        switch (sQLiteConnectionConfig.getDateClass()) {
            case TEXT: {
                this.batch(n2, FastDateFormat.getInstance(sQLiteConnectionConfig.getDateStringFormat(), calendar.getTimeZone()).format(new Date(l2)));
                return;
            }
            case REAL: {
                this.batch(n2, new Double((double)l2.longValue() / 8.64E7 + 2440587.5));
                return;
            }
        }
        this.batch(n2, new Long(l2 / sQLiteConnectionConfig.getDateMultiplier()));
    }
}

